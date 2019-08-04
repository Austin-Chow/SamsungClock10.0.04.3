package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;

public final class SGMatrix4fProperty extends SGMatrixfProperty {
    public SGMatrix4fProperty() {
        super(4);
    }

    public SGMatrix4fProperty(SGMatrix4f value) {
        super(value);
    }

    public SGMatrix4f get() {
        return new SGMatrix4f(SGJNI.SGMatrix4fProperty_get(this.swigCPtr, this));
    }
}
