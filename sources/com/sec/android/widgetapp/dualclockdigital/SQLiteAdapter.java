package com.sec.android.widgetapp.dualclockdigital;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.sec.android.app.clockpackage.common.util.Log;

public class SQLiteAdapter {
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;

    private static class DbHelper extends SQLiteOpenHelper {
        private DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS dualclock (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, city TEXT NOT NULL, gmt INTEGER NOT NULL, dst INTEGER DEFAULT 0, homezone INTEGER NOT NULL DEFAULT 0, uniqueid INTEGER NOT NULL DEFAULT 0, wid INTERGER NOT NULL DEFAULT 0)");
            } catch (SQLiteException e) {
                Log.m42e("SQLiteAdapter", "SQLiteException " + e.toString());
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 4) {
                db.delete("CREATE TABLE IF NOT EXISTS dualclock (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, city TEXT NOT NULL, gmt INTEGER NOT NULL, dst INTEGER DEFAULT 0, homezone INTEGER NOT NULL DEFAULT 0, uniqueid INTEGER NOT NULL DEFAULT 0, wid INTERGER NOT NULL DEFAULT 0)", "homezone = 0", null);
            }
        }
    }

    public SQLiteAdapter(Context context, String databaseName, int databaseVersion) {
        this.mDbHelper = new DbHelper(context, databaseName, null, databaseVersion);
    }

    public SQLiteAdapter open() {
        try {
            this.mDb = this.mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            try {
                this.mDb = this.mDbHelper.getReadableDatabase();
            } catch (SQLiteException e2) {
                Log.m42e("SQLiteAdapter", "SQLiteException " + e2.toString());
            }
        }
        return this;
    }

    public Cursor selectAll(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.mDb.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public long insert(String tableName, ContentValues values) {
        return this.mDb.insert(tableName, null, values);
    }

    public int updateByWhere(String tableName, ContentValues values, String whereClause) {
        return this.mDb.update(tableName, values, whereClause, null);
    }

    public int delete(String tableName, String whereClause, String[] whereArgs) {
        return this.mDb.delete(tableName, whereClause, whereArgs);
    }
}
