package com.sec.android.app.clockpackage.timer.callback;

public interface TimerBixbyActionListener {
    void onCancel();

    boolean onRestartAlert();

    void onSetInputTime(long j);

    String onSetSelectedPreset(String str, long j);

    void onStart();

    void onStop();
}
