package com.sec.android.app.clockpackage.worldclock.sgi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import android.view.animation.DecelerateInterpolator;
import com.samsung.android.sdk.sgi.animation.SGAnimationTimingFunction;
import com.samsung.android.sdk.sgi.animation.SGPredefinedTimingFunctionType;
import com.samsung.android.sdk.sgi.animation.SGTimingFunctionFactory;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;
import com.samsung.android.sdk.sgi.render.SGBitmapTexture2DProperty;
import com.samsung.android.sdk.sgi.render.SGCompressedTextureFactory;
import com.samsung.android.sdk.sgi.vi.SGContextConfiguration;
import com.samsung.android.sdk.sgi.vi.SGSurface;
import com.samsung.android.sdk.sgi.vi.SGSurfaceChangesDrawnListener;
import com.samsung.android.sdk.sgi.vi.SGSurfaceSizeChangeListener;
import com.samsung.android.sdk.sgi.vi.SGView;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.SgiPlayerListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;

public class SGIPlayer {
    private float hRotationPrev_nonwrap;
    private float hRotationPrev_nonwrapstart;
    private boolean isEndOnGlobe;
    private boolean isStartOnGlobe;
    private AccessibilityManager mAccessibilityManager;
    private AccessibilityStateChangeListener mAccessibilityStateChangeListener;
    private int mActualHeight;
    private int mActualWidth;
    private ValueAnimator mAnimator = null;
    private SGICities mBillboard;
    private final SGILayerConfig mConfig;
    private Context mContext;
    private float mDistance;
    private float mDistanceEndVal;
    private SGVector3f mFirstTouchPoint;
    private float mGapFromCenter;
    private SGIGlobeLayer mGlobeLayer;
    private float mHRotation;
    private float mHRotationTransient;
    private float mHalfNearPlaneGap;
    private float mHalfTouchGap;
    private boolean mIsMobileKeyboardCover = false;
    private boolean mIsZoom = false;
    private final float mKeyboardGap;
    private float mLastX;
    private float mLastY;
    private float mMarkerSize;
    private float mMotionX;
    private float mMotionY;
    private City mMoveToCity = null;
    private float mMoveToDistance;
    private ValueAnimator mRotationAnimator = null;
    private SGVector3f mSecondTouchPoint;
    private final SgiPlayerListener mSgiPlayerListener;
    private float mSmallerDimension;
    private float mVRotation;
    private float mVRotationTransient;
    private SGView mView;
    private boolean mWaitForAnimationEnd = false;
    private float touchLatitudeCurr;
    private float touchLatitudeStart;
    private float touchLongitudeCurr;
    private float touchLongitudeStart;
    private float vRotationPrev_nonwrap;
    private float vRotationPrev_nonwrapstart;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.sgi.SGIPlayer$2 */
    class C08462 implements SGSurfaceSizeChangeListener {
        C08462() {
        }

