package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimeZoneListViewModel$$Lambda$1 implements OnClickListener {
    private final TimeZoneListViewModel arg$1;

    private TimeZoneListViewModel$$Lambda$1(TimeZoneListViewModel timeZoneListViewModel) {
        this.arg$1 = timeZoneListViewModel;
    }

    public static OnClickListener lambdaFactory$(TimeZoneListViewModel timeZoneListViewModel) {
        return new TimeZoneListViewModel$$Lambda$1(timeZoneListViewModel);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$initView$0(view);
    }
}
