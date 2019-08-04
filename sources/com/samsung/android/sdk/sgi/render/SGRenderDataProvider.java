package com.samsung.android.sdk.sgi.render;

public abstract class SGRenderDataProvider {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected SGRenderDataProvider() {
        this(SGJNI.new_SGRenderDataProvider(), true);
        SGJNI.SGRenderDataProvider_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGRenderDataProvider(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGRenderDataProvider obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected static byte[] loadBuiltinShaderData(String name) {
        return SGJNI.SGRenderDataProvider_loadBuiltinShaderData(name);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGRenderDataProvider(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public abstract byte[] loadProgram(String str, String str2);

    public abstract byte[] loadShaderData(String str);

    public abstract void saveProgram(String str, String str2, byte[] bArr);
}
