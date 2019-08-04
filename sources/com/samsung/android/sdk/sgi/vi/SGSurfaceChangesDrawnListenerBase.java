package com.samsung.android.sdk.sgi.vi;

abstract class SGSurfaceChangesDrawnListenerBase {
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    public SGSurfaceChangesDrawnListenerBase() {
        this(SGJNI.new_SGSurfaceChangesDrawnListenerBase(), true);
        SGJNI.SGSurfaceChangesDrawnListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGSurfaceChangesDrawnListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGSurfaceChangesDrawnListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGSurfaceChangesDrawnListenerBase(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public abstract void onChangesDrawn();
}
