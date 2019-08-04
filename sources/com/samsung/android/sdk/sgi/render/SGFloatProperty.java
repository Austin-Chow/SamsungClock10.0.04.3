package com.samsung.android.sdk.sgi.render;

public final class SGFloatProperty extends SGProperty {
    public SGFloatProperty() {
        this(SGJNI.new_SGFloatProperty__SWIG_0(), true);
    }

    public SGFloatProperty(float value) {
        this(SGJNI.new_SGFloatProperty__SWIG_1(value), true);
    }

    protected SGFloatProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public float get() {
        return SGJNI.SGFloatProperty_get(this.swigCPtr, this);
    }

    public void set(float value) {
        SGJNI.SGFloatProperty_set(this.swigCPtr, this, value);
    }
}
