package com.samsung.android.sdk.sgi.base;

import android.graphics.Rect;

public class SGVector4i {
    private int[] mData;

    public SGVector4i() {
        this.mData = new int[4];
    }

    public SGVector4i(int x, int y, int z, int w) {
        this.mData = new int[4];
        set(x, y, z, w);
    }

    public SGVector4i(Rect rect) {
        this.mData = new int[4];
        this.mData[0] = rect.left;
        this.mData[1] = rect.top;
        this.mData[2] = rect.right;
        this.mData[3] = rect.bottom;
    }

    public SGVector4i(SGVector4i other) {
        this.mData = new int[4];
        for (int i = 0; i < 4; i++) {
            this.mData[i] = other.mData[i];
        }
    }

    public SGVector4i(int[] data) {
        this.mData = new int[4];
        this.mData[0] = data[0];
        this.mData[1] = data[1];
        this.mData[2] = data[2];
        this.mData[3] = data[3];
    }

    public Rect getAsRect() {
        return new Rect(this.mData[0], this.mData[1], this.mData[2], this.mData[3]);
    }

    public int[] getData() {
        return this.mData;
    }

    public int getW() {
        return this.mData[3];
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

    public void set(int x, int y, int z, int w) {
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
        this.mData[3] = w;
    }

    public void setW(int w) {
        this.mData[3] = w;
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
        return SGMathNative.arrayToString(this.mData, "Vector4i");
    }
}
