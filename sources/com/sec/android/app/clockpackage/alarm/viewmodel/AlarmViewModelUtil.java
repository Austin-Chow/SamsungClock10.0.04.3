package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import java.util.Calendar;

public class AlarmViewModelUtil {
    public static int checkDuplicationAlarm(Context context, AlarmItem item, boolean showToast) {
        Cursor cursor;
        int i;
        Log.secD("AlarmViewModelUtil", "checkDuplicationAlarm() mId = " + item.mId + ", mAlarmTime = " + item.mAlarmTime + ", mAlarmAlertTime = " + item.mAlarmAlertTime + ", mRepeatType = 0x" + Integer.toHexString(item.mRepeatType) + ", mAlarmName = " + item.mAlarmName + ", showToast = " + showToast);
        int duplicationAlarmItemId = -1;
        boolean isFirstMatch = false;
        String duplicatedAlarmMsg = null;
        String tempAlarmName = item.mAlarmName.replace("'", "''");
        if (item.isOneTimeAlarm()) {
            cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, "alarmtime = " + item.mAlarmTime + " AND " + "name" + "= '" + tempAlarmName + '\'', null, "createtime DESC");
        } else {
            cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, "alarmtime = " + item.mAlarmTime + " AND " + "repeattype" + " = " + item.mRepeatType + " AND " + "name" + "= '" + tempAlarmName + '\'', null, "createtime DESC");
        }
        if (cursor != null) {
            int cursorItemCnt = cursor.getCount();
            Log.secD("AlarmViewModelUtil", "checkDuplicationAlarm() / cursorItemCnt = " + cursorItemCnt);
            if (cursorItemCnt <= 0) {
                cursor.close();
                if (item.mId != -1 || AlarmProvider.getAlarmCount(context) < 50) {
                    if (showToast) {
                        AlarmUtil.saveMsg(context, item, null, 100);
                    }
                    i = -1;
                    return -1;
                }
                AlarmUtil.showMaxCountToast(context);
                i = -1;
                return -2;
            }
            cursor.moveToFirst();
            int i2;
            int cursorId;
            if (item.isOneTimeAlarm()) {
                Calendar itemCalendar = Calendar.getInstance();
                Calendar cursorCalendar = Calendar.getInstance();
                itemCalendar.setTimeInMillis(item.mAlarmAlertTime);
                for (i2 = 0; i2 < cursorItemCnt; i2++) {
                    cursorId = cursor.getInt(0);
                    int cursorRepeatType = cursor.getInt(5);
                    if (item.mId == cursorId || AlarmItem.isWeeklyAlarm(cursorRepeatType)) {
                        cursor.moveToNext();
                    } else {
                        int cursorDailyBriefing = cursor.getInt(11);
                        long cursorAlarmAlertTime = cursor.getLong(3);
                        cursorCalendar.setTimeInMillis(cursorAlarmAlertTime);
                        Log.secD("AlarmViewModelUtil", "______alarmAlertTime = " + itemCalendar.getTime().toString());
                        Log.secD("AlarmViewModelUtil", "cursorAlarmAlertTime = " + cursorCalendar.getTime().toString());
                        if (!(item.isSpecificDate() || AlarmItem.isSpecificDate(cursorDailyBriefing)) || cursorAlarmAlertTime == item.mAlarmAlertTime) {
                            if (!isFirstMatch) {
                                isFirstMatch = true;
                                duplicationAlarmItemId = cursorId;
                                Log.secD("AlarmViewModelUtil", "checkDuplicationAlarm() / 1 duplicationAlarmItemId = " + duplicationAlarmItemId);
                                if (item.mId != -1 && AlarmDataHandler.deleteAlarm(context, cursorId)) {
                                    AlarmNotificationHelper.clearNotification(context, cursorId);
                                }
                            } else if (AlarmDataHandler.deleteAlarm(context, cursorId)) {
                                AlarmNotificationHelper.clearNotification(context, cursorId);
                            }
                        }
                        cursor.moveToNext();
                    }
                }
            } else {
                for (i2 = 0; i2 < cursorItemCnt; i2++) {
                    cursorId = cursor.getInt(0);
                    if (item.mId == cursorId) {
                        cursor.moveToNext();
                    } else {
                        if (!isFirstMatch) {
                            isFirstMatch = true;
                            duplicationAlarmItemId = cursorId;
                            Log.secD("AlarmViewModelUtil", "checkDuplicationAlarm() / 2 duplicationAlarmItemId = " + duplicationAlarmItemId);
                            if (item.mId != -1 && AlarmDataHandler.deleteAlarm(context, cursorId)) {
                                AlarmNotificationHelper.clearNotification(context, cursorId);
                            }
                        } else if (AlarmDataHandler.deleteAlarm(context, cursorId)) {
                            AlarmNotificationHelper.clearNotification(context, cursorId);
                        }
                        cursor.moveToNext();
                    }
                }
            }
            if (duplicationAlarmItemId != -1) {
                String strAlarmTime = AlarmUtil.getAlarmTimeString(context, item.mAlarmTime);
                String duplicatedAlarm = null;
                try {
                    duplicatedAlarm = context.getResources().getString(C0490R.string.alarm_exist_update);
                } catch (NotFoundException e) {
                    Log.secE("AlarmViewModelUtil", "Exception : " + e.toString());
                }
                if (duplicatedAlarm != null) {
                    duplicatedAlarmMsg = String.format(duplicatedAlarm, new Object[]{strAlarmTime});
                }
            } else if (item.mId == -1 && AlarmProvider.getAlarmCount(context) >= 50) {
                AlarmUtil.showMaxCountToast(context);
                cursor.close();
                i = duplicationAlarmItemId;
                return -2;
            }
            Log.secD("AlarmViewModelUtil", "checkDuplicationAlarm() / 3 duplicationAlarmItemId = " + duplicationAlarmItemId);
            if (showToast) {
                if (cursor.getCount() <= 1) {
                    AlarmUtil.saveMsg(context, item, duplicatedAlarmMsg, 100);
                } else {
                    AlarmUtil.saveMsg(context, item, duplicatedAlarmMsg, 800);
                }
            }
            cursor.close();
        }
        i = duplicationAlarmItemId;
        return duplicationAlarmItemId;
    }

    public static int checkDuplicationAlarm(Context context, AlarmItem item) {
        return checkDuplicationAlarm(context, item, true);
    }

    public static void startAlarmService(Context context, Intent intent) {
        RuntimeException e;
        Log.secD("AlarmViewModelUtil", "->startAlarmService startForegroundService");
        try {
            Intent alert = new Intent(context, AlarmService.class);
            alert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA"));
            alert.putExtra("skip_to_check_old_alarm", intent.getBooleanExtra("skip_to_check_old_alarm", false));
            context.startForegroundService(alert);
            Logger.m47f("AlarmViewModelUtil", "startAlarmService");
        } catch (SecurityException e2) {
            e = e2;
            Log.m42e("AlarmViewModelUtil", "startAlarmService Exception : " + e.toString());
            Logger.m46e("AlarmViewModelUtil", "startAlarmService error");
            Log.secD("AlarmViewModelUtil", "<-startAlarmService");
        } catch (NullPointerException e3) {
            e = e3;
            Log.m42e("AlarmViewModelUtil", "startAlarmService Exception : " + e.toString());
            Logger.m46e("AlarmViewModelUtil", "startAlarmService error");
            Log.secD("AlarmViewModelUtil", "<-startAlarmService");
        }
        Log.secD("AlarmViewModelUtil", "<-startAlarmService");
    }

    public static void dismissAlarm(Context context, AlarmItem item, String action) {
        Log.secD("AlarmViewModelUtil", "->dismissAlarm item.mId = " + item.mId);
        AlarmNotificationHelper.clearNotification(context, item.mId);
        AlarmProvider.dismissAlarm(context, item, action);
    }
}
