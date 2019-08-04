package com.samsung.android.sdk.sgi.ui;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewConfiguration;
import com.samsung.android.sdk.sgi.animation.SGAnimation;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector2i;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent.SGAction;
import com.samsung.android.sdk.sgi.vi.SGBitmapScreenshotListener;
import com.samsung.android.sdk.sgi.vi.SGFilterPass;
import com.samsung.android.sdk.sgi.vi.SGGeometryGenerator;
import com.samsung.android.sdk.sgi.vi.SGLayer;
import com.samsung.android.sdk.sgi.vi.SGPropertyScreenshotListener;
import com.samsung.android.sdk.sgi.vi.SGSurface;
import com.samsung.android.sdk.sgi.vi.SGTransformationHints;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class SGWidget {
    static final int PFLAG_CHILDLISTCHANGED = 64;
    static final int PFLAG_HASPERFORMEDLONGPRESS = 2;
    static final int PFLAG_HOVERABLE = 4;
    static final int PFLAG_ISEVENTACTIVE = 16;
    static final int PFLAG_ISHOVERED = 8;
    static final int PFLAG_ISTOUCHFIRSTSENDTOPARENT = 32;
    static final int PFLAG_PRESSED = 1;
    public static boolean mSimple2DTransform = false;
    private ArrayList<Object> mAsyncListeners;
    ArrayList<SGWidget> mChildArray;
    ArrayList<SGLayer> mChildArrayLayer;
    private SGClickListener mClickListener;
    SGGeometryGenerator mGeometryGenerator;
    private SGWidget mHoverCapturedWidget;
    private SGHoverListener mHoverListener;
    private SGLongClickListener mLongClickListener;
    private CheckForLongPress mPendingCheckForLongPress;
    private PerformClick mPerformClick;
    private int mPrivateFlags;
    private boolean mSelfHovered;
    private Object mTag;
    private SGWidget mTouchCapturedWidget;
    private SGTouchListener mTouchListener;
    private SGWidgetListenerHolder mWidgetListener;
    boolean swigCMemOwn;
    long swigCPtr;

    private final class CheckForLongPress implements Runnable {
        private CheckForLongPress() {
        }

        public void run() {
            if ((SGWidget.this.mPrivateFlags & 1) != 0 && SGWidget.this.performLongClick()) {
                SGWidget.this.mPrivateFlags = SGWidget.this.mPrivateFlags | 2;
            }
        }
    }

    private static class Holder {
        public Object mHolder;
        public long mPtr;

        private Holder(ArrayList<Object> container, SGBitmapScreenshotListener listener) {
            Object obj = null;
            try {
                Class<?> c = Class.forName("com.samsung.android.sdk.sgi.vi.SGBitmapScreenshotListenerHolder");
                Constructor<?> constructor = c.getDeclaredConstructor(new Class[]{container.getClass(), listener.getClass().getInterfaces()[0]});
                constructor.setAccessible(true);
                obj = constructor.newInstance(new Object[]{container, listener});
                Field nameField = c.getSuperclass().getDeclaredField("swigCPtr");
                nameField.setAccessible(true);
                this.mPtr = ((Long) nameField.get(obj)).longValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mHolder = obj;
        }

        private Holder(ArrayList<Object> container, SGPropertyScreenshotListener listener) {
            Object obj = null;
            try {
                Class<?> c = Class.forName("com.samsung.android.sdk.sgi.vi.SGPropertyScreenshotListenerHolder");
                Constructor<?> constructor = c.getDeclaredConstructor(new Class[]{container.getClass(), listener.getClass().getInterfaces()[0]});
                constructor.setAccessible(true);
                obj = constructor.newInstance(new Object[]{container, listener});
                Field nameField = c.getSuperclass().getDeclaredField("swigCPtr");
                nameField.setAccessible(true);
                this.mPtr = ((Long) nameField.get(obj)).longValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mHolder = obj;
        }

        public static Holder create(ArrayList<Object> container, SGBitmapScreenshotListener listener) {
            return new Holder((ArrayList) container, listener);
        }

        public static Holder create(ArrayList<Object> container, SGPropertyScreenshotListener listener) {
            return new Holder((ArrayList) container, listener);
        }
    }

    private final class PerformClick implements Runnable {
        private PerformClick() {
        }

        public void run() {
            SGWidget.this.performClick();
        }
    }

    protected SGWidget(long cPtr, boolean cMemoryOwn) {
        this.mSelfHovered = false;
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
        this.mChildArray = new ArrayList();
        this.mChildArrayLayer = new ArrayList();
        this.mAsyncListeners = new ArrayList();
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
        this.mPrivateFlags = 16;
        this.mTag = null;
    }

    public SGWidget(SGVector2f size) {
        this(SGJNI.new_SGWidget(size.getData()), true);
        SGJNI.SGWidget_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    private int addW(SGWidget widget) {
        return SGJNI.SGWidget_addW__SWIG_0(this.swigCPtr, this, getCPtr(widget), widget);
    }

    private int addW(SGWidget widget, int index) {
        return SGJNI.SGWidget_addW__SWIG_1(this.swigCPtr, this, getCPtr(widget), widget, index);
    }

    private void bringToF() {
        SGJNI.SGWidget_bringToF(this.swigCPtr, this);
    }

    private void bringWToF(int index) {
        SGJNI.SGWidget_bringWToF__SWIG_0(this.swigCPtr, this, index);
    }

    private void bringWToF(SGWidget widget) {
        SGJNI.SGWidget_bringWToF__SWIG_1(this.swigCPtr, this, getCPtr(widget), widget);
    }

    private final void cancelLongClick() {
        if (this.mPendingCheckForLongPress != null) {
            SGSurface surface = getSurface();
            if (surface != null) {
                View view = surface.getView();
                if (view != null) {
                    view.removeCallbacks(this.mPendingCheckForLongPress);
                }
            }
        }
        this.mPrivateFlags &= -4;
    }

    private final boolean checkForClick() {
        SGSurface surface = getSurface();
        if (surface != null) {
            View view = surface.getView();
            if (view != null) {
                if (this.mPerformClick == null) {
                    this.mPerformClick = new PerformClick();
                }
                return view.post(this.mPerformClick);
            }
        }
        return false;
    }

    private final void checkForLongClick() {
        SGSurface surface = getSurface();
        if (surface != null) {
            View view = surface.getView();
            if (view != null) {
                this.mPrivateFlags &= -3;
                if (this.mPendingCheckForLongPress == null) {
                    this.mPendingCheckForLongPress = new CheckForLongPress();
                }
                view.postDelayed(this.mPendingCheckForLongPress, (long) ViewConfiguration.getLongPressTimeout());
            }
        }
    }

    private final void createWidgetListnerIfNeeded() {
        if (this.mWidgetListener == null) {
            this.mWidgetListener = new SGWidgetListenerHolder(this);
        }
    }

    private final boolean dispatchSelfHoverEvent(SGTouchEvent ev) {
        if (!isHoverable()) {
            return false;
        }
        boolean retValue = false;
        if (this.mHoverListener != null) {
            retValue = this.mHoverListener.onHover(ev, this);
        }
        if (!retValue) {
            retValue = onHoverEvent(ev);
        }
        if (!retValue) {
            return retValue;
        }
        switch (ev.getAction()) {
            case HOVER_ENTER:
                setHovered(true);
                return retValue;
            case HOVER_EXIT:
                setHovered(false);
                return retValue;
            default:
                return retValue;
        }
    }

    private final boolean dispatchSelfTouchEvent(SGTouchEvent ev) {
        if ((this.mPrivateFlags & 16) == 0) {
            return false;
        }
        return (this.mTouchListener == null || !this.mTouchListener.onTouchEvent(ev, this)) ? onTouchEvent(ev) : true;
    }

    private final boolean exitHoveredWidget(SGTouchEvent ev) {
        if (this.mHoverCapturedWidget == null) {
            return false;
        }
        SGTouchEvent exitEvent = getTouchEventInLocalWindow(ev);
        exitEvent.setAction(0, SGAction.HOVER_EXIT);
        boolean ret = this.mHoverCapturedWidget.dispatchHoverEvent(exitEvent);
        SGTouchEvent.recycle(exitEvent);
        return ret;
    }

    public static long getCPtr(SGWidget obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private void makeScreenShot(long listener) {
        SGJNI.SGWidget_makeScreenShot__SWIG_8(this.swigCPtr, this, listener);
    }

    private void makeScreenShot(Bitmap buffer, long listener) {
        SGJNI.SGWidget_makeScreenShot__SWIG_6(this.swigCPtr, this, buffer, listener);
    }

    private void makeScreenShot(Bitmap buffer, Rect rect, long listener) {
        SGJNI.SGWidget_makeScreenShot__SWIG_7(this.swigCPtr, this, buffer, SGMathNative.getArrayRect(rect), listener);
    }

    private void makeScreenShot(Rect rect, SGVector2i size, long listener) {
        SGJNI.SGWidget_makeScreenShot__SWIG_10(this.swigCPtr, this, SGMathNative.getArrayRect(rect), size.getData(), listener);
    }

    private void makeScreenShot(SGVector2i size, long listener) {
        SGJNI.SGWidget_makeScreenShot__SWIG_9(this.swigCPtr, this, size.getData(), listener);
    }

    private final boolean performClick() {
        if (this.mClickListener == null) {
            return false;
        }
        this.mClickListener.onClick(this);
        return true;
    }

    private final boolean performLongClick() {
        boolean handled = false;
        if (this.mLongClickListener != null) {
            handled = this.mLongClickListener.onLongClick(this);
        }
        if (handled) {
            this.mPrivateFlags |= 2;
        } else {
            this.mPrivateFlags &= -3;
        }
        return handled;
    }

    private void removeAllW() {
        SGJNI.SGWidget_removeAllW(this.swigCPtr, this);
    }

    private final void removeCapturing(SGWidget widget) {
        if (widget.equals(this.mHoverCapturedWidget)) {
            unhoverWidget();
        }
    }

    private void removeW(int index) {
        SGJNI.SGWidget_removeW__SWIG_1(this.swigCPtr, this, index);
    }

    private void removeW(SGWidget widget) {
        SGJNI.SGWidget_removeW__SWIG_0(this.swigCPtr, this, getCPtr(widget), widget);
    }

    private final void removeWidgetListnerIfNeeded() {
        if (this.mWidgetListener != null && this.mWidgetListener.mWidgetAnimationListener == null && this.mWidgetListener.mKeyEventListener == null && this.mWidgetListener.mWidgetTransformationListener == null) {
            this.mWidgetListener = null;
        }
    }

    private final boolean sendHoverToChildren(SGTouchEvent ev) {
        boolean handled = false;
        SGTouchEvent event = SGTouchEvent.create(ev);
        int count = event.getPointerCount();
        this.mPrivateFlags &= -65;
        for (int i = this.mChildArray.size(); i > 0; i--) {
            if ((this.mPrivateFlags & 64) != 0) {
                handled = false;
                break;
            }
            SGWidget widget = (SGWidget) this.mChildArray.get(i - 1);
            if (widget.isVisible() && widget.isEventActive()) {
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
                        boolean z = handled;
                        return false;
                    }
                }
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
                            exitHoveredWidget(event);
                        }
                        this.mHoverCapturedWidget = widget;
                    }
                }
                if (handled) {
                    break;
                }
            }
        }
        SGTouchEvent.recycle(event);
        return handled;
    }

    private void sendToB() {
        SGJNI.SGWidget_sendToB(this.swigCPtr, this);
    }

    private void sendWToB(int index) {
        SGJNI.SGWidget_sendWToB__SWIG_0(this.swigCPtr, this, index);
    }

    private void sendWToB(SGWidget widget) {
        SGJNI.SGWidget_sendWToB__SWIG_1(this.swigCPtr, this, getCPtr(widget), widget);
    }

    private void setKeyEventListener(SGWidgetListenerBase listener) {
        SGJNI.SGWidget_setKeyEventListener(this.swigCPtr, this, SGWidgetListenerBase.getCPtr(listener), listener);
    }

    private void setWidgetAnimationListener(SGWidgetListenerBase listener) {
        SGJNI.SGWidget_setWidgetAnimationListener(this.swigCPtr, this, SGWidgetListenerBase.getCPtr(listener), listener);
    }

    private void setWidgetTransformationListener(SGWidgetListenerBase listener) {
        SGJNI.SGWidget_setWidgetTransformationListener(this.swigCPtr, this, SGWidgetListenerBase.getCPtr(listener), listener);
    }

    private void swapW(SGWidget firstWidget, SGWidget secondWidget) {
        SGJNI.SGWidget_swapW(this.swigCPtr, this, getCPtr(firstWidget), firstWidget, getCPtr(secondWidget), secondWidget);
    }

    public int addAnimation(int materialId, SGAnimation animation) {
        return SGJNI.SGWidget_addAnimation__SWIG_2(this.swigCPtr, this, materialId, SGAnimation.getCPtr(animation), animation);
    }

    public int addAnimation(SGAnimation animation) {
        return SGJNI.SGWidget_addAnimation__SWIG_0(this.swigCPtr, this, SGAnimation.getCPtr(animation), animation);
    }

    public int addAnimation(SGFilterPass filterPass, SGAnimation animation) {
        return SGJNI.SGWidget_addAnimation__SWIG_1(this.swigCPtr, this, SGFilterPass.getCPtr(filterPass), filterPass, SGAnimation.getCPtr(animation), animation);
    }

    public int addWidget(SGWidget widget) {
        if (widget == null) {
            throw new NullPointerException();
        }
        SGWidget parent = widget.getParent();
        int result = addW(widget);
        if (parent != null) {
            parent.removeCapturing(widget);
            parent.mChildArray.remove(widget);
            parent.mPrivateFlags |= 64;
        }
        this.mChildArray.add(widget);
        this.mPrivateFlags |= 64;
        return result;
    }

    public int addWidget(SGWidget widget, int index) {
        if (widget == null) {
            throw new NullPointerException();
        }
        SGWidget parent = widget.getParent();
        int result = addW(widget, index);
        if (parent != null) {
            parent.removeCapturing(widget);
            parent.mChildArray.remove(widget);
            parent.mPrivateFlags |= 64;
        }
        this.mChildArray.add(index, widget);
        this.mPrivateFlags |= 64;
        return result;
    }

    public void bringToFront() {
        SGWidget parent = getParent();
        if (parent != null) {
            bringToF();
            parent.mChildArray.remove(this);
            parent.mChildArray.add(this);
            parent.mPrivateFlags |= 64;
            return;
        }
        SGSurface surface = getSurface();
        if (surface != null && surface.getWidgetIndex(this) != -1) {
            surface.bringWidgetToFront(this);
        }
    }

    public void bringWidgetToFront(int index) {
        SGWidget widget = (SGWidget) this.mChildArray.get(index);
        bringWToF(index);
        this.mChildArray.remove(widget);
        this.mChildArray.add(widget);
        this.mPrivateFlags |= 64;
    }

    public void bringWidgetToFront(SGWidget widget) {
        if (widget == null) {
            throw new NullPointerException();
        }
        SGWidget parent = widget.getParent();
        bringWToF(widget);
        if (parent != null) {
            parent.removeCapturing(widget);
            parent.mChildArray.remove(widget);
            this.mPrivateFlags |= 64;
        }
        this.mChildArray.add(widget);
        this.mPrivateFlags |= 64;
    }

    public void clearFocus() {
        if (getClass() == SGWidget.class) {
            SGJNI.SGWidget_clearFocus(this.swigCPtr, this);
        } else {
            SGJNI.SGWidget_clearFocusSwigExplicitSGWidget(this.swigCPtr, this);
        }
    }

    public boolean dispatchHoverEvent(SGTouchEvent event) {
        boolean z;
        switch (event.getAction()) {
            case HOVER_ENTER:
            case HOVER_MOVE:
            case HOVER_EXIT:
            case HOVER_BUTTON_DOWN:
            case HOVER_BUTTON_UP:
                boolean handled;
                if (event.getAction() == SGAction.HOVER_EXIT) {
                    if (this.mHoverCapturedWidget != null) {
                        handled = exitHoveredWidget(event);
                        this.mHoverCapturedWidget = null;
                    } else {
                        handled = dispatchSelfHoverEvent(event);
                    }
                    this.mSelfHovered = false;
                    z = handled;
                    return handled;
                }
                handled = sendHoverToChildren(event);
                if (!handled) {
                    if (this.mHoverCapturedWidget != null) {
                        exitHoveredWidget(event);
                        this.mHoverCapturedWidget = null;
                    }
                    this.mSelfHovered = dispatchSelfHoverEvent(event);
                    handled = this.mSelfHovered;
                } else if (this.mSelfHovered) {
                    this.mSelfHovered = false;
                    SGTouchEvent exitEvent = SGTouchEvent.create(event);
                    exitEvent.setAction(0, SGAction.HOVER_EXIT);
                    dispatchSelfHoverEvent(exitEvent);
                    SGTouchEvent.recycle(exitEvent);
                }
                z = handled;
                return handled;
            default:
                z = false;
                return 0;
        }
    }

    public boolean dispatchKeyEvent(SGKeyEvent event) {
        return getClass() == SGWidget.class ? SGJNI.SGWidget_dispatchKeyEvent(this.swigCPtr, this, SGKeyEvent.getCPtr(event), event) : SGJNI.SGWidget_dispatchKeyEventSwigExplicitSGWidget(this.swigCPtr, this, SGKeyEvent.getCPtr(event), event);
    }

    public boolean dispatchTouchEvent(SGTouchEvent event) {
        if (this.mTouchCapturedWidget == null) {
            if (event.getAction() == SGAction.DOWN) {
                if ((this.mPrivateFlags & 32) != 0) {
                    if (!dispatchSelfTouchEvent(event)) {
                        return dispatchTouchEventToChildren(event);
                    }
                    this.mTouchCapturedWidget = this;
                    return true;
                } else if (dispatchTouchEventToChildren(event)) {
                    return true;
                } else {
                    if (dispatchSelfTouchEvent(event)) {
                        this.mTouchCapturedWidget = this;
                        return true;
                    }
                }
            }
            return false;
        } else if (this.mTouchCapturedWidget == this) {
            return dispatchSelfTouchEvent(event);
        } else {
            SGTouchEvent ev = this.mTouchCapturedWidget.getTouchEventInLocalWindow(event);
            boolean ret = this.mTouchCapturedWidget.dispatchTouchEvent(ev);
            SGTouchEvent.recycle(ev);
            return ret;
        }
    }

    public boolean dispatchTouchEventToChildren(SGTouchEvent event) {
        SGTouchEvent ev = SGTouchEvent.create(event);
        int count = ev.getPointerCount();
        int widgetsCount = this.mChildArray.size();
        this.mPrivateFlags &= -65;
        for (int i = widgetsCount; i > 0 && (this.mPrivateFlags & 64) == 0; i--) {
            SGWidget widget = (SGWidget) this.mChildArray.get(i - 1);
            if (widget.isVisible()) {
                boolean validRegion = true;
                for (int j = 0; j < count; j++) {
                    try {
                        SGVector2f localData = widget.getLocationInWindow(ev.getRawX(j), ev.getRawY(j));
                        if (widget.isLocalPointOnWidget(localData)) {
                            ev.setX(j, localData.getX());
                            ev.setY(j, localData.getY());
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
                if (validRegion && widget.dispatchTouchEvent(ev)) {
                    this.mTouchCapturedWidget = widget;
                    SGTouchEvent.recycle(ev);
                    return true;
                }
            }
        }
        SGTouchEvent.recycle(ev);
        return false;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGWidget(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    @Deprecated
    public long getCameraVisibilityMask() {
        return getVisibilityMask();
    }

    public SGMatrix4f getFullTransform() {
        return new SGMatrix4f(SGJNI.SGWidget_getFullTransform(this.swigCPtr, this));
    }

    public SGHoverListener getHoverListener() {
        return this.mHoverListener;
    }

    public SGKeyEventListener getKeyEventListener() {
        return this.mWidgetListener == null ? null : this.mWidgetListener.mKeyEventListener;
    }

    public SGMatrix4f getLocalTransform() {
        return new SGMatrix4f(SGJNI.SGWidget_getLocalTransform(this.swigCPtr, this));
    }

    public SGVector2f getLocationInWindow(float x, float y) {
        return getLocationInWindow(x, y, mSimple2DTransform);
    }

    public SGVector2f getLocationInWindow(float x, float y, boolean useSimpleTransforms) {
        return new SGVector2f(SGJNI.SGWidget_getLocationInWindow__SWIG_1(this.swigCPtr, this, x, y, useSimpleTransforms));
    }

    public SGVector2f getLocationInWindow(SGVector2f point) {
        return getLocationInWindow(point, mSimple2DTransform);
    }

    public SGVector2f getLocationInWindow(SGVector2f point, boolean useSimpleTransforms) {
        return new SGVector2f(SGJNI.SGWidget_getLocationInWindow__SWIG_0(this.swigCPtr, this, point.getData(), useSimpleTransforms));
    }

    public String getName() {
        return SGJNI.SGWidget_getName(this.swigCPtr, this);
    }

    public SGClickListener getOnClickListener() {
        return this.mClickListener;
    }

    public SGLongClickListener getOnLongClickListener() {
        return this.mLongClickListener;
    }

    public float getOpacity() {
        return SGJNI.SGWidget_getOpacity(this.swigCPtr, this);
    }

    public SGWidget getParent() {
        return SGJNI.SGWidget_getParent(this.swigCPtr, this);
    }

    public SGVector2f getPosition() {
        return new SGVector2f(SGJNI.SGWidget_getPosition(this.swigCPtr, this));
    }

    public SGVector3f getPosition3f() {
        return new SGVector3f(SGJNI.SGWidget_getPosition3f(this.swigCPtr, this));
    }

    public SGVector2f getPositionPivot() {
        return new SGVector2f(SGJNI.SGWidget_getPositionPivot(this.swigCPtr, this));
    }

    public SGVector3f getPositionPivot3f() {
        return new SGVector3f(SGJNI.SGWidget_getPositionPivot3f(this.swigCPtr, this));
    }

    public SGMatrix4f getRelativeToAnotherParentTransform(SGWidget parent) {
        return new SGMatrix4f(SGJNI.SGWidget_getRelativeToAnotherParentTransform(this.swigCPtr, this, getCPtr(parent), parent));
    }

    public SGMatrix4f getRelativeTransform(SGWidget parent) {
        return new SGMatrix4f(SGJNI.SGWidget_getRelativeTransform(this.swigCPtr, this, getCPtr(parent), parent));
    }

    public SGQuaternion getRotation() {
        return new SGQuaternion(SGJNI.SGWidget_getRotation(this.swigCPtr, this));
    }

    public SGVector3f getRotation3f() {
        return new SGVector3f(SGJNI.SGWidget_getRotation3f(this.swigCPtr, this));
    }

    public SGVector2f getRotationPivot() {
        return new SGVector2f(SGJNI.SGWidget_getRotationPivot(this.swigCPtr, this));
    }

    public SGVector3f getRotationPivot3f() {
        return new SGVector3f(SGJNI.SGWidget_getRotationPivot3f(this.swigCPtr, this));
    }

    public SGVector2f getScale() {
        return new SGVector2f(SGJNI.SGWidget_getScale(this.swigCPtr, this));
    }

    public SGVector3f getScale3f() {
        return new SGVector3f(SGJNI.SGWidget_getScale3f(this.swigCPtr, this));
    }

    public SGVector2f getScalePivot() {
        return new SGVector2f(SGJNI.SGWidget_getScalePivot(this.swigCPtr, this));
    }

    public SGVector3f getScalePivot3f() {
        return new SGVector3f(SGJNI.SGWidget_getScalePivot3f(this.swigCPtr, this));
    }

    public SGVector2f getSize() {
        return new SGVector2f(SGJNI.SGWidget_getSize(this.swigCPtr, this));
    }

    public SGSurface getSurface() {
        return SGJNI.SGWidget_getSurface(this.swigCPtr, this);
    }

    public Object getTag() {
        return this.mTag;
    }

    public SGWidget getTouchCapturedWidget() {
        return this.mTouchCapturedWidget;
    }

    public final SGTouchEvent getTouchEventInLocalWindow(SGTouchEvent event) {
        SGTouchEvent result = SGTouchEvent.create(event);
        int count = event.getPointerCount();
        for (int i = 0; i < count; i++) {
            SGVector2f converted = getLocationInWindow(result.getRawX(i), result.getRawY(i));
            result.setX(i, converted.getX());
            result.setY(i, converted.getY());
        }
        return result;
    }

    public SGTouchListener getTouchListener() {
        return this.mTouchListener;
    }

    public SGTransformationHints getTransformationHint() {
        return ((SGTransformationHints[]) SGTransformationHints.class.getEnumConstants())[SGJNI.SGWidget_getTransformationHint(this.swigCPtr, this)];
    }

    public SGWidgetVisibility getVisibility() {
        return ((SGWidgetVisibility[]) SGWidgetVisibility.class.getEnumConstants())[SGJNI.SGWidget_getVisibility(this.swigCPtr, this)];
    }

    public long getVisibilityMask() {
        return SGJNI.SGWidget_getVisibilityMask(this.swigCPtr, this);
    }

    public SGWidget getWidget(int index) {
        return (SGWidget) this.mChildArray.get(index);
    }

    public SGWidget getWidget(String name) {
        int count = this.mChildArray.size();
        for (int i = 0; i < count; i++) {
            SGWidget widget = (SGWidget) this.mChildArray.get(i);
            if (widget.getName().contentEquals(name)) {
                return widget;
            }
        }
        return null;
    }

    public SGWidgetAnimationListener getWidgetAnimationListener() {
        return this.mWidgetListener == null ? null : this.mWidgetListener.mWidgetAnimationListener;
    }

    public SGWidget getWidgetAtPoint(SGVector2f point) {
        return SGJNI.SGWidget_getWidgetAtPoint(this.swigCPtr, this, point.getData());
    }

    public int getWidgetIndex(SGWidget widget) {
        return this.mChildArray.indexOf(widget);
    }

    public SGWidgetTransformationListener getWidgetTransformationListener() {
        return this.mWidgetListener == null ? null : this.mWidgetListener.mWidgetTransformationListener;
    }

    public int getWidgetsCount() {
        return this.mChildArray.size();
    }

    public void hideCursor() {
        SGJNI.SGWidget_hideCursor(this.swigCPtr, this);
    }

    public void invalidate() {
        if (getClass() == SGWidget.class) {
            SGJNI.SGWidget_invalidate__SWIG_0(this.swigCPtr, this);
        } else {
            SGJNI.SGWidget_invalidateSwigExplicitSGWidget__SWIG_0(this.swigCPtr, this);
        }
    }

    public void invalidate(RectF rect) {
        if (getClass() == SGWidget.class) {
            SGJNI.SGWidget_invalidate__SWIG_1(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
        } else {
            SGJNI.SGWidget_invalidateSwigExplicitSGWidget__SWIG_1(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
        }
    }

    public boolean isAddedToSurface() {
        return SGJNI.SGWidget_isAddedToSurface(this.swigCPtr, this);
    }

    public boolean isChildrenClippingEnabled() {
        return SGJNI.SGWidget_isChildrenClippingEnabled(this.swigCPtr, this);
    }

    public boolean isEventActive() {
        return (this.mPrivateFlags & 16) != 0;
    }

    public boolean isFiltersAttachDepthBuffer() {
        return SGJNI.SGWidget_isFiltersAttachDepthBuffer(this.swigCPtr, this);
    }

    public boolean isFiltersAttachStencilBuffer() {
        return SGJNI.SGWidget_isFiltersAttachStencilBuffer(this.swigCPtr, this);
    }

    public boolean isFocusable() {
        return getClass() == SGWidget.class ? SGJNI.SGWidget_isFocusable(this.swigCPtr, this) : SGJNI.SGWidget_isFocusableSwigExplicitSGWidget(this.swigCPtr, this);
    }

    public final boolean isHoverable() {
        return (this.mPrivateFlags & 20) == 20;
    }

    public final boolean isHovered() {
        return (this.mPrivateFlags & 8) != 0;
    }

    public boolean isInFocus() {
        return getClass() == SGWidget.class ? SGJNI.SGWidget_isInFocus(this.swigCPtr, this) : SGJNI.SGWidget_isInFocusSwigExplicitSGWidget(this.swigCPtr, this);
    }

    public boolean isInheritOpacity() {
        return SGJNI.SGWidget_isInheritOpacity(this.swigCPtr, this);
    }

    public boolean isLocalPointOnWidget(float x, float y) {
        return SGJNI.SGWidget_isLocalPointOnWidget__SWIG_1(this.swigCPtr, this, x, y);
    }

    public boolean isLocalPointOnWidget(SGVector2f point) {
        return SGJNI.SGWidget_isLocalPointOnWidget__SWIG_0(this.swigCPtr, this, point.getData());
    }

    public boolean isRawPointOnWidget(float x, float y) {
        return isRawPointOnWidget(x, y, mSimple2DTransform);
    }

    public boolean isRawPointOnWidget(float x, float y, boolean useSimpleTransforms) {
        return SGJNI.SGWidget_isRawPointOnWidget__SWIG_1(this.swigCPtr, this, x, y, useSimpleTransforms);
    }

    public boolean isRawPointOnWidget(SGVector2f point) {
        return isRawPointOnWidget(point, mSimple2DTransform);
    }

    public boolean isRawPointOnWidget(SGVector2f point, boolean useSimpleTransforms) {
        return SGJNI.SGWidget_isRawPointOnWidget__SWIG_0(this.swigCPtr, this, point.getData(), useSimpleTransforms);
    }

    public boolean isScreenShotAttachDepthBuffer() {
        return SGJNI.SGWidget_isScreenShotAttachDepthBuffer(this.swigCPtr, this);
    }

    public boolean isScreenShotAttachStencilBuffer() {
        return SGJNI.SGWidget_isScreenShotAttachStencilBuffer(this.swigCPtr, this);
    }

    public boolean isTouchFirstSendToParentEnabled() {
        return (this.mPrivateFlags & 32) != 0;
    }

    public boolean isTouchFocusable() {
        return SGJNI.SGWidget_isTouchFocusable(this.swigCPtr, this);
    }

    public boolean isVisible() {
        return SGJNI.SGWidget_isVisible(this.swigCPtr, this);
    }

    public SGProperty makeScreenShot() {
        return SGJNI.SGWidget_makeScreenShot__SWIG_2(this.swigCPtr, this);
    }

    public SGProperty makeScreenShot(Rect rect) {
        return SGJNI.SGWidget_makeScreenShot__SWIG_4(this.swigCPtr, this, SGMathNative.getArrayRect(rect));
    }

    public SGProperty makeScreenShot(Rect rect, SGVector2i size) {
        return SGJNI.SGWidget_makeScreenShot__SWIG_5(this.swigCPtr, this, SGMathNative.getArrayRect(rect), size.getData());
    }

    public SGProperty makeScreenShot(SGVector2i size) {
        return SGJNI.SGWidget_makeScreenShot__SWIG_3(this.swigCPtr, this, size.getData());
    }

    public void makeScreenShot(Bitmap result) {
        SGJNI.SGWidget_makeScreenShot__SWIG_0(this.swigCPtr, this, result);
    }

    public void makeScreenShot(Bitmap result, Rect rect) {
        SGJNI.SGWidget_makeScreenShot__SWIG_1(this.swigCPtr, this, result, SGMathNative.getArrayRect(rect));
    }

    public final void makeScreenShot(Bitmap bitmap, Rect rect, SGBitmapScreenshotListener listener) {
        Holder holder = Holder.create(this.mAsyncListeners, listener);
        makeScreenShot(bitmap, rect, holder.mPtr);
        this.mAsyncListeners.add(holder.mHolder);
    }

    public final void makeScreenShot(Bitmap bitmap, SGBitmapScreenshotListener listener) {
        Holder holder = Holder.create(this.mAsyncListeners, listener);
        makeScreenShot(bitmap, holder.mPtr);
        this.mAsyncListeners.add(holder.mHolder);
    }

    public final void makeScreenShot(Rect rect, SGVector2i size, SGPropertyScreenshotListener listener) {
        Holder holder = Holder.create(this.mAsyncListeners, listener);
        makeScreenShot(rect, size, holder.mPtr);
        this.mAsyncListeners.add(holder.mHolder);
    }

    public final void makeScreenShot(SGVector2i size, SGPropertyScreenshotListener listener) {
        Holder holder = Holder.create(this.mAsyncListeners, listener);
        makeScreenShot(size, holder.mPtr);
        this.mAsyncListeners.add(holder.mHolder);
    }

    public final void makeScreenShot(SGPropertyScreenshotListener listener) {
        Holder holder = Holder.create(this.mAsyncListeners, listener);
        makeScreenShot(holder.mPtr);
        this.mAsyncListeners.add(holder.mHolder);
    }

    public void moveCursor(SGVector2f size, SGVector2f position, int color) {
        SGJNI.SGWidget_moveCursor(this.swigCPtr, this, size.getData(), position.getData(), color);
    }

    public void onEventActiveChanged() {
    }

    public void onFocusChanged() {
        if (getClass() == SGWidget.class) {
            SGJNI.SGWidget_onFocusChanged(this.swigCPtr, this);
        } else {
            SGJNI.SGWidget_onFocusChangedSwigExplicitSGWidget(this.swigCPtr, this);
        }
    }

    public void onHoverChanged(boolean hovered) {
    }

    public boolean onHoverEvent(SGTouchEvent event) {
        return false;
    }

    public boolean onKeyEvent(SGKeyEvent event) {
        return getClass() == SGWidget.class ? SGJNI.SGWidget_onKeyEvent(this.swigCPtr, this, SGKeyEvent.getCPtr(event), event) : SGJNI.SGWidget_onKeyEventSwigExplicitSGWidget(this.swigCPtr, this, SGKeyEvent.getCPtr(event), event);
    }

    public boolean onTouchEvent(SGTouchEvent event) {
        if (this.mClickListener == null && this.mLongClickListener == null) {
            return false;
        }
        int x;
        int y;
        int slop;
        switch (event.getAction()) {
            case UP:
                if ((this.mPrivateFlags & 1) != 0) {
                    boolean focusTaken = false;
                    if (isFocusable() && isTouchFocusable() && !isInFocus()) {
                        focusTaken = setFocus();
                    }
                    if ((this.mPrivateFlags & 2) == 0) {
                        cancelLongClick();
                        if (!(focusTaken || checkForClick())) {
                            performClick();
                        }
                    }
                    this.mPrivateFlags &= -2;
                    break;
                }
                break;
            case DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                slop = ViewConfiguration.getTouchSlop();
                if (x >= 0 - slop || ((float) x) < getSize().getX() + ((float) slop) || y >= 0 - slop || ((float) y) < getSize().getY() + ((float) slop)) {
                    this.mPrivateFlags |= 1;
                    if (this.mLongClickListener != null) {
                        checkForLongClick();
                        break;
                    }
                }
                break;
            case MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                slop = ViewConfiguration.getTouchSlop();
                if ((x < 0 - slop || ((float) x) >= getSize().getX() + ((float) slop) || y < 0 - slop || ((float) y) >= getSize().getY() + ((float) slop)) && (this.mPrivateFlags & 1) != 0) {
                    cancelLongClick();
                    break;
                }
            case CANCEL:
                cancelLongClick();
                break;
        }
        return true;
    }

    public void removeAllAnimations() {
        SGJNI.SGWidget_removeAllAnimations(this.swigCPtr, this);
    }

    public void removeAllWidgets() {
        if (!this.mChildArray.isEmpty()) {
            unhoverWidget();
            removeAllW();
            this.mChildArray.clear();
            this.mPrivateFlags |= 64;
        }
    }

    public void removeAnimation(int id) {
        SGJNI.SGWidget_removeAnimation(this.swigCPtr, this, id);
    }

    public void removeWidget(int index) {
        removeCapturing(getWidget(index));
        removeW(index);
        this.mChildArray.remove(index);
        this.mPrivateFlags |= 64;
    }

    public void removeWidget(SGWidget widget) {
        if (widget != null) {
            removeCapturing(widget);
            removeW(widget);
            if (this.mChildArray.remove(widget)) {
                this.mPrivateFlags |= 64;
            }
        }
    }

    public void sendToBack() {
        SGWidget parent = getParent();
        if (parent != null) {
            sendToB();
            parent.mChildArray.remove(this);
            parent.mChildArray.add(0, this);
            parent.mPrivateFlags |= 64;
            return;
        }
        SGSurface surface = getSurface();
        if (surface != null && surface.getWidgetIndex(this) != -1) {
            surface.sendWidgetToBack(this);
        }
    }

    public void sendWidgetToBack(int index) {
        SGWidget widget = (SGWidget) this.mChildArray.get(index);
        sendWToB(index);
        this.mChildArray.remove(widget);
        this.mChildArray.add(0, widget);
        this.mPrivateFlags |= 64;
    }

    public void sendWidgetToBack(SGWidget widget) {
        if (widget == null) {
            throw new NullPointerException();
        }
        SGWidget parent = widget.getParent();
        sendWToB(widget);
        if (parent != null) {
            parent.removeCapturing(widget);
            parent.mChildArray.remove(widget);
            parent.mPrivateFlags |= 64;
        }
        this.mChildArray.add(0, widget);
        this.mPrivateFlags |= 64;
    }

    @Deprecated
    public void setCameraVisibilityMask(long mask) {
        setVisibilityMask(mask);
    }

    public void setChildrenClipping(boolean enabled) {
        SGJNI.SGWidget_setChildrenClipping(this.swigCPtr, this, enabled);
    }

    public void setEventActive(boolean active) {
        if (active) {
            if ((this.mPrivateFlags & 16) == 0) {
                this.mPrivateFlags |= 16;
                onEventActiveChanged();
            }
        } else if ((this.mPrivateFlags & 16) != 0) {
            this.mPrivateFlags &= -17;
            onEventActiveChanged();
        }
    }

    public void setFiltersOptions(boolean depth, boolean stencil) {
        SGJNI.SGWidget_setFiltersOptions(this.swigCPtr, this, depth, stencil);
    }

    public boolean setFocus() {
        if (!SGJNI.SGWidget_setFocus(this.swigCPtr, this)) {
            return false;
        }
        SGSurface surface = getSurface();
        if (surface != null) {
            View view = surface.getView();
            if (view != null) {
                view.requestFocus();
            }
        }
        return true;
    }

    public void setFocusable(boolean focusable) {
        if (getClass() == SGWidget.class) {
            SGJNI.SGWidget_setFocusable(this.swigCPtr, this, focusable);
        } else {
            SGJNI.SGWidget_setFocusableSwigExplicitSGWidget(this.swigCPtr, this, focusable);
        }
    }

    public void setHoverListener(SGHoverListener listener) {
        this.mHoverListener = listener;
    }

    public final void setHoverable(boolean hoverable) {
        if (hoverable) {
            this.mPrivateFlags |= 4;
        } else {
            this.mPrivateFlags &= -5;
        }
    }

    public void setHovered(boolean hovered) {
        if (!isHoverable()) {
            return;
        }
        if (hovered) {
            if ((this.mPrivateFlags & 8) == 0) {
                this.mPrivateFlags |= 8;
                onHoverChanged(true);
            }
        } else if ((this.mPrivateFlags & 8) != 0) {
            this.mPrivateFlags &= -9;
            onHoverChanged(false);
        }
    }

    public void setInheritOpacity(boolean inherit) {
        SGJNI.SGWidget_setInheritOpacity(this.swigCPtr, this, inherit);
    }

    public void setKeyEventListener(SGKeyEventListener listener) {
        if (listener == null) {
            setKeyEventListener((SGWidgetListenerHolder) null);
            if (this.mWidgetListener != null) {
                this.mWidgetListener.mKeyEventListener = null;
            }
            removeWidgetListnerIfNeeded();
            return;
        }
        createWidgetListnerIfNeeded();
        if (this.mWidgetListener.mKeyEventListener == null) {
            setKeyEventListener(this.mWidgetListener);
        }
        this.mWidgetListener.mKeyEventListener = listener;
    }

    public void setLocalTransform(SGMatrix4f transform) {
        SGJNI.SGWidget_setLocalTransform(this.swigCPtr, this, transform.getData());
    }

    public void setName(String name) {
        SGJNI.SGWidget_setName(this.swigCPtr, this, name);
    }

    public void setOnClickListener(SGClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnLongClickListener(SGLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public void setOpacity(float opacity) {
        SGJNI.SGWidget_setOpacity(this.swigCPtr, this, opacity);
    }

    public void setPivots(float x, float y) {
        SGJNI.SGWidget_setPivots__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setPivots(float x, float y, float z) {
        SGJNI.SGWidget_setPivots__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setPivots(SGVector2f pivot) {
        SGJNI.SGWidget_setPivots__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setPivots(SGVector3f pivot) {
        SGJNI.SGWidget_setPivots__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setPosition(float x, float y) {
        SGJNI.SGWidget_setPosition__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setPosition(float x, float y, float z) {
        SGJNI.SGWidget_setPosition__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setPosition(SGVector2f position) {
        SGJNI.SGWidget_setPosition__SWIG_0(this.swigCPtr, this, position.getData());
    }

    public void setPosition(SGVector3f position) {
        SGJNI.SGWidget_setPosition__SWIG_1(this.swigCPtr, this, position.getData());
    }

    public void setPositionPivot(float x, float y) {
        SGJNI.SGWidget_setPositionPivot__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setPositionPivot(float x, float y, float z) {
        SGJNI.SGWidget_setPositionPivot__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setPositionPivot(SGVector2f pivot) {
        SGJNI.SGWidget_setPositionPivot__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setPositionPivot(SGVector3f pivot) {
        SGJNI.SGWidget_setPositionPivot__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setRotation(float pitch, float yaw, float roll) {
        SGJNI.SGWidget_setRotation__SWIG_1(this.swigCPtr, this, pitch, yaw, roll);
    }

    public void setRotation(SGQuaternion rotation) {
        SGJNI.SGWidget_setRotation__SWIG_0(this.swigCPtr, this, rotation.getData());
    }

    public void setRotation(SGVector3f angles) {
        SGJNI.SGWidget_setRotation__SWIG_2(this.swigCPtr, this, angles.getData());
    }

    public void setRotationPivot(float x, float y) {
        SGJNI.SGWidget_setRotationPivot__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setRotationPivot(float x, float y, float z) {
        SGJNI.SGWidget_setRotationPivot__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setRotationPivot(SGVector2f pivot) {
        SGJNI.SGWidget_setRotationPivot__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setRotationPivot(SGVector3f pivot) {
        SGJNI.SGWidget_setRotationPivot__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setRotationX(float angle) {
        SGJNI.SGWidget_setRotationX(this.swigCPtr, this, angle);
    }

    public void setRotationY(float angle) {
        SGJNI.SGWidget_setRotationY(this.swigCPtr, this, angle);
    }

    public void setRotationZ(float angle) {
        SGJNI.SGWidget_setRotationZ(this.swigCPtr, this, angle);
    }

    public void setScale(float xScale, float yScale) {
        SGJNI.SGWidget_setScale__SWIG_2(this.swigCPtr, this, xScale, yScale);
    }

    public void setScale(float xScale, float yScale, float zScale) {
        SGJNI.SGWidget_setScale__SWIG_3(this.swigCPtr, this, xScale, yScale, zScale);
    }

    public void setScale(SGVector2f scale) {
        SGJNI.SGWidget_setScale__SWIG_0(this.swigCPtr, this, scale.getData());
    }

    public void setScale(SGVector3f scale) {
        SGJNI.SGWidget_setScale__SWIG_1(this.swigCPtr, this, scale.getData());
    }

    public void setScalePivot(float x, float y) {
        SGJNI.SGWidget_setScalePivot__SWIG_2(this.swigCPtr, this, x, y);
    }

    public void setScalePivot(float x, float y, float z) {
        SGJNI.SGWidget_setScalePivot__SWIG_3(this.swigCPtr, this, x, y, z);
    }

    public void setScalePivot(SGVector2f pivot) {
        SGJNI.SGWidget_setScalePivot__SWIG_0(this.swigCPtr, this, pivot.getData());
    }

    public void setScalePivot(SGVector3f pivot) {
        SGJNI.SGWidget_setScalePivot__SWIG_1(this.swigCPtr, this, pivot.getData());
    }

    public void setScreenShotOptions(boolean attachDepthBuffer, boolean attachStencilBuffer) {
        SGJNI.SGWidget_setScreenShotOptions(this.swigCPtr, this, attachDepthBuffer, attachStencilBuffer);
    }

    public void setSize(float width, float height) {
        SGJNI.SGWidget_setSize__SWIG_1(this.swigCPtr, this, width, height);
    }

    public void setSize(SGVector2f size) {
        SGJNI.SGWidget_setSize__SWIG_0(this.swigCPtr, this, size.getData());
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public void setTouchFirstSendToParent(boolean enabled) {
        if (enabled) {
            this.mPrivateFlags |= 32;
        } else {
            this.mPrivateFlags &= -33;
        }
    }

    public void setTouchFocusable(boolean focusable) {
        SGJNI.SGWidget_setTouchFocusable(this.swigCPtr, this, focusable);
    }

    public void setTouchListener(SGTouchListener listener) {
        this.mTouchListener = listener;
    }

    public void setTransformationHint(SGTransformationHints hint) {
        SGJNI.SGWidget_setTransformationHint(this.swigCPtr, this, hint.ordinal());
    }

    public void setVisibility(SGWidgetVisibility visibility) {
        SGJNI.SGWidget_setVisibility(this.swigCPtr, this, SGJNI.getData(visibility));
    }

    public void setVisibilityMask(long mask) {
        SGJNI.SGWidget_setVisibilityMask(this.swigCPtr, this, mask);
    }

    public void setWidgetAnimationListener(SGWidgetAnimationListener listener) {
        if (listener == null) {
            setWidgetAnimationListener((SGWidgetListenerHolder) null);
            if (this.mWidgetListener != null) {
                this.mWidgetListener.mWidgetAnimationListener = null;
            }
            removeWidgetListnerIfNeeded();
            return;
        }
        createWidgetListnerIfNeeded();
        if (this.mWidgetListener.mWidgetAnimationListener == null) {
            setWidgetAnimationListener(this.mWidgetListener);
        }
        this.mWidgetListener.mWidgetAnimationListener = listener;
    }

    public void setWidgetTransformationListener(SGWidgetTransformationListener listener) {
        if (listener == null) {
            setWidgetTransformationListener((SGWidgetListenerHolder) null);
            if (this.mWidgetListener != null) {
                this.mWidgetListener.mWidgetTransformationListener = null;
            }
            removeWidgetListnerIfNeeded();
            return;
        }
        createWidgetListnerIfNeeded();
        if (this.mWidgetListener.mWidgetTransformationListener == null) {
            setWidgetTransformationListener(this.mWidgetListener);
        }
        this.mWidgetListener.mWidgetTransformationListener = listener;
    }

    public void showCursor(SGVector2f size, SGVector2f position, int color) {
        SGJNI.SGWidget_showCursor(this.swigCPtr, this, size.getData(), position.getData(), color);
    }

    public void swapWidgets(SGWidget firstWidget, SGWidget secondWidget) {
        swapW(firstWidget, secondWidget);
        Collections.swap(this.mChildArray, this.mChildArray.indexOf(firstWidget), this.mChildArray.indexOf(secondWidget));
        this.mPrivateFlags |= 64;
    }

    public void uncaptureWidget() {
        if (this.mTouchCapturedWidget != null) {
            SGWidget tmp = this.mTouchCapturedWidget;
            this.mTouchCapturedWidget = null;
            tmp.uncaptureWidget();
        }
    }

    public void unhoverWidget() {
        if (this.mHoverCapturedWidget != null) {
            this.mHoverCapturedWidget.unhoverWidget();
        }
        this.mHoverCapturedWidget = null;
        this.mSelfHovered = false;
    }
}
