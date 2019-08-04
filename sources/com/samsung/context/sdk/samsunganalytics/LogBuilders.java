package com.samsung.context.sdk.samsunganalytics;

import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter.Depth;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LogBuilders {

    protected static abstract class LogBuilder<T extends LogBuilder> {
        protected Map<String, String> logs;

        protected abstract T getThis();

        private LogBuilder() {
            this.logs = new HashMap();
        }

        public final T set(String paramName, String paramValue) {
            if (paramName != null) {
                this.logs.put(paramName, paramValue);
            }
            return getThis();
        }

        public long getTimeStamp() {
            return System.currentTimeMillis();
        }

        public Map<String, String> build() {
            set("ts", String.valueOf(getTimeStamp()));
            return this.logs;
        }

        public T setScreenView(String screenName) {
            set("pn", screenName);
            return getThis();
        }

        public T setDimension(Map<String, String> dimensions) {
            set("cd", new Delimiter().makeDelimiterString(Validation.checkSizeLimit(dimensions), Depth.TWO_DEPTH));
            return getThis();
        }
    }

    public static class EventBuilder extends LogBuilder<EventBuilder> {
        public EventBuilder() {
            super();
        }

        public /* bridge */ /* synthetic */ long getTimeStamp() {
            return super.getTimeStamp();
        }

        public EventBuilder setEventName(String eventName) {
            if (TextUtils.isEmpty(eventName)) {
                Utils.throwException("Failure to build Log : Event name cannot be null");
            }
            set("en", eventName);
            return this;
        }

        public EventBuilder setEventValue(long eventValue) {
            set("ev", String.valueOf(eventValue));
            return this;
        }

        protected EventBuilder getThis() {
            return this;
        }

        public Map<String, String> build() {
            if (!this.logs.containsKey("en")) {
                Utils.throwException("Failure to build Log : Event name cannot be null");
            }
            set("t", "ev");
            return super.build();
        }
    }

    public static class ScreenViewBuilder extends LogBuilder<ScreenViewBuilder> {
        public ScreenViewBuilder() {
            super();
        }

        public /* bridge */ /* synthetic */ long getTimeStamp() {
            return super.getTimeStamp();
        }

        protected ScreenViewBuilder getThis() {
            return this;
        }

        public Map<String, String> build() {
            if (TextUtils.isEmpty((CharSequence) this.logs.get("pn"))) {
                Utils.throwException("Failure to build Log : Screen name cannot be null");
            } else {
                set("t", "pv");
            }
            return super.build();
        }
    }

    public static class SettingPrefBuilder {
        private Map<String, Set<String>> map = new HashMap();

        private SettingPrefBuilder addAppPref(String prefName) {
            if (!this.map.containsKey(prefName) && !TextUtils.isEmpty(prefName)) {
                this.map.put(prefName, new HashSet());
            } else if (TextUtils.isEmpty(prefName)) {
                Utils.throwException("Failure to build logs [setting preference] : Preference name cannot be null.");
            }
            return this;
        }

        public SettingPrefBuilder addKeys(String prefName, Set<String> keys) {
            if (keys == null || keys.isEmpty()) {
                Utils.throwException("Failure to build logs [setting preference] : Setting keys cannot be null.");
            }
            addAppPref(prefName);
            Set<String> keySet = (Set) this.map.get(prefName);
            for (String newKey : keys) {
                if (!TextUtils.isEmpty(newKey)) {
                    keySet.add(newKey);
                }
            }
            return this;
        }

        public Map<String, Set<String>> build() {
            Debug.LogENG(this.map.toString());
            return this.map;
        }
    }
}
