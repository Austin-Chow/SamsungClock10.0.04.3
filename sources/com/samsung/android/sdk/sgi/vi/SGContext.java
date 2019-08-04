package com.samsung.android.sdk.sgi.vi;

import android.content.Context;
import android.view.Surface;
import com.samsung.android.sdk.sgi.render.SGRenderDataProvider;

final class SGContext {
    private boolean swigCMemOwn;
    private long swigCPtr;

    private SGContext(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGContext(Context applicationContext, SGRenderDataProvider provider) {
        this(SGJNI.new_SGContext(applicationContext, SGRenderDataProvider.getCPtr(provider), provider), true);
    }

    public static long getCPtr(SGContext obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void attachCurrentThread() {
        SGJNI.SGContext_attachCurrentThread(this.swigCPtr, this);
    }

    public boolean attachToNativeWindow(SGSurface surface, Surface window, SGContextConfiguration contextConfiguration) {
        return SGJNI.SGContext_attachToNativeWindow(this.swigCPtr, this, SGSurface.getCPtr(surface), surface, window, contextConfiguration);
    }

    public void attachToSurface(SGSurface surface, SGSurface sharedSurface, int width, int height, SGContextConfiguration contextConfiguration) {
        if (width < 0) {
            throw new IllegalArgumentException("Negative width argument");
        } else if (height < 0) {
            throw new IllegalArgumentException("Negative height argument");
        } else {
            SGJNI.SGContext_attachToSurface(this.swigCPtr, this, SGSurface.getCPtr(surface), surface, SGSurface.getCPtr(sharedSurface), sharedSurface, width, height, contextConfiguration);
        }
    }

    public void detachCurrentThread() {
        SGJNI.SGContext_detachCurrentThread(this.swigCPtr, this);
    }

    public void detachFromNativeWindow(SGSurface surface) {
        SGJNI.SGContext_detachFromNativeWindow(this.swigCPtr, this, SGSurface.getCPtr(surface), surface);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGContext(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }
}
