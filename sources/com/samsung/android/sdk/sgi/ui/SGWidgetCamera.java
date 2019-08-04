package com.samsung.android.sdk.sgi.ui;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.util.Log;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGIntProperty;
import com.samsung.android.sdk.sgi.render.SGResourceShaderProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.render.SGShaderType;
import com.samsung.android.sdk.sgi.render.SGSurfaceTextureProperty;
import com.samsung.android.sdk.sgi.render.SGSurfaceTextureProperty.SurfaceTextureCallback;

public class SGWidgetCamera extends SGWidgetImage {
    private static final int BACK_CAMERA_FACE = 1;
    private static final String CAMERA_FACE_NAME = "SGCameraFace";
    private static final String CAMERA_ORIENTATION_NAME = "SGCameraOrientation";
    private static final String FRAGMENT_SHADER_NAME = "Camera.frag";
    private static final int FRONT_CAMERA_FACE = 0;
    private static final String SURFACE_TEXTURE_PROPERTY = "SGTextureVideo";
    private static final String TAG = SGWidgetCamera.class.getName();
    private static final String VERTEX_SHADER_NAME = "Camera.vert";
    private static final String VIDEO_RECT_PROPERTY = "SGVideoRect";
    private CameraSurfaceTextureCallback mCameraSurfaceTextureCallback;
    private DisplayOrientation mDisplayOrientation;
    private SGShaderProgramProperty mShaderProgram;
    private SGSurfaceTextureProperty mSurfaceTextureProperty;
    private SGWidgetDecorator mWidgetDecorator;

    private static class CameraSurfaceTextureCallback implements PreviewCallback, SurfaceTextureCallback {
        private Camera mCamera = null;
        private volatile int mCurrentCamera = 0;
        private Handler mHandler;
        private volatile boolean mIsPreviewStart = false;
        private SGWidgetDecorator mSelf;
        private SurfaceTexture mSurfaceTexture = null;
        private OnActiveCameraChangedListener onActiveCameraChangedListener;

        private static class HandlerRunnable implements Runnable {
            private int mFace;
            private SGWidgetDecorator mWd;

            public HandlerRunnable(SGWidgetDecorator aWd, int aFace) {
                this.mWd = aWd;
                this.mFace = aFace;
            }

            public void run() {
                this.mWd.setProperty(SGWidgetCamera.CAMERA_FACE_NAME, new SGIntProperty(this.mFace));
            }
        }

        public CameraSurfaceTextureCallback(SGWidgetDecorator aSelf, Handler aHandler, int cameraId) {
            checkCameraId(cameraId);
            this.mHandler = aHandler;
            this.mCurrentCamera = cameraId;
            this.mSelf = aSelf;
        }

        private void createCamera(int cameraId) {
            if (this.mSurfaceTexture == null) {
                throw new IllegalStateException("SurfaceTexture isn't ready");
            }
            try {
                synchronized (this) {
                    this.mCamera = Camera.open(cameraId);
                    this.mCamera.setPreviewTexture(this.mSurfaceTexture);
                    this.mCamera.setOneShotPreviewCallback(this);
                    this.mCamera.startPreview();
                }
            } catch (Exception e) {
                Log.e(SGWidgetCamera.TAG, "Create camera failed!" + e.getMessage());
            }
            Log.d("MyDebug", "finish");
        }

        private boolean destroyCamera() {
            try {
                synchronized (this) {
                    if (this.mCamera == null) {
                        return false;
                    }
                    this.mCamera.stopPreview();
                    this.mCamera.setOneShotPreviewCallback(null);
                    this.mCamera.release();
                    this.mCamera = null;
                    return true;
                }
            } catch (Exception e) {
                Log.d(SGWidgetCamera.TAG, "Destroy camera failed!" + e.getMessage());
                return false;
            }
        }

        public void checkCameraId(int cameraId) {
            if (cameraId >= Camera.getNumberOfCameras() || cameraId < 0) {
                throw new IllegalArgumentException("invalid Camera ID");
            }
        }

        public Camera getCamera() {
            return this.mCamera;
        }

        public boolean isPreviewStarted() {
            return this.mIsPreviewStart;
        }

