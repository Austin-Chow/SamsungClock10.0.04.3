package com.samsung.android.sdk.sgi.render;

import java.nio.ByteBuffer;

public class SGBuffer {
    protected ByteBuffer buffer;
    private boolean swigCMemOwn;
    protected long swigCPtr;

    SGBuffer() {
        this(SGJNI.new_SGBuffer(), true);
    }

    protected SGBuffer(long cPtr, boolean cMemoryOwn) {
        this.buffer = null;
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    private Object getBuffer() {
        return SGJNI.SGBuffer_getBuffer(this.swigCPtr, this);
    }

    public static long getCPtr(SGBuffer obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGBuffer_getHandle(this.swigCPtr, this);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGBuffer) && ((SGBuffer) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGBuffer(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public ByteBuffer getByteBuffer() {
        if (this.buffer == null) {
            this.buffer = (ByteBuffer) getBuffer();
        }
        return this.buffer;
    }

    public SGBufferDataType getDataType() {
        return ((SGBufferDataType[]) SGBufferDataType.class.getEnumConstants())[SGJNI.SGBuffer_getDataType(this.swigCPtr, this)];
    }

    public SGBufferUsageType getUsageType() {
        return ((SGBufferUsageType[]) SGBufferUsageType.class.getEnumConstants())[SGJNI.SGBuffer_getUsageType(this.swigCPtr, this)];
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void invalidate() {
        SGJNI.SGBuffer_invalidate__SWIG_0(this.swigCPtr, this);
    }

    public void invalidate(int numElements) {
        SGJNI.SGBuffer_invalidate__SWIG_1(this.swigCPtr, this, numElements);
    }

    public void swap(SGBuffer buffer) {
        SGJNI.SGBuffer_swap(this.swigCPtr, this, getCPtr(buffer), buffer);
    }
}
