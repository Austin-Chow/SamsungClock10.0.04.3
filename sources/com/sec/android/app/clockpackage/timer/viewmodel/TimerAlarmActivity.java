package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.activity.FullAlertActivity;
import com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab;
import com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab.OnTriggerListener;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerCoverListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.view.TimerAlarmTimeView;
import com.sec.android.app.clockpackage.timer.view.TimerCover;
import sec.color.gradient.view.RadialGradientView;

public class TimerAlarmActivity extends FullAlertActivity implements OnLongClickListener {
    private TimerCover mCover = null;
    private long mElapsedMillis = 0;
    private int mFinishMode = 1;
    private int mHour = 0;
    private long mHunElapsedMillis = 0;
    private boolean mIsHideByAlarm = false;
    private int mMinute = 0;
    private View mRestartBtn;
    private int mSecond = 0;
    private TimerAlarmTimeView mTimeView;
    private CountDownTimer mTimer;
    private TimerManager mTimerManager = null;
    private String mTimerNameString = "";

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity$1 */
    class C07561 implements Runnable {
        C07561() {
        }

        public void run() {
            if (!TimerAlarmActivity.this.mIsHideByAlarm) {
                Log.secD("TimerAlarmActivity", "ACTION_TIMER_STARTED_IN_ALERT called");
                Intent iTimerStarted = new Intent();
                iTimerStarted.setAction("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT");
                TimerAlarmActivity.this.sendBroadcast(iTimerStarted);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity$2 */
    class C07572 extends StateListener {
        C07572() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if (state.getSwitchState()) {
                Log.secD("TimerAlarmActivity", "mCoverStateListener cover is close -> open");
                if (TimerAlarmActivity.this.mCoverManager != null) {
                    TimerAlarmActivity.this.mCoverManager.setCoverModeToWindow(TimerAlarmActivity.this.getWindow(), 0);
                }
                TimerAlarmActivity.this.mIsCoverOpen = true;
                if (TimerAlarmActivity.this.mCoverType != 0) {
                    if (TimerAlarmActivity.this.mCover != null) {
                        TimerAlarmActivity.this.mCover.dismissDialog();
                        TimerAlarmActivity.this.mCover = null;
                    }
                    TimerAlarmActivity.this.setWindowOnTop();
                    TimerAlarmActivity.this.initTime(false);
                    TimerAlarmActivity.this.setVisibillityforView(true);
                    TimerAlarmActivity.this.initViews();
                    TimerAlarmActivity.this.setSystemUIFlagForFullScreen();
                    TimerAlarmActivity.this.updateTimeView();
                    return;
                }
                return;
            }
            Log.secD("TimerAlarmActivity", "mCoverStateListener cover is open -> close");
            TimerAlarmActivity.this.mIsCoverOpen = false;
            TimerAlarmActivity.this.finishTimer();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity$3 */
    class C07583 extends BroadcastReceiver {
        C07583() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("TimerAlarmActivity", "Received action :" + action);
            boolean z = true;
            switch (action.hashCode()) {
                case -1566616968:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT")) {
                        z = true;
                        break;
                    }
                    break;
                case -614903101:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT_FROM_ALARM")) {
                        z = true;
                        break;
                    }
                    break;
                case -23421476:
                    if (action.equals("com.samsung.flipfolder.OPEN")) {
                        z = true;
                        break;
                    }
                    break;
                case 198205126:
                    if (action.equals("com.sec.android.clockpackage.timer.FINISH_ALERT")) {
                        z = true;
                        break;
                    }
                    break;
                case 263975269:
                    if (action.equals("com.sec.android.clockpackage.timer.finishAlertByRestart")) {
                        z = true;
                        break;
                    }
                    break;
                case 907316959:
                    if (action.equals("com.sec.android.app.clockpackage.timer.KILL_DUPLICATED_BY_TIMER_SERVICE ")) {
                        z = true;
                        break;
                    }
                    break;
                case 1947666138:
                    if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                        z = false;
                        break;
                    }
                    break;
                case 1984222603:
                    if (action.equals("com.sec.android.app.clockpackage.timer.KILL_BY_TIMER_SERVICE")) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    TimerAlarmActivity.this.finishTimer();
                    return;
                case true:
                    boolean isFlipOpen = intent.getBooleanExtra("flipOpen", false);
                    if (TimerAlarmActivity.this.mIsPreFlipOpen && !isFlipOpen) {
                        TimerAlarmActivity.this.finishTimer();
                    }
                    TimerAlarmActivity.this.mIsPreFlipOpen = isFlipOpen;
                    return;
                case true:
                    TimerAlarmActivity.this.mFinishMode = intent.getIntExtra("com.sec.android.clockpackage.timer.FINISH_MODE", 1);
                    TimerAlarmActivity.this.finishTimer();
                    return;
                case true:
                    TimerAlarmActivity.this.mFinishMode = 0;
                    TimerAlarmActivity.this.finishTimer();
                    return;
                case true:
                    TimerAlarmActivity.this.mIsHideByAlarm = false;
                    return;
                case true:
                    TimerManager.sElapsedMillis = TimerAlarmActivity.this.mElapsedMillis;
                    TimerAlarmActivity.this.finish();
                    return;
                case true:
                    TimerAlarmActivity.this.mFinishMode = 2;
                    TimerAlarmActivity.this.finish();
                    return;
                case true:
                    TimerAlarmActivity.this.mIsHideByAlarm = true;
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity$4 */
    class C07594 implements OnTriggerListener {
        C07594() {
        }

        public void onGrabbedStateChange(View arg0, int isPressed) {
            Log.secD("TimerAlarmActivity", "onGrabbedStateChange : " + isPressed);
            if (TimerAlarmActivity.this.mRestartBtn == null) {
                return;
            }
            if (isPressed == 1) {
                TimerAlarmActivity.this.mRestartBtn.startAnimation(AnimationUtils.loadAnimation(TimerAlarmActivity.this.mContext, C0728R.anim.fade_out));
                TimerAlarmActivity.this.mRestartBtn.setAlpha(0.0f);
            } else if (ClockUtils.sIsTimerAlertStarted) {
                TimerAlarmActivity.this.mRestartBtn.startAnimation(AnimationUtils.loadAnimation(TimerAlarmActivity.this.mContext, C0728R.anim.fade_in));
                TimerAlarmActivity.this.mRestartBtn.setAlpha(1.0f);
            }
        }

        public void onTrigger(View arg0, int arg1) {
            if (1 == arg1) {
                Log.secD("TimerAlarmActivity", "onTrigger -> finishTimer");
                TimerAlarmActivity.this.finishTimer();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity$5 */
    class C07615 implements OnClickListener {

        /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity$5$1 */
        class C07601 implements Runnable {
            C07601() {
            }

            public void run() {
                TimerAlarmActivity.this.mTimerManager.callTimerBroadcast();
                TimerManager access$2000 = TimerAlarmActivity.this.mTimerManager;
                Context access$2100 = TimerAlarmActivity.this.mContext;
                boolean z = TimerAlarmActivity.this.mCoverViewSize == 2 && !TimerAlarmActivity.this.mIsCoverOpen;
                access$2000.callRestartToastPopup(access$2100, z);
            }
        }

        C07615() {
        }

        public void onClick(View v) {
            if (TimerAlarmActivity.this.mFinishMode != 0) {
                int i;
                Log.secD("TimerAlarmActivity", "callTimerBroadcast");
                Handler handler = new Handler();
                Runnable c07601 = new C07601();
                if (StateUtils.isContextInDexMode(TimerAlarmActivity.this.mContext)) {
                    i = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                } else {
                    i = 100;
                }
                handler.postDelayed(c07601, (long) i);
                TimerAlarmActivity.this.mFinishMode = 0;
                TimerAlarmActivity.this.finishTimer();
                ClockUtils.insertSaLog("132", "1143");
            }
        }
    }

    private class TimerCoverListenerImpl implements TimerCoverListener {
        private TimerCoverListenerImpl() {
        }

        public long onGetOffHookElapsedMillis() {
            return TimerManager.sOffHookElapsedMillis;
        }

        public String onGetContentDescription(int hour, int minute, int second) {
            return TimerUtils.getDescriptionString(TimerAlarmActivity.this.mContext, hour, minute, second);
        }

        public void onFinishAlert() {
            Intent timerAlert = new Intent();
            timerAlert.setAction("com.sec.android.clockpackage.timer.FINISH_ALERT");
            TimerAlarmActivity.this.mContext.sendBroadcast(timerAlert);
        }

        public void onRestartAlert() {
            boolean z = false;
            Log.secD("TimerAlarmActivity", "TimerCoverListener onRestartAlert");
            TimerAlarmActivity.this.mTimerManager.callTimerBroadcast();
            TimerAlarmActivity.this.mFinishMode = 0;
            TimerManager access$2000 = TimerAlarmActivity.this.mTimerManager;
            Context access$3000 = TimerAlarmActivity.this.mContext;
            if (TimerAlarmActivity.this.mCoverViewSize == 2 && !TimerAlarmActivity.this.mIsCoverOpen) {
                z = true;
            }
            access$2000.callRestartToastPopup(access$3000, z);
            TimerAlarmActivity.this.finishTimer();
        }

        public void onSendActionTimerStoppedInAlert(String targetPkg) {
            Intent finishPopupIntent = new Intent();
            finishPopupIntent.setAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
            if (targetPkg != null) {
                finishPopupIntent.setPackage(targetPkg);
            }
            TimerAlarmActivity.this.mContext.sendBroadcast(finishPopupIntent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("TimerAlarmActivity", "onCreate");
        this.mTimerManager = TimerManager.getInstance();
        this.mTimerManager.setContext(getApplicationContext());
        readIntent(getIntent());
        setCoverStateManager();
        setRegisterReceiver();
        TimerManager.sIsPausedTimerActivity = false;
        ClockUtils.insertSaLog("132");
        AlertSlidingTab.setType(1);
        Log.secD("TimerAlarmActivity", "mIsCoverOpen = " + this.mIsCoverOpen + ", mCoverType = " + this.mCoverType);
        setContentView(C0728R.layout.timer_alarm);
        if (this.mIsCoverOpen || StateUtils.isContextInDexMode(this.mContext) || this.mCoverType == 0) {
            setWindowOnTop();
            initViews();
            return;
        }
        setVisibillityforView(false);
        showCoverTimerByDialog();
    }

    private void setVisibillityforView(boolean flag) {
        int i;
        int i2 = 0;
        View view = findViewById(C0728R.id.timer_alarm_top_layout);
        if (flag) {
            i = 0;
        } else {
            i = 4;
        }
        view.setVisibility(i);
        view = findViewById(C0728R.id.gradient_bg);
        if (!flag) {
            i2 = 4;
        }
        view.setVisibility(i2);
    }

    private void readIntent(Intent intent) {
        Log.secD("TimerAlarmActivity", "readIntent()");
        Boolean bNotificationTouch = Boolean.valueOf(false);
        if (intent != null) {
            if (intent.hasExtra("HUN_ELAPSED_TIME")) {
                this.mHunElapsedMillis = intent.getLongExtra("HUN_ELAPSED_TIME", 0);
            } else {
                bNotificationTouch = Boolean.valueOf(intent.getBooleanExtra("com.sec.android.app.clockpackage.timer.TIMER_TIMER_NOTIFICATION_TOUCH", false));
                if (bNotificationTouch.booleanValue()) {
                    sendBroadcast(new Intent("com.sec.android.app.clockpackage.timer.ACTION_FINISH_TIMER_HUN"));
                    this.mHunElapsedMillis = TimerManager.sElapsedMillis;
                }
            }
            if (intent.hasExtra("com.sec.android.clockpackage.timer.TIMER_NAME")) {
                this.mTimerNameString = intent.getStringExtra("com.sec.android.clockpackage.timer.TIMER_NAME");
            }
            if (intent.hasExtra("android.intent.extra.alarm.MESSAGE")) {
                this.mTimerNameString = intent.getStringExtra("android.intent.extra.alarm.MESSAGE");
            }
            if (intent.hasExtra("IS_HIDE_BY_ALARM")) {
                this.mIsHideByAlarm = intent.getBooleanExtra("IS_HIDE_BY_ALARM", false);
            }
        }
        Log.secD("TimerAlarmActivity", "mTimerNameString = " + this.mTimerNameString);
        if (StateUtils.isContextInDexMode(this.mContext) && !bNotificationTouch.booleanValue()) {
            this.mElapsedMillis = 0;
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.secD("TimerAlarmActivity", "onNewIntent()");
        readIntent(intent);
    }

    protected void onResume() {
        super.onResume();
        Log.secD("TimerAlarmActivity", "onResume()");
        if (this.mCoverViewSize != 3) {
            setWindowOnTop();
        }
        initTime(this.mElapsedMillis == 0);
        alertStart();
        if (!(this.mIsCoverOpen || this.mCover == null)) {
            this.mCover.setSystemUIFlagForFullScreen(this);
        }
        acquireDim("TimerAlarmActivity");
        TimerManager.sIsPausedTimerActivity = false;
        Log.secD("TimerAlarmActivity", "onResume()- mIsHideByAlarm = " + this.mIsHideByAlarm);
        new Handler().postDelayed(new C07561(), 60);
    }

    protected void onPause() {
        super.onPause();
        Log.secD("TimerAlarmActivity", "onPause");
        TimerManager.sIsPausedTimerActivity = true;
    }

    protected void onDestroy() {
        Log.secD("TimerAlarmActivity", "onDestroy");
        if (isChangingConfigurations() && StateUtils.isContextInDexMode(this.mContext)) {
            this.mFinishMode = 2;
        }
        if (StateUtils.isContextInDexMode(this.mContext) && this.mFinishMode == 1) {
            finishTimer();
        }
        releaseDim();
        setStatusBarState(false);
        setUnregisterReceiver();
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        resetSelector();
        if (this.mCover != null) {
            this.mCover.dismissDialog();
            this.mCover = null;
        }
        unregisterCoverManager();
        TimerManager.sIsPausedTimerActivity = false;
        if (this.mTimeView != null) {
            this.mTimeView.onDestroy();
            this.mTimeView = null;
        }
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.secD("TimerAlarmActivity", "onConfigurationChanged()");
        if (this.mIsCoverOpen) {
            setWindowOnTop();
            initTime(false);
            setContentView(C0728R.layout.timer_alarm);
            initViews();
            alertStart();
        }
    }

    private void setCoverStateManager() {
        initScover();
        if (this.mDeviceSupportCoverSDK) {
            this.mCoverManager = new ScoverManager(this);
            this.mCoverStateListener = new C07572();
            registerCoverListener();
            initCoverState();
        }
    }

    protected void finishByLedCover() {
        finishTimer();
    }

    protected void finishByCoverPowerKey() {
        finishTimer();
        ClockUtils.insertSaLog("132", "1140", "KEYCODE_COVER_POWER");
    }

    protected int getNfcTouchListenerEventType() {
        return 2;
    }

    protected void finishByKey(int keyCode) {
        finishTimer();
        ClockUtils.insertSaLog("132", "1140", KeyEvent.keyCodeToString(keyCode));
    }

    private void setRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("com.sec.android.clockpackage.timer.FINISH_ALERT");
        filter.addAction("com.sec.android.clockpackage.timer.finishAlertByRestart");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");
        filter.addAction("com.sec.android.app.clockpackage.timer.KILL_BY_TIMER_SERVICE");
        filter.addAction("com.sec.android.app.clockpackage.timer.KILL_DUPLICATED_BY_TIMER_SERVICE ");
        if (!StateUtils.isContextInDexMode(this.mContext)) {
            filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT_FROM_ALARM");
        }
        if (Feature.isFolder(this.mContext)) {
            filter.addAction("com.samsung.flipfolder.OPEN");
        }
        filter.setPriority(999);
        if (this.mReceiver == null) {
            this.mReceiver = new C07583();
        }
        registerReceiver(this.mReceiver, filter);
    }

    protected void changedPhoneState(int callState) {
        if (callState == 0) {
            acquireDim("TimerAlarmActivity");
        }
    }

    private void initViews() {
        TextView timerName = (TextView) findViewById(C0728R.id.timer_time_out);
        timerName.setSingleLine(true);
        if (ClockUtils.isEnableString(this.mTimerNameString)) {
            timerName.setText(this.mTimerNameString);
        } else {
            timerName.setText(C0728R.string.timer_times_out);
        }
        ClockUtils.setLargeTextSize(this.mContext, new TextView[]{(TextView) findViewById(C0728R.id.restartBtn_textview), (TextView) findViewById(C0728R.id.timer_alarm_top_icon)}, 1.3f);
        this.mSelector = (AlertSlidingTab) findViewById(C0728R.id.tab_selector);
        if (this.mSelector != null) {
            this.mSelector.setOnTriggerListener(new C07594());
        }
        this.mTimeView = (TimerAlarmTimeView) findViewById(C0728R.id.timer_done_time);
        if (this.mTimeView != null) {
            this.mTimeView.init(this.mHour, this.mMinute, this.mSecond);
            this.mTimeView.setContentDescription(TimerUtils.getDescriptionString(this.mContext, this.mHour, this.mMinute, this.mSecond));
        }
        setGradientBackground((RadialGradientView) findViewById(C0728R.id.gradient_bg));
        initRestartView();
    }

    private void initRestartView() {
        this.mRestartBtn = findViewById(C0728R.id.timer_restart_btn);
        this.mRestartBtn.setContentDescription(getResources().getString(C0728R.string.restart) + ' ' + getResources().getString(C0728R.string.button));
        this.mRestartBtn.setOnClickListener(new C07615());
    }

    private void updateTimeView() {
        int hour = (int) (this.mElapsedMillis / 3600000);
        int minute = (int) ((this.mElapsedMillis % 3600000) / 60000);
        int second = (int) ((this.mElapsedMillis % 60000) / 1000);
        int milliSecond = (int) (this.mElapsedMillis % 1000);
        if (this.mTimeView != null && this.mSecond != second && milliSecond < Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            this.mTimeView.updateTimeView(hour, minute, second);
            this.mTimeView.setContentDescription(TimerUtils.getDescriptionString(this.mContext, hour, minute, second));
            this.mHour = hour;
            this.mMinute = minute;
            this.mSecond = second;
        }
    }

    public void finishTimer() {
        Log.secD("TimerAlarmActivity", "finishTimer mFinishMode = " + this.mFinishMode);
        releaseDim();
        if (this.mFinishMode == 1) {
            TimerData.setRestartMillis(0);
        }
        ClockUtils.timerAlertTimeInCall = 0;
        resetTelephonyListener();
        alertReset();
        if (TimerData.getTimerState() != 1) {
            this.mTimerManager.clearTimerAlarmManager();
        }
        if (this.mFinishMode != 2) {
            ClockUtils.sIsTimerAlertStarted = false;
            Log.secD("TimerAlarmActivity", "send ACTION_TIMER_STOPPED_IN_ALERT. mFinishMode = " + this.mFinishMode);
            Intent iTimerStopped = new Intent();
            iTimerStopped.setAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
            sendBroadcast(iTimerStopped);
        }
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Log.secD("TimerAlarmActivity", "onBackPressed -> finishTimer");
        finishTimer();
    }

    public boolean onLongClick(View v) {
        Log.secD("TimerAlarmActivity", "onLongClick -> finishTimer");
        finishTimer();
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == 4 || super.onKeyDown(keyCode, event);
    }

    private void initTime(boolean isFirst) {
        Log.secD("TimerAlarmActivity", "initTime");
        if (isFirst) {
            if (this.mHunElapsedMillis != 0) {
                this.mElapsedMillis = this.mHunElapsedMillis;
                this.mHunElapsedMillis = 0;
            }
            if (TimerManager.sOffHookElapsedMillis != 0) {
                this.mElapsedMillis = System.currentTimeMillis() - TimerManager.sOffHookElapsedMillis;
                TimerManager.sOffHookElapsedMillis = 0;
            }
        }
        Log.secD("TimerAlarmActivity", "initTime mElapsedMillis " + this.mElapsedMillis);
        this.mHour = (int) (this.mElapsedMillis / 3600000);
        this.mMinute = (int) ((this.mElapsedMillis % 3600000) / 60000);
        this.mSecond = (int) ((this.mElapsedMillis % 60000) / 1000);
    }

    private void alertStart() {
        Log.secD("TimerAlarmActivity", "alertStart");
        setTimer(359999990 - this.mElapsedMillis);
        if (this.mTimer != null) {
            this.mTimer.start();
        }
    }

    private void alertReset() {
        Log.secD("TimerAlarmActivity", "alertReset");
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
        this.mHour = 0;
        this.mMinute = 0;
        this.mSecond = 0;
        this.mElapsedMillis = 0;
        this.mHunElapsedMillis = 0;
        TimerManager.sOffHookElapsedMillis = 0;
        TimerManager.sElapsedMillis = 0;
    }

    private synchronized void setTimer(long time) {
        Log.secD("TimerAlarmActivity", "setTimer");
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        this.mTimer = new CountDownTimer(time, 50) {
            public void onTick(long millisUntilFinished) {
                TimerAlarmActivity.this.mElapsedMillis = 359999990 - millisUntilFinished;
                TimerManager.sElapsedMillis = TimerAlarmActivity.this.mElapsedMillis;
                if (TimerAlarmActivity.this.mIsCoverOpen || StateUtils.isContextInDexMode(TimerAlarmActivity.this.mContext)) {
                    TimerAlarmActivity.this.updateTimeView();
                }
            }

            public void onFinish() {
            }
        };
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.mElapsedMillis = savedInstanceState.getLong("TIMER_ALERT_ELAPSED_TIME", 0);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("TIMER_ALERT_ELAPSED_TIME", this.mElapsedMillis);
        super.onSaveInstanceState(outState);
    }

    private void showCoverTimerByDialog() {
        if (this.mCoverViewSize == 3) {
            Log.secD("TimerAlarmActivity", "isForSmartCover");
            return;
        }
        if (this.mCover == null) {
            ScoverState coverState = this.mCoverManager.getCoverState();
            if (coverState != null) {
                this.mCover = new TimerCover(this, this.mCoverViewSize, coverState, 1);
                this.mCover.setListener(new TimerCoverListenerImpl());
                this.mCover.setTimerValues(this.mTimerNameString, this.mHunElapsedMillis);
            }
        }
        switch (this.mCoverType) {
            case 1:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
                this.mCover.displayDialog(this, 1);
                return;
            default:
                Log.secD("TimerAlarmActivity", "Cover Type is not viewType");
                return;
        }
    }
}
