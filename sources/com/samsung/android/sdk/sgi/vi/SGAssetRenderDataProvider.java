package com.samsung.android.sdk.sgi.vi;

import android.content.Context;
import android.util.Log;
import com.samsung.android.sdk.sgi.render.SGRenderDataProvider;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public final class SGAssetRenderDataProvider extends SGRenderDataProvider {
    private Context mContext;
    private ShaderProgramBinaryRegistry mShaderProgramBinaries;
    private ShaderSourceRegistry mShaderSources;
    private String mShadersPath;

    static final class ShaderProgramBinaryRegistry {
        private static final String DATA_FILE_NAME = "shader-program-binary-info";
        private Context mContext;
        private HashMap<String, ProgramHash> mRegistry;

        public static final class ProgramHash implements Serializable {
            public final int fragment;
            public final int vertex;

            ProgramHash(int vertex, int fragment) {
                this.vertex = vertex;
                this.fragment = fragment;
            }
        }

        public ShaderProgramBinaryRegistry(Context context) {
            this.mContext = context;
            try {
                ObjectInputStream is = new ObjectInputStream(this.mContext.openFileInput(DATA_FILE_NAME));
                this.mRegistry = (HashMap) is.readObject();
                is.close();
            } catch (Exception e) {
                this.mRegistry = new HashMap();
            }
        }

        private ProgramHash getHash(String vertexShaderName, String fragmentShaderName) {
            return (ProgramHash) this.mRegistry.get(getName(vertexShaderName, fragmentShaderName));
        }

        private String getName(String vertexShaderName, String fragmentShaderName) {
            return vertexShaderName + '@' + fragmentShaderName;
        }

        private void save() {
            try {
                ObjectOutputStream os = new ObjectOutputStream(this.mContext.openFileOutput(DATA_FILE_NAME, 0));
                os.writeObject(this.mRegistry);
                os.close();
            } catch (Exception e) {
                Log.e("SGAssetRenderDataProvider", String.format("shader program binary save failed: %s", new Object[]{e.getMessage()}));
            }
        }

        private void setHash(String vertexShaderName, String fragmentShaderName, ProgramHash hash) {
            this.mRegistry.put(getName(vertexShaderName, fragmentShaderName), hash);
            save();
        }

        public byte[] load(String vertexShaderName, String fragmentShaderName, ProgramHash hash) {
            ProgramHash presentHash = getHash(vertexShaderName, fragmentShaderName);
            if (presentHash != null && presentHash.vertex == hash.vertex && presentHash.fragment == hash.fragment) {
                try {
                    return SGAssetRenderDataProvider.readStream(this.mContext.openFileInput(getName(vertexShaderName, fragmentShaderName)));
                } catch (Exception e) {
                    Log.e("SGAssetRenderDataProvider", String.format("shader program binary load failed (hit: vertex { name: %s hash: %H }, fragment { name: %s hash: %H }): %s", new Object[]{vertexShaderName, Integer.valueOf(hash.vertex), fragmentShaderName, Integer.valueOf(hash.fragment), e.getMessage()}));
                }
            }
            return null;
        }

        public void save(String vertexShaderName, String fragmentShaderName, ProgramHash hash, byte[] program) {
            try {
                this.mContext.openFileOutput(getName(vertexShaderName, fragmentShaderName), 0).write(program);
                setHash(vertexShaderName, fragmentShaderName, hash);
            } catch (Exception e) {
                Log.e("SGAssetRenderDataProvider", String.format("shader program binary save failed (vertex { name: %s hash: %H }, fragment { name: %s hash: %H }): %s", new Object[]{vertexShaderName, Integer.valueOf(hash.vertex), fragmentShaderName, Integer.valueOf(hash.fragment), e.getMessage()}));
            }
        }
    }

    static final class ShaderSourceRegistry {
        private static final String DATA_FILE_NAME = "shader-source-info";
        private Context mContext;
        private long mCurrentVersion;
        private HashMap<String, ShaderItem> mRegistry;
        private String mShaderPath;

        private static class ShaderItem implements Serializable {
            public transient byte[] body;
            public int hash;
            public long version;

            public ShaderItem(byte[] body, long version) {
                this.body = body;
                update(body, version);
            }

            public void update(byte[] body, long version) {
                this.body = body;
                if (this.version != version) {
                    this.version = version;
                    this.hash = Arrays.hashCode(body);
                }
            }
        }

        public ShaderSourceRegistry(Context context, String shaderPath) {
            this.mContext = context;
            this.mShaderPath = shaderPath;
            try {
                this.mCurrentVersion = context.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).lastUpdateTime;
            } catch (Exception e) {
                this.mCurrentVersion = 0;
            }
            try {
                ObjectInputStream is = new ObjectInputStream(this.mContext.openFileInput(DATA_FILE_NAME));
                this.mRegistry = (HashMap) is.readObject();
                is.close();
            } catch (Exception e2) {
                this.mRegistry = new HashMap();
            }
        }

        private ShaderItem loadShaderBody(String name) {
            Exception e;
            ShaderItem si = (ShaderItem) this.mRegistry.get(name);
            if (si == null || si.body == null) {
                ShaderItem si2;
                try {
                    byte[] body = SGRenderDataProvider.loadBuiltinShaderData(name);
                    if (body == null) {
                        body = SGAssetRenderDataProvider.loadShaderFromAssets(this.mContext, this.mShaderPath, name);
                    }
                    if (si == null) {
                        si2 = new ShaderItem(body, this.mCurrentVersion);
                        try {
                            this.mRegistry.put(name, si2);
                            si = si2;
                        } catch (Exception e2) {
                            e = e2;
                            si = si2;
                            Log.e("SGAssetRenderDataProvider", String.format("shader's '%s' body load failed: %s", new Object[]{name, e.getMessage()}));
                            si2 = si;
                            return null;
                        }
                    }
                    si.update(body, this.mCurrentVersion);
                    save();
                    si2 = si;
                    return si;
                } catch (Exception e3) {
                    e = e3;
                    Log.e("SGAssetRenderDataProvider", String.format("shader's '%s' body load failed: %s", new Object[]{name, e.getMessage()}));
                    si2 = si;
                    return null;
                }
            }
            return si;
        }

        public byte[] get(String name) {
            ShaderItem si = loadShaderBody(name);
            return si == null ? null : si.body;
        }

        public int getHash(String name) {
            ShaderItem si = (ShaderItem) this.mRegistry.get(name);
            if (si == null || si.version != this.mCurrentVersion) {
                si = loadShaderBody(name);
            }
            if (si != null) {
                return si.hash;
            }
            throw new ArrayIndexOutOfBoundsException();
        }

        public void save() {
            try {
                ObjectOutputStream os = new ObjectOutputStream(this.mContext.openFileOutput(DATA_FILE_NAME, 0));
                os.writeObject(this.mRegistry);
                os.close();
            } catch (Exception e) {
                Log.e("SGAssetRenderDataProvider", String.format("shader source registry save failed: %s", new Object[]{e.getMessage()}));
            }
        }
    }

    public SGAssetRenderDataProvider(Context context) {
        init(context, "Shaders/", true);
    }

    public SGAssetRenderDataProvider(Context context, String shaderPath) {
        init(context, shaderPath, true);
    }

    public SGAssetRenderDataProvider(Context context, String shaderPath, boolean useBinaryPrograms) {
        init(context, shaderPath, useBinaryPrograms);
    }

    public SGAssetRenderDataProvider(Context context, boolean useBinaryPrograms) {
        init(context, "Shaders/", useBinaryPrograms);
    }

    private ProgramHash getPresentProgramHash(String vertexShaderName, String fragmentShaderName) {
        return new ProgramHash(this.mShaderSources.getHash(vertexShaderName), this.mShaderSources.getHash(fragmentShaderName));
    }

    private void init(Context context, String shadersPath, boolean useBinaryProgram) {
        if (useBinaryProgram) {
            Log.d("SGAssetRenderDataProvider", String.format("shaders path: '%s', binary shader programs support: enabled", new Object[]{shadersPath}));
            this.mShaderSources = new ShaderSourceRegistry(context, shadersPath);
            this.mShaderProgramBinaries = new ShaderProgramBinaryRegistry(context);
            return;
        }
        Log.d("SGAssetRenderDataProvider", String.format("shaders path: '%s', binary shader programs support: disabled", new Object[]{shadersPath}));
        this.mContext = context;
        this.mShadersPath = shadersPath;
    }

    static byte[] loadShaderFromAssets(Context context, String shadersPath, String name) throws Exception {
        InputStream stream = context.getAssets().open(shadersPath + name, 3);
        byte[] bArr = null;
        try {
            bArr = readStream(stream);
            return bArr;
        } finally {
            stream.close();
        }
    }

    static byte[] readStream(InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        while (true) {
            int read = bis.read(buffer, 0, buffer.length);
            if (read == -1) {
                return os.toByteArray();
            }
            os.write(buffer, 0, read);
        }
    }

    public byte[] loadProgram(String vertexShaderName, String fragmentShaderName) {
        return this.mShaderProgramBinaries != null ? this.mShaderProgramBinaries.load(vertexShaderName, fragmentShaderName, getPresentProgramHash(vertexShaderName, fragmentShaderName)) : null;
    }

    public byte[] loadShaderData(String shaderName) {
        if (this.mShaderSources != null) {
            return this.mShaderSources.get(shaderName);
        }
        byte[] body = SGRenderDataProvider.loadBuiltinShaderData(shaderName);
        return body == null ? loadShaderFromAssets(shaderName) : body;
    }

    byte[] loadShaderFromAssets(String name) {
        InputStream stream = null;
        byte[] result = null;
        try {
            stream = this.mContext.getAssets().open(this.mShadersPath + name, 3);
            result = readStream(stream);
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            Log.e("SGAssetRenderDataProvider", String.format("shader '%s' load from assets fail: %s", new Object[]{name, e2.getMessage()}));
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e4) {
                }
            }
        }
        return result;
    }

    public void saveProgram(String vertexShaderName, String fragmentShaderName, byte[] program) {
        if (this.mShaderProgramBinaries != null) {
            this.mShaderProgramBinaries.save(vertexShaderName, fragmentShaderName, getPresentProgramHash(vertexShaderName, fragmentShaderName), program);
        }
    }
}
