package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimeZoneConvertorActivity$$Lambda$1 implements Runnable {
    private final TimeZoneConvertorActivity arg$1;

    private TimeZoneConvertorActivity$$Lambda$1(TimeZoneConvertorActivity timeZoneConvertorActivity) {
        this.arg$1 = timeZoneConvertorActivity;
    }

    public static Runnable lambdaFactory$(TimeZoneConvertorActivity timeZoneConvertorActivity) {
        return new TimeZoneConvertorActivity$$Lambda$1(timeZoneConvertorActivity);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$onResume$0();
    }
}
