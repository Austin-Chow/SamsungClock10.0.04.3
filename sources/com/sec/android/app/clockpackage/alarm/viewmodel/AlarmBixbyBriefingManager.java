package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmSharedManager;
import com.sec.android.app.clockpackage.alarm.model.BixbyBriefingAlarmItem;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.Calendar;

public class AlarmBixbyBriefingManager {
    public static void requestBixbyBriefingInformationIntent(Context context, AlarmItem item) {
        if (item.isPossibleBixbyBriefingAlarm() && StateUtils.isPossibleStateForBixbyBriefing(context)) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(item.mAlarmAlertTime);
            int hours = c.get(11);
            int minutes = c.get(12);
            Intent requestBixbyBriefingIntent = new Intent("com.samsung.android.bixby.intent.action.BIXBY_ALARM");
            requestBixbyBriefingIntent.setPackage(Feature.hasPackage(context, "com.samsung.android.bixby.service") ? "com.samsung.android.bixby.service" : "com.samsung.android.bixby.agent");
            if (item.isNewCelebOn()) {
                requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.TTS_URI_ACTION", "request_weather");
            } else {
                requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.TTS_URI_ACTION", "request_tts");
            }
            requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_ID", item.mId);
            String timeString = getStringTime((hours * 100) + minutes);
            requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIME", timeString);
            requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIMESTAMP", item.mAlarmAlertTime);
            if (Feature.isSupportBixbyBriefingMenu(context) && !Feature.isSupportNewsCpForBixbyBriefing()) {
                requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_BRIEFING_NEWS_COUNT", 0);
            }
            Intent alarmIntent = new Intent("com.sec.android.app.clockpackage.intent.action.RESPONSE_BIXBY_ALARM");
            alarmIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_ID", item.mId);
            alarmIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIME_INT", (hours * 100) + minutes);
            alarmIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIMESTAMP", item.mAlarmAlertTime);
            alarmIntent.setPackage("com.sec.android.app.clockpackage");
            requestBixbyBriefingIntent.putExtra("com.samsung.android.bixby.intent.extra.TTS_URI_PENDING_INTENT", PendingIntent.getBroadcast(context, 0, alarmIntent, 134217728));
            context.sendBroadcast(requestBixbyBriefingIntent);
            Log.m41d("AlarmBixbyBriefingManager", "requestBixbyBriefingInformationIntent item.mId = " + item.mId + ", timeString = " + timeString);
        }
    }

    public static void receiveBixbyAlarm(Context context, Intent intent) {
        BixbyBriefingAlarmItem bixbyBriefingAlarmItem = new BixbyBriefingAlarmItem();
        bixbyBriefingAlarmItem.mIsSuccess = intent.getBooleanExtra("success", false);
        if (bixbyBriefingAlarmItem.mIsSuccess) {
            bixbyBriefingAlarmItem.mId = intent.getIntExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_ID", -1);
            bixbyBriefingAlarmItem.mAlarmTime = intent.getIntExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIME_INT", -1);
            bixbyBriefingAlarmItem.mAlarmAlertTime = intent.getLongExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIMESTAMP", -1);
            bixbyBriefingAlarmItem.mUri = (Uri) intent.getParcelableExtra("uri");
            bixbyBriefingAlarmItem.mPath = intent.getStringExtra("absolute_path");
            bixbyBriefingAlarmItem.mPlayTimeOfBixbyBriefing = intent.getLongExtra("play_time", 0);
            bixbyBriefingAlarmItem.mWeatherConditionCode = intent.getIntExtra("conditions_code", 999);
            bixbyBriefingAlarmItem.mWeatherCpLink = intent.getStringExtra("weather_cp_link");
            if (intent.hasExtra("is_day_time")) {
                int i;
                if (intent.getBooleanExtra("is_day_time", false)) {
                    i = 1;
                } else {
                    i = 2;
                }
                bixbyBriefingAlarmItem.mDaytime = i;
            } else {
                int alarmTime;
                if (bixbyBriefingAlarmItem.mAlarmTime < 0 || bixbyBriefingAlarmItem.mAlarmTime > 2359) {
                    Calendar c = Calendar.getInstance();
                    alarmTime = (c.get(11) * 100) + c.get(12);
                } else {
                    alarmTime = bixbyBriefingAlarmItem.mAlarmTime;
                }
                Log.m41d("AlarmBixbyBriefingManager", "bixbyBriefingAlarmItem alarmTime = " + alarmTime);
                if (alarmTime >= 600 && alarmTime <= 1759) {
                    bixbyBriefingAlarmItem.mDaytime = 1;
                } else if ((alarmTime >= 0 && alarmTime <= 559) || (alarmTime >= 1800 && alarmTime <= 2359)) {
                    bixbyBriefingAlarmItem.mDaytime = 2;
                }
            }
            if (Feature.DEBUG_ENG) {
                Log.secD("AlarmBixbyBriefingManager", "bixbyBriefingAlarmItem SPOKEN_TEXT = " + intent.getStringExtra("spoken_text"));
            }
        } else {
            bixbyBriefingAlarmItem.init();
        }
        Log.m41d("AlarmBixbyBriefingManager", "bixbyBriefingAlarmItem = " + bixbyBriefingAlarmItem.toString());
        AlarmSharedManager.saveBixbyBriefingInformation(context, bixbyBriefingAlarmItem);
        if (bixbyBriefingAlarmItem.mIsSuccess && !Alarm.isStopAlarmAlert && AlarmService.sAlarmAlertId == bixbyBriefingAlarmItem.mId && AlarmService.sAlarmAlertTime == bixbyBriefingAlarmItem.mAlarmAlertTime) {
            Intent iAlarmStarted = new Intent();
            iAlarmStarted.setAction("com.sec.android.app.clockpackage.intent.action.RECEIVE_BIXBY_ALARM");
            Parcel out = Parcel.obtain();
            bixbyBriefingAlarmItem.writeToParcel(out);
            out.setDataPosition(0);
            iAlarmStarted.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_BIXBY_BRIEFING_DATA", out.marshall());
            out.recycle();
            context.sendBroadcast(iAlarmStarted);
        }
    }

    public static void sendBixbyAlarmDeletionIntent(Context context, BixbyBriefingAlarmItem bixbyBriefingAlarmItem) {
        Intent requestBixbyAlarmIntent = new Intent("com.samsung.android.bixby.intent.action.BIXBY_ALARM");
        requestBixbyAlarmIntent.setPackage(Feature.hasPackage(context, "com.samsung.android.bixby.service") ? "com.samsung.android.bixby.service" : "com.samsung.android.bixby.agent");
        requestBixbyAlarmIntent.putExtra("com.samsung.android.bixby.intent.extra.TTS_URI_ACTION", "delete_tts");
        requestBixbyAlarmIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_ID", bixbyBriefingAlarmItem.mId);
        String timeString = getStringTime(bixbyBriefingAlarmItem.mAlarmTime);
        requestBixbyAlarmIntent.putExtra("com.samsung.android.bixby.intent.extra.BIXBY_ALARM_TIME", timeString);
        context.sendBroadcast(requestBixbyAlarmIntent);
        Log.secD("AlarmBixbyBriefingManager", "sendBixbyAlarmDeletionIntent mId = " + bixbyBriefingAlarmItem.mId + ", timeString = " + timeString);
    }

    public static String getStringTime(int alarmTime) {
        String timeString = "-1";
        if (alarmTime >= 0 && alarmTime <= 2359) {
            int minutes = alarmTime % 100;
            timeString = String.format("%02d:%02d", new Object[]{Integer.valueOf(alarmTime / 100), Integer.valueOf(minutes)});
        }
        Log.secD("AlarmBixbyBriefingManager", "getStringTime timeString = " + timeString + " , alarmTime = " + alarmTime);
        return timeString;
    }
}
