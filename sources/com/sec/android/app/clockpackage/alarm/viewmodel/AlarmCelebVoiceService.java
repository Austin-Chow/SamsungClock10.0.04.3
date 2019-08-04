package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import com.sec.android.app.clockpackage.alarm.model.AlarmCvConstants;
import com.sec.android.app.clockpackage.alarm.model.CelebVoiceItemsDbHelper;
import com.sec.android.app.clockpackage.alarm.model.CelebVoicePackagesDbHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice.Stub;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmCelebVoiceService extends Service {
    private Stub mAlarmCelebVoiceInterface = new C05901();
    private CelebVoiceItemsDbHelper mCelebVoiceItemsDbHelper;
    private CelebVoicePackagesDbHelper mCelebVoicePackagesDbHelper;
    private ContentPackageInfo mContentPackageInfo;
    private String mInstallRootDir;
    private String mInstallSubDir;
    private int mResult = 0;

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService$1 */
    class C05901 extends Stub {
        C05901() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void installContent(java.lang.String r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, android.net.Uri r28, com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback r29) throws android.os.RemoteException {
            /*
            r22 = this;
            r3 = 1;
            r2 = "AlarmCelebVoiceService";
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = "installContent info ";
            r4 = r4.append(r5);
            r0 = r23;
            r4 = r4.append(r0);
            r5 = ", ";
            r4 = r4.append(r5);
            r0 = r24;
            r4 = r4.append(r0);
            r5 = ", ";
            r4 = r4.append(r5);
            r0 = r25;
            r4 = r4.append(r0);
            r5 = ", ";
            r4 = r4.append(r5);
            r0 = r26;
            r4 = r4.append(r0);
            r5 = ", ";
            r4 = r4.append(r5);
            r0 = r27;
            r4 = r4.append(r0);
            r4 = r4.toString();
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);
            r2 = com.sec.android.app.clockpackage.common.util.ClockUtils.isEnableString(r23);
            if (r2 == 0) goto L_0x0073;
        L_0x0051:
            r2 = com.sec.android.app.clockpackage.common.util.ClockUtils.isEnableString(r24);
            if (r2 == 0) goto L_0x0073;
        L_0x0057:
            r2 = com.sec.android.app.clockpackage.common.util.ClockUtils.isEnableString(r25);
            if (r2 == 0) goto L_0x0073;
        L_0x005d:
            r2 = com.sec.android.app.clockpackage.common.util.ClockUtils.isEnableString(r26);
            if (r2 == 0) goto L_0x0073;
        L_0x0063:
            r2 = com.sec.android.app.clockpackage.common.util.ClockUtils.isEnableString(r27);
            if (r2 == 0) goto L_0x0073;
        L_0x0069:
            r2 = r28.toString();
            r2 = com.sec.android.app.clockpackage.common.util.ClockUtils.isEnableString(r2);
            if (r2 != 0) goto L_0x014b;
        L_0x0073:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = -25;
            r2.mResult = r4;
        L_0x007c:
            if (r29 == 0) goto L_0x008d;
        L_0x007e:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r2 = r2.mResult;
            r0 = r29;
            r1 = r27;
            r0.procedureResult(r1, r3, r2);
        L_0x008d:
            r2 = "AlarmCelebVoiceService";
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = "installContent mResult ";
            r4 = r4.append(r5);
            r0 = r22;
            r5 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r5 = r5.mResult;
            r4 = r4.append(r5);
            r4 = r4.toString();
            com.sec.android.app.clockpackage.common.util.Log.m41d(r2, r4);
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r2 = r2.mResult;
            if (r2 != 0) goto L_0x0142;
        L_0x00b7:
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r4 = "content://com.sec.android.app.clockpackage.celebvoice/#";
            r2 = r2.append(r4);
            r2 = r2.append(r3);
            r4 = "#";
            r2 = r2.append(r4);
            r0 = r25;
            r2 = r2.append(r0);
            r4 = "#";
            r2 = r2.append(r4);
            r0 = r27;
            r2 = r2.append(r0);
            r4 = "#";
            r2 = r2.append(r4);
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.mContentPackageInfo;
            r4 = r4.versionCode;
            r2 = r2.append(r4);
            r4 = "#";
            r2 = r2.append(r4);
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.mContentPackageInfo;
            r4 = r4.versionName;
            r2 = r2.append(r4);
            r2 = r2.toString();
            r19 = android.net.Uri.parse(r2);
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r2 = r2.getContentResolver();
            r4 = 0;
            r0 = r19;
            r2.notifyChange(r0, r4);
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.mContentPackageInfo;
            r6 = r4.versionCode;
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.mContentPackageInfo;
            r7 = r4.versionName;
            r4 = r25;
            r5 = r27;
            r2.sendProcessIntent(r3, r4, r5, r6, r7);
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent Success";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);
        L_0x0142:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = 0;
            r2.mContentPackageInfo = r4;
            return;
        L_0x014b:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = 0;
            r2.mResult = r4;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = com.sec.android.app.clockpackage.alarm.model.AlarmCvConstants.getUIDRootPath();
            r4 = r4.append(r5);
            r0 = r25;
            r4 = r4.append(r0);
            r5 = "/";
            r4 = r4.append(r5);
            r0 = r27;
            r4 = r4.append(r0);
            r4 = r4.toString();
            r2.mInstallRootDir = r4;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r0 = r25;
            r4 = r4.append(r0);
            r5 = "/";
            r4 = r4.append(r5);
            r0 = r27;
            r4 = r4.append(r0);
            r4 = r4.toString();
            r2.mInstallSubDir = r4;
            r12 = new java.io.File;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r2 = r2.mInstallRootDir;
            r12.<init>(r2);
            r2 = r12.exists();
            if (r2 == 0) goto L_0x0207;
        L_0x01b2:
            r3 = 3;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r25;
            r1 = r27;
            r4 = r4.deleteContentPackageFolder(r0, r1);
            r2.mResult = r4;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r25;
            r1 = r27;
            r2 = r2.isPackageInDataBase(r0, r1);
            if (r2 == 0) goto L_0x02bf;
        L_0x01d4:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r27;
            r2.deleteContentPackageDataBase(r0);
        L_0x01dd:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.getApplicationContext();
            r0 = r27;
            r1 = r25;
            r4 = com.sec.android.app.clockpackage.alarm.model.CelebVoiceItemsDbHelper.getInstance(r4, r0, r1);
            r2.mCelebVoiceItemsDbHelper = r4;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r2 = r2.mCelebVoiceItemsDbHelper;
            r2.close();
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = 0;
            r2.mCelebVoiceItemsDbHelper = r4;
        L_0x0207:
            r18 = r3;
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.mInstallSubDir;
            r2.createSubDirs(r4);
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r0 = r22;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;
            r4 = r4.mInstallRootDir;
            r2 = r2.append(r4);
            r4 = "/";
            r2 = r2.append(r4);
            r4 = "install.apk";
            r2 = r2.append(r4);
            r8 = r2.toString();
            r17 = new java.io.File;
            r0 = r17;
            r0.<init>(r8);
            r2 = r17.exists();
            if (r2 == 0) goto L_0x0249;
        L_0x0246:
            r17.delete();
        L_0x0249:
            r14 = 0;
            r16 = new android.os.ParcelFileDescriptor$AutoCloseInputStream;	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            r2 = r2.getContentResolver();	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            r4 = "r";
            r0 = r28;
            r2 = r2.openFileDescriptor(r0, r4);	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            r0 = r16;
            r0.<init>(r2);	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            r21 = 0;
            r10 = new java.io.BufferedInputStream;	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
            r0 = r16;
            r10.<init>(r0);	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
            r20 = 0;
            r2 = r17.createNewFile();	 Catch:{ Throwable -> 0x02ff, all -> 0x03a5 }
            if (r2 == 0) goto L_0x02ee;
        L_0x0272:
            r15 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x02ff, all -> 0x03a5 }
            r0 = r17;
            r15.<init>(r0);	 Catch:{ Throwable -> 0x02ff, all -> 0x03a5 }
            r2 = 10240; // 0x2800 float:1.4349E-41 double:5.059E-320;
            r9 = new byte[r2];	 Catch:{ Throwable -> 0x0289, all -> 0x03ab }
        L_0x027d:
            r11 = r10.read(r9);	 Catch:{ Throwable -> 0x0289, all -> 0x03ab }
            r2 = -1;
            if (r11 == r2) goto L_0x02c2;
        L_0x0284:
            r2 = 0;
            r15.write(r9, r2, r11);	 Catch:{ Throwable -> 0x0289, all -> 0x03ab }
            goto L_0x027d;
        L_0x0289:
            r2 = move-exception;
            r14 = r15;
        L_0x028b:
            throw r2;	 Catch:{ all -> 0x028c }
        L_0x028c:
            r4 = move-exception;
            r5 = r2;
        L_0x028e:
            if (r10 == 0) goto L_0x0295;
        L_0x0290:
            if (r5 == 0) goto L_0x0316;
        L_0x0292:
            r10.close();	 Catch:{ Throwable -> 0x0311, all -> 0x0308 }
        L_0x0295:
            throw r4;	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
        L_0x0296:
            r2 = move-exception;
            throw r2;	 Catch:{ all -> 0x0298 }
        L_0x0298:
            r4 = move-exception;
            r5 = r2;
        L_0x029a:
            if (r16 == 0) goto L_0x02a1;
        L_0x029c:
            if (r5 == 0) goto L_0x0367;
        L_0x029e:
            r16.close();	 Catch:{ Throwable -> 0x0357 }
        L_0x02a1:
            throw r4;	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
        L_0x02a2:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : install APK not found";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);	 Catch:{ all -> 0x035d }
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;	 Catch:{ all -> 0x035d }
            r4 = -23;
            r2.mResult = r4;	 Catch:{ all -> 0x035d }
            if (r14 == 0) goto L_0x02bb;
        L_0x02b5:
            r14.flush();	 Catch:{ Exception -> 0x0378 }
            r14.close();	 Catch:{ Exception -> 0x0378 }
        L_0x02bb:
            r3 = r18;
            goto L_0x007c;
        L_0x02bf:
            r3 = 1;
            goto L_0x01dd;
        L_0x02c2:
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;	 Catch:{ Throwable -> 0x0289, all -> 0x03ab }
            r3 = r23;
            r4 = r24;
            r5 = r25;
            r6 = r26;
            r7 = r27;
            r2.installContentPackage(r3, r4, r5, r6, r7, r8);	 Catch:{ Throwable -> 0x0289, all -> 0x03ab }
            r14 = r15;
        L_0x02d4:
            if (r10 == 0) goto L_0x02db;
        L_0x02d6:
            if (r20 == 0) goto L_0x030d;
        L_0x02d8:
            r10.close();	 Catch:{ Throwable -> 0x0301, all -> 0x0308 }
        L_0x02db:
            if (r16 == 0) goto L_0x02e2;
        L_0x02dd:
            if (r21 == 0) goto L_0x033f;
        L_0x02df:
            r16.close();	 Catch:{ Throwable -> 0x031b }
        L_0x02e2:
            if (r14 == 0) goto L_0x02ea;
        L_0x02e4:
            r14.flush();	 Catch:{ Exception -> 0x036c }
            r14.close();	 Catch:{ Exception -> 0x036c }
        L_0x02ea:
            r3 = r18;
            goto L_0x007c;
        L_0x02ee:
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : install APK creation failed";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);	 Catch:{ Throwable -> 0x02ff, all -> 0x03a5 }
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;	 Catch:{ Throwable -> 0x02ff, all -> 0x03a5 }
            r4 = -21;
            r2.mResult = r4;	 Catch:{ Throwable -> 0x02ff, all -> 0x03a5 }
            goto L_0x02d4;
        L_0x02ff:
            r2 = move-exception;
            goto L_0x028b;
        L_0x0301:
            r2 = move-exception;
            r0 = r20;
            r0.addSuppressed(r2);	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
            goto L_0x02db;
        L_0x0308:
            r2 = move-exception;
            r4 = r2;
            r5 = r21;
            goto L_0x029a;
        L_0x030d:
            r10.close();	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
            goto L_0x02db;
        L_0x0311:
            r2 = move-exception;
            r5.addSuppressed(r2);	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
            goto L_0x0295;
        L_0x0316:
            r10.close();	 Catch:{ Throwable -> 0x0296, all -> 0x0308 }
            goto L_0x0295;
        L_0x031b:
            r2 = move-exception;
            r0 = r21;
            r0.addSuppressed(r2);	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            goto L_0x02e2;
        L_0x0322:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : IOException";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);	 Catch:{ all -> 0x035d }
            r0 = r22;
            r2 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.this;	 Catch:{ all -> 0x035d }
            r4 = -22;
            r2.mResult = r4;	 Catch:{ all -> 0x035d }
            if (r14 == 0) goto L_0x033b;
        L_0x0335:
            r14.flush();	 Catch:{ Exception -> 0x0384 }
            r14.close();	 Catch:{ Exception -> 0x0384 }
        L_0x033b:
            r3 = r18;
            goto L_0x007c;
        L_0x033f:
            r16.close();	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            goto L_0x02e2;
        L_0x0343:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : Exception path1";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);	 Catch:{ all -> 0x035d }
            if (r14 == 0) goto L_0x0353;
        L_0x034d:
            r14.flush();	 Catch:{ Exception -> 0x0390 }
            r14.close();	 Catch:{ Exception -> 0x0390 }
        L_0x0353:
            r3 = r18;
            goto L_0x007c;
        L_0x0357:
            r2 = move-exception;
            r5.addSuppressed(r2);	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            goto L_0x02a1;
        L_0x035d:
            r2 = move-exception;
            if (r14 == 0) goto L_0x0366;
        L_0x0360:
            r14.flush();	 Catch:{ Exception -> 0x039c }
            r14.close();	 Catch:{ Exception -> 0x039c }
        L_0x0366:
            throw r2;
        L_0x0367:
            r16.close();	 Catch:{ FileNotFoundException -> 0x02a2, IOException -> 0x0322, Exception -> 0x0343 }
            goto L_0x02a1;
        L_0x036c:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : Exception path2";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);
            r3 = r18;
            goto L_0x007c;
        L_0x0378:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : Exception path2";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);
            r3 = r18;
            goto L_0x007c;
        L_0x0384:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : Exception path2";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);
            r3 = r18;
            goto L_0x007c;
        L_0x0390:
            r13 = move-exception;
            r2 = "AlarmCelebVoiceService";
            r4 = "installContent : Exception path2";
            com.sec.android.app.clockpackage.common.util.Log.secD(r2, r4);
            r3 = r18;
            goto L_0x007c;
        L_0x039c:
            r13 = move-exception;
            r4 = "AlarmCelebVoiceService";
            r5 = "installContent : Exception path2";
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r5);
            goto L_0x0366;
        L_0x03a5:
            r2 = move-exception;
            r4 = r2;
            r5 = r20;
            goto L_0x028e;
        L_0x03ab:
            r2 = move-exception;
            r4 = r2;
            r5 = r20;
            r14 = r15;
            goto L_0x028e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.1.installContent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, android.net.Uri, com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback):void");
        }

        public void deleteContent(String type, String packageName, IAlarmCelebVoiceCallback callback) throws RemoteException {
            Log.secD("AlarmCelebVoiceService", "deleteContent " + type + "," + packageName);
            AlarmCelebVoiceService.this.mResult = 0;
            if (!(packageName == null || type == null)) {
                AlarmCelebVoiceService.this.mResult = AlarmCelebVoiceService.this.deleteContentPackageFolder(type, packageName);
                if (AlarmCelebVoiceService.this.isPackageInDataBase(type, packageName)) {
                    AlarmCelebVoiceService.this.deleteContentPackageDataBase(packageName);
                }
            }
            if (callback != null) {
                callback.procedureResult(packageName, 2, AlarmCelebVoiceService.this.mResult);
            }
            AlarmCelebVoiceService.this.mCelebVoiceItemsDbHelper = CelebVoiceItemsDbHelper.getInstance(AlarmCelebVoiceService.this.getApplicationContext(), packageName, type);
            AlarmCelebVoiceService.this.mCelebVoiceItemsDbHelper.close();
            AlarmCelebVoiceService.this.mCelebVoiceItemsDbHelper = null;
            if (AlarmCelebVoiceService.this.mResult == 0) {
                AlarmCelebVoiceService.this.getContentResolver().notifyChange(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/#2#" + type + "#" + packageName), null);
                AlarmCelebVoiceService.this.sendProcessIntent(2, type, packageName, null, null);
            }
        }
    }

    private static class ContentPackageInfo {
        String attribute;
        String bitrate;
        String file;
        ArrayList<String> fileNameList;
        ArrayList<Double> fileSizeList;
        String khz;
        String packageName;
        String previewFile;
        ArrayList<String> repStatic;
        String stereo;
        String title;
        String type;
        String validDate;
        String value;
        String versionCode;
        String versionName;

        private ContentPackageInfo(String validDate, String value, String type, String attribute, String packageName) {
            this.fileSizeList = new ArrayList();
            this.validDate = validDate;
            this.value = value;
            this.type = type;
            this.attribute = attribute;
            this.packageName = packageName;
        }
    }

    public void onCreate() {
        Log.secD("AlarmCelebVoiceService", "onCreate");
        createDir(AlarmCvConstants.getUIDRootPath());
        this.mCelebVoicePackagesDbHelper = CelebVoicePackagesDbHelper.getInstance(getApplicationContext());
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return this.mAlarmCelebVoiceInterface;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 2;
    }

    private int createDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            return -2;
        }
        if (!dir.mkdirs()) {
            return 0;
        }
        try {
            if (setDirectoryPermission(dir, 485) != 0) {
                return -4;
            }
            return 0;
        } catch (SecurityException e) {
            Log.secD("AlarmCelebVoiceService", "Security error while creating directory");
            return -24;
        } catch (Exception e2) {
            Log.secD("AlarmCelebVoiceService", "createDirs : permission error");
            return -4;
        }
    }

    private int createSubDirs(String path) {
        int lastIndex = path.lastIndexOf("/");
        if (lastIndex != -1) {
            createSubDirs(path.substring(0, lastIndex));
        }
        File dir = new File(AlarmCvConstants.getUIDRootPath() + path);
        if (dir.exists()) {
            return -2;
        }
        if (dir.mkdir()) {
            try {
                if (setDirectoryPermission(dir, 485) != 0) {
                    return -4;
                }
                return 0;
            } catch (Exception e) {
                Log.secD("AlarmCelebVoiceService", "createSubDirs : permission error");
                return -4;
            }
        }
        Log.secD("AlarmCelebVoiceService", "createSubDirs : not created");
        return -3;
    }

    private int removeDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return -5;
        }
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    removeDir(file.getPath());
                } else if (!file.delete()) {
                    Log.secD("AlarmCelebVoiceService", "Error deleting " + file);
                }
            }
        }
        try {
            if (dir.delete()) {
                return 0;
            }
            return -6;
        } catch (SecurityException e) {
            Log.secD("AlarmCelebVoiceService", "Security error while remove Directory");
            return -6;
        }
    }

    private void installContentPackage(String validDate, String cost, String type, String attribute, String packageName, String path) throws Exception {
        this.mContentPackageInfo = new ContentPackageInfo(validDate, cost, type, attribute, packageName);
        if (path.isEmpty()) {
            Log.secD("AlarmCelebVoiceService", "Path is Empty");
            this.mResult = -20;
        } else {
            File contentPackageFile = new File(path);
            if (contentPackageFile.exists()) {
                try {
                    checkContentPackageIntegrity(contentPackageFile);
                    createItemsDatabase(type, packageName);
                    createPackagesDatabase(validDate, attribute, packageName);
                } catch (Exception e) {
                    Log.secD("AlarmCelebVoiceService", "installContentPackage: error path 1");
                    throw new Exception();
                }
            }
            Log.secD("AlarmCelebVoiceService", "Celeb Voice APK does not exist");
            this.mResult = -10;
        }
        cleanUp();
    }

    private void checkContentPackageIntegrity(File contentPackageFile) throws Exception {
        try {
            unpackContentPackage(contentPackageFile);
            StringBuilder jsonData = new StringBuilder();
            constructJsonData(jsonData);
            constructContentPackageInfo(jsonData);
            if (new File(this.mInstallRootDir + "/assets", this.mContentPackageInfo.previewFile).exists()) {
                int sizeIndex = 0;
                Iterator it = this.mContentPackageInfo.fileNameList.iterator();
                while (it.hasNext()) {
                    File celebVoiceFile = new File(this.mInstallRootDir + "/assets", (String) it.next());
                    if (celebVoiceFile.exists()) {
                        this.mContentPackageInfo.fileSizeList.add(sizeIndex, Double.valueOf(((double) Math.round((((double) celebVoiceFile.length()) / 1024.0d) * 10.0d)) / 10.0d));
                        sizeIndex++;
                    } else {
                        Log.secD("AlarmCelebVoiceService", "Integrity Check: Celeb Voice File list and physical file do not match");
                        this.mResult = -18;
                        throw new Exception();
                    }
                }
                return;
            }
            Log.secD("AlarmCelebVoiceService", "Integrity Check: Celeb Voice File list and physical file do not match");
            this.mResult = -18;
            throw new Exception();
        } catch (Exception e) {
            Log.secD("AlarmCelebVoiceService", "checkContentPackageIntegrity: Error");
            throw new Exception();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void constructJsonData(java.lang.StringBuilder r13) throws java.lang.Exception {
        /*
        r12 = this;
        r11 = -12;
        r9 = 0;
        r6 = "AlarmCelebVoiceService";
        r7 = "constructJsonData";
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r2 = 0;
        r6 = "clockvoice";
        r7 = r12.mContentPackageInfo;
        r7 = r7.type;
        r6 = r6.equals(r7);
        if (r6 == 0) goto L_0x0033;
    L_0x0017:
        r2 = new java.io.File;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = r12.mInstallRootDir;
        r6 = r6.append(r7);
        r7 = "/assets";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r7 = "clockvoice.json";
        r2.<init>(r6, r7);
    L_0x0033:
        if (r2 != 0) goto L_0x0044;
    L_0x0035:
        r6 = "AlarmCelebVoiceService";
        r7 = "Integrity check: clockvoice.json not found";
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r12.mResult = r11;
        r6 = new java.lang.Exception;
        r6.<init>();
        throw r6;
    L_0x0044:
        r3 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
        r3.<init>(r2);	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
        r6 = 0;
        r4 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
        r4.<init>(r3);	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
        r7 = 0;
        r0 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
        r0.<init>(r4);	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
        r8 = 0;
    L_0x0056:
        r5 = r0.readLine();	 Catch:{ Throwable -> 0x0060, all -> 0x00fc }
        if (r5 == 0) goto L_0x008f;
    L_0x005c:
        r13.append(r5);	 Catch:{ Throwable -> 0x0060, all -> 0x00fc }
        goto L_0x0056;
    L_0x0060:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x0062 }
    L_0x0062:
        r7 = move-exception;
        r8 = r6;
    L_0x0064:
        if (r0 == 0) goto L_0x006b;
    L_0x0066:
        if (r8 == 0) goto L_0x00c9;
    L_0x0068:
        r0.close();	 Catch:{ Throwable -> 0x00c4, all -> 0x00bc }
    L_0x006b:
        throw r7;	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
    L_0x006c:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x006e }
    L_0x006e:
        r7 = move-exception;
        r8 = r6;
    L_0x0070:
        if (r4 == 0) goto L_0x0077;
    L_0x0072:
        if (r8 == 0) goto L_0x00de;
    L_0x0074:
        r4.close();	 Catch:{ Throwable -> 0x00d9, all -> 0x00d2 }
    L_0x0077:
        throw r7;	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
    L_0x0078:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x007a }
    L_0x007a:
        r7 = move-exception;
        r9 = r6;
    L_0x007c:
        if (r3 == 0) goto L_0x0083;
    L_0x007e:
        if (r9 == 0) goto L_0x00f8;
    L_0x0080:
        r3.close();	 Catch:{ Throwable -> 0x00f3 }
    L_0x0083:
        throw r7;	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
    L_0x0084:
        r1 = move-exception;
        r6 = "AlarmCelebVoiceService";
        r7 = "Integrity check: JSON file not found";
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r12.mResult = r11;
    L_0x008e:
        return;
    L_0x008f:
        if (r0 == 0) goto L_0x0096;
    L_0x0091:
        if (r9 == 0) goto L_0x00c0;
    L_0x0093:
        r0.close();	 Catch:{ Throwable -> 0x00b7, all -> 0x00bc }
    L_0x0096:
        if (r4 == 0) goto L_0x009d;
    L_0x0098:
        if (r9 == 0) goto L_0x00d5;
    L_0x009a:
        r4.close();	 Catch:{ Throwable -> 0x00cd, all -> 0x00d2 }
    L_0x009d:
        if (r3 == 0) goto L_0x008e;
    L_0x009f:
        if (r9 == 0) goto L_0x00e2;
    L_0x00a1:
        r3.close();	 Catch:{ Throwable -> 0x00a5 }
        goto L_0x008e;
    L_0x00a5:
        r7 = move-exception;
        r6.addSuppressed(r7);	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
        goto L_0x008e;
    L_0x00aa:
        r1 = move-exception;
        r6 = "AlarmCelebVoiceService";
        r7 = "Integrity check: JSON file security error";
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r6 = -15;
        r12.mResult = r6;
        goto L_0x008e;
    L_0x00b7:
        r10 = move-exception;
        r8.addSuppressed(r10);	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
        goto L_0x0096;
    L_0x00bc:
        r6 = move-exception;
        r7 = r6;
        r8 = r9;
        goto L_0x0070;
    L_0x00c0:
        r0.close();	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
        goto L_0x0096;
    L_0x00c4:
        r6 = move-exception;
        r8.addSuppressed(r6);	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
        goto L_0x006b;
    L_0x00c9:
        r0.close();	 Catch:{ Throwable -> 0x006c, all -> 0x00bc }
        goto L_0x006b;
    L_0x00cd:
        r8 = move-exception;
        r7.addSuppressed(r8);	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
        goto L_0x009d;
    L_0x00d2:
        r6 = move-exception;
        r7 = r6;
        goto L_0x007c;
    L_0x00d5:
        r4.close();	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
        goto L_0x009d;
    L_0x00d9:
        r6 = move-exception;
        r8.addSuppressed(r6);	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
        goto L_0x0077;
    L_0x00de:
        r4.close();	 Catch:{ Throwable -> 0x0078, all -> 0x00d2 }
        goto L_0x0077;
    L_0x00e2:
        r3.close();	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
        goto L_0x008e;
    L_0x00e6:
        r1 = move-exception;
        r6 = "AlarmCelebVoiceService";
        r7 = "Integrity check: JSON IO exception";
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r6 = -13;
        r12.mResult = r6;
        goto L_0x008e;
    L_0x00f3:
        r6 = move-exception;
        r9.addSuppressed(r6);	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
        goto L_0x0083;
    L_0x00f8:
        r3.close();	 Catch:{ FileNotFoundException -> 0x0084, SecurityException -> 0x00aa, IOException -> 0x00e6 }
        goto L_0x0083;
    L_0x00fc:
        r6 = move-exception;
        r7 = r6;
        r8 = r9;
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.constructJsonData(java.lang.StringBuilder):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void unpackContentPackage(java.io.File r24) throws java.lang.Exception {
        /*
        r23 = this;
        r8 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
        r0 = r24;
        r8.<init>(r0);	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
        r22 = 0;
        r18 = new java.util.zip.ZipInputStream;	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
        r0 = r18;
        r0.<init>(r8);	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
        r21 = 0;
    L_0x0012:
        r16 = r18.getNextEntry();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r16 == 0) goto L_0x019d;
    L_0x0018:
        r17 = r16.getName();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r16.isDirectory();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r19 == 0) goto L_0x0076;
    L_0x0022:
        r19 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19.<init>();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r23;
        r0 = r0.mInstallSubDir;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = r0;
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = "/";
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r19;
        r1 = r17;
        r19 = r0.append(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r19.toString();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r23;
        r1 = r19;
        r0.createSubDirs(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        goto L_0x0012;
    L_0x004b:
        r19 = move-exception;
        throw r19;	 Catch:{ all -> 0x004d }
    L_0x004d:
        r20 = move-exception;
        r21 = r19;
    L_0x0050:
        if (r18 == 0) goto L_0x0057;
    L_0x0052:
        if (r21 == 0) goto L_0x01e7;
    L_0x0054:
        r18.close();	 Catch:{ Throwable -> 0x01dd, all -> 0x01d2 }
    L_0x0057:
        throw r20;	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
    L_0x0058:
        r19 = move-exception;
        throw r19;	 Catch:{ all -> 0x005a }
    L_0x005a:
        r20 = move-exception;
        r21 = r19;
    L_0x005d:
        if (r8 == 0) goto L_0x0064;
    L_0x005f:
        if (r21 == 0) goto L_0x01fb;
    L_0x0061:
        r8.close();	 Catch:{ Throwable -> 0x01f1 }
    L_0x0064:
        throw r20;	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
    L_0x0065:
        r5 = move-exception;
        r19 = "AlarmCelebVoiceService";
        r20 = "unpackContentPackage: APK not found";
        com.sec.android.app.clockpackage.common.util.Log.secD(r19, r20);
        r19 = -10;
        r0 = r19;
        r1 = r23;
        r1.mResult = r0;
    L_0x0075:
        return;
    L_0x0076:
        r19 = ".ogg";
        r0 = r17;
        r1 = r19;
        r19 = r0.contains(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r19 != 0) goto L_0x00a6;
    L_0x0082:
        r19 = ".json";
        r0 = r17;
        r1 = r19;
        r19 = r0.contains(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r19 != 0) goto L_0x00a6;
    L_0x008e:
        r19 = ".png";
        r0 = r17;
        r1 = r19;
        r19 = r0.contains(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r19 != 0) goto L_0x00a6;
    L_0x009a:
        r19 = ".gif";
        r0 = r17;
        r1 = r19;
        r19 = r0.contains(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r19 == 0) goto L_0x0012;
    L_0x00a6:
        r3 = "assets/clockvoice.json";
        r0 = r17;
        r19 = r3.contentEquals(r0);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r19 == 0) goto L_0x00d8;
    L_0x00b0:
        r19 = "/";
        r0 = r17;
        r1 = r19;
        r19 = r0.lastIndexOf(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r11 = r19 + 1;
        r19 = ".";
        r0 = r17;
        r1 = r19;
        r10 = r0.lastIndexOf(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r23;
        r0 = r0.mContentPackageInfo;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r0;
        r0 = r17;
        r20 = r0.substring(r11, r10);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r20;
        r1 = r19;
        r1.type = r0;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
    L_0x00d8:
        r19 = "/";
        r0 = r17;
        r1 = r19;
        r4 = r0.lastIndexOf(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19.<init>();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r23;
        r0 = r0.mInstallRootDir;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = r0;
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = "/";
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = 0;
        r0 = r17;
        r1 = r20;
        r20 = r0.substring(r1, r4);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r13 = r19.toString();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19.<init>();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r23;
        r0 = r0.mInstallSubDir;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = r0;
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = "/";
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r20 = 0;
        r0 = r17;
        r1 = r20;
        r20 = r0.substring(r1, r4);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r19.append(r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r19.toString();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r23;
        r1 = r19;
        r0.createSubDirs(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = "/";
        r0 = r17;
        r1 = r19;
        r6 = r0.lastIndexOf(r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = r17.length();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r17;
        r1 = r19;
        r17 = r0.substring(r6, r1);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r7 = new java.io.File;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r0 = r17;
        r7.<init>(r13, r0);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r9 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r9.<init>(r7);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = 10240; // 0x2800 float:1.4349E-41 double:5.059E-320;
        r0 = r19;
        r2 = new byte[r0];	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
    L_0x015f:
        r0 = r18;
        r12 = r0.read(r2);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        if (r12 <= 0) goto L_0x0174;
    L_0x0167:
        r19 = 0;
        r0 = r19;
        r9.write(r2, r0, r12);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        goto L_0x015f;
    L_0x016f:
        r19 = move-exception;
        r20 = r19;
        goto L_0x0050;
    L_0x0174:
        r9.close();	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r14 = 485; // 0x1e5 float:6.8E-43 double:2.396E-321;
        r0 = r23;
        r15 = r0.setDirectoryPermission(r7, r14);	 Catch:{ Exception -> 0x018b }
        if (r15 == 0) goto L_0x0012;
    L_0x0181:
        r19 = -4;
        r0 = r19;
        r1 = r23;
        r1.mResult = r0;	 Catch:{ Exception -> 0x018b }
        goto L_0x0012;
    L_0x018b:
        r5 = move-exception;
        r19 = "AlarmCelebVoiceService";
        r20 = "unpackContentPackage : permission error";
        com.sec.android.app.clockpackage.common.util.Log.secD(r19, r20);	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        r19 = -4;
        r0 = r19;
        r1 = r23;
        r1.mResult = r0;	 Catch:{ Throwable -> 0x004b, all -> 0x016f }
        goto L_0x0012;
    L_0x019d:
        if (r18 == 0) goto L_0x01a4;
    L_0x019f:
        if (r21 == 0) goto L_0x01d9;
    L_0x01a1:
        r18.close();	 Catch:{ Throwable -> 0x01c9, all -> 0x01d2 }
    L_0x01a4:
        if (r8 == 0) goto L_0x0075;
    L_0x01a6:
        if (r22 == 0) goto L_0x01ec;
    L_0x01a8:
        r8.close();	 Catch:{ Throwable -> 0x01ad }
        goto L_0x0075;
    L_0x01ad:
        r19 = move-exception;
        r0 = r22;
        r1 = r19;
        r0.addSuppressed(r1);	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
        goto L_0x0075;
    L_0x01b7:
        r5 = move-exception;
        r19 = "AlarmCelebVoiceService";
        r20 = "unpackContentPackage: io exception";
        com.sec.android.app.clockpackage.common.util.Log.secD(r19, r20);
        r19 = -11;
        r0 = r19;
        r1 = r23;
        r1.mResult = r0;
        goto L_0x0075;
    L_0x01c9:
        r19 = move-exception;
        r0 = r21;
        r1 = r19;
        r0.addSuppressed(r1);	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
        goto L_0x01a4;
    L_0x01d2:
        r19 = move-exception;
        r20 = r19;
        r21 = r22;
        goto L_0x005d;
    L_0x01d9:
        r18.close();	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
        goto L_0x01a4;
    L_0x01dd:
        r19 = move-exception;
        r0 = r21;
        r1 = r19;
        r0.addSuppressed(r1);	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
        goto L_0x0057;
    L_0x01e7:
        r18.close();	 Catch:{ Throwable -> 0x0058, all -> 0x01d2 }
        goto L_0x0057;
    L_0x01ec:
        r8.close();	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
        goto L_0x0075;
    L_0x01f1:
        r19 = move-exception;
        r0 = r21;
        r1 = r19;
        r0.addSuppressed(r1);	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
        goto L_0x0064;
    L_0x01fb:
        r8.close();	 Catch:{ FileNotFoundException -> 0x0065, IOException -> 0x01b7 }
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.unpackContentPackage(java.io.File):void");
    }

    private void constructContentPackageInfo(StringBuilder jsonData) throws Exception {
        Log.secD("AlarmCelebVoiceService", "constructContentPackageInfo");
        try {
            JSONObject jsonDataObject = new JSONObject(jsonData.toString());
            ArrayList<String> parseResult = new ArrayList();
            getStringFromJsonObject(jsonDataObject, "title", null, 0, parseResult);
            this.mContentPackageInfo.title = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "version_code", null, 0, parseResult);
            this.mContentPackageInfo.versionCode = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "version_name", null, 0, parseResult);
            this.mContentPackageInfo.versionName = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "type", null, 0, parseResult);
            this.mContentPackageInfo.type = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "file", null, 0, parseResult);
            this.mContentPackageInfo.file = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "bitrate", null, 0, parseResult);
            this.mContentPackageInfo.bitrate = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "stereo", null, 0, parseResult);
            this.mContentPackageInfo.stereo = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "khz", null, 0, parseResult);
            this.mContentPackageInfo.khz = (String) parseResult.get(0);
            parseResult.clear();
            getStringFromJsonObject(jsonDataObject, "preview", null, 0, parseResult);
            this.mContentPackageInfo.previewFile = (String) parseResult.get(0);
            parseResult.clear();
            ArrayList<String> parseRepStaticResult = new ArrayList();
            getStringFromJsonObject(jsonDataObject, "static", null, 0, parseRepStaticResult);
            this.mContentPackageInfo.repStatic = parseRepStaticResult;
            parseResult.clear();
            ArrayList<String> parseOriginalResult = new ArrayList();
            getStringFromJsonObject(jsonDataObject, "original", null, 0, parseOriginalResult);
            this.mContentPackageInfo.fileNameList = parseOriginalResult;
        } catch (JSONException e) {
            Log.secD("AlarmCelebVoiceService", "Integrity check: JSON data exception");
            this.mResult = -13;
        } catch (Exception e2) {
        }
    }

    private void getStringFromJsonObject(JSONObject jsonObject, String key, String[] values, int index, ArrayList<String> resultArrayList) throws Exception {
        if (key != null && values == null) {
            values = (String[]) AlarmCvConstants.CELEBVOICE_JSON_FORMAT.get(key);
        }
        try {
            key = values[index];
            if (index < values.length - 1) {
                JSONObject newJsonObject = jsonObject.getJSONObject(key);
                if (newJsonObject != null && newJsonObject.length() > 0) {
                    getStringFromJsonObject(newJsonObject, null, values, index + 1, resultArrayList);
                }
            } else if ("static".equals(key) || "original".equals(key)) {
                JSONArray jsonArray = jsonObject.optJSONArray(key);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        resultArrayList.add(jsonArray.getString(i));
                    }
                }
            } else {
                String fileName = jsonObject.getString(key);
                if (fileName != null) {
                    resultArrayList.add(fileName);
                }
            }
        } catch (JSONException e) {
            Log.secD("AlarmCelebVoiceService", "getStringFromJSONFormat: JSON exception");
            this.mResult = -17;
        } catch (NullPointerException e2) {
            Log.secD("AlarmCelebVoiceService", "getStringFromJsonFormat: NPE");
            this.mResult = -16;
        }
    }

    private int deleteContentPackageFolder(String type, String packageName) {
        return removeDir(AlarmCvConstants.getUIDRootPath() + type + "/" + packageName);
    }

    private int deleteContentPackageDataBase(String packageName) {
        String query = "DELETE FROM CELEBVOICE_PACKAGES WHERE PKG_NAME = '" + packageName + "';";
        SQLiteDatabase db = this.mCelebVoicePackagesDbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isPackageInDataBase(java.lang.String r12, java.lang.String r13) {
        /*
        r11 = this;
        r10 = 0;
        r9 = 1;
        r2 = 0;
        r5 = r11.getApplicationContext();
        r5 = com.sec.android.app.clockpackage.alarm.model.CelebVoicePackagesDbHelper.getInstance(r5);
        r11.mCelebVoicePackagesDbHelper = r5;
        r5 = r11.mCelebVoicePackagesDbHelper;
        r1 = r5.getWritableDatabase();
        r0 = new android.database.sqlite.SQLiteQueryBuilder;
        r0.<init>();
        r5 = "CELEBVOICE_PACKAGES";
        r0.setTables(r5);
        r5 = com.sec.android.app.clockpackage.alarm.model.CelebVoiceProvider.mCelebVoicePackagesMap;
        r0.setProjectionMap(r5);
        r3 = "PKG_NAME=? AND TYPE=?";
        r5 = 2;
        r4 = new java.lang.String[r5];
        r4[r10] = r13;
        r4[r9] = r12;
        r5 = r2;
        r6 = r2;
        r7 = r2;
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);
        if (r8 == 0) goto L_0x0045;
    L_0x0034:
        r5 = r8.getCount();	 Catch:{ Throwable -> 0x0050, all -> 0x0065 }
        if (r5 != r9) goto L_0x0045;
    L_0x003a:
        if (r8 == 0) goto L_0x0041;
    L_0x003c:
        if (r2 == 0) goto L_0x004c;
    L_0x003e:
        r8.close();	 Catch:{ Throwable -> 0x0047 }
    L_0x0041:
        r1.close();
        return r9;
    L_0x0045:
        r9 = r10;
        goto L_0x003a;
    L_0x0047:
        r5 = move-exception;
        r2.addSuppressed(r5);
        goto L_0x0041;
    L_0x004c:
        r8.close();
        goto L_0x0041;
    L_0x0050:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x0052 }
    L_0x0052:
        r2 = move-exception;
        r5 = r2;
    L_0x0054:
        if (r8 == 0) goto L_0x005b;
    L_0x0056:
        if (r6 == 0) goto L_0x0061;
    L_0x0058:
        r8.close();	 Catch:{ Throwable -> 0x005c }
    L_0x005b:
        throw r5;
    L_0x005c:
        r2 = move-exception;
        r6.addSuppressed(r2);
        goto L_0x005b;
    L_0x0061:
        r8.close();
        goto L_0x005b;
    L_0x0065:
        r5 = move-exception;
        r6 = r2;
        goto L_0x0054;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.isPackageInDataBase(java.lang.String, java.lang.String):boolean");
    }

    private void createPackagesDatabase(String validDate, String attribute, String packageName) {
        SQLiteDatabase db = this.mCelebVoicePackagesDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PKG_NAME", packageName);
        cv.put("TYPE", this.mContentPackageInfo.type);
        cv.put("VALID_DATE", this.mContentPackageInfo.validDate);
        cv.put("VALID_DATE_LONG", Long.valueOf(calculateExpiredDate(this.mContentPackageInfo.validDate)));
        cv.put("COST", this.mContentPackageInfo.value);
        cv.put("FILE", this.mContentPackageInfo.file);
        cv.put("CONTENT_NAME", attribute);
        cv.put("TITLE", this.mContentPackageInfo.title);
        cv.put("VERSION_CODE", this.mContentPackageInfo.versionCode);
        cv.put("VERSION_NAME", this.mContentPackageInfo.versionName);
        cv.put("BITRATE", Integer.valueOf(Integer.parseInt(this.mContentPackageInfo.bitrate)));
        cv.put("STEREO", this.mContentPackageInfo.stereo);
        cv.put("KHZ", Integer.valueOf((int) (Float.parseFloat(this.mContentPackageInfo.khz) * 1000.0f)));
        cv.put("PREVIEW_FILE", this.mContentPackageInfo.previewFile);
        File fileRepStatic = new File(this.mInstallRootDir + "/assets" + "/" + ((String) this.mContentPackageInfo.repStatic.get(0)));
        if (fileRepStatic.exists()) {
            cv.put("REP_STATIC", fileToByteArray(fileRepStatic));
        }
        Log.secD("AlarmCelebVoiceService", "createPackagesDatabase result = " + db.insert("CELEBVOICE_PACKAGES", null, cv));
        db.close();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void createItemsDatabase(java.lang.String r14, java.lang.String r15) {
        /*
        r13 = this;
        r10 = r13.getApplicationContext();
        r10 = com.sec.android.app.clockpackage.alarm.model.CelebVoiceItemsDbHelper.getInstance(r10, r15, r14);
        r13.mCelebVoiceItemsDbHelper = r10;
        r10 = r13.mCelebVoiceItemsDbHelper;
        r1 = r10.getWritableDatabase();
        r0 = new android.content.ContentValues;
        r0.<init>();
        r6 = 0;
        r5 = 0;
        r1.beginTransaction();	 Catch:{ Exception -> 0x006b }
        r10 = r13.mContentPackageInfo;	 Catch:{ Exception -> 0x006b }
        r10 = r10.fileNameList;	 Catch:{ Exception -> 0x006b }
        r11 = r10.iterator();	 Catch:{ Exception -> 0x006b }
        r8 = r5;
    L_0x0024:
        r10 = r11.hasNext();	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        if (r10 == 0) goto L_0x00a6;
    L_0x002a:
        r3 = r11.next();	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r3 = (java.lang.String) r3;	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r10 = "/";
        r9 = r3.lastIndexOf(r10);	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r10 = r9 + 1;
        r12 = r3.length();	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r4 = r3.subSequence(r10, r12);	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r4 = (java.lang.String) r4;	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r10 = "FILE_NAME";
        r0.put(r10, r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r12 = "FILE_SIZE";
        r10 = r13.mContentPackageInfo;	 Catch:{ IndexOutOfBoundsException -> 0x0061 }
        r10 = r10.fileSizeList;	 Catch:{ IndexOutOfBoundsException -> 0x0061 }
        r5 = r8 + 1;
        r10 = r10.get(r8);	 Catch:{ IndexOutOfBoundsException -> 0x00b9 }
        r10 = (java.lang.Double) r10;	 Catch:{ IndexOutOfBoundsException -> 0x00b9 }
        r0.put(r12, r10);	 Catch:{ IndexOutOfBoundsException -> 0x00b9 }
    L_0x0058:
        r10 = "CELEBVOICE_ITEMS";
        r12 = 0;
        r6 = r1.insert(r10, r12, r0);	 Catch:{ Exception -> 0x006b }
        r8 = r5;
        goto L_0x0024;
    L_0x0061:
        r2 = move-exception;
        r5 = r8;
    L_0x0063:
        r10 = "AlarmCelebVoiceService";
        r12 = "FILE_SIZE IndexOutOfBoundsException";
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r12);	 Catch:{ Exception -> 0x006b }
        goto L_0x0058;
    L_0x006b:
        r2 = move-exception;
    L_0x006c:
        r10 = "AlarmCelebVoiceService";
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ae }
        r11.<init>();	 Catch:{ all -> 0x00ae }
        r12 = "happen Exception ";
        r11 = r11.append(r12);	 Catch:{ all -> 0x00ae }
        r11 = r11.append(r2);	 Catch:{ all -> 0x00ae }
        r11 = r11.toString();	 Catch:{ all -> 0x00ae }
        com.sec.android.app.clockpackage.common.util.Log.secE(r10, r11);	 Catch:{ all -> 0x00ae }
        r1.endTransaction();
    L_0x0087:
        r10 = "AlarmCelebVoiceService";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "createItemsDatabase result = ";
        r11 = r11.append(r12);
        r11 = r11.append(r6);
        r11 = r11.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r10, r11);
        r1.close();
        r10 = 0;
        r13.mCelebVoiceItemsDbHelper = r10;
        return;
    L_0x00a6:
        r1.setTransactionSuccessful();	 Catch:{ Exception -> 0x00b6, all -> 0x00b3 }
        r1.endTransaction();
        r5 = r8;
        goto L_0x0087;
    L_0x00ae:
        r10 = move-exception;
    L_0x00af:
        r1.endTransaction();
        throw r10;
    L_0x00b3:
        r10 = move-exception;
        r5 = r8;
        goto L_0x00af;
    L_0x00b6:
        r2 = move-exception;
        r5 = r8;
        goto L_0x006c;
    L_0x00b9:
        r2 = move-exception;
        goto L_0x0063;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.createItemsDatabase(java.lang.String, java.lang.String):void");
    }

    private void cleanUp() {
        Log.secD("AlarmCelebVoiceService", "cleanUp: " + this.mResult);
        if (this.mResult == 0) {
            new File(this.mInstallRootDir, "install.apk").delete();
            new File(this.mInstallRootDir + "/" + "assets/", "clockvoice.json").delete();
            removeDir(this.mInstallRootDir + "/" + "/res");
        } else if (new File(this.mInstallRootDir).exists()) {
            removeDir(this.mInstallRootDir);
        }
    }

    private void sendProcessIntent(int process, String type, String packageName, String version_code, String version_name) {
        Intent intent = new Intent();
        intent.setAction("samsung.clockpack.intent.PROCESS_COMPLETE");
        intent.putExtra("extra_process_no", process);
        intent.putExtra("extra_type", type);
        intent.putExtra("extra_package_name", packageName);
        intent.putExtra("extra_version_code", version_code);
        intent.putExtra("extra_version_name", version_name);
    }

    private int setDirectoryPermission(File path, int mode) throws Exception {
        return ((Integer) Class.forName("android.os.FileUtils").getMethod("setPermissions", new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE}).invoke(null, new Object[]{path.getAbsolutePath(), Integer.valueOf(mode), Integer.valueOf(-1), Integer.valueOf(-1)})).intValue();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] fileToByteArray(java.io.File r6) {
        /*
        r4 = r6.length();
        r3 = (int) r4;
        r0 = new byte[r3];
        r2 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x001d }
        r2.<init>(r6);	 Catch:{ Exception -> 0x001d }
        r4 = 0;
        r2.read(r0);	 Catch:{ Throwable -> 0x003b }
        if (r2 == 0) goto L_0x0017;
    L_0x0012:
        if (r4 == 0) goto L_0x0037;
    L_0x0014:
        r2.close();	 Catch:{ Throwable -> 0x0018 }
    L_0x0017:
        return r0;
    L_0x0018:
        r3 = move-exception;
        r4.addSuppressed(r3);	 Catch:{ Exception -> 0x001d }
        goto L_0x0017;
    L_0x001d:
        r1 = move-exception;
        r3 = "AlarmCelebVoiceService";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "happen Exception ";
        r4 = r4.append(r5);
        r4 = r4.append(r1);
        r4 = r4.toString();
        com.sec.android.app.clockpackage.common.util.Log.secE(r3, r4);
        goto L_0x0017;
    L_0x0037:
        r2.close();	 Catch:{ Exception -> 0x001d }
        goto L_0x0017;
    L_0x003b:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x003d }
    L_0x003d:
        r3 = move-exception;
        if (r2 == 0) goto L_0x0045;
    L_0x0040:
        if (r4 == 0) goto L_0x004b;
    L_0x0042:
        r2.close();	 Catch:{ Throwable -> 0x0046 }
    L_0x0045:
        throw r3;	 Catch:{ Exception -> 0x001d }
    L_0x0046:
        r5 = move-exception;
        r4.addSuppressed(r5);	 Catch:{ Exception -> 0x001d }
        goto L_0x0045;
    L_0x004b:
        r2.close();	 Catch:{ Exception -> 0x001d }
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService.fileToByteArray(java.io.File):byte[]");
    }

    private long calculateExpiredDate(String date) {
        String validDate = date.split(";")[1].substring(0, 15);
        long expiredDateLong = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            expiredDateLong = simpleDateFormat.parse(validDate).getTime();
        } catch (ParseException e) {
            Log.secE("AlarmCelebVoiceService", "happen Exception " + e);
        }
        return expiredDateLong;
    }
}
