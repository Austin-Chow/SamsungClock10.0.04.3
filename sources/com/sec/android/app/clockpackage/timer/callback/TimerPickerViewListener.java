package com.sec.android.app.clockpackage.timer.callback;

public interface TimerPickerViewListener {
    long getSelectedPresetId();

    boolean isActionMode();

    void onCheckInputData();

    void onEditModeChanged();

    void onKeypadAnimation(boolean z);

    void onSetSelectedPresetId(long j);

    void onTimeChanged();
}
