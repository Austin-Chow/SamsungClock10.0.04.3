package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

@Deprecated
public final class SGVector2fProperty extends SGVectorfProperty {
    public SGVector2fProperty() {
        super(2);
    }

    public SGVector2fProperty(SGVector2f value) {
        super(value);
    }

    public SGVector2f get() {
        return new SGVector2f(SGJNI.SGVector2fProperty_get(this.swigCPtr, this));
    }

    public void set(SGVector3f value) {
        throw new IllegalArgumentException();
    }

    public void set(SGVector4f value) {
        throw new IllegalArgumentException();
    }
}
