package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGVector4f;

public final class SGVector4fAnimation extends SGPropertyAnimation {
    protected SGVector4fAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, SGVector4f value) {
        return SGJNI.SGVector4fAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value.getData());
    }

    public SGVector4f getEndValue() {
        return new SGVector4f(SGJNI.SGVector4fAnimation_getEndValue(this.swigCPtr, this));
    }

    public SGVector4f getStartValue() {
        return new SGVector4f(SGJNI.SGVector4fAnimation_getStartValue(this.swigCPtr, this));
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGVector4fAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(SGVector4f endValue) {
        SGJNI.SGVector4fAnimation_setEndValue(this.swigCPtr, this, endValue.getData());
    }

    public void setStartValue(SGVector4f startValue) {
        SGJNI.SGVector4fAnimation_setStartValue(this.swigCPtr, this, startValue.getData());
    }
}
