package com.samsung.android.sdk.sgi.ui;

import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2f;

public abstract class SGWidgetCanvas extends SGWidget {
    public SGWidgetCanvas(float width, float height) {
        this(new SGVector2f(width, height), Config.ARGB_8888);
    }

    public SGWidgetCanvas(float width, float height, Config imageFormat) {
        this(new SGVector2f(width, height), imageFormat);
    }

    protected SGWidgetCanvas(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGWidgetCanvas(SGVector2f size) {
        this(size, Config.ARGB_8888);
    }

    public SGWidgetCanvas(SGVector2f size, Config canvasFormat) {
        this(SGJNI.new_SGWidgetCanvas(size.getData(), canvasFormat.name()), true);
        SGJNI.SGWidgetCanvas_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public SGVector2f getCanvasScale() {
        return new SGVector2f(SGJNI.SGWidgetCanvas_getCanvasScale(this.swigCPtr, this));
    }

    public RectF getContentRect() {
        return new RectF(SGJNI.SGWidgetCanvas_getContentRect(this.swigCPtr, this)[0], SGJNI.SGWidgetCanvas_getContentRect(this.swigCPtr, this)[1], SGJNI.SGWidgetCanvas_getContentRect(this.swigCPtr, this)[2], SGJNI.SGWidgetCanvas_getContentRect(this.swigCPtr, this)[3]);
    }

    public SGVector2f getContentRectPivot() {
        return new SGVector2f(SGJNI.SGWidgetCanvas_getContentRectPivot(this.swigCPtr, this));
    }

    public SGVector2f getContentRectScale() {
        return new SGVector2f(SGJNI.SGWidgetCanvas_getContentRectScale(this.swigCPtr, this));
    }

    public Config getFormat() {
        Config config = Config.valueOf(SGJNI.SGWidgetCanvas_getFormat(this.swigCPtr, this));
        if (config != null) {
            return config;
        }
        throw new NullPointerException("Unsupported android.graphics.Bitmap.Config format");
    }

    public void invalidate() {
        if (getClass() == SGWidgetCanvas.class) {
            SGJNI.SGWidgetCanvas_invalidate__SWIG_0(this.swigCPtr, this);
        } else {
            SGJNI.SGWidgetCanvas_invalidateSwigExplicitSGWidgetCanvas__SWIG_0(this.swigCPtr, this);
        }
    }

    public void invalidate(RectF rect) {
        if (getClass() == SGWidgetCanvas.class) {
            SGJNI.SGWidgetCanvas_invalidate__SWIG_1(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
        } else {
            SGJNI.SGWidgetCanvas_invalidateSwigExplicitSGWidgetCanvas__SWIG_1(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
        }
    }

    public abstract void onDraw(Canvas canvas);

    public void setCanvasScale(float xScale, float yScale) {
        SGJNI.SGWidgetCanvas_setCanvasScale__SWIG_1(this.swigCPtr, this, xScale, yScale);
    }

    public void setCanvasScale(SGVector2f canvasScale) {
        if (getClass() == SGWidgetCanvas.class) {
            SGJNI.SGWidgetCanvas_setCanvasScale__SWIG_0(this.swigCPtr, this, canvasScale.getData());
        } else {
            SGJNI.SGWidgetCanvas_setCanvasScaleSwigExplicitSGWidgetCanvas__SWIG_0(this.swigCPtr, this, canvasScale.getData());
        }
    }

    public void setContentRect(RectF rect) {
        SGJNI.SGWidgetCanvas_setContentRect(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public void setContentRectPivot(SGVector2f pivot) {
        SGJNI.SGWidgetCanvas_setContentRectPivot(this.swigCPtr, this, pivot.getData());
    }

    public void setContentRectScale(SGVector2f scale) {
        SGJNI.SGWidgetCanvas_setContentRectScale(this.swigCPtr, this, scale.getData());
    }

    public void setFormat(Config imageFormat) {
        SGJNI.SGWidgetCanvas_setFormat(this.swigCPtr, this, imageFormat.name());
    }
}
