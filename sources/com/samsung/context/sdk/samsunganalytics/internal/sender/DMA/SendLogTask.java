package com.samsung.context.sdk.samsunganalytics.internal.sender.DMA;

import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Tracker;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.sec.android.diagmonagent.sa.IDMAInterface;

public class SendLogTask implements AsyncTaskClient {
    private Configuration configuration;
    private IDMAInterface dmaInterface;
    private SimpleLog log;

    SendLogTask(IDMAInterface dmaInterface, Configuration configuration, SimpleLog log) {
        this.log = log;
        this.dmaInterface = dmaInterface;
        this.configuration = configuration;
    }

    public void run() {
        try {
            this.dmaInterface.sendLog(Tracker.sdkPolicy.ordinal(), this.configuration.getTrackingId(), this.log.getType().getAbbrev(), this.log.getTimestamp(), this.log.getData());
        } catch (Exception e) {
            Debug.LogException(e.getClass(), e);
        }
    }

    public int onFinish() {
        return 0;
    }
}
