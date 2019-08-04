package com.samsung.android.sdk.sgi.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public final class SGFloatArrayProperty extends SGArrayProperty {
    public SGFloatArrayProperty(int size) {
        this(SwigConstructSGFloatArrayProperty(size), true);
    }

    protected SGFloatArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    private static long SwigConstructSGFloatArrayProperty(int size) {
        if (size >= 0) {
            return SGJNI.new_SGFloatArrayProperty(size);
        }
        throw new NegativeArraySizeException("Negative array size!");
    }

    private Object getBuffer() {
        return SGJNI.SGFloatArrayProperty_getBuffer(this.swigCPtr, this);
    }

    public float[] get() {
        return SGJNI.SGFloatArrayProperty_get(this.swigCPtr, this);
    }

    public FloatBuffer getFloatBuffer() {
        return ((ByteBuffer) getBuffer()).asFloatBuffer();
    }

    public void set(float[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        SGJNI.SGFloatArrayProperty_set(this.swigCPtr, this, array);
    }

    public void setSize(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("Negative array size!");
        }
        SGJNI.SGFloatArrayProperty_setSize(this.swigCPtr, this, size);
    }
}
