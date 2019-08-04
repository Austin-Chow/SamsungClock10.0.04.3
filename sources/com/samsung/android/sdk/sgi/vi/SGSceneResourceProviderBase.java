package com.samsung.android.sdk.sgi.vi;

abstract class SGSceneResourceProviderBase {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGSceneResourceProviderBase() {
        this(SGJNI.new_SGSceneResourceProviderBase(), true);
        SGJNI.SGSceneResourceProviderBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGSceneResourceProviderBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGSceneResourceProviderBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGJNI.delete_SGSceneResourceProviderBase(this.swigCPtr);
            this.swigCPtr = 0;
        }
    }

    public abstract SGDataReaderBase getStream(String str);

    public abstract Object getTexture(String str);
}
