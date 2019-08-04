package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGVector2f;

final class SGSurfaceListenerHolder extends SGSurfaceListenerBase {
    public SGDrawFrameListener mDrawFrameListener = null;
    public SGSurfaceSizeChangeListener mSizeChangeListener = null;

    public void onFrameEnd() {
        try {
            this.mDrawFrameListener.onFrameEnd();
        } catch (Exception e) {
            SGVIException.handle(e, "SGDrawFrameListener::onFrameEnd error: uncaught exception");
        }
    }

    public void onResize(SGVector2f size) {
        try {
            this.mSizeChangeListener.onResize(size);
        } catch (Exception e) {
            SGVIException.handle(e, "SGSurfaceSizeChangeListener::onResize error: uncaught exception");
        }
    }
}
