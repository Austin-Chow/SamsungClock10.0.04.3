package com.samsung.android.sdk.sgi.vi;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface SGDataReader extends Closeable {
    void close() throws IOException;

    long getSize();

    int read(ByteBuffer byteBuffer) throws IOException;

    void seek(long j) throws IOException;
}