        public void onResize(SGVector2f size) {
            if (SGIPlayer.this.mActualWidth != ((int) size.getX()) || SGIPlayer.this.mActualHeight != ((int) size.getY())) {
                SGIPlayer.this.mIsMobileKeyboardCover = StateUtils.isMobileKeyboard(SGIPlayer.this.mView.getContext());
                SGIPlayer.this.mActualWidth = (int) size.getX();
                SGIPlayer.this.mActualHeight = (int) size.getY();
                SGIPlayer.this.mSmallerDimension = SGIPlayer.this.mActualWidth < SGIPlayer.this.mActualHeight ? (float) SGIPlayer.this.mActualWidth : (float) SGIPlayer.this.mActualHeight;
                SGIPlayer.this.mView.getSurface().getDefaultCamera().setViewport(new SGVector2f(0.0f, 0.0f), new SGVector2f((float) SGIPlayer.this.mActualWidth, (float) SGIPlayer.this.mActualHeight));
                SGIPlayer.this.mConfig.setScreenSize(SGIPlayer.this.mActualWidth, SGIPlayer.this.mActualHeight);
                if (SGIPlayer.this.mDistance < 0.0f) {
                    SGIPlayer.this.mDistance = 3.6144578f;
                }
                SGIPlayer.this.reloadCitiesLayer(true);
                SGIPlayer.this.updateLayers();
                if (SGIPlayer.this.mMoveToCity != null) {
                    SGIPlayer.this.moveToCity(SGIPlayer.this.mMoveToCity, SGIPlayer.this.mMoveToDistance, false, null);
                    SGIPlayer.this.mMoveToCity = null;
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.sgi.SGIPlayer$4 */
    class C08504 implements AccessibilityStateChangeListener {
        C08504() {
        }

        public void onAccessibilityStateChanged(boolean enabled) {
            if (SGIPlayer.this.mView != null) {
                SGIPlayer.this.mView.setFocusable(enabled);
            }
        }
    }

    private static class SineIOTimeInterpolator implements TimeInterpolator {
        static final SGAnimationTimingFunction sineInOut70 = SGTimingFunctionFactory.createPredefinedTimingFunction(SGPredefinedTimingFunctionType.SINE_IO_70);

        private SineIOTimeInterpolator() {
        }

        public float getInterpolation(float input) {
            return sineInOut70.getInterpolationTime(input);
        }
    }

    public SGIPlayer(Context context, City startCity, ViewGroup parentView, int searchHeight, Bundle savedInstanceState, SgiPlayerListener sgiPlayerListener) {
        City[] cities;
        int count;
        int i;
        float latitude;
        float longitude;
        this.mContext = context;
        this.mSgiPlayerListener = sgiPlayerListener;
        this.mKeyboardGap = context.getResources().getDimension(C0836R.dimen.worldclock_keyboard_gap);
        this.mConfig = new SGILayerConfig(true, searchHeight / 2, context);
        this.mActualWidth = -1;
        this.mActualHeight = -1;
        boolean restored = false;
        if (savedInstanceState != null) {
            Float hRotationObject = (Float) savedInstanceState.getSerializable("HRotation");
            Float vRotationObject = (Float) savedInstanceState.getSerializable("VRotation");
            Float distanceObject = (Float) savedInstanceState.getSerializable("Distance");
            boolean isShowPopup = savedInstanceState.getBoolean("IsShowCityPopup", false);
            if (hRotationObject != null && vRotationObject != null && distanceObject != null) {
                this.mHRotation = hRotationObject.floatValue();
                this.mVRotation = vRotationObject.floatValue();
                this.mDistance = distanceObject.floatValue();
                restored = true;
            } else if (isShowPopup) {
                if (distanceObject != null) {
                    this.mDistance = distanceObject.floatValue();
                } else {
                    this.mDistance = -1.0f;
                }
                if (startCity == null) {
                    cities = CityManager.getCitiesByRawOffset();
                    count = cities == null ? 0 : cities.length;
                    for (i = 0; i < count; i++) {
                        if (cities[i].getDBSelected()) {
                            startCity = cities[i];
                            break;
                        }
                    }
                }
                latitude = 0.0f;
                longitude = 0.0f;
                if (startCity != null) {
                    latitude = (-startCity.mLatitudeBillboard) * 0.017453292f;
                    longitude = startCity.mLongitudeBillboard * 0.017453292f;
                }
                this.mHRotation = cutPeriod(latitude);
                this.mVRotation = cutPeriod(longitude);
                restored = true;
            }
        }
        if (!restored) {
            if (startCity == null) {
                cities = CityManager.getCitiesByRawOffset();
                count = cities == null ? 0 : cities.length;
                for (i = 0; i < count; i++) {
                    if (cities[i].getDBSelected()) {
                        startCity = cities[i];
                        break;
                    }
                }
            }
            latitude = 0.0f;
            longitude = 0.0f;
            if (startCity != null) {
                latitude = (-startCity.mLatitudeBillboard) * 0.017453292f;
                longitude = startCity.mLongitudeBillboard * 0.017453292f;
            }
            this.mHRotation = cutPeriod(latitude);
            this.mVRotation = cutPeriod(longitude);
            this.mDistance = -1.0f;
        }
        SGContextConfiguration config = new SGContextConfiguration();
        config.setRGB565();
        config.mDepthSize = 0;
        config.mStencilSize = 0;
        config.mSamples = 0;
        config.mSampleBuffers = 0;
        config.mBackgroundThreadCount = 0;
        config.mSeparateThreads = true;
        this.mView = new SGView(this.mContext, config) {
            City city;

            public boolean onTouchEvent(MotionEvent event) {
                if (!(event.getToolType(0) == 0 && event.getPointerCount() == 1)) {
                    SGIPlayer.this.stopRotationAnimation();
                    if (SGIPlayer.this.mSgiPlayerListener != null) {
                        SGIPlayer.this.mSgiPlayerListener.touch(this, event);
                    }
                    SGIPlayer.this.processTouch(event);
                }
                return true;
            }

            public boolean onGenericMotionEvent(MotionEvent event) {
                if (event.isFromSource(2)) {
                    switch (event.getAction()) {
                        case 8:
                            float stepValue;
                            if (event.getAxisValue(9) < 0.0f) {
                                stepValue = 0.24f;
                            } else {
                                stepValue = -0.24f;
                            }
                            SGIPlayer.this.zoomInOut(stepValue);
                            break;
                    }
                }
                return super.onGenericMotionEvent(event);
            }

            public boolean onHoverEvent(MotionEvent evt) {
                if (SGIPlayer.this.mAccessibilityManager != null && SGIPlayer.this.mAccessibilityManager.isTouchExplorationEnabled() && evt.getPointerCount() == 1) {
                    SGIPlayer.this.setMotionEventPosition(evt.getX(), evt.getY());
                } else {
                    SGIPlayer.this.resetMotionEventPosition();
                }
                SGVector3f touchPoint = SGIPlayer.this.findTouchPointAt(evt.getX(), evt.getY());
                if (touchPoint == null || SGIPlayer.this.mBillboard == null) {
                    this.city = null;
                } else {
                    this.city = SGIPlayer.this.mBillboard.getCityAt((-((float) Math.atan2((double) touchPoint.getZ(), (double) touchPoint.getX()))) * 57.295776f, ((float) Math.atan2((double) touchPoint.getY(), (double) ((float) Math.sqrt((double) ((touchPoint.getX() * touchPoint.getX()) + (touchPoint.getZ() * touchPoint.getZ())))))) * 57.295776f, SGIPlayer.this.mDistance, evt.getX(), evt.getY());
                }
                return super.onHoverEvent(evt);
            }

            public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(event);
                event.getText().add(this.city != null ? this.city.getName() : "");
                this.city = null;
            }
        };
        SGSurface surface = this.mView.getSurface();
        if (surface != null) {
            surface.setSizeChangeListener((SGSurfaceSizeChangeListener) new C08462());
            final boolean ifRestored = restored;
            final City city = startCity;
            surface.addChangesDrawnListener((SGSurfaceChangesDrawnListener) new SGSurfaceChangesDrawnListener() {

                /* renamed from: com.sec.android.app.clockpackage.worldclock.sgi.SGIPlayer$3$1 */
                class C08471 implements SGSurfaceChangesDrawnListener {
                    C08471() {
                    }

                    public void onChangesDrawn() {
                        if (SGIPlayer.this.mBillboard != null) {
                            SGIPlayer.this.mBillboard.runInitialAnimation();
                        } else {
                            System.out.println("mBillboard is null ; PLM - P150312-02565");
                        }
                    }
                }

                /* renamed from: com.sec.android.app.clockpackage.worldclock.sgi.SGIPlayer$3$2 */
                class C08482 implements Runnable {
                    C08482() {
                    }

                    public void run() {
                        SGIPlayer.this.moveToCity(city, 3.0f, false, new SineIOTimeInterpolator());
                    }
                }

                public void onChangesDrawn() {
                    SGSurface surface = SGIPlayer.this.mView.getSurface();
                    SGIPlayer.this.mBillboard = new SGICities(surface, SGIPlayer.this.mConfig);
                    SGIPlayer.this.mBillboard.runAnimation(0.0f, false);
                    SGIPlayer.this.mBillboard.runFadeOutAnimation(false);
                    SGIPlayer.this.mBillboard.setSize(SGIPlayer.this.mActualWidth, SGIPlayer.this.mActualHeight);
                    surface.addChangesDrawnListener(new C08471());
                    if (!ifRestored) {
                        new Handler().postDelayed(new C08482(), 200);
                    }
                }
            });
            SGBitmapTexture2DProperty mDayTexture = SGCompressedTextureFactory.createTexture(this.mContext.getResources().getAssets(), "worldmap.astc");
            System.out.println("WorldClock: ------------> ASTC loaded");
            this.mGlobeLayer = new SGIGlobeLayer(this.mConfig, surface.getView().getContext());
            this.mGlobeLayer.setTexture(mDayTexture);
            surface.addLayer(this.mGlobeLayer);
            surface.getDefaultCamera().setClearColor(Constants.BG_COLOR);
            resetMotionEventPosition();
            this.mAccessibilityManager = (AccessibilityManager) this.mView.getContext().getSystemService("accessibility");
            this.mAccessibilityStateChangeListener = new C08504();
            this.mAccessibilityManager.addAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
            this.mView.setFocusable(StateUtils.isTalkBackEnabled(this.mView.getContext()));
            parentView.addView(this.mView, 0);
        }
    }

    private void clampDistance() {
        this.mDistance = clampDistance(this.mDistance);
    }

    private float clampDistance(float distance) {
        if (distance > 10.5f) {
            return 10.5f;
        }
        if (distance < 1.37f) {
            return 1.37f;
        }
        return distance;
    }

    private float cutPeriod(float angleRad) {
        float resultRotation = angleRad % 6.2831855f;
        if (resultRotation > 3.1415927f) {
            return resultRotation - 6.2831855f;
        }
        if (resultRotation < -3.1415927f) {
            return resultRotation + 6.2831855f;
        }
        return resultRotation;
    }

    private float cutPeriod(float compareVal, float angleRad) {
        if (compareVal > 3.1415927f) {
            return angleRad - 6.2831855f;
        }
        if (compareVal < -3.1415927f) {
            return angleRad + 6.2831855f;
        }
        return angleRad;
    }

    private void updateLayers() {
        this.mConfig.updateCamera(this.mDistance, this.mHRotation, this.mVRotation);
        this.mGlobeLayer.setSize(this.mActualWidth, this.mActualHeight);
        if (this.mBillboard != null) {
            this.mBillboard.setSize(this.mActualWidth, this.mActualHeight);
        }
    }

    private boolean raySphereIntersect(float aScreenX, float aScreenY, SGVector3f aSphereCenter, float aSphereRadius, float[] aView, float[] aProj, SGVector2f aViewportSize, SGVector3f aRayOrig, SGVector3f aRayDir, float[] t) {
        SGVector3f rayOrigin = new SGVector3f();
        SGVector3f rayDir = new SGVector3f();
        getRayParams(aScreenX, aScreenY, aView, aProj, aViewportSize, rayOrigin, rayDir);
        SGVector3f diff = new SGVector3f(rayOrigin);
        diff.subtract(aSphereCenter);
        float b = rayDir.getDotProduct(diff);
        float k = (b * b) - (diff.getDotProduct(diff) - (aSphereRadius * aSphereRadius));
        if (k < 0.0f) {
            return false;
        }
        float t1 = (-b) + k;
        float t2 = (-b) - k;
        t[0] = t1;
        t[1] = t2;
        aRayOrig.setX(rayOrigin.getX());
        aRayOrig.setY(rayOrigin.getY());
        aRayOrig.setZ(rayOrigin.getZ());
        aRayDir.setX(rayDir.getX());
        aRayDir.setY(rayDir.getY());
        aRayDir.setZ(rayDir.getZ());
        return t1 >= 0.0f || t2 >= 0.0f;
    }

    private void getRayParams(float aScreenX, float aScreenY, float[] aView, float[] aProj, SGVector2f aViewportSize, SGVector3f aOrigin, SGVector3f aDir) {
        float[] v = new float[16];
        float[] p = new float[16];
        for (int i = 0; i < 16; i++) {
            v[i] = aView[i];
            p[i] = aProj[i];
        }
        SGMatrix4f view = new SGMatrix4f(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9], v[10], v[11], v[12], v[13], v[14], v[15]);
        view.transpose();
        SGMatrix4f proj = new SGMatrix4f(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9], p[10], p[11], p[12], p[13], p[14], p[15]);
        proj.transpose();
        SGMatrix4f sGMatrix4f = new SGMatrix4f(proj);
        sGMatrix4f.multiply(view);
        sGMatrix4f = new SGMatrix4f(sGMatrix4f);
        sGMatrix4f.inverse();
        float nx = (2.0f * (aScreenX / aViewportSize.getX())) - 1.0f;
        float ny = 1.0f - (2.0f * (aScreenY / aViewportSize.getY()));
        SGVector4f sGVector4f = new SGVector4f(nx, ny, -1.0f, 1.0f);
        sGVector4f = new SGVector4f(nx, ny, 1.0f, 1.0f);
        SGVector4f rayOrigin = sGMatrix4f.transformVector(sGVector4f);
        SGVector4f rayTarget = sGMatrix4f.transformVector(sGVector4f);
        float invRayOrigin = 1.0f / rayOrigin.getW();
        float invRayTarget = 1.0f / rayTarget.getW();
        SGVector3f sGVector3f = new SGVector3f(rayOrigin.getX() * invRayOrigin, rayOrigin.getY() * invRayOrigin, rayOrigin.getZ() * invRayOrigin);
        sGVector3f = new SGVector3f(new SGVector3f(rayTarget.getX() * invRayTarget, rayTarget.getY() * invRayTarget, rayTarget.getZ() * invRayTarget));
        sGVector3f.subtract(sGVector3f);
        sGVector3f.normalize();
        aOrigin.set(sGVector3f.getX(), sGVector3f.getY(), sGVector3f.getZ());
        aDir.set(sGVector3f.getX(), sGVector3f.getY(), sGVector3f.getZ());
    }

