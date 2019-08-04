package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.animation.SGBone;
import com.samsung.android.sdk.sgi.animation.SGClipBoneParams;
import com.samsung.android.sdk.sgi.animation.SGPoseAnimationClip;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGMaterial;

public interface SGSceneNode {

    public static class SGCameraInfo {
        public int mColor;
        public String mName;
        public SGMatrix4f mProjection;
        public SGMatrix4f mWorld;
    }

    void addCamera(SGCameraInfo sGCameraInfo);

    void addChild(SGSceneNode sGSceneNode);

    void addMaterial(String str, SGMaterial sGMaterial);

    void addPoseAnimation(String str, SGPoseAnimationClip sGPoseAnimationClip);

    void addPoseTarget(String str, SGGeometry sGGeometry);

    void addSkeletonAnimationClip(SGClipBoneParams sGClipBoneParams);

    void onCompleted();

    void setGeometry(SGGeometry sGGeometry);

    void setLocalTransform(SGMatrix4f sGMatrix4f);

    void setName(String str);

    void setSkeleton(SGBone sGBone);
}
