package com.samsung.android.sdk.sgi.render;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2i;

public final class SGBitmapTexture2DProperty extends SGTextureProperty {
    protected SGBitmapTexture2DProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGBitmapTexture2DProperty(SGTextureWrapType wrapS, SGTextureWrapType wrapT) {
        this(SGJNI.new_SGBitmapTexture2DProperty(SGJNI.getData(wrapS), SGJNI.getData(wrapT)), true);
    }

    public void addPatch(Bitmap bitmap, SGVector2i destTopLeft, Rect sourceRect, boolean autoRecycle) {
        SGJNI.SGBitmapTexture2DProperty_addPatch(this.swigCPtr, this, bitmap, destTopLeft.getData(), SGMathNative.getArrayRect(sourceRect), autoRecycle);
    }

    public int getHeight() {
        return SGJNI.SGBitmapTexture2DProperty_getHeight(this.swigCPtr, this);
    }

    public int getWidth() {
        return SGJNI.SGBitmapTexture2DProperty_getWidth(this.swigCPtr, this);
    }

    public boolean isGenerateMipmapsEnabled() {
        return SGJNI.SGBitmapTexture2DProperty_isGenerateMipmapsEnabled(this.swigCPtr, this);
    }

    public void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, false);
    }

    public void setBitmap(Bitmap bitmap, boolean autoRecycle) {
        SGJNI.SGBitmapTexture2DProperty_setBitmap(this.swigCPtr, this, bitmap, autoRecycle);
    }

    public void setGenerateMipmapsEnabled(boolean enabled) {
        SGJNI.SGBitmapTexture2DProperty_setGenerateMipmapsEnabled(this.swigCPtr, this, enabled);
    }

    public void setWrapType(SGTextureWrapType wrapS, SGTextureWrapType wrapT) {
        SGJNI.SGBitmapTexture2DProperty_setWrapType(this.swigCPtr, this, SGJNI.getData(wrapS), SGJNI.getData(wrapT));
    }
}
