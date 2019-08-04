package com.samsung.context.sdk.samsunganalytics.internal.sender;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.sender.DLC.DLCLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.DLS.DLSLogSender;
import com.samsung.context.sdk.samsunganalytics.internal.sender.DMA.DMALogSender;

public class Sender {
    private static LogSender logSender;

    public enum Type {
        DLC,
        DLS,
        DMA
    }

    private Sender() {
    }

    public static LogSender get(Context context, Type senderType, Configuration configuration) {
        if (senderType == null) {
            senderType = configuration.isEnableUseInAppLogging() ? Type.DLS : Type.DLC;
        }
        if (logSender == null) {
            synchronized (Sender.class) {
                if (senderType.equals(Type.DLC)) {
                    logSender = new DLCLogSender(context, configuration);
                } else if (senderType.equals(Type.DLS)) {
                    logSender = new DLSLogSender(context, configuration);
                } else if (senderType.equals(Type.DMA)) {
                    logSender = new DMALogSender(context, configuration);
                }
            }
        }
        return logSender;
    }
}
