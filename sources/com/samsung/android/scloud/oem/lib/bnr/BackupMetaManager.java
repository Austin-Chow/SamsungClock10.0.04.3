package com.samsung.android.scloud.oem.lib.bnr;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.android.scloud.oem.lib.LOG;

public class BackupMetaManager {
    private static BackupMetaManager mMetaManager = null;
    private SharedPreferences mBackupMeta = null;

    public static synchronized BackupMetaManager getInstance(Context context) {
        BackupMetaManager backupMetaManager;
        synchronized (BackupMetaManager.class) {
            if (mMetaManager == null) {
                mMetaManager = new BackupMetaManager(context);
            }
            backupMetaManager = mMetaManager;
        }
        return backupMetaManager;
    }

    private BackupMetaManager(Context context) {
        this.mBackupMeta = context.getSharedPreferences("BackupMeta", 0);
    }

    public void setFirstBackup(String sourceKey, boolean isFirstBackup) {
        LOG.m5f("BackupMetaManager_" + sourceKey, "setFirstBackup(): " + isFirstBackup);
        this.mBackupMeta.edit().putBoolean(new StringBuilder(String.valueOf(sourceKey)).append("_").append("FIRST_BACKUP").toString(), isFirstBackup).commit();
    }

    public boolean isFirstBackup(String sourceKey) {
        boolean result = this.mBackupMeta.getBoolean(new StringBuilder(String.valueOf(sourceKey)).append("_").append("FIRST_BACKUP").toString(), true);
        LOG.m6i("BackupMetaManager_" + sourceKey, "setFirstBackup(): " + result);
        return result;
    }

    public void setLastBackupTime(String sourceKey, long time) {
        LOG.m5f("BackupMetaManager_" + sourceKey, "setLastBackupTime(): " + time);
        this.mBackupMeta.edit().putLong(new StringBuilder(String.valueOf(sourceKey)).append("_").append("LAST_BACKUP_TIME").toString(), time).commit();
    }

    public boolean clear(String sourceKey) {
        LOG.m5f("BackupMetaManager_" + sourceKey, "BackupMetaManager cleared!!!");
        return this.mBackupMeta.edit().clear().commit();
    }
}
