package com.samsung.android.sdk.sgi.ui;

import android.os.SystemClock;
import android.view.KeyEvent;
import java.util.Calendar;
import java.util.Date;

public class SGKeyEvent {
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    public SGKeyEvent() {
        this(SGJNI.new_SGKeyEvent__SWIG_0(), true);
    }

    public SGKeyEvent(int keyCode, SGKeyAction keyAction) {
        this(SGJNI.new_SGKeyEvent__SWIG_2(keyCode, SGJNI.getData(keyAction)), true);
    }

    public SGKeyEvent(int keyCode, SGKeyAction keyAction, SGKeyFlag keyFlag) {
        this(SGJNI.new_SGKeyEvent__SWIG_1(keyCode, SGJNI.getData(keyAction), SGJNI.getData(keyFlag)), true);
    }

    public SGKeyEvent(int keyCode, SGKeyAction keyAction, Date eventTime, String character) {
        this(SwigConstructSGKeyEvent(keyCode, keyAction, eventTime, character), true);
    }

    public SGKeyEvent(int keyCode, SGKeyAction keyAction, Date eventTime, String character, SGKeyFlag keyFlag) {
        this(SwigConstructSGKeyEvent(keyCode, keyAction, eventTime, character, keyFlag), true);
    }

    protected SGKeyEvent(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    private static long SwigConstructSGKeyEvent(int keyCode, SGKeyAction keyAction, Date eventTime, String character) {
        if (eventTime == null) {
            throw new NullPointerException("parameter Date is null");
        }
        Calendar calendar = Calendar.getInstance();
        return SGJNI.new_SGKeyEvent__SWIG_4(keyCode, SGJNI.getData(keyAction), eventTime.getTime() - ((long) (calendar.get(15) + calendar.get(16))), character);
    }

    private static long SwigConstructSGKeyEvent(int keyCode, SGKeyAction keyAction, Date eventTime, String character, SGKeyFlag keyFlag) {
        if (eventTime == null) {
            throw new NullPointerException("parameter Date is null");
        }
        Calendar calendar = Calendar.getInstance();
        return SGJNI.new_SGKeyEvent__SWIG_3(keyCode, SGJNI.getData(keyAction), eventTime.getTime() - ((long) (calendar.get(15) + calendar.get(16))), character, SGJNI.getData(keyFlag));
    }

    public static SGKeyFlag convertFlagFromAndroid(int flag) {
        switch (flag) {
            case 1:
                return SGKeyFlag.WOKE_HERE;
            case 2:
                return SGKeyFlag.SOFT_KEYBOARD;
            case 4:
                return SGKeyFlag.KEEP_TOUCH_MODE;
            case 8:
                return SGKeyFlag.FROM_SYSTEM;
            case 16:
                return SGKeyFlag.EDITOR_ACTION;
            case 32:
                return SGKeyFlag.CANCELED;
            case 64:
                return SGKeyFlag.VIRTUAL_HARD_KEY;
            case 128:
                return SGKeyFlag.LONG_PRESS;
            case 256:
                return SGKeyFlag.CANCELED_LONG_PRESS;
            case 512:
                return SGKeyFlag.TRACKING;
            case 1024:
                return SGKeyFlag.FALLBACK;
            default:
                return SGKeyFlag.NONE;
        }
    }

    public static final KeyEvent createKeyEvent(SGKeyEvent event) {
        long timeDelta = System.currentTimeMillis() - SystemClock.uptimeMillis();
        long eventTimeL = event.getEventTime().getTime() - timeDelta;
        long downTimeL = event.getDownTime().getTime() - timeDelta;
        SGKeyAction incommingAction = event.getKeyAction();
        int resultEventAction = 0;
        if (incommingAction == SGKeyAction.ON_KEY_DOWN) {
            resultEventAction = 0;
        }
        if (incommingAction == SGKeyAction.ON_KEY_UP) {
            resultEventAction = 1;
        }
        if (incommingAction == SGKeyAction.ON_KEY_MULTIPLE) {
            resultEventAction = 2;
        }
        if (incommingAction == SGKeyAction.UNKNOWN) {
            resultEventAction = -1;
        }
        int resultMetaState = 0;
        if (event.isNumLockOn()) {
            resultMetaState = 2097152;
        }
        if (event.isScrollLockOn()) {
            resultMetaState |= 4194304;
        }
        if (event.isLeftCtrlPressed()) {
            resultMetaState |= 12288;
        }
        if (event.isRightCtrlPressed()) {
            resultMetaState |= 20480;
        }
        if (event.isLeftAltPressed()) {
            resultMetaState |= 18;
        }
        if (event.isRightAltPressed()) {
            resultMetaState |= 34;
        }
        if (event.isLeftShiftPressed()) {
            resultMetaState |= 65;
        }
        if (event.isRightShiftPressed()) {
            resultMetaState |= 129;
        }
        if (event.isMetaPressed()) {
            resultMetaState |= 196608;
        }
        if (event.isSymPressed()) {
            resultMetaState |= 4;
        }
        if (event.isFunctionPressed()) {
            resultMetaState |= 8;
        }
        return new KeyEvent(downTimeL, eventTimeL, resultEventAction, event.getKeyCode(), event.getRepeatCount(), resultMetaState, 0, 0, 0, 0);
    }

    public static long getCPtr(SGKeyEvent obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGKeyEvent(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public String getCharacter() {
        return SGJNI.SGKeyEvent_getCharacter(this.swigCPtr, this);
    }

    public Date getDownTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        date.setTime((SGJNI.SGKeyEvent_getDownTime(this.swigCPtr, this) + ((long) calendar.get(15))) + ((long) calendar.get(16)));
        return date;
    }

    public Date getEventTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        date.setTime((SGJNI.SGKeyEvent_getEventTime(this.swigCPtr, this) + ((long) calendar.get(15))) + ((long) calendar.get(16)));
        return date;
    }

    public SGKeyAction getKeyAction() {
        return ((SGKeyAction[]) SGKeyAction.class.getEnumConstants())[SGJNI.SGKeyEvent_getKeyAction(this.swigCPtr, this)];
    }

    public int getKeyCode() {
        return SGJNI.SGKeyEvent_getKeyCode(this.swigCPtr, this);
    }

    public SGKeyFlag getKeyFlag() {
        return ((SGKeyFlag[]) SGKeyFlag.class.getEnumConstants())[SGJNI.SGKeyEvent_getKeyFlag(this.swigCPtr, this)];
    }

    public int getRepeatCount() {
        return SGJNI.SGKeyEvent_getRepeatCount(this.swigCPtr, this);
    }

    public boolean isCapsLockOn() {
        return SGJNI.SGKeyEvent_isCapsLockOn(this.swigCPtr, this);
    }

    public boolean isFunctionPressed() {
        return SGJNI.SGKeyEvent_isFunctionPressed(this.swigCPtr, this);
    }

    public boolean isLeftAltPressed() {
        return SGJNI.SGKeyEvent_isLeftAltPressed(this.swigCPtr, this);
    }

    public boolean isLeftCtrlPressed() {
        return SGJNI.SGKeyEvent_isLeftCtrlPressed(this.swigCPtr, this);
    }

    public boolean isLeftShiftPressed() {
        return SGJNI.SGKeyEvent_isLeftShiftPressed(this.swigCPtr, this);
    }

    public boolean isMetaPressed() {
        return SGJNI.SGKeyEvent_isMetaPressed(this.swigCPtr, this);
    }

    public boolean isNumLockOn() {
        return SGJNI.SGKeyEvent_isNumLockOn(this.swigCPtr, this);
    }

    public boolean isPrintable() {
        return SGJNI.SGKeyEvent_isPrintable(this.swigCPtr, this);
    }

    public boolean isRightAltPressed() {
        return SGJNI.SGKeyEvent_isRightAltPressed(this.swigCPtr, this);
    }

    public boolean isRightCtrlPressed() {
        return SGJNI.SGKeyEvent_isRightCtrlPressed(this.swigCPtr, this);
    }

    public boolean isRightShiftPressed() {
        return SGJNI.SGKeyEvent_isRightShiftPressed(this.swigCPtr, this);
    }

    public boolean isScrollLockOn() {
        return SGJNI.SGKeyEvent_isScrollLockOn(this.swigCPtr, this);
    }

    public boolean isSymPressed() {
        return SGJNI.SGKeyEvent_isSymPressed(this.swigCPtr, this);
    }

    public boolean isSystem() {
        return SGJNI.SGKeyEvent_isSystem(this.swigCPtr, this);
    }

    public void resetKeyEvent() {
        SGJNI.SGKeyEvent_resetKeyEvent(this.swigCPtr, this);
    }

    public void setCapsLockOn(boolean capsLockOn) {
        SGJNI.SGKeyEvent_setCapsLockOn(this.swigCPtr, this, capsLockOn);
    }

    public void setDownTime(Date downTime) {
        if (downTime == null) {
            throw new NullPointerException("parameter Date is null");
        }
        Calendar calendar = Calendar.getInstance();
        SGJNI.SGKeyEvent_setDownTime(this.swigCPtr, this, downTime.getTime() - ((long) (calendar.get(15) + calendar.get(16))));
    }

    public void setEventTime(Date dateTime) {
        if (dateTime == null) {
            throw new NullPointerException("parameter Date is null");
        }
        Calendar calendar = Calendar.getInstance();
        SGJNI.SGKeyEvent_setEventTime(this.swigCPtr, this, dateTime.getTime() - ((long) (calendar.get(15) + calendar.get(16))));
    }

    public void setFunctionPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setFunctionPressed(this.swigCPtr, this, pressed);
    }

