package com.samsung.android.sdk.sgi.render;

public abstract class SGRenderInterface {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public SGRenderInterface() {
        this(SGJNI.new_SGRenderInterface(), true);
        SGJNI.SGRenderInterface_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGRenderInterface(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGRenderInterface obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void draw() {
        if (getClass() == SGRenderInterface.class) {
            SGJNI.SGRenderInterface_draw(this.swigCPtr, this);
        } else {
            SGJNI.SGRenderInterface_drawSwigExplicitSGRenderInterface(this.swigCPtr, this);
        }
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGRenderInterface(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public void init() {
        if (getClass() == SGRenderInterface.class) {
            SGJNI.SGRenderInterface_init(this.swigCPtr, this);
        } else {
            SGJNI.SGRenderInterface_initSwigExplicitSGRenderInterface(this.swigCPtr, this);
        }
    }

    public boolean needRedraw() {
        return getClass() == SGRenderInterface.class ? SGJNI.SGRenderInterface_needRedraw(this.swigCPtr, this) : SGJNI.SGRenderInterface_needRedrawSwigExplicitSGRenderInterface(this.swigCPtr, this);
    }

    public void release() {
        if (getClass() == SGRenderInterface.class) {
            SGJNI.SGRenderInterface_release(this.swigCPtr, this);
        } else {
            SGJNI.SGRenderInterface_releaseSwigExplicitSGRenderInterface(this.swigCPtr, this);
        }
    }
}
