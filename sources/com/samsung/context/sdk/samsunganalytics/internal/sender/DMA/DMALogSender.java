package com.samsung.context.sdk.samsunganalytics.internal.sender.DMA;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.sender.BaseLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter;
import com.samsung.context.sdk.samsunganalytics.internal.util.Delimiter.Depth;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class DMALogSender extends BaseLogSender {
    private DMABinder dmaBinder;
    private int dmaStatus = 0;
    private boolean isReset = false;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.internal.sender.DMA.DMALogSender$1 */
    class C04741 implements Callback<Void, String> {
        C04741() {
        }

        public Void onResult(String did) {
            DMALogSender.this.sendCommon();
            DMALogSender.this.sendAll();
            return null;
        }
    }

    public DMALogSender(Context context, Configuration configuration) {
        super(context, configuration);
        this.dmaBinder = new DMABinder(context, new C04741());
        this.dmaBinder.bind();
    }

    public int send(Map<String, String> log) {
        if (this.dmaBinder.isTokenfail()) {
            return -8;
        }
        if (this.dmaStatus != 0) {
            return this.dmaStatus;
        }
        insert(log);
        if (this.dmaBinder.getDmaInterface() == null) {
            this.dmaBinder.bind();
        } else {
            sendAll();
        }
        if (this.isReset) {
            sendCommon();
            this.isReset = false;
        }
        return this.dmaStatus;
    }

    public int sendSync(Map<String, String> log) {
        return send(log);
    }

    private void sendAll() {
        if (this.dmaStatus == 0) {
            Queue<SimpleLog> queue = this.manager.get();
            while (!queue.isEmpty()) {
                this.executor.execute(new SendLogTask(this.dmaBinder.getDmaInterface(), this.configuration, (SimpleLog) queue.poll()));
            }
        }
    }

    private void sendCommon() {
        Delimiter<String, String> delimiter = new Delimiter();
        Map<String, String> commonMap = new HashMap();
        commonMap.put("av", this.deviceInfo.getAppVersionName());
        commonMap.put("uv", this.configuration.getVersion());
        String common = delimiter.makeDelimiterString(commonMap, Depth.ONE_DEPTH);
        String id = null;
        Map<String, String> idMap = new HashMap();
        if (!TextUtils.isEmpty(this.configuration.getDeviceId())) {
            idMap.put("auid", this.configuration.getDeviceId());
            idMap.put("at", String.valueOf(this.configuration.getAuidType()));
            id = delimiter.makeDelimiterString(idMap, Depth.ONE_DEPTH);
        }
        try {
            this.dmaStatus = this.dmaBinder.getDmaInterface().sendCommon(Tracker.sdkPolicy.ordinal(), this.configuration.getTrackingId(), common, id);
        } catch (Exception e) {
            Debug.LogException(e.getClass(), e);
            this.dmaStatus = -9;
        }
    }
}
