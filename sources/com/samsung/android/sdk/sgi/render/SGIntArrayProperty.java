package com.samsung.android.sdk.sgi.render;

import java.nio.IntBuffer;

public final class SGIntArrayProperty extends SGArrayProperty {
    protected SGIntArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGIntArrayProperty(int[] array) {
        this(SwigConstructSGIntArrayProperty(array), true);
    }

    private static long SwigConstructSGIntArrayProperty(int[] array) {
        if (array != null) {
            return SGJNI.new_SGIntArrayProperty(array);
        }
        throw new NullPointerException();
    }

    public int[] get() {
        return SGJNI.SGIntArrayProperty_get(this.swigCPtr, this);
    }

    public IntBuffer getIntBuffer() {
        return SGJNI.SGIntArrayProperty_getIntBuffer(this.swigCPtr, this).asIntBuffer();
    }

    public void set(int[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        SGJNI.SGIntArrayProperty_set(this.swigCPtr, this, array);
    }

    public void setSize(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("Negative array size!");
        }
        SGJNI.SGIntArrayProperty_setSize(this.swigCPtr, this, size);
    }
}
