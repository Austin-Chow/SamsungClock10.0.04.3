package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGMatrix2f;
import com.samsung.android.sdk.sgi.base.SGMatrix3f;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;

public final class SGMatrixfArrayProperty extends SGArrayProperty {
    protected SGMatrixfArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGMatrixfArrayProperty(float[] values, int dimension) {
        this(SwigConstructSGMatrixfArrayProperty(values, dimension), true);
    }

    public SGMatrixfArrayProperty(SGMatrix2f[] array) {
        this(SGJNI.new_SGMatrixfArrayProperty__SWIG_0(array), true);
    }

    public SGMatrixfArrayProperty(SGMatrix3f[] array) {
        this(SGJNI.new_SGMatrixfArrayProperty__SWIG_1(array), true);
    }

    public SGMatrixfArrayProperty(SGMatrix4f[] array) {
        this(SGJNI.new_SGMatrixfArrayProperty__SWIG_2(array), true);
    }

    private static long SwigConstructSGMatrixfArrayProperty(float[] values, int dimension) {
        if (values != null) {
            return SGJNI.new_SGMatrixfArrayProperty__SWIG_3(values, dimension);
        }
        throw new NullPointerException();
    }

    public float[] get() {
        return SGJNI.SGMatrixfArrayProperty_get(this.swigCPtr, this);
    }

    public int getDimension() {
        return SGJNI.SGMatrixfArrayProperty_getDimension(this.swigCPtr, this);
    }

    public void set(float[] values) {
        if (values == null) {
            throw new NullPointerException();
        }
        SGJNI.SGMatrixfArrayProperty_set__SWIG_0(this.swigCPtr, this, values);
    }

    public void set(SGMatrix2f[] array) {
        SGJNI.SGMatrixfArrayProperty_set__SWIG_3(this.swigCPtr, this, array);
    }

    public void set(SGMatrix3f[] array) {
        SGJNI.SGMatrixfArrayProperty_set__SWIG_2(this.swigCPtr, this, array);
    }

    public void set(SGMatrix4f[] array) {
        SGJNI.SGMatrixfArrayProperty_set__SWIG_1(this.swigCPtr, this, array);
    }
}
