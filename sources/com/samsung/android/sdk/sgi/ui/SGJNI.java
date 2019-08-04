package com.samsung.android.sdk.sgi.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.animation.SGAnimation;
import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGMaterial;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.vi.SGFilter;
import com.samsung.android.sdk.sgi.vi.SGFilterPass;
import com.samsung.android.sdk.sgi.vi.SGGeometryGenerator;
import com.samsung.android.sdk.sgi.vi.SGLayer;
import com.samsung.android.sdk.sgi.vi.SGSurface;
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

    public static final native String SGKeyEvent_getCharacter(long j, SGKeyEvent sGKeyEvent);

    public static final native long SGKeyEvent_getDownTime(long j, SGKeyEvent sGKeyEvent);

    public static final native long SGKeyEvent_getEventTime(long j, SGKeyEvent sGKeyEvent);

    public static final native int SGKeyEvent_getKeyAction(long j, SGKeyEvent sGKeyEvent);

    public static final native int SGKeyEvent_getKeyCode(long j, SGKeyEvent sGKeyEvent);

    public static final native int SGKeyEvent_getKeyFlag(long j, SGKeyEvent sGKeyEvent);

    public static final native int SGKeyEvent_getRepeatCount(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isCapsLockOn(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isFunctionPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isLeftAltPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isLeftCtrlPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isLeftShiftPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isMetaPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isNumLockOn(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isPrintable(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isRightAltPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isRightCtrlPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isRightShiftPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isScrollLockOn(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isSymPressed(long j, SGKeyEvent sGKeyEvent);

    public static final native boolean SGKeyEvent_isSystem(long j, SGKeyEvent sGKeyEvent);

    public static final native void SGKeyEvent_resetKeyEvent(long j, SGKeyEvent sGKeyEvent);

    public static final native void SGKeyEvent_setCapsLockOn(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setDownTime(long j, SGKeyEvent sGKeyEvent, long j2);

    public static final native void SGKeyEvent_setEventTime(long j, SGKeyEvent sGKeyEvent, long j2);

    public static final native void SGKeyEvent_setFunctionPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setKeyEvent__SWIG_0(long j, SGKeyEvent sGKeyEvent, int i, int i2, long j2, String str, int i3);

    public static final native void SGKeyEvent_setKeyEvent__SWIG_1(long j, SGKeyEvent sGKeyEvent, int i, int i2, long j2, String str);

    public static final native void SGKeyEvent_setLeftAltPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setLeftCtrlPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setLeftShiftPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setMetaPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setNumLockOn(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setPrintable(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setRepeatCount(long j, SGKeyEvent sGKeyEvent, int i);

    public static final native void SGKeyEvent_setRightAltPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setRightCtrlPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setRightShiftPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setScrollLockOn(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setSymPressed(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native void SGKeyEvent_setSystem(long j, SGKeyEvent sGKeyEvent, boolean z);

    public static final native long SGWidgetCanvas_SWIGUpcast(long j);

    public static final native void SGWidgetCanvas_change_ownership(SGWidgetCanvas sGWidgetCanvas, long j, boolean z);

    public static final native void SGWidgetCanvas_director_connect(SGWidgetCanvas sGWidgetCanvas, long j, boolean z, boolean z2);

    public static final native float[] SGWidgetCanvas_getCanvasScale(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native float[] SGWidgetCanvas_getContentRect(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native float[] SGWidgetCanvas_getContentRectPivot(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native float[] SGWidgetCanvas_getContentRectScale(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native String SGWidgetCanvas_getFormat(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native void SGWidgetCanvas_invalidateSwigExplicitSGWidgetCanvas__SWIG_0(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native void SGWidgetCanvas_invalidateSwigExplicitSGWidgetCanvas__SWIG_1(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_invalidate__SWIG_0(long j, SGWidgetCanvas sGWidgetCanvas);

    public static final native void SGWidgetCanvas_invalidate__SWIG_1(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_onDraw(long j, SGWidgetCanvas sGWidgetCanvas, Canvas canvas);

    public static final native void SGWidgetCanvas_setCanvasScaleSwigExplicitSGWidgetCanvas__SWIG_0(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_setCanvasScale__SWIG_0(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_setCanvasScale__SWIG_1(long j, SGWidgetCanvas sGWidgetCanvas, float f, float f2);

    public static final native void SGWidgetCanvas_setContentRect(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_setContentRectPivot(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_setContentRectScale(long j, SGWidgetCanvas sGWidgetCanvas, float[] fArr);

    public static final native void SGWidgetCanvas_setFormat(long j, SGWidgetCanvas sGWidgetCanvas, String str);

    public static final native void SGWidgetDecorator_addFilter(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGFilter sGFilter);

    public static final native int SGWidgetDecorator_addL__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGLayer sGLayer);

    public static final native int SGWidgetDecorator_addL__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGLayer sGLayer, int i);

    public static final native void SGWidgetDecorator_addMaterial(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGMaterial sGMaterial);

    public static final native void SGWidgetDecorator_bringLToF__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native void SGWidgetDecorator_bringLToF__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGLayer sGLayer);

    public static final native SGLayer SGWidgetDecorator_findLayerById(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native SGLayer SGWidgetDecorator_findLayerByName(long j, SGWidgetDecorator sGWidgetDecorator, String str);

    public static final native long SGWidgetDecorator_getFilter(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native int SGWidgetDecorator_getFilterCount(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native float SGWidgetDecorator_getGeometryGeneratorParam(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native long SGWidgetDecorator_getHandle(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native long SGWidgetDecorator_getMaterial(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native int SGWidgetDecorator_getMaterialsCount(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native SGShaderProgramProperty SGWidgetDecorator_getProgramProperty(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native int SGWidgetDecorator_getPropertyCount(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native String SGWidgetDecorator_getPropertyName(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native SGProperty SGWidgetDecorator_getProperty__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native SGProperty SGWidgetDecorator_getProperty__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, String str);

    public static final native void SGWidgetDecorator_removeAllFilters(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native void SGWidgetDecorator_removeAllL(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native void SGWidgetDecorator_removeAllMaterials(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native void SGWidgetDecorator_removeFilter__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGFilter sGFilter);

    public static final native void SGWidgetDecorator_removeFilter__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native void SGWidgetDecorator_removeL__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGLayer sGLayer);

    public static final native void SGWidgetDecorator_removeL__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native void SGWidgetDecorator_removeMaterial__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGMaterial sGMaterial);

    public static final native void SGWidgetDecorator_removeMaterial__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native void SGWidgetDecorator_removeProperty__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native void SGWidgetDecorator_removeProperty__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, String str);

    public static final native void SGWidgetDecorator_resetFilterFrameBufferSize(long j, SGWidgetDecorator sGWidgetDecorator);

    public static final native void SGWidgetDecorator_sendLToB__SWIG_0(long j, SGWidgetDecorator sGWidgetDecorator, int i);

    public static final native void SGWidgetDecorator_sendLToB__SWIG_1(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGLayer sGLayer);

    public static final native void SGWidgetDecorator_setFilterFrameBufferSize(long j, SGWidgetDecorator sGWidgetDecorator, float f, float f2);

    public static final native void SGWidgetDecorator_setGeometryGeneratorNative(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGGeometryGenerator sGGeometryGenerator);

    public static final native void SGWidgetDecorator_setGeometryGeneratorParam(long j, SGWidgetDecorator sGWidgetDecorator, float f);

    public static final native void SGWidgetDecorator_setProgramProperty(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGShaderProgramProperty sGShaderProgramProperty);

    public static final native void SGWidgetDecorator_setProperty(long j, SGWidgetDecorator sGWidgetDecorator, String str, long j2, SGProperty sGProperty);

    public static final native void SGWidgetDecorator_swapL(long j, SGWidgetDecorator sGWidgetDecorator, long j2, SGLayer sGLayer, long j3, SGLayer sGLayer2);

    public static final native long SGWidgetImage_SWIGUpcast(long j);

    public static final native void SGWidgetImage_change_ownership(SGWidgetImage sGWidgetImage, long j, boolean z);

    public static final native void SGWidgetImage_director_connect(SGWidgetImage sGWidgetImage, long j, boolean z, boolean z2);

    public static final native int SGWidgetImage_getBlendMode(long j, SGWidgetImage sGWidgetImage);

    public static final native int SGWidgetImage_getColor(long j, SGWidgetImage sGWidgetImage);

    public static final native float[] SGWidgetImage_getContentRect(long j, SGWidgetImage sGWidgetImage);

    public static final native float[] SGWidgetImage_getContentRectPivot(long j, SGWidgetImage sGWidgetImage);

    public static final native float[] SGWidgetImage_getContentRectScale(long j, SGWidgetImage sGWidgetImage);

    public static final native boolean SGWidgetImage_isAlphaBlendingEnabled(long j, SGWidgetImage sGWidgetImage);

    public static final native boolean SGWidgetImage_isPreMultipliedRGBAEnabled(long j, SGWidgetImage sGWidgetImage);

    public static final native void SGWidgetImage_setAlphaBlendingEnabled(long j, SGWidgetImage sGWidgetImage, boolean z);

    public static final native void SGWidgetImage_setBitmap(long j, SGWidgetImage sGWidgetImage, Bitmap bitmap, boolean z);

    public static final native void SGWidgetImage_setBlendMode(long j, SGWidgetImage sGWidgetImage, int i);

    public static final native void SGWidgetImage_setColor(long j, SGWidgetImage sGWidgetImage, int i);

    public static final native void SGWidgetImage_setContentRect(long j, SGWidgetImage sGWidgetImage, float[] fArr);

    public static final native void SGWidgetImage_setContentRectPivot(long j, SGWidgetImage sGWidgetImage, float[] fArr);

    public static final native void SGWidgetImage_setContentRectScale(long j, SGWidgetImage sGWidgetImage, float[] fArr);

    public static final native void SGWidgetImage_setPreMultipliedRGBAEnabled(long j, SGWidgetImage sGWidgetImage, boolean z);

    public static final native void SGWidgetImage_setTexture(long j, SGWidgetImage sGWidgetImage, long j2, SGProperty sGProperty);

    public static final native void SGWidgetListenerBase_change_ownership(SGWidgetListenerBase sGWidgetListenerBase, long j, boolean z);

    public static final native void SGWidgetListenerBase_director_connect(SGWidgetListenerBase sGWidgetListenerBase, long j, boolean z, boolean z2);

    public static final native void SGWidgetListenerBase_onFinished(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget);

    public static final native void SGWidgetListenerBase_onFinishedSwigExplicitSGWidgetListenerBase(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget);

    public static final native boolean SGWidgetListenerBase_onKeyEvent(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGKeyEvent sGKeyEvent, long j3, SGWidget sGWidget);

    public static final native void SGWidgetListenerBase_onLocalTransformChanged(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    /* renamed from: SGWidgetListenerBase_onLocalTransformChangedSwigExplicitSGWidgetListenerBase */
    public static final native void m30x28065b45(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidgetListenerBase_onOpacityChanged(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float f);

    /* renamed from: SGWidgetListenerBase_onOpacityChangedSwigExplicitSGWidgetListenerBase */
    public static final native void m31x647e93dd(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float f);

    public static final native void SGWidgetListenerBase_onPositionChanged(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    /* renamed from: SGWidgetListenerBase_onPositionChangedSwigExplicitSGWidgetListenerBase */
    public static final native void m32x1f37049d(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidgetListenerBase_onRotationChanged(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    /* renamed from: SGWidgetListenerBase_onRotationChangedSwigExplicitSGWidgetListenerBase */
    public static final native void m33x8d68c8a8(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidgetListenerBase_onScaleChanged(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    /* renamed from: SGWidgetListenerBase_onScaleChangedSwigExplicitSGWidgetListenerBase */
    public static final native void m34xb047c5fe(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidgetListenerBase_onSizeChanged(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    /* renamed from: SGWidgetListenerBase_onSizeChangedSwigExplicitSGWidgetListenerBase */
    public static final native void m35x9f150245(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidgetListenerBase_onStarted(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget);

    public static final native void SGWidgetListenerBase_onStartedSwigExplicitSGWidgetListenerBase(long j, SGWidgetListenerBase sGWidgetListenerBase, long j2, SGWidget sGWidget);

    public static final native long SGWidgetSurface_SWIGUpcast(long j);

    public static final native void SGWidgetSurface_change_ownership(SGWidgetSurface sGWidgetSurface, long j, boolean z);

    public static final native void SGWidgetSurface_director_connect(SGWidgetSurface sGWidgetSurface, long j, boolean z, boolean z2);

    public static final native float[] SGWidgetSurface_getContentRect(long j, SGWidgetSurface sGWidgetSurface);

    public static final native float[] SGWidgetSurface_getContentRectPivot(long j, SGWidgetSurface sGWidgetSurface);

    public static final native float[] SGWidgetSurface_getContentRectScale(long j, SGWidgetSurface sGWidgetSurface);

    public static final native void SGWidgetSurface_invalidateSwigExplicitSGWidgetSurface__SWIG_0_0(long j, SGWidgetSurface sGWidgetSurface, float[] fArr);

    public static final native void SGWidgetSurface_invalidateSwigExplicitSGWidgetSurface__SWIG_1(long j, SGWidgetSurface sGWidgetSurface);

    public static final native void SGWidgetSurface_invalidate__SWIG_0_0(long j, SGWidgetSurface sGWidgetSurface, float[] fArr);

    public static final native void SGWidgetSurface_invalidate__SWIG_1(long j, SGWidgetSurface sGWidgetSurface);

    public static final native void SGWidgetSurface_setContentRect(long j, SGWidgetSurface sGWidgetSurface, float[] fArr);

    public static final native void SGWidgetSurface_setContentRectPivot(long j, SGWidgetSurface sGWidgetSurface, float[] fArr);

    public static final native void SGWidgetSurface_setContentRectScale(long j, SGWidgetSurface sGWidgetSurface, float[] fArr);

    public static final native void SGWidgetSurface_setRenderer(long j, SGWidgetSurface sGWidgetSurface, long j2);

    public static final native void SGWidgetSurface_updateTextureMatrix(long j, SGWidgetSurface sGWidgetSurface, float[] fArr);

    public static final native int SGWidget_addAnimation__SWIG_0(long j, SGWidget sGWidget, long j2, SGAnimation sGAnimation);

    public static final native int SGWidget_addAnimation__SWIG_1(long j, SGWidget sGWidget, long j2, SGFilterPass sGFilterPass, long j3, SGAnimation sGAnimation);

    public static final native int SGWidget_addAnimation__SWIG_2(long j, SGWidget sGWidget, int i, long j2, SGAnimation sGAnimation);

    public static final native int SGWidget_addW__SWIG_0(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2);

    public static final native int SGWidget_addW__SWIG_1(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2, int i);

    public static final native void SGWidget_bringToF(long j, SGWidget sGWidget);

    public static final native void SGWidget_bringWToF__SWIG_0(long j, SGWidget sGWidget, int i);

    public static final native void SGWidget_bringWToF__SWIG_1(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2);

    public static final native void SGWidget_change_ownership(SGWidget sGWidget, long j, boolean z);

    public static final native void SGWidget_clearFocus(long j, SGWidget sGWidget);

    public static final native void SGWidget_clearFocusSwigExplicitSGWidget(long j, SGWidget sGWidget);

    public static final native void SGWidget_director_connect(SGWidget sGWidget, long j, boolean z, boolean z2);

    public static final native boolean SGWidget_dispatchKeyEvent(long j, SGWidget sGWidget, long j2, SGKeyEvent sGKeyEvent);

    public static final native boolean SGWidget_dispatchKeyEventSwigExplicitSGWidget(long j, SGWidget sGWidget, long j2, SGKeyEvent sGKeyEvent);

    public static final native float[] SGWidget_getFullTransform(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getLocalTransform(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getLocationInWindow__SWIG_0(long j, SGWidget sGWidget, float[] fArr, boolean z);

    public static final native float[] SGWidget_getLocationInWindow__SWIG_1(long j, SGWidget sGWidget, float f, float f2, boolean z);

    public static final native String SGWidget_getName(long j, SGWidget sGWidget);

    public static final native float SGWidget_getOpacity(long j, SGWidget sGWidget);

    public static final native SGWidget SGWidget_getParent(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getPosition(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getPosition3f(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getPositionPivot(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getPositionPivot3f(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getRelativeToAnotherParentTransform(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2);

    public static final native float[] SGWidget_getRelativeTransform(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2);

    public static final native float[] SGWidget_getRotation(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getRotation3f(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getRotationPivot(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getRotationPivot3f(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getScale(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getScale3f(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getScalePivot(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getScalePivot3f(long j, SGWidget sGWidget);

    public static final native float[] SGWidget_getSize(long j, SGWidget sGWidget);

    public static final native SGSurface SGWidget_getSurface(long j, SGWidget sGWidget);

    public static final native int SGWidget_getTransformationHint(long j, SGWidget sGWidget);

    public static final native int SGWidget_getVisibility(long j, SGWidget sGWidget);

    public static final native long SGWidget_getVisibilityMask(long j, SGWidget sGWidget);

    public static final native SGWidget SGWidget_getWidgetAtPoint(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_hideCursor(long j, SGWidget sGWidget);

    public static final native void SGWidget_invalidateSwigExplicitSGWidget__SWIG_0(long j, SGWidget sGWidget);

    public static final native void SGWidget_invalidateSwigExplicitSGWidget__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_invalidate__SWIG_0(long j, SGWidget sGWidget);

    public static final native void SGWidget_invalidate__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native boolean SGWidget_isAddedToSurface(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isChildrenClippingEnabled(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isFiltersAttachDepthBuffer(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isFiltersAttachStencilBuffer(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isFocusable(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isFocusableSwigExplicitSGWidget(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isInFocus(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isInFocusSwigExplicitSGWidget(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isInheritOpacity(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isLocalPointOnWidget__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native boolean SGWidget_isLocalPointOnWidget__SWIG_1(long j, SGWidget sGWidget, float f, float f2);

    public static final native boolean SGWidget_isRawPointOnWidget__SWIG_0(long j, SGWidget sGWidget, float[] fArr, boolean z);

    public static final native boolean SGWidget_isRawPointOnWidget__SWIG_1(long j, SGWidget sGWidget, float f, float f2, boolean z);

    public static final native boolean SGWidget_isScreenShotAttachDepthBuffer(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isScreenShotAttachStencilBuffer(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isTouchFocusable(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_isVisible(long j, SGWidget sGWidget);

    public static final native void SGWidget_makeScreenShot__SWIG_0(long j, SGWidget sGWidget, Bitmap bitmap);

    public static final native void SGWidget_makeScreenShot__SWIG_1(long j, SGWidget sGWidget, Bitmap bitmap, int[] iArr);

    public static final native void SGWidget_makeScreenShot__SWIG_10(long j, SGWidget sGWidget, int[] iArr, int[] iArr2, long j2);

    public static final native SGProperty SGWidget_makeScreenShot__SWIG_2(long j, SGWidget sGWidget);

    public static final native SGProperty SGWidget_makeScreenShot__SWIG_3(long j, SGWidget sGWidget, int[] iArr);

    public static final native SGProperty SGWidget_makeScreenShot__SWIG_4(long j, SGWidget sGWidget, int[] iArr);

    public static final native SGProperty SGWidget_makeScreenShot__SWIG_5(long j, SGWidget sGWidget, int[] iArr, int[] iArr2);

    public static final native void SGWidget_makeScreenShot__SWIG_6(long j, SGWidget sGWidget, Bitmap bitmap, long j2);

    public static final native void SGWidget_makeScreenShot__SWIG_7(long j, SGWidget sGWidget, Bitmap bitmap, int[] iArr, long j2);

    public static final native void SGWidget_makeScreenShot__SWIG_8(long j, SGWidget sGWidget, long j2);

    public static final native void SGWidget_makeScreenShot__SWIG_9(long j, SGWidget sGWidget, int[] iArr, long j2);

    public static final native void SGWidget_moveCursor(long j, SGWidget sGWidget, float[] fArr, float[] fArr2, int i);

    public static final native void SGWidget_onFocusChanged(long j, SGWidget sGWidget);

    public static final native void SGWidget_onFocusChangedSwigExplicitSGWidget(long j, SGWidget sGWidget);

    public static final native boolean SGWidget_onKeyEvent(long j, SGWidget sGWidget, long j2, SGKeyEvent sGKeyEvent);

    public static final native boolean SGWidget_onKeyEventSwigExplicitSGWidget(long j, SGWidget sGWidget, long j2, SGKeyEvent sGKeyEvent);

    public static final native void SGWidget_removeAllAnimations(long j, SGWidget sGWidget);

    public static final native void SGWidget_removeAllW(long j, SGWidget sGWidget);

    public static final native void SGWidget_removeAnimation(long j, SGWidget sGWidget, int i);

    public static final native void SGWidget_removeW__SWIG_0(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2);

    public static final native void SGWidget_removeW__SWIG_1(long j, SGWidget sGWidget, int i);

    public static final native void SGWidget_sendToB(long j, SGWidget sGWidget);

    public static final native void SGWidget_sendWToB__SWIG_0(long j, SGWidget sGWidget, int i);

    public static final native void SGWidget_sendWToB__SWIG_1(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2);

    public static final native void SGWidget_setChildrenClipping(long j, SGWidget sGWidget, boolean z);

    public static final native void SGWidget_setFiltersOptions(long j, SGWidget sGWidget, boolean z, boolean z2);

    public static final native boolean SGWidget_setFocus(long j, SGWidget sGWidget);

    public static final native void SGWidget_setFocusable(long j, SGWidget sGWidget, boolean z);

    public static final native void SGWidget_setFocusableSwigExplicitSGWidget(long j, SGWidget sGWidget, boolean z);

    public static final native void SGWidget_setInheritOpacity(long j, SGWidget sGWidget, boolean z);

    public static final native void SGWidget_setKeyEventListener(long j, SGWidget sGWidget, long j2, SGWidgetListenerBase sGWidgetListenerBase);

    public static final native void SGWidget_setLocalTransform(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setName(long j, SGWidget sGWidget, String str);

    public static final native void SGWidget_setOpacity(long j, SGWidget sGWidget, float f);

    public static final native void SGWidget_setPivots__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setPivots__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setPivots__SWIG_2(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setPivots__SWIG_3(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setPositionPivot__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setPositionPivot__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setPositionPivot__SWIG_2(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setPositionPivot__SWIG_3(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setPosition__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setPosition__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setPosition__SWIG_2(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setPosition__SWIG_3(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setRotationPivot__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setRotationPivot__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setRotationPivot__SWIG_2(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setRotationPivot__SWIG_3(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setRotationX(long j, SGWidget sGWidget, float f);

    public static final native void SGWidget_setRotationY(long j, SGWidget sGWidget, float f);

    public static final native void SGWidget_setRotationZ(long j, SGWidget sGWidget, float f);

    public static final native void SGWidget_setRotation__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setRotation__SWIG_1(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setRotation__SWIG_2(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setScalePivot__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setScalePivot__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setScalePivot__SWIG_2(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setScalePivot__SWIG_3(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setScale__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setScale__SWIG_1(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setScale__SWIG_2(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setScale__SWIG_3(long j, SGWidget sGWidget, float f, float f2, float f3);

    public static final native void SGWidget_setScreenShotOptions(long j, SGWidget sGWidget, boolean z, boolean z2);

    public static final native void SGWidget_setSize__SWIG_0(long j, SGWidget sGWidget, float[] fArr);

    public static final native void SGWidget_setSize__SWIG_1(long j, SGWidget sGWidget, float f, float f2);

    public static final native void SGWidget_setTouchFocusable(long j, SGWidget sGWidget, boolean z);

    public static final native void SGWidget_setTransformationHint(long j, SGWidget sGWidget, int i);

    public static final native void SGWidget_setVisibility(long j, SGWidget sGWidget, int i);

    public static final native void SGWidget_setVisibilityMask(long j, SGWidget sGWidget, long j2);

    public static final native void SGWidget_setWidgetAnimationListener(long j, SGWidget sGWidget, long j2, SGWidgetListenerBase sGWidgetListenerBase);

    public static final native void SGWidget_setWidgetTransformationListener(long j, SGWidget sGWidget, long j2, SGWidgetListenerBase sGWidgetListenerBase);

    public static final native void SGWidget_showCursor(long j, SGWidget sGWidget, float[] fArr, float[] fArr2, int i);

    public static final native void SGWidget_swapW(long j, SGWidget sGWidget, long j2, SGWidget sGWidget2, long j3, SGWidget sGWidget3);

    public static void SwigDirector_SGWidgetCanvas_clearFocus(SGWidgetCanvas jself) {
        jself.clearFocus();
    }

    public static boolean SwigDirector_SGWidgetCanvas_dispatchKeyEvent(SGWidgetCanvas jself, long event) {
        return jself.dispatchKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidgetCanvas_invalidate__SWIG_0(SGWidgetCanvas jself) {
        jself.invalidate();
    }

    public static void SwigDirector_SGWidgetCanvas_invalidate__SWIG_1(SGWidgetCanvas jself, float[] rect) {
        jself.invalidate(new RectF(rect[0], rect[1], rect[2], rect[3]));
    }

    public static boolean SwigDirector_SGWidgetCanvas_isFocusable(SGWidgetCanvas jself) {
        return jself.isFocusable();
    }

    public static boolean SwigDirector_SGWidgetCanvas_isInFocus(SGWidgetCanvas jself) {
        return jself.isInFocus();
    }

    public static void SwigDirector_SGWidgetCanvas_onDraw(SGWidgetCanvas jself, Canvas canvas) {
        jself.onDraw(canvas);
    }

    public static void SwigDirector_SGWidgetCanvas_onFocusChanged(SGWidgetCanvas jself) {
        jself.onFocusChanged();
    }

    public static boolean SwigDirector_SGWidgetCanvas_onKeyEvent(SGWidgetCanvas jself, long event) {
        return jself.onKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidgetCanvas_setCanvasScale__SWIG_0(SGWidgetCanvas jself, float[] canvasScale) {
        jself.setCanvasScale(new SGVector2f(canvasScale));
    }

    public static void SwigDirector_SGWidgetCanvas_setFocusable(SGWidgetCanvas jself, boolean focusable) {
        jself.setFocusable(focusable);
    }

    public static void SwigDirector_SGWidgetImage_clearFocus(SGWidgetImage jself) {
        jself.clearFocus();
    }

    public static boolean SwigDirector_SGWidgetImage_dispatchKeyEvent(SGWidgetImage jself, long event) {
        return jself.dispatchKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidgetImage_invalidate__SWIG_0(SGWidgetImage jself) {
        jself.invalidate();
    }

    public static void SwigDirector_SGWidgetImage_invalidate__SWIG_1(SGWidgetImage jself, float[] rect) {
        jself.invalidate(new RectF(rect[0], rect[1], rect[2], rect[3]));
    }

    public static boolean SwigDirector_SGWidgetImage_isFocusable(SGWidgetImage jself) {
        return jself.isFocusable();
    }

    public static boolean SwigDirector_SGWidgetImage_isInFocus(SGWidgetImage jself) {
        return jself.isInFocus();
    }

    public static void SwigDirector_SGWidgetImage_onFocusChanged(SGWidgetImage jself) {
        jself.onFocusChanged();
    }

    public static boolean SwigDirector_SGWidgetImage_onKeyEvent(SGWidgetImage jself, long event) {
        return jself.onKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidgetImage_setFocusable(SGWidgetImage jself, boolean focusable) {
        jself.setFocusable(focusable);
    }

    public static void SwigDirector_SGWidgetListenerBase_onFinished(SGWidgetListenerBase jself, long widget) {
        jself.onFinished(widget);
    }

    public static boolean SwigDirector_SGWidgetListenerBase_onKeyEvent(SGWidgetListenerBase jself, long event, long widget) {
        return jself.onKeyEvent(new SGKeyEvent(event, false), widget);
    }

    public static void SwigDirector_SGWidgetListenerBase_onLocalTransformChanged(SGWidgetListenerBase jself, long widget, float[] localTransform) {
        jself.onLocalTransformChanged(widget, new SGMatrix4f(localTransform));
    }

    public static void SwigDirector_SGWidgetListenerBase_onOpacityChanged(SGWidgetListenerBase jself, long widget, float opacity) {
        jself.onOpacityChanged(widget, opacity);
    }

    public static void SwigDirector_SGWidgetListenerBase_onPositionChanged(SGWidgetListenerBase jself, long widget, float[] position) {
        jself.onPositionChanged(widget, new SGVector3f(position));
    }

    public static void SwigDirector_SGWidgetListenerBase_onRotationChanged(SGWidgetListenerBase jself, long widget, float[] rotation) {
        jself.onRotationChanged(widget, new SGQuaternion(rotation));
    }

    public static void SwigDirector_SGWidgetListenerBase_onScaleChanged(SGWidgetListenerBase jself, long widget, float[] scale) {
        jself.onScaleChanged(widget, new SGVector3f(scale));
    }

    public static void SwigDirector_SGWidgetListenerBase_onSizeChanged(SGWidgetListenerBase jself, long widget, float[] size) {
        jself.onSizeChanged(widget, new SGVector2f(size));
    }

    public static void SwigDirector_SGWidgetListenerBase_onStarted(SGWidgetListenerBase jself, long widget) {
        jself.onStarted(widget);
    }

    public static void SwigDirector_SGWidgetSurface_clearFocus(SGWidgetSurface jself) {
        jself.clearFocus();
    }

    public static boolean SwigDirector_SGWidgetSurface_dispatchKeyEvent(SGWidgetSurface jself, long event) {
        return jself.dispatchKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidgetSurface_invalidate__SWIG_0_0(SGWidgetSurface jself, float[] rect) {
        jself.invalidate(new RectF(rect[0], rect[1], rect[2], rect[3]));
    }

    public static void SwigDirector_SGWidgetSurface_invalidate__SWIG_1(SGWidgetSurface jself) {
        jself.invalidate();
    }

    public static boolean SwigDirector_SGWidgetSurface_isFocusable(SGWidgetSurface jself) {
        return jself.isFocusable();
    }

    public static boolean SwigDirector_SGWidgetSurface_isInFocus(SGWidgetSurface jself) {
        return jself.isInFocus();
    }

    public static void SwigDirector_SGWidgetSurface_onFocusChanged(SGWidgetSurface jself) {
        jself.onFocusChanged();
    }

    public static boolean SwigDirector_SGWidgetSurface_onKeyEvent(SGWidgetSurface jself, long event) {
        return jself.onKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidgetSurface_setFocusable(SGWidgetSurface jself, boolean focusable) {
        jself.setFocusable(focusable);
    }

    public static void SwigDirector_SGWidget_clearFocus(SGWidget jself) {
        jself.clearFocus();
    }

    public static boolean SwigDirector_SGWidget_dispatchKeyEvent(SGWidget jself, long event) {
        return jself.dispatchKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidget_invalidate__SWIG_0(SGWidget jself) {
        jself.invalidate();
    }

    public static void SwigDirector_SGWidget_invalidate__SWIG_1(SGWidget jself, float[] rect) {
        jself.invalidate(new RectF(rect[0], rect[1], rect[2], rect[3]));
    }

    public static boolean SwigDirector_SGWidget_isFocusable(SGWidget jself) {
        return jself.isFocusable();
    }

    public static boolean SwigDirector_SGWidget_isInFocus(SGWidget jself) {
        return jself.isInFocus();
    }

    public static void SwigDirector_SGWidget_onFocusChanged(SGWidget jself) {
        jself.onFocusChanged();
    }

    public static boolean SwigDirector_SGWidget_onKeyEvent(SGWidget jself, long event) {
        return jself.onKeyEvent(new SGKeyEvent(event, false));
    }

    public static void SwigDirector_SGWidget_setFocusable(SGWidget jself, boolean focusable) {
        jself.setFocusable(focusable);
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

    public static final native void delete_SGKeyEvent(long j);

    public static final native void delete_SGWidget(long j);

    public static final native void delete_SGWidgetCanvas(long j);

    public static final native void delete_SGWidgetDecorator(long j);

    public static final native void delete_SGWidgetImage(long j);

    public static final native void delete_SGWidgetListenerBase(long j);

    public static final native void delete_SGWidgetSurface(long j);

    static int getData(Enum param) {
        if (param != null) {
            return param.ordinal();
        }
        throw new NullPointerException();
    }

    public static final native long new_SGKeyEvent__SWIG_0();

    public static final native long new_SGKeyEvent__SWIG_1(int i, int i2, int i3);

    public static final native long new_SGKeyEvent__SWIG_2(int i, int i2);

    public static final native long new_SGKeyEvent__SWIG_3(int i, int i2, long j, String str, int i3);

    public static final native long new_SGKeyEvent__SWIG_4(int i, int i2, long j, String str);

    public static final native long new_SGWidget(float[] fArr);

    public static final native long new_SGWidgetCanvas(float[] fArr, String str);

    public static final native long new_SGWidgetDecorator(long j, SGWidget sGWidget);

    public static final native long new_SGWidgetImage(float[] fArr, int i);

    public static final native long new_SGWidgetListenerBase();

    public static final native long new_SGWidgetSurface(float[] fArr, long j);

    private static final native void swig_module_init();
}
