package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;

public class SGBone {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGBone(int id, String name) {
        this(SGJNI.new_SGBone(id, name), true);
    }

    protected SGBone(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGBone obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGBone_getHandle(this.swigCPtr, this);
    }

    public void addBone(SGBone bone) {
        SGJNI.SGBone_addBone__SWIG_0(this.swigCPtr, this, getCPtr(bone), bone);
    }

    public void addBone(SGBone bone, int index) {
        SGJNI.SGBone_addBone__SWIG_1(this.swigCPtr, this, getCPtr(bone), bone, index);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGBone) && ((SGBone) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGBone(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGBone findBoneById(int id) {
        long cPtr = SGJNI.SGBone_findBoneById(this.swigCPtr, this, id);
        return cPtr != 0 ? (SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, cPtr, true) : null;
    }

    public SGBone findBoneByName(String name) {
        long cPtr = SGJNI.SGBone_findBoneByName(this.swigCPtr, this, name);
        return cPtr != 0 ? (SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, cPtr, true) : null;
    }

    public SGMatrix4f getBindPose() {
        return new SGMatrix4f(SGJNI.SGBone_getBindPose(this.swigCPtr, this));
    }

    public SGBoneParams getBindPoseParams() {
        long cPtr = SGJNI.SGBone_getBindPoseParams(this.swigCPtr, this);
        return cPtr != 0 ? new SGBoneParams(cPtr, true) : null;
    }

    public SGBone getBone(int index) {
        return new SGBone(SGJNI.SGBone_getBone(this.swigCPtr, this, index), true);
    }

    public int getBoneIndex(SGBone bone) {
        return SGJNI.SGBone_getBoneIndex(this.swigCPtr, this, getCPtr(bone), bone);
    }

    public int getBonesCount() {
        return SGJNI.SGBone_getBonesCount(this.swigCPtr, this);
    }

    public int getId() {
        return SGJNI.SGBone_getId(this.swigCPtr, this);
    }

    public SGMatrix4f getLocalOffsetTransform() {
        return new SGMatrix4f(SGJNI.SGBone_getLocalOffsetTransform(this.swigCPtr, this));
    }

    public SGBoneParams getLocalOffsetTransformParams() {
        long cPtr = SGJNI.SGBone_getLocalOffsetTransformParams(this.swigCPtr, this);
        return cPtr != 0 ? new SGBoneParams(cPtr, true) : null;
    }

    public SGMatrix4f getLocalTransform() {
        return new SGMatrix4f(SGJNI.SGBone_getLocalTransform(this.swigCPtr, this));
    }

    public SGBoneParams getLocalTransformParams() {
        long cPtr = SGJNI.SGBone_getLocalTransformParams(this.swigCPtr, this);
        return cPtr != 0 ? new SGBoneParams(cPtr, true) : null;
    }

    public String getName() {
        return SGJNI.SGBone_getName(this.swigCPtr, this);
    }

    public SGBone getParent() {
        long cPtr = SGJNI.SGBone_getParent(this.swigCPtr, this);
        return cPtr != 0 ? (SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, cPtr, true) : null;
    }

    public SGBone getTopLevelParent() {
        long cPtr = SGJNI.SGBone_getTopLevelParent(this.swigCPtr, this);
        return cPtr != 0 ? (SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, cPtr, true) : null;
    }

    public SGMatrix4f getWorldOffsetTransform() {
        return new SGMatrix4f(SGJNI.SGBone_getWorldOffsetTransform(this.swigCPtr, this));
    }

    public SGBoneParams getWorldOffsetTransformParams() {
        long cPtr = SGJNI.SGBone_getWorldOffsetTransformParams(this.swigCPtr, this);
        return cPtr != 0 ? new SGBoneParams(cPtr, true) : null;
    }

    public SGMatrix4f getWorldTransform() {
        return new SGMatrix4f(SGJNI.SGBone_getWorldTransform(this.swigCPtr, this));
    }

    public SGBoneParams getWorldTransformParams() {
        long cPtr = SGJNI.SGBone_getWorldTransformParams(this.swigCPtr, this);
        return cPtr != 0 ? new SGBoneParams(cPtr, true) : null;
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void removeAllBones() {
        SGJNI.SGBone_removeAllBones(this.swigCPtr, this);
    }

    public void removeBone(int index) {
        SGJNI.SGBone_removeBone__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeBone(SGBone bone) {
        SGJNI.SGBone_removeBone__SWIG_0(this.swigCPtr, this, getCPtr(bone), bone);
    }

    public void resetBindPose() {
        SGJNI.SGBone_resetBindPose(this.swigCPtr, this);
    }

    public void setLocalOffsetTransform(SGBoneParams boneParams) {
        SGJNI.SGBone_setLocalOffsetTransform__SWIG_1(this.swigCPtr, this, SGBoneParams.getCPtr(boneParams), boneParams);
    }

    public void setLocalOffsetTransform(SGMatrix4f transform) {
        SGJNI.SGBone_setLocalOffsetTransform__SWIG_0(this.swigCPtr, this, transform.getData());
    }

    public void setLocalTransform(SGBoneParams boneParams) {
        SGJNI.SGBone_setLocalTransform__SWIG_1(this.swigCPtr, this, SGBoneParams.getCPtr(boneParams), boneParams);
    }

    public void setLocalTransform(SGMatrix4f transform) {
        SGJNI.SGBone_setLocalTransform__SWIG_0(this.swigCPtr, this, transform.getData());
    }

    public void setWorldOffsetTransform(SGBoneParams boneParams) {
        SGJNI.SGBone_setWorldOffsetTransform__SWIG_1(this.swigCPtr, this, SGBoneParams.getCPtr(boneParams), boneParams);
    }

    public void setWorldOffsetTransform(SGMatrix4f transform) {
        SGJNI.SGBone_setWorldOffsetTransform__SWIG_0(this.swigCPtr, this, transform.getData());
    }

    public void setWorldTransform(SGBoneParams boneParams) {
        SGJNI.SGBone_setWorldTransform__SWIG_1(this.swigCPtr, this, SGBoneParams.getCPtr(boneParams), boneParams);
    }

    public void setWorldTransform(SGMatrix4f transform) {
        SGJNI.SGBone_setWorldTransform__SWIG_0(this.swigCPtr, this, transform.getData());
    }

    public void setupBindPose() {
        SGJNI.SGBone_setupBindPose(this.swigCPtr, this);
    }
}
