package com.sec.android.app.clockpackage.stopwatch.callback;

public interface StopwatchManagerListener {
    void onAddLap();

    void onReset();

    void onSetLapList();

    void onSetViewState();

    void onUpdateTimeView();
}
