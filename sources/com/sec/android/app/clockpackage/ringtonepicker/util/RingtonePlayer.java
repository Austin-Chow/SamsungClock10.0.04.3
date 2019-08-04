package com.sec.android.app.clockpackage.ringtonepicker.util;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFocusRequest.Builder;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.provider.FontsContractCompat.FontRequestCallback;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.IOException;

public class RingtonePlayer {
    private static long VOLUME_CHANGING_TERM = 1000;
    private static final OnAudioFocusChangeListener mAudioFocusListener = new C06811();
    private static Handler mSecondMediaPlayHandler = new Handler(Looper.getMainLooper());
    private static ValueAnimator mValueAnimator;
    private static AudioFocusRequest sAudioFocusRequest = null;
    private static AudioManager sAudioManager = null;
    private static MediaPlayer sMediaPlayer = null;
    private static MediaPlayer sSecondMediaPlayer = null;

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer$1 */
    static class C06811 implements OnAudioFocusChangeListener {
        C06811() {
        }

        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case FontRequestCallback.FAIL_REASON_FONT_LOAD_ERROR /*-3*/:
                case -2:
                case -1:
                    if (RingtonePlayer.getMediaPlayer().isPlaying()) {
                        RingtonePlayer.stopMediaPlayer();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer$2 */
    static class C06822 implements OnErrorListener {
        C06822() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.secE("RingtonePlayer", "Error occurred while playing audio.");
            mp.stop();
            mp.release();
            RingtonePlayer.sMediaPlayer = null;
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer$4 */
    static class C06844 implements Runnable {
        C06844() {
        }

        public void run() {
            RingtonePlayer.changeMediaPlayerVolumeGradually(true);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer$6 */
    static class C06866 implements OnCompletionListener {
        C06866() {
        }

        public void onCompletion(MediaPlayer mp) {
            RingtonePlayer.changeMediaPlayerVolumeGradually(false);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer$7 */
    static class C06877 implements OnErrorListener {
        C06877() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.secE("RingtonePlayer", "Error occurred while playing audio.");
            mp.stop();
            mp.release();
            RingtonePlayer.sSecondMediaPlayer = null;
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer$9 */
    static class C06899 implements AnimatorUpdateListener {
        C06899() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            if (RingtonePlayer.sMediaPlayer == null) {
                animation.cancel();
                return;
            }
            float volume = ((Float) animation.getAnimatedValue()).floatValue();
            RingtonePlayer.sMediaPlayer.setVolume(volume, volume);
        }
    }

    public static class AlarmRingtone {
        public static Uri getDefaultRingtoneUri(Context context) {
            Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, 4);
            if (defaultRingtoneUri == null) {
                Log.secE("RingtonePlayer", "defaultRingtoneUri == null");
                defaultRingtoneUri = Uri.parse("content://media/internal/audio/media/48");
            }
            if (Feature.DEBUG_ENG) {
                Log.secD("RingtonePlayer", "..getDefaultRingtoneUri.. Uri = " + defaultRingtoneUri);
            }
            return defaultRingtoneUri;
        }
    }

    public static class TimerRingtone {
        public static Uri getDefaultRingtoneUri(Context context) {
            Uri defaultRingtoneUri = null;
            Cursor cursor = context.getContentResolver().query(Media.INTERNAL_CONTENT_URI, null, "_data='/system/media/audio/ringtones/Chime_Time.ogg'", null, null);
            if (cursor != null && cursor.moveToNext()) {
                defaultRingtoneUri = ContentUris.withAppendedId(Media.INTERNAL_CONTENT_URI, (long) cursor.getInt(0));
                Log.secD("RingtonePlayer", "defaultRingtoneUri new one exists");
            }
            if (defaultRingtoneUri == null) {
                Log.secD("RingtonePlayer", "defaultRingtoneUri == null");
                cursor = context.getContentResolver().query(Media.INTERNAL_CONTENT_URI, null, "_data='/system/media/audio/ringtones/Time_Up.ogg'", null, null);
                if (cursor == null || !cursor.moveToNext()) {
                    defaultRingtoneUri = Uri.parse("/system/media/audio/ringtones/Time_Up.ogg");
                } else {
                    defaultRingtoneUri = ContentUris.withAppendedId(Media.INTERNAL_CONTENT_URI, (long) cursor.getInt(0));
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return defaultRingtoneUri;
        }
    }

    public static boolean requestAudioFocus(Context context) {
        if (getAudioManager(context).requestAudioFocus(getAudioFocusRequest()) != 0) {
            return true;
        }
        Log.secW("RingtonePlayer", "requestAudioFocus is failed");
        return false;
    }

    private static AudioFocusRequest getAudioFocusRequest() {
        if (sAudioFocusRequest == null) {
            sAudioFocusRequest = new Builder(2).setAudioAttributes(new AudioAttributes.Builder().setContentType(4).setLegacyStreamType(4).build()).setOnAudioFocusChangeListener(mAudioFocusListener).build();
        }
        return sAudioFocusRequest;
    }

    public static boolean isActiveStreamAlarm() {
        Log.secD("RingtonePlayer", "isActiveStreamAlarm : " + AudioManager.semGetActiveStreamType());
        return AudioManager.semGetActiveStreamType() == 4;
    }

    private static AudioManager getAudioManager(Context context) {
        if (sAudioManager == null) {
            sAudioManager = (AudioManager) context.getSystemService("audio");
        }
        return sAudioManager;
    }

    public static void setStreamVolume(Context context, int volume, boolean isVisibleVolumePanel) {
        int volumePanelOption;
        if (isVisibleVolumePanel) {
            volumePanelOption = 1;
        } else {
            volumePanelOption = 0;
        }
        getAudioManager(context).setStreamVolume(4, volume, volumePanelOption);
        Log.m41d("RingtonePlayer", "setStreamVolume STREAM_ALARM volume = " + volume);
        getAudioManager(context).adjustStreamVolume(4, 0, volumePanelOption);
    }

    public static void playMediaPlayer(Context context, final Uri selectedUri) {
        Exception e;
        Log.secI("RingtonePlayer", "selectedUri = " + selectedUri);
        if (selectedUri != null) {
            try {
                getMediaPlayer().setOnErrorListener(new C06822());
                sMediaPlayer.setDataSource(context, selectedUri);
                sMediaPlayer.setAudioAttributes(getAudioFocusRequest().getAudioAttributes());
                sMediaPlayer.prepareAsync();
                sMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                    public void onPrepared(MediaPlayer arg0) {
                        RingtonePlayer.getMediaPlayer().setLooping(true);
                        if (!RingtonePlayer.sMediaPlayer.isPlaying()) {
                            int offset = RingtonePlayer.getRecommendedRingtoneOffset(selectedUri);
                            Log.secD("RingtonePlayer", "playMediaPlayer offset : " + offset);
                            RingtonePlayer.sMediaPlayer.seekTo(offset);
                            RingtonePlayer.sMediaPlayer.start();
                            Log.m41d("RingtonePlayer", "playMediaPlayer sMediaPlayer.start");
                        }
                    }
                });
            } catch (IOException e2) {
                e = e2;
                Log.secI("RingtonePlayer", "Unable to play track" + e);
            } catch (IllegalStateException e3) {
                e = e3;
                Log.secI("RingtonePlayer", "Unable to play track" + e);
            }
        }
    }

    public static void playSecondMedioPlayer(final Context context, final Uri selectedUri, long delay) {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        mSecondMediaPlayHandler.removeCallbacksAndMessages(null);
        mSecondMediaPlayHandler.postDelayed(new C06844(), delay);
        mSecondMediaPlayHandler.postDelayed(new Runnable() {
            public void run() {
                RingtonePlayer.playSecondMedioPlayer(context, selectedUri);
            }
        }, VOLUME_CHANGING_TERM + delay);
    }

    private static void playSecondMedioPlayer(Context context, Uri selectedUri) {
        Exception e;
        final MediaPlayer VoiceMediaPlayer = getSecondPlayer();
        VoiceMediaPlayer.setOnCompletionListener(new C06866());
        VoiceMediaPlayer.setOnErrorListener(new C06877());
        try {
            VoiceMediaPlayer.setDataSource(context, selectedUri);
            VoiceMediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(4).setContentType(4).semAddAudioTag("NOFADE").build());
            VoiceMediaPlayer.prepareAsync();
            VoiceMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer arg0) {
                    RingtonePlayer.getSecondPlayer().setLooping(false);
                    if (!VoiceMediaPlayer.isPlaying()) {
                        VoiceMediaPlayer.start();
                        Log.m41d("RingtonePlayer", "playMediaPlayer VoiceMediaPlayer.start");
                    }
                }
            });
        } catch (IOException e2) {
            e = e2;
            Log.secI("RingtonePlayer", "Unable to play track" + e);
        } catch (IllegalStateException e3) {
            e = e3;
            Log.secI("RingtonePlayer", "Unable to play track" + e);
        }
    }

    private static void changeMediaPlayerVolumeGradually(boolean isDecrease) {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        if (isDecrease) {
            mValueAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.1f});
        } else {
            mValueAnimator = ValueAnimator.ofFloat(new float[]{0.1f, 1.0f});
        }
        mValueAnimator.setDuration(VOLUME_CHANGING_TERM);
        mValueAnimator.addUpdateListener(new C06899());
        mValueAnimator.start();
    }

