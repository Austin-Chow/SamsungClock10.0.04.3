package com.sec.android.app.clockpackage.timer.callback;

import android.support.v7.widget.Toolbar;

public interface TimerPresetViewListener {
    boolean isEditMode();

    void onCreateModifyPresetPopup(long j);

    void onDisablePickerEditMode();

    int onGetPickerHeight();

    Toolbar onGetToolbar();

    void onSetPickerTime(int i, int i2, int i3, boolean z);

    void onSetPresetViewVisibility();

    void onSetViewEnabled(boolean z);
}
