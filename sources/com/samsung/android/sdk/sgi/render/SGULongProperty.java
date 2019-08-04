package com.samsung.android.sdk.sgi.render;

public final class SGULongProperty extends SGProperty {
    public SGULongProperty() {
        this(SGJNI.new_SGULongProperty__SWIG_0(), true);
    }

    public SGULongProperty(long value) {
        this(SGJNI.new_SGULongProperty__SWIG_1(value), true);
    }

    protected SGULongProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public long get() {
        return SGJNI.SGULongProperty_get(this.swigCPtr, this);
    }

    public void set(long value) {
        SGJNI.SGULongProperty_set(this.swigCPtr, this, value);
    }
}
