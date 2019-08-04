package com.samsung.android.sdk.sgi.ui;

import android.graphics.Bitmap;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.vi.SGBlendMode;

public class SGWidgetImage extends SGWidget {
    public SGWidgetImage(float width, float height, int color) {
        this(new SGVector2f(width, height), color);
    }

    public SGWidgetImage(float width, float height, Bitmap bitmap) {
        this(width, height, bitmap, false);
    }

    public SGWidgetImage(float width, float height, Bitmap bitmap, boolean autoRecycle) {
        this(new SGVector2f(width, height), bitmap, autoRecycle);
    }

    protected SGWidgetImage(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGWidgetImage(Bitmap bitmap) {
        this(bitmap, false);
    }

    public SGWidgetImage(Bitmap bitmap, boolean autoRecycle) {
        float f = 0.0f;
        float width = bitmap == null ? 0.0f : (float) bitmap.getWidth();
        if (bitmap != null) {
            f = (float) bitmap.getHeight();
        }
        this(width, f, bitmap, autoRecycle);
    }

    public SGWidgetImage(RectF rect) {
        this(rect.width(), rect.height(), -1);
        setPosition(rect.left, rect.top);
    }

    public SGWidgetImage(SGVector2f size, int color) {
        this(SGJNI.new_SGWidgetImage(size.getData(), color), true);
        SGJNI.SGWidgetImage_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public SGWidgetImage(SGVector2f size, Bitmap bitmap) {
        this(size, bitmap, false);
    }

    public SGWidgetImage(SGVector2f size, Bitmap bitmap, boolean autoRecycle) {
        this(size, -1);
        setBitmap(bitmap, autoRecycle);
    }

    public SGBlendMode getBlendMode() {
        return ((SGBlendMode[]) SGBlendMode.class.getEnumConstants())[SGJNI.SGWidgetImage_getBlendMode(this.swigCPtr, this)];
    }

    public int getColor() {
        return SGJNI.SGWidgetImage_getColor(this.swigCPtr, this);
    }

    public RectF getContentRect() {
        return new RectF(SGJNI.SGWidgetImage_getContentRect(this.swigCPtr, this)[0], SGJNI.SGWidgetImage_getContentRect(this.swigCPtr, this)[1], SGJNI.SGWidgetImage_getContentRect(this.swigCPtr, this)[2], SGJNI.SGWidgetImage_getContentRect(this.swigCPtr, this)[3]);
    }

    public SGVector2f getContentRectPivot() {
        return new SGVector2f(SGJNI.SGWidgetImage_getContentRectPivot(this.swigCPtr, this));
    }

    public SGVector2f getContentRectScale() {
        return new SGVector2f(SGJNI.SGWidgetImage_getContentRectScale(this.swigCPtr, this));
    }

    public boolean isAlphaBlendingEnabled() {
        return SGJNI.SGWidgetImage_isAlphaBlendingEnabled(this.swigCPtr, this);
    }

    public boolean isPreMultipliedRGBAEnabled() {
        return SGJNI.SGWidgetImage_isPreMultipliedRGBAEnabled(this.swigCPtr, this);
    }

    public void setAlphaBlendingEnabled(boolean enabled) {
        SGJNI.SGWidgetImage_setAlphaBlendingEnabled(this.swigCPtr, this, enabled);
    }

    public void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, false);
    }

    public void setBitmap(Bitmap bitmap, boolean autoRecycle) {
        SGJNI.SGWidgetImage_setBitmap(this.swigCPtr, this, bitmap, autoRecycle);
    }

    public void setBlendMode(SGBlendMode mode) {
        SGJNI.SGWidgetImage_setBlendMode(this.swigCPtr, this, SGJNI.getData(mode));
    }

    public void setColor(int color) {
        SGJNI.SGWidgetImage_setColor(this.swigCPtr, this, color);
    }

    public void setContentRect(RectF rect) {
        SGJNI.SGWidgetImage_setContentRect(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public void setContentRectPivot(SGVector2f pivot) {
        SGJNI.SGWidgetImage_setContentRectPivot(this.swigCPtr, this, pivot.getData());
    }

    public void setContentRectScale(SGVector2f scale) {
        SGJNI.SGWidgetImage_setContentRectScale(this.swigCPtr, this, scale.getData());
    }

    public void setPreMultipliedRGBAEnabled(boolean enabled) {
        SGJNI.SGWidgetImage_setPreMultipliedRGBAEnabled(this.swigCPtr, this, enabled);
    }

    public void setTexture(SGProperty texture) {
        SGJNI.SGWidgetImage_setTexture(this.swigCPtr, this, SGProperty.getCPtr(texture), texture);
    }
}
