package com.samsung.android.sdk.sgi.vi;

final class SGLayerAnimationListenerHolder extends SGLayerAnimationListenerBase {
    SGLayer mLayer;
    SGLayerAnimationListener mListener;

    public SGLayerAnimationListenerHolder(SGLayer layer) {
        this.mLayer = layer;
    }

    public SGLayerAnimationListener getInterface() {
        return this.mListener;
    }

    public void onFinished(long layer) {
        try {
            this.mListener.onFinished(this.mLayer);
        } catch (Exception e) {
            SGVIException.handle(e, "SGLayerAnimationListener::onFinished error: uncaught exception");
        }
    }

    public void onStarted(long layer) {
        try {
            if (this.mListener != null) {
                this.mListener.onStarted(this.mLayer);
            }
        } catch (Exception e) {
            SGVIException.handle(e, "SGLayerAnimationListener::onStarted error: uncaught exception");
        }
    }

    public void setInterface(SGLayerAnimationListener listener) {
        this.mListener = listener;
    }
}
