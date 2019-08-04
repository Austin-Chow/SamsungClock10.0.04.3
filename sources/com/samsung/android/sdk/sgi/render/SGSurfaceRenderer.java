package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;

public class SGSurfaceRenderer extends SGSurfaceRendererBase {
    private boolean mKeep;

    private void keep() {
        SGMemoryRegistrator registry = SGMemoryRegistrator.getInstance();
        long cptr = SGSurfaceRendererBase.getCPtr(this);
        registry.Register(this, cptr);
        registry.AddToManagementList(cptr);
        this.mKeep = true;
    }

    private void release() {
        if (this.mKeep) {
            SGMemoryRegistrator registry = SGMemoryRegistrator.getInstance();
            long cptr = SGSurfaceRendererBase.getCPtr(this);
            registry.RemoveFromManagementList(cptr);
            registry.Deregister(cptr);
            this.mKeep = false;
        }
    }

    public /* bridge */ /* synthetic */ void finalize() {
        super.finalize();
    }

    public void onCreateTexture(int id) {
        try {
            keep();
        } catch (Exception e) {
            SGRenderException.handle(e, "SGSurfaceRenderer::onCreateTexture error: uncaught exception");
        }
    }

    public void onDestroyTexture() {
        try {
            release();
        } catch (Exception e) {
            SGRenderException.handle(e, "SGSurfaceRenderer::onDestroyTexture error: uncaught exception");
        }
    }

    public void onDraw(int id) {
    }
}
