package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnLongClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$2 implements OnLongClickListener {
    private final WorldclockMainListAdapter arg$1;
    private final WorldclockViewHolder arg$2;

    private WorldclockMainListAdapter$$Lambda$2(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        this.arg$1 = worldclockMainListAdapter;
        this.arg$2 = worldclockViewHolder;
    }

    public static OnLongClickListener lambdaFactory$(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        return new WorldclockMainListAdapter$$Lambda$2(worldclockMainListAdapter, worldclockViewHolder);
    }

    @Hidden
    public boolean onLongClick(View view) {
        return this.arg$1.lambda$onCreateViewHolder$1(this.arg$2, view);
    }
}
