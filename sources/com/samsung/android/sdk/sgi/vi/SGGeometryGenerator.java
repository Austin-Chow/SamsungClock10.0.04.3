package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGGeometry;
import java.util.Map;

public abstract class SGGeometryGenerator {
    protected boolean swigCMemOwn;
    protected long swigCPtr;

    protected SGGeometryGenerator() {
        this(SGJNI.new_SGGeometryGenerator(), true);
        SGJNI.SGGeometryGenerator_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    protected SGGeometryGenerator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
        SGMemoryRegistrator.getInstance().Register(this, this.swigCPtr);
    }

    public static long getCPtr(SGGeometryGenerator obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGGeometryGenerator(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    protected abstract SGGeometry generate(float f, SGGeometry sGGeometry, float f2, float f3);

    protected SGGeometry generate(float param, SGGeometry previousGeometry, float height, float width, Map<String, Float> map) {
        return generate(param, previousGeometry, height, width);
    }

    public final void invalidate() {
        SGJNI.SGGeometryGenerator_invalidate(this.swigCPtr, this);
    }

    protected abstract boolean isBelongsToGeometry(SGVector2f sGVector2f);
}
