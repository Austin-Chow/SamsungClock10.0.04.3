package com.sec.android.app.clockpackage.worldclock.sgi;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.Matrix;
import android.util.TypedValue;
import com.samsung.android.sdk.sgi.render.SGAlphaBlendProperty;
import com.samsung.android.sdk.sgi.render.SGBlendFactor;
import com.samsung.android.sdk.sgi.render.SGCullFaceProperty;
import com.samsung.android.sdk.sgi.render.SGDitherProperty;
import com.samsung.android.sdk.sgi.render.SGFloatProperty;
import com.samsung.android.sdk.sgi.render.SGFrontType;
import com.samsung.android.sdk.sgi.render.SGMatrix4fProperty;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.render.SGShaderType;
import com.samsung.android.sdk.sgi.render.SGStringShaderProperty;
import com.samsung.android.sdk.sgi.render.SGVectorfProperty;
import com.samsung.android.sdk.sgi.vi.SGLayer;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;

public final class SGILayerConfig {
    protected static final float[] REVEAL_POINTS = new float[]{13.965f, Constants.REVEAL_POINT_1, 3.1f, 2.5f, 1.75f};
    private float mAspectRatio = -1.0f;
    private final SGAlphaBlendProperty mBlendingCityProperty;
    private final SGAlphaBlendProperty mBlendingProperty;
    private final float[] mCameraDirection;
    private final SGFloatProperty mCameraDistanceProperty;
    private final SGCullFaceProperty mCullfaceProperty;
    protected final int mDPI;
    protected float mDistance;
    private SGDitherProperty mDitherProperty;
    protected final float[] mEye;
    private float mHRotation = 0.0f;
    private final SGMatrix4fProperty mMVPMatrixProperty;
    private final SGVectorfProperty mOzProperty;
    protected final float[] mProjMat;
    private final SGFloatProperty mPxInScreenXProperty;
    private final SGFloatProperty mPxInScreenYProperty;
    private final Resources mResources;
    private float mScreenHeight;
    private final float[] mTempMat = new float[16];
    private float mVRotation = 0.0f;
    protected final float[] mViewMat;
    private final float mYLayersOffset;

    protected SGILayerConfig(boolean dither, int yLayersOffset, Context context) {
        this.mResources = context.getResources();
        TypedValue multiplier = new TypedValue();
        this.mResources.getValue(C0836R.dimen.worldclock_zoom_multiplier, multiplier, true);
        Constants.MAXIMUM_MARKER_SCALE = multiplier.getFloat();
        if (StateUtils.isContextInDexMode(context)) {
            Constants.MAXIMUM_MARKER_SCALE = 1.0f;
        }
        this.mDPI = this.mResources.getDisplayMetrics().densityDpi;
        this.mYLayersOffset = (float) yLayersOffset;
        this.mCullfaceProperty = new SGCullFaceProperty();
        this.mCullfaceProperty.setFrontType(SGFrontType.CW);
        this.mCullfaceProperty.setFaceCullingEnabled(true);
        this.mBlendingProperty = new SGAlphaBlendProperty();
        this.mBlendingProperty.setAlphaBlendingEnabled(true);
        this.mBlendingCityProperty = new SGAlphaBlendProperty();
        this.mBlendingCityProperty.setAlphaBlendingEnabled(true);
        this.mBlendingCityProperty.setFactors(SGBlendFactor.SRC_ALPHA, SGBlendFactor.ONE_MINUS_SRC_ALPHA);
        this.mMVPMatrixProperty = new SGMatrix4fProperty();
        this.mCameraDistanceProperty = new SGFloatProperty();
        this.mPxInScreenXProperty = new SGFloatProperty();
        this.mPxInScreenYProperty = new SGFloatProperty();
        this.mOzProperty = new SGVectorfProperty(3);
        if (dither) {
            this.mDitherProperty = new SGDitherProperty();
            this.mDitherProperty.setDitherEnabled(true);
        }
        this.mCameraDirection = new float[3];
        this.mEye = new float[3];
        this.mViewMat = new float[16];
        this.mProjMat = new float[16];
    }

    protected float getYLayerOffset() {
        return this.mYLayersOffset;
    }

    protected void setScreenSize(int width, int height) {
        Constants.GLOBE_SIZE_LEVEL_0 = (float) ((int) this.mResources.getDimension(C0836R.dimen.worldclock_start_earth_size));
        Constants.GLOBE_SIZE_REVEAL_POINT_1 = (float) ((int) this.mResources.getDimension(C0836R.dimen.worldclock_reveal_earth_size));
        recalculateConstants();
        this.mScreenHeight = (float) height;
        this.mPxInScreenXProperty.set(2.0f / ((float) width));
        this.mPxInScreenYProperty.set(2.0f / ((float) height));
        this.mAspectRatio = ((float) width) / ((float) height);
        perspectiveM(this.mProjMat, 500.0f);
    }

