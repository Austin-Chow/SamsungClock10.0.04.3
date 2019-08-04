package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.animation.SGBone;
import java.lang.reflect.Field;

final class SGBoneParamsChangeListenerHolder extends SGBoneParamsChangeListenerBase {
    SGBone mBone = ((SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, 0, false));
    SGBoneParamsChangeListener mListener;

    public SGBoneParamsChangeListener getInterface() {
        return this.mListener;
    }

    public void onBoneParamsChanged(long cPtr) {
        try {
            Field nameField = this.mBone.getClass().getDeclaredField("swigCPtr");
            nameField.setAccessible(true);
            nameField.set(this.mBone, Long.valueOf(cPtr));
            this.mListener.onBoneParamsChanged(this.mBone);
        } catch (Exception e) {
            SGVIException.handle(e, "SGBoneParamsChangeListenerHolder::onBoneParamsChanged error: uncaught exception");
        }
    }

    public void setInterface(SGBoneParamsChangeListener listener) {
        this.mListener = listener;
    }
}
