package com.sec.android.app.clockpackage.common.callback;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class OnSingleClickListener implements OnClickListener {
    private long mLastClickTime;

    public abstract void onSingleClick(View view);

    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - this.mLastClickTime;
        this.mLastClickTime = currentClickTime;
        if (elapsedTime > 600) {
            onSingleClick(v);
        }
    }
}
