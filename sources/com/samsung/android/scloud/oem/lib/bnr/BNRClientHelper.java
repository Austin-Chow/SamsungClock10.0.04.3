package com.samsung.android.scloud.oem.lib.bnr;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import com.samsung.android.scloud.oem.lib.FileSavedList;
import com.samsung.android.scloud.oem.lib.ItemSavedList;
import com.samsung.android.scloud.oem.lib.LOG;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BNRClientHelper implements IBNRClientHelper {
    private static String OPERATION = "";
    private final Map<String, SyncServiceHandler> SyncServiceHandler_Map = new HashMap();
    private String appname;
    private String category;
    private String contentsId;
    private ISCloudBNRClient mClient;
    private List<String> mProcessedFileList;
    private List<String> mProcessedKeyList;
    private Bundle mResult = new Bundle();
    private String mTAG = "";

    private interface SyncServiceHandler {
        Bundle handleServiceAction(Context context, String str, Bundle bundle);
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$1 */
    class C04221 implements SyncServiceHandler {
        C04221() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            Bundle result = new Bundle();
            LOG.m5f(BNRClientHelper.this.mTAG, "GET_CLIENT_INFO, c: " + BNRClientHelper.this.category);
            boolean isSupportBackup = BNRClientHelper.this.mClient.isSupportBackup(context);
            boolean isEnableBackup = BNRClientHelper.this.mClient.isEnableBackup(context);
            LOG.m5f(BNRClientHelper.this.mTAG, "GET_CLIENT_INFO, s: " + isSupportBackup + ", e: " + isEnableBackup);
            boolean isFirstBackup = BackupMetaManager.getInstance(context).isFirstBackup(name);
            String label = BNRClientHelper.this.mClient.getLabel(context);
            String description = BNRClientHelper.this.mClient.getDescription(context);
            result.putBoolean("support_backup", isSupportBackup);
            result.putString("name", name);
            result.putString("contents_id", BNRClientHelper.this.contentsId);
            result.putBoolean("is_enable_backup", isEnableBackup);
            result.putBoolean("is_first_backup", isFirstBackup);
            result.putString("label", label);
            result.putString("description", description);
            result.putString("category", BNRClientHelper.this.category);
            LOG.m3d(BNRClientHelper.this.mTAG, "GET_CLIENT_INFO, " + BNRClientHelper.this.contentsId + ", " + label + ", " + description + ", " + BNRClientHelper.this.category);
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$2 */
    class C04232 implements SyncServiceHandler {
        C04232() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_PREPARE, v: 1.7.8");
            BNRClientHelper.OPERATION = "backup";
            BNRClientHelper.this.clearPrerestoredData(context, name);
            boolean isSuccess = BNRClientHelper.this.mClient.backupPrepare(context);
            LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_PREPARE, r: " + isSuccess);
            BNRClientHelper.this.mResult.putBoolean("is_success", isSuccess);
            return BNRClientHelper.this.mResult;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$3 */
    class C04243 implements SyncServiceHandler {
        C04243() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            Bundle result = new Bundle();
            int start = extras.getInt("start");
            int max_count = extras.getInt("max_count");
            LOG.m5f(BNRClientHelper.this.mTAG, "GET_ITEM_KEY, s: " + start + ", m: " + max_count);
            HashMap<String, Long> items = BNRClientHelper.this.mClient.getItemKey(context, start, max_count);
            if (items == null) {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_ITEM_KEY, nothing to backup");
                result.putBoolean("is_continue", false);
                result.putBoolean("is_success", true);
            } else if (items.size() == 0) {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_ITEM_KEY, value is incorrect, return err");
                result.putBoolean("is_success", false);
            } else {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_ITEM_KEY, c: " + items.size());
                if (items.size() > max_count) {
                    LOG.m5f(BNRClientHelper.this.mTAG, "GET_ITEM_KEY, value is over~!!, return err");
                    result.putBoolean("is_success", false);
                } else {
                    String[] localIdList = new String[items.size()];
                    long[] timestampList = new long[items.size()];
                    int i = 0;
                    for (Entry<String, Long> item : items.entrySet()) {
                        LOG.m3d(BNRClientHelper.this.mTAG, "GET_ITEM_KEY, item: " + ((String) item.getKey()) + ", " + item.getValue());
                        localIdList[i] = (String) item.getKey();
                        int i2 = i + 1;
                        timestampList[i] = ((Long) item.getValue()).longValue();
                        i = i2;
                    }
                    if (items.size() == max_count) {
                        result.putBoolean("is_continue", true);
                    } else if (items.size() < max_count) {
                        result.putBoolean("is_continue", false);
                    }
                    result.putStringArray("local_id", localIdList);
                    result.putLongArray("timestamp", timestampList);
                    result.putBoolean("is_success", true);
                }
            }
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$4 */
    class C04254 implements SyncServiceHandler {
        C04254() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            Bundle result = new Bundle();
            int start = extras.getInt("start");
            int max_count = extras.getInt("max_count");
            LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_META, s: " + start + ", m: " + max_count);
            ArrayList<BNRFile> files = BNRClientHelper.this.mClient.getFileMeta(context, start, max_count);
            if (files == null) {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_META, nothing to backup");
                result.putBoolean("is_continue", false);
                result.putBoolean("is_success", true);
            } else if (files.size() == 0) {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_META, value is incorrect, return err");
                result.putBoolean("is_success", false);
            } else {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_META, c: " + files.size());
                if (files.size() > max_count) {
                    LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_META, " + name + ", value is over~!!, return err");
                    result.putBoolean("is_success", false);
                } else {
                    String[] path = new String[files.size()];
                    long[] size = new long[files.size()];
                    boolean[] isExternal = new boolean[files.size()];
                    long[] timeStamp = new long[files.size()];
                    int i = 0;
                    Iterator it = files.iterator();
                    while (it.hasNext()) {
                        BNRFile file = (BNRFile) it.next();
                        LOG.m3d(BNRClientHelper.this.mTAG, "GET_FILE_META, " + file.getPath() + ", " + file.getSize() + ", " + file.getisExternal() + ", " + file.getTimeStamp());
                        path[i] = file.getPath();
                        size[i] = file.getSize();
                        isExternal[i] = file.getisExternal();
                        int i2 = i + 1;
                        timeStamp[i] = file.getTimeStamp();
                        i = i2;
                    }
                    if (files.size() == max_count) {
                        result.putBoolean("is_continue", true);
                    } else if (files.size() < max_count) {
                        result.putBoolean("is_continue", false);
                    }
                    result.putStringArray("path", path);
                    result.putLongArray("size", size);
                    result.putBooleanArray("external", isExternal);
                    result.putLongArray("timestamp", timeStamp);
                    result.putBoolean("is_success", true);
                }
            }
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$5 */
    class C04265 implements SyncServiceHandler {
        C04265() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            IOException e;
            FileNotFoundException e2;
            JSONException e3;
            Throwable th;
            Bundle result = new Bundle();
            FileWriter fw = null;
            ArrayList<String> toUploadList = extras.getStringArrayList("to_upload_list");
            ParcelFileDescriptor fd = (ParcelFileDescriptor) extras.getParcelable("file_descriptor");
            long maxSize = extras.getLong("max_size");
            if (toUploadList != null) {
                LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_ITEM, i: " + toUploadList.size());
            }
            ArrayList<BNRItem> items = BNRClientHelper.this.mClient.backupItem(context, toUploadList);
            if (items == null || (items != null && items.size() == 0)) {
                LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_ITEM, value is incorrect, return err");
                result.putBoolean("is_success", false);
            } else {
                LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_ITEM, c: " + items.size());
                try {
                    FileWriter fw2 = new FileWriter(fd.getFileDescriptor());
                    try {
                        String[] nowKey = new String[items.size()];
                        fw2.write("[");
                        BNRItem firstItem = (BNRItem) items.get(0);
                        nowKey[0] = firstItem.getLocalId();
                        long size = firstItem.getSize();
                        JSONObject json = new JSONObject();
                        LOG.m3d(BNRClientHelper.this.mTAG, "BACKUP_ITEM, item: " + firstItem.getLocalId() + ", " + firstItem.getTimeStamp());
                        json.put("key", firstItem.getLocalId());
                        json.put("value", firstItem.getData());
                        json.put("timestamp", firstItem.getTimeStamp());
                        fw2.write(json.toString());
                        if (items.size() > 1) {
                            int index = 1;
                            while (index < items.size()) {
                                BNRItem item = (BNRItem) items.get(index);
                                if (item != null) {
                                    if (item.getSize() + size >= maxSize) {
                                        break;
                                    }
                                    nowKey[index] = item.getLocalId();
                                    size += item.getSize();
                                    fw2.write(",");
                                    LOG.m3d(BNRClientHelper.this.mTAG, "BACKUP_ITEM, item: " + item.getLocalId() + ", " + item.getTimeStamp());
                                    json.put("key", item.getLocalId());
                                    json.put("value", item.getData());
                                    json.put("timestamp", item.getTimeStamp());
                                    fw2.write(json.toString());
                                    index++;
                                } else {
                                    LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_ITEM, item is incorrect: " + index + ", return err");
                                    fw2.close();
                                    result.putBoolean("is_success", false);
                                    if (fw2 != null) {
                                        try {
                                            fw2.close();
                                        } catch (IOException e4) {
                                            e4.printStackTrace();
                                        }
                                    }
                                    fw = fw2;
                                }
                            }
                        }
                        fw2.write("]");
                        fw2.flush();
                        result.putBoolean("is_success", true);
                        result.putStringArray("local_id", nowKey);
                        if (fw2 != null) {
                            try {
                                fw2.close();
                                fw = fw2;
                            } catch (IOException e42) {
                                e42.printStackTrace();
                            }
                        }
                        fw = fw2;
                    } catch (FileNotFoundException e5) {
                        e2 = e5;
                        fw = fw2;
                    } catch (IOException e6) {
                        e42 = e6;
                        fw = fw2;
                    } catch (JSONException e7) {
                        e3 = e7;
                        fw = fw2;
                    } catch (Throwable th2) {
                        th = th2;
                        fw = fw2;
                    }
                } catch (FileNotFoundException e8) {
                    e2 = e8;
                    try {
                        LOG.m4e(BNRClientHelper.this.mTAG, "FileNotFoundException~!!, " + name, e2);
                        result.putBoolean("is_success", false);
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e422) {
                                e422.printStackTrace();
                            }
                        }
                        return result;
                    } catch (Throwable th3) {
                        th = th3;
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e4222) {
                                e4222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e9) {
                    e4222 = e9;
                    LOG.m4e(BNRClientHelper.this.mTAG, "IOException~!!, " + name, e4222);
                    result.putBoolean("is_success", false);
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e42222) {
                            e42222.printStackTrace();
                        }
                    }
                    return result;
                } catch (JSONException e10) {
                    e3 = e10;
                    LOG.m4e(BNRClientHelper.this.mTAG, "JSONException~!!, " + name, e3);
                    result.putBoolean("is_success", false);
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e422222) {
                            e422222.printStackTrace();
                        }
                    }
                    return result;
                }
            }
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$6 */
    class C04276 implements SyncServiceHandler {
        C04276() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_PATH, " + BNRClientHelper.OPERATION);
            String path = extras.getString("path");
            boolean external = extras.getBoolean("is_external");
            Bundle result = new Bundle();
            LOG.m3d(BNRClientHelper.this.mTAG, "GET_FILE_PATH, " + path + ", " + external);
            String localPath = BNRClientHelper.this.mClient.getFilePath(context, path, external, BNRClientHelper.OPERATION);
            if (localPath != null) {
                LOG.m3d(BNRClientHelper.this.mTAG, "GET_FILE_PATH, r: " + localPath);
                result.putBoolean("is_success", true);
                result.putString("real_path", localPath);
            } else {
                LOG.m5f(BNRClientHelper.this.mTAG, "GET_FILE_PATH, value is incorrect, return err");
                result.putBoolean("is_success", false);
            }
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$7 */
    class C04287 implements SyncServiceHandler {
        C04287() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            boolean backupSuccess = extras.getBoolean("is_success");
            LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_COMPLETE, " + backupSuccess);
            boolean isSuccess = BNRClientHelper.this.mClient.backupComplete(context, backupSuccess);
            if (isSuccess && backupSuccess) {
                BackupMetaManager.getInstance(context).setFirstBackup(name, false);
                BackupMetaManager.getInstance(context).setLastBackupTime(name, System.currentTimeMillis());
            }
            LOG.m5f(BNRClientHelper.this.mTAG, "BACKUP_COMPLETE, return: " + isSuccess);
            BNRClientHelper.this.mResult.putBoolean("is_success", isSuccess);
            return BNRClientHelper.this.mResult;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$8 */
    class C04298 implements SyncServiceHandler {
        C04298() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_PREPARE, v: 1.7.8");
            BNRClientHelper.OPERATION = "restore";
            BNRClientHelper.this.clearPrerestoredData(context, name);
            Bundle result = new Bundle();
            boolean isSuccess = BNRClientHelper.this.mClient.restorePrepare(context, extras);
            LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_PREPARE, return: " + isSuccess);
            result.putBoolean("is_success", isSuccess);
            return result;
        }
    }

    /* renamed from: com.samsung.android.scloud.oem.lib.bnr.BNRClientHelper$9 */
    class C04309 implements SyncServiceHandler {
        C04309() {
        }

        public Bundle handleServiceAction(Context context, String name, Bundle extras) {
            ParcelFileDescriptor fd = (ParcelFileDescriptor) extras.getParcelable("file_descriptor");
            Bundle result = new Bundle();
            ArrayList<BNRItem> items = new ArrayList();
            ArrayList<String> insertedId = new ArrayList();
            LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_ITEM, c: " + items.size());
            BNRClientHelper.this.convertToBNRItems(fd, items);
            boolean is_success = BNRClientHelper.this.mClient.restoreItem(context, items, insertedId);
            LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_ITEM, return: " + insertedId.size() + ", " + is_success);
            if (insertedId.size() > 0) {
                Iterator it = insertedId.iterator();
                while (it.hasNext()) {
                    BNRClientHelper.this.addToList(context, name, 0, (String) it.next());
                }
            }
            result.putBoolean("is_success", is_success);
            result.putStringArray("inserted_id_list", (String[]) insertedId.toArray(new String[insertedId.size()]));
            return result;
        }
    }

    public BNRClientHelper(Context context, String name, ISCloudBNRClient client, String cid, String category) {
        this.mTAG = "BNRClientHelper_" + name;
        LOG.m5f(this.mTAG, "BNRCLIENTHELPER, v: 1.7.8");
        this.appname = name;
        this.mClient = client;
        this.contentsId = cid;
        this.category = category;
        LOG.m3d(this.mTAG, "BNRCLIENTHELPER, " + cid + ", " + category);
        setServiceHandler();
    }

    public Bundle handleRequest(Context context, String method, String name, Bundle param) {
        if (this.SyncServiceHandler_Map.containsKey(method)) {
            return ((SyncServiceHandler) this.SyncServiceHandler_Map.get(method)).handleServiceAction(context, name, param);
        }
        return null;
    }

    private void setServiceHandler() {
        this.SyncServiceHandler_Map.put("getClientInfo", new C04221());
        this.SyncServiceHandler_Map.put("backupPrepare", new C04232());
        this.SyncServiceHandler_Map.put("getItemKey", new C04243());
        this.SyncServiceHandler_Map.put("getFileMeta", new C04254());
        this.SyncServiceHandler_Map.put("backupItem", new C04265());
        this.SyncServiceHandler_Map.put("getFilePath", new C04276());
        this.SyncServiceHandler_Map.put("backupComplete", new C04287());
        this.SyncServiceHandler_Map.put("restorePrepare", new C04298());
        this.SyncServiceHandler_Map.put("restoreItem", new C04309());
        this.SyncServiceHandler_Map.put("restoreFile", new SyncServiceHandler() {
            public Bundle handleServiceAction(Context context, String name, Bundle extras) {
                Bundle result = new Bundle();
                BNRClientHelper.this.addToList(context, name, 1, extras.getString("path"));
                LOG.m3d(BNRClientHelper.this.mTAG, "RESTORE_FILE, " + extras.getString("path"));
                return result;
            }
        });
        this.SyncServiceHandler_Map.put("restoreComplete", new SyncServiceHandler() {
            public Bundle handleServiceAction(Context context, String name, Bundle extras) {
                boolean isSuccess = extras.getBoolean("is_success");
                LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, " + isSuccess);
                boolean local_fileCopy = true;
                boolean oemSuccess = true;
                int start = 0;
                ArrayList<String> total_local_file = new ArrayList();
                if (isSuccess) {
                    Iterator it;
                    ArrayList<BNRFile> sub_local_file;
                    do {
                        sub_local_file = BNRClientHelper.this.mClient.getFileMeta(context, start, 100);
                        if (sub_local_file != null && sub_local_file.size() == 0) {
                            BNRClientHelper.this.mResult.putBoolean("is_success", false);
                            return BNRClientHelper.this.mResult;
                        } else if (sub_local_file != null) {
                            start += sub_local_file.size();
                            it = sub_local_file.iterator();
                            while (it.hasNext()) {
                                BNRFile file = (BNRFile) it.next();
                                String rPath = BNRClientHelper.this.mClient.getFilePath(context, file.getPath(), file.getisExternal(), "restore");
                                if (!(rPath == null || rPath.contains("_scloud_dwnload") || rPath.contains("_scloud_origin"))) {
                                    total_local_file.add(rPath);
                                }
                            }
                            continue;
                        }
                    } while (sub_local_file != null);
                    if (BNRClientHelper.this.mProcessedFileList == null) {
                        BNRClientHelper.this.mProcessedFileList = FileSavedList.load(context, name);
                    }
                    LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, restoredFileList size : " + BNRClientHelper.this.mProcessedFileList.size());
                    for (String inserted_file : BNRClientHelper.this.mProcessedFileList) {
                        if (!BNRClientHelper.this.fileCopy(inserted_file, new StringBuilder(String.valueOf(inserted_file)).append("_scloud_origin").toString())) {
                            local_fileCopy = false;
                            break;
                        }
                        if (!BNRClientHelper.this.fileCopy(new StringBuilder(String.valueOf(inserted_file)).append("_scloud_dwnload").toString(), inserted_file)) {
                            local_fileCopy = false;
                            break;
                        }
                    }
                    if (local_fileCopy) {
                        if (BNRClientHelper.this.mProcessedKeyList == null) {
                            BNRClientHelper.this.mProcessedKeyList = ItemSavedList.load(context, name);
                        }
                        LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, restoredKeyList size : " + BNRClientHelper.this.mProcessedKeyList.size());
                        if (BNRClientHelper.this.mProcessedKeyList.size() >= 0) {
                            oemSuccess = BNRClientHelper.this.mClient.restoreComplete(context, (String[]) BNRClientHelper.this.mProcessedKeyList.toArray(new String[BNRClientHelper.this.mProcessedKeyList.size()]), local_fileCopy);
                            if (!oemSuccess) {
                                BNRClientHelper.this.clearPrerestoredData(context, name);
                            }
                        }
                        if (local_fileCopy && oemSuccess) {
                            for (String tmpfile : BNRClientHelper.this.mProcessedFileList) {
                                File originFile = new File(new StringBuilder(String.valueOf(tmpfile)).append("_scloud_origin").toString());
                                File downloadFile = new File(new StringBuilder(String.valueOf(tmpfile)).append("_scloud_dwnload").toString());
                                if (originFile != null && originFile.exists() && originFile.delete()) {
                                    LOG.m6i(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, delete :" + tmpfile + "_scloud_origin");
                                }
                                if (downloadFile != null && downloadFile.exists() && downloadFile.delete()) {
                                    LOG.m6i(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, delete :" + tmpfile + "_scloud_dwnload");
                                }
                                if (total_local_file.contains(tmpfile)) {
                                    total_local_file.remove(tmpfile);
                                }
                            }
                            it = total_local_file.iterator();
                            while (it.hasNext()) {
                                String path = (String) it.next();
                                File oldFile = new File(path);
                                if (oldFile != null && oldFile.exists() && oldFile.delete()) {
                                    LOG.m6i(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, delete :" + path);
                                }
                            }
                            BackupMetaManager.getInstance(context).setFirstBackup(name, true);
                            BNRClientHelper.this.mProcessedKeyList.clear();
                            BNRClientHelper.this.mProcessedFileList.clear();
                        }
                    } else {
                        BNRClientHelper.this.clearPrerestoredData(context, name);
                    }
                } else {
                    BNRClientHelper.this.clearPrerestoredData(context, name);
                }
                LOG.m5f(BNRClientHelper.this.mTAG, "RESTORE_COMPLETE, return: " + oemSuccess);
                BNRClientHelper.this.mResult.putBoolean("is_success", oemSuccess);
                return BNRClientHelper.this.mResult;
            }
        });
        this.SyncServiceHandler_Map.put("accountSignIn", new SyncServiceHandler() {
            public Bundle handleServiceAction(Context context, String name, Bundle extras) {
                LOG.m5f(BNRClientHelper.this.mTAG, "ACCOUNT_SIGN_IN");
                Bundle result = new Bundle();
                BNRClientHelper.this.clearPrerestoredData(context, name);
                if (BackupMetaManager.getInstance(context).clear(name)) {
                    result.putBoolean("is_success", true);
                } else {
                    result.putBoolean("is_success", false);
                }
                return result;
            }
        });
        this.SyncServiceHandler_Map.put("accountSignOut", new SyncServiceHandler() {
            public Bundle handleServiceAction(Context context, String name, Bundle extras) {
                LOG.m5f(BNRClientHelper.this.mTAG, "ACCOUNT_SIGN_OUT");
                Bundle result = new Bundle();
                BNRClientHelper.this.clearPrerestoredData(context, name);
                if (BackupMetaManager.getInstance(context).clear(name)) {
                    result.putBoolean("is_success", true);
                } else {
                    result.putBoolean("is_success", false);
                }
                return result;
            }
        });
    }

    private void convertToBNRItems(ParcelFileDescriptor fd, List<BNRItem> toUploadItems) {
        IOException e;
        JSONArray jSONArray;
        FileInputStream fileInputStream;
        JSONException e2;
        try {
            FileInputStream fileIpStream = new FileInputStream(fd.getFileDescriptor());
            try {
                byte[] buffer = new byte[((int) fd.getStatSize())];
                fileIpStream.read(buffer);
                JSONArray jsonArray = new JSONArray(new String(buffer));
                int i = 0;
                while (i < jsonArray.length()) {
                    try {
                        JSONObject json = jsonArray.optJSONObject(i);
                        BNRItem item = new BNRItem(json.optString("key"), json.optString("value"), json.optLong("timestamp"));
                        LOG.m3d(this.mTAG, "converToBNRItems : " + item.getLocalId() + ", " + item.getTimeStamp());
                        toUploadItems.add(item);
                        i++;
                    } catch (IOException e3) {
                        e = e3;
                        jSONArray = jsonArray;
                        fileInputStream = fileIpStream;
                    } catch (JSONException e4) {
                        e2 = e4;
                        jSONArray = jsonArray;
                        fileInputStream = fileIpStream;
                    }
                }
                fileIpStream.close();
                jSONArray = jsonArray;
                fileInputStream = fileIpStream;
            } catch (IOException e5) {
                e = e5;
                fileInputStream = fileIpStream;
                e.printStackTrace();
            } catch (JSONException e6) {
                e2 = e6;
                fileInputStream = fileIpStream;
                e2.printStackTrace();
            }
        } catch (IOException e7) {
            e = e7;
            e.printStackTrace();
        } catch (JSONException e8) {
            e2 = e8;
            e2.printStackTrace();
        }
    }

    private boolean fileCopy(String fromPath, String toPath) {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        LOG.m6i(this.mTAG, "fileCopy(), from : " + fromPath + " , to : " + toPath);
        File oldFile = new File(fromPath);
        if (oldFile == null || !oldFile.isFile()) {
            LOG.m6i(this.mTAG, "oldFile is null or not file~!");
            return true;
        }
        File newFile = new File(toPath);
        if (newFile != null && newFile.exists()) {
            newFile.delete();
        }
        if (oldFile.renameTo(newFile)) {
            if (oldFile.exists()) {
                oldFile.delete();
            }
            return true;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[1024];
        try {
            FileInputStream fis2 = new FileInputStream(oldFile);
            try {
                FileOutputStream fos2 = new FileOutputStream(newFile);
                while (true) {
                    try {
                        int read = fis2.read(buf, 0, buf.length);
                        if (read == -1) {
                            try {
                                break;
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        } else {
                            fos2.write(buf, 0, read);
                        }
                    } catch (FileNotFoundException e4) {
                        e2 = e4;
                        fos = fos2;
                        fis = fis2;
                    } catch (IOException e5) {
                        e3 = e5;
                        fos = fos2;
                        fis = fis2;
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                        fis = fis2;
                    }
                }
                if (oldFile.exists()) {
                    oldFile.delete();
                }
                if (fis2 != null) {
                    fis2.close();
                }
                if (fos2 != null) {
                    fos2.close();
                }
                return true;
            } catch (FileNotFoundException e6) {
                e2 = e6;
                fis = fis2;
                try {
                    LOG.m4e(this.mTAG, "fileCopy() failed", e2);
                    try {
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                        if (fis != null) {
                            fis.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                    return false;
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                        if (fis != null) {
                            fis.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                    throw th;
                }
            } catch (IOException e7) {
                e322 = e7;
                fis = fis2;
                LOG.m4e(this.mTAG, "fileCopy() failed", e322);
                try {
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e3222) {
                    e3222.printStackTrace();
                }
                return false;
            } catch (Throwable th4) {
                th = th4;
                fis = fis2;
                if (oldFile.exists()) {
                    oldFile.delete();
                }
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e8) {
            e2 = e8;
            LOG.m4e(this.mTAG, "fileCopy() failed", e2);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
            return false;
        } catch (IOException e9) {
            e3222 = e9;
            LOG.m4e(this.mTAG, "fileCopy() failed", e3222);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
            return false;
        }
    }

    private void clearPrerestoredData(Context context, String name) {
        this.mProcessedKeyList = ItemSavedList.load(context, name);
        if (this.mProcessedKeyList.size() > 0) {
            LOG.m5f(this.mTAG, "remove restored data in previous failed restoring.. - " + this.mProcessedKeyList.size());
            this.mClient.restoreComplete(context, (String[]) this.mProcessedKeyList.toArray(new String[this.mProcessedKeyList.size()]), false);
            this.mProcessedKeyList.clear();
        }
        this.mProcessedFileList = FileSavedList.load(context, name);
        if (this.mProcessedFileList.size() > 0) {
            LOG.m5f(this.mTAG, "remove restored files in previous failed restoring.. - " + this.mProcessedFileList.size());
            for (String processedFile : this.mProcessedFileList) {
                fileCopy(new StringBuilder(String.valueOf(processedFile)).append("_scloud_origin").toString(), processedFile);
                File tempFile = new File(new StringBuilder(String.valueOf(processedFile)).append("_scloud_dwnload").toString());
                if (tempFile != null && tempFile.exists() && tempFile.delete()) {
                    LOG.m6i(this.mTAG, "clearPreRestoredData() delete : " + processedFile + "_scloud_dwnload");
                }
            }
            this.mProcessedFileList.clear();
        }
    }

    private void addToList(Context context, String name, int type, String key) {
        switch (type) {
            case 0:
                if (this.mProcessedKeyList == null) {
                    this.mProcessedKeyList = ItemSavedList.load(context, name);
                }
                this.mProcessedKeyList.add(key);
                return;
            case 1:
                if (this.mProcessedFileList == null) {
                    this.mProcessedFileList = FileSavedList.load(context, name);
                }
                this.mProcessedFileList.add(key);
                return;
            default:
                return;
        }
    }
}
