package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.Lifecycle.State;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class LiveData<T> {
    private static final Object NOT_SET = new Object();
    private int mActiveCount;
    private volatile Object mData;
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private SafeIterableMap<Observer<T>, ObserverWrapper> mObservers;
    private int mVersion;

    private abstract class ObserverWrapper {
        boolean mActive;
        int mLastVersion;
        final Observer<T> mObserver;
        final /* synthetic */ LiveData this$0;

        abstract boolean shouldBeActive();

        void detachObserver() {
        }

        void activeStateChanged(boolean newActive) {
            int i = 1;
            if (newActive != this.mActive) {
                this.mActive = newActive;
                boolean wasInactive = this.this$0.mActiveCount == 0;
                LiveData liveData = this.this$0;
                int access$300 = liveData.mActiveCount;
                if (!this.mActive) {
                    i = -1;
                }
                liveData.mActiveCount = i + access$300;
                if (wasInactive && this.mActive) {
                    this.this$0.onActive();
                }
                if (this.this$0.mActiveCount == 0 && !this.mActive) {
                    this.this$0.onInactive();
                }
                if (this.mActive) {
                    this.this$0.dispatchingValue(this);
                }
            }
        }
    }

    class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver {
        final LifecycleOwner mOwner;
        final /* synthetic */ LiveData this$0;

        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(State.STARTED);
        }

        public void onStateChanged(LifecycleOwner source, Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
                this.this$0.removeObserver(this.mObserver);
            } else {
                activeStateChanged(shouldBeActive());
            }
        }

        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }
    }

    private void considerNotify(ObserverWrapper observer) {
        if (!observer.mActive) {
            return;
        }
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
        } else if (observer.mLastVersion < this.mVersion) {
            observer.mLastVersion = this.mVersion;
            observer.mObserver.onChanged(this.mData);
        }
    }

    private void dispatchingValue(ObserverWrapper initiator) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        do {
            this.mDispatchInvalidated = false;
            if (initiator == null) {
                Iterator<Entry<Observer<T>, ObserverWrapper>> iterator = this.mObservers.iteratorWithAdditions();
                while (iterator.hasNext()) {
                    considerNotify((ObserverWrapper) ((Entry) iterator.next()).getValue());
                    if (this.mDispatchInvalidated) {
                        break;
                    }
                }
            }
            considerNotify(initiator);
            initiator = null;
        } while (this.mDispatchInvalidated);
        this.mDispatchingValue = false;
    }

    public void removeObserver(Observer<T> observer) {
        assertMainThread("removeObserver");
        ObserverWrapper removed = (ObserverWrapper) this.mObservers.remove(observer);
        if (removed != null) {
            removed.detachObserver();
            removed.activeStateChanged(false);
        }
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    private static void assertMainThread(String methodName) {
        if (!ArchTaskExecutor.getInstance().isMainThread()) {
            throw new IllegalStateException("Cannot invoke " + methodName + " on a background" + " thread");
        }
    }
}
