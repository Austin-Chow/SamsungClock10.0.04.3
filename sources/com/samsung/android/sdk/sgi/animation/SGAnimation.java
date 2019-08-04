package com.samsung.android.sdk.sgi.animation;

public class SGAnimation {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGAnimation(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGAnimation obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGAnimation_getHandle(this.swigCPtr, this);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGAnimation) && ((SGAnimation) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGAnimation(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGAnimationListener getAnimationListener() {
        SGAnimationListenerHolder holder = SGJNI.SGAnimation_getAnimationListener(this.swigCPtr, this);
        return holder != null ? holder.getInterface() : null;
    }

    public SGAnimationDirectionType getDirection() {
        return ((SGAnimationDirectionType[]) SGAnimationDirectionType.class.getEnumConstants())[SGJNI.SGAnimation_getDirection(this.swigCPtr, this)];
    }

    public int getDuration() {
        return SGJNI.SGAnimation_getDuration(this.swigCPtr, this);
    }

    public SGFillAfterMode getFillAfterMode() {
        return ((SGFillAfterMode[]) SGFillAfterMode.class.getEnumConstants())[SGJNI.SGAnimation_getFillAfterMode(this.swigCPtr, this)];
    }

    public int getFpsLimit() {
        return SGJNI.SGAnimation_getFpsLimit(this.swigCPtr, this);
    }

    public int getOffset() {
        return SGJNI.SGAnimation_getOffset(this.swigCPtr, this);
    }

    public SGSuspendBehaviour getOnSuspendBehaviour() {
        return ((SGSuspendBehaviour[]) SGSuspendBehaviour.class.getEnumConstants())[SGJNI.SGAnimation_getOnSuspendBehaviour(this.swigCPtr, this)];
    }

    public int getRepeatCount() {
        return SGJNI.SGAnimation_getRepeatCount(this.swigCPtr, this);
    }

    public SGAnimationTimingFunction getTimingFunction() {
        return SGJNI.SGAnimation_getTimingFunction(this.swigCPtr, this);
    }

    public SGAnimationValueInterpolator getValueInterpolator() {
        return SGJNI.SGAnimation_getValueInterpolator(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public boolean isAutoReverseEnabled() {
        return SGJNI.SGAnimation_isAutoReverseEnabled(this.swigCPtr, this);
    }

    public boolean isDeferredStartEnabled() {
        return SGJNI.SGAnimation_isDeferredStartEnabled(this.swigCPtr, this);
    }

    public boolean isFillBeforeEnabled() {
        return SGJNI.SGAnimation_isFillBeforeEnabled(this.swigCPtr, this);
    }

    public boolean isSynchronizedStartEnabled() {
        return SGJNI.SGAnimation_isSynchronizedStartEnabled(this.swigCPtr, this);
    }

    public void setAnimationListener(SGAnimationListener listener) {
        SGAnimationListenerHolder holder = null;
        if (listener != null) {
            holder = SGJNI.SGAnimation_getAnimationListener(this.swigCPtr, this);
            if (holder != null) {
                holder.setInterface(listener);
                return;
            }
            holder = new SGAnimationListenerHolder(listener);
        }
        SGJNI.SGAnimation_setAnimationListener(this.swigCPtr, this, SGAnimationListenerBase.getCPtr(holder), holder);
    }

    public void setAutoReverseEnabled(boolean enabled) {
        SGJNI.SGAnimation_setAutoReverseEnabled(this.swigCPtr, this, enabled);
    }

    public void setDeferredStartEnabled(boolean enabled) {
        SGJNI.SGAnimation_setDeferredStartEnabled(this.swigCPtr, this, enabled);
    }

    public void setDirection(SGAnimationDirectionType direction) {
        SGJNI.SGAnimation_setDirection(this.swigCPtr, this, SGJNI.getData(direction));
    }

    public void setDuration(int duration) {
        SGJNI.SGAnimation_setDuration(this.swigCPtr, this, duration);
    }

    public void setFillAfterMode(SGFillAfterMode fillAfterMode) {
        SGJNI.SGAnimation_setFillAfterMode(this.swigCPtr, this, SGJNI.getData(fillAfterMode));
    }

    public void setFillBeforeEnabled(boolean enabled) {
        SGJNI.SGAnimation_setFillBeforeEnabled(this.swigCPtr, this, enabled);
    }

    public void setFpsLimit(int aFpsLimit) {
        SGJNI.SGAnimation_setFpsLimit(this.swigCPtr, this, aFpsLimit);
    }

    public void setOffset(int offset) {
        SGJNI.SGAnimation_setOffset(this.swigCPtr, this, offset);
    }

    public void setOnSuspendBehaviour(SGSuspendBehaviour suspendBehaviour) {
        SGJNI.SGAnimation_setOnSuspendBehaviour(this.swigCPtr, this, SGJNI.getData(suspendBehaviour));
    }

    public void setRepeatCount(int repeatCount) {
        SGJNI.SGAnimation_setRepeatCount(this.swigCPtr, this, repeatCount);
    }

    public void setSynchronizedStartEnabled(boolean enabled) {
        SGJNI.SGAnimation_setSynchronizedStartEnabled(this.swigCPtr, this, enabled);
    }

    public void setTimingFunction(SGAnimationTimingFunction timingFunction) {
        SGJNI.SGAnimation_setTimingFunction(this.swigCPtr, this, SGAnimationTimingFunction.getCPtr(timingFunction), timingFunction);
    }

    public void setValueInterpolator(SGAnimationValueInterpolator interpolator) {
        SGJNI.SGAnimation_setValueInterpolator(this.swigCPtr, this, SGAnimationValueInterpolatorHolder.getCPtr(interpolator));
    }
}
