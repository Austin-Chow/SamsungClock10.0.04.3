package com.samsung.android.sdk.sgi.vi;

import java.util.ArrayList;

final class SGSurfaceChangesDrawnListenerHolder extends SGSurfaceChangesDrawnListenerBase {
    ArrayList<Object> mContainer;
    SGSurfaceChangesDrawnListener mListener;

    public SGSurfaceChangesDrawnListenerHolder(ArrayList<Object> container, SGSurfaceChangesDrawnListener listener) {
        if (listener == null) {
            throw new NullPointerException("parameter listener is null");
        }
        this.mContainer = container;
        this.mListener = listener;
    }

    public void onChangesDrawn() {
        this.mContainer.remove(this);
        try {
            this.mListener.onChangesDrawn();
        } catch (Exception e) {
            SGVIException.handle(e, "SGSurfaceChangesDrawnListener::onChangesDrawn error: uncaught exception");
        }
    }
}
