package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.vi.SGSceneNode.SGCameraInfo;

abstract class SGSceneParserListenerBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGSceneParserListenerBase() {
        this(SGJNI.new_SGSceneParserListenerBase(), true);
        SGJNI.SGSceneParserListenerBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGSceneParserListenerBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGSceneParserListenerBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGSceneParserListenerBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract void onCamera(SGCameraInfo sGCameraInfo);

    public abstract void onColor(SGSceneMaterialColorTypes sGSceneMaterialColorTypes, int i);

    public abstract void onGeometry(long j);

    public abstract void onMaterialEnd();

    public abstract boolean onMaterialStart(String str);

    public abstract void onNodeEnd();

    public abstract boolean onNodeStart(String str, SGMatrix4f sGMatrix4f);

    public abstract void onPoseAnimation(String str, long j);

    public abstract void onPoseTarget(String str, long j);

    public abstract void onSkeletalAnimation(long j);

    public abstract void onSkeleton(long j);

    public abstract void onTexture(SGSceneMaterialTextureTypes sGSceneMaterialTextureTypes, String str);
}
