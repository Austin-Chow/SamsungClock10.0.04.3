package com.samsung.android.sdk.sgi.base;

import android.util.Log;

public final class SGConfiguration {
    static SGOpenGLInformation mInfo;
    private static boolean mInstance = false;

    private static String getBuildDate() {
        return SGJNI.SGConfiguration_getBuildDate();
    }

    public static SGGLESVersion getGLESVersion(boolean mode) {
        return ((SGGLESVersion[]) SGGLESVersion.class.getEnumConstants())[SGJNI.SGConfiguration_getGLESVersion(mode)];
    }

    static final native int getJniVersion();

    public static synchronized SGOpenGLInformation getOpenGLInformation() {
        Throwable th;
        SGOpenGLInformation sGOpenGLInformation;
        synchronized (SGConfiguration.class) {
            if (mInfo == null) {
                SGOpenGLContext ctx = null;
                try {
                    SGOpenGLContext ctx2 = new SGOpenGLContext();
                    try {
                        mInfo = ctx2.getGLInfo();
                        if (ctx2 != null) {
                            ctx2.destroy();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        ctx = ctx2;
                        if (ctx != null) {
                            ctx.destroy();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (ctx != null) {
                        ctx.destroy();
                    }
                    throw th;
                }
            }
            sGOpenGLInformation = mInfo;
        }
        return sGOpenGLInformation;
    }

    private static int getVersionInformation(int fieldNum) {
        return SGJNI.SGConfiguration_getVersionInformation(fieldNum);
    }

    public static SGVersionInformation getVersionInformation() {
        SGVersionInformation currVersion = new SGVersionInformation();
        currVersion.mMajor = getVersionInformation(0);
        currVersion.mMinor = getVersionInformation(1);
        currVersion.mPatch = getVersionInformation(2);
        currVersion.mBuild = getVersionInformation(3);
        currVersion.mDate = getBuildDate();
        return currVersion;
    }

    static final native String getVersionString();

    public static synchronized void initLibrary() {
        synchronized (SGConfiguration.class) {
            if (!mInstance) {
                try {
                    System.loadLibrary("Sgi");
                } catch (UnsatisfiedLinkError e) {
                    Log.e("SGI", "Native code library failed to load. " + e);
                    System.exit(1);
                }
                mInstance = true;
                if (33619969 != getJniVersion()) {
                    Log.e("SGI", "JNI side: " + getVersionString());
                    Log.e("SGI", "Java side: 2.1.4 Build type: Release Build date: 29.03.2018 Build number:local Core commit hash: 7f3a812ccd17c4f6b54480cdd1f5776b7c8eac9d OpenSDK commit hash: 3ad7433f534e132f6c83709bc4d55866e7fa43fa");
                    String msg = "Java and Native library version don`t match";
                    Log.e("SGI", "Java and Native library version don`t match");
                    throw new Error("Java and Native library version don`t match");
                }
            }
        }
    }

    public static boolean isDebugInfoEnabled() {
        return SGJNI.SGConfiguration_isDebugInfoEnabled();
    }

    public static boolean isSystraceEnabled() {
        return SGJNI.SGConfiguration_isSystraceEnabled();
    }

    public static void setDebugInfoEnabled(boolean enabled) {
        SGJNI.SGConfiguration_setDebugInfoEnabled(enabled);
    }

    public static void setGLESVersion(SGGLESVersion version) {
        SGJNI.SGConfiguration_setGLESVersion(SGJNI.getData(version));
    }

    public static void setSystraceEnabled(boolean enabled) {
        SGJNI.SGConfiguration_setSystraceEnabled(enabled);
    }
}
