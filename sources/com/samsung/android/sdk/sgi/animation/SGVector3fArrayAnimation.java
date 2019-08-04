package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGVector3f;

public final class SGVector3fArrayAnimation extends SGPropertyAnimation {
    protected SGVector3fArrayAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, SGVector3f[] value) {
        return SGJNI.SGVector3fArrayAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value);
    }

    public SGVector3f[] getEndValue() {
        return SGJNI.SGVector3fArrayAnimation_getEndValue(this.swigCPtr, this);
    }

    public SGVector3f[] getStartValue() {
        return SGJNI.SGVector3fArrayAnimation_getStartValue(this.swigCPtr, this);
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGVector3fArrayAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(SGVector3f[] endValue) {
        SGJNI.SGVector3fArrayAnimation_setEndValue(this.swigCPtr, this, endValue);
    }

    public void setStartValue(SGVector3f[] startValue) {
        SGJNI.SGVector3fArrayAnimation_setStartValue(this.swigCPtr, this, startValue);
    }
}
