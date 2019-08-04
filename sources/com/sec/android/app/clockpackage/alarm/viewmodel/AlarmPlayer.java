package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmManagerClient;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.VibrationEffect.SemMagnitudeType;
import android.os.Vibrator;
import android.os.Vibrator.SemMagnitudeTypes;
import android.provider.Settings.System;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.provider.FontsContractCompat.FontRequestCallback;
import android.telephony.TelephonyManager;
import com.samsung.android.gesture.SemMotionEventListener;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.gesture.SemMotionRecognitionManager;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.util.FlashNotificationUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.MissingResourceException;

public class AlarmPlayer {
    private static final int[] PLAY_MODE = new int[]{1, 16, 17, 256, 4352, 3, 0};
    private static Vibrator sAlarmVibrator;
    private static final Object sLock = new Object();
    private boolean hasGreetingInNewCeleb = false;
    private int mAudioAttribute = 4;
    private final OnAudioFocusChangeListener mAudioFocusListener = new C06238();
    private AudioManager mAudioManager;
    private int mAudioStream = 4;
    private int mBixbyBriefingState = 0;
    private Uri mBixbyBriefingUri;
    private float mBixbyBriefingVolume = 1.0f;
    private String mBixbyCelebrityGreetingVoiceFullPath;
    private Uri mBixbyCelebrityVGreetingVoiceUri;
    private String mBixbyCelebrityVoiceFullPath;
    private int mBriefingType = 0;
    private String mCallState = null;
    private final Context mContext;
    private Uri mDefaultSoundUri;
    private boolean mEndAlertOnVoice;
    private final Handler mHandler = new TTSHandler(this);
    private boolean mHasFocus = false;
    private boolean mIsBixbyBriefing = false;
    private boolean mIsFinishing;
    private boolean mIsHideByTimer = false;
    private boolean mIsIncreasedAlarm = false;
    private boolean mIsInsertedEarphone = false;
    public boolean mIsMute = false;
    private boolean mIsNewCeleb = false;
    private boolean mIsPalm = false;
    private float mMediaPlayerVolume = 1.0f;
    private SemMotionEventListener mMotionListener = new C06151();
    private SemMotionRecognitionManager mMotionSensorManager = null;
    private int mOldMusicVolume = 0;
    private boolean mPause;
    private int mPlayMode;
    private boolean mPrevDndModeAlarmMuted = false;
    private MediaPlayer mSoundBixbyBriefingPlayer;
    private MediaPlayer mSoundPlayer;
    private int mSoundPosition = 0;
    private Uri mSoundUri;
    private int mTtsResult;
    private String mTtsString;
    private int mVibPattern;
    private TextToSpeech mVoicePlayer;
    private int mVolume = 0;
    private int newCelebVoiceState = 0;

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$1 */
    class C06151 implements SemMotionEventListener {
        C06151() {
        }