    private void recalculateConstants() {
        Constants.REVEAL_POINT_1 = (10.5f * Constants.GLOBE_SIZE_LEVEL_0) / Constants.GLOBE_SIZE_REVEAL_POINT_1;
        REVEAL_POINTS[1] = Constants.REVEAL_POINT_1;
    }

    protected void setupLayer(SGLayer layer, int options, String vertexShader, String fragmentShader) {
        if (this.mDitherProperty != null) {
            layer.setProperty("SGDither", this.mDitherProperty);
        }
        if ((options & 1) != 0) {
            layer.setProperty("SGCullface", this.mCullfaceProperty);
        }
        if ((options & 2) != 0) {
            layer.setProperty(SGProperty.ALPHA_BLEND, this.mBlendingProperty);
        }
        if ((options & 8) != 0) {
            layer.setProperty(SGProperty.ALPHA_BLEND, this.mBlendingCityProperty);
        }
        if ((options & 4) != 0) {
            layer.setProperty("pxInScreenX", this.mPxInScreenXProperty);
            layer.setProperty("pxInScreenY", this.mPxInScreenYProperty);
        }
        if ((options & 16) != 0) {
            layer.setProperty("ModelViewProjection", this.mMVPMatrixProperty);
        }
        if ((options & 32) != 0) {
            layer.setProperty("cameraDistance", this.mCameraDistanceProperty);
        }
        if ((options & 128) != 0) {
            layer.setProperty("camVector", this.mOzProperty);
        }
        if (vertexShader != null) {
            layer.setProgramProperty(new SGShaderProgramProperty(new SGStringShaderProperty(SGShaderType.VERTEX, vertexShader), new SGStringShaderProperty(SGShaderType.FRAGMENT, fragmentShader)));
        }
    }

    protected void updateCamera(float distance, float hRotation, float vRotation) {
        if (distance != this.mDistance) {
            this.mDistance = distance;
            this.mCameraDistanceProperty.set(this.mDistance);
        }
        if (!(this.mHRotation == hRotation && this.mVRotation == vRotation)) {
            this.mHRotation = hRotation;
            this.mVRotation = vRotation;
            rotationToDirection(this.mCameraDirection, hRotation, vRotation);
        }
        this.mEye[0] = this.mDistance * this.mCameraDirection[0];
        this.mEye[1] = this.mDistance * this.mCameraDirection[1];
        this.mEye[2] = this.mDistance * this.mCameraDirection[2];
        Matrix.setLookAtM(this.mViewMat, 0, this.mEye[0], this.mEye[1], this.mEye[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(this.mTempMat, 0, this.mProjMat, 0, this.mViewMat, 0);
        this.mMVPMatrixProperty.set(this.mTempMat);
        this.mOzProperty.set(this.mCameraDirection[0], this.mCameraDirection[1], this.mCameraDirection[2]);
    }

    private void perspectiveM(float[] m, float zFar) {
        float[] fArr = m;
        createFrustum(fArr, this.mAspectRatio, this.mScreenHeight, Constants.GLOBE_SIZE_LEVEL_0 / 2.0f, 10.5f, this.mYLayersOffset, zFar, 0.35f);
    }

    private void createFrustum(float[] m, float aspect, float screenHeight, float desiredRadius, float distance, float yShift, float zFar, float zNear) {
        float fTop = ((float) Math.tan((double) (0.5f * (2.0f * ((float) Math.atan((double) (((0.5f * screenHeight) + yShift) / (desiredRadius * distance)))))))) * zNear;
        float fBottom = (-((float) Math.tan((double) (0.5f * (2.0f * ((float) Math.atan((double) (((0.5f * screenHeight) - yShift) / (desiredRadius * distance))))))))) * zNear;
        float fW = (((-fBottom) + fTop) * 0.5f) * aspect;
        frustum(m, -fW, fW, fBottom, fTop, zNear, zFar);
    }

    private void frustum(float[] m, float left, float right, float bottom, float top, float nearVal, float farVal) {
        for (int i = 0; i < 16; i++) {
            m[i] = 0.0f;
        }
        m[0] = (2.0f * nearVal) / (right - left);
        m[5] = (2.0f * nearVal) / (top - bottom);
        m[8] = (right + left) / (right - left);
        m[9] = (top + bottom) / (top - bottom);
        m[10] = (-(farVal + nearVal)) / (farVal - nearVal);
        m[11] = -1.0f;
        m[14] = (-2.0f * (farVal * nearVal)) / (farVal - nearVal);
    }

    protected static void normalize(float[] p) {
        float mag = (float) Math.sqrt((double) (((p[0] * p[0]) + (p[1] * p[1])) + (p[2] * p[2])));
        p[0] = p[0] / mag;
        p[1] = p[1] / mag;
        p[2] = p[2] / mag;
    }

    protected static void rotationToDirection(float[] direction, float hRotation, float vRotation) {
        float c = (float) Math.cos((double) vRotation);
        direction[0] = ((float) Math.cos((double) hRotation)) * c;
        direction[1] = (float) Math.sin((double) vRotation);
        direction[2] = ((float) Math.sin((double) hRotation)) * c;
    }
}
