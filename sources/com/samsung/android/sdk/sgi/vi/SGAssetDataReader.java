package com.samsung.android.sdk.sgi.vi;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class SGAssetDataReader implements SGDataReader {
    AssetInputStream mAssetInputStream;
    AssetManager mAssetManager;
    String mResourceName;

    public SGAssetDataReader(AssetManager assetManager, String resourceName) {
        this.mAssetManager = assetManager;
        this.mResourceName = resourceName;
    }

    public SGAssetDataReader(Resources res, int id) {
        this.mAssetInputStream = (AssetInputStream) res.openRawResource(id);
    }

    public void close() throws IOException {
    }

    public long getSize() {
        return 0;
    }

    public int read(ByteBuffer data) throws IOException {
        return 0;
    }

    public void seek(long position) throws IOException {
    }
}
