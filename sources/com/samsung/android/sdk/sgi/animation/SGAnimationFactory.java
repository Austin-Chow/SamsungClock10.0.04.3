package com.samsung.android.sdk.sgi.animation;

import java.util.Map;

public final class SGAnimationFactory {
    public static SGVector4fAnimation createContentRectAnimation() {
        return new SGVector4fAnimation(SGJNI.SGAnimationFactory_createContentRectAnimation(), true);
    }

    public static SGVector2fAnimation createContentRectScaleAnimation() {
        return new SGVector2fAnimation(SGJNI.SGAnimationFactory_createContentRectScaleAnimation(), true);
    }

    public static SGFloatAnimation createCustomFloatAnimation(String name) {
        return new SGFloatAnimation(SGJNI.SGAnimationFactory_createCustomFloatAnimation(name), true);
    }

    public static SGQuaternionArrayAnimation createCustomQuaternionArrayAnimation(String name) {
        return new SGQuaternionArrayAnimation(SGJNI.SGAnimationFactory_createCustomQuaternionArrayAnimation(name), true);
    }

    public static SGVector2fAnimation createCustomVector2fAnimation(String name) {
        return new SGVector2fAnimation(SGJNI.SGAnimationFactory_createCustomVector2fAnimation(name), true);
    }

    public static SGVector3fAnimation createCustomVector3fAnimation(String name) {
        return new SGVector3fAnimation(SGJNI.SGAnimationFactory_createCustomVector3fAnimation(name), true);
    }

    public static SGVector3fArrayAnimation createCustomVector3fArrayAnimation(String name) {
        return new SGVector3fArrayAnimation(SGJNI.SGAnimationFactory_createCustomVector3fArrayAnimation(name), true);
    }

    public static SGVector4fAnimation createCustomVector4fAnimation(String name) {
        return new SGVector4fAnimation(SGJNI.SGAnimationFactory_createCustomVector4fAnimation(name), true);
    }

    public static SGFloatAnimation createGeometryAnimation() {
        return new SGFloatAnimation(SGJNI.SGAnimationFactory_createGeometryAnimation(), true);
    }

    public static SGGroupAnimation createGroupAnimation(boolean groupType) {
        return new SGGroupAnimation(SGJNI.SGAnimationFactory_createGroupAnimation(groupType), true);
    }

    public static SGFloatAnimation createOpacityAnimation() {
        return new SGFloatAnimation(SGJNI.SGAnimationFactory_createOpacityAnimation(), true);
    }

    public static SGPoseAnimation createPoseAnimation(Map<String, SGPoseAnimationClip> params) {
        return new SGPoseAnimation(SGJNI.SGAnimationFactory_createPoseAnimation(params), true);
    }

    public static SGVector3fAnimation createPositionAnimation() {
        return new SGVector3fAnimation(SGJNI.SGAnimationFactory_createPositionAnimation(), true);
    }

    public static SGVector3fAnimation createPositionPivotAnimation() {
        return new SGVector3fAnimation(SGJNI.SGAnimationFactory_createPositionPivotAnimation(), true);
    }

    public static SGQuaternionAnimation createRotationAnimation() {
        return new SGQuaternionAnimation(SGJNI.SGAnimationFactory_createRotationAnimation(), true);
    }

    public static SGVector3fAnimation createRotationPivotAnimation() {
        return new SGVector3fAnimation(SGJNI.SGAnimationFactory_createRotationPivotAnimation(), true);
    }

    public static SGVector3fAnimation createScaleAnimation() {
        return new SGVector3fAnimation(SGJNI.SGAnimationFactory_createScaleAnimation(), true);
    }

    public static SGVector3fAnimation createScalePivotAnimation() {
        return new SGVector3fAnimation(SGJNI.SGAnimationFactory_createScalePivotAnimation(), true);
    }

    public static SGVector2fAnimation createSizeAnimation() {
        return new SGVector2fAnimation(SGJNI.SGAnimationFactory_createSizeAnimation(), true);
    }

    public static SGSkeletalAnimation createSkeletalAnimation(SGClipBoneParams clip) {
        return new SGSkeletalAnimation(SGJNI.SGAnimationFactory_createSkeletalAnimation(SGAnimationClip.getCPtr(clip), clip), true);
    }

    public static SGSpriteAnimation createSpriteAnimation() {
        return new SGSpriteAnimation(SGJNI.SGAnimationFactory_createSpriteAnimation(), true);
    }

    public static SGTransitionAnimation createTransitionAnimation(SGTransitionType type) {
        return new SGTransitionAnimation(SGJNI.SGAnimationFactory_createTransitionAnimation__SWIG_0(SGJNI.getData(type)), true);
    }

    public static SGTransitionAnimation createTransitionAnimation(SGTransitionType type, SGTransitionDirectionType direction) {
        return new SGTransitionAnimation(SGJNI.SGAnimationFactory_createTransitionAnimation__SWIG_1(SGJNI.getData(type), SGJNI.getData(direction)), true);
    }
}
