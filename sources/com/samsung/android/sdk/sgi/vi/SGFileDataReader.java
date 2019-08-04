package com.samsung.android.sdk.sgi.vi;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class SGFileDataReader implements SGDataReader {
    FileInputStream mFileInputStream;

    public SGFileDataReader(FileInputStream stream) {
        this.mFileInputStream = stream;
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
