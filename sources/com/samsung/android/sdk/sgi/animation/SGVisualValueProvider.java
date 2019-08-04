package com.samsung.android.sdk.sgi.animation;

public class SGVisualValueProvider {
    SGAnimationListenerBase mListener = null;

    SGVisualValueProvider() {
    }

    public void getCurrentValue(SGVisualValueReceiver receiver) {
        if (this.mListener != null) {
            this.mListener.getCurrentValue(receiver);
        }
    }

    void setAnimationListenerBase(SGAnimationListenerBase listener) {
        this.mListener = listener;
    }
}
