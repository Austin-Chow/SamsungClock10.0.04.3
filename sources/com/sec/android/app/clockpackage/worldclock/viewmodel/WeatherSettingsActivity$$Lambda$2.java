package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WeatherSettingsActivity$$Lambda$2 implements OnClickListener {
    private final WeatherSettingsActivity arg$1;

    private WeatherSettingsActivity$$Lambda$2(WeatherSettingsActivity weatherSettingsActivity) {
        this.arg$1 = weatherSettingsActivity;
    }

    public static OnClickListener lambdaFactory$(WeatherSettingsActivity weatherSettingsActivity) {
        return new WeatherSettingsActivity$$Lambda$2(weatherSettingsActivity);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$setClickListener$1(view);
    }
}
