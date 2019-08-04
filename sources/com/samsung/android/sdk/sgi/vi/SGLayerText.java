package com.samsung.android.sdk.sgi.vi;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.RectF;
import android.util.TypedValue;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2f;

@TargetApi(19)
public class SGLayerText extends SGLayer {
    public static final int TEXT_GRAVITY_BOTTOM = 8;
    public static final int TEXT_GRAVITY_CENTER = 15;
    public static final int TEXT_GRAVITY_CENTER_HORIZONTAL = 3;
    public static final int TEXT_GRAVITY_CENTER_VERTICAL = 12;
    public static final int TEXT_GRAVITY_LEFT = 1;
    public static final int TEXT_GRAVITY_NONE = 0;
    public static final int TEXT_GRAVITY_RIGHT = 2;
    public static final int TEXT_GRAVITY_TOP = 4;

    public SGLayerText() {
        this(SGJNI.new_SGLayerText__SWIG_0(), true);
    }

    protected SGLayerText(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGLayerText(RectF rect) {
        this(SGJNI.new_SGLayerText__SWIG_1(SGMathNative.getArrayRect(rect)), true);
    }

    public static SGTypeface getTypefaceDefault() {
        return new SGTypeface(SGJNI.SGLayerText_getTypefaceDefault(), true);
    }

    private void setTextSizeNative(float textSize) {
        SGJNI.SGLayerText_setTextSizeNative(this.swigCPtr, this, textSize);
    }

    public int getMaxLinesCount() {
        return SGJNI.SGLayerText_getMaxLinesCount(this.swigCPtr, this);
    }

    public int getSelectionBackgroundColor() {
        return SGJNI.SGLayerText_getSelectionBackgroundColor(this.swigCPtr, this);
    }

    public int getSelectionTextColor() {
        return SGJNI.SGLayerText_getSelectionTextColor(this.swigCPtr, this);
    }

    public int getShadowColor() {
        return SGJNI.SGLayerText_getShadowColor(this.swigCPtr, this);
    }

    public SGVector2f getShadowOffset() {
        return new SGVector2f(SGJNI.SGLayerText_getShadowOffset(this.swigCPtr, this));
    }

    public float getShadowRadius() {
        return SGJNI.SGLayerText_getShadowRadius(this.swigCPtr, this);
    }

    public float getShadowThickness() {
        return SGJNI.SGLayerText_getShadowThickness(this.swigCPtr, this);
    }

    public String getText() {
        return SGJNI.SGLayerText_getText(this.swigCPtr, this);
    }

    public int getTextColor() {
        return SGJNI.SGLayerText_getTextColor(this.swigCPtr, this);
    }

    public int getTextGravity() {
        return SGJNI.SGLayerText_getTextGravity(this.swigCPtr, this);
    }

    public float getTextSize() {
        return SGJNI.SGLayerText_getTextSize(this.swigCPtr, this);
    }

    public SGTypeface getTypeface() {
        long cPtr = SGJNI.SGLayerText_getTypeface(this.swigCPtr, this);
        return cPtr != 0 ? (SGTypeface) SGJNI.createObjectFromNativePtr(SGTypeface.class, cPtr, true) : null;
    }

    public void setMaxLinesCount(int num) {
        SGJNI.SGLayerText_setMaxLinesCount(this.swigCPtr, this, num);
    }

    public void setSelectionTextColor(int color) {
        SGJNI.SGLayerText_setSelectionTextColor(this.swigCPtr, this, color);
    }

    public void setShadow(float radius, SGVector2f offset, int color, float thickness) {
        SGJNI.SGLayerText_setShadow(this.swigCPtr, this, radius, offset.getData(), color, thickness);
    }

    public void setShadowColor(int color) {
        SGJNI.SGLayerText_setShadowColor(this.swigCPtr, this, color);
    }

    public void setShadowOffset(SGVector2f offset) {
        SGJNI.SGLayerText_setShadowOffset(this.swigCPtr, this, offset.getData());
    }

    public void setShadowRadius(float radius) {
        SGJNI.SGLayerText_setShadowRadius(this.swigCPtr, this, radius);
    }

    public void setShadowThickness(float thickness) {
        SGJNI.SGLayerText_setShadowThickness(this.swigCPtr, this, thickness);
    }

    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        SGJNI.SGLayerText_setText(this.swigCPtr, this, text);
    }

    public void setTextColor(int color) {
        SGJNI.SGLayerText_setTextColor(this.swigCPtr, this, color);
    }

    public void setTextGravity(int textGravity) {
        SGJNI.SGLayerText_setTextGravity(this.swigCPtr, this, textGravity);
    }

    public void setTextSelection(int firstSelectedGlyphNum, int selectionLength) {
        SGJNI.SGLayerText_setTextSelection(this.swigCPtr, this, firstSelectedGlyphNum, selectionLength);
    }

    public void setTextSize(float size) {
        setTextSize(2, size);
    }

    public void setTextSize(int unit, float size) {
        setTextSizeNative(TypedValue.applyDimension(unit, size, Resources.getSystem().getDisplayMetrics()));
    }

    public void setTypeface(SGTypeface typeface) {
        SGJNI.SGLayerText_setTypeface(this.swigCPtr, this, SGTypeface.getCPtr(typeface), typeface);
    }
}
