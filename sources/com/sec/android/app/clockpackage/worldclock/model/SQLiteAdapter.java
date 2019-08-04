package com.sec.android.app.clockpackage.worldclock.model;

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
        private Context mContext;

        private DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
            this.mContext = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS worldclock (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, city TEXT NOT NULL, gmt INTEGER NOT NULL, dst INTEGER DEFAULT 0, homezone INTEGER NOT NULL DEFAULT 0, pointX INTEGER NOT NULL DEFAULT 0, pointY INTEGER NOT NULL DEFAULT 0)");
            } catch (SQLiteException e) {
                Log.m42e("SQLiteAdapter", "Exception : " + e.toString());
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    Cursor cursor = db.rawQuery("select * from worldclock where " + Worldclock.WC_COLUMNS[4] + " >= 293 ;", null);
                    if (cursor != null) {
                        CityManager.initCity(this.mContext);
                        while (cursor.moveToNext()) {
                            String mOriginalCityName = cursor.getString(1);
                            if (!mOriginalCityName.equals(CityManager.findCityCountryNameByUniqueId(Integer.valueOf(cursor.getInt(4))))) {
                                City mUpdatedCity = CityManager.findCityObjectByName(mOriginalCityName);
                                if (mUpdatedCity != null) {
                                    db.execSQL("update worldclock set " + Worldclock.WC_COLUMNS[1] + " = '" + (mUpdatedCity.getName() + " / " + mUpdatedCity.getCountry()) + "' " + " , " + Worldclock.WC_COLUMNS[4] + " = " + mUpdatedCity.getUniqueId() + " where " + Worldclock.WC_COLUMNS[0] + " = " + cursor.getString(0) + ';');
                                }
                            }
                        }
                        cursor.close();
                        return;
                    }
                    return;
                case 2:
                    db.execSQL("UPDATE worldclock SET " + Worldclock.WC_COLUMNS[2] + " = " + "'Europe/Rome'" + " WHERE " + Worldclock.WC_COLUMNS[1] + " = " + "'Oslo / Norway'" + ';');
                    return;
                default:
                    Log.secE("SQLiteAdapter", "oldVersion is wrong : " + oldVersion);
                    return;
            }
        }
    }

    public SQLiteAdapter(Context context, String databaseName) {
        this.mDbHelper = new DbHelper(context, databaseName, null, 3);
    }

    public SQLiteAdapter open() {
        try {
            this.mDb = this.mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            try {
                this.mDb = this.mDbHelper.getReadableDatabase();
            } catch (SQLiteException e2) {
                Log.m42e("SQLiteAdapter", "Exception : " + e2.toString());
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
