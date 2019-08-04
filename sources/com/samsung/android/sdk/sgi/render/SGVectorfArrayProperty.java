package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

public final class SGVectorfArrayProperty extends SGArrayProperty {
    protected SGVectorfArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGVectorfArrayProperty(float[] values, int dimension) {
        this(SwigConstructSGVectorfArrayProperty(values, dimension), true);
    }

    public SGVectorfArrayProperty(SGVector2f[] other) {
        this(SGJNI.new_SGVectorfArrayProperty__SWIG_0(other), true);
    }

    public SGVectorfArrayProperty(SGVector3f[] other) {
        this(SGJNI.new_SGVectorfArrayProperty__SWIG_1(other), true);
    }

    public SGVectorfArrayProperty(SGVector4f[] other) {
        this(SGJNI.new_SGVectorfArrayProperty__SWIG_2(other), true);
    }

    private static long SwigConstructSGVectorfArrayProperty(float[] values, int dimension) {
        if (values != null) {
            return SGJNI.new_SGVectorfArrayProperty__SWIG_3(values, dimension);
        }
        throw new NullPointerException();
    }

    public float[] get() {
        return SGJNI.SGVectorfArrayProperty_get(this.swigCPtr, this);
    }

    public int getDimension() {
        return SGJNI.SGVectorfArrayProperty_getDimension(this.swigCPtr, this);
    }

    public void set(float[] values) {
        if (values == null) {
            throw new NullPointerException();
        }
        SGJNI.SGVectorfArrayProperty_set__SWIG_0(this.swigCPtr, this, values);
    }

    public void set(SGVector2f[] array) {
        SGJNI.SGVectorfArrayProperty_set__SWIG_3(this.swigCPtr, this, array);
    }

    public void set(SGVector3f[] array) {
        SGJNI.SGVectorfArrayProperty_set__SWIG_2(this.swigCPtr, this, array);
    }

    public void set(SGVector4f[] array) {
        SGJNI.SGVectorfArrayProperty_set__SWIG_1(this.swigCPtr, this, array);
    }
}