    private boolean setDragCoords(float eventX, float eventY, boolean isStart) {
        if (this.mBillboard == null) {
            return false;
        }
        SGVector3f sphereCenter = new SGVector3f(0.0f, 0.0f, 0.0f);
        SGVector2f viewportSize = new SGVector2f((float) this.mActualWidth, (float) this.mActualHeight);
        SGVector3f rayOrig = new SGVector3f();
        SGVector3f rayDir = new SGVector3f();
        float[] t = new float[2];
        if (raySphereIntersect(eventX, eventY, sphereCenter, 1.0f, this.mConfig.mViewMat, this.mConfig.mProjMat, viewportSize, rayOrig, rayDir, t)) {
            float x = rayOrig.getX() + (rayDir.getX() * t[1]);
            float z = rayOrig.getZ() + (rayDir.getZ() * t[1]);
            float touchLongitude = (-((float) Math.atan2((double) z, (double) x))) * 57.295776f;
            float touchLatitude = ((float) Math.atan2((double) (rayOrig.getY() + (rayDir.getY() * t[1])), (double) ((float) Math.sqrt((double) ((x * x) + (z * z)))))) * 57.295776f;
            this.isStartOnGlobe = true;
            if (isStart) {
                this.touchLongitudeStart = touchLongitude;
                this.touchLatitudeStart = touchLatitude;
            } else {
                this.isEndOnGlobe = true;
                this.touchLongitudeCurr = touchLongitude;
                this.touchLatitudeCurr = touchLatitude;
            }
            return true;
        }
        this.touchLongitudeCurr = 0.0f;
        this.touchLongitudeStart = 0.0f;
        this.touchLatitudeCurr = 0.0f;
        this.touchLatitudeStart = 0.0f;
        return false;
    }

