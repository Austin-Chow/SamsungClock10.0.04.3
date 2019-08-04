package com.samsung.android.sdk.sgi.vi;

abstract class SGBoneParamsChangeListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGBoneParamsChangeListenerBase() {
        this(SGJNI.new_SGBoneParamsChangeListenerBase(), true);
        SGJNI.SGBoneParamsChangeListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGBoneParamsChangeListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGBoneParamsChangeListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGBoneParamsChangeListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onBoneParamsChanged(long j);
}