    public void setKeyEvent(int keyCode, SGKeyAction keyAction, Date eventTime, String character) {
        if (eventTime == null) {
            throw new NullPointerException("parameter Date is null");
        }
        Calendar calendar = Calendar.getInstance();
        int i = keyCode;
        SGJNI.SGKeyEvent_setKeyEvent__SWIG_1(this.swigCPtr, this, i, SGJNI.getData(keyAction), eventTime.getTime() - ((long) (calendar.get(15) + calendar.get(16))), character);
    }

    public void setKeyEvent(int keyCode, SGKeyAction keyAction, Date eventTime, String character, SGKeyFlag keyFlag) {
        if (eventTime == null) {
            throw new NullPointerException("parameter Date is null");
        }
        Calendar calendar = Calendar.getInstance();
        int i = keyCode;
        SGJNI.SGKeyEvent_setKeyEvent__SWIG_0(this.swigCPtr, this, i, SGJNI.getData(keyAction), eventTime.getTime() - ((long) (calendar.get(15) + calendar.get(16))), character, SGJNI.getData(keyFlag));
    }

    public void setLeftAltPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setLeftAltPressed(this.swigCPtr, this, pressed);
    }

    public void setLeftCtrlPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setLeftCtrlPressed(this.swigCPtr, this, pressed);
    }

    public void setLeftShiftPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setLeftShiftPressed(this.swigCPtr, this, pressed);
    }

    public void setMetaPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setMetaPressed(this.swigCPtr, this, pressed);
    }

    public void setNumLockOn(boolean numLockOn) {
        SGJNI.SGKeyEvent_setNumLockOn(this.swigCPtr, this, numLockOn);
    }

    public void setPrintable(boolean printable) {
        SGJNI.SGKeyEvent_setPrintable(this.swigCPtr, this, printable);
    }

    public void setRepeatCount(int repeatCount) {
        SGJNI.SGKeyEvent_setRepeatCount(this.swigCPtr, this, repeatCount);
    }

    public void setRightAltPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setRightAltPressed(this.swigCPtr, this, pressed);
    }

    public void setRightCtrlPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setRightCtrlPressed(this.swigCPtr, this, pressed);
    }

    public void setRightShiftPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setRightShiftPressed(this.swigCPtr, this, pressed);
    }

    public void setScrollLockOn(boolean scrollLockOn) {
        SGJNI.SGKeyEvent_setScrollLockOn(this.swigCPtr, this, scrollLockOn);
    }

    public void setSymPressed(boolean pressed) {
        SGJNI.SGKeyEvent_setSymPressed(this.swigCPtr, this, pressed);
    }

    public void setSystem(boolean system) {
        SGJNI.SGKeyEvent_setSystem(this.swigCPtr, this, system);
    }
}
