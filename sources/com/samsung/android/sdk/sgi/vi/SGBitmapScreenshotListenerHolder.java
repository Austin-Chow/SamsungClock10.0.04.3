package com.samsung.android.sdk.sgi.vi;

import android.graphics.Bitmap;
import java.util.ArrayList;

final class SGBitmapScreenshotListenerHolder extends SGGraphicBufferScreenshotListenerBase {
    ArrayList<Object> mContainer;
    SGBitmapScreenshotListener mListener;

    public SGBitmapScreenshotListenerHolder(ArrayList<Object> container, SGBitmapScreenshotListener listener) {
        if (listener == null) {
            throw new NullPointerException("parameter listener is null");
        }
        this.mContainer = container;
        this.mListener = listener;
    }

    public void onCompleted(Bitmap bitmap) {
        this.mContainer.remove(this);
        try {
            this.mListener.onCompleted(bitmap);
        } catch (Exception e) {
            SGVIException.handle(e, "SGBitmapScreenshotListener::onCompleted error: uncaught exception");
        }
    }
}
