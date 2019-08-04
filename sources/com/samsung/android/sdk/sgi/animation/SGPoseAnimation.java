package com.samsung.android.sdk.sgi.animation;

public final class SGPoseAnimation extends SGPropertyAnimation {
    protected SGPoseAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGPoseAnimationClip getClip(int index) {
        return new SGPoseAnimationClip(SGJNI.SGPoseAnimation_getClip__SWIG_1(this.swigCPtr, this, index), true);
    }

    public SGPoseAnimationClip getClip(String name) {
        long cPtr = SGJNI.SGPoseAnimation_getClip__SWIG_0(this.swigCPtr, this, name);
        return cPtr != 0 ? (SGPoseAnimationClip) SGJNI.createObjectFromNativePtr(SGPoseAnimationClip.class, cPtr, true) : null;
    }

    public int getClipsCount() {
        return SGJNI.SGPoseAnimation_getClipsCount(this.swigCPtr, this);
    }

    public String getTargetName(int index) {
        return SGJNI.SGPoseAnimation_getTargetName(this.swigCPtr, this, index);
    }

    public int getTimeToBlend() {
        return SGJNI.SGPoseAnimation_getTimeToBlend(this.swigCPtr, this);
    }

    public void setClip(String name, SGPoseAnimationClip clip) {
        SGJNI.SGPoseAnimation_setClip(this.swigCPtr, this, name, SGAnimationClip.getCPtr(clip), clip);
    }

    public void setTimeToBlend(int duration) {
        SGJNI.SGPoseAnimation_setTimeToBlend(this.swigCPtr, this, duration);
    }
}
