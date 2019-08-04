package com.sec.android.app.clockpackage.backuprestore.scloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import com.samsung.android.scloud.oem.lib.bnr.BNRFile;
import com.samsung.android.scloud.oem.lib.bnr.BNRItem;
import com.samsung.android.scloud.oem.lib.bnr.ISCloudBNRClient;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.backuprestore.util.BackupRestoreUtils;
import com.sec.android.app.clockpackage.backuprestore.util.JSONParser;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class WorldclockSCloudBackupRestore implements ISCloudBNRClient {
    private String TAG = "BNR_CLOCK_WC_WorldclockSCloudBackupRestore";

    public boolean isSupportBackup(Context context) {
        return true;
    }

    public boolean isEnableBackup(Context context) {
        return true;
    }

    public String getLabel(Context context) {
        return context.getResources().getString(R.string.worldclock);
    }

    public String getDescription(Context context) {
        return context.getResources().getString(R.string.worldclock);
    }

    public boolean backupPrepare(Context context) {
        return true;
    }

    public HashMap<String, Long> getItemKey(Context context, int start, int maxCount) {
        Log.secD(this.TAG, "getItemKey() called~!!");
        HashMap<String, Long> result = new HashMap();
        String sortOrder = "_id";
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath("worldclock.db").getPath(), null, 0);
            Cursor cursor = db.query("worldclock", new String[]{"_id"}, null, null, null, null, sortOrder + " ASC" + " LIMIT " + maxCount + " OFFSET " + start);
            if (cursor == null || cursor.getCount() <= 0) {
                Log.secD(this.TAG, "getItemKey() : item count is 0 or null");
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
                return null;
            }
            while (cursor.moveToNext()) {
                result.put(Integer.toString(cursor.getInt(0)), Long.valueOf(Calendar.getInstance().getTimeInMillis()));
            }
            Log.secD(this.TAG, "getItemKey() : item count : " + result.size());
            db.close();
            cursor.close();
            return result;
        } catch (SQLiteException e) {
            Log.secE(this.TAG, "Exception : " + e.toString());
            Log.secE(this.TAG, "getItemKey() : SQLiteException : Unable to save alarm");
            return null;
        }
    }

    public ArrayList<BNRFile> getFileMeta(Context context, int start, int maxCount) {
        return null;
    }

    public ArrayList<BNRItem> backupItem(Context context, ArrayList<String> localIds) {
        Log.secD(this.TAG, "backupItem() called~!!,localIds :  " + localIds);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath("worldclock.db").getPath(), null, 0);
        Cursor cursor = db.query("worldclock", null, "_id IN " + ClockUtils.getWhereKey(ClockUtils.toStringArray(localIds)), null, null, null, null);
        ArrayList<BNRItem> result = new ArrayList();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                try {
                    JSONObject data = JSONParser.toJSON(cursor);
                    result.add(new BNRItem(cursor.getString(cursor.getColumnIndex("_id")), data.toString(), Calendar.getInstance().getTimeInMillis()));
                } catch (JSONException e) {
                    Log.secE(this.TAG, "Exception : " + e.toString());
                }
            }
            cursor.close();
            db.close();
            return result;
        } else if (cursor == null || cursor.getCount() != 0) {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return null;
        } else {
            result.add(null);
            cursor.close();
            db.close();
            return result;
        }
    }

    public String getFilePath(Context context, String path, boolean external, String operation) {
        return null;
    }

    public boolean backupComplete(Context context, boolean isSuccess) {
        return isSuccess;
    }

    public boolean restorePrepare(Context context, Bundle extras) {
        return true;
    }

    public boolean restoreItem(Context context, ArrayList<BNRItem> items, ArrayList<String> insertedRowIds) {
        SQLiteDatabase db;
        Log.secD(this.TAG, "restoreItem() called~!!");
        long rowId = -1;
        int insertedCount = 0;
        String dbName = context.getDatabasePath("worldclock.db").getPath();
        File dbFile = context.getDatabasePath(dbName);
        if (!CityManager.sIsCityManagerLoad) {
            CityManager.initCity(context);
        }
        if (dbFile.exists()) {
            db = SQLiteDatabase.openDatabase(dbName, null, 0);
        } else {
            db = context.openOrCreateDatabase(dbName, 0, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS worldclock (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, city TEXT NOT NULL, gmt INTEGER NOT NULL, dst INTEGER DEFAULT 0, homezone INTEGER NOT NULL DEFAULT 0, pointX INTEGER NOT NULL DEFAULT 0, pointY INTEGER NOT NULL DEFAULT 0)");
        }
        ContentValues cv = new ContentValues();
        ContentValues insertCV = new ContentValues();
        if (items == null || items.size() <= 0) {
            db.close();
            return true;
        }
        int index = 0;
        while (index < items.size() && insertedCount < 20) {
            cv.clear();
            insertCV.clear();
            BNRItem item = (BNRItem) items.get(index);
            if (item == null) {
                Log.secD(this.TAG, "NO ITEM");
                db.close();
                return false;
            }
            try {
                cv = JSONParser.fromJSON(new JSONObject(item.getData()));
                Integer homeZone = cv.getAsInteger("homezone");
                Log.secD(this.TAG, "restoreItem() homeZone :  " + homeZone);
                if (homeZone != null) {
                    try {
                        ContentValues contentValues = WorldclockDBManager.getContentValues(homeZone.intValue());
                        if (contentValues != null) {
                            rowId = db.insert("worldclock", null, contentValues);
                        } else {
                            Log.secD(this.TAG, "unsupported city. skip to add");
                            index++;
                        }
                    } catch (SQLiteException e) {
                        Log.secE(this.TAG, "Exception : " + e.toString());
                        Log.secE(this.TAG, "restoreItem() : SQLiteException : Unable to save alarm");
                    }
                }
                if (rowId == -1) {
                    Log.secD(this.TAG, "Item is not inserted");
                    db.close();
                    return false;
                }
                insertedCount++;
                insertedRowIds.add(Long.toString(rowId));
                cv.clear();
                insertCV.clear();
                index++;
            } catch (JSONException e2) {
                Log.secE(this.TAG, "JSONException" + e2);
                db.close();
                return false;
            }
        }
        db.close();
        return true;
    }

    public boolean restoreComplete(Context context, String[] insertedIds, boolean isSuccess) {
        Log.secD(this.TAG, "restoreComplete() called~!! / isSuccess = " + isSuccess);
        boolean result = true;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath("worldclock.db").getPath(), null, 0);
        String keys = ClockUtils.getWhereKey(insertedIds);
        Log.secD(this.TAG, keys);
        int size = 0;
        if (insertedIds != null && insertedIds.length > 0) {
            size = insertedIds.length;
        }
        if (isSuccess) {
            if (size > 0) {
                int count = 0;
                String firstKey = insertedIds[0];
                Cursor cursor = db.query("worldclock", null, "_id < " + firstKey, null, null, null, null, null);
                if (cursor != null) {
                    count = cursor.getCount();
                    cursor.close();
                    Log.secD(this.TAG, "old items count : " + count);
                }
                int deletedCount = db.delete("worldclock", "_id < " + firstKey, null);
                if (deletedCount != count) {
                    result = false;
                }
                Log.secD(this.TAG, "Delete old items. deletedCount : " + deletedCount + " result : " + result);
            }
            if (insertedIds != null && size == 0) {
                db.execSQL("delete from worldclock");
            }
            CityManager.setIsCityRestored(true);
            BackupRestoreUtils.sendWorldclockChangedIntent(context);
        } else {
            if (size > 0) {
                db.delete("worldclock", "_id IN " + keys, null);
            }
            result = false;
        }
        db.close();
        return result;
    }
}
