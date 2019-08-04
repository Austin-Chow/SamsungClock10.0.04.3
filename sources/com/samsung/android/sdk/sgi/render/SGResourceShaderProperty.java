package com.samsung.android.sdk.sgi.render;

public final class SGResourceShaderProperty extends SGShaderProperty {
    protected SGResourceShaderProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGResourceShaderProperty(SGShaderType type, String name) {
        this(SGJNI.new_SGResourceShaderProperty(SGJNI.getData(type), name), true);
    }
}
