package com.samsung.context.sdk.samsunganalytics.internal.policy;

import com.samsung.context.sdk.samsunganalytics.internal.sender.Sender.Type;

public enum PolicyType {
    DIAGNOSTIC_TERMS(Type.DLC, false, false),
    CUSTOM_TERMS(Type.DLS, true, true);
    
    private boolean enableUseDBQueue;
    private boolean needQuota;
    private Type senderType;

    private PolicyType(Type senderType, boolean needQuota, boolean enableUseDBQueue) {
        this.senderType = senderType;
        this.needQuota = needQuota;
        this.enableUseDBQueue = enableUseDBQueue;
    }

    public boolean needQuota() {
        return PolicyUtils.hasDMA ? false : this.needQuota;
    }

    public Type getSenderType() {
        return PolicyUtils.hasDMA ? Type.DMA : this.senderType;
    }

    public boolean enableUseDBQueue() {
        return PolicyUtils.hasDMA ? false : this.enableUseDBQueue;
    }

    public boolean isEnableProperty() {
        return getSenderType() != Type.DLC;
    }
}
