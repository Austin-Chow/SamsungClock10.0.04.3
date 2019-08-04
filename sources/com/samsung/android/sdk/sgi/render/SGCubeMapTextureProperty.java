package com.samsung.android.sdk.sgi.render;

import android.graphics.Bitmap;

public final class SGCubeMapTextureProperty extends SGTextureProperty {
    public SGCubeMapTextureProperty() {
        this(SGJNI.new_SGCubeMapTextureProperty__SWIG_0(), true);
    }

    protected SGCubeMapTextureProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGCubeMapTextureProperty(SGTextureFilterType minFilter, SGTextureFilterType magFilter, SGTextureWrapType wrapS, SGTextureWrapType wrapT, SGTextureWrapType wrapR) {
        this(SGJNI.new_SGCubeMapTextureProperty__SWIG_1(SGJNI.getData(minFilter), SGJNI.getData(magFilter), SGJNI.getData(wrapS), SGJNI.getData(wrapT), SGJNI.getData(wrapR)), true);
    }

    public SGTextureDataFormat getDataFormat(SGTextureTargetType targetType) {
        return ((SGTextureDataFormat[]) SGTextureDataFormat.class.getEnumConstants())[SGJNI.SGCubeMapTextureProperty_getDataFormat(this.swigCPtr, this, SGJNI.getData(targetType))];
    }

    public SGTextureDataType getDataType(SGTextureTargetType targetType) {
        return ((SGTextureDataType[]) SGTextureDataType.class.getEnumConstants())[SGJNI.SGCubeMapTextureProperty_getDataType(this.swigCPtr, this, SGJNI.getData(targetType))];
    }

    public SGTextureInternalFormat getInternalFormat(SGTextureTargetType targetType) {
        return ((SGTextureInternalFormat[]) SGTextureInternalFormat.class.getEnumConstants())[SGJNI.SGCubeMapTextureProperty_getInternalFormat(this.swigCPtr, this, SGJNI.getData(targetType))];
    }

    public int getNumMipmaps() {
        return SGJNI.SGCubeMapTextureProperty_getNumMipmaps(this.swigCPtr, this);
    }

    public int getSize(int mipmapNum) {
        return SGJNI.SGCubeMapTextureProperty_getSize(this.swigCPtr, this, mipmapNum);
    }

    public boolean isGenerateMipmapsEnabled() {
        return SGJNI.SGCubeMapTextureProperty_isGenerateMipmapsEnabled(this.swigCPtr, this);
    }

    public void setBitmaps(Bitmap positiveX, Bitmap negativeX, Bitmap positiveY, Bitmap negativeY, Bitmap positiveZ, Bitmap negativeZ, boolean autoRecycle) {
        SGJNI.SGCubeMapTextureProperty_setBitmaps(this.swigCPtr, this, positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ, autoRecycle);
    }

    public void setGenerateMipmapsEnabled(boolean enabled) {
        SGJNI.SGCubeMapTextureProperty_setGenerateMipmapsEnabled(this.swigCPtr, this, enabled);
    }
}
