package com.sec.android.app.clockpackage.worldclock.sgi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.internal.view.SupportMenu;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGBitmapTexture2DProperty;
import com.samsung.android.sdk.sgi.render.SGBufferDataType;
import com.samsung.android.sdk.sgi.render.SGBufferUsageType;
import com.samsung.android.sdk.sgi.render.SGFloatArrayProperty;
import com.samsung.android.sdk.sgi.render.SGFloatProperty;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGIndexBuffer;
import com.samsung.android.sdk.sgi.render.SGPrimitiveType;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGTextureFilterType;
import com.samsung.android.sdk.sgi.render.SGTextureWrapType;
import com.samsung.android.sdk.sgi.render.SGVectorfProperty;
import com.samsung.android.sdk.sgi.render.SGVertexBuffer;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.samsung.android.sdk.sgi.vi.SGGeometryGenerator;
import com.samsung.android.sdk.sgi.vi.SGGeometryGeneratorFactory;
import com.samsung.android.sdk.sgi.vi.SGLayerImage;
import com.samsung.android.sdk.sgi.vi.SGSurface;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public final class SGICityUnderSelection {
    public int US_TEXTURE_HEIGHT;
    private final SGICities mCitiesConfig;
    private final SGVectorfProperty mCityUSColorProperty = new SGVectorfProperty(3);
    private final SGILayerConfig mConfig;
    private final Context mContext;
    private final SGVectorfProperty mUSLocationProperty = new SGVectorfProperty(4);
    private SGGeometryGenerator mUSMarkerGeometryGenerator;
    private SGGeometryGenerator mUSTextGeometryGenerator;
    private SGLayerImage mUnderSelectionMarkerLayer;
    private SGLayerImage mUnderSelectionTextLayer;
    private final SGBitmapTexture2DProperty mUnderSelectionTexture;
    private final SGFloatProperty mUnderSelectionType = new SGFloatProperty(0.0f);

    protected SGICityUnderSelection(SGSurface surface, SGILayerConfig config, SGICities cities) {
        int underSelectionColor;
        this.mConfig = config;
        this.mCitiesConfig = cities;
        this.mUnderSelectionTexture = new SGBitmapTexture2DProperty(SGTextureWrapType.CLAMP_TO_EDGE, SGTextureWrapType.CLAMP_TO_EDGE);
        this.mContext = surface.getView().getContext();
        if (StateUtils.isContextInDexMode(this.mContext)) {
            this.mUnderSelectionTexture.setMinFilter(SGTextureFilterType.NEAREST);
            this.mUnderSelectionTexture.setMagFilter(SGTextureFilterType.NEAREST);
        }
        if (StateUtils.isContextInDexMode(this.mContext)) {
            underSelectionColor = this.mContext.getColor(C0836R.color.worldclock_dex_under_selection_textcolor);
        } else {
            underSelectionColor = this.mContext.getColor(C0836R.color.worldclock_under_selection_textcolor);
        }
        this.mCityUSColorProperty.set(new SGVector3f(((float) Color.red(underSelectionColor)) / 255.0f, ((float) Color.green(underSelectionColor)) / 255.0f, ((float) Color.blue(underSelectionColor)) / 255.0f));
        initUnderSelectionTextLayer();
        initUnderSelectionMarkerLayer();
    }

    public void addLayers(SGSurface surface) {
        surface.addLayer(this.mUnderSelectionTextLayer);
        surface.addLayer(this.mUnderSelectionMarkerLayer);
    }

    private void initUnderSelectionTextLayer() {
        this.mUnderSelectionTextLayer = new SGLayerImage();
        this.mUnderSelectionTextLayer.setGeometryGenerator(this.mUSTextGeometryGenerator);
        this.mUnderSelectionTextLayer.setTexture(this.mUnderSelectionTexture);
        this.mUnderSelectionTextLayer.setProperty("globalScale", this.mCitiesConfig.mGlobalScale);
        this.mUnderSelectionTextLayer.setProperty("interpolationFactor", this.mCitiesConfig.mInterpolationFactor);
        this.mConfig.setupLayer(this.mUnderSelectionTextLayer, SGKeyCode.CODE_NUMPAD_SUBTRACT, "attribute vec2 SGPositions;attribute vec2 SGTextureCoords;uniform vec4 underSelectionLocation;uniform mat4 ModelViewProjection;uniform vec3 camVector;uniform float alphaByLevels[6];uniform float pxInScreenX;uniform float pxInScreenY;uniform float interpolationFactor;uniform float underSelectionID;varying vec2 textureCoords;varying float alpha;void main(){   float anyUnderSelection = step(-0.5, underSelectionID);   alpha = (1.0 - smoothstep(0.2, 0.7, 1.0 - dot(camVector, underSelectionLocation.xyz))) * anyUnderSelection;   if(alpha < 0.001){       gl_Position = vec4(100.0);       textureCoords = vec2(0.0);   }else{       float disAlpha = alpha*interpolationFactor;       alpha *= alphaByLevels[int(underSelectionLocation.w)];       textureCoords = SGTextureCoords;       vec4 mesh = ModelViewProjection*vec4(underSelectionLocation.xyz, 1.0);       mesh.z = 0.0;       mesh /= mesh.w;       mesh.x += SGPositions.x*disAlpha*pxInScreenX;       mesh.y += SGPositions.y*disAlpha*pxInScreenY;       gl_Position = mesh;   }}", "precision highp float;uniform sampler2D SGTexture;uniform float globalScale;uniform vec3 cityTextColor;varying vec2 textureCoords;varying float alpha;void main(){    vec4 sceneColor = vec4(cityTextColor.rgb, texture2D(SGTexture, textureCoords).r);    gl_FragColor = sceneColor;    gl_FragColor.a *= alpha * globalScale;}");
        this.mUnderSelectionTextLayer.setProperty("cityTextColor", this.mCityUSColorProperty);
        this.mUnderSelectionTextLayer.setProperty("underSelectionID", this.mCitiesConfig.mUnderSelectionID);
        this.mUnderSelectionTextLayer.setProperty("underSelectionLocation", this.mUSLocationProperty);
    }

    private void initUnderSelectionMarkerLayer() {
        this.mUnderSelectionMarkerLayer = new SGLayerImage(50.0f, 50.0f, (int) SupportMenu.CATEGORY_MASK);
        this.mUnderSelectionMarkerLayer.setGeometryGenerator(this.mUSMarkerGeometryGenerator);
        this.mUnderSelectionMarkerLayer.setTexture(this.mCitiesConfig.mMarkerTexture);
        this.mUnderSelectionMarkerLayer.setProperty("textureOffsets", this.mCitiesConfig.mTextureOffset);
        this.mUnderSelectionMarkerLayer.setProperty("geometryOffsets", this.mCitiesConfig.mGeometryOffset);
        this.mUnderSelectionMarkerLayer.setProperty("interpolationFactor", this.mCitiesConfig.mInterpolationFactor);
        this.mConfig.setupLayer(this.mUnderSelectionMarkerLayer, SGKeyCode.CODE_NUMPAD_SUBTRACT, "attribute float indexOfVertex;uniform vec4 underSelectionLocation;uniform mat4 ModelViewProjection;uniform vec3 camVector;uniform float alphaByLevels[6];uniform float pxInScreenX;uniform float pxInScreenY;uniform float textureOffsets[64];uniform float geometryOffsets[64];uniform float interpolationFactor;uniform float underSelectionID;uniform float underSelectionType;uniform bool isCustomTheme;varying vec2 textureCoords;varying float alpha;varying float recolor;void main(){   float anyUnderSelection = step(-0.5, underSelectionID);   alpha = (1.0 - smoothstep(0.2, 0.7, 1.0 - dot(camVector, underSelectionLocation.xyz))) * anyUnderSelection;   recolor = float(isCustomTheme);   if(alpha < 0.001){       gl_Position = vec4(100.0);       textureCoords = vec2(0.0);   }else{       float disAlpha = alpha*interpolationFactor;       int xIndex = int(underSelectionType * 16.0 + indexOfVertex *  2.0);       int yIndex = xIndex + 1;       textureCoords = vec2(textureOffsets[xIndex], textureOffsets[yIndex]);       vec4 mesh = ModelViewProjection*vec4(underSelectionLocation.xyz, 1.0);       mesh.z = 0.0;       mesh /= mesh.w;       mesh.x += (geometryOffsets[xIndex]*disAlpha)*pxInScreenX;       mesh.y += (geometryOffsets[yIndex]*disAlpha)*pxInScreenY;       gl_Position = mesh;   }}", "precision mediump float;uniform sampler2D SGTexture;uniform vec3 iconColor;varying vec2 textureCoords;varying float alpha;varying float recolor;void main(){    vec4 sceneColor = texture2D(SGTexture, textureCoords);    vec3 hue = sceneColor.rgb * (1.0 - recolor) + iconColor.rgb * (recolor);    hue /= sceneColor.a;    gl_FragColor = vec4(hue, sceneColor.a);    gl_FragColor.a *= alpha;}");
        this.mUnderSelectionMarkerLayer.setProperty("underSelectionID", this.mCitiesConfig.mUnderSelectionID);
        this.mUnderSelectionMarkerLayer.setProperty("underSelectionLocation", this.mUSLocationProperty);
        this.mUnderSelectionMarkerLayer.setProperty("underSelectionType", this.mUnderSelectionType);
        this.mUnderSelectionMarkerLayer.setProperty("iconColor", this.mCitiesConfig.mIconColorProperty);
        this.mUnderSelectionMarkerLayer.setProperty("isCustomTheme", this.mCitiesConfig.mIsCustomTheme);
    }

    public void createUnderSelectionGeometry(int ID) {
        if (ID < 0) {
            ID = 0;
        }
        City[] cities = CityManager.getCitiesByRawOffset();
        if (cities != null) {
            int index = 0;
            while (index < cities.length && cities[index].getUniqueId() != ID) {
                index++;
            }
            createUSTextGeometry(cities[index], index);
            createUSMarkerGeometry(cities[index]);
            this.mUnderSelectionTextLayer.setGeometryGenerator(this.mUSTextGeometryGenerator);
            this.mUnderSelectionMarkerLayer.setGeometryGenerator(this.mUSMarkerGeometryGenerator);
        }
    }

    private void createUSTextGeometry(City city, int index) {
        SGIndexBuffer currentTextIndexBuffer = new SGIndexBuffer(SGPrimitiveType.TRIANGLES, SGBufferUsageType.STATIC_DRAW, 6);
        ShortBuffer currentTextIndices = currentTextIndexBuffer.getShortBuffer();
        SGVertexBuffer currentTextPositionBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, 4);
        FloatBuffer currentTextPositions = currentTextPositionBuffer.getByteBuffer().asFloatBuffer();
        SGVertexBuffer currentTextTexcoordBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, 4);
        FloatBuffer currentTextTexcoord = currentTextTexcoordBuffer.getByteBuffer().asFloatBuffer();
        currentTextIndices.put((short) 0);
        currentTextIndices.put((short) 1);
        currentTextIndices.put((short) 2);
        currentTextIndices.put((short) 0);
        currentTextIndices.put((short) 2);
        currentTextIndices.put((short) 3);
        int markerAlignY = (city.getArrowDirection() >> 2) & 3;
        Paint boldPaint = new Paint();
        boldPaint.setAntiAlias(true);
        boldPaint.setTextAlign(Align.CENTER);
        boldPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 1));
        Resources resources = this.mContext.getResources();
        float underSelectionTextSize = resources.getDimension(C0836R.dimen.worldclock_under_selection_textsize);
        if (StateUtils.isContextInDexMode(this.mContext)) {
            underSelectionTextSize = resources.getDimension(C0836R.dimen.worldclock_dexmode_under_selection_textsize);
        }
        boldPaint.setTextSize(underSelectionTextSize);
        FontMetricsInt fontMetrics = boldPaint.getFontMetricsInt();
        int normalCurrentTop = -fontMetrics.top;
        this.US_TEXTURE_HEIGHT = fontMetrics.bottom - fontMetrics.top;
        String name = city.getName();
        Bitmap locationBitmap = Bitmap.createBitmap(SGICities.US_TEXTURE_WIDTH, this.US_TEXTURE_HEIGHT, Config.ALPHA_8);
        Canvas canvas = new Canvas(locationBitmap);
        canvas.drawText(name, ((float) SGICities.US_TEXTURE_WIDTH) / 2.0f, (float) normalCurrentTop, boldPaint);
        if (this.mCitiesConfig.mLowDensity) {
            canvas.drawText(name, ((float) SGICities.US_TEXTURE_WIDTH) / 2.0f, (float) normalCurrentTop, boldPaint);
        }
        this.mUnderSelectionTexture.setBitmap(locationBitmap, true);
        currentTextTexcoord.put(0.0f);
        currentTextTexcoord.put(0.0f);
        currentTextTexcoord.put(0.0f);
        currentTextTexcoord.put(1.0f);
        currentTextTexcoord.put(1.0f);
        currentTextTexcoord.put(1.0f);
        currentTextTexcoord.put(1.0f);
        currentTextTexcoord.put(0.0f);
        RectF relativePosition = new RectF();
        float centerX = this.mCitiesConfig.getUSRect(index).centerX();
        relativePosition.left = centerX - (((float) SGICities.US_TEXTURE_WIDTH) / 2.0f);
        relativePosition.right = (((float) SGICities.US_TEXTURE_WIDTH) / 2.0f) + centerX;
        if (StateUtils.isContextInDexMode(this.mContext)) {
            relativePosition.right -= 0.1f;
        }
        if (markerAlignY == 1) {
            relativePosition.bottom = (((float) this.mCitiesConfig.mMarkerNormalHeight) / 2.0f) + this.mCitiesConfig.mDeltaTextMarker;
            relativePosition.top = relativePosition.bottom + ((float) this.US_TEXTURE_HEIGHT);
        } else {
            relativePosition.top = -((((float) this.mCitiesConfig.mMarkerNormalHeight) / 2.0f) + this.mCitiesConfig.mDeltaTextMarker);
            relativePosition.bottom = relativePosition.top - ((float) this.US_TEXTURE_HEIGHT);
        }
        currentTextPositions.put(relativePosition.left);
        currentTextPositions.put(relativePosition.top);
        currentTextPositions.put(relativePosition.left);
        currentTextPositions.put(relativePosition.bottom);
        currentTextPositions.put(relativePosition.right);
        currentTextPositions.put(relativePosition.bottom);
        currentTextPositions.put(relativePosition.right);
        currentTextPositions.put(relativePosition.top);
        SGGeometry sGGeometry = new SGGeometry(currentTextIndexBuffer);
        sGGeometry.addBuffer(SGProperty.POSITIONS, currentTextPositionBuffer);
        sGGeometry.addBuffer(SGProperty.TEXTURE_COORDS, currentTextTexcoordBuffer);
        this.mUSTextGeometryGenerator = SGGeometryGeneratorFactory.createStaticGeometryGenerator(sGGeometry);
    }

    private void createUSMarkerGeometry(City city) {
        SGIndexBuffer currentMarkerIndexBuffer = new SGIndexBuffer(SGPrimitiveType.TRIANGLES, SGBufferUsageType.STATIC_DRAW, 6);
        ShortBuffer currentMarkerIndices = currentMarkerIndexBuffer.getShortBuffer();
        SGVertexBuffer currentIndexOfVertexBuffer = new SGVertexBuffer(1, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, 4);
        FloatBuffer currentIndexOfVertexInfo = currentIndexOfVertexBuffer.getByteBuffer().asFloatBuffer();
        currentMarkerIndices.put((short) 0);
        currentMarkerIndices.put((short) 1);
        currentMarkerIndices.put((short) 2);
        currentMarkerIndices.put((short) 0);
        currentMarkerIndices.put((short) 2);
        currentMarkerIndices.put((short) 3);
        float direction = ((city.getArrowDirection() >> 2) & 3) != 1 ? 0.0f : 4.0f;
        currentIndexOfVertexInfo.put(0.0f + direction);
        currentIndexOfVertexInfo.put(1.0f + direction);
        currentIndexOfVertexInfo.put(2.0f + direction);
        currentIndexOfVertexInfo.put(3.0f + direction);
        SGGeometry geometry = new SGGeometry(currentMarkerIndexBuffer);
        geometry.addBuffer("indexOfVertex", currentIndexOfVertexBuffer);
        this.mUSMarkerGeometryGenerator = SGGeometryGeneratorFactory.createStaticGeometryGenerator(geometry);
    }

    public void setSize(int width, int height) {
        this.mUnderSelectionTextLayer.setSize((float) width, (float) height);
    }

    public void updateAlphas(SGFloatArrayProperty alpha) {
        this.mUnderSelectionTextLayer.setProperty("alphaByLevels", alpha);
    }

    public void setType(float markerType) {
        this.mUnderSelectionType.set(markerType);
    }

    public void setUSLocation(float[] cityLoc) {
        this.mUSLocationProperty.set(cityLoc);
    }

    public void reload() {
        this.mUnderSelectionTextLayer.setTexture(this.mUnderSelectionTexture);
        this.mUnderSelectionTextLayer.setGeometryGenerator(this.mUSTextGeometryGenerator);
        this.mUnderSelectionMarkerLayer.setGeometryGenerator(this.mUSMarkerGeometryGenerator);
        this.mUnderSelectionMarkerLayer.setProperty("textureOffsets", this.mCitiesConfig.mTextureOffset);
        this.mUnderSelectionMarkerLayer.setProperty("geometryOffsets", this.mCitiesConfig.mGeometryOffset);
    }
}
