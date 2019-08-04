package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

public class SGVisualValueReceiver {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public SGVisualValueReceiver() {
        this(SGJNI.new_SGVisualValueReceiver(), true);
        SGJNI.SGVisualValueReceiver_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGVisualValueReceiver(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGVisualValueReceiver obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGVisualValueReceiver(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void onContentRect(SGVector4f value) {
    }

    public void onContentRectScale(SGVector2f value) {
    }

    public void onCustomProperty(String propertyName, float value) {
    }

    public void onCustomProperty(String propertyName, SGVector2f value) {
    }

    public void onCustomProperty(String propertyName, SGVector3f value) {
    }

    public void onCustomProperty(String propertyName, SGVector4f value) {
    }

    public void onCustomProperty(String propertyName, SGQuaternion[] value) {
    }

    public void onCustomProperty(String propertyName, SGVector3f[] value) {
    }

    public void onGeometryGeneratorParam(float value) {
    }

    public void onOpacity(float value) {
    }

    public void onOther() {
    }

    public void onPosition(SGVector3f value) {
    }

    public void onPositionPivot(SGVector3f value) {
    }

    public void onRotation(SGQuaternion value) {
    }

    public void onRotationPivot(SGVector3f value) {
    }

    public void onScale(SGVector3f value) {
    }

    public void onScalePivot(SGVector3f value) {
    }

    public void onSize(SGVector2f value) {
    }

    public void onSpriteRect(SGVector4f value) {
    }
}
