package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGVector2f;

public final class SGSpriteAnimation extends SGAnimation {
    protected SGSpriteAnimation(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean addKeyFrame(float timeProgress, int value) {
        return SGJNI.SGSpriteAnimation_addKeyFrame(this.swigCPtr, this, timeProgress, value);
    }

    public int getEndIndex() {
        return SGJNI.SGSpriteAnimation_getEndIndex(this.swigCPtr, this);
    }

    public SGVector2f getFrameSize() {
        return new SGVector2f(SGJNI.SGSpriteAnimation_getFrameSize(this.swigCPtr, this));
    }

    public SGVector2f getSourceSize() {
        return new SGVector2f(SGJNI.SGSpriteAnimation_getSourceSize(this.swigCPtr, this));
    }

    public int getStartIndex() {
        return SGJNI.SGSpriteAnimation_getStartIndex(this.swigCPtr, this);
    }

    public boolean removeKeyFrame(float timeProgress) {
        return SGJNI.SGSpriteAnimation_removeKeyFrame(this.swigCPtr, this, timeProgress);
    }

    public void setEndIndex(int index) {
        SGJNI.SGSpriteAnimation_setEndIndex(this.swigCPtr, this, index);
    }

    public void setFrameSize(float width, float height) {
        SGJNI.SGSpriteAnimation_setFrameSize(this.swigCPtr, this, width, height);
    }

    public void setSourceSize(float width, float height) {
        SGJNI.SGSpriteAnimation_setSourceSize(this.swigCPtr, this, width, height);
    }

    public void setStartIndex(int index) {
        SGJNI.SGSpriteAnimation_setStartIndex(this.swigCPtr, this, index);
    }
}
