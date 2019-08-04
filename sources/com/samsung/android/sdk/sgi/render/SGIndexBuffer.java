package com.samsung.android.sdk.sgi.render;

import java.nio.ShortBuffer;

public final class SGIndexBuffer extends SGBuffer {
    protected SGIndexBuffer(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGIndexBuffer(SGPrimitiveType primitiveType, SGBufferUsageType usageType, int size) {
        this(SwigConstructSGIndexBuffer(primitiveType, usageType, size), true);
    }

    private static long SwigConstructSGIndexBuffer(SGPrimitiveType primitiveType, SGBufferUsageType usageType, int size) {
        if (size >= 0) {
            return SGJNI.new_SGIndexBuffer(SGJNI.getData(primitiveType), SGJNI.getData(usageType), size);
        }
        throw new NegativeArraySizeException("Negative array size");
    }

    public SGPrimitiveType getPrimitiveType() {
        return ((SGPrimitiveType[]) SGPrimitiveType.class.getEnumConstants())[SGJNI.SGIndexBuffer_getPrimitiveType(this.swigCPtr, this)];
    }

    public ShortBuffer getShortBuffer() {
        return getByteBuffer().asShortBuffer();
    }

    public void setSize(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("Negative array size");
        }
        SGJNI.SGIndexBuffer_setSize(this.swigCPtr, this, size);
        this.buffer = null;
    }
}
