package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.render.SGProperty;
import java.util.ArrayList;

final class SGBackgorundPropertyListenerHolder extends SGBackgroundPropertyListenerBase {
    ArrayList<Object> mContainer;
    SGBackgroundPropertyListener mListener;

    public SGBackgorundPropertyListenerHolder(ArrayList<Object> holderContainer, SGBackgroundPropertyListener listener) {
        this.mListener = listener;
        this.mContainer = holderContainer;
    }

    public void onFinish(long cPtr, int id) {
        try {
            this.mListener.onFinish((SGProperty) SGJNI.createObjectFromNativePtr(SGProperty.class, cPtr, true), id);
        } catch (Exception e) {
            SGVIException.handle(e, "SGBackgroundPropertyListener::onFinish error: uncaught exception");
        }
        this.mContainer.remove(this);
        this.mListener = null;
    }
}
