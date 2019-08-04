package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.render.SGProperty;
import java.util.ArrayList;

final class SGPropertyScreenshotListenerHolder extends SGScreenshotPropertyListenerBase {
    ArrayList<Object> mContainer;
    SGPropertyScreenshotListener mListener;

    public SGPropertyScreenshotListenerHolder(ArrayList<Object> container, SGPropertyScreenshotListener listener) {
        if (listener == null) {
            throw new NullPointerException("parameter listener is null");
        }
        this.mContainer = container;
        this.mListener = listener;
    }

    public void onCompleted(long cPtr) {
        this.mContainer.remove(this);
        try {
            this.mListener.onCompleted((SGProperty) SGJNI.createObjectFromNativePtr(SGProperty.class, cPtr, true));
        } catch (Exception e) {
            SGVIException.handle(e, "SGPropertyScreenshotListener::onCompleted error: uncaught exception");
        }
    }
}
