package com.samsung.android.sdk.sgi.vi;

abstract class SGScreenshotPropertyListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGScreenshotPropertyListenerBase() {
        this(SGJNI.new_SGScreenshotPropertyListenerBase(), true);
        SGJNI.SGScreenshotPropertyListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGScreenshotPropertyListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGScreenshotPropertyListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGScreenshotPropertyListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onCompleted(long j);
}
