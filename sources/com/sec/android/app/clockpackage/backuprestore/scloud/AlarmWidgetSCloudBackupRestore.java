package com.sec.android.app.clockpackage.backuprestore.scloud;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.samsung.android.scloud.oem.lib.FileTool;
import com.samsung.android.scloud.oem.lib.FileTool.PDMProgressListener;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient.QuickBackupListener;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.backuprestore.receiver.AlarmBackupRestoreReceiver;
import com.sec.android.app.clockpackage.backuprestore.util.AlarmWidgetDataConvertToXml;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AlarmWidgetSCloudBackupRestore implements ISCloudQBNRClient {
    private static final String SESSION_KEY_FOR_SCLOUD = "BNR_SCLOUD_ALARMWIDGET";
    private static final String TAG = "BNR_CLOCK_ALARMWIDGET_AlarmWidgetSCloudBackupRestore";

    public void backup(Context context, ParcelFileDescriptor file, final QuickBackupListener listener) {
        IOException e;
        Throwable th;
        Exception e2;
        Log.secD(TAG, "alarm widget backup() : start");
        String backupFilePath = context.getFilesDir().getAbsolutePath();
        Log.secD(TAG, "alarm widget backup() - 1");
        File backupFile = new File(backupFilePath + "/alarmWidget.exml");
        if (!backupFile.exists() || backupFile.delete()) {
            Log.secD(TAG, "alarm widget backup() - 2");
            int result = new AlarmWidgetDataConvertToXml(backupFilePath, SESSION_KEY_FOR_SCLOUD, -1, 4).backupData(context);
            OutputStream fos = null;
            try {
                Log.secD(TAG, "alarm widget backup() - 3 / result=" + result + "/ backupFile.exists()=" + backupFile.exists());
                if (result == 0 && backupFile.exists()) {
                    OutputStream fos2 = new FileOutputStream(file.getFileDescriptor());
                    try {
                        FileTool.writeToFile(backupFile.getPath(), backupFile.length(), (FileOutputStream) fos2, new PDMProgressListener() {
                            public void transferred(long now, long total) {
                                listener.onProgress(now, total);
                            }
                        });
                        Log.secD(TAG, "alarm widget backup() - success");
                        listener.complete(true);
                        fos = fos2;
                    } catch (IOException e3) {
                        e = e3;
                        fos = fos2;
                        try {
                            e.printStackTrace();
                            listener.complete(false);
                            Log.secD(TAG, "alarm widget backup() 4 - finally start");
                            close(fos);
                            Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                            Log.secD(TAG, "alarm widget backup() - end");
                            return;
                        } catch (Throwable th2) {
                            th = th2;
                            Log.secD(TAG, "alarm widget backup() 4 - finally start");
                            close(fos);
                            if (backupFile.exists() && !backupFile.delete()) {
                                Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                            }
                            Log.secD(TAG, "alarm widget backup() - end");
                            throw th;
                        }
                    } catch (Exception e4) {
                        e2 = e4;
                        fos = fos2;
                        e2.printStackTrace();
                        listener.complete(false);
                        Log.secD(TAG, "alarm widget backup() 4 - finally start");
                        close(fos);
                        Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                        Log.secD(TAG, "alarm widget backup() - end");
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        fos = fos2;
                        Log.secD(TAG, "alarm widget backup() 4 - finally start");
                        close(fos);
                        Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                        Log.secD(TAG, "alarm widget backup() - end");
                        throw th;
                    }
                }
                Log.secE(TAG, "alarm widget backup() - Fail transfer alarm widget backup data to SCloud");
                listener.complete(false);
                Log.secD(TAG, "alarm widget backup() 4 - finally start");
                close(fos);
                if (backupFile.exists() && !backupFile.delete()) {
                    Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                }
                Log.secD(TAG, "alarm widget backup() - end");
                return;
            } catch (IOException e5) {
                e = e5;
                e.printStackTrace();
                listener.complete(false);
                Log.secD(TAG, "alarm widget backup() 4 - finally start");
                close(fos);
                if (backupFile.exists() && !backupFile.delete()) {
                    Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                }
                Log.secD(TAG, "alarm widget backup() - end");
                return;
            } catch (Exception e6) {
                e2 = e6;
                e2.printStackTrace();
                listener.complete(false);
                Log.secD(TAG, "alarm widget backup() 4 - finally start");
                close(fos);
                if (backupFile.exists() && !backupFile.delete()) {
                    Log.secD(TAG, "alarm widget backup() - Fail deleteBackupFile: " + backupFile + "/ backupFile.exists()=" + backupFile.exists());
                }
                Log.secD(TAG, "alarm widget backup() - end");
                return;
            }
        }
        Log.secE(TAG, "alarm widget backup()- Fail deletePreviousBackupFile: " + backupFile);
        listener.complete(false);
    }

    public void restore(Context context, ParcelFileDescriptor file, final QuickBackupListener listener) {
        IOException e;
        Throwable th;
        Exception e2;
        Log.secD(TAG, "alarm widget restore() - start");
        String restoreFilePath = context.getFilesDir().getAbsolutePath();
        Log.secD(TAG, "alarm widget restore() - restoreFilePath = " + restoreFilePath);
        File restoreFile = new File(restoreFilePath + "/alarmWidget.exml");
        if (!restoreFile.exists() || restoreFile.delete()) {
            InputStream fis = null;
            try {
                if (restoreFile.exists() || restoreFile.createNewFile()) {
                    Log.secD(TAG, "alarm widget restore() - file exit =" + restoreFile.exists());
                    InputStream fis2 = new FileInputStream(file.getFileDescriptor());
                    try {
                        FileTool.writeToFile(fis2, file.getStatSize(), restoreFile.getAbsolutePath(), new PDMProgressListener() {
                            public void transferred(long now, long total) {
                                listener.onProgress(now, total);
                            }
                        });
                        Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                        close(fis2);
                        int result = AlarmBackupRestoreReceiver.restoreAlarmWidgetFromXML(context, restoreFilePath, SESSION_KEY_FOR_SCLOUD, -1);
                        if (result == 0) {
                            listener.complete(true);
                        } else {
                            Log.secD(TAG, "alarm widget restore() - Fail restoreAlarmWidgetFromXML: result " + result);
                            listener.complete(false);
                        }
                        Log.secD(TAG, "alarm widget restore() - end / result = " + result);
                        return;
                    } catch (IOException e3) {
                        e = e3;
                        fis = fis2;
                        try {
                            e.printStackTrace();
                            listener.complete(false);
                            Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                            close(fis);
                            return;
                        } catch (Throwable th2) {
                            th = th2;
                            Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                            close(fis);
                            throw th;
                        }
                    } catch (Exception e4) {
                        e2 = e4;
                        fis = fis2;
                        e2.printStackTrace();
                        listener.complete(false);
                        Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                        close(fis);
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        fis = fis2;
                        Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                        close(fis);
                        throw th;
                    }
                }
                Log.m42e(TAG, "alarm widget restore() - Fail createRestoreNewFile: " + restoreFile);
                listener.complete(false);
                Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                close(null);
                return;
            } catch (IOException e5) {
                e = e5;
                e.printStackTrace();
                listener.complete(false);
                Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                close(fis);
                return;
            } catch (Exception e6) {
                e2 = e6;
                e2.printStackTrace();
                listener.complete(false);
                Log.secD(TAG, "alarm widget restore() - close FileInputStream");
                close(fis);
                return;
            }
        }
        Log.secD(TAG, "alarm widget restore() - Fail deletePreviousRestoreFile: " + restoreFile);
        listener.complete(false);
    }

    private void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSupportBackup(Context context) {
        Log.secD(TAG, "isSupportBackup()");
        return true;
    }

    public boolean isEnableBackup(Context context) {
        Log.secD(TAG, "isEnableBackup()");
        return true;
    }

    public String getLabel(Context context) {
        Log.secD(TAG, "getLabel()");
        return context.getResources().getString(R.string.alarm);
    }

    public String getDescription(Context context) {
        Log.secD(TAG, "getDescription()");
        return context.getResources().getString(R.string.alarm);
    }
}
