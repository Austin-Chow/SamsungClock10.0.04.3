package com.sec.android.app.clockpackage.timer.model;

import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Locale;

public class TimerData {
    private static int sInputHour;
    private static long sInputMillis = 0;
    private static int sInputMin;
    private static int sInputSec;
    private static String sInputTimeStr = "00:00:00";
    private static String sOnGoingTimerName;
    private static long sOngoingInputMillis = 0;
    private static int sPreviousInputHour;
    private static int sPreviousInputMin;
    private static int sPreviousInputSec;
    private static long sRemainMillis = 0;
    private static long sRestartMillis = 0;
    private static String sRestartTimerName = null;
    private static int sTimerState = 0;

    public static void setInputTime(int hour, int min, int sec) {
        Log.secD("TimerData", "setInputTime() / h = " + hour + ", m = " + min + ", s = " + sec);
        sInputHour = hour;
        sInputMin = min;
        sInputSec = sec;
        sInputMillis = convertMillis(hour, min, sec);
        if (isTimerStateResetedOrNone()) {
            long j = sInputMillis;
            sOngoingInputMillis = j;
            sRemainMillis = j;
        }
        sInputTimeStr = makeTimeString(sInputHour, sInputMin, sInputSec);
    }

    public static void setInputTime(long millis) {
        Log.secD("TimerData", "setInputTime() / millis = " + millis);
        sInputHour = (int) (millis / 3600000);
        int tmp = (int) (millis - (((long) sInputHour) * 3600000));
        sInputMin = tmp / 60000;
        sInputSec = (tmp - (sInputMin * 60000)) / 1000;
        sInputTimeStr = makeTimeString(sInputHour, sInputMin, sInputSec);
        sInputMillis = millis;
        if (isTimerStateResetedOrNone()) {
            long j = sInputMillis;
            sOngoingInputMillis = j;
            sRemainMillis = j;
        }
    }

    public static void savePreviousInput(int previousInputHour, int previousInputMin, int previousInputSec) {
        sPreviousInputHour = previousInputHour;
        sPreviousInputMin = previousInputMin;
        sPreviousInputSec = previousInputSec;
    }

    public static void savePreviousInput(long millis) {
        sPreviousInputHour = (int) (millis / 3600000);
        int tmp = (int) (millis - (((long) sPreviousInputHour) * 3600000));
        sPreviousInputMin = tmp / 60000;
        sPreviousInputSec = (tmp - (sPreviousInputMin * 60000)) / 1000;
    }

    public static String getInputTimeStr() {
        return sInputTimeStr;
    }

    public static long getRemainMillis() {
        return sRemainMillis;
    }

    public static void setRemainMillis(long time) {
        sRemainMillis = time;
    }

    public static long getInputMillis() {
        return sInputMillis;
    }

    public static void setInputMillis(long time) {
        sInputMillis = time;
    }

    public static long getOngoingInputMillis() {
        return sOngoingInputMillis;
    }

    public static void setOngoingInputMillis(long time) {
        sOngoingInputMillis = time;
    }

    public static int getInputHour() {
        return sInputHour;
    }

    public static int getInputMin() {
        return sInputMin;
    }

    public static int getInputSec() {
        return sInputSec;
    }

    public static long getRestartMillis() {
        return sRestartMillis;
    }

    public static void setRestartMillis(long restartMillis) {
        sRestartMillis = restartMillis;
    }

    public static int getPreviousInputHour() {
        return sPreviousInputHour;
    }

    public static int getPreviousInputMin() {
        return sPreviousInputMin;
    }

    public static int getPreviousInputSec() {
        return sPreviousInputSec;
    }

    public static String getOnGoingTimerName() {
        return sOnGoingTimerName;
    }

    public static void setOnGoingTimerName(String timerName) {
        sOnGoingTimerName = timerName;
    }

    public static String getRestartTimerName() {
        return sRestartTimerName;
    }

    public static void setRestartTimerName(String timerName) {
        sRestartTimerName = timerName;
    }

    public static int getTimerState() {
        return sTimerState;
    }

    public static void setTimerState(int state) {
        Log.secD("TimerData", "setTimerState() / originalState = " + sTimerState + ", newState = " + state);
        sTimerState = state;
    }

    public static boolean isTimerStateResetedOrNone() {
        return sTimerState == 3 || sTimerState == 0;
    }

    public static void printTimerInfo() {
        if (Feature.DEBUG_ENG) {
            Log.secD("TimerData", "printTimeInfo() / timerState = " + sTimerState + ", remainMillis = " + sRemainMillis + ", inputMillis = " + sInputMillis + ", inputTimeStr = " + sInputTimeStr + ", ongoingInputMillis = " + sOngoingInputMillis);
        }
    }

    public static long convertMillis(int hour, int min, int sec) {
        return ((((long) hour) * 3600000) + (((long) min) * 60000)) + (((long) sec) * 1000);
    }

    private static String makeTimeString(int hour, int min, int sec) {
        StringBuilder retStr = new StringBuilder();
        if (hour != 0) {
            retStr.append(String.format(Locale.US, "%02d", new Object[]{Integer.valueOf(hour)})).append(':');
        }
        retStr.append(String.format(Locale.US, "%02d", new Object[]{Integer.valueOf(min)})).append(':');
        retStr.append(String.format(Locale.US, "%02d", new Object[]{Integer.valueOf(sec)}));
        if (hour != 0) {
            Log.secD("TimerData", "makeTimeString() / h = " + hour + ", m = " + min + ", s = " + sec + ", sb = " + retStr);
        } else {
            Log.secD("TimerData", "makeTimeString() / h = 00, m = " + min + ", s = " + sec + ", sb = " + retStr);
        }
        return retStr.toString();
    }

    public static String makeTimeString(long millis) {
        int hour = (int) (millis / 3600000);
        int tmp = (int) (millis - (((long) hour) * 3600000));
        int min = tmp / 60000;
        return makeTimeString(hour, min, (tmp - (min * 60000)) / 1000);
    }
}
