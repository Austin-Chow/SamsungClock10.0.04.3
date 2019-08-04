package com.samsung.android.sdk.sgi.render;

public final class SGBoolProperty extends SGProperty {
    public SGBoolProperty() {
        this(SGJNI.new_SGBoolProperty__SWIG_0(), true);
    }

    protected SGBoolProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGBoolProperty(boolean value) {
        this(SGJNI.new_SGBoolProperty__SWIG_1(value), true);
    }

    public boolean get() {
        return SGJNI.SGBoolProperty_get(this.swigCPtr, this);
    }

    public void set(boolean value) {
        SGJNI.SGBoolProperty_set(this.swigCPtr, this, value);
    }
}
