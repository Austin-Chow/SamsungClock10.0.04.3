package com.samsung.android.sdk.sgi.render;

public final class SGRenderTargetToTexture extends SGRenderTarget {
    public SGRenderTargetToTexture() {
        this(SGJNI.new_SGRenderTargetToTexture(), true);
    }

    protected SGRenderTargetToTexture(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }
}
