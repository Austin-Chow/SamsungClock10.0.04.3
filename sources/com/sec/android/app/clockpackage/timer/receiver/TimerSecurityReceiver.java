package com.sec.android.app.clockpackage.timer.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings.System;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PowerController;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmNotificationHelper;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerManager;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerService;
import java.io.IOException;
import java.util.Calendar;

public class TimerSecurityReceiver extends BroadcastReceiver {
    private static CallListener sCallPendingTimerListener;
    private static CallListener sCallPendingTimerListener2;
    private int mCallState = 0;
    private Context mContext = null;
    private MediaPlayer mMediaPlayer;
    private PowerController mPowerController = new PowerController();
    private long mTimerAlertInputTime = 0;
    private TimerManager mTimerManager = null;
    private String mTimerName = "";

    /* renamed from: com.sec.android.app.clockpackage.timer.receiver.TimerSecurityReceiver$1 */
    class C07301 implements Runnable {
        C07301() {
        }

        public void run() {
            if (TimerSecurityReceiver.this.mPowerController != null) {
                TimerSecurityReceiver.this.mPowerController.releasePartial();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.receiver.TimerSecurityReceiver$2 */
    class C07312 implements Runnable {
        C07312() {
        }

        public void run() {
            TimerSecurityReceiver.this.mTimerManager.cancelTimer();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.receiver.TimerSecurityReceiver$3 */
    class C07323 implements Runnable {
        C07323() {
        }

        public void run() {
            if (TimerSecurityReceiver.this.mPowerController != null) {
                TimerSecurityReceiver.this.mPowerController.releaseDim();
            }
        }
    }

    private class CallListener extends PhoneStateListener {
        private Context mContext;

        /* renamed from: com.sec.android.app.clockpackage.timer.receiver.TimerSecurityReceiver$CallListener$1 */
        class C07331 implements Runnable {
            C07331() {
            }

            public void run() {
                TimerSecurityReceiver.this.launchTimerAlarm();
            }
        }

        private CallListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if (this.mContext != null && StateUtils.isInLockTaskMode(this.mContext)) {
                Log.secD("TimerSecurityReceiver", "onCallStateChanged. isInLockTaskMode : true");
            } else if (TimerData.getRestartMillis() == 0) {
                TimerSecurityReceiver.this.destroyListener();
            } else {
                if (StateUtils.isDualSlot(this.mContext)) {
                    int callState = StateUtils.getMultiSimCallState(this.mContext, 0);
                    int callState2 = StateUtils.getMultiSimCallState(this.mContext, 1);
                    Log.secD("TimerSecurityReceiver", "callState: " + callState + " callState2: " + callState2);
                    if (state == 0 && callState == 0 && callState2 == 0) {
                        state = 0;
                    } else if (state == 1 || callState == 1 || callState2 == 1) {
                        state = 1;
                    } else if (state == 2 || callState == 2 || callState2 == 2) {
                        state = 2;
                    }
                }
                if (TimerSecurityReceiver.this.mCallState != state) {
                    TimerSecurityReceiver.this.mCallState = state;
                    Log.secD("TimerSecurityReceiver", "onCallStateChanged mCallState :" + TimerSecurityReceiver.this.mCallState);
                    if (TimerSecurityReceiver.this.mCallState != 0) {
                        return;
                    }
                    if (ClockUtils.alarmAlertTimeInCall == 0 || ClockUtils.alarmAlertTimeInCall >= ClockUtils.timerAlertTimeInCall) {
                        TimerSecurityReceiver.this.launchTimerAlarm();
                        return;
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(ClockUtils.alarmAlertTimeInCall);
                    Log.secD("TimerSecurityReceiver", "alarmAlertTimeInCall : " + cal.getTime().toString());
                    cal.setTimeInMillis(ClockUtils.timerAlertTimeInCall);
                    Log.secD("TimerSecurityReceiver", "timerAlertTimeInCall : " + cal.getTime().toString());
                    if (ClockUtils.sIsTimerAlertStarted) {
                        TimerSecurityReceiver.this.destroyListener();
                    } else {
                        new Handler().postDelayed(new C07331(), 500);
                    }
                }
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            Log.m41d("TimerSecurityReceiver", "onReceive() / action = " + action);
            this.mContext = context;
            this.mTimerName = intent.getStringExtra("com.sec.android.clockpackage.timer.TIMER_NAME");
            this.mTimerAlertInputTime = intent.getLongExtra("com.sec.android.clockpackage.timer.TIMER_ALERT_INPUT_MILLIS", 0);
            Log.secD("TimerSecurityReceiver", "mTimerName : " + this.mTimerName);
            Log.m41d("TimerSecurityReceiver", "mTimerSettingTime : " + this.mTimerAlertInputTime);
            this.mTimerManager = TimerManager.getInstance();
            if (this.mTimerManager != null) {
                this.mTimerManager.setContext(context);
            }
            Object obj = -1;
            switch (action.hashCode()) {
                case -1559351790:
                    if (action.equals("com.sec.android.app.clockpackage.timer.playsound")) {
                        obj = 6;
                        break;
                    }
                    break;
                case -1502299619:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_NOTIFICATION_SERVICE_STOP")) {
                        obj = 8;
                        break;
                    }
                    break;
                case -1414053883:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_STOP")) {
                        obj = 3;
                        break;
                    }
                    break;
                case -887364212:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_RESET")) {
                        obj = 4;
                        break;
                    }
                    break;
                case -886010721:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_START")) {
                        obj = 1;
                        break;
                    }
                    break;
                case 673338759:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_NOTIFICATION_SERVICE_START")) {
                        obj = 7;
                        break;
                    }
                    break;
                case 1704541645:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_RESTART_NEW")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 1851990829:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_SET_TIME")) {
                        obj = null;
                        break;
                    }
                    break;
                case 1941915404:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_RESTART")) {
                        obj = 5;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    if (checkCommand(action)) {
                        setInputTime(intent.getIntExtra("Hour", 0), intent.getIntExtra("Minute", 0), intent.getIntExtra("Second", 0));
                        this.mTimerManager.setTimerState(3);
                        this.mTimerManager.updateScreen();
                        sendSuccessResult(action, TimerData.getRemainMillis(), TimerData.getInputMillis());
                        return;
                    }
                    return;
                case 1:
                    if (TimerData.getInputMillis() == 0) {
                        sendFailResult(action, "Invalid Input Time", "", "");
                        return;
                    } else if (checkCommand(action)) {
                        this.mTimerManager.setInputTime(TimerData.getInputMillis());
                        this.mTimerManager.updateScreenStart(0, TimerData.getInputMillis());
                        sendSuccessResult(action, TimerData.getRemainMillis(), TimerData.getInputMillis());
                        return;
                    } else {
                        return;
                    }
                case 2:
                    long restartTime = TimerData.getRestartMillis();
                    Log.secD("TimerSecurityReceiver", "restartTime = " + restartTime);
                    if (restartTime == 0) {
                        sendFailResult(action, "Invalid Input Time", "", "");
                        return;
                    } else if (checkCommand(action)) {
                        if (TimerData.getTimerState() == 1) {
                            this.mTimerManager.stopTimer();
                            this.mTimerManager.setTimerState(2);
                        }
                        TimerData.savePreviousInput(TimerData.getInputHour(), TimerData.getInputMin(), TimerData.getInputSec());
                        this.mTimerManager.setInputTime(restartTime);
                        this.mTimerManager.updateScreenStart(1, restartTime, TimerData.getRestartTimerName());
                        this.mTimerManager.updateNotification();
                        sendSuccessResult(action, TimerData.getRemainMillis(), TimerData.getInputMillis());
                        return;
                    } else {
                        return;
                    }
                case 3:
                    if (checkCommand(action)) {
                        this.mTimerManager.stopTimer();
                        this.mTimerManager.setTimerState(2);
                        this.mTimerManager.updateScreen();
                        sendSuccessResult(action, TimerData.getRemainMillis(), TimerData.getInputMillis());
                        return;
                    }
                    return;
                case 4:
                    if (checkCommand(action)) {
                        this.mTimerManager.setInputTime(TimerData.getInputMillis());
                        setTimerReset();
                        sendSuccessResult(action, TimerData.getRemainMillis(), TimerData.getInputMillis());
                        return;
                    }
                    return;
                case 5:
                    if (TimerData.getInputMillis() == 0) {
                        sendFailResult(action, "Invalid Input Time", "", "");
                        return;
                    } else if (checkCommand(action)) {
                        this.mTimerManager.startTimer(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis(), TimerData.getOnGoingTimerName());
                        this.mTimerManager.setTimerState(1);
                        this.mTimerManager.updateScreen();
                        sendSuccessResult(action, TimerData.getRemainMillis(), TimerData.getInputMillis());
                        return;
                    } else {
                        return;
                    }
                case 6:
                    actionTimerPlaySound(context);
                    return;
                case 7:
                    this.mTimerManager.updateNotification();
                    return;
                case 8:
                    this.mTimerManager.stopNotification();
                    return;
                default:
                    return;
            }
        }
    }

    private void actionTimerPlaySound(Context context) {
        TimerData.setOnGoingTimerName(null);
        TimerManager.sOffHookElapsedMillis = 0;
        if (isClearDataState()) {
            Log.secD("TimerSecurityReceiver", "TimerAlarm will not occur, it maybe clear data case");
            return;
        }
        if (this.mPowerController != null) {
            this.mPowerController.acquirePartial(context, "TimerSecurityReceiver");
        }
        if (!StateUtils.isInCall(context) || StateUtils.isVideoCall(context)) {
            actionTimerPlaySoundInNormal(context);
        }
        if (StateUtils.isInCallState(context) && !StateUtils.isVideoCall(context)) {
            actionTimerPlaySoundInCall(context);
        }
        if (this.mTimerManager != null) {
            this.mTimerManager.stopNotification();
        }
        new Handler().postDelayed(new C07301(), 3000);
    }

    private void actionTimerPlaySoundInNormal(Context context) {
        int nAccessControl = System.getInt(context.getContentResolver(), "access_control_enabled", 0);
        Log.secD("TimerSecurityReceiver", "..nAccessControl.." + nAccessControl);
        if ((StateUtils.isScreenOn(context) && (StateUtils.isDriveLinkRunning(context) || StateUtils.isMirrorLinkRunning())) || nAccessControl == 1) {
            Log.secD("TimerSecurityReceiver", "It's not normal state. Timer is not ringing");
            setTimerReset();
            notifyTimerAlert();
            TimerManager.sOffHookElapsedMillis = System.currentTimeMillis();
            TimerData.setRestartMillis(this.mTimerAlertInputTime);
        } else if (StateUtils.isInLockTaskMode(context)) {
            Log.secD("TimerSecurityReceiver", "isInLockTaskMode : true");
            setTimerReset();
            TimerManager.sOffHookElapsedMillis = System.currentTimeMillis();
            if (!ClockUtils.sIsTimerAlertStarted) {
                TimerData.setRestartMillis(this.mTimerAlertInputTime);
                notifyTimerAlert();
            }
        } else {
            new Handler().postDelayed(new C07312(), 150);
            if (this.mTimerManager.isTimerAlarmTopActivity()) {
                Log.secD("TimerSecurityReceiver", "sendBroadcast ACTION_FINISH_ALERT");
                finishTimerAlert(2);
            } else if (ClockUtils.sIsTimerAlertStarted) {
                Log.secD("TimerSecurityReceiver", "killService and Activity in short time, instantly");
                finishTimerAlert(2);
                this.mContext.stopService(new Intent(this.mContext, TimerService.class));
            }
            if (StateUtils.isVideoCall(context)) {
                beepSound();
            }
            if (StateUtils.isAfwForByod(this.mContext)) {
                TimerManager.sOffHookElapsedMillis = System.currentTimeMillis();
            }
            launchTimerAlarm();
        }
    }

    private void actionTimerPlaySoundInCall(Context context) {
        setTimerReset();
        notifyTimerAlert();
        TimerManager.sOffHookElapsedMillis = System.currentTimeMillis();
        TimerData.setRestartMillis(this.mTimerAlertInputTime);
        if (ClockUtils.sIsTimerAlertStarted) {
            Log.secD("TimerSecurityReceiver", "sendBroadcast:ACTION_FINISH_ALERT");
            finishTimerAlert(1);
        }
        if (!StateUtils.isRecordingState(context)) {
            beepSound();
        }
        ClockUtils.timerAlertTimeInCall = System.currentTimeMillis();
        if (StateUtils.isInCall(context) && !StateUtils.isInBikeMode(context)) {
            waitForCallEnd(context);
        }
    }

    private boolean checkCommand(String inputCmd) {
        Log.secD("TimerSecurityReceiver", "checkCommand() / inputCmd" + inputCmd + "/mTimerManager.timerState = " + TimerData.getTimerState());
        boolean ret = false;
        switch (TimerData.getTimerState()) {
            case 0:
            case 3:
                if (!"com.sec.android.app.clockpackage.timer.TIMER_SET_TIME".equals(inputCmd) && !"com.sec.android.app.clockpackage.timer.TIMER_START".equals(inputCmd) && !"com.sec.android.app.clockpackage.timer.TIMER_RESTART_NEW".equals(inputCmd)) {
                    sendFailResult(inputCmd, "Invalid Command", "com.sec.android.app.clockpackage.timer.TIMER_SET_TIME", "com.sec.android.app.clockpackage.timer.TIMER_START");
                    break;
                }
                ret = true;
                break;
                break;
            case 1:
                if (!"com.sec.android.app.clockpackage.timer.TIMER_STOP".equals(inputCmd) && !"com.sec.android.app.clockpackage.timer.TIMER_RESET".equals(inputCmd) && !"com.sec.android.app.clockpackage.timer.TIMER_RESTART_NEW".equals(inputCmd)) {
                    sendFailResult(inputCmd, "Invalid Command", "com.sec.android.app.clockpackage.timer.TIMER_STOP", "com.sec.android.app.clockpackage.timer.TIMER_RESET");
                    break;
                }
                ret = true;
                break;
                break;
            case 2:
                if (!"com.sec.android.app.clockpackage.timer.TIMER_RESTART".equals(inputCmd) && !"com.sec.android.app.clockpackage.timer.TIMER_RESET".equals(inputCmd) && !"com.sec.android.app.clockpackage.timer.TIMER_RESTART_NEW".equals(inputCmd)) {
                    sendFailResult(inputCmd, "Invalid Command", "com.sec.android.app.clockpackage.timer.TIMER_RESTART", "com.sec.android.app.clockpackage.timer.TIMER_RESET");
                    break;
                }
                ret = true;
                break;
                break;
            default:
                Log.secD("TimerSecurityReceiver", "checkCommand() / Default!!");
                break;
        }
        Log.secD("TimerSecurityReceiver", "checkCommand() / return = " + ret);
        return ret;
    }

    private void sendSuccessResult(String inputCmd, long remainMillis, long inputMillis) {
        String RESULT_REMAIN_MILLIS = "Remain Millis";
        String RESULT_INPUT_MILLIS = "Input Millis";
        String RESULT_SUCCESS = "Success";
        Intent mIntent = new Intent("com.sec.android.app.clockpackage.timer.NOTIFY_TIMER_CMD_RESULT");
        mIntent.putExtra("Input command", inputCmd);
        mIntent.putExtra("Result", "Success");
        mIntent.putExtra("Remain Millis", remainMillis);
        mIntent.putExtra("Input Millis", inputMillis);
        mIntent.setPackage("com.samsung.voiceserviceplatform");
        this.mContext.sendBroadcast(mIntent);
        Log.secD("TimerSecurityReceiver", "sendSuccessResult()/inputCmd" + inputCmd + "/remainMillis=" + remainMillis + "/inputMillis=" + inputMillis + "/mIntent =" + mIntent);
    }

    private void sendFailResult(String inputCmd, String reason, String cmd01, String cmd02) {
        String RESULT_FAIL = "Fail";
        String RECOMMENDATION_CMD01 = "CMD01";
        String RECOMMENDATION_CMD02 = "CMD02";
        String REASON = "Reason";
        Intent mIntent = new Intent("com.sec.android.app.clockpackage.timer.NOTIFY_TIMER_CMD_RESULT");
        mIntent.putExtra("Input command", inputCmd);
        mIntent.putExtra("Result", "Fail");
        mIntent.putExtra("Reason", reason);
        if (reason.equals("Invalid Command")) {
            mIntent.putExtra("CMD01", cmd01);
            mIntent.putExtra("CMD02", cmd02);
        }
        mIntent.setPackage("com.samsung.voiceserviceplatform");
        this.mContext.sendBroadcast(mIntent);
        Log.secD("TimerSecurityReceiver", "sendFailResult()/inputCmd" + inputCmd + "/reason=" + reason + "/cmd01=" + cmd01 + "/cmd02=" + cmd02 + "/mIntent =" + mIntent);
    }

    private void waitForCallEnd(Context context) {
        if (sCallPendingTimerListener == null) {
            sCallPendingTimerListener = new CallListener();
            sCallPendingTimerListener.mContext = context;
            getTelephonyManager(context).listen(sCallPendingTimerListener, 32);
        }
        if (StateUtils.isDualSlot(context) && sCallPendingTimerListener2 == null) {
            sCallPendingTimerListener2 = new CallListener();
            sCallPendingTimerListener2.mContext = context;
            getTelephonyManager(context).listen(sCallPendingTimerListener2, 32);
        }
    }

    private void destroyListener() {
        if (sCallPendingTimerListener != null) {
            if (sCallPendingTimerListener.mContext != null) {
                getTelephonyManager(sCallPendingTimerListener.mContext).listen(sCallPendingTimerListener, 0);
                sCallPendingTimerListener.mContext = null;
            }
            sCallPendingTimerListener = null;
        }
        if (sCallPendingTimerListener2 != null && StateUtils.isDualSlot(this.mContext)) {
            if (sCallPendingTimerListener2.mContext != null) {
                getTelephonyManager(sCallPendingTimerListener2.mContext).listen(sCallPendingTimerListener2, 0);
                sCallPendingTimerListener2.mContext = null;
            }
            sCallPendingTimerListener2 = null;
        }
    }

    private TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    private void launchTimerAlarm() {
        Log.secD("TimerSecurityReceiver", "launchTimerAlarm");
        if (this.mContext != null) {
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_TIMER_ALERT_START"));
            Intent intent = new Intent(this.mContext, TimerService.class);
            intent.setAction("com.sec.android.app.clockpackage.timer.SERVICE_START");
            intent.putExtra("com.sec.android.clockpackage.timer.TIMER_NAME", this.mTimerName);
            intent.putExtra("com.sec.android.clockpackage.timer.TIMER_ALERT_INPUT_MILLIS", this.mTimerAlertInputTime);
            try {
                this.mContext.startService(intent);
            } catch (IllegalStateException e) {
                Log.secE("TimerSecurityReceiver", "exception : " + e.toString());
                Log.m42e("TimerSecurityReceiver", "try startForegroundService()");
                this.mContext.startForegroundService(intent);
            }
            destroyListener();
        }
    }

    private void beepSound() {
        Log.secD("TimerSecurityReceiver", "beepSound");
        if (System.getInt(this.mContext.getApplicationContext().getContentResolver(), "alertoncall_mode", 1) == 1 && this.mMediaPlayer == null) {
            if (!StateUtils.isDndModeAlarmMuted(this.mContext)) {
                try {
                    this.mMediaPlayer = new MediaPlayer();
                    AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                    if (audioManager != null) {
                        if (audioManager.getMode() == 1 && audioManager.getRingerMode() == 2) {
                            this.mMediaPlayer.setAudioAttributes(new Builder().setUsage(5).setContentType(4).build());
                        } else {
                            this.mMediaPlayer.setAudioAttributes(new Builder().setUsage(2).setContentType(4).build());
                        }
                    }
                    this.mMediaPlayer.setLooping(false);
                    this.mMediaPlayer.setDataSource(this.mContext, Uri.parse("android.resource://com.sec.android.app.clockpackage/raw/s_alarms_in_call"));
                    this.mMediaPlayer.prepare();
                    this.mMediaPlayer.start();
                } catch (IOException e) {
                    Log.secE("TimerSecurityReceiver", "Exception : " + e.toString());
                    Log.secE("Timer", "Failed load sound file");
                }
            }
            this.mPowerController.releasePartial();
            this.mPowerController.acquireDim(this.mContext, "TimerSecurityReceiver");
            new Handler().postDelayed(new C07323(), 500);
        }
    }

    private void setInputTime(int hour, int minute, int second) {
        Log.secD("TimerSecurityReceiver", "setInputTime");
        if (hour < 0) {
            hour = 0;
        }
        if (hour > 99) {
            hour = 99;
        }
        if (minute < 0) {
            minute = 0;
        }
        if (minute > 59) {
            minute = 59;
        }
        if (second < 0) {
            second = 0;
        }
        if (second > 59) {
            second = 59;
        }
        this.mTimerManager.setInputTime(hour, minute, second);
    }

    private void setTimerReset() {
        this.mTimerManager.cancelTimer();
        this.mTimerManager.setTimerState(3);
        this.mTimerManager.updateScreenReset();
    }

    private void finishTimerAlert(int mode) {
        Intent i = new Intent("com.sec.android.clockpackage.timer.FINISH_ALERT");
        i.putExtra("com.sec.android.clockpackage.timer.FINISH_MODE", mode);
        this.mContext.sendBroadcast(i);
    }

    private boolean isClearDataState() {
        SharedPreferences pref = this.mContext.getSharedPreferences("TIMER", 0);
        int currentState = pref.getInt("timer_current_state", -1);
        long elapsedRealTime = pref.getLong("timer_elapsed_realtime", -1);
        long inputMillis = pref.getLong("timer_inputMillis", -1);
        long remainMillis = pref.getLong("timer_remainMillis", -1);
        if (currentState == 0 && elapsedRealTime == -1 && inputMillis == -1 && remainMillis == -1) {
            return true;
        }
        return false;
    }

    private void notifyTimerAlert() {
        ((NotificationManager) this.mContext.getSystemService("notification")).notify(84637, TimerAlarmNotificationHelper.makeNotify(this.mContext, this.mTimerName));
    }
}
