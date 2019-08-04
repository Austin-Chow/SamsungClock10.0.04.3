package com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.samsung.context.sdk.samsunganalytics.DBOpenHelper;

public class DefaultDBOpenHelper extends SQLiteOpenHelper implements DBOpenHelper {
    public DefaultDBOpenHelper(Context context) {
        super(context, "SamsungAnalytics.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS logs_v2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp INTEGER, logtype TEXT, data TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }
}
