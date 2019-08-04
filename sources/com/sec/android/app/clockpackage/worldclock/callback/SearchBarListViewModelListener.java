package com.sec.android.app.clockpackage.worldclock.callback;

import com.sec.android.app.clockpackage.worldclock.model.City;

public interface SearchBarListViewModelListener {
    void onClearPopupTalkBackFocus();

    void onHideInput();

    void onSelectCity(City city, boolean z);

    void onShowList();
}
