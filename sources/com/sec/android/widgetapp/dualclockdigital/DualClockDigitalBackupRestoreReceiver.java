package com.sec.android.widgetapp.dualclockdigital;

import android.content.Context;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import com.samsung.android.scloud.oem.lib.FileTool;
import com.samsung.android.scloud.oem.lib.FileTool.PDMProgressListener;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient;
import com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient.QuickBackupListener;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.backuprestore.receiver.BackupRestoreReceiver;
import com.sec.android.app.clockpackage.backuprestore.util.ClockDataConvertToXML;
import com.sec.android.app.clockpackage.backuprestore.util.ClockDataEncryption;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class DualClockDigitalBackupRestoreReceiver extends BackupRestoreReceiver implements ISCloudQBNRClient {
    private ClockDataConvertToXML mXmlConverter;

    /* renamed from: com.sec.android.widgetapp.dualclockdigital.DualClockDigitalBackupRestoreReceiver$1 */
    class C09141 implements PDMProgressListener {
        final /* synthetic */ QuickBackupListener val$listener;

        C09141(QuickBackupListener quickBackupListener) {
            this.val$listener = quickBackupListener;
        }

        public void transferred(long now, long total) {
            this.val$listener.onProgress(now, total);
        }
    }

    public void backup(android.content.Context r17, android.os.ParcelFileDescriptor r18, com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient.QuickBackupListener r19) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x00f3 in list []
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
        r10 = "CLOCK_DC_SCLOUD_DualClockDigitalBackupRestoreReceiver";
        r11 = "Scloud DualclockWidget backup() - start";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        r10 = "dualclock";
        r0 = r17;
        r10 = r0.getDatabasePath(r10);
        r4 = r10.getPath();
        r10 = r17.getFilesDir();
        r6 = r10.getAbsolutePath();
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10 = r10.append(r6);
        r11 = "/dualclock.exml";
        r10 = r10.append(r11);
        r8 = r10.toString();
        r3 = new java.io.File;
        r3.<init>(r8);
        r2 = r3.exists();
        if (r2 == 0) goto L_0x005e;
    L_0x0039:
        r10 = r3.delete();
        if (r10 != 0) goto L_0x005e;
    L_0x003f:
        r10 = "BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Scloud DualclockWidget backup() - Fail deletePreviousBackupFile: ";
        r11 = r11.append(r12);
        r11 = r11.append(r3);
        r11 = r11.toString();
        com.sec.android.app.clockpackage.common.util.Log.secE(r10, r11);
        r10 = 0;
        r0 = r19;
        r0.complete(r10);
    L_0x005d:
        return;
    L_0x005e:
        r10 = new com.sec.android.app.clockpackage.backuprestore.util.ClockDataConvertToXML;
        r11 = "BNR_SCLOUD_DUALCLOCK";
        r12 = -1;
        r13 = 2;
        r10.<init>(r6, r11, r12, r13);
        r0 = r16;
        r0.mXmlConverter = r10;
        r0 = r16;
        r10 = r0.mXmlConverter;
        r9 = r10.backupData(r4);
        r2 = r3.exists();
        r7 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r10 = r18.getFileDescriptor();	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r7.<init>(r10);	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r12 = 0;
        if (r9 != 0) goto L_0x00c2;
    L_0x0083:
        if (r2 == 0) goto L_0x00c2;
    L_0x0085:
        r10 = r3.getPath();	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r14 = r3.length();	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r11 = new com.sec.android.widgetapp.dualclockdigital.DualClockDigitalBackupRestoreReceiver$1;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r0 = r16;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r1 = r19;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r11.<init>(r1);	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        com.samsung.android.scloud.oem.lib.FileTool.writeToFile(r10, r14, r7, r11);	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r10 = "BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver";	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r11 = "Scloud DualclockWidget backup()  - success";	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r10 = 1;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r0 = r19;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r0.complete(r10);	 Catch:{ all -> 0x0124, all -> 0x00d2 }
    L_0x00a6:
        if (r7 == 0) goto L_0x00ad;
    L_0x00a8:
        if (r12 == 0) goto L_0x0117;
    L_0x00aa:
        r7.close();	 Catch:{ Throwable -> 0x00fc }
    L_0x00ad:
        r0 = r16;
        r10 = r0.mXmlConverter;
        if (r10 == 0) goto L_0x00ba;
    L_0x00b3:
        r0 = r16;
        r10 = r0.mXmlConverter;
        r10.deleteData(r8);
    L_0x00ba:
        r10 = "CLOCK_DC_SCLOUD_DualClockDigitalBackupRestoreReceiver";
        r11 = "Scloud DualclockWidget backup()  - end";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        goto L_0x005d;
    L_0x00c2:
        r10 = "BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver";	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r11 = "Scloud DualclockWidget backup()  - Fail transfer dualclock widget backup data to SCloud";	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        com.sec.android.app.clockpackage.common.util.Log.secE(r10, r11);	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r10 = 0;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r0 = r19;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        r0.complete(r10);	 Catch:{ all -> 0x0124, all -> 0x00d2 }
        goto L_0x00a6;
    L_0x00d0:
        r10 = move-exception;
        throw r10;	 Catch:{ all -> 0x0124, all -> 0x00d2 }
    L_0x00d2:
        r11 = move-exception;
        r12 = r10;
    L_0x00d4:
        if (r7 == 0) goto L_0x00db;
    L_0x00d6:
        if (r12 == 0) goto L_0x0120;
    L_0x00d8:
        r7.close();	 Catch:{ Throwable -> 0x011b }
    L_0x00db:
        throw r11;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
    L_0x00dc:
        r5 = move-exception;
        r5.printStackTrace();	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r10 = 0;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r0 = r19;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r0.complete(r10);	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r0 = r16;
        r10 = r0.mXmlConverter;
        if (r10 == 0) goto L_0x00f3;
    L_0x00ec:
        r0 = r16;
        r10 = r0.mXmlConverter;
        r10.deleteData(r8);
    L_0x00f3:
        r10 = "CLOCK_DC_SCLOUD_DualClockDigitalBackupRestoreReceiver";
        r11 = "Scloud DualclockWidget backup()  - end";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        goto L_0x005d;
    L_0x00fc:
        r10 = move-exception;
        r12.addSuppressed(r10);	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        goto L_0x00ad;
    L_0x0101:
        r10 = move-exception;
        r0 = r16;
        r11 = r0.mXmlConverter;
        if (r11 == 0) goto L_0x010f;
    L_0x0108:
        r0 = r16;
        r11 = r0.mXmlConverter;
        r11.deleteData(r8);
    L_0x010f:
        r11 = "CLOCK_DC_SCLOUD_DualClockDigitalBackupRestoreReceiver";
        r12 = "Scloud DualclockWidget backup()  - end";
        com.sec.android.app.clockpackage.common.util.Log.secD(r11, r12);
        throw r10;
    L_0x0117:
        r7.close();	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        goto L_0x00ad;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
    L_0x011b:
        r10 = move-exception;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        r12.addSuppressed(r10);	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        goto L_0x00db;	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
    L_0x0120:
        r7.close();	 Catch:{ Exception -> 0x00dc, all -> 0x0101 }
        goto L_0x00db;
    L_0x0124:
        r10 = move-exception;
        r11 = r10;
        goto L_0x00d4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.widgetapp.dualclockdigital.DualClockDigitalBackupRestoreReceiver.backup(android.content.Context, android.os.ParcelFileDescriptor, com.samsung.android.scloud.oem.lib.qbnr.ISCloudQBNRClient$QuickBackupListener):void");
    }

    protected boolean isBackup(String action) {
        if (!"com.samsung.android.intent.action.REQUEST_BACKUP_DUALCLOCK_WIDGET".equals(action)) {
            return false;
        }
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "REQUEST_BACKUP_DUALCLOCK_WIDGET!!!!!");
        return true;
    }

    protected boolean isRestore(String action) {
        if (!"com.samsung.android.intent.action.REQUEST_RESTORE_DUALCLOCK_WIDGET".equals(action)) {
            return false;
        }
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "REQUEST_RESTORE_DUALCLOCK_WIDGET!!!!!");
        return true;
    }

    protected int getFileLength(String filePath) {
        return (int) new File(filePath + "/dualclock.exml").length();
    }

    protected String getResponseBackup() {
        return "com.samsung.android.intent.action.RESPONSE_BACKUP_DUALCLOCK_WIDGET";
    }

    protected String getResponseRestore() {
        return "com.samsung.android.intent.action.RESPONSE_RESTORE_DUALCLOCK_WIDGET";
    }

    protected boolean isEmpty() {
        boolean result = getDualClockCount(this.mContext) == 0;
        if (result) {
            Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "dual clock count == 0");
        }
        return result;
    }

    protected boolean isMaxCount() {
        return false;
    }

    protected int backupData(String filePath, String key, int securityLevel) {
        String dbPath = this.mContext.getDatabasePath("dualclock").getPath();
        this.mXmlConverter = new ClockDataConvertToXML(filePath, key, securityLevel, 2);
        return this.mXmlConverter.backupData(dbPath);
    }

    protected int restoreData(String filePath, String saveKey, int securityLevel) {
        return dualClockRestoreFromXml(filePath, saveKey, securityLevel);
    }

    protected void deleteXmlData(String filePath) {
        if (this.mXmlConverter != null) {
            this.mXmlConverter.deleteData(filePath);
        }
    }

    private int getDualClockCount(Context context) {
        Cursor cursor = context.getContentResolver().query(DualClockDigital.DATA_URI, null, null, null, null);
        if (cursor == null) {
            return 0;
        }
        int itemCount = cursor.getCount();
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "getDualClockCount() itemCount : " + itemCount);
        cursor.close();
        return itemCount;
    }

    public void restore(Context context, ParcelFileDescriptor file, QuickBackupListener listener) {
        Log.secD("CLOCK_DC_SCLOUD_DualClockDigitalBackupRestoreReceiver", "Scloud Dualclock restore() - start");
        this.mContext = context;
        File restoreFile = new File(context.getFilesDir() + "/dualclock.exml");
        if (restoreFile.exists() && !restoreFile.delete()) {
            Log.secE("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud Dualclock restore() - Fail deletePreviousRestoreFile: " + restoreFile);
            listener.complete(false);
        } else if (getRestoreData(restoreFile, file, listener)) {
            doRestoreDualClock(context, listener);
        }
    }

    private void doRestoreDualClock(Context context, QuickBackupListener listener) {
        int result = dualClockRestoreFromXml(context.getFilesDir().getAbsolutePath(), "BNR_SCLOUD_DUALCLOCK", -1);
        if (result == 0) {
            listener.complete(true);
        } else {
            listener.complete(false);
        }
        Log.secD("CLOCK_DC_SCLOUD_DualClockDigitalBackupRestoreReceiver", "Scloud Dualclock restore() - end / result = " + result);
    }

    private boolean getRestoreData(File restoreFile, ParcelFileDescriptor file, final QuickBackupListener listener) {
        try {
            Throwable th;
            InputStream fis = new FileInputStream(file.getFileDescriptor());
            Throwable th2 = null;
            try {
                if (restoreFile.createNewFile()) {
                    Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud Dualclock restore() / restoreFile file exit =" + restoreFile.exists());
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
                Log.secE("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud Dualclock restore() - Fail createRestoreNewFile: " + restoreFile);
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

    public boolean isSupportBackup(Context context) {
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud isSupportBackup()");
        return true;
    }

    public boolean isEnableBackup(Context context) {
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud isEnableBackup()");
        return true;
    }

    public String getLabel(Context context) {
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud getLabel()");
        return context.getResources().getString(R.string.dual_clock);
    }

    public String getDescription(Context context) {
        Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Scloud getDescription()");
        return context.getResources().getString(R.string.dual_clock);
    }

    private int dualClockRestoreFromXml(String filePath, String saveKey, int securityLevel) {
        Exception e;
        Throwable th;
        String TAG = "BNR_CLOCK_DC_dualClockRestoreFromXml";
        ClockDataEncryption clockDataEncryption = new ClockDataEncryption();
        String fullPath = filePath + "/dualclock.exml";
        File file = new File(fullPath);
        Log.secD(TAG, "fullPath !!!!!!!!!!!!!!!!!!!!!!" + fullPath);
        InputStream fis = null;
        InputStream dualClockXml = null;
        try {
            InputStream fis2 = new FileInputStream(file);
            try {
                dualClockXml = clockDataEncryption.decryptStream(fis2, saveKey, securityLevel);
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                if (dualClockXml == null) {
                    parser.setInput(fis2, "utf-8");
                } else {
                    parser.setInput(dualClockXml, "utf-8");
                }
                dualClockRestoreXmlParser(parser);
                closeInputStream(fis2);
                closeInputStream(dualClockXml);
                fis = fis2;
                return 0;
            } catch (Exception e2) {
                e = e2;
                fis = fis2;
                try {
                    Log.secE(TAG, "Exception : " + e.toString());
                    closeInputStream(fis);
                    closeInputStream(dualClockXml);
                    return 1;
                } catch (Throwable th2) {
                    th = th2;
                    closeInputStream(fis);
                    closeInputStream(dualClockXml);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                closeInputStream(fis);
                closeInputStream(dualClockXml);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            Log.secE(TAG, "Exception : " + e.toString());
            closeInputStream(fis);
            closeInputStream(dualClockXml);
            return 1;
        }
    }

    private static void closeInputStream(InputStream inputObj) {
        if (inputObj != null) {
            try {
                inputObj.close();
            } catch (IOException e) {
                Log.secE("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "fail : close Input stream");
            }
        }
    }

    private void dualClockRestoreXmlParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        boolean bHomezone = false;
        boolean bUniqueID = false;
        boolean bWidgetID = false;
        String homezone = null;
        int uniqueID = -1;
        String widgetID = null;
        while (eventType != 1) {
            String name;
            Object obj;
            switch (eventType) {
                case 0:
                    Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "START_DOC !!!!!!!!!!!!!!!!!!!!!!");
                    break;
                case 2:
                    Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "START_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getName());
                    name = parser.getName();
                    obj = -1;
                    switch (name.hashCode()) {
                        case -485060341:
                            if (name.equals("homezone")) {
                                obj = null;
                                break;
                            }
                            break;
                        case -294459220:
                            if (name.equals("uniqueid")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 117714:
                            if (name.equals("wid")) {
                                obj = 2;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            bHomezone = true;
                            break;
                        case 1:
                            bUniqueID = true;
                            break;
                        case 2:
                            bWidgetID = true;
                            break;
                        default:
                            break;
                    }
                case 3:
                    Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "END_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getName());
                    name = parser.getName();
                    obj = -1;
                    switch (name.hashCode()) {
                        case -485060341:
                            if (name.equals("homezone")) {
                                obj = null;
                                break;
                            }
                            break;
                        case -294459220:
                            if (name.equals("uniqueid")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 117714:
                            if (name.equals("wid")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case 3242771:
                            if (name.equals("item")) {
                                obj = 3;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            bHomezone = false;
                            break;
                        case 1:
                            bUniqueID = false;
                            break;
                        case 2:
                            bWidgetID = false;
                            break;
                        case 3:
                            if (Integer.parseInt(homezone) > 0) {
                                String key = widgetID + "_" + homezone;
                                Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "Restored data : name = " + key + " value = " + uniqueID);
                                SharedManager.addRestoredData(this.mContext, key, uniqueID);
                                SharedManager.addRestoredTime(this.mContext);
                            }
                            homezone = null;
                            uniqueID = -1;
                            widgetID = null;
                            break;
                        default:
                            break;
                    }
                case 4:
                    Log.secD("BNR_CLOCK_DC_DualClockDigitalBackupRestoreReceiver", "TEXT_TAG !!!!!!!!!!!!!!!!!!!!!! - " + parser.getText());
                    if (!bHomezone) {
                        if (!bUniqueID) {
                            if (bWidgetID && parser.getText() != null) {
                                widgetID = parser.getText();
                                break;
                            }
                        } else if (parser.getText() == null) {
                            break;
                        } else {
                            uniqueID = Integer.parseInt(parser.getText());
                            break;
                        }
                    } else if (parser.getText() == null) {
                        break;
                    } else {
                        homezone = parser.getText();
                        break;
                    }
                default:
                    break;
            }
            eventType = parser.next();
        }
    }
}
