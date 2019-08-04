package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.render.SGProperty;

public final class SGTransitionAnimation extends SGAnimation {
    protected SGTransitionAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public void enableAlphaBlending(boolean enabled) {
        SGJNI.SGTransitionAnimation_enableAlphaBlending(this.swigCPtr, this, enabled);
    }

    public SGTransitionDirectionType getTransitionDirection() {
        return ((SGTransitionDirectionType[]) SGTransitionDirectionType.class.getEnumConstants())[SGJNI.SGTransitionAnimation_getTransitionDirection(this.swigCPtr, this)];
    }

    public SGTransitionType getTransitionType() {
        return ((SGTransitionType[]) SGTransitionType.class.getEnumConstants())[SGJNI.SGTransitionAnimation_getTransitionType(this.swigCPtr, this)];
    }

    public boolean isAlphaBlendingEnabled() {
        return SGJNI.SGTransitionAnimation_isAlphaBlendingEnabled(this.swigCPtr, this);
    }

    public boolean isSynchronizedStartEnabled() {
        return SGJNI.SGTransitionAnimation_isSynchronizedStartEnabled(this.swigCPtr, this);
    }

    public void overrideSourceTexture(SGProperty property) {
        SGJNI.SGTransitionAnimation_overrideSourceTexture(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }

    public void overrideTargetTexture(SGProperty property) {
        SGJNI.SGTransitionAnimation_overrideTargetTexture(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }

    public void setSynchronizedStartEnabled(boolean enabled) {
        SGJNI.SGTransitionAnimation_setSynchronizedStartEnabled(this.swigCPtr, this, enabled);
    }
}
