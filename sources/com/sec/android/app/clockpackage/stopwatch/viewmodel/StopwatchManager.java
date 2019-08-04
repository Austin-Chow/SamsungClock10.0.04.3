package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.CountDownTimer;
import android.os.SystemClock;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.viewmodel.ChronometerManager;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchManagerListener;
import com.sec.android.app.clockpackage.stopwatch.model.ListItem;
import com.sec.android.app.clockpackage.stopwatch.model.StopwatchData;
import java.util.ArrayList;
import java.util.Locale;

public class StopwatchManager extends ChronometerManager {
    private static ArrayList<ListItem> mListItems = new ArrayList();
    private static StopwatchManager sInstance = null;
    private StopwatchManagerListener mStopwatchManagerListener;
    private CountDownTimer mTimer = null;

    private StopwatchManager() {
        Log.secD("StopwatchManager", "StopwatchManager()");
        this.mStopwatchManagerListener = null;
        this.mTimer = null;
    }

    public static StopwatchManager getInstance() {
        Log.secD("StopwatchManager", "getInstance() /");
        if (sInstance == null || mListItems == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new StopwatchManager();
                }
                if (mListItems == null) {
                    mListItems = new ArrayList();
                }
            }
        } else {
            Log.secD("StopwatchManager", "StopwatchManager instance already exist");
        }
        return sInstance;
    }

    public void setContext(Context context) {
        Log.secD("StopwatchManager", "setContext() / context = " + context);
        this.mContext = context;
    }

    public void setListener(StopwatchManagerListener listener) {
        Log.secD("StopwatchManager", "setListener() /listener =" + listener + " mStopwatchManagerListener : " + this.mStopwatchManagerListener);
        this.mStopwatchManagerListener = listener;
    }

    public CountDownTimer getCountdownTimer() {
        return this.mTimer;
    }

    public void fragmentStopped(Object listener) {
        if (this.mStopwatchManagerListener == listener) {
            this.mIsFragmentShowing = false;
        }
    }

    public void setListItems(ArrayList<ListItem> listItem) {
        mListItems = listItem;
    }

    public ArrayList<ListItem> getListItems() {
        return mListItems;
    }

    private void setTimer(long time) {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        this.mTimer = new CountDownTimer(time, 65) {
            public void onTick(long millisUntilFinished) {
                StopwatchData.sElapsedMillis = 359999990 - millisUntilFinished;
                if (StopwatchManager.this.mIsFragmentShowing && StopwatchManager.this.mStopwatchManagerListener != null) {
                    StopwatchManager.this.mStopwatchManagerListener.onUpdateTimeView();
                }
            }

            public void onFinish() {
            }
        };
    }

    public void start() {
        Log.m41d("StopwatchManager", "start() / " + new Throwable().getStackTrace()[1]);
        StopwatchData.setStopwatchState(1);
        ClockUtils.insertSaLog("120", "1301");
        setTimer(359999990);
        this.mTimer.start();
        if (this.mStopwatchManagerListener != null) {
            this.mStopwatchManagerListener.onSetViewState();
        }
    }

    public void stop() {
        Log.m41d("StopwatchManager", "stop() / " + new Throwable().getStackTrace()[1]);
        StopwatchData.setStopwatchState(2);
        ClockUtils.insertSaLog("120", "1303");
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
        if (this.mStopwatchManagerListener != null) {
            this.mStopwatchManagerListener.onSetViewState();
        }
    }

    public void resume() {
        Log.m41d("StopwatchManager", "resume() / " + new Throwable().getStackTrace()[1]);
        ClockUtils.insertSaLog("120", "1305");
        setTimer(359999990 - StopwatchData.sElapsedMillis);
        this.mTimer.start();
        StopwatchData.setStopwatchState(1);
        if (this.mStopwatchManagerListener != null && this.mIsFragmentShowing) {
            this.mStopwatchManagerListener.onSetViewState();
        }
    }

    public void reset() {
        Log.m41d("StopwatchManager", "reset() / " + new Throwable().getStackTrace()[1]);
        ClockUtils.insertSaLog("120", "1306");
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
        StopwatchData.sLapCount = 0;
        StopwatchData.sHour = 0;
        StopwatchData.sMinute = 0;
        StopwatchData.sSecond = 0;
        StopwatchData.sMillis = 0;
        StopwatchData.sElapsedMillis = 0;
        StopwatchData.sElapsedMillisBefore = 0;
        mListItems.clear();
        StopwatchData.setStopwatchState(3);
        if (this.mStopwatchManagerListener != null && this.mIsFragmentShowing) {
            this.mStopwatchManagerListener.onReset();
        }
        resetSharedPreference();
    }

    public void addLap() {
        Log.secD("StopwatchManager", "addLap");
        String timeSeparatorText = ClockUtils.getTimeSeparatorText(this.mContext);
        if (StopwatchData.sElapsedMillis == StopwatchData.sElapsedMillisBefore) {
            Log.secD("StopwatchManager", "addLap : double touch - return");
            return;
        }
        long elapsedMillisDiff;
        StringBuilder sb = new StringBuilder();
        if (StopwatchData.sHour > 0) {
            sb.append(ClockUtils.toTwoDigitString(StopwatchData.sHour)).append(timeSeparatorText);
        }
        sb.append(ClockUtils.toTwoDigitString(StopwatchData.sMinute)).append(timeSeparatorText);
        sb.append(ClockUtils.toTwoDigitString(StopwatchData.sSecond));
        if (mListItems.size() != 0) {
            elapsedMillisDiff = ((StopwatchData.sElapsedMillis / 10) - (((ListItem) mListItems.get(0)).getElapsedTime() / 10)) * 10;
        } else {
            elapsedMillisDiff = (StopwatchData.sElapsedMillis / 10) * 10;
        }
        int hourDiff = (int) (elapsedMillisDiff / 3600000);
        int tmp = (int) (elapsedMillisDiff - (((long) hourDiff) * 3600000));
        int minuteDiff = tmp / 60000;
        tmp -= 60000 * minuteDiff;
        int secondDiff = tmp / 1000;
        int millisDiff = (tmp - (secondDiff * 1000)) / 10;
        StringBuilder sb1 = new StringBuilder();
        if (StopwatchData.sHour > 0) {
            sb1.append(ClockUtils.toTwoDigitString(hourDiff)).append(timeSeparatorText);
        }
        sb1.append(ClockUtils.toTwoDigitString(minuteDiff)).append(timeSeparatorText);
        sb1.append(ClockUtils.toTwoDigitString(secondDiff));
        Log.secD("StopwatchManager", "elapsedMillisBefore = " + StopwatchData.sElapsedMillisBefore);
        Log.secD("StopwatchManager", "elapsedMillis       = " + StopwatchData.sElapsedMillis);
        String strLapCount = ClockUtils.toTwoDigitString(StopwatchData.sLapCount + 1);
        ArrayList arrayList = mListItems;
        ArrayList arrayList2 = arrayList;
        int i = 0;
        arrayList2.add(i, new ListItem(strLapCount, StopwatchData.sElapsedMillis, sb.toString(), '.' + ClockUtils.toTwoDigitString(StopwatchData.sMillis), sb1.toString(), '.' + ClockUtils.toTwoDigitString(millisDiff), StopwatchUtils.getDescriptionString(this.mContext, StopwatchData.sHour, StopwatchData.sMinute, StopwatchData.sSecond, StopwatchData.sMillis), StopwatchUtils.getDescriptionString(this.mContext, hourDiff, minuteDiff, secondDiff, millisDiff)));
        if (mListItems.size() != 0) {
            StopwatchData.sElapsedMillisBefore = ((ListItem) mListItems.get(0)).getElapsedTime();
        } else {
            StopwatchData.sElapsedMillisBefore = 0;
        }
        StopwatchData.sLapCount++;
        if (this.mStopwatchManagerListener != null) {
            this.mStopwatchManagerListener.onAddLap();
        }
        ClockUtils.insertSaLog("120", "1304");
    }

    private void addLap(long elapsedMillis, long elapsedMillisBefore) {
        long elapsedMillisDiff;
        StringBuilder sb = new StringBuilder();
        String timeSeparatorText = ClockUtils.getTimeSeparatorText(this.mContext);
        int hour = (int) (elapsedMillis / 3600000);
        int tmp = (int) (elapsedMillis - (((long) hour) * 3600000));
        int minute = tmp / 60000;
        tmp -= 60000 * minute;
        int second = tmp / 1000;
        int millis = (tmp - (second * 1000)) / 10;
        if (getListItems().size() != 0) {
            elapsedMillisDiff = ((elapsedMillis / 10) - (elapsedMillisBefore / 10)) * 10;
        } else {
            elapsedMillisDiff = (elapsedMillis / 10) * 10;
        }
        int hourDiff = (int) (elapsedMillisDiff / 3600000);
        int tmp_1 = (int) (elapsedMillisDiff - (((long) hourDiff) * 3600000));
        int minuteDiff = tmp_1 / 60000;
        tmp_1 -= 60000 * minuteDiff;
        int secondDiff = tmp_1 / 1000;
        int millisDiff = (tmp_1 - (secondDiff * 1000)) / 10;
        StringBuilder sb1 = new StringBuilder();
        if (hour > 0) {
            sb.append(ClockUtils.toTwoDigitString(hour)).append(timeSeparatorText);
            sb1.append(ClockUtils.toTwoDigitString(hourDiff)).append(timeSeparatorText);
        }
        sb.append(ClockUtils.toTwoDigitString(minute)).append(timeSeparatorText);
        sb.append(ClockUtils.toTwoDigitString(second));
        sb1.append(ClockUtils.toTwoDigitString(minuteDiff)).append(timeSeparatorText);
        sb1.append(ClockUtils.toTwoDigitString(secondDiff));
        ArrayList arrayList = mListItems;
        ArrayList arrayList2 = arrayList;
        int i = 0;
        arrayList2.add(i, new ListItem(ClockUtils.toTwoDigitString(getListItems().size() + 1), elapsedMillis, sb.toString(), '.' + ClockUtils.toTwoDigitString(millis), sb1.toString(), '.' + ClockUtils.toTwoDigitString(millisDiff), StopwatchUtils.getDescriptionString(this.mContext, hour, minute, second, millis), StopwatchUtils.getDescriptionString(this.mContext, hourDiff, minuteDiff, secondDiff, millisDiff)));
    }

    public void restoreSharedPreference() {
        Log.secD("StopwatchManager", "restoreSharedPreference");
        SharedPreferences pref = this.mContext.getSharedPreferences("Stopwatch", 0);
        if (pref != null) {
            Log.secD("StopwatchManager", "saved lap count :" + pref.getInt("stopwatch_lapcount", 0));
            StopwatchData.setStopwatchState(pref.getInt("stopwatch_current_state", 0));
            StopwatchData.setLapCount(pref.getInt("stopwatch_lapcount", 0));
            long savedElapsedTime = pref.getLong("stopwatch_elapsed_realtime", 0);
            long savedElapsedMillis = pref.getLong("stopwatch_elapsed_time", 0);
            long savedElapsedMillisBefore = pref.getLong("stopwatch_elapsed_time_before", 0);
            String lastLocale = pref.getString("stopwatch_last_locale", null);
            String currentLocale = Locale.getDefault().getLanguage();
            if (!(StopwatchData.getLapCount() == 0 || getListItems() == null || (StopwatchData.getLapCount() == getListItems().size() && ((!"ar".equalsIgnoreCase(lastLocale) || "ar".equalsIgnoreCase(currentLocale)) && ((!"fa".equalsIgnoreCase(lastLocale) || "fa".equalsIgnoreCase(currentLocale)) && (("ar".equalsIgnoreCase(lastLocale) || !"ar".equalsIgnoreCase(currentLocale)) && ("fa".equalsIgnoreCase(lastLocale) || !"fa".equalsIgnoreCase(currentLocale)))))))) {
                long savedLapElapsedMillisBefore = 0;
                getListItems().clear();
                for (int i = 0; i < StopwatchData.getLapCount(); i++) {
                    long savedLapElapsedMillis = pref.getLong("stopwatch_elapsed_time" + Integer.toString(i + 1), 0);
                    if (getListItems().size() == i) {
                        addLap(savedLapElapsedMillis, savedLapElapsedMillisBefore);
                        savedLapElapsedMillisBefore = savedLapElapsedMillis;
                    }
                }
                if (this.mStopwatchManagerListener != null) {
                    this.mStopwatchManagerListener.onSetLapList();
                }
            }
            if (StopwatchData.getStopwatchState() == 1) {
                try {
                    if (getCountdownTimer() != null) {
                        getCountdownTimer().cancel();
                    }
                } catch (NullPointerException e) {
                    Log.secE("StopwatchManager", "Exception : " + e.toString());
                }
                setTimer(359999990 - ((SystemClock.elapsedRealtime() - savedElapsedTime) + savedElapsedMillis));
                if (getCountdownTimer() != null) {
                    getCountdownTimer().start();
                }
                StopwatchData.sElapsedMillisBefore = (SystemClock.elapsedRealtime() - savedElapsedTime) + savedElapsedMillisBefore;
                StopwatchData.sElapsedMillis = savedElapsedMillis;
                StopwatchData.sElapsedMillisBefore = savedElapsedMillisBefore;
                return;
            }
            StopwatchData.setElapsedMillis(savedElapsedMillis);
            StopwatchData.sElapsedMillis = savedElapsedMillis;
            StopwatchData.sElapsedMillisBefore = savedElapsedMillisBefore;
        } else if (StopwatchData.getStopwatchState() == 1) {
            try {
                getCountdownTimer().cancel();
            } catch (NullPointerException e2) {
                Log.secE("StopwatchManager", "Exception : " + e2.toString());
            }
            setTimer(359999990 - StopwatchData.getElapsedMillis());
            getCountdownTimer().start();
        }
    }

    public void stopNotification() {
        Log.secD("StopwatchManager", "stopNotification()");
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchNotificationService");
        Intent intent = new Intent();
        intent.setComponent(cn);
        if (this.mContext != null) {
            this.mContext.stopService(intent);
        }
    }

    protected void startNotification() {
        if ((StopwatchData.getStopwatchState() == 1 || StopwatchData.getStopwatchState() == 2) && StopwatchData.getElapsedMillis() > 0) {
            Log.secD("StopwatchManager", "startNotification()");
            ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchNotificationService");
            Intent intent = new Intent();
            intent.setComponent(cn);
            try {
                this.mContext.startService(intent);
            } catch (IllegalStateException e) {
                Log.secE("StopwatchManager", "exception : " + e.toString());
                Log.m42e("StopwatchManager", "try startForegroundService()");
                this.mContext.startForegroundService(intent);
            }
        }
    }

    public void saveSharedPreference(String locale) {
        Log.secD("StopwatchManager", "saveSharedPreference");
        Editor editor = this.mContext.getSharedPreferences("Stopwatch", 0).edit();
        editor.putInt("stopwatch_current_state", StopwatchData.getStopwatchState());
        editor.putInt("stopwatch_lapcount", StopwatchData.getLapCount());
        editor.putLong("stopwatch_elapsed_realtime", SystemClock.elapsedRealtime());
        editor.putLong("stopwatch_elapsed_time", StopwatchData.getElapsedMillis());
        editor.putLong("stopwatch_elapsed_time_before", StopwatchData.sElapsedMillisBefore);
        editor.putString("stopwatch_last_locale", locale);
        if (getListItems().size() != 0) {
            for (int i = 0; i < getListItems().size(); i++) {
                editor.putLong("stopwatch_elapsed_time" + Integer.toString(getListItems().size() - i), ((ListItem) getListItems().get(i)).getElapsedTime());
            }
        }
        editor.apply();
    }

    public void resetSharedPreference() {
        Log.secD("StopwatchManager", "resetSharedPreference");
        Editor editor = this.mContext.getSharedPreferences("Stopwatch", 0).edit();
        editor.putInt("stopwatch_current_state", 3);
        editor.putInt("stopwatch_lapcount", 0);
        editor.putLong("stopwatch_elapsed_realtime", 0);
        editor.putLong("stopwatch_elapsed_time", 0);
        editor.putLong("stopwatch_elapsed_time_before", 0);
        editor.apply();
    }
}
