package com.sec.android.app.clockpackage.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmSharedManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.NotificationChannelUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    private final Handler mCallNextAlertHandler = new Handler();
    private Context mContext = null;
    private final Handler mHandler = new Handler();

    /* renamed from: com.sec.android.app.clockpackage.alarm.receiver.AlarmReceiver$1 */
    class C05411 implements Runnable {
        C05411() {
        }

        public void run() {
            if (AlarmReceiver.this.mContext != null) {
                Log.secD("AlarmReceiver", "ACTION_THEME_APPLY 25s mCallNextAlertHandler.postDelayed(new Runnable() {");
                AlarmProvider.enableNextAlert(AlarmReceiver.this.mContext);
                AlarmReceiver.this.mContext = null;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.receiver.AlarmReceiver$2 */
    class C05422 implements Runnable {
        C05422() {
        }

        public void run() {
            Log.secD("AlarmReceiver", "mHandler.postDelayed(new Runnable() {");
            WakeLock.release();
        }
    }

    private static class WakeLock {
        private static android.os.PowerManager.WakeLock sWakeLock;

        private static synchronized void acquire(Context context) {
            synchronized (WakeLock.class) {
                if (sWakeLock == null || !sWakeLock.isHeld()) {
                    PowerManager pm = (PowerManager) context.getSystemService("power");
                    if (pm == null) {
                        Log.m42e("AlarmReceiver", "acquire pm is null");
                    } else {
                        sWakeLock = pm.newWakeLock(1, "AlarmReceiver");
                        sWakeLock.acquire(3000);
                    }
                }
            }
        }

        private static synchronized void release() {
            synchronized (WakeLock.class) {
                if (sWakeLock != null) {
                    if (sWakeLock.isHeld()) {
                        sWakeLock.release();
                    }
                    sWakeLock = null;
                }
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                WakeLock.acquire(context);
                Log.m41d("AlarmReceiver", "onReceive() : action = " + action.substring(action.lastIndexOf(46)));
                int i = -1;
                switch (action.hashCode()) {
                    case -2005837264:
                        if (action.equals("com.sec.android.clockpackage.SCLOUD_RESTORE_ALARM")) {
                            i = 2;
                            break;
                        }
                        break;
                    case -905063602:
                        if (action.equals("android.intent.action.LOCKED_BOOT_COMPLETED")) {
                            i = 0;
                            break;
                        }
                        break;
                    case -147579983:
                        if (action.equals("com.samsung.intent.action.EMERGENCY_STATE_CHANGED")) {
                            i = 9;
                            break;
                        }
                        break;
                    case -19011148:
                        if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                            i = 6;
                            break;
                        }
                        break;
                    case 502473491:
                        if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                            i = 4;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            i = 3;
                            break;
                        }
                        break;
                    case 798292259:
                        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                            i = 1;
                            break;
                        }
                        break;
                    case 1275858459:
                        if (action.equals("com.samsung.intent.action.BCS_REQUEST")) {
                            i = 8;
                            break;
                        }
                        break;
                    case 1294398883:
                        if (action.equals("com.samsung.android.theme.themecenter.THEME_APPLY")) {
                            i = 7;
                            break;
                        }
                        break;
                    case 1737074039:
                        if (action.equals("android.intent.action.MY_PACKAGE_REPLACED")) {
                            i = 5;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        if (!StateUtils.isDirectBootMode(context)) {
                            createPresetAlarm(context);
                            break;
                        } else {
                            updateAllAlarm(context, action);
                            break;
                        }
                    case 1:
                        updateAllAlarm(context, action);
                        createPresetAlarm(context);
                        AlarmSharedManager.setTimeZone(context);
                        break;
                    case 2:
                        updateAllAlarm(context, action);
                        AlarmProvider.sendChangeToAlarmWidget(context);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        if (StateUtils.isDirectBootMode(context) || StateUtils.isUserUnlockedDevice(context)) {
                            updateAllAlarm(context, action);
                            if ("android.intent.action.TIMEZONE_CHANGED".equals(action) || "android.intent.action.MY_PACKAGE_REPLACED".equals(action)) {
                                AlarmSharedManager.setTimeZone(context);
                                break;
                            }
                        }
                        return;
                        break;
                    case 6:
                        AlarmProvider.enableNextAlert(context);
                        NotificationChannelUtils.updateAllChannels(context);
                        break;
                    case 7:
                        this.mContext = context;
                        this.mCallNextAlertHandler.postDelayed(new C05411(), 25000);
                        break;
                    case 8:
                        ATCommand(context, intent);
                        break;
                    case 9:
                        if (!Alarm.isStopAlarmAlert) {
                            int reason = intent.getIntExtra("reason", 0);
                            if (reason == 2 || reason == 4) {
                                context.sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP"));
                                break;
                            }
                        }
                        break;
                }
                this.mHandler.postDelayed(new C05422(), 3000);
            }
        }
    }

    private void createPresetAlarm(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences("isSetDefault", 0);
            if (pref.getInt("alarmBootState", 0) == 0) {
                int alarmCnt = AlarmProvider.getAlarmCount(context);
                Editor ed = pref.edit();
                if (alarmCnt == 0) {
                    AlarmUtil.setPresetAlarm(context);
                    ed.putInt("alarmBootState", 1).apply();
                } else if (alarmCnt > 0) {
                    ed.putInt("alarmBootState", 2).apply();
                } else {
                    Log.m42e("AlarmReceiver", "createPresetAlarm(), alarmCnt = " + alarmCnt);
                }
            }
        } catch (IllegalStateException e) {
            Log.m42e("AlarmReceiver", "IllegalStateException e = " + e);
        }
    }

    private void updateAllAlarm(Context context, String action) {
        Log.secD("AlarmReceiver", "updateAllAlarm()");
        AlarmNotificationHelper.showSnoozeNotification(context, null, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
        ArrayList<Integer> changedAlarmIds = AlarmProvider.updateAlarmAsNewTime(context, action);
        int size = changedAlarmIds.size();
        Log.secD("AlarmReceiver", "changedAlarmIds.size : " + size);
        for (int indexItem = 0; indexItem < size; indexItem++) {
            AlarmNotificationHelper.clearNotification(context, ((Integer) changedAlarmIds.get(indexItem)).intValue());
        }
        AlarmProvider.enableNextAlert(context);
    }

    private void ATCommand(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        if (data == null) {
            Log.secD("AlarmReceiver", "there is no extras");
            return;
        }
        String command = data.getString("command");
        Log.secD("AlarmReceiver", "Alarm BCS_REQUEST : " + command + ' ' + data.getString("param"));
        if (command != null) {
            String REQUEST_AT_CMD_NUMBER_OF_ALARM = "AT+CALRM=NR";
            String REQUEST_AT_CMD_TIME_OF_ALARM = "AT+CALRM=";
            if ("AT+CALRM=NR".equals(command)) {
                Log.secD("AlarmReceiver", "Number of Alarm" + AlarmProvider.getAlarmCount(context));
                ClockUtils.sendBCSResponseIntent(context, Integer.toString(AlarmProvider.getAlarmCount(context)));
            } else if (!command.contains("AT+CALRM=")) {
            } else {
                if (AlarmProvider.getAlarmCount(context) <= 0) {
                    Log.secD("AlarmReceiver", "getAlarmCount" + AlarmProvider.getAlarmCount(context));
                    ClockUtils.sendBCSResponseIntent(context, "00:00");
                    return;
                }
                String index_str = command;
                StringBuilder alarmStrBuffer = new StringBuilder();
                index_str = index_str.replace("AT+CALRM=", "");
                Log.secD("AlarmReceiver", "Alarm REQUEST_AT_CMD_TIME_OF_ALARM index str " + index_str);
                AlarmItem item = AlarmProvider.getAlarm(context, Integer.parseInt(index_str) + 1);
                if (item == null) {
                    Log.secD("AlarmReceiver", "Not exist Alarm");
                    ClockUtils.sendBCSResponseIntent(context, "00:00");
                    return;
                }
                int hour = item.mAlarmTime / 100;
                int min = item.mAlarmTime % 100;
                Log.secD("AlarmReceiver", "Alarm hour: min " + hour + ':' + min);
                alarmStrBuffer.append(Integer.toString(hour));
                alarmStrBuffer.append(':');
                alarmStrBuffer.append(Integer.toString(min));
                ClockUtils.sendBCSResponseIntent(context, alarmStrBuffer.toString());
            }
        }
    }
}
