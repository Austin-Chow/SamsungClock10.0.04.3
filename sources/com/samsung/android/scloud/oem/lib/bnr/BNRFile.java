package com.samsung.android.scloud.oem.lib.bnr;

public class BNRFile {
    private boolean isExternal;
    private String path;
    private long size;
    private long timestamp;

    public long getTimeStamp() {
        return this.timestamp;
    }

    public String getPath() {
        return this.path;
    }

    public boolean getisExternal() {
        return this.isExternal;
    }

    public long getSize() {
        return this.size;
    }
}
