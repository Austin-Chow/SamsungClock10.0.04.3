package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.animation.SGAnimation;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGRay;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector2i;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGMaterial;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.ui.SGWidget;
import com.samsung.android.sdk.sgi.ui.SGWidgetDecorator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class SGLayer {
    private SGLayerAnimationListenerHolder mAnimationListener;
    ArrayList<Object> mAsyncListeners;
    protected ArrayList<SGLayer> mChildArray;
    protected SGGeometryGenerator mGeometryGenerator;
    private Object mTag;
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGLayer() {
        this(SGJNI.new_SGLayer__SWIG_0(), true);
    }

    protected SGLayer(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
        this.mChildArray = new ArrayList();
        this.mAsyncListeners = new ArrayList();
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    public SGLayer(RectF rect) {
        this(SGJNI.new_SGLayer__SWIG_1(SGMathNative.getArrayRect(rect)), true);
    }

    private int addL(SGLayer layer) {
        return SGJNI.SGLayer_addL__SWIG_0(this.swigCPtr, this, getCPtr(layer), layer);
    }

    private int addL(SGLayer layer, int index) {
        return SGJNI.SGLayer_addL__SWIG_1(this.swigCPtr, this, getCPtr(layer), layer, index);
    }

    private void asyncTraceRay(SGRay ray, long listener) {
        SGJNI.SGLayer_asyncTraceRay(this.swigCPtr, this, ray, listener);
    }

    private void bringLayerToF(int index) {
        SGJNI.SGLayer_bringLayerToF__SWIG_0(this.swigCPtr, this, index);
    }

    private void bringLayerToF(SGLayer layer) {
        SGJNI.SGLayer_bringLayerToF__SWIG_1(this.swigCPtr, this, getCPtr(layer), layer);
    }

    private void bringToF() {
        SGJNI.SGLayer_bringToF(this.swigCPtr, this);
    }

    public static long getCPtr(SGLayer obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private Class<?> getSuperclass(Class<?> cls) {
        while (true) {
            String name = cls.getCanonicalName();
            if (name != null && name.compareTo("com.samsung.android.sdk.sgi.ui.SGWidget") == 0) {
                return cls;
            }
            cls = cls.getSuperclass();
        }
    }

    private void makeScreenShot(Bitmap buffer, Rect rect, SGGraphicBufferScreenshotListenerBase listener) {
        SGJNI.SGLayer_makeScreenShot__SWIG_7(this.swigCPtr, this, buffer, SGMathNative.getArrayRect(rect), SGGraphicBufferScreenshotListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(Bitmap buffer, SGGraphicBufferScreenshotListenerBase listener) {
        SGJNI.SGLayer_makeScreenShot__SWIG_6(this.swigCPtr, this, buffer, SGGraphicBufferScreenshotListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(Rect rect, SGVector2i size, SGScreenshotPropertyListenerBase listener) {
        SGJNI.SGLayer_makeScreenShot__SWIG_10(this.swigCPtr, this, SGMathNative.getArrayRect(rect), size.getData(), SGScreenshotPropertyListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(SGVector2i size, SGScreenshotPropertyListenerBase listener) {
        SGJNI.SGLayer_makeScreenShot__SWIG_9(this.swigCPtr, this, size.getData(), SGScreenshotPropertyListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(SGScreenshotPropertyListenerBase listener) {
        SGJNI.SGLayer_makeScreenShot__SWIG_8(this.swigCPtr, this, SGScreenshotPropertyListenerBase.getCPtr(listener), listener);
    }

    private void removeAllL() {
        SGJNI.SGLayer_removeAllL(this.swigCPtr, this);
    }

    private void removeL(int index) {
        SGJNI.SGLayer_removeL__SWIG_1(this.swigCPtr, this, index);
    }

    private void removeL(SGLayer layer) {
        SGJNI.SGLayer_removeL__SWIG_0(this.swigCPtr, this, getCPtr(layer), layer);
    }

    private void sendLayerToB(int index) {
        SGJNI.SGLayer_sendLayerToB__SWIG_0(this.swigCPtr, this, index);
    }

    private void sendLayerToB(SGLayer layer) {
        SGJNI.SGLayer_sendLayerToB__SWIG_1(this.swigCPtr, this, getCPtr(layer), layer);
    }

    private void sendToB() {
        SGJNI.SGLayer_sendToB(this.swigCPtr, this);
    }

    private void setGeometryGeneratorNative(SGGeometryGenerator generator) {
        SGJNI.SGLayer_setGeometryGeneratorNative(this.swigCPtr, this, SGGeometryGenerator.getCPtr(generator), generator);
    }

    private void setLayerAnimationListener(SGLayerAnimationListenerBase listener) {
        SGJNI.SGLayer_setLayerAnimationListener(this.swigCPtr, this, SGLayerAnimationListenerBase.getCPtr(listener), listener);
    }

    private void swapL(int firstIndex, int secondIndex) {
        SGJNI.SGLayer_swapL__SWIG_1(this.swigCPtr, this, firstIndex, secondIndex);
    }

    private void swapL(SGLayer firstLayer, SGLayer secondLayer) {
        SGJNI.SGLayer_swapL__SWIG_0(this.swigCPtr, this, getCPtr(firstLayer), firstLayer, getCPtr(secondLayer), secondLayer);
    }

    public int addAnimation(SGAnimation animation) {
        return SGJNI.SGLayer_addAnimation__SWIG_0(this.swigCPtr, this, SGAnimation.getCPtr(animation), animation);
    }

    public int addAnimation(SGAnimation animation, int materialId) {
        return SGJNI.SGLayer_addAnimation__SWIG_2(this.swigCPtr, this, SGAnimation.getCPtr(animation), animation, materialId);
    }

    public int addAnimation(SGFilterPass filterPass, SGAnimation animation) {
        return SGJNI.SGLayer_addAnimation__SWIG_1(this.swigCPtr, this, SGFilterPass.getCPtr(filterPass), filterPass, SGAnimation.getCPtr(animation), animation);
    }

    public void addFilter(SGFilter filter) {
        SGJNI.SGLayer_addFilter(this.swigCPtr, this, SGFilter.getCPtr(filter), filter);
    }

    public int addLayer(SGLayer layer) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        int result = addL(layer);
        SGLayer parentLayer = layer.getParent();
        SGWidget parentWidget = layer.getParentWidget();
        if (parentLayer != null) {
            parentLayer.mChildArray.remove(layer);
        } else if (parentWidget != null) {
            try {
                Field field = getSuperclass(parentWidget.getClass()).getDeclaredField("mChildArrayLayer");
                field.setAccessible(true);
                ((ArrayList) field.get(parentWidget)).remove(layer);
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException(e.getMessage());
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        }
        this.mChildArray.add(layer);
        return result;
    }

    public int addLayer(SGLayer layer, int index) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        int result = addL(layer, index);
        SGLayer parentLayer = layer.getParent();
        SGWidget parentWidget = layer.getParentWidget();
        if (parentLayer != null) {
            parentLayer.mChildArray.remove(layer);
        } else if (parentWidget != null) {
            try {
                Field field = getSuperclass(parentWidget.getClass()).getDeclaredField("mChildArrayLayer");
                field.setAccessible(true);
                ((ArrayList) field.get(parentWidget)).remove(layer);
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException(e.getMessage());
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        }
        this.mChildArray.add(index, layer);
        return result;
    }

    public void addMaterial(SGMaterial material) {
        SGJNI.SGLayer_addMaterial(this.swigCPtr, this, SGMaterial.getCPtr(material), material);
    }

    public void asyncTraceRay(SGRay ray, SGTraceRayListener listener) {
        SGTraceRayListenerHolder traceRayListenerHolder = new SGTraceRayListenerHolder(this.mAsyncListeners, listener);
        asyncTraceRay(ray, SGTraceRayListenerBase.getCPtr(traceRayListenerHolder));
        this.mAsyncListeners.add(traceRayListenerHolder);
    }

    public void bringLayerToFront(int index) {
        SGLayer layer = (SGLayer) this.mChildArray.get(index);
        SGLayer parent = layer.getParent();
        bringLayerToF(index);
        parent.mChildArray.remove(index);
        this.mChildArray.add(layer);
    }

    public void bringLayerToFront(SGLayer layer) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        bringLayerToF(layer);
        SGLayer parent = layer.getParent();
        if (parent != null) {
            parent.mChildArray.remove(layer);
        }
        this.mChildArray.add(layer);
    }

    public void bringToFront() {
        SGLayer parent = getParent();
        if (parent != null) {
            bringToF();
            parent.mChildArray.remove(this);
            parent.mChildArray.add(this);
            return;
        }
        SGWidget parentW = getParentWidget();
        if (parentW != null) {
            bringToF();
            new SGWidgetDecorator(parentW).bringLayerToFront(this);
        }
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGLayer(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGLayer findLayerById(int id) {
        return SGJNI.SGLayer_findLayerById(this.swigCPtr, this, id);
    }

    public SGLayer findLayerByName(String name) {
        return SGJNI.SGLayer_findLayerByName(this.swigCPtr, this, name);
    }

    @Deprecated
    public long getCameraVisibilityMask() {
        return getVisibilityMask();
    }

    public SGFilter getFilter(int index) {
        return (SGFilter) SGJNI.createObjectFromNativePtr(SGFilter.class, SGJNI.SGLayer_getFilter(this.swigCPtr, this, index), true);
    }

    public int getFilterCount() {
        return SGJNI.SGLayer_getFilterCount(this.swigCPtr, this);
    }

    public SGMatrix4f getFullTransform() {
        return new SGMatrix4f(SGJNI.SGLayer_getFullTransform(this.swigCPtr, this));
    }

    public SGGeometryGenerator getGeometryGenerator() {
        return this.mGeometryGenerator;
    }

    public float getGeometryGeneratorParam() {
        return SGJNI.SGLayer_getGeometryGeneratorParam(this.swigCPtr, this);
    }

    public SGLayer getLayer(int index) {
        return (SGLayer) this.mChildArray.get(index);
    }

    public SGLayer getLayer(String name) {
        int count = this.mChildArray.size();
        for (int i = 0; i < count; i++) {
            SGLayer layer = (SGLayer) this.mChildArray.get(i);
            if (layer.getName().contentEquals(name)) {
                return layer;
            }
        }
        return null;
    }

    public SGLayerAnimationListener getLayerAnimationListener() {
        return this.mAnimationListener == null ? null : this.mAnimationListener.getInterface();
    }

    public SGLayer getLayerAtPoint(SGVector2f point) {
        return SGJNI.SGLayer_getLayerAtPoint(this.swigCPtr, this, point.getData());
    }

    public int getLayerIndex(SGLayer layer) {
        return this.mChildArray.indexOf(layer);
    }

    public int getLayersCount() {
        return this.mChildArray.size();
    }

    public SGMatrix4f getLocalTransform() {
        return new SGMatrix4f(SGJNI.SGLayer_getLocalTransform(this.swigCPtr, this));
    }

    public SGVector2f getLocationInWindow(float x, float y) {
        return new SGVector2f(SGJNI.SGLayer_getLocationInWindow__SWIG_1(this.swigCPtr, this, x, y));
    }

    public SGVector2f getLocationInWindow(SGVector2f point) {
        return new SGVector2f(SGJNI.SGLayer_getLocationInWindow__SWIG_0(this.swigCPtr, this, point.getData()));
    }

    public SGMaterial getMaterial(int index) {
        return (SGMaterial) SGJNI.createObjectFromNativePtr(SGMaterial.class, SGJNI.SGLayer_getMaterial(this.swigCPtr, this, index), true);
    }

    public int getMaterialsCount() {
        return SGJNI.SGLayer_getMaterialsCount(this.swigCPtr, this);
    }

    public String getName() {
        return SGJNI.SGLayer_getName(this.swigCPtr, this);
    }

    public float getOpacity() {
        return SGJNI.SGLayer_getOpacity(this.swigCPtr, this);
    }

    public SGLayer getParent() {
        return SGJNI.SGLayer_getParent(this.swigCPtr, this);
    }

    public RectF getParentBounds() {
        return new RectF(SGJNI.SGLayer_getParentBounds(this.swigCPtr, this)[0], SGJNI.SGLayer_getParentBounds(this.swigCPtr, this)[1], SGJNI.SGLayer_getParentBounds(this.swigCPtr, this)[2], SGJNI.SGLayer_getParentBounds(this.swigCPtr, this)[3]);
    }

    public SGWidget getParentWidget() {
        return SGJNI.SGLayer_getParentWidget(this.swigCPtr, this);
    }

    public SGVector2f getPosition() {
        return new SGVector2f(SGJNI.SGLayer_getPosition(this.swigCPtr, this));
    }

    public SGVector3f getPosition3f() {
        return new SGVector3f(SGJNI.SGLayer_getPosition3f(this.swigCPtr, this));
    }

    public SGVector2f getPositionPivot() {
        return new SGVector2f(SGJNI.SGLayer_getPositionPivot(this.swigCPtr, this));
    }

    public SGVector3f getPositionPivot3f() {
        return new SGVector3f(SGJNI.SGLayer_getPositionPivot3f(this.swigCPtr, this));
    }

    public SGShaderProgramProperty getProgramProperty() {
        return SGJNI.SGLayer_getProgramProperty(this.swigCPtr, this);
    }

    public SGProperty getProperty(int index) {
        return SGJNI.SGLayer_getProperty__SWIG_0(this.swigCPtr, this, index);
    }

    public SGProperty getProperty(String name) {
        return SGJNI.SGLayer_getProperty__SWIG_1(this.swigCPtr, this, name);
    }

    public int getPropertyCount() {
        return SGJNI.SGLayer_getPropertyCount(this.swigCPtr, this);
    }

    public String getPropertyName(int index) {
        return SGJNI.SGLayer_getPropertyName(this.swigCPtr, this, index);
    }

    public SGMatrix4f getRelativeToAnotherParentTransform(SGLayer parent) {
        return new SGMatrix4f(SGJNI.SGLayer_getRelativeToAnotherParentTransform(this.swigCPtr, this, getCPtr(parent), parent));
    }

    public SGMatrix4f getRelativeTransform(SGLayer parent) {
        return new SGMatrix4f(SGJNI.SGLayer_getRelativeTransform(this.swigCPtr, this, getCPtr(parent), parent));
    }

    public SGQuaternion getRotation() {
        return new SGQuaternion(SGJNI.SGLayer_getRotation(this.swigCPtr, this));
    }

    public SGVector3f getRotation3f() {
        return new SGVector3f(SGJNI.SGLayer_getRotation3f(this.swigCPtr, this));
    }

    public SGVector2f getRotationPivot() {
        return new SGVector2f(SGJNI.SGLayer_getRotationPivot(this.swigCPtr, this));
    }

    public SGVector3f getRotationPivot3f() {
        return new SGVector3f(SGJNI.SGLayer_getRotationPivot3f(this.swigCPtr, this));
    }

    public SGVector2f getScale() {
        return new SGVector2f(SGJNI.SGLayer_getScale(this.swigCPtr, this));
    }

    public SGVector3f getScale3f() {
        return new SGVector3f(SGJNI.SGLayer_getScale3f(this.swigCPtr, this));
    }

    public SGVector2f getScalePivot() {
        return new SGVector2f(SGJNI.SGLayer_getScalePivot(this.swigCPtr, this));
    }

    public SGVector3f getScalePivot3f() {
        return new SGVector3f(SGJNI.SGLayer_getScalePivot3f(this.swigCPtr, this));
    }

    public RectF getScreenBounds() {
        return new RectF(SGJNI.SGLayer_getScreenBounds(this.swigCPtr, this)[0], SGJNI.SGLayer_getScreenBounds(this.swigCPtr, this)[1], SGJNI.SGLayer_getScreenBounds(this.swigCPtr, this)[2], SGJNI.SGLayer_getScreenBounds(this.swigCPtr, this)[3]);
    }

    public SGVector2f getSize() {
        return new SGVector2f(SGJNI.SGLayer_getSize(this.swigCPtr, this));
    }

    public SGSurface getSurface() {
        return SGJNI.SGLayer_getSurface(this.swigCPtr, this);
    }

    public Object getTag() {
        return this.mTag;
    }

    public SGTransformationHints getTransformationHint() {
        return ((SGTransformationHints[]) SGTransformationHints.class.getEnumConstants())[SGJNI.SGLayer_getTransformationHint(this.swigCPtr, this)];
    }

    public boolean getVisibility() {
        return SGJNI.SGLayer_getVisibility(this.swigCPtr, this);
    }

    public long getVisibilityMask() {
        return SGJNI.SGLayer_getVisibilityMask(this.swigCPtr, this);
    }

    public boolean isAddedToSurface() {
        return SGJNI.SGLayer_isAddedToSurface(this.swigCPtr, this);
    }

    public boolean isChildrenClippingEnabled() {
        return SGJNI.SGLayer_isChildrenClippingEnabled(this.swigCPtr, this);
    }

    public boolean isFiltersAttachDepthBuffer() {
        return SGJNI.SGLayer_isFiltersAttachDepthBuffer(this.swigCPtr, this);
    }

    public boolean isFiltersAttachStencilBuffer() {
        return SGJNI.SGLayer_isFiltersAttachStencilBuffer(this.swigCPtr, this);
    }

    public boolean isInheritOpacity() {
        return SGJNI.SGLayer_isInheritOpacity(this.swigCPtr, this);
    }

    public boolean isScreenShotAttachDepthBuffer() {
        return SGJNI.SGLayer_isScreenShotAttachDepthBuffer(this.swigCPtr, this);
    }

    public boolean isScreenShotAttachStencilBuffer() {
        return SGJNI.SGLayer_isScreenShotAttachStencilBuffer(this.swigCPtr, this);
    }

    public SGProperty makeScreenShot() {
        return SGJNI.SGLayer_makeScreenShot__SWIG_2(this.swigCPtr, this);
    }

    public SGProperty makeScreenShot(Rect rect) {
        return SGJNI.SGLayer_makeScreenShot__SWIG_4(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public SGProperty makeScreenShot(Rect rect, SGVector2i size) {
        return SGJNI.SGLayer_makeScreenShot__SWIG_5(this.swigCPtr, this, SGMathNative.getArrayRect(rect), size.getData());
    }

    public SGProperty makeScreenShot(SGVector2i size) {
        return SGJNI.SGLayer_makeScreenShot__SWIG_3(this.swigCPtr, this, size.getData());
    }

    public void makeScreenShot(Bitmap result) {
        SGJNI.SGLayer_makeScreenShot__SWIG_0(this.swigCPtr, this, result);
    }

    public void makeScreenShot(Bitmap result, Rect rect) {
        SGJNI.SGLayer_makeScreenShot__SWIG_1(this.swigCPtr, this, result, SGMathNative.getArrayRect(rect));
    }

    public void makeScreenShot(Bitmap bitmap, Rect rect, SGBitmapScreenshotListener listener) {
        SGGraphicBufferScreenshotListenerBase holder = new SGBitmapScreenshotListenerHolder(this.mAsyncListeners, listener);
        makeScreenShot(bitmap, rect, holder);
        this.mAsyncListeners.add(holder);
    }

    public void makeScreenShot(Bitmap bitmap, SGBitmapScreenshotListener listener) {
        SGGraphicBufferScreenshotListenerBase holder = new SGBitmapScreenshotListenerHolder(this.mAsyncListeners, listener);
        makeScreenShot(bitmap, holder);
        this.mAsyncListeners.add(holder);
    }

    public void makeScreenShot(Rect rect, SGVector2i size, SGPropertyScreenshotListener listener) {
        SGScreenshotPropertyListenerBase holder = new SGPropertyScreenshotListenerHolder(this.mAsyncListeners, listener);
        makeScreenShot(rect, size, holder);
        this.mAsyncListeners.add(holder);
    }

    public void makeScreenShot(SGVector2i size, SGPropertyScreenshotListener listener) {
        SGScreenshotPropertyListenerBase holder = new SGPropertyScreenshotListenerHolder(this.mAsyncListeners, listener);
        makeScreenShot(size, holder);
        this.mAsyncListeners.add(holder);
    }

    public void makeScreenShot(SGPropertyScreenshotListener listener) {
        SGScreenshotPropertyListenerBase holder = new SGPropertyScreenshotListenerHolder(this.mAsyncListeners, listener);
        makeScreenShot(holder);
        this.mAsyncListeners.add(holder);
    }

    public void removeAllAnimations() {
        SGJNI.SGLayer_removeAllAnimations(this.swigCPtr, this);
    }

    public void removeAllFilters() {
        SGJNI.SGLayer_removeAllFilters(this.swigCPtr, this);
    }

    public void removeAllLayers() {
        if (!this.mChildArray.isEmpty()) {
            removeAllL();
            this.mChildArray.clear();
        }
    }

    public void removeAllMaterials() {
        SGJNI.SGLayer_removeAllMaterials(this.swigCPtr, this);
    }

    public void removeAnimation(int id) {
        SGJNI.SGLayer_removeAnimation(this.swigCPtr, this, id);
    }

    public void removeFilter(int index) {
        SGJNI.SGLayer_removeFilter__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeFilter(SGFilter filter) {
        SGJNI.SGLayer_removeFilter__SWIG_0(this.swigCPtr, this, SGFilter.getCPtr(filter), filter);
    }

    public void removeLayer(int index) {
        removeL(index);
        this.mChildArray.remove(index);
    }

    public void removeLayer(SGLayer layer) {
        removeL(layer);
        this.mChildArray.remove(layer);
    }

    public void removeMaterial(int index) {
        SGJNI.SGLayer_removeMaterial__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeMaterial(SGMaterial material) {
        SGJNI.SGLayer_removeMaterial__SWIG_0(this.swigCPtr, this, SGMaterial.getCPtr(material), material);
    }

    public void removeProperty(int index) {
        SGJNI.SGLayer_removeProperty__SWIG_0(this.swigCPtr, this, index);
    }

    public void removeProperty(String name) {
        SGJNI.SGLayer_removeProperty__SWIG_1(this.swigCPtr, this, name);
    }

    public void resetFilterFrameBufferSize() {
        SGJNI.SGLayer_resetFilterFrameBufferSize(this.swigCPtr, this);
    }

    public void sendLayerToBack(int index) {
        SGLayer layer = (SGLayer) this.mChildArray.get(index);
        SGLayer parent = layer.getParent();
        sendLayerToB(index);
        parent.mChildArray.remove(index);
        this.mChildArray.add(0, layer);
    }

    public void sendLayerToBack(SGLayer layer) {
        if (layer == null) {
            throw new NullPointerException("parameter layer is null");
        }
        sendLayerToB(layer);
        SGLayer parent = layer.getParent();
        if (parent != null) {
            parent.mChildArray.remove(layer);
        }
        this.mChildArray.add(0, layer);
    }

    public void sendToBack() {
        SGLayer parent = getParent();
        if (parent != null) {
            sendToB();
            parent.mChildArray.remove(this);
            parent.mChildArray.add(0, this);
            return;
        }
        SGWidget parentW = getParentWidget();
        if (parentW != null) {
            sendToB();
            new SGWidgetDecorator(parentW).sendLayerToBack(this);
        }
    }

    @Deprecated
    public void setCameraVisibilityMask(long mask) {
        setVisibilityMask(mask);
    }

    public void setChildrenClipping(boolean enabled) {
        SGJNI.SGLayer_setChildrenClipping(this.swigCPtr, this, enabled);
    }

    public void setFilterFrameBufferSize(float width, float height) {
        SGJNI.SGLayer_setFilterFrameBufferSize(this.swigCPtr, this, width, height);
    }

    public void setFiltersOptions(boolean depth, boolean stencil) {
        SGJNI.SGLayer_setFiltersOptions(this.swigCPtr, this, depth, stencil);
    }

    public void setGeometryGenerator(SGGeometryGenerator generator) {
        setGeometryGeneratorNative(generator);
        this.mGeometryGenerator = generator;
    }

    public void setGeometryGeneratorParam(float param) {
        SGJNI.SGLayer_setGeometryGeneratorParam(this.swigCPtr, this, param);
    }

    public void setInheritOpacity(boolean enabled) {
        SGJNI.SGLayer_setInheritOpacity(this.swigCPtr, this, enabled);
    }

    public void setLayerAnimationListener(SGLayerAnimationListener listener) {
        if (listener == null) {
            this.mAnimationListener = null;
            setLayerAnimationListener((SGLayerAnimationListenerHolder) null);
            return;
        }
        if (this.mAnimationListener == null) {
            this.mAnimationListener = new SGLayerAnimationListenerHolder(this);
            setLayerAnimationListener(this.mAnimationListener);
        }
        this.mAnimationListener.setInterface(listener);
    }

    public void setLocalTransform(SGMatrix4f value) {
        SGJNI.SGLayer_setLocalTransform(this.swigCPtr, this, value.getData());
    }

    public void setName(String name) {
        SGJNI.SGLayer_setName(this.swigCPtr, this, name);
    }

    public void setOpacity(float opacity) {
        SGJNI.SGLayer_setOpacity(this.swigCPtr, this, opacity);
    }

    public void setPivots(float x, float y) {
        SGJNI.SGLayer_setPivots__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setPivots(float x, float y, float z) {
        SGJNI.SGLayer_setPivots__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setPivots(SGVector2f pivot) {
        SGJNI.SGLayer_setPivots__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setPivots(SGVector3f pivot) {
        SGJNI.SGLayer_setPivots__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setPosition(float x, float y) {
        SGJNI.SGLayer_setPosition__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setPosition(float x, float y, float z) {
        SGJNI.SGLayer_setPosition__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setPosition(SGVector2f position) {
        SGJNI.SGLayer_setPosition__SWIG_0(this.swigCPtr, this, position.getData());
    }

    public void setPosition(SGVector3f position) {
        SGJNI.SGLayer_setPosition__SWIG_1(this.swigCPtr, this, position.getData());
    }

    public void setPositionPivot(float x, float y) {
        SGJNI.SGLayer_setPositionPivot__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setPositionPivot(float x, float y, float z) {
        SGJNI.SGLayer_setPositionPivot__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setPositionPivot(SGVector2f pivot) {
        SGJNI.SGLayer_setPositionPivot__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setPositionPivot(SGVector3f pivot) {
        SGJNI.SGLayer_setPositionPivot__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setProgramProperty(SGShaderProgramProperty property) {
        SGJNI.SGLayer_setProgramProperty(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }

    public void setProperty(String name, SGProperty property) {
        SGJNI.SGLayer_setProperty(this.swigCPtr, this, name, SGProperty.getCPtr(property), property);
    }

    public void setRotation(float pitch, float yaw, float roll) {
        SGJNI.SGLayer_setRotation__SWIG_1(this.swigCPtr, this, pitch, yaw, roll);
    }

    public void setRotation(SGQuaternion rotation) {
        SGJNI.SGLayer_setRotation__SWIG_0(this.swigCPtr, this, rotation.getData());
    }

    public void setRotationPivot(float x, float y) {
        SGJNI.SGLayer_setRotationPivot__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setRotationPivot(float x, float y, float z) {
        SGJNI.SGLayer_setRotationPivot__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setRotationPivot(SGVector2f pivot) {
        SGJNI.SGLayer_setRotationPivot__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setRotationPivot(SGVector3f pivot) {
        SGJNI.SGLayer_setRotationPivot__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setRotationX(float angle) {
        SGJNI.SGLayer_setRotationX(this.swigCPtr, this, angle);
    }

    public void setRotationY(float angle) {
        SGJNI.SGLayer_setRotationY(this.swigCPtr, this, angle);
    }

    public void setRotationZ(float angle) {
        SGJNI.SGLayer_setRotationZ(this.swigCPtr, this, angle);
    }

    public void setScale(float xScale, float yScale) {
        SGJNI.SGLayer_setScale__SWIG_2(this.swigCPtr, this, xScale, yScale);
    }

    public void setScale(float xScale, float yScale, float zScale) {
        SGJNI.SGLayer_setScale__SWIG_3(this.swigCPtr, this, xScale, yScale, zScale);
    }

    public void setScale(SGVector2f scale) {
        SGJNI.SGLayer_setScale__SWIG_0(this.swigCPtr, this, scale.getData());
    }

    public void setScale(SGVector3f scale) {
        SGJNI.SGLayer_setScale__SWIG_1(this.swigCPtr, this, scale.getData());
    }

    public void setScalePivot(float x, float y) {
        SGJNI.SGLayer_setScalePivot__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setScalePivot(float x, float y, float z) {
        SGJNI.SGLayer_setScalePivot__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setScalePivot(SGVector2f pivot) {
        SGJNI.SGLayer_setScalePivot__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setScalePivot(SGVector3f pivot) {
        SGJNI.SGLayer_setScalePivot__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setScreenShotOptions(boolean attachDepthBuffer, boolean attachStencilBuffer) {
        SGJNI.SGLayer_setScreenShotOptions(this.swigCPtr, this, attachDepthBuffer, attachStencilBuffer);
    }

    public void setSize(float width, float height) {
        SGJNI.SGLayer_setSize__SWIG_1(this.swigCPtr, this, width, height);
    }

    public void setSize(SGVector2f size) {
        SGJNI.SGLayer_setSize__SWIG_0(this.swigCPtr, this, size.getData());
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public void setTransformationHint(SGTransformationHints hint) {
        SGJNI.SGLayer_setTransformationHint(this.swigCPtr, this, hint.ordinal());
    }

    public void setVisibility(boolean enabled) {
        SGJNI.SGLayer_setVisibility(this.swigCPtr, this, enabled);
    }

    public void setVisibilityMask(long mask) {
        SGJNI.SGLayer_setVisibilityMask(this.swigCPtr, this, mask);
    }

    @Deprecated
    public void swapLayers(int firstIndex, int secondIndex) {
        swapL(firstIndex, secondIndex);
        Collections.swap(this.mChildArray, firstIndex, secondIndex);
    }

    public void swapLayers(SGLayer firstLayer, SGLayer secondLayer) {
        swapL(firstLayer, secondLayer);
        Collections.swap(this.mChildArray, this.mChildArray.indexOf(firstLayer), this.mChildArray.indexOf(secondLayer));
    }

    public void traceRay(SGRay ray, SGTraceRayListener listener) {
        SGTraceRayListenerHolder traceRayListenerHolder = new SGTraceRayListenerHolder(null, listener);
        try {
            SGJNI.SGLayer_traceRay(this.swigCPtr, this, ray, SGTraceRayListenerBase.getCPtr(traceRayListenerHolder));
        } finally {
            traceRayListenerHolder.onCompleted();
        }
    }
}
