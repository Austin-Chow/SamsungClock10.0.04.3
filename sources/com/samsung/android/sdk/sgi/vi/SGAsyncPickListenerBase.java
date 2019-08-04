package com.samsung.android.sdk.sgi.vi;

abstract class SGAsyncPickListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGAsyncPickListenerBase() {
        this(SGJNI.new_SGAsyncPickListenerBase(), true);
        SGJNI.SGAsyncPickListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGAsyncPickListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGAsyncPickListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGAsyncPickListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onCompleted(long[] jArr);
}
