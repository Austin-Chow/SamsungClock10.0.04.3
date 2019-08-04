package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WeatherSettingsActivity$$Lambda$3 implements OnItemClickListener {
    private final WeatherSettingsActivity arg$1;

    private WeatherSettingsActivity$$Lambda$3(WeatherSettingsActivity weatherSettingsActivity) {
        this.arg$1 = weatherSettingsActivity;
    }

    public static OnItemClickListener lambdaFactory$(WeatherSettingsActivity weatherSettingsActivity) {
        return new WeatherSettingsActivity$$Lambda$3(weatherSettingsActivity);
    }

    @Hidden
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.arg$1.lambda$setClickListener$2(adapterView, view, i, j);
    }
}
