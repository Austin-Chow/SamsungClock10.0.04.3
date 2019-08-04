package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2i;
import com.samsung.android.sdk.sgi.base.SGVector3i;
import com.samsung.android.sdk.sgi.base.SGVector4i;

public final class SGVectoriProperty extends SGProperty {
    public SGVectoriProperty(int dimension) {
        this(SGJNI.new_SGVectoriProperty__SWIG_0(dimension), true);
    }

    protected SGVectoriProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGVectoriProperty(int[] values) {
        this(SwigConstructSGVectoriProperty(values), true);
    }

    private static long SwigConstructSGVectoriProperty(int[] values) {
        if (values != null) {
            return SGJNI.new_SGVectoriProperty__SWIG_1(values);
        }
        throw new NullPointerException();
    }

    public int getDimension() {
        return SGJNI.SGVectoriProperty_getDimension(this.swigCPtr, this);
    }

    public void set(int x, int y) {
        SGJNI.SGVectoriProperty_set__SWIG_5(this.swigCPtr, this, x, y);
    }

    public void set(int x, int y, int z) {
        SGJNI.SGVectoriProperty_set__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void set(int x, int y, int z, int w) {
        SGJNI.SGVectoriProperty_set__SWIG_1(this.swigCPtr, this, x, y, z, w);
    }

    public void set(SGVector2i value) {
        SGJNI.SGVectoriProperty_set__SWIG_6(this.swigCPtr, this, value.getData());
    }

    public void set(SGVector3i value) {
        SGJNI.SGVectoriProperty_set__SWIG_4(this.swigCPtr, this, value.getData());
    }

    public void set(SGVector4i value) {
        SGJNI.SGVectoriProperty_set__SWIG_2(this.swigCPtr, this, value.getData());
    }

    public void set(int[] value) {
        if (value == null) {
            throw new NullPointerException();
        }
        SGJNI.SGVectoriProperty_set__SWIG_0(this.swigCPtr, this, value);
    }

    public int[] toIntArray() {
        return SGJNI.SGVectoriProperty_toIntArray(this.swigCPtr, this);
    }
}
