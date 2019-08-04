package com.samsung.context.sdk.samsunganalytics.internal;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.DBOpenHelper;
import com.samsung.context.sdk.samsunganalytics.UserAgreement;
import com.samsung.context.sdk.samsunganalytics.internal.connection.Directory;
import com.samsung.context.sdk.samsunganalytics.internal.connection.Domain;
import com.samsung.context.sdk.samsunganalytics.internal.device.DeviceInfo;
import com.samsung.context.sdk.samsunganalytics.internal.exception.DiagMonLogger;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.SingleThreadExecutor;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyType;
import com.samsung.context.sdk.samsunganalytics.internal.policy.PolicyUtils;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.sender.Sender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.Sender.Type;
import com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.Manager;
import com.samsung.context.sdk.samsunganalytics.internal.setting.BuildClient;
import com.samsung.context.sdk.samsunganalytics.internal.setting.RegisterClient;
import com.samsung.context.sdk.samsunganalytics.internal.terms.RegisterTask;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

public class Tracker {
    public static PolicyType sdkPolicy;
    private Application application;
    private Configuration configuration;
    private Intent getCFIDIntent;
    private boolean isEnableAutoActivityTracking = false;
    private boolean isEnableUncaughtExceptionLogging = false;
    private UncaughtExceptionHandler originUncaughtExceptionHandler;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.Tracker$2 */
    class C04612 extends BroadcastReceiver {
        C04612() {
        }

