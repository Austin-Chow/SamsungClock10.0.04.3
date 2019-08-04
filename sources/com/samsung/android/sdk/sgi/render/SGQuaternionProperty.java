package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGQuaternion;

public final class SGQuaternionProperty extends SGProperty {
    public SGQuaternionProperty() {
        this(SGJNI.new_SGQuaternionProperty__SWIG_0(), true);
    }

    protected SGQuaternionProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGQuaternionProperty(SGQuaternion value) {
        this(SGJNI.new_SGQuaternionProperty__SWIG_1(value.getData()), true);
    }

    public SGQuaternion get() {
        return new SGQuaternion(SGJNI.SGQuaternionProperty_get(this.swigCPtr, this));
    }

    public void set(float x, float y, float z, float w) {
        SGJNI.SGQuaternionProperty_set__SWIG_1(this.swigCPtr, this, x, y, z, w);
    }

    public void set(SGQuaternion value) {
        SGJNI.SGQuaternionProperty_set__SWIG_0(this.swigCPtr, this, value.getData());
    }
}
