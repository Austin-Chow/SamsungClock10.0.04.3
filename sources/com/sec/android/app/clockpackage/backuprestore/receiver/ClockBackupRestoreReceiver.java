package com.sec.android.app.clockpackage.backuprestore.receiver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.ParcelFileDescriptor;
import com.samsung.android.scloud.oem.lib.FileTool;
import com.samsung.android.scloud.oem.lib.FileTool.PDMProgressListener;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient.QuickBackupListener;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.backuprestore.util.BackupRestoreUtils;
import com.sec.android.app.clockpackage.backuprestore.util.ClockDataConvertToXML;
import com.sec.android.app.clockpackage.backuprestore.util.ClockDataEncryption;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.model.Timer;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import com.sec.android.app.clockpackage.timer.model.TimerProvider;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.Worldclock;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ClockBackupRestoreReceiver extends BackupRestoreReceiver implements ISCloudQBNRClient {
    private static int sInsertedCount = 0;
    private static ArrayList<String> sInsertedRowIds;
    private boolean mRestoreWorldclockV2 = false;
    private ClockDataConvertToXML mXmlConverterTimer;
    private ClockDataConvertToXML mXmlConverterWorldclock;

    /* renamed from: com.sec.android.app.clockpackage.backuprestore.receiver.ClockBackupRestoreReceiver$1 */
    class C06401 implements PDMProgressListener {
        final /* synthetic */ QuickBackupListener val$listener;

        C06401(QuickBackupListener quickBackupListener) {
            this.val$listener = quickBackupListener;
        }

        public void transferred(long now, long total) {
            this.val$listener.onProgress(now, total);
        }
    }

    public void backup(android.content.Context r17, android.os.ParcelFileDescriptor r18, com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient.QuickBackupListener r19) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x010e in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1603177117.run(Unknown Source)
*/
        /*
        r16 = this;
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";
        r11 = "Scloud Timer backup() - start";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        r0 = r17;
        r1 = r16;
        r1.mContext = r0;
        r10 = com.sec.android.app.clockpackage.timer.model.TimerPresetItem.getPresetCount(r17);
        if (r10 != 0) goto L_0x0021;
    L_0x0013:
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";
        r11 = "Scloud Timer backup() item count == 0 - end";
        com.sec.android.app.clockpackage.common.util.Log.m42e(r10, r11);
        r10 = 1;
        r0 = r19;
        r0.complete(r10);
    L_0x0020:
        return;
    L_0x0021:
        r10 = "timer.db";
        r0 = r17;
        r10 = r0.getDatabasePath(r10);
        r4 = r10.getPath();
        r10 = r17.getFilesDir();
        r6 = r10.getAbsolutePath();
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10 = r10.append(r6);
        r11 = "/timer.exml";
        r10 = r10.append(r11);
        r8 = r10.toString();
        r3 = new java.io.File;
        r3.<init>(r8);
        r2 = r3.exists();
        if (r2 == 0) goto L_0x0078;
    L_0x0053:
        r10 = r3.delete();
        if (r10 != 0) goto L_0x0078;
    L_0x0059:
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Scloud Timer backup() - Fail deletePreviousBackupFile: ";
        r11 = r11.append(r12);
        r11 = r11.append(r3);
        r11 = r11.toString();
        com.sec.android.app.clockpackage.common.util.Log.secE(r10, r11);
        r10 = 0;
        r0 = r19;
        r0.complete(r10);
        goto L_0x0020;
    L_0x0078:
        r10 = new com.sec.android.app.clockpackage.backuprestore.util.ClockDataConvertToXML;
        r11 = "BNR_SCLOUD_TIMER";
        r12 = -1;
        r13 = 3;
        r10.<init>(r6, r11, r12, r13);
        r0 = r16;
        r0.mXmlConverterTimer = r10;
        r0 = r16;
        r10 = r0.mXmlConverterTimer;
        r9 = r10.backupData(r4);
        r2 = r3.exists();
        r7 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r10 = r18.getFileDescriptor();	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r7.<init>(r10);	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r12 = 0;
        if (r9 != 0) goto L_0x00dd;
    L_0x009d:
        if (r2 == 0) goto L_0x00dd;
    L_0x009f:
        r10 = r3.getPath();	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r14 = r3.length();	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r11 = new com.sec.android.app.clockpackage.backuprestore.receiver.ClockBackupRestoreReceiver$1;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r0 = r16;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r1 = r19;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r11.<init>(r1);	 Catch:{ all -> 0x013f, all -> 0x00ed }
        com.samsung.android.scloud.oem.lib.FileTool.writeToFile(r10, r14, r7, r11);	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r11 = "Scloud Timer backup()  - success";	 Catch:{ all -> 0x013f, all -> 0x00ed }
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r10 = 1;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r0 = r19;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r0.complete(r10);	 Catch:{ all -> 0x013f, all -> 0x00ed }
    L_0x00c0:
        if (r7 == 0) goto L_0x00c7;
    L_0x00c2:
        if (r12 == 0) goto L_0x0132;
    L_0x00c4:
        r7.close();	 Catch:{ Throwable -> 0x0117 }
    L_0x00c7:
        r0 = r16;
        r10 = r0.mXmlConverterTimer;
        if (r10 == 0) goto L_0x00d4;
    L_0x00cd:
        r0 = r16;
        r10 = r0.mXmlConverterTimer;
        r10.deleteData(r8);
    L_0x00d4:
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";
        r11 = "Scloud Timer backup()  - end";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        goto L_0x0020;
    L_0x00dd:
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r11 = "Scloud Timer backup()  - Fail transfer timer preset backup data to SCloud";	 Catch:{ all -> 0x013f, all -> 0x00ed }
        com.sec.android.app.clockpackage.common.util.Log.m42e(r10, r11);	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r10 = 0;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r0 = r19;	 Catch:{ all -> 0x013f, all -> 0x00ed }
        r0.complete(r10);	 Catch:{ all -> 0x013f, all -> 0x00ed }
        goto L_0x00c0;
    L_0x00eb:
        r10 = move-exception;
        throw r10;	 Catch:{ all -> 0x013f, all -> 0x00ed }
    L_0x00ed:
        r11 = move-exception;
        r12 = r10;
    L_0x00ef:
        if (r7 == 0) goto L_0x00f6;
    L_0x00f1:
        if (r12 == 0) goto L_0x013b;
    L_0x00f3:
        r7.close();	 Catch:{ Throwable -> 0x0136 }
    L_0x00f6:
        throw r11;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
    L_0x00f7:
        r5 = move-exception;
        r5.printStackTrace();	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r10 = 0;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r0 = r19;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r0.complete(r10);	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r0 = r16;
        r10 = r0.mXmlConverterTimer;
        if (r10 == 0) goto L_0x010e;
    L_0x0107:
        r0 = r16;
        r10 = r0.mXmlConverterTimer;
        r10.deleteData(r8);
    L_0x010e:
        r10 = "BNR_CLOCK_ClockBackupRestoreReceiver";
        r11 = "Scloud Timer backup()  - end";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        goto L_0x0020;
    L_0x0117:
        r10 = move-exception;
        r12.addSuppressed(r10);	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        goto L_0x00c7;
    L_0x011c:
        r10 = move-exception;
        r0 = r16;
        r11 = r0.mXmlConverterTimer;
        if (r11 == 0) goto L_0x012a;
    L_0x0123:
        r0 = r16;
        r11 = r0.mXmlConverterTimer;
        r11.deleteData(r8);
    L_0x012a:
        r11 = "BNR_CLOCK_ClockBackupRestoreReceiver";
        r12 = "Scloud Timer backup()  - end";
        com.sec.android.app.clockpackage.common.util.Log.secD(r11, r12);
        throw r10;
    L_0x0132:
        r7.close();	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        goto L_0x00c7;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
    L_0x0136:
        r10 = move-exception;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        r12.addSuppressed(r10);	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        goto L_0x00f6;	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
    L_0x013b:
        r7.close();	 Catch:{ Exception -> 0x00f7, all -> 0x011c }
        goto L_0x00f6;
    L_0x013f:
        r10 = move-exception;
        r11 = r10;
        goto L_0x00ef;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.backuprestore.receiver.ClockBackupRestoreReceiver.backup(android.content.Context, android.os.ParcelFileDescriptor, com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient$QuickBackupListener):void");
    }

    protected boolean isBackup(String action) {
        if (!"com.sec.android.intent.action.REQUEST_BACKUP_WORLDCLOCK".equals(action)) {
            return false;
        }
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "BACKUP_WROLDCLOCK_TIMER!!!!!");
        return true;
    }

    protected boolean isRestore(String action) {
        if ("com.sec.android.intent.action.REQUEST_RESTORE_WORLDCLOCK".equals(action)) {
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Worldclock.RESTORE_WROLDCLOCK_TIMER !!!!!");
            return true;
        } else if (!"com.sec.android.intent.action.REQUEST_RESTORE_WORLDCLOCK_V2".equals(action)) {
            return false;
        } else {
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Worldclock.RESTORE_WROLDCLOCK_V2 !!!!!");
            this.mRestoreWorldclockV2 = true;
            return true;
        }
    }

    protected int getFileLength(String filePath) {
        return ((int) new File(filePath + "/worldclock.exml").length()) + ((int) new File(filePath + "/timer.exml").length());
    }

    protected String getResponseBackup() {
        return "com.samsung.android.intent.action.RESPONSE_BACKUP_WORLDCLOCK";
    }

    protected String getResponseRestore() {
        return this.mRestoreWorldclockV2 ? "com.samsung.android.intent.action.RESPONSE_RESTORE_WORLDCLOCK_V2" : "com.samsung.android.intent.action.RESPONSE_RESTORE_WORLDCLOCK";
    }

    protected boolean isEmpty() {
        boolean result = WorldclockDBManager.getDBCursorCnt(this.mContext) == 0 && TimerPresetItem.getPresetCount(this.mContext) == 0;
        if (result) {
            Log.m42e("BNR_CLOCK_ClockBackupRestoreReceiver", "worldclock & timer count == 0");
        }
        return result;
    }

    protected boolean isMaxCount() {
        boolean result = true;
        int worldclockCount = WorldclockDBManager.getDBCursorCnt(this.mContext);
        int timerCount = TimerPresetItem.getPresetCount(this.mContext);
        if (this.mRestoreWorldclockV2) {
            if (worldclockCount < 20) {
                result = false;
            }
        } else if (worldclockCount < 20 || timerCount < Timer.PRESET_MAX_COUNT) {
            result = false;
        }
        if (result) {
            Log.m42e("BNR_CLOCK_ClockBackupRestoreReceiver", "Count is MAX. worldclockCount : " + worldclockCount + " timerCount : " + timerCount);
        }
        return result;
    }

    protected int backupData(String filePath, String key, int securityLevel) {
        int resultWorldclock = 0;
        int resultTimer = 0;
        if (WorldclockDBManager.getDBCursorCnt(this.mContext) > 0) {
            String dbPath = this.mContext.getDatabasePath("worldclock.db").getPath();
            this.mXmlConverterWorldclock = new ClockDataConvertToXML(filePath, key, securityLevel, 1);
            resultWorldclock = this.mXmlConverterWorldclock.backupData(dbPath);
        }
        if (TimerPresetItem.getPresetCount(this.mContext) > 0) {
            resultTimer = new ClockDataConvertToXML(filePath, key, securityLevel, 3).backupData(this.mContext.getDatabasePath("timer.db").getPath());
        }
        if (resultWorldclock == 0 || resultTimer == 0) {
            return 0;
        }
        return 1;
    }

    protected int restoreData(String filePath, String saveKey, int securityLevel) {
        int resultWorldclock = worldclockRestoreFromXml(filePath, saveKey, securityLevel);
        int result = resultWorldclock;
        if (this.mRestoreWorldclockV2) {
            return result;
        }
        int resultTimer = timerRestoreFromXml(filePath, saveKey, securityLevel, 0);
        if (resultWorldclock == 0 || resultTimer == 0) {
            return 0;
        }
        return 1;
    }

    protected void deleteXmlData(String filePath) {
        if (this.mXmlConverterWorldclock != null) {
            this.mXmlConverterWorldclock.deleteData(filePath);
        }
        if (this.mXmlConverterTimer != null) {
            this.mXmlConverterTimer.deleteData(filePath);
        }
    }

    public void restore(Context context, ParcelFileDescriptor file, QuickBackupListener listener) {
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud Timer restore() - start");
        this.mContext = context;
        File restoreFile = new File(context.getFilesDir() + "/timer.exml");
        if (restoreFile.exists() && !restoreFile.delete()) {
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud Timer restore() - Fail deletePreviousRestoreFile: " + restoreFile);
            listener.complete(false);
        } else if (getRestoreData(restoreFile, file, listener)) {
            doRestoreTimer(context, listener);
        }
    }

    private void doRestoreTimer(Context context, QuickBackupListener listener) {
        sInsertedCount = 0;
        sInsertedRowIds = new ArrayList();
        int result = timerRestoreFromXml(context.getFilesDir().getAbsolutePath(), "BNR_SCLOUD_TIMER", -1, 1);
        String[] insertedRowIds = ClockUtils.toStringArray(sInsertedRowIds);
        String keys = ClockUtils.getWhereKey(insertedRowIds);
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", keys);
        SQLiteDatabase db = getTimerDb();
        if (result == 0) {
            if (sInsertedCount > 0) {
                db.delete("timerlife", "id < " + insertedRowIds[0], null);
            }
            if (sInsertedRowIds != null && sInsertedCount == 0) {
                db.execSQL("delete from timerlife");
            }
            listener.complete(true);
        } else {
            if (sInsertedCount > 0) {
                db.delete("timerlife", "id IN " + keys, null);
            }
            listener.complete(false);
        }
        db.close();
        BackupRestoreUtils.sendTimerPresetChangedIntent(context);
        sInsertedCount = 0;
        if (sInsertedRowIds != null) {
            sInsertedRowIds.clear();
        }
        Log.m41d("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud Timer restore() - end / result = " + result);
    }

    private boolean getRestoreData(File restoreFile, ParcelFileDescriptor file, final QuickBackupListener listener) {
        try {
            Throwable th;
            InputStream fis = new FileInputStream(file.getFileDescriptor());
            Throwable th2 = null;
            try {
                if (restoreFile.createNewFile()) {
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud Timer restore() / restoreFile file exit =" + restoreFile.exists());
                    FileTool.writeToFile(fis, file.getStatSize(), restoreFile.getAbsolutePath(), new PDMProgressListener() {
                        public void transferred(long now, long total) {
                            listener.onProgress(now, total);
                        }
                    });
                    if (fis != null) {
                        if (th2 != null) {
                            try {
                                fis.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            fis.close();
                        }
                    }
                    return true;
                }
                Log.secE("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud Timer restore() - Fail createRestoreNewFile: " + restoreFile);
                listener.complete(false);
                if (fis == null) {
                    return false;
                }
                if (th2 != null) {
                    try {
                        fis.close();
                        return false;
                    } catch (Throwable th32) {
                        th2.addSuppressed(th32);
                        return false;
                    }
                }
                fis.close();
                return false;
            } catch (Throwable th4) {
                th = th4;
                th2 = th32;
            }
            if (fis != null) {
                if (th2 != null) {
                    try {
                        fis.close();
                    } catch (Throwable th322) {
                        th2.addSuppressed(th322);
                    }
                } else {
                    fis.close();
                }
            }
            throw th;
            throw th;
        } catch (Exception e) {
            e.printStackTrace();
            listener.complete(false);
            return false;
        }
    }

    private SQLiteDatabase getTimerDb() {
        String dbName = this.mContext.getDatabasePath("timer.db").getPath();
        if (this.mContext.getDatabasePath(dbName).exists()) {
            return SQLiteDatabase.openDatabase(dbName, null, 0);
        }
        Log.m41d("BNR_CLOCK_ClockBackupRestoreReceiver", "restore() timer.db dbFile doesn't exist");
        SQLiteDatabase db = this.mContext.openOrCreateDatabase(dbName, 0, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timerlife (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,timername TEXT NOT NULL,timerimagename TEXT NOT NULL,time TEXT NOT NULL,timerorder INTEGER NOT NULL)");
        return db;
    }

    public boolean isSupportBackup(Context context) {
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud isSupportBackup()");
        return true;
    }

    public boolean isEnableBackup(Context context) {
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud isEnableBackup()");
        return true;
    }

    public String getLabel(Context context) {
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud getLabel()");
        return context.getResources().getString(R.string.timer);
    }

    public String getDescription(Context context) {
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Scloud getDescription()");
        return context.getResources().getString(R.string.timer);
    }

    private int RestoreFromXml(boolean isTimer, String fullPath, String saveKey, int securityLevel, int fromWhich) {
        Exception e;
        Throwable th;
        File file = new File(fullPath);
        ClockDataEncryption clockDataEncryption = new ClockDataEncryption();
        InputStream inputStream = null;
        InputStream xml = null;
        try {
            InputStream fis = new FileInputStream(file);
            try {
                xml = clockDataEncryption.decryptStream(fis, saveKey, securityLevel);
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                if (xml == null) {
                    parser.setInput(fis, "utf-8");
                } else {
                    parser.setInput(xml, "utf-8");
                }
                if (isTimer) {
                    timerRestoreXmlParser(parser, fromWhich);
                } else {
                    worldclockRestoreXmlParser(parser);
                }
                closeInputStream(fis);
                closeInputStream(xml);
                inputStream = fis;
                return 0;
            } catch (Exception e2) {
                e = e2;
                inputStream = fis;
                try {
                    Log.secE("BNR_CLOCK_ClockBackupRestoreReceiver", "Exception : " + e.toString());
                    closeInputStream(inputStream);
                    closeInputStream(xml);
                    return 1;
                } catch (Throwable th2) {
                    th = th2;
                    closeInputStream(inputStream);
                    closeInputStream(xml);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = fis;
                closeInputStream(inputStream);
                closeInputStream(xml);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            Log.secE("BNR_CLOCK_ClockBackupRestoreReceiver", "Exception : " + e.toString());
            closeInputStream(inputStream);
            closeInputStream(xml);
            return 1;
        }
    }

    private static void closeInputStream(InputStream inputObj) {
        if (inputObj != null) {
            try {
                inputObj.close();
            } catch (IOException e) {
                Log.secE("BNR_CLOCK_ClockBackupRestoreReceiver", "fail : close Input stream");
            }
        }
    }

    public int timerRestoreFromXml(String filePath, String saveKey, int securityLevel, int fromWhich) {
        String fullPath = filePath + "/timer.exml";
        Log.m41d("BNR_CLOCK_ClockBackupRestoreReceiver", "timerRestoreFromXML start fullPath !!!!!!!!!!!!!!!!!!!!!!" + fullPath);
        return RestoreFromXml(true, fullPath, saveKey, securityLevel, fromWhich);
    }

    private long addPresetFromXml(String name, String imageName, String time, int order, int fromWhich) {
        SQLiteDatabase db;
        Log.m41d("BNR_CLOCK_ClockBackupRestoreReceiver", "addPresetFromXml() name : " + name + " time : " + time);
        String path = this.mContext.getDatabasePath("timer.db").getPath();
        if (this.mContext.getDatabasePath(path).exists()) {
            db = SQLiteDatabase.openDatabase(path, null, 0);
        } else {
            Log.m41d("BNR_CLOCK_ClockBackupRestoreReceiver", "dbFile doesn't exist");
            db = this.mContext.openOrCreateDatabase(path, 0, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS timerlife (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,timername TEXT NOT NULL,timerimagename TEXT NOT NULL,time TEXT NOT NULL,timerorder INTEGER NOT NULL)");
        }
        db.beginTransaction();
        long transactionResult = 0;
        if (ClockUtils.isEnableString(time)) {
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Start adding timer preset");
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "timername=" + name + " " + "time" + "=" + time + " " + "timerorder" + "=" + order);
            Cursor cursor = null;
            boolean proceed = false;
            if (fromWhich == 1) {
                proceed = true;
                try {
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "BNR_SCLOUD : ignore duplicated item");
                } catch (SQLException e) {
                    Log.secE("BNR_CLOCK_ClockBackupRestoreReceiver", "addDefaultPreset SQLException");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    db.endTransaction();
                }
            } else {
                cursor = db.query("timerlife", null, "timername = '" + name.replace("'", "''") + "' AND " + "time" + " = '" + time + "'", null, null, null, null);
                if (cursor != null && cursor.getCount() <= 0) {
                    Log.m42e("BNR_CLOCK_ClockBackupRestoreReceiver", "BNR_SMART_SWITCH : After checking duplicated item, there is no item would be duplicated");
                    proceed = true;
                }
            }
            if (proceed) {
                TimerPresetItem item = new TimerPresetItem();
                item.setName(name);
                item.setImage(imageName);
                item.setTime(time);
                item.setOrder(order);
                transactionResult = db.insert("timerlife", null, TimerPresetItem.getContentValues(item));
                db.setTransactionSuccessful();
            }
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
            Log.m41d("BNR_CLOCK_ClockBackupRestoreReceiver", "End adding timer preset. transactionResult : " + transactionResult);
            db.close();
        }
        return transactionResult;
    }

    private void timerRestoreXmlParserForItem(int fromWhich, String name, String imageName, String time) throws IOException {
        int presetCount;
        if (fromWhich == 1) {
            presetCount = sInsertedCount;
        } else {
            presetCount = TimerPresetItem.getPresetCount(this.mContext);
            if (presetCount >= Timer.PRESET_MAX_COUNT) {
                Log.m42e("BNR_CLOCK_ClockBackupRestoreReceiver", "already MAX COUNT");
                throw new IOException();
            }
        }
        if (sInsertedCount < Timer.PRESET_MAX_COUNT) {
            long transactionResult = addPresetFromXml(name, imageName, time, presetCount, fromWhich);
            if (transactionResult > 0) {
                this.mContext.getContentResolver().notifyChange(TimerProvider.CONTENT_URI, null);
                if (fromWhich == 1) {
                    if (sInsertedRowIds != null) {
                        sInsertedRowIds.add(Long.toString(transactionResult));
                    }
                    sInsertedCount++;
                    return;
                }
                BackupRestoreUtils.sendTimerPresetChangedIntent(this.mContext);
                return;
            }
            Log.m42e("BNR_CLOCK_ClockBackupRestoreReceiver", "duplicated or error");
        }
    }

    private void timerRestoreXmlParser(XmlPullParser parser, int fromWhich) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        boolean bName = false;
        boolean bImageName = false;
        boolean bTime = false;
        String name = "";
        String imageName = "";
        String time = "";
        while (eventType != 1) {
            switch (eventType) {
                case 0:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "START_DOC !!!!!!!!!!!!!!!!!!!!!!");
                    break;
                case 2:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "START_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getName());
                    if (!parser.getName().equals("timername")) {
                        if (!parser.getName().equals("timerimagename")) {
                            if (!parser.getName().equals("time")) {
                                break;
                            }
                            bTime = true;
                            break;
                        }
                        bImageName = true;
                        break;
                    }
                    bName = true;
                    break;
                case 3:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "END_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getName());
                    if (parser.getName().equals("timername")) {
                        bName = false;
                    } else if (parser.getName().equals("timerimagename")) {
                        bImageName = false;
                    } else if (parser.getName().equals("time")) {
                        bTime = false;
                    }
                    if (!parser.getName().equals("item")) {
                        break;
                    }
                    timerRestoreXmlParserForItem(fromWhich, name, imageName, time);
                    name = "";
                    imageName = "";
                    time = "";
                    break;
                case 4:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "TEXT_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getText());
                    if (!bName) {
                        if (!bImageName) {
                            if (bTime && parser.getText() != null) {
                                time = parser.getText();
                                break;
                            }
                        } else if (parser.getText() == null) {
                            break;
                        } else {
                            imageName = parser.getText();
                            break;
                        }
                    } else if (parser.getText() == null) {
                        break;
                    } else {
                        name = parser.getText();
                        break;
                    }
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    private int worldclockRestoreFromXml(String filePath, String saveKey, int securityLevel) {
        String xmlPath = "/worldclock.exml";
        String fullPath = filePath + "/worldclock.exml";
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "fullPath !!!!!!!!!!!!!!!!!!!!!!" + fullPath);
        if (!CityManager.sIsCityManagerLoad) {
            CityManager.initCity(this.mContext);
        }
        if (CityManager.sCitiesByRawOffsetEn == null) {
            CityManager.loadCitiesEn(this.mContext);
        }
        return RestoreFromXml(false, fullPath, saveKey, securityLevel, 0);
    }

    private void worldclockRestoreXmlParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        boolean isHomezone = false;
        boolean isCityNameEng = false;
        int cityHomezone = -1;
        String cityNameEng = "";
        while (eventType != 1) {
            switch (eventType) {
                case 0:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "START_DOC !!!!!!!!!!!!!!!!!!!!!!");
                    break;
                case 2:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "START_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getName());
                    if (!parser.getName().equals("homezone")) {
                        if (!parser.getName().equals("city")) {
                            break;
                        }
                        isCityNameEng = true;
                        break;
                    }
                    isHomezone = true;
                    break;
                case 3:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "END_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getName());
                    if (parser.getName().equals("homezone")) {
                        isHomezone = false;
                    } else if (parser.getName().equals("city")) {
                        isCityNameEng = false;
                    }
                    if (parser.getName().equals("item")) {
                        if (WorldclockDBManager.getDBCursorCnt(this.mContext) != 20) {
                            if (addCityFromXml(cityNameEng, cityHomezone) > 0) {
                                this.mContext.getContentResolver().notifyChange(Worldclock.DATA_URI, null);
                                BackupRestoreUtils.sendWorldclockChangedIntent(this.mContext);
                            }
                            cityHomezone = -1;
                            cityNameEng = "";
                            break;
                        }
                        throw new IOException();
                    }
                    continue;
                case 4:
                    Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "TEXT_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getText());
                    if (!isHomezone) {
                        if (isCityNameEng && parser.getText() != null) {
                            cityNameEng = parser.getText();
                            break;
                        }
                    } else if (parser.getText() == null) {
                        break;
                    } else {
                        cityHomezone = Integer.parseInt(parser.getText());
                        break;
                    }
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    private long addCityFromXml(String cityName, int uniqueId) {
        SQLiteDatabase db;
        String path = this.mContext.getDatabasePath("worldclock.db").getPath();
        if (this.mContext.getDatabasePath(path).exists()) {
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "addCityFromXml() dbFile.exists()");
            db = SQLiteDatabase.openDatabase(path, null, 0);
        } else {
            db = this.mContext.openOrCreateDatabase(path, 0, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS worldclock (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, city TEXT NOT NULL, gmt INTEGER NOT NULL, dst INTEGER DEFAULT 0, homezone INTEGER NOT NULL DEFAULT 0, pointX INTEGER NOT NULL DEFAULT 0, pointY INTEGER NOT NULL DEFAULT 0)");
        }
        db.beginTransaction();
        long transactionResult = 0;
        if (uniqueId == -1 && cityName != null && cityName.length() > 0) {
            uniqueId = CityManager.findUniqueIdByIOsEngCityName(cityName).intValue();
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "findUniqueIdByIOsEngCityName => result uniqueId : " + uniqueId);
        }
        Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "Start adding world clock");
        Cursor cursor = null;
        try {
            cursor = db.query("worldclock", null, "homezone = " + uniqueId, null, null, null, null);
            ContentValues contentValues = WorldclockDBManager.getContentValues(uniqueId);
            if (cursor.getCount() > 0 || uniqueId == -1 || contentValues == null) {
                Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "duplicated/unsupported city. skip to add");
            } else {
                Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "There is no item would be duplicated");
                transactionResult = db.insert("worldclock", null, contentValues);
                db.setTransactionSuccessful();
            }
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
            Log.secD("BNR_CLOCK_ClockBackupRestoreReceiver", "End adding world clock. transactionResult : " + transactionResult);
            db.close();
            return transactionResult;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
        }
    }
}