        public void onMotionEvent(SemMotionRecognitionEvent motionEvent) {
            int motion = motionEvent.getMotion();
            Log.secD("AlarmPlayer", "onMotionListener mIsHideByTimer = " + AlarmPlayer.this.mIsHideByTimer + " mPause = " + AlarmPlayer.this.mPause + " motion = " + motion);
            if (AlarmPlayer.this.mIsHideByTimer || AlarmPlayer.this.mPause) {
                Log.secD("AlarmPlayer", "mIsHideByTimer = " + AlarmPlayer.this.mIsHideByTimer + " mPause= " + AlarmPlayer.this.mPause);
            } else if (!AlarmPlayer.this.mIsMute || AlarmPlayer.this.mIsPalm) {
                switch (motion) {
                    case 10:
                        AlarmPlayer.this.mIsMute = true;
                        AlarmPlayer.this.mIsPalm = true;
                        AlarmPlayer.this.stop();
                        ClockUtils.insertSaLog("107", "1142");
                        FlashNotificationUtils.stopFlashNotification(AlarmPlayer.this.mContext);
                        return;
                    case 86:
                        FlashNotificationUtils.stopFlashNotification(AlarmPlayer.this.mContext);
                        return;
                    default:
                        return;
                }
            } else {
                Log.secD("AlarmPlayer", "mIsMute = " + AlarmPlayer.this.mIsMute + "mIsPalm = " + AlarmPlayer.this.mIsPalm + " return");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$2 */
    class C06162 implements OnErrorListener {
        C06162() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.secE("AlarmPlayer", "Error occurred while playing audio.");
            mp.stop();
            mp.release();
            AlarmPlayer.this.mSoundPlayer = null;
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$3 */
    class C06173 implements OnErrorListener {
        C06173() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.secE("AlarmPlayer", "Error occurred while playing audio.");
            mp.stop();
            mp.release();
            AlarmPlayer.this.mSoundBixbyBriefingPlayer = null;
            AlarmPlayer.this.setBixbyBriefingState(2);
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$4 */
    class C06184 implements OnCompletionListener {
        C06184() {
        }

        public void onCompletion(MediaPlayer mp) {
            Exception e;
            Log.secD("AlarmPlayer", "mSoundBixbyBriefingPlayer setOnCompletionListener onCompletion mBixbyCelebrityVoiceFullPath = " + AlarmPlayer.this.mBixbyCelebrityVoiceFullPath + " hasGreetingInNewCeleb() = " + AlarmPlayer.this.hasGreetingInNewCeleb());
            if (AlarmPlayer.this.hasGreetingInNewCeleb() && AlarmPlayer.this.mBixbyCelebrityVoiceFullPath != null && AlarmPlayer.this.getNewCelebVoiceState() == 1) {
                AlarmPlayer.this.setNewCelebVoiceState(2);
                try {
                    Log.secD("AlarmPlayer", "mSoundBixbyBriefingPlayer setDataSource mBixbyCelebrityVoiceFullPath = " + AlarmPlayer.this.mBixbyCelebrityVoiceFullPath);
                    AlarmPlayer.this.mSoundBixbyBriefingPlayer.reset();
                    AlarmPlayer.this.mSoundBixbyBriefingPlayer.setDataSource(AlarmPlayer.this.mBixbyCelebrityVoiceFullPath);
                    AlarmPlayer.this.mSoundBixbyBriefingPlayer.prepare();
                    AlarmPlayer.this.mSoundBixbyBriefingPlayer.start();
                    return;
                } catch (IOException e2) {
                    e = e2;
                } catch (IllegalArgumentException e3) {
                    e = e3;
                } catch (SecurityException e4) {
                    e = e4;
                } catch (IllegalStateException e5) {
                    e = e5;
                }
            } else {
                if (AlarmPlayer.this.hasGreetingInNewCeleb() && AlarmPlayer.this.getNewCelebVoiceState() == 2) {
                    AlarmPlayer.this.setNewCelebVoiceState(0);
                }
                AlarmPlayer.this.finishBixbyBriefing();
                return;
            }
            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
            AlarmPlayer.this.finishBixbyBriefing();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$5 */
    class C06195 implements OnErrorListener {
        C06195() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.secE("AlarmPlayer", "Error occurred while playing audio.");
            mp.stop();
            mp.release();
            AlarmPlayer.this.mSoundPlayer = null;
            AlarmPlayer.this.abandonAudioFocus();
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$6 */
    class C06206 extends UtteranceProgressListener {
        C06206() {
        }

        public void onStart(String utteranceId) {
            Log.secD("AlarmPlayer", "mVoicePlayer onStart");
        }

        public void onDone(String utteranceId) {
            Log.secD("AlarmPlayer", "onDone utteranceId : " + utteranceId);
            if ((AlarmPlayer.this.mPlayMode & 4352) == 4352) {
                AlarmPlayer.this.mEndAlertOnVoice = true;
                AlarmPlayer.this.stopVoiceOnly();
            }
        }

        public void onError(String utteranceId) {
            Log.secE("AlarmPlayer", "mVoicePlayer onError");
            AlarmPlayer.this.mEndAlertOnVoice = true;
            AlarmPlayer.this.stopVoiceOnly();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$7 */
    class C06217 implements OnInitListener {
        C06217() {
        }

        public void onInit(int status) {
            AlarmPlayer.this.mTtsResult = status;
            if (AlarmPlayer.this.mTtsResult == 0) {
                AlarmPlayer.this.mHandler.sendMessage(Message.obtain(AlarmPlayer.this.mHandler, 16385));
            } else if (!AlarmPlayer.this.mPause) {
                AlarmPlayer.this.mHandler.sendMessageDelayed(Message.obtain(AlarmPlayer.this.mHandler, 16384), 200);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$8 */
    class C06238 implements OnAudioFocusChangeListener {

        /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer$8$1 */
        class C06221 implements Runnable {
            C06221() {
            }

            public void run() {
                AlarmPlayer.this.setVolume(1.0f);
                Log.secD("AlarmPlayer", "case FOCUS_GAIN: setVolume(1.0f);");
            }
        }

        C06238() {
        }

        public void onAudioFocusChange(int focusChange) {
            Log.secD("AlarmPlayer", "onAudioFocusChange - focusChange : " + focusChange);
            switch (focusChange) {
                case FontRequestCallback.FAIL_REASON_FONT_LOAD_ERROR /*-3*/:
                    Log.secD("AlarmPlayer", "case FOCUS_LOSS_TRANSIENT_CAN_DUCK(-3)");
                    AlarmPlayer.this.saveAlarmVolume();
                    return;
                case -2:
                case -1:
                    Log.secD("AlarmPlayer", "case FOCUS_LOSS(-1) or FOCUS_LOSS_TRANSIENT(-2)");
                    AlarmPlayer.this.mIsMute = true;
                    AlarmPlayer.this.saveAlarmVolume();
                    AlarmPlayer.this.stopSoundOnly();
                    AlarmPlayer.this.stopBixbyBriefingOnly();
                    return;
                case 1:
                    if (!AlarmPlayer.this.mPause) {
                        AlarmPlayer.this.mIsMute = false;
                    }
                    AlarmPlayer.this.setStreamAlarmVolume();
                    Log.secD("AlarmPlayer", "case FOCUS_GAIN: mIsIncreasedAlarm = " + AlarmPlayer.this.mIsIncreasedAlarm + ", getBixbyBriefingState() = " + AlarmPlayer.this.getBixbyBriefingState());
                    if ((!AlarmPlayer.this.mIsIncreasedAlarm || ((double) AlarmPlayer.this.mMediaPlayerVolume) >= 1.0d) && AlarmPlayer.this.getBixbyBriefingState() != 1) {
                        new Handler().postDelayed(new C06221(), 300);
                    }
                    AlarmPlayer.this.setBixbyBriefingVolume(1.0f);
                    Log.secD("AlarmPlayer", "case FOCUS_GAIN: mIsMute = " + AlarmPlayer.this.mIsMute + ", mPause = " + AlarmPlayer.this.mPause);
                    return;
                default:
                    return;
            }
        }
    }

    static class TTSHandler extends Handler {
        private final WeakReference<AlarmPlayer> mActivity;

        TTSHandler(AlarmPlayer activity) {
            this.mActivity = new WeakReference(activity);
        }

        public void handleMessage(Message msg) {
            AlarmPlayer activity = (AlarmPlayer) this.mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    public AlarmPlayer(Context c) {
        this.mContext = ClockUtils.getBootAwareContext(c);
        this.mPlayMode = 0;
        sAlarmVibrator = null;
        this.mVoicePlayer = null;
        this.mTtsString = null;
        this.mSoundUri = null;
        this.mDefaultSoundUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 4);
        if (this.mDefaultSoundUri == null) {
            Log.secE("AlarmPlayer", "mDefaultSoundUri == null");
            this.mDefaultSoundUri = Uri.parse("content://media/internal/audio/media/48");
        }
        this.mPause = false;
        this.mEndAlertOnVoice = false;
        this.mIsFinishing = false;
        this.mAudioManager = (AudioManager) c.getSystemService("audio");
        Log.secD("AlarmPlayer", "AlertPlayer constructor call.");
        if (StateUtils.isMannerModeState(this.mContext)) {
            this.mOldMusicVolume = this.mAudioManager.getStreamVolume(3);
        }
    }

    private synchronized void registerMotionSensorManager() {
        Log.secD("AlarmPlayer", "registerMotionSensorManager");
        if (this.mMotionSensorManager == null) {
            this.mMotionSensorManager = (SemMotionRecognitionManager) this.mContext.getSystemService("motion_recognition");
        }
        if (this.mMotionSensorManager != null) {
            this.mMotionSensorManager.registerListener(this.mMotionListener, 1);
            this.mMotionSensorManager.registerListener(this.mMotionListener, 131072);
        }
    }

    private void unregisterMotionSensorManager() {
        if (this.mMotionSensorManager != null) {
            Log.secD("AlarmPlayer", "unregisterMotionSensorManager");
            this.mMotionSensorManager.unregisterListener(this.mMotionListener);
            this.mMotionSensorManager = null;
        }
    }

    public void setPlayMode(int mode) {
        if (mode <= 6) {
            this.mPlayMode = PLAY_MODE[mode];
        } else {
            this.mPlayMode = PLAY_MODE[2];
        }
        Log.m41d("AlarmPlayer", "setPlayMode :mode = " + mode + ", mPlayMode = " + this.mPlayMode);
    }

    public int getPlayMode() {
        return this.mPlayMode;
    }

    public void setPlayResource(String uri, String tts, int vib_pattern) {
        if (uri != null) {
            String UriString = Uri.decode(uri);
            if (UriString.equals("alarm_silent_ringtone")) {
                this.mSoundUri = Uri.parse(UriString);
            } else {
                if (UriString.contains("content://media/")) {
                    this.mSoundUri = Uri.parse(UriString);
                } else if (UriString.contains("android.resource://com.sec.android.app.clockpackage/raw/")) {
                    this.mSoundUri = Uri.parse(UriString);
                }
                if (this.mSoundUri == null) {
                    this.mSoundUri = this.mDefaultSoundUri;
                }
            }
        } else {
            this.mSoundUri = this.mDefaultSoundUri;
        }
        this.mTtsString = tts;
        this.mVibPattern = vib_pattern;
        Log.secD("AlarmPlayer", "mVibPattern = " + this.mVibPattern);
    }

    public void setVolume(float nVol) {
        if (this.mSoundPlayer != null && this.mSoundPlayer.isPlaying()) {
            Log.secD("AlarmPlayer", "mSoundPlayer setVolume : " + nVol);
            this.mMediaPlayerVolume = nVol;
            this.mSoundPlayer.setVolume(nVol, nVol);
        }
    }

    private void setBixbyBriefingVolume(float nVol) {
        if (this.mSoundBixbyBriefingPlayer != null && this.mSoundBixbyBriefingPlayer.isPlaying()) {
            Log.secD("AlarmPlayer", "mSoundBixbyBriefingPlayer setVolume : " + nVol);
            this.mBixbyBriefingVolume = nVol;
            this.mSoundBixbyBriefingPlayer.setVolume(nVol, nVol);
        }
    }

    public boolean isPause() {
        return this.mPause;
    }

    public void play() {
        if (this.mIsMute || this.mIsPalm) {
            if (this.mIsMute) {
                Log.secD("AlarmPlayer", "play - mIsMute is TRUE");
            }
            if (this.mIsPalm) {
                Log.secD("AlarmPlayer", "play - mIsPalm is TRUE");
            }
            stop();
        } else if (!StateUtils.isDndModeAlarmMuted(this.mContext)) {
            if (this.mPrevDndModeAlarmMuted) {
                this.mPrevDndModeAlarmMuted = false;
            }
            if (this.mCallState != null && this.mCallState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.secD("AlarmPlayer", "mCallState = " + this.mCallState);
                if (System.getInt(this.mContext.getContentResolver(), "alertoncall_mode", 1) != 1) {
                    stop();
                    return;
                }
            }
            Log.secD("AlarmPlayer", "play - PlayerMode = 0x" + Integer.toHexString(this.mPlayMode));
            if (this.mPlayMode == 0 || this.mPause || this.mIsFinishing) {
                if (this.mPause) {
                    Log.secD("AlarmPlayer", "mPause = true");
                }
                if (this.mIsFinishing) {
                    Log.secD("AlarmPlayer", "mIsFinishing = true");
                    return;
                }
                return;
            }
            if ((this.mPlayMode & 1) > 0) {
                if (this.mPlayMode == 3) {
                    playBeepSound();
                } else if (isPlaySoundOnJPNMannerMode(this.mContext)) {
                    playSound();
                }
            }
            if ((this.mPlayMode & 16) > 0 && this.mCallState != null) {
                if (this.mCallState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    stopVibration();
                } else {
                    playVibration();
                }
            }
            if ((this.mPlayMode & 256) <= 0) {
                return;
            }
            if (StateUtils.isMannerModeState(this.mContext)) {
                playVibration();
            } else if (getBixbyBriefing()) {
                playSound();
                playBixbyBriefing();
            } else {
                playVoice();
            }
        } else if (!this.mPrevDndModeAlarmMuted) {
            this.mPrevDndModeAlarmMuted = true;
            stop();
            this.mContext.sendBroadcast(new Intent("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION"));
        }
    }

    public void stop() {
        Log.secD("AlarmPlayer", "stop");
        if ((this.mPlayMode & 1) > 0 && !this.mPause) {
            stopSound();
        }
        if ((this.mPlayMode & 16) > 0) {
            stopVibration();
        }
        if ((this.mPlayMode & 256) > 0) {
            if (getBixbyBriefing()) {
                stopSound();
                stopBixbyBriefingOnly();
            } else {
                stopVoice();
            }
        }
        if (this.mVoicePlayer != null) {
            stopVoice();
        }
    }

    public void setAudioVolume(int vol) {
        this.mVolume = vol;
    }

    public void setEarphoneVib(int earphoneVib) {
        this.mIsInsertedEarphone = earphoneVib > 0;
    }

    public void setStreamAlarmVolume() {
        if (this.mAudioManager == null) {
            return;
        }
        if (StateUtils.isMannerModeState(this.mContext)) {
            this.mAudioAttribute = 1;
            this.mAudioStream = 3;
            if (this.mOldMusicVolume != this.mVolume) {
                this.mAudioManager.setStreamVolume(3, this.mVolume, 0);
                Log.m41d("AlarmPlayer", "setStreamAlarmVolume STREAM_MUSIC mVolume = " + this.mVolume);
                return;
            }
            return;
        }
        int curAlarmVolume = this.mAudioManager.getStreamVolume(4);
        this.mAudioAttribute = 4;
        this.mAudioStream = 4;
        if (curAlarmVolume != this.mVolume) {
            this.mAudioManager.setStreamVolume(4, this.mVolume, 0);
            Log.m41d("AlarmPlayer", "setStreamAlarmVolume STREAM_ALARM mVolume = " + this.mVolume);
        }
    }

    public int getStreamAlarmVolume() {
        if (this.mAudioManager != null) {
            return this.mAudioManager.getStreamVolume(4);
        }
        return -1;
    }

    public void pause() {
        this.mPause = true;
        Log.secD("AlarmPlayer", "pause");
        if (this.mSoundPlayer != null) {
            this.mSoundPlayer.setVolume(0.0f, 0.0f);
            this.mSoundPlayer.pause();
            if (!(this.mCallState == null || this.mCallState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) || this.mCallState.equals(TelephonyManager.EXTRA_STATE_RINGING))) {
                abandonAudioFocus();
            }
        }
        if (this.mSoundBixbyBriefingPlayer != null) {
            this.mSoundBixbyBriefingPlayer.setVolume(0.0f, 0.0f);
            this.mSoundBixbyBriefingPlayer.pause();
            if (!(this.mCallState == null || this.mCallState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) || this.mCallState.equals(TelephonyManager.EXTRA_STATE_RINGING))) {
                abandonAudioFocus();
            }
        }
        stop();
    }

    private void abandonAudioFocus() {
        if (this.mHasFocus) {
            if (this.mAudioManager != null) {
                this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
            }
            this.mHasFocus = false;
        }
    }

    public void resume(int count) {
        this.mPause = false;
        Log.secD("AlarmPlayer", "resume");
        if (count > 1) {
            Log.secD("AlarmPlayer", "resume - count is greater than 1");
            play();
        }
    }

    public void setCallState(String strValue) {
        Log.secD("AlarmPlayer", "setCallState strValue = " + strValue);
        this.mCallState = strValue;
    }

    public void release() {
        stopSound();
        stopVibration();
        stopVoice();
        stopBixbyBriefingOnly();
    }

    public boolean getEndAlertOnVoice() {
        return this.mEndAlertOnVoice;
    }

    public int getBixbyBriefingState() {
        return this.mBixbyBriefingState;
    }

    public void setBixbyBriefing(boolean type) {
        this.mIsBixbyBriefing = type;
    }

    public void setBriefingType(int type) {
        Log.secD("AlarmPlayer", "setBriefingType type = " + type);
        this.mBriefingType = type;
    }

    public boolean getBixbyBriefing() {
        return this.mIsBixbyBriefing;
    }

    public void setBixbyBriefingUri(Uri uri) {
        Log.secD("AlarmPlayer", "setBixbyBriefingUri uri = " + uri);
        this.mBixbyBriefingUri = uri;
    }

    public void setBixbyCelebrityVoicePath(String fullFilePath) {
        Log.secD("AlarmPlayer", "setBixbyCelebrityVoicePath fullFilePath = " + fullFilePath);
        this.mBixbyCelebrityVoiceFullPath = fullFilePath;
    }

    public void setBixbyCelebrityGreetingUri(Uri uri) {
        Log.secD("AlarmPlayer", "setBixbyCelebrityGreetingUri uri = " + uri);
        this.mBixbyCelebrityVGreetingVoiceUri = uri;
    }

    public void setBixbyBriefingState(int type) {
        this.mBixbyBriefingState = type;
    }

    public void setEndAlertOnVoice(boolean bAlertOnVoice) {
        this.mEndAlertOnVoice = bAlertOnVoice;
    }

    public void setNewCelebState(boolean state) {
        this.mIsNewCeleb = state;
    }

    private boolean getNewCelebState() {
        return this.mIsNewCeleb;
    }

    private void playBeepSound() {
        Log.secD("AlarmPlayer", "playBeepSound");
        if (this.mSoundPlayer == null && !this.mIsFinishing) {
            this.mSoundPlayer = new MediaPlayer();
            try {
                this.mSoundPlayer.setOnErrorListener(new C06162());
                if (this.mAudioManager != null) {
                    this.mSoundPlayer.setDataSource(this.mContext, Uri.parse("android.resource://com.sec.android.app.clockpackage/raw/s_alarms_in_call"));
                    if (this.mAudioManager.getMode() != 1) {
                        this.mSoundPlayer.setAudioAttributes(new Builder().setUsage(2).setContentType(4).build());
                        this.mSoundPlayer.setVolume(0.4f, 0.4f);
                        Log.secD("AlarmPlayer", "STREAM_VOICE_CALL = " + this.mAudioManager.getStreamVolume(0));
                    } else if (this.mAudioManager.getRingerMode() == 0 || this.mAudioManager.getRingerMode() == 1) {
                        this.mSoundPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                        this.mSoundPlayer.setVolume(0.6f, 0.6f);
                        Log.secD("AlarmPlayer", "STREAM_ALARM = " + this.mAudioManager.getStreamVolume(4));
                    } else {
                        this.mSoundPlayer.setAudioAttributes(new Builder().setUsage(5).setContentType(4).build());
                        Log.secD("AlarmPlayer", "STREAM_NOTIFICATION = " + this.mAudioManager.getStreamVolume(5));
                    }
                    this.mSoundPlayer.prepare();
                    this.mSoundPlayer.setLooping(false);
                    this.mSoundPlayer.start();
                    Log.m41d("AlarmPlayer", "playBeepSound mSoundPlayer.start");
                    Logger.m47f("AlarmPlayer", "beep");
                }
            } catch (IOException e) {
                Log.secE("AlarmPlayer", "playBeepSound Exception");
            } catch (IllegalArgumentException e2) {
                Log.secE("AlarmPlayer", "playBeepSound Exception");
            } catch (SecurityException e3) {
                Log.secE("AlarmPlayer", "playBeepSound Exception");
            } catch (IllegalStateException e4) {
                Log.secE("AlarmPlayer", "playBeepSound Exception");
            }
        } else if (this.mSoundPlayer != null && !this.mSoundPlayer.isPlaying()) {
            this.mSoundPlayer.setLooping(false);
            this.mSoundPlayer.start();
            Log.m41d("AlarmPlayer", "playBeepSound else mSoundPlayer.start");
            Logger.m47f("AlarmPlayer", "else beep");
        }
    }

    private void playBixbyBriefing() {
        Exception e;
        if (getBixbyBriefingState() != 1) {
            Log.secD("AlarmPlayer", "playBixbyBriefing return");
        } else if (this.mSoundBixbyBriefingPlayer == null) {
            this.mSoundBixbyBriefingPlayer = new MediaPlayer();
            this.mSoundBixbyBriefingPlayer.setOnErrorListener(new C06173());
            this.mSoundBixbyBriefingPlayer.setOnCompletionListener(new C06184());
            Log.secD("AlarmPlayer", "mSoundBixbyBriefingPlayer mBriefingType = " + this.mBriefingType);
            switch (this.mBriefingType) {
                case 2:
                    Log.m41d("AlarmPlayer", "mSoundBixbyBriefingPlayer mBixbyBriefingUri = " + this.mBixbyBriefingUri);
                    if (this.mBixbyBriefingUri != null) {
                        try {
                            this.mSoundBixbyBriefingPlayer.setDataSource(this.mContext, this.mBixbyBriefingUri);
                        } catch (IOException e2) {
                            e = e2;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                try {
                                    if (Feature.isSupportDualSpeaker()) {
                                        this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                    } else {
                                        Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                        this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                    }
                                    this.mSoundBixbyBriefingPlayer.prepare();
                                    this.mSoundBixbyBriefingPlayer.setLooping(false);
                                    this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                    this.mSoundBixbyBriefingPlayer.seekTo(0);
                                    this.mSoundBixbyBriefingPlayer.start();
                                    Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                    Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                                } catch (IOException e3) {
                                    e = e3;
                                    Log.secE("AlarmPlayer", "playBixbyBriefing Exception e = " + e.toString());
                                    finishBixbyBriefing();
                                    return;
                                } catch (IllegalStateException e4) {
                                    e = e4;
                                    Log.secE("AlarmPlayer", "playBixbyBriefing Exception e = " + e.toString());
                                    finishBixbyBriefing();
                                    return;
                                }
                            }
                        } catch (IllegalArgumentException e5) {
                            e = e5;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (SecurityException e6) {
                            e = e6;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalStateException e7) {
                            e = e7;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        }
                    }
                    finishBixbyBriefing();
                case 3:
                case 5:
                    if (this.mBixbyCelebrityGreetingVoiceFullPath != null) {
                        try {
                            setNewCelebVoiceState(1);
                            this.mSoundBixbyBriefingPlayer.setDataSource(this.mBixbyCelebrityGreetingVoiceFullPath);
                            Log.m41d("AlarmPlayer", "mSoundBixbyBriefingPlayer mBixbyCelebrityGreetingVoiceFullPath = " + this.mBixbyCelebrityGreetingVoiceFullPath);
                        } catch (IOException e8) {
                            e = e8;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalArgumentException e9) {
                            e = e9;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (SecurityException e10) {
                            e = e10;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalStateException e11) {
                            e = e11;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        }
                    }
                    Log.m41d("AlarmPlayer", "mSoundBixbyBriefingPlayer mBixbyCelebrityVoiceFullPath = " + this.mBixbyCelebrityVoiceFullPath);
                    if (this.mBixbyCelebrityVoiceFullPath != null) {
                        try {
                            this.mSoundBixbyBriefingPlayer.setDataSource(this.mBixbyCelebrityVoiceFullPath);
                        } catch (IOException e12) {
                            e = e12;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalArgumentException e13) {
                            e = e13;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (SecurityException e14) {
                            e = e14;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalStateException e15) {
                            e = e15;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        }
                    }
                    finishBixbyBriefing();
                case 4:
                    Log.m41d("AlarmPlayer", "mSoundBixbyBriefingPlayer mBixbyCelebrityVGreetingVoiceUri = " + this.mBixbyCelebrityVGreetingVoiceUri);
                    if (this.mBixbyCelebrityVGreetingVoiceUri != null) {
                        try {
                            this.mSoundBixbyBriefingPlayer.setDataSource(this.mContext, this.mBixbyCelebrityVGreetingVoiceUri);
                        } catch (IOException e16) {
                            e = e16;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalArgumentException e17) {
                            e = e17;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (SecurityException e18) {
                            e = e18;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                } else {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        } catch (IllegalStateException e19) {
                            e = e19;
                            Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException e = " + e.toString());
                            finishBixbyBriefing();
                            if (this.mSoundBixbyBriefingPlayer != null) {
                                if (Feature.isSupportDualSpeaker()) {
                                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                                } else {
                                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                                }
                                this.mSoundBixbyBriefingPlayer.prepare();
                                this.mSoundBixbyBriefingPlayer.setLooping(false);
                                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                                this.mSoundBixbyBriefingPlayer.seekTo(0);
                                this.mSoundBixbyBriefingPlayer.start();
                                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
                            }
                        }
                    }
                    finishBixbyBriefing();
            }
            if (this.mSoundBixbyBriefingPlayer != null) {
                if (Feature.isSupportDualSpeaker()) {
                    Log.secD("AlarmPlayer", "isSupportDualSpeaker");
                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
                } else {
                    this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
                }
                this.mSoundBixbyBriefingPlayer.prepare();
                this.mSoundBixbyBriefingPlayer.setLooping(false);
                this.mSoundBixbyBriefingPlayer.setVolume(1.0f, 1.0f);
                this.mSoundBixbyBriefingPlayer.seekTo(0);
                this.mSoundBixbyBriefingPlayer.start();
                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer.start");
                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(4));
            }
        } else if (!this.mSoundBixbyBriefingPlayer.isPlaying()) {
            Log.secE("AlarmPlayer", "!mSoundBixbyBriefingPlayer.isPlaying()");
            if (StateUtils.isSoundModeOnForJapan(this.mContext)) {
                this.mSoundBixbyBriefingPlayer.setAudioAttributes(new Builder().setUsage(4).setContentType(4).build());
            }
            this.mSoundBixbyBriefingPlayer.setLooping(false);
            if (this.mIsMute) {
                this.mSoundBixbyBriefingPlayer.setVolume(0.0f, 0.0f);
            } else {
                this.mSoundBixbyBriefingPlayer.setVolume(this.mBixbyBriefingVolume, this.mBixbyBriefingVolume);
            }
            try {
                this.mSoundBixbyBriefingPlayer.start();
                Log.m41d("AlarmPlayer", "playBixbyBriefing mSoundBixbyBriefingPlayer != null mSoundBixbyBriefingPlayer.start");
            } catch (IllegalStateException e20) {
                Log.secE("AlarmPlayer", "playBixbyBriefing !mSoundBixbyBriefingPlayer Exception e = " + e20.toString());
                finishBixbyBriefing();
            }
        }
    }

    private void finishBixbyBriefing() {
        Log.secD("AlarmPlayer", "finishBixbyBriefing");
        stopBixbyBriefingOnly();
        setBixbyBriefingState(2);
        setBixbyBriefing(false);
    }

    private void playSound() {
        Exception e2;
        int offset;
        RuntimeException e;
        Log.secD("AlarmPlayer", "playSound");
        AudioAttributes alarmPlayAttributes = new Builder().setLegacyStreamType(this.mAudioStream).setUsage(this.mAudioAttribute).setContentType(4).build();
        AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(2).setAudioAttributes(alarmPlayAttributes).setOnAudioFocusChangeListener(this.mAudioFocusListener).build();
        if (this.mSoundUri != null && this.mSoundUri.toString().equals("alarm_silent_ringtone")) {
            Log.secD("AlarmPlayer", "playSound : Silent alarm tone");
            if (!this.mHasFocus) {
                int result = this.mAudioManager.requestAudioFocus(focusRequest);
                Log.m41d("AlarmPlayer", "playSound ALARM_SILENT_RINGTONE requestAudioFocus result = " + result);
                if (result == 1) {
                    this.mHasFocus = true;
                }
            }
        } else if ((this.mSoundPlayer == null && !this.mIsFinishing) || (this.mSoundPlayer != null && !this.mSoundPlayer.isLooping())) {
            if (this.mSoundPlayer == null) {
                this.mSoundPlayer = new MediaPlayer();
            }
            setStreamAlarmVolume();
            Log.secD("AlarmPlayer", "mSoundPlayer.isLooping() = " + this.mSoundPlayer.isLooping());
            this.mSoundPlayer.setOnErrorListener(new C06195());
            if (!(getBixbyBriefing() || !Feature.isDCM(this.mContext) || getNewCelebState())) {
                DrmManagerClient drmClient = new DrmManagerClient(this.mContext);
                try {
                    if (drmClient.canHandle(this.mSoundUri, null)) {
                        Log.secE("AlarmPlayer", "canHandle is true");
                        if (drmClient.checkRightsStatus(this.mSoundUri, 2) != 0) {
                            Log.m42e("AlarmPlayer", "getRingtone() : PR DRM File Ringtone Rights invalid !!!");
                            this.mSoundUri = this.mDefaultSoundUri;
                        }
                    }
                } catch (IllegalArgumentException e3) {
                    this.mSoundUri = this.mDefaultSoundUri;
                    Log.secE("AlarmPlayer", "IllegalArgumentException Exception mSoundUri = " + this.mSoundUri + " e = " + e3.toString());
                }
            }
            try {
                this.mSoundPlayer.setDataSource(this.mContext, this.mSoundUri);
            } catch (FileNotFoundException e4) {
                this.mSoundUri = this.mDefaultSoundUri;
                Log.secE("AlarmPlayer", "setOnErrorListener FileNotFoundException mSoundUri = " + this.mSoundUri);
            } catch (IOException e5) {
                try {
                    if (!StateUtils.isDirectBootMode(this.mContext) || StateUtils.isUserUnlockedDevice(this.mContext)) {
                        this.mSoundUri = this.mDefaultSoundUri;
                    } else {
                        this.mSoundUri = Uri.parse("content://settings/system/alram_sound");
                    }
                    this.mSoundPlayer.setDataSource(this.mContext, this.mSoundUri);
                } catch (IOException e6) {
                    e2 = e6;
                    Log.secE("AlarmPlayer", "setOnErrorListener IOException mSoundUri = " + this.mSoundUri + " e2 = " + e2.toString());
                    this.mSoundPlayer.setAudioAttributes(alarmPlayAttributes);
                    this.mSoundPlayer.prepare();
                    this.mSoundPlayer.setLooping(true);
                    if (!this.mIsMute) {
                    }
                    this.mSoundPlayer.setVolume(0.0f, 0.0f);
                    offset = getRecommendedRingtoneOffset(this.mSoundUri);
                    Log.secD("AlarmPlayer", "mSoundPosition : " + this.mSoundPosition);
                    if (this.mSoundPosition == 0) {
                        this.mSoundPlayer.seekTo(this.mSoundPosition);
                    } else {
                        this.mSoundPlayer.seekTo(offset);
                    }
                    if (this.mAudioManager.requestAudioFocus(focusRequest) == 1) {
                        this.mSoundPlayer.start();
                        Log.m41d("AlarmPlayer", "playSound mSoundPlayer.start");
                        Logger.m47f("AlarmPlayer", "mSoundPlayer.start");
                        this.mHasFocus = true;
                    }
                    Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(this.mAudioStream));
                } catch (IllegalArgumentException e7) {
                    e2 = e7;
                    Log.secE("AlarmPlayer", "setOnErrorListener IOException mSoundUri = " + this.mSoundUri + " e2 = " + e2.toString());
                    this.mSoundPlayer.setAudioAttributes(alarmPlayAttributes);
                    this.mSoundPlayer.prepare();
                    this.mSoundPlayer.setLooping(true);
                    if (this.mIsMute) {
                    }
                    this.mSoundPlayer.setVolume(0.0f, 0.0f);
                    offset = getRecommendedRingtoneOffset(this.mSoundUri);
                    Log.secD("AlarmPlayer", "mSoundPosition : " + this.mSoundPosition);
                    if (this.mSoundPosition == 0) {
                        this.mSoundPlayer.seekTo(offset);
                    } else {
                        this.mSoundPlayer.seekTo(this.mSoundPosition);
                    }
                    if (this.mAudioManager.requestAudioFocus(focusRequest) == 1) {
                        this.mSoundPlayer.start();
                        Log.m41d("AlarmPlayer", "playSound mSoundPlayer.start");
                        Logger.m47f("AlarmPlayer", "mSoundPlayer.start");
                        this.mHasFocus = true;
                    }
                    Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(this.mAudioStream));
                } catch (SecurityException e8) {
                    e2 = e8;
                    Log.secE("AlarmPlayer", "setOnErrorListener IOException mSoundUri = " + this.mSoundUri + " e2 = " + e2.toString());
                    this.mSoundPlayer.setAudioAttributes(alarmPlayAttributes);
                    this.mSoundPlayer.prepare();
                    this.mSoundPlayer.setLooping(true);
                    if (this.mIsMute) {
                    }
                    this.mSoundPlayer.setVolume(0.0f, 0.0f);
                    offset = getRecommendedRingtoneOffset(this.mSoundUri);
                    Log.secD("AlarmPlayer", "mSoundPosition : " + this.mSoundPosition);
                    if (this.mSoundPosition == 0) {
                        this.mSoundPlayer.seekTo(this.mSoundPosition);
                    } else {
                        this.mSoundPlayer.seekTo(offset);
                    }
                    if (this.mAudioManager.requestAudioFocus(focusRequest) == 1) {
                        this.mSoundPlayer.start();
                        Log.m41d("AlarmPlayer", "playSound mSoundPlayer.start");
                        Logger.m47f("AlarmPlayer", "mSoundPlayer.start");
                        this.mHasFocus = true;
                    }
                    Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(this.mAudioStream));
                } catch (IllegalStateException e9) {
                    e2 = e9;
                    Log.secE("AlarmPlayer", "setOnErrorListener IOException mSoundUri = " + this.mSoundUri + " e2 = " + e2.toString());
                    this.mSoundPlayer.setAudioAttributes(alarmPlayAttributes);
                    this.mSoundPlayer.prepare();
                    this.mSoundPlayer.setLooping(true);
                    if (this.mIsMute) {
                    }
                    this.mSoundPlayer.setVolume(0.0f, 0.0f);
                    offset = getRecommendedRingtoneOffset(this.mSoundUri);
                    Log.secD("AlarmPlayer", "mSoundPosition : " + this.mSoundPosition);
                    if (this.mSoundPosition == 0) {
                        this.mSoundPlayer.seekTo(offset);
                    } else {
                        this.mSoundPlayer.seekTo(this.mSoundPosition);
                    }
                    if (this.mAudioManager.requestAudioFocus(focusRequest) == 1) {
                        this.mSoundPlayer.start();
                        Log.m41d("AlarmPlayer", "playSound mSoundPlayer.start");
                        Logger.m47f("AlarmPlayer", "mSoundPlayer.start");
                        this.mHasFocus = true;
                    }
                    Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(this.mAudioStream));
                }
            } catch (IllegalArgumentException e10) {
                e = e10;
                this.mSoundUri = this.mDefaultSoundUri;
                Log.secE("AlarmPlayer", "setOnErrorListener Exception mSoundUri = " + this.mSoundUri + " e = " + e.toString());
            } catch (SecurityException e11) {
                e = e11;
                this.mSoundUri = this.mDefaultSoundUri;
                Log.secE("AlarmPlayer", "setOnErrorListener Exception mSoundUri = " + this.mSoundUri + " e = " + e.toString());
            } catch (IllegalStateException e12) {
                e = e12;
                this.mSoundUri = this.mDefaultSoundUri;
                Log.secE("AlarmPlayer", "setOnErrorListener Exception mSoundUri = " + this.mSoundUri + " e = " + e.toString());
            }
            try {
                this.mSoundPlayer.setAudioAttributes(alarmPlayAttributes);
                this.mSoundPlayer.prepare();
                this.mSoundPlayer.setLooping(true);
                if (this.mIsMute || this.mIsIncreasedAlarm) {
                    this.mSoundPlayer.setVolume(0.0f, 0.0f);
                } else if (getBixbyBriefingState() == 1) {
                    this.mSoundPlayer.setVolume(0.1f, 0.1f);
                } else {
                    this.mSoundPlayer.setVolume(1.0f, 1.0f);
                }
                offset = getRecommendedRingtoneOffset(this.mSoundUri);
                Log.secD("AlarmPlayer", "mSoundPosition : " + this.mSoundPosition);
                if (this.mSoundPosition == 0) {
                    this.mSoundPlayer.seekTo(offset);
                } else {
                    this.mSoundPlayer.seekTo(this.mSoundPosition);
                }
                if (this.mAudioManager.requestAudioFocus(focusRequest) == 1) {
                    this.mSoundPlayer.start();
                    Log.m41d("AlarmPlayer", "playSound mSoundPlayer.start");
                    Logger.m47f("AlarmPlayer", "mSoundPlayer.start");
                    this.mHasFocus = true;
                }
                Log.secD("AlarmPlayer", "melody getStreamVolume : " + ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(this.mAudioStream));
            } catch (Exception e13) {
                Log.secE("AlarmPlayer", "playSound Exception " + e13.toString());
                this.mSoundPlayer.release();
                this.mSoundPlayer = null;
                abandonAudioFocus();
                this.mSoundUri = this.mDefaultSoundUri;
            }
        } else if (this.mSoundPlayer != null && !this.mSoundPlayer.isPlaying()) {
            if (StateUtils.isMannerModeState(this.mContext)) {
                this.mSoundPlayer.setAudioAttributes(alarmPlayAttributes);
            }
            this.mSoundPlayer.setLooping(true);
            if (this.mIsMute) {
                this.mSoundPlayer.setVolume(0.0f, 0.0f);
            } else {
                setStreamAlarmVolume();
                this.mSoundPlayer.setVolume(this.mMediaPlayerVolume, this.mMediaPlayerVolume);
            }
            if (this.mAudioManager.requestAudioFocus(focusRequest) == 1) {
                this.mSoundPlayer.start();
                Log.m41d("AlarmPlayer", "playSound else mSoundPlayer.start");
                Logger.m47f("AlarmPlayer", "else mSoundPlayer.start");
                this.mHasFocus = true;
            }
        }
    }

    public void resetVibrate() {
        Log.secD("AlarmPlayer", "resetVibrate");
        if (sAlarmVibrator != null) {
            sAlarmVibrator.cancel();
        }
        sAlarmVibrator = null;
    }

    private Vibrator getAlarmVibrator() {
        Log.secD("AlarmPlayer", "getAlarmVibrator");
        if (sAlarmVibrator == null) {
            synchronized (sLock) {
                sAlarmVibrator = (Vibrator) this.mContext.getSystemService("vibrator");
            }
        } else {
            Log.secD("AlarmPlayer", "sAlarmVibrator instance already exist");
        }
        return sAlarmVibrator;
    }

    private void playVibration() {
        Log.secD("AlarmPlayer", "playVibration");
        if (sAlarmVibrator == null && !this.mIsFinishing) {
            AudioAttributes attributes = new Builder().setUsage(4).build();
            if (VERSION.SEM_INT >= 2701) {
                getAlarmVibrator().vibrate(VibrationEffect.semCreateWaveform(this.mVibPattern, 0, SemMagnitudeType.TYPE_MAX), attributes);
            } else {
                getAlarmVibrator().semVibrate(this.mVibPattern, 0, attributes, SemMagnitudeTypes.TYPE_MAX);
            }
            AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(2).setAudioAttributes(attributes).setOnAudioFocusChangeListener(this.mAudioFocusListener).build();
            Log.secD("AlarmPlayer", "playVibration " + sAlarmVibrator.toString());
            if (this.mAudioManager != null && (((this.mPlayMode & 1) == 0 || !isPlaySoundOnJPNMannerMode(this.mContext)) && !StateUtils.isRecordingState(this.mContext))) {
                int result = this.mAudioManager.requestAudioFocus(focusRequest);
                Log.secD("AlarmPlayer", "playVibration requestAudioFocus result = " + result);
                if (result == 1) {
                    this.mHasFocus = true;
                }
            } else if (this.mAudioManager != null) {
                Log.secD("AlarmPlayer", "playVibration mAudioManager != null");
            }
        }
    }

    private void setCompleteListener() {
        if (this.mVoicePlayer != null) {
            this.mVoicePlayer.setOnUtteranceProgressListener(new C06206());
        }
    }

    private void doSpeak() {
        Log.secD("AlarmPlayer", "doSpeak");
        if (StateUtils.isMannerModeState(this.mContext) && !this.mIsInsertedEarphone) {
            stopVoiceOnly();
        }
        if (this.mIsMute) {
            stopVoiceOnly();
        }
        if (this.mVoicePlayer != null && !this.mVoicePlayer.isSpeaking()) {
            this.mEndAlertOnVoice = false;
            int usage = this.mIsInsertedEarphone ? 1 : 4;
            Log.m41d("AlarmPlayer", " mIsInsertedEarphone = " + this.mIsInsertedEarphone + "usage : " + usage);
            this.mVoicePlayer.setAudioAttributes(new Builder().setUsage(usage).setContentType(4).semAddAudioTag("NOFADE").build());
            if (Feature.isTablet(this.mContext)) {
                Locale currentLocale;
                if (this.mVoicePlayer.getVoice() == null) {
                    currentLocale = null;
                } else {
                    currentLocale = this.mVoicePlayer.getVoice().getLocale();
                }
                if (currentLocale == null) {
                    int languageResult = this.mVoicePlayer.setLanguage(Locale.getDefault());
                    if (languageResult == -2 || languageResult == -1) {
                        this.mVoicePlayer.setLanguage(Locale.US);
                    }
                }
            } else {
                String defaultLanguage;
                String defaultCountry = Locale.getDefault().getCountry();
                String defaultLocVariant = Locale.getDefault().getVariant();
                try {
                    defaultLanguage = Locale.getDefault().getISO3Language();
                } catch (MissingResourceException e) {
                    Log.m42e("AlarmPlayer", "MissingResourceException : " + e);
                    defaultLanguage = Locale.getDefault().getLanguage();
                }
                if ("MO".equals(defaultCountry)) {
                    defaultCountry = "HK";
                }
                if (defaultLanguage.equals("kor")) {
                    if (this.mVoicePlayer.isLanguageAvailable(new Locale(defaultLanguage, defaultCountry, "f02")) >= 0) {
                        defaultLocVariant = "f02";
                        Log.secD("AlarmPlayer", "defaultLocVariant = f02");
                    }
                }
                if (this.mVoicePlayer.setLanguage(new Locale(defaultLanguage, defaultCountry, defaultLocVariant)) < 0) {
                    this.mVoicePlayer.setLanguage(Locale.US);
                }
            }
            this.mTtsResult = this.mVoicePlayer.speak(this.mTtsString, 0, null, "alarm_name_string");
        }
    }

    private void playVoice() {
        Log.secD("AlarmPlayer", "playVoice");
        if (this.mVoicePlayer == null && !this.mIsFinishing) {
            this.mVoicePlayer = new TextToSpeech(this.mContext, new C06217());
        }
    }

    private void stopSound() {
        Log.secD("AlarmPlayer", "stopSound");
        if (this.mSoundPlayer != null) {
            stopSoundOnly();
            abandonAudioFocus();
        } else if (Alarm.isStopAlarmAlert) {
            abandonAudioFocus();
        }
    }

    private void stopSoundOnly() {
        if (this.mSoundPlayer != null) {
            try {
                this.mSoundPosition = this.mSoundPlayer.getCurrentPosition();
                Log.secD("AlarmPlayer", "stopSoundOnly mSoundPosition = " + this.mSoundPosition);
                this.mSoundPlayer.setVolume(0.0f, 0.0f);
                this.mSoundPlayer.pause();
                this.mSoundPlayer.stop();
                this.mSoundPlayer.reset();
                this.mSoundPlayer.release();
            } catch (Exception e) {
                Log.secE("AlarmPlayer", "stopSoundOnly Exception");
            }
            this.mSoundPlayer = null;
        }
    }

    private void stopBixbyBriefingOnly() {
        if (this.mSoundBixbyBriefingPlayer != null) {
            try {
                Log.secD("AlarmPlayer", "stopBixbyBriefingOnly");
                this.mSoundBixbyBriefingPlayer.setVolume(0.0f, 0.0f);
                this.mSoundBixbyBriefingPlayer.pause();
                this.mSoundBixbyBriefingPlayer.stop();
                this.mSoundBixbyBriefingPlayer.reset();
                this.mSoundBixbyBriefingPlayer.release();
            } catch (Exception e) {
                Log.secE("AlarmPlayer", "stopBixbyBriefingOnly Exception");
            }
            this.mSoundBixbyBriefingPlayer = null;
        }
    }

    private void stopVibration() {
        Log.secD("AlarmPlayer", "stopVibration");
        stopVibrationOnly();
        if ((Alarm.isStopAlarmAlert && !this.mCallState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && !this.mCallState.equals(TelephonyManager.EXTRA_STATE_RINGING)) || this.mIsPalm) {
            abandonAudioFocus();
        }
    }

    private void stopVibrationOnly() {
        Log.secD("AlarmPlayer", "stopVibrationOnly");
        resetVibrate();
    }

    private void stopVoice() {
        Log.secD("AlarmPlayer", "stopVoice");
        stopVoiceOnly();
        if (Alarm.isStopAlarmAlert) {
            abandonAudioFocus();
        }
    }

    private void stopVoiceOnly() {
        if (this.mVoicePlayer != null) {
            if (this.mIsMute || this.mTtsResult != 0) {
                this.mEndAlertOnVoice = true;
            }
            try {
                this.mVoicePlayer.setOnUtteranceProgressListener(null);
                this.mVoicePlayer.stop();
                this.mVoicePlayer.shutdown();
            } catch (IllegalArgumentException e) {
                Log.secE("AlarmPlayer", "stopSoundOnly IllegalArgumentException");
            } catch (NullPointerException e2) {
                Log.secE("AlarmPlayer", "stopSoundOnly NullPointerException");
            } finally {
                this.mVoicePlayer = null;
            }
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case 16384:
                Log.m41d("AlarmPlayer", "case TTS_INIT_FAILED");
                playVoice();
                return;
            case 16385:
                Log.m41d("AlarmPlayer", "case TTS_INIT_SUCCESS");
                setCompleteListener();
                doSpeak();
                return;
            default:
                return;
        }
    }

    public void setIsFinishing() {
        if (StateUtils.isMannerModeState(this.mContext) && this.mIsInsertedEarphone && this.mAudioManager != null) {
            this.mAudioManager.setStreamVolume(3, this.mOldMusicVolume, 0);
            Log.m41d("AlarmPlayer", "setIsFinishing setStreamVolume STREAM_MUSIC mOldMusicVolume = " + this.mOldMusicVolume);
            this.mOldMusicVolume = 0;
            this.mIsInsertedEarphone = false;
        }
        if (this.mAudioManager != null) {
            this.mAudioManager = null;
        }
        if (!Feature.isTablet(this.mContext) && Feature.isMotionSupported()) {
            unregisterMotionSensorManager();
        }
        if (this.mMotionListener != null) {
            this.mMotionListener = null;
        }
        this.mIsFinishing = true;
    }

    public void onPause() {
        if (!Feature.isTablet(this.mContext) && Feature.isMotionSupported()) {
            unregisterMotionSensorManager();
        }
        if (StateUtils.isInCallState(this.mContext)) {
            this.mIsMute = true;
        }
        saveAlarmVolume();
    }

    public void onResume() {
        if (!Feature.isTablet(this.mContext) && Feature.isMotionSupported()) {
            registerMotionSensorManager();
        }
        this.mIsMute = false;
        Log.secD("AlarmPlayer", "onResume mIsMute = false");
    }

    private void saveAlarmVolume() {
        if (this.mAudioManager == null) {
            return;
        }
        if (StateUtils.isMannerModeState(this.mContext) && this.mIsInsertedEarphone) {
            int curMusicVolume = this.mAudioManager.getStreamVolume(3);
            if (curMusicVolume != this.mVolume) {
                this.mVolume = curMusicVolume;
                Log.secD("AlarmPlayer", "saveMusicVolume mVolume = " + this.mVolume);
                return;
            }
            return;
        }
        int curAlarmVolume = this.mAudioManager.getStreamVolume(4);
        if (this.mVolume != curAlarmVolume) {
            this.mVolume = curAlarmVolume;
            Log.secD("AlarmPlayer", "saveAlarmVolume mVolume = " + this.mVolume);
        }
    }

    public void setIncreasedAlarm(boolean bValue) {
        this.mIsIncreasedAlarm = bValue;
        Log.secD("AlarmPlayer", "setIncreasedAlarm mIsIncreasedAlarm = " + this.mIsIncreasedAlarm);
    }

    public boolean getIncreasedAlarm() {
        Log.secD("AlarmPlayer", "getIncreasedAlarm mIsIncreasedAlarm = " + this.mIsIncreasedAlarm);
        return this.mIsIncreasedAlarm;
    }

    private int getRecommendedRingtoneOffset(Uri tone) {
        String SeekToMillis = tone.getQueryParameter("highlight_offset");
        if (SeekToMillis == null) {
            return 0;
        }
        int offset = Integer.parseInt(SeekToMillis);
        Log.m41d("AlarmPlayer", "getRecommendedRingtoneOffset offset = " + offset);
        return offset;
    }

    public boolean getPalm() {
        return this.mIsPalm;
    }

    public void setPalm(boolean IsPalm) {
        this.mIsPalm = IsPalm;
    }

    public boolean getBehindTimerState() {
        return this.mIsHideByTimer;
    }

    public void setBehindTimerState(boolean hidden) {
        Log.secD("AlarmPlayer", "setBehindTimerState hidden = " + hidden);
        this.mIsHideByTimer = hidden;
    }

    public void resumeAndSetVolume(int count, float curVol) {
        onResume();
        resume(count);
        if (getBixbyBriefingState() != 1) {
            setVolume(curVol);
        }
    }

    public boolean hasGreetingInNewCeleb() {
        return this.hasGreetingInNewCeleb;
    }

    private void setNewCelebVoiceState(int state) {
        this.newCelebVoiceState = state;
    }

    private int getNewCelebVoiceState() {
        return this.newCelebVoiceState;
    }

    private boolean isPlaySoundOnJPNMannerMode(Context context) {
        return this.mIsInsertedEarphone || !StateUtils.isMannerModeState(this.mContext);
    }
}
