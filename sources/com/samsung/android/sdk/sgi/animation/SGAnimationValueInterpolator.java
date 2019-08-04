package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

public abstract class SGAnimationValueInterpolator extends SGAnimationValueInterpolatorHolder {
    SGVector2f endValue2F;
    SGVector3f endValue3F;
    SGVector4f endValue4F;
    SGQuaternion endValueQ;
    SGVector2f startValue2F;
    SGVector3f startValue3F;
    SGVector4f startValue4F;
    SGQuaternion startValueQ;

    protected SGAnimationValueInterpolator() {
        this.startValue2F = new SGVector2f();
        this.endValue2F = new SGVector2f();
        this.startValue3F = new SGVector3f();
        this.endValue3F = new SGVector3f();
        this.startValue4F = new SGVector4f();
        this.endValue4F = new SGVector4f();
        this.startValueQ = new SGQuaternion();
        this.endValueQ = new SGQuaternion();
    }

    SGAnimationValueInterpolator(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public /* bridge */ /* synthetic */ void finalize() {
        super.finalize();
    }

    public abstract float interpolate(float f, float f2, float f3);

    public abstract SGQuaternion interpolate(float f, SGQuaternion sGQuaternion, SGQuaternion sGQuaternion2);

    public abstract SGVector2f interpolate(float f, SGVector2f sGVector2f, SGVector2f sGVector2f2);

    public abstract SGVector3f interpolate(float f, SGVector3f sGVector3f, SGVector3f sGVector3f2);

    public abstract SGVector4f interpolate(float f, SGVector4f sGVector4f, SGVector4f sGVector4f2);

    SGVector2f interpolate2F(float timeProgress, float startValueX, float startValueY, float endValueX, float endValueY) {
        this.startValue2F.set(startValueX, startValueY);
        this.endValue2F.set(endValueX, endValueY);
        return interpolate(timeProgress, this.startValue2F, this.endValue2F);
    }

    SGVector3f interpolate3F(float timeProgress, float startValueX, float startValueY, float startValueZ, float endValueX, float endValueY, float endValueZ) {
        this.startValue3F.set(startValueX, startValueY, startValueZ);
        this.endValue3F.set(endValueX, endValueY, endValueZ);
        return interpolate(timeProgress, this.startValue3F, this.endValue3F);
    }

    SGVector4f interpolate4F(float timeProgress, float startValueX, float startValueY, float startValueZ, float startValueW, float endValueX, float endValueY, float endValueZ, float endValueW) {
        this.startValue4F.set(startValueX, startValueY, startValueZ, startValueW);
        this.endValue4F.set(endValueX, endValueY, endValueZ, endValueW);
        return interpolate(timeProgress, this.startValue4F, this.endValue4F);
    }

    float interpolateF(float timeProgress, float startValue, float endValue) {
        return interpolate(timeProgress, startValue, endValue);
    }

    SGQuaternion interpolateQ(float timeProgress, float startValueX, float startValueY, float startValueZ, float startValueW, float endValueX, float endValueY, float endValueZ, float endValueW) {
        this.startValueQ.set(startValueX, startValueY, startValueZ, startValueW);
        this.endValueQ.set(endValueX, endValueY, endValueZ, endValueW);
        return interpolate(timeProgress, this.startValueQ, this.endValueQ);
    }
}
