package com.samsung.context.sdk.samsunganalytics.internal.connection;

public enum Directory {
    DEVICE_CONTROLLER_DIR("/v1/quotas"),
    DATA_DELETE("/app/delete"),
    DLS_DIR(""),
    DLS_DIR_BAT("");
    
    String directory;

    private Directory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
