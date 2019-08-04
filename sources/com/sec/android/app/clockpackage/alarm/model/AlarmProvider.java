package com.sec.android.app.clockpackage.alarm.model;

import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Parcel;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Time;
import com.samsung.android.feature.SemGateConfig;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.samsung.sec.android.clockpackage/alarm");
    public static long sNextAlarmAlertTime = 0;
    private static PendingIntent sPendingIntent;
    private static PendingIntent sPendingIntent2;
    private static PendingIntent sPendingIntentForBixbyBriefing;
    private static PendingIntent sPendingIntentForPreDismissedAlarm;
    private SQLiteOpenHelper mOpenHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private final int mDatabaseVersion;

        private DatabaseHelper(Context context) {
            super(context, "alarm.db", null, 6);
            this.mDatabaseVersion = 6;
            Log.secD("AlarmProvider", "DatabaseHelper: " + this.mDatabaseVersion);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.secD("AlarmProvider", "creating new alarm database");
            db.execSQL("CREATE TABLE IF NOT EXISTS alarm (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,active INTEGER NOT NULL,createtime LONG NOT NULL,alerttime LONG NOT NULL,alarmtime INTEGER NOT NULL,repeattype INTEGER NOT NULL,notitype INTEGER NOT NULL,snzactive INTEGER NOT NULL,snzduration INTEGER NOT NULL,snzrepeat INTEGER NOT NULL,snzcount INTEGER NOT NULL,dailybrief INTEGER NOT NULL,sbdactive INTEGER NOT NULL,sbdduration INTEGER NOT NULL,sbdtone INTEGER NOT NULL,alarmsound INTEGER NOT NULL,alarmtone INTEGER NOT NULL,volume INTEGER NOT NULL,sbduri INTEGER NOT NULL,alarmuri TEXT,name TEXT,locationactive INTEGER NOT NULL DEFAULT 0,latitude DOUBLE NOT NULL DEFAULT 0,longitude DOUBLE NOT NULL DEFAULT 0,locationtext TEXT,map TEXT,vibrationpattern INTEGER NOT NULL)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Cursor cursor;
            Log.secD("AlarmProvider", "onUpgrade triggered");
            int version = oldVersion;
            Log.secD("AlarmProvider", "old version : " + oldVersion + " new version : " + newVersion);
            if (oldVersion == 5 && newVersion == 6) {
                cursor = db.rawQuery("select _id, dailybrief, volume from alarm", null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int _id = cursor.getInt(0);
                        int dailyBrief = cursor.getInt(1);
                        int volume = cursor.getInt(2);
                        boolean masterSound = AlarmItem.isMasterSoundOn(dailyBrief);
                        boolean alarmToneOn = AlarmItem.isAlarmToneOn(dailyBrief);
                        boolean bixbyBriefingOn = AlarmItem.isBixbyBriefingOn(dailyBrief);
                        boolean bixbyVoiceOn = AlarmItem.isBixbyVoiceOn(dailyBrief);
                        boolean celebOn = AlarmItem.isBixbyCelebVoice(dailyBrief);
                        boolean newBixbyOn = AlarmItem.isNewBixbyOn(dailyBrief);
                        Log.secD("AlarmProvider", "0 id = " + _id + "/ volume = " + volume + "/ master sounnd = " + masterSound + "/ alarmToneOn = " + alarmToneOn + "/ bixbyBriefingOn =" + bixbyBriefingOn + "/ bixbyVoiceOn =" + bixbyVoiceOn + "/ celebOn =" + celebOn + "/ newBixbyOn =" + newBixbyOn + "/ newCelebOn =" + AlarmItem.isNewCelebOn(dailyBrief));
                        if (!(AlarmItem.isAlarmToneOn(dailyBrief) || AlarmItem.isBixbyBriefingOn(dailyBrief) || !AlarmItem.isMasterSoundOn(dailyBrief))) {
                            Log.secD("AlarmProvider", "bixby off / tone off -> master off");
                            dailyBrief = AlarmItem.setAlarmToneOn(AlarmItem.setMasterSoundOn(dailyBrief, false), true);
                        }
                        if (volume == 0 && AlarmItem.isMasterSoundOn(dailyBrief)) {
                            Log.secD("AlarmProvider", "volume 0 -> master off, default vol");
                            dailyBrief = AlarmItem.setMasterSoundOn(dailyBrief, false);
                            volume = 11;
                        }
                        if (AlarmItem.isBixbyBriefingOn(dailyBrief)) {
                            if (AlarmItem.isBixbyCelebVoice(dailyBrief)) {
                                dailyBrief = AlarmItem.setSoundModeNewCeleb(dailyBrief);
                            } else if (AlarmItem.isBixbyVoiceOn(dailyBrief)) {
                                dailyBrief = AlarmItem.setSoundModeNewBixby(dailyBrief);
                            } else {
                                dailyBrief = AlarmItem.setSoundModeAlarmTone(dailyBrief);
                            }
                        }
                        dailyBrief = AlarmItem.initNotUsedBixbyCeleb(dailyBrief);
                        masterSound = AlarmItem.isMasterSoundOn(dailyBrief);
                        alarmToneOn = AlarmItem.isAlarmToneOn(dailyBrief);
                        bixbyBriefingOn = AlarmItem.isBixbyBriefingOn(dailyBrief);
                        bixbyVoiceOn = AlarmItem.isBixbyVoiceOn(dailyBrief);
                        celebOn = AlarmItem.isBixbyCelebVoice(dailyBrief);
                        newBixbyOn = AlarmItem.isNewBixbyOn(dailyBrief);
                        Log.secD("AlarmProvider", "1 id = " + _id + "/ volume = " + volume + "/ master sounnd = " + masterSound + "/ alarmToneOn = " + alarmToneOn + "/ bixbyBriefingOn =" + bixbyBriefingOn + "/ bixbyVoiceOn =" + bixbyVoiceOn + "/ celebOn =" + celebOn + "/ newBixbyOn =" + newBixbyOn + "/ newCelebOn =" + AlarmItem.isNewCelebOn(dailyBrief));
                        db.execSQL("update alarm set dailybrief = " + dailyBrief + ", " + "volume" + " = " + volume + " WHERE _id = " + _id + ';');
                    }
                    cursor.close();
                }
                version = newVersion;
            }
            if (version < 5) {
                Log.secD("AlarmProvider", "add column alarm Table version 5");
                db.execSQL("ALTER TABLE alarm ADD COLUMN vibrationpattern INTEGER NOT NULL DEFAULT 50035");
                version = newVersion;
            }
            if (version < 3) {
                cursor = db.rawQuery("select _id, sbdduration from alarm", null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        SQLiteDatabase sQLiteDatabase = db;
                        sQLiteDatabase.execSQL("update alarm set sbdduration = " + (cursor.getInt(1) + 1) + " WHERE _id = " + cursor.getInt(0) + ';');
                    }
                    cursor.close();
                }
            }
            if (version < this.mDatabaseVersion) {
                if (version == 1) {
                    Log.secD("AlarmProvider", "add column alarm Table version 1 to 2");
                    db.execSQL("ALTER TABLE alarm ADD COLUMN locationactive INTEGER NOT NULL DEFAULT 0");
                    db.execSQL("ALTER TABLE alarm ADD COLUMN latitude DOUBLE NOT NULL DEFAULT 0");
                    db.execSQL("ALTER TABLE alarm ADD COLUMN longitude DOUBLE NOT NULL DEFAULT 0");
                    db.execSQL("ALTER TABLE alarm ADD COLUMN locationtext TEXT");
                    db.execSQL("ALTER TABLE alarm ADD COLUMN map TEXT");
                }
                version = newVersion;
            }
            if (version != this.mDatabaseVersion) {
                Log.secW("AlarmProvider", "Destroying all old data.");
                db.execSQL("DROP TABLE IF EXISTS alarm");
                onCreate(db);
            }
        }
    }

    private static class SqlArguments {
        public final String[] args;
        public final String table;
        public final String where;

        private SqlArguments(Uri url, String where, String[] args) {
            if (url.getPathSegments().size() == 1) {
                this.table = (String) url.getPathSegments().get(0);
                this.where = where;
                this.args = args;
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (TextUtils.isEmpty(where)) {
                this.table = (String) url.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(url);
                this.args = null;
            } else {
                throw new UnsupportedOperationException("WHERE clause not supported: " + url);
            }
        }

        private SqlArguments(Uri url) {
            if (url.getPathSegments().size() == 1) {
                this.table = (String) url.getPathSegments().get(0);
                this.where = null;
                this.args = null;
                return;
            }
            throw new IllegalArgumentException("Invalid URI: " + url);
        }
    }

    public boolean onCreate() {
        Context context = getContext();
        if (StateUtils.isDirectBootMode(context)) {
            Context deviceContext = context.createDeviceProtectedStorageContext();
            if (!deviceContext.moveDatabaseFrom(context, "alarm.db")) {
                Log.secE("AlarmProvider", "Failed to migrate database");
            }
            this.mOpenHelper = new DatabaseHelper(deviceContext);
        } else {
            this.mOpenHelper = new DatabaseHelper(context);
        }
        Logger.init(context);
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db;
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(args.table);
        try {
            db = this.mOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.secE("AlarmProvider", "query Exception");
            try {
                db = this.mOpenHelper.getReadableDatabase();
            } catch (SQLiteException e2) {
                Log.m42e("AlarmProvider", "Exception : " + e2.toString());
                return null;
            }
        }
        Cursor result = qb.query(db, projection, args.where, args.args, null, null, sortOrder);
        if (result == null) {
            return result;
        }
        Context context = getContext();
        if (context == null) {
            return result;
        }
        result.setNotificationUri(context.getContentResolver(), uri);
        return result;
    }

    public Uri insert(Uri uri, ContentValues initialValues) {
        Uri uri2 = null;
        SqlArguments args = new SqlArguments(uri);
        try {
            long rowId = this.mOpenHelper.getWritableDatabase().insert(args.table, null, initialValues);
            if (rowId > 0) {
                uri2 = ContentUris.withAppendedId(uri, rowId);
                sendAlarmInformation(uri2);
                if (SemGateConfig.isGateEnabled()) {
                    Log.m43i("GATE", "<GATE-M>ALARM_CREATED :  </GATE-M>");
                }
            }
            return uri2;
        } catch (Exception e) {
            Log.secE("AlarmProvider", "insert Exception");
            throw e;
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        try {
            int count = this.mOpenHelper.getWritableDatabase().delete(args.table, args.where, args.args);
            if (count > 0) {
                sendAlarmInformation(uri);
            }
            if (SemGateConfig.isGateEnabled()) {
                Log.m43i("GATE", "<GATE-M>ALARM_DELETED :  </GATE-M>");
            }
            return count;
        } catch (Exception e) {
            Log.secE("AlarmProvider", "delete Exception");
            throw e;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
        try {
            int count = this.mOpenHelper.getWritableDatabase().update(args.table, values, args.where, args.args);
            if (count > 0) {
                sendAlarmInformation(uri);
            }
            return count;
        } catch (Exception e) {
            Log.secE("AlarmProvider", "update Exception");
            throw e;
        }
    }

    public String getType(Uri uri) {
        SqlArguments args = new SqlArguments(uri, null, null);
        if (TextUtils.isEmpty(args.where)) {
            return "vnd.android.cursor.dir/" + args.table;
        }
        return "vnd.android.cursor.item/" + args.table;
    }

    private void sendNotify(Uri uri) {
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    private void sendAlarmInformation(Uri uri) {
        Log.secD("AlarmProvider", "sendAlarmInformation uri = " + uri);
        sendNotify(uri);
        sendAlarmChangedIntent(getContext());
    }

    private static void SetNextAlarmTime(Context context, long alarmAlertTime) {
        Intent intent = new Intent("com.sec.android.app.clockpackage.alarm.ALARMALERTTIME");
        intent.putExtra("alarmAlertTime", alarmAlertTime);
        Log.secV("AlarmProvider", "SetNextAT: " + alarmAlertTime);
        intent.setPackage("com.android.internal.policy.impl");
        intent.setFlags(32);
        context.sendBroadcast(intent);
    }

    public static void dismissAlarm(Context context, AlarmItem item, String action) {
        if (item != null) {
            Log.secD("AlarmProvider", "dismissAlarm id = " + item.mId);
            long upcomingAlertTime = item.mAlarmAlertTime + 1;
            int snoozeRepeatTimes = item.getSnoozeRepeatTimes();
            if (!item.mSnoozeActivate || (item.mSnoozeDoneCount >= snoozeRepeatTimes && AlarmItemUtil.CONTINUOUSLY != snoozeRepeatTimes)) {
                item.calculateNextAlertTime(upcomingAlertTime);
            } else if (item.isOneTimeAlarm()) {
                if (item.isSpecificDate()) {
                    item.setSpecificDate(false);
                }
                if (item.getAlertDayCount() == 1) {
                    item.mActivate = 0;
                    item.calculateNextAlertTime(upcomingAlertTime);
                } else {
                    item.clearRepeatDay(Calendar.getInstance());
                    item.calculateOriginalAlertTime();
                    item.mActivate = 1;
                    item.calculateFirstAlertTime(context, upcomingAlertTime);
                    item.mSnoozeDoneCount = 0;
                }
            } else {
                item.calculateOriginalAlertTime();
                item.mActivate = 1;
                item.calculateFirstAlertTime(context, upcomingAlertTime);
                item.mSnoozeDoneCount = 0;
            }
            if (("com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_DISMISS".equals(action) || "com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_DISMISS_COVERSTATE".equals(action)) && item.mActivate == 1 && item.isFirstAlarm() && item.isWeeklyAlarm()) {
                AlarmSharedManager.addPreDismissedAlarmInformation(context, item.mId, upcomingAlertTime - 1);
                enableNextPreDismissedAlert(context);
            }
            context.getContentResolver().update(CONTENT_URI, item.getContentValues(), "_id = " + item.mId, null);
            enableNextAlert(context);
        }
    }

    public static boolean hasAlarm(Context context, int id) {
        boolean bHasAlarm = false;
        Cursor c1 = context.getContentResolver().query(CONTENT_URI, null, "_id = " + id, null, "createtime DESC");
        if (c1 != null) {
            c1.moveToFirst();
            if (c1.getCount() == 1) {
                bHasAlarm = true;
                Log.secD("AlarmProvider", "hasAlarm bHasAlarm = true");
            }
            c1.close();
        }
        return bHasAlarm;
    }

    public static AlarmItem getAlarm(Context context, int id) {
        Cursor c1 = context.getContentResolver().query(CONTENT_URI, null, "_id = " + id, null, "createtime DESC");
        if (c1 == null) {
            return null;
        }
        c1.moveToFirst();
        int nCount = c1.getCount();
        if (nCount != 1) {
            Log.secD("AlarmProvider", "getAlarm() - too many same ID alarm data. nCount: " + nCount);
            c1.close();
            return null;
        }
        AlarmItem item = AlarmItem.createItemFromCursor(c1);
        c1.close();
        return item;
    }

    private static int getNextAlarmId(Context context) {
        int nextAlarmId = -1;
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[]{"_id"}, "active > 0", null, "alerttime ASC, active ASC, createtime DESC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                nextAlarmId = cursor.getInt(0);
            }
            cursor.close();
        }
        Log.secD("AlarmProvider", "getNextAlarm select id: " + nextAlarmId);
        return nextAlarmId;
    }

    public static ArrayList<Integer> getNextAlarmWithin5m(Context context) {
        ArrayList<Integer> candidateUpcomingAlarmIds = new ArrayList();
        String selection = System.currentTimeMillis() + " < " + "alerttime" + " AND " + "alerttime" + " <= " + (System.currentTimeMillis() + 299000) + " AND " + "active" + " = " + 1;
        Log.secD("AlarmProvider", "getNextAlarm select " + selection);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[]{"_id"}, selection, null, "alerttime ASC , createtime DESC");
        if (cursor != null) {
            String logCursorIds = "";
            while (cursor.moveToNext()) {
                int cursorId = cursor.getInt(0);
                candidateUpcomingAlarmIds.add(Integer.valueOf(cursorId));
                logCursorIds = logCursorIds + " " + cursorId;
            }
            cursor.close();
            Log.secD("AlarmProvider", "getNextAlarmWithin5m logCursorIds" + logCursorIds);
        }
        return candidateUpcomingAlarmIds;
    }

    public static int getNextAlarmAfter5m(Context context) {
        int id = -1;
        String selection = ((System.currentTimeMillis() + 299000) + 1000) + " < " + "alerttime" + " AND " + "active" + " = " + 1;
        Log.secD("AlarmProvider", "getNextAlarmAfter5m select " + selection);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[]{"_id"}, selection, null, "alerttime ASC , createtime DESC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                id = cursor.getInt(0);
            }
            cursor.close();
        }
        Log.secD("AlarmProvider", "getNextAlarmAfter5m id" + id);
        return id;
    }

    public static void disableAlert(Context context) {
        Logger.m47f("AlarmProvider", "disableAlert");
        cancelPendingIntent(context);
        sNextAlarmAlertTime = 0;
        sendNextAlarmTime(context);
    }

    public static void enableNextAlert(Context context) {
        AlarmItem item = null;
        try {
            cancelPendingIntent(context);
            item = getAlarm(context, getNextAlarmId(context));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Logger.m47f("AlarmProvider", "set e = " + sw.toString());
        }
        Log.secD("AlarmProvider", "enableNextAlert next alarm : " + (item == null ? "null" : item.toStringShort()));
        if (item != null) {
            try {
                Calendar.getInstance().setTimeInMillis(item.mAlarmAlertTime);
                Intent intent = new Intent("com.samsung.sec.android.clockpackage.alarm.EXPLICIT_ALARM_ALERT");
                intent.setPackage("com.sec.android.app.clockpackage");
                Parcel out = Parcel.obtain();
                item.writeToParcel(out);
                out.setDataPosition(0);
                intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
                intent.addFlags(402653184);
                sPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 268435456);
                ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setAlarmClock(new AlarmClockInfo(item.mAlarmAlertTime, sPendingIntent), sPendingIntent);
                Logger.m47f("AlarmProvider", "set " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(item.mAlarmAlertTime)) + ' ' + AlarmItem.digitToAlphabetStr(Integer.toString(item.mAlarmTime)) + ' ' + item.mActivate + (item.mSnoozeActivate ? "-" + item.getSnoozeDuration() + '-' + item.getSnoozeRepeatTimes() + '-' + item.mSnoozeDoneCount : ""));
                if (!item.isFirstAlarm()) {
                    enableNextUpcomingAlertAfter5m(context);
                } else if (System.currentTimeMillis() < item.mAlarmAlertTime) {
                    enableNextUpcomingAlert(context, item.mAlarmAlertTime, out);
                    if (item.isMasterSoundOn() && (item.isNewBixbyOn() || item.isNewCelebOn())) {
                        enableNextBixbyBriefingAlert(context, item.mId, out);
                    }
                }
                sNextAlarmAlertTime = item.mAlarmAlertTime;
                out.recycle();
            } catch (Exception e2) {
                sw = new StringWriter();
                e2.printStackTrace(new PrintWriter(sw));
                Logger.m47f("AlarmProvider", "set e2 = " + sw.toString());
                return;
            }
        }
        Logger.m47f("AlarmProvider", "set null");
        sNextAlarmAlertTime = 0;
        sendNextAlarmTime(context);
    }

    private static void enableNextUpcomingAlert(Context context, long alertTime, Parcel out) {
        Intent upcomingAlert = new Intent("com.samsung.sec.android.clockpackage.alarm.UPCOMING_ALERT");
        upcomingAlert.setPackage("com.sec.android.app.clockpackage");
        long resultTime = alertTime - 299000;
        if (resultTime < System.currentTimeMillis()) {
            upcomingAlert.putExtra("direct_register_upcoming", true);
        }
        upcomingAlert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
        upcomingAlert.addFlags(402653184);
        sPendingIntent2 = PendingIntent.getBroadcast(context, 0, upcomingAlert, 268435456);
        ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setAlarmClock(new AlarmClockInfo(resultTime, sPendingIntent2), sPendingIntent2);
        Log.secD("AlarmProvider", "UPCOMING_ALERT next alarm : " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(resultTime)));
    }

    public static void enableNextUpcomingAlertAfter5m(Context context) {
        cancelUpcomingPendingIntent(context);
        int id = getNextAlarmAfter5m(context);
        if (id != -1) {
            AlarmItem item = getAlarm(context, id);
            long currentTime = System.currentTimeMillis();
            if (item.isFirstAlarm() && currentTime < item.mAlarmAlertTime) {
                Intent upcomingAlert = new Intent("com.samsung.sec.android.clockpackage.alarm.UPCOMING_ALERT");
                upcomingAlert.setPackage("com.sec.android.app.clockpackage");
                long resultTime = item.mAlarmAlertTime - 299000;
                if (currentTime < resultTime) {
                    Parcel out = Parcel.obtain();
                    item.writeToParcel(out);
                    out.setDataPosition(0);
                    upcomingAlert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
                    out.recycle();
                    upcomingAlert.addFlags(402653184);
                    sPendingIntent2 = PendingIntent.getBroadcast(context, 0, upcomingAlert, 268435456);
                    ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setAlarmClock(new AlarmClockInfo(resultTime, sPendingIntent2), sPendingIntent2);
                    Log.secD("AlarmProvider", "UPCOMING_ALERT after 5m next alarm : " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(resultTime)));
                }
            }
        }
    }

    private static void enableNextBixbyBriefingAlert(Context context, int alarmId, Parcel out) {
        AlarmItem item = getAlarm(context, alarmId);
        if (item == null) {
            Log.m41d("AlarmProvider", "enableNextBixbyBriefingAlert return");
        } else if (Feature.isSupportBixbyBriefingMenu(context)) {
            AlarmManager am = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            Intent intent = new Intent("com.samsung.sec.android.clockpackage.alarm.UPCOMING_BIXBY_BRIEFING_ALERT");
            intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
            intent.addFlags(402653184);
            intent.setPackage("com.sec.android.app.clockpackage");
            sPendingIntentForBixbyBriefing = PendingIntent.getBroadcast(context, 0, intent, 268435456);
            long alertTime = item.mAlarmAlertTime - 59000;
            if (System.currentTimeMillis() < alertTime) {
                long randomNumber = ((long) (Math.random() * 500.0d)) * 100;
                alertTime += randomNumber;
                Log.m41d("AlarmProvider", "alertTime =  " + alertTime + ", randomNumber =  " + randomNumber + " ms");
            }
            am.setAlarmClock(new AlarmClockInfo(alertTime, sPendingIntentForBixbyBriefing), sPendingIntentForBixbyBriefing);
            Log.m41d("AlarmProvider", "enableNextBixbyBriefingAlert alertTime : " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(alertTime)));
        } else {
            Log.m41d("AlarmProvider", "enableNextBixbyBriefingAlert !isSupportBixbyBriefingMenu return");
        }
    }

    public static void enableNextPreDismissedAlert(Context context) {
        cancelPreDismissedAlarmsPendingIntent(context);
        long currentTime = System.currentTimeMillis();
        long closetAlertTime = AlarmSharedManager.getClosetAlertTimeInPreDismissedAlarms(context, currentTime).longValue();
        if (closetAlertTime >= currentTime) {
            Intent preDismissedAlert = new Intent("com.sec.android.app.clockpackage.alarm.REMOVE_PRE_DISMISSED_ALARMS");
            preDismissedAlert.setPackage("com.sec.android.app.clockpackage");
            preDismissedAlert.addFlags(402653184);
            sPendingIntentForPreDismissedAlarm = PendingIntent.getBroadcast(context, 0, preDismissedAlert, 268435456);
            ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setExactAndAllowWhileIdle(0, closetAlertTime, sPendingIntentForPreDismissedAlarm);
            Log.secD("AlarmProvider", "enableNextPreDismissedAlert next alarm : " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(closetAlertTime)));
        }
    }

    private static void cancelUpcomingPendingIntent(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (sPendingIntent2 != null) {
            am.cancel(sPendingIntent2);
        } else {
            Intent intent = new Intent("com.samsung.sec.android.clockpackage.alarm.UPCOMING_ALERT");
            intent.setPackage("com.sec.android.app.clockpackage");
            sPendingIntent2 = PendingIntent.getBroadcast(context, 0, intent, 134217728);
            if (sPendingIntent2 != null) {
                am.cancel(sPendingIntent2);
            } else {
                Log.m42e("AlarmProvider", "cancelPendingIntent sPendingIntent2 == null");
            }
        }
        sPendingIntent2 = null;
    }

    private static void cancelPendingIntent(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (sPendingIntent != null) {
            am.cancel(sPendingIntent);
            Logger.m47f("AlarmProvider", "cancelPI");
        } else {
            Intent intent = new Intent("com.samsung.sec.android.clockpackage.alarm.EXPLICIT_ALARM_ALERT");
            intent.setPackage("com.sec.android.app.clockpackage");
            sPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 134217728);
            if (sPendingIntent != null) {
                am.cancel(sPendingIntent);
                Logger.m47f("AlarmProvider", "cancelPI else");
            } else {
                Log.m42e("AlarmProvider", "cancelPendingIntent sPendingIntent == null");
            }
        }
        sPendingIntent = null;
        cancelUpcomingPendingIntent(context);
        if (sPendingIntentForBixbyBriefing != null) {
            am.cancel(sPendingIntentForBixbyBriefing);
        } else {
            Log.secD("AlarmProvider", "cancelPendingIntent sPendingIntentForBixbyBriefing == null");
            intent = new Intent("com.samsung.sec.android.clockpackage.alarm.UPCOMING_BIXBY_BRIEFING_ALERT");
            intent.setPackage("com.sec.android.app.clockpackage");
            sPendingIntentForBixbyBriefing = PendingIntent.getBroadcast(context, 0, intent, 134217728);
            am.cancel(sPendingIntentForBixbyBriefing);
        }
        sPendingIntentForBixbyBriefing = null;
    }

    private static void cancelPreDismissedAlarmsPendingIntent(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (sPendingIntentForPreDismissedAlarm != null) {
            am.cancel(sPendingIntentForPreDismissedAlarm);
        } else {
            Log.secD("AlarmProvider", "cancelPreDismissedAlarmsPendingIntent sPendingIntentForPreDismissedAlarm == null");
            Intent intent = new Intent("com.samsung.sec.android.clockpackage.alarm.UPCOMING_BIXBY_BRIEFING_ALERT");
            intent.setPackage("com.sec.android.app.clockpackage");
            sPendingIntentForPreDismissedAlarm = PendingIntent.getBroadcast(context, 0, intent, 134217728);
            am.cancel(sPendingIntentForPreDismissedAlarm);
        }
        sPendingIntentForPreDismissedAlarm = null;
    }

    private static void sendNextAlarmTime(Context context) {
        long j = -1;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null && packageManager.hasSystemFeature("com.sec.feature.sensorhub")) {
            SetNextAlarmTime(context, sNextAlarmAlertTime);
        }
        if (Feature.isAutoPowerOnOffMenuSupported()) {
            long j2;
            if (sNextAlarmAlertTime > 0) {
                j2 = sNextAlarmAlertTime;
            } else {
                j2 = -1;
            }
            sendAutoPowerUpData(context, j2);
            String str = "AlarmProvider";
            StringBuilder append = new StringBuilder().append("sendNextAlarmTime sNextAlarmAlertTime = ");
            if (sNextAlarmAlertTime > 0) {
                j = sNextAlarmAlertTime;
            }
            Log.secD(str, append.append(j).toString());
        }
    }

    public static ArrayList<Integer> updateAlarmAsNewTime(Context context, String action) {
        int updateCount = 0;
        ArrayList<Integer> changedAlarmIds = new ArrayList();
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, "0 < active", null, null);
        if (cursor != null) {
            long currentTimeMillis = System.currentTimeMillis();
            AlarmSharedManager.remainValidPreDismissedInformation(context);
            ArrayList<Integer> preDismissedAlarmIds = AlarmSharedManager.getPreDismissedAlarmIds(context);
            SQLiteDatabase db = SQLiteDatabase.openDatabase(StateUtils.getAlarmDBContext(context).getDatabasePath("alarm.db").getPath(), null, 0);
            db.beginTransaction();
            while (cursor.moveToNext()) {
                AlarmItem item = AlarmItem.createItemFromCursor(cursor);
                AlarmItem backupItem = (AlarmItem) item.clone();
                Log.secD("AlarmProvider", "Item before update : " + item.toStringShort());
                if (("android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action) || "android.intent.action.BOOT_COMPLETED".equals(action) || "android.intent.action.LOCKED_BOOT_COMPLETED".equals(action) || "android.intent.action.MY_PACKAGE_REPLACED".equals(action) || "com.samsung.sec.android.clockpackage.alarm.EXPLICIT_ALARM_ALERT".equals(action) || "com.sec.android.clockpackage.SCLOUD_RESTORE_ALARM".equals(action) || "com.samsung.android.intent.action.RESPONSE_RESTORE_ALARM".equals(action)) && item.mActivate == 2 && item.mSnoozeDoneCount > 0 && item.mAlarmAlertTime - ((long) item.getSnoozeDurationMinutes()) < currentTimeMillis && currentTimeMillis < item.mAlarmAlertTime) {
                    Log.secD("AlarmProvider", action + " ALARM_SNOOZE continue");
                } else {
                    try {
                        if (item.isSpecificDate() && item.mAlarmAlertTime != -1) {
                            boolean bUpdatedSpecificDateAlertTime = false;
                            if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                                bUpdatedSpecificDateAlertTime = item.updateSpecificDateAlertTime(context);
                            }
                            if (bUpdatedSpecificDateAlertTime) {
                                if (item.mAlarmAlertTime < currentTimeMillis) {
                                    Log.secD("AlarmProvider", "updateAlarmAsNewTime - dateAlarm time is past1");
                                    item.setSpecificDate(false);
                                    item.setOneTimeAlarm();
                                }
                            } else if (currentTimeMillis < item.mAlarmAlertTime) {
                                Log.secD("AlarmProvider", "SpecificDate continue");
                            } else {
                                Log.secD("AlarmProvider", "updateAlarmAsNewTime - dateAlarm time is past2");
                                item.setSpecificDate(false);
                                item.setOneTimeAlarm();
                            }
                        }
                        if (!item.isSpecificDate()) {
                            if (!item.isOneTimeAlarm() || item.mAlarmAlertTime >= currentTimeMillis) {
                                item.mActivate = 1;
                                item.mSnoozeDoneCount = 0;
                                item.calculateOriginalAlertTime();
                                item.calculateFirstAlertTime(context);
                            } else {
                                item.mActivate = 0;
                                item.mSnoozeDoneCount = 0;
                                item.mAlarmAlertTime = -1;
                            }
                        }
                        if (!item.equals(backupItem)) {
                            if (item.isFirstAlarm() && item.isWeeklyAlarm()) {
                                if (preDismissedAlarmIds.contains(Integer.valueOf(item.mId))) {
                                    long doubleNextAlertTime = item.getNextAlertTimeForPreDismissedAlarm(context, item, item.mAlarmAlertTime);
                                    Log.secD("AlarmProvider", "doubleNextAlertTime = " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(doubleNextAlertTime)));
                                    if (doubleNextAlertTime == backupItem.mAlarmAlertTime) {
                                        Log.secD("AlarmProvider", "dismissed UpcomingAlarmId continue");
                                    }
                                }
                            }
                            Log.secD("AlarmProvider", "Item after.. update : " + item.toStringShort());
                            changedAlarmIds.add(Integer.valueOf(item.mId));
                            updateCount += db.update(NotificationCompat.CATEGORY_ALARM, item.getContentValues(), "_id = " + item.mId, null);
                        }
                    } catch (Exception e) {
                        Log.secE("AlarmProvider", "updateAlarmAsNewTime Exception");
                    } finally {
                        db.endTransaction();
                    }
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
            db.close();
            if (updateCount > 0) {
                context.getContentResolver().notifyChange(CONTENT_URI, null);
                sendAlarmChangedIntent(context);
            }
            Log.secD("AlarmProvider", "updateAlarmAsNewTime updateCount : " + updateCount);
        }
        return changedAlarmIds;
    }

    public static int getAlarmCount(Context context) {
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[]{"_id"}, null, null, null);
        if (cursor == null) {
            return 0;
        }
        int itemCount = cursor.getCount();
        Log.secD("AlarmProvider", "getAlarmCount() itemCount : " + itemCount);
        cursor.close();
        return itemCount;
    }

    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }

    private static void sendAutoPowerUpData(Context context, long time) {
        try {
            boolean isAutoPowerUp = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("auto_power_up", true);
            String formalFormatData = getAutoPowerUpDataString(isAutoPowerUp, time);
            if (formalFormatData != null && formalFormatData.length() != 0) {
                Intent intent = new Intent("com.samsung.sec.android.clockpackage.AUTO_POWER_UP");
                intent.putExtra("Alarm_Power_Up_Time", time);
                intent.setPackage("com.samsung.android.SettingsReceiver");
                context.sendBroadcast(intent);
                ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).semSetAutoPowerUp(formalFormatData);
                Log.m41d("AutoPowerUp", "enabled: " + isAutoPowerUp + ", time: " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(time)) + ", formalFormatData: " + AlarmItem.digitToAlphabetStr(formalFormatData));
            }
        } catch (IllegalStateException e) {
            Log.m42e("AlarmProvider", "IllegalStateException e = " + e);
        }
    }

    private static String getAutoPowerUpDataString(boolean isEnable, long time) {
        String formalFormatData = "0000000000000";
        if (!isEnable || time == -1) {
            return formalFormatData;
        }
        Time t = new Time("UTC");
        t.set(time - 300000);
        t.normalize(false);
        String yearString = String.valueOf(t.year);
        String monthString = t.month + 1 < 10 ? "0" + (t.month + 1) : String.valueOf(t.month + 1);
        String dayString = t.monthDay < 10 ? "0" + t.monthDay : String.valueOf(t.monthDay);
        formalFormatData = String.valueOf(1) + yearString + monthString + dayString + (t.hour < 10 ? "0" + t.hour : String.valueOf(t.hour)) + (t.minute < 10 ? "0" + t.minute : String.valueOf(t.minute));
        if (Feature.DEBUG_ENG) {
            Log.secE("AlarmProvider", "formalFormatData : " + formalFormatData);
        }
        String str = formalFormatData;
        return formalFormatData;
    }

    public static void sendAlarmChangedIntent(Context context) {
        if (Feature.DEBUG_ENG) {
            Log.secV("AlarmProvider", "sendAlarmChangedIntent");
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_CHANGE"));
        sendChangeToAlarmWidget(context);
    }

    public static void sendChangeToAlarmWidget(Context context) {
        Log.secD("AlarmProvider", "sendChangeToAlarmWidget");
        Intent intent = new Intent("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET");
        context.sendBroadcast(intent);
        intent.setPackage("com.sec.android.app.clockpackage");
        context.sendBroadcast(intent);
    }

    public static boolean isEcbm(Context context) {
        boolean bEcbmMode = false;
        if (context != null) {
            bEcbmMode = ((TelephonyManager) context.getSystemService("phone")).semIsInEmergencyCallbackMode();
            if (bEcbmMode) {
                Log.secD("AlarmProvider", "isEcbm is true");
            }
        }
        return bEcbmMode;
    }

    private void writeAllAlarmDb(Context context) {
        Cursor cursor = StateUtils.getAlarmDBContext(context).getContentResolver().query(CONTENT_URI, null, null, null, "alarmtime ASC , alerttime ASC");
        if (cursor != null) {
            String log = "";
            while (cursor.moveToNext()) {
                log = log + '\n' + AlarmItem.createItemFromCursor(cursor).toStringShort();
            }
            if (log != null) {
                Logger.m47f("AlarmProvider", log);
            }
            cursor.close();
        }
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Context context = getContext();
        writeAllAlarmDb(context);
        writer.println("clock_version: " + ClockUtils.getVersionInfo(context));
        writer.println(Logger.getLogText(context));
    }
}
