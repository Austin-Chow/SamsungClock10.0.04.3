package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGRay;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGRenderTarget;

public class SGLayerCamera extends SGLayer {
    public SGLayerCamera() {
        this(SGJNI.new_SGLayerCamera(), true);
    }

    protected SGLayerCamera(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public void enableClearColorPremultiply(boolean value) {
        SGJNI.SGLayerCamera_enableClearColorPremultiply(this.swigCPtr, this, value);
    }

    public float getAspect() {
        return SGJNI.SGLayerCamera_getAspect(this.swigCPtr, this);
    }

    public float getBottom() {
        return SGJNI.SGLayerCamera_getBottom(this.swigCPtr, this);
    }

    public int getClearColor() {
        return SGJNI.SGLayerCamera_getClearColor(this.swigCPtr, this);
    }

    public int getClearStencilValue() {
        return SGJNI.SGLayerCamera_getClearStencilValue(this.swigCPtr, this);
    }

    public float getDistance() {
        return SGJNI.SGLayerCamera_getDistance(this.swigCPtr, this);
    }

    public float getFovY() {
        return SGJNI.SGLayerCamera_getFovY(this.swigCPtr, this);
    }

    public float getLeft() {
        return SGJNI.SGLayerCamera_getLeft(this.swigCPtr, this);
    }

    public int getPriority() {
        return SGJNI.SGLayerCamera_getPriority(this.swigCPtr, this);
    }

    public SGMatrix4f getProjection() {
        return new SGMatrix4f(SGJNI.SGLayerCamera_getProjection(this.swigCPtr, this));
    }

    public SGRay getRay(SGVector2f screenPoint) {
        return SGJNI.SGLayerCamera_getRay(this.swigCPtr, this, screenPoint.getData());
    }

    public SGRenderTarget getRenderTarget() {
        long cPtr = SGJNI.SGLayerCamera_getRenderTarget(this.swigCPtr, this);
        return cPtr != 0 ? (SGRenderTarget) SGJNI.createObjectFromNativePtr(SGRenderTarget.class, cPtr, true) : null;
    }

    public float getRight() {
        return SGJNI.SGLayerCamera_getRight(this.swigCPtr, this);
    }

    public SGVector2f getScissorsPosition() {
        return new SGVector2f(SGJNI.SGLayerCamera_getScissorsPosition(this.swigCPtr, this));
    }

    public SGVector2f getScissorsSize() {
        return new SGVector2f(SGJNI.SGLayerCamera_getScissorsSize(this.swigCPtr, this));
    }

    public SGSurfaceAlphaApplyMode getSurfaceAlphaApplyMode() {
        return ((SGSurfaceAlphaApplyMode[]) SGSurfaceAlphaApplyMode.class.getEnumConstants())[SGJNI.SGLayerCamera_getSurfaceAlphaApplyMode(this.swigCPtr, this)];
    }

    public String getTechnicName() {
        return SGJNI.SGLayerCamera_getTechnicName(this.swigCPtr, this);
    }

    public float getTop() {
        return SGJNI.SGLayerCamera_getTop(this.swigCPtr, this);
    }

    public SGVector2f getViewportPosition() {
        return new SGVector2f(SGJNI.SGLayerCamera_getViewportPosition(this.swigCPtr, this));
    }

    public SGVector2f getViewportSize() {
        return new SGVector2f(SGJNI.SGLayerCamera_getViewportSize(this.swigCPtr, this));
    }

    public float getZFar() {
        return SGJNI.SGLayerCamera_getZFar(this.swigCPtr, this);
    }

    public float getZNear() {
        return SGJNI.SGLayerCamera_getZNear(this.swigCPtr, this);
    }

    public boolean isClearColorEnabled() {
        return SGJNI.SGLayerCamera_isClearColorEnabled(this.swigCPtr, this);
    }

    public boolean isClearColorPremultiply() {
        return SGJNI.SGLayerCamera_isClearColorPremultiply(this.swigCPtr, this);
    }

    public boolean isClearDepthEnabled() {
        return SGJNI.SGLayerCamera_isClearDepthEnabled(this.swigCPtr, this);
    }

    public boolean isClearStencilEnabled() {
        return SGJNI.SGLayerCamera_isClearStencilEnabled(this.swigCPtr, this);
    }

    public boolean isFrustumCullingEnabled() {
        return SGJNI.SGLayerCamera_isFrustumCullingEnabled(this.swigCPtr, this);
    }

    public boolean isScissorsEnabled() {
        return SGJNI.SGLayerCamera_isScissorsEnabled(this.swigCPtr, this);
    }

    public void setAspect(float value) {
        SGJNI.SGLayerCamera_setAspect(this.swigCPtr, this, value);
    }

    public void setBottom(float value) {
        SGJNI.SGLayerCamera_setBottom(this.swigCPtr, this, value);
    }

    public void setClearColor(int color) {
        SGJNI.SGLayerCamera_setClearColor(this.swigCPtr, this, color);
    }

    public void setClearColorEnabled(boolean enabled) {
        SGJNI.SGLayerCamera_setClearColorEnabled(this.swigCPtr, this, enabled);
    }

    public void setClearDepth(boolean enabled) {
        SGJNI.SGLayerCamera_setClearDepth(this.swigCPtr, this, enabled);
    }

    public void setClearStencil(boolean enabled) {
        SGJNI.SGLayerCamera_setClearStencil(this.swigCPtr, this, enabled);
    }

    public void setClearStencilValue(int clearValue) {
        SGJNI.SGLayerCamera_setClearStencilValue(this.swigCPtr, this, clearValue);
    }

    public void setFovY(float value) {
        SGJNI.SGLayerCamera_setFovY(this.swigCPtr, this, value);
    }

    public void setFrustumCulling(boolean enabled) {
        SGJNI.SGLayerCamera_setFrustumCulling(this.swigCPtr, this, enabled);
    }

    public void setLeft(float value) {
        SGJNI.SGLayerCamera_setLeft(this.swigCPtr, this, value);
    }

    public void setPriority(int priority) {
        SGJNI.SGLayerCamera_setPriority(this.swigCPtr, this, priority);
    }

    public void setProjection(SGMatrix4f projection) {
        SGJNI.SGLayerCamera_setProjection(this.swigCPtr, this, projection.getData());
    }

    public void setRenderTarget(SGRenderTarget renderTarget) {
        SGJNI.SGLayerCamera_setRenderTarget(this.swigCPtr, this, SGRenderTarget.getCPtr(renderTarget), renderTarget);
    }

    public void setRight(float value) {
        SGJNI.SGLayerCamera_setRight(this.swigCPtr, this, value);
    }

    public void setScissors(boolean enabled) {
        SGJNI.SGLayerCamera_setScissors(this.swigCPtr, this, enabled);
    }

    public void setScissorsRect(SGVector2f position, SGVector2f size) {
        SGJNI.SGLayerCamera_setScissorsRect(this.swigCPtr, this, position.getData(), size.getData());
    }

    public void setSurfaceAlphaApplyMode(SGSurfaceAlphaApplyMode value) {
        SGJNI.SGLayerCamera_setSurfaceAlphaApplyMode(this.swigCPtr, this, SGJNI.getData(value));
    }

    public void setTechnicName(String name) {
        SGJNI.SGLayerCamera_setTechnicName(this.swigCPtr, this, name);
    }

    public void setTop(float value) {
        SGJNI.SGLayerCamera_setTop(this.swigCPtr, this, value);
    }

    public void setViewport(SGVector2f position, SGVector2f size) {
        SGJNI.SGLayerCamera_setViewport(this.swigCPtr, this, position.getData(), size.getData());
    }

    public void setZFar(float value) {
        SGJNI.SGLayerCamera_setZFar(this.swigCPtr, this, value);
    }

    public void setZNear(float value) {
        SGJNI.SGLayerCamera_setZNear(this.swigCPtr, this, value);
    }
}
