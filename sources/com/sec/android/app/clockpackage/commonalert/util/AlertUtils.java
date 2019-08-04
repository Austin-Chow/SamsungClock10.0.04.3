package com.sec.android.app.clockpackage.commonalert.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.TimerTask;

public class AlertUtils {

    public static class StopLedBackCoverTimerTask extends TimerTask {
        private Context mContext;
        private int mType;

        public StopLedBackCoverTimerTask(Context context, int type) {
            this.mContext = context;
            this.mType = type;
        }

        public void run() {
            Log.secD("AlertUtils", "send broadcast to LED side");
            Intent finishLEDIconIntent = new Intent(this.mType == 0 ? "com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT" : "com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
            finishLEDIconIntent.setPackage("com.sec.android.cover.ledcover");
            this.mContext.sendBroadcast(finishLEDIconIntent);
        }
    }

    public static void sendStopAlarmAlertIntent(Context context, boolean bTimeOut) {
        if (Feature.DEBUG_ENG) {
            Log.secV("AlertUtils", "sendStopAlarmAlertIntent bTimeOut = " + bTimeOut);
        }
        context.sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT"));
        if (!bTimeOut) {
            sendLocalStopAlarmAlertIntent(context);
        }
    }

    public static void sendLocalStopAlarmAlertIntent(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_STOP"));
    }

    public static void sendAlarmStartedIntent(Context context) {
        Intent alarmAlert = new Intent();
        alarmAlert.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT_FROM_ALARM");
        context.sendBroadcast(alarmAlert);
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_START"));
    }

    public static void sendAlarmStartedIntent(Context context, String alarmId, int id) {
        Intent iAlarmStarted = new Intent();
        iAlarmStarted.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
        iAlarmStarted.putExtra(alarmId, id);
        context.sendBroadcast(iAlarmStarted);
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_START"));
    }
}
