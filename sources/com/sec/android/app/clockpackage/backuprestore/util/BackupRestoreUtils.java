package com.sec.android.app.clockpackage.backuprestore.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;

public class BackupRestoreUtils {
    public static void sendWorldclockChangedIntent(Context context) {
        Log.secD("BackupRestoreUtils", "sendWorldclockChangedIntent");
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.sec.android.app.clockpackage.worldclock.NOTIFY_WORLDCLOCK_CHANGE"));
    }

    public static void sendTimerPresetChangedIntent(Context context) {
        Log.secD("BackupRestoreUtils", "sendTimerPresetChangedIntent");
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.sec.android.app.clockpackage.timer.NOTIFY_TIMER_PRESET_CHANGED"));
    }

    public static void addBnRAlarmData(Context ctx, int oldId, int newId) {
        Log.secD("BNR_CLOCK_ALARM", "bnr_alarm : addBnRAlarm() / oldId=" + oldId + "/newId=" + newId);
        ctx.getSharedPreferences("BNR_ALARM", 0).edit().putInt(Integer.toString(oldId), newId).apply();
    }

    public static int getBnRAlarmData(Context ctx, String oldID) {
        int newId = ctx.getSharedPreferences("BNR_ALARM", 0).getInt(oldID, -1);
        Log.secD("BNR_CLOCK_ALARM", "bnr_alarm : getBnRAlarm() / oldID=" + oldID + "/newId=" + newId);
        return newId;
    }

    public static void clearBnRAlarmData(Context ctx) {
        Log.secD("BNR_CLOCK_ALARM", "bnr_alarm : clearBnRAlarm()");
        ctx.getSharedPreferences("BNR_ALARM", 0).edit().clear().apply();
    }

    public static void addBnRAlarmWidgetData(Context ctx, String widgetId, int alarmId) {
        Log.secD("BNR_CLOCK_ALARM", "bnr_alarmwidget : addBnRAlarmWidget() / widgetId=" + widgetId + "/alarmId=" + alarmId);
        ctx.getSharedPreferences("BNR_ALARM_WIDGET", 0).edit().putInt(widgetId, alarmId).apply();
    }

    public static String getAlarmDBPath(Context context) {
        String dbPath = StateUtils.getAlarmDBContext(context).getDatabasePath("alarm.db").getPath();
        Log.secD("BackupRestoreUtils", "getAlarmDBPath () / dbPath = " + dbPath);
        return dbPath;
    }
}
