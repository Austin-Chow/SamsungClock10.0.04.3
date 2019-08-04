package com.sec.android.app.clockpackage.timer.callback;

import android.support.v7.widget.Toolbar;

public interface TimerPresetViewActionModeListener {
    Toolbar onGetToolbar();

    void onSetViewEnabled(boolean z);

    void onUpdateActionModeMenu();
}
