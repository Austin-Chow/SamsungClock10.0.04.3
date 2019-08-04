package com.sec.android.app.clockpackage.stopwatch.model;

import com.sec.android.app.clockpackage.common.util.Log;

public class StopwatchData {
    public static long sElapsedMillis;
    public static long sElapsedMillisBefore = 0;
    public static int sHour;
    public static int sLapCount;
    private static int sLapHour;
    private static int sLapMillis;
    private static int sLapMinute;
    private static int sLapSecond;
    public static int sMillis;
    public static int sMinute;
    public static int sSecond;
    private static int sStopwatchState = 0;

    public static void setInputTime(long time) {
        sElapsedMillis = time;
        sHour = (int) (sElapsedMillis / 3600000);
        int tmp = (int) (sElapsedMillis - (((long) sHour) * 3600000));
        sMinute = tmp / 60000;
        tmp -= sMinute * 60000;
        sSecond = tmp / 1000;
        sMillis = (tmp - (sSecond * 1000)) / 10;
    }

    public static void setInputLapTime(long time) {
        sLapHour = (int) (time / 3600000);
        int tmpLapTime = (int) (time - (((long) sLapHour) * 3600000));
        sLapMinute = tmpLapTime / 60000;
        tmpLapTime -= sLapMinute * 60000;
        sLapSecond = tmpLapTime / 1000;
        sLapMillis = (tmpLapTime - (sLapSecond * 1000)) / 10;
    }

    public static void setStopwatchState(int state) {
        Log.secD("StopwatchData", "setStopwatchState() /original_state=" + sStopwatchState + "/new_state=" + state);
        sStopwatchState = state;
    }

    public static int getStopwatchState() {
        return sStopwatchState;
    }

    public static int getLapCount() {
        return sLapCount;
    }

    public static void setLapCount(int lapCount) {
        sLapCount = lapCount;
    }

    public static long getElapsedMillisBefore() {
        return sElapsedMillisBefore;
    }

    public static long getElapsedMillis() {
        return sElapsedMillis;
    }

    public static void setElapsedMillis(long elapsedTime) {
        sElapsedMillis = elapsedTime;
    }

    public static int getHour() {
        return sHour;
    }

    public static int getMinute() {
        return sMinute;
    }

    public static int getSecond() {
        return sSecond;
    }

    public static int getMillis() {
        return sMillis;
    }

    public static int getLapHour() {
        return sLapHour;
    }

    public static int getLapMinute() {
        return sLapMinute;
    }

    public static int getLapSecond() {
        return sLapSecond;
    }

    public static int getLapMillis() {
        return sLapMillis;
    }
}
