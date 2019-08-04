package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$1 implements OnClickListener {
    private final WorldclockMainListAdapter arg$1;
    private final WorldclockViewHolder arg$2;

    private WorldclockMainListAdapter$$Lambda$1(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        this.arg$1 = worldclockMainListAdapter;
        this.arg$2 = worldclockViewHolder;
    }

    public static OnClickListener lambdaFactory$(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        return new WorldclockMainListAdapter$$Lambda$1(worldclockMainListAdapter, worldclockViewHolder);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCreateViewHolder$0(this.arg$2, view);
    }
}
