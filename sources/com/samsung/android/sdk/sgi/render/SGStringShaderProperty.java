package com.samsung.android.sdk.sgi.render;

public final class SGStringShaderProperty extends SGShaderProperty {
    protected SGStringShaderProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGStringShaderProperty(SGShaderType type, String source) {
        this(SGJNI.new_SGStringShaderProperty(SGJNI.getData(type), source), true);
    }
}
