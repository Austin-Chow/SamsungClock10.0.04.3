package com.samsung.android.sdk;

public class SsdkUnsupportedException extends Exception {
    private int mErrorType = 0;

    public SsdkUnsupportedException(String message, int errorType) {
        super(message);
        this.mErrorType = errorType;
    }
}
