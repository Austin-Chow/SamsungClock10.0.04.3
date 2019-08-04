package com.sec.android.app.clockpackage.backuprestore.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.IOException;

public final class ClockDataConvertToXML extends DataConvertToXML {
    private final int STRING_LENGTH = 0;

    private void exportTable(java.lang.String r11, android.database.sqlite.SQLiteDatabase r12) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0104 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1603177117.run(Unknown Source)
*/
        /*
        r10 = this;
        r7 = "BNR_CLOCK_ClockDataConvertToXML";
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "[";
        r8 = r8.append(r9);
        r9 = r10.mWhichFunction;
        r8 = r8.append(r9);
        r9 = "] exportTable()";
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r7, r8);
        r0 = 0;
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.<init>();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = "select * from ";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7 = r7.append(r11);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r5 = r7.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7 = 0;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7 = new java.lang.String[r7];	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r0 = r12.rawQuery(r5, r7);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r0 == 0) goto L_0x0129;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x003d:
        r4 = r0.getColumnCount();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7 = "BNR_CLOCK_ClockDataConvertToXML";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8.<init>();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "[";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "] Start exporting table ";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r11);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r7, r8);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r7 == 0) goto L_0x006e;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0069:
        r7 = r10.mExporter;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.startTable();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x006e:
        r0.moveToFirst();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0071:
        r7 = r0.getPosition();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r0.getCount();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r7 >= r8) goto L_0x0120;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x007b:
        r7 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r7 != 0) goto L_0x00cd;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x007f:
        r7 = r10.mExporter;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.startTable();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0084:
        r2 = 0;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0085:
        if (r2 >= r4) goto L_0x0105;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0087:
        r3 = r0.getColumnName(r2);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r6 = r0.getString(r2);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r6 != 0) goto L_0x0093;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0091:
        r6 = "";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0093:
        r7 = "BNR_CLOCK_ClockDataConvertToXML";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8.<init>();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "[";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "] col '";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r3);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "' -- val '";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r6);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = 39;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r7, r8);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r10.addData(r3, r6);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r2 = r2 + 1;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        goto L_0x0085;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x00cd:
        r7 = r10.mExporter;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.startRow();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        goto L_0x0084;
    L_0x00d3:
        r1 = move-exception;
        r7 = "BNR_CLOCK_ClockDataConvertToXML";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8.<init>();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "[";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = "] Exception : ";	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r9 = r1.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r8 = r8.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        com.sec.android.app.clockpackage.common.util.Log.secE(r7, r8);	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r1.printStackTrace();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r0 == 0) goto L_0x0104;
    L_0x0101:
        r0.close();
    L_0x0104:
        return;
    L_0x0105:
        r7 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r7 != 0) goto L_0x011a;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0109:
        r7 = r10.mExporter;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.endTable();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x010e:
        r0.moveToNext();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        goto L_0x0071;
    L_0x0113:
        r7 = move-exception;
        if (r0 == 0) goto L_0x0119;
    L_0x0116:
        r0.close();
    L_0x0119:
        throw r7;
    L_0x011a:
        r7 = r10.mExporter;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.endRow();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        goto L_0x010e;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0120:
        r7 = r10.mWhichFunction;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        if (r7 == 0) goto L_0x0129;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0124:
        r7 = r10.mExporter;	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
        r7.endTable();	 Catch:{ IOException -> 0x00d3, all -> 0x0113 }
    L_0x0129:
        if (r0 == 0) goto L_0x0104;
    L_0x012b:
        r0.close();
        goto L_0x0104;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.backuprestore.util.ClockDataConvertToXML.exportTable(java.lang.String, android.database.sqlite.SQLiteDatabase):void");
    }

    public ClockDataConvertToXML(String filePath, String saveKey, int securityLevel, int whichFunction) {
        super(filePath, saveKey, securityLevel, whichFunction);
        Log.secD("BNR_CLOCK_ClockDataConvertToXML", "ClockDataConvertToXML()");
    }

    public int exportData(Object obj) {
        Exception e;
        int i = 0;
        String dbPath = (String) obj;
        Log.secD("BNR_CLOCK_ClockDataConvertToXML", "exportClockData() / String" + (obj instanceof String));
        Log.secD("BNR_CLOCK_ClockDataConvertToXML", "[" + this.mWhichFunction + "] exportData()");
        Cursor cur = null;
        SQLiteDatabase db = null;
        try {
            this.mExporter.startDbExport();
            this.mExporter.startBackupVersionExport();
            this.mExporter.startBackupModelNameExport();
            db = SQLiteDatabase.openDatabase(dbPath, null, 268435457);
            cur = db.rawQuery("SELECT * FROM sqlite_master", new String[0]);
            Log.secD("BNR_CLOCK_ClockDataConvertToXML", "[" + this.mWhichFunction + "] show tables, cur size " + cur.getCount());
            cur.moveToFirst();
            while (cur.getPosition() < cur.getCount()) {
                String tableName = cur.getString(cur.getColumnIndex("name"));
                Log.secD("BNR_CLOCK_ClockDataConvertToXML", "[" + this.mWhichFunction + "] table name " + tableName);
                if (!(tableName.equals("android_metadata") || tableName.equals("sqlite_sequence"))) {
                    exportTable(tableName, db);
                }
                cur.moveToNext();
            }
            this.mExporter.endDbExport();
            this.mExporter.close();
            close();
            Log.secD("BNR_CLOCK_ClockDataConvertToXML", "[" + this.mWhichFunction + "] DB export Completed");
            if (cur != null) {
                cur.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        } catch (Exception e2) {
            e = e2;
            try {
                Log.secE("BNR_CLOCK_ClockDataConvertToXML", "[" + this.mWhichFunction + "] Exception : " + e.toString());
                e.printStackTrace();
                i = 1;
                return i;
            } finally {
                if (cur != null) {
                    cur.close();
                }
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        } catch (Exception e22) {
            e = e22;
            Log.secE("BNR_CLOCK_ClockDataConvertToXML", "[" + this.mWhichFunction + "] Exception : " + e.toString());
            e.printStackTrace();
            i = 1;
            return i;
        }
        return i;
    }

    private void addData(String name, String val) throws IOException {
        if ("name".equals(name) || "timername".equals(name)) {
            val = val.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        if (this.mWhichFunction == 0) {
            if ("vibrationpattern".equals(name)) {
                Log.secD("BNR_CLOCK_ClockDataConvertToXML", "BNR_ALARM_VIB : adjust alarm vibration pattern name = " + name + "/ original user value = " + val + "/ default value = " + 50035);
                if (AlarmUtil.isNewVibrationList(Integer.parseInt(val))) {
                    this.mExporter.addColumn(name, Integer.toString(50035));
                } else {
                    this.mExporter.addColumn(name, val);
                }
                name = "vibrationpattern_user";
            } else if ("dailybrief".equals(name)) {
                Log.secD("BNR_CLOCK_ClockDataConvertToXML", "daily briefing 0 / name = " + name + " / val = " + val);
                int dailyBriefing = AlarmItem.initRecommendWeatherBg(AlarmItem.initIncreasingVolume(Integer.parseInt(val)));
                if (!AlarmItem.isMasterSoundOn(dailyBriefing)) {
                    dailyBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setBixbyBriefingOn(AlarmItem.setMasterSoundOn(dailyBriefing, true), false), false);
                } else if (AlarmItem.isNewBixbyOn(dailyBriefing)) {
                    dailyBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setBixbyCelebVoice(AlarmItem.setBixbyVoiceOn(AlarmItem.setBixbyBriefingOn(dailyBriefing, true), true), false), false);
                } else {
                    dailyBriefing = AlarmItem.setAlarmToneOn(AlarmItem.setBixbyCelebVoice(AlarmItem.setBixbyVoiceOn(AlarmItem.setBixbyBriefingOn(dailyBriefing, false), true), false), true);
                }
                this.mExporter.addColumn(name, Integer.toString(dailyBriefing));
                name = "dailybrief_BACKUP_VER_8";
                Log.secD("BNR_CLOCK_ClockDataConvertToXML", "daily briefing 2 / name = " + name + " / val = " + val);
            } else if ("locationtext".equals(name)) {
                Log.secD("BNR_CLOCK_ClockDataConvertToXML", "weather music value / original value = " + val + "-> empty string");
                val = "";
            } else if ("map".equals(name)) {
                Log.secD("BNR_CLOCK_ClockDataConvertToXML", "celeb voice value / original value = " + val);
                this.mExporter.addColumn(name, "");
                name = "map_user";
            }
        }
        this.mExporter.addColumn(name, val);
    }
}
