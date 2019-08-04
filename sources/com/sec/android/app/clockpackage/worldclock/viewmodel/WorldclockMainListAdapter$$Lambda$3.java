package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$3 implements OnKeyListener {
    private final WorldclockMainListAdapter arg$1;

    private WorldclockMainListAdapter$$Lambda$3(WorldclockMainListAdapter worldclockMainListAdapter) {
        this.arg$1 = worldclockMainListAdapter;
    }

    public static OnKeyListener lambdaFactory$(WorldclockMainListAdapter worldclockMainListAdapter) {
        return new WorldclockMainListAdapter$$Lambda$3(worldclockMainListAdapter);
    }

    @Hidden
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return this.arg$1.lambda$onCreateViewHolder$2(view, i, keyEvent);
    }
}
