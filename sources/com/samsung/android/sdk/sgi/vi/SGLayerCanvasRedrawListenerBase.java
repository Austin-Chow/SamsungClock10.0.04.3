package com.samsung.android.sdk.sgi.vi;

import android.graphics.Canvas;

abstract class SGLayerCanvasRedrawListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGLayerCanvasRedrawListenerBase() {
        this(SGJNI.new_SGLayerCanvasRedrawListenerBase(), true);
        SGJNI.SGLayerCanvasRedrawListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGLayerCanvasRedrawListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGLayerCanvasRedrawListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGLayerCanvasRedrawListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onDraw(long j, Canvas canvas);
}
