package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGRay;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector2i;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.ui.SGKeyEvent;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent.SGAction;
import com.samsung.android.sdk.sgi.ui.SGWidget;
import java.util.ArrayList;

public final class SGSurface {
    ArrayList<Object> mAsyncListeners;
    private SGLayerCamera mDefaultCamera;
    private SGWidget mHoverCapturedWidget;
    private SGLayer mRootLayer;
    SGSurfaceListenerHolder mSurfaceListener;
    private SGWidget mTouchCapturedWidget;
    private View mView;
    private boolean mWidgetListChanged;
    private ArrayList<SGWidget> mWidgets;
    private boolean swigCMemOwn;
    private long swigCPtr;

    private SGSurface(long cPtr, boolean cMemoryOwn) {
        this.mWidgetListChanged = false;
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
        this.mSurfaceListener = new SGSurfaceListenerHolder();
        this.mAsyncListeners = new ArrayList();
        this.mWidgets = new ArrayList();
    }

    SGSurface(View view, SGVector2f size, SGContextConfiguration contextConfiguration) {
        this(size, contextConfiguration);
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
        this.mRootLayer = getRootNative();
        this.mDefaultCamera = getDefaultCameraNative();
        this.mView = view;
    }

    private SGSurface(SGVector2f size, SGContextConfiguration contextConfiguration) {
        this(SGJNI.new_SGSurface(size.getData(), contextConfiguration), true);
    }

    private int addW(SGWidget widget) {
        return SGJNI.SGSurface_addW__SWIG_0(this.swigCPtr, this, SGWidget.getCPtr(widget), widget);
    }

    private int addW(SGWidget widget, int index) {
        return SGJNI.SGSurface_addW__SWIG_1(this.swigCPtr, this, SGWidget.getCPtr(widget), widget, index);
    }

    private void asyncTraceRay(SGRay ray, long listener) {
        SGJNI.SGSurface_asyncTraceRay(this.swigCPtr, this, ray, listener);
    }

    private void bringWToF(SGWidget widget) {
        SGJNI.SGSurface_bringWToF(this.swigCPtr, this, SGWidget.getCPtr(widget), widget);
    }

