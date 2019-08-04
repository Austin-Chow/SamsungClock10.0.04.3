package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

@Deprecated
public final class SGVector4fProperty extends SGVectorfProperty {
    public SGVector4fProperty() {
        super(4);
    }

    public SGVector4fProperty(SGVector4f value) {
        super(value);
    }

    public SGVector4f get() {
        return new SGVector4f(SGJNI.SGVector4fProperty_get(this.swigCPtr, this));
    }

    public void set(SGVector2f value) {
        throw new IllegalArgumentException();
    }

    public void set(SGVector3f value) {
        throw new IllegalArgumentException();
    }
}
