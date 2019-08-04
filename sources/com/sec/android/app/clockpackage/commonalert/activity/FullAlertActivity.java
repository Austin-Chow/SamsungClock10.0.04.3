package com.sec.android.app.clockpackage.commonalert.activity;

import android.annotation.SuppressLint;
import android.app.SemStatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PointerIconCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.Scover;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.CoverPowerKeyListener;
import com.samsung.android.sdk.cover.ScoverManager.NfcLedCoverTouchListener;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.samsung.android.view.SemWindowManager;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PowerController;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.C0661R;
import com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab;
import java.lang.reflect.Field;
import sec.color.gradient.view.RadialGradientView;

public abstract class FullAlertActivity extends ClockActivity {
    private static Object sLock = new Object();
    protected int mCallState = 0;
    protected Context mContext;
    protected ScoverManager mCoverManager;
    private final CoverPowerKeyListener mCoverPowerKeyListener = new C06653();
    protected StateListener mCoverStateListener;
    protected int mCoverType = 1;
    protected int mCoverViewSize = 0;
    protected boolean mDeviceSupportCoverSDK = true;
    protected boolean mIsCoverOpen = true;
    protected boolean mIsPreFlipOpen;
    protected final NfcLedCoverTouchListener mNfcLedCoverCallback = new C06642();
    protected final PhoneStateListener mPhoneStateListener = new C06631();
    protected final PowerController mPowerController = new PowerController();
    protected BroadcastReceiver mReceiver = null;
    protected AlertSlidingTab mSelector;
    private SemStatusBarManager mStatusBar;

