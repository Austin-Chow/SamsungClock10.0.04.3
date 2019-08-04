package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGBox3f;

public final class SGGeometry {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    private SGGeometry(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGGeometry(SGIndexBuffer indexBuffer) {
        this(SGJNI.new_SGGeometry__SWIG_0(SGBuffer.getCPtr(indexBuffer), indexBuffer), true);
    }

    public SGGeometry(SGPrimitiveType primitiveType) {
        this(SGJNI.new_SGGeometry__SWIG_1(SGJNI.getData(primitiveType)), true);
    }

    public static long getCPtr(SGGeometry obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGGeometry_getHandle(this.swigCPtr, this);
    }

    public void addBuffer(String name, SGBuffer buffer) {
        SGJNI.SGGeometry_addBuffer(this.swigCPtr, this, name, SGBuffer.getCPtr(buffer), buffer);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGGeometry) && ((SGGeometry) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGGeometry(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGBox3f getBoundingBox() {
        return new SGBox3f(SGJNI.SGGeometry_getBoundingBox(this.swigCPtr, this));
    }

    public String getBufferName(int index) {
        return SGJNI.SGGeometry_getBufferName(this.swigCPtr, this, index);
    }

    public int getBuffersCount() {
        return SGJNI.SGGeometry_getBuffersCount(this.swigCPtr, this);
    }

    public SGIndexBuffer getIndexBuffer() {
        long cPtr = SGJNI.SGGeometry_getIndexBuffer(this.swigCPtr, this);
        return cPtr != 0 ? (SGIndexBuffer) SGJNI.createObjectFromNativePtr(SGIndexBuffer.class, cPtr, true) : null;
    }

    public SGPrimitiveType getPrimitiveType() {
        return ((SGPrimitiveType[]) SGPrimitiveType.class.getEnumConstants())[SGJNI.SGGeometry_getPrimitiveType(this.swigCPtr, this)];
    }

    public SGVertexBuffer getVertexBuffer(int index) {
        return new SGVertexBuffer(SGJNI.SGGeometry_getVertexBuffer__SWIG_1(this.swigCPtr, this, index), true);
    }

    public SGVertexBuffer getVertexBuffer(String name) {
        return new SGVertexBuffer(SGJNI.SGGeometry_getVertexBuffer__SWIG_0(this.swigCPtr, this, name), true);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void invalidate() {
        SGJNI.SGGeometry_invalidate__SWIG_0(this.swigCPtr, this);
    }

    public void invalidate(int index) {
        SGJNI.SGGeometry_invalidate__SWIG_1(this.swigCPtr, this, index);
    }

    public void setBoundingBox(SGBox3f boundingBox) {
        SGJNI.SGGeometry_setBoundingBox(this.swigCPtr, this, boundingBox.getData());
    }
}
