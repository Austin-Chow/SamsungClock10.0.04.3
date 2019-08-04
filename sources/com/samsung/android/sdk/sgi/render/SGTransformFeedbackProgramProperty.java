package com.samsung.android.sdk.sgi.render;

public final class SGTransformFeedbackProgramProperty extends SGProperty {
    private SGTransformFeedbackProgramProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGTransformFeedbackProgramProperty(String[] outAttrs, SGShaderProperty vertexShader, SGShaderProperty fragmentShader) {
        this(SGJNI.new_SGTransformFeedbackProgramProperty(outAttrs, SGProperty.getCPtr(vertexShader), vertexShader, SGProperty.getCPtr(fragmentShader), fragmentShader), true);
    }

    public String[] getOutputAttrsList() {
        return SGJNI.SGTransformFeedbackProgramProperty_getOutputAttrsList(this.swigCPtr, this);
    }

    public SGShaderProperty getShader(SGShaderType type) {
        return SGJNI.SGTransformFeedbackProgramProperty_getShader(this.swigCPtr, this, SGJNI.getData(type));
    }
}
