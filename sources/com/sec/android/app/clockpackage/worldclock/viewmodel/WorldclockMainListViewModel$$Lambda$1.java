package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorFinishedListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListViewModel$$Lambda$1 implements ItemAnimatorFinishedListener {
    private final WorldclockMainListViewModel arg$1;

    private WorldclockMainListViewModel$$Lambda$1(WorldclockMainListViewModel worldclockMainListViewModel) {
        this.arg$1 = worldclockMainListViewModel;
    }

    public static ItemAnimatorFinishedListener lambdaFactory$(WorldclockMainListViewModel worldclockMainListViewModel) {
        return new WorldclockMainListViewModel$$Lambda$1(worldclockMainListViewModel);
    }

    @Hidden
    public void onAnimationsFinished() {
        this.arg$1.lambda$startDeleteAnimation$1();
    }
}
