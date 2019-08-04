package com.samsung.android.sdk.sgi.vi;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public interface SGInputConnectionListener {
    boolean checkInputConnectionProxy(View view);

    InputConnection onInputConnectionCreated(EditorInfo editorInfo);

    void onResumeSip();

    void onSuspendSip();
}