        public void onReceive(Context context, Intent intent) {
            Debug.LogENG("receive BR");
            Tracker.this.sendSettingLogs();
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.Tracker$3 */
    class C04623 implements Callback<Void, Boolean> {
        C04623() {
        }

        public Void onResult(Boolean useDbBuffer) {
            if (useDbBuffer.booleanValue()) {
                DBOpenHelper dbOpenHelper = Tracker.this.configuration.getDbOpenHelper();
                if (dbOpenHelper == null) {
                    Manager.getInstance(Tracker.this.application.getApplicationContext(), Tracker.this.configuration).enableDatabaseBuffering(Tracker.this.application.getApplicationContext());
                } else {
                    Manager.getInstance(Tracker.this.application.getApplicationContext(), Tracker.this.configuration).enableDatabaseBuffering(dbOpenHelper);
                }
            }
            return null;
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.Tracker$6 */
    class C04636 extends BroadcastReceiver {
        C04636() {
        }

        public void onReceive(Context context, Intent intent) {
            int auidType;
            String did = intent.getStringExtra("DID");
            if (TextUtils.isEmpty(did)) {
                did = Tracker.this.generateRandomDeviceId();
                auidType = 1;
                Debug.LogD("Get CF id empty");
            } else {
                auidType = 0;
                Debug.LogD("Get CF id");
            }
            Tracker.this.setDeviceId(did, auidType);
            Tracker.this.application.unregisterReceiver(this);
        }
    }

    public Tracker(final Application application, Configuration configuration) {
        this.application = application;
        this.configuration = configuration;
        sdkPolicy = configuration.isEnableUseInAppLogging() ? PolicyType.CUSTOM_TERMS : PolicyType.DIAGNOSTIC_TERMS;
        if (!TextUtils.isEmpty(configuration.getDeviceId())) {
            this.configuration.setAuidType(2);
        } else if (!loadDeviceId() && configuration.isEnableAutoDeviceId()) {
            if (sdkPolicy.equals(PolicyType.CUSTOM_TERMS)) {
                setDeviceId(generateRandomDeviceId(), 1);
            } else if (sdkPolicy.getSenderType() == Type.DLC && !getCFDeviceId()) {
                setDeviceId(generateRandomDeviceId(), 1);
            }
        }
        if (sdkPolicy.needQuota()) {
            getPolicy();
        }
        if (sdkPolicy == PolicyType.DIAGNOSTIC_TERMS) {
            this.configuration.setUserAgreement(new UserAgreement() {
                public boolean isAgreement() {
                    return Utils.isDiagnosticAgree(application.getApplicationContext());
                }
            });
        }
        if (isUserAgreement()) {
            if (configuration.isEnableFastReady()) {
                Sender.get(application, sdkPolicy.getSenderType(), configuration);
            }
            sendSettingLogs();
        }
        sendPreviousUserAgreementState();
        Debug.LogD("Tracker", "Tracker start:2.00.003" + sdkPolicy.getSenderType().name());
    }

    private void registerReceiver() {
        if (BuildClient.isFirstTry()) {
            BuildClient.setFirstTry(false);
        }
        Debug.LogENG("register BR");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        this.application.getApplicationContext().registerReceiver(new C04612(), filter);
    }

    private void getPolicy() {
        SharedPreferences pref = Preferences.getPreferences(this.application);
        Domain.DLS.setDomain(pref.getString("dom", ""));
        Directory.DLS_DIR.setDirectory(pref.getString("uri", ""));
        Directory.DLS_DIR_BAT.setDirectory(pref.getString("bat-uri", ""));
        if (PolicyUtils.isPolicyExpired(this.application.getApplicationContext())) {
            PolicyUtils.getPolicy(this.application, this.configuration, SingleThreadExecutor.getInstance(), new DeviceInfo(this.application), new C04623());
        }
    }

    public void enableUncaughtExceptionLogging(String serviceID, boolean wifiOnly, boolean useDiagnostic) {
        this.isEnableUncaughtExceptionLogging = true;
        this.originUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (TextUtils.isEmpty(serviceID)) {
            serviceID = this.configuration.getTrackingId();
        }
        Thread.setDefaultUncaughtExceptionHandler(new DiagMonLogger(this.application, this.configuration, this.originUncaughtExceptionHandler, serviceID, useDiagnostic, wifiOnly));
    }

    public int sendLog(Map<String, String> log, boolean isSync) {
        if (!isUserAgreement()) {
            Debug.LogD("user do not agree");
            return -2;
        } else if (log == null || log.isEmpty()) {
            Debug.LogD("Failure to send Logs : No data");
            return -3;
        } else if (!checkDeviceId()) {
            if (this.getCFIDIntent != null) {
                this.application.sendBroadcast(this.getCFIDIntent);
            }
            return -5;
        } else if (((String) log.get("t")).equals("pp") && !isSendProperty()) {
            return -9;
        } else {
            if (isSync) {
                return Sender.get(this.application, sdkPolicy.getSenderType(), this.configuration).sendSync(log);
            }
            return Sender.get(this.application, sdkPolicy.getSenderType(), this.configuration).send(log);
        }
    }

    private boolean compareCFVersion(String CF_PKG) {
        try {
            StringTokenizer stk = new StringTokenizer(this.application.getApplicationContext().getPackageManager().getPackageInfo(CF_PKG, 0).versionName, ".");
            int major = Integer.parseInt(stk.nextToken());
            int minor = Integer.parseInt(stk.nextToken());
            int micro = Integer.parseInt(stk.nextToken());
            if (major < 2) {
                Debug.LogENG("CF version < 2.0.9");
                return false;
            } else if (major != 2 || minor != 0 || micro >= 9) {
                return true;
            } else {
                Debug.LogENG("CF version < 2.0.9");
                return false;
            }
        } catch (Exception e) {
            Debug.LogException(getClass(), e);
            return false;
        }
    }

    private void setDeviceId(String did, int auidType) {
        Preferences.getPreferences(this.application.getApplicationContext()).edit().putString("deviceId", did).putInt("auidType", auidType).apply();
        this.configuration.setAuidType(auidType);
        this.configuration.setDeviceId(did);
    }

    private boolean getCFDeviceId() {
        String CF_PKG = "com.samsung.android.providers.context";
        String REQUEST_ID = ".log.action.REQUEST_DID";
        String GET_ID = ".log.action.GET_DID";
        String EXTRA_PKGNAME = "PKGNAME";
        if (!Validation.isLoggingEnableDevice() || !sdkPolicy.equals(PolicyType.DIAGNOSTIC_TERMS) || !TextUtils.isEmpty(this.configuration.getUserId()) || !compareCFVersion("com.samsung.android.providers.context")) {
            return false;
        }
        this.application.registerReceiver(new C04636(), new IntentFilter("com.samsung.android.providers.context.log.action.GET_DID"));
        this.getCFIDIntent = new Intent("com.samsung.android.providers.context.log.action.REQUEST_DID");
        this.getCFIDIntent.putExtra("PKGNAME", this.application.getPackageName());
        this.getCFIDIntent.setPackage("com.samsung.android.providers.context");
        this.application.sendBroadcast(this.getCFIDIntent);
        Debug.LogD("request CF id");
        return true;
    }

    private boolean loadDeviceId() {
        SharedPreferences pref = Preferences.getPreferences(this.application);
        String deviceId = pref.getString("deviceId", "");
        int auidType = pref.getInt("auidType", -1);
        if (TextUtils.isEmpty(deviceId) || deviceId.length() != 32 || auidType == -1) {
            return false;
        }
        this.configuration.setAuidType(auidType);
        this.configuration.setDeviceId(deviceId);
        return true;
    }

    private String generateRandomDeviceId() {
        String ALLOWED_CHARACTERS = "0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        byte[] output = new byte[16];
        StringBuilder sb = new StringBuilder(32);
        int i = 0;
        while (i < 32) {
            random.nextBytes(output);
            try {
                sb.append("0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int) (Math.abs(new BigInteger(output).longValue()) % ((long) "0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length()))));
                i++;
            } catch (Exception e) {
                Debug.LogException(getClass(), e);
                return null;
            }
        }
        return sb.toString();
    }

