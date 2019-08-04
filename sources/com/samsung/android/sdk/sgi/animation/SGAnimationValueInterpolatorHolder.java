package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

abstract class SGAnimationValueInterpolatorHolder {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGAnimationValueInterpolatorHolder() {
        this(SGJNI.new_SGAnimationValueInterpolatorHolder(), true);
        SGJNI.SGAnimationValueInterpolatorHolder_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    protected SGAnimationValueInterpolatorHolder(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGAnimationValueInterpolatorHolder obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGAnimationValueInterpolatorHolder(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public float interpolate(float timeProgress, float startValue, float endValue) {
        return SGJNI.SGAnimationValueInterpolatorHolder_interpolate__SWIG_0(this.swigCPtr, this, timeProgress, startValue, endValue);
    }

    public SGQuaternion interpolate(float timeProgress, SGQuaternion startValue, SGQuaternion endValue) {
        return new SGQuaternion(SGJNI.SGAnimationValueInterpolatorHolder_interpolate__SWIG_4(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    public SGVector2f interpolate(float timeProgress, SGVector2f startValue, SGVector2f endValue) {
        return new SGVector2f(SGJNI.SGAnimationValueInterpolatorHolder_interpolate__SWIG_1(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    public SGVector3f interpolate(float timeProgress, SGVector3f startValue, SGVector3f endValue) {
        return new SGVector3f(SGJNI.SGAnimationValueInterpolatorHolder_interpolate__SWIG_2(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    public SGVector4f interpolate(float timeProgress, SGVector4f startValue, SGVector4f endValue) {
        return new SGVector4f(SGJNI.SGAnimationValueInterpolatorHolder_interpolate__SWIG_3(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    abstract SGVector2f interpolate2F(float f, float f2, float f3, float f4, float f5);

    abstract SGVector3f interpolate3F(float f, float f2, float f3, float f4, float f5, float f6, float f7);

    abstract SGVector4f interpolate4F(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    abstract float interpolateF(float f, float f2, float f3);

    abstract SGQuaternion interpolateQ(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);
}
