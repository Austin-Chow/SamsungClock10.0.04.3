package com.samsung.android.sdk.sgi.vi;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGRenderDataProvider;
import com.samsung.android.sdk.sgi.ui.SGKeyAction;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.samsung.android.sdk.sgi.ui.SGKeyEvent;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent.SGAction;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent.SGPointerType;
import com.samsung.android.sdk.sgi.ui.SGWidget;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

final class SGViewImpl implements FrameCallback {
    private static final String PROPERTY_PREFIX = "sgi.";
    private static SGInputConnectionListener mInputConnectionListener = null;
    private SGContext mContext;
    private SGContextConfiguration mContextConfiguration;
    private boolean mIsSuspended;
    private SGMotionEventConverter mMotionEventConverter;
    private boolean mPartialUpdateEnabled;
    private SGRenderDataProvider mRenderDataProvider;
    private boolean mShowFpsIndicator;
    private SGSurface mSurface;
    private boolean mSurfaceResumed;
    private SGSurfaceStateListener mSurfaceStateListener;
    private boolean mSurfaceWasDestroyed;
    private boolean mSuspendedByUser;
    private boolean mVSyncActivated;

    public interface DefaultCheckInputConnectionProxyStrategy {
        boolean checkInputConnectionProxy(View view);
    }

    public interface DefaultOnKeyLongPress {
        boolean onKeyLongPress(int i, KeyEvent keyEvent);
    }

    public interface DefaultOnKeyEventStrategy {
        boolean onKeyDown(int i, KeyEvent keyEvent);

        boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent);

