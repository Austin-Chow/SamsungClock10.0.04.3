package com.samsung.android.sdk.sgi.render;

public final class SGDepthProperty extends SGProperty {
    public SGDepthProperty() {
        this(SGJNI.new_SGDepthProperty(), true);
    }

    protected SGDepthProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public boolean isDepthTestEnabled() {
        return SGJNI.SGDepthProperty_isDepthTestEnabled(this.swigCPtr, this);
    }

    public boolean isWriteEnabled() {
        return SGJNI.SGDepthProperty_isWriteEnabled(this.swigCPtr, this);
    }

    public void setDepthTestEnabled(boolean enabled) {
        SGJNI.SGDepthProperty_setDepthTestEnabled(this.swigCPtr, this, enabled);
    }

    public void setWriteEnabled(boolean enabled) {
        SGJNI.SGDepthProperty_setWriteEnabled(this.swigCPtr, this, enabled);
    }
}
