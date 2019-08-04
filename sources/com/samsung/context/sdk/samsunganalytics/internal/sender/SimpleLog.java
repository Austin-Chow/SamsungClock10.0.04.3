package com.samsung.context.sdk.samsunganalytics.internal.sender;

public class SimpleLog {
    private String _id;
    private String data;
    private long timestamp;
    private LogType type;

    public SimpleLog(long timestamp, String data, LogType type) {
        this("", timestamp, data, type);
    }

    public SimpleLog(String _id, long timestamp, String data, LogType type) {
        this._id = _id;
        this.timestamp = timestamp;
        this.data = data;
        this.type = type;
    }

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LogType getType() {
        return this.type;
    }

    public void setType(LogType type) {
        this.type = type;
    }
}
