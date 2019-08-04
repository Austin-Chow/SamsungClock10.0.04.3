package com.samsung.android.sdk.sgi.render;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGVector2i;
import java.nio.ByteBuffer;

public final class SGSharedTextureProperty extends SGTextureProperty {
    public SGSharedTextureProperty(int width, int height, Config config) {
        this(SwigConstructSGSharedTextureProperty(width, height, config), true);
    }

    public SGSharedTextureProperty(int width, int height, Config config, SGTextureFilterType minFilter, SGTextureFilterType magFilter, SGTextureWrapType wrapS, SGTextureWrapType wrapT) {
        this(SwigConstructSGSharedTextureProperty(width, height, config, minFilter, magFilter, wrapS, wrapT), true);
    }

    protected SGSharedTextureProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGSharedTextureProperty(Bitmap bitmap, SGTextureFilterType minFilter, SGTextureFilterType magFilter, SGTextureWrapType wrapS, SGTextureWrapType wrapT, boolean autoRecycle) {
        this(SGJNI.new_SGSharedTextureProperty__SWIG_2(bitmap, SGJNI.getData(minFilter), SGJNI.getData(magFilter), SGJNI.getData(wrapS), SGJNI.getData(wrapT), autoRecycle), true);
    }

    private static long SwigConstructSGSharedTextureProperty(int width, int height, Config config) {
        if (width <= 0) {
            throw new IllegalArgumentException("Negative width argument");
        } else if (height <= 0) {
            throw new IllegalArgumentException("Negative height argument");
        } else if (config != null) {
            return SGJNI.new_SGSharedTextureProperty__SWIG_0(width, height, config.name());
        } else {
            throw new NullPointerException("Parameter android.graphics.Bitmap.Config is null");
        }
    }

    private static long SwigConstructSGSharedTextureProperty(int width, int height, Config config, SGTextureFilterType minFilter, SGTextureFilterType magFilter, SGTextureWrapType wrapS, SGTextureWrapType wrapT) {
        if (width <= 0) {
            throw new IllegalArgumentException("Negative width argument");
        } else if (height <= 0) {
            throw new IllegalArgumentException("Negative height argument");
        } else if (config == null) {
            throw new NullPointerException("Parameter android.graphics.Bitmap.Config is null");
        } else {
            return SGJNI.new_SGSharedTextureProperty__SWIG_1(width, height, config.name(), SGJNI.getData(minFilter), SGJNI.getData(magFilter), SGJNI.getData(wrapS), SGJNI.getData(wrapT));
        }
    }

    private ByteBuffer getBufferAndLock() {
        return SGJNI.SGSharedTextureProperty_getBufferAndLock(this.swigCPtr, this);
    }

    private void unlockNative() {
        SGJNI.SGSharedTextureProperty_unlockNative(this.swigCPtr, this);
    }

    public void addPatch(Bitmap bitmap, SGVector2i destTopLeft, Rect sourceRect, boolean autoRecycle) {
        SGJNI.SGSharedTextureProperty_addPatch(this.swigCPtr, this, bitmap, destTopLeft.getData(), SGMathNative.getArrayRect(sourceRect), autoRecycle);
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            unlock();
        }
        super.finalize();
    }

    public long getBuffer() {
        return SGJNI.SGSharedTextureProperty_getBuffer(this.swigCPtr, this);
    }

    public SGTextureDataType getFormat() {
        return ((SGTextureDataType[]) SGTextureDataType.class.getEnumConstants())[SGJNI.SGSharedTextureProperty_getFormat(this.swigCPtr, this)];
    }

    public int getHeight() {
        return SGJNI.SGSharedTextureProperty_getHeight(this.swigCPtr, this);
    }

    public int getSize() {
        return SGJNI.SGSharedTextureProperty_getSize(this.swigCPtr, this);
    }

    public SGTextureDataFormat getSpace() {
        return ((SGTextureDataFormat[]) SGTextureDataFormat.class.getEnumConstants())[SGJNI.SGSharedTextureProperty_getSpace(this.swigCPtr, this)];
    }

    public int getStride() {
        return SGJNI.SGSharedTextureProperty_getStride(this.swigCPtr, this);
    }

    public int getWidth() {
        return SGJNI.SGSharedTextureProperty_getWidth(this.swigCPtr, this);
    }

    public ByteBuffer lock() {
        long handle = getHandle();
        ByteBuffer byteBuffer = (ByteBuffer) SGMemoryRegistrator.getInstance().GetObjectByPointer(handle);
        if (byteBuffer != null) {
            return byteBuffer;
        }
        byteBuffer = getBufferAndLock();
        SGMemoryRegistrator.getInstance().Register(byteBuffer, handle);
        return byteBuffer;
    }

    public long lockNative() {
        return SGJNI.SGSharedTextureProperty_lockNative(this.swigCPtr, this);
    }

    public void textureUpdated() {
        SGJNI.SGSharedTextureProperty_textureUpdated(this.swigCPtr, this);
    }

    public void unlock() {
        long handle = getHandle();
        SGMemoryRegistrator reg = SGMemoryRegistrator.getInstance();
        ByteBuffer byteBuffer = (ByteBuffer) reg.GetObjectByPointer(handle);
        if (byteBuffer != null) {
            byteBuffer.clear();
            byteBuffer.limit(0);
            reg.Deregister(handle);
        }
        unlockNative();
    }
}
