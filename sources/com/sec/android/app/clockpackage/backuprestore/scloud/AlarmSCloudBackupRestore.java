package com.sec.android.app.clockpackage.backuprestore.scloud;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.SemSystemProperties;
import android.support.v4.app.NotificationCompat;
import com.samsung.android.scloud.oem.lib.bnr.BNRFile;
import com.samsung.android.scloud.oem.lib.bnr.BNRItem;
import com.samsung.android.scloud.oem.lib.bnr.ISCloudBNRClient;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.backuprestore.util.BackupRestoreUtils;
import com.sec.android.app.clockpackage.backuprestore.util.JSONParser;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmSCloudBackupRestore implements ISCloudBNRClient {
    private static final String TAG = "BNR_CLOCK_ALARM_AlarmSCloudBackupRestore";
    private static boolean mIsSupportBixbyBriefing = false;
    private static boolean mIsSupportCelebVoice = false;
    private int mBackupVersion = 0;
    private String mDefaultSoundUri = null;
    private boolean mIsSamePhone = false;
    private int mNewAlarmId = -1;
    private int mOldAlarmId = -1;

    public boolean isSupportBackup(Context context) {
        return true;
    }

    public boolean isEnableBackup(Context context) {
        return true;
    }

    public String getLabel(Context context) {
        return context.getResources().getString(R.string.alarm);
    }

    public String getDescription(Context context) {
        return context.getResources().getString(R.string.alarm);
    }

    public boolean backupPrepare(Context context) {
        Log.secD(TAG, "backupPrepare()");
        return true;
    }

    public HashMap<String, Long> getItemKey(Context context, int start, int max_count) {
        Log.secD(TAG, "getItemKey() called~!!");
        HashMap<String, Long> result = new HashMap();
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(BackupRestoreUtils.getAlarmDBPath(context), null, 0);
            Cursor cursor = db.query(NotificationCompat.CATEGORY_ALARM, new String[]{"_id", "createtime"}, null, null, null, null, "_id ASC LIMIT " + max_count + " OFFSET " + start);
            if (cursor == null || cursor.getCount() <= 0) {
                Log.secD(TAG, "getItemKey() : item count is 0 or null");
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
                return null;
            }
            while (cursor.moveToNext()) {
                result.put(Integer.toString(cursor.getInt(0)), Long.valueOf(System.currentTimeMillis()));
            }
            Log.secD(TAG, "getItemKey() : item count : " + result.size());
            db.close();
            cursor.close();
            return result;
        } catch (SQLiteException e) {
            Log.secE(TAG, "Exception : " + e.toString());
            Log.secE(TAG, "getItemKey() : SQLiteException : Unable to save alarm");
            return null;
        }
    }

    public ArrayList<BNRFile> getFileMeta(Context context, int start, int max_count) {
        return null;
    }

    public ArrayList<BNRItem> backupItem(Context context, ArrayList<String> localIds) {
        Log.m41d(TAG, "backupItem()");
        int alarmActiveCount = 0;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(BackupRestoreUtils.getAlarmDBPath(context), null, 1);
        Cursor cursor = db.query(NotificationCompat.CATEGORY_ALARM, null, "_id IN " + ClockUtils.getWhereKey(ClockUtils.toStringArray(localIds)), null, null, null, null);
        if (cursor != null) {
            int alarmCount = cursor.getCount();
            Log.secD(TAG, "backupItem : alarmCount =  " + alarmCount);
            ArrayList<BNRItem> result = new ArrayList();
            if (alarmCount > 0) {
                while (cursor.moveToNext()) {
                    try {
                        JSONObject data = JSONParser.toJSON(cursor);
                        long timeStamp = cursor.getLong(cursor.getColumnIndex("createtime"));
                        int idIndex = cursor.getColumnIndex("_id");
                        int active = cursor.getColumnIndex("active");
                        int alarmTime = cursor.getColumnIndex("alarmtime");
                        if (cursor.getInt(active) != 0) {
                            alarmActiveCount++;
                        }
                        Log.secD(TAG, "backupItem : idIndex = " + cursor.getString(idIndex) + ",  active = " + cursor.getString(active) + ", ALARMTIME = " + cursor.getString(alarmTime));
                        result.add(new BNRItem(cursor.getString(idIndex), data.toString(), timeStamp));
                    } catch (JSONException e) {
                        Log.secE(TAG, "Exception : " + e.toString());
                    }
                }
                Log.secD(TAG, "backupItem : alarmActiveCount =  " + alarmActiveCount);
                cursor.close();
                db.close();
                return result;
            } else if (cursor.getCount() == 0) {
                result.add(null);
                cursor.close();
                db.close();
                return result;
            } else {
                cursor.close();
            }
        }
        db.close();
        return null;
    }

    public String getFilePath(Context context, String path, boolean external, String operation) {
        return null;
    }

    public boolean backupComplete(Context context, boolean isSuccess) {
        Log.secD(TAG, "backupComplete() / isSuccess = " + isSuccess);
        return isSuccess;
    }

    public boolean restorePrepare(Context context, Bundle extras) {
        String productModel = SemSystemProperties.get("ro.product.model");
        if (extras != null) {
            String deviceName = extras.getString("device_name");
            Log.secD(TAG, "restorePrepare  phone productModel = " + productModel + ", deviceName = " + deviceName);
            this.mIsSamePhone = deviceName.equals(productModel);
        }
        return true;
    }

    public boolean restoreItem(Context context, ArrayList<BNRItem> items, ArrayList<String> insertedRowId) {
        Exception e;
        Log.secD(TAG, "restoreItem() / items = " + items);
        if (items == null || items.size() <= 0) {
            Log.secD(TAG, "no restore item");
            return false;
        }
        SQLiteDatabase db;
        AlarmUtil.deletePresetAlarm(context);
        mIsSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(context);
        mIsSupportCelebVoice = Feature.isSupportCelebrityAlarm();
        AlarmUtil.sendAlarmDeleteModeUpdate(context);
        BackupRestoreUtils.clearBnRAlarmData(context);
        String dbPath = BackupRestoreUtils.getAlarmDBPath(context);
        Context dbContext = StateUtils.getAlarmDBContext(context);
        if (dbContext.getDatabasePath(dbPath).exists()) {
            db = SQLiteDatabase.openDatabase(dbPath, null, 0);
        } else {
            db = dbContext.openOrCreateDatabase(dbPath, 0, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS alarm (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,active INTEGER NOT NULL,createtime LONG NOT NULL,alerttime LONG NOT NULL,alarmtime INTEGER NOT NULL,repeattype INTEGER NOT NULL,notitype INTEGER NOT NULL,snzactive INTEGER NOT NULL,snzduration INTEGER NOT NULL,snzrepeat INTEGER NOT NULL,snzcount INTEGER NOT NULL,dailybrief INTEGER NOT NULL,sbdactive INTEGER NOT NULL,sbdduration INTEGER NOT NULL,sbdtone INTEGER NOT NULL,alarmsound INTEGER NOT NULL,alarmtone INTEGER NOT NULL,volume INTEGER NOT NULL,sbduri INTEGER NOT NULL,alarmuri TEXT,name TEXT,locationactive INTEGER NOT NULL DEFAULT 0,latitude DOUBLE NOT NULL DEFAULT 0,longitude DOUBLE NOT NULL DEFAULT 0,locationtext TEXT,map TEXT,vibrationpattern INTEGER NOT NULL)");
        }
        int insertedCnt = 0;
        this.mDefaultSoundUri = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(context).toString());
        Log.secD(TAG, "restoreItem() / mIsSamePhone=" + this.mIsSamePhone + "/ mDefaultSoundUri=" + this.mDefaultSoundUri);
        int index = 0;
        while (index < items.size()) {
            this.mNewAlarmId = -1;
            this.mOldAlarmId = -1;
            BNRItem item = (BNRItem) items.get(index);
            if (item == null) {
                Log.secD(TAG, "restoreItem : NO ITEM");
                db.close();
                return false;
            } else if (insertedCnt >= 50) {
                Log.secD(TAG, "restoreItem : reach to MAX count");
                db.close();
                return true;
            } else {
                try {
                    ContentValues insertCV = loadAlarmData(context, item);
                    Log.secD(TAG, "restoreItem : idIndex = " + index + ",  active = " + insertCV.getAsString("active") + ", ALARM_TIME = " + insertCV.getAsString("alarmtime"));
                    this.mNewAlarmId = (int) db.insert(NotificationCompat.CATEGORY_ALARM, null, insertCV);
                    Log.secD(TAG, "alarmwidget_bnr (**) restoreItem / mOldAlarmId=" + this.mOldAlarmId + "/mNewAlarmId=" + this.mNewAlarmId);
                    if (this.mNewAlarmId == -1) {
                        Log.secD(TAG, "restoreItem : Item is NOT inserted");
                        db.close();
                        return false;
                    }
                    BackupRestoreUtils.addBnRAlarmData(context, this.mOldAlarmId, this.mNewAlarmId);
                    insertedCnt++;
                    Log.secD(TAG, "restoreItem : Item is inserted / id (" + this.mNewAlarmId + " -> " + insertedCnt + ")");
                    insertedRowId.add(Long.toString((long) this.mNewAlarmId));
                    index++;
                } catch (JSONException e2) {
                    e = e2;
                } catch (SQLiteException e3) {
                    e = e3;
                }
            }
        }
        db.close();
        return true;
        Log.secE(TAG, "Exception : " + e.toString());
        db.close();
        return false;
    }

    private ContentValues loadAlarmData(Context context, BNRItem item) throws JSONException {
        ContentValues backupCV = JSONParser.fromJSON(new JSONObject(item.getData()));
        ContentValues insertCV = new ContentValues();
        if (backupCV.getAsString("BackupVersion") != null) {
            this.mBackupVersion = Integer.parseInt(backupCV.getAsString("BackupVersion"));
        }
        Log.secD(TAG, "mBackupVersion = " + this.mBackupVersion);
        Integer mOldAlarmIdByInteger = backupCV.getAsInteger("_id");
        if (mOldAlarmIdByInteger != null) {
            this.mOldAlarmId = mOldAlarmIdByInteger.intValue();
        } else {
            Log.secE(TAG, "restoreItem : alarm_id is null, can't restore alarm widget");
        }
        insertCV.put("active", backupCV.getAsString("active"));
        insertCV.put("createtime", backupCV.getAsString("createtime"));
        insertCV.put("alerttime", backupCV.getAsString("alerttime"));
        insertCV.put("alarmtime", backupCV.getAsString("alarmtime"));
        insertCV.put("repeattype", backupCV.getAsString("repeattype"));
        insertCV.put("notitype", backupCV.getAsString("notitype"));
        insertCV.put("snzactive", backupCV.getAsString("snzactive"));
        insertCV.put("snzduration", backupCV.getAsString("snzduration"));
        insertCV.put("snzrepeat", backupCV.getAsString("snzrepeat"));
        insertCV.put("snzcount", backupCV.getAsString("snzcount"));
        insertCV.put("sbdactive", backupCV.getAsString("sbdactive"));
        insertCV.put("sbdduration", backupCV.getAsString("sbdduration"));
        insertCV.put("sbdtone", backupCV.getAsString("sbdtone"));
        insertCV.put("sbduri", backupCV.getAsString("sbduri"));
        int alarmSoundType = Integer.parseInt(backupCV.getAsString("alarmsound"));
        if (!Feature.isSupportVibration(context)) {
            insertCV.put("alarmsound", Integer.valueOf(0));
        } else if (alarmSoundType == 1) {
            insertCV.put("alarmsound", Integer.valueOf(2));
        } else {
            insertCV.put("alarmsound", backupCV.getAsString("alarmsound"));
        }
        int volume = Integer.parseInt(backupCV.getAsString("volume"));
        if (volume == 0) {
            insertCV.put("volume", Integer.valueOf(11));
        } else {
            insertCV.put("volume", Integer.valueOf(volume));
        }
        String backupSoundUri = backupCV.getAsString("alarmuri");
        String restoreSoundUri = (ClockUtils.isEnableString(backupSoundUri) && this.mIsSamePhone) ? backupSoundUri : this.mDefaultSoundUri;
        insertCV.put("alarmuri", restoreSoundUri);
        insertCV.put("alarmtone", Integer.valueOf(new AlarmRingtoneManager(context).getRingtoneIndex(restoreSoundUri)));
        insertCV.put("dailybrief", Integer.toString(getValidBriefing(context, backupCV)));
        insertCV.put("name", backupCV.getAsString("name"));
        insertCV.put("vibrationpattern", getValidVibPattern(context, backupCV));
        insertCV.put("locationtext", "");
        String backupCelebPath = backupCV.getAsString("map");
        String insertCelebPath = "";
        if (mIsSupportCelebVoice) {
            insertCelebPath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        }
        Log.secD(TAG, "insertCelebPath =" + insertCelebPath);
        insertCV.put("map", insertCelebPath);
        return insertCV;
    }

    private int getValidBriefing(Context context, ContentValues backupCV) {
        int backupBriefing;
        if (8 <= this.mBackupVersion) {
            backupBriefing = Integer.parseInt(backupCV.getAsString("dailybrief_BACKUP_VER_8"));
        } else {
            backupBriefing = Integer.parseInt(backupCV.getAsString("dailybrief"));
            if (AlarmItem.isMasterSoundOn(backupBriefing)) {
                if (Integer.parseInt(backupCV.getAsString("alarmsound")) == 1) {
                    Log.m41d(TAG, "vibration type -> master sound off");
                    backupBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setMasterSoundOn(backupBriefing, false), true);
                }
                String backupSoundUri = backupCV.getAsString("alarmuri");
                if (ClockUtils.isEnableString(backupSoundUri) && "alarm_silent_ringtone".equals(backupSoundUri)) {
                    Log.m41d(TAG, "Silent Ringtone -> master sound off, alarm tone on");
                    backupBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setMasterSoundOn(backupBriefing, false), true);
                }
                if (Integer.parseInt(backupCV.getAsString("volume")) == 0) {
                    Log.m41d(TAG, "alarm tone off, bixby off -> master sound off");
                    backupBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setMasterSoundOn(backupBriefing, false), true);
                }
                if (!(AlarmItem.isAlarmToneOn(backupBriefing) || AlarmItem.isBixbyBriefingOn(backupBriefing))) {
                    Log.m41d(TAG, "alarm tone off, bixby off -> master sound off, alarm tone on");
                    backupBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setMasterSoundOn(backupBriefing, false), true);
                }
                if (AlarmItem.isBixbyBriefingOn(backupBriefing) && AlarmItem.isBixbyVoiceOn(backupBriefing) && mIsSupportBixbyBriefing) {
                    Log.m41d(TAG, "bixby briefing, bixby voice -> new bixby");
                    backupBriefing = AlarmItem.setSoundModeNewBixby(backupBriefing);
                } else if (AlarmItem.isBixbyBriefingOn(backupBriefing) && AlarmItem.isBixbyCelebVoice(backupBriefing) && mIsSupportCelebVoice) {
                    Log.m41d(TAG, "bixby celeb voice -> new celeb");
                    backupBriefing = AlarmItem.setSoundModeNewCeleb(backupBriefing);
                } else {
                    Log.m41d(TAG, "alarm tone");
                    backupBriefing = AlarmItem.setSoundModeAlarmTone(backupBriefing);
                }
            } else {
                Log.m41d(TAG, "backup master sound off -> set alarm tone mode");
                backupBriefing = AlarmItem.setSoundModeAlarmTone(backupBriefing);
            }
        }
        if ((AlarmItem.isNewCelebOn(backupBriefing) && !mIsSupportCelebVoice) || (AlarmItem.isNewBixbyOn(backupBriefing) && !mIsSupportBixbyBriefing)) {
            Log.m41d(TAG, "feature not support-> alarm tone mode");
            backupBriefing = AlarmItem.setSoundModeAlarmTone(backupBriefing);
        }
        backupBriefing = AlarmItem.initRecommendWeatherBg(AlarmItem.initIncreasingVolume(AlarmItem.initNotUsedBixbyCeleb(backupBriefing)));
        Log.secD(TAG, "getValidBriefing() / " + Integer.toBinaryString(backupBriefing));
        return backupBriefing;
    }

    private String getValidVibPattern(Context context, ContentValues backupCV) {
        String backupVibPattern = backupCV.getAsString("vibrationpattern");
        String backupVibPatternUser = backupCV.getAsString("vibrationpattern_user");
        String restoreVibPattern = Integer.toString(50035);
        if (backupVibPatternUser != null && AlarmUtil.isValidVibPattern(context, Integer.parseInt(backupVibPatternUser))) {
            restoreVibPattern = backupVibPatternUser;
        } else if (backupVibPattern != null && AlarmUtil.isValidVibPattern(context, Integer.parseInt(backupVibPattern))) {
            restoreVibPattern = backupVibPattern;
        }
        Log.secD(TAG, "BNR_ALARM_VIB : backupVib = " + backupVibPattern + "/ backupVibUser = " + backupVibPatternUser + " / restoreVib = " + restoreVibPattern);
        return restoreVibPattern;
    }

    public boolean restoreComplete(Context context, String[] insertedIdList, boolean isSuccess) {
        Log.secD(TAG, "restoreComplete() / isSuccess = " + isSuccess);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(BackupRestoreUtils.getAlarmDBPath(context), null, 0);
        String keys = ClockUtils.getWhereKey(insertedIdList);
        Log.secD(TAG, keys);
        int size = 0;
        Intent isScloudRestore = new Intent();
        isScloudRestore.setPackage("com.sec.android.app.clockpackage");
        isScloudRestore.setAction("com.sec.android.clockpackage.SCLOUD_RESTORE_ALARM");
        if (insertedIdList != null && insertedIdList.length > 0) {
            size = insertedIdList.length;
        }
        if (isSuccess) {
            if (size > 0) {
                String firstKey = insertedIdList[0];
                Cursor cursor = db.query(NotificationCompat.CATEGORY_ALARM, null, "_id < " + firstKey, null, null, null, null);
                if (cursor == null || cursor.getCount() <= 0) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    db.delete(NotificationCompat.CATEGORY_ALARM, "_id < " + firstKey, null);
                } else {
                    cursor.moveToFirst();
                    do {
                        AlarmNotificationHelper.clearNotification(context, cursor.getInt(cursor.getColumnIndex("_id")));
                    } while (cursor.moveToNext());
                    if (cursor != null) {
                        cursor.close();
                    }
                    db.delete(NotificationCompat.CATEGORY_ALARM, "_id < " + firstKey, null);
                }
            }
            if (insertedIdList != null && size == 0) {
                db.execSQL("delete from alarm");
            }
            context.sendBroadcast(isScloudRestore);
        } else if (size > 0) {
            db.delete(NotificationCompat.CATEGORY_ALARM, "_id IN " + keys, null);
        }
        db.close();
        return true;
    }
}
