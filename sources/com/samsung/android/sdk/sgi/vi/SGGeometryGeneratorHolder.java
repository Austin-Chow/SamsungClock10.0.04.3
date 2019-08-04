package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import java.util.Map;

final class SGGeometryGeneratorHolder extends SGGeometryGenerator {
    private SGGeometryGeneratorHolder(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    protected SGGeometry generate(float param, SGGeometry previousGeometry, float height, float width) {
        return (SGGeometry) SGJNI.createObjectFromNativePtr(SGGeometry.class, SGJNI.SGGeometryGenerator_generate__SWIG_0(this.swigCPtr, this, param, SGGeometry.getCPtr(previousGeometry), previousGeometry, height, width), true);
    }

    protected SGGeometry generate(float param, SGGeometry previousGeometry, float height, float width, Map<String, Float> poseParams) {
        return (SGGeometry) SGJNI.createObjectFromNativePtr(SGGeometry.class, SGJNI.SGGeometryGenerator_generate__SWIG_1(this.swigCPtr, this, param, SGGeometry.getCPtr(previousGeometry), previousGeometry, height, width, poseParams), true);
    }

    protected boolean isBelongsToGeometry(SGVector2f point) {
        return SGJNI.SGGeometryGenerator_isBelongsToGeometry(this.swigCPtr, this, point.getData());
    }
}
