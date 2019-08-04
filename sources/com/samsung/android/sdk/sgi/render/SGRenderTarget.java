package com.samsung.android.sdk.sgi.render;

public class SGRenderTarget {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGRenderTarget() {
        this(SGJNI.new_SGRenderTarget(), true);
    }

    protected SGRenderTarget(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGRenderTarget obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGRenderTarget_getHandle(this.swigCPtr, this);
    }

    public void attachRenderBuffer(SGRenderTargetAttachment attachmentType, SGRenderBuffer renderBuffer) {
        SGJNI.SGRenderTarget_attachRenderBuffer(this.swigCPtr, this, SGJNI.getData(attachmentType), SGRenderBuffer.getCPtr(renderBuffer), renderBuffer);
    }

    public void attachTexture2D(SGRenderTargetAttachment attachmentType, SGTexture2DAttachmentProperty attachProperty, int mipLevel) {
        SGJNI.SGRenderTarget_attachTexture2D(this.swigCPtr, this, SGJNI.getData(attachmentType), SGProperty.getCPtr(attachProperty), attachProperty, mipLevel);
    }

    public void detach(SGRenderTargetAttachment attachmentType) {
        SGJNI.SGRenderTarget_detach(this.swigCPtr, this, SGJNI.getData(attachmentType));
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGRenderTarget) && ((SGRenderTarget) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGRenderTarget(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGRenderTargetAntiAliasing getAntiAliasingType() {
        return ((SGRenderTargetAntiAliasing[]) SGRenderTargetAntiAliasing.class.getEnumConstants())[SGJNI.SGRenderTarget_getAntiAliasingType(this.swigCPtr, this)];
    }

    public int getHeight() {
        return SGJNI.SGRenderTarget_getHeight(this.swigCPtr, this);
    }

    public int getWidth() {
        return SGJNI.SGRenderTarget_getWidth(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void setAntiAliasingType(SGRenderTargetAntiAliasing antiAliasingType) {
        SGJNI.SGRenderTarget_setAntiAliasingType(this.swigCPtr, this, SGJNI.getData(antiAliasingType));
    }

    public void setSize(int width, int height) {
        SGJNI.SGRenderTarget_setSize(this.swigCPtr, this, width, height);
    }
}
