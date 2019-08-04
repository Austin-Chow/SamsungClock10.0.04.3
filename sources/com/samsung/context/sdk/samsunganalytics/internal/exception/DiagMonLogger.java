package com.samsung.context.sdk.samsunganalytics.internal.exception;

import android.app.Application;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.android.diagmonagent.log.provider.DiagMonConfig;
import com.sec.android.diagmonagent.log.provider.DiagMonSDK;
import com.sec.android.diagmonagent.log.provider.DiagMonSDK.DiagMonHelper;
import com.sec.android.diagmonagent.log.provider.IssueBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

public class DiagMonLogger implements UncaughtExceptionHandler {
    private final String DIRECTORY;
    private Application application;
    private Configuration configuration;
    private UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private boolean useDiagnostic = true;
    private boolean wifiOnly = true;

    public DiagMonLogger(Application application, Configuration configuration, UncaughtExceptionHandler uncaughtExceptionHandler, String serviceID, boolean useDiagnostic, boolean wifiOnly) {
        this.application = application;
        this.DIRECTORY = application.getApplicationInfo().dataDir;
        this.defaultUncaughtExceptionHandler = uncaughtExceptionHandler;
        this.configuration = configuration;
        this.useDiagnostic = useDiagnostic;
        this.wifiOnly = wifiOnly;
        setConfiguration(serviceID);
    }

    private void setConfiguration(String serviceID) {
        List<String> logList = new ArrayList();
        logList.add(this.DIRECTORY + "/" + "diagmon.log");
        DiagMonSDK.setConfiguration(new DiagMonConfig(this.application).setServiceId(serviceID).setAgree(this.useDiagnostic ? "D" : "Y").setTrackingId(this.configuration.getTrackingId()).setLogList(logList));
    }

    public void uncaughtException(Thread t, Throwable e) {
        if (this.configuration.getUserAgreement().isAgreement()) {
            write(makeFile(this.DIRECTORY, "diagmon.log"), e);
            issueReport();
        }
        this.defaultUncaughtExceptionHandler.uncaughtException(t, e);
    }

    private void issueReport() {
        Debug.LogENG("issue report");
        DiagMonHelper.issueReport(this.application.getApplicationContext(), new IssueBuilder().setResultCode("fatal exception").setUiMode(false).setWifiOnly(this.wifiOnly));
    }

    private void write(File file, Throwable e) {
        Throwable th;
        FileOutputStream outputStream = null;
        try {
            FileOutputStream outputStream2 = new FileOutputStream(file, false);
            try {
                e.printStackTrace(new PrintStream(outputStream2));
                if (outputStream2 != null) {
                    try {
                        outputStream2.close();
                        outputStream = outputStream2;
                        return;
                    } catch (IOException e2) {
                        outputStream = outputStream2;
                        return;
                    }
                }
            } catch (FileNotFoundException e3) {
                outputStream = outputStream2;
                try {
                    Debug.LogENG("Failed to write.");
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = outputStream2;
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e6) {
            Debug.LogENG("Failed to write.");
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private File makeDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private File makeFile(String dirPath, String fileName) {
        File file = null;
        if (makeDir(dirPath).isDirectory()) {
            file = new File(dirPath + "/" + fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                Debug.LogENG(e.getLocalizedMessage());
            }
        }
        return file;
    }
}
