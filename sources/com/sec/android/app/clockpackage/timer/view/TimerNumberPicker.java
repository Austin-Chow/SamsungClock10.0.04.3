package com.sec.android.app.clockpackage.timer.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import com.samsung.android.widget.SemNumberPicker;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;

public class TimerNumberPicker extends SemNumberPicker {
    public TimerNumberPicker(Context context) {
        super(context);
    }

    public TimerNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEditTextMode(boolean isEditMode) {
        super.setEditTextMode(isEditMode);
    }

    public void onWindowFocusChanged(final boolean hasWindowFocus) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (ClockTab.isTimerTab()) {
                    super.onWindowFocusChanged(hasWindowFocus);
                    Log.secD("TimerNumberPicker", "onWindowFocusChanged() / hasWindowFocus = " + hasWindowFocus);
                }
            }
        }, 50);
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (!ClockTab.isTimerTab()) {
            return false;
        }
        Log.secD("TimerNumberPicker", "dispatchKeyEventPreIme() / keyCode = " + event.getKeyCode());
        return super.dispatchKeyEventPreIme(event);
    }
}
