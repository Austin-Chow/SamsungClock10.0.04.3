package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGProperty;

public class SGLayerImage extends SGLayer {
    public SGLayerImage() {
        this(SGJNI.new_SGLayerImage(), true);
    }

    public SGLayerImage(float width, float height, int color) {
        this(new SGVector2f(width, height), color);
    }

    public SGLayerImage(float width, float height, Bitmap bitmap) {
        this(width, height, bitmap, false);
    }

    public SGLayerImage(float width, float height, Bitmap bitmap, boolean autoRecycle) {
        this();
        setSize(new SGVector2f(width, height));
        setBitmap(bitmap, autoRecycle);
    }

    protected SGLayerImage(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGLayerImage(Bitmap bitmap) {
        this(bitmap, false);
    }

    public SGLayerImage(Bitmap bitmap, boolean autoRecycle) {
        this();
        if (bitmap != null) {
            setBitmap(bitmap, autoRecycle);
            setSize(new SGVector2f((float) bitmap.getWidth(), (float) bitmap.getHeight()));
        }
    }

    public SGLayerImage(RectF rect) {
        this();
        setSize(new SGVector2f(rect.width(), rect.height()));
        setPosition(new SGVector2f(rect.left, rect.top));
    }

    public SGLayerImage(SGVector2f size, int color) {
        this();
        setSize(size);
        setColor(color);
    }

    public SGLayerImage(SGVector2f size, Bitmap bitmap) {
        this(size, bitmap, false);
    }

    public SGLayerImage(SGVector2f size, Bitmap bitmap, boolean autoRecycle) {
        this();
        setSize(size);
        setBitmap(bitmap, autoRecycle);
    }

    public SGBlendMode getBlendMode() {
        return ((SGBlendMode[]) SGBlendMode.class.getEnumConstants())[SGJNI.SGLayerImage_getBlendMode(this.swigCPtr, this)];
    }

    public int getColor() {
        return SGJNI.SGLayerImage_getColor(this.swigCPtr, this);
    }

    public RectF getContentRect() {
        return new RectF(SGJNI.SGLayerImage_getContentRect(this.swigCPtr, this)[0], SGJNI.SGLayerImage_getContentRect(this.swigCPtr, this)[1], SGJNI.SGLayerImage_getContentRect(this.swigCPtr, this)[2], SGJNI.SGLayerImage_getContentRect(this.swigCPtr, this)[3]);
    }

    public SGVector2f getContentRectPivot() {
        return new SGVector2f(SGJNI.SGLayerImage_getContentRectPivot(this.swigCPtr, this));
    }

    public SGVector2f getContentRectScale() {
        return new SGVector2f(SGJNI.SGLayerImage_getContentRectScale(this.swigCPtr, this));
    }

    public boolean isAlphaBlendingEnabled() {
        return SGJNI.SGLayerImage_isAlphaBlendingEnabled(this.swigCPtr, this);
    }

    public boolean isPreMultipliedRGBAEnabled() {
        return SGJNI.SGLayerImage_isPreMultipliedRGBAEnabled(this.swigCPtr, this);
    }

    public void setAlphaBlendingEnabled(boolean enabled) {
        SGJNI.SGLayerImage_setAlphaBlendingEnabled(this.swigCPtr, this, enabled);
    }

    public void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, false);
    }

    public void setBitmap(Bitmap bitmap, boolean autoRecycle) {
        SGJNI.SGLayerImage_setBitmap(this.swigCPtr, this, bitmap, autoRecycle);
    }

    public void setBlendMode(SGBlendMode mode) {
        SGJNI.SGLayerImage_setBlendMode(this.swigCPtr, this, SGJNI.getData(mode));
    }

    public void setColor(int color) {
        SGJNI.SGLayerImage_setColor(this.swigCPtr, this, color);
    }

    public void setContentRect(RectF rect) {
        SGJNI.SGLayerImage_setContentRect(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public void setContentRectPivot(SGVector2f pivot) {
        SGJNI.SGLayerImage_setContentRectPivot(this.swigCPtr, this, pivot.getData());
    }

    public void setContentRectScale(SGVector2f scale) {
        SGJNI.SGLayerImage_setContentRectScale(this.swigCPtr, this, scale.getData());
    }

    public void setPreMultipliedRGBAEnabled(boolean enabled) {
        SGJNI.SGLayerImage_setPreMultipliedRGBAEnabled(this.swigCPtr, this, enabled);
    }

    public void setTexture(SGProperty property) {
        SGJNI.SGLayerImage_setTexture(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }
}
