package com.samsung.android.sdk.sgi.render;

public final class SGColorMaskProperty extends SGProperty {
    public SGColorMaskProperty() {
        this(SGJNI.new_SGColorMaskProperty(), true);
    }

    protected SGColorMaskProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean getWriteStateA() {
        return SGJNI.SGColorMaskProperty_getWriteStateA(this.swigCPtr, this);
    }

    public boolean getWriteStateB() {
        return SGJNI.SGColorMaskProperty_getWriteStateB(this.swigCPtr, this);
    }

    public boolean getWriteStateG() {
        return SGJNI.SGColorMaskProperty_getWriteStateG(this.swigCPtr, this);
    }

    public boolean getWriteStateR() {
        return SGJNI.SGColorMaskProperty_getWriteStateR(this.swigCPtr, this);
    }

    public boolean isColorMaskingEnabled() {
        return SGJNI.SGColorMaskProperty_isColorMaskingEnabled(this.swigCPtr, this);
    }

    public void setColorMaskingEnabled(boolean enabled) {
        SGJNI.SGColorMaskProperty_setColorMaskingEnabled(this.swigCPtr, this, enabled);
    }

    public void setWriteState(boolean enableWriteRed, boolean enableWriteGreen, boolean enableWriteBlue, boolean enableWriteAlpha) {
        SGJNI.SGColorMaskProperty_setWriteState(this.swigCPtr, this, enableWriteRed, enableWriteGreen, enableWriteBlue, enableWriteAlpha);
    }
}
