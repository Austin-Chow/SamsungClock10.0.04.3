package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.app.Notification;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.model.AlarmSharedManager;
import com.sec.android.app.clockpackage.alarm.model.BixbyBriefingAlarmItem;
import com.sec.android.app.clockpackage.alarm.model.CelebVoiceProvider;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.util.AlertUtils;
import com.sec.android.app.clockpackage.commonalert.util.AlertUtils.StopLedBackCoverTimerTask;
import com.sec.android.app.clockpackage.commonalert.util.FlashNotificationUtils;
import com.sec.android.app.clockpackage.commonalert.util.VRHelper.Global;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;

public class AlarmService extends Service {
    public static int sAlarmAlertId = -1;
    public static long sAlarmAlertTime = -1;
    public static int sBixbyBriefDaytime = 0;
    public static int sBixbyBriefWeatherConditionCode = 999;
    private static String sBixbyBriefWeatherCpLink = "";
    private final int DURATION_OF_DECREASE_ALARM = 1000;
    private final int DURATION_OF_INCREASE_ALARM = 12000;
    private final int DURATION_OF_VOLUME_INCREASE_FOR_TTS = 1000;
    private final int INCREASING_SECONDS = 12;
    private final double INTERVAL_DEGREE = 1.875d;
    private final int MIN_INCREASING_VOLUME = 3;
    private final int SLEEP_DURATION = Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    private final float START_VOLUME = 0.2f;
    private final float[] START_VOLUME_LEVEL = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 0.7f, 0.6f, 0.4f, 0.3f, 0.25f, 0.2f, 0.1f, 0.05f, 0.04f, 0.04f, 0.04f, 0.04f};
    private CountDownTimer mAlertTimer;
    private AudioManager mAudioManager;
    private BixbyBriefingAlarmItem mBixbyBriefingItem = new BixbyBriefingAlarmItem();
    private int mBriefingType = 0;
    private Context mContext;
    private int mCount;
    private float mCurVol = 0.2f;
    private float mCurVolForTts = 0.2f;
    private int mDelayTime;
    private long mDelayTimeForForceStopBixbyBriefing = 120000;
    private int mFinishAlarmHhMm = -1;
    private final Handler mHandler = new Handler();
    private final Handler mHandler2 = new Handler();
    private InternalReceiver mInternalReceiver;
    private boolean mIsBixbyCelebVoice = false;
    private boolean mIsBixbyOrCelebVoice = false;
    private boolean mIsFirstAlarm = false;
    private boolean mIsPausePlaying = false;
    private int mIsPlugInEarphone = 0;
    private boolean mIsRecording = false;
    private boolean mIsSupportCelebVoice;
    private Boolean mIsTimeOut = Boolean.valueOf(false);
    private boolean mIsTtsAlarm = false;
    private boolean mIsVideoCall = false;
    private boolean mIsVoipCall = false;
    private boolean mIsVoipCallToIdle = false;
    private AlarmItem mItem = new AlarmItem();
    private String mPhoneStateExtra = TelephonyManager.EXTRA_STATE_IDLE;
    private AlarmPlayer mPlayer;
    private AudioReceiver mReceiver = null;
    private int mRingerModeState;
    private String mScreenId = "107";
    private String mSoundTone;
    private Timer mStopLedBackCoverTimer = null;
    private final Handler mTtsHandler = new TtsHandler(this);
    private int mVibPattern = 50035;
    private String mVoiceString;
    private int mVolume;
    private float mVolumeDecreaseRate;
    private float mVolumeDecreaseRateForTts;
    private CountDownTimer mVolumeDecreaseTimer;
    private CountDownTimer mVolumeDecreaseTimerForTts;
    private float mVolumeIncreaseRate;
    private float mVolumeIncreaseRateForTts;
    private CountDownTimer mVolumeIncreaseTimer;
    private CountDownTimer mVolumeIncreaseTimerForTts;
    private boolean mWasRecording = false;
    private final PhoneStateListener phoneStateListener = new C06241();

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService$1 */
    class C06241 extends PhoneStateListener {
        C06241() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            String backupPhoneStateExtra = AlarmService.this.mPhoneStateExtra;
            switch (state) {
                case 0:
                    AlarmService.this.mPhoneStateExtra = TelephonyManager.EXTRA_STATE_IDLE;
                    Log.secI("AlarmService", "PhoneStateListener - CALL_STATE_IDLE");
                    break;
                case 1:
                    Log.secI("AlarmService", "PhoneStateListener - CALL_STATE_RINGING");
                    AlarmService.this.mPhoneStateExtra = TelephonyManager.EXTRA_STATE_RINGING;
                    break;
                case 2:
                    Log.secI("AlarmService", "PhoneStateListener - CALL_STATE_OFFHOOK");
                    AlarmService.this.mPhoneStateExtra = TelephonyManager.EXTRA_STATE_OFFHOOK;
                    break;
            }
            if (!AlarmService.this.mPhoneStateExtra.equals(backupPhoneStateExtra)) {
                AlarmService.this.changedPhoneState(AlarmService.this.mPhoneStateExtra);
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService$2 */
    class C06252 implements Runnable {
        C06252() {
        }

        public void run() {
            Log.secD("AlarmService", "run mHandler.postDelayed");
            if (!AlarmService.this.mIsPausePlaying && AlarmService.this.mPlayer != null) {
                AlarmService.this.mPlayer.setCallState(AlarmService.this.mPhoneStateExtra);
                if (!AlarmService.this.mPlayer.getPalm()) {
                    Log.secD("AlarmService", "getPalm: false");
                    AlarmService.this.setMode();
                    AlarmService.this.mPlayer.resumeAndSetVolume(AlarmService.this.mCount, AlarmService.this.mCurVol);
                    if (AlarmService.this.mContext != null) {
                        FlashNotificationUtils.startFlashNotification(AlarmService.this.mContext);
                    }
                }
            }
        }
    }

    private class AudioReceiver extends BroadcastReceiver {

        /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService$AudioReceiver$1 */
        class C06321 implements Runnable {
            C06321() {
            }

            public void run() {
                if (!ClockUtils.sIsTimerAlertStarted) {
                    if (AlarmService.this.mPlayer != null) {
                        AlarmService.this.mPlayer.setStreamAlarmVolume();
                        if (!AlarmService.this.mPlayer.getPalm()) {
                            FlashNotificationUtils.startFlashNotification(AlarmService.this.mContext);
                        }
                    }
                    AlarmService.this.playAlarm();
                }
            }
        }

        private AudioReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.m41d("AlarmService", "onReceive() : action = " + (action.contains(".") ? action.substring(action.lastIndexOf(46)) : action));
            int i = -1;
            switch (action.hashCode()) {
                case -2128145023:
                    if (action.equals("android.intent.action.SCREEN_OFF")) {
                        i = 6;
                        break;
                    }
                    break;
                case -2092605434:
                    if (action.equals("com.samsung.android.mirrorlink.ML_STATE")) {
                        i = 11;
                        break;
                    }
                    break;
                case -1682130478:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_PAUSE")) {
                        i = 0;
                        break;
                    }
                    break;
                case -1676458352:
                    if (action.equals("android.intent.action.HEADSET_PLUG")) {
                        i = 2;
                        break;
                    }
                    break;
                case -1670536538:
                    if (action.equals("android.app.action.ENTER_KNOX_DESKTOP_MODE")) {
                        i = 8;
                        break;
                    }
                    break;
                case -1303321789:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT")) {
                        i = 4;
                        break;
                    }
                    break;
                case -369676425:
                    if (action.equals("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION")) {
                        i = 14;
                        break;
                    }
                    break;
                case -103598843:
                    if (action.equals("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_TOUCH")) {
                        i = 12;
                        break;
                    }
                    break;
                case 2034148:
                    if (action.equals("android.app.action.EXIT_KNOX_DESKTOP_MODE")) {
                        i = 9;
                        break;
                    }
                    break;
                case 279267191:
                    if (action.equals("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT")) {
                        i = 5;
                        break;
                    }
                    break;
                case 959232034:
                    if (action.equals("android.intent.action.USER_SWITCHED")) {
                        i = 10;
                        break;
                    }
                    break;
                case 1055975735:
                    if (action.equals("com.samsung.android.motion.PALM_DOWN")) {
                        boolean z = true;
                        break;
                    }
                    break;
                case 1145761348:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT")) {
                        i = 7;
                        break;
                    }
                    break;
                case 1482290872:
                    if (action.equals("com.sec.android.app.clockpackage.intent.action.RECEIVE_BIXBY_ALARM")) {
                        i = 13;
                        break;
                    }
                    break;
                case 2070024785:
                    if (action.equals("android.media.RINGER_MODE_CHANGED")) {
                        i = 3;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    if (AlarmService.this.mPlayer != null) {
                        AlarmService.this.pausePlaying();
                        return;
                    }
                    return;
                case 1:
                    if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.getBehindTimerState() && !AlarmService.this.mPlayer.mIsMute) {
                        AlarmService.this.mPlayer.mIsMute = true;
                        AlarmService.this.mPlayer.setPalm(true);
                        AlarmService.this.mPlayer.stop();
                        FlashNotificationUtils.stopFlashNotification(AlarmService.this.mContext);
                        ClockUtils.insertSaLog(AlarmService.this.mScreenId, "1141");
                        return;
                    }
                    return;
                case 2:
                    if (StateUtils.isMannerModeState(AlarmService.this.mContext)) {
                        int state = intent.getIntExtra("state", 0);
                        AlarmService.this.mIsPlugInEarphone = state;
                        AlarmService.this.mPlayer.setEarphoneVib(state);
                        AlarmService.this.setMannerModeSoundConcept();
                        return;
                    }
                    return;
                case 3:
                    if (StateUtils.isSoundModeOnForJapan(AlarmService.this.mContext)) {
                        if (AlarmService.this.mAudioManager == null) {
                            AlarmService.this.mAudioManager = (AudioManager) AlarmService.this.getSystemService("audio");
                        }
                        int currentState = AlarmService.this.mAudioManager.getRingerMode();
                        int previousState = AlarmService.this.mRingerModeState;
                        AlarmService.this.mRingerModeState = currentState;
                        if (AlarmService.this.mRingerModeState != previousState) {
                            AlarmService.this.setMannerModeSoundConcept();
                            return;
                        }
                        return;
                    }
                    return;
                case 4:
                    if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.getBehindTimerState()) {
                        AlarmService.this.mPlayer.setBehindTimerState(true);
                        AlarmService.this.pausePlaying();
                        AlarmService.this.mPlayer.onPause();
                        return;
                    }
                    return;
                case 5:
                    new Handler().postDelayed(new C06321(), 100);
                    return;
                case 6:
                    if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.getBehindTimerState()) {
                        if (!(AlarmService.this.mPlayer.getPlayMode() == 4352 && AlarmService.this.mIsTtsAlarm)) {
                            AlarmService.this.mPlayer.resetVibrate();
                            AlarmService.this.setMode();
                        }
                        if (AlarmService.this.mPhoneStateExtra.equals(TelephonyManager.EXTRA_STATE_IDLE) && !StateUtils.isInVoipCall(AlarmService.this.mContext)) {
                            AlarmService.this.mPlayer.resumeAndSetVolume(AlarmService.this.mCount, AlarmService.this.mCurVol);
                            return;
                        }
                        return;
                    }
                    return;
                case 7:
                    AlarmService.this.playAlarm();
                    return;
                case 8:
                case 9:
                case 10:
                case 11:
                    AlarmService.this.stopAlarm();
                    return;
                case 12:
                    AlarmService.this.setModeAndResume();
                    return;
                case 13:
                    if (!AlarmService.this.mBixbyBriefingItem.mIsSuccess) {
                        AlarmService.this.mBixbyBriefingItem.readFromIntent(intent);
                        Log.secD("AlarmService", "ACTION_RECEIVE_BIXBY_ALARM mBixbyBriefingItem = " + AlarmService.this.mBixbyBriefingItem.toString());
                        if (AlarmService.this.isSameIdAndTimeWithBixbyBriefingItem()) {
                            AlarmService.this.setBixbyBriefingInformation(true);
                            AlarmService.this.setBriefingTypeAndDelayForTtsAlarm(true);
                            AlarmUtil.sendShowWeatherIconIntent(AlarmService.this.mContext);
                            return;
                        }
                        return;
                    }
                    return;
                case 14:
                    if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.getBehindTimerState()) {
                        FlashNotificationUtils.stopFlashNotification(AlarmService.this.mContext);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private class InternalReceiver extends BroadcastReceiver {

        /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService$InternalReceiver$1 */
        class C06331 implements Runnable {
            C06331() {
            }

            public void run() {
                AlarmService.this.stopSelf();
            }
        }

        private InternalReceiver() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
            r8 = this;
            r5 = 1;
            r3 = 0;
            r2 = 0;
            r4 = -1;
            r0 = r10.getAction();
            r1 = "AlarmService";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "InternalReceiver receive action : ";
            r6 = r6.append(r7);
            r6 = r6.append(r0);
            r6 = r6.toString();
            com.sec.android.app.clockpackage.common.util.Log.secD(r1, r6);
            r1 = r0.hashCode();
            switch(r1) {
                case -1007258726: goto L_0x0036;
                case 2035452574: goto L_0x002c;
                default: goto L_0x0027;
            };
        L_0x0027:
            r1 = r4;
        L_0x0028:
            switch(r1) {
                case 0: goto L_0x0040;
                case 1: goto L_0x006c;
                default: goto L_0x002b;
            };
        L_0x002b:
            return;
        L_0x002c:
            r1 = "com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_TIMER_ALERT_START";
            r1 = r0.equals(r1);
            if (r1 == 0) goto L_0x0027;
        L_0x0034:
            r1 = r3;
            goto L_0x0028;
        L_0x0036:
            r1 = "com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_STOP";
            r1 = r0.equals(r1);
            if (r1 == 0) goto L_0x0027;
        L_0x003e:
            r1 = r5;
            goto L_0x0028;
        L_0x0040:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mPlayer;
            if (r1 == 0) goto L_0x002b;
        L_0x0048:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mPlayer;
            r1 = r1.getBehindTimerState();
            if (r1 != 0) goto L_0x002b;
        L_0x0054:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mPlayer;
            r1.setBehindTimerState(r5);
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1.pausePlaying();
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mPlayer;
            r1.onPause();
            goto L_0x002b;
        L_0x006c:
            r5 = "AlarmService";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r6 = "ALERT_STOP Vol=";
            r6 = r1.append(r6);
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mItem;
            if (r1 == 0) goto L_0x0139;
        L_0x0081:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mItem;
            r1 = r1.mAlarmVolume;
            r1 = java.lang.Integer.valueOf(r1);
        L_0x008d:
            r6 = r6.append(r1);
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mPlayer;
            if (r1 == 0) goto L_0x013c;
        L_0x0099:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r7 = " Real=";
            r1 = r1.append(r7);
            r7 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r7 = r7.mPlayer;
            r7 = r7.getStreamAlarmVolume();
            r1 = r1.append(r7);
            r7 = " Mode=";
            r1 = r1.append(r7);
            r7 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r7 = r7.mPlayer;
            r7 = r7.getPlayMode();
            r1 = r1.append(r7);
            r1 = r1.toString();
        L_0x00ca:
            r1 = r6.append(r1);
            r1 = r1.toString();
            com.sec.android.app.clockpackage.common.util.Logger.m47f(r5, r1);
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mTtsHandler;
            if (r1 == 0) goto L_0x00e6;
        L_0x00dd:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mTtsHandler;
            r1.removeCallbacksAndMessages(r2);
        L_0x00e6:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1.removeCountDownTimer();
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1.removePlayer();
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.sAlarmAlertId;
            if (r1 == r4) goto L_0x011d;
        L_0x00f4:
            com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.sAlarmAlertId = r4;
            r4 = -1;
            com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.sAlarmAlertTime = r4;
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mItem;
            r1 = r1.isPossibleBixbyBriefingAlarm();
            if (r1 == 0) goto L_0x011d;
        L_0x0106:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.isSameIdAndTimeWithBixbyBriefingItem();
            if (r1 == 0) goto L_0x011d;
        L_0x010e:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r1 = r1.mContext;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r2 = r2.mBixbyBriefingItem;
            com.sec.android.app.clockpackage.alarm.viewmodel.AlarmBixbyBriefingManager.sendBixbyAlarmDeletionIntent(r1, r2);
        L_0x011d:
            r1 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.this;
            r2 = r2.mScreenId;
            r1.insertSaLogAlarmDuration(r2, r3);
            r1 = new android.os.Handler;
            r1.<init>();
            r2 = new com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService$InternalReceiver$1;
            r2.<init>();
            r4 = 50;
            r1.postDelayed(r2, r4);
            goto L_0x002b;
        L_0x0139:
            r1 = r2;
            goto L_0x008d;
        L_0x013c:
            r1 = r2;
            goto L_0x00ca;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.InternalReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    private static class TtsHandler extends Handler {
        private final WeakReference<AlarmService> mParent;

        TtsHandler(AlarmService parent) {
            this.mParent = new WeakReference(parent);
        }

        public void handleMessage(Message msg) {
            AlarmService refer = (AlarmService) this.mParent.get();
            if (refer != null) {
                refer.handleMessage(msg);
            }
        }
    }

    public static void startCpLink(Context context) {
        if (sBixbyBriefWeatherCpLink != null && sBixbyBriefWeatherCpLink.length() > 0) {
            Log.secD("AlarmService", "startCpLink sBixbyBriefWeatherCpLink = " + sBixbyBriefWeatherCpLink);
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sBixbyBriefWeatherCpLink));
                intent.setFlags(268435456);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.m42e("AlarmService", "startCpLink error :" + e.toString());
            }
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mContext = ClockUtils.getBootAwareContext(this);
        this.mItem = new AlarmItem();
        this.mItem.readFromIntent(intent);
        checkInvalidAlarmAndChangeCorrectAlarm(this.mContext);
        getAlarmInformation();
        if ((this.mItem.isNewBixbyOn() || this.mItem.isNewCelebOn()) && !this.mItem.isFirstAlarm()) {
            sBixbyBriefWeatherConditionCode = AlarmSharedManager.getWeatherConditionCode(this.mContext, this.mItem.mId);
            sBixbyBriefDaytime = AlarmSharedManager.getWeatherDaytime(this.mContext, this.mItem.mId);
        }
        Notification notification = AlarmNotificationHelper.makeNotify(this.mContext, this.mItem);
        startForeground(268439552, notification);
        if (intent == null || (!intent.getBooleanExtra("skip_to_check_old_alarm", false) && isOldAlarm())) {
            Log.m41d("AlarmService", "onStartCommand stopSelf() intent is " + (intent == null ? "null" : "not null"));
            stopSelf();
            return 2;
        }
        AlertUtils.sendAlarmStartedIntent(this.mContext);
        sAlarmAlertId = this.mItem.mId;
        sAlarmAlertTime = this.mItem.mAlarmAlertTime;
        this.mIsSupportCelebVoice = Feature.isSupportCelebrityAlarm();
        setRegisterInternalReceiver();
        setBixbyBriefingInformation(false);
        if (this.mItem.isMasterSoundOn() && (this.mItem.isNewBixbyOn() || this.mItem.isNewCelebOn())) {
            if (this.mItem.isFirstAlarm()) {
                this.mSoundTone = AlarmWeatherUtil.getWeatherBackgroundPath(this.mBixbyBriefingItem.mWeatherConditionCode);
            } else {
                this.mSoundTone = AlarmWeatherUtil.getWeatherBackgroundPath(sBixbyBriefWeatherConditionCode);
            }
            Log.secD("AlarmService", "onStartCommand mSoundTone = " + this.mSoundTone);
        }
        this.mVolumeIncreaseRate = getVolumeIncreaseRate();
        this.mVolumeDecreaseRate = getVolumeDecreaseRate();
        this.mIsVoipCall = StateUtils.isInVoipCall(this.mContext);
        setAlarmSound();
        if (!Global.getBoolean(this.mContext.getContentResolver(), "hmt_dock", false) && StateUtils.needToShowAsFullScreen(this)) {
            Log.m41d("AlarmService", "onStartCommand callAlarmAlertActivity 1");
            callAlarmAlertActivity(intent);
        } else if (ClockUtils.getTopActivity(getApplicationContext()).equals("com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity") && !StateUtils.isContextInDexMode(this) && ClockUtils.getMyProcessId() == ClockUtils.getTopActivityProcessId(getApplicationContext())) {
            callAlarmAlertActivity(intent);
        } else {
            Log.secD("AlarmService", "onStartCommand call AlarmAlertPopupService");
            Intent alert = new Intent();
            alert.setComponent(new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService"));
            alert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA"));
            try {
                char c;
                this.mContext.startService(alert);
                if (!this.mItem.isMasterSoundOn() || (!this.mItem.isNewBixbyOn() && !this.mItem.isNewCelebOn())) {
                    this.mScreenId = "108";
                } else if (this.mBixbyBriefingItem.mIsSuccess) {
                    this.mScreenId = "118";
                } else {
                    this.mScreenId = "117";
                }
                String str = "AlarmService";
                StringBuilder append = new StringBuilder().append("call HUN M_Sound = ").append(this.mItem.isMasterSoundOn() ? '1' : '0').append(" vib ");
                if (this.mItem.mAlarmSoundType == 2) {
                    c = '1';
                } else {
                    c = '0';
                }
                Logger.m47f(str, append.append(c).append(" Brief:").append(Integer.toBinaryString(this.mItem.mDailyBriefing)).toString());
            } catch (SecurityException e) {
                Logger.m47f("AlarmService", "call HUN SecurityException");
            }
            if (StateUtils.isUniversalSwitchEnabled(this.mContext)) {
                AccessibilityManager am = (AccessibilityManager) this.mContext.getSystemService("accessibility");
                if (am.isEnabled()) {
                    Log.secD("AlarmService", "Send Accessibility Event for Alarm Alert");
                    AccessibilityEvent event = AccessibilityEvent.obtain(64);
                    event.setPackageName(this.mContext.getPackageName());
                    event.setClassName(AlarmAlertPopupService.class.getName());
                    event.setParcelableData(notification);
                    CharSequence tickerText = notification.tickerText;
                    if (!TextUtils.isEmpty(tickerText)) {
                        event.getText().add(tickerText);
                    }
                    am.sendAccessibilityEvent(event);
                }
            }
        }
        setTelephonyListener();
        registerReceiver();
        if (!(this.mPlayer == null || this.mPlayer.getPalm())) {
            Log.secD("AlarmService", "getPalm:false, mPlayer -> " + this.mPlayer);
            FlashNotificationUtils.startFlashNotification(this.mContext);
            this.mPlayer.onResume();
            this.mPlayer.resume(this.mCount);
            if (this.mVolumeIncreaseRate > 0.0f) {
                this.mCurVol = this.START_VOLUME_LEVEL[this.mItem.mAlarmVolume];
            } else {
                this.mCurVol = 1.0f;
            }
            this.mPlayer.setVolume(this.mCurVol);
        }
        if (ClockUtils.alarmAlertTimeInCall < ClockUtils.timerAlertTimeInCall) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(ClockUtils.alarmAlertTimeInCall);
            Log.secD("AlarmService", "alarmAlertTimeInCall : " + cal.getTime().toString());
            cal.setTimeInMillis(ClockUtils.timerAlertTimeInCall);
            Log.secD("AlarmService", "timerAlertTimeInCall : " + cal.getTime().toString());
            if (this.mPlayer != null) {
                this.mPlayer.setBehindTimerState(true);
                pausePlaying();
                this.mPlayer.onPause();
            }
        }
        if (this.mItem.isFirstAlarm()) {
            ClockUtils.insertSaLog(this.mScreenId, "1248", Integer.toString(this.mItem.mAlarmTime / 100));
        }
        ScoverManager coverManager = new ScoverManager(this);
        if (coverManager != null) {
            ScoverState coverState = coverManager.getCoverState();
            if (coverState != null && coverState.getType() == 14) {
                Log.secD("AlarmService", "when TYPE_LED_BACK_COVER , after 1 min send broadcast to LED side");
                startLEDBackCoverTimer();
            }
        }
        return 1;
    }

    private void callAlarmAlertActivity(Intent intent) {
        char c;
        char c2 = '1';
        String str = "AlarmService";
        StringBuilder append = new StringBuilder().append("call FULL M_Sound = ");
        if (this.mItem.isMasterSoundOn()) {
            c = '1';
        } else {
            c = '0';
        }
        StringBuilder append2 = append.append(c).append(" vib ");
        if (this.mItem.mAlarmSoundType != 2) {
            c2 = '0';
        }
        Logger.m47f(str, append2.append(c2).append(" Brief:").append(Integer.toBinaryString(this.mItem.mDailyBriefing)).toString());
        Intent alert = new Intent();
        alert.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity");
        alert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA"));
        alert.setFlags(268697600);
        this.mContext.startActivity(alert);
        if (!this.mItem.isMasterSoundOn() || (!this.mItem.isNewBixbyOn() && !this.mItem.isNewCelebOn())) {
            this.mScreenId = "107";
        } else if (this.mBixbyBriefingItem.mIsSuccess) {
            this.mScreenId = "116";
        } else {
            this.mScreenId = "117";
        }
    }

    public void onDestroy() {
        Log.secD("AlarmService", "onDestroy");
        resetTelephonyListener();
        unregisterReceiver();
        stopForeground(true);
        if (this.mTtsHandler != null) {
            this.mTtsHandler.removeCallbacksAndMessages(null);
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
        if (this.mHandler2 != null) {
            this.mHandler2.removeCallbacksAndMessages(null);
        }
        if (this.mStopLedBackCoverTimer != null) {
            this.mStopLedBackCoverTimer.cancel();
        }
        Log.secD("AlarmService", "<-onDestroy");
        super.onDestroy();
    }

    private void getAlarmInformation() {
        if (this.mItem != null) {
            this.mSoundTone = this.mItem.mSoundUri;
            this.mVolume = this.mItem.mAlarmVolume;
            this.mVibPattern = this.mItem.mVibrationPattern;
            if (this.mItem.isFirstAlarm()) {
                this.mIsFirstAlarm = true;
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(this.mItem.mAlarmAlertTime);
                c.add(12, this.mItem.getSnoozeDuration() - 1);
                this.mFinishAlarmHhMm = (c.get(11) * 100) + c.get(12);
            } else {
                this.mIsFirstAlarm = false;
            }
            Log.secD("AlarmService", "getAlarmInformation mIsFirstAlarm = " + this.mIsFirstAlarm);
        }
    }

    private void setTelephonyListener() {
        ((TelephonyManager) getSystemService("phone")).listen(this.phoneStateListener, 32);
    }

    private void resetTelephonyListener() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService("phone");
        if (TelephonyMgr != null) {
            TelephonyMgr.listen(this.phoneStateListener, 0);
        }
    }

    private void changedPhoneState(String phoneStateExtra) {
        if (phoneStateExtra.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) || phoneStateExtra.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (this.mPlayer != null) {
                Log.m41d("AlarmService", "OFFHOOK || RINGING");
                this.mPlayer.setCallState(this.mPhoneStateExtra);
                this.mPlayer.pause();
                FlashNotificationUtils.stopFlashNotification(this.mContext);
            }
        } else if (phoneStateExtra.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            long delayTime = (this.mWasRecording || (this.mPlayer != null && this.mPlayer.getBixbyBriefingState() == 1)) ? 3000 : 100;
            Log.m41d("AlarmService", "EXTRA_STATE_IDLE mHandler.postDelayed mIsPausePlaying = " + this.mIsPausePlaying + "mWasRecording = " + this.mWasRecording + " " + delayTime);
            this.mHandler.postDelayed(new C06252(), delayTime);
        }
    }

    private void setAlarmSound() {
        long j;
        Log.secD("AlarmService", "setAlarmSound()");
        long MAX_PLAY_COUNT_5M = (60 * ((long) (AlarmItemUtil.ALARM_SNOOZE_DURATION_TABLE[1] - 1))) + 58;
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mPlayer = new AlarmPlayer(this);
        setBriefingTypeAndDelayForTtsAlarm(false);
        this.mPlayer.setNewCelebState(this.mItem.isNewCelebOn());
        this.mPlayer.setCallState(this.mPhoneStateExtra);
        this.mPlayer.setAudioVolume(this.mVolume);
        if (this.mContext == null) {
            this.mContext = ClockUtils.getBootAwareContext(this);
        }
        this.mIsRecording = StateUtils.isRecordingState(this.mContext);
        if (this.mIsRecording) {
            if (StateUtils.isRecordingCamcorder(this.mContext)) {
                Log.m41d("AlarmService", "mIsRecording = true isRecordingCamcorder");
            } else {
                String packageName = StateUtils.getCurrentAudioFocusPackageName(this.mContext);
                if (packageName != null) {
                    Log.m41d("AlarmService", "mIsRecording = true AudioFocusPackageName  = " + packageName.substring(packageName.lastIndexOf(46)));
                }
            }
        }
        this.mIsVideoCall = StateUtils.isVideoCall(this.mContext);
        setMode();
        initIncreaseTimer();
        if (this.mIsFirstAlarm) {
            j = 1000 * MAX_PLAY_COUNT_5M;
        } else {
            j = 59000;
        }
        this.mAlertTimer = new CountDownTimer(j, 250) {
            boolean bPreviousMuteState = false;
            boolean bPreviousPauseState = false;
            /* renamed from: c */
            final Calendar f14c = Calendar.getInstance();

            /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService$3$1 */
            class C06261 implements Runnable {
                C06261() {
                }

                public void run() {
                    Log.secD("AlarmService", "run mHandler2.postDelayed");
                    AlarmService.this.setModeAndResume();
                }
            }

            public void onTick(long millisUntilFinished) {
                AlarmService.this.mCount = AlarmService.this.mCount + 1;
                this.f14c.setTimeInMillis(System.currentTimeMillis());
                int sec = this.f14c.get(13);
                if (AlarmService.this.mIsFirstAlarm) {
                    if (AlarmService.this.mFinishAlarmHhMm == (this.f14c.get(11) * 100) + this.f14c.get(12)) {
                        if (sec == 57) {
                            AlarmService.this.decreaseAlarmVolume();
                        } else if (sec == 58 || sec == 59) {
                            AlarmService.this.stopAlarm();
                        }
                    } else if (sec == 58 || sec == 59) {
                        if (!AlarmService.this.mIsTimeOut.booleanValue()) {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime < AlarmProvider.sNextAlarmAlertTime && AlarmProvider.sNextAlarmAlertTime - currentTime <= 2000) {
                                AlarmService.this.stopAlarm();
                            }
                        }
                    } else if (AlarmService.this.mPlayer != null && AlarmService.this.mPlayer.getBixbyBriefing()) {
                        if (this.bPreviousMuteState != AlarmService.this.mPlayer.mIsMute) {
                            this.bPreviousMuteState = AlarmService.this.mPlayer.mIsMute;
                            Log.secD("AlarmService", "mPlayer.mIsMute = " + AlarmService.this.mPlayer.mIsMute);
                            AlarmService.this.setMessageForForceStopBixbyBriefing(AlarmService.this.mPlayer.mIsMute);
                        }
                        if (this.bPreviousPauseState != AlarmService.this.mPlayer.isPause()) {
                            this.bPreviousPauseState = AlarmService.this.mPlayer.isPause();
                            Log.secD("AlarmService", "mPlayer.isPause = " + AlarmService.this.mPlayer.isPause());
                            AlarmService.this.setMessageForForceStopBixbyBriefing(AlarmService.this.mPlayer.isPause());
                        }
                    }
                } else if (sec == 59) {
                    AlarmService.this.stopAlarm();
                } else if (sec == 58) {
                    AlarmService.this.decreaseAlarmVolume();
                }
                if (AlarmService.this.mPlayer != null) {
                    if (!AlarmService.this.mPlayer.isPause()) {
                        if (AlarmService.this.mPlayer.getEndAlertOnVoice()) {
                            Log.secD("AlarmService", "getEndAlertOnVoice");
                            AlarmService.this.mPlayer.setEndAlertOnVoice(false);
                            AlarmService.this.setMode();
                            AlarmService.this.mPlayer.resume(AlarmService.this.mCount);
                            AlarmService.this.mPlayer.setPlayResource(AlarmService.this.mSoundTone, "", AlarmService.this.mVibPattern);
                            AlarmService.this.mCurVolForTts = 0.1f;
                            AlarmService.this.mPlayer.setVolume(AlarmService.this.mCurVolForTts);
                            AlarmService.this.initIncreaseTimerForTts();
                            AlarmService.this.mTtsHandler.sendEmptyMessageDelayed(10060, 2000);
                        }
                        if (AlarmService.this.mIsBixbyOrCelebVoice && AlarmService.this.mPlayer.getBixbyBriefingState() == 2) {
                            Log.secD("AlarmService", "BIXBY_BRIEFING_ENDED");
                            AlarmService.this.mTtsHandler.removeMessages(10160);
                            AlarmService.this.setMode();
                            AlarmService.this.mPlayer.resume(AlarmService.this.mCount);
                            AlarmService.this.mPlayer.setPlayResource(AlarmService.this.mSoundTone, "", AlarmService.this.mVibPattern);
                            AlarmService.this.mCurVolForTts = 0.1f;
                            AlarmService.this.mPlayer.setVolume(AlarmService.this.mCurVolForTts);
                            AlarmService.this.initIncreaseTimerForTts();
                            if (AlarmService.this.mIsBixbyCelebVoice) {
                                AlarmService.this.mPlayer.setBixbyBriefingState(0);
                            } else {
                                AlarmService.this.mIsBixbyOrCelebVoice = false;
                            }
                        }
                    }
                    boolean nowVoipCallState = StateUtils.isInVoipCall(AlarmService.this.mContext);
                    if (AlarmService.this.mIsVoipCall && !nowVoipCallState) {
                        AlarmService.this.mIsVoipCall = false;
                        AlarmService.this.mIsVoipCallToIdle = true;
                        long delayTime = AlarmService.this.mWasRecording ? 3000 : 100;
                        Log.m41d("AlarmService", "onTick mIsVoipCall = false" + delayTime);
                        AlarmService.this.mHandler2.postDelayed(new C06261(), delayTime);
                    } else if (((long) AlarmService.this.mCount) % 4 == 0 && !AlarmService.this.mIsVoipCall && nowVoipCallState) {
                        AlarmService.this.mIsVoipCall = true;
                        if (AlarmService.this.mPlayer != null) {
                            AlarmService.this.mPlayer.pause();
                        }
                    } else if (!AlarmService.this.mPlayer.isPause() && nowVoipCallState) {
                        AlarmService.this.mPlayer.pause();
                    }
                    if (AlarmService.this.mIsVideoCall && !StateUtils.isVideoCall(AlarmService.this.mContext)) {
                        AlarmService.this.mIsVideoCall = false;
                        Log.m41d("AlarmService", "onTick mIsVideoCall = false");
                        AlarmService.this.mPlayer.setStreamAlarmVolume();
                        AlarmService.this.setModeAndResume();
                    }
                    if ((AlarmService.this.mCount & 1) == 1) {
                        boolean nowRecordingState = StateUtils.isRecordingState(AlarmService.this.mContext);
                        if (AlarmService.this.mIsRecording && !nowRecordingState) {
                            AlarmService.this.mIsRecording = false;
                            FlashNotificationUtils.stopFlashNotification(AlarmService.this.mContext);
                            Log.m41d("AlarmService", "onTick mIsRecording = false - don't resume");
                        } else if (!AlarmService.this.mIsRecording && nowRecordingState) {
                            AlarmService.this.mIsRecording = true;
                            if (StateUtils.isVoiceNoteRecording(AlarmService.this.mContext)) {
                                AlarmService.this.mWasRecording = true;
                            }
                            Log.m41d("AlarmService", "onTick mIsRecording = true");
                            if (AlarmService.this.mPlayer != null) {
                                AlarmService.this.mPlayer.pause();
                                if (!(!AlarmService.this.mPlayer.mIsMute || AlarmService.this.mPlayer.getPalm() || AlarmService.this.mPlayer.getBehindTimerState())) {
                                    AlarmService.this.mPlayer.mIsMute = false;
                                }
                            }
                        }
                        AlarmService.this.mPlayer.play();
                    }
                }
            }

            public void onFinish() {
                Log.secD("AlarmService", "mAlertTimer onFinish()");
                AlarmService.this.stopAlarm();
            }
        };
        this.mPlayer.setAudioVolume(this.mVolume);
        if (this.mVolumeIncreaseRate > 0.0f) {
            this.mPlayer.setIncreasedAlarm(true);
        }
        if (this.mAlertTimer != null) {
            this.mAlertTimer.start();
        }
        if ((!this.mItem.isMasterSoundOn() || (!this.mItem.isNewBixbyOn() && !this.mItem.isNewCelebOn())) && this.mVolumeIncreaseTimer != null) {
            Log.secD("AlarmService", "mVolumeIncreaseTimer.start()");
            this.mVolumeIncreaseTimer.start();
        }
    }

    private void setMessageForForceStopBixbyBriefing(boolean bEnable) {
        if (bEnable) {
            this.mTtsHandler.removeMessages(10160);
            Log.secD("AlarmService", "removeMessages(FORCE_STOP_BIXBYBRIEFING)");
            return;
        }
        this.mTtsHandler.sendEmptyMessageDelayed(10160, this.mDelayTimeForForceStopBixbyBriefing);
        Log.secD("AlarmService", "sendEmptyMessageDelayed FORCE_STOP_BIXBYBRIEFING mDelayTimeForForceStopBixbyBriefing = " + this.mDelayTimeForForceStopBixbyBriefing);
    }

    private void decreaseAlarmVolume() {
        initDecreaseTimer();
        if (this.mVolumeDecreaseTimer != null) {
            this.mVolumeDecreaseTimer.start();
        }
    }

    private void setModeAndResume() {
        setMode();
        if (this.mPlayer != null && !this.mPlayer.getPalm() && !this.mIsPausePlaying) {
            this.mPlayer.resume(this.mCount);
        }
    }

    private void removePlayer() {
        Log.secD("AlarmService", "removePlayer()");
        if (this.mPlayer != null) {
            this.mPlayer.release();
            this.mPlayer.setPalm(false);
            this.mPlayer.setIsFinishing();
            this.mPlayer = null;
            FlashNotificationUtils.stopFlashNotification(this.mContext);
        }
        if (this.mAudioManager != null) {
            this.mAudioManager = null;
        }
    }

    private void setMode() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) getSystemService("audio");
        }
        boolean bRecordingState = StateUtils.isRecordingState(this.mContext);
        boolean bMannerModeState = StateUtils.isMannerModeState(this.mContext);
        Log.m41d("AlarmService", "setMode bRecordingState = " + bRecordingState);
        if (bRecordingState) {
            if (StateUtils.isVoiceNoteRecording(this.mContext)) {
                this.mWasRecording = true;
            }
            if (this.mItem != null && (this.mItem.mAlarmSoundType != 0 || this.mItem.mAlarmVolume != 0)) {
                Log.secD("AlarmService", "mIsVoipCallToIdle = " + this.mIsVoipCallToIdle + ", mIsVoipCall = " + this.mIsVoipCall);
                if (this.mIsVoipCallToIdle) {
                    if (this.mItem != null) {
                        setPlayModeBySoundSwitchAndAlarmType(bRecordingState);
                    }
                    this.mIsVoipCallToIdle = false;
                } else if (this.mPlayer != null) {
                    if (this.mIsVoipCall || !(StateUtils.needToShowAsFullScreen(this.mContext) || "com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity".equals(ClockUtils.getTopActivity(getApplication())))) {
                        Log.m41d("AlarmService", "setMode PLAY_NULL");
                        this.mPlayer.setPlayMode(6);
                    } else {
                        Log.m41d("AlarmService", "setMode PLAY_VIB");
                        this.mPlayer.setPlayMode(1);
                    }
                }
            } else if (!(this.mItem == null || this.mItem.mAlarmVolume != 0 || this.mPlayer == null)) {
                Log.m41d("AlarmService", "setMode PLAY_NULL 2");
                this.mPlayer.setPlayMode(6);
            }
        } else if (bMannerModeState && this.mItem.mAlarmSoundType == 0 && this.mItem.isSoundOnState()) {
            this.mPlayer.setPlayMode(2);
        } else if (this.mIsVoipCall || this.mIsVideoCall) {
            Log.m41d("AlarmService", "setMode mIsVoipCall = " + this.mIsVoipCall + " mIsVideoCall = " + this.mIsVideoCall);
            if (this.mPlayer != null) {
                this.mPlayer.setPlayMode(6);
            }
        } else if (this.mIsBixbyOrCelebVoice && this.mPlayer.getBixbyBriefingState() == 1) {
            if (this.mPlayer != null) {
                this.mPlayer.setPlayMode(4);
            }
        } else if (!AlarmProvider.isEcbm(this.mContext)) {
            Log.secD("AlarmService", "!AlarmUtil.isEcbm()");
            if (this.mItem != null) {
                setPlayModeBySoundSwitchAndAlarmType(bRecordingState);
            }
        }
        if (this.mPlayer != null) {
            this.mPlayer.setPlayResource(this.mSoundTone, this.mVoiceString, this.mVibPattern);
        }
    }

    private void setPlayModeBySoundSwitchAndAlarmType(boolean bRecordingState) {
        Log.secD("AlarmService", "setPlayModeBySoundSwitchAndAlarmType bRecordingState = " + bRecordingState);
        if (this.mPlayer == null) {
            Log.secD("AlarmService", "setPlayModeBySoundSwitchAndAlarmType mPlayer == null");
        } else if (this.mItem.isSoundOnState()) {
            if (!bRecordingState) {
                this.mPlayer.setPlayMode(this.mItem.mAlarmSoundType);
            } else if (this.mPlayer == null) {
            } else {
                if (this.mIsVoipCall || !(StateUtils.needToShowAsFullScreen(this.mContext) || "com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity".equals(ClockUtils.getTopActivity(getApplication())))) {
                    Log.m41d("AlarmService", "setPlayModeBySoundSwitchAndAlarmType bRecordingState PLAY_NULL");
                    this.mPlayer.setPlayMode(6);
                    return;
                }
                Log.m41d("AlarmService", "setPlayModeBySoundSwitchAndAlarmType bRecordingState PLAY_VIB");
                this.mPlayer.setPlayMode(1);
            }
        } else if (this.mItem.mAlarmSoundType == 2 || this.mItem.mAlarmSoundType == 1) {
            Log.m41d("AlarmService", "setPlayModeBySoundSwitchAndAlarmType PLAY_VIB");
            this.mPlayer.setPlayMode(1);
        } else {
            Log.m41d("AlarmService", "setPlayModeBySoundSwitchAndAlarmType PLAY_NULL");
            this.mPlayer.setPlayMode(6);
        }
    }

    private void setMannerModeSoundConcept() {
        if (this.mPlayer != null) {
            this.mPlayer.stop();
            if (this.mIsPlugInEarphone == 1) {
                this.mPlayer.setStreamAlarmVolume();
            }
            if (StateUtils.isRecordingState(this.mContext)) {
                Log.secD("AlarmService", "recording state, setPlayMode(6) PLAY_MODE_NULL");
                this.mPlayer.setPlayMode(6);
                return;
            }
            changeModeAndResumePlayer();
        }
    }

    private void pausePlaying() {
        Log.secD("AlarmService", "pausePlaying");
        if (this.mContext != null) {
            this.mPlayer.pause();
            this.mIsPausePlaying = true;
        }
    }

    private float getVolumeIncreaseRate() {
        Log.secD("AlarmService", "getVolumeIncreaseRate");
        float volumeIncreaseRate = 0.0f;
        if (this.mItem != null && (!(this.mItem.isMasterSoundOn() && (this.mItem.isNewBixbyOn() || this.mItem.isNewCelebOn())) && 3 < this.mVolume)) {
            volumeIncreaseRate = (1.0f - this.START_VOLUME_LEVEL[this.mItem.mAlarmVolume]) / 48.0f;
        }
        Log.secD("AlarmService", "mVolumeIncreaseRate = " + volumeIncreaseRate);
        return volumeIncreaseRate;
    }

    private float getVolumeIncreaseRateForTts() {
        float volumeAfter1s = 1.0f;
        if (3 < this.mVolume) {
            volumeAfter1s = this.mCurVol + (this.mVolumeIncreaseRate * 4.0f);
        }
        float volumeIncreaseRateForTts = (volumeAfter1s - 0.1f) / 4.0f;
        if (volumeIncreaseRateForTts < 0.0f) {
            volumeIncreaseRateForTts = 0.0f;
        }
        Log.secD("AlarmService", "getVolumeIncreaseRateForTts volumeIncreaseRateForTts = " + volumeIncreaseRateForTts + " volumeAfter1s = " + volumeAfter1s + " mVolumeIncreaseRate = " + this.mVolumeIncreaseRate);
        return volumeIncreaseRateForTts;
    }

    private float getVolumeDecreaseRate() {
        Log.secD("AlarmService", "getVolumeDecreaseRate volumeDecreaseRate = " + 0.2f);
        return 0.2f;
    }

    private float getVolumeDecreaseRateForTts() {
        float volumeDecreaseRateForTts = 0.0f;
        if (this.mItem != null) {
            volumeDecreaseRateForTts = (this.mCurVol - 0.1f) / 4.0f;
        }
        if (volumeDecreaseRateForTts < 0.0f) {
            volumeDecreaseRateForTts = 0.0f;
        }
        Log.secD("AlarmService", "getVolumeDecreaseRateForTts volumeDecreaseRateForTts = " + volumeDecreaseRateForTts + " (mCurVol - MIN_VOLUME_DURING_TTS) = " + (this.mCurVol - 0.1f));
        return volumeDecreaseRateForTts;
    }

    private void increaseVolume(int count) {
        Log.secD("AlarmService", "increaseVolume mCurVol = " + this.mCurVol);
        float fVolume = BigDecimal.valueOf(1.0d).add(BigDecimal.valueOf(Math.sin(Math.toRadians((1.875d * ((double) count)) + 270.0d)))).setScale(6, RoundingMode.HALF_UP).floatValue() + this.START_VOLUME_LEVEL[this.mItem.mAlarmVolume];
        if (((double) fVolume) <= 0.0d || ((double) fVolume) >= 1.0d) {
            Log.m42e("AlarmService", "increaseVolume fVolume = " + fVolume + " mVolumeIncreaseRate = " + this.mVolumeIncreaseRate);
            this.mCurVol += this.mVolumeIncreaseRate;
        } else {
            this.mCurVol = fVolume;
        }
        if (this.mCurVol >= 1.0f) {
            Log.secD("AlarmService", "increaseVolume stopTimer");
            this.mCurVol = 1.0f;
            removeVolumeIncreaseTimer();
        }
        if (!this.mPlayer.mIsMute && !this.mIsTtsAlarm) {
            this.mPlayer.setVolume(this.mCurVol);
        }
    }

    private void increaseVolumeForTts() {
        if (this.mCurVolForTts == 1.0f) {
            Log.secD("AlarmService", "increaseVolumeForTts 1.0f");
            if (this.mVolumeIncreaseTimerForTts != null) {
                this.mVolumeIncreaseTimerForTts.cancel();
                this.mVolumeIncreaseTimerForTts = null;
            }
        } else if (this.mCurVolForTts > 1.0f) {
            Log.secD("AlarmService", "increaseVolumeForTts stopAlarm");
            this.mCurVolForTts = 1.0f;
            this.mPlayer.setVolume(this.mCurVolForTts);
        } else {
            Log.secD("AlarmService", "increaseVolumeForTts mCurVolForTts = " + this.mCurVolForTts);
            this.mCurVolForTts += this.mVolumeIncreaseRateForTts;
            this.mPlayer.setVolume(this.mCurVolForTts);
        }
    }

    private void decreaseVolume() {
        if (this.mCurVol == 0.0f) {
            if (this.mVolumeDecreaseTimer != null) {
                this.mVolumeDecreaseTimer.cancel();
                this.mVolumeDecreaseTimer = null;
            }
        } else if (this.mCurVol >= 0.0f) {
            Log.secD("AlarmService", "decreaseVolume mCurVol = " + this.mCurVol);
            this.mCurVol -= this.mVolumeDecreaseRate;
            this.mPlayer.setVolume(this.mCurVol);
        } else {
            this.mCurVol = 0.0f;
            this.mPlayer.setVolume(this.mCurVol);
        }
    }

    private void decreaseVolumeForTts() {
        if (this.mCurVolForTts <= 0.1f) {
            this.mCurVolForTts = 0.1f;
            if (this.mVolumeDecreaseTimerForTts != null) {
                this.mVolumeDecreaseTimerForTts.cancel();
                this.mVolumeDecreaseTimerForTts = null;
            }
        } else if (this.mCurVolForTts > 0.1f) {
            this.mCurVolForTts -= this.mVolumeDecreaseRateForTts;
            if (this.mCurVolForTts < 0.1f) {
                this.mCurVolForTts = 0.1f;
            }
        } else {
            this.mCurVolForTts = 0.1f;
        }
        this.mPlayer.setVolume(this.mCurVolForTts);
        Log.secD("AlarmService", "decreaseVolumeForTts mCurVolForTts = " + this.mCurVolForTts);
    }

    private void initIncreaseTimer() {
        removeVolumeIncreaseTimer();
        if (this.mItem != null) {
            this.mVolumeIncreaseTimer = new CountDownTimer((long) 12000, 250) {
                int count;

                public void onTick(long millisUntilFinished) {
                    AlarmService alarmService = AlarmService.this;
                    int i = this.count + 1;
                    this.count = i;
                    alarmService.increaseVolume(i);
                }

                public void onFinish() {
                    Log.secD("AlarmService", "mVolumeIncreaseTimer onFinish()");
                    if (AlarmService.this.mCurVol < 1.0f) {
                        Log.secD("AlarmService", "increaseVolume stopTimer");
                        AlarmService.this.mCurVol = 1.0f;
                        if (!AlarmService.this.mPlayer.mIsMute) {
                            AlarmService.this.mPlayer.setVolume(AlarmService.this.mCurVol);
                        }
                    }
                    if (AlarmService.this.mPlayer.getIncreasedAlarm()) {
                        AlarmService.this.mPlayer.setIncreasedAlarm(false);
                    }
                }
            };
        }
    }

    private void initIncreaseTimerForTts() {
        Log.secD("AlarmService", "initIncreaseTimerForTts");
        this.mVolumeIncreaseRateForTts = getVolumeIncreaseRateForTts();
        if (this.mVolumeIncreaseRateForTts > 0.0f) {
            if (this.mVolumeIncreaseTimerForTts != null) {
                this.mVolumeIncreaseTimerForTts.cancel();
                this.mVolumeIncreaseTimerForTts = null;
            }
            if (this.mItem != null) {
                this.mVolumeIncreaseTimerForTts = new CountDownTimer(1000, 250) {
                    public void onTick(long millisUntilFinished) {
                        if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.mIsMute && AlarmService.this.mIsTtsAlarm) {
                            AlarmService.this.increaseVolumeForTts();
                        }
                    }

                    public void onFinish() {
                        Log.secD("AlarmService", "mVolumeIncreaseTimerForTts onFinish()");
                        if (AlarmService.this.mCurVolForTts < 1.0f) {
                            Log.secD("AlarmService", "increaseVolumeForTts stopTimer");
                            AlarmService.this.mCurVolForTts = AlarmService.this.mCurVol;
                            if (!AlarmService.this.mPlayer.mIsMute) {
                                AlarmService.this.mPlayer.setVolume(AlarmService.this.mCurVolForTts);
                            }
                        }
                    }
                };
            }
            if (this.mVolumeIncreaseTimerForTts != null) {
                this.mVolumeIncreaseTimerForTts.start();
            }
        }
    }

    private void initDecreaseTimer() {
        Log.secD("AlarmService", "initDecreaseTimer");
        if (this.mVolumeDecreaseTimer == null) {
            this.mVolumeDecreaseTimer = new CountDownTimer(1000, 250) {
                public void onTick(long millisUntilFinished) {
                    if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.mIsMute) {
                        AlarmService.this.decreaseVolume();
                    }
                }

                public void onFinish() {
                    Log.secD("AlarmService", "mVolumeDecreaseTimer onFinish()");
                }
            };
        }
    }

    private void initDecreaseTimerForTts() {
        Log.secD("AlarmService", "initDecreaseTimerForTts");
        this.mVolumeDecreaseRateForTts = getVolumeDecreaseRateForTts();
        if (this.mVolumeDecreaseRateForTts <= 0.0f) {
            this.mPlayer.setVolume(0.1f);
            return;
        }
        if (this.mVolumeDecreaseTimerForTts == null) {
            this.mVolumeDecreaseTimerForTts = new CountDownTimer(1000, 250) {
                public void onTick(long millisUntilFinished) {
                    if (AlarmService.this.mPlayer != null && !AlarmService.this.mPlayer.mIsMute && AlarmService.this.mIsTtsAlarm) {
                        AlarmService.this.decreaseVolumeForTts();
                    }
                }

                public void onFinish() {
                    if (AlarmService.this.mCurVolForTts > 0.1f) {
                        AlarmService.this.mCurVolForTts = 0.1f;
                        Log.secD("AlarmService", "initDecreaseTimerForTts onFinish mCurVolForTts = " + AlarmService.this.mCurVolForTts);
                        AlarmService.this.mPlayer.setVolume(AlarmService.this.mCurVolForTts);
                    }
                    Log.secD("AlarmService", "initDecreaseTimerForTts onFinish()");
                }
            };
        }
        if (this.mVolumeDecreaseTimerForTts != null) {
            this.mVolumeDecreaseTimerForTts.start();
        }
    }

    private void removeCountDownTimer() {
        if (this.mAlertTimer != null) {
            this.mAlertTimer.cancel();
            this.mAlertTimer = null;
        }
        removeVolumeIncreaseTimer();
        if (this.mVolumeDecreaseTimer != null) {
            this.mVolumeDecreaseTimer.cancel();
            this.mVolumeDecreaseTimer = null;
        }
        if (this.mVolumeIncreaseTimerForTts != null) {
            this.mVolumeIncreaseTimerForTts.cancel();
            this.mVolumeIncreaseTimerForTts = null;
        }
        if (this.mVolumeDecreaseTimerForTts != null) {
            this.mVolumeDecreaseTimerForTts.cancel();
            this.mVolumeDecreaseTimerForTts = null;
        }
    }

    private void removeVolumeIncreaseTimer() {
        if (this.mVolumeIncreaseTimer != null) {
            this.mVolumeIncreaseTimer.cancel();
            this.mVolumeIncreaseTimer = null;
            if (this.mPlayer.getIncreasedAlarm()) {
                this.mPlayer.setIncreasedAlarm(false);
            }
        }
    }

    private void setVoiceString() {
        Calendar c = Calendar.getInstance();
        boolean b24HMode = DateFormat.is24HourFormat(this);
        int hour = c.get(11);
        int min = c.get(12);
        if (Locale.getDefault().getLanguage().equals("ar")) {
            if (b24HMode) {
                this.mVoiceString = getResources().getString(C0490R.string.alarm_tts_current_time_for_arabic);
            } else {
                hour = ClockUtils.getAmPmHour(hour);
                if (hour < 0) {
                    hour *= -1;
                    this.mVoiceString = getResources().getString(C0490R.string.alarm_tts_current_time_am_for_arabic);
                } else {
                    this.mVoiceString = getResources().getString(C0490R.string.alarm_tts_current_time_pm_for_arabic);
                }
            }
            if (this.mItem != null) {
                this.mVoiceString = String.format(this.mVoiceString, new Object[]{Integer.valueOf(hour), Integer.valueOf(min)});
                this.mVoiceString = ClockUtils.arabicToNumber(this.mVoiceString);
            }
        } else {
            if (b24HMode) {
                this.mVoiceString = getResources().getString(C0490R.string.alarm_tts_current_time);
            } else {
                hour = ClockUtils.getAmPmHour(hour);
                if (hour < 0) {
                    hour *= -1;
                    this.mVoiceString = getResources().getString(C0490R.string.alarm_tts_current_time_am);
                } else {
                    this.mVoiceString = getResources().getString(C0490R.string.alarm_tts_current_time_pm);
                }
            }
            if (this.mItem != null) {
                this.mVoiceString = String.format(this.mVoiceString, new Object[]{Integer.valueOf(hour), Integer.valueOf(min)});
            }
        }
        Log.m41d("AlarmService", "setVoiceString mVoiceString = " + this.mVoiceString);
    }

    private boolean isOldAlarm() {
        boolean isOld = false;
        if (!(AlarmProvider.isEcbm(this.mContext) || this.mItem == null || this.mItem.mId == -1)) {
            int alarmRingingTime;
            long currentTime = System.currentTimeMillis();
            if (this.mItem.isFirstAlarm()) {
                alarmRingingTime = this.mItem.getSnoozeDurationMinutes();
            } else {
                alarmRingingTime = 60000;
            }
            Log.secD("AlarmService", "alarmRingingTime = " + alarmRingingTime);
            if (this.mItem.mAlarmAlertTime + ((long) alarmRingingTime) < currentTime) {
                isOld = true;
                AlarmNotificationHelper.getInstance().cancel(this, 268439552);
                Log.secD("AlarmService", "isOldAlarm isOld mItem = " + this.mItem.toString());
                AlarmItem itemReal = AlarmProvider.getAlarm(getApplicationContext(), this.mItem.mId);
                if (itemReal != null) {
                    Log.secD("AlarmService", "itemReal = " + itemReal.toString());
                    if (itemReal.isFirstAlarm() || itemReal.mActivate == 0) {
                        AlarmNotificationHelper.clearNotification(this.mContext, this.mItem.mId);
                        if (!this.mItem.mSnoozeActivate) {
                            AlarmNotificationHelper.showMissedNotification(this.mContext, this.mItem);
                        } else if (this.mItem.getSnoozeRepeatTimes() == this.mItem.mSnoozeDoneCount) {
                            AlarmNotificationHelper.showMissedNotification(this.mContext, this.mItem);
                        }
                    }
                }
            }
        }
        return isOld;
    }

    private void startLEDBackCoverTimer() {
        Log.secD("AlarmService", "startLEDBackCoverTimer");
        if (this.mStopLedBackCoverTimer != null) {
            this.mStopLedBackCoverTimer.cancel();
        }
        this.mStopLedBackCoverTimer = new Timer();
        this.mStopLedBackCoverTimer.schedule(new StopLedBackCoverTimerTask(this.mContext, 0), 60000);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_PAUSE");
        filter.addAction("com.samsung.android.motion.PALM_DOWN");
        filter.addAction("com.android.phone.COMPLETE_AUDIO_RESET_AFTER_CALL_END");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
        filter.addAction("android.app.action.ENTER_KNOX_DESKTOP_MODE");
        filter.addAction("android.app.action.EXIT_KNOX_DESKTOP_MODE");
        filter.addAction("com.samsung.android.mirrorlink.ML_STATE");
        filter.addAction("android.intent.action.USER_SWITCHED");
        filter.addAction("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_TOUCH");
        filter.addAction("com.sec.android.app.clockpackage.intent.action.RECEIVE_BIXBY_ALARM");
        filter.addAction("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION");
        if (StateUtils.isSoundModeOnForJapan(this.mContext)) {
            filter.addAction("android.intent.action.HEADSET_PLUG");
            filter.addAction("android.media.RINGER_MODE_CHANGED");
        }
        if (this.mReceiver == null) {
            this.mReceiver = new AudioReceiver();
        }
        registerReceiver(this.mReceiver, filter);
    }

    private void setRegisterInternalReceiver() {
        this.mInternalReceiver = new InternalReceiver();
        IntentFilter internalFilter = new IntentFilter();
        internalFilter.addAction("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_TIMER_ALERT_START");
        internalFilter.addAction("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_STOP");
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mInternalReceiver, internalFilter);
    }

    private void unregisterReceiver() {
        if (this.mReceiver != null) {
            Log.secD("AlarmService", "unregisterReceiver");
            try {
                unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secE("AlarmService", "unregisterReceiver IllegalArgumentException");
            } finally {
                this.mReceiver = null;
            }
        }
        if (this.mInternalReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInternalReceiver);
            this.mInternalReceiver = null;
            Log.secD("AlarmService", "unregisterReceiver LocalBroadcastManager");
        }
    }

    private void playAlarm() {
        if (this.mPlayer != null) {
            if (this.mPlayer.getBehindTimerState()) {
                this.mPlayer.setBehindTimerState(false);
                this.mIsPausePlaying = false;
            }
            if (!this.mIsVoipCall) {
                this.mIsVoipCall = StateUtils.isInVoipCall(this.mContext);
            }
            if (!this.mIsRecording) {
                this.mIsRecording = StateUtils.isRecordingState(this.mContext);
            }
            if (!this.mIsVideoCall) {
                this.mIsVideoCall = StateUtils.isVideoCall(this.mContext);
            }
            Log.secD("AlarmService", "playAlarm mIsVoipCall = " + this.mIsVoipCall + " mIsRecording = " + this.mIsRecording + " mIsVideoCall = " + this.mIsVideoCall);
            if (!this.mIsVoipCall && !this.mIsRecording && !this.mIsVideoCall) {
                changeModeAndResumePlayer();
            }
        }
    }

    private void changeModeAndResumePlayer() {
        Log.secD("AlarmService", "changeModeAndResumePlayer");
        int backupPlayMode = this.mPlayer.getPlayMode();
        setMode();
        int currentPlayMode = this.mPlayer.getPlayMode();
        if ((backupPlayMode & 16) > 0 && (currentPlayMode & 16) == 0) {
            this.mPlayer.resetVibrate();
        }
        if (this.mPhoneStateExtra.equals(TelephonyManager.EXTRA_STATE_IDLE) && !this.mPlayer.getPalm()) {
            Log.secD("AlarmService", "EXTRA_STATE_IDLE && !getPalm()");
            this.mPlayer.resumeAndSetVolume(this.mCount, this.mCurVol);
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case 10000:
                Log.secD("AlarmService", "case START_DECREASE_SOUND");
                if (isNeededToPlayTtsAlarm()) {
                    this.mIsTtsAlarm = true;
                    this.mCurVolForTts = this.mCurVol;
                    initDecreaseTimerForTts();
                    if (this.mItem.isFirstAlarm()) {
                        this.mTtsHandler.sendEmptyMessageDelayed(10000, 60000);
                        return;
                    }
                    return;
                }
                return;
            case 10020:
                Log.m41d("AlarmService", "case START_TTS");
                if (isNeededToPlayTtsAlarm()) {
                    if (this.mItem.isFirstAlarm() && (!this.mIsBixbyOrCelebVoice || this.mIsBixbyCelebVoice)) {
                        this.mTtsHandler.sendEmptyMessageDelayed(10020, 60000);
                        if (this.mIsBixbyCelebVoice) {
                            this.mTtsHandler.sendEmptyMessageDelayed(10120, 60000);
                            this.mPlayer.setBixbyBriefing(true);
                        }
                    }
                    if (this.mIsBixbyOrCelebVoice) {
                        this.mTtsHandler.sendEmptyMessageDelayed(10160, this.mDelayTimeForForceStopBixbyBriefing);
                    } else {
                        this.mTtsHandler.sendEmptyMessageDelayed(10080, 10000);
                    }
                    if (!StateUtils.isRecordingState(this.mContext)) {
                        if (!this.mIsBixbyOrCelebVoice) {
                            setVoiceString();
                        }
                        if (this.mPlayer != null) {
                            if ((this.mPlayer.getPlayMode() & 16) > 0) {
                                this.mPlayer.resetVibrate();
                            }
                            this.mPlayer.setPlayMode(4);
                            this.mPlayer.setPlayResource(this.mSoundTone, this.mVoiceString, this.mVibPattern);
                            this.mPlayer.play();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 10060:
                Log.m41d("AlarmService", "case END_TTS");
                this.mTtsHandler.removeMessages(10080);
                this.mIsTtsAlarm = false;
                if (this.mPlayer != null) {
                    this.mPlayer.setVolume(this.mCurVol);
                    return;
                }
                return;
            case 10080:
                Log.m41d("AlarmService", "case FORCE_STOP_TTS");
                if (this.mPlayer != null) {
                    this.mPlayer.setEndAlertOnVoice(true);
                    return;
                }
                return;
            case 10120:
                Log.m41d("AlarmService", "case START_BIXBY_BRIEFING");
                if (this.mPlayer != null) {
                    this.mPlayer.setBixbyBriefingState(1);
                    return;
                }
                return;
            case 10160:
                Log.m41d("AlarmService", "case FORCE_STOP_BIXBYBRIEFING");
                if (this.mPlayer != null) {
                    this.mPlayer.setBixbyBriefingState(2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private boolean isNeededToPlayTtsAlarm() {
        boolean bNeededToPlayTtsAlarm = this.mIsBixbyOrCelebVoice || !(this.mItem.isNewBixbyOn() || this.mItem.isNewCelebOn() || !this.mItem.isPossibleTtsAlarm());
        Log.secD("AlarmService", "isNeededToPlayTtsAlarm bNeededToPlayTtsAlarm = " + bNeededToPlayTtsAlarm);
        return bNeededToPlayTtsAlarm;
    }

    private boolean isSameIdAndTimeWithBixbyBriefingItem() {
        boolean bSame = false;
        if (this.mItem.mId == this.mBixbyBriefingItem.mId && this.mItem.mAlarmAlertTime == this.mBixbyBriefingItem.mAlarmAlertTime) {
            bSame = true;
        }
        if (this.mItem.mActivate == 2 && this.mIsBixbyCelebVoice && this.mItem.mId == this.mBixbyBriefingItem.mId && this.mItem.mAlarmTime == this.mBixbyBriefingItem.mAlarmTime) {
            bSame = true;
        }
        Log.secD("AlarmService", "isSameIdAndTimeWithBixbyBriefingItem bSame = " + bSame);
        return bSame;
    }

    private void setBixbyBriefingInformation(boolean bDirect) {
        if (this.mItem.isPossibleBixbyBriefingAlarm()) {
            String logString = "";
            if (!bDirect) {
                this.mBixbyBriefingItem = AlarmSharedManager.getBixbyBriefingInformation(this.mContext);
            }
            if (!(isSameIdAndTimeWithBixbyBriefingItem() && StateUtils.isPossibleStateForBixbyBriefing(this.mContext))) {
                this.mBixbyBriefingItem.init();
            }
            sBixbyBriefWeatherConditionCode = this.mBixbyBriefingItem.mWeatherConditionCode;
            sBixbyBriefWeatherCpLink = this.mBixbyBriefingItem.mWeatherCpLink;
            if (this.mBixbyBriefingItem.mDaytime == 0) {
                Calendar c = Calendar.getInstance();
                int alarmTime = (c.get(11) * 100) + c.get(12);
                Log.m41d("AlarmService", "setWeatherImg alarmTime = " + alarmTime);
                if (alarmTime >= 600 && alarmTime <= 1759) {
                    sBixbyBriefDaytime = 1;
                } else if ((alarmTime >= 0 && alarmTime <= 559) || (alarmTime >= 1800 && alarmTime <= 2359)) {
                    sBixbyBriefDaytime = 2;
                }
            } else {
                sBixbyBriefDaytime = this.mBixbyBriefingItem.mDaytime;
            }
            this.mIsBixbyOrCelebVoice = this.mItem.isSoundOnState();
            if (this.mIsBixbyOrCelebVoice && this.mItem.isNewBixbyOn()) {
                boolean z;
                if (isSameIdAndTimeWithBixbyBriefingItem() && StateUtils.isPossibleStateForBixbyBriefing(this.mContext)) {
                    z = true;
                } else {
                    z = false;
                }
                this.mIsBixbyOrCelebVoice = z;
            }
            Log.m41d("AlarmService", "setBixbyBriefingInformation mIsBixbyOrCelebVoice = " + this.mIsBixbyOrCelebVoice + " before if (mItem.isNewCelebOn() && mIsBixbyOrCelebVoice) {");
            if (this.mItem.isNewCelebOn() && this.mIsBixbyOrCelebVoice) {
                boolean bValidCelebrityVoicePath = AlarmUtil.isValidCelebrityVoicePath(this.mContext, this.mItem.mCelebVoicePath);
                boolean bExpiredCelebVoice = AlarmUtil.isExpiredCelebVoice(this.mContext, this.mItem.mCelebVoicePath);
                if (!this.mIsSupportCelebVoice || (bValidCelebrityVoicePath && bExpiredCelebVoice)) {
                    this.mIsBixbyOrCelebVoice = false;
                    if (!bValidCelebrityVoicePath) {
                        logString = ", bValidCelebrityVoicePath = false";
                    }
                    if (bExpiredCelebVoice) {
                        logString = logString + ", bExpiredCelebVoice = true";
                    }
                } else {
                    this.mIsBixbyOrCelebVoice = true;
                    this.mIsBixbyCelebVoice = true;
                    if (bExpiredCelebVoice && !bValidCelebrityVoicePath) {
                        logString = ", !bValidCelebrityVoicePath";
                        this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
                    }
                }
                if (this.mIsBixbyCelebVoice) {
                    this.mDelayTimeForForceStopBixbyBriefing = 50000;
                }
            }
            if (Feature.isPackageSupportedVersion(this.mContext, "com.samsung.android.bixby.agent", 100806002) && this.mItem.isNewBixbyOn() && this.mIsBixbyOrCelebVoice) {
                this.mDelayTimeForForceStopBixbyBriefing = 15000 + (this.mBixbyBriefingItem.mPlayTimeOfBixbyBriefing * 1000);
            }
            Log.m41d("AlarmService", "setBixbyBriefingInformation mDelayTimeForForceStopBixbyBriefing = " + this.mDelayTimeForForceStopBixbyBriefing + " ms, mIsBixbyOrCelebVoice = " + this.mIsBixbyOrCelebVoice + logString);
            return;
        }
        sBixbyBriefWeatherCpLink = "";
        sBixbyBriefDaytime = 0;
    }

    private void setBriefingTypeAndDelayForTtsAlarm(boolean bDirect) {
        setDelayTimeForTtsAlarm(bDirect);
        setBriefingTypeAndPath();
    }

    private void setDelayTimeForTtsAlarm(boolean bDirect) {
        if (isNeededToPlayTtsAlarm()) {
            this.mDelayTime = 5000;
            if (bDirect) {
                this.mDelayTime = 1000;
            }
            if (StateUtils.isTalkBackEnabled(this.mContext)) {
                if (this.mIsBixbyOrCelebVoice) {
                    this.mDelayTime += 5000;
                } else {
                    this.mDelayTime += 60000;
                }
            }
            Log.m41d("AlarmService", "setDelayTimeForTtsAlarm mDelayTime = " + this.mDelayTime + " ms, bDirect = " + bDirect);
            this.mTtsHandler.sendEmptyMessageDelayed(10000, (long) this.mDelayTime);
            this.mTtsHandler.sendEmptyMessageDelayed(10020, (long) (this.mDelayTime + 2000));
        }
    }

    private void setBriefingTypeAndPath() {
        if (isNeededToPlayTtsAlarm()) {
            if (!this.mItem.isNewBixbyOn() && !this.mItem.isNewCelebOn() && this.mItem.isPossibleTtsAlarm()) {
                this.mBriefingType = 1;
            } else if (this.mIsBixbyOrCelebVoice) {
                this.mTtsHandler.sendEmptyMessageDelayed(10120, (long) (this.mDelayTime + 2000));
                this.mPlayer.setBixbyBriefing(true);
                if (this.mIsBixbyCelebVoice) {
                    String preFixFileName = this.mItem.mCelebVoicePath.substring(this.mItem.mCelebVoicePath.lastIndexOf(46) + 1);
                    String type = CelebVoiceProvider.getCelebrityVoiceType(this.mContext, this.mItem.mCelebVoicePath);
                    if (preFixFileName.startsWith("bca_")) {
                        String fullFilePath = CelebUtils.getOldCelebFullPath(this.mItem.mCelebVoicePath, type, this.mBixbyBriefingItem.mWeatherConditionCode);
                        if (fullFilePath != null) {
                            this.mBriefingType = 3;
                            this.mPlayer.setBixbyCelebrityVoicePath(fullFilePath);
                        }
                    } else if (AlarmUtil.isNewCelebDefault(this.mItem.mCelebVoicePath)) {
                        this.mBriefingType = 4;
                        this.mPlayer.setBixbyCelebrityGreetingUri(Uri.parse(CelebUtils.getGreetingOnlyFullPath(this.mContext)));
                    } else if (preFixFileName.startsWith("sca_")) {
                        this.mBriefingType = 5;
                        String fullPath = null;
                        if (Feature.isSupportHolidayForCeleb()) {
                            fullPath = CelebUtils.getHolidayFullPath(this.mContext, this.mItem.mCelebVoicePath, type, this.mItem.mAlarmAlertTime);
                        }
                        if (fullPath == null) {
                            fullPath = CelebUtils.getWeatherOrTimeDayGenderFullPath(this.mContext, this.mItem.mCelebVoicePath, type, this.mItem.mAlarmTime, this.mBixbyBriefingItem.mWeatherConditionCode);
                        }
                        this.mPlayer.setBixbyCelebrityVoicePath(fullPath);
                    }
                } else {
                    this.mBriefingType = 2;
                    this.mPlayer.setBixbyBriefingUri(this.mBixbyBriefingItem.mUri);
                }
            }
            this.mPlayer.setBriefingType(this.mBriefingType);
            Log.secD("AlarmService", "setBriefingTypeAndPath mBriefingType = " + this.mBriefingType);
        }
    }

    private void checkInvalidAlarmAndChangeCorrectAlarm(Context context) {
        int i = 0;
        if (this.mItem.isMasterSoundOn()) {
            int i2;
            boolean bSupportNewCelebFeature = Feature.isSupportCelebrityAlarm();
            boolean bSupportBixbyAlarm = Feature.isSupportBixbyBriefingMenu(context);
            String log = " bSupportNewCelebFeature = " + bSupportNewCelebFeature + " bSupportBixbyAlarm = " + bSupportBixbyAlarm;
            if (this.mItem.isNewCelebOn()) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            i2 += this.mItem.isNewBixbyOn() ? 1 : 0;
            if (this.mItem.isAlarmToneOn()) {
                i = 1;
            }
            if (i2 + i == 1) {
                if (!this.mItem.isNewCelebOn() || bSupportNewCelebFeature) {
                    if (this.mItem.isNewBixbyOn() && !bSupportBixbyAlarm) {
                        if (bSupportNewCelebFeature) {
                            this.mItem.setSoundModeNewCeleb();
                            this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
                        } else {
                            this.mItem.setSoundModeAlarmTone();
                        }
                    }
                } else if (bSupportBixbyAlarm) {
                    this.mItem.setSoundModeNewBixby();
                } else {
                    this.mItem.setSoundModeAlarmTone();
                }
                Log.m41d("AlarmService", "checkInvalidAlarmAndChangeCorrectAlarm after mDailyBriefing = " + Integer.toBinaryString(this.mItem.mDailyBriefing) + log);
            } else {
                if (this.mItem.isNewCelebOn() && bSupportNewCelebFeature) {
                    this.mItem.setSoundModeNewCeleb();
                    this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
                } else if (this.mItem.isNewBixbyOn() && bSupportBixbyAlarm) {
                    this.mItem.setSoundModeNewBixby();
                } else {
                    this.mItem.setSoundModeAlarmTone();
                }
                Log.m41d("AlarmService", "checkInvalidAlarmAndChangeCorrectAlarm after2 mDailyBriefing = " + Integer.toBinaryString(this.mItem.mDailyBriefing) + log);
            }
            if (!this.mItem.isAlarmToneOn()) {
                return;
            }
            if (this.mItem.mAlarmSoundTone == -1 || !AlarmRingtoneManager.validRingtoneStr(this.mContext, this.mItem.mSoundUri)) {
                this.mItem.mSoundUri = AlarmRingtoneManager.getAlarmTonePreferenceOrDefaultRingtoneUri(this.mContext);
                Log.m41d("AlarmService", "checkInvalidAlarmAndChangeCorrectAlarm after3 mSoundUri = " + this.mItem.mSoundUri);
                return;
            }
            Log.m42e("AlarmService", "checkInvalidAlarmAndChangeCorrectAlarm after3 AlarmRingtoneManager.validRingtoneStr");
        }
    }

    private void stopAlarm() {
        Log.secD("AlarmService", "stopAlarm mIsTimeOut = " + this.mIsTimeOut);
        if (!this.mIsTimeOut.booleanValue()) {
            this.mIsTimeOut = Boolean.valueOf(true);
            Intent stopAlarmIntent = new Intent();
            stopAlarmIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP");
            stopAlarmIntent.putExtra("bisTimeOut", this.mIsTimeOut);
            if (this.mItem.isDefaultStop()) {
                stopAlarmIntent.putExtra("bDismiss", true);
                ClockUtils.insertSaLog(this.mScreenId, "1244");
            } else {
                ClockUtils.insertSaLog(this.mScreenId, "1245");
            }
            insertSaLogAlarmDuration(this.mScreenId, true);
            this.mContext.sendBroadcast(stopAlarmIntent);
            AlertUtils.sendLocalStopAlarmAlertIntent(this.mContext);
            stopSelf();
        }
    }

    private void insertSaLogAlarmDuration(String screenId, boolean isTimeOut) {
        if (!isTimeOut) {
            int duration_5sec = (this.mCount / 20) + 1;
            if (this.mIsFirstAlarm) {
                ClockUtils.insertSaLog(screenId, "1246", String.valueOf(duration_5sec));
            } else {
                ClockUtils.insertSaLog(screenId, "1247", String.valueOf(duration_5sec));
            }
        } else if (this.mIsFirstAlarm) {
            ClockUtils.insertSaLog(screenId, "1246", String.valueOf(61));
        } else {
            ClockUtils.insertSaLog(screenId, "1247", String.valueOf(13));
        }
    }
}
