package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGVector3f;

@Deprecated
public final class SGVector3fArrayProperty extends SGArrayProperty {
    protected SGVector3fArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGVector3fArrayProperty(SGVector3f[] array) {
        this(SGJNI.new_SGVector3fArrayProperty(array), true);
    }

    public SGVector3f[] get() {
        return SGJNI.SGVector3fArrayProperty_get(this.swigCPtr, this);
    }

    public void set(SGVector3f[] array) {
        SGJNI.SGVector3fArrayProperty_set(this.swigCPtr, this, array);
    }
}
