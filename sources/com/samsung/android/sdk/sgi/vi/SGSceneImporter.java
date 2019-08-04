package com.samsung.android.sdk.sgi.vi;

class SGSceneImporter {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGSceneImporter(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGSceneImporter(SGSceneResourceProviderBase resourceProvider, SGSceneParserListenerBase sceneParserListener) {
        this(SGJNI.new_SGSceneImporter(SGSceneResourceProviderBase.getCPtr(resourceProvider), resourceProvider, SGSceneParserListenerBase.getCPtr(sceneParserListener), sceneParserListener), true);
    }

    public static long getCPtr(SGSceneImporter obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGSceneImporter(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void load(String path) {
        SGJNI.SGSceneImporter_load(this.swigCPtr, this, path);
    }
}
