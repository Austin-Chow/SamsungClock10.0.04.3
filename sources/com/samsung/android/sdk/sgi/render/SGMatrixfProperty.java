package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;

public class SGMatrixfProperty extends SGProperty {
    public SGMatrixfProperty(int dimension) {
        this(SGJNI.new_SGMatrixfProperty__SWIG_0(dimension), true);
    }

    protected SGMatrixfProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGMatrixfProperty(SGMatrix4f value) {
        this(value.getData());
    }

    public SGMatrixfProperty(float[] value) {
        this(SwigConstructSGMatrixfProperty(value), true);
    }

    private static long SwigConstructSGMatrixfProperty(float[] value) {
        if (value != null) {
            return SGJNI.new_SGMatrixfProperty__SWIG_1(value);
        }
        throw new NullPointerException();
    }

    public int getDimension() {
        return SGJNI.SGMatrixfProperty_getDimension(this.swigCPtr, this);
    }

    public void set(SGMatrix4f value) {
        set(value.getData());
    }

    public void set(float[] value) {
        if (value == null) {
            throw new NullPointerException();
        }
        SGJNI.SGMatrixfProperty_set(this.swigCPtr, this, value);
    }

    public float[] toFloatArray() {
        return SGJNI.SGMatrixfProperty_toFloatArray(this.swigCPtr, this);
    }
}
