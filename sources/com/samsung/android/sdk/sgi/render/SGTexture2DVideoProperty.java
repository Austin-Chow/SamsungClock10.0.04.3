package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;

class SGTexture2DVideoProperty extends SGTextureProperty {
    SGSurfaceRenderer mRendererListener;

    protected SGTexture2DVideoProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    protected SGTexture2DVideoProperty(String videoRectUniformName) {
        this(SGJNI.new_SGTexture2DVideoProperty(videoRectUniformName), true);
    }

    private void setUpdater(long surfaceRenderer) {
        SGJNI.SGTexture2DVideoProperty_setUpdater(this.swigCPtr, this, surfaceRenderer);
    }

    protected SGSurfaceRenderer getRenderer() {
        return this.mRendererListener == null ? null : this.mRendererListener;
    }

    protected void setUpdater(SGSurfaceRenderer renderer) {
        if (renderer == null) {
            throw new NullPointerException("SGTexture2DVideoProperty::setRenderer error: parameter renderer is null");
        }
        this.mRendererListener = renderer;
        setUpdater(SGSurfaceRendererBase.getCPtr(this.mRendererListener));
    }

    public void textureUpdated() {
        SGJNI.SGTexture2DVideoProperty_textureUpdated(this.swigCPtr, this);
    }

    public void updateTextureMatrix(SGMatrix4f value) {
        SGJNI.SGTexture2DVideoProperty_updateTextureMatrix(this.swigCPtr, this, value.getData());
    }
}
