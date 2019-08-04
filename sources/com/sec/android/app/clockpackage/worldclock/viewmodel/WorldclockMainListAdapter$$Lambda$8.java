package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$8 implements OnMenuItemClickListener {
    private final WorldclockMainListAdapter arg$1;
    private final int arg$2;

    private WorldclockMainListAdapter$$Lambda$8(WorldclockMainListAdapter worldclockMainListAdapter, int i) {
        this.arg$1 = worldclockMainListAdapter;
        this.arg$2 = i;
    }

    public static OnMenuItemClickListener lambdaFactory$(WorldclockMainListAdapter worldclockMainListAdapter, int i) {
        return new WorldclockMainListAdapter$$Lambda$8(worldclockMainListAdapter, i);
    }

    @Hidden
    public boolean onMenuItemClick(MenuItem menuItem) {
        return this.arg$1.lambda$null$3(this.arg$2, menuItem);
    }
}