    private float getLimitingAngle(float distance) {
        return 1.1f + (0.3f * (1.0f - ((distance - 1.37f) / 9.13f)));
    }

    private void processTouch(MotionEvent evt) {
        if (this.mAnimator == null || !this.mAnimator.isRunning()) {
            showCityUnderSelection(-1);
            int action = evt.getAction() & 255;
            if (action == 0 || action == 5) {
                this.mIsZoom = false;
                clampDistance();
            }
            if (evt.getPointerCount() > 1) {
                zoom(evt, action);
                clampDistance();
                this.mConfig.updateCamera(this.mDistance, this.mHRotation, this.mVRotation);
                if (this.mBillboard != null) {
                    this.mBillboard.runAnimation(0.0f, true);
                    this.mBillboard.runFadeOutAnimation(true);
                    return;
                }
                return;
            } else if (!this.mIsZoom) {
                drag(evt, action);
                return;
            } else if (action == 1 && evt.getPointerCount() == 1) {
                this.mIsZoom = false;
                clampDistance();
                updateRotation();
                return;
            } else {
                return;
            }
        }
        this.mLastX = evt.getX();
        this.mLastY = evt.getY();
        if (evt.getPointerCount() > 1) {
            this.mWaitForAnimationEnd = true;
        }
    }

    private void updateRotation() {
        this.mHRotation = (this.mHRotation + this.mHRotationTransient) % 6.2831855f;
        this.mHRotationTransient = 0.0f;
        this.mVRotation += this.mVRotationTransient;
        this.mVRotationTransient = 0.0f;
    }

    private void drag(MotionEvent evt, int action) {
        switch (action) {
            case 0:
                this.hRotationPrev_nonwrap = this.mHRotation;
                this.vRotationPrev_nonwrap = this.mVRotation;
                this.hRotationPrev_nonwrapstart = this.mHRotation;
                this.vRotationPrev_nonwrapstart = this.mVRotation;
                this.mLastX = evt.getX();
                this.mLastY = evt.getY();
                return;
            case 1:
                updateRotation();
                return;
            case 2:
                boolean isOutsideBefore = setDragCoords(this.mLastX, this.mLastY, true);
                this.mLastX = evt.getX();
                this.mLastY = evt.getY();
                boolean isOutsideNow = setDragCoords(this.mLastX, this.mLastY, false);
                if (isOutsideBefore || !isOutsideNow) {
                    this.touchLongitudeCurr = (this.touchLongitudeCurr + 360.0f) % 360.0f;
                    this.touchLongitudeStart = (this.touchLongitudeStart + 360.0f) % 360.0f;
                    this.mHRotationTransient = (this.touchLongitudeCurr - this.touchLongitudeStart) * 0.017453292f;
                    this.mVRotationTransient = (-(this.touchLatitudeCurr - this.touchLatitudeStart)) * 0.017453292f;
                    float hangle = cutPeriod(this.mHRotationTransient);
                    float trans_factor = this.mDistance / 1000.0f;
                    if (hangle > trans_factor || hangle < (-trans_factor)) {
                        this.hRotationPrev_nonwrapstart = this.hRotationPrev_nonwrap;
                    }
                    float temp = this.mVRotation + this.mVRotationTransient;
                    float limitingAngle = getLimitingAngle(this.mDistance);
                    if (temp > limitingAngle) {
                        temp = limitingAngle;
                        this.mVRotation = temp;
                        this.mVRotationTransient = 0.0f;
                        this.touchLatitudeStart = this.touchLatitudeCurr;
                    } else if (temp < (-limitingAngle)) {
                        temp = -limitingAngle;
                        this.mVRotation = temp;
                        this.mVRotationTransient = 0.0f;
                        this.touchLatitudeStart = this.touchLatitudeCurr;
                    }
                    this.mConfig.updateCamera(this.mDistance, this.mHRotation + this.mHRotationTransient, temp);
                    this.hRotationPrev_nonwrap += hangle;
                }
                updateRotation();
                return;
            default:
                return;
        }
    }

