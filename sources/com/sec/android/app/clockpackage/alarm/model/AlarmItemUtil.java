package com.sec.android.app.clockpackage.alarm.model;

import com.sec.android.app.clockpackage.common.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmItemUtil {
    public static final int[] ALARM_SNOOZE_COUNT_TABLE = new int[]{1, 2, 3, 5, 10};
    public static final int[] ALARM_SNOOZE_DURATION_TABLE = new int[]{3, 5, 10, 15, 30};
    public static final int CONTINUOUSLY = ALARM_SNOOZE_COUNT_TABLE[4];

    public static int getNextAlertDayOffset(Calendar c, int repeatType) {
        int day = c.get(7);
        Log.secD("AlarmItemUtil", "getNextAlertDayOffset c = " + c.getTime().toString());
        Log.secD("AlarmItemUtil", "day = " + day);
        for (int i = 1; i <= 7; i++) {
            int nextDay = day + i;
            if (nextDay > 7) {
                nextDay -= 7;
            }
            int operator = 268435456 >> (nextDay * 4);
            if (((repeatType >> 4) & operator) > 0) {
                Log.secD("AlarmItemUtil", "nextDay = " + nextDay + "\n ___operator = 0x" + Integer.toHexString(operator) + "\n mRepeatType = 0x" + Integer.toHexString(repeatType) + "\n return i = " + i);
                return i;
            }
        }
        return 0;
    }

    public static String getTimeString(long ms) {
        return new SimpleDateFormat("YY-MM-dd HH:mm:ss", Locale.getDefault()).format(Long.valueOf(ms));
    }
}
