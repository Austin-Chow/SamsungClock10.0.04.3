package com.samsung.android.sdk.sgi.render;

public final class SGPartVertexBuffer extends SGBuffer {
    protected SGPartVertexBuffer(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public int getComponentsPerElement() {
        return SGJNI.SGPartVertexBuffer_getComponentsPerElement(this.swigCPtr, this);
    }

    public SGCompositeVertexBuffer getCompositeBuffer() {
        return new SGCompositeVertexBuffer(SGJNI.SGPartVertexBuffer_getCompositeBuffer(this.swigCPtr, this), true);
    }

    public int getOffset() {
        return SGJNI.SGPartVertexBuffer_getOffset(this.swigCPtr, this);
    }

    public int getStride() {
        return SGJNI.SGPartVertexBuffer_getStride(this.swigCPtr, this);
    }
}
