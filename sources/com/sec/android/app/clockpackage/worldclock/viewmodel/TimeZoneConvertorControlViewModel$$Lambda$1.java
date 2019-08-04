package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimeZoneConvertorControlViewModel$$Lambda$1 implements OnClickListener {
    private final TimeZoneConvertorControlViewModel arg$1;

    private TimeZoneConvertorControlViewModel$$Lambda$1(TimeZoneConvertorControlViewModel timeZoneConvertorControlViewModel) {
        this.arg$1 = timeZoneConvertorControlViewModel;
    }

    public static OnClickListener lambdaFactory$(TimeZoneConvertorControlViewModel timeZoneConvertorControlViewModel) {
        return new TimeZoneConvertorControlViewModel$$Lambda$1(timeZoneConvertorControlViewModel);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$initResetButton$0(view);
    }
}
