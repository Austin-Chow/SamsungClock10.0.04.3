package com.sec.android.app.clockpackage.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import java.util.ArrayList;
import java.util.TimeZone;

public class AlarmSharedManager {
    private static int getPreDismissedAlarmCount(Context context) {
        int preDismissedAlarmCount = 0;
        try {
            preDismissedAlarmCount = ClockUtils.getBootAwareContext(context).getSharedPreferences("PreDismissedAlarmIds", 0).getInt("PreDismissedAlarmIdCount", 0);
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
        Log.secD("AlarmSharedManager", "getPreDismissedAlarmCount preDismissedAlarmCount = " + preDismissedAlarmCount);
        return preDismissedAlarmCount;
    }

    public static ArrayList<Integer> getPreDismissedAlarmIds(Context context) {
        ArrayList<Integer> dismissedUpcomingAlarmIds = new ArrayList();
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("PreDismissedAlarmIds", 0);
            int length = pref.getInt("PreDismissedAlarmIdCount", 0);
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int dismissedUpcomingAlarmId = pref.getInt("PreDismissedAlarmIds" + (i + 1), 0);
                dismissedUpcomingAlarmIds.add(Integer.valueOf(dismissedUpcomingAlarmId));
                log.append(dismissedUpcomingAlarmId).append(' ');
            }
            Log.secD("AlarmSharedManager", "getPreDismissedAlarmIds = " + log);
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
        return dismissedUpcomingAlarmIds;
    }

    public static Long getClosetAlertTimeInPreDismissedAlarms(Context context, long currentTime) {
        long closetAlertTime = -1;
        int count = getPreDismissedAlarmCount(context);
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("PreDismissedAlarmIds", 0);
            for (int i = count; i > 0; i--) {
                long alarmAlertTime = pref.getLong("PreDismissedAlertTimes" + i, -1);
                int id = pref.getInt("PreDismissedAlarmIds" + i, 0);
                Log.secD("AlarmSharedManager", "getClosetAlertTimeInPreDismissedAlarms i = " + i + " id = " + id + " alarmAlertTime = " + alarmAlertTime);
                if (alarmAlertTime < currentTime) {
                    Log.secD("AlarmSharedManager", "continue id = " + id + ", alarmAlertTime = " + alarmAlertTime);
                    removePreDismissedAlarmId(context, id);
                } else if (currentTime < alarmAlertTime && (closetAlertTime == -1 || alarmAlertTime < closetAlertTime)) {
                    closetAlertTime = alarmAlertTime;
                }
            }
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
        Log.secD("AlarmSharedManager", "getClosetAlertTimeInPreDismissedAlarms closetAlertTime = " + closetAlertTime);
        return Long.valueOf(closetAlertTime);
    }

