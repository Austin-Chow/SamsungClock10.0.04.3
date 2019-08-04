package com.sec.android.app.clockpackage.worldclock.sgi;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.opengl.GLU;
import android.support.v4.view.ViewCompat;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGBitmapTexture2DProperty;
import com.samsung.android.sdk.sgi.render.SGBoolProperty;
import com.samsung.android.sdk.sgi.render.SGBuffer;
import com.samsung.android.sdk.sgi.render.SGBufferDataType;
import com.samsung.android.sdk.sgi.render.SGBufferUsageType;
import com.samsung.android.sdk.sgi.render.SGFloatArrayProperty;
import com.samsung.android.sdk.sgi.render.SGFloatProperty;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGIndexBuffer;
import com.samsung.android.sdk.sgi.render.SGPrimitiveType;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGShaderProgramProperty;
import com.samsung.android.sdk.sgi.render.SGShaderType;
import com.samsung.android.sdk.sgi.render.SGStringShaderProperty;
import com.samsung.android.sdk.sgi.render.SGTextureFilterType;
import com.samsung.android.sdk.sgi.render.SGTextureWrapType;
import com.samsung.android.sdk.sgi.render.SGVectorfProperty;
import com.samsung.android.sdk.sgi.render.SGVertexBuffer;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.samsung.android.sdk.sgi.vi.SGGeometryGenerator;
import com.samsung.android.sdk.sgi.vi.SGGeometryGeneratorFactory;
import com.samsung.android.sdk.sgi.vi.SGLayerImage;
import com.samsung.android.sdk.sgi.vi.SGSurface;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.TimeZone;

public final class SGICities {
    protected static int US_TEXTURE_WIDTH;
    private int MARKER_ATLAS_HEIGHT;
    private int MARKER_ATLAS_WIDTH;
    protected int TEXTURE_ATLAS_HEIGHT;
    protected int TEXTURE_ATLAS_WIDTH = 2048;
    private final SGFloatArrayProperty mAlpha = new SGFloatArrayProperty(6);
    private final float[] mAlphas = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private ValueAnimator mAnimator;
    private ValueAnimator mAnimatorInitial;
    protected BillboardData[] mBillboardData;
    private SGLayerImage mCitiesDummyLayer;
    protected final SGFloatProperty mCitiesFlag = new SGFloatProperty(0.0f);
    private SGGeometryGenerator mCitiesGeometryGenerator;
    private SGLayerImage mCitiesLayer;
    protected SGShaderProgramProperty mCitiesTextProgram;
    private final SGICitiesTime mCitiesTime;
    private float[] mCitiesType;
    private SGFloatArrayProperty mCitiesTypeProperty;
    private final SGVectorfProperty mCityNameColorProperty = new SGVectorfProperty(3);
    private final SGICityUnderSelection mCityUS;
    private int mCityUnderSelectionID = -1;
    private final SGILayerConfig mConfig;
    private final Context mContext;
    private float mDeltaTextBalloon;
    protected float mDeltaTextMarker;
    protected float mDeltaTextTime;
    private float mDistance;
    private float mDummyDistance;
    protected final SGFloatProperty mDummyFlag = new SGFloatProperty(1.0f);
    private final SGFloatArrayProperty mFadeOutAlpha = new SGFloatArrayProperty(6);
    private final float[] mFadeOutAlphas = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private final ValueAnimator[] mFadeOutAnimators = new ValueAnimator[5];
    protected final SGFloatArrayProperty mGeometryOffset = new SGFloatArrayProperty(64);
    protected final SGFloatProperty mGlobalScale = new SGFloatProperty();
    protected final SGVectorfProperty mIconColorProperty = new SGVectorfProperty(3);
    protected final SGFloatProperty mInterpolationFactor = new SGFloatProperty();
    protected final SGBoolProperty mIsCustomTheme = new SGBoolProperty(false);
    public boolean mLowDensity;
    protected int mMarkerNormalHeight;
    protected final SGFloatProperty mMarkerSize = new SGFloatProperty();
    protected SGBitmapTexture2DProperty mMarkerTexture;
    private int mMarkerUnderSelectionHeight;
    private SGLayerImage mMarkersDummyLayer;
    private SGGeometryGenerator mMarkersGeometryGenerator;
    private SGLayerImage mMarkersLayer;
    private int mNormalTop;
    private Rect[] mTextCoordinatesArray;
    private int mTextHeightInAtlas;
    private int[] mTextWidthArray;
    protected SGBitmapTexture2DProperty mTexture;
    protected final SGFloatArrayProperty mTextureOffset = new SGFloatArrayProperty(64);
    protected int mTimeHeightInAtlas;
    protected final SGFloatProperty mUnderSelectionID = new SGFloatProperty(-1.0f);

