package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.Context;
import android.drm.DrmManagerClient;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.VibrationEffect.SemMagnitudeType;
import android.os.Vibrator;
import android.os.Vibrator.SemMagnitudeTypes;
import android.support.v4.provider.FontsContractCompat.FontRequestCallback;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import com.sec.android.app.clockpackage.timer.callback.TimerAlertPlayerListener;
import java.io.IOException;
import java.util.Calendar;

public class TimerAlertPlayer {
    private static Object sLock = new Object();
    private static Vibrator sTimerVibrator;
    private OnAudioFocusChangeListener mAudioFocusListener = new C07722();
    private AudioFocusRequest mAudioFocusRequest = null;
    private AudioManager mAudioManager;
    private Context mContext;
    private int mCurrentAudioFocus = 0;
    private boolean mIsPlaying = false;
    private boolean mIsStreamMusicModeForJapan = false;
    private boolean mIsVibrating = false;
    private MediaPlayer mMediaPlayer;
    private boolean mNeedReplay = false;
    private boolean mPlayMute = false;
    private boolean mPlayVib = false;
    private int mSoundPosition = 0;
    private Uri mSoundUri;
    private int mStreamType = 4;
    private TimerAlertPlayerListener mTimerAlertPlayerListener;
    private TimerManager mTimerManager = null;
    private int mUsageType = 4;
    private int mVolumeValue = 11;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlertPlayer$1 */
    class C07711 implements Runnable {
        C07711() {
        }

