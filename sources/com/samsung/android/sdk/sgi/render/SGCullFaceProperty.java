package com.samsung.android.sdk.sgi.render;

public final class SGCullFaceProperty extends SGProperty {
    public SGCullFaceProperty() {
        this(SGJNI.new_SGCullFaceProperty(), true);
    }

    protected SGCullFaceProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGCullType getCullType() {
        return ((SGCullType[]) SGCullType.class.getEnumConstants())[SGJNI.SGCullFaceProperty_getCullType(this.swigCPtr, this)];
    }

    public SGFrontType getFrontType() {
        return ((SGFrontType[]) SGFrontType.class.getEnumConstants())[SGJNI.SGCullFaceProperty_getFrontType(this.swigCPtr, this)];
    }

    public boolean isFaceCullingEnabled() {
        return SGJNI.SGCullFaceProperty_isFaceCullingEnabled(this.swigCPtr, this);
    }

    public void setCullType(SGCullType type) {
        SGJNI.SGCullFaceProperty_setCullType(this.swigCPtr, this, SGJNI.getData(type));
    }

    public void setFaceCullingEnabled(boolean enabled) {
        SGJNI.SGCullFaceProperty_setFaceCullingEnabled(this.swigCPtr, this, enabled);
    }

    public void setFrontType(SGFrontType type) {
        SGJNI.SGCullFaceProperty_setFrontType(this.swigCPtr, this, SGJNI.getData(type));
    }
}
