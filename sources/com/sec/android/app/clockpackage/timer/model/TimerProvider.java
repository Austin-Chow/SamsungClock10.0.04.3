package com.sec.android.app.clockpackage.timer.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.C0728R;

public class TimerProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.samsung.sec.android.clockpackage.timer/timerlife");
    public static final String DEFAULT_ORDER_BY = "timerorder asc";
    public static final String[] QUERY_COLUMNS = new String[]{"id", "timername", "timerimagename", "time", "timerorder"};
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private SQLiteDatabase mDb;
    private SQLiteOpenHelper mOpenHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private Context mContext;

        private DatabaseHelper(Context context) {
            super(context, "timer.db", null, 3);
            this.mContext = context;
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS timerlife (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,timername TEXT NOT NULL,timerimagename TEXT NOT NULL,time TEXT NOT NULL,timerorder INTEGER NOT NULL)");
            if (Feature.isSupportChinaPresetTimer()) {
                addDefaultPreset(db);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.m41d("TimerProvider", "onUpgrade : Upgrade database from version " + oldVersion + " to " + newVersion);
        }

        private void addDefaultPreset(SQLiteDatabase db) {
            Cursor cursor = db.rawQuery("select * from timerlife", null);
            if (cursor != null) {
                int count = cursor.getCount();
                cursor.close();
                if (count == 0) {
                    String[] presetName = this.mContext.getResources().getStringArray(C0728R.array.timer_default_preset_name);
                    String[] presetTime = this.mContext.getResources().getStringArray(C0728R.array.timer_default_preset_time);
                    int[] presetOrder = this.mContext.getResources().getIntArray(C0728R.array.timer_default_preset_order);
                    ContentValues values = new ContentValues();
                    try {
                        db.beginTransaction();
                        for (int i = 0; i < presetName.length; i++) {
                            values.put("timername", presetName[i]);
                            values.put("timerimagename", "default");
                            values.put("time", presetTime[i]);
                            values.put("timerorder", Integer.valueOf(presetOrder[i]));
                            db.insert("timerlife", null, values);
                        }
                        db.setTransactionSuccessful();
                    } catch (SQLException e) {
                        Log.secE("TimerProvider", "addDefaultPreset SQLException");
                    } finally {
                        db.endTransaction();
                    }
                }
            }
        }
    }

    static {
        sUriMatcher.addURI("com.samsung.sec.android.clockpackage.timer", "timerlife", 1);
        sUriMatcher.addURI("com.samsung.sec.android.clockpackage.timer", "timerlife/#", 2);
    }

    public boolean onCreate() {
        this.mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case 1:
                qb.setTables("timerlife");
                break;
            case 2:
                qb.setTables("timerlife");
                qb.appendWhere("id=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Cannot query URL : " + uri);
        }
        try {
            String str;
            SQLiteDatabase db = getDb();
            if (TextUtils.isEmpty(sortOrder)) {
                str = DEFAULT_ORDER_BY;
            } else {
                str = sortOrder;
            }
            Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, str);
            if (cursor == null) {
                Log.m41d("TimerProvider", "Timer query failed");
                return cursor;
            }
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } catch (SQLException e) {
            Log.secE("TimerProvider", "query() / SQLException : " + e);
            return null;
        }
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        try {
            switch (sUriMatcher.match(uri)) {
                case 1:
                    Uri uriResult = ContentUris.withAppendedId(CONTENT_URI, getDb().insert("timerlife", null, values));
                    getContext().getContentResolver().notifyChange(uriResult, null);
                    return uriResult;
                default:
                    throw new IllegalArgumentException("Cannot insert URL : " + uri);
            }
        } catch (SQLException e) {
            Log.secE("TimerProvider", "insert() / SQLException : " + e);
            if (e instanceof SQLiteFullException) {
                return null;
            }
            throw e;
        }
        Log.secE("TimerProvider", "insert() / SQLException : " + e);
        if (e instanceof SQLiteFullException) {
            return null;
        }
        throw e;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        try {
            int count;
            switch (sUriMatcher.match(uri)) {
                case 1:
                    count = getDb().delete("timerlife", selection, selectionArgs);
                    break;
                case 2:
                    String str;
                    StringBuilder append = new StringBuilder().append("id=").append(uri.getLastPathSegment());
                    if (TextUtils.isEmpty(selection)) {
                        str = "";
                    } else {
                        str = " AND (" + selection + ")";
                    }
                    count = getDb().delete("timerlife", append.append(str).toString(), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Cannot delete URL : " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } catch (SQLException e) {
            Log.secE("TimerProvider", "delete() / SQLException : " + e);
            if (!(e instanceof SQLiteFullException)) {
                return 0;
            }
            throw e;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try {
            int count;
            switch (sUriMatcher.match(uri)) {
                case 1:
                    count = getDb().update("timerlife", values, selection, selectionArgs);
                    break;
                case 2:
                    String str;
                    StringBuilder append = new StringBuilder().append("id=").append(uri.getLastPathSegment());
                    if (TextUtils.isEmpty(selection)) {
                        str = "";
                    } else {
                        str = " AND (" + selection + ")";
                    }
                    count = getDb().update("timerlife", values, append.append(str).toString(), selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Cannot update URL : " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } catch (SQLException e) {
            Log.secE("TimerProvider", "update() / SQLException : " + e);
            if (!(e instanceof SQLiteFullException)) {
                return 0;
            }
            throw e;
        }
    }

    private SQLiteDatabase getDb() throws SQLException {
        if (this.mDb == null) {
            this.mDb = this.mOpenHelper.getWritableDatabase();
        }
        return this.mDb;
    }
}
