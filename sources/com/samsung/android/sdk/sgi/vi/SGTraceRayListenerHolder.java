package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.ui.SGWidget;
import java.util.ArrayList;

final class SGTraceRayListenerHolder extends SGTraceRayListenerBase {
    ArrayList<Object> mContainer;
    SGTraceRayListener mListener;
    SGVector3f mPoint;

    public SGTraceRayListenerHolder(ArrayList<Object> container, SGTraceRayListener listener) {
        if (listener == null) {
            throw new NullPointerException("parameter listener is null");
        }
        this.mListener = listener;
        this.mPoint = new SGVector3f();
        this.mContainer = container;
    }

    public void onCompleted() {
        if (this.mContainer != null) {
            this.mContainer.remove(this);
        }
        try {
            this.mListener.onCompleted();
        } catch (Exception e) {
            SGVIException.handle(e, "SGTraceRayListenerHolder::onCompleted error: uncaught exception");
        }
    }

    public boolean onLayer(long cPtr) {
        try {
            return this.mListener.onLayer((SGLayer) SGMemoryRegistrator.getInstance().GetObjectByPointer(cPtr));
        } catch (Exception e) {
            SGVIException.handle(e, "SGTraceRayListenerHolder::onLayer error: uncaught exception");
            return false;
        }
    }

    public boolean onWidget(long cPtr) {
        try {
            return this.mListener.onWidget((SGWidget) SGMemoryRegistrator.getInstance().GetObjectByPointer(cPtr));
        } catch (Exception e) {
            SGVIException.handle(e, "SGTraceRayListenerHolder::onWidget error: uncaught exception");
            return false;
        }
    }
}
