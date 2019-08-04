package com.samsung.android.sdk.sgi.render;

import java.nio.FloatBuffer;

public final class SGVertexBuffer extends SGBuffer {
    public SGVertexBuffer(int componentsPerElement, SGBufferDataType dataType, SGBufferUsageType usage, int size) {
        this(SwigConstructSGVertexBuffer(componentsPerElement, dataType, usage, size), true);
    }

    protected SGVertexBuffer(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    private static long SwigConstructSGVertexBuffer(int componentsPerElement, SGBufferDataType dataType, SGBufferUsageType usage, int size) {
        if (componentsPerElement < 0) {
            throw new NegativeArraySizeException("Negative components number!");
        } else if (size >= 0) {
            return SGJNI.new_SGVertexBuffer(componentsPerElement, SGJNI.getData(dataType), SGJNI.getData(usage), size);
        } else {
            throw new NegativeArraySizeException("Negative array size!");
        }
    }

    public int getComponentsPerElement() {
        return SGJNI.SGVertexBuffer_getComponentsPerElement(this.swigCPtr, this);
    }

    public FloatBuffer getFloatBuffer() {
        return getByteBuffer().asFloatBuffer();
    }

    public int getVertexCount() {
        return SGJNI.SGVertexBuffer_getVertexCount(this.swigCPtr, this);
    }

    public void setVertexCount(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("Negative array size!");
        }
        SGJNI.SGVertexBuffer_setVertexCount(this.swigCPtr, this, size);
        this.buffer = null;
    }
}