    /* renamed from: com.sec.android.app.clockpackage.worldclock.sgi.SGICities$1 */
    class C08411 implements AnimatorUpdateListener {
        C08411() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            if (SGICities.this.mGlobalScale != null) {
                SGICities.this.mGlobalScale.set(value);
            }
        }
    }

    protected static class BillboardData {
        protected RectF combinedRect;
        protected RectF markerRect;
        protected RectF textRect;
        protected final TimeZone timeZone;
        /* renamed from: x */
        protected final float f27x;
        /* renamed from: y */
        protected final float f28y;
        /* renamed from: z */
        protected final float f29z;
        protected final float zoom;

        private BillboardData(float[] xyzw, TimeZone tz) {
            this.f27x = xyzw[0];
            this.f28y = xyzw[1];
            this.f29z = xyzw[2];
            this.zoom = xyzw[3];
            this.timeZone = tz;
        }
    }

    protected static class SymbolData {
        protected Rect contentRect = null;
        protected int drawOffsetX;
        protected int width;

        protected SymbolData() {
        }
    }

    protected SGICities(SGSurface surface, SGILayerConfig config) {
        this.mConfig = config;
        this.mContext = surface.getView().getContext();
        initCommonProperties();
        this.mCitiesTime = new SGICitiesTime(surface, config, this);
        this.mCityUS = new SGICityUnderSelection(surface, config, this);
        createBillboardGeometries();
        initCitiesLayers();
        initMarkersLayers();
        updateAlphas();
        this.mCitiesTime.addLayers(surface);
        surface.addLayer(this.mCitiesDummyLayer);
        surface.addLayer(this.mCitiesLayer);
        surface.addLayer(this.mMarkersDummyLayer);
        surface.addLayer(this.mMarkersLayer);
        this.mCityUS.addLayers(surface);
    }

    private void initCommonProperties() {
        boolean isCustomTheme;
        int cityNameColor;
        int iconColor = ViewCompat.MEASURED_STATE_MASK;
        if (StateUtils.isCustomTheme(this.mContext)) {
            isCustomTheme = true;
            iconColor = this.mContext.getColor(C0836R.color.worldclock_icons_color);
        } else {
            isCustomTheme = false;
        }
        this.mIconColorProperty.set(new SGVector3f(((float) Color.red(iconColor)) / 255.0f, ((float) Color.green(iconColor)) / 255.0f, ((float) Color.blue(iconColor)) / 255.0f));
        this.mIsCustomTheme.set(isCustomTheme);
        this.mTexture = new SGBitmapTexture2DProperty(SGTextureWrapType.CLAMP_TO_EDGE, SGTextureWrapType.CLAMP_TO_EDGE);
        if (StateUtils.isContextInDexMode(this.mContext)) {
            this.mTexture.setMinFilter(SGTextureFilterType.NEAREST);
            this.mTexture.setMagFilter(SGTextureFilterType.NEAREST);
        }
        this.mMarkerTexture = new SGBitmapTexture2DProperty(SGTextureWrapType.CLAMP_TO_EDGE, SGTextureWrapType.CLAMP_TO_EDGE);
        this.mInterpolationFactor.set(1.0f);
        this.mMarkerSize.set(findInterpolation(this.mConfig.mDistance));
        if (StateUtils.isContextInDexMode(this.mContext)) {
            cityNameColor = this.mContext.getColor(C0836R.color.worldclock_dex_city_name_textcolor);
        } else {
            cityNameColor = this.mContext.getColor(C0836R.color.worldclock_city_name_textcolor);
        }
        this.mCityNameColorProperty.set(new SGVector3f(((float) Color.red(cityNameColor)) / 255.0f, ((float) Color.green(cityNameColor)) / 255.0f, ((float) Color.blue(cityNameColor)) / 255.0f));
        this.mCitiesTextProgram = new SGShaderProgramProperty(new SGStringShaderProperty(SGShaderType.VERTEX, "attribute float cityId;attribute vec2 SGPositions;attribute vec2 SGTextureCoords;attribute vec4 location;uniform mat4 ModelViewProjection;uniform vec3 camVector;uniform float alphaByLevels[6];uniform float pxInScreenX;uniform float pxInScreenY;uniform float interpolationFactor;uniform float dummyFlag;uniform float underSelectionID;varying vec2 textureCoords;varying float alpha;void main(){   float isUnderSelection = step(0.5, abs(underSelectionID - cityId));   alpha = (1.0 - smoothstep(0.2, 0.7, 1.0 - dot(camVector, location.xyz))) * isUnderSelection;   float disAlpha = alpha*interpolationFactor;   alpha = ((1.0 - dummyFlag)*alpha + dummyFlag)*alphaByLevels[int(location.w)];   if(alpha < 0.001){       gl_Position = vec4(100.0);       textureCoords = vec2(0.0);   }else{       textureCoords = SGTextureCoords;       vec4 mesh = ModelViewProjection*vec4(location.xyz, 1.0);       mesh.z = 0.0;       mesh /= mesh.w;       mesh.x += SGPositions.x*disAlpha*pxInScreenX;       mesh.y += SGPositions.y*disAlpha*pxInScreenY;       gl_Position = mesh;   }}"), new SGStringShaderProperty(SGShaderType.FRAGMENT, "precision highp float;uniform sampler2D SGTexture;uniform float globalScale;uniform vec3 cityTextColor;varying vec2 textureCoords;varying float alpha;void main(){    vec4 sceneColor = vec4(cityTextColor.rgb, texture2D(SGTexture, textureCoords).r);    gl_FragColor = sceneColor;    gl_FragColor.a *= alpha * globalScale;}"));
    }

    private void initCitiesLayers() {
        this.mCitiesLayer = new SGLayerImage();
        this.mCitiesDummyLayer = new SGLayerImage();
        this.mCitiesLayer.setGeometryGenerator(this.mCitiesGeometryGenerator);
        this.mCitiesDummyLayer.setGeometryGenerator(this.mCitiesGeometryGenerator);
        this.mCitiesLayer.setProperty(SGProperty.TEXTURE, this.mTexture);
        this.mCitiesDummyLayer.setProperty(SGProperty.TEXTURE, this.mTexture);
        this.mGlobalScale.set(0.0f);
        this.mCitiesLayer.setProperty("globalScale", this.mGlobalScale);
        this.mCitiesDummyLayer.setProperty("globalScale", this.mGlobalScale);
        this.mCitiesLayer.setProperty("interpolationFactor", this.mInterpolationFactor);
        this.mCitiesDummyLayer.setProperty("interpolationFactor", this.mMarkerSize);
        this.mConfig.setupLayer(this.mCitiesLayer, SGKeyCode.CODE_NUMPAD_SUBTRACT, null, null);
        this.mConfig.setupLayer(this.mCitiesDummyLayer, SGKeyCode.CODE_NUMPAD_SUBTRACT, null, null);
        this.mCitiesLayer.setProgramProperty(this.mCitiesTextProgram);
        this.mCitiesDummyLayer.setProgramProperty(this.mCitiesTextProgram);
        this.mCitiesLayer.setProperty("dummyFlag", this.mCitiesFlag);
        this.mCitiesDummyLayer.setProperty("dummyFlag", this.mDummyFlag);
        this.mCitiesLayer.setProperty("cityTextColor", this.mCityNameColorProperty);
        this.mCitiesDummyLayer.setProperty("cityTextColor", this.mCityNameColorProperty);
        this.mCitiesLayer.setProperty("underSelectionID", this.mUnderSelectionID);
        this.mCitiesDummyLayer.setProperty("underSelectionID", this.mUnderSelectionID);
    }

    private void initMarkersLayers() {
        this.mMarkersLayer = new SGLayerImage();
        this.mMarkersDummyLayer = new SGLayerImage();
        this.mMarkersLayer.setGeometryGenerator(this.mMarkersGeometryGenerator);
        this.mMarkersDummyLayer.setGeometryGenerator(this.mMarkersGeometryGenerator);
        this.mMarkersLayer.setProperty("interpolationFactor", this.mInterpolationFactor);
        this.mMarkersDummyLayer.setProperty("interpolationFactor", this.mMarkerSize);
        setDummyVisibility(false);
        this.mMarkersLayer.setProperty(SGProperty.TEXTURE, this.mMarkerTexture);
        this.mMarkersDummyLayer.setProperty(SGProperty.TEXTURE, this.mMarkerTexture);
        this.mMarkersLayer.setProperty("citiesType", this.mCitiesTypeProperty);
        this.mMarkersDummyLayer.setProperty("citiesType", this.mCitiesTypeProperty);
        this.mMarkersLayer.setProperty("textureOffsets", this.mTextureOffset);
        this.mMarkersDummyLayer.setProperty("textureOffsets", this.mTextureOffset);
        this.mMarkersLayer.setProperty("geometryOffsets", this.mGeometryOffset);
        this.mMarkersDummyLayer.setProperty("geometryOffsets", this.mGeometryOffset);
        this.mMarkersLayer.setProperty("underSelectionID", this.mUnderSelectionID);
        this.mMarkersDummyLayer.setProperty("underSelectionID", this.mUnderSelectionID);
        this.mMarkersLayer.setProperty("iconColor", this.mIconColorProperty);
        this.mMarkersDummyLayer.setProperty("iconColor", this.mIconColorProperty);
        this.mMarkersLayer.setProperty("isCustomTheme", this.mIsCustomTheme);
        this.mMarkersDummyLayer.setProperty("isCustomTheme", this.mIsCustomTheme);
        this.mConfig.setupLayer(this.mMarkersLayer, SGKeyCode.CODE_NUMPAD_SUBTRACT, null, null);
        this.mConfig.setupLayer(this.mMarkersDummyLayer, SGKeyCode.CODE_NUMPAD_SUBTRACT, null, null);
        SGShaderProgramProperty program = new SGShaderProgramProperty(new SGStringShaderProperty(SGShaderType.VERTEX, "attribute float cityId;attribute vec4 location;attribute float indexOfVertex;uniform mat4 ModelViewProjection;uniform vec3 camVector;uniform float alphaByLevels[6];uniform float pxInScreenX;uniform float pxInScreenY;uniform float textureOffsets[64];uniform float geometryOffsets[64];uniform float citiesType[50];uniform float interpolationFactor;uniform float dummyFlag;uniform float underSelectionID;uniform bool isCustomTheme;varying vec2 textureCoords;varying float alpha;varying float recolor;void main(){   float isUnderSelection = step(0.5, abs(underSelectionID - cityId));   alpha = (1.0 - smoothstep(0.2, 0.7, 1.0 - dot(camVector, location.xyz))) * isUnderSelection;   if(alpha < 0.001){       gl_Position = vec4(100.0);       textureCoords = vec2(0.0);       recolor = 1.0;   }else{       int index = int(cityId)/8;       float shift = pow(2.0, mod(cityId, 8.0)*2.0);       float markerType = mod(floor(citiesType[index]/shift), 4.0);       float disAlpha = alpha*interpolationFactor;       recolor = step(0.5, markerType);       alpha = ((1.0 - dummyFlag) * alpha + dummyFlag) * alphaByLevels[int(location.w * step(0.5, 1.0 - markerType))];       int xIndex = int(markerType * 16.0 + indexOfVertex *  2.0);       int yIndex = xIndex + 1;       textureCoords = vec2(textureOffsets[xIndex], textureOffsets[yIndex]);       vec4 mesh = ModelViewProjection*vec4(location.xyz, 1.0);       mesh.z = 0.0;       mesh /= mesh.w;       mesh.x += (geometryOffsets[xIndex]*disAlpha)*pxInScreenX;       mesh.y += (geometryOffsets[yIndex]*disAlpha)*pxInScreenY;       gl_Position = mesh;   }   recolor *= float(isCustomTheme);}"), new SGStringShaderProperty(SGShaderType.FRAGMENT, "precision mediump float;uniform sampler2D SGTexture;uniform vec3 iconColor;varying vec2 textureCoords;varying float alpha;varying float recolor;void main(){    vec4 sceneColor = texture2D(SGTexture, textureCoords);    vec3 hue = sceneColor.rgb * (1.0 - recolor) + iconColor.rgb * (recolor);    hue /= sceneColor.a;    gl_FragColor = vec4(hue, sceneColor.a);    gl_FragColor.a *= alpha;}"));
        this.mMarkersLayer.setProgramProperty(program);
        this.mMarkersDummyLayer.setProgramProperty(program);
    }

    protected void setSize(int width, int height) {
        this.mCitiesLayer.setSize((float) width, (float) height);
        this.mCitiesTime.setSize(width, height);
        this.mCityUS.setSize(width, height);
        this.mMarkersLayer.setSize((float) width, (float) height);
        this.mCitiesDummyLayer.setSize((float) width, (float) height);
        this.mMarkersDummyLayer.setSize((float) width, (float) height);
        updateAlphas();
    }

    private void setDummyVisibility(boolean visibility) {
        this.mCitiesDummyLayer.setVisibility(visibility);
        this.mCitiesTime.setDummyVisibility(visibility);
        this.mMarkersDummyLayer.setVisibility(visibility);
    }

    private void updateAlphas() {
        this.mAlpha.set(this.mAlphas);
        this.mCitiesLayer.setProperty("alphaByLevels", this.mAlpha);
        this.mCitiesTime.updateAlphas(this.mAlpha);
        this.mCityUS.updateAlphas(this.mAlpha);
        this.mMarkersLayer.setProperty("alphaByLevels", this.mAlpha);
    }

    private void updateFadeOutAlphas() {
        if (this.mCitiesDummyLayer.getVisibility()) {
            this.mFadeOutAlpha.set(this.mFadeOutAlphas);
            this.mCitiesDummyLayer.setProperty("alphaByLevels", this.mFadeOutAlpha);
            this.mCitiesTime.updateFadeOutAlphas(this.mFadeOutAlpha);
            this.mMarkersDummyLayer.setProperty("alphaByLevels", this.mFadeOutAlpha);
        }
    }

    protected void runInitialAnimation() {
        this.mAnimatorInitial = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimatorInitial.setDuration(100);
        this.mAnimatorInitial.addUpdateListener(new C08411());
        this.mAnimatorInitial.start();
        updateAlphas();
    }

    private void updateCityMarkersInternal() {
        City[] cities = CityManager.getCitiesByRawOffset();
        if (cities != null) {
            int count = cities.length;
            Arrays.fill(this.mCitiesType, 0.0f);
            for (int i = 0; i < count; i++) {
                int index = i / 8;
                int shift = i % 8;
                float[] fArr = this.mCitiesType;
                fArr[index] = (float) (((double) fArr[index]) + (((double) (cities[i].getDBSelected() ? 2.0f : 0.0f)) * Math.pow(2.0d, (double) (shift * 2))));
            }
            this.mCitiesTypeProperty.set(this.mCitiesType);
        }
    }

    protected void updateCityMarkers() {
        updateCityMarkersInternal();
        this.mMarkersLayer.setProperty("citiesType", this.mCitiesTypeProperty);
        this.mMarkersDummyLayer.setProperty("citiesType", this.mCitiesTypeProperty);
    }

    protected void setCityUnderSelID() {
        setCityUnderSelID(this.mCityUnderSelectionID);
    }

    protected void setCityUnderSelID(int ID) {
        this.mCityUnderSelectionID = ID;
        this.mUnderSelectionID.set((float) this.mCityUnderSelectionID);
        if (ID >= 0) {
            this.mCityUS.createUnderSelectionGeometry(ID);
        }
        City[] cities = CityManager.getCitiesByRawOffset();
        if (cities != null) {
            int count = cities.length;
            Arrays.fill(this.mCitiesType, 0.0f);
            int i = 0;
            while (i < count) {
                int index = i / 8;
                int shift = i % 8;
                if (ID < 0 || cities[i].getUniqueId() != ID) {
                    float[] fArr = this.mCitiesType;
                    fArr[index] = (float) (((double) fArr[index]) + (((double) (cities[i].getDBSelected() ? 2.0f : 0.0f)) * Math.pow(2.0d, (double) (shift * 2))));
                } else {
                    float markerType = cities[i].getDBCurrentLocation() ? 3.0f : 1.0f;
                    float[] fArr2 = this.mCitiesType;
                    fArr2[index] = (float) (((double) fArr2[index]) + (((double) markerType) * Math.pow(2.0d, (double) (shift * 2))));
                    this.mCityUS.setType(markerType);
                    this.mCityUS.setUSLocation(new float[]{this.mBillboardData[ID].f27x, this.mBillboardData[ID].f28y, this.mBillboardData[ID].f29z, (float) (cities[ID].mZoomLevel + 1)});
                }
                i++;
            }
            this.mCitiesTypeProperty.set(this.mCitiesType);
            this.mMarkersLayer.setProperty("citiesType", this.mCitiesTypeProperty);
            this.mMarkersDummyLayer.setProperty("citiesType", this.mCitiesTypeProperty);
        }
    }

    protected City getCityAt(float touchLongitude, float touchLatitude, float distance, float windowX, float windowY) {
        float cutOffDist = distance * 5.5f;
        float[] tempCoords = new float[4];
        windowY = this.mCitiesLayer.getSize().getY() - windowY;
        City selectedCity = null;
        City[] cities = CityManager.getCitiesByRawOffset();
        if (cities != null) {
            float dist_comp = Float.MAX_VALUE;
            int zoomLevel = findZoomLevel(distance);
            int i = cities.length - 1;
            while (i >= 0) {
                City city = cities[i];
                if (city != null && (zoomLevel >= city.mZoomLevel || city.getDBSelected())) {
                    float distLat = Math.abs(touchLongitude - city.mLatitudeBillboard);
                    float distLong = Math.abs(touchLatitude - city.mLongitudeBillboard);
                    if (distLat < cutOffDist && distLong < cutOffDist && this.mBillboardData[i] != null) {
                        GLU.gluProject(this.mBillboardData[i].f27x, this.mBillboardData[i].f28y, this.mBillboardData[i].f29z, this.mConfig.mViewMat, 0, this.mConfig.mProjMat, 0, new int[]{0, 0, (int) viewSize.getX(), (int) viewSize.getY()}, 0, tempCoords, 0);
                        float priority = 10.0f;
                        boolean isCityTouched = isPointInRect(windowX, windowY, tempCoords, this.mInterpolationFactor.get(), this.mBillboardData[i].textRect);
                        if (!isCityTouched) {
                            isCityTouched = isPointInRect(windowX, windowY, tempCoords, this.mInterpolationFactor.get(), this.mBillboardData[i].markerRect);
                        }
                        if (!isCityTouched) {
                            isCityTouched = isPointInRect(windowX, windowY, tempCoords, this.mInterpolationFactor.get(), this.mBillboardData[i].combinedRect);
                            priority = 1.0f;
                        }
                        if (isCityTouched) {
                            float dist_touch_centre = (((windowX - tempCoords[0]) * (windowX - tempCoords[0])) + ((windowY - tempCoords[1]) * (windowY - tempCoords[1]))) / priority;
                            if (dist_touch_centre < dist_comp) {
                                dist_comp = dist_touch_centre;
                                selectedCity = city;
                            }
                        }
                    }
                }
                i--;
            }
        }
        return selectedCity;
    }

    private boolean isPointInRect(float touchX, float touchY, float[] cityPos, float zoom, RectF rt) {
        if (touchX <= cityPos[0] + (rt.left * zoom) || touchX >= cityPos[0] + (rt.right * zoom)) {
            return false;
        }
        if (touchY >= cityPos[1] + (rt.top * zoom) || touchY <= cityPos[1] + (rt.bottom * zoom)) {
            return false;
        }
        return true;
    }

    private static int findZoomLevel(float distance) {
        for (int i = 1; i < SGILayerConfig.REVEAL_POINTS.length; i++) {
            if (distance >= SGILayerConfig.REVEAL_POINTS[i]) {
                return i - 1;
            }
        }
        return SGILayerConfig.REVEAL_POINTS.length - 1;
    }

    private static float findInterpolation(float distance) {
        if (distance >= SGILayerConfig.REVEAL_POINTS[1]) {
            return 1.0f;
        }
        float OldMax = 0.0f;
        float OldMin = 0.0f;
        float NewMin = Constants.MAXIMUM_MARKER_SCALE;
        float oldScale = distance;
        if (distance < SGILayerConfig.REVEAL_POINTS[1] && distance > SGILayerConfig.REVEAL_POINTS[2]) {
            OldMin = SGILayerConfig.REVEAL_POINTS[2];
            OldMax = SGILayerConfig.REVEAL_POINTS[1];
        }
        if (distance < SGILayerConfig.REVEAL_POINTS[2] && distance > SGILayerConfig.REVEAL_POINTS[3]) {
            OldMin = SGILayerConfig.REVEAL_POINTS[3];
            OldMax = SGILayerConfig.REVEAL_POINTS[2];
        }
        if (distance < SGILayerConfig.REVEAL_POINTS[3] && distance > SGILayerConfig.REVEAL_POINTS[4]) {
            OldMin = SGILayerConfig.REVEAL_POINTS[4];
            OldMax = SGILayerConfig.REVEAL_POINTS[3];
        }
        if (distance < SGILayerConfig.REVEAL_POINTS[4] && distance >= 1.37f) {
            OldMin = 1.37f;
            OldMax = SGILayerConfig.REVEAL_POINTS[4];
        }
        return (((oldScale - OldMin) * (1.0f - NewMin)) / (OldMax - OldMin)) + NewMin;
    }

    protected void runAnimation(final float markerSize, boolean isAnimated) {
        float distance = this.mConfig.mDistance;
        int k;
        if (isAnimated) {
            for (k = SGILayerConfig.REVEAL_POINTS.length - 1; k >= 0; k--) {
                float f;
                float revealDistance = SGILayerConfig.REVEAL_POINTS[k];
                final boolean isShowAnimCase = distance < revealDistance && revealDistance <= this.mDistance;
                boolean isHideAnimCase = this.mDistance < revealDistance && revealDistance <= distance;
                if (isShowAnimCase || isHideAnimCase) {
                    final int index = k;
                    if (this.mAnimator != null) {
                        this.mAnimator.cancel();
                    }
                    this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                    this.mAnimator.setDuration((long) ((int) Math.min(60.0f / Math.abs(distance - this.mDistance), 500.0f)));
                    this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = ((Float) animation.getAnimatedValue()).floatValue();
                            for (int i = 1; i < SGICities.this.mAlphas.length; i++) {
                                if (i <= index) {
                                    float f;
                                    float[] access$000 = SGICities.this.mAlphas;
                                    if (markerSize > 0.0f) {
                                        f = 1.0f;
                                    } else {
                                        f = value;
                                    }
                                    access$000[i] = f;
                                } else if (i != index + 1) {
                                    SGICities.this.mAlphas[i] = 0.0f;
                                } else if (isShowAnimCase) {
                                    SGICities.this.mAlphas[i] = value;
                                } else {
                                    SGICities.this.mAlphas[i] = 0.0f;
                                }
                            }
                            SGICities.this.updateAlphas();
                        }
                    });
                    this.mAnimator.start();
                    if (isShowAnimCase) {
                        break;
                    }
                }
                float[] fArr = this.mAlphas;
                int i = k + 1;
                float f2 = fArr[i];
                if (distance < revealDistance) {
                    f = 1.0f;
                } else {
                    f = 0.0f;
                }
                fArr[i] = f * f2;
            }
        } else {
            for (k = SGILayerConfig.REVEAL_POINTS.length - 1; k >= 0; k--) {
                this.mAlphas[k + 1] = distance < SGILayerConfig.REVEAL_POINTS[k] ? 1.0f : 0.0f;
            }
        }
        updateAlphas();
        this.mDistance = distance;
        SGFloatProperty sGFloatProperty = this.mInterpolationFactor;
        if (markerSize <= 0.0f) {
            markerSize = findInterpolation(distance);
        }
        sGFloatProperty.set(markerSize);
    }

    protected void runFadeOutAnimation(boolean isAnimated) {
        float distance = this.mConfig.mDistance;
        int i;
        if (isAnimated) {
            int endIndex = 0;
            boolean isShowAnimCase = false;
            boolean isHideAnimCase = false;
            for (i = SGILayerConfig.REVEAL_POINTS.length - 1; i >= 0; i--) {
                float revealDistance = SGILayerConfig.REVEAL_POINTS[i];
                isShowAnimCase = distance < revealDistance && this.mDummyDistance >= revealDistance;
                isHideAnimCase = distance >= revealDistance && this.mDummyDistance < revealDistance;
                if (isShowAnimCase || isHideAnimCase) {
                    endIndex = i;
                    break;
                }
            }
            final int tempEndIndex = endIndex;
            i = 0;
            while (true) {
                if (i >= (isHideAnimCase ? 1 : 0) + endIndex) {
                    break;
                }
                final int index = i;
                if (this.mFadeOutAnimators[i] != null) {
                    this.mFadeOutAnimators[i].cancel();
                }
                if (isShowAnimCase) {
                    this.mFadeOutAnimators[i] = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                    this.mMarkerSize.set(Constants.MAXIMUM_MARKER_SCALE);
                    this.mFadeOutAnimators[i].setDuration(500);
                } else {
                    this.mFadeOutAnimators[i] = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                    this.mMarkerSize.set(1.0f);
                    this.mFadeOutAnimators[i].setDuration((long) Math.min(500, (int) (5.0f / Math.abs(distance - this.mDummyDistance))));
                }
                this.mFadeOutAnimators[i].addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (SGICities.this.mFadeOutAlphas != null) {
                            float value = ((Float) animation.getAnimatedValue()).floatValue();
                            SGICities.this.mFadeOutAlphas[index + 1] = value;
                            float[] access$200 = SGICities.this.mFadeOutAlphas;
                            if (tempEndIndex <= 1) {
                                value = 0.0f;
                            }
                            access$200[0] = value;
                            SGICities.this.updateFadeOutAlphas();
                        }
                    }
                });
                this.mFadeOutAnimators[i].addListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                        SGICities.this.setDummyVisibility(true);
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }

                    public void onAnimationEnd(Animator animation) {
                        SGICities.this.mFadeOutAlphas[index + 1] = 0.0f;
                        SGICities.this.updateFadeOutAlphas();
                        SGICities.this.setDummyVisibility(false);
                    }

                    public void onAnimationCancel(Animator animation) {
                    }
                });
                this.mFadeOutAnimators[i].start();
                i++;
            }
        } else {
            for (i = 0; i < SGILayerConfig.REVEAL_POINTS.length; i++) {
                this.mFadeOutAlphas[i + 1] = distance < SGILayerConfig.REVEAL_POINTS[i] ? 1.0f : 0.0f;
            }
        }
        updateFadeOutAlphas();
        this.mDummyDistance = distance;
    }

    protected void reload() {
        createBillboardGeometries();
        this.mCitiesLayer.setProperty(SGProperty.TEXTURE, this.mTexture);
        this.mCitiesDummyLayer.setProperty(SGProperty.TEXTURE, this.mTexture);
        this.mMarkersLayer.setProperty(SGProperty.TEXTURE, this.mMarkerTexture);
        this.mMarkersDummyLayer.setProperty(SGProperty.TEXTURE, this.mMarkerTexture);
        this.mCitiesLayer.setGeometryGenerator(this.mCitiesGeometryGenerator);
        this.mCitiesDummyLayer.setGeometryGenerator(this.mCitiesGeometryGenerator);
        this.mMarkersLayer.setGeometryGenerator(this.mMarkersGeometryGenerator);
        this.mMarkersDummyLayer.setGeometryGenerator(this.mMarkersGeometryGenerator);
        this.mMarkersLayer.setProperty("citiesType", this.mCitiesTypeProperty);
        this.mMarkersDummyLayer.setProperty("citiesType", this.mCitiesTypeProperty);
        this.mMarkersLayer.setProperty("textureOffsets", this.mTextureOffset);
        this.mMarkersDummyLayer.setProperty("textureOffsets", this.mTextureOffset);
        this.mMarkersLayer.setProperty("geometryOffsets", this.mGeometryOffset);
        this.mMarkersDummyLayer.setProperty("geometryOffsets", this.mGeometryOffset);
        this.mMarkersLayer.setProperty("iconColor", this.mIconColorProperty);
        this.mMarkersDummyLayer.setProperty("iconColor", this.mIconColorProperty);
        this.mMarkersLayer.setProperty("isCustomTheme", this.mIsCustomTheme);
        this.mMarkersDummyLayer.setProperty("isCustomTheme", this.mIsCustomTheme);
        this.mCitiesTime.reload();
        this.mCityUS.reload();
    }

    protected void updateCityTime() {
        this.mCitiesTime.updateCityTime();
    }

    public void onResume() {
        City[] cities = CityManager.getCitiesByRawOffset();
        if (cities != null) {
            this.mCitiesTime.createCityTimeGeometry(cities);
        }
    }

    private void createBillboardGeometries() {
        boolean z = Feature.isTablet(this.mContext) && ((float) this.mConfig.mDPI) <= 240.0f;
        this.mLowDensity = z;
        City[] cities = CityManager.getCitiesByRawOffset();
        if (cities != null) {
            int count = cities.length;
            int propertyCount = (count / 8) + 1;
            this.mCitiesTypeProperty = new SGFloatArrayProperty(propertyCount);
            this.mCitiesType = new float[propertyCount];
            updateCityMarkersInternal();
            this.mBillboardData = new BillboardData[count];
            for (int i = 0; i < count; i++) {
                cityLoc = new float[4];
                SGILayerConfig.rotationToDirection(cityLoc, (-cities[i].mLatitudeBillboard) * 0.017453292f, cities[i].mLongitudeBillboard * 0.017453292f);
                cityLoc[3] = (float) (cities[i].mZoomLevel + 1);
                this.mBillboardData[i] = new BillboardData(cityLoc, cities[i].getTimeZone());
            }
            SGIndexBuffer indexBuffer = new SGIndexBuffer(SGPrimitiveType.TRIANGLES, SGBufferUsageType.STATIC_DRAW, count * 6);
            SGVertexBuffer locationBuffer = new SGVertexBuffer(4, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, count * 4);
            SGVertexBuffer cityIdBuffer = new SGVertexBuffer(1, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, count * 4);
            SGVector2f maxMarkerSize = createMarkerGeometry(cities, indexBuffer, locationBuffer, cityIdBuffer);
            createCitiesTextTexture(cities);
            createCityNameGeometry(cities, maxMarkerSize, indexBuffer, locationBuffer, cityIdBuffer);
            this.mCitiesTime.createCityTimeGeometry(cities);
            this.mCityUS.createUnderSelectionGeometry(this.mCityUnderSelectionID);
        }
    }

    private SGVector2f createMarkerGeometry(City[] cities, SGIndexBuffer markersIndexBuffer, SGVertexBuffer markersLocationBuffer, SGVertexBuffer markersCityIdBuffer) {
        Resources resources = this.mContext.getResources();
        int count = cities.length;
        float markerZoom = 1.0f;
        int markerShift = 0;
        if (this.mLowDensity) {
            markerZoom = 320.0f / ((float) this.mConfig.mDPI);
            markerShift = 1;
        }
        Drawable markerNormal = this.mContext.getDrawable(C0836R.drawable.clock_worldclock_map_ic_pointer_02);
        Drawable markerUnderSelection = this.mContext.getDrawable(C0836R.drawable.clock_worldclock_map_ic_select);
        Drawable markerSelected = this.mContext.getDrawable(C0836R.drawable.clock_worldclock_map_ic_pointer_01);
        Drawable markerCurrentLocation = this.mContext.getDrawable(C0836R.drawable.clock_worldclock_map_ic_select_current);
        float normal_icon_size = resources.getDimension(C0836R.dimen.worldclock_normal_icon_width) * markerZoom;
        float under_selection_icon_size = resources.getDimension(C0836R.dimen.worldclock_under_selection_icon_width) * markerZoom;
        float selected_icon_size = resources.getDimension(C0836R.dimen.worldclock_selected_icon_width) * markerZoom;
        float current_location_icon_size = resources.getDimension(C0836R.dimen.worldclock_current_location_icon_width) * markerZoom;
        float ResOrXml = normal_icon_size > 0.0f ? normal_icon_size / ((float) markerNormal.getIntrinsicWidth()) : 1.0f;
        int markerNormalWidth = (int) (((float) markerNormal.getIntrinsicWidth()) * ResOrXml);
        this.mMarkerNormalHeight = (int) (((float) markerNormal.getIntrinsicHeight()) * ResOrXml);
        ResOrXml = under_selection_icon_size > 0.0f ? under_selection_icon_size / ((float) markerUnderSelection.getIntrinsicWidth()) : 1.0f;
        int markerUnderSelectionWidth = (int) (((float) markerUnderSelection.getIntrinsicWidth()) * ResOrXml);
        this.mMarkerUnderSelectionHeight = (int) (((float) markerUnderSelection.getIntrinsicHeight()) * ResOrXml);
        ResOrXml = selected_icon_size > 0.0f ? selected_icon_size / ((float) markerSelected.getIntrinsicWidth()) : 1.0f;
        int markerSelectedWidth = (int) (((float) markerSelected.getIntrinsicWidth()) * ResOrXml);
        int markerSelectedHeight = (int) (((float) markerSelected.getIntrinsicHeight()) * ResOrXml);
        int halfMaxMarkerWidth = Math.max(markerSelectedWidth, Math.max(markerNormalWidth, markerUnderSelectionWidth)) / 2;
        int halfMaxMarkerHeight = Math.max(markerSelectedHeight, Math.max(this.mMarkerNormalHeight, this.mMarkerUnderSelectionHeight)) / 2;
        this.MARKER_ATLAS_WIDTH = ((markerNormalWidth + markerUnderSelectionWidth) + markerSelectedWidth) + 8;
        this.MARKER_ATLAS_HEIGHT = max(this.mMarkerNormalHeight, this.mMarkerUnderSelectionHeight, markerSelectedHeight);
        int markerCurrentLocationWidth = 0;
        int markerCurrentLocationHeight = 0;
        if (markerCurrentLocation != null) {
            ResOrXml = current_location_icon_size > 0.0f ? current_location_icon_size / ((float) markerCurrentLocation.getIntrinsicWidth()) : 1.0f;
            markerCurrentLocationWidth = (int) (((float) markerCurrentLocation.getIntrinsicWidth()) * ResOrXml);
            markerCurrentLocationHeight = (int) (((float) markerCurrentLocation.getIntrinsicHeight()) * ResOrXml);
            this.MARKER_ATLAS_WIDTH += markerCurrentLocationWidth + 4;
            this.MARKER_ATLAS_HEIGHT = Math.max(this.MARKER_ATLAS_HEIGHT, markerCurrentLocationHeight);
        }
        System.out.println("------------>Marker atlas size: " + this.MARKER_ATLAS_WIDTH + " x " + this.MARKER_ATLAS_HEIGHT);
        Bitmap markerTextureAtlas = Bitmap.createBitmap(this.MARKER_ATLAS_WIDTH, this.MARKER_ATLAS_HEIGHT, Config.ARGB_8888);
        Canvas canvas = new Canvas(markerTextureAtlas);
        int positionXOffset = 0 + markerNormalWidth;
        markerNormal.setBounds(0 + markerShift, markerShift, positionXOffset - markerShift, this.mMarkerNormalHeight - markerShift);
        markerNormal.draw(canvas);
        positionXOffset += 4;
        int i = positionXOffset + markerShift;
        positionXOffset += markerUnderSelectionWidth;
        markerUnderSelection.setBounds(i, markerShift, positionXOffset - markerShift, this.mMarkerUnderSelectionHeight - markerShift);
        markerUnderSelection.draw(canvas);
        positionXOffset += 4;
        i = positionXOffset + markerShift;
        positionXOffset += markerSelectedWidth;
        markerSelected.setBounds(i, markerShift, positionXOffset - markerShift, markerSelectedHeight - markerShift);
        markerSelected.draw(canvas);
        positionXOffset += 4;
        if (markerCurrentLocation != null) {
            markerCurrentLocation.setBounds(positionXOffset + markerShift, markerShift, (positionXOffset + markerCurrentLocationWidth) - markerShift, markerCurrentLocationHeight - markerShift);
            markerCurrentLocation.draw(canvas);
        }
        this.mMarkerTexture.setBitmap(markerTextureAtlas, true);
        ShortBuffer markersIndices = markersIndexBuffer.getShortBuffer();
        FloatBuffer markersLocations = markersLocationBuffer.getByteBuffer().asFloatBuffer();
        FloatBuffer markersCityId = markersCityIdBuffer.getByteBuffer().asFloatBuffer();
        SGBuffer sGVertexBuffer = new SGVertexBuffer(1, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, count * 4);
        FloatBuffer markersIndexOfVertexInfo = sGVertexBuffer.getByteBuffer().asFloatBuffer();
        for (int i2 = 0; i2 < count; i2++) {
            int tmp = i2 * 4;
            markersIndices.put((short) tmp);
            markersIndices.put((short) (tmp + 1));
            markersIndices.put((short) (tmp + 2));
            markersIndices.put((short) tmp);
            markersIndices.put((short) (tmp + 2));
            markersIndices.put((short) (tmp + 3));
            markersLocations.put(this.mBillboardData[i2].f27x);
            markersLocations.put(this.mBillboardData[i2].f28y);
            markersLocations.put(this.mBillboardData[i2].f29z);
            markersLocations.put(this.mBillboardData[i2].zoom);
            markersLocations.put(this.mBillboardData[i2].f27x);
            markersLocations.put(this.mBillboardData[i2].f28y);
            markersLocations.put(this.mBillboardData[i2].f29z);
            markersLocations.put(this.mBillboardData[i2].zoom);
            markersLocations.put(this.mBillboardData[i2].f27x);
            markersLocations.put(this.mBillboardData[i2].f28y);
            markersLocations.put(this.mBillboardData[i2].f29z);
            markersLocations.put(this.mBillboardData[i2].zoom);
            markersLocations.put(this.mBillboardData[i2].f27x);
            markersLocations.put(this.mBillboardData[i2].f28y);
            markersLocations.put(this.mBillboardData[i2].f29z);
            markersLocations.put(this.mBillboardData[i2].zoom);
            float direction = ((cities[i2].getArrowDirection() >> 2) & 3) != 1 ? 0.0f : 4.0f;
            int ID = cities[i2].getUniqueId();
            markersIndexOfVertexInfo.put(0.0f + direction);
            markersIndexOfVertexInfo.put(1.0f + direction);
            markersIndexOfVertexInfo.put(2.0f + direction);
            markersIndexOfVertexInfo.put(3.0f + direction);
            markersCityId.put((float) ID);
            markersCityId.put((float) ID);
            markersCityId.put((float) ID);
            markersCityId.put((float) ID);
            this.mBillboardData[i2].markerRect = new RectF((float) (-halfMaxMarkerWidth), (float) (-halfMaxMarkerHeight), (float) halfMaxMarkerWidth, (float) halfMaxMarkerHeight);
        }
        SGGeometry geometry = new SGGeometry(markersIndexBuffer);
        geometry.addBuffer("location", markersLocationBuffer);
        geometry.addBuffer("cityId", markersCityIdBuffer);
        geometry.addBuffer("indexOfVertex", sGVertexBuffer);
        this.mMarkersGeometryGenerator = SGGeometryGeneratorFactory.createStaticGeometryGenerator(geometry);
        FloatBuffer textureOffset = this.mTextureOffset.getFloatBuffer();
        this.mDeltaTextMarker = resources.getDimension(C0836R.dimen.worldclock_normal_marker_text_gap);
        this.mDeltaTextBalloon = resources.getDimension(C0836R.dimen.worldclock_balloon_marker_text_gap);
        float delta = this.mDeltaTextBalloon - this.mDeltaTextMarker;
        float markerRight = ((float) markerNormalWidth) / ((float) this.MARKER_ATLAS_WIDTH);
        float markerBottom = ((float) this.mMarkerNormalHeight) / ((float) this.MARKER_ATLAS_HEIGHT);
        textureOffset.put(0.0f);
        textureOffset.put(0.0f);
        textureOffset.put(0.0f);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        textureOffset.put(0.0f);
        textureOffset.put(0.0f);
        textureOffset.put(0.0f);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        float markerLeft = markerRight + (4.0f / ((float) this.MARKER_ATLAS_WIDTH));
        markerRight = markerLeft + (((float) markerUnderSelectionWidth) / ((float) this.MARKER_ATLAS_WIDTH));
        markerBottom = ((float) this.mMarkerUnderSelectionHeight) / ((float) this.MARKER_ATLAS_HEIGHT);
        textureOffset.put(markerLeft);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        markerLeft = markerRight + (4.0f / ((float) this.MARKER_ATLAS_WIDTH));
        markerRight = markerLeft + (((float) markerSelectedWidth) / ((float) this.MARKER_ATLAS_WIDTH));
        markerBottom = ((float) markerSelectedHeight) / ((float) this.MARKER_ATLAS_HEIGHT);
        textureOffset.put(markerLeft);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        markerLeft = markerRight + (4.0f / ((float) this.MARKER_ATLAS_WIDTH));
        markerRight = markerLeft + (((float) markerCurrentLocationWidth) / ((float) this.MARKER_ATLAS_WIDTH));
        markerBottom = ((float) markerCurrentLocationHeight) / ((float) this.MARKER_ATLAS_HEIGHT);
        textureOffset.put(markerLeft);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(0.0f);
        textureOffset.put(markerLeft);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(markerBottom);
        textureOffset.put(markerRight);
        textureOffset.put(0.0f);
        FloatBuffer geometryOffset = this.mGeometryOffset.getFloatBuffer();
        float markerHalfWidth = ((float) markerNormalWidth) / (2.0f * markerZoom);
        float markerHalfHeight = ((float) this.mMarkerNormalHeight) / (2.0f * markerZoom);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerHalfHeight);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(-markerHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(-markerHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerHalfHeight);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerHalfHeight);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(-markerHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(-markerHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerHalfHeight);
        markerHalfWidth = ((float) markerUnderSelectionWidth) / (2.0f * markerZoom);
        float markerSecondHalfHeight = (((float) this.mMarkerUnderSelectionHeight) / markerZoom) - markerHalfHeight;
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerSecondHalfHeight + delta);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put((-markerHalfHeight) + delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put((-markerHalfHeight) + delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerSecondHalfHeight + delta);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerHalfHeight - delta);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put((-markerSecondHalfHeight) - delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put((-markerSecondHalfHeight) - delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerHalfHeight - delta);
        markerHalfWidth = ((float) markerSelectedWidth) / (2.0f * markerZoom);
        markerSecondHalfHeight = (((float) markerSelectedHeight) / markerZoom) - markerHalfHeight;
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerSecondHalfHeight);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(-markerHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(-markerHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerSecondHalfHeight);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerHalfHeight);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(-markerSecondHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(-markerSecondHalfHeight);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerHalfHeight);
        markerHalfWidth = ((float) markerCurrentLocationWidth) / (2.0f * markerZoom);
        markerSecondHalfHeight = (((float) markerCurrentLocationHeight) / markerZoom) - markerHalfHeight;
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerSecondHalfHeight + delta);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put((-markerHalfHeight) + delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put((-markerHalfHeight) + delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerSecondHalfHeight + delta);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put(markerHalfHeight - delta);
        geometryOffset.put(-markerHalfWidth);
        geometryOffset.put((-markerSecondHalfHeight) - delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put((-markerSecondHalfHeight) - delta);
        geometryOffset.put(markerHalfWidth);
        geometryOffset.put(markerHalfHeight - delta);
        this.mTextureOffset.invalidate();
        this.mGeometryOffset.invalidate();
        return new SGVector2f((float) (halfMaxMarkerWidth * 2), (float) (halfMaxMarkerHeight * 2));
    }

    private void createCitiesTextTexture(City[] cities) {
        int i;
        this.mCitiesTime.createTimeSymbolsString();
        Resources resources = this.mContext.getResources();
        int count = cities.length;
        Paint cityPaint = new Paint();
        cityPaint.setAntiAlias(true);
        cityPaint.setTextAlign(Align.LEFT);
        cityPaint.setTypeface(Typeface.DEFAULT);
        float textSize = resources.getDimension(C0836R.dimen.worldclock_city_textsize);
        if (StateUtils.isContextInDexMode(this.mContext)) {
            textSize = resources.getDimension(C0836R.dimen.worldclock_dexmode_city_textsize);
        }
        if (textSize > 64.0f) {
            this.TEXTURE_ATLAS_WIDTH = 4096;
        }
        cityPaint.setTextSize(textSize);
        FontMetricsInt fontMetrics = cityPaint.getFontMetricsInt();
        this.mNormalTop = -fontMetrics.top;
        this.mTextHeightInAtlas = (fontMetrics.bottom - fontMetrics.top) + 4;
        int xPos = 0;
        int yPos = this.mTextHeightInAtlas;
        Rect textBounds = new Rect();
        this.mTextWidthArray = new int[count];
        this.mTextCoordinatesArray = new Rect[count];
        US_TEXTURE_WIDTH = 0;
        for (i = 0; i < count; i++) {
            String name = cities[i].getName();
            cityPaint.getTextBounds(name, 0, name.length(), textBounds);
            int widthInAtlas = textBounds.width() + 4;
            this.mTextWidthArray[i] = widthInAtlas;
            if (widthInAtlas > US_TEXTURE_WIDTH) {
                US_TEXTURE_WIDTH = widthInAtlas;
            }
            xPos += widthInAtlas;
            if (xPos >= this.TEXTURE_ATLAS_WIDTH) {
                yPos += this.mTextHeightInAtlas;
                xPos = widthInAtlas;
            }
            this.mTextCoordinatesArray[i] = new Rect(textBounds);
        }
        US_TEXTURE_WIDTH = (int) (((double) US_TEXTURE_WIDTH) * 1.3d);
        this.mCitiesTime.calculateTimeSymbols(xPos, yPos);
        System.out.println("------------>Name atlas size: " + this.TEXTURE_ATLAS_WIDTH + " x " + this.TEXTURE_ATLAS_HEIGHT);
        Bitmap textureAtlas = Bitmap.createBitmap(this.TEXTURE_ATLAS_WIDTH, this.TEXTURE_ATLAS_HEIGHT, Config.ALPHA_8);
        Canvas canvas = new Canvas(textureAtlas);
        xPos = 0;
        yPos = this.mNormalTop;
        for (i = 0; i < count; i++) {
            name = cities[i].getName();
            widthInAtlas = this.mTextWidthArray[i];
            if (xPos + widthInAtlas >= this.TEXTURE_ATLAS_WIDTH) {
                xPos = 0;
                yPos += this.mTextHeightInAtlas;
            }
            canvas.drawText(name, (float) ((xPos + 2) - this.mTextCoordinatesArray[i].left), (float) (yPos + 2), cityPaint);
            if (this.mLowDensity) {
                canvas.drawText(name, (float) ((xPos + 2) - this.mTextCoordinatesArray[i].left), (float) (yPos + 2), cityPaint);
            }
            xPos += widthInAtlas;
        }
        this.mCitiesTime.drawTimeSymbols(canvas);
        this.mTexture.setBitmap(textureAtlas, true);
    }

    private void createCityNameGeometry(City[] cities, SGVector2f maxMarkerSize, SGIndexBuffer citiesIndexBuffer, SGVertexBuffer citiesLocationBuffer, SGVertexBuffer citiesCityIdBuffer) {
        Resources resources = this.mContext.getResources();
        int count = cities.length;
        float dpSize = resources.getDisplayMetrics().density;
        SGVertexBuffer citiesPositionBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, count * 4);
        FloatBuffer citiesPositions = citiesPositionBuffer.getByteBuffer().asFloatBuffer();
        SGVertexBuffer citiesTexcoordBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, count * 4);
        FloatBuffer citiesTexcoord = citiesTexcoordBuffer.getByteBuffer().asFloatBuffer();
        this.mDeltaTextTime = resources.getDimension(C0836R.dimen.worldclock_text_time_gap);
        int xPos = 0;
        int yPos = 0;
        float maxMarkerWidth = maxMarkerSize.getX();
        float maxMarkerHeight = maxMarkerSize.getY();
        float halfMaxMarkerHeight = maxMarkerHeight / 2.0f;
        for (int i = 0; i < count; i++) {
            int arrowDirection = cities[i].getArrowDirection();
            int markerAlignX = arrowDirection & 3;
            int markerAlignY = (arrowDirection >> 2) & 3;
            int textWidthInAtlas = this.mTextWidthArray[i];
            if (xPos + textWidthInAtlas >= this.TEXTURE_ATLAS_WIDTH) {
                xPos = 0;
                yPos += this.mTextHeightInAtlas;
            }
            float uLeft = ((float) xPos) / ((float) this.TEXTURE_ATLAS_WIDTH);
            float uRight = ((float) (xPos + textWidthInAtlas)) / ((float) this.TEXTURE_ATLAS_WIDTH);
            float vTop = ((float) (((this.mNormalTop + yPos) + this.mTextCoordinatesArray[i].top) - 2)) / ((float) this.TEXTURE_ATLAS_HEIGHT);
            float vBottom = ((float) (((this.mNormalTop + yPos) + this.mTextCoordinatesArray[i].bottom) + 2)) / ((float) this.TEXTURE_ATLAS_HEIGHT);
            xPos += textWidthInAtlas;
            citiesTexcoord.put(uLeft);
            citiesTexcoord.put(vTop);
            citiesTexcoord.put(uLeft);
            citiesTexcoord.put(vBottom);
            citiesTexcoord.put(uRight);
            citiesTexcoord.put(vBottom);
            citiesTexcoord.put(uRight);
            citiesTexcoord.put(vTop);
            int billboardTextXOffset = (int) (((float) cities[i].getXTextOffset()) * dpSize);
            int billboardTextYOffset = (int) (((float) cities[i].getYTextOffset()) * dpSize);
            RectF textRect = new RectF();
            RectF combinedRect = new RectF();
            switch (markerAlignX) {
                case 1:
                    textRect.right = this.mBillboardData[i].markerRect.right;
                    textRect.left = textRect.right - ((float) textWidthInAtlas);
                    break;
                case 2:
                    textRect.left = this.mBillboardData[i].markerRect.left;
                    textRect.right = textRect.left + ((float) textWidthInAtlas);
                    break;
                default:
                    textRect.left = ((float) (-textWidthInAtlas)) / 2.0f;
                    textRect.right = ((float) textWidthInAtlas) / 2.0f;
                    break;
            }
            textRect.left -= (float) billboardTextXOffset;
            textRect.right -= (float) billboardTextXOffset;
            combinedRect.left = Math.min(this.mBillboardData[i].markerRect.left, textRect.left) - maxMarkerWidth;
            combinedRect.right = Math.max(this.mBillboardData[i].markerRect.right, textRect.right) + maxMarkerWidth;
            if (markerAlignY == 1) {
                textRect.bottom = (((((float) billboardTextYOffset) + (((float) this.mMarkerNormalHeight) / 2.0f)) + this.mDeltaTextMarker) + ((float) this.mTimeHeightInAtlas)) + this.mDeltaTextTime;
                textRect.top = textRect.bottom + ((float) (this.mTextCoordinatesArray[i].height() + 4));
                combinedRect.top = textRect.top + halfMaxMarkerHeight;
                combinedRect.bottom = -maxMarkerHeight;
            } else {
                textRect.top = ((float) billboardTextYOffset) - ((((float) this.mMarkerNormalHeight) / 2.0f) + this.mDeltaTextMarker);
                textRect.bottom = textRect.top - ((float) (this.mTextCoordinatesArray[i].height() + 4));
                combinedRect.bottom = (textRect.bottom - halfMaxMarkerHeight) - ((float) this.mTimeHeightInAtlas);
                combinedRect.top = maxMarkerHeight;
            }
            this.mBillboardData[i].textRect = textRect;
            this.mBillboardData[i].combinedRect = combinedRect;
            citiesPositions.put(textRect.left);
            citiesPositions.put(textRect.top);
            citiesPositions.put(textRect.left);
            citiesPositions.put(textRect.bottom);
            citiesPositions.put(textRect.right);
            citiesPositions.put(textRect.bottom);
            citiesPositions.put(textRect.right);
            citiesPositions.put(textRect.top);
        }
        SGGeometry geometry = new SGGeometry(citiesIndexBuffer);
        geometry.addBuffer("location", citiesLocationBuffer);
        geometry.addBuffer(SGProperty.POSITIONS, citiesPositionBuffer);
        geometry.addBuffer(SGProperty.TEXTURE_COORDS, citiesTexcoordBuffer);
        geometry.addBuffer("cityId", citiesCityIdBuffer);
        this.mCitiesGeometryGenerator = SGGeometryGeneratorFactory.createStaticGeometryGenerator(geometry);
    }

    RectF getUSRect(int index) {
        return this.mBillboardData[index].textRect;
    }

    private static int max(int a, int b, int c) {
        return a > b ? a > c ? a : c : b > c ? b : c;
    }

    private float cityCardDistance(City targetCity, float distance) {
        float delta = this.mDeltaTextBalloon - this.mDeltaTextMarker;
        float offsetUp = (this.mDeltaTextBalloon + (((float) this.mMarkerNormalHeight) / 2.0f)) - delta;
        if (findZoomLevel(distance) >= targetCity.getZoomLevel()) {
            offsetUp += (float) this.mCityUS.US_TEXTURE_HEIGHT;
        }
        float offsetDown = (((float) this.mMarkerUnderSelectionHeight) - (((float) this.mMarkerNormalHeight) / 2.0f)) + delta;
        if (((targetCity.getArrowDirection() >> 2) & 3) == 1) {
            return offsetUp;
        }
        return offsetDown;
    }

    protected float cityCardOffset(City targetCity) {
        return this.mInterpolationFactor.get() * cityCardDistance(targetCity, this.mDistance);
    }

    protected float cityCardOffset(City targetCity, float distance) {
        return findInterpolation(distance) * cityCardDistance(targetCity, distance);
    }
}
