package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;

abstract class SGAnimationListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGAnimationListenerBase() {
        this(SGJNI.new_SGAnimationListenerBase(), true);
        SGJNI.SGAnimationListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    protected SGAnimationListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGAnimationListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        SGMemoryRegistrator.getInstance().Deregister(this.swigCPtr);
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGAnimationListenerBase(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void getCurrentValue(SGVisualValueReceiver valueVisitor) {
        SGJNI.SGAnimationListenerBase_getCurrentValue(this.swigCPtr, this, SGVisualValueReceiver.getCPtr(valueVisitor), valueVisitor);
    }

    abstract void onCancelled(int i);

    abstract void onDiscarded(int i);

    abstract void onFinished(int i);

    abstract void onRepeated(int i);

    abstract void onStarted(int i);
}
