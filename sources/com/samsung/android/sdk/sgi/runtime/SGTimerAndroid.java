package com.samsung.android.sdk.sgi.runtime;

import android.os.Handler;
import android.util.Log;

final class SGTimerAndroid {
    private Handler handler = new Handler();
    private long nativeInstance;
    private Runnable runnable = new C04451();

    /* renamed from: com.samsung.android.sdk.sgi.runtime.SGTimerAndroid$1 */
    class C04451 implements Runnable {
        C04451() {
        }

        public void run() {
            SGTimerAndroid.this.nativeInvokeCallback(SGTimerAndroid.this.nativeInstance);
        }
    }

    public SGTimerAndroid(long aNativeInstance) {
        this.nativeInstance = aNativeInstance;
    }

    private native void nativeInvokeCallback(long j);

    public void forceInvokeImpl() {
        this.handler.removeCallbacks(this.runnable);
        if (!this.handler.postAtFrontOfQueue(this.runnable)) {
            Log.e("SGI", "Timer: Handler.postAtFrontOfQueue failed");
        }
    }

    public void invokeImpl(long aAfterMilliseconds) {
        this.handler.removeCallbacks(this.runnable);
        if (!(aAfterMilliseconds == 0 ? this.handler.post(this.runnable) : this.handler.postDelayed(this.runnable, aAfterMilliseconds))) {
            Log.e("SGI", "Timer: Handler.post failed");
        }
    }

    public void release() {
        this.handler.removeCallbacks(this.runnable);
    }
}
