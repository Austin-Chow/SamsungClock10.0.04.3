package com.samsung.android.sdk.sgi.render;

public final class SGLineWidthProperty extends SGProperty {
    public SGLineWidthProperty() {
        this(SGJNI.new_SGLineWidthProperty__SWIG_0(), true);
    }

    public SGLineWidthProperty(float lineWidth) {
        this(SGJNI.new_SGLineWidthProperty__SWIG_1(lineWidth), true);
    }

    protected SGLineWidthProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public float getWidth() {
        return SGJNI.SGLineWidthProperty_getWidth(this.swigCPtr, this);
    }

    public boolean isLineWidthPropertyEnabled() {
        return SGJNI.SGLineWidthProperty_isLineWidthPropertyEnabled(this.swigCPtr, this);
    }

    public void setLineWidthPropertyEnabled(boolean enabled) {
        SGJNI.SGLineWidthProperty_setLineWidthPropertyEnabled(this.swigCPtr, this, enabled);
    }

    public void setWidth(float lineWidth) {
        SGJNI.SGLineWidthProperty_setWidth(this.swigCPtr, this, lineWidth);
    }
}
