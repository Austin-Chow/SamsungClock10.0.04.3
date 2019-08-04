package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import java.util.ArrayList;

final class SGAsyncPickListenerHolder extends SGAsyncPickListenerBase {
    ArrayList<Object> mContainer;
    SGTraceRayListener mListener;

    public SGAsyncPickListenerHolder(ArrayList<Object> container, SGTraceRayListener listener) {
        if (listener == null) {
            throw new NullPointerException("parameter listener is null");
        }
        this.mContainer = container;
        this.mListener = listener;
    }

    public void onCompleted(long[] handles) {
        this.mContainer.remove(this);
        try {
            int len = handles.length;
            SGMemoryRegistrator reg = SGMemoryRegistrator.getInstance();
            for (int i = 0; i < len; i++) {
                if (handles[i] != 0) {
                    SGLayer layer = (SGLayer) reg.GetObjectByPointer(handles[i]);
                    if (!(layer == null || this.mListener.onLayer(layer))) {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            SGVIException.handle(e, "SGAsyncPickListenerHolder::onCompleted error: uncaught exception");
        }
    }
}
