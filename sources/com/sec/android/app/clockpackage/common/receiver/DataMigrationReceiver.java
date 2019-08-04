package com.sec.android.app.clockpackage.common.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataMigrationReceiver extends BroadcastReceiver {
    private static final Uri CONTENT_URI = Uri.parse("content://com.sec.android.app.clockpackagechina.MigrationProvider/");
    private static final Uri COPY_URI = Uri.parse("content://com.sec.android.app.clockpackagechina.MigrationProvider/copy");
    private static final Uri LIST_URI = Uri.parse("content://com.sec.android.app.clockpackagechina.MigrationProvider/fileList");
    Context mContext;

    public void onReceive(Context context, Intent intent) {
        Log.secV("DataMigrationReceiver", "onReceive");
        if (intent != null && validAction(intent.getAction()) && intent.getAction() != null) {
            Log.secV("DataMigrationReceiver", "onReceive : " + intent.getAction());
            this.mContext = context;
            if (isMigrationProviderEnabled()) {
                if (getMigrationProvider()) {
                    Log.secV("DataMigrationReceiver", "getMigrationProvider() true");
                }
                disableMigrator();
            }
        }
    }

    private boolean validAction(String action) {
        return "android.intent.action.BOOT_COMPLETED".equals(action) || "android.intent.action.PRE_BOOT_COMPLETED".equals(action);
    }

    private boolean getMigrationProvider() {
        String internalDataPath = this.mContext.getDataDir().getPath();
        String originalDataPath = internalDataPath.substring(0, internalDataPath.lastIndexOf(47) + 1) + "com.sec.android.app.clockpackagechina";
        if (!isMigrationProviderEnabled()) {
            return false;
        }
        copyDirectory(appendPath(originalDataPath, "shared_prefs"), appendPath(internalDataPath, "shared_prefs"), true);
        copyDirectory(appendPath(originalDataPath, "databases"), appendPath(internalDataPath, "databases"), true);
        return true;
    }

    private void disableMigrator() {
        Uri componentUri = CONTENT_URI.buildUpon().appendPath("application").build();
        ContentValues values = new ContentValues(1);
        values.put("disabled", Boolean.valueOf(true));
        this.mContext.getContentResolver().update(componentUri, values, null, null);
        Process.killProcess(Process.myPid());
    }

    private boolean copyDirectory(String sourcePath, String targetPath, boolean recursive) {
        if (TextUtils.isEmpty(sourcePath) || TextUtils.isEmpty(targetPath)) {
            Log.m42e("DataMigrationReceiver", "Empty path");
            return false;
        }
        Log.m41d("DataMigrationReceiver", "copyDirectory start " + sourcePath);
        long startT = System.nanoTime();
        createDir(targetPath);
        try {
            return copyDirectoryDetail(sourcePath, targetPath, recursive, startT);
        } catch (Exception e) {
            Log.secE("DataMigrationReceiver", sourcePath + "copyDirectory " + e);
            return true;
        }
    }

    private boolean copyDirectoryDetail(String sourcePath, String targetPath, boolean recursive, long startT) throws Exception {
        Cursor cursor = this.mContext.getContentResolver().query(LIST_URI, null, sourcePath, null, null);
        if (cursor == null) {
            Log.m44w("DataMigrationReceiver", "copyDirectory nothing file " + sourcePath);
            return false;
        } else if (cursor.moveToFirst()) {
            Log.m41d("DataMigrationReceiver", "copyDirectory file count=" + cursor.getCount());
            int copied = 0;
            do {
                String currentFile = cursor.getString(cursor.getColumnIndex("name"));
                if ((cursor.getInt(cursor.getColumnIndex("isDirectory")) == 1) && recursive) {
                    copyDirectory(appendPath(sourcePath, currentFile), appendPath(targetPath, currentFile), recursive);
                } else if (copy(appendPath(sourcePath, currentFile), appendPath(targetPath, currentFile))) {
                    copied++;
                }
            } while (cursor.moveToNext());
            cursor.close();
            Log.m41d("DataMigrationReceiver", "copyDirectory end: " + sourcePath + " files: " + copied + " time:" + ((System.nanoTime() - startT) / 1000000) + "ms");
            return true;
        } else {
            Log.m44w("DataMigrationReceiver", "copyDirectory nothing file " + sourcePath);
            cursor.close();
            return false;
        }
    }

    private String appendPath(String path, String fileName) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(fileName)) {
            Log.m42e("DataMigrationReceiver", "Wrong path/file name");
            return "";
        }
        StringBuilder fullPath = new StringBuilder(path);
        if (fullPath.lastIndexOf("/") != fullPath.length() - 1) {
            fullPath.append("/");
        }
        return fullPath.append(fileName).toString();
    }

    private void createDir(String sPath) {
        if (sPath == null || 1 > sPath.length()) {
            Log.m42e("DataMigrationReceiver", "createDir sPath is null");
            return;
        }
        try {
            File dir = new File(sPath);
            if (dir.exists()) {
                Log.m41d("DataMigrationReceiver", "createDir exists :" + sPath);
            } else if (dir.mkdir()) {
                Log.m41d("DataMigrationReceiver", "createDir make success :" + sPath);
            } else {
                Log.m42e("DataMigrationReceiver", "createDir make fail :" + sPath);
            }
        } catch (Exception e) {
            Log.m42e("DataMigrationReceiver", "createDir " + e);
        }
    }

    private boolean copy(String from, String to) {
        if (new File(to).exists()) {
            Log.m41d("DataMigrationReceiver", "copy " + to + " exists but was overridden");
        }
        try {
            return copyDetail(COPY_URI.buildUpon().appendQueryParameter("path", from).build(), from, to);
        } catch (Exception e) {
            Log.m41d("DataMigrationReceiver", "Exception = " + e.toString());
            return false;
        }
    }

    private boolean copyDetail(Uri fromUri, String from, String to) throws IOException {
        boolean result = false;
        int copied = 0;
        FileInputStream fis = (FileInputStream) this.mContext.getContentResolver().openInputStream(fromUri);
        FileOutputStream fos = new FileOutputStream(to);
        byte[] buf = new byte[8192];
        while (fis != null) {
            int len = fis.read(buf);
            if (len <= 0) {
                break;
            }
            fos.write(buf, 0, len);
            copied += len;
        }
        if (copied > 0) {
            result = true;
        }
        Log.m41d("DataMigrationReceiver", "copy(context, uri, uri) size = " + copied + " from " + from + " to " + to);
        return result;
    }

    private boolean isMigrationProviderEnabled() {
        String MIGRATION_COMPONENT_NAME = "com.sec.android.app.clockpackagechina.MigrationProvider";
        try {
            if (isEnabledPkg(this.mContext, "com.sec.android.app.clockpackagechina")) {
                Log.m41d("DataMigrationReceiver", "Disabling com.sec.android.app.clockpackagechina");
                int enable = this.mContext.getPackageManager().getComponentEnabledSetting(new ComponentName("com.sec.android.app.clockpackagechina", "com.sec.android.app.clockpackagechina.MigrationProvider"));
                if (2 != enable && 3 != enable) {
                    return true;
                }
                Log.m41d("DataMigrationReceiver", "component disabled");
                return false;
            }
            Log.m41d("DataMigrationReceiver", "Disabled com.sec.android.app.clockpackagechina");
            return false;
        } catch (Exception e) {
            Log.m41d("DataMigrationReceiver", "Exception " + e);
            return false;
        }
    }

    public static boolean isEnabledPkg(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            int enable = pm.getApplicationEnabledSetting(packageName);
            if (2 == enable || 3 == enable) {
                Log.secV("DataMigrationReceiver", packageName + " is disabled");
                return false;
            }
            try {
                pm.getPackageInfo(packageName, 1);
                Log.secV("DataMigrationReceiver", packageName + " is enabled");
                return true;
            } catch (NameNotFoundException e) {
                Log.secV("DataMigrationReceiver", packageName + " is disabled");
                return false;
            } catch (RuntimeException e2) {
                Log.secV("DataMigrationReceiver", packageName + " is disabled");
                return false;
            }
        } catch (IllegalArgumentException e3) {
            Log.secV("DataMigrationReceiver", packageName + " is not installed");
            return false;
        }
    }
}
