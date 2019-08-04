package com.samsung.android.sdk.sgi.render;

public final class SGRenderBuffer {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGRenderBuffer() {
        this(SGJNI.new_SGRenderBuffer__SWIG_0(), true);
    }

    private SGRenderBuffer(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGRenderBuffer(SGTextureInternalFormat internalFormat) {
        this(SGJNI.new_SGRenderBuffer__SWIG_1(SGJNI.getData(internalFormat)), true);
    }

    public static long getCPtr(SGRenderBuffer obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGRenderBuffer(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGTextureInternalFormat getInternalFormat() {
        return ((SGTextureInternalFormat[]) SGTextureInternalFormat.class.getEnumConstants())[SGJNI.SGRenderBuffer_getInternalFormat(this.swigCPtr, this)];
    }
}
