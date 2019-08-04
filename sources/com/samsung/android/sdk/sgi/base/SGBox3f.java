package com.samsung.android.sdk.sgi.base;

import java.util.Arrays;

public class SGBox3f {
    private float[] mData;

    public SGBox3f() {
        this.mData = new float[6];
    }

    public SGBox3f(SGVector3f firstPoint, SGVector3f secondPoint) {
        this.mData = new float[6];
        setBox(firstPoint, secondPoint);
    }

    public SGBox3f(float[] data) {
        this.mData = new float[6];
        System.arraycopy(data, 0, this.mData, 0, 6);
    }

    public SGBox3f createTransformed(SGMatrix4f tran) {
        return SGSgfxBox3f.createTransformed(this, tran);
    }

    public void extend(SGBox3f other) {
        int i = 0;
        while (i < 3) {
            this.mData[i] = this.mData[i] < other.mData[i] ? this.mData[i] : other.mData[i];
            i++;
        }
        i = 3;
        while (i < 6) {
            this.mData[i] = this.mData[i] > other.mData[i] ? this.mData[i] : other.mData[i];
            i++;
        }
    }

    public float[] getData() {
        return this.mData;
    }

    public SGVector3f getMax() {
        return new SGVector3f(this.mData[3], this.mData[4], this.mData[5]);
    }

    public SGVector3f getMin() {
        return new SGVector3f(this.mData[0], this.mData[1], this.mData[2]);
    }

    public void resetToZero() {
        Arrays.fill(this.mData, 0.0f);
    }

    public void setBox(SGVector3f firstPoint, SGVector3f secondPoint) {
        if (firstPoint.getX() < secondPoint.getX()) {
            this.mData[0] = firstPoint.getX();
            this.mData[3] = secondPoint.getX();
        } else {
            this.mData[0] = secondPoint.getX();
            this.mData[3] = firstPoint.getX();
        }
        if (firstPoint.getY() < secondPoint.getY()) {
            this.mData[1] = firstPoint.getY();
            this.mData[4] = secondPoint.getY();
        } else {
            this.mData[1] = secondPoint.getY();
            this.mData[4] = firstPoint.getY();
        }
        if (firstPoint.getZ() < secondPoint.getZ()) {
            this.mData[2] = firstPoint.getZ();
            this.mData[5] = secondPoint.getZ();
            return;
        }
        this.mData[2] = secondPoint.getZ();
        this.mData[5] = firstPoint.getZ();
    }

    public void setMax(SGVector3f maximum) {
        this.mData[3] = maximum.getX();
        this.mData[4] = maximum.getY();
        this.mData[5] = maximum.getZ();
    }

    public void setMin(SGVector3f minimum) {
        this.mData[0] = minimum.getX();
        this.mData[1] = minimum.getY();
        this.mData[2] = minimum.getZ();
    }

    public String toString() {
        return "Box3f(" + this.mData[0] + ", " + this.mData[1] + ", " + this.mData[2] + " - " + this.mData[3] + ", " + this.mData[4] + ", " + this.mData[5] + ")";
    }
}
