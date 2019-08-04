package com.samsung.android.sdk.sgi.base;

import android.opengl.GLES20;
import android.opengl.GLUtils;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

final class SGOpenGLContext {
    static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    static final int EGL_OPENGL_ES2_BIT = 4;
    static final int EGL_OPENGL_ES3_BIT = 64;
    static final int EGL_OPENGL_ES_BIT = 1;
    boolean mAlreadyExist;
    EGL10 mEGL = ((EGL10) EGLContext.getEGL());
    EGLContext mEGLContext = this.mEGL.eglGetCurrentContext();
    EGLDisplay mEGLDisplay;
    EGLSurface mEGLSurface;
    GL10 mGL;

    public SGOpenGLContext() {
        if (this.mEGLContext == null || this.mEGLContext.equals(EGL10.EGL_NO_CONTEXT)) {
            this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEGLDisplay.equals(EGL10.EGL_NO_DISPLAY)) {
                throw new RuntimeException("GL display initialization error");
            }
            if (this.mEGL.eglInitialize(this.mEGLDisplay, new int[2])) {
                EGLConfig config = getConfig();
                selectOpenGLVersion(config, 2);
                if (this.mEGLContext == null) {
                    this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, config, EGL10.EGL_NO_CONTEXT, null);
                }
                if (this.mEGLContext == null || this.mEGLContext.equals(EGL10.EGL_NO_CONTEXT)) {
                    throw new RuntimeException("GL error: " + GLUtils.getEGLErrorString(this.mEGL.eglGetError()));
                }
                this.mEGLSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, config, new int[]{12375, 1, 12374, 1, 12344});
                if (this.mEGLSurface == null || this.mEGLSurface == EGL10.EGL_NO_SURFACE) {
                    throw new RuntimeException("GL error: " + GLUtils.getEGLErrorString(this.mEGL.eglGetError()));
                } else if (!this.mEGL.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
                    throw new RuntimeException("GL error: " + GLUtils.getEGLErrorString(this.mEGL.eglGetError()));
                }
            }
            throw new RuntimeException("GL error: " + GLUtils.getEGLErrorString(this.mEGL.eglGetError()));
        }
        this.mAlreadyExist = true;
        this.mGL = (GL10) this.mEGLContext.getGL();
    }

    private EGLConfig getConfig() {
        int[] attribList = new int[]{12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12344};
        int[] numConfig = new int[1];
        this.mEGL.eglChooseConfig(this.mEGLDisplay, attribList, null, 0, numConfig);
        int configSize = numConfig[0];
        EGLConfig[] configs = new EGLConfig[configSize];
        if (this.mEGL.eglChooseConfig(this.mEGLDisplay, attribList, configs, configSize, numConfig)) {
            return configs[0];
        }
        throw new RuntimeException("Failed to choose config: " + GLUtils.getEGLErrorString(this.mEGL.eglGetError()));
    }

    private int getInt(int attr) {
        int[] value = new int[1];
        GLES20.glGetIntegerv(attr, value, 0);
        return value[0];
    }

    private String getString(int attr) {
        return this.mGL.glGetString(attr);
    }

    private void selectOpenGLVersion(EGLConfig config, int glVersion) {
        this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, config, EGL10.EGL_NO_CONTEXT, new int[]{EGL_CONTEXT_CLIENT_VERSION, glVersion, 12344});
    }

    public void destroy() {
        if (!this.mAlreadyExist) {
            if (this.mEGLSurface != null) {
                this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
                this.mEGLSurface = null;
            }
            if (this.mEGLContext != null) {
                this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
                this.mEGLContext = null;
            }
            if (this.mEGLDisplay != null) {
                this.mEGL.eglTerminate(this.mEGLDisplay);
                this.mEGLDisplay = null;
            }
        }
        this.mGL = null;
        this.mEGL = null;
        this.mEGLContext = null;
    }

    public SGOpenGLInformation getGLInfo() {
        SGOpenGLInformation info = new SGOpenGLInformation();
        info.Vendor = getString(7936);
        info.Renderer = getString(7937);
        info.Version = getString(7938);
        info.ShadingLangVersion = getString(35724);
        info.Extensions = getString(7939);
        info.MaxTextureSize = getInt(3379);
        return info;
    }
}
