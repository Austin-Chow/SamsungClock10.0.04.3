package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector3f;

public final class SGBoneParams {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGBoneParams() {
        this(SGJNI.new_SGBoneParams__SWIG_0(), true);
    }

    public SGBoneParams(int bonesCount) {
        this(SGJNI.new_SGBoneParams__SWIG_1(bonesCount), true);
    }

    protected SGBoneParams(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGBoneParams(SGQuaternion[] rotations) {
        this(SGJNI.new_SGBoneParams__SWIG_2(rotations), true);
    }

    public SGBoneParams(SGQuaternion[] rotations, SGVector3f[] translations) {
        this(SGJNI.new_SGBoneParams__SWIG_3(rotations, translations), true);
    }

    public SGBoneParams(SGQuaternion[] rotations, SGVector3f[] translations, SGVector3f[] scales) {
        this(SGJNI.new_SGBoneParams__SWIG_4(rotations, translations, scales), true);
    }

    public static long getCPtr(SGBoneParams obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGBoneParams(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGQuaternion[] getRotations() {
        return SGJNI.SGBoneParams_getRotations(this.swigCPtr, this);
    }

    public SGVector3f[] getScales() {
        return SGJNI.SGBoneParams_getScales(this.swigCPtr, this);
    }

    public SGVector3f[] getTranslations() {
        return SGJNI.SGBoneParams_getTranslations(this.swigCPtr, this);
    }

    public void setRotation(int pos, SGQuaternion value) {
        if (pos < 0) {
            throw new IllegalArgumentException("Negative pos argument");
        }
        SGJNI.SGBoneParams_setRotation(this.swigCPtr, this, pos, value.getData());
    }

    public void setScale(int pos, SGVector3f value) {
        if (pos < 0) {
            throw new IllegalArgumentException("Negative pos argument");
        }
        SGJNI.SGBoneParams_setScale(this.swigCPtr, this, pos, value.getData());
    }

    public void setTranslation(int pos, SGVector3f value) {
        if (pos < 0) {
            throw new IllegalArgumentException("Negative pos argument");
        }
        SGJNI.SGBoneParams_setTranslation(this.swigCPtr, this, pos, value.getData());
    }
}
