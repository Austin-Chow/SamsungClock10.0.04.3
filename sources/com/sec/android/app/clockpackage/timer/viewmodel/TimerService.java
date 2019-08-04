package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.samsung.android.gesture.SemMotionEventListener;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.gesture.SemMotionRecognitionManager;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.util.AlertUtils.StopLedBackCoverTimerTask;
import com.sec.android.app.clockpackage.commonalert.util.FlashNotificationUtils;
import com.sec.android.app.clockpackage.commonalert.util.VRHelper.Global;
import com.sec.android.app.clockpackage.timer.callback.TimerAlertPlayerListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import java.util.Timer;

public class TimerService extends Service {
    private CountDownTimer mCheckingPhoneStateTimer;
    private Context mContext;
    private int mInitialCallState = 0;
    private int mInitialCallState2 = 0;
    private int mInitialRingState;
    private InternalReceiver mInternalReceiver;
    private boolean mIsHideByAlarm = false;
    private boolean mIsMuted = false;
    private boolean mIsPalm = false;
    private int mIsPlugInEarphone = 0;
    private boolean mIsVoipCall = false;
    private SemMotionEventListener mMotionListener = null;
    private SemMotionRecognitionManager mMotionSensorManager = null;
    private boolean mNotificationTouch = false;
    private BroadcastReceiver mReceiver = new C08306();
    private CountDownTimer mReplayLoopTimer;
    private Timer mStopLedBackCoverTimer = null;
    private TelephonyManager mTelephonyManager;
    private TelephonyManager mTelephonyManager2;
    private TimerAlertPlayer mTimerAlertPlayer;
    private TimerAlertPlayerListener mTimerAlertPlayerListener = new C08339();
    private TimerManager mTimerManager = null;
    String mTimerName = "";
    private boolean mWasRecording = false;
    private PhoneStateListener phoneStateListener = new C08317();
    private PhoneStateListener phoneStateListener2 = new C08328();

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$1 */
    class C08241 implements Runnable {
        C08241() {
        }