        public void run() {
            if (StateUtils.isRecordingState(TimerAlertPlayer.this.mContext)) {
                Log.secD("TimerAlertPlayer", "isRecording : true");
                return;
            }
            Log.secD("TimerAlertPlayer", "startVibrate run()");
            if (TimerAlertPlayer.this.mAudioManager != null) {
                int result = TimerAlertPlayer.this.mAudioManager.requestAudioFocus(TimerAlertPlayer.this.mAudioFocusRequest);
                if (result == 1) {
                    TimerAlertPlayer.this.mCurrentAudioFocus = 1;
                    Log.secD("TimerAlertPlayer", "startVibrate : result = " + result);
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAlertPlayer$2 */
    class C07722 implements OnAudioFocusChangeListener {
        C07722() {
        }

        public void onAudioFocusChange(int focusChange) {
            Log.secD("TimerAlertPlayer", "onAudioFocusChange - focusChange : " + focusChange);
            if (TimerAlertPlayer.this.mCurrentAudioFocus != focusChange) {
                TimerAlertPlayer.this.mCurrentAudioFocus = focusChange;
                switch (focusChange) {
                    case FontRequestCallback.FAIL_REASON_FONT_LOAD_ERROR /*-3*/:
                        TimerAlertPlayer.this.mVolumeValue = TimerAlertPlayer.this.mAudioManager.getStreamVolume(TimerAlertPlayer.this.mStreamType);
                        return;
                    case -2:
                    case -1:
                        if (TimerAlertPlayer.this.mIsPlaying || TimerAlertPlayer.this.mIsVibrating) {
                            TimerAlertPlayer.this.stop(false);
                        }
                        try {
                            Thread.sleep(150);
                            return;
                        } catch (InterruptedException e) {
                            Log.secE("TimerAlertPlayer", "InterruptedException : " + e.toString());
                            return;
                        }
                    case 1:
                        if (TimerAlertPlayer.this.mMediaPlayer == null) {
                            TimerAlertPlayer.this.play();
                            TimerAlertPlayer.this.setVolume();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    TimerAlertPlayer(Context context, TimerAlertPlayerListener timerAlertPlayerListener) {
        this.mContext = context;
        this.mTimerAlertPlayerListener = timerAlertPlayerListener;
        buildAudioFocusRequest();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mNeedReplay = false;
        this.mTimerManager = TimerManager.getInstance();
        this.mTimerManager.setContext(this.mContext);
        initTimerSoundInfo();
    }

    public boolean isPlaying() {
        return this.mIsPlaying;
    }

    public boolean isVibrating() {
        return this.mIsVibrating;
    }

    public boolean isAudioManagerNormalMode() {
        return this.mAudioManager != null && this.mAudioManager.getMode() == 0;
    }

    public AudioManager getAudioManager() {
        return this.mAudioManager;
    }

    public boolean isNeedReplay() {
        return this.mNeedReplay;
    }

    public void setNeedReplay(boolean bNeedReplay) {
        this.mNeedReplay = bNeedReplay;
    }

    public boolean isAudioFocusGain() {
        return this.mCurrentAudioFocus == 1;
    }

    public void resetCurrentAudioFocus() {
        this.mCurrentAudioFocus = 0;
    }

    public synchronized void play() {
        int i = 1;
        synchronized (this) {
            Log.secD("TimerAlertPlayer", "play : mNeedReplay = " + this.mNeedReplay);
            if (this.mNeedReplay) {
                stop(false);
            }
            if (!(StateUtils.isDndModeAlarmMuted(this.mContext) || StateUtils.isInCallState(this.mContext.getApplicationContext()))) {
                if (StateUtils.isRecordingState(this.mContext)) {
                    Log.secD("TimerAlertPlayer", "isRecordingState : true");
                    boolean isTopActivity = this.mTimerManager.isTimerAlarmTopActivity();
                    if (StateUtils.needToShowAsFullScreen(this.mContext.getApplicationContext()) || isTopActivity) {
                        startVibrate(false);
                    }
                } else if (ClockUtils.timerAlertTimeInCall <= 0 || ClockUtils.timerAlertTimeInCall >= ClockUtils.alarmAlertTimeInCall) {
                    this.mStreamType = this.mIsStreamMusicModeForJapan ? 3 : 4;
                    if (!this.mIsStreamMusicModeForJapan) {
                        i = 4;
                    }
                    this.mUsageType = i;
                    buildAudioFocusRequest();
                    if (this.mPlayVib) {
                        startVibrate(true);
                    }
                    startRing();
                } else {
                    Log.secD("TimerAlertPlayer", "Timer is mute because alarm is ringing after call");
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(ClockUtils.alarmAlertTimeInCall);
                    Log.secD("TimerAlertPlayer", "alarmAlertTimeInCall : " + cal.getTime().toString());
                    cal.setTimeInMillis(ClockUtils.timerAlertTimeInCall);
                    Log.secD("TimerAlertPlayer", "timerAlertTimeInCall : " + cal.getTime().toString());
                    this.mNeedReplay = true;
                    stop(true);
                    this.mTimerAlertPlayerListener.onSetIsHideByAlarm(true);
                }
            }
        }
    }

    public void stop(boolean isAbandonAudioFocus) {
        Log.secD("TimerAlertPlayer", "stop :isAbandonAudioFocus = " + isAbandonAudioFocus);
        stopVibrate();
        try {
            stopRing();
        } catch (Exception e) {
            Log.secE("TimerAlertPlayer", "Exception : " + e.toString());
        }
        if (isAbandonAudioFocus) {
            this.mAudioManager.abandonAudioFocusRequest(this.mAudioFocusRequest);
        }
    }

    public void updateVolume() {
        Log.secD("TimerAlertPlayer", "updateVolume()");
        this.mVolumeValue = this.mAudioManager.getStreamVolume(this.mStreamType);
        this.mAudioManager.setStreamVolume(this.mStreamType, this.mVolumeValue, 0);
    }

    private void startVibrate(boolean requestAudioFocus) {
        Log.secD("TimerAlertPlayer", "startVibrate :requestAudioFocus = " + requestAudioFocus);
        AudioAttributes attributes = new Builder().setUsage(4).build();
        long[] vibratorPattern = new long[]{0, 400, 200, 100, 400, 100, 400};
        if (VERSION.SEM_INT >= 2701) {
            VibrationEffect vibeEffect = VibrationEffect.createWaveform(vibratorPattern, 0);
            vibeEffect.semSetMagnitudeType(SemMagnitudeType.TYPE_MAX);
            getTimerVibrator().vibrate(vibeEffect, attributes);
        } else {
            getTimerVibrator().semVibrate(vibratorPattern, 0, attributes, SemMagnitudeTypes.TYPE_MAX);
        }
        this.mIsVibrating = true;
        if (requestAudioFocus) {
            new Handler().postDelayed(new C07711(), 100);
        }
    }

    private void startRing() {
        Exception e;
        Log.secD("TimerAlertPlayer", "startRing");
        if (this.mAudioManager.requestAudioFocus(this.mAudioFocusRequest) == 1) {
            this.mCurrentAudioFocus = 1;
            if (this.mMediaPlayer == null) {
                try {
                    this.mIsPlaying = true;
                    if (!this.mPlayMute) {
                        if (!StateUtils.isMannerModeState(this.mContext) || this.mIsStreamMusicModeForJapan) {
                            this.mMediaPlayer = new MediaPlayer();
                            this.mMediaPlayer.setAudioAttributes(new Builder().setLegacyStreamType(this.mStreamType).setUsage(this.mUsageType).setContentType(4).build());
                            if (Feature.isDCM(this.mContext.getApplicationContext())) {
                                DrmManagerClient drmClient = new DrmManagerClient(this.mContext.getApplicationContext());
                                if (drmClient.canHandle(this.mSoundUri, null)) {
                                    Log.secE("TimerAlertPlayer", "can handle is true");
                                    if (drmClient.checkRightsStatus(this.mSoundUri, 2) != 0) {
                                        Log.secE("TimerAlertPlayer", "Rights not valid");
                                        Log.secE("TimerAlertPlayer", "getRingtone() : PR DRM File Ringtone Rights invalid !!!");
                                        this.mSoundUri = TimerManager.getDefaultRingtoneUri(this.mContext);
                                    }
                                }
                            }
                            this.mMediaPlayer.setLooping(true);
                            this.mMediaPlayer.setDataSource(this.mContext, this.mSoundUri);
                            setVolume();
                            this.mMediaPlayer.prepare();
                            int offset = getRecommendedRingtoneOffset(this.mSoundUri);
                            Log.secD("TimerAlertPlayer", "mSoundUri : " + this.mSoundUri + " offset : " + offset + " soundPosition : " + this.mSoundPosition);
                            if (this.mSoundPosition == 0) {
                                this.mMediaPlayer.seekTo(offset);
                            } else {
                                this.mMediaPlayer.seekTo(this.mSoundPosition);
                            }
                            this.mMediaPlayer.start();
                            Log.m41d("TimerAlertPlayer", "startRing mMediaPlayer.start");
                        }
                    }
                } catch (IOException e2) {
                    e = e2;
                    Log.secD("TimerAlertPlayer", "Exception : " + e.toString() + ", Set alternative ringtone");
                    startDefaultRingtone();
                } catch (IllegalStateException e3) {
                    e = e3;
                    Log.secD("TimerAlertPlayer", "Exception : " + e.toString() + ", Set alternative ringtone");
                    startDefaultRingtone();
                } catch (NullPointerException e4) {
                    Log.secE("TimerAlertPlayer", "Exception : " + e4.toString());
                    startRing();
                }
            }
        }
    }

    private void stopRing() {
        Log.secD("TimerAlertPlayer", "stopRing");
        if (this.mMediaPlayer != null) {
            this.mSoundPosition = this.mMediaPlayer.getCurrentPosition();
            this.mVolumeValue = this.mAudioManager.getStreamVolume(this.mStreamType);
            Log.secD("TimerAlertPlayer", "stopRing : soundPosition = " + this.mSoundPosition + " mVolumeValue = " + this.mVolumeValue);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        this.mIsPlaying = false;
    }

    private void stopVibrate() {
        Log.secD("TimerAlertPlayer", "stopVibrate");
        if (sTimerVibrator != null) {
            sTimerVibrator.cancel();
            this.mIsVibrating = false;
        }
    }

    private Vibrator getTimerVibrator() {
        Log.secD("TimerAlertPlayer", "getTimerVibrator()");
        if (sTimerVibrator == null) {
            synchronized (sLock) {
                sTimerVibrator = (Vibrator) this.mContext.getSystemService("vibrator");
            }
        } else {
            Log.secD("TimerAlertPlayer", "TimerVibrator instance already exist");
        }
        return sTimerVibrator;
    }

    public void initTimerSoundInfo() {
        boolean z;
        boolean z2 = false;
        Log.secD("TimerAlertPlayer", "initTimerSoundInfo");
        this.mSoundUri = this.mTimerManager.getTimerSoundUri();
        this.mVolumeValue = this.mTimerManager.getTimerVolume();
        this.mSoundPosition = 0;
        if (Uri.EMPTY.equals(this.mSoundUri) || this.mVolumeValue == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mPlayMute = z;
        if (this.mTimerManager.isTimerVibOn() || StateUtils.isMannerModeState(this.mContext)) {
            z2 = true;
        }
        this.mPlayVib = z2;
    }

    private void buildAudioFocusRequest() {
        Log.secD("TimerAlertPlayer", "buildAudioFocusRequest");
        this.mAudioFocusRequest = new AudioFocusRequest.Builder(2).setAudioAttributes(new Builder().setUsage(this.mUsageType).setContentType(4).setLegacyStreamType(this.mStreamType).build()).setOnAudioFocusChangeListener(this.mAudioFocusListener).build();
    }

    private void startDefaultRingtone() {
        Log.secD("TimerAlertPlayer", "startDefaultRingtone");
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
                this.mMediaPlayer = new MediaPlayer();
                this.mMediaPlayer.setAudioAttributes(new Builder().setLegacyStreamType(this.mStreamType).setUsage(this.mUsageType).setContentType(4).build());
                this.mMediaPlayer.setLooping(true);
                this.mSoundUri = TimerManager.getDefaultRingtoneUri(this.mContext);
                this.mMediaPlayer.setDataSource(this.mContext, this.mSoundUri);
                setVolume();
                this.mMediaPlayer.prepare();
                this.mMediaPlayer.start();
                Log.m41d("TimerAlertPlayer", "startDefaultRingtone mMediaPlayer.start");
                this.mIsPlaying = true;
            } catch (IOException e) {
                startAlternativeRingtone();
            } catch (NullPointerException e2) {
                Log.secE("TimerAlertPlayer", "Exception : " + e2.toString());
                Log.secE("TimerAlertPlayer", "MediaPlayer is not normal state retry it.");
                startRing();
            }
        }
    }

    private void startAlternativeRingtone() throws NullPointerException {
        try {
            this.mSoundUri = RingtonePlayer.getAlternativeRingtoneUri();
            this.mMediaPlayer.setDataSource(this.mContext, this.mSoundUri);
            setVolume();
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();
            Log.m41d("TimerAlertPlayer", "startDefaultRingtone IOException1 mMediaPlayer.start");
            this.mIsPlaying = true;
        } catch (IOException e) {
            startSecondAlternativeRingtone();
        }
    }

    private void startSecondAlternativeRingtone() throws NullPointerException {
        try {
            String secondAlternativeTimerRingtone = "/system/media/audio/alarms/Ticktac.ogg";
            this.mSoundUri = Uri.parse("/system/media/audio/alarms/Ticktac.ogg");
            this.mMediaPlayer.setDataSource(this.mContext, this.mSoundUri);
            setVolume();
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();
            Log.m41d("TimerAlertPlayer", "startDefaultRingtone IOException2 mMediaPlayer.start");
            this.mIsPlaying = true;
        } catch (IOException e) {
            Log.secE("TimerAlertPlayer", "Exception : " + e.toString());
            Log.secE("TimerAlertPlayer", "Failed load sound file");
        }
    }

    private void setVolume() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mAudioManager.setStreamVolume(this.mStreamType, this.mVolumeValue, 0);
            Log.m41d("TimerAlertPlayer", "setVolume mStreamType = " + this.mStreamType + " mVolumeValue = " + this.mVolumeValue);
        }
    }

    private int getRecommendedRingtoneOffset(Uri tone) {
        String seekToMillis = tone.getQueryParameter("highlight_offset");
        if (seekToMillis == null) {
            return 0;
        }
        return Integer.parseInt(seekToMillis);
    }

    public void setStreamMusicModeForJapan(boolean isStreamMusicModeForJapan) {
        this.mIsStreamMusicModeForJapan = isStreamMusicModeForJapan;
    }

    public void updateVibrationState() {
        boolean z = this.mTimerManager.isTimerVibOn() || StateUtils.isMannerModeState(this.mContext);
        this.mPlayVib = z;
    }
}
