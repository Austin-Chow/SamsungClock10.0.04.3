package com.sec.android.app.clockpackage.worldclock.sgi;

import android.content.Context;
import android.graphics.Color;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.render.SGBuffer;
import com.samsung.android.sdk.sgi.render.SGBufferDataType;
import com.samsung.android.sdk.sgi.render.SGBufferUsageType;
import com.samsung.android.sdk.sgi.render.SGFloatProperty;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import com.samsung.android.sdk.sgi.render.SGIndexBuffer;
import com.samsung.android.sdk.sgi.render.SGPrimitiveType;
import com.samsung.android.sdk.sgi.render.SGProperty;
import com.samsung.android.sdk.sgi.render.SGVectorfProperty;
import com.samsung.android.sdk.sgi.render.SGVertexBuffer;
import com.samsung.android.sdk.sgi.vi.SGGeometryGeneratorFactory;
import com.samsung.android.sdk.sgi.vi.SGLayerImage;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Calendar;
import java.util.TimeZone;

public class SGIGlobeLayer extends SGLayerImage {
    private Calendar mCalendar;
    private int mDay;
    private int mHour;
    private final SGVectorfProperty mLightDirProperty = new SGVectorfProperty(3);
    private final float[] mLightDirection = new float[3];
    private int mMinute;

    protected SGIGlobeLayer(SGILayerConfig config, Context context) {
        int rings = 50;
        int sectors = 50;
        if (Feature.isTablet(context) && config.mDPI <= 240) {
            rings = 80;
            sectors = 80;
        }
        setGeometryGenerator(SGGeometryGeneratorFactory.createStaticGeometryGenerator(generateSphere(rings, sectors, 1.0f)));
        SGFloatProperty darkOverlayCrossOver = new SGFloatProperty();
        darkOverlayCrossOver.set(4.77f);
        setProperty("overlayCrossOver", darkOverlayCrossOver);
        setProperty("lightDirection", this.mLightDirProperty);
        config.setupLayer(this, 51, "attribute vec3 SGPositions;attribute vec3 SGNormals;attribute vec2 SGTextureCoords;uniform mat4 ModelViewProjection;varying vec2 vTextureCoords;varying vec3 normal;void main(){    vTextureCoords = SGTextureCoords;    gl_Position = ModelViewProjection * vec4(SGPositions, 1.0);    normal = SGNormals;}", "precision mediump float;uniform sampler2D SGTexture;uniform vec3 landColor;uniform vec3 seaColor;uniform vec3 nightColor;uniform vec3 lightDirection;uniform float cameraDistance;uniform float overlayCrossOver;varying vec2 vTextureCoords;varying vec3 normal;void main(){    vec4 dayColor = texture2D(SGTexture, vTextureCoords);    vec3 stepColor = step(0.5, dayColor.rgb);    float continents = clamp((stepColor.r + stepColor.g + stepColor.b), 0.0, 1.0);    vec4 mainLandColor = continents*vec4(landColor.xyz, 1.0);    vec4 finalColor = mix(vec4(seaColor.xyz, 1.0), mainLandColor, continents);    float nightValue = smoothstep(0.2, 0.5, dot(normal, lightDirection) + 0.3);    float darkOverlayFactor = 0.0;    if(cameraDistance < overlayCrossOver){        darkOverlayFactor = 1.0 - smoothstep(overlayCrossOver - 0.5, overlayCrossOver, cameraDistance);    }    nightValue += darkOverlayFactor;    nightValue = clamp(nightValue, 0.0, 1.0);    float shadowAlpha = 0.15;    gl_FragColor = vec4(mix(finalColor.rgb, mix(nightColor, finalColor.rgb, nightValue), shadowAlpha), 1.0);}");
        int landColor = context.getColor(C0836R.color.worldclock_globe_land_color);
        SGVectorfProperty landColorProperty = new SGVectorfProperty(3);
        landColorProperty.set(new SGVector3f(((float) Color.red(landColor)) / 255.0f, ((float) Color.green(landColor)) / 255.0f, ((float) Color.blue(landColor)) / 255.0f));
        setProperty("landColor", landColorProperty);
        int seaColor = context.getColor(C0836R.color.worldclock_globe_sea_color);
        SGVectorfProperty seaColorProperty = new SGVectorfProperty(3);
        seaColorProperty.set(new SGVector3f(((float) Color.red(seaColor)) / 255.0f, ((float) Color.green(seaColor)) / 255.0f, ((float) Color.blue(seaColor)) / 255.0f));
        setProperty("seaColor", seaColorProperty);
        int nightColor = context.getColor(C0836R.color.worldclock_globe_night_color);
        SGVectorfProperty nightColorProperty = new SGVectorfProperty(3);
        nightColorProperty.set(new SGVector3f(((float) Color.red(nightColor)) / 255.0f, ((float) Color.green(nightColor)) / 255.0f, ((float) Color.blue(nightColor)) / 255.0f));
        setProperty("nightColor", nightColorProperty);
        Constants.BG_COLOR = context.getColor(C0836R.color.worldclock_globe_space_color);
        onResume();
    }

