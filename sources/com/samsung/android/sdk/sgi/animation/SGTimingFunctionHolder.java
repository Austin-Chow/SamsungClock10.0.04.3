package com.samsung.android.sdk.sgi.animation;

final class SGTimingFunctionHolder extends SGAnimationTimingFunction {
    private SGTimingFunctionHolder(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public float getInterpolationTime(float time) {
        return SGJNI.SGAnimationTimingFunction_getInterpolationTime(this.swigCPtr, this, time);
    }
}
