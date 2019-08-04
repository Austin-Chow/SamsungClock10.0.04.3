package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;

public abstract class SGAnimationTimingFunction {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGAnimationTimingFunction() {
        this(SGJNI.new_SGAnimationTimingFunction(), true);
        SGJNI.SGAnimationTimingFunction_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    protected SGAnimationTimingFunction(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGAnimationTimingFunction obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGAnimationTimingFunction(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public abstract float getInterpolationTime(float f);
}
