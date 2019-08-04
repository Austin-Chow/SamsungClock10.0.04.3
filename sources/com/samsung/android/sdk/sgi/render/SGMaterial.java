package com.samsung.android.sdk.sgi.render;

public final class SGMaterial {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGMaterial() {
        this(SGJNI.new_SGMaterial(), true);
    }

    private SGMaterial(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public static long getCPtr(SGMaterial obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private long getHandle() {
        return SGJNI.SGMaterial_getHandle(this.swigCPtr, this);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGMaterial) && ((SGMaterial) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGMaterial(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public int getId() {
        return SGJNI.SGMaterial_getId(this.swigCPtr, this);
    }

    public SGShaderProgramProperty getProgramProperty() {
        return SGJNI.SGMaterial_getProgramProperty(this.swigCPtr, this);
    }

    public SGProperty getProperty(int index) {
        if (index >= 0) {
            return SGJNI.SGMaterial_getProperty__SWIG_1(this.swigCPtr, this, index);
        }
        throw new NegativeArraySizeException("Negative array size!");
    }

    public SGProperty getProperty(String name) {
        return SGJNI.SGMaterial_getProperty__SWIG_0(this.swigCPtr, this, name);
    }

    public int getPropertyCount() {
        return SGJNI.SGMaterial_getPropertyCount(this.swigCPtr, this);
    }

    public String getPropertyName(int index) {
        if (index >= 0) {
            return SGJNI.SGMaterial_getPropertyName(this.swigCPtr, this, index);
        }
        throw new NegativeArraySizeException("Negative array size!");
    }

    public String getTechnicName() {
        return SGJNI.SGMaterial_getTechnicName(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }

    public void removeProperty(int index) {
        if (index < 0) {
            throw new NegativeArraySizeException("Negative array size!");
        }
        SGJNI.SGMaterial_removeProperty__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeProperty(String name) {
        SGJNI.SGMaterial_removeProperty__SWIG_0(this.swigCPtr, this, name);
    }

    public void setProgramProperty(SGShaderProgramProperty property) {
        SGJNI.SGMaterial_setProgramProperty(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }

    public void setProperty(String name, SGProperty property) {
        SGJNI.SGMaterial_setProperty(this.swigCPtr, this, name, SGProperty.getCPtr(property), property);
    }

    public void setTechnicName(String name) {
        SGJNI.SGMaterial_setTechnicName(this.swigCPtr, this, name);
    }
}
