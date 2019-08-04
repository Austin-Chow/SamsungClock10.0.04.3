package com.sec.android.app.clockpackage.common.util;

import android.content.Context;
import android.os.Binder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logger {
    private static String sLogFilePath = "";
    private static java.util.logging.Logger sLogger;

    /* renamed from: d */
    private static void m45d(String tag, String msg) {
        Log.d("Clocks$".concat(tag), msg);
    }

    /* renamed from: e */
    public static void m46e(String tag, String msg) {
        Log.e("Clocks$".concat(tag), msg);
    }

    /* renamed from: f */
    public static void m47f(String tag, String msg) {
        if (sLogger != null) {
            sLogger.log(Level.INFO, String.format(" %s(%d): %s\n", new Object[]{tag, Integer.valueOf(Binder.getCallingPid()), msg}));
        }
        Log.v("Clocks$".concat(tag), msg);
    }

    public static void init(Context context) {
        try {
            String LOG_FILE_NAME = "FileLog%g.log";
            final Date date = new Date();
            Log.d("Clocks$", "init Log Path : " + getLogFilePath(context));
            FileHandler fileHandler = new FileHandler(getLogFilePath(context) + File.separator + "FileLog%g.log", 3072, 2, true);
            fileHandler.setFormatter(new Formatter() {
                public String format(LogRecord r) {
                    date.setTime(System.currentTimeMillis());
                    return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(date) + r.getMessage();
                }
            });
            sLogger = java.util.logging.Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
            sLogger.addHandler(fileHandler);
            sLogger.setLevel(Level.ALL);
            sLogger.setUseParentHandlers(false);
            Log.d("Clocks$", "init success");
        } catch (IOException e) {
            Log.d("Clocks$", "init failure");
        }
    }

    private static String getLogFilePath(Context context) {
        if (!(!sLogFilePath.equals("") || context == null || context.getFilesDir() == null)) {
            File parentFile = context.getFilesDir().getParentFile();
            if (parentFile != null) {
                sLogFilePath = parentFile.getAbsolutePath() + "/" + "Log";
            }
        }
        File dir = new File(sLogFilePath);
        if (!(dir.exists() || dir.mkdir())) {
            m46e("Clocks$", "getLogFilePath, Failed to make directory");
        }
        return sLogFilePath + "/";
    }

    public static StringBuilder getLogText(Context context) {
        File[] files = new File(getLogFilePath(context)).listFiles();
        if (files == null || files.length <= 0) {
            m45d("Logger", "logText path is empty.");
            return null;
        }
        StringBuilder text = new StringBuilder();
        for (File openFile : files) {
            String fileName = openFile.getName();
            if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("log") && openFile.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(openFile));
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        text.append(line);
                        text.append("\n");
                    }
                    br.close();
                    text.append("\n");
                } catch (IOException e) {
                    m46e("Logger", "getLogText : " + e);
                    return null;
                }
            }
        }
        Log.d("Logger", "getLogText = " + text);
        return text;
    }
}
