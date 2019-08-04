package com.samsung.context.sdk.samsunganalytics;

import android.app.Application;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyUtils;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.util.Map;
import java.util.Set;

public class SamsungAnalytics {
    private static SamsungAnalytics instance;
    private Tracker tracker = null;

    private SamsungAnalytics(Application context, Configuration configuration) {
        PolicyUtils.checkDMA(context);
        if (!Validation.isValidConfig(context, configuration)) {
            return;
        }
        if (configuration.isEnableUseInAppLogging()) {
            this.tracker = new Tracker(context, configuration);
        } else if (Validation.isLoggingEnableDevice()) {
            this.tracker = new Tracker(context, configuration);
        }
    }

    private static SamsungAnalytics getInstanceAndConfig(Application application, Configuration configuration) {
        if (instance == null || instance.tracker == null) {
            synchronized (SamsungAnalytics.class) {
                instance = new SamsungAnalytics(application, configuration);
            }
        }
        return instance;
    }

    public static void setConfiguration(Application application, Configuration configuration) {
        getInstanceAndConfig(application, configuration);
    }

    public static SamsungAnalytics getInstance() {
        if (instance == null) {
            Utils.throwException("call after setConfiguration() method");
            if (!Utils.isEngBin()) {
                return getInstanceAndConfig(null, null);
            }
        }
        return instance;
    }

    public SamsungAnalytics enableUncaughtExceptionLogging(String serviceID) {
        try {
            this.tracker.enableUncaughtExceptionLogging(serviceID, true, true);
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
        return this;
    }

    public int sendLog(Map<String, String> log) {
        try {
            return this.tracker.sendLog(log, false);
        } catch (NullPointerException e) {
            return -100;
        }
    }

    public void registerSettingPref(Map<String, Set<String>> map) {
        try {
            this.tracker.registerSettingPref(map);
        } catch (NullPointerException e) {
            Debug.LogException(getClass(), e);
        }
    }
}
