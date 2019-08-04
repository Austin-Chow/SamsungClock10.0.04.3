package com.samsung.android.sdk.sgi.render;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGVector2i;

public class SGTextureAtlas {
    public static final long INVALID_HANDLE = 0;
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    public SGTextureAtlas(int width, int height) {
        this(SGJNI.new_SGTextureAtlas__SWIG_2(width, height), true);
    }

    private SGTextureAtlas(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGTextureAtlas(SGBitmapTexture2DProperty texture) {
        this(SGJNI.new_SGTextureAtlas__SWIG_0(SGProperty.getCPtr(texture), texture), true);
    }

    public SGTextureAtlas(SGSharedTextureProperty texture) {
        this(SGJNI.new_SGTextureAtlas__SWIG_1(SGProperty.getCPtr(texture), texture), true);
    }

    public static long getCPtr(SGTextureAtlas obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public boolean addPatch(long handle, Bitmap bitmap, SGVector2i srcTopLeft, boolean autoRecycle) {
        return SGJNI.SGTextureAtlas_addPatch(this.swigCPtr, this, handle, bitmap, srcTopLeft == null ? null : srcTopLeft.getData(), autoRecycle);
    }

    public boolean addPatch(long handle, Bitmap bitmap, boolean autoRecycle) {
        return addPatch(handle, bitmap, null, autoRecycle);
    }

    public long addRect(int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("Negative width argument");
        } else if (height >= 0) {
            return SGJNI.SGTextureAtlas_addRect__SWIG_0(this.swigCPtr, this, width, height);
        } else {
            throw new IllegalArgumentException("Negative height argument");
        }
    }

    public long addRect(Bitmap bitmap, boolean autoRecycle) {
        return SGJNI.SGTextureAtlas_addRect__SWIG_1(this.swigCPtr, this, bitmap, autoRecycle);
    }

    public void clear() {
        SGJNI.SGTextureAtlas_clear(this.swigCPtr, this);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGTextureAtlas(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public int getHeight() {
        return SGJNI.SGTextureAtlas_getHeight(this.swigCPtr, this);
    }

    public Rect getRect(long handle) {
        return new Rect(SGJNI.SGTextureAtlas_getRect(this.swigCPtr, this, handle)[0], SGJNI.SGTextureAtlas_getRect(this.swigCPtr, this, handle)[1], SGJNI.SGTextureAtlas_getRect(this.swigCPtr, this, handle)[2], SGJNI.SGTextureAtlas_getRect(this.swigCPtr, this, handle)[3]);
    }

    public SGTextureProperty getTexture() {
        return SGJNI.SGTextureAtlas_getTexture(this.swigCPtr, this);
    }

    public RectF getTextureCoords(long handle) {
        return new RectF(SGJNI.SGTextureAtlas_getTextureCoords(this.swigCPtr, this, handle)[0], SGJNI.SGTextureAtlas_getTextureCoords(this.swigCPtr, this, handle)[1], SGJNI.SGTextureAtlas_getTextureCoords(this.swigCPtr, this, handle)[2], SGJNI.SGTextureAtlas_getTextureCoords(this.swigCPtr, this, handle)[3]);
    }

    public int getWidth() {
        return SGJNI.SGTextureAtlas_getWidth(this.swigCPtr, this);
    }

    public boolean isEmpty() {
        return SGJNI.SGTextureAtlas_isEmpty(this.swigCPtr, this);
    }

    public boolean isFilled() {
        return SGJNI.SGTextureAtlas_isFilled(this.swigCPtr, this);
    }

    public boolean removeRect(long handle) {
        return SGJNI.SGTextureAtlas_removeRect(this.swigCPtr, this, handle);
    }
}
