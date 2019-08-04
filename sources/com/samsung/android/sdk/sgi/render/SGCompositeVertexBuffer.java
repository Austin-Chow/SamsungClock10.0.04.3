package com.samsung.android.sdk.sgi.render;

import java.nio.FloatBuffer;

public final class SGCompositeVertexBuffer extends SGBuffer {
    protected SGCompositeVertexBuffer(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public FloatBuffer getFloatBuffer() {
        return getByteBuffer().asFloatBuffer();
    }
}
