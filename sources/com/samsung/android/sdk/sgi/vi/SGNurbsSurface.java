package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGVector3f;

public final class SGNurbsSurface {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGNurbsSurface(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGNurbsSurface(SGVector3f[][] controlPoints, float[][] weights, int curveOrderU, int curveOrderV, float[] knotsU, float[] knotsV) {
        this(SGJNI.new_SGNurbsSurface(controlPoints, weights, curveOrderU, curveOrderV, knotsU, knotsV), true);
    }

    public static long getCPtr(SGNurbsSurface obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGNurbsSurface_getHandle(this.swigCPtr, this);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGNurbsSurface(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }
}
