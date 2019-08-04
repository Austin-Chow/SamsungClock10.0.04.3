package com.sec.android.app.clockpackage.alarm.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CelebVoiceProvider extends ContentProvider {
    private static final Uri CELEB_VOICE_URI = Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*");
    private static final HashMap<String, String> mCelebVoiceDisplayedItemsMap = new HashMap();
    private static final HashMap<String, String> mCelebVoiceItemsMap = new HashMap();
    private static final HashMap<String, String> mCelebVoicePackagesFileMap = new HashMap();
    public static final HashMap<String, String> mCelebVoicePackagesMap = new HashMap();
    private static final HashMap<String, String> mCelebVoicePackagesNoBlobMap = new HashMap();
    private static final UriMatcher uriMatcher = new UriMatcher(-1);
    private CelebVoicePackagesDbHelper mCelebVoicePackagesDbHelper;

    static {
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "clockvoice/*/*", 0);
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "clockvoice/*/*/*", 1);
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "noBlob/clockvoice/*", 2);
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "noBlob/clockvoice/*/*", 3);
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "preview/clockvoice/*", 4);
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "validItem/clockvoice/*/*", 5);
        uriMatcher.addURI("com.sec.android.app.clockpackage.celebvoice", "validItem/clockvoice/*/*/*", 6);
        mCelebVoicePackagesMap.put("_id", "_id");
        mCelebVoicePackagesMap.put("PKG_NAME", "PKG_NAME");
        mCelebVoicePackagesMap.put("TYPE", "TYPE");
        mCelebVoicePackagesMap.put("VALID_DATE", "VALID_DATE");
        mCelebVoicePackagesMap.put("VALID_DATE_LONG", "VALID_DATE_LONG");
        mCelebVoicePackagesMap.put("COST", "COST");
        mCelebVoicePackagesMap.put("FILE", "FILE");
        mCelebVoicePackagesMap.put("CONTENT_NAME", "CONTENT_NAME");
        mCelebVoicePackagesMap.put("TITLE", "TITLE");
        mCelebVoicePackagesMap.put("VERSION_CODE", "VERSION_CODE");
        mCelebVoicePackagesMap.put("VERSION_NAME", "VERSION_NAME");
        mCelebVoicePackagesMap.put("BITRATE", "BITRATE");
        mCelebVoicePackagesMap.put("STEREO", "STEREO");
        mCelebVoicePackagesMap.put("KHZ", "KHZ");
        mCelebVoicePackagesMap.put("REP_STATIC", "REP_STATIC");
        mCelebVoicePackagesMap.put("REP_DYNAMIC", "REP_DYNAMIC");
        mCelebVoicePackagesMap.put("PREVIEW_FILE", "PREVIEW_FILE");
        mCelebVoicePackagesMap.put("EXTRA_STRING_1", "EXTRA_STRING_1");
        mCelebVoicePackagesMap.put("EXTRA_STRING_2", "EXTRA_STRING_2");
        mCelebVoicePackagesMap.put("EXTRA_STRING_3", "EXTRA_STRING_3");
        mCelebVoicePackagesMap.put("EXTRA_LONG_1", "EXTRA_LONG_1");
        mCelebVoicePackagesMap.put("EXTRA_LONG_2", "EXTRA_LONG_2");
        mCelebVoicePackagesMap.put("EXTRA_INT_1", "EXTRA_INT_1");
        mCelebVoicePackagesMap.put("EXTRA_INT_2", "EXTRA_INT_2");
        mCelebVoicePackagesMap.put("EXTRA_INT_3", "EXTRA_INT_3");
        mCelebVoicePackagesNoBlobMap.put("_id", "_id");
        mCelebVoicePackagesNoBlobMap.put("PKG_NAME", "PKG_NAME");
        mCelebVoicePackagesNoBlobMap.put("TYPE", "TYPE");
        mCelebVoicePackagesNoBlobMap.put("VALID_DATE", "VALID_DATE");
        mCelebVoicePackagesNoBlobMap.put("VALID_DATE_LONG", "VALID_DATE_LONG");
        mCelebVoicePackagesNoBlobMap.put("COST", "COST");
        mCelebVoicePackagesNoBlobMap.put("FILE", "FILE");
        mCelebVoicePackagesNoBlobMap.put("CONTENT_NAME", "CONTENT_NAME");
        mCelebVoicePackagesNoBlobMap.put("VERSION_CODE", "VERSION_CODE");
        mCelebVoicePackagesNoBlobMap.put("VERSION_NAME", "VERSION_NAME");
        mCelebVoicePackagesFileMap.put("_id", "_id");
        mCelebVoicePackagesFileMap.put("FILE", "FILE");
        mCelebVoiceItemsMap.put("_id", "_id");
        mCelebVoiceItemsMap.put("FILE_NAME", "FILE_NAME");
        mCelebVoiceItemsMap.put("FILE_SIZE", "FILE_SIZE");
        mCelebVoiceItemsMap.put("EXTRA_1", "EXTRA_1");
        mCelebVoiceItemsMap.put("EXTRA_2", "EXTRA_2");
        mCelebVoiceItemsMap.put("EXTRA_3", "EXTRA_3");
        mCelebVoiceDisplayedItemsMap.put("_id", "_id");
        mCelebVoiceDisplayedItemsMap.put("PKG_NAME", "PKG_NAME");
        mCelebVoiceDisplayedItemsMap.put("TYPE", "TYPE");
        mCelebVoiceDisplayedItemsMap.put("VALID_DATE", "VALID_DATE");
        mCelebVoiceDisplayedItemsMap.put("VALID_DATE_LONG", "VALID_DATE_LONG");
        mCelebVoiceDisplayedItemsMap.put("COST", "COST");
        mCelebVoiceDisplayedItemsMap.put("FILE", "FILE");
        mCelebVoiceDisplayedItemsMap.put("CONTENT_NAME", "CONTENT_NAME");
        mCelebVoiceDisplayedItemsMap.put("REP_STATIC", "REP_STATIC");
        mCelebVoiceDisplayedItemsMap.put("VERSION_NAME", "VERSION_NAME");
        mCelebVoiceDisplayedItemsMap.put("PREVIEW_FILE", "PREVIEW_FILE");
    }

    public boolean onCreate() {
        createDir(AlarmCvConstants.getUIDRootPath());
        this.mCelebVoicePackagesDbHelper = CelebVoicePackagesDbHelper.getInstance(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<String> segment = uri.getPathSegments();
        String content = (String) segment.get(0);
        String callingPkgName = getCallingPackage();
        if (isAuthorizedCelebVoiceApp(callingPkgName)) {
            SQLiteDatabase db = null;
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String[] localProjection = null;
            String localSelect = selection;
            String[] localSelectArgs = selectionArgs;
            int match = uriMatcher.match(uri);
            if (match <= -1) {
                return null;
            }
            switch (match) {
                case 0:
                    String allByType = (String) segment.get(1);
                    db = this.mCelebVoicePackagesDbHelper.getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_PACKAGES");
                    queryBuilder.setProjectionMap(mCelebVoicePackagesMap);
                    if (selection == null && selectionArgs == null) {
                        localSelect = "TYPE=?";
                        localSelectArgs = new String[]{allByType};
                        break;
                    }
                case 1:
                    String type = (String) segment.get(1);
                    String pkgName = (String) segment.get(2);
                    Log.secD("CelebVoiceProvider", "type= " + type + " pkgName= " + pkgName);
                    db = CelebVoiceItemsDbHelper.getInstance(getContext(), pkgName, type).getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_ITEMS");
                    queryBuilder.setProjectionMap(mCelebVoiceItemsMap);
                    localSelect = selection;
                    localSelectArgs = selectionArgs;
                    localProjection = new String[]{"_id", "FILE_NAME", "FILE_SIZE", "EXTRA_1", "EXTRA_2", "EXTRA_3"};
                    break;
                case 2:
                    db = this.mCelebVoicePackagesDbHelper.getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_PACKAGES");
                    queryBuilder.setProjectionMap(mCelebVoicePackagesNoBlobMap);
                    localProjection = new String[]{"_id", "PKG_NAME", "TYPE", "VALID_DATE", "VALID_DATE_LONG", "COST", "FILE", "CONTENT_NAME", "VERSION_CODE", "VERSION_NAME"};
                    break;
                case 3:
                    db = this.mCelebVoicePackagesDbHelper.getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_PACKAGES");
                    queryBuilder.setProjectionMap(mCelebVoicePackagesNoBlobMap);
                    localSelect = "PKG_NAME=?";
                    localSelectArgs = new String[]{(String) segment.get(2)};
                    localProjection = new String[]{"_id", "PKG_NAME", "TYPE", "VALID_DATE", "VALID_DATE_LONG", "COST", "FILE", "CONTENT_NAME", "VERSION_CODE", "VERSION_NAME"};
                    break;
                case 4:
                    db = this.mCelebVoicePackagesDbHelper.getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_PACKAGES");
                    queryBuilder.setProjectionMap(mCelebVoiceDisplayedItemsMap);
                    localProjection = new String[]{"_id", "PKG_NAME", "TYPE", "VALID_DATE", "VALID_DATE_LONG", "COST", "FILE", "CONTENT_NAME", "REP_STATIC", "VERSION_NAME", "PREVIEW_FILE"};
                    break;
                case 5:
                    db = this.mCelebVoicePackagesDbHelper.getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_PACKAGES");
                    queryBuilder.setProjectionMap(mCelebVoicePackagesNoBlobMap);
                    String validDate = (String) segment.get(2);
                    Log.secD("CelebVoiceProvider", "validDate: " + validDate + " validDateLong: " + calculateExpiredDate(validDate));
                    localSelect = "VALID_DATE_LONG>=?";
                    localSelectArgs = new String[]{Long.toString(validDateLong)};
                    localProjection = new String[]{"_id", "PKG_NAME", "TYPE", "VALID_DATE", "VALID_DATE_LONG", "COST", "FILE", "CONTENT_NAME", "VERSION_CODE", "VERSION_NAME"};
                    break;
                case 6:
                    db = this.mCelebVoicePackagesDbHelper.getReadableDatabase();
                    queryBuilder.setTables("CELEBVOICE_PACKAGES");
                    queryBuilder.setProjectionMap(mCelebVoicePackagesNoBlobMap);
                    String validDatePkgName = (String) segment.get(2);
                    Log.secD("CelebVoiceProvider", "validOneDate: " + validDatePkgName + " validDateLongForOne: " + calculateExpiredDate(validDatePkgName) + " pkgOneName: " + ((String) segment.get(3)));
                    localSelect = "PKG_NAME=? AND VALID_DATE_LONG>=?";
                    localSelectArgs = new String[]{validPkgName, Long.toString(validDatePkgNameLong)};
                    localProjection = new String[]{"_id", "PKG_NAME", "TYPE", "VALID_DATE", "VALID_DATE_LONG", "COST", "FILE", "CONTENT_NAME", "VERSION_CODE", "VERSION_NAME"};
                    break;
            }
            return queryBuilder.query(db, localProjection, localSelect, localSelectArgs, null, null, sortOrder);
        }
        Log.secD("CelebVoiceProvider", "Unauthorized calling package : " + callingPkgName);
        return null;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        List<String> segment = uri.getPathSegments();
        String content = (String) segment.get(0);
        String type = (String) segment.get(1);
        String packageName = (String) segment.get(2);
        String size = (String) segment.get(3);
        String fileName = uri.getEncodedFragment();
        String callingPkgName = getCallingPackage();
        if (!isAuthorizedCelebVoiceApp(callingPkgName)) {
            Log.secD("CelebVoiceProvider", "Unauthorized calling package : " + callingPkgName);
            return null;
        } else if (!"clockvoice".equals(content)) {
            return null;
        } else {
            String path = AlarmCvConstants.getUIDRootPath() + type + "/" + packageName + "/assets" + "/" + fileName;
            Log.secD("CelebVoiceProvider", "PFD: " + path);
            return ParcelFileDescriptor.open(new File(path), 268435456);
        }
    }

    private boolean isAuthorizedCelebVoiceApp(String callingPkgName) {
        return AlarmCvConstants.AUTHORIZED_PACKAGES.contains(callingPkgName);
    }

    private int createDir(String path) {
        File dir = new File(path);
        if (dir.exists() || !dir.mkdirs()) {
            return 0;
        }
        try {
            if (((Integer) Class.forName("android.os.FileUtils").getMethod("setPermissions", new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE}).invoke(null, new Object[]{dir.getAbsolutePath(), Integer.valueOf(485), Integer.valueOf(-1), Integer.valueOf(-1)})).intValue() != 0) {
                return -4;
            }
            return 0;
        } catch (SecurityException e) {
            Log.secD("CelebVoiceProvider", "Security error while creating directory");
            return -24;
        } catch (Exception e2) {
            Log.secD("CelebVoiceProvider", "createDir : permission error");
            return -4;
        }
    }

    public static String getCelebrityVoiceType(Context context, String pkg_name) {
        String celebrityVoiceType = "";
        Cursor cursor = context.getContentResolver().query(CELEB_VOICE_URI, null, "PKG_NAME='" + pkg_name + '\'', null, null);
        if (cursor != null) {
            int count = cursor.getCount();
            Log.secD("CelebVoiceProvider", "getCelebrityVoiceType count = " + count);
            if (count != 0) {
                cursor.moveToFirst();
                celebrityVoiceType = cursor.getString(cursor.getColumnIndex("TYPE"));
            }
            cursor.close();
        }
        Log.secD("CelebVoiceProvider", "getCelebrityVoiceType pkg_name = " + pkg_name + ", celebrityVoiceType = " + celebrityVoiceType);
        return celebrityVoiceType;
    }

    private long calculateExpiredDate(String date) {
        long expiredDateLong = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            expiredDateLong = simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            Log.secE("CelebVoiceProvider", "happen Exception " + e);
        }
        return expiredDateLong;
    }

    public static int getCelebGender(Context context, String pkgName) {
        Cursor cursor;
        Throwable th;
        Throwable th2;
        Throwable th3;
        int celebGender = 1;
        try {
            cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/clockvoice/ClockVoice/" + pkgName + "/*"), null, "FILE_NAME LIKE '%_no_fri_%'", null, null);
            th = null;
            if (cursor != null) {
                do {
                    try {
                        if (!cursor.moveToNext()) {
                            break;
                        }
                    } catch (Throwable th4) {
                        th2 = th4;
                        th3 = th;
                    }
                } while (!cursor.getString(1).contains("_no_fri_m"));
                celebGender = 2;
                cursor.close();
            }
            if (cursor != null) {
                if (null != null) {
                    try {
                        cursor.close();
                    } catch (Throwable th22) {
                        th.addSuppressed(th22);
                    }
                } else {
                    cursor.close();
                }
            }
        } catch (NullPointerException e) {
            Log.secD("CelebVoiceProvider", "getCelebGender query error");
            Log.secD("CelebVoiceProvider", "getCelebGender celebGender = " + celebGender);
            return celebGender;
        } catch (SQLException e2) {
            Log.secD("CelebVoiceProvider", "getCelebGender query error");
            Log.secD("CelebVoiceProvider", "getCelebGender celebGender = " + celebGender);
            return celebGender;
        }
        Log.secD("CelebVoiceProvider", "getCelebGender celebGender = " + celebGender);
        return celebGender;
        throw th22;
        if (cursor != null) {
            if (th3 != null) {
                try {
                    cursor.close();
                } catch (Throwable th5) {
                    th3.addSuppressed(th5);
                }
            } else {
                cursor.close();
            }
        }
        throw th22;
    }

    public static int getCountOfSelection(Context context, String pkgName, String selection) {
        Cursor cursor;
        Throwable th;
        Throwable th2;
        Throwable th3;
        int count = 0;
        try {
            cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/clockvoice/ClockVoice/" + pkgName + "/*"), null, selection, null, null);
            th = null;
            if (cursor != null) {
                try {
                    count = cursor.getCount();
                    cursor.close();
                } catch (Throwable th4) {
                    th2 = th4;
                    th3 = th;
                }
            }
            if (cursor != null) {
                if (null != null) {
                    try {
                        cursor.close();
                    } catch (Throwable th22) {
                        th.addSuppressed(th22);
                    }
                } else {
                    cursor.close();
                }
            }
        } catch (NullPointerException e) {
            Log.secE("CelebVoiceProvider", "getCountOfSelection error");
            Log.m41d("CelebVoiceProvider", "getCountOfSelection count = " + count + "selection = " + selection);
            return count;
        } catch (SQLException e2) {
            Log.secE("CelebVoiceProvider", "getCountOfSelection error");
            Log.m41d("CelebVoiceProvider", "getCountOfSelection count = " + count + "selection = " + selection);
            return count;
        }
        Log.m41d("CelebVoiceProvider", "getCountOfSelection count = " + count + "selection = " + selection);
        return count;
        if (cursor != null) {
            if (th3 != null) {
                try {
                    cursor.close();
                } catch (Throwable th5) {
                    th3.addSuppressed(th5);
                }
            } else {
                cursor.close();
            }
        }
        throw th22;
        throw th22;
    }
}
