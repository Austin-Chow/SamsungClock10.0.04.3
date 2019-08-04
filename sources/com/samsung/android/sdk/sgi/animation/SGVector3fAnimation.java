package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGVector3f;

public final class SGVector3fAnimation extends SGPropertyAnimation {
    protected SGVector3fAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, SGVector3f value) {
        return SGJNI.SGVector3fAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value.getData());
    }

    public SGVector3f getEndValue() {
        return new SGVector3f(SGJNI.SGVector3fAnimation_getEndValue(this.swigCPtr, this));
    }

    public SGVector3f getStartValue() {
        return new SGVector3f(SGJNI.SGVector3fAnimation_getStartValue(this.swigCPtr, this));
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGVector3fAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(SGVector3f endValue) {
        SGJNI.SGVector3fAnimation_setEndValue(this.swigCPtr, this, endValue.getData());
    }

    public void setStartValue(SGVector3f startValue) {
        SGJNI.SGVector3fAnimation_setStartValue(this.swigCPtr, this, startValue.getData());
    }
}
