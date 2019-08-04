package com.samsung.android.sdk.sgi.vi;

import android.view.MotionEvent;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent;

public interface SGMotionEventConverter {
    SGTouchEvent convertHoverEvent(MotionEvent motionEvent);

    SGTouchEvent convertTouchEvent(MotionEvent motionEvent);
}
