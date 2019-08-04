package com.samsung.android.sdk.sgi.animation;

public class SGAnimationClip {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGAnimationClip(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGAnimationClip(String name, int defaultDuration) {
        this(SGJNI.new_SGAnimationClip(name, defaultDuration), true);
    }

    public static long getCPtr(SGAnimationClip obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGAnimationClip_getHandle(this.swigCPtr, this);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGAnimationClip) && ((SGAnimationClip) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGAnimationClip(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public int getDuration() {
        return SGJNI.SGAnimationClip_getDuration(this.swigCPtr, this);
    }

    public String getName() {
        return SGJNI.SGAnimationClip_getName(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }
}
