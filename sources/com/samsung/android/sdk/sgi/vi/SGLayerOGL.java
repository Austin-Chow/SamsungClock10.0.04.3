package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.render.SGRenderInterface;

public class SGLayerOGL extends SGLayer {
    private SGRenderInterface mRenderInterface;

    public SGLayerOGL() {
        this(SGJNI.new_SGLayerOGL__SWIG_2(), true);
    }

    private SGLayerOGL(long renderer) {
        this(SGJNI.new_SGLayerOGL__SWIG_1(renderer), true);
    }

    private SGLayerOGL(long renderer, SGSurfaceFormat surfaceFormat) {
        this(SGJNI.new_SGLayerOGL__SWIG_0(renderer, SGJNI.getData(surfaceFormat)), true);
    }

    protected SGLayerOGL(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
        this.mRenderInterface = null;
    }

    public SGLayerOGL(SGRenderInterface renderer) {
        this(SGRenderInterface.getCPtr(renderer));
        this.mRenderInterface = renderer;
    }

    public SGLayerOGL(SGRenderInterface renderer, SGSurfaceFormat surfaceFormat) {
        this(SGRenderInterface.getCPtr(renderer), surfaceFormat);
        this.mRenderInterface = renderer;
    }

    private void setRenderer(long renderer) {
        SGJNI.SGLayerOGL_setRenderer(this.swigCPtr, this, renderer);
    }

    public SGRenderInterface getRenderer() {
        return this.mRenderInterface;
    }

    public void setRenderer(SGRenderInterface renderer) {
        setRenderer(SGRenderInterface.getCPtr(renderer));
        this.mRenderInterface = renderer;
    }
}
