package com.samsung.android.sdk.sgi.render;

public final class SGPreMultiplyProperty extends SGProperty {
    public SGPreMultiplyProperty() {
        this(SGJNI.new_SGPreMultiplyProperty__SWIG_0(), true);
    }

    protected SGPreMultiplyProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGPreMultiplyProperty(boolean preMultiply) {
        this(SGJNI.new_SGPreMultiplyProperty__SWIG_1(preMultiply), true);
    }

    public boolean isPremultiplyEnabled() {
        return SGJNI.SGPreMultiplyProperty_isPremultiplyEnabled(this.swigCPtr, this);
    }

    public void setPremultiplyEnabled(boolean enabled) {
        SGJNI.SGPreMultiplyProperty_setPremultiplyEnabled(this.swigCPtr, this, enabled);
    }
}