        boolean onKeyUp(int i, KeyEvent keyEvent);
    }

    SGViewImpl(Context context, SGRenderDataProvider renderDataProvider, SGContextConfiguration contextConfiguration) {
        this.mSurfaceWasDestroyed = true;
        this.mSurfaceResumed = false;
        this.mContextConfiguration = null;
        this.mVSyncActivated = false;
        this.mShowFpsIndicator = false;
        this.mPartialUpdateEnabled = false;
        this.mMotionEventConverter = null;
        this.mSuspendedByUser = false;
        this.mIsSuspended = false;
        this.mSurfaceStateListener = null;
        if (renderDataProvider == null) {
            renderDataProvider = new SGAssetRenderDataProvider(context);
        }
        this.mRenderDataProvider = renderDataProvider;
        this.mContextConfiguration = new SGContextConfiguration();
        if (contextConfiguration != null) {
            this.mContextConfiguration.mRedSize = contextConfiguration.mRedSize;
            this.mContextConfiguration.mGreenSize = contextConfiguration.mGreenSize;
            this.mContextConfiguration.mBlueSize = contextConfiguration.mBlueSize;
            this.mContextConfiguration.mAlphaSize = contextConfiguration.mAlphaSize;
            this.mContextConfiguration.mDepthSize = contextConfiguration.mDepthSize;
            this.mContextConfiguration.mStencilSize = contextConfiguration.mStencilSize;
            this.mContextConfiguration.mSampleBuffers = contextConfiguration.mSampleBuffers;
            this.mContextConfiguration.mSamples = contextConfiguration.mSamples;
            this.mContextConfiguration.mDirtyBoxVisualization = contextConfiguration.mDirtyBoxVisualization;
            this.mContextConfiguration.mRecreateSurfaceOnSizeChange = contextConfiguration.mRecreateSurfaceOnSizeChange;
            this.mContextConfiguration.mBackgroundThreadCount = contextConfiguration.mBackgroundThreadCount;
            this.mContextConfiguration.mSeparateThreads = contextConfiguration.mSeparateThreads;
            this.mContextConfiguration.mVSync = contextConfiguration.mVSync;
            this.mContextConfiguration.mSimpleClipping = contextConfiguration.mSimpleClipping;
        }
        getSystemProperties(context);
        this.mContext = new SGContext(context.getApplicationContext(), this.mRenderDataProvider);
    }

    SGViewImpl(View view, SGRenderDataProvider renderDataProvider, SGContextConfiguration contextConfiguration) {
        this(view.getContext(), renderDataProvider, contextConfiguration);
        this.mSurface = new SGSurface(view, new SGVector2f(1.0f, 1.0f), this.mContextConfiguration);
        if (this.mShowFpsIndicator) {
            this.mSurface.setFpsIndicatorVisible(true);
        }
        if (this.mPartialUpdateEnabled) {
            this.mSurface.setPartialUpdateEnabled(true);
        }
    }

    SGViewImpl(SGViewImpl viewImpl, Context context, int width, int height) {
        this(context, viewImpl.mRenderDataProvider, viewImpl.mContextConfiguration);
        this.mSurfaceWasDestroyed = false;
        this.mSurface = new SGSurface(null, new SGVector2f((float) width, (float) height), this.mContextConfiguration);
        this.mContext.attachToSurface(this.mSurface, viewImpl.mSurface, width, height, this.mContextConfiguration);
    }

    private void VSyncOff() {
        if (this.mContextConfiguration.mVSync && this.mVSyncActivated) {
            Choreographer.getInstance().removeFrameCallback(this);
            this.mVSyncActivated = false;
        }
    }

    private void VSyncOn() {
        if (this.mContextConfiguration.mVSync && !this.mVSyncActivated) {
            Choreographer.getInstance().postFrameCallback(this);
            this.mVSyncActivated = true;
        }
    }

    private SGTouchEvent convertHoverEventDefault(MotionEvent event) {
        SGTouchEvent touch = null;
        if (event.getPointerCount() == 1) {
            touch = SGTouchEvent.create(null);
            long timeDelta = System.currentTimeMillis() - SystemClock.uptimeMillis();
            touch.setTouchTime(new Date(event.getEventTime() - timeDelta));
            touch.setDownTime(new Date(event.getDownTime() - timeDelta));
            int count = event.getPointerCount();
            for (int i = 0; i < count; i++) {
                SGAction currentAction = SGAction.NOTHING;
                if (event.getAction() == 9) {
                    currentAction = SGAction.HOVER_ENTER;
                } else if (event.getAction() == 10) {
                    currentAction = SGAction.HOVER_EXIT;
                } else if (event.getAction() == 7) {
                    currentAction = SGAction.HOVER_MOVE;
                }
                touch.setPointer(i, event.getPointerId(i), 0.0f, 0.0f, event.getX(i), event.getY(i), currentAction, event.getPressure(i), SGPointerType.STYLUS);
            }
        }
        return touch;
    }

    private SGTouchEvent convertTouchEventDefault(MotionEvent event) {
        SGTouchEvent touch = SGTouchEvent.create(null);
        int count = event.getPointerCount();
        long timeDelta = System.currentTimeMillis() - SystemClock.uptimeMillis();
        touch.setTouchTime(new Date(event.getEventTime() + timeDelta));
        touch.setDownTime(new Date(event.getDownTime() + timeDelta));
        int actionIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();
        for (int i = 0; i < count; i++) {
            SGAction currentAction = SGAction.NOTHING;
            if (i == actionIndex) {
                if (maskedAction == 0 || maskedAction == 5) {
                    currentAction = SGAction.DOWN;
                } else if (maskedAction == 1 || maskedAction == 6) {
                    currentAction = SGAction.UP;
                } else if (maskedAction == 2) {
                    currentAction = SGAction.MOVE;
                } else if (maskedAction == 3) {
                    currentAction = SGAction.CANCEL;
                } else if (maskedAction == 4) {
                    currentAction = SGAction.OUTSIDE;
                }
            }
            touch.setPointer(i, event.getPointerId(i), event.getRawX(), event.getRawY(), event.getX(i), event.getY(i), currentAction, event.getPressure(i), SGPointerType.FINGER);
        }
        return touch;
    }

    public static SGInputConnectionListener getInputConnectionListener() {
        return mInputConnectionListener;
    }

    private void getSystemProperties(Context context) {
        String appName;
        int stringId = context.getApplicationInfo().labelRes;
        if (stringId == 0) {
            String packageName = context.getPackageName();
            appName = packageName.substring(packageName.lastIndexOf(".") + 1);
        } else {
            appName = context.getString(stringId);
        }
        Method get = null;
        try {
            get = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
        } catch (Exception e) {
        }
        if (get != null) {
            this.mPartialUpdateEnabled = getSystemProperty(get, "partialupdate", appName, false);
            this.mContextConfiguration.mVSync = getSystemProperty(get, "vsync", appName, this.mContextConfiguration.mVSync);
            SGWidget.mSimple2DTransform = getSystemProperty(get, "2dtransform", appName, SGWidget.mSimple2DTransform);
            SGConfiguration.setDebugInfoEnabled(getSystemProperty(get, "debug", appName, SGConfiguration.isDebugInfoEnabled()));
            SGConfiguration.setSystraceEnabled(getSystemProperty(get, "trace", appName, SGConfiguration.isSystraceEnabled()));
            this.mShowFpsIndicator = getSystemProperty(get, "fpsindicator", appName, this.mShowFpsIndicator);
        }
    }

    private boolean getSystemProperty(Method get, String property, String appName, boolean defaultValue) {
        boolean z = false;
        String propName = PROPERTY_PREFIX + property + "." + appName;
        if (propName.length() > 32) {
            propName = propName.substring(0, 32);
        }
        int value = propertyCall(get, propName);
        if (value == -1) {
            value = propertyCall(get, PROPERTY_PREFIX + property);
        }
        if (value < 0) {
            return defaultValue;
        }
        if (value != 0) {
            z = true;
        }
        return z;
    }

    private void notifySurfaceSizeChange() {
        SGSurfaceSizeChangeListener listener = this.mSurface.getSizeChangeListener();
        if (listener != null) {
            listener.onResize(this.mSurface.getSize());
        }
    }

    private int parsePropertyString(String propertyValue) {
        if (propertyValue == null || propertyValue.isEmpty()) {
            return -1;
        }
        if (propertyValue.compareToIgnoreCase("true") == 0 || propertyValue.compareToIgnoreCase("on") == 0) {
            return 1;
        }
        try {
            Integer tmp = Integer.valueOf(Integer.parseInt(propertyValue));
            return tmp != null ? tmp.intValue() : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int propertyCall(Method get, String propertyName) {
        String value = null;
        try {
            value = (String) get.invoke(null, new Object[]{propertyName});
        } catch (Exception e) {
        }
        return parsePropertyString(value);
    }

    public static void releaseSip(SGInputConnectionListener inputDest) {
        if (mInputConnectionListener == inputDest) {
            mInputConnectionListener = null;
        }
    }

    public static void requestSip(SGInputConnectionListener inputDest) {
        mInputConnectionListener = inputDest;
    }

    private void resumeSip() {
        if (mInputConnectionListener != null) {
            mInputConnectionListener.onResumeSip();
        }
    }

    private SGTouchEvent setLastMotionEvent(SGTouchEvent touch, MotionEvent event) {
        if (touch == null) {
            return null;
        }
        try {
            Field field = touch.getClass().getDeclaredField("lastMotionEvent");
            field.setAccessible(true);
            field.set(touch, event);
            return touch;
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
            return touch;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return touch;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return touch;
        }
    }

    private void suspendSip() {
        if (mInputConnectionListener != null) {
            mInputConnectionListener.onSuspendSip();
        }
    }

    public void attachCurrentThread() {
        this.mContext.attachCurrentThread();
    }

    public boolean checkInputConnectionProxy(View view, DefaultCheckInputConnectionProxyStrategy defaultStrategy) {
        return (mInputConnectionListener == null || !mInputConnectionListener.checkInputConnectionProxy(view)) ? defaultStrategy.checkInputConnectionProxy(view) : true;
    }

    public SGTouchEvent createHoverEvent(MotionEvent event) {
        return setLastMotionEvent(this.mMotionEventConverter == null ? convertHoverEventDefault(event) : this.mMotionEventConverter.convertHoverEvent(event), event);
    }

    public SGTouchEvent createTouchEvent(MotionEvent event) {
        return setLastMotionEvent(this.mMotionEventConverter == null ? convertTouchEventDefault(event) : this.mMotionEventConverter.convertTouchEvent(event), event);
    }

    public void detachCurrentThread() {
        this.mContext.detachCurrentThread();
    }

    public void doFrame(long frameTimeNanos) {
        if (SGSurface.getCPtr(this.mSurface) != 0) {
            this.mSurface.onVsync(frameTimeNanos);
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    public SGMotionEventConverter getMotionEventConverter() {
        return this.mMotionEventConverter;
    }

    public SGSurface getSurface() {
        return this.mSurface;
    }

    public SGSurfaceStateListener getSurfaceStateListener() {
        return this.mSurfaceStateListener;
    }

    public void onAttachedToWindow() {
        VSyncOn();
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttributes) {
        return mInputConnectionListener != null ? mInputConnectionListener.onInputConnectionCreated(outAttributes) : null;
    }

    public void onDetachedFromWindow() {
        VSyncOff();
    }

    protected void onFocusChanged(boolean gainFocus) {
        this.mSurface.setFocus(gainFocus);
    }

    public boolean onHoverEvent(MotionEvent event) {
        SGTouchEvent ev = createHoverEvent(event);
        boolean ret = false;
        if (ev != null) {
            ret = this.mSurface.onHoverEvent(ev);
        }
        SGTouchEvent.recycle(ev);
        return ret;
    }

    public boolean onKeyEvent(int keyCode, int repeatCount, KeyEvent event, DefaultOnKeyEventStrategy defaultStrategy) {
        switch (event.getKeyCode()) {
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 113:
            case 114:
            case SGKeyCode.CODE_CLIPBOARD /*221*/:
                break;
            case 66:
                keyCode = 66;
                break;
            case 67:
                keyCode = 67;
                break;
            default:
                keyCode = event.getKeyCode();
                break;
        }
        SGKeyEvent keyEvent = new SGKeyEvent();
        keyEvent.setNumLockOn(event.isNumLockOn());
        keyEvent.setCapsLockOn(event.isCapsLockOn());
        keyEvent.setScrollLockOn(event.isScrollLockOn());
        keyEvent.setLeftCtrlPressed(event.isCtrlPressed());
        keyEvent.setRightCtrlPressed(event.isCtrlPressed());
        keyEvent.setLeftAltPressed(event.isAltPressed());
        keyEvent.setRightAltPressed(event.isAltPressed());
        keyEvent.setLeftShiftPressed(event.isShiftPressed());
        keyEvent.setRightShiftPressed(event.isShiftPressed());
        keyEvent.setMetaPressed(event.isMetaPressed());
        keyEvent.setSymPressed(event.isSymPressed());
        keyEvent.setFunctionPressed(event.isFunctionPressed());
        keyEvent.setSystem(event.isSystem());
        boolean z = event.isPrintingKey() || event.getKeyCode() == 62;
        keyEvent.setPrintable(z);
        Date eventDate = new Date(System.currentTimeMillis() - (SystemClock.uptimeMillis() - event.getEventTime()));
        keyEvent.setEventTime(eventDate);
        keyEvent.setDownTime(eventDate);
        SGKeyAction sgiAction = null;
        int action = event.getAction();
        if (action == 0) {
            sgiAction = SGKeyAction.ON_KEY_DOWN;
        } else if (action == 1) {
            sgiAction = SGKeyAction.ON_KEY_UP;
        } else if (action == 2) {
            sgiAction = SGKeyAction.ON_KEY_MULTIPLE;
        }
        if (sgiAction != null) {
            String chars = event.getCharacters();
            if (chars == null) {
                chars = ((char) event.getUnicodeChar());
            }
            keyEvent.setKeyEvent(keyCode, sgiAction, eventDate, chars, SGKeyEvent.convertFlagFromAndroid(event.getFlags()));
            if (this.mSurface.onKeyEvent(keyEvent)) {
                return true;
            }
        }
        if (action == 0) {
            return defaultStrategy.onKeyDown(keyCode, event);
        }
        if (action == 1) {
            return defaultStrategy.onKeyUp(keyCode, event);
        }
        if (action != 2) {
            return false;
        }
        return defaultStrategy.onKeyMultiple(keyCode, event.getRepeatCount(), event);
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event, DefaultOnKeyLongPress defaultStrategy) {
        return defaultStrategy.onKeyLongPress(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        SGTouchEvent ev = createTouchEvent(event);
        boolean res = this.mSurface.onTouchEvent(ev);
        SGTouchEvent.recycle(ev);
        return res;
    }

    public void resume() {
        if (!this.mSurfaceResumed) {
            this.mSurfaceResumed = true;
            this.mSurface.resume();
            resumeSip();
            setResumed(true);
            VSyncOn();
        }
    }

    public void setMotionEventConverter(SGMotionEventConverter motionEventConverter) {
        this.mMotionEventConverter = motionEventConverter;
    }

    public final void setResumed(boolean suspendedByUser) {
        if ((this.mSuspendedByUser == suspendedByUser && this.mIsSuspended) || (!suspendedByUser && !this.mIsSuspended)) {
            this.mIsSuspended = false;
            if (this.mSurfaceStateListener != null) {
                this.mSurfaceStateListener.onResumed();
            }
        }
    }

    public void setSurfaceStateListener(SGSurfaceStateListener aListener) {
        this.mSurfaceStateListener = aListener;
    }

    public final void setSuspended(boolean suspendedByUser) {
        Log.e("SGI", "SGViewImpl.setSuspended(" + suspendedByUser + ")");
        if (!this.mIsSuspended) {
            this.mIsSuspended = true;
            this.mSuspendedByUser = suspendedByUser;
            if (this.mSurfaceStateListener != null) {
                this.mSurfaceStateListener.onSuspended();
            }
        }
    }

    public boolean surfaceAvailable(Surface surface) {
        this.mSurfaceWasDestroyed = false;
        return this.mContext.attachToNativeWindow(this.mSurface, surface, this.mContextConfiguration);
    }

    public void surfaceChanged(int width, int height) {
        SGVector2f size = new SGVector2f((float) width, (float) height);
        SGVector2f currentSize = this.mSurface.getSize();
        this.mSurface.setSize(size);
        if (currentSize.isEqual(size, 0.4f)) {
            notifySurfaceSizeChange();
        }
    }

    public void surfaceDestroyed() {
        this.mSurfaceWasDestroyed = true;
        this.mContext.detachFromNativeWindow(this.mSurface);
        VSyncOff();
    }

    public void suspend() {
        if (this.mSurfaceResumed) {
            setSuspended(true);
            this.mSurfaceResumed = false;
            suspendSip();
            this.mSurface.suspend();
            VSyncOff();
        }
    }
}
