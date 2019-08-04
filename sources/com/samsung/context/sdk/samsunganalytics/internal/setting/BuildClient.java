package com.samsung.context.sdk.samsunganalytics.internal.setting;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.sender.Sender;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildClient implements AsyncTaskClient {
    private static boolean firstTry = true;
    private Configuration config;
    private Context context;
    private List<String> settings;

    public BuildClient(Context context, Configuration config) {
        this.context = context;
        this.config = config;
    }

    public static boolean isFirstTry() {
        return firstTry;
    }

    public static void setFirstTry(boolean firstTry) {
        firstTry = firstTry;
    }

    public void run() {
        this.settings = new SettingReader(this.context).read();
    }

    public int onFinish() {
        if (this.settings.isEmpty()) {
            Debug.LogD("Setting Sender", "No status log");
        } else {
            Map<String, String> logs = new HashMap();
            logs.put("ts", String.valueOf(System.currentTimeMillis()));
            logs.put("t", "st");
            long sent_time_in_millis = 0;
            for (String setting : this.settings) {
                logs.put("sti", setting);
                if (Sender.get(this.context, Tracker.sdkPolicy.getSenderType(), this.config).send(logs) == 0) {
                    Debug.LogD("Setting Sender", "Send success");
                    sent_time_in_millis = System.currentTimeMillis();
                } else {
                    Debug.LogD("Setting Sender", "Send fail");
                }
            }
            if (sent_time_in_millis != 0) {
                Preferences.getPreferences(this.context).edit().putLong("status_sent_date", sent_time_in_millis).apply();
            }
        }
        return 0;
    }
}
