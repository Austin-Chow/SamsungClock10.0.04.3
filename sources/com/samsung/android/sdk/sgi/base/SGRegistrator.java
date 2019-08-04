package com.samsung.android.sdk.sgi.base;

public class SGRegistrator {
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    public SGRegistrator() {
        this(SGJNI.new_SGRegistrator(), true);
        SGJNI.SGRegistrator_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGRegistrator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGRegistrator obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public boolean AddToManagementList(long aPointer) {
        return SGJNI.SGRegistrator_AddToManagementList(this.swigCPtr, this, aPointer);
    }

    public boolean Deregister(long aPointer) {
        return SGJNI.SGRegistrator_Deregister(this.swigCPtr, this, aPointer);
    }

    public Object GetObjectByPointer(long aPointer) {
        return SGJNI.SGRegistrator_GetObjectByPointer(this.swigCPtr, this, aPointer);
    }

    public boolean Register(Object obj, long aPointer) {
        return SGJNI.SGRegistrator_Register(this.swigCPtr, this, obj, aPointer);
    }

    public boolean RemoveFromManagementList(long aPointer) {
        return SGJNI.SGRegistrator_RemoveFromManagementList(this.swigCPtr, this, aPointer);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGRegistrator(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }
}
