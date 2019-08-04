package com.sec.android.app.clockpackage.alarm.model;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;

public class CelebVoicePackagesDbHelper extends SQLiteOpenHelper {
    private static CelebVoicePackagesDbHelper mCelebVoicePkgDbHelper = null;

    private static class CelebVoicePackagesDbErrorHandler implements DatabaseErrorHandler {
        private CelebVoicePackagesDbErrorHandler() {
        }

        public void onCorruption(SQLiteDatabase dbObj) {
            boolean databaseOk = dbObj.isDatabaseIntegrityOk();
            try {
                Log.secD("CelebVoicePackagesDbHelper", "Packages DB error handle. Close DB.");
                dbObj.close();
            } catch (SQLiteException e) {
            }
            if (!databaseOk) {
                Log.secD("CelebVoicePackagesDbHelper", "Packages DB is corrupt. Delete DB.");
                SQLiteDatabase.deleteDatabase(new File(AlarmCvConstants.getUIDRootPath() + "celebvoicepkgs.db"));
            }
        }
    }

    public static synchronized CelebVoicePackagesDbHelper getInstance(Context context) {
        CelebVoicePackagesDbHelper celebVoicePackagesDbHelper;
        synchronized (CelebVoicePackagesDbHelper.class) {
            if (mCelebVoicePkgDbHelper == null) {
                mCelebVoicePkgDbHelper = new CelebVoicePackagesDbHelper(context, "celebvoicepkgs.db", null, 1, new CelebVoicePackagesDbErrorHandler());
            }
            celebVoicePackagesDbHelper = mCelebVoicePkgDbHelper;
        }
        return celebVoicePackagesDbHelper;
    }

    private CelebVoicePackagesDbHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS CELEBVOICE_PACKAGES (_id INTEGER PRIMARY KEY AUTOINCREMENT, PKG_NAME TEXT NOT NULL, TYPE TEXT NOT NULL, VALID_DATE TEXT NOT NULL, VALID_DATE_LONG LONG NOT NULL, COST TEXT NOT NULL, FILE TEXT, CONTENT_NAME TEXT NOT NULL, TITLE TEXT, VERSION_CODE TEXT NOT NULL, VERSION_NAME TEXT NOT NULL, BITRATE INT NOT NULL, STEREO TEXT NOT NULL, KHZ INT NOT NULL, REP_STATIC BLOB, REP_DYNAMIC BLOB, PREVIEW_FILE TEXT, EXTRA_STRING_1 TEXT, EXTRA_STRING_2 TEXT, EXTRA_STRING_3 TEXT, EXTRA_LONG_1 LONG, EXTRA_LONG_2 LONG, EXTRA_INT_1 INT, EXTRA_INT_2 INT, EXTRA_INT_3 INT  );");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CELEBVOICE_PACKAGES;");
        onCreate(db);
    }
}
