package com.sec.android.app.clockpackage.worldclock.callback;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

public interface WorldclockMainListViewModelListener {
    void onActionModeFinished();

    void onActionModeUpdate(int i);

    View onGetFragmentView();

    Toolbar onGetToolbar();

    void onLaunchSmartTip();

    void onStartActivityForResult(Intent intent);
}
