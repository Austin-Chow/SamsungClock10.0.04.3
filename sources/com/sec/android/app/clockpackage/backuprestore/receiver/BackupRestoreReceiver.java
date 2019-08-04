package com.sec.android.app.clockpackage.backuprestore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;

public abstract class BackupRestoreReceiver extends BroadcastReceiver {
    private Thread mBackupThread;
    protected Context mContext;

    protected abstract int backupData(String str, String str2, int i);

    protected abstract void deleteXmlData(String str);

    protected abstract int getFileLength(String str);

    protected abstract String getResponseBackup();

    protected abstract String getResponseRestore();

    protected abstract boolean isBackup(String str);

    protected abstract boolean isEmpty();

    protected abstract boolean isMaxCount();

    protected abstract boolean isRestore(String str);

    protected abstract int restoreData(String str, String str2, int i);

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && context != null) {
            this.mContext = context;
            Log.secD("BNR_CLOCK_BackupRestoreReceiver", "onReceive() : action = " + action.substring(action.lastIndexOf(46)));
            String filePath = intent.getStringExtra("SAVE_PATH");
            String source = intent.getStringExtra("SOURCE");
            final String saveKey = intent.getStringExtra("SESSION_KEY");
            String sessionTime = intent.getStringExtra("EXPORT_SESSION_TIME");
            final int securityLevel = intent.getIntExtra("SECURITY_LEVEL", 0);
            int extraAction = intent.getIntExtra("ACTION", 0);
            Log.secD("BNR_CLOCK_BackupRestoreReceiver", "SAVE_PATH=" + filePath + "ACTION=" + extraAction + "extraAction=" + extraAction + "SOURCE=" + source + "EXPORT_SESSION_TIME=" + sessionTime + "SECURITY_LEVEL=" + securityLevel);
            boolean hasPermission = PermissionUtils.hasPermissionExternalStorage(context);
            if (isBackup(action)) {
                if (extraAction == 0) {
                    if (!hasPermission) {
                        Log.m42e("BNR_CLOCK_BackupRestoreReceiver", "(backup fail) No External Storage permission!");
                        sendResponse(getResponseBackup(), filePath, 1, 4, source, sessionTime);
                    } else if (this.mBackupThread == null || !this.mBackupThread.isAlive()) {
                        final String str = filePath;
                        final String str2 = source;
                        hasPermission = sessionTime;
                        this.mBackupThread = new Thread(new Runnable() {
                            public void run() {
                                BackupRestoreReceiver.this.backupData(str, str2, saveKey, securityLevel, hasPermission);
                            }
                        });
                        this.mBackupThread.start();
                        Log.secD("BNR_CLOCK_BackupRestoreReceiver", "mBackupThread is started");
                    } else {
                        Log.secD("BNR_CLOCK_BackupRestoreReceiver", "there is alive mBackupThread!! ignore this request");
                    }
                } else if (extraAction == 2 && this.mBackupThread != null && this.mBackupThread.isAlive()) {
                    this.mBackupThread.stop();
                    Log.secD("BNR_CLOCK_BackupRestoreReceiver", "Cancel request, mBackupThread is stopped!");
                    deleteXmlData(filePath);
                }
            } else if (isRestore(action)) {
                restoreData(filePath, source, saveKey, securityLevel, hasPermission);
            }
        }
    }

    private void backupData(String filePath, String source, String key, int securityLevel, String sessionTime) {
        int result;
        int errCode = 0;
        if (isEmpty()) {
            Log.m42e("BNR_CLOCK_BackupRestoreReceiver", "backupData() / no item to back up");
            result = 1;
            errCode = 3;
        } else {
            result = backupData(filePath, key, securityLevel);
        }
        sendResponse(getResponseBackup(), filePath, result, errCode, source, sessionTime);
    }

    private void restoreData(String filePath, String source, String saveKey, int securityLevel, boolean hasPermission) {
        int result;
        int errCode;
        if (isMaxCount()) {
            Log.m42e("BNR_CLOCK_BackupRestoreReceiver", "restoreData() / count is MAX");
            result = 1;
            errCode = 2;
        } else if (hasPermission) {
            result = restoreData(filePath, saveKey, securityLevel);
            errCode = 0;
        } else {
            Log.m42e("BNR_CLOCK_BackupRestoreReceiver", "(restore fail) No External Storage permission!");
            sendResponse(getResponseRestore(), filePath, 1, 4, source, "");
            errCode = 0;
            return;
        }
        sendResponse(getResponseRestore(), filePath, result, errCode, source, "");
    }

    private void sendResponse(String rspAction, String filePath, int result, int errCode, String source, String sessionTime) {
        Log.secD("BNR_CLOCK_BackupRestoreReceiver", "sendResponse()/rspAction=" + rspAction.substring(rspAction.lastIndexOf(46)) + "/result=" + (result == 1 ? "FAIL" : "SUCCESS") + "/err_code=" + errCode);
        Intent intent = new Intent(rspAction);
        intent.putExtra("RESULT", result);
        intent.putExtra("ERR_CODE", errCode);
        intent.putExtra("REQ_SIZE", getFileLength(filePath));
        intent.putExtra("SOURCE", source);
        intent.putExtra("EXPORT_SESSION_TIME", sessionTime);
        this.mContext.sendBroadcast(intent, "com.wssnps.permission.COM_WSSNPS");
    }
}