        public void onPreviewFrame(byte[] data, Camera camera) {
            int faceOrientation = 1;
            this.mIsPreviewStart = true;
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(this.mCurrentCamera, info);
            if (info.facing == 0) {
                faceOrientation = 0;
            }
            this.mHandler.post(new HandlerRunnable(this.mSelf, faceOrientation));
            if (this.onActiveCameraChangedListener != null) {
                this.onActiveCameraChangedListener.onActiveCameraChanged();
                this.onActiveCameraChangedListener = null;
            }
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture) {
            synchronized (this) {
                this.mSurfaceTexture = surfaceTexture;
            }
            createCamera(this.mCurrentCamera);
        }

        public void onSurfaceTextureDestroy() {
            destroyCamera();
            synchronized (this) {
                this.mSurfaceTexture = null;
            }
        }

        public void setActiveCamera(int cameraId) {
            checkCameraId(cameraId);
            boolean destroied = false;
            if (this.mCamera != null) {
                if (cameraId != this.mCurrentCamera) {
                    destroied = destroyCamera();
                } else {
                    return;
                }
            }
            if (destroied) {
                this.mCurrentCamera = cameraId;
                createCamera(this.mCurrentCamera);
            }
        }

        public void setActiveCameraChangedListener(OnActiveCameraChangedListener listener) {
            this.onActiveCameraChangedListener = listener;
        }
    }

    public enum DisplayOrientation {
        LANDSCAPE,
        PORTRAIT
    }

    public interface OnActiveCameraChangedListener {
        void onActiveCameraChanged();
    }

    public SGWidgetCamera(SGVector2f size, int aCameraId) {
        this(size, DisplayOrientation.LANDSCAPE, aCameraId);
    }

    public SGWidgetCamera(SGVector2f size, DisplayOrientation aDefaultOrientation, int aCameraId) {
        super(size, -1);
        this.mDisplayOrientation = DisplayOrientation.LANDSCAPE;
        this.mDisplayOrientation = aDefaultOrientation;
        this.mWidgetDecorator = new SGWidgetDecorator(this);
        this.mCameraSurfaceTextureCallback = new CameraSurfaceTextureCallback(this.mWidgetDecorator, new Handler(), aCameraId);
        this.mSurfaceTextureProperty = new SGSurfaceTextureProperty(this.mCameraSurfaceTextureCallback, VIDEO_RECT_PROPERTY);
        this.mShaderProgram = new SGShaderProgramProperty(new SGResourceShaderProperty(SGShaderType.VERTEX, VERTEX_SHADER_NAME), new SGResourceShaderProperty(SGShaderType.FRAGMENT, FRAGMENT_SHADER_NAME));
        SGIntProperty SGCameraOrientation = new SGIntProperty(aDefaultOrientation.ordinal());
        SGIntProperty SGCameraFace = new SGIntProperty(aCameraId % 2 == 0 ? 0 : 1);
        this.mWidgetDecorator.setProgramProperty(this.mShaderProgram);
        this.mWidgetDecorator.setProperty(SURFACE_TEXTURE_PROPERTY, this.mSurfaceTextureProperty);
        this.mWidgetDecorator.setProperty(CAMERA_FACE_NAME, SGCameraFace);
        this.mWidgetDecorator.setProperty(CAMERA_ORIENTATION_NAME, SGCameraOrientation);
    }

    public final boolean canSwitchCamera() {
        return this.mCameraSurfaceTextureCallback.isPreviewStarted();
    }

    public void finalize() {
        super.finalize();
        this.mCameraSurfaceTextureCallback.destroyCamera();
    }

    public final Camera getCamera() {
        return this.mCameraSurfaceTextureCallback.getCamera();
    }

    public final boolean releaseCamera() {
        return this.mCameraSurfaceTextureCallback.destroyCamera();
    }

    public final void setActiveCamera(int cameraId, OnActiveCameraChangedListener listener) {
        this.mCameraSurfaceTextureCallback.setActiveCameraChangedListener(listener);
        this.mCameraSurfaceTextureCallback.setActiveCamera(cameraId);
    }

    public void updateDisplayOrientation(DisplayOrientation aDisplayOrientation) {
        if (this.mDisplayOrientation != aDisplayOrientation) {
            this.mWidgetDecorator.setProperty(CAMERA_ORIENTATION_NAME, new SGIntProperty(aDisplayOrientation.ordinal()));
            this.mDisplayOrientation = aDisplayOrientation;
        }
    }
}
