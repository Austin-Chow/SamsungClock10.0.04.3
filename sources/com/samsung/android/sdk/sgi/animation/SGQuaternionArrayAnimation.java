package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGQuaternion;

public final class SGQuaternionArrayAnimation extends SGPropertyAnimation {
    protected SGQuaternionArrayAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, SGQuaternion[] value) {
        return SGJNI.SGQuaternionArrayAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value);
    }

    public SGQuaternion[] getEndValue() {
        return SGJNI.SGQuaternionArrayAnimation_getEndValue(this.swigCPtr, this);
    }

    public SGQuaternion[] getStartValue() {
        return SGJNI.SGQuaternionArrayAnimation_getStartValue(this.swigCPtr, this);
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGQuaternionArrayAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(SGQuaternion[] endValue) {
        SGJNI.SGQuaternionArrayAnimation_setEndValue(this.swigCPtr, this, endValue);
    }

    public void setStartValue(SGQuaternion[] startValue) {
        SGJNI.SGQuaternionArrayAnimation_setStartValue(this.swigCPtr, this, startValue);
    }
}
