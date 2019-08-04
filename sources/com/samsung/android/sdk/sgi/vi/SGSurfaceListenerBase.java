package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGVector2f;

abstract class SGSurfaceListenerBase {
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    public SGSurfaceListenerBase() {
        this(SGJNI.new_SGSurfaceListenerBase(), true);
        SGJNI.SGSurfaceListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGSurfaceListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGSurfaceListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGSurfaceListenerBase(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public abstract void onFrameEnd();

    public abstract void onResize(SGVector2f sGVector2f);
}
