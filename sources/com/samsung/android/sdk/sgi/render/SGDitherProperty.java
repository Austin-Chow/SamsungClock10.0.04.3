package com.samsung.android.sdk.sgi.render;

public final class SGDitherProperty extends SGProperty {
    public SGDitherProperty() {
        this(SGJNI.new_SGDitherProperty(), true);
    }

    protected SGDitherProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean isDitherEnabled() {
        return SGJNI.SGDitherProperty_isDitherEnabled(this.swigCPtr, this);
    }

    public void setDitherEnabled(boolean enabled) {
        SGJNI.SGDitherProperty_setDitherEnabled(this.swigCPtr, this, enabled);
    }
}
