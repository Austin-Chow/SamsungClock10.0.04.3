package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

public final class SGLinearValueInterpolator extends SGAnimationValueInterpolator {
    public SGLinearValueInterpolator() {
        this(true);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    protected SGLinearValueInterpolator(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    private SGLinearValueInterpolator(boolean a) {
        this(SGJNI.new_SGLinearValueInterpolator(a), true);
    }

    public float interpolate(float timeProgress, float startValue, float endValue) {
        return SGJNI.SGLinearValueInterpolator_interpolate__SWIG_0(this.swigCPtr, this, timeProgress, startValue, endValue);
    }

    public SGQuaternion interpolate(float timeProgress, SGQuaternion startValue, SGQuaternion endValue) {
        return new SGQuaternion(SGJNI.SGLinearValueInterpolator_interpolate__SWIG_4(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    public SGVector2f interpolate(float timeProgress, SGVector2f startValue, SGVector2f endValue) {
        return new SGVector2f(SGJNI.SGLinearValueInterpolator_interpolate__SWIG_1(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    public SGVector3f interpolate(float timeProgress, SGVector3f startValue, SGVector3f endValue) {
        return new SGVector3f(SGJNI.SGLinearValueInterpolator_interpolate__SWIG_2(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }

    public SGVector4f interpolate(float timeProgress, SGVector4f startValue, SGVector4f endValue) {
        return new SGVector4f(SGJNI.SGLinearValueInterpolator_interpolate__SWIG_3(this.swigCPtr, this, timeProgress, startValue.getData(), endValue.getData()));
    }
}
