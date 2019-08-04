package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.ui.SGWidget;

public interface SGTraceRayListener {
    void onCompleted();

    boolean onLayer(SGLayer sGLayer);

    boolean onWidget(SGWidget sGWidget);
}
