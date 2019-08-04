package com.samsung.android.sdk.sgi.animation;

public final class SGClipBoneParams extends SGAnimationClip {
    protected SGClipBoneParams(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGClipBoneParams(String name, int defaultDuration) {
        this(SGJNI.new_SGClipBoneParams(name, defaultDuration), true);
    }

    public boolean addKeyFrame(float timeProgress, SGBoneParams value) {
        return SGJNI.SGClipBoneParams_addKeyFrame(this.swigCPtr, this, timeProgress, SGBoneParams.getCPtr(value), value);
    }

    public void complete() {
        SGJNI.SGClipBoneParams_complete(this.swigCPtr, this);
    }

    public boolean findKeyFrame(float key) {
        return SGJNI.SGClipBoneParams_findKeyFrame(this.swigCPtr, this, key);
    }

    public SGBoneParams getKeyFrame(float key) {
        return new SGBoneParams(SGJNI.SGClipBoneParams_getKeyFrame(this.swigCPtr, this, key), true);
    }

    public boolean removeKeyFrame(float key) {
        return SGJNI.SGClipBoneParams_removeKeyFrame(this.swigCPtr, this, key);
    }
}
