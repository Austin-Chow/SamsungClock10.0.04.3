package com.samsung.android.sdk.sgi.vi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

final class SGChannelDataReader implements SGDataReader {
    private FileChannel mChannel;
    private long mPos = 0;
    private long mSize;
    private long mStartOffset;

    public SGChannelDataReader(FileChannel channel, long startOffset, long size) throws IOException {
        this.mChannel = channel;
        this.mStartOffset = startOffset;
        this.mSize = size;
    }

    public void close() throws IOException {
        this.mChannel.close();
    }

    public long getSize() {
        return this.mSize;
    }

    public int read(ByteBuffer data) throws IOException {
        long left = this.mSize - this.mPos;
        if (left < ((long) data.remaining())) {
            data.limit(((int) left) + data.position());
        }
        int r = this.mChannel.read(data, this.mStartOffset + this.mPos);
        if (r >= 0) {
            this.mPos += (long) r;
        }
        return r;
    }

    public void seek(long position) throws IOException {
        if (position > this.mSize) {
            position = this.mSize;
        }
        this.mChannel.position(this.mStartOffset + position);
        this.mPos = position;
    }
}