    public static int getTimeZone(Context context) {
        int curTimeZone = 999;
        try {
            curTimeZone = ClockUtils.getBootAwareContext(context).getSharedPreferences("DB_SET_TIMEZONE", 0).getInt("DB_SET_TIMEZONE", 999);
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
        Log.secD("AlarmSharedManager", "getTimeZone curTimeZone = " + curTimeZone);
        return curTimeZone;
    }

    public static void setTimeZone(Context context) {
        int curTimeZone = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        try {
            Editor ed = ClockUtils.getBootAwareContext(context).getSharedPreferences("DB_SET_TIMEZONE", 0).edit();
            ed.putInt("DB_SET_TIMEZONE", curTimeZone);
            ed.apply();
            Log.secD("AlarmSharedManager", "setTimeZone curTimeZone = " + curTimeZone);
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static void remainValidPreDismissedInformation(Context context) {
        Log.secD("AlarmSharedManager", "remainValidPreDismissedInformation");
        long currentTime = System.currentTimeMillis();
        int count = getPreDismissedAlarmCount(context);
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("PreDismissedAlarmIds", 0);
            for (int i = count; i > 0; i--) {
                long alarmAlertTime = pref.getLong("PreDismissedAlertTimes" + i, -1);
                if (alarmAlertTime < currentTime) {
                    int id = pref.getInt("PreDismissedAlarmIds" + i, 0);
                    Log.secD("AlarmSharedManager", "remainValidPreDismissedInformation id = " + id + ", alarmAlertTime = " + alarmAlertTime);
                    removePreDismissedAlarmId(context, id);
                }
            }
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static int[] getUpcomingAlarmIds(Context context) {
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("UpcomingAlarmIds", 0);
            int length = pref.getInt("UpcomingAlarmIdCount", 0);
            int[] upcomingAlarmIds = new int[length];
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < length; i++) {
                upcomingAlarmIds[i] = pref.getInt("UpcomingAlarmIds" + (i + 1), 0);
                log.append(upcomingAlarmIds[i]).append(' ');
            }
            Log.secD("AlarmSharedManager", "getUpcomingAlarmIds = " + log);
            return upcomingAlarmIds;
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
            return new int[0];
        }
    }

    public static int getSnoozeAlarmCount(Context context) {
        int snoozeAlarmCount = 0;
        try {
            snoozeAlarmCount = ClockUtils.getBootAwareContext(context).getSharedPreferences("SnoozedAlarmIDs", 0).getInt("SnoozedAlarmID_Count", 0);
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
        return snoozeAlarmCount;
    }

    public static int[] getSnoozeAlarmIds(Context context) {
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("SnoozedAlarmIDs", 0);
            int length = pref.getInt("SnoozedAlarmID_Count", 0);
            int[] iArr = new int[length];
            for (int i = 0; i < length; i++) {
                iArr[i] = pref.getInt("SnoozedAlarmIDs" + (i + 1), 0);
            }
            return iArr;
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
            return new int[0];
        }
    }

    public static int getWeatherConditionCode(Context context, int id) {
        int weatherCode = 999;
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("SnoozedAlarmIDs", 0);
            int i;
            if (pref.contains("SnoozedAlarmID_Count")) {
                int length = pref.getInt("SnoozedAlarmID_Count", 0);
                for (int i2 = 0; i2 < length; i2++) {
                    if (pref.getInt("SnoozedAlarmIDs" + (i2 + 1), 0) == id) {
                        weatherCode = pref.getInt("BixbyBriefingWeatherConditionCodes" + (i2 + 1), 999);
                        break;
                    }
                }
                Log.secD("AlarmSharedManager", "getWeatherConditionCode id = " + id + ", weatherCode = " + weatherCode);
                i = weatherCode;
                return weatherCode;
            }
            Log.secD("AlarmSharedManager", "getWeatherConditionCode id = " + id + ", !pref.contains(SNOOZED_ALARM_ID_COUNT) -1");
            i = 999;
            return 999;
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static int getWeatherDaytime(Context context, int id) {
        int daytime = 0;
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("SnoozedAlarmIDs", 0);
            int i;
            if (pref.contains("SnoozedAlarmID_Count")) {
                int length = pref.getInt("SnoozedAlarmID_Count", 0);
                for (int i2 = 0; i2 < length; i2++) {
                    if (pref.getInt("SnoozedAlarmIDs" + (i2 + 1), 0) == id) {
                        daytime = pref.getInt("BixbyBriefingDaytimes" + (i2 + 1), 0);
                        break;
                    }
                }
                Log.secD("AlarmSharedManager", "getWeatherDaytime id = " + id + ", daytime = " + daytime);
                i = daytime;
                return daytime;
            }
            Log.secD("AlarmSharedManager", "getWeatherDaytime id = " + id + ", !pref.contains(SNOOZED_ALARM_ID_COUNT) -1");
            i = 0;
            return 0;
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static void addPreDismissedAlarmInformation(Context context, int id, long alertTime) {
        if (!getPreDismissedAlarmIds(context).contains(Integer.valueOf(id))) {
            try {
                SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("PreDismissedAlarmIds", 0);
                int length = pref.getInt("PreDismissedAlarmIdCount", 0) + 1;
                Editor ed = pref.edit();
                ed.putInt("PreDismissedAlarmIdCount", length);
                ed.putInt("PreDismissedAlarmIds" + length, id);
                ed.putLong("PreDismissedAlertTimes" + length, alertTime);
                ed.apply();
                Log.secD("AlarmSharedManager", "addPreDismissedAlarmInformation id = " + id + " alertTime = " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(alertTime)));
                Logger.m47f("AlarmSharedManager", "PreDismissed " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(alertTime)));
            } catch (IllegalStateException e) {
                Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
            }
        }
    }

    public static boolean removePreDismissedAlarmId(Context context, int id) {
        boolean bRemoved;
        boolean bRemoved2 = false;
        if (getPreDismissedAlarmIds(context).contains(Integer.valueOf(id))) {
            try {
                SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("PreDismissedAlarmIds", 0);
                if (pref.contains("PreDismissedAlarmIdCount")) {
                    int i;
                    int length = pref.getInt("PreDismissedAlarmIdCount", 0);
                    int[] alarmIds = new int[length];
                    int[] tmpAlarmIds = new int[length];
                    long[] alertTimes = new long[length];
                    long[] tmpAlertTimes = new long[length];
                    int tmp = 0;
                    int alarmPosition = -1;
                    for (i = 0; i < length; i++) {
                        alarmIds[i] = pref.getInt("PreDismissedAlarmIds" + (i + 1), 0);
                        alertTimes[i] = pref.getLong("PreDismissedAlertTimes" + (i + 1), -1);
                        if (alarmIds[i] == id) {
                            alarmPosition = i;
                        } else {
                            tmpAlarmIds[tmp] = alarmIds[i];
                            tmpAlertTimes[tmp] = alertTimes[i];
                            tmp++;
                        }
                    }
                    if (alarmPosition != -1) {
                        Editor ed = pref.edit();
                        ed.putInt("PreDismissedAlarmIdCount", length - 1);
                        for (i = 0; i < length - 1; i++) {
                            ed.putInt("PreDismissedAlarmIds" + (i + 1), tmpAlarmIds[i]);
                            ed.putLong("PreDismissedAlertTimes" + (i + 1), tmpAlertTimes[i]);
                        }
                        ed.remove("PreDismissedAlarmIds" + length);
                        ed.remove("PreDismissedAlertTimes" + length);
                        ed.apply();
                        bRemoved2 = true;
                    }
                } else {
                    Log.secD("AlarmSharedManager", "removePreDismissedAlarmId id = " + id + "return false");
                    bRemoved = false;
                    return false;
                }
            } catch (IllegalStateException e) {
                Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
            }
        }
        Log.secD("AlarmSharedManager", "removePreDismissedAlarmId id = " + id + ", bRemoved = " + bRemoved2);
        bRemoved = bRemoved2;
        return bRemoved2;
    }

    public static void addUpcomingAlarmIds(Context context, int id) {
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("UpcomingAlarmIds", 0);
            int length = pref.getInt("UpcomingAlarmIdCount", 0) + 1;
            Log.secD("AlarmSharedManager", "addUpcomingAlarmIds id = " + id);
            if (id == pref.getInt("UpcomingAlarmIds" + (length - 1), 0)) {
                Log.secD("AlarmSharedManager", "addUpcomingAlarmIds return");
                return;
            }
            Editor ed = pref.edit();
            ed.putInt("UpcomingAlarmIdCount", length);
            ed.putInt("UpcomingAlarmIds" + length, id);
            ed.apply();
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static boolean removeUpcomingAlarmId(Context context, int id) {
        boolean bRemoved = false;
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("UpcomingAlarmIds", 0);
            boolean z;
            if (pref.contains("UpcomingAlarmIdCount")) {
                int i;
                int length = pref.getInt("UpcomingAlarmIdCount", 0);
                int[] alarmIds = new int[length];
                int[] tmpAlarmIds = new int[length];
                int tmp = 0;
                int alarmPosition = -1;
                for (i = 0; i < length; i++) {
                    alarmIds[i] = pref.getInt("UpcomingAlarmIds" + (i + 1), 0);
                    if (alarmIds[i] == id) {
                        alarmPosition = i;
                    } else {
                        tmpAlarmIds[tmp] = alarmIds[i];
                        tmp++;
                    }
                }
                if (alarmPosition != -1) {
                    Editor ed = pref.edit();
                    ed.putInt("UpcomingAlarmIdCount", length - 1);
                    for (i = 0; i < length - 1; i++) {
                        ed.putInt("UpcomingAlarmIds" + (i + 1), tmpAlarmIds[i]);
                    }
                    ed.remove("UpcomingAlarmIds" + length);
                    ed.apply();
                    bRemoved = true;
                }
                Log.secD("AlarmSharedManager", "removeUpcomingAlarmId id = " + id + ", bRemoved = " + bRemoved);
                z = bRemoved;
                return bRemoved;
            }
            Log.secD("AlarmSharedManager", "removeUpcomingAlarmId id = " + id + "return false");
            z = false;
            return false;
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static void addSnoozedAlarmId(Context context, int id, int bixbyBriefWeatherConditionCode, int bixbyBriefDaytime) {
        int snoozedAlarmCount = getSnoozeAlarmCount(context);
        int[] snoozedAlarmIds = getSnoozeAlarmIds(context);
        int i = 0;
        while (i < snoozedAlarmCount) {
            if (snoozedAlarmIds[i] != id) {
                i++;
            } else {
                return;
            }
        }
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("SnoozedAlarmIDs", 0);
            int length = pref.getInt("SnoozedAlarmID_Count", 0) + 1;
            if (id != pref.getInt("SnoozedAlarmIDs" + (length - 1), 0)) {
                Editor ed = pref.edit();
                ed.putInt("SnoozedAlarmID_Count", length);
                ed.putInt("SnoozedAlarmIDs" + length, id);
                ed.putInt("BixbyBriefingWeatherConditionCodes" + length, bixbyBriefWeatherConditionCode);
                ed.putInt("BixbyBriefingDaytimes" + length, bixbyBriefDaytime);
                ed.apply();
            }
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static boolean removeSnoozeAlarmId(Context context, int id) {
        boolean bRemoved = false;
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("SnoozedAlarmIDs", 0);
            boolean z;
            if (pref.contains("SnoozedAlarmID_Count")) {
                int i;
                int length = pref.getInt("SnoozedAlarmID_Count", 0);
                int[] alarmIds = new int[length];
                int[] tmpAlarmIds = new int[length];
                int[] weatherConditionCodes = new int[length];
                int[] tmpWeatherConditionCodes = new int[length];
                int[] dayTimes = new int[length];
                int[] tmpDayTimes = new int[length];
                int tmp = 0;
                int alarmPosition = -1;
                for (i = 0; i < length; i++) {
                    alarmIds[i] = pref.getInt("SnoozedAlarmIDs" + (i + 1), 0);
                    weatherConditionCodes[i] = pref.getInt("BixbyBriefingWeatherConditionCodes" + (i + 1), 999);
                    dayTimes[i] = pref.getInt("BixbyBriefingDaytimes" + (i + 1), 0);
                    if (alarmIds[i] == id) {
                        alarmPosition = i;
                    } else {
                        tmpAlarmIds[tmp] = alarmIds[i];
                        tmpWeatherConditionCodes[tmp] = weatherConditionCodes[i];
                        tmpDayTimes[tmp] = dayTimes[i];
                        tmp++;
                    }
                }
                if (alarmPosition != -1) {
                    Editor ed = pref.edit();
                    ed.putInt("SnoozedAlarmID_Count", length - 1);
                    for (i = 0; i < length - 1; i++) {
                        ed.putInt("SnoozedAlarmIDs" + (i + 1), tmpAlarmIds[i]);
                        ed.putInt("BixbyBriefingWeatherConditionCodes" + (i + 1), tmpWeatherConditionCodes[i]);
                        ed.putInt("BixbyBriefingDaytimes" + (i + 1), tmpDayTimes[i]);
                    }
                    ed.remove("SnoozedAlarmIDs" + length);
                    ed.apply();
                    bRemoved = true;
                }
                Log.secD("AlarmSharedManager", "removeSnoozeAlarmId id = " + id + ", bRemoved = " + bRemoved);
                z = bRemoved;
                return bRemoved;
            }
            Log.secD("AlarmSharedManager", "removeSnoozeAlarmId id = " + id + " return false");
            z = false;
            return false;
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static void saveBixbyBriefingInformation(Context context, BixbyBriefingAlarmItem bixbyBriefingAlarmItem) {
        try {
            Editor ed = ClockUtils.getBootAwareContext(context).getSharedPreferences("BixbyBriefingInformation", 0).edit();
            ed.putBoolean("BixbyBriefingAlarmSuccess", bixbyBriefingAlarmItem.mIsSuccess);
            ed.putInt("BixbyBriefingAlarmId", bixbyBriefingAlarmItem.mId);
            ed.putInt("BixbyBriefingAlarmTime", bixbyBriefingAlarmItem.mAlarmTime);
            ed.putLong("BixbyBriefingAlarmAlertTime", bixbyBriefingAlarmItem.mAlarmAlertTime);
            if (bixbyBriefingAlarmItem.mUri != null) {
                ed.putString("BixbyBriefingUri", bixbyBriefingAlarmItem.mUri.toString());
            }
            ed.putString("BixbyBriefingPath", bixbyBriefingAlarmItem.mPath);
            ed.putLong("BixbyBriefingPlayTime", bixbyBriefingAlarmItem.mPlayTimeOfBixbyBriefing);
            ed.putInt("BixbyBriefingWeatherConditionCode", bixbyBriefingAlarmItem.mWeatherConditionCode);
            ed.putString("BixbyBriefingWeatherCpLink", bixbyBriefingAlarmItem.mWeatherCpLink);
            ed.putInt("BixbyBriefingDaytime", bixbyBriefingAlarmItem.mDaytime);
            ed.apply();
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
    }

    public static BixbyBriefingAlarmItem getBixbyBriefingInformation(Context context) {
        Uri uri = null;
        BixbyBriefingAlarmItem bixbyBriefingAlarmItem = new BixbyBriefingAlarmItem();
        try {
            SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("BixbyBriefingInformation", 0);
            bixbyBriefingAlarmItem.mIsSuccess = pref.getBoolean("BixbyBriefingAlarmSuccess", false);
            bixbyBriefingAlarmItem.mId = pref.getInt("BixbyBriefingAlarmId", -1);
            bixbyBriefingAlarmItem.mAlarmTime = pref.getInt("BixbyBriefingAlarmTime", -1);
            bixbyBriefingAlarmItem.mAlarmAlertTime = pref.getLong("BixbyBriefingAlarmAlertTime", -1);
            String uriString = pref.getString("BixbyBriefingUri", null);
            if (uriString != null) {
                uri = Uri.parse(uriString);
            }
            bixbyBriefingAlarmItem.mUri = uri;
            bixbyBriefingAlarmItem.mPath = pref.getString("BixbyBriefingPath", "");
            bixbyBriefingAlarmItem.mPlayTimeOfBixbyBriefing = pref.getLong("BixbyBriefingPlayTime", 0);
            bixbyBriefingAlarmItem.mWeatherConditionCode = pref.getInt("BixbyBriefingWeatherConditionCode", 999);
            bixbyBriefingAlarmItem.mWeatherCpLink = pref.getString("BixbyBriefingWeatherCpLink", "");
            bixbyBriefingAlarmItem.mDaytime = pref.getInt("BixbyBriefingDaytime", 0);
            Log.m41d("AlarmSharedManager", "getBixbyBriefingInformation bixbyBriefingAlarmItem = " + bixbyBriefingAlarmItem.toString());
        } catch (IllegalStateException e) {
            Log.m42e("AlarmSharedManager", "IllegalStateException e = " + e);
        }
        return bixbyBriefingAlarmItem;
    }
}
