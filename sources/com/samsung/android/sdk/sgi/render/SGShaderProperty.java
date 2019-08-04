package com.samsung.android.sdk.sgi.render;

public class SGShaderProperty extends SGProperty {
    protected SGShaderProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGShaderDataType getDataType() {
        return ((SGShaderDataType[]) SGShaderDataType.class.getEnumConstants())[SGJNI.SGShaderProperty_getDataType(this.swigCPtr, this)];
    }

    public SGShaderType getShaderType() {
        return ((SGShaderType[]) SGShaderType.class.getEnumConstants())[SGJNI.SGShaderProperty_getShaderType(this.swigCPtr, this)];
    }
}
