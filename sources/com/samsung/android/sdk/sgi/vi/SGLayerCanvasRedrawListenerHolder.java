package com.samsung.android.sdk.sgi.vi;

import android.graphics.Canvas;

final class SGLayerCanvasRedrawListenerHolder extends SGLayerCanvasRedrawListenerBase {
    SGLayer mLayer;
    SGLayerCanvasRedrawListener mListener;

    public SGLayerCanvasRedrawListenerHolder(SGLayer layer) {
        this.mLayer = layer;
    }

    public SGLayerCanvasRedrawListener getInterface() {
        return this.mListener;
    }

    public void onDraw(long layer, Canvas canvas) {
        try {
            this.mListener.onDraw(this.mLayer, canvas);
        } catch (Exception e) {
            SGVIException.handle(e, "SGLayerCanvasRedrawListener::onDraw error: uncaught exception");
        }
    }

    public void setInterface(SGLayerCanvasRedrawListener listener) {
        this.mListener = listener;
    }
}
