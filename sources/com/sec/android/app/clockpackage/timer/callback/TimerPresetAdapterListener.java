package com.sec.android.app.clockpackage.timer.callback;

import android.view.View;

public interface TimerPresetAdapterListener {
    boolean isActionMode();

    void onDeleteContextMenuClick(int i);

    void onItemClick(View view, int i);

    void onItemLongClick(View view, int i);
}