    private final boolean checkOnTouchUp(SGTouchEvent ev) {
        int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            SGAction action = ev.getAction(i);
            if (action != SGAction.UP && action != SGAction.CANCEL) {
                return false;
            }
        }
        return true;
    }

    private final boolean dispatchHoverEvent(SGTouchEvent ev) {
        SGTouchEvent event = SGTouchEvent.create(ev);
        int count = event.getPointerCount();
        for (int i = this.mWidgets.size(); i > 0; i--) {
            SGWidget widget = (SGWidget) this.mWidgets.get(i - 1);
            boolean validRegion = true;
            int j = 0;
            while (j < count) {
                try {
                    SGVector2f localData = widget.getLocationInWindow(event.getRawX(j), event.getRawY(j));
                    validRegion = validRegion && widget.isLocalPointOnWidget(localData);
                    if (!validRegion) {
                        break;
                    }
                    event.setX(j, localData.getX());
                    event.setY(j, localData.getY());
                    j++;
                } catch (Exception e) {
                    SGTouchEvent.recycle(event);
                    return false;
                }
            }
            boolean handled = false;
            if (validRegion) {
                if (widget.equals(this.mHoverCapturedWidget)) {
                    handled = this.mHoverCapturedWidget.dispatchHoverEvent(event);
                    if (!handled) {
                        this.mHoverCapturedWidget = null;
                    }
                } else {
                    SGAction action = event.getAction();
                    event.setAction(0, SGAction.HOVER_ENTER);
                    handled = widget.dispatchHoverEvent(event);
                    if (action != SGAction.HOVER_ENTER) {
                        event.setAction(0, action);
                        handled = handled && widget.dispatchHoverEvent(event);
                    }
                    if (handled) {
                        if (this.mHoverCapturedWidget != null) {
                            SGTouchEvent exitEvent = SGTouchEvent.create(event);
                            exitEvent.setAction(0, SGAction.HOVER_EXIT);
                            this.mHoverCapturedWidget.dispatchHoverEvent(exitEvent);
                            SGTouchEvent.recycle(exitEvent);
                        }
                        this.mHoverCapturedWidget = widget;
                    }
                }
            }
            if (handled) {
                SGTouchEvent.recycle(event);
                return true;
            }
        }
        if (this.mHoverCapturedWidget != null) {
            exitEvent = SGTouchEvent.create(event);
            exitEvent.setAction(0, SGAction.HOVER_EXIT);
            this.mHoverCapturedWidget.dispatchHoverEvent(exitEvent);
            SGTouchEvent.recycle(exitEvent);
            this.mHoverCapturedWidget = null;
        }
        SGTouchEvent.recycle(event);
        return false;
    }

    private final boolean dispatchTouchEvent(SGTouchEvent ev) {
        boolean result = false;
        SGAction action = ev.getAction();
        if (action == SGAction.DOWN) {
            signalTouchEvent(true);
        } else if (action == SGAction.UP) {
            signalTouchEvent(false);
        }
        if (this.mTouchCapturedWidget != null) {
            SGTouchEvent event = this.mTouchCapturedWidget.getTouchEventInLocalWindow(ev);
            result = this.mTouchCapturedWidget.dispatchTouchEvent(event);
            SGTouchEvent.recycle(event);
        } else if (action == SGAction.DOWN) {
            result = dispatchTouchEventToChildren(ev);
        }
        if (this.mTouchCapturedWidget != null && checkOnTouchUp(ev)) {
            uncaptureWidget();
        }
        return result;
    }

    private final boolean dispatchTouchEventToChildren(SGTouchEvent ev) {
        SGTouchEvent event = SGTouchEvent.create(ev);
        int count = event.getPointerCount();
        int widgetsCount = this.mWidgets.size();
        this.mWidgetListChanged = false;
        for (int i = widgetsCount; i > 0 && !this.mWidgetListChanged; i--) {
            SGWidget widget = (SGWidget) this.mWidgets.get(i - 1);
            if (widget.isVisible()) {
                boolean validRegion = true;
                for (int j = 0; j < count; j++) {
                    try {
                        SGVector2f localData = widget.getLocationInWindow(event.getRawX(j), event.getRawY(j));
                        if (widget.isLocalPointOnWidget(localData)) {
                            event.setX(j, localData.getX());
                            event.setY(j, localData.getY());
                        } else {
                            validRegion = false;
                        }
                    } catch (Exception e) {
                        validRegion = false;
                    }
                    if (!validRegion) {
                        break;
                    }
                }
                if (validRegion && widget.dispatchTouchEvent(event)) {
                    this.mTouchCapturedWidget = widget;
                    SGTouchEvent.recycle(event);
                    return true;
                }
            }
        }
        SGTouchEvent.recycle(event);
        return false;
    }

    private void enqueueTexture(Bitmap bitmap, boolean autoRecycle, SGBackgroundPropertyListenerBase listener, int id) {
        SGJNI.SGSurface_enqueueTexture(this.swigCPtr, this, bitmap, autoRecycle, SGBackgroundPropertyListenerBase.getCPtr(listener), listener, id);
    }

    public static long getCPtr(SGSurface obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private SGLayerCamera getDefaultCameraNative() {
        long cPtr = SGJNI.SGSurface_getDefaultCameraNative(this.swigCPtr, this);
        return cPtr == 0 ? null : new SGLayerCamera(cPtr, false);
    }

    private SGLayer getRootNative() {
        long cPtr = SGJNI.SGSurface_getRootNative(this.swigCPtr, this);
        return cPtr == 0 ? null : new SGLayer(cPtr, false);
    }

    private void makeScreenShot(Bitmap buffer, Rect rect, SGGraphicBufferScreenshotListenerBase listener) {
        SGJNI.SGSurface_makeScreenShot__SWIG_7(this.swigCPtr, this, buffer, SGMathNative.getArrayRect(rect), SGGraphicBufferScreenshotListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(Bitmap buffer, SGGraphicBufferScreenshotListenerBase listener) {
        SGJNI.SGSurface_makeScreenShot__SWIG_6(this.swigCPtr, this, buffer, SGGraphicBufferScreenshotListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(Rect rect, SGVector2i size, SGScreenshotPropertyListenerBase listener) {
        SGJNI.SGSurface_makeScreenShot__SWIG_10(this.swigCPtr, this, SGMathNative.getArrayRect(rect), size.getData(), SGScreenshotPropertyListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(SGVector2i size, SGScreenshotPropertyListenerBase listener) {
        SGJNI.SGSurface_makeScreenShot__SWIG_9(this.swigCPtr, this, size.getData(), SGScreenshotPropertyListenerBase.getCPtr(listener), listener);
    }

    private void makeScreenShot(SGScreenshotPropertyListenerBase listener) {
        SGJNI.SGSurface_makeScreenShot__SWIG_8(this.swigCPtr, this, SGScreenshotPropertyListenerBase.getCPtr(listener), listener);
    }

    private final void removeCapturing(SGWidget widget) {
        if (widget.equals(this.mHoverCapturedWidget)) {
            unhoverWidget();
        }
    }

    private void removeW(int index) {
        SGJNI.SGSurface_removeW__SWIG_1(this.swigCPtr, this, index);
    }

    private void removeW(SGWidget widget) {
        SGJNI.SGSurface_removeW__SWIG_0(this.swigCPtr, this, SGWidget.getCPtr(widget), widget);
    }

    private void sendWToB(SGWidget widget) {
        SGJNI.SGSurface_sendWToB(this.swigCPtr, this, SGWidget.getCPtr(widget), widget);
    }

    private void setDrawFrameListener(SGSurfaceListenerBase listener) {
        SGJNI.SGSurface_setDrawFrameListener(this.swigCPtr, this, SGSurfaceListenerBase.getCPtr(listener), listener);
    }

    private void setSizeChangeListener(SGSurfaceListenerBase listener) {
        SGJNI.SGSurface_setSizeChangeListener(this.swigCPtr, this, SGSurfaceListenerBase.getCPtr(listener), listener);
    }

    public void addChangesDrawnListener(SGSurfaceChangesDrawnListener listener) {
        SGSurfaceChangesDrawnListenerBase holder = new SGSurfaceChangesDrawnListenerHolder(this.mAsyncListeners, listener);
        addChangesDrawnListener(holder);
        this.mAsyncListeners.add(holder);
    }

    public void addChangesDrawnListener(SGSurfaceChangesDrawnListenerBase listener) {
        SGJNI.SGSurface_addChangesDrawnListener(this.swigCPtr, this, SGSurfaceChangesDrawnListenerBase.getCPtr(listener), listener);
    }

    public int addLayer(SGLayer layer) {
        return this.mRootLayer.addLayer(layer);
    }

    public int addLayer(SGLayer layer, int index) {
        return this.mRootLayer.addLayer(layer, index);
    }

    public int addWidget(SGWidget widget) {
        SGWidget parent = widget.getParent();
        int id = addW(widget);
        if (parent != null) {
            parent.removeWidget(widget);
        } else {
            this.mWidgets.remove(widget);
        }
        this.mWidgets.add(widget);
        this.mWidgetListChanged = true;
        return id;
    }

    public int addWidget(SGWidget widget, int index) {
        SGWidget parent = widget.getParent();
        int id = addW(widget, index);
        if (parent != null) {
            parent.removeWidget(widget);
        } else {
            this.mWidgets.remove(widget);
        }
        this.mWidgets.add(index, widget);
        this.mWidgetListChanged = true;
        return id;
    }

    public void asyncTraceRay(SGRay ray, SGTraceRayListener listener) {
        SGTraceRayListenerHolder traceRayListenerHolder = new SGTraceRayListenerHolder(this.mAsyncListeners, listener);
        asyncTraceRay(ray, SGTraceRayListenerBase.getCPtr(traceRayListenerHolder));
        this.mAsyncListeners.add(traceRayListenerHolder);
    }

    public void bringWidgetToFront(SGWidget widget) {
        if (widget != null && getWidgetIndex(widget) != -1) {
            bringWToF(widget);
            this.mWidgets.remove(widget);
            this.mWidgets.add(widget);
            this.mWidgetListChanged = true;
        }
    }

    public void destroy() {
        SGJNI.SGSurface_destroy(this.swigCPtr, this);
    }

    public void dumpProfilingInformation(SGProfilingInformationType type) {
        SGJNI.SGSurface_dumpProfilingInformation(this.swigCPtr, this, SGJNI.getData(type));
    }

    public void enqueueTexture(Bitmap bitmap, SGBackgroundPropertyListener listener, int id) {
        enqueueTexture(bitmap, false, listener, id);
    }

    public void enqueueTexture(Bitmap bitmap, boolean autoRecycle, SGBackgroundPropertyListener listener, int id) {
        SGBackgroundPropertyListenerBase holder = new SGBackgorundPropertyListenerHolder(this.mAsyncListeners, listener);
        enqueueTexture(bitmap, autoRecycle, holder, id);
        this.mAsyncListeners.add(holder);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGSurface(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public int getAffinityMask() {
        return SGJNI.SGSurface_getAffinityMask(this.swigCPtr, this);
    }

    public SGLayerCamera getDefaultCamera() {
        return this.mDefaultCamera;
    }

    public SGDrawFrameListener getDrawFrameListener() {
        return this.mSurfaceListener.mDrawFrameListener;
    }

    public SGFpsIndicator getFpsIndicator() {
        return SGJNI.SGSurface_getFpsIndicator(this.swigCPtr, this);
    }

    public int getFpsLimit() {
        return SGJNI.SGSurface_getFpsLimit(this.swigCPtr, this);
    }

    public float getOpacity() {
        return SGJNI.SGSurface_getOpacity(this.swigCPtr, this);
    }

    public SGLayer getRoot() {
        return this.mRootLayer;
    }

    public SGVector2f getSize() {
        return new SGVector2f(SGJNI.SGSurface_getSize(this.swigCPtr, this));
    }

    public SGSurfaceSizeChangeListener getSizeChangeListener() {
        return this.mSurfaceListener.mSizeChangeListener;
    }

    public SGWidget getTouchCapturedWidget() {
        return this.mTouchCapturedWidget;
    }

    public View getView() {
        return this.mView;
    }

    public SGWidget getWidget(int index) {
        return (SGWidget) this.mWidgets.get(index);
    }

    public SGWidget getWidget(String name) {
        int count = this.mWidgets.size();
        for (int i = 0; i < count; i++) {
            SGWidget widget = (SGWidget) this.mWidgets.get(i);
            if (widget.getName().contentEquals(name)) {
                return widget;
            }
        }
        return null;
    }

    public SGWidget getWidgetAtPoint(SGVector2f point) {
        return SGJNI.SGSurface_getWidgetAtPoint(this.swigCPtr, this, point.getData());
    }

    public int getWidgetIndex(SGWidget widget) {
        return this.mWidgets.indexOf(widget);
    }

    public int getWidgetsCount() {
        return SGJNI.SGSurface_getWidgetsCount(this.swigCPtr, this);
    }

    public boolean isContextPreservationEnabled() {
        return SGJNI.SGSurface_isContextPreservationEnabled(this.swigCPtr, this);
    }

    public boolean isFpsIndicatorVisible() {
        return SGJNI.SGSurface_isFpsIndicatorVisible(this.swigCPtr, this);
    }

    public boolean isPartialUpdateEnabled() {
        return SGJNI.SGSurface_isPartialUpdateEnabled(this.swigCPtr, this);
    }

    public boolean isScreenShotAttachDepthBuffer() {
        return SGJNI.SGSurface_isScreenShotAttachDepthBuffer(this.swigCPtr, this);
    }

    public boolean isScreenShotAttachStencilBuffer() {
        return SGJNI.SGSurface_isScreenShotAttachStencilBuffer(this.swigCPtr, this);
    }

    public SGProperty makeScreenShot() {
        return SGJNI.SGSurface_makeScreenShot__SWIG_2(this.swigCPtr, this);
    }

    public SGProperty makeScreenShot(Rect rect) {
        return SGJNI.SGSurface_makeScreenShot__SWIG_4(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public SGProperty makeScreenShot(Rect rect, SGVector2i size) {
        return SGJNI.SGSurface_makeScreenShot__SWIG_5(this.swigCPtr, this, SGMathNative.getArrayRect(rect), size.getData());
    }

    public SGProperty makeScreenShot(SGVector2i size) {
        return SGJNI.SGSurface_makeScreenShot__SWIG_3(this.swigCPtr, this, size.getData());
    }

    public void makeScreenShot(Bitmap result) {
        SGJNI.SGSurface_makeScreenShot__SWIG_0(this.swigCPtr, this, result);
    }

    public void makeScreenShot(Bitmap result, Rect rect) {
        SGJNI.SGSurface_makeScreenShot__SWIG_1(this.swigCPtr, this, result, SGMathNative.getArrayRect(rect));
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

    public boolean onHoverEvent(SGTouchEvent event) {
        switch (event.getAction()) {
            case HOVER_ENTER:
            case HOVER_MOVE:
            case HOVER_EXIT:
            case HOVER_BUTTON_DOWN:
            case HOVER_BUTTON_UP:
                return dispatchHoverEvent(event);
            default:
                return false;
        }
    }

    public boolean onKeyEvent(SGKeyEvent event) {
        return SGJNI.SGSurface_onKeyEvent(this.swigCPtr, this, SGKeyEvent.getCPtr(event), event);
    }

    public boolean onTouchEvent(SGTouchEvent event) {
        switch (event.getAction()) {
            case HOVER_ENTER:
            case HOVER_MOVE:
            case HOVER_EXIT:
            case HOVER_BUTTON_DOWN:
            case HOVER_BUTTON_UP:
                return false;
            default:
                return dispatchTouchEvent(event);
        }
    }

    public void onVsync(long frameTimeNanos) {
        SGJNI.SGSurface_onVsync(this.swigCPtr, this, frameTimeNanos);
    }

    public void pauseAnimations(boolean aSync) {
        SGJNI.SGSurface_pauseAnimations(this.swigCPtr, this, aSync);
    }

    public void pushQueue() {
        SGJNI.SGSurface_pushQueue(this.swigCPtr, this);
    }

    public void removeLayer(int index) {
        this.mRootLayer.removeLayer(index);
    }

    public void removeLayer(SGLayer layer) {
        this.mRootLayer.removeLayer(layer);
    }

    public void removeWidget(int index) {
        removeCapturing(getWidget(index));
        removeW(index);
        this.mWidgets.remove(index);
        this.mWidgetListChanged = true;
    }

    public void removeWidget(SGWidget widget) {
        int index = this.mWidgets.indexOf(widget);
        if (index >= 0) {
            removeCapturing(widget);
            removeW(widget);
            this.mWidgets.remove(index);
            this.mWidgetListChanged = true;
        }
    }

    public void resume() {
        SGJNI.SGSurface_resume(this.swigCPtr, this);
    }

    public void resumeAnimations(boolean aSync) {
        SGJNI.SGSurface_resumeAnimations(this.swigCPtr, this, aSync);
    }

    public void sendWidgetToBack(SGWidget widget) {
        if (widget != null && getWidgetIndex(widget) != -1) {
            sendWToB(widget);
            this.mWidgets.remove(widget);
            this.mWidgets.add(0, widget);
            this.mWidgetListChanged = true;
        }
    }

    public int setAffinityMask(int affinityMask) {
        return SGJNI.SGSurface_setAffinityMask(this.swigCPtr, this, affinityMask);
    }

    public void setContextPreservationEnabled(boolean enabled) {
        SGJNI.SGSurface_setContextPreservationEnabled(this.swigCPtr, this, enabled);
    }

    public void setDrawFrameListener(SGDrawFrameListener listener) {
        setDrawFrameListener(listener != null ? this.mSurfaceListener : null);
        this.mSurfaceListener.mDrawFrameListener = listener;
    }

    public void setFocus(boolean enabled) {
        SGJNI.SGSurface_setFocus(this.swigCPtr, this, enabled);
    }

    public void setFpsIndicator(SGFpsIndicator indicator) {
        SGJNI.SGSurface_setFpsIndicator(this.swigCPtr, this, SGFpsIndicator.getCPtr(indicator), indicator);
    }

    public void setFpsIndicatorVisible(boolean visible) {
        if (visible && getFpsIndicator() == null) {
            setFpsIndicator(new SGSimpleFpsIndicator());
        }
        SGJNI.SGSurface_setFpsIndicatorVisible(this.swigCPtr, this, visible);
    }

    public void setFpsLimit(int value) {
        SGJNI.SGSurface_setFpsLimit(this.swigCPtr, this, value);
    }

    public void setOpacity(float opacity) {
        SGJNI.SGSurface_setOpacity(this.swigCPtr, this, opacity);
    }

    public void setPartialUpdateEnabled(boolean enabled) {
        SGJNI.SGSurface_setPartialUpdateEnabled(this.swigCPtr, this, enabled);
    }

    public void setScreenShotOptions(boolean attachDepthBuffer, boolean attachStencilBuffer) {
        SGJNI.SGSurface_setScreenShotOptions(this.swigCPtr, this, attachDepthBuffer, attachStencilBuffer);
    }

    void setSize(SGVector2f size) {
        SGJNI.SGSurface_setSize(this.swigCPtr, this, size.getData());
    }

    public void setSizeChangeListener(SGSurfaceSizeChangeListener listener) {
        setSizeChangeListener(listener != null ? this.mSurfaceListener : null);
        this.mSurfaceListener.mSizeChangeListener = listener;
    }

    public void signalTouchEvent(boolean actionDown) {
        SGJNI.SGSurface_signalTouchEvent(this.swigCPtr, this, actionDown);
    }

    public void suspend() {
        SGJNI.SGSurface_suspend(this.swigCPtr, this);
    }

    public void traceRay(SGRay ray, SGTraceRayListener listener) {
        SGTraceRayListenerHolder traceRayListenerHolder = new SGTraceRayListenerHolder(null, listener);
        try {
            SGJNI.SGSurface_traceRay(this.swigCPtr, this, ray, SGTraceRayListenerBase.getCPtr(traceRayListenerHolder));
        } finally {
            traceRayListenerHolder.onCompleted();
        }
    }

    public void uncaptureWidget() {
        if (this.mTouchCapturedWidget != null) {
            this.mTouchCapturedWidget.uncaptureWidget();
            this.mTouchCapturedWidget = null;
        }
    }

    public void unhoverWidget() {
        if (this.mHoverCapturedWidget != null) {
            this.mHoverCapturedWidget.unhoverWidget();
            this.mHoverCapturedWidget = null;
        }
    }
}
