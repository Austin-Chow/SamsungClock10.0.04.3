package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGVector2f;

public final class SGVector2fAnimation extends SGPropertyAnimation {
    protected SGVector2fAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, SGVector2f value) {
        return SGJNI.SGVector2fAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value.getData());
    }

    public SGVector2f getEndValue() {
        return new SGVector2f(SGJNI.SGVector2fAnimation_getEndValue(this.swigCPtr, this));
    }

    public SGVector2f getStartValue() {
        return new SGVector2f(SGJNI.SGVector2fAnimation_getStartValue(this.swigCPtr, this));
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGVector2fAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(SGVector2f endValue) {
        SGJNI.SGVector2fAnimation_setEndValue(this.swigCPtr, this, endValue.getData());
    }

    public void setStartValue(SGVector2f startValue) {
        SGJNI.SGVector2fAnimation_setStartValue(this.swigCPtr, this, startValue.getData());
    }
}
