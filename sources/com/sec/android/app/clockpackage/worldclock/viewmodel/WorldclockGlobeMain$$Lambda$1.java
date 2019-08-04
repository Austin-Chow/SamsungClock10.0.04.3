package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockGlobeMain$$Lambda$1 implements Runnable {
    private final WorldclockGlobeMain arg$1;

    private WorldclockGlobeMain$$Lambda$1(WorldclockGlobeMain worldclockGlobeMain) {
        this.arg$1 = worldclockGlobeMain;
    }

    public static Runnable lambdaFactory$(WorldclockGlobeMain worldclockGlobeMain) {
        return new WorldclockGlobeMain$$Lambda$1(worldclockGlobeMain);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$onResume$0();
    }
}
