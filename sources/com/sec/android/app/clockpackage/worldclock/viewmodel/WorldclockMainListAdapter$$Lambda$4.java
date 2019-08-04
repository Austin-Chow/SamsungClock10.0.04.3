package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$4 implements OnCreateContextMenuListener {
    private final WorldclockMainListAdapter arg$1;
    private final WorldclockViewHolder arg$2;

    private WorldclockMainListAdapter$$Lambda$4(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        this.arg$1 = worldclockMainListAdapter;
        this.arg$2 = worldclockViewHolder;
    }

    public static OnCreateContextMenuListener lambdaFactory$(WorldclockMainListAdapter worldclockMainListAdapter, WorldclockViewHolder worldclockViewHolder) {
        return new WorldclockMainListAdapter$$Lambda$4(worldclockMainListAdapter, worldclockViewHolder);
    }

    @Hidden
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        this.arg$1.lambda$onCreateViewHolder$4(this.arg$2, contextMenu, view, contextMenuInfo);
    }
}
