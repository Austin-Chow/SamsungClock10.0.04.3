package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.render.SGTextureProperty;

final class SGSceneResourceProviderHolder extends SGSceneResourceProviderBase {
    private SGSceneResourceProvider mResourceProvider;

    public SGSceneResourceProviderHolder(SGSceneResourceProvider resourceProvider) {
        this.mResourceProvider = resourceProvider;
    }

    public void clear() {
        this.mResourceProvider.clear();
    }

    public SGDataReaderBase getStream(String path) {
        try {
            SGDataReader stream = this.mResourceProvider.getStream(path);
            if (!(stream instanceof SGAssetDataReader)) {
                return stream instanceof SGFileDataReader ? new SGFileDataReaderNative(((SGFileDataReader) stream).mFileInputStream) : new SGDataReaderHolder(stream);
            } else {
                SGAssetDataReader assetReader = (SGAssetDataReader) stream;
                return assetReader.mAssetManager != null ? new SGAssetDataReaderNative(assetReader.mAssetManager, assetReader.mResourceName) : new SGAssetDataReaderNative(assetReader.mAssetInputStream);
            }
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneResourceProvider::onOpenStream error: uncaught exception");
            return null;
        }
    }

    public SGTextureProperty getTexture(String resourceName) {
        SGTextureProperty texture = null;
        try {
            texture = this.mResourceProvider.getTexture(resourceName);
        } catch (Exception e) {
            SGVIException.handle(e, "SGSceneResourceProvider::getTexture error: uncaught exception");
        }
        return texture;
    }

    public void setInterface(SGSceneResourceProvider resourceProvider) {
        this.mResourceProvider = resourceProvider;
    }
}
