package com.sec.android.app.clockpackage.worldclock.viewmodel;

import com.sec.android.app.clockpackage.common.view.ClockSubAppBar.SubAppBarPressListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WeatherSettingsActivity$$Lambda$1 implements SubAppBarPressListener {
    private final WeatherSettingsActivity arg$1;

    private WeatherSettingsActivity$$Lambda$1(WeatherSettingsActivity weatherSettingsActivity) {
        this.arg$1 = weatherSettingsActivity;
    }

    public static SubAppBarPressListener lambdaFactory$(WeatherSettingsActivity weatherSettingsActivity) {
        return new WeatherSettingsActivity$$Lambda$1(weatherSettingsActivity);
    }

    @Hidden
    public void setChecked(boolean z) {
        this.arg$1.lambda$addSubAppBarView$0(z);
    }
}
