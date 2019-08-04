package com.sec.android.app.clockpackage.worldclock.callback;

import com.sec.android.app.clockpackage.worldclock.model.City;

public interface SearchBarViewListener {
    void onCityTouched(int i);

    void onClearPopupTalkBackFocus();

    void onHideAllPopup(boolean z);

    void onMoveToCity(City city);

    void onSetZoomInOutVisibility();

    void onUpdateWeatherLogo();

    void setSgiVisibility(int i);
}