        public void run() {
            TimerService.this.killTimerAlarmActivity("com.sec.android.app.clockpackage.timer.KILL_DUPLICATED_BY_TIMER_SERVICE ");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$3 */
    class C08263 implements Runnable {
        C08263() {
        }

        public void run() {
            TimerAlarmNotificationHelper.cancel(TimerService.this.getApplicationContext());
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$4 */
    class C08274 implements SemMotionEventListener {
        C08274() {
        }

        public void onMotionEvent(SemMotionRecognitionEvent motionEvent) {
            switch (motionEvent.getMotion()) {
                case 10:
                    Log.secD("TimerService", "Motion : FLIP_SCREEN_DOWN/ sIsTimerAlertStarted = " + ClockUtils.sIsTimerAlertStarted + ", mIsHideByAlarm = " + TimerService.this.mIsHideByAlarm);
                    ClockUtils.insertSaLog("132", "1142");
                    if (ClockUtils.sIsTimerAlertStarted && !TimerService.this.mIsHideByAlarm && !StateUtils.isInCallState(TimerService.this.getApplicationContext())) {
                        TimerService.this.mIsPalm = true;
                        if (TimerService.this.mTimerAlertPlayer != null) {
                            TimerService.this.mTimerAlertPlayer.stop(true);
                        }
                        FlashNotificationUtils.stopFlashNotification(TimerService.this.mContext);
                        if (TimerService.this.mCheckingPhoneStateTimer != null) {
                            TimerService.this.mCheckingPhoneStateTimer.cancel();
                            TimerService.this.mCheckingPhoneStateTimer = null;
                            return;
                        }
                        return;
                    }
                    return;
                case 86:
                    Log.secD("TimerService", "Motion : FLIP_BOTTOM_TO_TOP/ sIsTimerAlertStarted = " + ClockUtils.sIsTimerAlertStarted + ", mIsHideByAlarm = " + TimerService.this.mIsHideByAlarm);
                    if (ClockUtils.sIsTimerAlertStarted && !TimerService.this.mIsHideByAlarm && !StateUtils.isInCallState(TimerService.this.getApplicationContext())) {
                        FlashNotificationUtils.stopFlashNotification(TimerService.this.mContext);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$6 */
    class C08306 extends BroadcastReceiver {

        /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$6$1 */
        class C08291 implements Runnable {
            C08291() {
            }

            public void run() {
                TimerService.this.stopSelf();
            }
        }

        C08306() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && TimerService.this.mTimerAlertPlayer != null) {
                String action = intent.getAction();
                Log.secD("TimerService", "Received action : " + action);
                boolean z = true;
                switch (action.hashCode()) {
                    case -2128145023:
                        if (action.equals("android.intent.action.SCREEN_OFF")) {
                            z = false;
                            break;
                        }
                        break;
                    case -2092605434:
                        if (action.equals("com.samsung.android.mirrorlink.ML_STATE")) {
                            z = true;
                            break;
                        }
                        break;
                    case -1676458352:
                        if (action.equals("android.intent.action.HEADSET_PLUG")) {
                            z = true;
                            break;
                        }
                        break;
                    case -1670536538:
                        if (action.equals("android.app.action.ENTER_KNOX_DESKTOP_MODE")) {
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
                    case -1303321789:
                        if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT")) {
                            z = true;
                            break;
                        }
                        break;
                    case -369676425:
                        if (action.equals("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION")) {
                            z = true;
                            break;
                        }
                        break;
                    case 2034148:
                        if (action.equals("android.app.action.EXIT_KNOX_DESKTOP_MODE")) {
                            z = true;
                            break;
                        }
                        break;
                    case 279267191:
                        if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT")) {
                            z = true;
                            break;
                        }
                        break;
                    case 959232034:
                        if (action.equals("android.intent.action.USER_SWITCHED")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1055975735:
                        if (action.equals("com.samsung.android.motion.PALM_DOWN")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1920758225:
                        if (action.equals("android.media.STREAM_MUTE_CHANGED_ACTION")) {
                            z = true;
                            break;
                        }
                        break;
                    case 2070024785:
                        if (action.equals("android.media.RINGER_MODE_CHANGED")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        if (!TimerService.this.mIsPalm && !TimerService.this.mIsHideByAlarm && !StateUtils.isInCallState(TimerService.this.getApplicationContext()) && !TimerService.this.mIsMuted) {
                            TimerService.this.mTimerAlertPlayer.play();
                            return;
                        }
                        return;
                    case true:
                        ClockUtils.insertSaLog("132", "1141");
                        if (!TimerService.this.mIsHideByAlarm) {
                            if (TimerService.this.mTimerAlertPlayer.isPlaying() || TimerService.this.mTimerAlertPlayer.isVibrating()) {
                                TimerService.this.mTimerAlertPlayer.stop(true);
                                FlashNotificationUtils.stopFlashNotification(TimerService.this.mContext);
                                TimerService.this.mIsPalm = true;
                                if (TimerService.this.mCheckingPhoneStateTimer != null) {
                                    TimerService.this.mCheckingPhoneStateTimer.cancel();
                                    TimerService.this.mCheckingPhoneStateTimer = null;
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    case true:
                    case true:
                        TimerService.this.mIsHideByAlarm = false;
                        if (!ClockUtils.sIsTimerAlertStarted) {
                            Log.secD("TimerService", "No need to Alert again because the same timer has dismissed");
                            TimerService.this.stopSelf();
                            return;
                        } else if (TimerService.this.mTimerAlertPlayer.isAudioManagerNormalMode() && TimerService.this.mTimerAlertPlayer.isNeedReplay()) {
                            Log.secD("TimerService", "Play");
                            TimerService.this.mTimerAlertPlayer.setNeedReplay(false);
                            if (!TimerService.this.mIsPalm) {
                                FlashNotificationUtils.startFlashNotification(TimerService.this.mContext);
                                if (!TimerService.this.mIsMuted) {
                                    TimerService.this.mTimerAlertPlayer.play();
                                    return;
                                }
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    case true:
                        new Handler().postDelayed(new C08291(), 10);
                        return;
                    case true:
                    case true:
                    case true:
                    case true:
                        TimerService.this.sendBroadcast(new Intent("com.sec.android.clockpackage.timer.FINISH_ALERT"));
                        TimerService.this.killTimerAlarmActivity("com.sec.android.app.clockpackage.timer.KILL_BY_TIMER_SERVICE");
                        TimerService.this.stopSelf();
                        return;
                    case true:
                        if (StateUtils.isSoundModeOnForJapan(TimerService.this.getApplicationContext())) {
                            int state = intent.getIntExtra("state", 0);
                            Log.secD("TimerService", "ACTION_HEADSET_PLUG state : " + state);
                            if (TimerService.this.mIsPlugInEarphone != state) {
                                TimerService.this.mIsPlugInEarphone = state;
                                if (StateUtils.isMuteOrVibrateForSystemSoundMode(TimerService.this.getApplicationContext()) && !StateUtils.isRecordingState(context)) {
                                    TimerService.this.mTimerAlertPlayer.stop(true);
                                    if (TimerService.this.mIsPlugInEarphone == 1) {
                                        TimerService.this.mTimerAlertPlayer.setStreamMusicModeForJapan(true);
                                    } else {
                                        TimerService.this.mTimerAlertPlayer.setStreamMusicModeForJapan(false);
                                    }
                                    TimerService.this.mTimerAlertPlayer.play();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    case true:
                        if (StateUtils.isSoundModeOnForJapan(TimerService.this.getApplicationContext())) {
                            int currentRingerMode = TimerService.this.mTimerAlertPlayer == null ? 0 : TimerService.this.mTimerAlertPlayer.getAudioManager().getRingerMode();
                            if (TimerService.this.mInitialRingState != currentRingerMode && TimerService.this.mTimerAlertPlayer != null && TimerService.this.mTimerAlertPlayer.isAudioFocusGain() && ClockUtils.sIsTimerAlertStarted) {
                                if (TimerService.this.mIsPlugInEarphone == 1) {
                                    if (StateUtils.isMuteOrVibrateForSystemSoundMode(TimerService.this.getApplicationContext())) {
                                        TimerService.this.mTimerAlertPlayer.setStreamMusicModeForJapan(true);
                                    } else {
                                        TimerService.this.mTimerAlertPlayer.setStreamMusicModeForJapan(false);
                                    }
                                }
                                if (TimerService.this.mTimerAlertPlayer != null) {
                                    TimerService.this.mTimerAlertPlayer.updateVibrationState();
                                    if (!TimerService.this.mIsHideByAlarm) {
                                        TimerService.this.mTimerAlertPlayer.stop(false);
                                        TimerService.this.mTimerAlertPlayer.play();
                                    }
                                }
                                TimerService.this.mInitialRingState = currentRingerMode;
                                return;
                            }
                            return;
                        }
                        return;
                    case true:
                        if (!TimerService.this.mIsHideByAlarm) {
                            FlashNotificationUtils.stopFlashNotification(TimerService.this.mContext);
                            return;
                        }
                        return;
                    case true:
                        if (intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", 0) == 4 && !intent.getBooleanExtra("android.media.EXTRA_STREAM_VOLUME_MUTED", false)) {
                            TimerService.this.mTimerAlertPlayer.updateVolume();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$7 */
    class C08317 extends PhoneStateListener {
        C08317() {
        }

        public void onCallStateChanged(int state, String ignored) {
            TimerService.this.doCheckCallState(state);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$8 */
    class C08328 extends PhoneStateListener {
        C08328() {
        }

        public void onCallStateChanged(int state, String ignored) {
            TimerService.this.doCheckCallState(state);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerService$9 */
    class C08339 implements TimerAlertPlayerListener {
        C08339() {
        }

        public void onSetIsHideByAlarm(boolean b) {
            TimerService.this.mIsHideByAlarm = b;
        }
    }

    private class InternalReceiver extends BroadcastReceiver {
        private InternalReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("TimerService", "onReceive() " + action);
            boolean z = true;
            switch (action.hashCode()) {
                case -1160262742:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_START")) {
                        z = false;
                        break;
                    }
                    break;
                case 279267191:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT")) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    if (TimerService.this.mTimerAlertPlayer != null) {
                        TimerService.this.mTimerAlertPlayer.setNeedReplay(true);
                        TimerService.this.mTimerAlertPlayer.stop(true);
                        TimerService.this.mIsHideByAlarm = true;
                        return;
                    }
                    return;
                case true:
                    TimerService.this.stopSelf();
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        Log.secD("TimerService", "onCreate");
        this.mContext = this;
        this.mTimerManager = TimerManager.getInstance();
        this.mTimerManager.setContext(getApplicationContext());
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
        this.mTelephonyManager.listen(this.phoneStateListener, 32);
        if (this.mTimerAlertPlayer == null) {
            Log.secD("TimerService", "mTimerAlertPlayer == null");
            this.mTimerAlertPlayer = new TimerAlertPlayer(this, this.mTimerAlertPlayerListener);
        }
        if (StateUtils.isDualSlot(this.mContext)) {
            this.mTelephonyManager2 = (TelephonyManager) getSystemService("phone");
            this.mTelephonyManager2.listen(this.phoneStateListener2, 32);
        }
        this.mInternalReceiver = new InternalReceiver();
        setRegisterReceiver();
        if (!Feature.isTablet(getApplicationContext()) && Feature.isMotionSupported()) {
            Log.secD("TimerService", "registerMotionListener");
            registerMotionListener();
        }
    }

    private void setRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
        filter.addAction("android.app.action.ENTER_KNOX_DESKTOP_MODE");
        filter.addAction("android.app.action.EXIT_KNOX_DESKTOP_MODE");
        filter.addAction("com.samsung.android.mirrorlink.ML_STATE");
        filter.addAction("com.samsung.android.motion.PALM_DOWN");
        filter.addAction("android.intent.action.USER_SWITCHED");
        filter.addAction("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION");
        filter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
        if (StateUtils.isSoundModeOnForJapan(getApplicationContext())) {
            filter.addAction("android.intent.action.HEADSET_PLUG");
            filter.addAction("android.media.RINGER_MODE_CHANGED");
        }
        registerReceiver(this.mReceiver, filter);
        IntentFilter internalFilter = new IntentFilter();
        internalFilter.addAction("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_START");
        internalFilter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mInternalReceiver, internalFilter);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.secD("TimerService", "onStartCommand");
        if (intent == null) {
            stopSelf();
            return 2;
        }
        this.mNotificationTouch = intent.getBooleanExtra("com.sec.android.app.clockpackage.timer.TIMER_TIMER_NOTIFICATION_TOUCH", false);
        boolean bHmtDocked = Global.getBoolean(getApplicationContext().getContentResolver(), "hmt_dock", false);
        this.mTimerName = intent.getStringExtra("com.sec.android.clockpackage.timer.TIMER_NAME");
        TimerData.setRestartTimerName(this.mTimerName);
        Log.secD("TimerService", "mRestartTime = " + TimerData.getRestartMillis() + ", mTimerName = " + this.mTimerName);
        Notification notification = TimerAlarmNotificationHelper.makeNotify(this, this.mTimerName);
        if (!this.mNotificationTouch) {
            startForeground(84637, notification);
            TimerData.setRestartMillis(intent.getLongExtra("com.sec.android.clockpackage.timer.TIMER_ALERT_INPUT_MILLIS", 0));
            if (this.mTimerManager != null) {
                this.mTimerManager.setTimerState(3);
                this.mTimerManager.updateScreenReset();
            }
        }
        if ((!bHmtDocked && StateUtils.needToShowAsFullScreen(this)) || this.mNotificationTouch) {
            Intent i = new Intent();
            i.setClass(this, TimerAlarmActivity.class);
            i.addFlags(268697600);
            i.putExtra("com.sec.android.clockpackage.timer.TIMER_NAME", this.mTimerName);
            i.putExtra("com.sec.android.app.clockpackage.timer.TIMER_TIMER_NOTIFICATION_TOUCH", this.mNotificationTouch);
            if (this.mNotificationTouch) {
                startActivity(i, ActivityOptions.makeBasic().setLaunchDisplayId(intent.getIntExtra("CLICK_DISPLAYID", 0)).toBundle());
            } else {
                startActivity(i);
            }
        } else if (!(this.mTimerManager.isTimerAlarmTopActivity() && !StateUtils.isContextInDexMode(this) && ClockUtils.getMyProcessId() == ClockUtils.getTopActivityProcessId(getApplicationContext()))) {
            new Handler().postDelayed(new C08241(), 500);
            if (StateUtils.isUniversalSwitchEnabled(this)) {
                sendAccessibilityEvent(notification);
            }
            callHUN(this.mTimerName, false);
        }
        onStartCommandAction(intent);
        ScoverManager coverManager = new ScoverManager(this);
        if (coverManager != null) {
            ScoverState coverState = coverManager.getCoverState();
            if (coverState != null && coverState.getType() == 14) {
                Log.secD("TimerService", "when TYPE_LED_BACK_COVER , after 1 min send broadcast to LED side");
                startLEDBackCoverTimer();
            }
        }
        return 1;
    }

    public void onStartCommandAction(Intent intent) {
        String action = intent.getAction();
        boolean z = true;
        switch (action.hashCode()) {
            case 1903286863:
                if (action.equals("com.sec.android.app.clockpackage.timer.SERVICE_START")) {
                    z = false;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                ClockUtils.sIsTimerAlertStarted = true;
                if (this.mTimerAlertPlayer != null) {
                    this.mTimerAlertPlayer.resetCurrentAudioFocus();
                }
                Log.secD("TimerService", "ACTION_TIMER_SERVICE_START");
                Log.secD("TimerService", "mIsPalm = " + this.mIsPalm);
                if (!this.mIsPalm) {
                    if (!StateUtils.isInCallState(getApplicationContext())) {
                        FlashNotificationUtils.startFlashNotification(this.mContext);
                    }
                    if (!(this.mTimerAlertPlayer == null || this.mIsMuted)) {
                        if (this.mTimerAlertPlayer.isPlaying() && !this.mNotificationTouch) {
                            this.mTimerAlertPlayer.stop(false);
                            this.mTimerAlertPlayer.initTimerSoundInfo();
                        }
                        this.mTimerAlertPlayer.play();
                    }
                    if (this.mCheckingPhoneStateTimer == null) {
                        this.mCheckingPhoneStateTimer = new CountDownTimer(359999990, 500) {
                            public void onTick(long millisUntilFinished) {
                                Log.secD("TimerService", "mCheckingPhoneStateTimer onTick");
                                boolean isRecordingNow = StateUtils.isRecordingState(TimerService.this.mContext);
                                if (!TimerService.this.mWasRecording && isRecordingNow) {
                                    Log.secD("TimerService", "mWasRecording " + TimerService.this.mWasRecording + " isRecordingNow " + isRecordingNow);
                                    TimerService.this.mWasRecording = true;
                                    FlashNotificationUtils.stopFlashNotification(TimerService.this.mContext);
                                } else if (TimerService.this.mTimerAlertPlayer != null && !TimerService.this.mIsHideByAlarm) {
                                    TimerService.this.updateTimerAlertPlayerState();
                                }
                            }

                            public void onFinish() {
                                Log.secD("TimerService", "mCheckingPhoneStateTimer onFinish()");
                                TimerService.this.stopSelf();
                            }
                        };
                    }
                    if (!(this.mTimerAlertPlayer == null || StateUtils.isInCall(getApplicationContext()) || this.mCheckingPhoneStateTimer == null)) {
                        Log.secD("TimerService", "mCheckingPhoneStateTimer.start");
                        this.mCheckingPhoneStateTimer.start();
                    }
                }
                setInitialCallState();
                if (this.mTimerAlertPlayer != null) {
                    this.mInitialRingState = this.mTimerAlertPlayer.getAudioManager().getRingerMode();
                    if (StateUtils.isInCallState(getApplicationContext())) {
                        this.mTimerAlertPlayer.setNeedReplay(true);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setInitialCallState() {
        if (StateUtils.isDualSlot(this.mContext)) {
            int callState = StateUtils.getMultiSimCallState(this, 0);
            int callState2 = StateUtils.getMultiSimCallState(this, 1);
            if (callState == 0 && callState2 == 0) {
                this.mInitialCallState2 = 0;
            } else if (callState == 1 || callState2 == 1) {
                this.mInitialCallState2 = 1;
            } else if (callState == 2 || callState2 == 2) {
                this.mInitialCallState2 = 2;
            }
            this.mInitialCallState = this.mInitialCallState2;
            return;
        }
        this.mInitialCallState = this.mTelephonyManager.getCallState();
    }

    public void onDestroy() {
        ClockUtils.sIsTimerAlertStarted = false;
        Log.secD("TimerService", "onDestroy");
        FlashNotificationUtils.stopFlashNotification(this.mContext);
        if (this.mTimerAlertPlayer != null) {
            this.mTimerAlertPlayer.stop(true);
            this.mTimerAlertPlayer.initTimerSoundInfo();
            this.mTimerAlertPlayer.setNeedReplay(false);
            this.mTimerAlertPlayer = null;
        }
        stopForeground(true);
        if (this.mCheckingPhoneStateTimer != null) {
            this.mCheckingPhoneStateTimer.cancel();
            Log.secD("TimerService", "mCheckingPhoneStateTimer.cancel");
            this.mCheckingPhoneStateTimer = null;
        }
        if (this.mReplayLoopTimer != null) {
            this.mReplayLoopTimer.cancel();
            this.mReplayLoopTimer = null;
        }
        if (this.mStopLedBackCoverTimer != null) {
            this.mStopLedBackCoverTimer.cancel();
        }
        new Handler().postDelayed(new C08263(), 200);
        setUnregisterReceiver();
        if (!Feature.isTablet(getApplicationContext()) && Feature.isMotionSupported()) {
            unregisterMotionListener();
        }
        this.mTelephonyManager.listen(this.phoneStateListener, 0);
        if (StateUtils.isDualSlot(this.mContext)) {
            this.mTelephonyManager2.listen(this.phoneStateListener2, 0);
        }
        if (this.mTimerManager != null) {
            this.mTimerManager = null;
        }
        super.onDestroy();
    }

    private void setUnregisterReceiver() {
        unregisterReceiver(this.mReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.mInternalReceiver);
    }

    private void registerMotionListener() {
        RuntimeException e;
        try {
            this.mMotionListener = new C08274();
            if (this.mMotionSensorManager == null) {
                this.mMotionSensorManager = (SemMotionRecognitionManager) getSystemService("motion_recognition");
            }
            if (this.mMotionSensorManager != null) {
                this.mMotionSensorManager.registerListener(this.mMotionListener, 1);
                this.mMotionSensorManager.registerListener(this.mMotionListener, 131072);
            }
        } catch (IllegalArgumentException e2) {
            e = e2;
            Log.secD("TimerService", "SemMotionEventListener.onMotionEvent exception : " + e.toString());
        } catch (NullPointerException e3) {
            e = e3;
            Log.secD("TimerService", "SemMotionEventListener.onMotionEvent exception : " + e.toString());
        }
    }

    private void unregisterMotionListener() {
        if (this.mMotionSensorManager != null) {
            this.mMotionSensorManager.unregisterListener(this.mMotionListener);
        }
    }

    private void killTimerAlarmActivity(String action) {
        Intent iStop = new Intent();
        iStop.setAction(action);
        sendBroadcast(iStop);
    }

    private void callHUN(String timerName, boolean bKill) {
        Intent alert = new Intent();
        alert.setClass(this, TimerAlarmPopupService.class);
        alert.putExtra("com.sec.android.clockpackage.timer.TIMER_NAME", timerName);
        if (bKill) {
            alert.putExtra("bKillByTimerService", true);
        }
        try {
            startService(alert);
        } catch (IllegalStateException e) {
            Log.m42e("TimerService", "callHUN exception : " + e.toString());
        }
    }

    private void doCheckCallState(int state) {
        Log.secD("TimerService", "doCheckCallState() state = " + state);
        if (StateUtils.isDualSlot(this.mContext)) {
            if (state == this.mInitialCallState2) {
                return;
            }
        } else if (state == this.mInitialCallState) {
            return;
        }
        this.mInitialCallState = state;
        this.mInitialCallState2 = state;
        if (this.mTimerAlertPlayer != null) {
            switch (state) {
                case 0:
                    replayLoopAfterIdle();
                    Log.secD("TimerService", "sIsPausedTimerActivity = " + TimerManager.sIsPausedTimerActivity);
                    if ((TimerManager.sIsPausedTimerActivity || this.mIsHideByAlarm) && !StateUtils.isRecordingState(getApplicationContext())) {
                        String currentTop = ClockUtils.getTopActivity(getApplication());
                        if (ClockUtils.sIsTimerAlertStarted && !this.mTimerManager.isTimerAlarmTopActivity() && !currentTop.contains("InCallActivity")) {
                            Log.secD("TimerService", "HUN should be shown. TimerAlarmActivity killed");
                            killTimerAlarmActivity("com.sec.android.app.clockpackage.timer.KILL_DUPLICATED_BY_TIMER_SERVICE ");
                            callHUN(this.mTimerName, true);
                            return;
                        }
                        return;
                    }
                    return;
                case 1:
                case 2:
                    this.mTimerAlertPlayer.setNeedReplay(true);
                    if (this.mCheckingPhoneStateTimer != null) {
                        this.mCheckingPhoneStateTimer.cancel();
                    }
                    this.mIsVoipCall = false;
                    this.mTimerAlertPlayer.stop(true);
                    FlashNotificationUtils.stopFlashNotification(this.mContext);
                    return;
                default:
                    return;
            }
        }
    }

    private void replayLoopAfterIdle() {
        this.mReplayLoopTimer = new CountDownTimer(7000, 100) {
            public void onTick(long millisUntilFinished) {
                if (!TimerService.this.mIsHideByAlarm && TimerService.this.mTimerAlertPlayer != null && TimerService.this.mTimerAlertPlayer.isAudioManagerNormalMode() && TimerService.this.mTimerAlertPlayer.isNeedReplay()) {
                    Log.secD("TimerService", "Play in mReplayLoopTimer");
                    if (TimerService.this.mTimerAlertPlayer.isPlaying() && TimerService.this.mReplayLoopTimer != null) {
                        TimerService.this.mTimerAlertPlayer.setNeedReplay(false);
                        TimerService.this.mReplayLoopTimer.cancel();
                    }
                    if (!TimerService.this.mIsPalm && !TimerService.this.mIsMuted) {
                        FlashNotificationUtils.startFlashNotification(TimerService.this.mContext);
                        TimerService.this.mTimerAlertPlayer.play();
                        if (TimerService.this.mCheckingPhoneStateTimer != null) {
                            TimerService.this.mCheckingPhoneStateTimer.start();
                        }
                    }
                } else if (!TimerService.this.mIsVoipCall && !TimerService.this.mIsPalm && !TimerService.this.mIsMuted && StateUtils.isInVoipCall(TimerService.this.getApplicationContext()) && TimerService.this.mCheckingPhoneStateTimer != null) {
                    TimerService.this.mIsVoipCall = true;
                    TimerService.this.mCheckingPhoneStateTimer.start();
                }
            }

            public void onFinish() {
                Log.secD("TimerService", "Finish in mReplayLoopTimer");
                if (TimerService.this.mTimerAlertPlayer != null) {
                    TimerService.this.mTimerAlertPlayer.setNeedReplay(true);
                }
            }
        }.start();
    }

    private void sendAccessibilityEvent(Notification notification) {
        AccessibilityManager am = (AccessibilityManager) getSystemService("accessibility");
        if (am != null && am.isEnabled()) {
            Log.secD("TimerService", "Send Accessibility Event for Timer Alert");
            AccessibilityEvent event = AccessibilityEvent.obtain(64);
            event.setPackageName(getPackageName());
            event.setClassName(TimerAlarmPopupService.class.getName());
            event.setParcelableData(notification);
            CharSequence tickerText = notification.tickerText;
            if (!TextUtils.isEmpty(tickerText)) {
                event.getText().add(tickerText);
            }
            am.sendAccessibilityEvent(event);
        }
    }

    private void updateTimerAlertPlayerState() {
        int minute = (int) ((TimerManager.sElapsedMillis % 3600000) / 60000);
        if (!this.mIsMuted && minute >= 5) {
            Log.secD("TimerService", "Timer alert is muted");
            if (!this.mIsMuted) {
                this.mIsMuted = true;
                if (this.mTimerAlertPlayer != null) {
                    this.mTimerAlertPlayer.stop(true);
                }
                this.mCheckingPhoneStateTimer.cancel();
            }
        }
        boolean isRecordingNow = StateUtils.isRecordingState(this.mContext);
        if (StateUtils.isDndModeAlarmMuted(getBaseContext())) {
            this.mTimerAlertPlayer.stop(false);
            FlashNotificationUtils.stopFlashNotification(this.mContext);
        } else if (!this.mIsMuted && this.mTimerAlertPlayer.isAudioFocusGain() && !StateUtils.isRecordingState(getApplicationContext()) && !StateUtils.isInVoipCall(getApplicationContext())) {
            this.mIsVoipCall = false;
            if (!ClockUtils.sIsTimerAlertStarted) {
                Log.secD("TimerService", "mCheckingPhoneStateTimer/ sIsTimerAlertStarted : " + ClockUtils.sIsTimerAlertStarted);
                stopSelf();
            } else if (!this.mTimerAlertPlayer.isPlaying()) {
                Log.secD("TimerService", "mCheckingPhoneStateTimer : replay again");
                this.mTimerAlertPlayer.stop(true);
                this.mTimerAlertPlayer.play();
            }
        } else if (this.mIsVoipCall && !StateUtils.isInVoipCall(getApplicationContext())) {
            this.mIsVoipCall = false;
            if (this.mTimerAlertPlayer != null) {
                this.mTimerAlertPlayer.play();
            }
        } else if (!this.mIsVoipCall && StateUtils.isInVoipCall(getApplicationContext())) {
            this.mIsVoipCall = true;
            this.mTimerAlertPlayer.stop(false);
        } else if (this.mWasRecording && !isRecordingNow) {
            Log.secD("TimerService", "updateTimerAlertPlayerState mWasRecording " + this.mWasRecording + " isRecordingNow " + isRecordingNow);
            this.mWasRecording = false;
        }
    }

    private void startLEDBackCoverTimer() {
        Log.secD("TimerService", "startLEDBackCoverTimer");
        if (this.mStopLedBackCoverTimer != null) {
            this.mStopLedBackCoverTimer.cancel();
        }
        this.mStopLedBackCoverTimer = new Timer();
        this.mStopLedBackCoverTimer.schedule(new StopLedBackCoverTimerTask(this.mContext, 1), 60000);
    }
}
