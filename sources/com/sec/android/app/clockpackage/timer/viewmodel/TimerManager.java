package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.ContextThemeWrapper;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.ChronometerManager;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer.TimerRingtone;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerManagerListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class TimerManager extends ChronometerManager {
    protected static CountDownTimer sCountDownTimer = null;
    public static long sElapsedMillis = 0;
    public static boolean sIsPausedTimerActivity = false;
    public static long sOffHookElapsedMillis = 0;
    private AlarmManager mAlarmManager;
    private TimerManagerListener mTimerManagerListener;
    private PendingIntent mTimerSender;

    private static final class NoLeakCountDownTimer extends CountDownTimer {
        private final WeakReference<TimerManager> mManager;

        private NoLeakCountDownTimer(TimerManager manager, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.mManager = new WeakReference(manager);
        }

        public void onTick(long millisUntilFinished) {
            TimerManager manager = (TimerManager) this.mManager.get();
            if (manager != null) {
                TimerData.setRemainMillis(millisUntilFinished);
                manager.updateTime();
                if (TimerData.getTimerState() != 1 && TimerManager.sCountDownTimer != null) {
                    Log.secD("TimerManager", "CountDownTimer onTick() / setTimerState(TimerManager.STARTED)");
                    manager.setTimerState(1);
                }
            }
        }

        public void onFinish() {
            Log.secD("TimerManager", "CountDownTimer onFinish()");
            TimerManager manager = (TimerManager) this.mManager.get();
            if (manager != null) {
                TimerData.setRemainMillis(0);
                manager.updateTime();
            }
        }
    }

    private static class SingletonHelper {
        private static final TimerManager INSTANCE = new TimerManager();
    }

    private TimerManager() {
        this.mAlarmManager = null;
        Log.secD("TimerManager", "TimerManager()");
        this.mTimerManagerListener = null;
        TimerData.setTimerState(0);
        this.mAlarmManager = null;
        this.mTimerSender = null;
        sCountDownTimer = null;
    }

    public static TimerManager getInstance() {
        Log.secD("TimerManager", "getInstance()");
        return SingletonHelper.INSTANCE;
    }

    public static Uri getDefaultRingtoneUri(Context context) {
        return TimerRingtone.getDefaultRingtoneUri(context);
    }

    public void setContext(Context context) {
        if (Feature.DEBUG_ENG) {
            Log.secD("TimerManager", "setContext() / context = " + context);
        }
        this.mContext = context.getApplicationContext();
        if (this.mAlarmManager == null) {
            initIntentForAlarmManager();
            Log.secD("TimerManager", "setContext() alarmManager is null so call initIntentAlarmManager()");
        }
    }

    public void setTimerManagerListener(TimerManagerListener listener) {
        if (Feature.DEBUG_ENG) {
            Log.secD("TimerManager", "setFragmentInstance() / listener = " + listener + ", mTimerManagerListener = " + this.mTimerManagerListener);
        }
        this.mTimerManagerListener = listener;
    }

    public void setInputTimeForPicker(int hour, int min, int sec) {
        TimerData.setInputTime(hour, min, sec);
        TimerData.printTimerInfo();
    }

    public void setInputTime(int hour, int min, int sec) {
        TimerData.setInputTime(hour, min, sec);
        saveSharedPreference();
        TimerData.printTimerInfo();
    }

    public void setInputTime(long millis) {
        TimerData.setInputTime(millis);
        saveSharedPreference();
        TimerData.printTimerInfo();
    }

    public void setTimerState(int state) {
        Log.m41d("TimerManager", "setTimerState() / state = " + state + ", " + new Throwable().getStackTrace()[1]);
        TimerData.setTimerState(state);
        saveSharedPreference();
    }

    public void fragmentStopped(Object listener) {
        if (this.mTimerManagerListener == listener) {
            this.mIsFragmentShowing = false;
        }
    }

    public void startTimer(long inputMillis, long remainMillis) {
        startTimer(inputMillis, remainMillis, null);
    }

    public void startTimer(long inputMillis, long remainMillis, String timerName) {
        if (Feature.DEBUG_ENG) {
            Log.secD("TimerManager", "startTimer() / inputMillis = " + inputMillis + ", remainMillis = " + remainMillis + ", timerName = " + timerName + " / mContext = " + this.mContext + " / this = " + this);
        }
        setTimerToAlarmManager(inputMillis, remainMillis, timerName);
        setCountDownTimer(remainMillis);
        startCountDownTimer();
        TimerData.setOngoingInputMillis(inputMillis);
        TimerData.setRemainMillis(remainMillis);
        TimerData.setOnGoingTimerName(timerName);
    }

    public void cancelTimer() {
        Log.secD("TimerManager", "cancelTimer()");
        stopNotification();
        cancelTimerToAlarmManager();
        cancelCountDownTimer();
        loadPreviousInput();
        TimerData.setOngoingInputMillis(TimerData.getInputMillis());
        TimerData.setRemainMillis(TimerData.getInputMillis());
        TimerData.setOnGoingTimerName(null);
    }

    public void stopTimer() {
        Log.secD("TimerManager", "stopTimer()");
        cancelTimerToAlarmManager();
        cancelCountDownTimer();
    }

    private void initIntentForAlarmManager() {
        Log.secD("TimerManager", "initIntentForAlarmManager()");
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(NotificationCompat.CATEGORY_ALARM);
    }

    private void setTimerToAlarmManager(long inputMillis, long remainMillis, String timerName) {
        if (this.mAlarmManager == null) {
            initIntentForAlarmManager();
        }
        Log.secD("TimerManager", "timerName" + timerName);
        Intent timerIntent = new Intent("com.sec.android.app.clockpackage.timer.playsound");
        timerIntent.putExtra("com.sec.android.clockpackage.timer.TIMER_NAME", timerName);
        timerIntent.putExtra("com.sec.android.clockpackage.timer.TIMER_ALERT_INPUT_MILLIS", inputMillis);
        timerIntent.setPackage("com.sec.android.app.clockpackage");
        timerIntent.addFlags(402653184);
        this.mTimerSender = PendingIntent.getBroadcast(this.mContext, 0, timerIntent, 134217728);
        remainMillis -= 100;
        if (remainMillis < 500) {
            remainMillis = 0;
        }
        this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + remainMillis, this.mTimerSender);
        Log.m41d("TimerManager", "setTimerToAlarmManager() / mContext =" + this.mContext + " / this = " + this + " / inputMillis = " + inputMillis + " / remainMillis = " + remainMillis + " / mAlarmManager = " + this.mAlarmManager + " / mTimerSender = " + this.mTimerSender);
    }

    private void cancelTimerToAlarmManager() {
        Log.m41d("TimerManager", "cancelTimerToAlarmManager() / mContext = " + this.mContext + " / this = " + this + " / mAlarmManager = " + this.mAlarmManager);
        if (this.mAlarmManager == null) {
            initIntentForAlarmManager();
        }
        if (this.mTimerSender == null) {
            this.mTimerSender = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.sec.android.app.clockpackage.timer.playsound"), 134217728);
        }
        if (!(this.mAlarmManager == null || this.mTimerSender == null)) {
            Log.m41d("TimerManager", "cancelTimerToAlarmManager() / mTimerSender = " + this.mTimerSender);
            this.mAlarmManager.cancel(this.mTimerSender);
        }
        clearTimerAlarmManager();
        if (this.mTimerSender != null) {
            this.mTimerSender = null;
        }
    }

    public void clearTimerAlarmManager() {
        Log.secD("TimerManager", "clearTimerAlarmManager()");
        if (this.mAlarmManager != null) {
            this.mAlarmManager = null;
        }
    }

    private void setCountDownTimer(long time) {
        Log.secD("TimerManager", "setCountDownTimer() / time= " + time + " / COUNT_DOWN_ADJUSTMENT = " + 0);
        long countDownInterval = time / 1000;
        if (countDownInterval > 50) {
            countDownInterval = 50;
        } else if (countDownInterval < 10) {
            countDownInterval = 10;
        }
        sCountDownTimer = new NoLeakCountDownTimer(time + 0, countDownInterval);
    }

    private void startCountDownTimer() {
        if (Feature.DEBUG_ENG) {
            Log.secD("TimerManager", "startCountDownTimer() / " + (sCountDownTimer == null ? "null" : sCountDownTimer));
        }
        if (sCountDownTimer != null) {
            sCountDownTimer.start();
        }
    }

    public void cancelCountDownTimer() {
        if (Feature.DEBUG_ENG) {
            Log.secD("TimerManager", "cancelCountDownTimer() / " + (sCountDownTimer == null ? "null" : sCountDownTimer));
        }
        if (sCountDownTimer != null) {
            sCountDownTimer.cancel();
            sCountDownTimer = null;
        }
    }

    private void loadPreviousInput() {
        setInputTime(TimerData.getPreviousInputHour(), TimerData.getPreviousInputMin(), TimerData.getPreviousInputSec());
    }

    public void updateScreen() {
        Log.secD("TimerManager", "updateScreen() / " + (this.mTimerManagerListener == null ? "null" : "updateScreen"));
        if (this.mTimerManagerListener != null && this.mIsFragmentShowing) {
            this.mTimerManagerListener.onUpdateTime();
            this.mTimerManagerListener.onSetViewState();
        }
    }

    private void updateTime() {
        if (this.mTimerManagerListener != null && this.mIsFragmentShowing) {
            this.mTimerManagerListener.onUpdateTime();
        }
    }

    public void updateScreenStart(int mode, long startTime) {
        updateScreenStart(mode, startTime, null);
    }

    public void updateScreenStart(int mode, long startTime, String timerName) {
        Log.secD("TimerManager", "updateScreenStart() / " + (this.mTimerManagerListener == null ? "null" : "updateScreenStart") + " , timerName = " + timerName);
        if (this.mTimerManagerListener != null) {
            this.mTimerManagerListener.onSetStartMode(mode, timerName);
            if (this.mIsFragmentShowing) {
                this.mTimerManagerListener.onStart();
                return;
            }
            startTimer(startTime, startTime, timerName);
            setTimerState(1);
            this.mTimerManagerListener.onResetViewToStart();
            return;
        }
        startTimer(startTime, startTime, timerName);
        setTimerState(1);
    }

    public void updateScreenReset() {
        Log.secD("TimerManager", "updateScreenReset() / " + (this.mTimerManagerListener == null ? "null" : "updateScreenReset"));
        if (this.mTimerManagerListener != null && this.mIsFragmentShowing) {
            this.mTimerManagerListener.onCancel();
        }
    }

    public void saveSharedPreference() {
        Log.secD("TimerManager", "saveSharedPreference() / SystemClock.elapsedRealtime = " + SystemClock.elapsedRealtime());
        TimerData.printTimerInfo();
        Editor ed = this.mContext.getSharedPreferences("TIMER", 0).edit();
        ed.putInt("timer_current_state", TimerData.getTimerState());
        ed.putLong("timer_elapsed_realtime", SystemClock.elapsedRealtime());
        ed.putLong("timer_remainMillis", TimerData.getRemainMillis());
        ed.putLong("timer_inputMillis", TimerData.getInputMillis());
        ed.putLong("timer_ongoing_inputMillis", TimerData.getOngoingInputMillis());
        ed.putString("timer_ongoing_timerName", TimerData.getOnGoingTimerName());
        ed.apply();
    }

    public void restoreSharedPreference() {
        SharedPreferences pref = this.mContext.getSharedPreferences("TIMER", 0);
        int savedState = pref.getInt("timer_current_state", 0);
        Log.secD("TimerManager", "restoreSharedPreference() / TimerData.getTimerState = " + TimerData.getTimerState() + " / savedState = " + savedState);
        long savedElapsedTime;
        long savedRemainMillis;
        String savedOngoingTimerName;
        if (TimerData.getTimerState() != savedState) {
            savedElapsedTime = pref.getLong("timer_elapsed_realtime", 0);
            savedRemainMillis = pref.getLong("timer_remainMillis", 0);
            long savedInputMillis = pref.getLong("timer_inputMillis", 0);
            long savedOngoingInputMillis = pref.getLong("timer_ongoing_inputMillis", 0);
            savedOngoingTimerName = pref.getString("timer_ongoing_timerName", null);
            if (Feature.DEBUG_ENG) {
                Log.secD("TimerManager", "restoreSharedPreference() / savedState = " + savedState + ", savedRemainMil = " + savedRemainMillis + ", savedElapsedTime = " + savedElapsedTime + ", savedInputMillis = " + savedInputMillis + ", savedOngoingInputMillis = " + savedOngoingInputMillis);
            }
            setInputTime(savedInputMillis);
            TimerData.savePreviousInput(savedInputMillis);
            if (savedState == 1) {
                TimerData.setRemainMillis(savedRemainMillis - (SystemClock.elapsedRealtime() - savedElapsedTime));
                if (TimerData.getRemainMillis() < 0) {
                    TimerData.setRemainMillis(0);
                    TimerData.setOngoingInputMillis(TimerData.getInputMillis());
                    savedState = 3;
                } else {
                    TimerData.setOngoingInputMillis(savedOngoingInputMillis);
                }
                TimerData.setOnGoingTimerName(savedOngoingTimerName);
                cancelCountDownTimer();
                setCountDownTimer(TimerData.getRemainMillis());
                startCountDownTimer();
            } else if (savedState == 2) {
                TimerData.setOnGoingTimerName(savedOngoingTimerName);
                TimerData.setRemainMillis(savedRemainMillis);
                TimerData.setOngoingInputMillis(savedOngoingInputMillis);
            }
            setTimerState(savedState);
        } else {
            TimerData.setInputMillis(pref.getLong("timer_inputMillis", 0));
            if (savedState == 1) {
                savedElapsedTime = pref.getLong("timer_elapsed_realtime", 0);
                savedRemainMillis = pref.getLong("timer_remainMillis", 0);
                savedOngoingTimerName = pref.getString("timer_ongoing_timerName", null);
                if (Feature.DEBUG_ENG) {
                    Log.secD("TimerManager", "restoreSharedPreference() / savedRemainMillis = " + savedRemainMillis + " / savedElapsedTime = " + savedElapsedTime + " / SystemClock.elapsedRealtime()  = " + SystemClock.elapsedRealtime());
                }
                TimerData.setRemainMillis(savedRemainMillis - (SystemClock.elapsedRealtime() - savedElapsedTime));
                TimerData.setOnGoingTimerName(savedOngoingTimerName);
                if (TimerData.getRemainMillis() < 0) {
                    TimerData.setRemainMillis(0);
                    TimerData.setOngoingInputMillis(TimerData.getInputMillis());
                    setTimerState(3);
                }
            } else {
                TimerData.savePreviousInput(TimerData.getInputMillis());
            }
        }
        Log.secD("TimerManager", "restoreSharedPreference() / end");
        TimerData.printTimerInfo();
    }

    public void resetSharedPreference() {
        Log.secD("TimerManager", "resetSharedPreference()");
        SharedPreferences pref = this.mContext.getSharedPreferences("TIMER", 0);
        Editor ed = pref.edit();
        TimerData.setInputMillis(pref.getLong("timer_inputMillis", 0));
        ed.putInt("timer_current_state", 0);
        ed.putLong("timer_elapsed_realtime", 0);
        ed.putLong("timer_remainMillis", 0);
        ed.putLong("timer_inputMillis", TimerData.getInputMillis());
        ed.putLong("timer_ongoing_inputMillis", TimerData.getInputMillis());
        ed.putString("timer_ongoing_timerName", null);
        ed.apply();
    }

    public void removeInstance() {
        Log.secD("TimerManager", "removeInstance()");
        clearTimerAlarmManager();
    }

    public void stopNotification() {
        Log.secD("TimerManager", "stopNotification()");
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.timer.viewmodel.TimerNotificationService");
        Intent intent = new Intent();
        intent.setComponent(cn);
        if (this.mContext != null) {
            this.mContext.stopService(intent);
        }
    }

    protected void startNotification() {
        if (!TimerData.isTimerStateResetedOrNone() && TimerData.getRemainMillis() > 300) {
            Log.secD("TimerManager", "startNotification()");
            ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.timer.viewmodel.TimerNotificationService");
            Intent intent = new Intent();
            intent.setComponent(cn);
            try {
                this.mContext.startService(intent);
            } catch (IllegalStateException e) {
                Log.secE("TimerManager", "exception : " + e.toString());
                Log.m42e("TimerManager", "try startForegroundService()");
                this.mContext.startForegroundService(intent);
            }
        }
    }

    public void sendTimerState() {
        Log.secD("TimerManager", "sendTimerState() / TimerData.getTimerState() = " + TimerData.getTimerState());
        Intent intent = new Intent();
        intent.putExtra("timer_send_time", SystemClock.elapsedRealtime());
        intent.setPackage("com.samsung.voiceserviceplatform");
        switch (TimerData.getTimerState()) {
            case 1:
                intent.setAction("com.sec.android.app.clockpackage.timer.ticker.TIMER_START");
                intent.putExtra("timer_remain_time", TimerData.getRemainMillis());
                break;
            case 2:
                intent.setAction("com.sec.android.app.clockpackage.timer.ticker.TIMER_STOP");
                intent.putExtra("timer_remain_time", TimerData.getRemainMillis());
                break;
            case 3:
                intent.setAction("com.sec.android.app.clockpackage.timer.ticker.TIMER_RESET");
                intent.putExtra("timer_remain_time", 0);
                break;
            default:
                intent.setAction("com.sec.android.app.clockpackage.timer.ticker.TIMER_NONE");
                intent.putExtra("timer_remain_time", 0);
                break;
        }
        this.mContext.sendBroadcast(intent);
    }

    public void callTimerBroadcast() {
        Intent br = new Intent();
        long sendTime = SystemClock.elapsedRealtime();
        br.putExtra("timer_send_time", sendTime);
        br.addFlags(402653184);
        br.setPackage("com.sec.android.app.clockpackage");
        br.setAction("com.sec.android.app.clockpackage.timer.TIMER_RESTART_NEW");
        this.mContext.sendBroadcast(br);
        Log.m41d("TimerManager", "callTimerBroadcast() / mContext = " + this.mContext + " SendTime = " + sendTime + " setAction = " + "com.sec.android.app.clockpackage.timer.TIMER_RESTART_NEW");
    }

    public void callRestartToastPopup(Context context, boolean isClearCoverClosed) {
        String restartTime;
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
            restartTime = ClockUtils.numberToArabic(TimerData.makeTimeString(TimerData.getRestartMillis()));
        } else if (Locale.getDefault().getLanguage().equalsIgnoreCase("fa")) {
            restartTime = ClockUtils.numberToPersian(TimerData.makeTimeString(TimerData.getRestartMillis()));
        } else {
            restartTime = TimerData.makeTimeString(TimerData.getRestartMillis());
        }
        showToast(context, context.getString(C0728R.string.timer_alert_restarted, new Object[]{restartTime}), 0, isClearCoverClosed);
        if (!this.mIsClockPackageResumed && StateUtils.isDndModeAlarmMuted(context)) {
            showToast(context, context.getString(C0728R.string.timer_zen_mode), 1, isClearCoverClosed);
        }
    }

    private void showToast(Context context, CharSequence text, int duration, boolean isClearCoverClosed) {
        Context context2 = new ContextThemeWrapper(context, 16974123);
        if (isClearCoverClosed) {
            int yOffset = context2.getResources().getDimensionPixelSize(C0728R.dimen.timer_alert_toast_y_offset_clear_cover);
            Toast timerAlertToast = Toast.makeText(context2, text, duration);
            timerAlertToast.setGravity(timerAlertToast.getGravity(), timerAlertToast.getXOffset(), yOffset);
            timerAlertToast.show();
            return;
        }
        Toast.makeText(context2, text, duration).show();
    }

    public boolean isTimerAlarmTopActivity() {
        String currentTop = ClockUtils.getTopActivity(this.mContext);
        Log.secI("TimerManager", "current Top Activity : " + currentTop);
        return "com.sec.android.app.clockpackage.timer.viewmodel.TimerAlarmActivity".equals(currentTop);
    }

    public void setTimerSoundValue(Uri timerSoundUri, int volume, boolean bIsVibOn) {
        Log.secD("TimerManager", "setTimerSoundValue() " + timerSoundUri + " " + volume + " " + bIsVibOn);
        Editor ed = this.mContext.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
        ed.putString("timer_tone", timerSoundUri.toString());
        ed.putInt("timer_volume_value", volume);
        ed.putBoolean("timer_vibration_on", bIsVibOn);
        ed.apply();
    }

    public Uri getTimerSoundUri() {
        Log.secD("TimerManager", "getTimerSoundUri()");
        return Uri.parse(this.mContext.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getString("timer_tone", getDefaultRingtoneUri(this.mContext).toString()));
    }

    public int getTimerVolume() {
        Log.secD("TimerManager", "getTimerVolume()");
        return this.mContext.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getInt("timer_volume_value", 11);
    }

    public boolean isTimerVibOn() {
        Log.secD("TimerManager", "isTimerVibOn()");
        return this.mContext.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getBoolean("timer_vibration_on", false);
    }
}
