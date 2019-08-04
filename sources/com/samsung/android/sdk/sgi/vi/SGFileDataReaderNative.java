package com.samsung.android.sdk.sgi.vi;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

final class SGFileDataReaderNative extends SGDataReaderBase {
    public SGFileDataReaderNative() {
        this(SGJNI.new_SGFileDataReaderNative(), true);
    }

    protected SGFileDataReaderNative(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    SGFileDataReaderNative(FileInputStream fileInputStream) {
        this(SGJNI.new_SGFileDataReaderNative(), true);
        init(fileInputStream);
    }

    private void init(FileInputStream fileInputStream) {
        SGJNI.SGFileDataReaderNative_init(this.swigCPtr, this, fileInputStream);
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
