package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$5 implements OnTouchListener {
    private final WorldclockMainListAdapter arg$1;
    private final WorldclockViewHolder arg$2;

    private WorldclockMainListAdapter$$Lambda$5(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        this.arg$1 = worldclockMainListAdapter;
        this.arg$2 = worldclockViewHolder;
    }

    public static OnTouchListener lambdaFactory$(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        return new WorldclockMainListAdapter$$Lambda$5(worldclockMainListAdapter, worldclockViewHolder);
    }

    @Hidden
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.arg$1.lambda$setReorder$5(this.arg$2, view, motionEvent);
    }
}