    /* renamed from: com.sec.android.app.clockpackage.commonalert.activity.FullAlertActivity$1 */
    class C06631 extends PhoneStateListener {
        C06631() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if (FullAlertActivity.this.mCallState != state) {
                FullAlertActivity.this.mCallState = state;
                Log.secD("FullAlertActivity", "onCallStateChanged mCallState : " + FullAlertActivity.this.mCallState);
                FullAlertActivity.this.changedPhoneState(FullAlertActivity.this.mCallState);
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.activity.FullAlertActivity$2 */
    class C06642 extends NfcLedCoverTouchListener {
        C06642() {
        }

        public void onCoverTouchAccept() {
            FullAlertActivity.this.finishByLedCover();
            Log.m41d("FullAlertActivity", "LEDCover-onCoverTouchAccept()");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.activity.FullAlertActivity$3 */
    class C06653 extends CoverPowerKeyListener {
        C06653() {
        }

        public void onPowerKeyPress() {
            super.onPowerKeyPress();
            Log.m41d("FullAlertActivity", "LED/Neon/Flip Cover-onPowerKeyPress");
            FullAlertActivity.this.finishByCoverPowerKey();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.activity.FullAlertActivity$5 */
    class C06675 implements Runnable {
        C06675() {
        }

        public void run() {
            FullAlertActivity.this.mPowerController.releaseDim();
        }
    }

    protected abstract void changedPhoneState(int i);

    protected abstract void finishByCoverPowerKey();

    protected abstract void finishByKey(int i);

    protected abstract void finishByLedCover();

    protected abstract int getNfcTouchListenerEventType();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        if (!Feature.isTablet(getApplicationContext())) {
            setRequestedOrientation(1);
        }
        setWindowManager();
        this.mCallState = ((TelephonyManager) getSystemService("phone")).getCallState();
    }

    protected void onResume() {
        super.onResume();
        if (this.mDeviceSupportCoverSDK && this.mCoverManager != null) {
            ScoverState coverState = this.mCoverManager.getCoverState();
            if (coverState != null) {
                this.mCoverManager.registerListener(this.mCoverStateListener);
                if (coverState.getSwitchState()) {
                    this.mIsCoverOpen = true;
                } else {
                    this.mCoverType = coverState.getType();
                    this.mIsCoverOpen = false;
                }
            }
            Log.secD("FullAlertActivity", "mDeviceSupportCoverSDK = true, mIsCoverOpen = " + this.mIsCoverOpen);
        }
        setTelephonyListener();
        requestSystemKeyEvents(true);
        setStatusBarState(true);
        setSystemUIFlagForFullScreen();
    }

    protected void onPause() {
        super.onPause();
        Log.secD("FullAlertActivity", "onPause");
        requestSystemKeyEvents(false);
        setStatusBarState(false);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean up = false;
        if ((this.mCoverType == 0 || this.mCoverType == 11 || this.mCoverType == 7) && !this.mIsCoverOpen && !StateUtils.isContextInDexMode(this.mContext)) {
            return false;
        }
        if (event.getAction() == 1) {
            up = true;
        }
        Log.secD("FullAlertActivity", "dispatchKeyEvent action = " + event.getAction());
        Log.secD("FullAlertActivity", "event.getKeyCode() = " + event.getKeyCode());
        switch (event.getKeyCode()) {
            case 3:
            case 4:
            case 6:
            case 24:
            case 25:
            case 26:
            case 27:
            case 79:
            case 80:
            case 111:
            case 127:
            case 164:
            case 168:
            case 169:
            case PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW /*1015*/:
            case 1082:
                if (event.getKeyCode() == 4 && !StateUtils.isContextInDexMode(this.mContext)) {
                    Log.secD("FullAlertActivity", "dispatchKeyEvent KEYCODE_BACK && !StateUtils.isContextInDexMode(mContext) return true");
                    return true;
                } else if (!up) {
                    return true;
                } else {
                    Log.m41d("FullAlertActivity", "dispatchKeyEvent code = " + (event.getKeyCode() + 999));
                    Log.secD("FullAlertActivity", "finishByKey");
                    finishByKey(event.getKeyCode());
                    return true;
                }
            case 82:
            case PointerIconCompat.TYPE_CONTEXT_MENU /*1001*/:
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.secD("FullAlertActivity", "onTouchEvent event = " + event);
        if (event.getAction() == 0 && StateUtils.isLightNotificationEnabled(this.mContext)) {
            Intent mStopFlashNoti = new Intent();
            mStopFlashNoti.setAction("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION");
            this.mContext.sendBroadcast(mStopFlashNoti);
        }
        return super.onTouchEvent(event);
    }

    protected void setGradientBackground(RadialGradientView background) {
        background.init();
        background.setColors(getResources().getColor(C0661R.color.full_alert_background_gradient_circle_color_first, null), getResources().getColor(C0661R.color.full_alert_background_gradient_circle_color_second, null), getResources().getColor(C0661R.color.full_alert_background_gradient_circle_color_third, null));
        background.setAlphas(0.85f, 1.0f, 0.9f);
        background.startAnimation();
    }

    @SuppressLint({"NewApi"})
    protected void setWindowManager() {
        Log.secD("FullAlertActivity", "setWindowManager()");
        int privateFlag = 0;
        LayoutParams lp = getWindow().getAttributes();
        try {
            Field field = LayoutParams.class.getField("SEM_EXTENSION_FLAG_FORCE_HIDE_FLOATING_MULTIWINDOW");
            if (field != null) {
                privateFlag = field.getInt(field);
            }
            lp.layoutInDisplayCutoutMode = 1;
        } catch (NoSuchFieldError e) {
            Log.secE("FullAlertActivity", "setWindowManager NoSuchFieldError | NoSuchFieldException | IllegalAccessException");
            if (privateFlag != 0) {
                lp.semAddExtensionFlags(privateFlag);
            }
            getWindow().setAttributes(lp);
        } catch (NoSuchFieldException e2) {
            Log.secE("FullAlertActivity", "setWindowManager NoSuchFieldError | NoSuchFieldException | IllegalAccessException");
            if (privateFlag != 0) {
                lp.semAddExtensionFlags(privateFlag);
            }
            getWindow().setAttributes(lp);
        } catch (IllegalAccessException e3) {
            Log.secE("FullAlertActivity", "setWindowManager NoSuchFieldError | NoSuchFieldException | IllegalAccessException");
            if (privateFlag != 0) {
                lp.semAddExtensionFlags(privateFlag);
            }
            getWindow().setAttributes(lp);
        }
        if (privateFlag != 0) {
            lp.semAddExtensionFlags(privateFlag);
        }
        getWindow().setAttributes(lp);
    }

    protected void setWindowOnTop() {
        getWindow().addFlags(2621441);
    }

    protected void setStatusBarState(boolean isExpanded) {
        if (!StateUtils.isContextInDexMode(this)) {
            if (isExpanded) {
                try {
                    getSemStatusBarManager().disable(65536);
                    return;
                } catch (SecurityException e) {
                    Log.secE("FullAlertActivity", "Exception : " + e.toString());
                    return;
                }
            }
            getSemStatusBarManager().disable(0);
        }
    }

    private SemStatusBarManager getSemStatusBarManager() {
        Log.secD("FullAlertActivity", "getSemStatusBarManager()");
        if (this.mStatusBar == null) {
            synchronized (sLock) {
                this.mStatusBar = (SemStatusBarManager) getSystemService("sem_statusbar");
            }
        } else {
            Log.secD("FullAlertActivity", "mStatusBar instance already exist");
        }
        return this.mStatusBar;
    }

    protected void setSystemUIFlagForFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(2566);
    }

    protected void requestSystemKeyEvents(boolean request) {
        requestSystemKeyEvent(3, request);
        requestSystemKeyEvent(26, request);
        requestSystemKeyEvent(6, request);
        requestSystemKeyEvent(SGKeyCode.CODE_APP_SWITCH, request);
        requestSystemKeyEvent(PointerIconCompat.TYPE_CONTEXT_MENU, request);
        requestSystemKeyEvent(1082, request);
    }

    protected boolean requestSystemKeyEvent(int keyCode, boolean request) {
        try {
            return SemWindowManager.getInstance().requestSystemKeyEvent(keyCode, getComponentName(), request);
        } catch (Exception e) {
            Log.secE("FullAlertActivity", "Exception : " + e.toString());
            return false;
        }
    }

    protected void initScover() {
        try {
            new Scover().initialize(this);
            Log.secI("FullAlertActivity", "initScover() - initialize");
        } catch (IllegalArgumentException e) {
            Log.secE("FullAlertActivity", "Exception : " + e.toString());
            this.mDeviceSupportCoverSDK = false;
            Log.secE("FullAlertActivity", "initScover() - IllegalArgumentException");
        } catch (SsdkUnsupportedException e2) {
            Log.secE("FullAlertActivity", "Exception : " + e2.toString());
            this.mDeviceSupportCoverSDK = false;
            Log.secE("FullAlertActivity", "initScover() - SsdkUnsupportedException");
        }
    }

    protected void setTelephonyListener() {
        ((TelephonyManager) getSystemService("phone")).listen(this.mPhoneStateListener, 32);
    }

    protected void resetTelephonyListener() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService("phone");
        if (TelephonyMgr != null) {
            TelephonyMgr.listen(this.mPhoneStateListener, 0);
        }
    }

    protected void setUnregisterReceiver() {
        if (this.mReceiver != null) {
            try {
                unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secE("FullAlertActivity", "IllegalArgumentException - unregisterReceiver(Receiver)");
                Log.secE("FullAlertActivity", "Exception : " + e.toString());
            }
            this.mReceiver = null;
        }
    }

    protected void unregisterCoverManager() {
        if (this.mCoverManager != null) {
            ScoverState coverState = this.mCoverManager.getCoverState();
            if (coverState != null) {
                if (coverState.getType() == 7) {
                    try {
                        this.mCoverManager.unregisterNfcTouchListener(this.mNfcLedCoverCallback);
                        this.mCoverManager.unregisterCoverPowerKeyListener(this.mCoverPowerKeyListener);
                    } catch (SsdkUnsupportedException e) {
                        Log.secE("FullAlertActivity", "unregisterCoverManager SsdkUnsupportedException");
                    }
                } else if (coverState.getType() == 0 || coverState.getType() == 11) {
                    Log.secD("FullAlertActivity", "unregisterCoverPowerKeyListener");
                    try {
                        this.mCoverManager.unregisterCoverPowerKeyListener(this.mCoverPowerKeyListener);
                    } catch (SsdkUnsupportedException e2) {
                        Log.secE("FullAlertActivity", "unregisterCoverManager SsdkUnsupportedException");
                    }
                }
            }
            this.mCoverManager.unregisterListener(this.mCoverStateListener);
            this.mCoverManager = null;
        }
    }

    protected void registerCoverListener() {
        if (this.mCoverManager != null) {
            ScoverState coverState = this.mCoverManager.getCoverState();
            if (coverState != null) {
                if (coverState.getType() == 7) {
                    Log.secD("FullAlertActivity", "registerNfcLedCallBackListener()");
                    try {
                        this.mCoverManager.registerNfcTouchListener(getNfcTouchListenerEventType(), this.mNfcLedCoverCallback);
                        this.mCoverManager.registerCoverPowerKeyListener(this.mCoverPowerKeyListener);
                    } catch (SsdkUnsupportedException e) {
                        Log.secE("FullAlertActivity", "SsdkUnsupportedException : " + e.toString());
                    }
                } else if (coverState.getType() == 0 || coverState.getType() == 11) {
                    Log.secD("FullAlertActivity", "registerCoverPowerKeyListener");
                    try {
                        this.mCoverManager.registerCoverPowerKeyListener(this.mCoverPowerKeyListener);
                    } catch (SsdkUnsupportedException e2) {
                        Log.secE("FullAlertActivity", "registerCoverListener SsdkUnsupportedException");
                    }
                }
            }
            this.mCoverManager.registerListener(this.mCoverStateListener);
        }
    }

    protected void initCoverState() {
        if (this.mCoverManager != null) {
            ScoverState coverState = this.mCoverManager.getCoverState();
            if (coverState == null) {
                this.mIsCoverOpen = false;
                this.mCoverViewSize = 3;
                Log.secD("FullAlertActivity", "mCoverState is null.");
            } else if (coverState.getSwitchState()) {
                this.mIsCoverOpen = true;
            } else {
                this.mIsCoverOpen = false;
                this.mCoverType = coverState.getType();
                if (this.mCoverType == 8) {
                    this.mCoverViewSize = 2;
                } else if (this.mCoverType == 7) {
                    this.mCoverViewSize = 4;
                }
            }
        }
    }

    protected void resetSelector() {
        if (this.mSelector != null) {
            this.mSelector.removeAllViews();
            this.mSelector.setOnTriggerListener(null);
            this.mSelector.resetContext();
            this.mSelector = null;
        }
    }

    protected void acquireDim(final String tag) {
        new Handler().post(new Runnable() {
            public void run() {
                if (FullAlertActivity.this.mPowerController != null) {
                    FullAlertActivity.this.mPowerController.acquireDim(FullAlertActivity.this.mContext, tag);
                }
            }
        });
    }

    protected void releaseDim() {
        new Handler().postDelayed(new C06675(), 150);
    }
}
