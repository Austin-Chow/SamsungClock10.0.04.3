package com.sec.android.app.clockpackage.worldclock.callback;

public interface TimeZoneListViewModelListener {
    boolean isEditMode();

    void setFabButtonVisible(boolean z);

    void startAddCityActivity();
}
