package android.support.v4.provider;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SelfDestructiveThread {
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private Callback mCallback = new C01581();
    private final int mDestructAfterMillisec;
    private int mGeneration;
    private Handler mHandler;
    private final Object mLock = new Object();
    private final int mPriority;
    private HandlerThread mThread;
    private final String mThreadName;

    public interface ReplyCallback<T> {
        void onReply(T t);
    }

    /* renamed from: android.support.v4.provider.SelfDestructiveThread$1 */
    class C01581 implements Callback {
        C01581() {
        }

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SelfDestructiveThread.this.onDestruction();
                    break;
                case 1:
                    SelfDestructiveThread.this.onInvokeRunnable((Runnable) msg.obj);
                    break;
            }
            return true;
        }
    }

    public SelfDestructiveThread(String threadName, int priority, int destructAfterMillisec) {
        this.mThreadName = threadName;
        this.mPriority = priority;
        this.mDestructAfterMillisec = destructAfterMillisec;
        this.mGeneration = 0;
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mThread != null;
        }
        return z;
    }

    public int getGeneration() {
        int i;
        synchronized (this.mLock) {
            i = this.mGeneration;
        }
        return i;
    }

    private void post(Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                this.mThread = new HandlerThread(this.mThreadName, this.mPriority);
                this.mThread.start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                this.mGeneration++;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, runnable));
        }
    }

    public <T> void postAndReply(final Callable<T> callable, final ReplyCallback<T> reply) {
        final Handler callingHandler = new Handler();
        post(new Runnable() {
            public void run() {
                T t;
                try {
                    t = callable.call();
                } catch (Exception e) {
                    t = null;
                }
                callingHandler.post(new Runnable() {
                    public void run() {
                        reply.onReply(t);
                    }
                });
            }
        });
    }

    public <T> T postAndWait(Callable<T> callable, int timeoutMillis) throws InterruptedException {
        final ReentrantLock lock = new ReentrantLock();
        final Condition cond = lock.newCondition();
        final AtomicReference<T> holder = new AtomicReference();
        final AtomicBoolean running = new AtomicBoolean(true);
        final Callable<T> callable2 = callable;
        post(new Runnable() {
            public void run() {
                try {
                    holder.set(callable2.call());
                } catch (Exception e) {
                }
                lock.lock();
                try {
                    running.set(false);
                    cond.signal();
                } finally {
                    lock.unlock();
                }
            }
        });
        lock.lock();
        try {
            T t;
            if (running.get()) {
                long remaining = TimeUnit.MILLISECONDS.toNanos((long) timeoutMillis);
                while (true) {
                    try {
                        remaining = cond.awaitNanos(remaining);
                    } catch (InterruptedException e) {
                    }
                    if (!running.get()) {
                        break;
                    } else if (remaining <= 0) {
                        throw new InterruptedException("timeout");
                    }
                }
                t = holder.get();
                lock.unlock();
            } else {
                t = holder.get();
            }
            return t;
        } finally {
            lock.unlock();
        }
    }

    private void onInvokeRunnable(Runnable runnable) {
        runnable.run();
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), (long) this.mDestructAfterMillisec);
        }
    }

    private void onDestruction() {
        synchronized (this.mLock) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mThread.quit();
            this.mThread = null;
            this.mHandler = null;
        }
    }
}