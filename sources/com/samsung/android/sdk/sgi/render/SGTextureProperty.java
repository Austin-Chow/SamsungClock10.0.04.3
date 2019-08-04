package com.samsung.android.sdk.sgi.render;

public class SGTextureProperty extends SGProperty {
    protected SGTextureProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGTextureDataFormat getDataFormat() {
        return ((SGTextureDataFormat[]) SGTextureDataFormat.class.getEnumConstants())[SGJNI.SGTextureProperty_getDataFormat(this.swigCPtr, this)];
    }

    public SGTextureDataType getDataType() {
        return ((SGTextureDataType[]) SGTextureDataType.class.getEnumConstants())[SGJNI.SGTextureProperty_getDataType(this.swigCPtr, this)];
    }

    public SGTextureInternalFormat getInternalFormat() {
        return ((SGTextureInternalFormat[]) SGTextureInternalFormat.class.getEnumConstants())[SGJNI.SGTextureProperty_getInternalFormat(this.swigCPtr, this)];
    }

    public SGTextureFilterType getMagFilter() {
        return ((SGTextureFilterType[]) SGTextureFilterType.class.getEnumConstants())[SGJNI.SGTextureProperty_getMagFilter(this.swigCPtr, this)];
    }

    public SGTextureFilterType getMinFilter() {
        return ((SGTextureFilterType[]) SGTextureFilterType.class.getEnumConstants())[SGJNI.SGTextureProperty_getMinFilter(this.swigCPtr, this)];
    }

    public SGTextureWrapType getWrapR() {
        return ((SGTextureWrapType[]) SGTextureWrapType.class.getEnumConstants())[SGJNI.SGTextureProperty_getWrapR(this.swigCPtr, this)];
    }

    public SGTextureWrapType getWrapS() {
        return ((SGTextureWrapType[]) SGTextureWrapType.class.getEnumConstants())[SGJNI.SGTextureProperty_getWrapS(this.swigCPtr, this)];
    }

    public SGTextureWrapType getWrapT() {
        return ((SGTextureWrapType[]) SGTextureWrapType.class.getEnumConstants())[SGJNI.SGTextureProperty_getWrapT(this.swigCPtr, this)];
    }

    public void setMagFilter(SGTextureFilterType type) {
        SGJNI.SGTextureProperty_setMagFilter(this.swigCPtr, this, SGJNI.getData(type));
    }

    public void setMinFilter(SGTextureFilterType type) {
        SGJNI.SGTextureProperty_setMinFilter(this.swigCPtr, this, SGJNI.getData(type));
    }
}
