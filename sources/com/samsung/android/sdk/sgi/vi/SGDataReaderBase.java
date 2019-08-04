package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import java.nio.ByteBuffer;

public abstract class SGDataReaderBase {
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGDataReaderBase() {
        this(SGJNI.new_SGDataReaderBase(), true);
        SGJNI.SGDataReaderBase_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    SGDataReaderBase(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
        SGMemoryRegistrator reg = SGMemoryRegistrator.getInstance();
        reg.Register(this, this.swigCPtr);
        reg.AddToManagementList(this.swigCPtr);
    }

    public static long getCPtr(SGDataReaderBase obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public abstract void close();

    public void finalize() {
        if (this.swigCPtr != 0) {
            SGMemoryRegistrator.getInstance().Deregister(this.swigCPtr);
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGDataReaderBase(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public abstract long getSize();

    public abstract int read(ByteBuffer byteBuffer);

    public abstract void seek(long j);
}
