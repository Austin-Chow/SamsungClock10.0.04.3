package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGVector2f;

public abstract class SGFpsIndicator {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGFpsIndicator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGFpsIndicator(RectF rect, float opacity) {
        this(SGJNI.new_SGFpsIndicator__SWIG_0(SGMathNative.getArrayRect(rect), opacity), true);
        SGJNI.SGFpsIndicator_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    public SGFpsIndicator(boolean visible) {
        this(SGJNI.new_SGFpsIndicator__SWIG_1(visible), true);
        SGJNI.SGFpsIndicator_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    public static long getCPtr(SGFpsIndicator obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        SGMemoryRegistrator.getInstance().Deregister(this.swigCPtr);
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGFpsIndicator(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public float getFps() {
        return SGJNI.SGFpsIndicator_getFps(this.swigCPtr, this);
    }

    protected SGVector2f getPosition() {
        return new SGVector2f(SGJNI.SGFpsIndicator_getPosition(this.swigCPtr, this));
    }

    public SGVector2f getScreenSize() {
        return new SGVector2f(SGJNI.SGFpsIndicator_getScreenSize(this.swigCPtr, this));
    }

    protected SGVector2f getSize() {
        return new SGVector2f(SGJNI.SGFpsIndicator_getSize(this.swigCPtr, this));
    }

    protected abstract void onDraw(Bitmap bitmap);

    protected void setPosition(SGVector2f pos) {
        SGJNI.SGFpsIndicator_setPosition(this.swigCPtr, this, pos.getData());
    }

    protected void setSize(SGVector2f size) {
        SGJNI.SGFpsIndicator_setSize(this.swigCPtr, this, size.getData());
    }
}
