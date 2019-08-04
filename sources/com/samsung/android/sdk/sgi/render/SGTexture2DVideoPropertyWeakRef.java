package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;

class SGTexture2DVideoPropertyWeakRef {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public SGTexture2DVideoPropertyWeakRef(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGTexture2DVideoPropertyWeakRef(SGTexture2DVideoProperty aProperty) {
        this(SGJNI.new_SGTexture2DVideoPropertyWeakRef(SGProperty.getCPtr(aProperty), aProperty), true);
    }

    public static long getCPtr(SGTexture2DVideoPropertyWeakRef obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGTexture2DVideoPropertyWeakRef(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public boolean textureUpdated() {
        return SGJNI.SGTexture2DVideoPropertyWeakRef_textureUpdated(this.swigCPtr, this);
    }

    public boolean updateTextureMatrix(SGMatrix4f value) {
        return SGJNI.SGTexture2DVideoPropertyWeakRef_updateTextureMatrix(this.swigCPtr, this, value.getData());
    }
}
