package com.sec.android.app.clockpackage.worldclock.callback;

import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;

public interface PopupViewListener {
    float onCityCardOffset(City city);

    int onGetCityUniqueIdSel();

    void onHideSoftInput();

    void onSaveDB(City city, WorldclockCityWeatherInfo worldclockCityWeatherInfo);

    void onShowCityUnderSelection(int i);
}
