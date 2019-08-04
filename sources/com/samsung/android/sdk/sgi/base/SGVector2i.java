package com.samsung.android.sdk.sgi.base;

public class SGVector2i {
    private int[] mData;

    public SGVector2i() {
        this.mData = new int[2];
    }

    public SGVector2i(int x, int y) {
        this.mData = new int[2];
        this.mData[0] = x;
        this.mData[1] = y;
    }

    public SGVector2i(SGVector2i other) {
        this.mData = new int[2];
        this.mData[0] = other.mData[0];
        this.mData[1] = other.mData[1];
    }

    public SGVector2i(int[] data) {
        this.mData = new int[2];
        this.mData[0] = data[0];
        this.mData[1] = data[1];
    }

    public void add(SGVector2i other) {
        int[] iArr = this.mData;
        iArr[0] = iArr[0] + other.mData[0];
        iArr = this.mData;
        iArr[1] = iArr[1] + other.mData[1];
    }

    public int distanceSqr(SGVector2i other) {
        return ((this.mData[0] - other.mData[0]) * (this.mData[0] - other.mData[0])) + ((this.mData[1] - other.mData[1]) * (this.mData[1] - other.mData[1]));
    }

    public void divide(SGVector2i other) {
        int[] iArr = this.mData;
        iArr[0] = iArr[0] / other.mData[0];
        iArr = this.mData;
        iArr[1] = iArr[1] / other.mData[1];
    }

    public int[] getData() {
        return this.mData;
    }

    public float getDistance(SGVector2i other) {
        return (float) Math.sqrt((double) distanceSqr(other));
    }

    public int getDotProduct(SGVector2i other) {
        return (this.mData[0] * other.mData[0]) + (this.mData[1] * other.mData[1]);
    }

    public float getLength() {
        return (float) Math.sqrt((double) ((this.mData[0] * this.mData[0]) + (this.mData[1] * this.mData[1])));
    }

    public int getX() {
        return this.mData[0];
    }

    public int getY() {
        return this.mData[1];
    }

    public void inverse() {
        this.mData[0] = -this.mData[0];
        this.mData[1] = -this.mData[1];
    }

    public boolean isEqual(SGVector2i other) {
        return other.mData[0] == this.mData[0] && other.mData[1] == this.mData[1];
    }

    public void multiply(SGVector2i other) {
        int[] iArr = this.mData;
        iArr[0] = iArr[0] * other.mData[0];
        iArr = this.mData;
        iArr[1] = iArr[1] * other.mData[1];
    }

    public void scale(int scale) {
        this.mData[0] = this.mData[0] * scale;
        this.mData[1] = this.mData[1] * scale;
    }

    public void set(int x, int y) {
        this.mData[0] = x;
        this.mData[1] = y;
    }

    public void setAt(int index, int value) {
        if (index < 2 && index >= 0) {
            this.mData[index] = value;
        }
    }

    public void setX(int x) {
        this.mData[0] = x;
    }

    public void setY(int y) {
        this.mData[1] = y;
    }

    public void subtract(SGVector2i other) {
        int[] iArr = this.mData;
        iArr[0] = iArr[0] - other.mData[0];
        iArr = this.mData;
        iArr[1] = iArr[1] - other.mData[1];
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Vector2i");
    }
}
