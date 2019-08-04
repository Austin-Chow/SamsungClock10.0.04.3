package com.sec.android.app.clockpackage.worldclock.callback;

import android.view.KeyEvent;
import android.view.View;

public interface WorldclockMainListListener {
    int getCurrentPosition(View view);

    boolean isActionMode();

    boolean isMinimumSize();

    void onClick(View view, int i);

    void onContextItemSelected(int i);

    void onDrop();

    void onEnterKeyEvent(View view, int i, KeyEvent keyEvent);

    boolean onLongClick(View view, int i);

    void requestFocusToSelectAll();
}
