package com.sec.android.app.clockpackage.timer.viewmodel;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.util.VRHelper.Global;
import com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.model.TimerData;

public class TimerAlarmPopupService extends HeadUpNotificationService {
    private final OnClickListener mClickListener = new C07685();
    private ScoverManager mCoverManager;
    private StateListener mCoverStateListener;
    private TextView mDoneTimeMinusText = null;
    private TextView mDoneTimeText = null;
    private int mHour = 0;
    private boolean mIsHideByAlarm = false;
    private int mMinute = 0;
    private BroadcastReceiver mReceiver = new C07707();
    private int mSecond = 0;
    private CountDownTimer mTimer;
    private RelativeLayout mTimerAlarmPopup;
    private ConstraintLayout mTimerAlarmPopupTopLayout;
    private TimerManager mTimerManager = null;
    private TextView mTimerName = null;
    private String mTimerNameString = null;
    @SuppressLint({"ClickableViewAccessibility"})
    private final OnTouchListener mTouchListener = new C07664();

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$1 */
    class C07631 implements Runnable {
        C07631() {
        }

        public void run() {
            TimerAlarmPopupService.this.setTelephonyListener();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$3 */
    class C07653 extends StateListener {
        C07653() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if ((!state.getSwitchState() ? 1 : null) == 1) {
                Log.secD("TimerAlarmPopupService", "mCoverStateListener cover is open -> close");
                TimerAlarmPopupService.this.finishTimer();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$4 */
    class C07664 implements OnTouchListener {
        C07664() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 0) {
                int id = v.getId();
                if (TimerData.getTimerState() == 1) {
                    if (TimerData.getRemainMillis() < ((long) (id == C0728R.id.timer_dismiss_btn_textview ? 550 : Callback.DEFAULT_SWIPE_ANIMATION_DURATION))) {
                        Log.secD("TimerAlarmPopupService", "Remaining time is too short");
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$5 */
    class C07685 implements OnClickListener {

        /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$5$1 */
        class C07671 implements Runnable {
            C07671() {
            }

            public void run() {
                TimerAlarmPopupService.this.mTimerManager.callTimerBroadcast();
                TimerAlarmPopupService.this.mTimerManager.callRestartToastPopup(TimerAlarmPopupService.this.mContext, false);
            }
        }

        C07685() {
        }

        public void onClick(View v) {
            int id = v.getId();
            Log.secD("TimerAlarmPopupService", "onClick(View " + v + ", id " + id + ")...");
            if (TimerAlarmPopupService.this.mIsAnimationRunning) {
                Log.secD("TimerAlarmPopupService", "animation is running");
            } else if (id == C0728R.id.timer_dismiss_btn_textview) {
                TimerAlarmPopupService.this.dismissAlertPopup();
                ClockUtils.insertSaLog("133", "1145");
            } else if (id == C0728R.id.timer_restart_btn_textview) {
                if (TimerData.getTimerState() == 1) {
                    TimerAlarmPopupService.this.mTimerManager.stopTimer();
                    TimerAlarmPopupService.this.mTimerManager.setTimerState(2);
                }
                new Handler().postDelayed(new C07671(), 100);
                TimerAlarmPopupService.this.animateForHide();
                TimerAlarmPopupService.this.finishTimer(0);
                ClockUtils.insertSaLog("133", "1144");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$6 */
    class C07696 implements Runnable {
        C07696() {
        }

        public void run() {
            TimerAlarmPopupService.this.finishTimer();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmPopupService$7 */
    class C07707 extends BroadcastReceiver {
        C07707() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                boolean bHmtDocked = Global.getBoolean(TimerAlarmPopupService.this.mContext.getContentResolver(), "hmt_dock", false);
                Log.secD("TimerAlarmPopupService", "mReceiver bHmtDocked = " + bHmtDocked);
                String action = intent.getAction();
                Log.secD("TimerAlarmPopupService", "Received action :" + action);
                boolean z = true;
                switch (action.hashCode()) {
                    case -2128145023:
                        if (action.equals("android.intent.action.SCREEN_OFF")) {
                            z = true;
                            break;
                        }
                        break;
                    case -1566616968:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT")) {
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
                    case 1145761348:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1318650491:
                        if (action.equals("com.sec.android.app.clockpackage.timer.ACTION_FINISH_TIMER_HUN")) {
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
                }
                switch (z) {
                    case false:
                        TimerAlarmPopupService.this.finishTimer();
                        return;
                    case true:
                        boolean isFlipOpen = intent.getBooleanExtra("flipOpen", false);
                        if (TimerAlarmPopupService.this.mIsPreFlipOpen && !isFlipOpen) {
                            Log.secD("TimerAlarmPopupService", "FlipFolder close");
                            TimerAlarmPopupService.this.finishTimer();
                        }
                        TimerAlarmPopupService.this.mIsPreFlipOpen = isFlipOpen;
                        return;
                    case true:
                        if (!bHmtDocked && !StateUtils.isInCallState(TimerAlarmPopupService.this.mContext)) {
                            TimerAlarmPopupService.this.startTimerAlarmActivity();
                            return;
                        }
                        return;
                    case true:
                        TimerAlarmPopupService.this.animateForHide();
                        TimerAlarmPopupService.this.stopTimerAlarmPopupService();
                        return;
                    case true:
                        TimerAlarmPopupService.this.mIsHideByAlarm = true;
                        TimerAlarmPopupService.this.removeHeadUpNotification();
                        return;
                    case true:
                        if (TimerAlarmPopupService.this.mIsHideByAlarm) {
                            TimerAlarmPopupService.this.mIsHideByAlarm = false;
                            if (!ClockUtils.sIsTimerAlertStarted) {
                                TimerAlarmPopupService.this.stopSelf();
                                return;
                            } else if (StateUtils.needToShowAsFullScreen(TimerAlarmPopupService.this.mContext)) {
                                TimerAlarmPopupService.this.startTimerAlarmActivity();
                                return;
                            } else {
                                TimerAlarmPopupService.this.showHeadUpNotification();
                                TimerAlarmPopupService.this.initTimeViews(false);
                                return;
                            }
                        }
                        return;
                    case true:
                        TimerAlarmPopupService.this.finishTimer(intent.getIntExtra("com.sec.android.clockpackage.timer.FINISH_MODE", 1));
                        return;
                    case true:
                        TimerAlarmPopupService.this.finishTimer(0);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void onCreate() {
        Log.secD("TimerAlarmPopupService", "onCreate()");
        this.mTimerManager = TimerManager.getInstance();
        this.mTimerManager.setContext(getApplicationContext());
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean z = false;
        Log.secD("TimerAlarmPopupService", "onStartCommand()");
        if (intent == null) {
            stopSelf();
            return 2;
        }
        if (intent.hasExtra("com.sec.android.clockpackage.timer.TIMER_NAME")) {
            this.mTimerNameString = intent.getStringExtra("com.sec.android.clockpackage.timer.TIMER_NAME");
        }
        setRegisterReceiver();
        boolean bKillByTimerService = intent.getBooleanExtra("bKillByTimerService", false);
        if (!(this.mTimer == null || bKillByTimerService)) {
            updateTimerAlarmPopup();
        }
        this.mIsHideByAlarm = false;
        new Handler().postDelayed(new C07631(), 200);
        setCoverStateManager();
        if (!bKillByTimerService) {
            z = true;
        }
        initTimeViews(z);
        alertStart();
        Intent iTimerStarted = new Intent();
        iTimerStarted.setAction("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT");
        sendBroadcast(iTimerStarted);
        ClockUtils.insertSaLog("133");
        return 1;
    }

    public void onDestroy() {
        Log.secD("TimerAlarmPopupService", "onDestroy()");
        unregisterCoverManager();
        resetTelephonyListener();
        setUnregisterReceiver();
        if (this.mTimerAlarmPopup != null) {
            this.mTimerAlarmPopup.removeAllViews();
            this.mTimerAlarmPopup = null;
        }
        alertReset();
        super.onDestroy();
    }

    private void setRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("com.sec.android.app.clockpackage.timer.ACTION_FINISH_TIMER_HUN");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");
        filter.addAction("com.sec.android.clockpackage.timer.FINISH_ALERT");
        filter.addAction("com.sec.android.clockpackage.timer.finishAlertByRestart");
        if (Feature.isFolder(this.mContext)) {
            filter.addAction("com.samsung.flipfolder.OPEN");
        }
        filter.setPriority(999);
        registerReceiver(this.mReceiver, filter);
    }

    private void setUnregisterReceiver() {
        if (this.mReceiver != null) {
            try {
                unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secE("TimerAlarmPopupService", "IllegalArgumentException - unregisterReceiver(MyReceiver)");
                Log.secE("TimerAlarmPopupService", "Exception : " + e.toString());
            }
        }
    }

    private void setTelephonyListener() {
        ((TelephonyManager) getSystemService("phone")).listen(this.phoneStateListener, 32);
    }

    private void resetTelephonyListener() {
        ((TelephonyManager) getSystemService("phone")).listen(this.phoneStateListener, 0);
    }

    private void finishTimer() {
        finishTimer(1);
    }

    private void finishTimer(int mode) {
        Log.secD("TimerAlarmPopupService", "finishTimer mode = " + mode);
        if (mode != 2) {
            if (mode == 1) {
                TimerData.setRestartMillis(0);
            }
            Intent iTimerStopped = new Intent();
            iTimerStopped.setAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
            sendBroadcast(iTimerStopped);
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT"));
        }
        stopTimerAlarmPopupService();
    }

    private void stopTimerAlarmPopupService() {
        ClockUtils.timerAlertTimeInCall = 0;
        alertReset();
        if (TimerData.getTimerState() != 1) {
            this.mTimerManager.clearTimerAlarmManager();
        }
        stopSelf();
    }

    private void initTimeViews(boolean isFirst) {
        Log.secD("TimerAlarmPopupService", "initTimeViews() isFirst = " + isFirst);
        if (isFirst) {
            TimerManager.sElapsedMillis = 0;
            if (TimerManager.sOffHookElapsedMillis != 0) {
                TimerManager.sElapsedMillis = System.currentTimeMillis() - TimerManager.sOffHookElapsedMillis;
                Log.secD("TimerAlarmPopupService", "initTimeViews original mElapsedMillis1 = " + TimerManager.sElapsedMillis);
                if (TimerManager.sElapsedMillis % 1000 > 600) {
                    TimerManager.sElapsedMillis = ((TimerManager.sElapsedMillis / 1000) + 1) * 1000;
                    Log.secD("TimerAlarmPopupService", "initTimeViews modified mElapsedMillis2 = " + TimerManager.sElapsedMillis);
                }
                TimerManager.sOffHookElapsedMillis = 0;
            }
        }
        Log.secD("TimerAlarmPopupService", "initTimeViews sElapsedMillis " + TimerManager.sElapsedMillis);
        this.mHour = (int) (TimerManager.sElapsedMillis / 3600000);
        this.mMinute = (int) ((TimerManager.sElapsedMillis % 3600000) / 60000);
        this.mSecond = (int) ((TimerManager.sElapsedMillis % 60000) / 1000);
        if (this.mTimerName != null) {
            if (this.mTimerNameString == null || "".equals(this.mTimerNameString)) {
                this.mTimerName.setText(C0728R.string.timer_times_out);
            } else {
                this.mTimerName.setText(this.mTimerNameString);
            }
        }
        if (this.mTimerAlarmPopupTopLayout != null) {
            this.mTimerAlarmPopupTopLayout.setContentDescription(TimerUtils.getDescriptionString(this.mContext, this.mHour, this.mMinute, this.mSecond));
        }
        setTimeView(this.mHour, this.mMinute, this.mSecond);
        setFontFromOpenTheme();
    }

    @SuppressLint({"SetTextI18n"})
    private void setTimeView(int hour, int minute, int second) {
        if (this.mDoneTimeMinusText != null) {
            if (hour == 0 && minute == 0 && second == 0) {
                this.mDoneTimeMinusText.setVisibility(4);
            } else {
                this.mDoneTimeMinusText.setVisibility(0);
            }
        }
        if (this.mDoneTimeText != null) {
            String timeSeparatorText = ClockUtils.getTimeSeparatorText(this.mContext);
            this.mDoneTimeText.setText(ClockUtils.toTwoDigitString(hour) + timeSeparatorText + ClockUtils.toTwoDigitString(minute) + timeSeparatorText + ClockUtils.toTwoDigitString(second));
        }
        if (this.mTimerAlarmPopupTopLayout != null) {
            this.mTimerAlarmPopupTopLayout.setContentDescription(TimerUtils.getDescriptionString(this.mContext, hour, minute, second));
        }
    }

    private void setFontFromOpenTheme() {
        Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (font == null) {
            if (StateUtils.isGameModeOn()) {
                font = Typeface.create("sec-roboto-condensed", 1);
            } else {
                font = Typeface.create("sans-serif-light", 0);
            }
        }
        if (this.mDoneTimeText != null) {
            this.mDoneTimeText.setTypeface(font);
        }
        if (this.mDoneTimeMinusText != null) {
            this.mDoneTimeMinusText.setTypeface(font);
        }
    }

    private void alertStart() {
        Log.secD("TimerAlarmPopupService", "alertStart()");
        setTimer(359999990 - TimerManager.sElapsedMillis);
        if (this.mTimer != null) {
            this.mTimer.start();
        }
    }

    private void alertReset() {
        Log.secD("TimerAlarmPopupService", "alertReset()");
        resetTimer();
        this.mHour = 0;
        this.mMinute = 0;
        this.mSecond = 0;
        TimerManager.sElapsedMillis = 0;
    }

    private synchronized void setTimer(long time) {
        Log.secD("TimerAlarmPopupService", "setTimer()");
        resetTimer();
        this.mTimer = new CountDownTimer(time, 50) {
            public void onTick(long millisUntilFinished) {
                TimerManager.sElapsedMillis = 359999990 - millisUntilFinished;
                TimerAlarmPopupService.this.updateTimeView();
            }

            public void onFinish() {
                Log.secD("TimerAlarmPopupService", "onFinish()");
            }
        };
    }

    private void updateTimeView() {
        int hour = (int) (TimerManager.sElapsedMillis / 3600000);
        int minute = (int) ((TimerManager.sElapsedMillis % 3600000) / 60000);
        int second = (int) ((TimerManager.sElapsedMillis % 60000) / 1000);
        int milliSecond = (int) (TimerManager.sElapsedMillis % 1000);
        if (this.mSecond != second && milliSecond < Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            setTimeView(hour, minute, second);
            this.mHour = hour;
            this.mMinute = minute;
            this.mSecond = second;
        }
    }

    public void animateForShow() {
        super.animateForShow();
    }

    public void animateForHide() {
        super.animateForHide();
    }

    public void animateForSlideOut(boolean toLeft) {
        super.animateForSlideOut(toLeft);
        finishTimer();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initTimeViews(false);
    }

    protected void onCreateCustomView(ViewGroup root) {
        Log.secD("TimerAlarmPopupService", "onCreateCustomView()");
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (StateUtils.isGameModeOn()) {
            this.mTimerAlarmPopup = (RelativeLayout) inflater.inflate(C0728R.layout.timer_alarm_popup_view_game, root);
        } else {
            this.mTimerAlarmPopup = (RelativeLayout) inflater.inflate(C0728R.layout.timer_alarm_popup_view, root);
            this.mTimerName = (TextView) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_time_out);
        }
        this.mTimerAlarmPopupTopLayout = (ConstraintLayout) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_hun_top_layout);
        this.mDoneTimeText = (TextView) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_done_time);
        this.mDoneTimeMinusText = (TextView) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_done_time_minus);
        Button dismissBtn = (Button) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_dismiss_btn_textview);
        Button restartBtn = (Button) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_restart_btn_textview);
        try {
            dismissBtn.semSetButtonShapeEnabled(true);
            restartBtn.semSetButtonShapeEnabled(true);
        } catch (NoSuchMethodError e) {
            Log.secE("TimerAlarmPopupService", "NoSuchMethodError : " + e);
        }
        if (dismissBtn != null) {
            dismissBtn.setOnClickListener(this.mClickListener);
            dismissBtn.setOnTouchListener(this.mTouchListener);
        }
        if (restartBtn != null) {
            restartBtn.setOnClickListener(this.mClickListener);
            restartBtn.setOnTouchListener(this.mTouchListener);
        }
        ClockUtils.setLargeTextSize(this.mContext, new TextView[]{(TextView) this.mTimerAlarmPopup.findViewById(C0728R.id.timer_app_name), dismissBtn, restartBtn}, 1.3f);
    }

    private void updateTimerAlarmPopup() {
        removeHeadUpNotification();
        showHeadUpNotification();
        resetTimer();
        TimerManager.sElapsedMillis = 0;
    }

    private void setCoverStateManager() {
        this.mCoverManager = new ScoverManager(this.mContext);
        this.mCoverStateListener = new C07653();
        this.mCoverManager.registerListener(this.mCoverStateListener);
    }

    private void unregisterCoverManager() {
        if (this.mCoverManager != null) {
            this.mCoverManager.unregisterListener(this.mCoverStateListener);
            this.mCoverManager = null;
        }
    }

    private void resetTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    protected void changedViewByPhoneState(int callState) {
        switch (callState) {
            case 0:
                if (StateUtils.needToShowAsFullScreen(this.mContext)) {
                    startTimerAlarmActivity();
                    return;
                } else if (!this.mTimerManager.isTimerAlarmTopActivity() && !StateUtils.isVideoCall(this.mContext)) {
                    animateForShow();
                    return;
                } else {
                    return;
                }
            case 1:
            case 2:
                if (!StateUtils.isVideoCall(this.mContext)) {
                    animateForHide();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void dismissAlertPopup() {
        animateForHide();
        Log.secD("TimerAlarmPopupService", "dismissAlertPopup");
        new Handler().postDelayed(new C07696(), 250);
    }

    private void startTimerAlarmActivity() {
        Log.secD("TimerAlarmPopupService", "startTimerAlarmActivity");
        if (!StateUtils.isInLockTaskMode(this.mContext) && !this.mIsHideByAlarm) {
            animateForHide();
            Intent i = new Intent();
            i.setClass(this.mContext, TimerAlarmActivity.class);
            i.setFlags(268697600);
            i.putExtra("HUN_ELAPSED_TIME", TimerManager.sElapsedMillis);
            i.putExtra("android.intent.extra.alarm.MESSAGE", this.mTimerNameString);
            i.putExtra("IS_HIDE_BY_ALARM", this.mIsHideByAlarm);
            this.mContext.startActivity(i);
            stopTimerAlarmPopupService();
        }
    }
}