    public void registerSettingPref(Map<String, Set<String>> map) {
        SingleThreadExecutor.getInstance().execute(new RegisterClient(this.application.getApplicationContext(), map));
        if (!BuildClient.isFirstTry() || !this.configuration.isAlwaysRunningApp()) {
            return;
        }
        if (this.configuration.isEnableUseInAppLogging() || Validation.isLoggingEnableDevice()) {
            registerReceiver();
        }
    }

    private void sendSettingLogs() {
        if (!isUserAgreement()) {
            Debug.LogD("user do not agree setting");
        } else if (Utils.compareDays(7, Long.valueOf(Preferences.getPreferences(this.application).getLong("status_sent_date", 0)))) {
            Debug.LogD("send setting");
            SingleThreadExecutor.getInstance().execute(new BuildClient(this.application, this.configuration));
        } else {
            Debug.LogD("do not send setting < 7days");
        }
    }

    private boolean isUserAgreement() {
        return this.configuration.getUserAgreement().isAgreement();
    }

    private boolean checkDeviceId() {
        if (sdkPolicy.getSenderType() == Type.DMA || !TextUtils.isEmpty(this.configuration.getDeviceId())) {
            return true;
        }
        Debug.LogD("did is empty");
        return false;
    }

    private boolean isSendProperty() {
        if (!sdkPolicy.isEnableProperty()) {
            Debug.LogD("property disable " + sdkPolicy.getSenderType());
            return false;
        } else if (Utils.compareDays(1, Long.valueOf(Preferences.getPreferences(this.application).getLong("property_sent_date", 0)))) {
            Preferences.getPreferences(this.application).edit().putLong("property_sent_date", System.currentTimeMillis()).apply();
            return true;
        } else {
            Debug.LogD("do not send property < 1day");
            return false;
        }
    }

    private void sendPreviousUserAgreementState() {
        final SharedPreferences pref = this.application.getSharedPreferences("SATerms", 0);
        for (Entry<String, ?> entry : pref.getAll().entrySet()) {
            final String deviceId = (String) entry.getKey();
            SingleThreadExecutor.getInstance().execute(new RegisterTask(this.configuration.getTrackingId(), deviceId, ((Long) entry.getValue()).longValue(), new AsyncTaskCallback() {
                public void onSuccess(int code, String param, String param2, String param3) {
                    pref.edit().remove(deviceId).apply();
                }

                public void onFail(int code, String param, String param2, String param3) {
                }
            }));
        }
    }
}
