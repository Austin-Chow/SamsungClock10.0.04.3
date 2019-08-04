package com.samsung.android.sdk.sgi.animation;

public final class SGSkeletalAnimation extends SGPropertyAnimation {
    protected SGSkeletalAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public void disableBone(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Negative index argument");
        }
        SGJNI.SGSkeletalAnimation_disableBone(this.swigCPtr, this, index);
    }

    public void enableBone(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Negative index argument");
        }
        SGJNI.SGSkeletalAnimation_enableBone(this.swigCPtr, this, index);
    }

    public int getAnimationToBlendId() {
        return SGJNI.SGSkeletalAnimation_getAnimationToBlendId(this.swigCPtr, this);
    }

    public float getBoneWeight(int index) {
        if (index >= 0) {
            return SGJNI.SGSkeletalAnimation_getBoneWeight(this.swigCPtr, this, index);
        }
        throw new IllegalArgumentException("Negative index argument");
    }

    public float[] getBonesWeights() {
        return SGJNI.SGSkeletalAnimation_getBonesWeights(this.swigCPtr, this);
    }

    public int getTimeToBlend() {
        return SGJNI.SGSkeletalAnimation_getTimeToBlend(this.swigCPtr, this);
    }

    public float getWeight() {
        return SGJNI.SGSkeletalAnimation_getWeight(this.swigCPtr, this);
    }

    public boolean isBoneEnabled(int index) {
        if (index >= 0) {
            return SGJNI.SGSkeletalAnimation_isBoneEnabled(this.swigCPtr, this, index);
        }
        throw new IllegalArgumentException("Negative index argument");
    }

    public void setAnimationToBlend(int id, int time) {
        SGJNI.SGSkeletalAnimation_setAnimationToBlend(this.swigCPtr, this, id, time);
    }

    public void setBoneWeight(int index, float weight) {
        if (index < 0) {
            throw new IllegalArgumentException("Negative index argument");
        }
        SGJNI.SGSkeletalAnimation_setBoneWeight(this.swigCPtr, this, index, weight);
    }

    public void setWeight(float weight) {
        SGJNI.SGSkeletalAnimation_setWeight(this.swigCPtr, this, weight);
    }
}
