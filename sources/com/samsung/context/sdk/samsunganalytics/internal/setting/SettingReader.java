package com.samsung.context.sdk.samsunganalytics.internal.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter.Depth;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SettingReader {
    private final String THREE_DEPTH_ENTITY_DELIMETER = Depth.THREE_DEPTH.getCollectionDLM();
    private final String TWO_DEPTH_DELIMETER = Depth.TWO_DEPTH.getKeyValueDLM();
    private final String TWO_DEPTH_ENTITY_DELIMETER = Depth.TWO_DEPTH.getCollectionDLM();
    private Set<String> appPrefNames;
    private Context context;

    public SettingReader(Context context) {
        this.context = context;
        this.appPrefNames = Preferences.getPreferences(context).getStringSet("AppPrefs", new HashSet());
    }

    private SharedPreferences getPreference(String prefName) {
        return this.context.getSharedPreferences(prefName, 0);
    }

    private Set<String> getKeySet(String prefName) {
        return Preferences.getPreferences(this.context).getStringSet(prefName, new HashSet());
    }

    private List<String> readFromApp() {
        if (this.appPrefNames.isEmpty()) {
            return null;
        }
        List<String> statusSet = new ArrayList();
        String settingInfo = "";
        for (String prefName : this.appPrefNames) {
            SharedPreferences pref = getPreference(prefName);
            Set<String> registeredkeySet = getKeySet(prefName);
            for (Entry<String, ?> entry : pref.getAll().entrySet()) {
                if (registeredkeySet.contains(entry.getKey())) {
                    String entity = "";
                    Class type = entry.getValue().getClass();
                    if (type.equals(Integer.class) || type.equals(Float.class) || type.equals(Long.class) || type.equals(String.class) || type.equals(Boolean.class)) {
                        entity = entity + ((String) entry.getKey()) + this.TWO_DEPTH_DELIMETER + entry.getValue();
                    } else {
                        String list = null;
                        Set<String> values = (Set) entry.getValue();
                        entity = entity + ((String) entry.getKey()) + this.TWO_DEPTH_DELIMETER;
                        for (String val : values) {
                            if (!TextUtils.isEmpty(list)) {
                                list = list + this.THREE_DEPTH_ENTITY_DELIMETER;
                            }
                            list = list + val;
                        }
                        entity = entity + list;
                    }
                    if (settingInfo.length() + entity.length() > 512) {
                        statusSet.add(settingInfo);
                        settingInfo = "";
                    } else if (!TextUtils.isEmpty(settingInfo)) {
                        settingInfo = settingInfo + this.TWO_DEPTH_ENTITY_DELIMETER;
                    }
                    settingInfo = settingInfo + entity;
                }
            }
        }
        if (settingInfo.length() == 0) {
            return statusSet;
        }
        statusSet.add(settingInfo);
        return statusSet;
    }

    public List<String> read() {
        List<String> statusSet = readFromApp();
        Map<String, String> map = getPreference("SASettingPref").getAll();
        if (!(map == null || map.isEmpty())) {
            statusSet.add(new Delimiter().makeDelimiterString(map, Depth.TWO_DEPTH));
        }
        return statusSet;
    }
}
