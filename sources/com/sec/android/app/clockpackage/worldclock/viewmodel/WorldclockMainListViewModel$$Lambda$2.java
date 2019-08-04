package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListViewModel$$Lambda$2 implements OnTouchListener {
    private final WorldclockMainListViewModel arg$1;

    private WorldclockMainListViewModel$$Lambda$2(WorldclockMainListViewModel worldclockMainListViewModel) {
        this.arg$1 = worldclockMainListViewModel;
    }

    public static OnTouchListener lambdaFactory$(WorldclockMainListViewModel worldclockMainListViewModel) {
        return new WorldclockMainListViewModel$$Lambda$2(worldclockMainListViewModel);
    }

    @Hidden
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.arg$1.lambda$onUpdateEmptyView$2(view, motionEvent);
    }
}