    private void zoom(MotionEvent evt, int action) {
        float firstTouchX = evt.getX(0);
        float firstTouchY = evt.getY(0);
        float secondTouchX = evt.getX(1);
        float secondTouchY = evt.getY(1);
        if (findTouchPointAt(evt.getX(0), evt.getY(0)) != null) {
            if (findTouchPointAt(evt.getX(1), evt.getY(1)) != null) {
                float distanceBtwTouchPoints = (float) Math.hypot((double) (firstTouchX - secondTouchX), (double) (firstTouchY - secondTouchY));
                if (distanceBtwTouchPoints < 2.0f) {
                    distanceBtwTouchPoints = 2.0f;
                }
                secondTouchX = ((float) this.mActualWidth) / 2.0f;
                firstTouchX = secondTouchX;
                firstTouchY = (((float) this.mActualHeight) / 2.0f) + (distanceBtwTouchPoints / 2.0f);
                secondTouchY = (((float) this.mActualHeight) / 2.0f) - (distanceBtwTouchPoints / 2.0f);
                if (action == 5 || this.mWaitForAnimationEnd) {
                    this.mWaitForAnimationEnd = false;
                    this.mFirstTouchPoint = findTouchPointAt(firstTouchX, firstTouchY);
                    this.mSecondTouchPoint = findTouchPointAt(secondTouchX, secondTouchY);
                    if (this.mFirstTouchPoint == null || this.mSecondTouchPoint == null) {
                        this.mFirstTouchPoint = new SGVector3f();
                        this.mSecondTouchPoint = new SGVector3f();
                    }
                    SGVector3f sGVector3f = new SGVector3f(this.mFirstTouchPoint);
                    sGVector3f.add(this.mSecondTouchPoint);
                    this.mGapFromCenter = sGVector3f.getLength() / 2.0f;
                    this.mHalfTouchGap = this.mFirstTouchPoint.getDistance(this.mSecondTouchPoint) / 2.0f;
                }
                if (action == 2) {
                    this.mHRotationTransient = 0.0f;
                    this.mVRotationTransient = 0.0f;
                    float temp = this.mVRotation;
                    float limitingAngle = getLimitingAngle(this.mDistance);
                    if (temp > limitingAngle) {
                        this.mVRotation = limitingAngle;
                    } else if (temp < (-limitingAngle)) {
                        this.mVRotation = -limitingAngle;
                    }
                    int[] viewData = new int[]{0, 0, this.mActualWidth, this.mActualHeight};
                    worldTouchPoint1 = new float[4];
                    GLU.gluUnProject(firstTouchX, ((float) this.mActualHeight) - firstTouchY, 1.0f, this.mConfig.mViewMat, 0, this.mConfig.mProjMat, 0, viewData, 0, worldTouchPoint1, 0);
                    worldTouchPoint1[0] = worldTouchPoint1[0] / worldTouchPoint1[3];
                    worldTouchPoint1[1] = worldTouchPoint1[1] / worldTouchPoint1[3];
                    worldTouchPoint1[2] = worldTouchPoint1[2] / worldTouchPoint1[3];
                    worldTouchPoint1[3] = 1.0f;
                    worldTouchPoint2 = new float[4];
                    GLU.gluUnProject(secondTouchX, ((float) this.mActualHeight) - secondTouchY, 1.0f, this.mConfig.mViewMat, 0, this.mConfig.mProjMat, 0, viewData, 0, worldTouchPoint2, 0);
                    worldTouchPoint2[0] = worldTouchPoint2[0] / worldTouchPoint2[3];
                    worldTouchPoint2[1] = worldTouchPoint2[1] / worldTouchPoint2[3];
                    worldTouchPoint2[2] = worldTouchPoint2[2] / worldTouchPoint2[3];
                    worldTouchPoint2[3] = 1.0f;
                    this.mHalfNearPlaneGap = new SGVector3f(worldTouchPoint1).getDistance(new SGVector3f(worldTouchPoint2));
                    this.mHalfNearPlaneGap /= 2.0f;
                    this.mDistance = this.mGapFromCenter + (this.mHalfTouchGap * (500.0f / this.mHalfNearPlaneGap));
                    this.mIsZoom = true;
                }
            }
        }
    }

    private SGView getSGView() {
        return this.mView;
    }

