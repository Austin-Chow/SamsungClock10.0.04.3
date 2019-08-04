package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.animation.SGBone;

public class SGLayerSkeleton extends SGLayer {
    private SGBoneParamsChangeListenerHolder mBoneParamsChangeListener;

    public SGLayerSkeleton() {
        this(SGJNI.new_SGLayerSkeleton(), true);
    }

    protected SGLayerSkeleton(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    private void _setBoneParamsChangeListener(SGBoneParamsChangeListenerBase aListener) {
        SGJNI.SGLayerSkeleton__setBoneParamsChangeListener(this.swigCPtr, this, SGBoneParamsChangeListenerBase.getCPtr(aListener), aListener);
    }

    void _setBoneParamsChangeListener(SGBoneParamsChangeListener listener) {
        if (listener == null) {
            this.mBoneParamsChangeListener = null;
            _setBoneParamsChangeListener((SGBoneParamsChangeListenerHolder) null);
            return;
        }
        if (this.mBoneParamsChangeListener == null) {
            this.mBoneParamsChangeListener = new SGBoneParamsChangeListenerHolder();
            _setBoneParamsChangeListener(this.mBoneParamsChangeListener);
        }
        this.mBoneParamsChangeListener.setInterface(listener);
    }

    public SGBone getSkeleton() {
        long cPtr = SGJNI.SGLayerSkeleton_getSkeleton(this.swigCPtr, this);
        return cPtr != 0 ? (SGBone) SGJNI.createObjectFromNativePtr(SGBone.class, cPtr, true) : null;
    }

    public void setSkeleton(SGBone skeleton) {
        SGJNI.SGLayerSkeleton_setSkeleton(this.swigCPtr, this, SGBone.getCPtr(skeleton), skeleton);
    }
}
