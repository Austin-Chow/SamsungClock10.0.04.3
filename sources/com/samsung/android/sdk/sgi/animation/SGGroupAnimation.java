package com.samsung.android.sdk.sgi.animation;

public final class SGGroupAnimation extends SGAnimation {
    protected SGGroupAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public int addAnimation(SGAnimation animation) {
        return SGJNI.SGGroupAnimation_addAnimation(this.swigCPtr, this, SGAnimation.getCPtr(animation), animation);
    }

    public SGAnimation getAnimation(int index) {
        return SGJNI.SGGroupAnimation_getAnimation(this.swigCPtr, this, index);
    }

    public int getAnimationCount() {
        return SGJNI.SGGroupAnimation_getAnimationCount(this.swigCPtr, this);
    }

    public int insertAnimation(int index, SGAnimation animation) {
        return SGJNI.SGGroupAnimation_insertAnimation(this.swigCPtr, this, index, SGAnimation.getCPtr(animation), animation);
    }

    public boolean isParallel() {
        return SGJNI.SGGroupAnimation_isParallel(this.swigCPtr, this);
    }

    public void removeAllAnimations() {
        SGJNI.SGGroupAnimation_removeAllAnimations(this.swigCPtr, this);
    }

    public boolean removeAnimation(int index) {
        return SGJNI.SGGroupAnimation_removeAnimation(this.swigCPtr, this, index);
    }
}
