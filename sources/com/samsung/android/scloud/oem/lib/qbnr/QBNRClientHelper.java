package com.samsung.android.scloud.oem.lib.qbnr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NotificationCompat;
import com.samsung.android.scloud.oem.lib.LOG;
import com.samsung.android.scloud.oem.lib.bnr.BackupMetaManager;
import com.samsung.android.scloud.oem.lib.bnr.IBNRClientHelper;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient.QuickBackupListener;
import java.util.HashMap;
import java.util.Map;

public class QBNRClientHelper implements IBNRClientHelper {
    private String category;
    private String contentsId;
    private ISCloudQBNRClient mClient;
    private boolean mIsFinished;
    private boolean mIsSuccess;
    private String mName;
    private long mProcNow;
    private long mProcTotal;
    private final Map<String, SyncServiceHandler> syncServiceHandlerMap = new HashMap();

    private interface SyncServiceHandler {
        Bundle handleServiceAction(Context context, String str, Bundle bundle);
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.qbnr.QBNRClientHelper$1 */
    class C04311 implements SyncServiceHandler {
        C04311() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            LOG.m5f("QBNRClientHelper", "GET_CLIENT_INFO, " + name);
            boolean isFirstBackup = BackupMetaManager.getInstance(context).isFirstBackup(name);
            boolean isSupportBackup = QBNRClientHelper.this.mClient.isSupportBackup(context);
            boolean isEnableBackup = QBNRClientHelper.this.mClient.isEnableBackup(context);
            String label = QBNRClientHelper.this.mClient.getLabel(context);
            String description = QBNRClientHelper.this.mClient.getDescription(context);
            Bundle result = new Bundle();
            result.putBoolean("support_backup", isSupportBackup);
            result.putString("name", name);
            result.putBoolean("is_enable_backup", isEnableBackup);
            result.putBoolean("is_first_backup", isFirstBackup);
            result.putString("label", label);
            result.putString("description", description);
            result.putString("category", QBNRClientHelper.this.category);
            result.putString("contents_id", QBNRClientHelper.this.contentsId);
            LOG.m3d("QBNRClientHelper", "GET_CLIENT_INFO, " + name + ", " + QBNRClientHelper.this.contentsId + ", " + label + ", " + description + ", " + QBNRClientHelper.this.category);
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.qbnr.QBNRClientHelper$2 */
    class C04342 implements SyncServiceHandler {
        C04342() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            final Uri observingUri = Uri.parse(extras.getString("observing_uri"));
            final ParcelFileDescriptor file = (ParcelFileDescriptor) extras.getParcelable("file");
            QBNRClientHelper.this.init();
            final Context context2 = context;
            final String str = name;
            new Thread(new Runnable() {
                public void run() {
                    ISCloudQBNRClient access$0 = QBNRClientHelper.this.mClient;
                    Context context = context2;
                    ParcelFileDescriptor parcelFileDescriptor = file;
                    final Context context2 = context2;
                    final Uri uri = observingUri;
                    final String str = str;
                    access$0.backup(context, parcelFileDescriptor, new QuickBackupListener() {
                        public void onProgress(long proc, long total) {
                            LOG.m3d("QBNRClientHelper", "onProgress -  proc : " + proc + " / " + total);
                            QBNRClientHelper.this.mProcNow = proc;
                            QBNRClientHelper.this.mProcTotal = total;
                            context2.getContentResolver().notifyChange(uri, null);
                        }

                        public void complete(boolean isSuccess) {
                            LOG.m5f("QBNRClientHelper", "BACKUP, " + str + ", complete - isSuccess : " + isSuccess);
                            QBNRClientHelper.this.mIsFinished = true;
                            QBNRClientHelper.this.mIsSuccess = isSuccess;
                            context2.getContentResolver().notifyChange(uri, null);
                        }
                    });
                }
            }, "BACKUP_" + name).start();
            return null;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.qbnr.QBNRClientHelper$3 */
    class C04373 implements SyncServiceHandler {
        C04373() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            final Uri observingUri = Uri.parse(extras.getString("observing_uri"));
            final ParcelFileDescriptor file = (ParcelFileDescriptor) extras.getParcelable("file");
            QBNRClientHelper.this.init();
            final Context context2 = context;
            final String str = name;
            new Thread(new Runnable() {
                public void run() {
                    ISCloudQBNRClient access$0 = QBNRClientHelper.this.mClient;
                    Context context = context2;
                    ParcelFileDescriptor parcelFileDescriptor = file;
                    final Context context2 = context2;
                    final Uri uri = observingUri;
                    final String str = str;
                    access$0.restore(context, parcelFileDescriptor, new QuickBackupListener() {
                        public void onProgress(long proc, long total) {
                            LOG.m3d("QBNRClientHelper", "onProgress -  proc : " + proc + " / " + total);
                            QBNRClientHelper.this.mProcNow = proc;
                            QBNRClientHelper.this.mProcTotal = total;
                            context2.getContentResolver().notifyChange(uri, null);
                        }

                        public void complete(boolean isSuccess) {
                            LOG.m5f("QBNRClientHelper", "RESTORE, " + str + ", complete - isSuccess : " + isSuccess);
                            QBNRClientHelper.this.mIsFinished = true;
                            QBNRClientHelper.this.mIsSuccess = isSuccess;
                            context2.getContentResolver().notifyChange(uri, null);
                        }
                    });
                }
            }, "RESTORE_" + name).start();
            return null;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.qbnr.QBNRClientHelper$4 */
    class C04384 implements SyncServiceHandler {
        C04384() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            long j = 0;
            LOG.m5f("QBNRClientHelper", "GET_STATUS, " + name + ", is_finished : " + QBNRClientHelper.this.mIsFinished + ", is_success : " + QBNRClientHelper.this.mIsSuccess + ", proc : " + QBNRClientHelper.this.mProcNow + ", total : " + QBNRClientHelper.this.mProcTotal);
            Bundle result = new Bundle();
            result.putBoolean("is_finished", QBNRClientHelper.this.mIsFinished);
            result.putBoolean("is_success", QBNRClientHelper.this.mIsSuccess);
            if (!QBNRClientHelper.this.mIsFinished) {
                String str = NotificationCompat.CATEGORY_PROGRESS;
                if (QBNRClientHelper.this.mProcTotal != 0) {
                    j = (QBNRClientHelper.this.mProcNow * 100) / QBNRClientHelper.this.mProcTotal;
                }
                result.putInt(str, (int) j);
            }
            return result;
        }
    }

    public QBNRClientHelper(Context context, String name, ISCloudQBNRClient client, String cid, String category) {
        LOG.m5f("QBNRClientHelper", "init SyncClientHelper : " + name);
        this.mName = name;
        this.mClient = client;
        this.category = category;
        this.contentsId = cid;
        setHandlers();
    }

    private void init() {
        this.mProcNow = 0;
        this.mProcTotal = 0;
        this.mIsFinished = false;
        this.mIsSuccess = false;
    }

    public Bundle handleRequest(Context context, String method, String name, Bundle param) {
        if (this.syncServiceHandlerMap.containsKey(method)) {
            return ((SyncServiceHandler) this.syncServiceHandlerMap.get(method)).handleServiceAction(context, name, param);
        }
        return null;
    }

    void setHandlers() {
        this.syncServiceHandlerMap.put("getClientInfo", new C04311());
        this.syncServiceHandlerMap.put("backup", new C04342());
        this.syncServiceHandlerMap.put("restore", new C04373());
        this.syncServiceHandlerMap.put("get_status", new C04384());
    }
}
