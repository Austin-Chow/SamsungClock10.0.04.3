package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnLongClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$7 implements OnLongClickListener {
    private static final WorldclockMainListAdapter$$Lambda$7 instance = new WorldclockMainListAdapter$$Lambda$7();

    private WorldclockMainListAdapter$$Lambda$7() {
    }

    public static OnLongClickListener lambdaFactory$() {
        return instance;
    }

    @Hidden
    public boolean onLongClick(View view) {
        return view.setHapticFeedbackEnabled(false);
    }
}
