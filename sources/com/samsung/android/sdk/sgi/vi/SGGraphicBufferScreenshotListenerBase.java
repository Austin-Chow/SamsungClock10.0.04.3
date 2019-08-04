package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap;

abstract class SGGraphicBufferScreenshotListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGGraphicBufferScreenshotListenerBase() {
        this(SGJNI.new_SGGraphicBufferScreenshotListenerBase(), true);
        SGJNI.SGGraphicBufferScreenshotListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGGraphicBufferScreenshotListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGGraphicBufferScreenshotListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGGraphicBufferScreenshotListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onCompleted(Bitmap bitmap);
}
