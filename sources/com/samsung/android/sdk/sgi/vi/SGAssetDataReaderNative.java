package com.samsung.android.sdk.sgi.vi;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import java.nio.ByteBuffer;

final class SGAssetDataReaderNative extends SGDataReaderBase {
    public SGAssetDataReaderNative() {
        this(SGJNI.new_SGAssetDataReaderNative(), true);
    }

    protected SGAssetDataReaderNative(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    SGAssetDataReaderNative(AssetInputStream assetInputStream) {
        this(SGJNI.new_SGAssetDataReaderNative(), true);
        init(assetInputStream);
    }

    SGAssetDataReaderNative(AssetManager assetManaget, String name) {
        this(SGJNI.new_SGAssetDataReaderNative(), true);
        init(assetManaget, name);
    }

    private void init(AssetInputStream assetInputSteam) {
        SGJNI.SGAssetDataReaderNative_init__SWIG_1(this.swigCPtr, this, assetInputSteam);
    }

    private void init(AssetManager assetManager, String assetName) {
        SGJNI.SGAssetDataReaderNative_init__SWIG_0(this.swigCPtr, this, assetManager, assetName);
    }

    public void close() {
    }

    public long getSize() {
        return 0;
    }

    public int read(ByteBuffer data) {
        return 0;
    }

    public void seek(long position) {
    }
}
