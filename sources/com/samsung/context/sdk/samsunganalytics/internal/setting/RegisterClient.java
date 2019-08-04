package com.samsung.context.sdk.samsunganalytics.internal.setting;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RegisterClient implements AsyncTaskClient {
    private Context context;
    private Map<String, Set<String>> map;

    public RegisterClient(Context context, Map<String, Set<String>> map) {
        this.context = context;
        this.map = map;
    }

    public void run() {
        SharedPreferences saPref = Preferences.getPreferences(this.context);
        for (String key : saPref.getStringSet("AppPrefs", new HashSet())) {
            saPref.edit().remove(key).apply();
        }
        saPref.edit().remove("AppPrefs").apply();
        Set<String> appPrefNames = new HashSet();
        Set<String> nonexistentKeys = new HashSet();
        for (Entry<String, Set<String>> entry : this.map.entrySet()) {
            String prefName = (String) entry.getKey();
            appPrefNames.add(prefName);
            saPref.edit().putStringSet(prefName, (Set) entry.getValue()).apply();
            SharedPreferences pref = this.context.getSharedPreferences(prefName, 0);
            for (String key2 : (Set) entry.getValue()) {
                if (!pref.contains(key2)) {
                    nonexistentKeys.add(key2);
                }
            }
        }
        saPref.edit().putStringSet("AppPrefs", appPrefNames).apply();
        if (!nonexistentKeys.isEmpty()) {
            Debug.LogENG("Keys not found " + nonexistentKeys);
        }
    }

    public int onFinish() {
        return 0;
    }
}
