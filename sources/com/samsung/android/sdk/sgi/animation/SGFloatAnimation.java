package com.samsung.android.sdk.sgi.animation;

public final class SGFloatAnimation extends SGPropertyAnimation {
    protected SGFloatAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, float value) {
        return SGJNI.SGFloatAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value);
    }

    public float getEndValue() {
        return SGJNI.SGFloatAnimation_getEndValue(this.swigCPtr, this);
    }

    public float getStartValue() {
        return SGJNI.SGFloatAnimation_getStartValue(this.swigCPtr, this);
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGFloatAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndValue(float endValue) {
        SGJNI.SGFloatAnimation_setEndValue(this.swigCPtr, this, endValue);
    }

    public void setStartValue(float startValue) {
        SGJNI.SGFloatAnimation_setStartValue(this.swigCPtr, this, startValue);
    }
}
