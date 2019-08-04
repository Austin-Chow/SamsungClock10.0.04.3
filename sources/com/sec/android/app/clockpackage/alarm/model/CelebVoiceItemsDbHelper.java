package com.sec.android.app.clockpackage.alarm.model;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;

public class CelebVoiceItemsDbHelper extends SQLiteOpenHelper {
    private static CelebVoiceItemsDbHelper mCelebVoiceItemsDbHelper = null;
    private static String mPackageName = null;
    private static String mPackageType = null;

    private static class CelebVoiceItemsDbErrorHandler implements DatabaseErrorHandler {
        private CelebVoiceItemsDbErrorHandler() {
        }

        public void onCorruption(SQLiteDatabase dbObj) {
            boolean databaseOk = dbObj.isDatabaseIntegrityOk();
            try {
                Log.secD("CelebVoiceItemsDbHelper", "Items DB error handle. Close DB.");
                dbObj.close();
            } catch (SQLiteException e) {
            }
            if (!databaseOk) {
                Log.secD("CelebVoiceItemsDbHelper", "Items DB is corrupt. Delete DB.");
                SQLiteDatabase.deleteDatabase(new File(AlarmCvConstants.getUIDRootPath() + CelebVoiceItemsDbHelper.mPackageType + "/" + CelebVoiceItemsDbHelper.mPackageName + "/" + "celebvoiceItems.db"));
            }
        }
    }

    public static synchronized CelebVoiceItemsDbHelper getInstance(Context context, String packageName, String type) {
        CelebVoiceItemsDbHelper celebVoiceItemsDbHelper;
        synchronized (CelebVoiceItemsDbHelper.class) {
            if (mCelebVoiceItemsDbHelper != null && mPackageName.contentEquals(packageName) && mPackageType.contentEquals(type)) {
                celebVoiceItemsDbHelper = mCelebVoiceItemsDbHelper;
            } else {
                if (mCelebVoiceItemsDbHelper != null) {
                    mCelebVoiceItemsDbHelper.close();
                    mCelebVoiceItemsDbHelper = null;
                }
                mPackageName = packageName;
                mPackageType = type;
                mCelebVoiceItemsDbHelper = new CelebVoiceItemsDbHelper(context, AlarmCvConstants.getUIDRootPath() + type + "/" + packageName + "/" + "celebvoiceItems.db", null, 1, new CelebVoiceItemsDbErrorHandler());
                celebVoiceItemsDbHelper = mCelebVoiceItemsDbHelper;
            }
        }
        return celebVoiceItemsDbHelper;
    }

    private CelebVoiceItemsDbHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS CELEBVOICE_ITEMS (_id INTEGER PRIMARY KEY AUTOINCREMENT, FILE_NAME TEXT, FILE_SIZE DOUBLE, EXTRA_1 TEXT, EXTRA_2 TEXT, EXTRA_3 TEXT  );");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
