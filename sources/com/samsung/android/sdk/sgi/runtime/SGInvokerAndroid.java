package com.samsung.android.sdk.sgi.runtime;

import android.os.Handler;
import android.util.Log;

final class SGInvokerAndroid {
    private Handler handler = new Handler();
    private long nativeInstance;
    private Runnable runnable = new C04441();

    /* renamed from: com.samsung.android.sdk.sgi.runtime.SGInvokerAndroid$1 */
    class C04441 implements Runnable {
        C04441() {
        }

        public void run() {
            SGInvokerAndroid.this.nativeInvokeCallback(SGInvokerAndroid.this.nativeInstance);
        }
    }

    public SGInvokerAndroid(long aNativeInstance) {
        this.nativeInstance = aNativeInstance;
    }

    private native void nativeInvokeCallback(long j);

    public void forceInvokeImpl() {
        if (!this.handler.postAtFrontOfQueue(this.runnable)) {
            Log.e("SGI", "Invoker: Handler.postAtFrontOfQueue failed");
        }
    }

    public void invokeImpl() {
        if (!this.handler.post(this.runnable)) {
            Log.e("SGI", "Invoker: Handler.post failed");
        }
    }

    public void release() {
        this.handler.removeCallbacks(this.runnable);
    }
}