    public static MediaPlayer getMediaPlayer() {
        if (sMediaPlayer == null) {
            Log.secI("RingtonePlayer", "get a new sMediaPlayer instance ");
            sMediaPlayer = new MediaPlayer();
        }
        return sMediaPlayer;
    }

    private static MediaPlayer getSecondPlayer() {
        if (sSecondMediaPlayer == null) {
            Log.secI("RingtonePlayer", "get a new sSecondMediaPlayer instance ");
            sSecondMediaPlayer = new MediaPlayer();
        }
        return sSecondMediaPlayer;
    }

    private static int getRecommendedRingtoneOffset(Uri tone) {
        String SeekToMillis = tone.getQueryParameter("highlight_offset");
        if (SeekToMillis == null) {
            return 0;
        }
        return Integer.parseInt(SeekToMillis);
    }

    public static void stopMediaPlayer() {
        if (sSecondMediaPlayer != null) {
            getSecondPlayer().setVolume(0.0f, 0.0f);
            sSecondMediaPlayer.pause();
            sSecondMediaPlayer.stop();
            sSecondMediaPlayer.release();
            sSecondMediaPlayer = null;
        }
        getMediaPlayer().setVolume(0.0f, 0.0f);
        sMediaPlayer.pause();
        sMediaPlayer.stop();
        sMediaPlayer.release();
        sMediaPlayer = null;
        if (sAudioManager != null) {
            sAudioManager.abandonAudioFocusRequest(getAudioFocusRequest());
        }
        sAudioManager = null;
        sAudioFocusRequest = null;
        mSecondMediaPlayHandler.removeCallbacksAndMessages(null);
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    public static void stopMediaPlayerExceptAbandon() {
        if (sSecondMediaPlayer != null) {
            getSecondPlayer().setVolume(0.0f, 0.0f);
            sSecondMediaPlayer.pause();
            sSecondMediaPlayer.stop();
            sSecondMediaPlayer.release();
            sSecondMediaPlayer = null;
        }
        getMediaPlayer().setVolume(0.0f, 0.0f);
        sMediaPlayer.pause();
        sMediaPlayer.stop();
        sMediaPlayer.release();
        sMediaPlayer = null;
        mSecondMediaPlayHandler.removeCallbacksAndMessages(null);
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    public static Uri getDefaultRingtoneUri(Context context) {
        return AlarmRingtone.getDefaultRingtoneUri(context);
    }

    public static Uri getAlternativeRingtoneUri() {
        return Uri.parse("/system/media/audio/ringtones/Time_Up.ogg");
    }
}
