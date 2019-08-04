package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockListViewActionMode$$Lambda$3 implements Runnable {
    private final WorldclockListViewActionMode arg$1;
    private final boolean arg$2;
    private final int arg$3;
    private final int arg$4;

    private WorldclockListViewActionMode$$Lambda$3(WorldclockListViewActionMode worldclockListViewActionMode, boolean z, int i, int i2) {
        this.arg$1 = worldclockListViewActionMode;
        this.arg$2 = z;
        this.arg$3 = i;
        this.arg$4 = i2;
    }

    public static Runnable lambdaFactory$(WorldclockListViewActionMode worldclockListViewActionMode, boolean z, int i, int i2) {
        return new WorldclockListViewActionMode$$Lambda$3(worldclockListViewActionMode, z, i, i2);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$setToolbar$2(this.arg$2, this.arg$3, this.arg$4);
    }
}
