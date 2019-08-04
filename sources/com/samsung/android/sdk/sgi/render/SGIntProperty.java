package com.samsung.android.sdk.sgi.render;

public final class SGIntProperty extends SGProperty {
    public SGIntProperty() {
        this(SGJNI.new_SGIntProperty__SWIG_0(), true);
    }

    public SGIntProperty(int value) {
        this(SGJNI.new_SGIntProperty__SWIG_1(value), true);
    }

    protected SGIntProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public int get() {
        return SGJNI.SGIntProperty_get(this.swigCPtr, this);
    }

    public void set(int value) {
        SGJNI.SGIntProperty_set(this.swigCPtr, this, value);
    }
}
