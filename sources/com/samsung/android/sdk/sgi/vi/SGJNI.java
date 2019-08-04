package com.samsung.android.sdk.sgi.vi;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Surface;
import com.samsung.android.sdk.sgi.animation.SGAnimation;
import com.samsung.android.sdk.sgi.animation.SGBone;
import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGRay;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGMaterial;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGRenderDataProvider;
import com.samsung.android.sdk.sgi.render.SGRenderTarget;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.ui.SGKeyEvent;
import com.samsung.android.sdk.sgi.ui.SGWidget;
import com.samsung.android.sdk.sgi.vi.SGSceneNode.SGCameraInfo;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
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

    public static final native long SGAssetDataReaderNative_SWIGUpcast(long j);

    public static final native void SGAssetDataReaderNative_init__SWIG_0(long j, SGAssetDataReaderNative sGAssetDataReaderNative, AssetManager assetManager, String str);

    public static final native void SGAssetDataReaderNative_init__SWIG_1(long j, SGAssetDataReaderNative sGAssetDataReaderNative, AssetInputStream assetInputStream);

    public static final native void SGAsyncPickListenerBase_change_ownership(SGAsyncPickListenerBase sGAsyncPickListenerBase, long j, boolean z);

    public static final native void SGAsyncPickListenerBase_director_connect(SGAsyncPickListenerBase sGAsyncPickListenerBase, long j, boolean z, boolean z2);

    public static final native void SGAsyncPickListenerBase_onCompleted(long j, SGAsyncPickListenerBase sGAsyncPickListenerBase, long[] jArr);

    public static final native void SGBackgroundPropertyListenerBase_change_ownership(SGBackgroundPropertyListenerBase sGBackgroundPropertyListenerBase, long j, boolean z);

    public static final native void SGBackgroundPropertyListenerBase_director_connect(SGBackgroundPropertyListenerBase sGBackgroundPropertyListenerBase, long j, boolean z, boolean z2);

    public static final native void SGBackgroundPropertyListenerBase_onFinish(long j, SGBackgroundPropertyListenerBase sGBackgroundPropertyListenerBase, long j2, int i);

    public static final native void SGBoneParamsChangeListenerBase_change_ownership(SGBoneParamsChangeListenerBase sGBoneParamsChangeListenerBase, long j, boolean z);

    public static final native void SGBoneParamsChangeListenerBase_director_connect(SGBoneParamsChangeListenerBase sGBoneParamsChangeListenerBase, long j, boolean z, boolean z2);

    public static final native void SGBoneParamsChangeListenerBase_onBoneParamsChanged(long j, SGBoneParamsChangeListenerBase sGBoneParamsChangeListenerBase, long j2, SGBone sGBone);

    public static final native void SGContext_attachCurrentThread(long j, SGContext sGContext);

    public static final native boolean SGContext_attachToNativeWindow(long j, SGContext sGContext, long j2, SGSurface sGSurface, Surface surface, SGContextConfiguration sGContextConfiguration);

    public static final native void SGContext_attachToSurface(long j, SGContext sGContext, long j2, SGSurface sGSurface, long j3, SGSurface sGSurface2, int i, int i2, SGContextConfiguration sGContextConfiguration);

    public static final native void SGContext_detachCurrentThread(long j, SGContext sGContext);

    public static final native void SGContext_detachFromNativeWindow(long j, SGContext sGContext, long j2, SGSurface sGSurface);

    public static final native void SGDataReaderBase_change_ownership(SGDataReaderBase sGDataReaderBase, long j, boolean z);

    public static final native void SGDataReaderBase_close(long j, SGDataReaderBase sGDataReaderBase);

    public static final native void SGDataReaderBase_director_connect(SGDataReaderBase sGDataReaderBase, long j, boolean z, boolean z2);

    public static final native long SGDataReaderBase_getSize(long j, SGDataReaderBase sGDataReaderBase);

    public static final native int SGDataReaderBase_read(long j, SGDataReaderBase sGDataReaderBase, ByteBuffer byteBuffer);

    public static final native void SGDataReaderBase_seek(long j, SGDataReaderBase sGDataReaderBase, long j2);

    public static final native long SGFileDataReaderNative_SWIGUpcast(long j);

    public static final native void SGFileDataReaderNative_init(long j, SGFileDataReaderNative sGFileDataReaderNative, FileInputStream fileInputStream);

    public static final native SGShaderProgramProperty SGFilterPass_getProgramProperty(long j, SGFilterPass sGFilterPass);

    public static final native int SGFilterPass_getPropertyCount(long j, SGFilterPass sGFilterPass);

    public static final native String SGFilterPass_getPropertyName(long j, SGFilterPass sGFilterPass, int i);

    public static final native SGProperty SGFilterPass_getProperty__SWIG_0(long j, SGFilterPass sGFilterPass, String str);

    public static final native SGProperty SGFilterPass_getProperty__SWIG_1(long j, SGFilterPass sGFilterPass, int i);

    public static final native boolean SGFilterPass_isAlphaBlendingEnabled(long j, SGFilterPass sGFilterPass);

    public static final native boolean SGFilterPass_isMipmapGenerationEnabled(long j, SGFilterPass sGFilterPass);

    public static final native boolean SGFilterPass_isStaticContentEnabled(long j, SGFilterPass sGFilterPass);

    public static final native void SGFilterPass_removeProperty__SWIG_0(long j, SGFilterPass sGFilterPass, String str);

    public static final native void SGFilterPass_removeProperty__SWIG_1(long j, SGFilterPass sGFilterPass, int i);

    public static final native void SGFilterPass_setAlphaBlendingEnabled(long j, SGFilterPass sGFilterPass, boolean z);

    public static final native void SGFilterPass_setMipmapGeneration(long j, SGFilterPass sGFilterPass, boolean z);

    public static final native void SGFilterPass_setProgramProperty(long j, SGFilterPass sGFilterPass, long j2, SGShaderProgramProperty sGShaderProgramProperty);

    public static final native void SGFilterPass_setProperty(long j, SGFilterPass sGFilterPass, String str, long j2, SGProperty sGProperty);

    public static final native void SGFilterPass_setStaticContentEnabled(long j, SGFilterPass sGFilterPass, boolean z);

    public static final native void SGFilter_addFilterPass(long j, SGFilter sGFilter, long j2, SGFilterPass sGFilterPass);

    public static final native long SGFilter_clone(long j, SGFilter sGFilter);

    public static final native long SGFilter_getFilterPass(long j, SGFilter sGFilter, int i);

    public static final native int SGFilter_getFilterPassCount(long j, SGFilter sGFilter);

    public static final native long SGFilter_getHandle(long j, SGFilter sGFilter);

    public static final native void SGFilter_removeFilterPass__SWIG_0(long j, SGFilter sGFilter, long j2, SGFilterPass sGFilterPass);

    public static final native void SGFilter_removeFilterPass__SWIG_1(long j, SGFilter sGFilter, int i);

    public static final native void SGFpsIndicator_change_ownership(SGFpsIndicator sGFpsIndicator, long j, boolean z);

    public static final native void SGFpsIndicator_director_connect(SGFpsIndicator sGFpsIndicator, long j, boolean z, boolean z2);

    public static final native float SGFpsIndicator_getFps(long j, SGFpsIndicator sGFpsIndicator);

    public static final native float[] SGFpsIndicator_getPosition(long j, SGFpsIndicator sGFpsIndicator);

    public static final native float[] SGFpsIndicator_getScreenSize(long j, SGFpsIndicator sGFpsIndicator);

    public static final native float[] SGFpsIndicator_getSize(long j, SGFpsIndicator sGFpsIndicator);

    public static final native void SGFpsIndicator_onDraw(long j, SGFpsIndicator sGFpsIndicator, Bitmap bitmap);

    public static final native void SGFpsIndicator_setPosition(long j, SGFpsIndicator sGFpsIndicator, float[] fArr);

    public static final native void SGFpsIndicator_setSize(long j, SGFpsIndicator sGFpsIndicator, float[] fArr);

    public static final native long SGGeometryGeneratorFactory_createCircleGeometryGenerator(float[] fArr, float f, int i);

    public static final native long SGGeometryGeneratorFactory_createNurbsMorphingGeometryGenerator(long j, SGNurbsSurface sGNurbsSurface, long j2, SGNurbsSurface sGNurbsSurface2);

    public static final native long SGGeometryGeneratorFactory_createPoseGeometryGenerator(long j, SGGeometry sGGeometry, Map<String, SGGeometry> map);

    public static final native long SGGeometryGeneratorFactory_createRectGeometryGenerator(float[] fArr, float[] fArr2);

    public static final native long SGGeometryGeneratorFactory_createRoundBorderGeometryGenerator(float[] fArr, float f, float f2, float f3, float f4, int i);

    public static final native long SGGeometryGeneratorFactory_createRoundRectGeometryGenerator(float[] fArr, float f, float f2, int i);

    public static final native long SGGeometryGeneratorFactory_createStaticGeometryGenerator(long j, SGGeometry sGGeometry);

    /* renamed from: SGGeometryGeneratorFactory_createTextMorphingGeometryGenerator__SWIG_0 */
    public static final native long m36x31de2b8f(String str, String str2);

    /* renamed from: SGGeometryGeneratorFactory_createTextMorphingGeometryGenerator__SWIG_1 */
    public static final native long m37x31de2b90(String str, String str2, float f, float f2, String str3, int i, int i2, int i3, int i4, int i5);

    public static final native long SGGeometryGeneratorFactory_createTriangleGeometryGenerator(float[] fArr, float[] fArr2, float[] fArr3);

    public static final native void SGGeometryGenerator_change_ownership(SGGeometryGenerator sGGeometryGenerator, long j, boolean z);

    public static final native void SGGeometryGenerator_director_connect(SGGeometryGenerator sGGeometryGenerator, long j, boolean z, boolean z2);

    /* renamed from: SGGeometryGenerator_generateSwigExplicitSGGeometryGenerator__SWIG_1 */
    public static final native long m38x4487c5a4(long j, SGGeometryGenerator sGGeometryGenerator, float f, long j2, SGGeometry sGGeometry, float f2, float f3, Map<String, Float> map);

    public static final native long SGGeometryGenerator_generate__SWIG_0(long j, SGGeometryGenerator sGGeometryGenerator, float f, long j2, SGGeometry sGGeometry, float f2, float f3);

    public static final native long SGGeometryGenerator_generate__SWIG_1(long j, SGGeometryGenerator sGGeometryGenerator, float f, long j2, SGGeometry sGGeometry, float f2, float f3, Map<String, Float> map);

    public static final native void SGGeometryGenerator_invalidate(long j, SGGeometryGenerator sGGeometryGenerator);

    public static final native boolean SGGeometryGenerator_isBelongsToGeometry(long j, SGGeometryGenerator sGGeometryGenerator, float[] fArr);

    public static final native void SGGraphicBufferScreenshotListenerBase_change_ownership(SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase, long j, boolean z);

    public static final native void SGGraphicBufferScreenshotListenerBase_director_connect(SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase, long j, boolean z, boolean z2);

    public static final native void SGGraphicBufferScreenshotListenerBase_onCompleted(long j, SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase, Bitmap bitmap);

    public static final native void SGLayerAnimationListenerBase_change_ownership(SGLayerAnimationListenerBase sGLayerAnimationListenerBase, long j, boolean z);

    public static final native void SGLayerAnimationListenerBase_director_connect(SGLayerAnimationListenerBase sGLayerAnimationListenerBase, long j, boolean z, boolean z2);

    public static final native void SGLayerAnimationListenerBase_onFinished(long j, SGLayerAnimationListenerBase sGLayerAnimationListenerBase, long j2, SGLayer sGLayer);

    /* renamed from: SGLayerAnimationListenerBase_onFinishedSwigExplicitSGLayerAnimationListenerBase */
    public static final native void m39xf1887b86(long j, SGLayerAnimationListenerBase sGLayerAnimationListenerBase, long j2, SGLayer sGLayer);

    public static final native void SGLayerAnimationListenerBase_onStarted(long j, SGLayerAnimationListenerBase sGLayerAnimationListenerBase, long j2, SGLayer sGLayer);

    /* renamed from: SGLayerAnimationListenerBase_onStartedSwigExplicitSGLayerAnimationListenerBase */
    public static final native void m40xae60a3f1(long j, SGLayerAnimationListenerBase sGLayerAnimationListenerBase, long j2, SGLayer sGLayer);

    public static final native long SGLayerCamera_SWIGUpcast(long j);

    public static final native void SGLayerCamera_enableClearColorPremultiply(long j, SGLayerCamera sGLayerCamera, boolean z);

    public static final native float SGLayerCamera_getAspect(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getBottom(long j, SGLayerCamera sGLayerCamera);

    public static final native int SGLayerCamera_getClearColor(long j, SGLayerCamera sGLayerCamera);

    public static final native int SGLayerCamera_getClearStencilValue(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getDistance(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getFovY(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getLeft(long j, SGLayerCamera sGLayerCamera);

    public static final native int SGLayerCamera_getPriority(long j, SGLayerCamera sGLayerCamera);

    public static final native float[] SGLayerCamera_getProjection(long j, SGLayerCamera sGLayerCamera);

    public static final native SGRay SGLayerCamera_getRay(long j, SGLayerCamera sGLayerCamera, float[] fArr);

    public static final native long SGLayerCamera_getRenderTarget(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getRight(long j, SGLayerCamera sGLayerCamera);

    public static final native float[] SGLayerCamera_getScissorsPosition(long j, SGLayerCamera sGLayerCamera);

    public static final native float[] SGLayerCamera_getScissorsSize(long j, SGLayerCamera sGLayerCamera);

    public static final native int SGLayerCamera_getSurfaceAlphaApplyMode(long j, SGLayerCamera sGLayerCamera);

    public static final native String SGLayerCamera_getTechnicName(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getTop(long j, SGLayerCamera sGLayerCamera);

    public static final native float[] SGLayerCamera_getViewportPosition(long j, SGLayerCamera sGLayerCamera);

    public static final native float[] SGLayerCamera_getViewportSize(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getZFar(long j, SGLayerCamera sGLayerCamera);

    public static final native float SGLayerCamera_getZNear(long j, SGLayerCamera sGLayerCamera);

    public static final native boolean SGLayerCamera_isClearColorEnabled(long j, SGLayerCamera sGLayerCamera);

    public static final native boolean SGLayerCamera_isClearColorPremultiply(long j, SGLayerCamera sGLayerCamera);

    public static final native boolean SGLayerCamera_isClearDepthEnabled(long j, SGLayerCamera sGLayerCamera);

    public static final native boolean SGLayerCamera_isClearStencilEnabled(long j, SGLayerCamera sGLayerCamera);

    public static final native boolean SGLayerCamera_isFrustumCullingEnabled(long j, SGLayerCamera sGLayerCamera);

    public static final native boolean SGLayerCamera_isScissorsEnabled(long j, SGLayerCamera sGLayerCamera);

    public static final native void SGLayerCamera_setAspect(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setBottom(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setClearColor(long j, SGLayerCamera sGLayerCamera, int i);

    public static final native void SGLayerCamera_setClearColorEnabled(long j, SGLayerCamera sGLayerCamera, boolean z);

    public static final native void SGLayerCamera_setClearDepth(long j, SGLayerCamera sGLayerCamera, boolean z);

    public static final native void SGLayerCamera_setClearStencil(long j, SGLayerCamera sGLayerCamera, boolean z);

    public static final native void SGLayerCamera_setClearStencilValue(long j, SGLayerCamera sGLayerCamera, int i);

    public static final native void SGLayerCamera_setFovY(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setFrustumCulling(long j, SGLayerCamera sGLayerCamera, boolean z);

    public static final native void SGLayerCamera_setLeft(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setPriority(long j, SGLayerCamera sGLayerCamera, int i);

    public static final native void SGLayerCamera_setProjection(long j, SGLayerCamera sGLayerCamera, float[] fArr);

    public static final native void SGLayerCamera_setRenderTarget(long j, SGLayerCamera sGLayerCamera, long j2, SGRenderTarget sGRenderTarget);

    public static final native void SGLayerCamera_setRight(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setScissors(long j, SGLayerCamera sGLayerCamera, boolean z);

    public static final native void SGLayerCamera_setScissorsRect(long j, SGLayerCamera sGLayerCamera, float[] fArr, float[] fArr2);

    public static final native void SGLayerCamera_setSurfaceAlphaApplyMode(long j, SGLayerCamera sGLayerCamera, int i);

    public static final native void SGLayerCamera_setTechnicName(long j, SGLayerCamera sGLayerCamera, String str);

    public static final native void SGLayerCamera_setTop(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setViewport(long j, SGLayerCamera sGLayerCamera, float[] fArr, float[] fArr2);

    public static final native void SGLayerCamera_setZFar(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCamera_setZNear(long j, SGLayerCamera sGLayerCamera, float f);

    public static final native void SGLayerCanvasRedrawListenerBase_change_ownership(SGLayerCanvasRedrawListenerBase sGLayerCanvasRedrawListenerBase, long j, boolean z);

    public static final native void SGLayerCanvasRedrawListenerBase_director_connect(SGLayerCanvasRedrawListenerBase sGLayerCanvasRedrawListenerBase, long j, boolean z, boolean z2);

    public static final native void SGLayerCanvasRedrawListenerBase_onDraw(long j, SGLayerCanvasRedrawListenerBase sGLayerCanvasRedrawListenerBase, long j2, SGLayer sGLayer, Canvas canvas);

    public static final native long SGLayerCanvas_SWIGUpcast(long j);

    public static final native float[] SGLayerCanvas_getCanvasScale(long j, SGLayerCanvas sGLayerCanvas);

    public static final native float[] SGLayerCanvas_getContentRect(long j, SGLayerCanvas sGLayerCanvas);

    public static final native float[] SGLayerCanvas_getContentRectPivot(long j, SGLayerCanvas sGLayerCanvas);

    public static final native float[] SGLayerCanvas_getContentRectScale(long j, SGLayerCanvas sGLayerCanvas);

    public static final native String SGLayerCanvas_getFormat(long j, SGLayerCanvas sGLayerCanvas);

    public static final native void SGLayerCanvas_invalidate__SWIG_0(long j, SGLayerCanvas sGLayerCanvas);

    public static final native void SGLayerCanvas_invalidate__SWIG_1(long j, SGLayerCanvas sGLayerCanvas, float[] fArr);

    public static final native void SGLayerCanvas_setCanvasScale(long j, SGLayerCanvas sGLayerCanvas, float[] fArr);

    public static final native void SGLayerCanvas_setContentRect(long j, SGLayerCanvas sGLayerCanvas, float[] fArr);

    public static final native void SGLayerCanvas_setContentRectPivot(long j, SGLayerCanvas sGLayerCanvas, float[] fArr);

    public static final native void SGLayerCanvas_setContentRectScale(long j, SGLayerCanvas sGLayerCanvas, float[] fArr);

    public static final native void SGLayerCanvas_setFormat(long j, SGLayerCanvas sGLayerCanvas, String str);

    public static final native void SGLayerCanvas_setRedrawListener(long j, SGLayerCanvas sGLayerCanvas, long j2, SGLayerCanvasRedrawListenerBase sGLayerCanvasRedrawListenerBase);

    public static final native long SGLayerImage_SWIGUpcast(long j);

    public static final native int SGLayerImage_getBlendMode(long j, SGLayerImage sGLayerImage);

    public static final native int SGLayerImage_getColor(long j, SGLayerImage sGLayerImage);

    public static final native float[] SGLayerImage_getContentRect(long j, SGLayerImage sGLayerImage);

    public static final native float[] SGLayerImage_getContentRectPivot(long j, SGLayerImage sGLayerImage);

    public static final native float[] SGLayerImage_getContentRectScale(long j, SGLayerImage sGLayerImage);

    public static final native boolean SGLayerImage_isAlphaBlendingEnabled(long j, SGLayerImage sGLayerImage);

    public static final native boolean SGLayerImage_isPreMultipliedRGBAEnabled(long j, SGLayerImage sGLayerImage);

    public static final native void SGLayerImage_setAlphaBlendingEnabled(long j, SGLayerImage sGLayerImage, boolean z);

    public static final native void SGLayerImage_setBitmap(long j, SGLayerImage sGLayerImage, Bitmap bitmap, boolean z);

    public static final native void SGLayerImage_setBlendMode(long j, SGLayerImage sGLayerImage, int i);

    public static final native void SGLayerImage_setColor(long j, SGLayerImage sGLayerImage, int i);

    public static final native void SGLayerImage_setContentRect(long j, SGLayerImage sGLayerImage, float[] fArr);

    public static final native void SGLayerImage_setContentRectPivot(long j, SGLayerImage sGLayerImage, float[] fArr);

    public static final native void SGLayerImage_setContentRectScale(long j, SGLayerImage sGLayerImage, float[] fArr);

    public static final native void SGLayerImage_setPreMultipliedRGBAEnabled(long j, SGLayerImage sGLayerImage, boolean z);

    public static final native void SGLayerImage_setTexture(long j, SGLayerImage sGLayerImage, long j2, SGProperty sGProperty);

    public static final native long SGLayerOGL_SWIGUpcast(long j);

    public static final native void SGLayerOGL_setRenderer(long j, SGLayerOGL sGLayerOGL, long j2);

    public static final native long SGLayerSkeleton_SWIGUpcast(long j);

    public static final native void SGLayerSkeleton__setBoneParamsChangeListener(long j, SGLayerSkeleton sGLayerSkeleton, long j2, SGBoneParamsChangeListenerBase sGBoneParamsChangeListenerBase);

    public static final native long SGLayerSkeleton_getSkeleton(long j, SGLayerSkeleton sGLayerSkeleton);

    public static final native void SGLayerSkeleton_setSkeleton(long j, SGLayerSkeleton sGLayerSkeleton, long j2, SGBone sGBone);

    public static final native long SGLayerText_SWIGUpcast(long j);

    public static final native int SGLayerText_getMaxLinesCount(long j, SGLayerText sGLayerText);

    public static final native int SGLayerText_getSelectionBackgroundColor(long j, SGLayerText sGLayerText);

    public static final native int SGLayerText_getSelectionTextColor(long j, SGLayerText sGLayerText);

    public static final native int SGLayerText_getShadowColor(long j, SGLayerText sGLayerText);

    public static final native float[] SGLayerText_getShadowOffset(long j, SGLayerText sGLayerText);

    public static final native float SGLayerText_getShadowRadius(long j, SGLayerText sGLayerText);

    public static final native float SGLayerText_getShadowThickness(long j, SGLayerText sGLayerText);

    public static final native String SGLayerText_getText(long j, SGLayerText sGLayerText);

    public static final native int SGLayerText_getTextColor(long j, SGLayerText sGLayerText);

    public static final native int SGLayerText_getTextGravity(long j, SGLayerText sGLayerText);

    public static final native float SGLayerText_getTextSize(long j, SGLayerText sGLayerText);

    public static final native long SGLayerText_getTypeface(long j, SGLayerText sGLayerText);

    public static final native long SGLayerText_getTypefaceDefault();

    public static final native void SGLayerText_setMaxLinesCount(long j, SGLayerText sGLayerText, int i);

    public static final native void SGLayerText_setSelectionTextColor(long j, SGLayerText sGLayerText, int i);

    public static final native void SGLayerText_setShadow(long j, SGLayerText sGLayerText, float f, float[] fArr, int i, float f2);

    public static final native void SGLayerText_setShadowColor(long j, SGLayerText sGLayerText, int i);

    public static final native void SGLayerText_setShadowOffset(long j, SGLayerText sGLayerText, float[] fArr);

    public static final native void SGLayerText_setShadowRadius(long j, SGLayerText sGLayerText, float f);

    public static final native void SGLayerText_setShadowThickness(long j, SGLayerText sGLayerText, float f);

    public static final native void SGLayerText_setText(long j, SGLayerText sGLayerText, String str);

    public static final native void SGLayerText_setTextColor(long j, SGLayerText sGLayerText, int i);

    public static final native void SGLayerText_setTextGravity(long j, SGLayerText sGLayerText, int i);

    public static final native void SGLayerText_setTextSelection(long j, SGLayerText sGLayerText, int i, int i2);

    public static final native void SGLayerText_setTextSizeNative(long j, SGLayerText sGLayerText, float f);

    public static final native void SGLayerText_setTypeface(long j, SGLayerText sGLayerText, long j2, SGTypeface sGTypeface);

    public static final native int SGLayer_addAnimation__SWIG_0(long j, SGLayer sGLayer, long j2, SGAnimation sGAnimation);

    public static final native int SGLayer_addAnimation__SWIG_1(long j, SGLayer sGLayer, long j2, SGFilterPass sGFilterPass, long j3, SGAnimation sGAnimation);

    public static final native int SGLayer_addAnimation__SWIG_2(long j, SGLayer sGLayer, long j2, SGAnimation sGAnimation, int i);

    public static final native void SGLayer_addFilter(long j, SGLayer sGLayer, long j2, SGFilter sGFilter);

    public static final native int SGLayer_addL__SWIG_0(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2);

    public static final native int SGLayer_addL__SWIG_1(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2, int i);

    public static final native void SGLayer_addMaterial(long j, SGLayer sGLayer, long j2, SGMaterial sGMaterial);

    public static final native void SGLayer_asyncTraceRay(long j, SGLayer sGLayer, SGRay sGRay, long j2);

    public static final native void SGLayer_bringLayerToF__SWIG_0(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_bringLayerToF__SWIG_1(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2);

    public static final native void SGLayer_bringToF(long j, SGLayer sGLayer);

    public static final native SGLayer SGLayer_findLayerById(long j, SGLayer sGLayer, int i);

    public static final native SGLayer SGLayer_findLayerByName(long j, SGLayer sGLayer, String str);

    public static final native long SGLayer_getFilter(long j, SGLayer sGLayer, int i);

    public static final native int SGLayer_getFilterCount(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getFullTransform(long j, SGLayer sGLayer);

    public static final native float SGLayer_getGeometryGeneratorParam(long j, SGLayer sGLayer);

    public static final native SGLayer SGLayer_getLayerAtPoint(long j, SGLayer sGLayer, float[] fArr);

    public static final native float[] SGLayer_getLocalTransform(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getLocationInWindow__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native float[] SGLayer_getLocationInWindow__SWIG_1(long j, SGLayer sGLayer, float f, float f2);

    public static final native long SGLayer_getMaterial(long j, SGLayer sGLayer, int i);

    public static final native int SGLayer_getMaterialsCount(long j, SGLayer sGLayer);

    public static final native String SGLayer_getName(long j, SGLayer sGLayer);

    public static final native float SGLayer_getOpacity(long j, SGLayer sGLayer);

    public static final native SGLayer SGLayer_getParent(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getParentBounds(long j, SGLayer sGLayer);

    public static final native SGWidget SGLayer_getParentWidget(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getPosition(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getPosition3f(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getPositionPivot(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getPositionPivot3f(long j, SGLayer sGLayer);

    public static final native SGShaderProgramProperty SGLayer_getProgramProperty(long j, SGLayer sGLayer);

    public static final native int SGLayer_getPropertyCount(long j, SGLayer sGLayer);

    public static final native String SGLayer_getPropertyName(long j, SGLayer sGLayer, int i);

    public static final native SGProperty SGLayer_getProperty__SWIG_0(long j, SGLayer sGLayer, int i);

    public static final native SGProperty SGLayer_getProperty__SWIG_1(long j, SGLayer sGLayer, String str);

    public static final native float[] SGLayer_getRelativeToAnotherParentTransform(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2);

    public static final native float[] SGLayer_getRelativeTransform(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2);

    public static final native float[] SGLayer_getRotation(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getRotation3f(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getRotationPivot(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getRotationPivot3f(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getScale(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getScale3f(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getScalePivot(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getScalePivot3f(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getScreenBounds(long j, SGLayer sGLayer);

    public static final native float[] SGLayer_getSize(long j, SGLayer sGLayer);

    public static final native SGSurface SGLayer_getSurface(long j, SGLayer sGLayer);

    public static final native int SGLayer_getTransformationHint(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_getVisibility(long j, SGLayer sGLayer);

    public static final native long SGLayer_getVisibilityMask(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isAddedToSurface(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isChildrenClippingEnabled(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isFiltersAttachDepthBuffer(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isFiltersAttachStencilBuffer(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isInheritOpacity(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isScreenShotAttachDepthBuffer(long j, SGLayer sGLayer);

    public static final native boolean SGLayer_isScreenShotAttachStencilBuffer(long j, SGLayer sGLayer);

    public static final native void SGLayer_makeScreenShot__SWIG_0(long j, SGLayer sGLayer, Bitmap bitmap);

    public static final native void SGLayer_makeScreenShot__SWIG_1(long j, SGLayer sGLayer, Bitmap bitmap, int[] iArr);

    public static final native void SGLayer_makeScreenShot__SWIG_10(long j, SGLayer sGLayer, int[] iArr, int[] iArr2, long j2, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase);

    public static final native SGProperty SGLayer_makeScreenShot__SWIG_2(long j, SGLayer sGLayer);

    public static final native SGProperty SGLayer_makeScreenShot__SWIG_3(long j, SGLayer sGLayer, int[] iArr);

    public static final native SGProperty SGLayer_makeScreenShot__SWIG_4(long j, SGLayer sGLayer, int[] iArr);

    public static final native SGProperty SGLayer_makeScreenShot__SWIG_5(long j, SGLayer sGLayer, int[] iArr, int[] iArr2);

    public static final native void SGLayer_makeScreenShot__SWIG_6(long j, SGLayer sGLayer, Bitmap bitmap, long j2, SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase);

    public static final native void SGLayer_makeScreenShot__SWIG_7(long j, SGLayer sGLayer, Bitmap bitmap, int[] iArr, long j2, SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase);

    public static final native void SGLayer_makeScreenShot__SWIG_8(long j, SGLayer sGLayer, long j2, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase);

    public static final native void SGLayer_makeScreenShot__SWIG_9(long j, SGLayer sGLayer, int[] iArr, long j2, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase);

    public static final native void SGLayer_removeAllAnimations(long j, SGLayer sGLayer);

    public static final native void SGLayer_removeAllFilters(long j, SGLayer sGLayer);

    public static final native void SGLayer_removeAllL(long j, SGLayer sGLayer);

    public static final native void SGLayer_removeAllMaterials(long j, SGLayer sGLayer);

    public static final native void SGLayer_removeAnimation(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_removeFilter__SWIG_0(long j, SGLayer sGLayer, long j2, SGFilter sGFilter);

    public static final native void SGLayer_removeFilter__SWIG_1(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_removeL__SWIG_0(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2);

    public static final native void SGLayer_removeL__SWIG_1(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_removeMaterial__SWIG_0(long j, SGLayer sGLayer, long j2, SGMaterial sGMaterial);

    public static final native void SGLayer_removeMaterial__SWIG_1(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_removeProperty__SWIG_0(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_removeProperty__SWIG_1(long j, SGLayer sGLayer, String str);

    public static final native void SGLayer_resetFilterFrameBufferSize(long j, SGLayer sGLayer);

    public static final native void SGLayer_sendLayerToB__SWIG_0(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_sendLayerToB__SWIG_1(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2);

    public static final native void SGLayer_sendToB(long j, SGLayer sGLayer);

    public static final native void SGLayer_setChildrenClipping(long j, SGLayer sGLayer, boolean z);

    public static final native void SGLayer_setFilterFrameBufferSize(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setFiltersOptions(long j, SGLayer sGLayer, boolean z, boolean z2);

    public static final native void SGLayer_setGeometryGeneratorNative(long j, SGLayer sGLayer, long j2, SGGeometryGenerator sGGeometryGenerator);

    public static final native void SGLayer_setGeometryGeneratorParam(long j, SGLayer sGLayer, float f);

    public static final native void SGLayer_setInheritOpacity(long j, SGLayer sGLayer, boolean z);

    public static final native void SGLayer_setLayerAnimationListener(long j, SGLayer sGLayer, long j2, SGLayerAnimationListenerBase sGLayerAnimationListenerBase);

    public static final native void SGLayer_setLocalTransform(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setName(long j, SGLayer sGLayer, String str);

    public static final native void SGLayer_setOpacity(long j, SGLayer sGLayer, float f);

    public static final native void SGLayer_setPivots__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setPivots__SWIG_1(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setPivots__SWIG_2(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setPivots__SWIG_3(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setPositionPivot__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setPositionPivot__SWIG_1(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setPositionPivot__SWIG_2(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setPositionPivot__SWIG_3(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setPosition__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setPosition__SWIG_1(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setPosition__SWIG_2(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setPosition__SWIG_3(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setProgramProperty(long j, SGLayer sGLayer, long j2, SGShaderProgramProperty sGShaderProgramProperty);

    public static final native void SGLayer_setProperty(long j, SGLayer sGLayer, String str, long j2, SGProperty sGProperty);

    public static final native void SGLayer_setRotationPivot__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setRotationPivot__SWIG_1(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setRotationPivot__SWIG_2(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setRotationPivot__SWIG_3(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setRotationX(long j, SGLayer sGLayer, float f);

    public static final native void SGLayer_setRotationY(long j, SGLayer sGLayer, float f);

    public static final native void SGLayer_setRotationZ(long j, SGLayer sGLayer, float f);

    public static final native void SGLayer_setRotation__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setRotation__SWIG_1(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setScalePivot__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setScalePivot__SWIG_1(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setScalePivot__SWIG_2(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setScalePivot__SWIG_3(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setScale__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setScale__SWIG_1(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setScale__SWIG_2(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setScale__SWIG_3(long j, SGLayer sGLayer, float f, float f2, float f3);

    public static final native void SGLayer_setScreenShotOptions(long j, SGLayer sGLayer, boolean z, boolean z2);

    public static final native void SGLayer_setSize__SWIG_0(long j, SGLayer sGLayer, float[] fArr);

    public static final native void SGLayer_setSize__SWIG_1(long j, SGLayer sGLayer, float f, float f2);

    public static final native void SGLayer_setTransformationHint(long j, SGLayer sGLayer, int i);

    public static final native void SGLayer_setVisibility(long j, SGLayer sGLayer, boolean z);

    public static final native void SGLayer_setVisibilityMask(long j, SGLayer sGLayer, long j2);

    public static final native void SGLayer_swapL__SWIG_0(long j, SGLayer sGLayer, long j2, SGLayer sGLayer2, long j3, SGLayer sGLayer3);

    public static final native void SGLayer_swapL__SWIG_1(long j, SGLayer sGLayer, int i, int i2);

    public static final native void SGLayer_traceRay(long j, SGLayer sGLayer, SGRay sGRay, long j2);

    public static final native long SGNurbsSurface_getHandle(long j, SGNurbsSurface sGNurbsSurface);

    public static final native void SGSceneImporter_load(long j, SGSceneImporter sGSceneImporter, String str);

    public static final native void SGSceneParserListenerBase_change_ownership(SGSceneParserListenerBase sGSceneParserListenerBase, long j, boolean z);

    public static final native void SGSceneParserListenerBase_director_connect(SGSceneParserListenerBase sGSceneParserListenerBase, long j, boolean z, boolean z2);

    public static final native void SGSceneParserListenerBase_onCamera(long j, SGSceneParserListenerBase sGSceneParserListenerBase, SGCameraInfo sGCameraInfo);

    public static final native void SGSceneParserListenerBase_onColor(long j, SGSceneParserListenerBase sGSceneParserListenerBase, int i, int i2);

    public static final native void SGSceneParserListenerBase_onGeometry(long j, SGSceneParserListenerBase sGSceneParserListenerBase, long j2);

    public static final native void SGSceneParserListenerBase_onMaterialEnd(long j, SGSceneParserListenerBase sGSceneParserListenerBase);

    public static final native boolean SGSceneParserListenerBase_onMaterialStart(long j, SGSceneParserListenerBase sGSceneParserListenerBase, String str);

    public static final native void SGSceneParserListenerBase_onNodeEnd(long j, SGSceneParserListenerBase sGSceneParserListenerBase);

    public static final native boolean SGSceneParserListenerBase_onNodeStart(long j, SGSceneParserListenerBase sGSceneParserListenerBase, String str, float[] fArr);

    public static final native void SGSceneParserListenerBase_onPoseAnimation(long j, SGSceneParserListenerBase sGSceneParserListenerBase, String str, long j2);

    public static final native void SGSceneParserListenerBase_onPoseTarget(long j, SGSceneParserListenerBase sGSceneParserListenerBase, String str, long j2);

    public static final native void SGSceneParserListenerBase_onSkeletalAnimation(long j, SGSceneParserListenerBase sGSceneParserListenerBase, long j2);

    public static final native void SGSceneParserListenerBase_onSkeleton(long j, SGSceneParserListenerBase sGSceneParserListenerBase, long j2);

    public static final native void SGSceneParserListenerBase_onTexture(long j, SGSceneParserListenerBase sGSceneParserListenerBase, int i, String str);

    public static final native void SGSceneResourceProviderBase_change_ownership(SGSceneResourceProviderBase sGSceneResourceProviderBase, long j, boolean z);

    public static final native void SGSceneResourceProviderBase_director_connect(SGSceneResourceProviderBase sGSceneResourceProviderBase, long j, boolean z, boolean z2);

    public static final native long SGSceneResourceProviderBase_getStream(long j, SGSceneResourceProviderBase sGSceneResourceProviderBase, String str);

    public static final native Object SGSceneResourceProviderBase_getTexture(long j, SGSceneResourceProviderBase sGSceneResourceProviderBase, String str);

    public static final native void SGScreenshotPropertyListenerBase_change_ownership(SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase, long j, boolean z);

    public static final native void SGScreenshotPropertyListenerBase_director_connect(SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase, long j, boolean z, boolean z2);

    public static final native void SGScreenshotPropertyListenerBase_onCompleted(long j, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase, long j2, SGProperty sGProperty);

    public static final native void SGSurfaceChangesDrawnListenerBase_change_ownership(SGSurfaceChangesDrawnListenerBase sGSurfaceChangesDrawnListenerBase, long j, boolean z);

    public static final native void SGSurfaceChangesDrawnListenerBase_director_connect(SGSurfaceChangesDrawnListenerBase sGSurfaceChangesDrawnListenerBase, long j, boolean z, boolean z2);

    public static final native void SGSurfaceChangesDrawnListenerBase_onChangesDrawn(long j, SGSurfaceChangesDrawnListenerBase sGSurfaceChangesDrawnListenerBase);

    public static final native void SGSurfaceListenerBase_change_ownership(SGSurfaceListenerBase sGSurfaceListenerBase, long j, boolean z);

    public static final native void SGSurfaceListenerBase_director_connect(SGSurfaceListenerBase sGSurfaceListenerBase, long j, boolean z, boolean z2);

    public static final native void SGSurfaceListenerBase_onFrameEnd(long j, SGSurfaceListenerBase sGSurfaceListenerBase);

    public static final native void SGSurfaceListenerBase_onResize(long j, SGSurfaceListenerBase sGSurfaceListenerBase, float[] fArr);

    public static final native void SGSurface_addChangesDrawnListener(long j, SGSurface sGSurface, long j2, SGSurfaceChangesDrawnListenerBase sGSurfaceChangesDrawnListenerBase);

    public static final native int SGSurface_addW__SWIG_0(long j, SGSurface sGSurface, long j2, SGWidget sGWidget);

    public static final native int SGSurface_addW__SWIG_1(long j, SGSurface sGSurface, long j2, SGWidget sGWidget, int i);

    public static final native void SGSurface_asyncTraceRay(long j, SGSurface sGSurface, SGRay sGRay, long j2);

    public static final native void SGSurface_bringWToF(long j, SGSurface sGSurface, long j2, SGWidget sGWidget);

    public static final native void SGSurface_destroy(long j, SGSurface sGSurface);

    public static final native void SGSurface_dumpProfilingInformation(long j, SGSurface sGSurface, int i);

    public static final native void SGSurface_enqueueTexture(long j, SGSurface sGSurface, Bitmap bitmap, boolean z, long j2, SGBackgroundPropertyListenerBase sGBackgroundPropertyListenerBase, int i);

    public static final native int SGSurface_getAffinityMask(long j, SGSurface sGSurface);

    public static final native long SGSurface_getDefaultCameraNative(long j, SGSurface sGSurface);

    public static final native SGFpsIndicator SGSurface_getFpsIndicator(long j, SGSurface sGSurface);

    public static final native int SGSurface_getFpsLimit(long j, SGSurface sGSurface);

    public static final native float SGSurface_getOpacity(long j, SGSurface sGSurface);

    public static final native long SGSurface_getRootNative(long j, SGSurface sGSurface);

    public static final native float[] SGSurface_getSize(long j, SGSurface sGSurface);

    public static final native SGWidget SGSurface_getWidgetAtPoint(long j, SGSurface sGSurface, float[] fArr);

    public static final native int SGSurface_getWidgetsCount(long j, SGSurface sGSurface);

    public static final native boolean SGSurface_isContextPreservationEnabled(long j, SGSurface sGSurface);

    public static final native boolean SGSurface_isFpsIndicatorVisible(long j, SGSurface sGSurface);

    public static final native boolean SGSurface_isPartialUpdateEnabled(long j, SGSurface sGSurface);

    public static final native boolean SGSurface_isScreenShotAttachDepthBuffer(long j, SGSurface sGSurface);

    public static final native boolean SGSurface_isScreenShotAttachStencilBuffer(long j, SGSurface sGSurface);

    public static final native void SGSurface_makeScreenShot__SWIG_0(long j, SGSurface sGSurface, Bitmap bitmap);

    public static final native void SGSurface_makeScreenShot__SWIG_1(long j, SGSurface sGSurface, Bitmap bitmap, int[] iArr);

    public static final native void SGSurface_makeScreenShot__SWIG_10(long j, SGSurface sGSurface, int[] iArr, int[] iArr2, long j2, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase);

    public static final native SGProperty SGSurface_makeScreenShot__SWIG_2(long j, SGSurface sGSurface);

    public static final native SGProperty SGSurface_makeScreenShot__SWIG_3(long j, SGSurface sGSurface, int[] iArr);

    public static final native SGProperty SGSurface_makeScreenShot__SWIG_4(long j, SGSurface sGSurface, int[] iArr);

    public static final native SGProperty SGSurface_makeScreenShot__SWIG_5(long j, SGSurface sGSurface, int[] iArr, int[] iArr2);

    public static final native void SGSurface_makeScreenShot__SWIG_6(long j, SGSurface sGSurface, Bitmap bitmap, long j2, SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase);

    public static final native void SGSurface_makeScreenShot__SWIG_7(long j, SGSurface sGSurface, Bitmap bitmap, int[] iArr, long j2, SGGraphicBufferScreenshotListenerBase sGGraphicBufferScreenshotListenerBase);

    public static final native void SGSurface_makeScreenShot__SWIG_8(long j, SGSurface sGSurface, long j2, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase);

    public static final native void SGSurface_makeScreenShot__SWIG_9(long j, SGSurface sGSurface, int[] iArr, long j2, SGScreenshotPropertyListenerBase sGScreenshotPropertyListenerBase);

    public static final native boolean SGSurface_onKeyEvent(long j, SGSurface sGSurface, long j2, SGKeyEvent sGKeyEvent);

    public static final native void SGSurface_onVsync(long j, SGSurface sGSurface, long j2);

    public static final native void SGSurface_pauseAnimations(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_pushQueue(long j, SGSurface sGSurface);

    public static final native void SGSurface_removeW__SWIG_0(long j, SGSurface sGSurface, long j2, SGWidget sGWidget);

    public static final native void SGSurface_removeW__SWIG_1(long j, SGSurface sGSurface, int i);

    public static final native void SGSurface_resume(long j, SGSurface sGSurface);

    public static final native void SGSurface_resumeAnimations(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_sendWToB(long j, SGSurface sGSurface, long j2, SGWidget sGWidget);

    public static final native int SGSurface_setAffinityMask(long j, SGSurface sGSurface, int i);

    public static final native void SGSurface_setContextPreservationEnabled(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_setDrawFrameListener(long j, SGSurface sGSurface, long j2, SGSurfaceListenerBase sGSurfaceListenerBase);

    public static final native void SGSurface_setFocus(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_setFpsIndicator(long j, SGSurface sGSurface, long j2, SGFpsIndicator sGFpsIndicator);

    public static final native void SGSurface_setFpsIndicatorVisible(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_setFpsLimit(long j, SGSurface sGSurface, int i);

    public static final native void SGSurface_setOpacity(long j, SGSurface sGSurface, float f);

    public static final native void SGSurface_setPartialUpdateEnabled(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_setScreenShotOptions(long j, SGSurface sGSurface, boolean z, boolean z2);

    public static final native void SGSurface_setSize(long j, SGSurface sGSurface, float[] fArr);

    public static final native void SGSurface_setSizeChangeListener(long j, SGSurface sGSurface, long j2, SGSurfaceListenerBase sGSurfaceListenerBase);

    public static final native void SGSurface_signalTouchEvent(long j, SGSurface sGSurface, boolean z);

    public static final native void SGSurface_suspend(long j, SGSurface sGSurface);

    public static final native void SGSurface_traceRay(long j, SGSurface sGSurface, SGRay sGRay, long j2);

    public static final native void SGTraceRayListenerBase_change_ownership(SGTraceRayListenerBase sGTraceRayListenerBase, long j, boolean z);

    public static final native void SGTraceRayListenerBase_director_connect(SGTraceRayListenerBase sGTraceRayListenerBase, long j, boolean z, boolean z2);

    public static final native void SGTraceRayListenerBase_onCompleted(long j, SGTraceRayListenerBase sGTraceRayListenerBase);

    public static final native boolean SGTraceRayListenerBase_onLayer(long j, SGTraceRayListenerBase sGTraceRayListenerBase, long j2);

    public static final native boolean SGTraceRayListenerBase_onWidget(long j, SGTraceRayListenerBase sGTraceRayListenerBase, long j2);

    public static final native long SGTypeface_create__SWIG_0(String str, int i);

    public static final native long SGTypeface_create__SWIG_1(int i);

    public static final native String SGTypeface_getFamilyName(long j, SGTypeface sGTypeface);

    public static final native int SGTypeface_getHandle(long j, SGTypeface sGTypeface);

    public static final native int SGTypeface_getStyle(long j, SGTypeface sGTypeface);

    public static final native void SGTypeface_reinitDefaultFonts();

    public static final native void SGTypeface_resetDefault(long j, SGTypeface sGTypeface, String str, int i);

    public static void SwigDirector_SGAsyncPickListenerBase_onCompleted(SGAsyncPickListenerBase jself, long[] result) {
        jself.onCompleted(result);
    }

    public static void SwigDirector_SGBackgroundPropertyListenerBase_onFinish(SGBackgroundPropertyListenerBase jself, long cPtr, int id) {
        jself.onFinish(cPtr, id);
    }

    public static void SwigDirector_SGBoneParamsChangeListenerBase_onBoneParamsChanged(SGBoneParamsChangeListenerBase jself, long aSkeleton) {
        jself.onBoneParamsChanged(aSkeleton);
    }

    public static void SwigDirector_SGDataReaderBase_close(SGDataReaderBase jself) {
        jself.close();
    }

    public static long SwigDirector_SGDataReaderBase_getSize(SGDataReaderBase jself) {
        return jself.getSize();
    }

    public static int SwigDirector_SGDataReaderBase_read(SGDataReaderBase jself, ByteBuffer length) {
        return jself.read(length);
    }

    public static void SwigDirector_SGDataReaderBase_seek(SGDataReaderBase jself, long position) {
        jself.seek(position);
    }

    public static void SwigDirector_SGFpsIndicator_onDraw(SGFpsIndicator jself, Bitmap bitmap) {
        jself.onDraw(bitmap);
    }

    public static long SwigDirector_SGGeometryGenerator_generate__SWIG_0(SGGeometryGenerator jself, float param, long previousGeometry, float height, float width) {
        return SGGeometry.getCPtr(jself.generate(param, previousGeometry == 0 ? null : (SGGeometry) createObjectFromNativePtr(SGGeometry.class, previousGeometry, false), height, width));
    }

    public static long SwigDirector_SGGeometryGenerator_generate__SWIG_1(SGGeometryGenerator jself, float param, long previousGeometry, float height, float width, Map<String, Float> poseParams) {
        return SGGeometry.getCPtr(jself.generate(param, previousGeometry == 0 ? null : (SGGeometry) createObjectFromNativePtr(SGGeometry.class, previousGeometry, false), height, width, poseParams));
    }

    public static boolean SwigDirector_SGGeometryGenerator_isBelongsToGeometry(SGGeometryGenerator jself, float[] point) {
        return jself.isBelongsToGeometry(new SGVector2f(point));
    }

    public static void SwigDirector_SGGraphicBufferScreenshotListenerBase_onCompleted(SGGraphicBufferScreenshotListenerBase jself, Bitmap result) {
        jself.onCompleted(result);
    }

    public static void SwigDirector_SGLayerAnimationListenerBase_onFinished(SGLayerAnimationListenerBase jself, long layer) {
        jself.onFinished(layer);
    }

    public static void SwigDirector_SGLayerAnimationListenerBase_onStarted(SGLayerAnimationListenerBase jself, long layer) {
        jself.onStarted(layer);
    }

    public static void SwigDirector_SGLayerCanvasRedrawListenerBase_onDraw(SGLayerCanvasRedrawListenerBase jself, long layer, Canvas canvas) {
        jself.onDraw(layer, canvas);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onCamera(SGSceneParserListenerBase jself, SGCameraInfo cameraInfo) {
        jself.onCamera(cameraInfo);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onColor(SGSceneParserListenerBase jself, int colorType, int color) {
        jself.onColor(((SGSceneMaterialColorTypes[]) SGSceneMaterialColorTypes.class.getEnumConstants())[colorType], color);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onGeometry(SGSceneParserListenerBase jself, long cPtr) {
        jself.onGeometry(cPtr);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onMaterialEnd(SGSceneParserListenerBase jself) {
        jself.onMaterialEnd();
    }

    public static boolean SwigDirector_SGSceneParserListenerBase_onMaterialStart(SGSceneParserListenerBase jself, String name) {
        return jself.onMaterialStart(name);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onNodeEnd(SGSceneParserListenerBase jself) {
        jself.onNodeEnd();
    }

    public static boolean SwigDirector_SGSceneParserListenerBase_onNodeStart(SGSceneParserListenerBase jself, String name, float[] transform) {
        return jself.onNodeStart(name, new SGMatrix4f(transform));
    }

    public static void SwigDirector_SGSceneParserListenerBase_onPoseAnimation(SGSceneParserListenerBase jself, String target, long cPtr) {
        jself.onPoseAnimation(target, cPtr);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onPoseTarget(SGSceneParserListenerBase jself, String target, long cPtr) {
        jself.onPoseTarget(target, cPtr);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onSkeletalAnimation(SGSceneParserListenerBase jself, long cPtr) {
        jself.onSkeletalAnimation(cPtr);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onSkeleton(SGSceneParserListenerBase jself, long cPtr) {
        jself.onSkeleton(cPtr);
    }

    public static void SwigDirector_SGSceneParserListenerBase_onTexture(SGSceneParserListenerBase jself, int textureType, String name) {
        jself.onTexture(((SGSceneMaterialTextureTypes[]) SGSceneMaterialTextureTypes.class.getEnumConstants())[textureType], name);
    }

    public static long SwigDirector_SGSceneResourceProviderBase_getStream(SGSceneResourceProviderBase jself, String resourceName) {
        return SGDataReaderBase.getCPtr(jself.getStream(resourceName));
    }

    public static Object SwigDirector_SGSceneResourceProviderBase_getTexture(SGSceneResourceProviderBase jself, String resourceName) {
        return jself.getTexture(resourceName);
    }

    public static void SwigDirector_SGScreenshotPropertyListenerBase_onCompleted(SGScreenshotPropertyListenerBase jself, long result) {
        jself.onCompleted(result);
    }

    public static void SwigDirector_SGSurfaceChangesDrawnListenerBase_onChangesDrawn(SGSurfaceChangesDrawnListenerBase jself) {
        jself.onChangesDrawn();
    }

    public static void SwigDirector_SGSurfaceListenerBase_onFrameEnd(SGSurfaceListenerBase jself) {
        jself.onFrameEnd();
    }

    public static void SwigDirector_SGSurfaceListenerBase_onResize(SGSurfaceListenerBase jself, float[] size) {
        jself.onResize(new SGVector2f(size));
    }

    public static void SwigDirector_SGTraceRayListenerBase_onCompleted(SGTraceRayListenerBase jself) {
        jself.onCompleted();
    }

    public static boolean SwigDirector_SGTraceRayListenerBase_onLayer(SGTraceRayListenerBase jself, long cPtr) {
        return jself.onLayer(cPtr);
    }

    public static boolean SwigDirector_SGTraceRayListenerBase_onWidget(SGTraceRayListenerBase jself, long cPtr) {
        return jself.onWidget(cPtr);
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

    public static final native void delete_SGAssetDataReaderNative(long j);

    public static final native void delete_SGAsyncPickListenerBase(long j);

    public static final native void delete_SGBackgroundPropertyListenerBase(long j);

    public static final native void delete_SGBoneParamsChangeListenerBase(long j);

    public static final native void delete_SGContext(long j);

    public static final native void delete_SGDataReaderBase(long j);

    public static final native void delete_SGFileDataReaderNative(long j);

    public static final native void delete_SGFilter(long j);

    public static final native void delete_SGFilterPass(long j);

    public static final native void delete_SGFpsIndicator(long j);

    public static final native void delete_SGGeometryGenerator(long j);

    public static final native void delete_SGGeometryGeneratorFactory(long j);

    public static final native void delete_SGGraphicBufferScreenshotListenerBase(long j);

    public static final native void delete_SGLayer(long j);

    public static final native void delete_SGLayerAnimationListenerBase(long j);

    public static final native void delete_SGLayerCamera(long j);

    public static final native void delete_SGLayerCanvas(long j);

    public static final native void delete_SGLayerCanvasRedrawListenerBase(long j);

    public static final native void delete_SGLayerImage(long j);

    public static final native void delete_SGLayerOGL(long j);

    public static final native void delete_SGLayerSkeleton(long j);

    public static final native void delete_SGLayerText(long j);

    public static final native void delete_SGNurbsSurface(long j);

    public static final native void delete_SGSceneImporter(long j);

    public static final native void delete_SGSceneParserListenerBase(long j);

    public static final native void delete_SGSceneResourceProviderBase(long j);

    public static final native void delete_SGScreenshotPropertyListenerBase(long j);

    public static final native void delete_SGSurface(long j);

    public static final native void delete_SGSurfaceChangesDrawnListenerBase(long j);

    public static final native void delete_SGSurfaceListenerBase(long j);

    public static final native void delete_SGTraceRayListenerBase(long j);

    public static final native void delete_SGTypeface(long j);

    static int getData(Enum param) {
        if (param != null) {
            return param.ordinal();
        }
        throw new NullPointerException();
    }

    public static final native long new_SGAssetDataReaderNative();

    public static final native long new_SGAsyncPickListenerBase();

    public static final native long new_SGBackgroundPropertyListenerBase();

    public static final native long new_SGBoneParamsChangeListenerBase();

    public static final native long new_SGContext(Context context, long j, SGRenderDataProvider sGRenderDataProvider);

    public static final native long new_SGDataReaderBase();

    public static final native long new_SGFileDataReaderNative();

    public static final native long new_SGFilter();

    public static final native long new_SGFilterPass__SWIG_0();

    public static final native long new_SGFilterPass__SWIG_1(boolean z);

    public static final native long new_SGFpsIndicator__SWIG_0(float[] fArr, float f);

    public static final native long new_SGFpsIndicator__SWIG_1(boolean z);

    public static final native long new_SGGeometryGenerator();

    public static final native long new_SGGraphicBufferScreenshotListenerBase();

    public static final native long new_SGLayerAnimationListenerBase();

    public static final native long new_SGLayerCamera();

    public static final native long new_SGLayerCanvasRedrawListenerBase();

    public static final native long new_SGLayerCanvas__SWIG_0();

    public static final native long new_SGLayerCanvas__SWIG_1(float[] fArr);

    public static final native long new_SGLayerImage();

    public static final native long new_SGLayerOGL__SWIG_0(long j, int i);

    public static final native long new_SGLayerOGL__SWIG_1(long j);

    public static final native long new_SGLayerOGL__SWIG_2();

    public static final native long new_SGLayerSkeleton();

    public static final native long new_SGLayerText__SWIG_0();

    public static final native long new_SGLayerText__SWIG_1(float[] fArr);

    public static final native long new_SGLayer__SWIG_0();

    public static final native long new_SGLayer__SWIG_1(float[] fArr);

    public static final native long new_SGNurbsSurface(SGVector3f[][] sGVector3fArr, float[][] fArr, int i, int i2, float[] fArr2, float[] fArr3);

    public static final native long new_SGSceneImporter(long j, SGSceneResourceProviderBase sGSceneResourceProviderBase, long j2, SGSceneParserListenerBase sGSceneParserListenerBase);

    public static final native long new_SGSceneParserListenerBase();

    public static final native long new_SGSceneResourceProviderBase();

    public static final native long new_SGScreenshotPropertyListenerBase();

    public static final native long new_SGSurface(float[] fArr, SGContextConfiguration sGContextConfiguration);

    public static final native long new_SGSurfaceChangesDrawnListenerBase();

    public static final native long new_SGSurfaceListenerBase();

    public static final native long new_SGTraceRayListenerBase();

    public static final native long new_SGTypeface(long j, SGTypeface sGTypeface);

    private static final native void swig_module_init();
}
