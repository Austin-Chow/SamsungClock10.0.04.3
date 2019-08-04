package com.samsung.android.sdk.sgi.render;

import com.samsung.android.sdk.sgi.base.SGQuaternion;

public final class SGQuaternionArrayProperty extends SGArrayProperty {
    protected SGQuaternionArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public SGQuaternionArrayProperty(SGQuaternion[] array) {
        this(SGJNI.new_SGQuaternionArrayProperty(array), true);
    }

    public SGQuaternion[] get() {
        return SGJNI.SGQuaternionArrayProperty_get(this.swigCPtr, this);
    }

    public void set(SGQuaternion[] array) {
        SGJNI.SGQuaternionArrayProperty_set(this.swigCPtr, this, array);
    }
}
