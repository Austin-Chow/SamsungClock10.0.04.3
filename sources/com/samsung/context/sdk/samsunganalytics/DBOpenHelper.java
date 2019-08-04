package com.samsung.context.sdk.samsunganalytics;

import android.database.sqlite.SQLiteDatabase;

public interface DBOpenHelper {
    SQLiteDatabase getReadableDatabase();

    SQLiteDatabase getWritableDatabase();
}
