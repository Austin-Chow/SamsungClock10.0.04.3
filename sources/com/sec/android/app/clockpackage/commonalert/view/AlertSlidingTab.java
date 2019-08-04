package com.sec.android.app.clockpackage.commonalert.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.VibrationEffect;
import android.os.VibrationEffect.SemMagnitudeType;
import android.os.Vibrator;
import android.os.Vibrator.SemMagnitudeTypes;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.C0661R;

public class AlertSlidingTab extends FrameLayout {
    private static int mType = -1;
    private Context mContext = null;
    private AlertSlidingTabHandle mDismissHandle = null;
    private int mGrabbedState = 0;
    private boolean mIsSingTapMode = false;
    private boolean mIsTracking;
    private OnTriggerListener mOnTriggerListener;
    private String mTag = null;
    private Vibrator mVibrator;

    public interface OnTriggerListener {
        void onGrabbedStateChange(View view, int i);

        void onTrigger(View view, int i);
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab$1 */
    class C06681 implements OnFocusChangeListener {
        C06681() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.m41d("AlertSlidingTab", "setOnFocusChangeListener hasFocus STATE_ACTIVE");
                AlertSlidingTab.this.mDismissHandle.setState(1);
                AlertSlidingTab.this.vibrate();
                AlertSlidingTab.this.setGrabbedState(1);
                return;
            }
            AlertSlidingTab.this.mDismissHandle.setState(0);
            AlertSlidingTab.this.setGrabbedState(0);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab$2 */
    class C06692 implements OnClickListener {
        C06692() {
        }

        public void onClick(View v) {
            if (AlertSlidingTab.this.getSingTapMode()) {
                Log.m41d("AlertSlidingTab", "mTabBg onClick getSingTapMode dispatchTriggerEvent");
                AlertSlidingTab.this.dispatchTriggerEvent(1);
            } else if (AlertSlidingTab.this.mDismissHandle.getState() == 1) {
                AlertSlidingTab.this.mDismissHandle.clearCircleAnimation();
                AlertSlidingTab.this.setIsTracking(false);
                Log.m41d("AlertSlidingTab", "mTabBg onClick STATE_ACTIVE dispatchTriggerEvent");
                AlertSlidingTab.this.dispatchTriggerEvent(AlertSlidingTab.this.mDismissHandle.getState());
                AlertSlidingTab.this.setGrabbedState(0);
            }
        }
    }

    public AlertSlidingTab(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public AlertSlidingTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        Log.secD("AlertSlidingTab", "init()");
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        this.mTag = (String) getTag();
        if (!(isClearCover(this.mTag) || isSViewCover(this.mTag))) {
            ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        }
        initHandle();
        this.mIsSingTapMode = shouldAcceptByTapping();
        Log.secD("AlertSlidingTab", "isSingleTapMode : " + this.mIsSingTapMode);
    }

    private void initHandle() {
        if (isClearCover(this.mTag)) {
            this.mDismissHandle = new AlertSlidingTabHandle(this.mContext, 1, 2);
        } else if (isSViewCover(this.mTag)) {
            this.mDismissHandle = new AlertSlidingTabHandle(this.mContext, 1, 3);
        } else {
            this.mDismissHandle = new AlertSlidingTabHandle(this.mContext, 1);
        }
        addView(this.mDismissHandle);
        if (getType() == 0) {
            this.mDismissHandle.getDismissHandle().setContentDescription(getResources().getString(C0661R.string.dismiss) + ' ' + getResources().getString(C0661R.string.button) + ", " + getResources().getString(C0661R.string.alarm_alert_dismiss_swipe_comment));
        } else {
            this.mDismissHandle.getDismissHandle().setContentDescription(getResources().getString(C0661R.string.dismiss) + ' ' + getResources().getString(C0661R.string.button) + ", " + getResources().getString(C0661R.string.timer_alert_dismiss_swipe_comment));
        }
        this.mDismissHandle.getTabBg().setOnFocusChangeListener(new C06681());
        this.mDismissHandle.getTabBg().setOnClickListener(new C06692());
    }

    public void setOnTriggerListener(OnTriggerListener listener) {
        this.mOnTriggerListener = listener;
    }

    public void resetContext() {
        Log.secD("AlertSlidingTab", "resetContext()");
        mType = -1;
        if (this.mDismissHandle != null) {
            this.mDismissHandle.setState(3);
        }
        if (this.mContext != null) {
            this.mContext = null;
        }
    }

    public void dispatchTriggerEvent(int whichHandle) {
        Log.secD("AlertSlidingTab", "dispatchTriggerEvent whichHandle = " + whichHandle);
        if (StateUtils.checkHapticFeedbackEnabled(this.mContext)) {
            vibrate();
        }
        if (this.mOnTriggerListener != null) {
            this.mOnTriggerListener.onTrigger(this, whichHandle);
        }
    }

