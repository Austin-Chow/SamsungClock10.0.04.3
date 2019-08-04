package com.samsung.android.sdk.sgi.render;

public final class SGDiscardRasterizerProperty extends SGProperty {
    public SGDiscardRasterizerProperty() {
        this(SGJNI.new_SGDiscardRasterizerProperty(), true);
    }

    protected SGDiscardRasterizerProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean isDiscardRasterizerEnabled() {
        return SGJNI.SGDiscardRasterizerProperty_isDiscardRasterizerEnabled(this.swigCPtr, this);
    }

    public void setDiscardRasterizerEnabled(boolean enabled) {
        SGJNI.SGDiscardRasterizerProperty_setDiscardRasterizerEnabled(this.swigCPtr, this, enabled);
    }
}
