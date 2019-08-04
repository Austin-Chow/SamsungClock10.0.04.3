package com.samsung.android.sdk.sgi.ui;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;

abstract class SGWidgetListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGWidgetListenerBase() {
        this(SGJNI.new_SGWidgetListenerBase(), true);
        SGJNI.SGWidgetListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGWidgetListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGWidgetListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGWidgetListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onFinished(long j);

    public abstract boolean onKeyEvent(SGKeyEvent sGKeyEvent, long j);

    public abstract void onLocalTransformChanged(long j, SGMatrix4f sGMatrix4f);

    public abstract void onOpacityChanged(long j, float f);

    public abstract void onPositionChanged(long j, SGVector3f sGVector3f);

    public abstract void onRotationChanged(long j, SGQuaternion sGQuaternion);

    public abstract void onScaleChanged(long j, SGVector3f sGVector3f);

    public abstract void onSizeChanged(long j, SGVector2f sGVector2f);

    public abstract void onStarted(long j);
}
