package com.samsung.android.sdk.sgi.render;

public final class SGBatchingGroupProperty extends SGProperty {
    public SGBatchingGroupProperty(int id) {
        this(SGJNI.new_SGBatchingGroupProperty(id), true);
    }

    protected SGBatchingGroupProperty(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    public int getId() {
        return SGJNI.SGBatchingGroupProperty_getId(this.swigCPtr, this);
    }

    public boolean isBatchingGroupEnabled() {
        return SGJNI.SGBatchingGroupProperty_isBatchingGroupEnabled(this.swigCPtr, this);
    }

    public void setBatchingGroupEnabled(boolean enabled) {
        SGJNI.SGBatchingGroupProperty_setBatchingGroupEnabled(this.swigCPtr, this, enabled);
    }

    public void setId(int id) {
        SGJNI.SGBatchingGroupProperty_setId(this.swigCPtr, this, id);
    }
}
