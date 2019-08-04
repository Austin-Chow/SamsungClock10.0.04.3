package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGBox3f;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGVector2f;

public final class SGCamera {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGCamera() {
        this(SGJNI.new_SGCamera(), true);
    }

    private SGCamera(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGCamera obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGCamera_getHandle(this.swigCPtr, this);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGCamera) && ((SGCamera) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGCamera(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGBox3f getBoundingBox() {
        return new SGBox3f(SGJNI.SGCamera_getBoundingBox(this.swigCPtr, this));
    }

    public int getClearColor() {
        return SGJNI.SGCamera_getClearColor(this.swigCPtr, this);
    }

    public float getDistance() {
        return SGJNI.SGCamera_getDistance(this.swigCPtr, this);
    }

    public long getObjectVisibilityMask() {
        return SGJNI.SGCamera_getObjectVisibilityMask(this.swigCPtr, this);
    }

    public int getPriority() {
        return SGJNI.SGCamera_getPriority(this.swigCPtr, this);
    }

    public SGMatrix4f getProjection() {
        return new SGMatrix4f(SGJNI.SGCamera_getProjection(this.swigCPtr, this));
    }

    public SGRenderTarget getRenderTarget() {
        long cPtr = SGJNI.SGCamera_getRenderTarget(this.swigCPtr, this);
        return cPtr != 0 ? (SGRenderTarget) SGJNI.createObjectFromNativePtr(SGRenderTarget.class, cPtr, true) : null;
    }

    public SGVector2f getScissorsPosition() {
        return new SGVector2f(SGJNI.SGCamera_getScissorsPosition(this.swigCPtr, this));
    }

    public SGVector2f getScissorsSize() {
        return new SGVector2f(SGJNI.SGCamera_getScissorsSize(this.swigCPtr, this));
    }

    public String getTechnicName() {
        return SGJNI.SGCamera_getTechnicName(this.swigCPtr, this);
    }

    public SGVector2f getViewportPosition() {
        return new SGVector2f(SGJNI.SGCamera_getViewportPosition(this.swigCPtr, this));
    }

    public SGVector2f getViewportSize() {
        return new SGVector2f(SGJNI.SGCamera_getViewportSize(this.swigCPtr, this));
    }

    @Deprecated
    public long getVisibilityMask() {
        return getObjectVisibilityMask();
    }

    public SGMatrix4f getWorldTransformation() {
        return new SGMatrix4f(SGJNI.SGCamera_getWorldTransformation(this.swigCPtr, this));
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public boolean isClearColorEnabled() {
        return SGJNI.SGCamera_isClearColorEnabled(this.swigCPtr, this);
    }

    public boolean isClearDepthEnabled() {
        return SGJNI.SGCamera_isClearDepthEnabled(this.swigCPtr, this);
    }

    public boolean isClearStencilEnabled() {
        return SGJNI.SGCamera_isClearStencilEnabled(this.swigCPtr, this);
    }

    public boolean isFrustumCullingEnabled() {
        return SGJNI.SGCamera_isFrustumCullingEnabled(this.swigCPtr, this);
    }

    public boolean isScissorsEnabled() {
        return SGJNI.SGCamera_isScissorsEnabled(this.swigCPtr, this);
    }

    public void setClearColor(int color) {
        SGJNI.SGCamera_setClearColor(this.swigCPtr, this, color);
    }

    public void setClearColorEnabled(boolean enabled) {
        SGJNI.SGCamera_setClearColorEnabled(this.swigCPtr, this, enabled);
    }

    public void setClearDepth(boolean enabled) {
        SGJNI.SGCamera_setClearDepth(this.swigCPtr, this, enabled);
    }

    @Deprecated
    public void setClearDepthEnabled(boolean enabled) {
        setClearDepth(enabled);
    }

    public void setClearStencil(boolean enabled) {
        SGJNI.SGCamera_setClearStencil(this.swigCPtr, this, enabled);
    }

    @Deprecated
    public void setClearStencilEnabled(boolean enabled) {
        setClearStencil(enabled);
    }

    public void setFrustumCulling(boolean enabled) {
        SGJNI.SGCamera_setFrustumCulling(this.swigCPtr, this, enabled);
    }

    @Deprecated
    public void setFrustumCullingEnabled(boolean enabled) {
        setFrustumCulling(enabled);
    }

    public void setObjectVisibilityMask(long mask) {
        SGJNI.SGCamera_setObjectVisibilityMask(this.swigCPtr, this, mask);
    }

    public void setPriority(int priority) {
        SGJNI.SGCamera_setPriority(this.swigCPtr, this, priority);
    }

    public void setProjection(SGMatrix4f projection) {
        SGJNI.SGCamera_setProjection(this.swigCPtr, this, projection.getData());
    }

    public void setRenderTarget(SGRenderTarget renderTarget) {
        SGJNI.SGCamera_setRenderTarget(this.swigCPtr, this, SGRenderTarget.getCPtr(renderTarget), renderTarget);
    }

    public void setScissors(boolean enabled) {
        SGJNI.SGCamera_setScissors(this.swigCPtr, this, enabled);
    }

    public void setScissorsRect(SGVector2f position, SGVector2f size) {
        SGJNI.SGCamera_setScissorsRect(this.swigCPtr, this, position.getData(), size.getData());
    }

    public void setTechnicName(String name) {
        SGJNI.SGCamera_setTechnicName(this.swigCPtr, this, name);
    }

    public void setViewport(SGVector2f position, SGVector2f size) {
        SGJNI.SGCamera_setViewport(this.swigCPtr, this, position.getData(), size.getData());
    }

    @Deprecated
    public void setVisibilityMask(long mask) {
        setObjectVisibilityMask(mask);
    }

    public void setWorldTransformation(SGMatrix4f worldTransformation) {
        SGJNI.SGCamera_setWorldTransformation(this.swigCPtr, this, worldTransformation.getData());
    }
}
