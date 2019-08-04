package com.sec.android.app.clockpackage.worldclock.sgi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGBuffer;
import com.samsung.android.sdk.sgi.render.SGBufferDataType;
import com.samsung.android.sdk.sgi.render.SGBufferUsageType;
import com.samsung.android.sdk.sgi.render.SGFloatArrayProperty;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGIndexBuffer;
import com.samsung.android.sdk.sgi.render.SGPrimitiveType;
import com.samsung.android.sdk.sgi.render.SGProperty;
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
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class SGICitiesTime {
    private final SGICities mCitiesConfig;
    private SGLayerImage mCitiesTimeDummyLayer;
    private SGGeometryGenerator mCitiesTimeGeometryGenerator;
    private SGLayerImage mCitiesTimeLayer;
    private final SGVectorfProperty mCityTimeColorProperty = new SGVectorfProperty(3);
    private final SGILayerConfig mConfig;
    private final Context mContext;
    private int mCurrentMinutes = -1;
    private boolean mCurrentTimeFormat;
    private int mNormalTimeTop;
    private SymbolData[] mSymbolData;
    private Paint mTimePaint;
    private String[] mTimeSymbols;

    protected SGICitiesTime(SGSurface surface, SGILayerConfig config, SGICities cities) {
        int cityTimeColor;
        this.mConfig = config;
        this.mCitiesConfig = cities;
        this.mContext = surface.getView().getContext();
        if (StateUtils.isContextInDexMode(this.mContext)) {
            cityTimeColor = this.mContext.getColor(C0836R.color.worldclock_dex_city_time_textcolor);
        } else {
            cityTimeColor = this.mContext.getColor(C0836R.color.worldclock_city_time_textcolor);
        }
        this.mCityTimeColorProperty.set(new SGVector3f(((float) Color.red(cityTimeColor)) / 255.0f, ((float) Color.green(cityTimeColor)) / 255.0f, ((float) Color.blue(cityTimeColor)) / 255.0f));
        initTimeLayers();
    }

    public void addLayers(SGSurface surface) {
        surface.addLayer(this.mCitiesTimeLayer);
        surface.addLayer(this.mCitiesTimeDummyLayer);
    }

    private void initTimeLayers() {
        this.mCitiesTimeLayer = new SGLayerImage();
        this.mCitiesTimeDummyLayer = new SGLayerImage();
        this.mCitiesTimeLayer.setGeometryGenerator(this.mCitiesTimeGeometryGenerator);
        this.mCitiesTimeDummyLayer.setGeometryGenerator(this.mCitiesTimeGeometryGenerator);
        this.mCitiesTimeLayer.setProperty(SGProperty.TEXTURE, this.mCitiesConfig.mTexture);
        this.mCitiesTimeDummyLayer.setProperty(SGProperty.TEXTURE, this.mCitiesConfig.mTexture);
        this.mCitiesTimeLayer.setProperty("globalScale", this.mCitiesConfig.mGlobalScale);
        this.mCitiesTimeDummyLayer.setProperty("globalScale", this.mCitiesConfig.mGlobalScale);
        this.mCitiesTimeLayer.setProperty("interpolationFactor", this.mCitiesConfig.mInterpolationFactor);
        this.mCitiesTimeDummyLayer.setProperty("interpolationFactor", this.mCitiesConfig.mMarkerSize);
        this.mConfig.setupLayer(this.mCitiesTimeLayer, SGKeyCode.CODE_NUMPAD_4, null, null);
        this.mConfig.setupLayer(this.mCitiesTimeDummyLayer, SGKeyCode.CODE_NUMPAD_4, null, null);
        this.mCitiesTimeLayer.setProgramProperty(this.mCitiesConfig.mCitiesTextProgram);
        this.mCitiesTimeDummyLayer.setProgramProperty(this.mCitiesConfig.mCitiesTextProgram);
        this.mCitiesTimeLayer.setProperty("dummyFlag", this.mCitiesConfig.mCitiesFlag);
        this.mCitiesTimeDummyLayer.setProperty("dummyFlag", this.mCitiesConfig.mDummyFlag);
        this.mCitiesTimeLayer.setProperty("cityTextColor", this.mCityTimeColorProperty);
        this.mCitiesTimeDummyLayer.setProperty("cityTextColor", this.mCityTimeColorProperty);
        this.mCitiesTimeLayer.setProperty("underSelectionID", this.mCitiesConfig.mUnderSelectionID);
        this.mCitiesTimeDummyLayer.setProperty("underSelectionID", this.mCitiesConfig.mUnderSelectionID);
    }

    public void createTimeSymbolsString() {
        int i = 0;
        this.mTimeSymbols = new String[14];
        NumberFormat nf = NumberFormat.getInstance();
        int frontIndex = 0;
        while (frontIndex < 10) {
            this.mTimeSymbols[frontIndex] = nf.format((long) frontIndex);
            frontIndex++;
        }
        String[] strArr = Constants.TIME_DELIMITERS;
        int length = strArr.length;
        int i2 = 0;
        int frontIndex2 = frontIndex;
        while (i2 < length) {
            frontIndex = frontIndex2 + 1;
            this.mTimeSymbols[frontIndex2] = strArr[i2];
            i2++;
            frontIndex2 = frontIndex;
        }
        String[] ampm = new DateFormatSymbols().getAmPmStrings();
        i2 = ampm.length;
        while (i < i2) {
            frontIndex = frontIndex2 + 1;
            this.mTimeSymbols[frontIndex2] = ampm[i];
            i++;
            frontIndex2 = frontIndex;
        }
        this.mSymbolData = new SymbolData[14];
    }

    public void calculateTimeSymbols(int xPos, int yPos) {
        int i;
        Resources resources = this.mContext.getResources();
        this.mTimePaint = new Paint();
        this.mTimePaint.setAntiAlias(true);
        this.mTimePaint.setTextAlign(Align.LEFT);
        this.mTimePaint.setTypeface(Typeface.DEFAULT);
        float timeSize = resources.getDimension(C0836R.dimen.worldclock_city_timesize);
        if (StateUtils.isContextInDexMode(this.mContext)) {
            timeSize = resources.getDimension(C0836R.dimen.worldclock_dexmode_city_timesize);
        }
        this.mTimePaint.setTextSize(timeSize);
        String all = "";
        for (i = 0; i < 14; i++) {
            all = all + this.mTimeSymbols[i];
        }
        Rect textBounds = new Rect();
        this.mTimePaint.getTextBounds(all, 0, all.length(), textBounds);
        int initialTimeSize = textBounds.height();
        this.mNormalTimeTop = -textBounds.top;
        this.mCitiesConfig.mTimeHeightInAtlas = initialTimeSize + 4;
        for (i = 0; i < 14; i++) {
            this.mTimePaint.getTextBounds(this.mTimeSymbols[i], 0, this.mTimeSymbols[i].length(), textBounds);
            int widthInAtlas = textBounds.width() + 4;
            xPos += widthInAtlas;
            if (xPos >= this.mCitiesConfig.TEXTURE_ATLAS_WIDTH) {
                yPos += this.mCitiesConfig.mTimeHeightInAtlas;
                xPos = widthInAtlas;
            }
            this.mSymbolData[i] = new SymbolData();
            this.mSymbolData[i].contentRect = new Rect(xPos - widthInAtlas, yPos - this.mCitiesConfig.mTimeHeightInAtlas, xPos, yPos);
            this.mSymbolData[i].drawOffsetX = textBounds.left;
            this.mSymbolData[i].width = (int) this.mTimePaint.measureText(this.mTimeSymbols[i]);
        }
        this.mCitiesConfig.TEXTURE_ATLAS_HEIGHT = yPos;
    }

    public void drawTimeSymbols(Canvas canvas) {
        for (int i = 0; i < 14; i++) {
            canvas.drawText(this.mTimeSymbols[i], (float) ((this.mSymbolData[i].contentRect.left - this.mSymbolData[i].drawOffsetX) + 2), (float) ((this.mSymbolData[i].contentRect.top + this.mNormalTimeTop) + 2), this.mTimePaint);
            if (this.mCitiesConfig.mLowDensity) {
                canvas.drawText(this.mTimeSymbols[i], (float) ((this.mSymbolData[i].contentRect.left - this.mSymbolData[i].drawOffsetX) + 2), (float) ((this.mSymbolData[i].contentRect.top + this.mNormalTimeTop) + 2), this.mTimePaint);
            }
        }
    }

    public void createCityTimeGeometry(City[] cities) {
        this.mCurrentMinutes = (int) (System.currentTimeMillis() / 60000);
        this.mCurrentTimeFormat = DateFormat.is24HourFormat(this.mContext);
        int count = cities.length;
        int symbols = 5;
        if (!DateFormat.is24HourFormat(this.mContext)) {
            symbols = 5 + 2;
        }
        SGIndexBuffer citiesTimeIndexBuffer = new SGIndexBuffer(SGPrimitiveType.TRIANGLES, SGBufferUsageType.STATIC_DRAW, (symbols * 6) * count);
        ShortBuffer citiesTimeIndices = citiesTimeIndexBuffer.getShortBuffer();
        SGVertexBuffer citiesTimeLocationBuffer = new SGVertexBuffer(4, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, (symbols * 4) * count);
        FloatBuffer citiesTimeLocations = citiesTimeLocationBuffer.getByteBuffer().asFloatBuffer();
        SGVertexBuffer citiesTimePositionBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, (symbols * 4) * count);
        FloatBuffer citiesTimePositions = citiesTimePositionBuffer.getByteBuffer().asFloatBuffer();
        SGVertexBuffer citiesTimeTexcoordBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, (symbols * 4) * count);
        FloatBuffer citiesTimeTexcoord = citiesTimeTexcoordBuffer.getByteBuffer().asFloatBuffer();
        SGBuffer sGVertexBuffer = new SGVertexBuffer(1, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, (symbols * 4) * count);
        FloatBuffer timeCityId = sGVertexBuffer.getByteBuffer().asFloatBuffer();
        int[] timeSymbols = new int[symbols];
        Calendar localTime = new GregorianCalendar();
        SGVector2f timeCenter = new SGVector2f();
        for (int i = 0; i < count; i++) {
            int j;
            int tmp = (i * symbols) * 4;
            for (j = 0; j < symbols; j++) {
                int tm = j * 4;
                citiesTimeIndices.put((short) (tmp + tm));
                citiesTimeIndices.put((short) ((tmp + tm) + 1));
                citiesTimeIndices.put((short) ((tmp + tm) + 2));
                citiesTimeIndices.put((short) (tmp + tm));
                citiesTimeIndices.put((short) ((tmp + tm) + 2));
                citiesTimeIndices.put((short) ((tmp + tm) + 3));
            }
            int ID = cities[i].getUniqueId();
            for (j = 0; j < symbols * 4; j++) {
                citiesTimeLocations.put(this.mCitiesConfig.mBillboardData[i].f27x);
                citiesTimeLocations.put(this.mCitiesConfig.mBillboardData[i].f28y);
                citiesTimeLocations.put(this.mCitiesConfig.mBillboardData[i].f29z);
                citiesTimeLocations.put(this.mCitiesConfig.mBillboardData[i].zoom);
                timeCityId.put((float) ID);
            }
            localTime.setTimeZone(this.mCitiesConfig.mBillboardData[i].timeZone);
            int hr = localTime.get(11);
            int h = hr / 10;
            int r = hr % 10;
            int mn = localTime.get(12);
            int numbersShift = 0;
            if (!this.mCurrentTimeFormat) {
                if (hr > 12) {
                    hr -= 12;
                }
                if (hr == 0) {
                    hr = 12;
                }
                if (hr == 12 && Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
                    hr = 0;
                }
                int amPmIndex = 12;
                if (localTime.get(9) == 1) {
                    amPmIndex = 12 + 1;
                }
                if ((StateUtils.isLeftAmPm() ^ StateUtils.isRtl()) != 0) {
                    timeSymbols[0] = amPmIndex;
                    timeSymbols[1] = 11;
                    numbersShift = 2;
                } else {
                    timeSymbols[5] = 11;
                    timeSymbols[6] = amPmIndex;
                }
                h = hr / 10;
                if (h == 0) {
                    h = 11;
                }
                r = hr % 10;
            }
            timeSymbols[numbersShift] = h;
            timeSymbols[numbersShift + 1] = r;
            timeSymbols[numbersShift + 2] = 10;
            timeSymbols[numbersShift + 3] = mn / 10;
            timeSymbols[numbersShift + 4] = mn % 10;
            timeCenter.set(this.mCitiesConfig.mBillboardData[i].textRect.centerX(), (this.mCitiesConfig.mBillboardData[i].textRect.bottom - (((float) this.mCitiesConfig.mTimeHeightInAtlas) / 2.0f)) - this.mCitiesConfig.mDeltaTextTime);
            fillTimeBuffers(citiesTimeTexcoord, timeSymbols, citiesTimePositions, timeCenter);
        }
        SGGeometry sGGeometry = new SGGeometry(citiesTimeIndexBuffer);
        sGGeometry.addBuffer("location", citiesTimeLocationBuffer);
        sGGeometry.addBuffer(SGProperty.POSITIONS, citiesTimePositionBuffer);
        sGGeometry.addBuffer(SGProperty.TEXTURE_COORDS, citiesTimeTexcoordBuffer);
        sGGeometry.addBuffer("cityId", sGVertexBuffer);
        this.mCitiesTimeGeometryGenerator = SGGeometryGeneratorFactory.createStaticGeometryGenerator(sGGeometry);
        this.mCitiesTimeLayer.setGeometryGenerator(this.mCitiesTimeGeometryGenerator);
        this.mCitiesTimeDummyLayer.setGeometryGenerator(this.mCitiesTimeGeometryGenerator);
    }

    private void fillTimeBuffers(FloatBuffer textureBuffer, int[] number, FloatBuffer positionBuffer, SGVector2f timeCenter) {
        int index;
        int i;
        int symbols = number.length;
        for (int index2 : number) {
            float uLeft = ((float) this.mSymbolData[index2].contentRect.left) / ((float) this.mCitiesConfig.TEXTURE_ATLAS_WIDTH);
            float uRight = ((float) this.mSymbolData[index2].contentRect.right) / ((float) this.mCitiesConfig.TEXTURE_ATLAS_WIDTH);
            float vTop = ((float) this.mSymbolData[index2].contentRect.top) / ((float) this.mCitiesConfig.TEXTURE_ATLAS_HEIGHT);
            float vBottom = ((float) this.mSymbolData[index2].contentRect.bottom) / ((float) this.mCitiesConfig.TEXTURE_ATLAS_HEIGHT);
            textureBuffer.put(uLeft);
            textureBuffer.put(vTop);
            textureBuffer.put(uLeft);
            textureBuffer.put(vBottom);
            textureBuffer.put(uRight);
            textureBuffer.put(vBottom);
            textureBuffer.put(uRight);
            textureBuffer.put(vTop);
        }
        SGVector2f[] symbolSizes = new SGVector2f[symbols];
        float cursorX = timeCenter.getX();
        float cursorY = timeCenter.getY();
        for (i = 0; i < symbols; i++) {
            symbolSizes[i] = new SGVector2f((float) this.mSymbolData[number[i]].width, (float) this.mSymbolData[number[i]].contentRect.height());
        }
        float timeHeight = 0.0f;
        float timeWidth = 4.0f;
        for (i = 0; i < symbols; i++) {
            timeWidth += symbolSizes[i].getX();
            if (symbolSizes[i].getY() > timeHeight) {
                timeHeight = symbolSizes[i].getY();
            }
        }
        cursorX -= timeWidth / 2.0f;
        cursorY -= timeHeight / 2.0f;
        for (i = 0; i < symbols; i++) {
            index2 = number[i];
            cursorX += (float) this.mSymbolData[index2].drawOffsetX;
            positionBuffer.put(cursorX);
            positionBuffer.put(cursorY + timeHeight);
            positionBuffer.put(cursorX);
            positionBuffer.put(cursorY);
            cursorX += (float) this.mSymbolData[index2].contentRect.width();
            positionBuffer.put(cursorX);
            positionBuffer.put(cursorY);
            positionBuffer.put(cursorX);
            positionBuffer.put(cursorY + timeHeight);
            cursorX += symbolSizes[i].getX() - ((float) (this.mSymbolData[index2].drawOffsetX + this.mSymbolData[index2].contentRect.width()));
        }
    }

    public void setSize(int width, int height) {
        this.mCitiesTimeLayer.setSize((float) width, (float) height);
        this.mCitiesTimeDummyLayer.setSize((float) width, (float) height);
    }

    public void updateAlphas(SGFloatArrayProperty alpha) {
        this.mCitiesTimeLayer.setProperty("alphaByLevels", alpha);
    }

    public void updateFadeOutAlphas(SGFloatArrayProperty dummyAlpha) {
        this.mCitiesTimeDummyLayer.setProperty("alphaByLevels", dummyAlpha);
    }

    protected void updateCityTime() {
        int currentMinutes = (int) (System.currentTimeMillis() / 60000);
        boolean currentTimeFormat = DateFormat.is24HourFormat(this.mContext);
        if (this.mCurrentMinutes != currentMinutes || this.mCurrentTimeFormat != currentTimeFormat) {
            City[] cities = CityManager.getCitiesByRawOffset();
            if (cities != null) {
                createCityTimeGeometry(cities);
            }
        }
    }

    public void setDummyVisibility(boolean visibility) {
        this.mCitiesTimeDummyLayer.setVisibility(visibility);
    }

    public void reload() {
        this.mCitiesTimeLayer.setProperty(SGProperty.TEXTURE, this.mCitiesConfig.mTexture);
        this.mCitiesTimeDummyLayer.setProperty(SGProperty.TEXTURE, this.mCitiesConfig.mTexture);
        this.mCitiesTimeLayer.setGeometryGenerator(this.mCitiesTimeGeometryGenerator);
        this.mCitiesTimeDummyLayer.setGeometryGenerator(this.mCitiesTimeGeometryGenerator);
    }
}
