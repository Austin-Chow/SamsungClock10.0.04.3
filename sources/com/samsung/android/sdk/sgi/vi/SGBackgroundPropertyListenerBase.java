package com.samsung.android.sdk.sgi.vi;

abstract class SGBackgroundPropertyListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGBackgroundPropertyListenerBase() {
        this(SGJNI.new_SGBackgroundPropertyListenerBase(), true);
        SGJNI.SGBackgroundPropertyListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGBackgroundPropertyListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGBackgroundPropertyListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGBackgroundPropertyListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onFinish(long j, int i);
}
