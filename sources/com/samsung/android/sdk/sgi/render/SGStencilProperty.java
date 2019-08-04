package com.samsung.android.sdk.sgi.render;

public final class SGStencilProperty extends SGProperty {
    public SGStencilProperty() {
        this(SGJNI.new_SGStencilProperty(), true);
    }

    protected SGStencilProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGStencilFunction getFunction() {
        return ((SGStencilFunction[]) SGStencilFunction.class.getEnumConstants())[SGJNI.SGStencilProperty_getFunction(this.swigCPtr, this)];
    }

    public int getGlobalMask() {
        return SGJNI.SGStencilProperty_getGlobalMask(this.swigCPtr, this);
    }

    public int getMask() {
        return SGJNI.SGStencilProperty_getMask(this.swigCPtr, this);
    }

    public SGStencilOperation getOperationDepthFail() {
        return ((SGStencilOperation[]) SGStencilOperation.class.getEnumConstants())[SGJNI.SGStencilProperty_getOperationDepthFail(this.swigCPtr, this)];
    }

    public SGStencilOperation getOperationDepthPass() {
        return ((SGStencilOperation[]) SGStencilOperation.class.getEnumConstants())[SGJNI.SGStencilProperty_getOperationDepthPass(this.swigCPtr, this)];
    }

    public SGStencilOperation getOperationStencilFail() {
        return ((SGStencilOperation[]) SGStencilOperation.class.getEnumConstants())[SGJNI.SGStencilProperty_getOperationStencilFail(this.swigCPtr, this)];
    }

    public int getReference() {
        return SGJNI.SGStencilProperty_getReference(this.swigCPtr, this);
    }

    public boolean isStencilPropertyEnabled() {
        return SGJNI.SGStencilProperty_isStencilPropertyEnabled(this.swigCPtr, this);
    }

    public void setFunction(SGStencilFunction function, int value, int mask) {
        SGJNI.SGStencilProperty_setFunction(this.swigCPtr, this, SGJNI.getData(function), value, mask);
    }

    public void setGlobalMask(int mask) {
        SGJNI.SGStencilProperty_setGlobalMask(this.swigCPtr, this, mask);
    }

    public void setStencilOperation(SGStencilOperation stencilFail, SGStencilOperation depthFail, SGStencilOperation depthPass) {
        SGJNI.SGStencilProperty_setStencilOperation(this.swigCPtr, this, SGJNI.getData(stencilFail), SGJNI.getData(depthFail), SGJNI.getData(depthPass));
    }

    public void setStencilPropertyEnabled(boolean enabled) {
        SGJNI.SGStencilProperty_setStencilPropertyEnabled(this.swigCPtr, this, enabled);
    }
}
