package com.sec.android.app.clockpackage.common.callback;

public interface VolumeProgressListener {
    void onProgressChanged(int i);

    void onStartTrackingTouch();

    void onStopTrackingTouch();
}
