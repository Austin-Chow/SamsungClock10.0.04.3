package com.sec.android.app.clockpackage.alarm.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmViewModelUtil;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.CtsVoiceController;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmCTSHandleActivity extends Activity {
    private Intent intentSetAlarmSave;
    private final AlarmItem mItem = new AlarmItem();

    protected void onCreate(android.os.Bundle r9) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0060 in list [B:14:0x005d]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1603177117.run(Unknown Source)
*/
        /*
        r8 = this;
        r2 = 1;
        super.onCreate(r9);	 Catch:{ all -> 0x0094 }
        r5 = "AlarmCTSHandleActivity";	 Catch:{ all -> 0x0094 }
        r6 = "onCreate()";	 Catch:{ all -> 0x0094 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r5, r6);	 Catch:{ all -> 0x0094 }
        r1 = r8.getIntent();	 Catch:{ all -> 0x0094 }
        r5 = com.sec.android.app.clockpackage.common.util.CtsVoiceController.getController();	 Catch:{ all -> 0x0094 }
        r6 = r8.getApplicationContext();	 Catch:{ all -> 0x0094 }
        r5.setContext(r6);	 Catch:{ all -> 0x0094 }
        if (r1 == 0) goto L_0x005b;	 Catch:{ all -> 0x0094 }
    L_0x001c:
        r5 = r1.getAction();	 Catch:{ all -> 0x0094 }
        if (r5 == 0) goto L_0x005b;	 Catch:{ all -> 0x0094 }
    L_0x0022:
        r0 = r1.getAction();	 Catch:{ all -> 0x0094 }
        r6 = "AlarmCTSHandleActivity";	 Catch:{ all -> 0x0094 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0094 }
        r5.<init>();	 Catch:{ all -> 0x0094 }
        r7 = "onCreate() : action = ";	 Catch:{ all -> 0x0094 }
        r7 = r5.append(r7);	 Catch:{ all -> 0x0094 }
        r5 = ".";	 Catch:{ all -> 0x0094 }
        r5 = r0.contains(r5);	 Catch:{ all -> 0x0094 }
        if (r5 == 0) goto L_0x0061;	 Catch:{ all -> 0x0094 }
    L_0x003b:
        r5 = 46;	 Catch:{ all -> 0x0094 }
        r5 = r0.lastIndexOf(r5);	 Catch:{ all -> 0x0094 }
        r5 = r0.substring(r5);	 Catch:{ all -> 0x0094 }
    L_0x0045:
        r5 = r7.append(r5);	 Catch:{ all -> 0x0094 }
        r5 = r5.toString();	 Catch:{ all -> 0x0094 }
        com.sec.android.app.clockpackage.common.util.Log.m41d(r6, r5);	 Catch:{ all -> 0x0094 }
        r5 = -1;	 Catch:{ all -> 0x0094 }
        r6 = r0.hashCode();	 Catch:{ all -> 0x0094 }
        switch(r6) {
            case -805737507: goto L_0x0081;
            case 128174967: goto L_0x0077;
            case 252113103: goto L_0x0063;
            case 1112785375: goto L_0x006d;
            default: goto L_0x0058;
        };
    L_0x0058:
        switch(r5) {
            case 0: goto L_0x008b;
            case 1: goto L_0x0090;
            case 2: goto L_0x009b;
            case 3: goto L_0x00c3;
            default: goto L_0x005b;
        };
    L_0x005b:
        if (r2 == 0) goto L_0x0060;
    L_0x005d:
        r8.finish();
    L_0x0060:
        return;
    L_0x0061:
        r5 = r0;
        goto L_0x0045;
    L_0x0063:
        r6 = "android.intent.action.SET_ALARM";	 Catch:{ all -> 0x0094 }
        r6 = r0.equals(r6);	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x006b:
        r5 = 0;	 Catch:{ all -> 0x0094 }
        goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x006d:
        r6 = "android.intent.action.SHOW_ALARMS";	 Catch:{ all -> 0x0094 }
        r6 = r0.equals(r6);	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x0075:
        r5 = 1;	 Catch:{ all -> 0x0094 }
        goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x0077:
        r6 = "android.intent.action.DISMISS_ALARM";	 Catch:{ all -> 0x0094 }
        r6 = r0.equals(r6);	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x007f:
        r5 = 2;	 Catch:{ all -> 0x0094 }
        goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x0081:
        r6 = "android.intent.action.SNOOZE_ALARM";	 Catch:{ all -> 0x0094 }
        r6 = r0.equals(r6);	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x0089:
        r5 = 3;	 Catch:{ all -> 0x0094 }
        goto L_0x0058;	 Catch:{ all -> 0x0094 }
    L_0x008b:
        r8.handleSetAlarm(r1);	 Catch:{ all -> 0x0094 }
        r2 = 0;	 Catch:{ all -> 0x0094 }
        goto L_0x005b;	 Catch:{ all -> 0x0094 }
    L_0x0090:
        r8.handleShowAlarms();	 Catch:{ all -> 0x0094 }
        goto L_0x005b;
    L_0x0094:
        r5 = move-exception;
        if (r2 == 0) goto L_0x009a;
    L_0x0097:
        r8.finish();
    L_0x009a:
        throw r5;
    L_0x009b:
        r5 = r8.getApplicationContext();	 Catch:{ all -> 0x0094 }
        r5 = com.sec.android.app.clockpackage.alarm.model.AlarmProvider.getAlarmCount(r5);	 Catch:{ all -> 0x0094 }
        if (r5 <= 0) goto L_0x00b4;	 Catch:{ all -> 0x0094 }
    L_0x00a5:
        r8.handleShowAlarmsDelete();	 Catch:{ all -> 0x0094 }
        r4 = "Pick which alarm to dismiss";	 Catch:{ all -> 0x0094 }
        r5 = com.sec.android.app.clockpackage.common.util.CtsVoiceController.getController();	 Catch:{ all -> 0x0094 }
        r6 = "Pick which alarm to dismiss";	 Catch:{ all -> 0x0094 }
        r5.notifyVoiceSuccess(r8, r6);	 Catch:{ all -> 0x0094 }
        goto L_0x005b;	 Catch:{ all -> 0x0094 }
    L_0x00b4:
        r8.handleShowAlarms();	 Catch:{ all -> 0x0094 }
        r4 = "No scheduled alarms";	 Catch:{ all -> 0x0094 }
        r5 = com.sec.android.app.clockpackage.common.util.CtsVoiceController.getController();	 Catch:{ all -> 0x0094 }
        r6 = "No scheduled alarms";	 Catch:{ all -> 0x0094 }
        r5.notifyVoiceFailure(r8, r6);	 Catch:{ all -> 0x0094 }
        goto L_0x005b;	 Catch:{ all -> 0x0094 }
    L_0x00c3:
        r5 = com.sec.android.app.clockpackage.alarm.model.Alarm.isStopAlarmAlert;	 Catch:{ all -> 0x0094 }
        if (r5 != 0) goto L_0x00e5;	 Catch:{ all -> 0x0094 }
    L_0x00c7:
        r3 = new android.content.Intent;	 Catch:{ all -> 0x0094 }
        r3.<init>();	 Catch:{ all -> 0x0094 }
        r5 = "AlarmSnooze";	 Catch:{ all -> 0x0094 }
        r3.setAction(r5);	 Catch:{ all -> 0x0094 }
        r5 = r8.getApplicationContext();	 Catch:{ all -> 0x0094 }
        r5.sendBroadcast(r3);	 Catch:{ all -> 0x0094 }
        r4 = "alarm snoozed";	 Catch:{ all -> 0x0094 }
        r5 = com.sec.android.app.clockpackage.common.util.CtsVoiceController.getController();	 Catch:{ all -> 0x0094 }
        r6 = "alarm snoozed";	 Catch:{ all -> 0x0094 }
        r5.notifyVoiceSuccess(r8, r6);	 Catch:{ all -> 0x0094 }
        goto L_0x005b;	 Catch:{ all -> 0x0094 }
    L_0x00e5:
        r8.handleShowAlarms();	 Catch:{ all -> 0x0094 }
        r4 = "No firing alarms";	 Catch:{ all -> 0x0094 }
        r5 = com.sec.android.app.clockpackage.common.util.CtsVoiceController.getController();	 Catch:{ all -> 0x0094 }
        r6 = "No firing alarms";	 Catch:{ all -> 0x0094 }
        r5.notifyVoiceFailure(r8, r6);	 Catch:{ all -> 0x0094 }
        goto L_0x005b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.activity.AlarmCTSHandleActivity.onCreate(android.os.Bundle):void");
    }

    private void handleSetAlarm(Intent intent) {
        int minutes;
        Log.secD("AlarmCTSHandleActivity", "handleSetAlarm()");
        int hour = intent.getIntExtra("android.intent.extra.alarm.HOUR", -1);
        if (intent.hasExtra("android.intent.extra.alarm.MINUTES")) {
            minutes = intent.getIntExtra("android.intent.extra.alarm.MINUTES", -1);
        } else {
            minutes = 0;
        }
        if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59) {
            Log.secD("AlarmCTSHandleActivity", "call createAlarmIntent  hour = " + hour + " minutes" + minutes);
            Intent createAlarmIntent = new Intent();
            createAlarmIntent.setClass(this, AlarmEditActivity.class);
            createAlarmIntent.setType("alarm_create");
            createAlarmIntent.putExtra("widgetId", -1);
            PackageManager mPm = getApplicationContext().getPackageManager();
            if (mPm == null || mPm.queryIntentActivities(createAlarmIntent, 0).size() <= 0) {
                Log.secD("AlarmCTSHandleActivity", "Activity Not Found !");
            } else {
                startActivity(createAlarmIntent);
                String voiceMessage = "Invalid time";
                CtsVoiceController.getController().notifyVoiceFailure(this, "Invalid time");
            }
            finish();
            return;
        }
        setAlarm(intent);
    }

    private void setAlarm(Intent intent) {
        String voiceMessage;
        Log.secD("AlarmCTSHandleActivity", "setAlarm()");
        boolean skipUi = intent.getBooleanExtra("android.intent.extra.alarm.SKIP_UI", false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String str = "android.intent.extra.alarm.VIBRATE";
        boolean z = this.mItem.mAlarmSoundType == 2 && Feature.isSupportVibration(getApplicationContext());
        boolean vibrate = intent.getBooleanExtra(str, z);
        int hour = intent.getIntExtra("android.intent.extra.alarm.HOUR", calendar.get(11));
        int minutes = intent.getIntExtra("android.intent.extra.alarm.MINUTES", calendar.get(12));
        String message = intent.getStringExtra("android.intent.extra.alarm.MESSAGE");
        this.mItem.setCreateTime();
        if (message != null && message.length() > 0) {
            if (message.length() > 20) {
                this.mItem.mAlarmName = message.substring(0, 20);
            } else {
                this.mItem.mAlarmName = message;
            }
        }
        this.mItem.mAlarmTime = (hour * 100) + minutes;
        this.mItem.mAlarmAlertTime = this.mItem.mCreateTime;
        if (vibrate && Feature.isSupportVibration(getApplicationContext())) {
            this.mItem.mAlarmSoundType = 2;
        } else {
            this.mItem.mAlarmSoundType = 0;
        }
        Log.secD("AlarmCTSHandleActivity", "setAlarm() vibrate = " + vibrate + ", mAlarmSoundType = " + this.mItem.mAlarmSoundType);
        boolean isSupportCelebrity = Feature.isSupportCelebrityAlarm();
        boolean isSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(getApplicationContext());
        str = "alarm_tone_off_option";
        z = isSupportBixbyBriefing || isSupportCelebrity;
        boolean isAlarmToneOn = !intent.getBooleanExtra(str, z);
        str = "bixby_alarm";
        z = isSupportBixbyBriefing || isSupportCelebrity;
        boolean isBixbyBriefing = intent.getBooleanExtra(str, z);
        if (isAlarmToneOn && isBixbyBriefing) {
            isBixbyBriefing = isSupportBixbyBriefing || isSupportCelebrity;
        }
        if (isSupportCelebrity && isBixbyBriefing) {
            this.mItem.setSoundModeNewCeleb();
            this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        } else if (isBixbyBriefing) {
            this.mItem.setSoundModeNewBixby();
        } else {
            this.mItem.setSoundModeAlarmTone();
        }
        this.mItem.mSnoozeDoneCount = 0;
        this.mItem.mActivate = 1;
        AlarmRingtoneManager alarmRingtoneManager = new AlarmRingtoneManager(getApplicationContext());
        this.mItem.mSoundUri = alarmRingtoneManager.getAlarmTonePreference(getApplicationContext());
        String alert = intent.getStringExtra("android.intent.extra.alarm.RINGTONE");
        if (alert != null && ("silent".equals(alert) || alert.isEmpty())) {
            this.mItem.setMasterSoundOn(false);
        } else if (alert != null) {
            Uri uriBase = Uri.parse(alert);
            if (uriBase == null || !uriBase.getHost().equals("media") || PermissionUtils.hasPermissionExternalStorage(getBaseContext())) {
                Uri uri = ClockUtils.setMusicLibraryRingtone(uriBase, getApplicationContext(), 4);
                if (uri != null) {
                    this.mItem.mSoundUri = uri.toString();
                }
            } else {
                PermissionUtils.requestPermissions((Activity) this, 1);
                this.intentSetAlarmSave = intent;
                voiceMessage = "Alarm media permission error";
                CtsVoiceController.getController().notifyVoiceFailure(this, "Alarm media permission error");
                return;
            }
        }
        Log.secD("AlarmCTSHandleActivity", "mItem.mSoundUri :" + this.mItem.mSoundUri);
        this.mItem.mAlarmSoundTone = alarmRingtoneManager.getRingtoneIndex(this.mItem.mSoundUri);
        calendar.set(11, hour);
        calendar.set(12, minutes);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (calendar.getTimeInMillis() < this.mItem.mCreateTime) {
            calendar.add(7, 1);
        }
        int curDay = calendar.get(7);
        AlarmItem alarmItem;
        if (intent.hasExtra("android.intent.extra.alarm.DAYS")) {
            ArrayList<Integer> days = intent.getIntegerArrayListExtra("android.intent.extra.alarm.DAYS");
            if (days != null) {
                int[] repeatDays = new int[days.size()];
                int size = days.size();
                for (int indexItem = 0; indexItem < size; indexItem++) {
                    repeatDays[indexItem] = ((Integer) days.get(indexItem)).intValue();
                    alarmItem = this.mItem;
                    alarmItem.mRepeatType |= (1 << (((7 - repeatDays[indexItem]) + 1) * 4)) & -16;
                }
            }
            this.mItem.setWeeklyAlarm();
        } else {
            alarmItem = this.mItem;
            alarmItem.mRepeatType |= (1 << (((7 - curDay) + 1) * 4)) & -16;
            this.mItem.setOneTimeAlarm();
        }
        this.mItem.calculateFirstAlertTime(getApplicationContext());
        voiceMessage = "Alarm is set";
        if (AlarmUtil.checkSameAlarm(getApplicationContext(), this.mItem) != -1) {
            Log.secD("AlarmCTSHandleActivity", "Exist same alarm, do not insert/update db");
            AlarmUtil.sendAlarmDeleteModeUpdate(getApplicationContext());
            CtsVoiceController.getController().notifyVoiceSuccess(this, "Alarm is set");
            finish();
            return;
        }
        long transactionResult;
        int id = AlarmViewModelUtil.checkDuplicationAlarm(getApplicationContext(), this.mItem);
        if (id == -1) {
            transactionResult = AlarmProvider.getId(getApplicationContext().getContentResolver().insert(AlarmProvider.CONTENT_URI, this.mItem.getContentValues()));
        } else {
            transactionResult = (long) getApplicationContext().getContentResolver().update(AlarmProvider.CONTENT_URI, this.mItem.getContentValues(), "_id = " + id, null);
        }
        if (transactionResult > 0) {
            AlarmProvider.enableNextAlert(this);
            AlarmUtil.saveMsg(getApplicationContext(), this.mItem, null, 600);
            AlarmUtil.sendAlarmDeleteModeUpdate(getApplicationContext());
        }
        if (!skipUi) {
            handleShowAlarms();
        }
        CtsVoiceController.getController().notifyVoiceSuccess(this, "Alarm is set");
        finish();
    }

    private void handleShowAlarms() {
        Log.secD("AlarmCTSHandleActivity", "handleShowAlarms()");
        Intent intent = new Intent(this, getAlarmMainClass());
        intent.putExtra("AlarmLaunchMode", 3);
        startActivity(intent);
        finish();
    }

    private void handleShowAlarmsDelete() {
        Log.secD("AlarmCTSHandleActivity", "handleShowAlarms()");
        Intent intent = new Intent(this, getAlarmMainClass());
        intent.putExtra("AlarmLaunchMode", 3);
        intent.putExtra("AlarmDeleteMode", true);
        startActivity(intent);
        finish();
    }

    private static Class<?> getAlarmMainClass() {
        try {
            return Class.forName("com.sec.android.app.clockpackage.alarm.activity.AlarmWidgetListActivity");
        } catch (ClassNotFoundException e) {
            Log.secE("AlarmCTSHandleActivity", "getAlarmMainClass ClassNotFoundException");
            return ClockActivity.class;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    finish();
                    return;
                } else if (this.intentSetAlarmSave != null) {
                    try {
                        setAlarm(this.intentSetAlarmSave);
                        return;
                    } catch (Exception e) {
                        finish();
                        return;
                    }
                } else {
                    return;
                }
            default:
                finish();
                return;
        }
    }
}
