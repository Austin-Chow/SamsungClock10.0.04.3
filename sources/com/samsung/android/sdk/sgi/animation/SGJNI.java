package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;
import com.samsung.android.sdk.sgi.render.SGProperty;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

class SGJNI {
    private static Map<Class, Constructor> mCache = new HashMap();

    static {
        SGConfiguration.initLibrary();
        swig_module_init();
    }

    SGJNI() {
    }

    public static final native int SGAnimationClip_getDuration(long j, SGAnimationClip sGAnimationClip);

    public static final native long SGAnimationClip_getHandle(long j, SGAnimationClip sGAnimationClip);

    public static final native String SGAnimationClip_getName(long j, SGAnimationClip sGAnimationClip);

    public static final native long SGAnimationFactory_createContentRectAnimation();

    public static final native long SGAnimationFactory_createContentRectScaleAnimation();

    public static final native long SGAnimationFactory_createCustomFloatAnimation(String str);

    public static final native long SGAnimationFactory_createCustomQuaternionArrayAnimation(String str);

    public static final native long SGAnimationFactory_createCustomVector2fAnimation(String str);

    public static final native long SGAnimationFactory_createCustomVector3fAnimation(String str);

    public static final native long SGAnimationFactory_createCustomVector3fArrayAnimation(String str);

    public static final native long SGAnimationFactory_createCustomVector4fAnimation(String str);

    public static final native long SGAnimationFactory_createGeometryAnimation();

    public static final native long SGAnimationFactory_createGroupAnimation(boolean z);

    public static final native long SGAnimationFactory_createOpacityAnimation();

    public static final native long SGAnimationFactory_createPoseAnimation(Map<String, SGPoseAnimationClip> map);

    public static final native long SGAnimationFactory_createPositionAnimation();

    public static final native long SGAnimationFactory_createPositionPivotAnimation();

    public static final native long SGAnimationFactory_createRotationAnimation();

    public static final native long SGAnimationFactory_createRotationPivotAnimation();

    public static final native long SGAnimationFactory_createScaleAnimation();

    public static final native long SGAnimationFactory_createScalePivotAnimation();

    public static final native long SGAnimationFactory_createSizeAnimation();

    public static final native long SGAnimationFactory_createSkeletalAnimation(long j, SGClipBoneParams sGClipBoneParams);

    public static final native long SGAnimationFactory_createSpriteAnimation();

    public static final native long SGAnimationFactory_createTransitionAnimation__SWIG_0(int i);

    public static final native long SGAnimationFactory_createTransitionAnimation__SWIG_1(int i, int i2);

    public static final native void SGAnimationListenerBase_change_ownership(SGAnimationListenerBase sGAnimationListenerBase, long j, boolean z);

    public static final native void SGAnimationListenerBase_director_connect(SGAnimationListenerBase sGAnimationListenerBase, long j, boolean z, boolean z2);

    public static final native void SGAnimationListenerBase_getCurrentValue(long j, SGAnimationListenerBase sGAnimationListenerBase, long j2, SGVisualValueReceiver sGVisualValueReceiver);

