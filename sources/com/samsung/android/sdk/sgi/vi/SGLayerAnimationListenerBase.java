package com.samsung.android.sdk.sgi.vi;

abstract class SGLayerAnimationListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGLayerAnimationListenerBase() {
        this(SGJNI.new_SGLayerAnimationListenerBase(), true);
        SGJNI.SGLayerAnimationListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGLayerAnimationListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGLayerAnimationListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGLayerAnimationListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onFinished(long j);

    public abstract void onStarted(long j);
}
