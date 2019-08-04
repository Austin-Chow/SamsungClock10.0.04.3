package com.samsung.android.sdk.sgi.vi;

abstract class SGTraceRayListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGTraceRayListenerBase() {
        this(SGJNI.new_SGTraceRayListenerBase(), true);
        SGJNI.SGTraceRayListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGTraceRayListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGTraceRayListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGTraceRayListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onCompleted();

    public abstract boolean onLayer(long j);

    public abstract boolean onWidget(long j);
}
