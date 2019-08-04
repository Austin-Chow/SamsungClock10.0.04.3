package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;
import com.samsung.android.sdk.sgi.base.SGVector4f;

@Deprecated
public final class SGVector3fProperty extends SGVectorfProperty {
    public SGVector3fProperty() {
        super(3);
    }

    public SGVector3fProperty(SGVector3f value) {
        super(value);
    }

    public SGVector3f get() {
        return new SGVector3f(SGJNI.SGVector3fProperty_get(this.swigCPtr, this));
    }

    public void set(SGVector2f value) {
        throw new IllegalArgumentException();
    }

    public void set(SGVector4f value) {
        throw new IllegalArgumentException();
    }
}
