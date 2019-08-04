package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListViewModel$$Lambda$3 implements Runnable {
    private final WorldclockMainListViewModel arg$1;

    private WorldclockMainListViewModel$$Lambda$3(WorldclockMainListViewModel worldclockMainListViewModel) {
        this.arg$1 = worldclockMainListViewModel;
    }

    public static Runnable lambdaFactory$(WorldclockMainListViewModel worldclockMainListViewModel) {
        return new WorldclockMainListViewModel$$Lambda$3(worldclockMainListViewModel);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$refreshCityListDelayed$3();
    }
}