    protected void setSize(int width, int height) {
        super.setSize((float) width, (float) height);
    }

    public void onResume() {
        this.mMinute = -1;
        this.mCalendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/Greenwich"));
        updateNightImage();
    }

    protected void updateNightImage() {
        int h = this.mCalendar.get(11);
        int m = this.mCalendar.get(12);
        int d = this.mCalendar.get(6);
        if (m != this.mMinute || h != this.mHour || d != this.mDay) {
            this.mMinute = m;
            this.mHour = h;
            this.mDay = d;
            float vRotation = -0.44f * ((float) Math.sin((double) (((((float) d) / 365.0f) * 6.28f) - 1.57f)));
            SGILayerConfig.rotationToDirection(this.mLightDirection, 6.28f * (1.0f - ((((float) h) + (((float) m) / 60.0f)) / 24.0f)), vRotation);
            this.mLightDirProperty.set(this.mLightDirection);
        }
    }

    private static SGGeometry generateSphere(int rings, int sectors, float radius) {
        int s;
        rings = Math.max(rings, 5);
        sectors = Math.max(sectors, 5);
        if (sectors % 2 != 0) {
            sectors++;
        }
        float R = 1.0f / ((float) (rings - 1));
        float S = 1.0f / ((float) sectors);
        sectors++;
        SGIndexBuffer indexBuffer = new SGIndexBuffer(SGPrimitiveType.TRIANGLE_STRIP, SGBufferUsageType.STATIC_DRAW, (rings * sectors) * 2);
        ShortBuffer indices = indexBuffer.getShortBuffer();
        indices.put((short) 0);
        for (s = 1; s < sectors - 1; s += 2) {
            int r = 1;
            while (r < rings - 1) {
                indices.put((short) ((r * sectors) + (s + 1)));
                indices.put((short) ((r * sectors) + s));
                r++;
            }
            while (r >= 0) {
                indices.put((short) ((r * sectors) + (s - 1)));
                indices.put((short) ((r * sectors) + s));
                r--;
            }
        }
        SGVertexBuffer positionBuffer = new SGVertexBuffer(3, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, rings * sectors);
        FloatBuffer positions = positionBuffer.getByteBuffer().asFloatBuffer();
        positions.clear();
        SGVertexBuffer normalBuffer = new SGVertexBuffer(3, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, rings * sectors);
        FloatBuffer normals = normalBuffer.getByteBuffer().asFloatBuffer();
        normals.clear();
        SGBuffer sGVertexBuffer = new SGVertexBuffer(2, SGBufferDataType.FLOAT, SGBufferUsageType.STATIC_DRAW, rings * sectors);
        FloatBuffer texcoord = sGVertexBuffer.getByteBuffer().asFloatBuffer();
        texcoord.clear();
        for (r = 0; r < rings; r++) {
            for (s = 0; s < sectors; s++) {
                float y = (float) Math.sin((double) (((-1078530011) * 0.5f) + ((((float) r) * 3.1415927f) * R)));
                float x = ((float) Math.cos((double) (((2.0f * 3.1415927f) * ((float) s)) * S))) * ((float) Math.sin((double) ((((float) r) * 3.1415927f) * R)));
                float z = ((float) Math.sin((double) (((2.0f * 3.1415927f) * ((float) s)) * S))) * ((float) Math.sin((double) ((((float) r) * 3.1415927f) * R)));
                texcoord.put(((float) s) * S);
                texcoord.put(((float) r) * R);
                positions.put((x * radius) * -1.0f);
                positions.put((y * radius) * -1.0f);
                positions.put(z * radius);
                normals.put(x);
                normals.put(y);
                normals.put(z);
            }
        }
        SGGeometry geometry = new SGGeometry(indexBuffer);
        geometry.addBuffer(SGProperty.TEXTURE_COORDS, sGVertexBuffer);
        geometry.addBuffer(SGProperty.POSITIONS, positionBuffer);
        geometry.addBuffer(SGProperty.NORMALS, normalBuffer);
        return geometry;
    }
}
