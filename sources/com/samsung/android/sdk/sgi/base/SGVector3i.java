package com.samsung.android.sdk.sgi.base;

public class SGVector3i {
    private int[] mData;

    public SGVector3i() {
        this.mData = new int[3];
    }

    public SGVector3i(int x, int y) {
        this.mData = new int[3];
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = 0;
    }

    public SGVector3i(int x, int y, int z) {
        this.mData = new int[3];
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
    }

    public SGVector3i(SGVector3i other) {
        this.mData = new int[3];
        this.mData[0] = other.mData[0];
        this.mData[1] = other.mData[1];
        this.mData[2] = other.mData[2];
    }

    public SGVector3i(int[] data) {
        this.mData = new int[3];
        this.mData[0] = data[0];
        this.mData[1] = data[1];
        this.mData[2] = data[2];
    }

    public int[] getData() {
        return this.mData;
    }

    public int getX() {
        return this.mData[0];
    }

    public int getY() {
        return this.mData[1];
    }

    public int getZ() {
        return this.mData[2];
    }

    public void set(int x, int y, int z) {
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
    }

    public void setX(int x) {
        this.mData[0] = x;
    }

    public void setY(int y) {
        this.mData[1] = y;
    }

    public void setZ(int z) {
        this.mData[2] = z;
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Vector3i");
    }
}
