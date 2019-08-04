package com.samsung.android.sdk.sgi.render;

abstract class SGSurfaceRendererBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGSurfaceRendererBase() {
        this(SGJNI.new_SGSurfaceRendererBase(), true);
        SGJNI.SGSurfaceRendererBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGSurfaceRendererBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGSurfaceRendererBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGSurfaceRendererBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onCreateTexture(int i);

    public abstract void onDestroyTexture();

    public abstract void onDraw(int i);
}
