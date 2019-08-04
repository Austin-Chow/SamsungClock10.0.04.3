package com.sec.android.app.clockpackage.backuprestore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SemSystemProperties;
import android.support.v4.app.NotificationCompat;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmViewModelUtil;
import com.sec.android.app.clockpackage.backuprestore.util.AlarmWidgetDataConvertToXml;
import com.sec.android.app.clockpackage.backuprestore.util.BackupRestoreUtils;
import com.sec.android.app.clockpackage.backuprestore.util.ClockDataConvertToXML;
import com.sec.android.app.clockpackage.backuprestore.util.ClockDataEncryption;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AlarmBackupRestoreReceiver extends BroadcastReceiver {
    private static String mDefaultRingtone = "";
    private static boolean mIsSupportBixbyBriefing = false;
    private static boolean mIsSupportCelebVoice = false;
    private static boolean sBackupModelNameIsSame = false;
    private static int sNewAlarmId = -1;
    private static int sOldAlarmId;
    private static int sXmlBackupVersion = 0;
    private Thread mBackupThread;
    private Context mContext;
    private Thread mRestoreThread;
    private AlarmWidgetDataConvertToXml mWidgetXmlConvert;
    private ClockDataConvertToXML mXmlConverter;

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        String action = intent.getAction();
        if (action != null) {
            Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "onReceive() : action = " + action.substring(action.lastIndexOf(46)));
            String filePath = intent.getStringExtra("SAVE_PATH");
            String source = intent.getStringExtra("SOURCE");
            final String saveKey = intent.getStringExtra("SESSION_KEY");
            String sessionTime = intent.getStringExtra("EXPORT_SESSION_TIME");
            final int securityLevel = intent.getIntExtra("SECURITY_LEVEL", 0);
            int extraAction = intent.getIntExtra("ACTION", 0);
            Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "SAVE_PATH=" + filePath + "ACTION=" + extraAction + "extraAction=" + extraAction + "SOURCE=" + source + "EXPORT_SESSION_TIME=" + sessionTime + "SECURITY_LEVEL=" + securityLevel);
            final boolean hasPermission = PermissionUtils.hasPermissionExternalStorage(context);
            final boolean alarmWidget = checkAlarmWidget(intent.getStringArrayListExtra("EXTRA_BACKUP_ITEM"));
            final String str;
            final String str2;
            if (action.equals("com.sec.android.intent.action.REQUEST_BACKUP_ALARM")) {
                Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "Alarm.BACKUP_ALARM!!!!!");
                if (extraAction == 0) {
                    if (!hasPermission) {
                        Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "(alarm backup fail) No External Storage permission!");
                        sendResponse("com.samsung.android.intent.action.RESPONSE_BACKUP_ALARM", filePath, 1, 4, source, sessionTime);
                        return;
                    } else if (this.mBackupThread == null || !this.mBackupThread.isAlive()) {
                        str = filePath;
                        str2 = source;
                        hasPermission = sessionTime;
                        this.mBackupThread = new Thread(new Runnable() {
                            public void run() {
                                AlarmBackupRestoreReceiver.this.backupData(str, str2, saveKey, securityLevel, hasPermission, alarmWidget);
                            }
                        });
                        this.mBackupThread.start();
                        Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", " mBackupThread is started");
                        return;
                    } else {
                        Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", " there is alive mBackupThread!! ignore this request");
                        return;
                    }
                } else if (extraAction == 2 && this.mBackupThread != null && this.mBackupThread.isAlive()) {
                    this.mBackupThread = null;
                    Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", " Cancel request, mBackupThread is stopped!");
                    if (this.mXmlConverter != null) {
                        this.mXmlConverter.deleteData(filePath);
                    }
                    if (this.mWidgetXmlConvert != null) {
                        this.mWidgetXmlConvert.deleteData(filePath);
                        return;
                    }
                    return;
                } else {
                    return;
                }
            }
            if (!action.equals("com.sec.android.intent.action.REQUEST_RESTORE_ALARM")) {
                return;
            }
            if (this.mRestoreThread == null || !this.mRestoreThread.isAlive()) {
                str = filePath;
                str2 = source;
                this.mRestoreThread = new Thread(new Runnable() {
                    public void run() {
                        AlarmUtil.deletePresetAlarm(AlarmBackupRestoreReceiver.this.mContext);
                        Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "Alarm.RESTORE_ALARM!!!!!");
                        AlarmBackupRestoreReceiver.this.restoreData(str, str2, saveKey, securityLevel, hasPermission, alarmWidget);
                    }
                });
                this.mRestoreThread.start();
                Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", " mRestoreThread is started");
                return;
            }
            Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", " there is alive mRestoreThread!! ignore this request");
        }
    }

    private void backupData(String filePath, String source, String key, int securityLevel, String sessionTime, boolean alarmWidgetBackup) {
        int result;
        int errCode = 0;
        if (AlarmProvider.getAlarmCount(this.mContext) == 0) {
            Log.secE("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "backupData() / alarm item == 0");
            result = 1;
            errCode = 3;
        } else {
            String dbPath = BackupRestoreUtils.getAlarmDBPath(this.mContext);
            this.mXmlConverter = new ClockDataConvertToXML(filePath, key, securityLevel, 0);
            result = this.mXmlConverter.backupData(dbPath);
        }
        Log.m41d("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "alarm backupData result = " + result + "/alarmWidgetBackup=" + alarmWidgetBackup);
        if (result == 0 && alarmWidgetBackup) {
            this.mWidgetXmlConvert = new AlarmWidgetDataConvertToXml(filePath, key, securityLevel, 4);
            this.mWidgetXmlConvert.backupData(this.mContext);
        }
        sendResponse("com.samsung.android.intent.action.RESPONSE_BACKUP_ALARM", filePath, result, errCode, source, sessionTime);
    }

    private void restoreData(String filePath, String source, String saveKey, int securityLevel, boolean hasPermission, boolean alarmWidgetRestore) {
        int result;
        int errCode;
        if (AlarmProvider.getAlarmCount(this.mContext) == 50) {
            Log.secE("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "restoreData() / alarm count is MAX");
            result = 1;
            errCode = 2;
        } else if (hasPermission) {
            result = restoreAlarmFromXML(this.mContext, filePath, saveKey, securityLevel);
            errCode = 0;
        } else {
            Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "(alarm restore fail) No External Storage permission!");
            sendResponse("com.samsung.android.intent.action.RESPONSE_RESTORE_ALARM", filePath, 1, 4, source, "");
            errCode = 0;
            return;
        }
        Log.m41d("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "alarm restoreData result = " + result + "/alarmWidgetRestore=" + alarmWidgetRestore);
        if (result == 0 && alarmWidgetRestore) {
            restoreAlarmWidgetFromXML(this.mContext, filePath, saveKey, securityLevel);
        }
        updateAllAlarm(this.mContext, "com.samsung.android.intent.action.RESPONSE_RESTORE_ALARM");
        sendResponse("com.samsung.android.intent.action.RESPONSE_RESTORE_ALARM", filePath, result, errCode, source, "");
        if (this.mRestoreThread != null && this.mRestoreThread.isAlive()) {
            this.mRestoreThread = null;
            Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "restore done, mRestoreThread is stopped!");
        }
    }

    private void sendResponse(String rspAction, String filePath, int result, int errCode, String source, String sessionTime) {
        Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "sendResponse()/rspAction=" + rspAction.substring(rspAction.lastIndexOf(46)) + "/result=" + (result == 1 ? "FAIL" : "SUCCESS") + "/err_code=" + errCode);
        File AlarmXML = new File(filePath + "/alarm.exml");
        Intent intent = new Intent(rspAction);
        intent.putExtra("RESULT", result);
        intent.putExtra("ERR_CODE", errCode);
        intent.putExtra("REQ_SIZE", (int) AlarmXML.length());
        intent.putExtra("SOURCE", source);
        intent.putExtra("EXPORT_SESSION_TIME", sessionTime);
        this.mContext.sendBroadcast(intent, "com.wssnps.permission.COM_WSSNPS");
    }

    private boolean checkAlarmWidget(ArrayList<String> extraBackup) {
        boolean ret = false;
        if (extraBackup != null) {
            Iterator it = extraBackup.iterator();
            while (it.hasNext()) {
                if ("Widget".equals((String) it.next())) {
                    Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "alarmwidget_bnr : checkAlarmWidget() / widget exist");
                    ret = true;
                }
            }
        }
        Log.secD("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "alarmwidget_bnr : checkAlarmWidget() / widget BnR=" + ret);
        return ret;
    }

    private static int restoreAlarmFromXML(Context context, String filePath, String saveKey, int securityLevel) {
        Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "restoreAlarmFromXML() !!!!!!!!");
        AlarmUtil.sendAlarmDeleteModeUpdate(context);
        ClockDataEncryption clockDataEncryption = new ClockDataEncryption();
        mIsSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(context);
        mIsSupportCelebVoice = Feature.isSupportCelebrityAlarm();
        String fullPath = filePath + "/alarm.exml";
        File file = new File(fullPath);
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarm restore fullPath = " + fullPath);
        if (file.exists()) {
            try {
                InputStream fis = new FileInputStream(file);
                InputStream alarmXml = clockDataEncryption.decryptStream(fis, saveKey, securityLevel);
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                if (alarmXml == null || securityLevel == -1) {
                    parser.setInput(fis, "utf-8");
                } else {
                    parser.setInput(alarmXml, "utf-8");
                }
                int eventType = parser.getEventType();
                AlarmItem item = new AlarmItem();
                String startTag = null;
                mDefaultRingtone = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(context).toString());
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "mDefaultRingtone = " + mDefaultRingtone);
                while (eventType != 1) {
                    switch (eventType) {
                        case 0:
                            Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "========[START_DOCUMENT]========");
                            break;
                        case 2:
                            startTag = parser.getName();
                            if (!startTag.equals(NotificationCompat.CATEGORY_ALARM)) {
                                break;
                            }
                            item = new AlarmItem();
                            sNewAlarmId = -1;
                            sOldAlarmId = -1;
                            break;
                        case 3:
                            if (parser.getName().equals(NotificationCompat.CATEGORY_ALARM)) {
                                if (AlarmProvider.getAlarmCount(context) == 50) {
                                    Log.secE("BNR_CLOCK_ALARM_restoreAlarmFromXML", "already MAX COUNT");
                                    return 1;
                                }
                                adjustAlarm(context, item);
                                saveAlarm(context, item);
                                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "----------------------------------\n");
                            }
                            startTag = null;
                            break;
                        case 4:
                            String parserText = parser.getText();
                            if (startTag != null && parserText != null) {
                                loadAlarm(context, item, startTag, parserText);
                                break;
                            }
                            Log.secE("BNR_CLOCK_ALARM_restoreAlarmFromXML", "startTag = " + startTag + "/ TEXT = " + parserText);
                            break;
                            break;
                        default:
                            Log.secE("BNR_CLOCK_ALARM_restoreAlarmFromXML", "Invalid eventType =" + eventType);
                            return 1;
                    }
                    eventType = parser.next();
                }
                if (eventType == 1) {
                    Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "=========[END_DOCUMENT]=========");
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
        }
        Log.m42e("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarm restore file not exist : " + fullPath);
        return 1;
    }

    private static void loadAlarm(Context context, AlarmItem item, String startTag, String parserText) {
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "loadAlarm() /  " + startTag + " = " + parserText);
        Object obj = -1;
        int i;
        switch (startTag.hashCode()) {
            case -2025228483:
                if (startTag.equals("dailybrief_BACKUP_VER_8")) {
                    obj = 12;
                    break;
                }
                break;
            case -1461465559:
                if (startTag.equals("alerttime")) {
                    obj = 3;
                    break;
                }
                break;
            case -1422950650:
                if (startTag.equals("active")) {
                    obj = 1;
                    break;
                }
                break;
            case -986525546:
                if (startTag.equals("BackupVersion")) {
                    obj = 21;
                    break;
                }
                break;
            case -938206882:
                if (startTag.equals("alarmsound")) {
                    obj = 13;
                    break;
                }
                break;
            case -810883302:
                if (startTag.equals("volume")) {
                    obj = 15;
                    break;
                }
                break;
            case -758034242:
                if (startTag.equals("vibrationpattern")) {
                    obj = 19;
                    break;
                }
                break;
            case -722977634:
                if (startTag.equals("alarmtime")) {
                    obj = 4;
                    break;
                }
                break;
            case -722971837:
                if (startTag.equals("alarmtone")) {
                    obj = 14;
                    break;
                }
                break;
            case -57343390:
                if (startTag.equals("locationtext")) {
                    obj = 23;
                    break;
                }
                break;
            case 94650:
                if (startTag.equals("_id")) {
                    obj = null;
                    break;
                }
                break;
            case 3373707:
                if (startTag.equals("name")) {
                    obj = 17;
                    break;
                }
                break;
            case 179108046:
                if (startTag.equals("map_user")) {
                    obj = 18;
                    break;
                }
                break;
            case 204217363:
                if (startTag.equals("snzduration")) {
                    i = 8;
                    break;
                }
                break;
            case 355146412:
                if (startTag.equals("vibrationpattern_user")) {
                    obj = 20;
                    break;
                }
                break;
            case 510058821:
                if (startTag.equals("snzactive")) {
                    obj = 7;
                    break;
                }
                break;
            case 532998546:
                if (startTag.equals("BackupModelName")) {
                    obj = 22;
                    break;
                }
                break;
            case 988490480:
                if (startTag.equals("snzcount")) {
                    obj = 10;
                    break;
                }
                break;
            case 998477786:
                if (startTag.equals("snzrepeat")) {
                    obj = 9;
                    break;
                }
                break;
            case 1160542037:
                if (startTag.equals("repeattype")) {
                    obj = 5;
                    break;
                }
                break;
            case 1370166729:
                if (startTag.equals("createtime")) {
                    i = 2;
                    break;
                }
                break;
            case 1500700027:
                if (startTag.equals("alarmuri")) {
                    obj = 16;
                    break;
                }
                break;
            case 1529836033:
                if (startTag.equals("dailybrief")) {
                    obj = 11;
                    break;
                }
                break;
            case 1585796144:
                if (startTag.equals("notitype")) {
                    obj = 6;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                sOldAlarmId = Integer.parseInt(parserText);
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarmwidget_bnr (**) : orgAlarmID=" + sOldAlarmId);
                return;
            case 1:
                item.mActivate = getValidActivation(Integer.parseInt(parserText));
                return;
            case 2:
                item.mCreateTime = Long.parseLong(parserText);
                return;
            case 3:
                item.mAlarmAlertTime = Long.parseLong(parserText);
                return;
            case 4:
                item.mAlarmTime = Integer.parseInt(parserText);
                return;
            case 5:
                item.mRepeatType = Integer.parseInt(parserText);
                return;
            case 6:
                item.mNotificationType = Integer.parseInt(parserText);
                return;
            case 7:
                item.mSnoozeActivate = parserText.equals("1");
                return;
            case 8:
                item.mSnoozeDuration = getValidSnzDuration(Integer.parseInt(parserText));
                return;
            case 9:
                item.mSnoozeRepeat = getValidSnzRepeat(Integer.parseInt(parserText));
                return;
            case 10:
                item.mSnoozeDoneCount = Integer.parseInt(parserText);
                return;
            case 11:
                if (2 <= sXmlBackupVersion) {
                    Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "backup device version is under 8");
                    item.mDailyBriefing = Integer.parseInt(parserText);
                    return;
                }
                return;
            case 12:
                if (8 <= sXmlBackupVersion) {
                    Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "backup device version is more than 8");
                    item.mDailyBriefing = Integer.parseInt(parserText);
                    return;
                }
                return;
            case 13:
                item.mAlarmSoundType = Integer.parseInt(parserText);
                return;
            case 14:
                item.mAlarmSoundTone = Integer.parseInt(parserText);
                return;
            case 15:
                item.mAlarmVolume = Integer.parseInt(parserText);
                return;
            case 16:
                item.mSoundUri = parserText;
                return;
            case 17:
                item.mAlarmName = parserText;
                return;
            case 18:
                item.mCelebVoicePath = parserText;
                return;
            case 19:
            case 20:
                int backupVibPattern = Integer.parseInt(parserText);
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", startTag + " : item.mVibrationPattern " + item.mVibrationPattern + "/ backupVibPattern = " + backupVibPattern);
                if (AlarmUtil.isValidVibPattern(context, backupVibPattern)) {
                    item.mVibrationPattern = backupVibPattern;
                }
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", startTag + " : item.mVibrationPattern " + item.mVibrationPattern);
                return;
            case 21:
                sXmlBackupVersion = Integer.parseInt(parserText);
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "CURRENT_BNR_VER = 8 / BACKUP_BNR_VER = " + sXmlBackupVersion);
                return;
            case 22:
                sBackupModelNameIsSame = parserText.equals(SemSystemProperties.get("ro.product.model"));
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "sBackupModelNameIsSame =" + sBackupModelNameIsSame);
                return;
            case 23:
                item.mWeatherMusicPath = "";
                return;
            default:
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "Invalid eventType startTag = " + startTag);
                return;
        }
    }

    private static int getValidActivation(int activate) {
        int ret = activate;
        if (sXmlBackupVersion < 2) {
            ret = activate > 0 ? 1 : 0;
        }
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidActivation() / " + activate + " -> " + ret);
        return activate;
    }

    private static int getValidSnzDuration(int snzDuration) {
        int ret = snzDuration;
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnoozeDuration()()/CURRENT_BACKUP_VERSION=8/sXmlBackupVersion=" + sXmlBackupVersion + "/snzDuration" + snzDuration);
        if (sXmlBackupVersion < 3 && snzDuration < 1) {
            ret = 1;
            Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnoozeDuration() / Change! / " + snzDuration + " -> " + 1);
        }
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnoozeDuration() / " + snzDuration + " -> " + ret);
        return ret;
    }

    private static int getValidSnzRepeat(int snzRepeat) {
        int ret = snzRepeat;
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnzRepeat()/CURRENT_BACKUP_VERSION=8/sXmlBackupVersion=" + sXmlBackupVersion + "/mSnoozeRepeat=" + snzRepeat);
        if (sXmlBackupVersion < 3) {
            if (snzRepeat < 2) {
                ret = 2;
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnoozeDuration() / Change! / " + snzRepeat + " -> " + 2);
            } else if (AlarmItemUtil.ALARM_SNOOZE_COUNT_TABLE[snzRepeat] == AlarmItemUtil.CONTINUOUSLY) {
                ret = snzRepeat - 1;
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnoozeDuration() / Change! / " + snzRepeat + " -> " + ret);
            }
        }
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "getValidSnzRepeat() / " + snzRepeat + " -> " + ret);
        return ret;
    }

    private static void adjustAlarm(Context context, AlarmItem item) {
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "\n@ adjustAlarm() / backup version = " + sXmlBackupVersion + " / current version = " + 8);
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "\n@ adjustAlarm() / item = \n" + item + "\n");
        if (sXmlBackupVersion == 0 && item.isOneTimeAlarm()) {
            int repeatCount = item.getAlertDayCount();
            Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "repeatCount = " + repeatCount);
            if (repeatCount <= 1) {
                long time = item.mAlarmAlertTime - Calendar.getInstance().getTimeInMillis();
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "time = " + time);
                if (86400000 < time) {
                    Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "Const.DAY_MILLIS < time");
                    item.setWeeklyAlarm();
                    Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "once alarm to weekly alarm item.mRepeatType : 0x" + Integer.toHexString(item.mRepeatType));
                }
            } else {
                item.setWeeklyAlarm();
                Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "else once alarm to weekly alarm item.mRepeatType : 0x" + Integer.toHexString(item.mRepeatType));
            }
        }
        if (!item.isSpecificDate() && item.isOneTimeAlarm() && item.mAlarmAlertTime < Calendar.getInstance().getTimeInMillis()) {
            Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "&& (item.mAlarmAlertTime < curCalendar.getTimeInMillis())) {");
            item.mActivate = 0;
            item.mSnoozeDoneCount = 0;
            item.mAlarmAlertTime = -1;
        } else if (item.mSnoozeDoneCount <= 0 || !item.isOneTimeAlarm()) {
            item.mSnoozeDoneCount = 0;
            item.calculateFirstAlertTime(context);
        } else {
            item.mActivate = 0;
            item.mSnoozeDoneCount = 0;
            item.mAlarmAlertTime = -1;
        }
        if (item.mAlarmName.length() > 20) {
            item.mAlarmName = item.mAlarmName.substring(0, 20);
        }
        if (8 <= sXmlBackupVersion) {
            if (!item.isMasterSoundOn()) {
                item.setSoundModeAlarmTone();
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "backup master sound off -> set alarm tone mode");
            }
        } else if (item.isMasterSoundOn()) {
            if (!(item.isBixbyBriefingOn() || item.isAlarmToneOn())) {
                item.setMasterSoundOn(false);
                item.setSoundModeAlarmTone();
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "bixby off, sound off -> master sound off, alarm tone on");
            }
            if (item.mAlarmVolume == 0) {
                item.setMasterSoundOn(false);
                item.setSoundModeAlarmTone();
                item.mAlarmVolume = 11;
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarm volume : 0 -> master sound off, alarm tone on, default volume(11)");
            }
            if (item.mAlarmSoundType == 1 && sXmlBackupVersion < 5) {
                item.setMasterSoundOn(false);
                item.setSoundModeAlarmTone();
                item.mAlarmSoundType = 2;
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarmSoundType : VIBRATE -> master sound off, alarm tone on, SOUND_AND_VIB");
            }
            if ("alarm_silent_ringtone".equals(item.mSoundUri)) {
                item.setMasterSoundOn(false);
                item.setSoundModeAlarmTone();
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "silent ringtone -> master sound off, alarm tone on");
            }
            if (6 > sXmlBackupVersion || sXmlBackupVersion >= 8) {
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarm tone");
                item.setSoundModeAlarmTone();
            } else if (item.isBixbyBriefingOn() && item.isBixbyVoiceOn() && mIsSupportBixbyBriefing) {
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "bixby briefing, bixby voice -> new bixby");
                item.setSoundModeNewBixby();
            } else if (item.isBixbyBriefingOn() && item.isBixbyCelebVoice() && mIsSupportCelebVoice) {
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "bixby celeb voice -> new celeb");
                item.setSoundModeNewCeleb();
            } else {
                Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarm tone");
                item.setSoundModeAlarmTone();
            }
        } else {
            Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "backup master sound off -> set alarm tone mode");
            item.setSoundModeAlarmTone();
        }
        if ((item.isNewCelebOn() && !mIsSupportCelebVoice) || (item.isNewBixbyOn() && !mIsSupportBixbyBriefing)) {
            Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "feature not support-> alarm tone mode");
            item.setSoundModeAlarmTone();
        }
        if (!mIsSupportCelebVoice) {
            item.mCelebVoicePath = "";
        } else if (!(ClockUtils.isEnableString(item.mCelebVoicePath) && AlarmUtil.isValidCelebrityVoicePath(context, item.mCelebVoicePath))) {
            item.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        }
        item.initNotUsedBixbyCeleb();
        item.initIncreasingVolume();
        item.initWeatherBg();
        if (item.mAlarmSoundType == 3) {
            item.mAlarmSoundType = 0;
            Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", " alarmSoundType : VOICE -> SOUND");
        }
        String backupSoundUri = item.mSoundUri;
        item.mSoundUri = mDefaultRingtone;
        if (sBackupModelNameIsSame && ClockUtils.isEnableString(item.mSoundUri) && AlarmRingtoneManager.isAlarmTypeRingtone(context, item.mSoundUri) && AlarmRingtoneManager.validRingtoneStr(context, item.mSoundUri)) {
            item.mSoundUri = backupSoundUri;
        }
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "sBackupModelNameIsSame = " + sBackupModelNameIsSame + "/ item.mSoundUri = " + item.mSoundUri);
        item.mAlarmSoundTone = new AlarmRingtoneManager(context).getRingtoneIndex(item.mSoundUri);
    }

    private static void saveAlarm(Context context, AlarmItem item) {
        int i = -1;
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "\n@ saveAlarm() / item = \n" + item + "\n");
        int id = AlarmUtil.checkSameAlarm(context, item);
        if (id != -1) {
            sNewAlarmId = id;
            BackupRestoreUtils.addBnRAlarmData(context, sOldAlarmId, sNewAlarmId);
            Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarmwidget_bnr (**) : (check_same) orgID=" + sOldAlarmId + "/changedID=" + sNewAlarmId + "/same id=" + id);
            return;
        }
        long transactionResult;
        id = AlarmViewModelUtil.checkDuplicationAlarm(context, item, false);
        if (id == -1) {
            transactionResult = AlarmProvider.getId(context.getContentResolver().insert(AlarmProvider.CONTENT_URI, item.getContentValues()));
            sNewAlarmId = (int) transactionResult;
        } else {
            transactionResult = (long) context.getContentResolver().update(AlarmProvider.CONTENT_URI, item.getContentValues(), "_id = " + id, null);
            if (transactionResult > 0) {
                i = id;
            }
            sNewAlarmId = i;
        }
        BackupRestoreUtils.addBnRAlarmData(context, sOldAlarmId, sNewAlarmId);
        Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarmwidget_bnr (**) : (check_duplicated) transactionResult=" + transactionResult + "duplicatedID=" + id);
        Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "alarmwidget_bnr (**) : (check_duplicated) orgID=" + sOldAlarmId + "/changedID=" + sNewAlarmId);
        Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "\n@ saveAlarm() / t =" + item.mAlarmTime + "/ alertT" + item.mAlarmAlertTime);
        if (transactionResult > 0) {
            AlarmProvider.enableNextAlert(context);
        }
    }

    private void updateAllAlarm(Context context, String action) {
        Log.m41d("BNR_CLOCK_ALARM_restoreAlarmFromXML", "updateAllAlarm()");
        AlarmNotificationHelper.showSnoozeNotification(context, null, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
        ArrayList<Integer> changedAlarmIds = AlarmProvider.updateAlarmAsNewTime(context, action);
        int size = changedAlarmIds.size();
        Log.secD("BNR_CLOCK_ALARM_restoreAlarmFromXML", "changedAlarmIds.size : " + size);
        for (int indexItem = 0; indexItem < size; indexItem++) {
            AlarmNotificationHelper.clearNotification(context, ((Integer) changedAlarmIds.get(indexItem)).intValue());
        }
        AlarmProvider.enableNextAlert(context);
    }

    public static int restoreAlarmWidgetFromXML(Context context, String filePath, String saveKey, int securityLevel) {
        Exception e;
        Throwable th;
        Log.m41d("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "restoreAlarmWidgetFromXML !!!!!!!!");
        ClockDataEncryption clockDataEncryption = new ClockDataEncryption();
        String fullPath = filePath + "/alarmWidget.exml";
        File file = new File(fullPath);
        Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "alarm widget restore fullPath = " + fullPath);
        InputStream inputStream = null;
        InputStream alarmWidgetXml = null;
        try {
            InputStream fis = new FileInputStream(file);
            try {
                alarmWidgetXml = clockDataEncryption.decryptStream(fis, saveKey, securityLevel);
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                if (alarmWidgetXml == null || securityLevel == -1) {
                    parser.setInput(fis, "utf-8");
                } else {
                    parser.setInput(alarmWidgetXml, "utf-8");
                }
                int eventType = parser.getEventType();
                while (eventType != 1) {
                    switch (eventType) {
                        case 0:
                            Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "========[START_DOCUMENT]========");
                            break;
                        case 2:
                            String startTag = parser.getName();
                            Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "[START_TAG] : " + startTag);
                            processAlarmWidget(context, startTag, parser);
                            break;
                        case 3:
                            Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "[END_TAG] : " + parser.getName());
                            break;
                        case 4:
                            Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "[TEXT] : " + parser.getText());
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }
                if (eventType == 1) {
                    Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", "=========[END_DOCUMENT]=========");
                }
                BackupRestoreUtils.clearBnRAlarmData(context);
                closeInputStream(fis);
                closeInputStream(alarmWidgetXml);
                inputStream = fis;
                return 0;
            } catch (Exception e2) {
                e = e2;
                inputStream = fis;
            } catch (Throwable th2) {
                th = th2;
                inputStream = fis;
            }
        } catch (Exception e3) {
            e = e3;
            try {
                e.printStackTrace();
                BackupRestoreUtils.clearBnRAlarmData(context);
                closeInputStream(inputStream);
                closeInputStream(alarmWidgetXml);
                return 1;
            } catch (Throwable th3) {
                th = th3;
                BackupRestoreUtils.clearBnRAlarmData(context);
                closeInputStream(inputStream);
                closeInputStream(alarmWidgetXml);
                throw th;
            }
        }
    }

    private static void closeInputStream(InputStream inputObj) {
        if (inputObj != null) {
            try {
                inputObj.close();
            } catch (IOException e) {
                Log.secE("BNR_CLOCK_ALARM_AlarmBackupRestoreReceiver", "fail : close Input stream");
            }
        }
    }

    private static void processAlarmWidget(Context context, String startTag, XmlPullParser parser) throws IOException, XmlPullParserException {
        int i = -1;
        switch (startTag.hashCode()) {
            case -986525546:
                if (startTag.equals("BackupVersion")) {
                    i = 1;
                    break;
                }
                break;
            case -788047292:
                if (startTag.equals("widget")) {
                    i = 0;
                    break;
                }
                break;
        }
        String parserText;
        switch (i) {
            case 0:
                parserText = parser.nextText();
                if (!parserText.isEmpty()) {
                    String[] data = parserText.split(",");
                    int newAlarmId = BackupRestoreUtils.getBnRAlarmData(context, data[1]);
                    Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", " widget id = " + data[0] + "/ alarm id = " + data[1] + "/ new alarm id = " + newAlarmId);
                    BackupRestoreUtils.addBnRAlarmWidgetData(context, data[0], newAlarmId);
                    return;
                }
                return;
            case 1:
                parserText = parser.nextText();
                if (!parserText.isEmpty()) {
                    sXmlBackupVersion = Integer.parseInt(parserText);
                    Log.secD("BNR_CLOCK_ALARMWIDGET_restoreAlarmWidgetFromXML", " current =1/ backup = " + sXmlBackupVersion);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
