package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockFragment$$Lambda$1 implements Runnable {
    private final WorldclockFragment arg$1;

    private WorldclockFragment$$Lambda$1(WorldclockFragment worldclockFragment) {
        this.arg$1 = worldclockFragment;
    }

    public static Runnable lambdaFactory$(WorldclockFragment worldclockFragment) {
        return new WorldclockFragment$$Lambda$1(worldclockFragment);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$launchSmartTip$0();
    }
}
