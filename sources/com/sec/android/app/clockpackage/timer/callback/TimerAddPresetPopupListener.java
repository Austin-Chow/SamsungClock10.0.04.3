package com.sec.android.app.clockpackage.timer.callback;

import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;

public interface TimerAddPresetPopupListener {
    long getDuplicatedPresetId(String str, int i, int i2, int i3);

    boolean isMultiWindowMode();

    void onDisablePickerEditMode();

    TimerPresetItem onGetPresetItemById(long j);

    TimerPresetItem onGetPresetItemByPosition(int i);

    void onSetBackupTime(int i, int i2, int i3);

    void onSetPickerTime(int i, int i2, int i3);

    void onSetSelectedPresetId(long j);

    void onSetSoftInputMode(boolean z);

    void onUpdatePresetView(boolean z);
}
