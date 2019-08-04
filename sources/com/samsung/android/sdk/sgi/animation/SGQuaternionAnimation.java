package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGQuaternion;

public final class SGQuaternionAnimation extends SGPropertyAnimation {
    protected SGQuaternionAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, SGQuaternion value) {
        return SGJNI.SGQuaternionAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value.getData());
    }

    public SGQuaternion getEndValue() {
        return new SGQuaternion(SGJNI.SGQuaternionAnimation_getEndValue(this.swigCPtr, this));
    }

    public SGQuaternion getStartValue() {
        return new SGQuaternion(SGJNI.SGQuaternionAnimation_getStartValue(this.swigCPtr, this));
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGQuaternionAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(SGQuaternion endValue) {
        SGJNI.SGQuaternionAnimation_setEndValue(this.swigCPtr, this, endValue.getData());
    }

    public void setStartValue(SGQuaternion startValue) {
        SGJNI.SGQuaternionAnimation_setStartValue(this.swigCPtr, this, startValue.getData());
    }
}
