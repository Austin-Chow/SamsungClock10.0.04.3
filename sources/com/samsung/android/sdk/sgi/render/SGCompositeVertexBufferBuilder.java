package com.samsung.android.sdk.sgi.render;

public class SGCompositeVertexBufferBuilder {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGCompositeVertexBufferBuilder(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGCompositeVertexBufferBuilder(String[] name, SGVertexBuffer[] buffer, SGBufferUsageType usage) {
        this(SGJNI.new_SGCompositeVertexBufferBuilder(name, buffer, SGJNI.getData(usage)), true);
    }

    public static long getCPtr(SGCompositeVertexBufferBuilder obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void exportBuffers(SGGeometry destination) {
        SGJNI.SGCompositeVertexBufferBuilder_exportBuffers(this.swigCPtr, this, SGGeometry.getCPtr(destination), destination);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGCompositeVertexBufferBuilder(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGPartVertexBuffer getBuffer(int index) {
        if (index >= 0) {
            return new SGPartVertexBuffer(SGJNI.SGCompositeVertexBufferBuilder_getBuffer(this.swigCPtr, this, index), true);
        }
        throw new NegativeArraySizeException("Negative array size!");
    }

    public String getBufferName(int index) {
        if (index >= 0) {
            return SGJNI.SGCompositeVertexBufferBuilder_getBufferName(this.swigCPtr, this, index);
        }
        throw new NegativeArraySizeException("Negative array size!");
    }

    public int getBuffersCount() {
        return SGJNI.SGCompositeVertexBufferBuilder_getBuffersCount(this.swigCPtr, this);
    }

    public SGCompositeVertexBuffer getCompositeVertexBuffer() {
        return new SGCompositeVertexBuffer(SGJNI.SGCompositeVertexBufferBuilder_getCompositeVertexBuffer(this.swigCPtr, this), true);
    }
}
