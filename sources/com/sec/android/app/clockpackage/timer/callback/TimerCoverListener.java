package com.sec.android.app.clockpackage.timer.callback;

public interface TimerCoverListener {
    void onFinishAlert();

    String onGetContentDescription(int i, int i2, int i3);

    long onGetOffHookElapsedMillis();

    void onRestartAlert();

    void onSendActionTimerStoppedInAlert(String str);
}
