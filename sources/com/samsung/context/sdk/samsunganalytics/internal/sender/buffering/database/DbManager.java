package com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.context.sdk.samsunganalytics.DBOpenHelper;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DbManager {
    private DBOpenHelper dbOpenHelper;
    private Queue<SimpleLog> list;

    public DbManager(Context context) {
        this(new DefaultDBOpenHelper(context));
    }

    public DbManager(DBOpenHelper dbOpenHelper) {
        this.list = new LinkedBlockingQueue();
        if (dbOpenHelper != null) {
            this.dbOpenHelper = dbOpenHelper;
            dbOpenHelper.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS logs_v2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp INTEGER, logtype TEXT, data TEXT)");
        }
        delete(5);
    }

    private Queue<SimpleLog> select(String rawQuery) {
        this.list.clear();
        Cursor cursor = this.dbOpenHelper.getReadableDatabase().rawQuery(rawQuery, null);
        while (cursor.moveToNext()) {
            SimpleLog log = new SimpleLog();
            log.setId(cursor.getString(cursor.getColumnIndex("_id")));
            log.setData(cursor.getString(cursor.getColumnIndex("data")));
            log.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
            log.setType(cursor.getString(cursor.getColumnIndex("logtype")).equals(LogType.DEVICE.getAbbrev()) ? LogType.DEVICE : LogType.UIX);
            this.list.add(log);
        }
        cursor.close();
        return this.list;
    }

    public Queue<SimpleLog> selectSome(int limit) {
        return select("select * from logs_v2 LIMIT " + limit);
    }

    public Queue<SimpleLog> selectAll() {
        return select("select * from logs_v2");
    }

    public void insert(SimpleLog simpleLog) {
        SQLiteDatabase database = this.dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp", Long.valueOf(simpleLog.getTimestamp()));
        values.put("data", simpleLog.getData());
        values.put("logtype", simpleLog.getType().getAbbrev());
        database.insert("logs_v2", null, values);
    }

    public void delete(long baseTimestamp) {
        this.dbOpenHelper.getWritableDatabase().delete("logs_v2", "timestamp <= " + baseTimestamp, null);
    }

    public void delete(List<String> ids) {
        SQLiteDatabase database = this.dbOpenHelper.getWritableDatabase();
        database.beginTransaction();
        int start = 0;
        try {
            int size = ids.size();
            while (size > 0) {
                int subSize;
                if (size < 900) {
                    subSize = size;
                } else {
                    subSize = 900;
                }
                List<String> subList = ids.subList(start, start + subSize);
                String selection = "_id IN(" + new String(new char[(subList.size() - 1)]).replaceAll("\u0000", "?,");
                database.delete("logs_v2", selection + "?)", (String[]) subList.toArray(new String[0]));
                start += subSize;
                size -= subSize;
            }
            ids.clear();
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }
}
