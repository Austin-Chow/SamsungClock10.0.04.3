package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap.Config;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2f;

public class SGLayerCanvas extends SGLayer {
    private SGLayerCanvasRedrawListenerHolder mCanvasRedrawListener;

    public SGLayerCanvas() {
        this(SGJNI.new_SGLayerCanvas__SWIG_0(), true);
    }

    protected SGLayerCanvas(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
        this.mCanvasRedrawListener = new SGLayerCanvasRedrawListenerHolder(this);
    }

    public SGLayerCanvas(Config imageFormat) {
        this();
        setFormat(imageFormat);
    }

    public SGLayerCanvas(RectF rect) {
        this(SGJNI.new_SGLayerCanvas__SWIG_1(SGMathNative.getArrayRect(rect)), true);
    }

    private void setRedrawListener(SGLayerCanvasRedrawListenerBase listener) {
        SGJNI.SGLayerCanvas_setRedrawListener(this.swigCPtr, this, SGLayerCanvasRedrawListenerBase.getCPtr(listener), listener);
    }

    public SGVector2f getCanvasScale() {
        return new SGVector2f(SGJNI.SGLayerCanvas_getCanvasScale(this.swigCPtr, this));
    }

    public RectF getContentRect() {
        return new RectF(SGJNI.SGLayerCanvas_getContentRect(this.swigCPtr, this)[0], SGJNI.SGLayerCanvas_getContentRect(this.swigCPtr, this)[1], SGJNI.SGLayerCanvas_getContentRect(this.swigCPtr, this)[2], SGJNI.SGLayerCanvas_getContentRect(this.swigCPtr, this)[3]);
    }

    public SGVector2f getContentRectPivot() {
        return new SGVector2f(SGJNI.SGLayerCanvas_getContentRectPivot(this.swigCPtr, this));
    }

    public SGVector2f getContentRectScale() {
        return new SGVector2f(SGJNI.SGLayerCanvas_getContentRectScale(this.swigCPtr, this));
    }

    public Config getFormat() {
        Config config = Config.valueOf(SGJNI.SGLayerCanvas_getFormat(this.swigCPtr, this));
        if (config != null) {
            return config;
        }
        throw new NullPointerException("Unsupported android.graphics.Bitmap.Config format");
    }

    public SGLayerCanvasRedrawListener getRedrawListener() {
        return this.mCanvasRedrawListener.getInterface();
    }

    public void invalidate() {
        SGJNI.SGLayerCanvas_invalidate__SWIG_0(this.swigCPtr, this);
    }

    public void invalidate(RectF rect) {
        SGJNI.SGLayerCanvas_invalidate__SWIG_1(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public void setCanvasScale(SGVector2f scale) {
        SGJNI.SGLayerCanvas_setCanvasScale(this.swigCPtr, this, scale.getData());
    }

    public void setContentRect(RectF rect) {
        SGJNI.SGLayerCanvas_setContentRect(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public void setContentRectPivot(SGVector2f pivot) {
        SGJNI.SGLayerCanvas_setContentRectPivot(this.swigCPtr, this, pivot.getData());
    }

    public void setContentRectScale(SGVector2f scale) {
        SGJNI.SGLayerCanvas_setContentRectScale(this.swigCPtr, this, scale.getData());
    }

    public void setFormat(Config imageFormat) {
        SGJNI.SGLayerCanvas_setFormat(this.swigCPtr, this, imageFormat.name());
    }

    public void setRedrawListener(SGLayerCanvasRedrawListener listener) {
        this.mCanvasRedrawListener.setInterface(listener);
        setRedrawListener(listener == null ? null : this.mCanvasRedrawListener);
    }
}
