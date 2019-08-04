package com.samsung.android.sdk.sgi.render;

public abstract class SGArrayProperty extends SGProperty {
    public SGArrayProperty() {
        this(SGJNI.new_SGArrayProperty(), true);
    }

    protected SGArrayProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public int getNumElements() {
        return SGJNI.SGArrayProperty_getNumElements(this.swigCPtr, this);
    }

    public void invalidate() {
        SGJNI.SGArrayProperty_invalidate(this.swigCPtr, this);
    }
}
