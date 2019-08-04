package com.sec.android.app.clockpackage.backuprestore.util;

import android.content.Context;
import android.os.SemSystemProperties;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class DataConvertToXML {
    protected final String SAVE_KEY;
    protected final int SECURITY_LEVEL;
    protected ClockDataEncryption clockDataEncryption = new ClockDataEncryption();
    protected BufferedOutputStream mBOS;
    protected Exporter mExporter;
    protected FileOutputStream mFileOutputStream;
    protected String mFilepath;
    protected OutputStream mOutputStream;
    protected int mWhichFunction;

    protected class Exporter {
        private final String[] END_DB = new String[]{"</Alarms>", "</Worldclocks>", "</Dualclocks>", "</Timer>", "</AlarmWidgets>"};
        private final String[] END_TABLE = new String[]{"</alarm>", "</worldclock>", "</dualclock>", "</timerlife>", "</widget>"};
        private final String[] START_DB = new String[]{"<Alarms>", "<Worldclocks>", "<Dualclocks>", "<Timer>", "<AlarmWidgets>"};
        private final String[] START_TABLE = new String[]{"<alarm>", "<worldclock>", "<dualclock>", "<timerlife>", "<widget>"};
        private final BufferedOutputStream _bos;

        public Exporter(BufferedOutputStream bos) {
            this._bos = bos;
        }

        public void close() throws IOException {
            if (this._bos != null) {
                this._bos.close();
            }
        }

        public void startBackupVersionExport() throws IOException {
            this._bos.write(("<BackupVersion>" + (DataConvertToXML.this.mWhichFunction == 4 ? 1 : 8) + "</BackupVersion>").getBytes());
        }

        public void startBackupModelNameExport() throws IOException {
            this._bos.write(("<BackupModelName>" + SemSystemProperties.get("ro.product.model") + "</BackupModelName>").getBytes());
        }

        public void startDbExport() throws IOException {
            this._bos.write(this.START_DB[DataConvertToXML.this.mWhichFunction].getBytes());
        }

        public void endDbExport() throws IOException {
            this._bos.write(this.END_DB[DataConvertToXML.this.mWhichFunction].getBytes());
        }

        public void startTable() throws IOException {
            this._bos.write(this.START_TABLE[DataConvertToXML.this.mWhichFunction].getBytes());
        }

        public void endTable() throws IOException {
            this._bos.write(this.END_TABLE[DataConvertToXML.this.mWhichFunction].getBytes());
        }

        public void addColumn(String name, String val) throws IOException {
            this._bos.write(("<" + name + ">" + val + "</" + name + ">").getBytes());
        }

        public void startRow() throws IOException {
            this._bos.write("<item>".getBytes());
        }

        public void endRow() throws IOException {
            this._bos.write("</item>".getBytes());
        }

        public void addWidgetItem(String widgetId, String alarmId) throws IOException {
            String stg = this.START_TABLE[4] + widgetId + "," + alarmId + this.END_TABLE[4];
            Log.secD("BNR_CLOCK_DataConvertToXML", "addWidgetItem() / stg =" + stg);
            this._bos.write(stg.getBytes());
        }
    }

    protected abstract int exportData(Object obj);

    public DataConvertToXML(String filePath, String saveKey, int securityLevel, int whichFunction) {
        Log.secD("BNR_CLOCK_DataConvertToXML", "DataConvertToXML() / from = " + whichFunction);
        this.mFilepath = filePath;
        this.SAVE_KEY = saveKey;
        this.SECURITY_LEVEL = securityLevel;
        this.mWhichFunction = whichFunction;
    }

    private String getFileName() {
        switch (this.mWhichFunction) {
            case 0:
                return "/alarm.exml";
            case 1:
                return "/worldclock.exml";
            case 2:
                return "/dualclock.exml";
            case 3:
                return "/timer.exml";
            case 4:
                return "/alarmWidget.exml";
            default:
                return "";
        }
    }

    public int backupData(Object obj) {
        Log.secD("BNR_CLOCK_DataConvertToXML", "backupData() / obj = " + obj + "/ context ? = " + (obj instanceof Context) + "/ String? = " + (obj instanceof String));
        int result = prepareData();
        if (result == 0) {
            return exportData(obj);
        }
        return result;
    }

    private int prepareData() {
        Log.secD("BNR_CLOCK_DataConvertToXML", "[" + this.mWhichFunction + "] prepareData()");
        try {
            String fullPathName = this.mFilepath + getFileName();
            Log.secD("BNR_CLOCK_DataConvertToXML", "[" + this.mWhichFunction + "] fullPathName  ::::::   " + fullPathName);
            File myFile = new File(fullPathName);
            if (myFile.createNewFile()) {
                this.mFileOutputStream = new FileOutputStream(myFile);
                if (this.SECURITY_LEVEL == -1) {
                    this.mBOS = new BufferedOutputStream(this.mFileOutputStream);
                } else {
                    this.mOutputStream = this.clockDataEncryption.encryptStream(this.mFileOutputStream, this.SAVE_KEY, this.SECURITY_LEVEL);
                    this.mBOS = new BufferedOutputStream(this.mOutputStream);
                }
                this.mExporter = new Exporter(this.mBOS);
                return 0;
            }
            Log.secD("BNR_CLOCK_DataConvertToXML", "[" + this.mWhichFunction + "] Fail prepareData createNewFile : " + fullPathName);
            return 1;
        } catch (Exception e) {
            Log.secE("BNR_CLOCK_DataConvertToXML", "[" + this.mWhichFunction + "] Exception : " + e.toString());
            e.printStackTrace();
            return 1;
        }
    }

    public void deleteData(String filePath) {
        Log.secD("BNR_CLOCK_DataConvertToXML", "[" + this.mWhichFunction + "] deleteData()");
        String fullPathName = filePath + getFileName();
        File myFile = new File(fullPathName);
        if (myFile.exists() && !myFile.delete()) {
            Log.secD("BNR_CLOCK_DataConvertToXML", "Fail deleteData : " + fullPathName);
        }
    }

    protected void close() {
        try {
            if (this.mFileOutputStream != null) {
                this.mFileOutputStream.close();
            }
            if (this.mOutputStream != null) {
                this.mOutputStream.close();
            }
        } catch (IOException e) {
            Log.secE("BNR_CLOCK_DataConvertToXML", "[" + this.mWhichFunction + "] Exception : " + e.toString());
        }
    }
}
