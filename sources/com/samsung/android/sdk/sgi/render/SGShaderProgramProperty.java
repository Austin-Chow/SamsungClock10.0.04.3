package com.samsung.android.sdk.sgi.render;

public final class SGShaderProgramProperty extends SGProperty {
    private SGShaderProgramProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGShaderProgramProperty(SGShaderProperty vertexShader, SGShaderProperty fragmentShader) {
        this(SGJNI.new_SGShaderProgramProperty(SGProperty.getCPtr(vertexShader), vertexShader, SGProperty.getCPtr(fragmentShader), fragmentShader), true);
    }

    public SGShaderProperty getShader(SGShaderType type) {
        return SGJNI.SGShaderProgramProperty_getShader(this.swigCPtr, this, SGJNI.getData(type));
    }
}
