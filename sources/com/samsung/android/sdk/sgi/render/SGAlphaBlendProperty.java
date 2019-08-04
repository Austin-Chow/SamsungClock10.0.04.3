package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector4f;

public final class SGAlphaBlendProperty extends SGProperty {
    public SGAlphaBlendProperty() {
        this(SGJNI.new_SGAlphaBlendProperty(), true);
    }

    protected SGAlphaBlendProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGVector4f getBlendColor() {
        return new SGVector4f(SGJNI.SGAlphaBlendProperty_getBlendColor(this.swigCPtr, this));
    }

    public SGBlendEquation getBlendEquationAlpha() {
        return ((SGBlendEquation[]) SGBlendEquation.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getBlendEquationAlpha(this.swigCPtr, this)];
    }

    public SGBlendEquation getBlendEquationColor() {
        return ((SGBlendEquation[]) SGBlendEquation.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getBlendEquationColor(this.swigCPtr, this)];
    }

    public SGBlendFactor getDestinationFactor() {
        return ((SGBlendFactor[]) SGBlendFactor.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getDestinationFactor(this.swigCPtr, this)];
    }

    public SGBlendFactor getDestinationFactorAlpha() {
        return ((SGBlendFactor[]) SGBlendFactor.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getDestinationFactorAlpha(this.swigCPtr, this)];
    }

    public SGBlendFactor getDestinationFactorColor() {
        return ((SGBlendFactor[]) SGBlendFactor.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getDestinationFactorColor(this.swigCPtr, this)];
    }

    public SGBlendFactor getSourceFactor() {
        return ((SGBlendFactor[]) SGBlendFactor.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getSourceFactor(this.swigCPtr, this)];
    }

    public SGBlendFactor getSourceFactorAlpha() {
        return ((SGBlendFactor[]) SGBlendFactor.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getSourceFactorAlpha(this.swigCPtr, this)];
    }

    public SGBlendFactor getSourceFactorColor() {
        return ((SGBlendFactor[]) SGBlendFactor.class.getEnumConstants())[SGJNI.SGAlphaBlendProperty_getSourceFactorColor(this.swigCPtr, this)];
    }

    public boolean isAlphaBlendingEnabled() {
        return SGJNI.SGAlphaBlendProperty_isAlphaBlendingEnabled(this.swigCPtr, this);
    }

    public void setAlphaBlendingEnabled(boolean enabled) {
        SGJNI.SGAlphaBlendProperty_setAlphaBlendingEnabled(this.swigCPtr, this, enabled);
    }

    public void setBlendColor(SGVector4f value) {
        SGJNI.SGAlphaBlendProperty_setBlendColor(this.swigCPtr, this, value.getData());
    }

    public void setBlendEquation(SGBlendEquation equationColor, SGBlendEquation equationAlpha) {
        SGJNI.SGAlphaBlendProperty_setBlendEquation(this.swigCPtr, this, SGJNI.getData(equationColor), SGJNI.getData(equationAlpha));
    }

    public void setFactors(SGBlendFactor source, SGBlendFactor destination) {
        SGJNI.SGAlphaBlendProperty_setFactors(this.swigCPtr, this, SGJNI.getData(source), SGJNI.getData(destination));
    }

    public void setFactorsSeparate(SGBlendFactor sourceColor, SGBlendFactor destinationColor, SGBlendFactor sourceAlpha, SGBlendFactor destinationAlpha) {
        SGJNI.SGAlphaBlendProperty_setFactorsSeparate(this.swigCPtr, this, SGJNI.getData(sourceColor), SGJNI.getData(destinationColor), SGJNI.getData(sourceAlpha), SGJNI.getData(destinationAlpha));
    }
}
