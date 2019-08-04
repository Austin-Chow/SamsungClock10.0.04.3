package com.sec.android.app.clockpackage.timer.callback;

public interface TimerManagerListener {
    void onCancel();

    void onResetViewToStart();

    void onSetStartMode(int i, String str);

    void onSetViewState();

    void onStart();

    void onUpdateTime();
}
