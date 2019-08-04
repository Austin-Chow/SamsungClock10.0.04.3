package com.samsung.android.sdk.sgi.animation;

public final class SGPoseAnimationClip extends SGAnimationClip {
    protected SGPoseAnimationClip(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGPoseAnimationClip(String name, int defaultDuration) {
        this(SGJNI.new_SGPoseAnimationClip(name, defaultDuration), true);
    }

    public boolean addKeyFrame(float key, float value) {
        return SGJNI.SGPoseAnimationClip_addKeyFrame(this.swigCPtr, this, key, value);
    }

    public void complete() {
        SGJNI.SGPoseAnimationClip_complete(this.swigCPtr, this);
    }

    public boolean findKeyFrame(float key) {
        return SGJNI.SGPoseAnimationClip_findKeyFrame(this.swigCPtr, this, key);
    }

    public float getKeyFrame(float key) {
        return SGJNI.SGPoseAnimationClip_getKeyFrame(this.swigCPtr, this, key);
    }

    public boolean isCompleted() {
        return SGJNI.SGPoseAnimationClip_isCompleted(this.swigCPtr, this);
    }

    public boolean removeKeyFrame(float key) {
        return SGJNI.SGPoseAnimationClip_removeKeyFrame(this.swigCPtr, this, key);
    }
}
