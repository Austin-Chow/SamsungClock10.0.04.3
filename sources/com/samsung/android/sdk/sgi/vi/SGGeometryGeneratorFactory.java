package com.samsung.android.sdk.sgi.vi;

import android.graphics.RectF;
import com.samsung.android.sdk.sgi.base.SGMathNative;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import java.util.Map;

public final class SGGeometryGeneratorFactory {
    public static SGGeometryGenerator createCircleGeometryGenerator(SGVector2f center, float radius, int density) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createCircleGeometryGenerator(center.getData(), radius, density);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createNurbsMorphingGeometryGenerator(SGNurbsSurface initialSurface, SGNurbsSurface finalSurface) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createNurbsMorphingGeometryGenerator(SGNurbsSurface.getCPtr(initialSurface), initialSurface, SGNurbsSurface.getCPtr(finalSurface), finalSurface);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createPoseGeometryGenerator(SGGeometry pose, Map<String, SGGeometry> targets) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createPoseGeometryGenerator(SGGeometry.getCPtr(pose), pose, targets);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createRectGeometryGenerator(RectF geometryRect, RectF textureRect) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createRectGeometryGenerator(SGMathNative.getArrayRect(geometryRect), SGMathNative.getArrayRect(textureRect));
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createRoundBorderGeometryGenerator(RectF geometryRect, float minRadius, float maxRadius, float minBorderWidth, float maxBorderWidth, int cornerDensity) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createRoundBorderGeometryGenerator(SGMathNative.getArrayRect(geometryRect), minRadius, maxRadius, minBorderWidth, maxBorderWidth, cornerDensity);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createRoundRectGeometryGenerator(RectF geometryRect, float minRadius, float maxRadius, int cornerDensity) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createRoundRectGeometryGenerator(SGMathNative.getArrayRect(geometryRect), minRadius, maxRadius, cornerDensity);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createStaticGeometryGenerator(SGGeometry geometry) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createStaticGeometryGenerator(SGGeometry.getCPtr(geometry), geometry);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createTextMorphingGeometryGenerator(String initialText, String finalText) {
        long cPtr = SGJNI.m36x31de2b8f(initialText, finalText);
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createTextMorphingGeometryGenerator(String initialText, String finalText, float textHeight, float charSapcing, String fontName, int color, SGMorphTextHorizontalAlignType horizontalAlignType, SGMorphTextVerticalAlignType verticalAlignType, SGGlyphType glyphType, SGGlyphMorphingType glyphMorphingType) {
        long cPtr = SGJNI.m37x31de2b90(initialText, finalText, textHeight, charSapcing, fontName, color, SGJNI.getData(horizontalAlignType), SGJNI.getData(verticalAlignType), SGJNI.getData(glyphType), SGJNI.getData(glyphMorphingType));
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }

    public static SGGeometryGenerator createTriangleGeometryGenerator(SGVector2f firstVertex, SGVector2f secondVertex, SGVector2f thirdVertex) {
        long cPtr = SGJNI.SGGeometryGeneratorFactory_createTriangleGeometryGenerator(firstVertex.getData(), secondVertex.getData(), thirdVertex.getData());
        return cPtr == 0 ? null : (SGGeometryGeneratorHolder) SGJNI.createObjectFromNativePtr(SGGeometryGeneratorHolder.class, cPtr, true);
    }
}