    public static final native void SGAnimationListenerBase_onCancelled(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    /* renamed from: SGAnimationListenerBase_onCancelledSwigExplicitSGAnimationListenerBase */
    public static final native void m10xaad1aa57(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    public static final native void SGAnimationListenerBase_onDiscarded(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    /* renamed from: SGAnimationListenerBase_onDiscardedSwigExplicitSGAnimationListenerBase */
    public static final native void m11xbbcb66b(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    public static final native void SGAnimationListenerBase_onFinished(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    /* renamed from: SGAnimationListenerBase_onFinishedSwigExplicitSGAnimationListenerBase */
    public static final native void m12x62733f24(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    public static final native void SGAnimationListenerBase_onRepeated(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    /* renamed from: SGAnimationListenerBase_onRepeatedSwigExplicitSGAnimationListenerBase */
    public static final native void m13x2e86231c(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    public static final native void SGAnimationListenerBase_onStarted(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    /* renamed from: SGAnimationListenerBase_onStartedSwigExplicitSGAnimationListenerBase */
    public static final native void m14x902b9b67(long j, SGAnimationListenerBase sGAnimationListenerBase, int i);

    public static final native void SGAnimationTimingFunction_change_ownership(SGAnimationTimingFunction sGAnimationTimingFunction, long j, boolean z);

    public static final native void SGAnimationTimingFunction_director_connect(SGAnimationTimingFunction sGAnimationTimingFunction, long j, boolean z, boolean z2);

    public static final native float SGAnimationTimingFunction_getInterpolationTime(long j, SGAnimationTimingFunction sGAnimationTimingFunction, float f);

    public static final native boolean SGAnimationTransaction_begin(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native boolean SGAnimationTransaction_end(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native SGAnimationListenerHolder SGAnimationTransaction_getAnimationListener(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native int SGAnimationTransaction_getCurrentAnimationId(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native int SGAnimationTransaction_getDefaultDuration();

    public static final native boolean SGAnimationTransaction_getDefaultEnabled();

    public static final native int SGAnimationTransaction_getDefaultOffset();

    public static final native int SGAnimationTransaction_getDefaultOnSuspendBehaviour();

    public static final native SGAnimationTimingFunction SGAnimationTransaction_getDefaultTimingFunction();

    public static final native SGAnimationValueInterpolator SGAnimationTransaction_getDefaultValueInterpolator();

    public static final native int SGAnimationTransaction_getDuration(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native long SGAnimationTransaction_getHandle(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native int SGAnimationTransaction_getOffset(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native int SGAnimationTransaction_getOnSuspendBehaviour(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native SGAnimationTimingFunction SGAnimationTransaction_getTimingFunction(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native SGAnimationValueInterpolator SGAnimationTransaction_getValueInterpolator(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native boolean SGAnimationTransaction_isDefaultDeferredStartEnabled();

    public static final native boolean SGAnimationTransaction_isDefaultSynchronizedStartEnabled();

    public static final native boolean SGAnimationTransaction_isDeferredStartEnabled(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native boolean SGAnimationTransaction_isSynchronizedStartEnabled(long j, SGAnimationTransaction sGAnimationTransaction);

    public static final native void SGAnimationTransaction_setAnimationListener(long j, SGAnimationTransaction sGAnimationTransaction, long j2, SGAnimationListenerBase sGAnimationListenerBase);

    public static final native void SGAnimationTransaction_setDefaultDeferredStartEnabled(boolean z);

    public static final native void SGAnimationTransaction_setDefaultDuration(int i);

    public static final native void SGAnimationTransaction_setDefaultEnabled(boolean z);

    public static final native void SGAnimationTransaction_setDefaultOffset(int i);

    public static final native void SGAnimationTransaction_setDefaultOnSuspendBehaviour(int i);

    public static final native void SGAnimationTransaction_setDefaultSynchronizedStartEnabled(boolean z);

    public static final native void SGAnimationTransaction_setDefaultTimingFunction(long j, SGAnimationTimingFunction sGAnimationTimingFunction);

    public static final native void SGAnimationTransaction_setDefaultValueInterpolator(long j);

    public static final native void SGAnimationTransaction_setDeferredStartEnabled(long j, SGAnimationTransaction sGAnimationTransaction, boolean z);

    public static final native void SGAnimationTransaction_setDuration(long j, SGAnimationTransaction sGAnimationTransaction, int i);

    public static final native void SGAnimationTransaction_setOffset(long j, SGAnimationTransaction sGAnimationTransaction, int i);

    public static final native void SGAnimationTransaction_setOnSuspendBehaviour(long j, SGAnimationTransaction sGAnimationTransaction, int i);

    public static final native void SGAnimationTransaction_setSynchronizedStartEnabled(long j, SGAnimationTransaction sGAnimationTransaction, boolean z);

    public static final native void SGAnimationTransaction_setTimingFunction(long j, SGAnimationTransaction sGAnimationTransaction, long j2, SGAnimationTimingFunction sGAnimationTimingFunction);

    public static final native void SGAnimationTransaction_setValueInterpolator(long j, SGAnimationTransaction sGAnimationTransaction, long j2);

    public static final native void SGAnimationValueInterpolatorHolder_change_ownership(SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, long j, boolean z);

    public static final native void SGAnimationValueInterpolatorHolder_director_connect(SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, long j, boolean z, boolean z2);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate2F(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float f2, float f3, float f4, float f5);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate3F(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float f2, float f3, float f4, float f5, float f6, float f7);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate4F(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    public static final native float SGAnimationValueInterpolatorHolder_interpolateF(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float f2, float f3);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolateQ(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    public static final native float SGAnimationValueInterpolatorHolder_interpolate__SWIG_0(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float f2, float f3);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate__SWIG_1(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float[] fArr, float[] fArr2);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate__SWIG_2(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float[] fArr, float[] fArr2);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate__SWIG_3(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float[] fArr, float[] fArr2);

    public static final native float[] SGAnimationValueInterpolatorHolder_interpolate__SWIG_4(long j, SGAnimationValueInterpolatorHolder sGAnimationValueInterpolatorHolder, float f, float[] fArr, float[] fArr2);

    public static final native SGAnimationListenerHolder SGAnimation_getAnimationListener(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getDirection(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getDuration(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getFillAfterMode(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getFpsLimit(long j, SGAnimation sGAnimation);

    public static final native long SGAnimation_getHandle(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getOffset(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getOnSuspendBehaviour(long j, SGAnimation sGAnimation);

    public static final native int SGAnimation_getRepeatCount(long j, SGAnimation sGAnimation);

    public static final native SGAnimationTimingFunction SGAnimation_getTimingFunction(long j, SGAnimation sGAnimation);

    public static final native SGAnimationValueInterpolator SGAnimation_getValueInterpolator(long j, SGAnimation sGAnimation);

    public static final native boolean SGAnimation_isAutoReverseEnabled(long j, SGAnimation sGAnimation);

    public static final native boolean SGAnimation_isDeferredStartEnabled(long j, SGAnimation sGAnimation);

    public static final native boolean SGAnimation_isFillBeforeEnabled(long j, SGAnimation sGAnimation);

    public static final native boolean SGAnimation_isSynchronizedStartEnabled(long j, SGAnimation sGAnimation);

    public static final native void SGAnimation_setAnimationListener(long j, SGAnimation sGAnimation, long j2, SGAnimationListenerBase sGAnimationListenerBase);

    public static final native void SGAnimation_setAutoReverseEnabled(long j, SGAnimation sGAnimation, boolean z);

    public static final native void SGAnimation_setDeferredStartEnabled(long j, SGAnimation sGAnimation, boolean z);

    public static final native void SGAnimation_setDirection(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setDuration(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setFillAfterMode(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setFillBeforeEnabled(long j, SGAnimation sGAnimation, boolean z);

    public static final native void SGAnimation_setFpsLimit(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setOffset(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setOnSuspendBehaviour(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setRepeatCount(long j, SGAnimation sGAnimation, int i);

    public static final native void SGAnimation_setSynchronizedStartEnabled(long j, SGAnimation sGAnimation, boolean z);

    public static final native void SGAnimation_setTimingFunction(long j, SGAnimation sGAnimation, long j2, SGAnimationTimingFunction sGAnimationTimingFunction);

    public static final native void SGAnimation_setValueInterpolator(long j, SGAnimation sGAnimation, long j2);

    public static final native SGQuaternion[] SGBoneParams_getRotations(long j, SGBoneParams sGBoneParams);

    public static final native SGVector3f[] SGBoneParams_getScales(long j, SGBoneParams sGBoneParams);

    public static final native SGVector3f[] SGBoneParams_getTranslations(long j, SGBoneParams sGBoneParams);

    public static final native void SGBoneParams_setRotation(long j, SGBoneParams sGBoneParams, int i, float[] fArr);

    public static final native void SGBoneParams_setScale(long j, SGBoneParams sGBoneParams, int i, float[] fArr);

    public static final native void SGBoneParams_setTranslation(long j, SGBoneParams sGBoneParams, int i, float[] fArr);

    public static final native void SGBone_addBone__SWIG_0(long j, SGBone sGBone, long j2, SGBone sGBone2);

    public static final native void SGBone_addBone__SWIG_1(long j, SGBone sGBone, long j2, SGBone sGBone2, int i);

    public static final native long SGBone_findBoneById(long j, SGBone sGBone, int i);

    public static final native long SGBone_findBoneByName(long j, SGBone sGBone, String str);

    public static final native float[] SGBone_getBindPose(long j, SGBone sGBone);

    public static final native long SGBone_getBindPoseParams(long j, SGBone sGBone);

    public static final native long SGBone_getBone(long j, SGBone sGBone, int i);

    public static final native int SGBone_getBoneIndex(long j, SGBone sGBone, long j2, SGBone sGBone2);

    public static final native int SGBone_getBonesCount(long j, SGBone sGBone);

    public static final native long SGBone_getHandle(long j, SGBone sGBone);

    public static final native int SGBone_getId(long j, SGBone sGBone);

    public static final native float[] SGBone_getLocalOffsetTransform(long j, SGBone sGBone);

    public static final native long SGBone_getLocalOffsetTransformParams(long j, SGBone sGBone);

    public static final native float[] SGBone_getLocalTransform(long j, SGBone sGBone);

    public static final native long SGBone_getLocalTransformParams(long j, SGBone sGBone);

    public static final native String SGBone_getName(long j, SGBone sGBone);

    public static final native long SGBone_getParent(long j, SGBone sGBone);

    public static final native long SGBone_getTopLevelParent(long j, SGBone sGBone);

    public static final native float[] SGBone_getWorldOffsetTransform(long j, SGBone sGBone);

    public static final native long SGBone_getWorldOffsetTransformParams(long j, SGBone sGBone);

    public static final native float[] SGBone_getWorldTransform(long j, SGBone sGBone);

    public static final native long SGBone_getWorldTransformParams(long j, SGBone sGBone);

    public static final native void SGBone_removeAllBones(long j, SGBone sGBone);

    public static final native void SGBone_removeBone__SWIG_0(long j, SGBone sGBone, long j2, SGBone sGBone2);

    public static final native void SGBone_removeBone__SWIG_1(long j, SGBone sGBone, int i);

    public static final native void SGBone_resetBindPose(long j, SGBone sGBone);

    public static final native void SGBone_setLocalOffsetTransform__SWIG_0(long j, SGBone sGBone, float[] fArr);

    public static final native void SGBone_setLocalOffsetTransform__SWIG_1(long j, SGBone sGBone, long j2, SGBoneParams sGBoneParams);

    public static final native void SGBone_setLocalTransform__SWIG_0(long j, SGBone sGBone, float[] fArr);

    public static final native void SGBone_setLocalTransform__SWIG_1(long j, SGBone sGBone, long j2, SGBoneParams sGBoneParams);

    public static final native void SGBone_setWorldOffsetTransform__SWIG_0(long j, SGBone sGBone, float[] fArr);

    public static final native void SGBone_setWorldOffsetTransform__SWIG_1(long j, SGBone sGBone, long j2, SGBoneParams sGBoneParams);

    public static final native void SGBone_setWorldTransform__SWIG_0(long j, SGBone sGBone, float[] fArr);

    public static final native void SGBone_setWorldTransform__SWIG_1(long j, SGBone sGBone, long j2, SGBoneParams sGBoneParams);

    public static final native void SGBone_setupBindPose(long j, SGBone sGBone);

    public static final native long SGClipBoneParams_SWIGUpcast(long j);

    public static final native boolean SGClipBoneParams_addKeyFrame(long j, SGClipBoneParams sGClipBoneParams, float f, long j2, SGBoneParams sGBoneParams);

    public static final native void SGClipBoneParams_complete(long j, SGClipBoneParams sGClipBoneParams);

    public static final native boolean SGClipBoneParams_findKeyFrame(long j, SGClipBoneParams sGClipBoneParams, float f);

    public static final native long SGClipBoneParams_getKeyFrame(long j, SGClipBoneParams sGClipBoneParams, float f);

    public static final native boolean SGClipBoneParams_removeKeyFrame(long j, SGClipBoneParams sGClipBoneParams, float f);

    public static final native long SGFloatAnimation_SWIGUpcast(long j);

    public static final native boolean SGFloatAnimation_addKeyFrame(long j, SGFloatAnimation sGFloatAnimation, float f, float f2);

    public static final native float SGFloatAnimation_getEndValue(long j, SGFloatAnimation sGFloatAnimation);

    public static final native float SGFloatAnimation_getStartValue(long j, SGFloatAnimation sGFloatAnimation);

    public static final native boolean SGFloatAnimation_removeKeyFrame(long j, SGFloatAnimation sGFloatAnimation, float f);

    public static final native void SGFloatAnimation_setEndValue(long j, SGFloatAnimation sGFloatAnimation, float f);

    public static final native void SGFloatAnimation_setStartValue(long j, SGFloatAnimation sGFloatAnimation, float f);

    public static final native long SGGroupAnimation_SWIGUpcast(long j);

    public static final native int SGGroupAnimation_addAnimation(long j, SGGroupAnimation sGGroupAnimation, long j2, SGAnimation sGAnimation);

    public static final native SGAnimation SGGroupAnimation_getAnimation(long j, SGGroupAnimation sGGroupAnimation, int i);

    public static final native int SGGroupAnimation_getAnimationCount(long j, SGGroupAnimation sGGroupAnimation);

    public static final native int SGGroupAnimation_insertAnimation(long j, SGGroupAnimation sGGroupAnimation, int i, long j2, SGAnimation sGAnimation);

    public static final native boolean SGGroupAnimation_isParallel(long j, SGGroupAnimation sGGroupAnimation);

    public static final native void SGGroupAnimation_removeAllAnimations(long j, SGGroupAnimation sGGroupAnimation);

    public static final native boolean SGGroupAnimation_removeAnimation(long j, SGGroupAnimation sGGroupAnimation, int i);

    public static final native float SGLinearValueInterpolator_interpolate__SWIG_0(long j, SGLinearValueInterpolator sGLinearValueInterpolator, float f, float f2, float f3);

    public static final native float[] SGLinearValueInterpolator_interpolate__SWIG_1(long j, SGLinearValueInterpolator sGLinearValueInterpolator, float f, float[] fArr, float[] fArr2);

    public static final native float[] SGLinearValueInterpolator_interpolate__SWIG_2(long j, SGLinearValueInterpolator sGLinearValueInterpolator, float f, float[] fArr, float[] fArr2);

    public static final native float[] SGLinearValueInterpolator_interpolate__SWIG_3(long j, SGLinearValueInterpolator sGLinearValueInterpolator, float f, float[] fArr, float[] fArr2);

    public static final native float[] SGLinearValueInterpolator_interpolate__SWIG_4(long j, SGLinearValueInterpolator sGLinearValueInterpolator, float f, float[] fArr, float[] fArr2);

    public static final native long SGPoseAnimationClip_SWIGUpcast(long j);

    public static final native boolean SGPoseAnimationClip_addKeyFrame(long j, SGPoseAnimationClip sGPoseAnimationClip, float f, float f2);

    public static final native void SGPoseAnimationClip_complete(long j, SGPoseAnimationClip sGPoseAnimationClip);

    public static final native boolean SGPoseAnimationClip_findKeyFrame(long j, SGPoseAnimationClip sGPoseAnimationClip, float f);

    public static final native float SGPoseAnimationClip_getKeyFrame(long j, SGPoseAnimationClip sGPoseAnimationClip, float f);

    public static final native boolean SGPoseAnimationClip_isCompleted(long j, SGPoseAnimationClip sGPoseAnimationClip);

    public static final native boolean SGPoseAnimationClip_removeKeyFrame(long j, SGPoseAnimationClip sGPoseAnimationClip, float f);

    public static final native long SGPoseAnimation_SWIGUpcast(long j);

    public static final native long SGPoseAnimation_getClip__SWIG_0(long j, SGPoseAnimation sGPoseAnimation, String str);

    public static final native long SGPoseAnimation_getClip__SWIG_1(long j, SGPoseAnimation sGPoseAnimation, int i);

    public static final native int SGPoseAnimation_getClipsCount(long j, SGPoseAnimation sGPoseAnimation);

    public static final native String SGPoseAnimation_getTargetName(long j, SGPoseAnimation sGPoseAnimation, int i);

    public static final native int SGPoseAnimation_getTimeToBlend(long j, SGPoseAnimation sGPoseAnimation);

    public static final native void SGPoseAnimation_setClip(long j, SGPoseAnimation sGPoseAnimation, String str, long j2, SGPoseAnimationClip sGPoseAnimationClip);

    public static final native void SGPoseAnimation_setTimeToBlend(long j, SGPoseAnimation sGPoseAnimation, int i);

    public static final native long SGPropertyAnimation_SWIGUpcast(long j);

    public static final native long SGQuaternionAnimation_SWIGUpcast(long j);

    public static final native boolean SGQuaternionAnimation_addKeyFrame(long j, SGQuaternionAnimation sGQuaternionAnimation, float f, float[] fArr);

    public static final native float[] SGQuaternionAnimation_getEndValue(long j, SGQuaternionAnimation sGQuaternionAnimation);

    public static final native float[] SGQuaternionAnimation_getStartValue(long j, SGQuaternionAnimation sGQuaternionAnimation);

    public static final native boolean SGQuaternionAnimation_removeKeyFrame(long j, SGQuaternionAnimation sGQuaternionAnimation, float f);

    public static final native void SGQuaternionAnimation_setEndValue(long j, SGQuaternionAnimation sGQuaternionAnimation, float[] fArr);

    public static final native void SGQuaternionAnimation_setStartValue(long j, SGQuaternionAnimation sGQuaternionAnimation, float[] fArr);

    public static final native long SGQuaternionArrayAnimation_SWIGUpcast(long j);

    public static final native boolean SGQuaternionArrayAnimation_addKeyFrame(long j, SGQuaternionArrayAnimation sGQuaternionArrayAnimation, float f, SGQuaternion[] sGQuaternionArr);

    public static final native SGQuaternion[] SGQuaternionArrayAnimation_getEndValue(long j, SGQuaternionArrayAnimation sGQuaternionArrayAnimation);

    public static final native SGQuaternion[] SGQuaternionArrayAnimation_getStartValue(long j, SGQuaternionArrayAnimation sGQuaternionArrayAnimation);

    public static final native boolean SGQuaternionArrayAnimation_removeKeyFrame(long j, SGQuaternionArrayAnimation sGQuaternionArrayAnimation, float f);

    public static final native void SGQuaternionArrayAnimation_setEndValue(long j, SGQuaternionArrayAnimation sGQuaternionArrayAnimation, SGQuaternion[] sGQuaternionArr);

    public static final native void SGQuaternionArrayAnimation_setStartValue(long j, SGQuaternionArrayAnimation sGQuaternionArrayAnimation, SGQuaternion[] sGQuaternionArr);

    public static final native long SGSkeletalAnimation_SWIGUpcast(long j);

    public static final native void SGSkeletalAnimation_disableBone(long j, SGSkeletalAnimation sGSkeletalAnimation, int i);

    public static final native void SGSkeletalAnimation_enableBone(long j, SGSkeletalAnimation sGSkeletalAnimation, int i);

    public static final native int SGSkeletalAnimation_getAnimationToBlendId(long j, SGSkeletalAnimation sGSkeletalAnimation);

    public static final native float SGSkeletalAnimation_getBoneWeight(long j, SGSkeletalAnimation sGSkeletalAnimation, int i);

    public static final native float[] SGSkeletalAnimation_getBonesWeights(long j, SGSkeletalAnimation sGSkeletalAnimation);

    public static final native int SGSkeletalAnimation_getTimeToBlend(long j, SGSkeletalAnimation sGSkeletalAnimation);

    public static final native float SGSkeletalAnimation_getWeight(long j, SGSkeletalAnimation sGSkeletalAnimation);

    public static final native boolean SGSkeletalAnimation_isBoneEnabled(long j, SGSkeletalAnimation sGSkeletalAnimation, int i);

    public static final native void SGSkeletalAnimation_setAnimationToBlend(long j, SGSkeletalAnimation sGSkeletalAnimation, int i, int i2);

    public static final native void SGSkeletalAnimation_setBoneWeight(long j, SGSkeletalAnimation sGSkeletalAnimation, int i, float f);

    public static final native void SGSkeletalAnimation_setWeight(long j, SGSkeletalAnimation sGSkeletalAnimation, float f);

    public static final native long SGSpriteAnimation_SWIGUpcast(long j);

    public static final native boolean SGSpriteAnimation_addKeyFrame(long j, SGSpriteAnimation sGSpriteAnimation, float f, int i);

    public static final native int SGSpriteAnimation_getEndIndex(long j, SGSpriteAnimation sGSpriteAnimation);

    public static final native float[] SGSpriteAnimation_getFrameSize(long j, SGSpriteAnimation sGSpriteAnimation);

    public static final native float[] SGSpriteAnimation_getSourceSize(long j, SGSpriteAnimation sGSpriteAnimation);

    public static final native int SGSpriteAnimation_getStartIndex(long j, SGSpriteAnimation sGSpriteAnimation);

    public static final native boolean SGSpriteAnimation_removeKeyFrame(long j, SGSpriteAnimation sGSpriteAnimation, float f);

    public static final native void SGSpriteAnimation_setEndIndex(long j, SGSpriteAnimation sGSpriteAnimation, int i);

    public static final native void SGSpriteAnimation_setFrameSize(long j, SGSpriteAnimation sGSpriteAnimation, float f, float f2);

    public static final native void SGSpriteAnimation_setSourceSize(long j, SGSpriteAnimation sGSpriteAnimation, float f, float f2);

    public static final native void SGSpriteAnimation_setStartIndex(long j, SGSpriteAnimation sGSpriteAnimation, int i);

    public static final native long SGTimingFunctionFactory_createAccelerateTimingFunction__SWIG_0(int i);

    public static final native long SGTimingFunctionFactory_createAccelerateTimingFunction__SWIG_1(int i, float f);

    public static final native long SGTimingFunctionFactory_createBounceEaseTimingFunction(int i);

    public static final native long SGTimingFunctionFactory_createCubicBezierTimingFunction__SWIG_0(int i);

    public static final native long SGTimingFunctionFactory_createCubicBezierTimingFunction__SWIG_1(float f, float f2, float f3, float f4);

    public static final native long SGTimingFunctionFactory_createLinearTimingFunction();

    public static final native long SGTimingFunctionFactory_createPredefinedTimingFunction(int i);

    public static final native long SGTransitionAnimation_SWIGUpcast(long j);

    public static final native void SGTransitionAnimation_enableAlphaBlending(long j, SGTransitionAnimation sGTransitionAnimation, boolean z);

    public static final native int SGTransitionAnimation_getTransitionDirection(long j, SGTransitionAnimation sGTransitionAnimation);

    public static final native int SGTransitionAnimation_getTransitionType(long j, SGTransitionAnimation sGTransitionAnimation);

    public static final native boolean SGTransitionAnimation_isAlphaBlendingEnabled(long j, SGTransitionAnimation sGTransitionAnimation);

    public static final native boolean SGTransitionAnimation_isSynchronizedStartEnabled(long j, SGTransitionAnimation sGTransitionAnimation);

    public static final native void SGTransitionAnimation_overrideSourceTexture(long j, SGTransitionAnimation sGTransitionAnimation, long j2, SGProperty sGProperty);

    public static final native void SGTransitionAnimation_overrideTargetTexture(long j, SGTransitionAnimation sGTransitionAnimation, long j2, SGProperty sGProperty);

    public static final native void SGTransitionAnimation_setSynchronizedStartEnabled(long j, SGTransitionAnimation sGTransitionAnimation, boolean z);

    public static final native long SGVector2fAnimation_SWIGUpcast(long j);

    public static final native boolean SGVector2fAnimation_addKeyFrame(long j, SGVector2fAnimation sGVector2fAnimation, float f, float[] fArr);

    public static final native float[] SGVector2fAnimation_getEndValue(long j, SGVector2fAnimation sGVector2fAnimation);

    public static final native float[] SGVector2fAnimation_getStartValue(long j, SGVector2fAnimation sGVector2fAnimation);

    public static final native boolean SGVector2fAnimation_removeKeyFrame(long j, SGVector2fAnimation sGVector2fAnimation, float f);

    public static final native void SGVector2fAnimation_setEndValue(long j, SGVector2fAnimation sGVector2fAnimation, float[] fArr);

    public static final native void SGVector2fAnimation_setStartValue(long j, SGVector2fAnimation sGVector2fAnimation, float[] fArr);

    public static final native long SGVector3fAnimation_SWIGUpcast(long j);

    public static final native boolean SGVector3fAnimation_addKeyFrame(long j, SGVector3fAnimation sGVector3fAnimation, float f, float[] fArr);

    public static final native float[] SGVector3fAnimation_getEndValue(long j, SGVector3fAnimation sGVector3fAnimation);

    public static final native float[] SGVector3fAnimation_getStartValue(long j, SGVector3fAnimation sGVector3fAnimation);

    public static final native boolean SGVector3fAnimation_removeKeyFrame(long j, SGVector3fAnimation sGVector3fAnimation, float f);

    public static final native void SGVector3fAnimation_setEndValue(long j, SGVector3fAnimation sGVector3fAnimation, float[] fArr);

    public static final native void SGVector3fAnimation_setStartValue(long j, SGVector3fAnimation sGVector3fAnimation, float[] fArr);

    public static final native long SGVector3fArrayAnimation_SWIGUpcast(long j);

    public static final native boolean SGVector3fArrayAnimation_addKeyFrame(long j, SGVector3fArrayAnimation sGVector3fArrayAnimation, float f, SGVector3f[] sGVector3fArr);

    public static final native SGVector3f[] SGVector3fArrayAnimation_getEndValue(long j, SGVector3fArrayAnimation sGVector3fArrayAnimation);

    public static final native SGVector3f[] SGVector3fArrayAnimation_getStartValue(long j, SGVector3fArrayAnimation sGVector3fArrayAnimation);

    public static final native boolean SGVector3fArrayAnimation_removeKeyFrame(long j, SGVector3fArrayAnimation sGVector3fArrayAnimation, float f);

    public static final native void SGVector3fArrayAnimation_setEndValue(long j, SGVector3fArrayAnimation sGVector3fArrayAnimation, SGVector3f[] sGVector3fArr);

    public static final native void SGVector3fArrayAnimation_setStartValue(long j, SGVector3fArrayAnimation sGVector3fArrayAnimation, SGVector3f[] sGVector3fArr);

    public static final native long SGVector4fAnimation_SWIGUpcast(long j);

    public static final native boolean SGVector4fAnimation_addKeyFrame(long j, SGVector4fAnimation sGVector4fAnimation, float f, float[] fArr);

    public static final native float[] SGVector4fAnimation_getEndValue(long j, SGVector4fAnimation sGVector4fAnimation);

    public static final native float[] SGVector4fAnimation_getStartValue(long j, SGVector4fAnimation sGVector4fAnimation);

    public static final native boolean SGVector4fAnimation_removeKeyFrame(long j, SGVector4fAnimation sGVector4fAnimation, float f);

    public static final native void SGVector4fAnimation_setEndValue(long j, SGVector4fAnimation sGVector4fAnimation, float[] fArr);

    public static final native void SGVector4fAnimation_setStartValue(long j, SGVector4fAnimation sGVector4fAnimation, float[] fArr);

    public static final native void SGVisualValueReceiver_change_ownership(SGVisualValueReceiver sGVisualValueReceiver, long j, boolean z);

    public static final native void SGVisualValueReceiver_director_connect(SGVisualValueReceiver sGVisualValueReceiver, long j, boolean z, boolean z2);

    public static final native void SGVisualValueReceiver_onContentRect(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onContentRectScale(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onContentRectScaleSwigExplicitSGVisualValueReceiver */
    public static final native void m15xe8ea8e97(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onContentRectSwigExplicitSGVisualValueReceiver */
    public static final native void m16x260f308b(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onCustomPropertySwigExplicitSGVisualValueReceiver__SWIG_0 */
    public static final native void m17x13d119b1(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float f);

    /* renamed from: SGVisualValueReceiver_onCustomPropertySwigExplicitSGVisualValueReceiver__SWIG_1 */
    public static final native void m18x13d119b2(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onCustomPropertySwigExplicitSGVisualValueReceiver__SWIG_2 */
    public static final native void m19x13d119b3(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onCustomPropertySwigExplicitSGVisualValueReceiver__SWIG_3 */
    public static final native void m20x13d119b4(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onCustomPropertySwigExplicitSGVisualValueReceiver__SWIG_4 */
    public static final native void m21x13d119b5(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, SGVector3f[] sGVector3fArr);

    /* renamed from: SGVisualValueReceiver_onCustomPropertySwigExplicitSGVisualValueReceiver__SWIG_5 */
    public static final native void m22x13d119b6(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, SGQuaternion[] sGQuaternionArr);

    public static final native void SGVisualValueReceiver_onCustomProperty__SWIG_0(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float f);

    public static final native void SGVisualValueReceiver_onCustomProperty__SWIG_1(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float[] fArr);

    public static final native void SGVisualValueReceiver_onCustomProperty__SWIG_2(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float[] fArr);

    public static final native void SGVisualValueReceiver_onCustomProperty__SWIG_3(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, float[] fArr);

    public static final native void SGVisualValueReceiver_onCustomProperty__SWIG_4(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, SGVector3f[] sGVector3fArr);

    public static final native void SGVisualValueReceiver_onCustomProperty__SWIG_5(long j, SGVisualValueReceiver sGVisualValueReceiver, String str, SGQuaternion[] sGQuaternionArr);

    public static final native void SGVisualValueReceiver_onGeometryGeneratorParam(long j, SGVisualValueReceiver sGVisualValueReceiver, float f);

    /* renamed from: SGVisualValueReceiver_onGeometryGeneratorParamSwigExplicitSGVisualValueReceiver */
    public static final native void m23x5431f2b8(long j, SGVisualValueReceiver sGVisualValueReceiver, float f);

    public static final native void SGVisualValueReceiver_onOpacity(long j, SGVisualValueReceiver sGVisualValueReceiver, float f);

    public static final native void SGVisualValueReceiver_onOpacitySwigExplicitSGVisualValueReceiver(long j, SGVisualValueReceiver sGVisualValueReceiver, float f);

    public static final native void SGVisualValueReceiver_onOther(long j, SGVisualValueReceiver sGVisualValueReceiver);

    public static final native void SGVisualValueReceiver_onOtherSwigExplicitSGVisualValueReceiver(long j, SGVisualValueReceiver sGVisualValueReceiver);

    public static final native void SGVisualValueReceiver_onPosition(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onPositionPivot(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onPositionPivotSwigExplicitSGVisualValueReceiver */
    public static final native void m24x2b7dc8ef(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onPositionSwigExplicitSGVisualValueReceiver */
    public static final native void m25x8b5fc03b(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onRotation(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onRotationPivot(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onRotationPivotSwigExplicitSGVisualValueReceiver */
    public static final native void m26x374dd784(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onRotationSwigExplicitSGVisualValueReceiver */
    public static final native void m27x630c2886(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onScale(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onScalePivot(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onScalePivotSwigExplicitSGVisualValueReceiver */
    public static final native void m28x211726ac(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onScaleSwigExplicitSGVisualValueReceiver(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onSize(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onSizeSwigExplicitSGVisualValueReceiver(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static final native void SGVisualValueReceiver_onSpriteRect(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    /* renamed from: SGVisualValueReceiver_onSpriteRectSwigExplicitSGVisualValueReceiver */
    public static final native void m29x46e97b9b(long j, SGVisualValueReceiver sGVisualValueReceiver, float[] fArr);

    public static void SwigDirector_SGAnimationListenerBase_onCancelled(SGAnimationListenerBase jself, int id) {
        jself.onCancelled(id);
    }

    public static void SwigDirector_SGAnimationListenerBase_onDiscarded(SGAnimationListenerBase jself, int id) {
        jself.onDiscarded(id);
    }

    public static void SwigDirector_SGAnimationListenerBase_onFinished(SGAnimationListenerBase jself, int id) {
        jself.onFinished(id);
    }

    public static void SwigDirector_SGAnimationListenerBase_onRepeated(SGAnimationListenerBase jself, int id) {
        jself.onRepeated(id);
    }

    public static void SwigDirector_SGAnimationListenerBase_onStarted(SGAnimationListenerBase jself, int id) {
        jself.onStarted(id);
    }

    public static float SwigDirector_SGAnimationTimingFunction_getInterpolationTime(SGAnimationTimingFunction jself, float time) {
        return jself.getInterpolationTime(time);
    }

    public static float[] SwigDirector_SGAnimationValueInterpolatorHolder_interpolate2F(SGAnimationValueInterpolatorHolder jself, float timeProgress, float startValueX, float startValueY, float endValueX, float endValueY) {
        return jself.interpolate2F(timeProgress, startValueX, startValueY, endValueX, endValueY).getData();
    }

    public static float[] SwigDirector_SGAnimationValueInterpolatorHolder_interpolate3F(SGAnimationValueInterpolatorHolder jself, float timeProgress, float startValueX, float startValueY, float startValueZ, float endValueX, float endValueY, float endValueZ) {
        return jself.interpolate3F(timeProgress, startValueX, startValueY, startValueZ, endValueX, endValueY, endValueZ).getData();
    }

    public static float[] SwigDirector_SGAnimationValueInterpolatorHolder_interpolate4F(SGAnimationValueInterpolatorHolder jself, float timeProgress, float startValueX, float startValueY, float startValueZ, float startValueW, float endValueX, float endValueY, float endValueZ, float endValueW) {
        return jself.interpolate4F(timeProgress, startValueX, startValueY, startValueZ, startValueW, endValueX, endValueY, endValueZ, endValueW).getData();
    }

    public static float SwigDirector_SGAnimationValueInterpolatorHolder_interpolateF(SGAnimationValueInterpolatorHolder jself, float timeProgress, float startValue, float endValue) {
        return jself.interpolateF(timeProgress, startValue, endValue);
    }

    public static float[] SwigDirector_SGAnimationValueInterpolatorHolder_interpolateQ(SGAnimationValueInterpolatorHolder jself, float timeProgress, float startValueX, float startValueY, float startValueZ, float startValueW, float endValueX, float endValueY, float endValueZ, float endValueW) {
        return jself.interpolateQ(timeProgress, startValueX, startValueY, startValueZ, startValueW, endValueX, endValueY, endValueZ, endValueW).getData();
    }

    public static void SwigDirector_SGVisualValueReceiver_onContentRect(SGVisualValueReceiver jself, float[] value) {
        jself.onContentRect(new SGVector4f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onContentRectScale(SGVisualValueReceiver jself, float[] value) {
        jself.onContentRectScale(new SGVector2f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onCustomProperty__SWIG_0(SGVisualValueReceiver jself, String propertyName, float value) {
        jself.onCustomProperty(propertyName, value);
    }

    public static void SwigDirector_SGVisualValueReceiver_onCustomProperty__SWIG_1(SGVisualValueReceiver jself, String propertyName, float[] value) {
        jself.onCustomProperty(propertyName, new SGVector2f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onCustomProperty__SWIG_2(SGVisualValueReceiver jself, String propertyName, float[] value) {
        jself.onCustomProperty(propertyName, new SGVector3f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onCustomProperty__SWIG_3(SGVisualValueReceiver jself, String propertyName, float[] value) {
        jself.onCustomProperty(propertyName, new SGVector4f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onCustomProperty__SWIG_4(SGVisualValueReceiver jself, String propertyName, SGVector3f[] value) {
        jself.onCustomProperty(propertyName, value);
    }

    public static void SwigDirector_SGVisualValueReceiver_onCustomProperty__SWIG_5(SGVisualValueReceiver jself, String propertyName, SGQuaternion[] value) {
        jself.onCustomProperty(propertyName, value);
    }

    public static void SwigDirector_SGVisualValueReceiver_onGeometryGeneratorParam(SGVisualValueReceiver jself, float value) {
        jself.onGeometryGeneratorParam(value);
    }

    public static void SwigDirector_SGVisualValueReceiver_onOpacity(SGVisualValueReceiver jself, float value) {
        jself.onOpacity(value);
    }

    public static void SwigDirector_SGVisualValueReceiver_onOther(SGVisualValueReceiver jself) {
        jself.onOther();
    }

    public static void SwigDirector_SGVisualValueReceiver_onPosition(SGVisualValueReceiver jself, float[] value) {
        jself.onPosition(new SGVector3f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onPositionPivot(SGVisualValueReceiver jself, float[] value) {
        jself.onPositionPivot(new SGVector3f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onRotation(SGVisualValueReceiver jself, float[] value) {
        jself.onRotation(new SGQuaternion(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onRotationPivot(SGVisualValueReceiver jself, float[] value) {
        jself.onRotationPivot(new SGVector3f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onScale(SGVisualValueReceiver jself, float[] value) {
        jself.onScale(new SGVector3f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onScalePivot(SGVisualValueReceiver jself, float[] value) {
        jself.onScalePivot(new SGVector3f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onSize(SGVisualValueReceiver jself, float[] value) {
        jself.onSize(new SGVector2f(value));
    }

    public static void SwigDirector_SGVisualValueReceiver_onSpriteRect(SGVisualValueReceiver jself, float[] value) {
        jself.onSpriteRect(new SGVector4f(value));
    }

    static synchronized Object createObjectFromNativePtr(Class objClass, long cPtr, boolean cMemoryOwn) {
        Object ret;
        synchronized (SGJNI.class) {
            ret = null;
            Constructor<?> swigCtr = (Constructor) mCache.get(objClass);
            if (swigCtr == null) {
                try {
                    swigCtr = objClass.getDeclaredConstructor(new Class[]{Long.TYPE, Boolean.TYPE});
                    if (swigCtr != null) {
                        swigCtr.setAccessible(true);
                        mCache.put(objClass, swigCtr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (swigCtr != null) {
                ret = swigCtr.newInstance(new Object[]{Long.valueOf(cPtr), Boolean.valueOf(cMemoryOwn)});
            }
        }
        return ret;
    }

    public static final native void delete_SGAnimation(long j);

    public static final native void delete_SGAnimationClip(long j);

    public static final native void delete_SGAnimationFactory(long j);

    public static final native void delete_SGAnimationListenerBase(long j);

    public static final native void delete_SGAnimationTimingFunction(long j);

    public static final native void delete_SGAnimationTransaction(long j);

    public static final native void delete_SGAnimationValueInterpolatorHolder(long j);

    public static final native void delete_SGBone(long j);

    public static final native void delete_SGBoneParams(long j);

    public static final native void delete_SGClipBoneParams(long j);

    public static final native void delete_SGFloatAnimation(long j);

    public static final native void delete_SGGroupAnimation(long j);

    public static final native void delete_SGLinearValueInterpolator(long j);

    public static final native void delete_SGPoseAnimation(long j);

    public static final native void delete_SGPoseAnimationClip(long j);

    public static final native void delete_SGPropertyAnimation(long j);

    public static final native void delete_SGQuaternionAnimation(long j);

    public static final native void delete_SGQuaternionArrayAnimation(long j);

    public static final native void delete_SGSkeletalAnimation(long j);

    public static final native void delete_SGSpriteAnimation(long j);

    public static final native void delete_SGTimingFunctionFactory(long j);

    public static final native void delete_SGTransitionAnimation(long j);

    public static final native void delete_SGVector2fAnimation(long j);

    public static final native void delete_SGVector3fAnimation(long j);

    public static final native void delete_SGVector3fArrayAnimation(long j);

    public static final native void delete_SGVector4fAnimation(long j);

    public static final native void delete_SGVisualValueReceiver(long j);

    static int getData(Enum param) {
        if (param != null) {
            return param.ordinal();
        }
        throw new NullPointerException();
    }

    public static final native long new_SGAnimationClip(String str, int i);

    public static final native long new_SGAnimationListenerBase();

    public static final native long new_SGAnimationTimingFunction();

    public static final native long new_SGAnimationTransaction();

    public static final native long new_SGAnimationValueInterpolatorHolder();

    public static final native long new_SGBone(int i, String str);

    public static final native long new_SGBoneParams__SWIG_0();

    public static final native long new_SGBoneParams__SWIG_1(int i);

    public static final native long new_SGBoneParams__SWIG_2(SGQuaternion[] sGQuaternionArr);

    public static final native long new_SGBoneParams__SWIG_3(SGQuaternion[] sGQuaternionArr, SGVector3f[] sGVector3fArr);

    public static final native long new_SGBoneParams__SWIG_4(SGQuaternion[] sGQuaternionArr, SGVector3f[] sGVector3fArr, SGVector3f[] sGVector3fArr2);

    public static final native long new_SGClipBoneParams(String str, int i);

    public static final native long new_SGLinearValueInterpolator(boolean z);

    public static final native long new_SGPoseAnimationClip(String str, int i);

    public static final native long new_SGVisualValueReceiver();

    private static final native void swig_module_init();
}
