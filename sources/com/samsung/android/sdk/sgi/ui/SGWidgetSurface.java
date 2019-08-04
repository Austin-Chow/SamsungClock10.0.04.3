package com.samsung.android.sdk.sgi.ui;

import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGSurfaceRenderer;

class SGWidgetSurface extends SGWidget {
    private SGSurfaceRenderer mRendererListener;

    protected SGWidgetSurface(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGWidgetSurface(SGVector2f size, SGSurfaceRenderer renderer) {
        this(SGJNI.new_SGWidgetSurface(size.getData(), SGSurfaceRendererBase.getCPtr(renderer)), true);
        SGJNI.SGWidgetSurface_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
        this.mRendererListener = renderer;
    }

    public RectF getContentRect() {
        return new RectF(SGJNI.SGWidgetSurface_getContentRect(this.swigCPtr, this)[0], SGJNI.SGWidgetSurface_getContentRect(this.swigCPtr, this)[1], SGJNI.SGWidgetSurface_getContentRect(this.swigCPtr, this)[2], SGJNI.SGWidgetSurface_getContentRect(this.swigCPtr, this)[3]);
    }

    public SGVector2f getContentRectPivot() {
        return new SGVector2f(SGJNI.SGWidgetSurface_getContentRectPivot(this.swigCPtr, this));
    }

    public SGVector2f getContentRectScale() {
        return new SGVector2f(SGJNI.SGWidgetSurface_getContentRectScale(this.swigCPtr, this));
    }

    public SGSurfaceRenderer getRenderer() {
        return this.mRendererListener;
    }

    public void invalidate() {
        if (getClass() == SGWidgetSurface.class) {
            SGJNI.SGWidgetSurface_invalidate__SWIG_1(this.swigCPtr, this);
        } else {
            SGJNI.SGWidgetSurface_invalidateSwigExplicitSGWidgetSurface__SWIG_1(this.swigCPtr, this);
        }
    }

    public void invalidate(RectF rect) {
        if (getClass() == SGWidgetSurface.class) {
            SGJNI.SGWidgetSurface_invalidate__SWIG_0_0(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
        } else {
            SGJNI.SGWidgetSurface_invalidateSwigExplicitSGWidgetSurface__SWIG_0_0(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
        }
    }

    public void setContentRect(RectF rect) {
        SGJNI.SGWidgetSurface_setContentRect(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public void setContentRectPivot(SGVector2f pivot) {
        SGJNI.SGWidgetSurface_setContentRectPivot(this.swigCPtr, this, pivot.getData());
    }

    public void setContentRectScale(SGVector2f scale) {
        SGJNI.SGWidgetSurface_setContentRectScale(this.swigCPtr, this, scale.getData());
    }

    public void setRenderer(SGSurfaceRenderer renderer) {
        if (renderer == null) {
            throw new NullPointerException("SGWidgetSurface::setRenderer error: parameter renderer is null");
        }
        this.mRendererListener = renderer;
        SGJNI.SGWidgetSurface_setRenderer(this.swigCPtr, this, SGSurfaceRendererBase.getCPtr(renderer));
    }

    public void updateTextureMatrix(SGMatrix4f transform) {
        SGJNI.SGWidgetSurface_updateTextureMatrix(this.swigCPtr, this, transform.getData());
    }
}