    public void processFlingEvent(float velocityX, float velocityY, float touchX, float touchY) {
        if (this.mIsZoom || !this.isStartOnGlobe || !this.isEndOnGlobe) {
            return;
        }
        if ((this.mAnimator == null || !this.mAnimator.isRunning()) && setDragCoords(touchX, touchY, true)) {
            velocityX = ((Math.abs(velocityX) * 1080.0f) / this.mSmallerDimension) * ((float) (this.hRotationPrev_nonwrap - this.hRotationPrev_nonwrapstart < 0.0f ? -1 : 1));
            velocityY = ((Math.abs(velocityY) * 1920.0f) / this.mSmallerDimension) * ((float) (this.vRotationPrev_nonwrap - this.vRotationPrev_nonwrapstart < 0.0f ? -1 : 1));
            updateRotation();
            float slowDownFactor = (float) ((findZoomLevel(this.mDistance) * 2500) + 10000);
            final float fromValueH = this.mHRotation;
            final float toValueH = fromValueH + ((velocityX / slowDownFactor) * this.mDistance);
            final float fromValueV = this.mVRotation;
            final float toValueV = fromValueV + ((velocityY / slowDownFactor) * this.mDistance);
            this.mRotationAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            this.mRotationAnimator.setDuration(750);
            this.mRotationAnimator.setInterpolator(new DecelerateInterpolator());
            this.mRotationAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    SGIPlayer.this.mHRotation = (fromValueH * value) + (toValueH * (1.0f - value));
                    SGIPlayer.this.mVRotation = (fromValueV * value) + (toValueV * (1.0f - value));
                    float limitingAngle = SGIPlayer.this.getLimitingAngle(SGIPlayer.this.mDistance);
                    if (SGIPlayer.this.mVRotation > limitingAngle) {
                        SGIPlayer.this.mVRotation = limitingAngle;
                    } else if (SGIPlayer.this.mVRotation < (-limitingAngle)) {
                        SGIPlayer.this.mVRotation = -limitingAngle;
                    }
                    if (SGIPlayer.this.mConfig != null) {
                        SGIPlayer.this.mConfig.updateCamera(SGIPlayer.this.mDistance, SGIPlayer.this.mHRotation, SGIPlayer.this.mVRotation);
                    }
                }
            });
            this.mDistanceEndVal = this.mDistance;
            this.mRotationAnimator.start();
        }
    }

    public void processDoubleTapEvent(MotionEvent evt) {
        if (this.mAnimator == null || !this.mAnimator.isRunning()) {
            checkVoiceAssistState();
            SGVector3f touchPoint = findTouchPointAt(this.mMotionX < 0.0f ? evt.getX() : this.mMotionX, this.mMotionY < 0.0f ? evt.getY() : this.mMotionY);
            SGVector2f touch = null;
            if (touchPoint != null) {
                touch = new SGVector2f(((float) Math.atan2((double) touchPoint.getY(), (double) ((float) Math.sqrt((double) ((touchPoint.getX() * touchPoint.getX()) + (touchPoint.getZ() * touchPoint.getZ())))))) * 57.295776f, (-((float) Math.atan2((double) touchPoint.getZ(), (double) touchPoint.getX()))) * 57.295776f);
            }
            int currentZoomLevel = getCurrentZoomLevel();
            if (currentZoomLevel < 4) {
                moveToPoint(touch, 1.37f);
            } else if (currentZoomLevel == 4) {
                moveToPoint(touch, 3.0f);
            }
        }
    }

    private SGVector3f findTouchPointAt(float eventX, float eventY) {
        worldTouchPoint = new float[4];
        int[] viewData = new int[]{0, 0, this.mActualWidth, this.mActualHeight};
        GLU.gluUnProject(eventX, ((float) this.mActualHeight) - eventY, 1.0f, this.mConfig.mViewMat, 0, this.mConfig.mProjMat, 0, viewData, 0, worldTouchPoint, 0);
        worldTouchPoint[0] = worldTouchPoint[0] / worldTouchPoint[3];
        worldTouchPoint[1] = worldTouchPoint[1] / worldTouchPoint[3];
        worldTouchPoint[2] = worldTouchPoint[2] / worldTouchPoint[3];
        worldTouchPoint[3] = 1.0f;
        float[] directionVector = new float[]{worldTouchPoint[0] - this.mConfig.mEye[0], worldTouchPoint[1] - this.mConfig.mEye[1], worldTouchPoint[2] - this.mConfig.mEye[2]};
        SGILayerConfig.normalize(directionVector);
        float a = dot(directionVector, directionVector);
        float b = 2.0f * dot(directionVector, this.mConfig.mEye);
        float disc = (b * b) - ((4.0f * a) * (dot(this.mConfig.mEye, this.mConfig.mEye) - 1.0f));
        if (disc <= 0.0f) {
            return null;
        }
        float nearDist = ((-b) - ((float) Math.sqrt((double) disc))) / (2.0f * a);
        directionVector[0] = directionVector[0] * nearDist;
        directionVector[1] = directionVector[1] * nearDist;
        directionVector[2] = directionVector[2] * nearDist;
        return new SGVector3f(this.mConfig.mEye[0] + directionVector[0], this.mConfig.mEye[1] + directionVector[1], this.mConfig.mEye[2] + directionVector[2]);
    }

    public void processClickEvent(MotionEvent evt) {
        checkVoiceAssistState();
        float x = this.mMotionX < 0.0f ? evt.getX() : this.mMotionX;
        float y = this.mMotionY < 0.0f ? evt.getY() : this.mMotionY;
        if (this.mAnimator == null || !this.mAnimator.isRunning()) {
            SGVector3f touchPoint = findTouchPointAt(x, y);
            if (touchPoint != null && this.mBillboard != null) {
                City city = this.mBillboard.getCityAt((-((float) Math.atan2((double) touchPoint.getZ(), (double) touchPoint.getX()))) * 57.295776f, ((float) Math.atan2((double) touchPoint.getY(), (double) ((float) Math.sqrt((double) ((touchPoint.getX() * touchPoint.getX()) + (touchPoint.getZ() * touchPoint.getZ())))))) * 57.295776f, this.mDistance, x, y);
                if (city != null) {
                    moveToCity(city);
                } else {
                    showCityUnderSelection(-1);
                }
            }
        }
    }

    private static float dot(float[] v1, float[] v2) {
        return ((v1[0] * v2[0]) + (v1[1] * v2[1])) + (v1[2] * v2[2]);
    }

    private void stopRotationAnimation() {
        if (this.mRotationAnimator != null && this.mRotationAnimator.isRunning()) {
            this.mRotationAnimator.cancel();
        }
    }

    private int getCurrentZoomLevel() {
        return findZoomLevel(this.mConfig.mDistance);
    }

    private static int findZoomLevel(float distance) {
        for (int i = 1; i < SGILayerConfig.REVEAL_POINTS.length; i++) {
            if (distance >= SGILayerConfig.REVEAL_POINTS[i]) {
                return i - 1;
            }
        }
        return SGILayerConfig.REVEAL_POINTS.length - 1;
    }

    public void showCityUnderSelection(int cityUnderSelID) {
        if (cityUnderSelID < 0) {
            CityManager.cleanDBCurrentLocation();
        }
        if (this.mBillboard != null) {
            this.mBillboard.setCityUnderSelID(cityUnderSelID);
        }
    }

    public void showCityUnderSelection() {
        if (this.mBillboard != null) {
            this.mBillboard.setCityUnderSelID();
        }
    }

    private float calculateMarkerSize(float distance) {
        float OldMin;
        float OldMax;
        float NewMin = Constants.MAXIMUM_MARKER_SCALE;
        float OldValue = distance;
        if (distance > 3.1f) {
            OldMin = 3.1f;
            OldMax = Constants.REVEAL_POINT_1;
        } else if (distance > 2.5f) {
            OldMin = 2.5f;
            OldMax = 3.1f;
        } else if (distance > 1.75f) {
            OldMin = 1.75f;
            OldMax = 2.5f;
        } else {
            OldMin = 1.38f;
            OldMax = 1.75f;
        }
        float val = (((OldValue - OldMin) * (1.0f - NewMin)) / (OldMax - OldMin)) + NewMin;
        if (distance >= Constants.REVEAL_POINT_1) {
            return 1.0f;
        }
        return val;
    }

    public void moveToCity(City city, float targetDistance, boolean bounceEffect, TimeInterpolator timeInterpolator) {
        if (this.mDistance < 0.0f) {
            this.mMoveToCity = city;
            this.mMoveToDistance = targetDistance;
            return;
        }
        float vRotationEndVal;
        final float hRotationStartVal = cutPeriod(this.mHRotation);
        final float vRotationStartVal = cutPeriod(this.mVRotation);
        float latitude = (-city.mLatitudeBillboard) * 0.017453292f;
        float longitude = city.mLongitudeBillboard * 0.017453292f;
        SGVector2f deltaLatLong = getDeltaRotation(targetDistance);
        longitude -= deltaLatLong.getX() - this.mVRotation;
        latitude = cutPeriod(latitude - (this.mHRotation + deltaLatLong.getY()));
        longitude = cutPeriod(longitude);
        final float hRotationEndVal = cutPeriod(latitude - hRotationStartVal, latitude);
        float limitingAngle = getLimitingAngle(targetDistance);
        if (longitude > limitingAngle) {
            vRotationEndVal = limitingAngle;
        } else if (longitude < (-limitingAngle)) {
            vRotationEndVal = -limitingAngle;
        } else {
            vRotationEndVal = longitude;
        }
        final float startMarkerSize = calculateMarkerSize(this.mDistance);
        final float finalMarkerSize = calculateMarkerSize(targetDistance);
        this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimator.setDuration(1000);
        if (timeInterpolator != null) {
            this.mAnimator.setInterpolator(timeInterpolator);
        }
        final boolean z = bounceEffect;
        this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            final float MIDWAY;
            final float distanceStartVal = SGIPlayer.this.mDistance;

            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                SGIPlayer.this.mHRotation = (hRotationStartVal * (1.0f - value)) + (hRotationEndVal * value);
                SGIPlayer.this.mVRotation = (vRotationStartVal * (1.0f - value)) + (vRotationEndVal * value);
                if (this.MIDWAY == 0.0f) {
                    SGIPlayer.this.mDistance = (this.distanceStartVal * (1.0f - value)) + (SGIPlayer.this.mDistanceEndVal * value);
                } else {
                    SGIPlayer.this.mDistance = ((1.0f - value) * ((this.distanceStartVal * (1.0f - value)) + (this.MIDWAY * value))) + (((this.MIDWAY * (1.0f - value)) + (SGIPlayer.this.mDistanceEndVal * value)) * value);
                }
                SGIPlayer.this.mMarkerSize = (startMarkerSize * (1.0f - value)) + (finalMarkerSize * value);
                if (SGIPlayer.this.mConfig != null) {
                    SGIPlayer.this.mConfig.updateCamera(SGIPlayer.this.mDistance, SGIPlayer.this.mHRotation, SGIPlayer.this.mVRotation);
                }
                if (SGIPlayer.this.mBillboard != null) {
                    SGIPlayer.this.mBillboard.runAnimation(SGIPlayer.this.mMarkerSize, true);
                    SGIPlayer.this.mBillboard.runFadeOutAnimation(false);
                }
            }
        });
        this.mDistanceEndVal = targetDistance;
        this.mAnimator.start();
    }

    private void moveToPoint(SGVector2f targetPoint, float targetDistance) {
        float latitude;
        float longitude;
        float vRotationEndVal;
        final float hRotationStartVal = this.mHRotation % 6.2831855f;
        final float vRotationStartVal = this.mVRotation % 6.2831855f;
        if (targetPoint != null) {
            latitude = (-targetPoint.getY()) * 0.017453292f;
            longitude = targetPoint.getX() * 0.017453292f;
        } else {
            latitude = hRotationStartVal;
            longitude = vRotationStartVal;
        }
        final float hRotationEndVal = cutPeriod(latitude - hRotationStartVal, latitude);
        float limitingAngle = getLimitingAngle(targetDistance);
        if (longitude > limitingAngle) {
            vRotationEndVal = limitingAngle;
        } else if (longitude < (-limitingAngle)) {
            vRotationEndVal = -limitingAngle;
        } else {
            vRotationEndVal = longitude;
        }
        final float startMarkerSize = calculateMarkerSize(this.mDistance);
        final float finalMarkerSize = calculateMarkerSize(targetDistance);
        this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimator.setDuration(1000);
        this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            final float distanceStartVal = SGIPlayer.this.mDistance;

            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                SGIPlayer.this.mHRotation = (hRotationStartVal * (1.0f - value)) + (hRotationEndVal * value);
                SGIPlayer.this.mVRotation = (vRotationStartVal * (1.0f - value)) + (vRotationEndVal * value);
                SGIPlayer.this.mDistance = (this.distanceStartVal * (1.0f - value)) + (SGIPlayer.this.mDistanceEndVal * value);
                SGIPlayer.this.mMarkerSize = (startMarkerSize * (1.0f - value)) + (finalMarkerSize * value);
                if (SGIPlayer.this.mConfig != null) {
                    SGIPlayer.this.mConfig.updateCamera(SGIPlayer.this.mDistance, SGIPlayer.this.mHRotation, SGIPlayer.this.mVRotation);
                }
                if (SGIPlayer.this.mBillboard != null) {
                    SGIPlayer.this.mBillboard.runAnimation(SGIPlayer.this.mMarkerSize, true);
                    SGIPlayer.this.mBillboard.runFadeOutAnimation(false);
                }
            }
        });
        this.mDistanceEndVal = targetDistance;
        this.mAnimator.start();
    }

    private SGVector2f getDeltaRotation(float newDistance) {
        SGVector3f sphereCenter = new SGVector3f(0.0f, 0.0f, 0.0f);
        SGVector2f viewportSize = new SGVector2f((float) this.mActualWidth, (float) this.mActualHeight);
        SGVector3f rayOrig = new SGVector3f();
        SGVector3f rayDir = new SGVector3f();
        float[] t = new float[2];
        boolean isNewEndDistance = ((double) Math.abs(newDistance - this.mDistance)) > 1.0E-4d;
        if (isNewEndDistance) {
            this.mConfig.updateCamera(newDistance, this.mHRotation, this.mVRotation);
        }
        boolean intersect = raySphereIntersect(((float) this.mActualWidth) / 2.0f, (this.mConfig.getYLayerOffset() + (((float) this.mActualHeight) / 2.0f)) + (this.mIsMobileKeyboardCover ? this.mKeyboardGap : 0.0f), sphereCenter, 1.0f, this.mConfig.mViewMat, this.mConfig.mProjMat, viewportSize, rayOrig, rayDir, t);
        if (isNewEndDistance) {
            this.mConfig.updateCamera(this.mDistance, this.mHRotation, this.mVRotation);
        }
        float touchLongitude = 0.0f;
        float touchLatitude = 0.0f;
        if (intersect) {
            float x = rayOrig.getX() + (rayDir.getX() * t[1]);
            float z = rayOrig.getZ() + (rayDir.getZ() * t[1]);
            touchLongitude = -((float) Math.atan2((double) z, (double) x));
            touchLatitude = (float) Math.atan2((double) (rayOrig.getY() + (rayDir.getY() * t[1])), (double) ((float) Math.sqrt((double) ((x * x) + (z * z)))));
        }
        return new SGVector2f(touchLatitude, touchLongitude);
    }

    private void moveToCity(final City city) {
        float vRotationEndVal;
        final float hRotationStartVal = cutPeriod(this.mHRotation);
        final float vRotationStartVal = cutPeriod(this.mVRotation);
        float latitude = (-city.mLatitudeBillboard) * 0.017453292f;
        float longitude = city.mLongitudeBillboard * 0.017453292f;
        SGVector2f deltaLatLong = getDeltaRotation(this.mDistance);
        longitude -= deltaLatLong.getX() - this.mVRotation;
        latitude = cutPeriod(latitude - (this.mHRotation + deltaLatLong.getY()));
        longitude = cutPeriod(longitude);
        final float hRotationEndVal = cutPeriod(latitude - hRotationStartVal, latitude);
        float limitingAngle = getLimitingAngle(this.mDistance);
        if (longitude > limitingAngle) {
            vRotationEndVal = limitingAngle;
        } else if (longitude < (-limitingAngle)) {
            vRotationEndVal = -limitingAngle;
        } else {
            vRotationEndVal = longitude;
        }
        this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimator.setDuration(500);
        this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                SGIPlayer.this.mHRotation = (hRotationStartVal * (1.0f - value)) + (hRotationEndVal * value);
                SGIPlayer.this.mVRotation = (vRotationStartVal * (1.0f - value)) + (vRotationEndVal * value);
                if (SGIPlayer.this.mConfig != null) {
                    SGIPlayer.this.mConfig.updateCamera(SGIPlayer.this.mDistance, SGIPlayer.this.mHRotation, SGIPlayer.this.mVRotation);
                }
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                if (SGIPlayer.this.mSgiPlayerListener != null) {
                    SGIPlayer.this.mSgiPlayerListener.cityTouchedInGlobe(city.getUniqueId());
                    SGIPlayer.this.mView.playSoundEffect(0);
                }
            }
        });
        this.mDistanceEndVal = this.mDistance;
        this.mAnimator.start();
    }

    public void updateNightImage() {
        if (this.mView != null) {
            this.mGlobeLayer.updateNightImage();
        }
    }

    public void updateCityTime() {
        if (this.mBillboard != null) {
            this.mBillboard.updateCityTime();
        }
    }

    public void reloadCitiesLayer(boolean fullUpdate) {
        if (this.mBillboard == null) {
            return;
        }
        if (fullUpdate) {
            this.mBillboard.reload();
        } else {
            this.mBillboard.updateCityMarkers();
        }
    }

    public void destroy() {
        if (!(this.mAccessibilityManager == null || this.mAccessibilityStateChangeListener == null)) {
            this.mAccessibilityManager.removeAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
            this.mAccessibilityManager = null;
        }
        ((ViewGroup) this.mView.getParent()).removeView(this.mView);
        this.mView = null;
        this.mContext = null;
    }

    public void setSgiVisibility(int visibility) {
        if (this.mView != null) {
            this.mView.setVisibility(visibility);
        }
    }

    public float cityCardOffset(City targetCity) {
        if (targetCity == null || this.mBillboard == null) {
            return 0.0f;
        }
        float normalPositionOffset;
        float limitingAngle;
        float distance;
        if (this.mAnimator == null || !this.mAnimator.isRunning()) {
            normalPositionOffset = this.mBillboard.cityCardOffset(targetCity);
            limitingAngle = getLimitingAngle(this.mDistance);
            distance = this.mDistance - 1.0f;
        } else {
            normalPositionOffset = this.mBillboard.cityCardOffset(targetCity, this.mDistanceEndVal);
            limitingAngle = getLimitingAngle(this.mDistanceEndVal);
            distance = this.mDistanceEndVal - 1.0f;
        }
        float deltaAngle = 0.0f;
        float targetLatitude = (-targetCity.mLongitudeBillboard) * 0.017453292f;
        if (targetLatitude > limitingAngle) {
            deltaAngle = targetLatitude - limitingAngle;
        } else if (targetLatitude < (-limitingAngle)) {
            deltaAngle = targetLatitude + limitingAngle;
        }
        return normalPositionOffset + ((((-((float) Math.sin((double) deltaAngle))) * Constants.GLOBE_SIZE_LEVEL_0) * 10.5f) / (2.0f * (distance - ((float) Math.cos((double) deltaAngle)))));
    }

    public void viewSuspend() {
        getSGView().suspend();
    }

    public void viewResume() {
        getSGView().resume();
        this.mGlobeLayer.onResume();
        if (this.mBillboard != null) {
            this.mBillboard.onResume();
        }
    }

    public boolean getSGViewVisibility() {
        return getSGView().getVisibility() == 0;
    }

    public void zoomInOut(float deltaDistance) {
        Float finalZoomLevel = Float.valueOf(clampDistance(Float.valueOf(this.mDistanceEndVal + deltaDistance).floatValue()));
        final float startMarkerSize = calculateMarkerSize(this.mDistance);
        final float finalMarkerSize = calculateMarkerSize(finalZoomLevel.floatValue());
        this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimator.setDuration(150);
        this.mAnimator.setInterpolator(new DecelerateInterpolator());
        this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            final float distanceStartVal = SGIPlayer.this.mDistance;

            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();
                SGIPlayer.this.mDistance = (this.distanceStartVal * (1.0f - value)) + (SGIPlayer.this.mDistanceEndVal * value);
                SGIPlayer.this.mMarkerSize = (startMarkerSize * (1.0f - value)) + (finalMarkerSize * value);
                if (SGIPlayer.this.mConfig != null) {
                    SGIPlayer.this.mConfig.updateCamera(SGIPlayer.this.mDistance, SGIPlayer.this.mHRotation, SGIPlayer.this.mVRotation);
                }
                if (SGIPlayer.this.mBillboard != null) {
                    SGIPlayer.this.mBillboard.runAnimation(SGIPlayer.this.mMarkerSize, true);
                    SGIPlayer.this.mBillboard.runFadeOutAnimation(false);
                }
            }
        });
        this.mDistanceEndVal = finalZoomLevel.floatValue();
        this.mAnimator.start();
    }

    private void setMotionEventPosition(float x, float y) {
        this.mMotionX = x;
        this.mMotionY = y;
    }

    private void resetMotionEventPosition() {
        setMotionEventPosition(-1.0f, -1.0f);
    }

    private void checkVoiceAssistState() {
        if (this.mAccessibilityManager == null || !this.mAccessibilityManager.isEnabled()) {
            resetMotionEventPosition();
        }
    }

    public float getHRotation() {
        return this.mHRotation;
    }

    public float getVRotation() {
        return this.mVRotation;
    }

    public float getDistance() {
        return this.mDistance;
    }
}
