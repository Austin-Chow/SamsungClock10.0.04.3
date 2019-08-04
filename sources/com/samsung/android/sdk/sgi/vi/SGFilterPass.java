package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;

public final class SGFilterPass {
    private boolean swigCMemOwn;
    protected long swigCPtr;

    public SGFilterPass() {
        this(SGJNI.new_SGFilterPass__SWIG_0(), true);
    }

    protected SGFilterPass(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGFilterPass(boolean alphaBlendingEnabled) {
        this(SGJNI.new_SGFilterPass__SWIG_1(alphaBlendingEnabled), true);
    }

    public static long getCPtr(SGFilterPass obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGFilterPass(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public SGShaderProgramProperty getProgramProperty() {
        return SGJNI.SGFilterPass_getProgramProperty(this.swigCPtr, this);
    }

    public SGProperty getProperty(int index) {
        return SGJNI.SGFilterPass_getProperty__SWIG_1(this.swigCPtr, this, index);
    }

    public SGProperty getProperty(String name) {
        return SGJNI.SGFilterPass_getProperty__SWIG_0(this.swigCPtr, this, name);
    }

    public int getPropertyCount() {
        return SGJNI.SGFilterPass_getPropertyCount(this.swigCPtr, this);
    }

    public String getPropertyName(int index) {
        return SGJNI.SGFilterPass_getPropertyName(this.swigCPtr, this, index);
    }

    public boolean isAlphaBlendingEnabled() {
        return SGJNI.SGFilterPass_isAlphaBlendingEnabled(this.swigCPtr, this);
    }

    public boolean isMipmapGenerationEnabled() {
        return SGJNI.SGFilterPass_isMipmapGenerationEnabled(this.swigCPtr, this);
    }

    public boolean isStaticContentEnabled() {
        return SGJNI.SGFilterPass_isStaticContentEnabled(this.swigCPtr, this);
    }

    public void removeProperty(int index) {
        SGJNI.SGFilterPass_removeProperty__SWIG_1(this.swigCPtr, this, index);
    }

    public void removeProperty(String name) {
        SGJNI.SGFilterPass_removeProperty__SWIG_0(this.swigCPtr, this, name);
    }

    public void setAlphaBlendingEnabled(boolean enabled) {
        SGJNI.SGFilterPass_setAlphaBlendingEnabled(this.swigCPtr, this, enabled);
    }

    public void setMipmapGeneration(boolean enabled) {
        SGJNI.SGFilterPass_setMipmapGeneration(this.swigCPtr, this, enabled);
    }

    public void setProgramProperty(SGShaderProgramProperty property) {
        SGJNI.SGFilterPass_setProgramProperty(this.swigCPtr, this, SGProperty.getCPtr(property), property);
    }

    public void setProperty(String name, SGProperty property) {
        SGJNI.SGFilterPass_setProperty(this.swigCPtr, this, name, SGProperty.getCPtr(property), property);
    }

    public void setStaticContentEnabled(boolean enabled) {
        SGJNI.SGFilterPass_setStaticContentEnabled(this.swigCPtr, this, enabled);
    }
}
