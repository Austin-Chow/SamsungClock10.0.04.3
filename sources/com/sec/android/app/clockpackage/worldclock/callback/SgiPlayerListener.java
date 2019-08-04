package com.sec.android.app.clockpackage.worldclock.callback;

import android.view.MotionEvent;
import android.view.View;
import com.sec.android.app.clockpackage.worldclock.model.City;

public interface SgiPlayerListener {
    void cityTouchedInGlobe(int i);

    City onCityUnderSelection();

    boolean touch(View view, MotionEvent motionEvent);
}
