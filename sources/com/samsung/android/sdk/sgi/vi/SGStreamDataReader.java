package com.samsung.android.sdk.sgi.vi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class SGStreamDataReader implements SGDataReader {
    private byte[] mBuffer = new byte[8192];
    private long mPos = 0;
    private long mSize;
    private InputStream mStream;

    public SGStreamDataReader(InputStream stream) throws IOException {
        this.mStream = stream;
        this.mSize = (long) stream.available();
    }

    public void close() throws IOException {
        this.mStream.close();
        this.mBuffer = null;
    }

    public long getSize() {
        return this.mSize;
    }

    public int read(ByteBuffer data) throws IOException {
        int toRead = data.remaining();
        long left = this.mSize - this.mPos;
        if (((long) toRead) > left) {
            toRead = (int) left;
        }
        if (toRead == 0) {
            return 0;
        }
        if (this.mBuffer.length < toRead) {
            this.mBuffer = new byte[toRead];
        }
        int r = this.mStream.read(this.mBuffer, 0, toRead);
        if (r < 0) {
            return -1;
        }
        data.put(this.mBuffer, 0, r);
        this.mPos += (long) r;
        return r;
    }

    public void seek(long position) throws IOException {
        if (position > this.mSize) {
            position = this.mSize;
        }
        long byteCount = position - this.mPos;
        if (this.mStream.skip(byteCount) != byteCount) {
            throw new IOException("Can't skip " + byteCount + " bytes");
        }
        this.mPos = position;
    }
}
