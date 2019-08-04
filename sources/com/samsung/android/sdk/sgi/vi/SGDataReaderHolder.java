package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import java.nio.ByteBuffer;

final class SGDataReaderHolder extends SGDataReaderBase {
    SGDataReader mListener;

    SGDataReaderHolder(SGDataReader dataReader) {
        this.mListener = dataReader;
    }

    public void close() {
        try {
            this.mListener.close();
        } catch (Exception e) {
            SGVIException.handle(e, "SGDataReaderListener::close error: uncaught exception");
        } finally {
            SGMemoryRegistrator.getInstance().RemoveFromManagementList(this.swigCPtr);
        }
    }

    public long getSize() {
        long result = 0;
        try {
            result = this.mListener.getSize();
        } catch (Exception e) {
            SGVIException.handle(e, "SGDataReaderListener::getSize error: uncaught exception");
        }
        return result;
    }

    public int read(ByteBuffer data) {
        int result = 0;
        try {
            result = this.mListener.read(data);
        } catch (Exception e) {
            SGVIException.handle(e, "SGDataReaderListener::read error: uncaught exception");
        }
        return result;
    }

    public void seek(long position) {
        try {
            this.mListener.seek(position);
        } catch (Exception e) {
            SGVIException.handle(e, "SGDataReaderListener::seek error: uncaught exception");
        }
    }
}
