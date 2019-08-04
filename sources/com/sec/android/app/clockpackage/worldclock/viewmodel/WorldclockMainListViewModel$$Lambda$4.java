package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.view.MenuItem;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListViewModel$$Lambda$4 implements OnNavigationItemSelectedListener {
    private final WorldclockMainListViewModel arg$1;

    private WorldclockMainListViewModel$$Lambda$4(WorldclockMainListViewModel worldclockMainListViewModel) {
        this.arg$1 = worldclockMainListViewModel;
    }

    public static OnNavigationItemSelectedListener lambdaFactory$(WorldclockMainListViewModel worldclockMainListViewModel) {
        return new WorldclockMainListViewModel$$Lambda$4(worldclockMainListViewModel);
    }

    @Hidden
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return this.arg$1.lambda$initBottomNavigationView$4(menuItem);
    }
}
