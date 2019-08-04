package com.samsung.android.sdk.sgi.render;

public final class SGTexture2DAttachmentProperty extends SGTextureProperty {
    protected SGTexture2DAttachmentProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGTexture2DAttachmentProperty(boolean isMipmapGenerationNeeded, SGTextureInternalFormat textureFormat, SGTextureDataFormat textureDataFormat, SGTextureDataType textureDataType, SGTextureFilterType minFilter, SGTextureFilterType maxFilter, SGTextureWrapType wrapS, SGTextureWrapType wrapT) {
        this(SGJNI.new_SGTexture2DAttachmentProperty(isMipmapGenerationNeeded, SGJNI.getData(textureFormat), SGJNI.getData(textureDataFormat), SGJNI.getData(textureDataType), SGJNI.getData(minFilter), SGJNI.getData(maxFilter), SGJNI.getData(wrapS), SGJNI.getData(wrapT)), true);
    }

    private long getExternalId() {
        return SGJNI.SGTexture2DAttachmentProperty_getExternalId(this.swigCPtr, this);
    }

    private void setExternalId(long aId) {
        SGJNI.SGTexture2DAttachmentProperty_setExternalId(this.swigCPtr, this, aId);
    }

    public boolean isGenerateMipmapsEnabled() {
        return SGJNI.SGTexture2DAttachmentProperty_isGenerateMipmapsEnabled(this.swigCPtr, this);
    }

    public void setGenerateMipmapsEnabled(boolean isNeeded) {
        SGJNI.SGTexture2DAttachmentProperty_setGenerateMipmapsEnabled(this.swigCPtr, this, isNeeded);
    }
}
