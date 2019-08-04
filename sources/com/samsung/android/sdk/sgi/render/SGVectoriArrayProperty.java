package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2i;
import com.samsung.android.sdk.sgi.base.SGVector3i;
import com.samsung.android.sdk.sgi.base.SGVector4i;

public final class SGVectoriArrayProperty extends SGArrayProperty {
    protected SGVectoriArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGVectoriArrayProperty(int[] values, int dimension) {
        this(SwigConstructSGVectoriArrayProperty(values, dimension), true);
    }

    public SGVectoriArrayProperty(SGVector2i[] other) {
        this(SGJNI.new_SGVectoriArrayProperty__SWIG_0(other), true);
    }

    public SGVectoriArrayProperty(SGVector3i[] other) {
        this(SGJNI.new_SGVectoriArrayProperty__SWIG_1(other), true);
    }

    public SGVectoriArrayProperty(SGVector4i[] other) {
        this(SGJNI.new_SGVectoriArrayProperty__SWIG_2(other), true);
    }

    private static long SwigConstructSGVectoriArrayProperty(int[] values, int dimension) {
        if (values != null) {
            return SGJNI.new_SGVectoriArrayProperty__SWIG_3(values, dimension);
        }
        throw new NullPointerException();
    }

    public int[] get() {
        return SGJNI.SGVectoriArrayProperty_get(this.swigCPtr, this);
    }

    public int getDimension() {
        return SGJNI.SGVectoriArrayProperty_getDimension(this.swigCPtr, this);
    }

    public void set(int[] values) {
        if (values == null) {
            throw new NullPointerException();
        }
        SGJNI.SGVectoriArrayProperty_set__SWIG_0(this.swigCPtr, this, values);
    }

    public void set(SGVector2i[] array) {
        SGJNI.SGVectoriArrayProperty_set__SWIG_3(this.swigCPtr, this, array);
    }

    public void set(SGVector3i[] array) {
        SGJNI.SGVectoriArrayProperty_set__SWIG_2(this.swigCPtr, this, array);
    }

    public void set(SGVector4i[] array) {
        SGJNI.SGVectoriArrayProperty_set__SWIG_1(this.swigCPtr, this, array);
    }
}
