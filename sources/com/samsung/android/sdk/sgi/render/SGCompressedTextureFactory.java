package com.samsung.android.sdk.sgi.render;

import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import java.io.FileDescriptor;
import java.io.FileInputStream;

public final class SGCompressedTextureFactory {
    public static SGBitmapTexture2DProperty createTexture(AssetManager assetManager, String assetName) {
        return new SGBitmapTexture2DProperty(SGJNI.SGCompressedTextureFactory_createTexture__SWIG_0(assetManager, assetName), true);
    }

    public static SGBitmapTexture2DProperty createTexture(Resources res, int id) throws Exception {
        AssetInputStream in = null;
        try {
            in = (AssetInputStream) res.openRawResource(id);
            SGBitmapTexture2DProperty createTextureAsset = createTextureAsset(in);
            return createTextureAsset;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static SGBitmapTexture2DProperty createTexture(FileDescriptor fd) throws Exception {
        Throwable th;
        FileInputStream in = null;
        try {
            FileInputStream in2 = new FileInputStream(fd);
            try {
                SGBitmapTexture2DProperty createTexture = createTexture(in2);
                if (in2 != null) {
                    in2.close();
                }
                return createTexture;
            } catch (Throwable th2) {
                th = th2;
                in = in2;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    private static SGBitmapTexture2DProperty createTexture(FileInputStream fileInputStream) {
        return new SGBitmapTexture2DProperty(SGJNI.SGCompressedTextureFactory_createTexture__SWIG_1(fileInputStream), true);
    }

    private static SGBitmapTexture2DProperty createTextureAsset(AssetInputStream assetInputStream) {
        return new SGBitmapTexture2DProperty(SGJNI.SGCompressedTextureFactory_createTextureAsset(assetInputStream), true);
    }
}