    public void setGrabbedState(int newState) {
        Log.secD("AlertSlidingTab", "setGrabbedState newState = " + newState);
        if (newState != this.mGrabbedState) {
            this.mGrabbedState = newState;
            if (this.mOnTriggerListener != null) {
                this.mOnTriggerListener.onGrabbedStateChange(this, this.mGrabbedState);
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.secD("AlertSlidingTab", "onLayout() changed = " + changed + " l = " + l + " t = " + t + " r = " + r + " b = " + b);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean leftHit = this.mDismissHandle.isHandleSelected((float) Math.round(event.getX()), (float) Math.round(event.getY()));
        Log.secD("AlertSlidingTab", "onInterceptTouchEvent event = " + event);
        if (!this.mIsTracking && !leftHit) {
            return false;
        }
        if (StateUtils.isLightNotificationEnabled(this.mContext)) {
            Intent mStopFlashNoti = new Intent();
            mStopFlashNoti.setAction("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION");
            this.mContext.sendBroadcast(mStopFlashNoti);
        }
        switch (action) {
            case 0:
            case 9:
                this.mIsTracking = true;
                if (leftHit && getSingTapMode()) {
                    Log.m41d("AlertSlidingTab", "onInterceptTouchEvent: getSingTapMode");
                    dispatchTriggerEvent(1);
                    return true;
                }
                if (StateUtils.checkHapticFeedbackEnabled(this.mContext)) {
                    vibrate();
                }
                if (!leftHit) {
                    return true;
                }
                Log.m41d("AlertSlidingTab", "setState STATE_ACTIVE0 + event.getAction :" + action);
                this.mDismissHandle.setState(1);
                setGrabbedState(1);
                return true;
            default:
                return true;
        }
    }

    private boolean shouldAcceptByTapping() {
        boolean assistantMenu;
        if (System.getInt(this.mContext.getContentResolver(), "assistant_menu", 0) == 1) {
            assistantMenu = true;
        } else {
            assistantMenu = false;
        }
        boolean singleTapToSwipe;
        if (System.getInt(this.mContext.getContentResolver(), "easy_interaction", 0) == 1) {
            singleTapToSwipe = true;
        } else {
            singleTapToSwipe = false;
        }
        if ((assistantMenu && singleTapToSwipe) || StateUtils.isTalkBackEnabled(this.mContext) || StateUtils.isUniversalSwitchEnabled(this.mContext) || StateUtils.isSwitchAccessEnabled(this.mContext)) {
            return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z = false;
        if (this.mIsTracking) {
            int action = event.getAction();
            Log.secD("AlertSlidingTab", "onTouchEvent  action = " + action);
            switch (action) {
                case 1:
                case 3:
                case 6:
                    this.mIsTracking = false;
                    this.mDismissHandle.setState(0);
                    this.mDismissHandle.invalidateCircle();
                    setGrabbedState(0);
                    if (action != 1) {
                        return true;
                    }
                    performClick();
                    return true;
                case 2:
                    if (this.mDismissHandle.getState() != 1 || this.mIsSingTapMode) {
                        return true;
                    }
                    Log.secD("AlertSlidingTab", "processMoveEvent(event)");
                    processMoveEvent(event);
                    return true;
            }
        }
        if (this.mIsTracking || super.onTouchEvent(event)) {
            z = true;
        }
        return z;
    }

    public boolean performClick() {
        return super.performClick();
    }

    public synchronized void vibrate() {
        if (Feature.isVibetonzSupported(getContext())) {
            if (this.mVibrator == null) {
                this.mVibrator = (Vibrator) getContext().getSystemService("vibrator");
            }
            if (VERSION.SEM_INT >= 2801) {
                this.mVibrator.vibrate(VibrationEffect.semCreateWaveform(HapticFeedbackConstants.semGetVibrationIndex(1), -1, SemMagnitudeType.TYPE_MAX));
            } else if (VERSION.SEM_INT >= 2701) {
                this.mVibrator.vibrate(VibrationEffect.semCreateWaveform(50025, -1, SemMagnitudeType.TYPE_MAX));
            } else {
                this.mVibrator.semVibrate(50025, -1, null, SemMagnitudeTypes.TYPE_MAX);
            }
        }
    }

    private void processMoveEvent(MotionEvent motion) {
        if (this.mDismissHandle.getState() == 1) {
            float x = motion.getX();
            float y = motion.getY();
            if (this.mDismissHandle.isThresholdReached(x, y)) {
                this.mDismissHandle.clearCircleAnimation();
                setIsTracking(false);
                Log.m41d("AlertSlidingTab", "processMoveEvent isThresholdReached dispatchTriggerEvent");
                dispatchTriggerEvent(this.mDismissHandle.getHandleType());
                setGrabbedState(0);
                return;
            }
            this.mDismissHandle.updateMovingCircle(x, y);
        }
    }

    public void inactiveHandle() {
        Log.secD("AlertSlidingTab", "inactiveHandle()");
        this.mDismissHandle.setState(2);
    }

    public boolean getSingTapMode() {
        return this.mIsSingTapMode;
    }

    public void setIsTracking(boolean b) {
        this.mIsTracking = b;
    }

    private boolean isClearCover(String mTag) {
        return "clear_cover_alert".equals(mTag);
    }

    private boolean isSViewCover(String mTag) {
        return "sview_cover_alert".equals(mTag);
    }

    public static void setType(int type) {
        mType = type;
    }

    public static int getType() {
        return mType;
    }
}
