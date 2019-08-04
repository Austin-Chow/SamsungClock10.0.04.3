package com.samsung.android.sdk.sgi.vi;

public final class SGFilter implements Cloneable {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGFilter() {
        this(SGJNI.new_SGFilter(), true);
    }

    private SGFilter(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGFilter obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGFilter_getHandle(this.swigCPtr, this);
    }

    public void addFilterPass(SGFilterPass filterPass) {
        SGJNI.SGFilter_addFilterPass(this.swigCPtr, this, SGFilterPass.getCPtr(filterPass), filterPass);
    }

    public SGFilter clone() {
        return new SGFilter(SGJNI.SGFilter_clone(this.swigCPtr, this), true);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGFilter) && ((SGFilter) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGFilter(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGFilterPass getFilterPass(int index) {
        return new SGFilterPass(SGJNI.SGFilter_getFilterPass(this.swigCPtr, this, index), true);
    }

    public int getFilterPassCount() {
        return SGJNI.SGFilter_getFilterPassCount(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void removeFilterPass(int index) {
        SGJNI.SGFilter_removeFilterPass__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeFilterPass(SGFilterPass filterPass) {
        SGJNI.SGFilter_removeFilterPass__SWIG_0(this.swigCPtr, this, SGFilterPass.getCPtr(filterPass), filterPass);
    }
}
