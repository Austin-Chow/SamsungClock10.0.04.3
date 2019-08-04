package com.sec.android.app.clockpackage.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.Settings.System;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.model.AlarmSharedManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmBixbyBriefingManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmEcbmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmPlayer;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmViewModelUtil;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import com.sec.android.app.clockpackage.common.util.PowerController;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class AlarmSecurityReceiver extends BroadcastReceiver {
    private static CallListener mCallPendingAlarmListener = null;
    private static CallListener mCallPendingAlarmListenerForSIMSLOT2 = null;
    private final Handler mHandler = new Handler();
    private final Handler mHandler2 = new Handler();
    private final PowerController mPowerController = new PowerController();

    /* renamed from: com.sec.android.app.clockpackage.alarm.receiver.AlarmSecurityReceiver$1 */
    class C05431 implements Runnable {
        C05431() {
        }

        public void run() {
            Log.secD("AlarmSecurityReceiver", "mHandler.postDelayed(new Runnable() {");
            if (AlarmSecurityReceiver.this.mPowerController != null) {
                AlarmSecurityReceiver.this.mPowerController.releasePartial();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.receiver.AlarmSecurityReceiver$2 */
    class C05442 implements Runnable {
        C05442() {
        }

        public void run() {
            Log.secD("AlarmSecurityReceiver", "mHandler2.postDelayed(new Runnable() {");
            if (AlarmSecurityReceiver.this.mPowerController != null) {
                AlarmSecurityReceiver.this.mPowerController.releaseDim();
            }
        }
    }

    private class CallListener extends PhoneStateListener {
        private Context mContext;
        private Intent mIntent;
        private int mPendingEvent = 0;

        /* renamed from: com.sec.android.app.clockpackage.alarm.receiver.AlarmSecurityReceiver$CallListener$1 */
        class C05451 implements Runnable {
            C05451() {
            }

            public void run() {
                AlarmItem item = new AlarmItem();
                if (!(CallListener.this.mIntent == null || CallListener.this.mContext == null)) {
                    item.readFromIntent(CallListener.this.mIntent);
                    AlarmViewModelUtil.startAlarmService(CallListener.this.mContext, CallListener.this.mIntent);
                }
                if (!(CallListener.this.mContext == null || item == null)) {
                    AlarmSecurityReceiver.this.showNotification(CallListener.this.mContext, item);
                    AlarmSecurityReceiver.this.updateAndEnableNextAlarm(CallListener.this.mContext, item);
                }
                AlarmSecurityReceiver.this.destroyListener(1);
            }
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if (StateUtils.isDualSlot(this.mContext)) {
                int i = 0;
                int i2 = 0;
                try {
                    i = StateUtils.getMultiSimCallState(this.mContext, 0);
                    i2 = StateUtils.getMultiSimCallState(this.mContext, 1);
                    Log.secD("AlarmSecurityReceiver", "callStateForSIMSLOT1: " + i + " callStateForSIMSLOT2: " + i2);
                } catch (NullPointerException e) {
                    Log.secE("AlarmSecurityReceiver", "onCallStateChanged Exception");
                }
                if (state == 0 && callStateForSIMSLOT1 == 0 && callStateForSIMSLOT2 == 0) {
                    state = 0;
                } else if (state == 1 || callStateForSIMSLOT1 == 1 || callStateForSIMSLOT2 == 1) {
                    state = 1;
                } else if (state == 2 || callStateForSIMSLOT1 == 2 || callStateForSIMSLOT2 == 2) {
                    state = 2;
                }
            }
            if (state == 0) {
                Log.secD("AlarmSecurityReceiver", "TelephonyManager.CALL_STATE_IDLE");
                long currentTimeMillis = System.currentTimeMillis();
                if (this.mPendingEvent == 1 && this.mIntent != null && this.mContext != null) {
                    this.mPendingEvent = 0;
                    AlarmItem item = new AlarmItem();
                    item.readFromIntent(this.mIntent);
                    int delay_time = item.isFirstAlarm() ? item.getSnoozeDurationMinutes() : 60000;
                    Log.secD("AlarmSecurityReceiver", "delay_time = " + delay_time + " item : " + item.toStringShort());
                    if (!Alarm.isStopAlarmAlert && item.mAlarmAlertTime <= currentTimeMillis && currentTimeMillis < item.mAlarmAlertTime + ((long) delay_time)) {
                        Logger.m47f("AlarmSecurityReceiver", "end Call startAlarmService");
                        if (ClockUtils.timerAlertTimeInCall == 0 || ClockUtils.timerAlertTimeInCall >= ClockUtils.alarmAlertTimeInCall) {
                            AlarmViewModelUtil.startAlarmService(this.mContext, this.mIntent);
                            AlarmSecurityReceiver.this.showNotification(this.mContext, item);
                            AlarmSecurityReceiver.this.updateAndEnableNextAlarm(this.mContext, item);
                            AlarmSecurityReceiver.this.destroyListener(1);
                            return;
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(ClockUtils.timerAlertTimeInCall);
                        Log.secD("AlarmSecurityReceiver", "timerAlertTimeInCall : " + cal.getTime().toString());
                        cal.setTimeInMillis(ClockUtils.alarmAlertTimeInCall);
                        Log.secD("AlarmSecurityReceiver", "alarmAlertTimeInCall : " + cal.getTime().toString());
                        new Handler().postDelayed(new C05451(), 500);
                    } else if (!Alarm.isStopAlarmAlert && item.mAlarmAlertTime + ((long) delay_time) < currentTimeMillis) {
                        Log.secD("AlarmSecurityReceiver", "item.mAlarmAlertTime + delay_time < currentTimeMillis");
                        ClockUtils.alarmAlertTimeInCall = 0;
                        if (item != null) {
                            if (!item.mSnoozeActivate) {
                                AlarmNotificationHelper.showMissedNotification(this.mContext, item);
                            } else if (item.getSnoozeRepeatTimes() == item.mSnoozeDoneCount) {
                                AlarmNotificationHelper.showMissedNotification(this.mContext, item);
                            } else {
                                AlarmNotificationHelper.showSnoozeNotification(this.mContext, item, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
                            }
                        }
                        AlarmSecurityReceiver.this.destroyListener(1);
                    }
                }
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (this.mPowerController != null) {
                    this.mPowerController.acquirePartial(context, "AlarmSecurityReceiver");
                }
                Log.m41d("AlarmSecurityReceiver", "onReceive() : action = " + (action.contains(".") ? action.substring(action.lastIndexOf(46)) : action));
                Object obj = -1;
                switch (action.hashCode()) {
                    case -1631777497:
                        if (action.equals("com.sec.android.clockpackage.SET_ALARM")) {
                            obj = 11;
                            break;
                        }
                        break;
                    case -1238412078:
                        if (action.equals("com.sec.android.app.clockpackage.alarm.REMOVE_PRE_DISMISSED_ALARMS")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case -1096441623:
                        if (action.equals("com.sec.android.clockpackage.EDIT_ALARM")) {
                            obj = 9;
                            break;
                        }
                        break;
                    case -1087785841:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.UPCOMING_ALERT")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case -484237174:
                        if (action.equals("com.sec.android.clockpackage.DELETE_ALARM")) {
                            obj = 7;
                            break;
                        }
                        break;
                    case -386189105:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_CLEAR_SNOOZE_ALL_COVERSTATE")) {
                            obj = 14;
                            break;
                        }
                        break;
                    case -383827326:
                        if (action.equals("com.sec.android.app.clockpackage.intent.action.RESPONSE_BIXBY_ALARM")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 64644877:
                        if (action.equals("com.samsung.sec.android.clockpackage.DIRECT_ALARM_STOP")) {
                            obj = 10;
                            break;
                        }
                        break;
                    case 676385023:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_DISMISS_COVERSTATE")) {
                            obj = 13;
                            break;
                        }
                        break;
                    case 1168516041:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.UPCOMING_BIXBY_BRIEFING_ALERT")) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 1505084421:
                        if (action.equals("com.sec.android.clockpackage.DIRECT_EDIT_ALARM")) {
                            obj = 6;
                            break;
                        }
                        break;
                    case 1537957849:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.EXPLICIT_ALARM_ALERT")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 1734239312:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_DIRECTSAVED")) {
                            obj = 8;
                            break;
                        }
                        break;
                    case 1745527755:
                        if (action.equals("com.sec.android.clockpackage.REQUEST_ALARM_BACKUPVERSION")) {
                            obj = 12;
                            break;
                        }
                        break;
                    case 2074631718:
                        if (action.equals("com.sec.android.clockpackage.ADD_ALARM")) {
                            obj = 5;
                            break;
                        }
                        break;
                }
                int alarmId;
                AlarmItem item;
                switch (obj) {
                    case null:
                        Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT");
                        receiveAlarmAlertIntent(context, intent);
                        break;
                    case 1:
                        AlarmSharedManager.remainValidPreDismissedInformation(context);
                        AlarmProvider.enableNextPreDismissedAlert(context);
                        break;
                    case 2:
                        AlarmItem alarmItem = new AlarmItem();
                        alarmItem.readFromIntent(intent);
                        AlarmBixbyBriefingManager.requestBixbyBriefingInformationIntent(context, alarmItem);
                        break;
                    case 3:
                        AlarmItem upcomingItem = new AlarmItem();
                        upcomingItem.readFromIntent(intent);
                        if (intent.getBooleanExtra("direct_register_upcoming", false)) {
                            try {
                                int i;
                                int id;
                                ArrayList<Integer> remainUpcomingAlarmIds = new ArrayList();
                                ArrayList<Integer> candidateUpcomingAlarmIds = AlarmProvider.getNextAlarmWithin5m(context);
                                for (int id2 : AlarmSharedManager.getUpcomingAlarmIds(context)) {
                                    if (candidateUpcomingAlarmIds.contains(Integer.valueOf(id2))) {
                                        remainUpcomingAlarmIds.add(Integer.valueOf(id2));
                                    } else {
                                        AlarmNotificationHelper.clearUpcomingNotification(context, id2);
                                    }
                                }
                                int length = candidateUpcomingAlarmIds.size();
                                for (i = 0; i < length; i++) {
                                    id2 = ((Integer) candidateUpcomingAlarmIds.get(i)).intValue();
                                    if (remainUpcomingAlarmIds.contains(Integer.valueOf(id2))) {
                                        Log.secD("AlarmSecurityReceiver", "remainUpcomingAlarmIds.contains id : " + id2);
                                    } else if (id2 != upcomingItem.mId) {
                                        AlarmNotificationHelper.showUpcomingNotification(context, id2);
                                    } else if (System.currentTimeMillis() + 58000 < upcomingItem.mAlarmAlertTime) {
                                        AlarmNotificationHelper.showUpcomingNotification(context, id2);
                                    }
                                }
                            } catch (Throwable th) {
                                AlarmProvider.enableNextUpcomingAlertAfter5m(context);
                            }
                        } else if (AlarmUtil.needToShowAlarmAlert(context, upcomingItem)) {
                            AlarmNotificationHelper.showUpcomingNotification(context, upcomingItem.mId);
                        }
                        AlarmProvider.enableNextUpcomingAlertAfter5m(context);
                        break;
                    case 4:
                        AlarmBixbyBriefingManager.receiveBixbyAlarm(context, intent);
                        break;
                    case 5:
                        int mSVoiceReqId = intent.getIntExtra("svoice_req_id", -1);
                        if (AlarmProvider.getAlarmCount(context) < 50) {
                            int result = launchDirectAddAlarm(context, intent);
                            Log.secD("AlarmSecurityReceiver", "JavaE : ACTION_ADD_ALARM /launchDirectAddAlarm() result = " + result + "/mSVoiceReqId=" + mSVoiceReqId);
                            if (mSVoiceReqId != -1) {
                                sendAddAlarmResult(context, mSVoiceReqId, result);
                                break;
                            }
                        }
                        AlarmUtil.showMaxCountToast(context);
                        Log.secD("AlarmSecurityReceiver", "JavaE : ACTION_ADD_ALARM / Alarm Max Size / mSVoiceReqId=" + mSVoiceReqId);
                        if (mSVoiceReqId != -1) {
                            sendAddAlarmResult(context, mSVoiceReqId, -2);
                            break;
                        }
                        break;
                    case 6:
                        launchDirectEditAlarm(context, intent);
                        break;
                    case 7:
                        alarmId = intent.getIntExtra("listitemId", -1);
                        boolean isDeleteAll = intent.getBooleanExtra("deleteall", false);
                        if (!Feature.DEBUG_ENG || !isDeleteAll) {
                            if (AlarmDataHandler.deleteAlarm(context, alarmId)) {
                                AlarmNotificationHelper.clearNotification(context, alarmId);
                                break;
                            }
                        } else if (AlarmProvider.getAlarmCount(context) > 0) {
                            ArrayList<Integer> alarmIds = AlarmDataHandler.getAllAlarmIds(context);
                            Iterator it = alarmIds.iterator();
                            while (it.hasNext()) {
                                int alarmDeleteId = ((Integer) it.next()).intValue();
                                Log.secD("AlarmSecurityReceiver", "deleteAllAlarms alarmDeleteId = " + alarmDeleteId);
                                AlarmUtil.sendStopAlertByChangeIntent(context, alarmDeleteId);
                                AlarmNotificationHelper.clearNotification(context, alarmDeleteId);
                            }
                            AlarmDataHandler.deleteAllAlarms(context, alarmIds);
                            break;
                        }
                        break;
                    case 8:
                        String str = intent.getStringExtra("save_msg");
                        if (str != null) {
                            Toast.makeText(context, str, 1).show();
                            break;
                        }
                        break;
                    case 9:
                        launchEditAlarm(context, intent.getIntExtra("listitemId", -1));
                        break;
                    case 10:
                        alarmId = intent.getIntExtra("_id", -1);
                        boolean bDismiss = intent.getBooleanExtra("bDismiss", false);
                        if (bDismiss) {
                            item = AlarmProvider.getAlarm(context, alarmId);
                            if (item != null) {
                                AlarmViewModelUtil.dismissAlarm(context, item, null);
                            }
                        }
                        Log.secD("AlarmSecurityReceiver", "Id= " + alarmId + ",bDismiss= " + bDismiss);
                        break;
                    case 11:
                        if (AlarmProvider.getAlarmCount(context) < 50) {
                            launchSetAlarm(context, intent);
                            break;
                        } else {
                            AlarmUtil.showMaxCountToast(context);
                            break;
                        }
                    case 12:
                        sendAlarmBackupVersion(context);
                        break;
                    case 13:
                        item = new AlarmItem();
                        item.readFromIntent(intent);
                        if (item != null) {
                            AlarmViewModelUtil.dismissAlarm(context, item, action);
                            break;
                        }
                        break;
                    case 14:
                        AlarmNotificationHelper.clearAllSnoozedNotification(context);
                        break;
                }
                this.mHandler.postDelayed(new C05431(), 3000);
            }
        }
    }

    private void receiveAlarmAlertIntent(Context context, Intent intent) {
        AlarmItem item = new AlarmItem();
        item.readFromIntent(intent);
        Log.m41d("AlarmSecurityReceiver", "item = " + item.toString());
        if (Feature.isSupportAlarmOptionMenuForWorkingDay()) {
            if (StateUtils.isUltraPowerSavingMode(context) && !Feature.hasActivity(context, new Intent("com.sec.android.intent.calendar.setting"))) {
                AlarmNotificationHelper.showToAddCalendarNotification(context);
            } else if (item.isWorkingDay() && !AlarmUtil.isValidChinaDB(context)) {
                AlarmNotificationHelper.showChinaDbUpdatingNotification(context);
            }
        }
        AlarmNotificationHelper.clearNotification(context, item.mId);
        int notificationType = getAlertNotificationType(context, item, intent.getAction());
        boolean bVideoCall = StateUtils.isVideoCall(context);
        if (notificationType == 1) {
            if (AlarmProvider.isEcbm(context)) {
                executeEcbmService(context, intent);
                Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT ECBM");
            } else if (!StateUtils.isInCallState(context) || bVideoCall) {
                if (bVideoCall) {
                    beepSound(context);
                    Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT BEEP VIDEO_CALL");
                }
                AlarmViewModelUtil.startAlarmService(context, intent);
            } else {
                showNotification(context, item);
                AlarmUtil.setStopAlarmAlertValue(false);
                if (!StateUtils.isRecordingState(context)) {
                    beepSound(context);
                    Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT BEEP");
                }
                ClockUtils.alarmAlertTimeInCall = item.mAlarmAlertTime;
                waitForCallEnd(context, intent, 1);
            }
            updateAndEnableNextAlarm(context, item);
        } else if (notificationType == 2) {
            updateAndEnableNextAlarm(context, item);
            AlarmItem itemReal = AlarmProvider.getAlarm(context, item.mId);
            if (!(item == null || itemReal == null)) {
                if (itemReal.isFirstAlarm() || itemReal.mActivate == 0) {
                    AlarmNotificationHelper.showMissedNotification(context, item);
                } else if (itemReal.mActivate == 2) {
                    AlarmNotificationHelper.showSnoozeNotification(context, item, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
                }
            }
        }
        AlarmUtil.sendAlarmAlertIntent(context, intent);
    }

    private int launchDirectAddAlarm(Context context, Intent intent) {
        Log.secD("AlarmSecurityReceiver", "launchDirectAddAlarm");
        AlarmItem mItem = new AlarmItem();
        int result = -1;
        int i;
        if (intent.hasExtra("android.intent.extra.alarm.HOUR") || Feature.DEBUG_ENG) {
            Calendar c;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour = intent.getIntExtra("android.intent.extra.alarm.HOUR", calendar.get(11));
            int minutes = intent.getIntExtra("android.intent.extra.alarm.MINUTES", calendar.get(12));
            int count = intent.getIntExtra("add_count", 0);
            if (Feature.DEBUG_ENG && count > 0) {
                Log.secD("AlarmSecurityReceiver", "add +1m alarm count = " + count);
                c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.add(12, 1);
                hour = c.get(11);
                minutes = c.get(12);
            }
            String message = intent.getStringExtra("android.intent.extra.alarm.MESSAGE");
            int alarm_repeat = intent.getIntExtra("alarm_repeat", 0);
            int alarm_EveryWeekRepeat = intent.getIntExtra("alarm_everyweekrepeat", -1);
            int alarm_specific_date = intent.getIntExtra("alarm_specific_date", 0);
            int alarmSnoozeActivate = intent.getIntExtra("alarm_snooze", 1);
            int alarmVolume = intent.getIntExtra("alarm_volume", 11);
            boolean bBixbyDeleteMode = intent.getBooleanExtra("bixby_delete_mode", false);
            mItem.mSnoozeActivate = alarmSnoozeActivate == 1;
            mItem.mSnoozeDuration = intent.getIntExtra("alarm_snooze_minutes", 1);
            mItem.mSnoozeRepeat = intent.getIntExtra("alarm_snooze_counts", 2);
            String str = "android.intent.extra.alarm.VIBRATE";
            boolean z = mItem.mAlarmSoundType == 2 && Feature.isSupportVibration(context);
            boolean vibrate = intent.getBooleanExtra(str, z);
            if (!vibrate) {
                mItem.mAlarmSoundType = 0;
            }
            boolean isSupportCelebrity = Feature.isSupportCelebrityAlarm();
            boolean isSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(context);
            str = "alarm_tone_off_option";
            z = isSupportBixbyBriefing || isSupportCelebrity;
            boolean isAlarmToneOn = !intent.getBooleanExtra(str, z);
            str = "bixby_alarm";
            z = isSupportBixbyBriefing || isSupportCelebrity;
            boolean isBixbyBriefing = intent.getBooleanExtra(str, z);
            if (isAlarmToneOn && isBixbyBriefing) {
                isAlarmToneOn = (isSupportBixbyBriefing || isSupportCelebrity) ? false : true;
                isBixbyBriefing = isSupportBixbyBriefing || isSupportCelebrity;
            }
            if (isSupportCelebrity && isBixbyBriefing) {
                mItem.setSoundModeNewCeleb();
                mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
            } else if (isBixbyBriefing) {
                mItem.setSoundModeNewBixby();
            } else {
                mItem.setSoundModeAlarmTone();
            }
            boolean isTtsAlarm = intent.getBooleanExtra("tts_alarm", false);
            mItem.setTtsAlarm(isTtsAlarm);
            mItem.setHoliday(intent.getBooleanExtra("alarm_holiday_active", false));
            if (Feature.DEBUG_ENG) {
                Log.secD("AlarmSecurityReceiver", "vibrate = " + vibrate + " mItem.mAlarmSoundType = " + mItem.mAlarmSoundType);
                Log.secD("AlarmSecurityReceiver", "isAlarmToneOn = " + isAlarmToneOn + " isSupportBixbyBriefing : " + isSupportBixbyBriefing + " isBixbyBriefing : " + isBixbyBriefing + " isSupportCelebrity : " + isSupportCelebrity);
                Log.secD("AlarmSecurityReceiver", "time : " + hour + ':' + minutes + " alarm_specific_date :" + alarm_specific_date);
                Log.secD("AlarmSecurityReceiver", "alarmSnoozeActivate : " + alarmSnoozeActivate + " mAlarmVolume : " + alarmVolume);
                Log.secD("AlarmSecurityReceiver", "isTtsAlarm = " + isTtsAlarm);
            }
            if (alarmVolume != 11 && alarmVolume >= 0 && alarmVolume <= 15) {
                mItem.mAlarmVolume = alarmVolume;
            }
            Log.secD("AlarmSecurityReceiver", "alarm_repeat = 0x" + Integer.toHexString(alarm_repeat));
            Log.secD("AlarmSecurityReceiver", "alarm_EveryWeekRepeat = " + alarm_EveryWeekRepeat);
            mItem.setCreateTime();
            if (message != null && message.length() > 0) {
                if (message.length() > 20) {
                    mItem.mAlarmName = message.substring(0, 20);
                } else {
                    mItem.mAlarmName = message;
                }
            }
            mItem.mAlarmTime = (hour * 100) + minutes;
            mItem.mAlarmAlertTime = mItem.mCreateTime;
            mItem.mSnoozeDoneCount = 0;
            if (intent.getBooleanExtra("alarm_activate", true)) {
                mItem.mActivate = 1;
            } else {
                mItem.mActivate = 0;
            }
            AlarmRingtoneManager alarmRingtoneManager = new AlarmRingtoneManager(context);
            mItem.mSoundUri = alarmRingtoneManager.getAlarmTonePreference(context);
            mItem.mAlarmSoundTone = alarmRingtoneManager.getRingtoneIndex(mItem.mSoundUri);
            Log.secD("AlarmSecurityReceiver", "mItem.mSoundUri :" + mItem.mSoundUri);
            if (alarm_specific_date == 1) {
                mItem.setSpecificDate(true);
                long alarm_AlertTime = intent.getLongExtra("alarm_alerttime", -1);
                if (alarm_AlertTime <= mItem.mCreateTime) {
                    Log.secD("AlarmSecurityReceiver", "Can't create alarm of previous time.");
                    i = -1;
                    return -1;
                }
                mItem.mAlarmAlertTime = alarm_AlertTime;
                c = Calendar.getInstance();
                c.setTimeInMillis(mItem.mAlarmAlertTime);
                mItem.mAlarmTime = (c.get(11) * 100) + c.get(12);
                int tempRepeatType = 0 & 15;
                mItem.mRepeatType |= (((((1 << (((7 - c.get(7)) + 1) * 4)) & -16) | 0) >> 4) << 4) & -16;
                mItem.setOneTimeAlarm();
            } else {
                calendar.set(11, hour);
                calendar.set(12, minutes);
                calendar.set(13, 0);
                calendar.set(14, 0);
                if (calendar.getTimeInMillis() < mItem.mCreateTime) {
                    calendar.add(7, 1);
                }
                int curDay = calendar.get(7);
                if (alarm_repeat == 0) {
                    mItem.mRepeatType |= (1 << (((7 - curDay) + 1) * 4)) & -16;
                } else {
                    mItem.mRepeatType |= alarm_repeat << 4;
                }
                if (alarm_EveryWeekRepeat == -1 || alarm_EveryWeekRepeat == 0) {
                    mItem.setOneTimeAlarm();
                } else if (alarm_EveryWeekRepeat == 1) {
                    mItem.setWeeklyAlarm();
                }
                mItem.calculateFirstAlertTime(context);
            }
            if (AlarmUtil.checkSameAlarm(context, mItem) != -1) {
                Log.secD("AlarmSecurityReceiver", "Exist same alarm, do not insert/update db");
                AlarmUtil.sendAlarmDeleteModeUpdate(context);
                i = -1;
                return 2;
            }
            long transactionResult;
            int id = AlarmViewModelUtil.checkDuplicationAlarm(context, mItem);
            if (id == -1) {
                transactionResult = AlarmProvider.getId(context.getContentResolver().insert(AlarmProvider.CONTENT_URI, mItem.getContentValues()));
            } else {
                transactionResult = (long) context.getContentResolver().update(AlarmProvider.CONTENT_URI, mItem.getContentValues(), "_id = " + id, null);
            }
            if (transactionResult > 0) {
                AlarmProvider.enableNextAlert(context);
                result = id == -1 ? 1 : 3;
                if (!bBixbyDeleteMode) {
                    AlarmUtil.sendAlarmDeleteModeUpdate(context);
                }
            }
            i = result;
            return result;
        }
        Log.secD("AlarmSecurityReceiver", "EXTRA_HOUR is no");
        i = -1;
        return -1;
    }

    private void launchDirectEditAlarm(Context context, Intent intent) {
        int listItemID = intent.getIntExtra("listitemId", -1);
        Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
        int hour = intent.getIntExtra("android.intent.extra.alarm.HOUR", -1);
        int minutes = intent.getIntExtra("android.intent.extra.alarm.MINUTES", -1);
        int alarm_repeat = intent.getIntExtra("alarm_repeat", 0);
        int alarm_EveryWeekRepeat = intent.getIntExtra("alarm_everyweekrepeat", -1);
        boolean alarm_activate = intent.getBooleanExtra("alarm_activate", false);
        if (Feature.DEBUG_ENG) {
            Log.secD("AlarmSecurityReceiver", "launchDirectEditAlarm listItemID = " + listItemID + " time : " + hour + ':' + minutes + " alarm_repeat : 0x" + Integer.toHexString(alarm_repeat) + " alarm_EveryWeekRepeat : " + alarm_EveryWeekRepeat + " alarm_activate :" + alarm_activate);
        }
        AlarmItem item = AlarmProvider.getAlarm(context, listItemID);
        if (item != null) {
            if (!(hour == -1 && minutes == -1 && alarm_repeat == 0 && alarm_EveryWeekRepeat != 0 && alarm_EveryWeekRepeat != 1)) {
                item.setCreateTime();
                item.mSnoozeDoneCount = 0;
                item.mActivate = 1;
            }
            if (hour >= 0 && hour < 24 && minutes >= 0 && minutes < 60) {
                item.mAlarmTime = (hour * 100) + minutes;
            }
            if (alarm_repeat != 0) {
                item.mRepeatType &= 15;
                item.mRepeatType |= alarm_repeat << 4;
            }
            if (alarm_EveryWeekRepeat == 0) {
                item.setOneTimeAlarm();
            } else if (alarm_EveryWeekRepeat == 1) {
                item.setWeeklyAlarm();
            }
            if (alarm_activate) {
                item.mActivate = 1;
                if (item.isSpecificDate()) {
                    item.mSnoozeDoneCount = 0;
                    if (System.currentTimeMillis() > item.mAlarmAlertTime) {
                        item.setSpecificDate(false);
                    }
                }
                if (!item.isSpecificDate()) {
                    if (item.isOneTimeAlarm()) {
                        item.mSnoozeDoneCount = 0;
                        Calendar c = Calendar.getInstance();
                        int curHour = c.get(11);
                        int curMinute = c.get(12);
                        int curDay = c.get(7);
                        if ((curHour * 100) + curMinute >= item.mAlarmTime) {
                            Log.secD("AlarmSecurityReceiver", "saveAlarmRepeat() - (curHour * 100 + (mHour * 100 + mMin) )");
                            c.add(6, 1);
                            curDay = c.get(7);
                            Log.secD("AlarmSecurityReceiver", "curDay = " + curDay);
                        }
                        item.mRepeatType &= 15;
                        item.mRepeatType |= (1 << (((7 - curDay) + 1) * 4)) & -16;
                        int checkDay = item.getAlarmRepeat();
                        Log.secD("AlarmSecurityReceiver", "saveAlarmRepeat() : checkDay = 0x" + Integer.toHexString(checkDay));
                        int result = 0 | ((checkDay << 4) & -16);
                        Log.secD("AlarmSecurityReceiver", "result = 0x" + Integer.toHexString(result));
                        item.mRepeatType = result | 1;
                        Log.secD("AlarmSecurityReceiver", "mItem.mRepeatType = 0x" + Integer.toHexString(item.mRepeatType));
                    } else {
                        item.mSnoozeDoneCount = 0;
                    }
                    long oldCreationTime = item.getCreateTime();
                    item.setCreateTime();
                    item.calculateOriginalAlertTime();
                    item.calculateFirstAlertTime(context);
                    item.setCreateTime(oldCreationTime);
                }
            } else {
                if (item.mActivate != 2) {
                    item.mActivate = 0;
                } else if (!item.isOneTimeAlarm()) {
                    item.mActivate = 0;
                    item.mSnoozeDoneCount = 0;
                    item.calculateOriginalAlertTime();
                    item.calculateFirstAlertTime(context);
                } else if (item.getAlertDayCount() == 1) {
                    item.mActivate = 0;
                    item.mSnoozeDoneCount = 0;
                    item.mAlarmAlertTime = -1;
                    if (item.isSpecificDate()) {
                        item.setSpecificDate(false);
                    }
                } else {
                    item.clearRepeatDay(Calendar.getInstance());
                    item.calculateOriginalAlertTime();
                    item.mActivate = 0;
                    item.calculateFirstAlertTime(context);
                    item.mSnoozeDoneCount = 0;
                }
                AlarmNotificationHelper.clearNotification(context, item.mId);
            }
            Log.secD("AlarmSecurityReceiver", "after item.toString" + item.toString());
            int duplicatedAlarmId = AlarmViewModelUtil.checkDuplicationAlarm(context, item);
            context.getContentResolver().update(AlarmProvider.CONTENT_URI, item.getContentValues(), "_id = " + item.mId, null);
            Log.secD("AlarmSecurityReceiver", "launchDirectEditAlarm() / removed duplicatedAlarmId =" + duplicatedAlarmId + "/ updated item.mId = " + item.mId);
            AlarmProvider.enableNextAlert(context);
        } else if (Feature.DEBUG_ENG) {
            Log.secD("AlarmSecurityReceiver", "item is null");
        }
    }

    private int getAlertNotificationType(Context context, AlarmItem item, String action) {
        if (AlarmUtil.needToShowAlarmAlert(context, item)) {
            int isAccessControlEnable = System.getInt(context.getContentResolver(), "access_control_enabled", 0);
            boolean bInLockTaskMode = StateUtils.isInLockTaskMode(context);
            boolean bBikeMode = StateUtils.isInBikeMode(context);
            StringBuilder append;
            String str;
            if (isAccessControlEnable == 1 || bInLockTaskMode || bBikeMode) {
                append = new StringBuilder().append("NOTIFICATION_ONLY").append(isAccessControlEnable == 1 ? " AccessControl" : "");
                if (bInLockTaskMode) {
                    str = " bInLockTaskMode";
                } else {
                    str = "";
                }
                append = append.append(str);
                if (bBikeMode) {
                    str = " bBikeMode";
                } else {
                    str = "";
                }
                Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT " + append.append(str).toString());
                return 2;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime < item.mAlarmAlertTime || item.mAlarmAlertTime + 50000 < currentTime) {
                Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT " + ("ALERT_TYPE_IGNORE old alarm: " + AlarmItem.digitToAlphabetStr(Integer.toString(item.mAlarmTime)) + ' ' + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(item.mAlarmAlertTime))));
                ArrayList<Integer> changedAlarmIds = AlarmProvider.updateAlarmAsNewTime(context, action);
                int size = changedAlarmIds.size();
                Log.secD("AlarmSecurityReceiver", "changedAlarmIds.size : " + size);
                for (int indexItem = 0; indexItem < size; indexItem++) {
                    AlarmNotificationHelper.clearNotification(context, ((Integer) changedAlarmIds.get(indexItem)).intValue());
                }
                AlarmProvider.enableNextAlert(context);
                return 0;
            }
            Cursor cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, new String[]{"_id"}, "_id = " + item.mId + " AND " + "active" + " > " + 0, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                Logger.m47f("AlarmSecurityReceiver", "ALERT_TYPE_IGNORE it doesn't have same ID or deactivate(mId = " + item.mId + ") cursor == null");
                AlarmProvider.enableNextAlert(context);
                return 0;
            }
            boolean bDriverLink = StateUtils.isDriveLinkRunning(context);
            boolean bMirrorLink = StateUtils.isMirrorLinkRunning();
            if (!StateUtils.isScreenOn(context) || (!bDriverLink && !bMirrorLink)) {
                return 1;
            }
            append = new StringBuilder().append("NOTIFICATION_ONLY").append(bDriverLink ? " bDriverLink" : "");
            if (bMirrorLink) {
                str = " bMirrorLink";
            } else {
                str = "";
            }
            Logger.m47f("AlarmSecurityReceiver", "ALARM_ALERT " + append.append(str).toString());
            Intent iAlarmStarted = new Intent();
            iAlarmStarted.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_DRIVELINK");
            iAlarmStarted.putExtra("alertAlarmID", item.mId);
            context.sendBroadcast(iAlarmStarted);
            return 2;
        }
        if (item != null) {
            AlarmViewModelUtil.dismissAlarm(context, item, null);
        }
        return 0;
    }

    private void waitForCallEnd(Context context, Intent intent, int pendingEvent) {
        Log.secD("AlarmSecurityReceiver", "waitForCallEnd");
        destroyListener(1);
        if (pendingEvent == 1) {
            if (StateUtils.isDualSlot(context)) {
                if (mCallPendingAlarmListenerForSIMSLOT2 == null || mCallPendingAlarmListener == null) {
                    mCallPendingAlarmListener = new CallListener();
                    mCallPendingAlarmListenerForSIMSLOT2 = new CallListener();
                }
                mCallPendingAlarmListenerForSIMSLOT2.mPendingEvent = pendingEvent;
                mCallPendingAlarmListenerForSIMSLOT2.mIntent = intent;
                mCallPendingAlarmListenerForSIMSLOT2.mContext = context;
                getTelephonyManager(context).listen(mCallPendingAlarmListenerForSIMSLOT2, 32);
            } else if (mCallPendingAlarmListener == null) {
                mCallPendingAlarmListener = new CallListener();
            }
            mCallPendingAlarmListener.mPendingEvent = pendingEvent;
            mCallPendingAlarmListener.mIntent = intent;
            mCallPendingAlarmListener.mContext = context;
            getTelephonyManager(context).listen(mCallPendingAlarmListener, 32);
        }
    }

    private TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    private void destroyListener(int pendingEvent) {
        if (pendingEvent == 1) {
            if (mCallPendingAlarmListener != null) {
                Log.secD("AlarmSecurityReceiver", "destroyListener if (mCallPendingAlarmListener != null) {");
                getTelephonyManager(mCallPendingAlarmListener.mContext).listen(mCallPendingAlarmListener, 0);
                mCallPendingAlarmListener.mIntent = null;
                mCallPendingAlarmListener.mContext = null;
            }
            mCallPendingAlarmListener = null;
            if (!(mCallPendingAlarmListenerForSIMSLOT2 == null || mCallPendingAlarmListenerForSIMSLOT2.mContext == null || !StateUtils.isDualSlot(mCallPendingAlarmListenerForSIMSLOT2.mContext))) {
                getTelephonyManager(mCallPendingAlarmListenerForSIMSLOT2.mContext).listen(mCallPendingAlarmListenerForSIMSLOT2, 0);
                mCallPendingAlarmListenerForSIMSLOT2.mIntent = null;
                mCallPendingAlarmListenerForSIMSLOT2.mContext = null;
            }
            mCallPendingAlarmListenerForSIMSLOT2 = null;
        }
    }

    private void updateAlarmOnTime(Context context, AlarmItem item) {
        Log.secD("AlarmSecurityReceiver", "updateAlarmOnTime : " + item.mAlarmAlertTime + '(' + item.mAlarmTime + ')');
        Cursor cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, "alerttime = " + item.mAlarmAlertTime + " AND " + "active" + " > " + 0, null, null);
        if (cursor != null) {
            int itemCount = cursor.getCount();
            if (itemCount <= 0) {
                cursor.close();
                return;
            }
            cursor.moveToFirst();
            for (int i = 0; i < itemCount; i++) {
                AlarmItem updateItem = AlarmItem.createItemFromCursor(cursor);
                if (cursor.getInt(0) != item.mId) {
                    Log.secD("AlarmSecurityReceiver", "updateItem.mId = " + updateItem.mId + " mActivate = " + updateItem.mActivate);
                    AlarmNotificationHelper.clearNotification(context, updateItem.mId);
                    updateItem.updateDismissedState(context);
                    Log.secD("AlarmSecurityReceiver", "match and update item mId -> " + updateItem.mId);
                } else {
                    updateItem.calculateNextAlertTime();
                    Log.secD("AlarmSecurityReceiver", "<- after updateAlarmOnTime : " + updateItem.toString());
                }
                context.getContentResolver().update(AlarmProvider.CONTENT_URI, updateItem.getContentValues(), "_id = " + updateItem.mId, null);
                if (cursor.getInt(0) != item.mId) {
                    AlarmNotificationHelper.showMissedNotification(context, updateItem);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    private void updateAndEnableNextAlarm(Context context, AlarmItem item) {
        updateAlarmOnTime(context, item);
        AlarmProvider.enableNextAlert(context);
    }

    private void launchSetAlarm(Context context, Intent intent) {
        Log.secD("AlarmSecurityReceiver", "launchSetAlarm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent alarmDetail = new Intent();
        alarmDetail.setClass(context, AlarmEditActivity.class);
        alarmDetail.setType("alarm_create_direct");
        alarmDetail.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", AlarmProvider.getAlarmCount(context));
        alarmDetail.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA"));
        alarmDetail.putExtra("android.intent.extra.alarm.HOUR", intent.getIntExtra("android.intent.extra.alarm.HOUR", calendar.get(11)));
        alarmDetail.putExtra("android.intent.extra.alarm.MINUTES", intent.getIntExtra("android.intent.extra.alarm.MINUTES", calendar.get(12)));
        alarmDetail.putExtra("android.intent.extra.alarm.MESSAGE", intent.getStringExtra("android.intent.extra.alarm.MESSAGE"));
        alarmDetail.setFlags(335806464);
        context.startActivity(alarmDetail);
    }

    private void launchEditAlarm(Context context, int listItemID) {
        Log.secD("AlarmSecurityReceiver", "launchEditAlarm() - listItemID:" + listItemID);
        Intent alarmDetail = new Intent();
        alarmDetail.setClass(context, AlarmEditActivity.class);
        alarmDetail.setType("alarm_edit_direct");
        alarmDetail.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", listItemID);
        alarmDetail.setFlags(335806464);
        context.startActivity(alarmDetail);
    }

    private void executeEcbmService(Context context, Intent intent) {
        RuntimeException e;
        Log.secD("AlarmSecurityReceiver", "->executeEcbmService startForegroundService");
        try {
            Intent alert = new Intent();
            alert.setClass(context, AlarmEcbmService.class);
            alert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA"));
            context.startForegroundService(alert);
        } catch (SecurityException e2) {
            e = e2;
            Log.m42e("AlarmSecurityReceiver", "executeEcbmService SecurityException : " + e.toString());
            Log.secD("AlarmSecurityReceiver", "<-executeEcbmService");
        } catch (NullPointerException e3) {
            e = e3;
            Log.m42e("AlarmSecurityReceiver", "executeEcbmService SecurityException : " + e.toString());
            Log.secD("AlarmSecurityReceiver", "<-executeEcbmService");
        }
        Log.secD("AlarmSecurityReceiver", "<-executeEcbmService");
    }

    private void showNotification(Context context, AlarmItem item) {
        Log.secD("AlarmSecurityReceiver", "showNotification : " + item.mId);
        AlarmNotificationHelper.getInstance().notify(context, item);
    }

    private void beepSound(Context context) {
        int mAlertMode = System.getInt(context.getContentResolver(), "alertoncall_mode", 1);
        Log.m41d("AlarmSecurityReceiver", "beepSound mAlertMode = " + mAlertMode);
        switch (mAlertMode) {
            case 1:
                AlarmPlayer player = new AlarmPlayer(context);
                player.setPlayMode(5);
                player.play();
                this.mPowerController.releasePartial();
                this.mPowerController.acquireDim(context, "AlarmSecurityReceiver");
                this.mHandler2.postDelayed(new C05442(), 500);
                return;
            default:
                return;
        }
    }

    private void sendAlarmBackupVersion(Context context) {
        Intent iBackupVersion = new Intent("android.intent.action.RESPONSE_ALARM_BACKUPVERSION");
        iBackupVersion.putExtra("alarm_backupversion", 8);
        Log.secD("AlarmSecurityReceiver", "alarm_backupversion = 8");
        iBackupVersion.putExtra("alarm_add_result", "com.sec.android.clockpackage.SVOICE_ADD_ALARM_RESULT");
        context.sendBroadcast(iBackupVersion);
    }

    private void sendAddAlarmResult(Context context, int mSVoiceReqID, int result) {
        Log.secD("AlarmSecurityReceiver", "sendAddAlarmResult() / mSVoiceReqID=" + mSVoiceReqID + "/result=" + result);
        Intent addResultIntent = new Intent("com.sec.android.clockpackage.SVOICE_ADD_ALARM_RESULT");
        addResultIntent.putExtra("svoice_req_id", mSVoiceReqID);
        addResultIntent.putExtra("svoice_add_alarm_result", result);
        context.sendBroadcast(addResultIntent);
    }
}
