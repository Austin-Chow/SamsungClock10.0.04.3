package com.samsung.android.sdk.sgi.base;

public class SGRay {
    private float[] data = new float[6];
    private boolean mIsGeometryIntersect = false;
    private long mObjectVisibilityMask = -1;

    public SGVector3f getDirection() {
        return new SGVector3f(this.data[3], this.data[4], this.data[5]);
    }

    public long getObjectVisibilityMask() {
        return this.mObjectVisibilityMask;
    }

    public SGVector3f getOrigin() {
        return new SGVector3f(this.data[0], this.data[1], this.data[2]);
    }

    @Deprecated
    public long getVisibilityMask() {
        return getObjectVisibilityMask();
    }

    public boolean isIntersectGeometryEnabled() {
        return this.mIsGeometryIntersect;
    }

    public void setDirection(SGVector3f direction) {
        this.data[3] = direction.getX();
        this.data[4] = direction.getY();
        this.data[5] = direction.getZ();
    }

    public void setIntersectGeometryEnabled(boolean enabled) {
        this.mIsGeometryIntersect = enabled;
    }

    public void setObjectVisibilityMask(long mask) {
        this.mObjectVisibilityMask = mask;
    }

    public void setOrigin(SGVector3f origin) {
        this.data[0] = origin.getX();
        this.data[1] = origin.getY();
        this.data[2] = origin.getZ();
    }

    @Deprecated
    public void setVisibilityMask(long mask) {
        setObjectVisibilityMask(mask);
    }
}
