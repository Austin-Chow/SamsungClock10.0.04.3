package com.samsung.android.sdk.sgi.animation;

public final class SGAnimationTransaction {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGAnimationTransaction() {
        this(SGJNI.new_SGAnimationTransaction(), true);
    }

    protected SGAnimationTransaction(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGAnimationTransaction obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public static int getDefaultDuration() {
        return SGJNI.SGAnimationTransaction_getDefaultDuration();
    }

    public static boolean getDefaultEnabled() {
        return SGJNI.SGAnimationTransaction_getDefaultEnabled();
    }

    public static int getDefaultOffset() {
        return SGJNI.SGAnimationTransaction_getDefaultOffset();
    }

    public static SGSuspendBehaviour getDefaultOnSuspendBehaviour() {
        return ((SGSuspendBehaviour[]) SGSuspendBehaviour.class.getEnumConstants())[SGJNI.SGAnimationTransaction_getDefaultOnSuspendBehaviour()];
    }

    public static SGAnimationTimingFunction getDefaultTimingFunction() {
        return SGJNI.SGAnimationTransaction_getDefaultTimingFunction();
    }

    public static SGAnimationValueInterpolator getDefaultValueInterpolator() {
        return SGJNI.SGAnimationTransaction_getDefaultValueInterpolator();
    }

    private long getHandle() {
        return SGJNI.SGAnimationTransaction_getHandle(this.swigCPtr, this);
    }

    public static boolean isDefaultDeferredStartEnabled() {
        return SGJNI.SGAnimationTransaction_isDefaultDeferredStartEnabled();
    }

    public static boolean isDefaultSynchronizedStartEnabled() {
        return SGJNI.SGAnimationTransaction_isDefaultSynchronizedStartEnabled();
    }

    public static void setDefaultDeferredStartEnabled(boolean enabled) {
        SGJNI.SGAnimationTransaction_setDefaultDeferredStartEnabled(enabled);
    }

    public static void setDefaultDuration(int duration) {
        SGJNI.SGAnimationTransaction_setDefaultDuration(duration);
    }

    public static void setDefaultEnabled(boolean enabled) {
        SGJNI.SGAnimationTransaction_setDefaultEnabled(enabled);
    }

    public static void setDefaultOffset(int durationOffset) {
        SGJNI.SGAnimationTransaction_setDefaultOffset(durationOffset);
    }

    public static void setDefaultOnSuspendBehaviour(SGSuspendBehaviour suspendBehaviour) {
        SGJNI.SGAnimationTransaction_setDefaultOnSuspendBehaviour(SGJNI.getData(suspendBehaviour));
    }

    public static void setDefaultSynchronizedStartEnabled(boolean enabled) {
        SGJNI.SGAnimationTransaction_setDefaultSynchronizedStartEnabled(enabled);
    }

    public static void setDefaultTimingFunction(SGAnimationTimingFunction defaultTimingFunction) {
        SGJNI.SGAnimationTransaction_setDefaultTimingFunction(SGAnimationTimingFunction.getCPtr(defaultTimingFunction), defaultTimingFunction);
    }

    public static void setDefaultValueInterpolator(SGAnimationValueInterpolator valueInterpolator) {
        SGJNI.SGAnimationTransaction_setDefaultValueInterpolator(SGAnimationValueInterpolatorHolder.getCPtr(valueInterpolator));
    }

    public boolean begin() {
        return SGJNI.SGAnimationTransaction_begin(this.swigCPtr, this);
    }

    public boolean end() {
        return SGJNI.SGAnimationTransaction_end(this.swigCPtr, this);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGAnimationTransaction) && ((SGAnimationTransaction) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGAnimationTransaction(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGAnimationListener getAnimationListener() {
        SGAnimationListenerHolder holder = SGJNI.SGAnimationTransaction_getAnimationListener(this.swigCPtr, this);
        return holder != null ? holder.getInterface() : null;
    }

    public int getCurrentAnimationId() {
        return SGJNI.SGAnimationTransaction_getCurrentAnimationId(this.swigCPtr, this);
    }

    public int getDuration() {
        return SGJNI.SGAnimationTransaction_getDuration(this.swigCPtr, this);
    }

    public int getOffset() {
        return SGJNI.SGAnimationTransaction_getOffset(this.swigCPtr, this);
    }

    public SGSuspendBehaviour getOnSuspendBehaviour() {
        return ((SGSuspendBehaviour[]) SGSuspendBehaviour.class.getEnumConstants())[SGJNI.SGAnimationTransaction_getOnSuspendBehaviour(this.swigCPtr, this)];
    }

    public SGAnimationTimingFunction getTimingFunction() {
        return SGJNI.SGAnimationTransaction_getTimingFunction(this.swigCPtr, this);
    }

    public SGAnimationValueInterpolator getValueInterpolator() {
        return SGJNI.SGAnimationTransaction_getValueInterpolator(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public boolean isDeferredStartEnabled() {
        return SGJNI.SGAnimationTransaction_isDeferredStartEnabled(this.swigCPtr, this);
    }

    public boolean isSynchronizedStartEnabled() {
        return SGJNI.SGAnimationTransaction_isSynchronizedStartEnabled(this.swigCPtr, this);
    }

    public void setAnimationListener(SGAnimationListener listener) {
        SGAnimationListenerHolder holder = null;
        if (listener != null) {
            holder = SGJNI.SGAnimationTransaction_getAnimationListener(this.swigCPtr, this);
            if (holder != null) {
                holder.setInterface(listener);
                return;
            }
            holder = new SGAnimationListenerHolder(listener);
        }
        SGJNI.SGAnimationTransaction_setAnimationListener(this.swigCPtr, this, SGAnimationListenerBase.getCPtr(holder), holder);
    }

    public void setDeferredStartEnabled(boolean enabled) {
        SGJNI.SGAnimationTransaction_setDeferredStartEnabled(this.swigCPtr, this, enabled);
    }

    public void setDuration(int duration) {
        SGJNI.SGAnimationTransaction_setDuration(this.swigCPtr, this, duration);
    }

    public void setOffset(int offset) {
        SGJNI.SGAnimationTransaction_setOffset(this.swigCPtr, this, offset);
    }

    public void setOnSuspendBehaviour(SGSuspendBehaviour suspendBehaviour) {
        SGJNI.SGAnimationTransaction_setOnSuspendBehaviour(this.swigCPtr, this, SGJNI.getData(suspendBehaviour));
    }

    public void setSynchronizedStartEnabled(boolean enabled) {
        SGJNI.SGAnimationTransaction_setSynchronizedStartEnabled(this.swigCPtr, this, enabled);
    }

    public void setTimingFunction(SGAnimationTimingFunction timingFunction) {
        SGJNI.SGAnimationTransaction_setTimingFunction(this.swigCPtr, this, SGAnimationTimingFunction.getCPtr(timingFunction), timingFunction);
    }

    public void setValueInterpolator(SGAnimationValueInterpolator valueInterpolator) {
        SGJNI.SGAnimationTransaction_setValueInterpolator(this.swigCPtr, this, SGAnimationValueInterpolatorHolder.getCPtr(valueInterpolator));
    }
}
