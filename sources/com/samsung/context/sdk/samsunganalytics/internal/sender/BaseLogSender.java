package com.samsung.context.sdk.samsunganalytics.internal.sender;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.BuildConfig;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.device.DeviceInfo;
import com.samsung.context.sdk.samsunganalytics.internal.executor.Executor;
import com.samsung.context.sdk.samsunganalytics.internal.executor.SingleThreadExecutor;
import com.samsung.context.sdk.samsunganalytics.internal.sender.Sender.Type;
import com.samsung.context.sdk.samsunganalytics.internal.sender.buffering.Manager;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter.Depth;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.util.Map;

public abstract class BaseLogSender implements LogSender {
    protected Configuration configuration;
    protected Context context;
    protected Delimiter<String, String> delimiterUtil;
    protected DeviceInfo deviceInfo;
    protected Executor executor = SingleThreadExecutor.getInstance();
    protected Manager manager;

    public BaseLogSender(Context context, Configuration configuration) {
        this.context = context.getApplicationContext();
        this.configuration = configuration;
        this.deviceInfo = new DeviceInfo(context);
        this.delimiterUtil = new Delimiter();
        this.manager = Manager.getInstance(context, configuration);
    }

    protected Map<String, String> setCommonParamToLog(Map<String, String> log) {
        if (Tracker.sdkPolicy.getSenderType() != Type.DMA) {
            log.put("la", this.deviceInfo.getLanguage());
            if (!TextUtils.isEmpty(this.deviceInfo.getMcc())) {
                log.put("mcc", this.deviceInfo.getMcc());
            }
            if (!TextUtils.isEmpty(this.deviceInfo.getMnc())) {
                log.put("mnc", this.deviceInfo.getMnc());
            }
            log.put("dm", this.deviceInfo.getDeviceModel());
            log.put("auid", this.configuration.getDeviceId());
            log.put("do", this.deviceInfo.getAndroidVersion());
            log.put("av", this.deviceInfo.getAppVersionName());
            log.put("uv", this.configuration.getVersion());
            log.put("at", String.valueOf(this.configuration.getAuidType()));
            log.put("fv", this.deviceInfo.getFirmwareVersion());
            log.put("tid", this.configuration.getTrackingId());
        }
        log.put("v", BuildConfig.VERSION_NAME);
        log.put("tz", this.deviceInfo.getTimeZoneOffset());
        if (this.configuration.isUseAnonymizeIp()) {
            log.put("aip", "1");
            String overIp = this.configuration.getOverrideIp();
            if (overIp != null) {
                log.put("oip", overIp);
            }
        }
        return log;
    }

    protected String makeBodyString(Map<String, String> logs) {
        return this.delimiterUtil.makeDelimiterString(logs, Depth.ONE_DEPTH);
    }

    protected void insert(Map<String, String> log) {
        this.manager.insert(new SimpleLog((String) log.get("t"), Long.valueOf((String) log.get("ts")).longValue(), makeBodyString(setCommonParamToLog(log)), getLogType(log)));
    }

    protected LogType getLogType(Map<String, String> log) {
        return Utils.getTypeForServer((String) log.get("t"));
    }
}
