package com.sec.android.app.clockpackage;

import android.app.Application;
import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.SettingPrefBuilder;
import com.samsung.context.sdk.samsunganalytics.SamsungAnalytics;
import com.sec.android.app.clockpackage.bixby.BixbyDeepLink;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.NotificationChannelUtils;
import java.util.HashSet;

public class ClockPackageApplication extends Application {
    private final String SA_CLOCKPACKAGE_UI_VER = "10.0";
    private final String SA_TRACKING_ID = "781-399-579798";
    private final String TAG = "ClockPackageApplication";

    /* renamed from: com.sec.android.app.clockpackage.ClockPackageApplication$1 */
    class C04801 implements Runnable {
        C04801() {
        }

        public void run() {
            NotificationChannelUtils.createAllChannels(ClockPackageApplication.this.getApplicationContext());
            try {
                Class.forName("android.os.UserManager").getMethod("get", new Class[]{Context.class}).invoke(null, new Object[]{ClockPackageApplication.this.getApplicationContext()});
            } catch (Exception e) {
                Log.secE("ClockPackageApplication", "exception during invoking UserManager.get()");
            }
            if (Feature.isBixbySupported()) {
                BixbyDeepLink bixbyDeepLink = new BixbyDeepLink();
                bixbyDeepLink.registerInstanceToCapsule(ClockPackageApplication.this.getApplicationContext());
                bixbyDeepLink.registerActionHandler();
            }
            SettingPrefBuilder settingPrefBuilder = new SettingPrefBuilder();
            HashSet<String> statusSet = new HashSet();
            statusSet.add("5001");
            statusSet.add("5101");
            statusSet.add("5102");
            statusSet.add("5111");
            statusSet.add("5115");
            statusSet.add("6501");
            statusSet.add("6504");
            settingPrefBuilder.addKeys("SASettingPref", statusSet);
            SamsungAnalytics.getInstance().registerSettingPref(settingPrefBuilder.build());
            SamsungAnalytics.getInstance().enableUncaughtExceptionLogging("clj47h9zhd");
        }
    }

    public void onCreate() {
        super.onCreate();
        SamsungAnalytics.setConfiguration(this, new Configuration().setTrackingId("781-399-579798").setVersion("10.0").enableAutoDeviceId());
        new Thread(new C04801()).start();
    }
}
