package com.samsung.android.sdk.sgi.vi;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.TypedValue;
import com.samsung.android.sdk.sgi.render.SGBitmapTexture2DProperty;
import com.samsung.android.sdk.sgi.render.SGCompressedTextureFactory;
import com.samsung.android.sdk.sgi.render.SGTextureProperty;
import com.samsung.android.sdk.sgi.render.SGTextureWrapType;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SGDefaultSceneResourceProvider implements SGSceneResourceProvider {
    protected static final ArrayList<String> mCompressedFormats = new ArrayList(Arrays.asList(new String[]{".PKM", ".ASTC", ".DDS", ".KTX"}));
    protected AssetManager mAssetManager;
    protected boolean mIsPersistentCache;
    protected String mPackageName;
    protected Resources mResources;
    protected Map<String, Object> mResourcesCache;
    protected String mTextureFolder;

    public SGDefaultSceneResourceProvider(AssetManager assetManager) {
        this.mIsPersistentCache = false;
        this.mResourcesCache = new HashMap();
        this.mAssetManager = assetManager;
    }

    public SGDefaultSceneResourceProvider(AssetManager assetManager, String textureFolder) {
        this.mIsPersistentCache = false;
        this.mAssetManager = assetManager;
        this.mResourcesCache = new HashMap();
        setTextureFolder(textureFolder);
    }

    public SGDefaultSceneResourceProvider(Resources res, String packageName) {
        this.mIsPersistentCache = false;
        this.mResourcesCache = new HashMap();
        this.mPackageName = packageName;
        this.mResources = res;
        this.mAssetManager = res.getAssets();
    }

    public SGDefaultSceneResourceProvider(String folder) {
        this.mIsPersistentCache = false;
        this.mResourcesCache = new HashMap();
        if (folder != null && !folder.isEmpty()) {
            setTextureFolder(folder);
        }
    }

    protected static boolean isCompressedFormat(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            String extension = fileName.substring(index);
            Iterator it = mCompressedFormats.iterator();
            while (it.hasNext()) {
                if (((String) it.next()).compareToIgnoreCase(extension) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean isCompressedFormatExtension(String extension) {
        Iterator it = mCompressedFormats.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).indexOf(extension, 1) != -1) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        if (!this.mIsPersistentCache) {
            this.mResourcesCache.clear();
        }
    }

    public Object get(String key) {
        return this.mResourcesCache.get(key);
    }

    public SGDataReader getStream(String path) {
        String prefix = "res:";
        if (path.startsWith("res:")) {
            try {
                return new SGAssetDataReader(this.mResources, Integer.parseInt(path.substring("res:".length())));
            } catch (NumberFormatException e) {
                Log.e("SGI", "Error: Can't parse resource " + path);
                return null;
            }
        } else if (this.mAssetManager == null) {
            String fullName = null;
            if (this.mTextureFolder == null) {
                index = path.lastIndexOf(47);
                if (index >= 0) {
                    this.mTextureFolder = path.substring(0, index + 1);
                }
            } else if (path.charAt(0) != '/') {
                fullName = this.mTextureFolder + path;
            }
            if (fullName == null) {
                fullName = path;
            }
            try {
                return new SGFileDataReader(new FileInputStream(fullName));
            } catch (IOException e2) {
                Log.e("SGI", "Error: Can't load resource " + fullName);
                return null;
            }
        } else {
            if (this.mTextureFolder == null) {
                index = path.lastIndexOf(47);
                if (index >= 0) {
                    this.mTextureFolder = path.substring(0, index + 1);
                }
            }
            return new SGAssetDataReader(this.mAssetManager, path);
        }
    }

    public SGTextureProperty getTexture(String fileName) throws IOException {
        SGBitmapTexture2DProperty texture = null;
        String msg = null;
        InputStream in = null;
        Bitmap bitmap;
        if (!fileName.startsWith("R.") || this.mResources == null) {
            String fullName = (fileName.charAt(0) == '/' || this.mTextureFolder == null) ? fileName : this.mTextureFolder + fileName;
            if (isCompressedFormat(fileName)) {
                FileInputStream fileInputStream = null;
                try {
                    if (this.mAssetManager == null) {
                        FileInputStream fi = new FileInputStream(fullName);
                        try {
                            texture = SGCompressedTextureFactory.createTexture(fi.getFD());
                            fileInputStream = fi;
                        } catch (Exception e) {
                            fileInputStream = fi;
                            msg = "Can't load compressed file " + fullName;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e2) {
                                }
                            }
                            if (msg != null) {
                                Log.e("SGI", msg);
                            }
                            return texture;
                        }
                    }
                    texture = SGCompressedTextureFactory.createTexture(this.mAssetManager, fullName);
                    texture.setWrapType(SGTextureWrapType.REPEAT, SGTextureWrapType.REPEAT);
                } catch (Exception e3) {
                    msg = "Can't load compressed file " + fullName;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (msg != null) {
                        Log.e("SGI", msg);
                    }
                    return texture;
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } else {
                try {
                    if (this.mAssetManager != null) {
                        in = this.mAssetManager.open(fullName);
                        bitmap = BitmapFactory.decodeStream(new BufferedInputStream(in));
                    } else {
                        bitmap = BitmapFactory.decodeFile(fullName);
                    }
                    if (bitmap == null) {
                        throw new IOException();
                    }
                    SGBitmapTexture2DProperty texture2 = new SGBitmapTexture2DProperty(SGTextureWrapType.REPEAT, SGTextureWrapType.REPEAT);
                    try {
                        texture2.setBitmap(bitmap);
                        texture = texture2;
                    } catch (IOException e4) {
                        texture = texture2;
                        msg = this.mAssetManager == null ? "Can't load file " + fullName : "Can't load asset " + fullName;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e5) {
                            }
                        }
                        if (msg != null) {
                            Log.e("SGI", msg);
                        }
                        return texture;
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e6) {
                    if (this.mAssetManager == null) {
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (msg != null) {
                        Log.e("SGI", msg);
                    }
                    return texture;
                }
            }
        }
        int index = fileName.lastIndexOf(".");
        int id = this.mResources.getIdentifier(fileName.substring(index + 1), fileName.substring(2, index), this.mPackageName);
        if (id != 0) {
            TypedValue info = new TypedValue();
            this.mResources.getValue(id, info, true);
            String path = info.string.toString();
            if (isCompressedFormatExtension(path.substring(path.lastIndexOf("/") + 1).toUpperCase())) {
                try {
                    texture = SGCompressedTextureFactory.createTexture(this.mResources, id);
                    texture.setWrapType(SGTextureWrapType.REPEAT, SGTextureWrapType.REPEAT);
                } catch (Exception e7) {
                    msg = "Can't load compressed resource " + fileName;
                }
            } else {
                bitmap = BitmapFactory.decodeResource(this.mResources, id);
                if (bitmap != null) {
                    SGBitmapTexture2DProperty sGBitmapTexture2DProperty = new SGBitmapTexture2DProperty(SGTextureWrapType.REPEAT, SGTextureWrapType.REPEAT);
                    sGBitmapTexture2DProperty.setBitmap(bitmap);
                } else {
                    msg = "Can't load resource " + fileName;
                }
            }
        } else {
            msg = "Can't load resource " + fileName;
        }
        if (msg != null) {
            Log.e("SGI", msg);
        }
        return texture;
    }

    public void put(String key, Object value) {
        this.mResourcesCache.put(key, value);
    }

    public final void setPersistentCache(boolean enable) {
        this.mIsPersistentCache = enable;
    }

    public final void setTextureFolder(String textureFolder) {
        this.mTextureFolder = textureFolder;
        if (this.mTextureFolder != null) {
            int l = this.mTextureFolder.length();
            if (l > 0 && this.mTextureFolder.charAt(l - 1) != '/') {
                this.mTextureFolder += "/";
            }
        }
    }
}
