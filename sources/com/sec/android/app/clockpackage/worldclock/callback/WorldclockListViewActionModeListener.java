package com.sec.android.app.clockpackage.worldclock.callback;

import android.support.v7.widget.Toolbar;

public interface WorldclockListViewActionModeListener {
    boolean canNotCheckSelectAll();

    void onActionModeFinished();

    void onActionModeUpdate(int i);

    Toolbar onGetToolbar();

    void onUpdateDeleteButton();
}
