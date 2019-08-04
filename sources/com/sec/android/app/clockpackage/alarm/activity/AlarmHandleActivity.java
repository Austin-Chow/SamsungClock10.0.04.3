package com.sec.android.app.clockpackage.alarm.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.app.SemMultiWindowManager;
import com.sec.android.app.clockpackage.common.util.Log;

public class AlarmHandleActivity extends Activity {
    Context mContext = null;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r8) {
        /*
        r7 = this;
        r7.mContext = r7;
        super.onCreate(r8);	 Catch:{ all -> 0x0092 }
        r1 = r7.getIntent();	 Catch:{ all -> 0x0092 }
        if (r1 == 0) goto L_0x004a;
    L_0x000b:
        r3 = r1.getAction();	 Catch:{ all -> 0x0092 }
        if (r3 == 0) goto L_0x004a;
    L_0x0011:
        r0 = r1.getAction();	 Catch:{ all -> 0x0092 }
        r4 = "AlarmHandleActivity";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0092 }
        r3.<init>();	 Catch:{ all -> 0x0092 }
        r5 = "onCreate() : action = ";
        r5 = r3.append(r5);	 Catch:{ all -> 0x0092 }
        r3 = ".";
        r3 = r0.contains(r3);	 Catch:{ all -> 0x0092 }
        if (r3 == 0) goto L_0x004e;
    L_0x002a:
        r3 = 46;
        r3 = r0.lastIndexOf(r3);	 Catch:{ all -> 0x0092 }
        r3 = r0.substring(r3);	 Catch:{ all -> 0x0092 }
    L_0x0034:
        r3 = r5.append(r3);	 Catch:{ all -> 0x0092 }
        r3 = r3.toString();	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.common.util.Log.m41d(r4, r3);	 Catch:{ all -> 0x0092 }
        r3 = -1;
        r4 = r0.hashCode();	 Catch:{ all -> 0x0092 }
        switch(r4) {
            case -1210379830: goto L_0x0078;
            case -1072515174: goto L_0x006e;
            case 155606184: goto L_0x0082;
            case 229746792: goto L_0x0050;
            case 1202060552: goto L_0x005a;
            case 1499710301: goto L_0x0064;
            default: goto L_0x0047;
        };
    L_0x0047:
        switch(r3) {
            case 0: goto L_0x008c;
            case 1: goto L_0x0097;
            case 2: goto L_0x00a2;
            case 3: goto L_0x00a6;
            case 4: goto L_0x00b6;
            case 5: goto L_0x00bc;
            default: goto L_0x004a;
        };
    L_0x004a:
        r7.finish();
        return;
    L_0x004e:
        r3 = r0;
        goto L_0x0034;
    L_0x0050:
        r4 = "com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_CHINA_DB_NO_UPDATE";
        r4 = r0.equals(r4);	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0047;
    L_0x0058:
        r3 = 0;
        goto L_0x0047;
    L_0x005a:
        r4 = "com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_CHINA_DB_UPDATE";
        r4 = r0.equals(r4);	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0047;
    L_0x0062:
        r3 = 1;
        goto L_0x0047;
    L_0x0064:
        r4 = "com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_CLEAR";
        r4 = r0.equals(r4);	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0047;
    L_0x006c:
        r3 = 2;
        goto L_0x0047;
    L_0x006e:
        r4 = "com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_DISMISS";
        r4 = r0.equals(r4);	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0047;
    L_0x0076:
        r3 = 3;
        goto L_0x0047;
    L_0x0078:
        r4 = "com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_CLEAR_SNOOZE_ALL";
        r4 = r0.equals(r4);	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0047;
    L_0x0080:
        r3 = 4;
        goto L_0x0047;
    L_0x0082:
        r4 = "com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM";
        r4 = r0.equals(r4);	 Catch:{ all -> 0x0092 }
        if (r4 == 0) goto L_0x0047;
    L_0x008a:
        r3 = 5;
        goto L_0x0047;
    L_0x008c:
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper.clearChinaDbUpdatingNotification(r3);	 Catch:{ all -> 0x0092 }
        goto L_0x004a;
    L_0x0092:
        r3 = move-exception;
        r7.finish();
        throw r3;
    L_0x0097:
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper.clearChinaDbUpdatingNotification(r3);	 Catch:{ all -> 0x0092 }
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil.sendOpenCalendarSettingIntent(r3);	 Catch:{ all -> 0x0092 }
        goto L_0x004a;
    L_0x00a2:
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper.clearMissedAlarm();	 Catch:{ all -> 0x0092 }
        goto L_0x004a;
    L_0x00a6:
        r2 = new com.sec.android.app.clockpackage.alarm.model.AlarmItem;	 Catch:{ all -> 0x0092 }
        r2.<init>();	 Catch:{ all -> 0x0092 }
        r2.readFromIntent(r1);	 Catch:{ all -> 0x0092 }
        if (r2 == 0) goto L_0x004a;
    L_0x00b0:
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmViewModelUtil.dismissAlarm(r3, r2, r0);	 Catch:{ all -> 0x0092 }
        goto L_0x004a;
    L_0x00b6:
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper.clearAllSnoozedNotification(r3);	 Catch:{ all -> 0x0092 }
        goto L_0x004a;
    L_0x00bc:
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper.clearMissedAlarm();	 Catch:{ all -> 0x0092 }
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        r3 = com.sec.android.app.clockpackage.common.util.StateUtils.isContextInDexMode(r3);	 Catch:{ all -> 0x0092 }
        if (r3 != 0) goto L_0x00d1;
    L_0x00c7:
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        r4 = 0;
        r5 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.sBixbyBriefWeatherConditionCode;	 Catch:{ all -> 0x0092 }
        r6 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService.sBixbyBriefDaytime;	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper.showSnoozeNotification(r3, r4, r5, r6);	 Catch:{ all -> 0x0092 }
    L_0x00d1:
        r3 = r7.mContext;	 Catch:{ all -> 0x0092 }
        r7.launchClockPackage(r3);	 Catch:{ all -> 0x0092 }
        goto L_0x004a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.activity.AlarmHandleActivity.onCreate(android.os.Bundle):void");
    }

    private void launchClockPackage(Context context) {
        Log.secD("AlarmHandleActivity", "launchClockPackage");
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
        Intent alert = new Intent("android.intent.action.MAIN");
        alert.putExtra("clockpackage.select.tab", 0);
        alert.addCategory("android.intent.category.LAUNCHER");
        alert.setFlags(268468224);
        alert.setComponent(cn);
        if (new SemMultiWindowManager().getMode() == 1) {
            context.startActivity(alert, setWindowingLaunchMode().toBundle());
        } else {
            context.startActivity(alert);
        }
    }

    private ActivityOptions setWindowingLaunchMode() {
        ActivityOptions opt = ActivityOptions.makeBasic();
        try {
            ActivityOptions.class.getMethod("setForceLaunchWindowingMode", new Class[]{Integer.TYPE}).invoke(opt, new Object[]{Integer.valueOf(5)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opt;
    }
}
