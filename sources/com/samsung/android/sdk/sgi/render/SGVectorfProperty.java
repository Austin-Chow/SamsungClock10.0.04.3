package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

public class SGVectorfProperty extends SGProperty {
    public SGVectorfProperty(int dimension) {
        this(SGJNI.new_SGVectorfProperty__SWIG_0(dimension), true);
    }

    protected SGVectorfProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGVectorfProperty(SGVector2f other) {
        this(SGJNI.new_SGVectorfProperty__SWIG_4(other.getData()), true);
    }

    public SGVectorfProperty(SGVector3f other) {
        this(SGJNI.new_SGVectorfProperty__SWIG_3(other.getData()), true);
    }

    public SGVectorfProperty(SGVector4f other) {
        this(SGJNI.new_SGVectorfProperty__SWIG_2(other.getData()), true);
    }

    public SGVectorfProperty(float[] values) {
        this(SwigConstructSGVectorfProperty(values), true);
    }

    private static long SwigConstructSGVectorfProperty(float[] values) {
        if (values != null) {
            return SGJNI.new_SGVectorfProperty__SWIG_1(values);
        }
        throw new NullPointerException();
    }

    public int getDimension() {
        return SGJNI.SGVectorfProperty_getDimension(this.swigCPtr, this);
    }

    public void set(float x, float y) {
        SGJNI.SGVectorfProperty_set__SWIG_5(this.swigCPtr, this, x, y);
    }

    public void set(float x, float y, float z) {
        SGJNI.SGVectorfProperty_set__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void set(float x, float y, float z, float w) {
        SGJNI.SGVectorfProperty_set__SWIG_1(this.swigCPtr, this, x, y, z, w);
    }

    public void set(SGVector2f value) {
        SGJNI.SGVectorfProperty_set__SWIG_6(this.swigCPtr, this, value.getData());
    }

    public void set(SGVector3f value) {
        SGJNI.SGVectorfProperty_set__SWIG_4(this.swigCPtr, this, value.getData());
    }

    public void set(SGVector4f value) {
        SGJNI.SGVectorfProperty_set__SWIG_2(this.swigCPtr, this, value.getData());
    }

    public void set(float[] value) {
        if (value == null) {
            throw new NullPointerException();
        }
        SGJNI.SGVectorfProperty_set__SWIG_0(this.swigCPtr, this, value);
    }

    public float[] toFloatArray() {
        return SGJNI.SGVectorfProperty_toFloatArray(this.swigCPtr, this);
    }
}
