package com.samsung.android.sdk.sgi.render;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;

public class SGSurfaceTextureProperty extends SGTexture2DVideoProperty {

    public interface SurfaceTextureCallback {
        void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture);

        void onSurfaceTextureDestroy();
    }

    public interface SurfaceTextureUpdatedCallback {
        void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
    }

    private static class Updater extends SGSurfaceRenderer {
        private int mFramesAvailable;
        private SGTexture2DVideoPropertyWeakRef mPropertyWeak;
        private SurfaceTexture mSurfaceTexture;
        private SurfaceTextureCallback mSurfaceTextureCallback;
        private boolean mSurfaceTextureSingleBufferMode;
        private SurfaceTextureUpdatedCallback mSurfaceTextureUpdatedCallback;

        /* renamed from: com.samsung.android.sdk.sgi.render.SGSurfaceTextureProperty$Updater$1 */
        class C04431 implements OnFrameAvailableListener {
            Handler handler = new Handler(Looper.getMainLooper());

            /* renamed from: com.samsung.android.sdk.sgi.render.SGSurfaceTextureProperty$Updater$1$1 */
            class C04421 implements Runnable {
                C04421() {
                }

                public void run() {
                    Updater.this.surfaceTextureUpdated();
                }
            }

            C04431() {
            }

            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    Updater.this.surfaceTextureUpdated();
                } else {
                    this.handler.post(new C04421());
                }
            }
        }

        public Updater(SGTexture2DVideoProperty property, SurfaceTextureCallback callback, boolean surfaceTextureSingleBufferMode) {
            this.mPropertyWeak = new SGTexture2DVideoPropertyWeakRef(property);
            this.mSurfaceTextureCallback = callback;
            this.mSurfaceTextureSingleBufferMode = surfaceTextureSingleBufferMode;
        }

        private SGMatrix4f getTextureTrasform() {
            float[] t = new float[16];
            this.mSurfaceTexture.getTransformMatrix(t);
            return new SGMatrix4f(t[0], t[4], t[8], t[12], t[1], t[5], t[9], t[13], t[2], t[6], t[10], t[14], t[3], t[7], t[11], t[15]);
        }

        private void surfaceTextureUpdated() {
            synchronized (this) {
                this.mFramesAvailable++;
                this.mPropertyWeak.textureUpdated();
                if (this.mSurfaceTextureUpdatedCallback != null) {
                    this.mSurfaceTextureUpdatedCallback.onSurfaceTextureUpdated(this.mSurfaceTexture);
                }
            }
        }

        public void onCreateTexture(int id) {
            super.onCreateTexture(id);
            this.mSurfaceTexture = new SurfaceTexture(id, this.mSurfaceTextureSingleBufferMode);
            this.mSurfaceTextureCallback.onSurfaceTextureAvailable(this.mSurfaceTexture);
            this.mSurfaceTexture.setOnFrameAvailableListener(new C04431());
        }

        public void onDestroyTexture() {
            this.mSurfaceTextureCallback.onSurfaceTextureDestroy();
            this.mSurfaceTexture.release();
            super.onDestroyTexture();
        }

        public void onDraw(int id) {
            synchronized (this) {
                if (this.mFramesAvailable > 0) {
                    this.mSurfaceTexture.updateTexImage();
                    this.mFramesAvailable = 0;
                    this.mPropertyWeak.updateTextureMatrix(getTextureTrasform());
                }
            }
        }

        public void setTextureUpdatedCallback(SurfaceTextureUpdatedCallback callback) {
            this.mSurfaceTextureUpdatedCallback = callback;
        }
    }

    public SGSurfaceTextureProperty(SurfaceTextureCallback surfaceTextureCallback, String videoRectUniformName) {
        super(videoRectUniformName);
        setUpdater(new Updater(this, surfaceTextureCallback, false));
    }

    public SGSurfaceTextureProperty(SurfaceTextureCallback surfaceTextureCallback, String videoRectUniformName, boolean surfaceTextureSingleBufferMode) {
        super(videoRectUniformName);
        setUpdater(new Updater(this, surfaceTextureCallback, surfaceTextureSingleBufferMode));
    }

    public void setTextureUpdatedCallback(SurfaceTextureUpdatedCallback callback) {
        SGSurfaceRenderer renderer = getRenderer();
        if (renderer instanceof Updater) {
            ((Updater) renderer).setTextureUpdatedCallback(callback);
        }
    }

    public /* bridge */ /* synthetic */ void textureUpdated() {
        super.textureUpdated();
    }

    public /* bridge */ /* synthetic */ void updateTextureMatrix(SGMatrix4f sGMatrix4f) {
        super.updateTextureMatrix(sGMatrix4f);
    }
}
