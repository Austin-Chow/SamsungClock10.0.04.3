package com.sec.android.app.clockpackage.worldclock.callback;

import java.util.List;
import java.util.TimeZone;

public interface TimeZoneConvertorControlViewListener {
    TimeZone getDefaultTimeZone();

    boolean getIsPortraitLayout();

    List<String> getSpinnerArrayList();

    void initItemValue(boolean z);

    void onEditTextModeChanged(boolean z);

    void onRefreshCityListDelayed(long j);

    void onResetSelectedState();

    void onSelectCityItem(int i);

    void onTimeChanged(int i, int i2);
}
