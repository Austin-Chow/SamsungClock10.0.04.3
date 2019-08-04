package com.samsung.android.sdk.sgi.base;

import android.graphics.RectF;

public class SGVector4f {
    private float[] mData;

    public SGVector4f() {
        this.mData = new float[4];
    }

    public SGVector4f(float x, float y, float z, float w) {
        this.mData = new float[4];
        set(x, y, z, w);
    }

    public SGVector4f(RectF rect) {
        this.mData = new float[4];
        this.mData[0] = rect.left;
        this.mData[1] = rect.top;
        this.mData[2] = rect.right;
        this.mData[3] = rect.bottom;
    }

    public SGVector4f(SGVector4f other) {
        this.mData = new float[4];
        for (int i = 0; i < 4; i++) {
            this.mData[i] = other.mData[i];
        }
    }

    public SGVector4f(float[] data) {
        this.mData = new float[4];
        this.mData[0] = data[0];
        this.mData[1] = data[1];
        this.mData[2] = data[2];
        this.mData[3] = data[3];
    }

    public void add(SGVector4f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] + other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] + other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] + other.mData[2];
        fArr = this.mData;
        fArr[3] = fArr[3] + other.mData[3];
    }

    public void divide(SGVector4f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] / other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] / other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] / other.mData[2];
        fArr = this.mData;
        fArr[3] = fArr[3] / other.mData[3];
    }

    public RectF getAsRect() {
        return new RectF(this.mData[0], this.mData[1], this.mData[2], this.mData[3]);
    }

    public float[] getData() {
        return this.mData;
    }

    public float getDistance(SGVector4f other) {
        return (float) Math.sqrt((double) (((((this.mData[0] - other.mData[0]) * (this.mData[0] - other.mData[0])) + ((this.mData[1] - other.mData[1]) * (this.mData[1] - other.mData[1]))) + ((this.mData[2] - other.mData[2]) * (this.mData[2] - other.mData[2]))) + ((this.mData[3] - other.mData[3]) * (this.mData[3] - other.mData[3]))));
    }

    public float getDotProduct(SGVector4f other) {
        return (((this.mData[0] * other.mData[0]) + (this.mData[1] * other.mData[1])) + (this.mData[2] * other.mData[2])) + (this.mData[3] * other.mData[3]);
    }

    public float getLength() {
        return (float) Math.sqrt((double) ((((this.mData[0] * this.mData[0]) + (this.mData[1] * this.mData[1])) + (this.mData[2] * this.mData[2])) + (this.mData[3] * this.mData[3])));
    }

    public float getW() {
        return this.mData[3];
    }

    public float getX() {
        return this.mData[0];
    }

    public float getY() {
        return this.mData[1];
    }

    public float getZ() {
        return this.mData[2];
    }

    public void interpolate(SGVector4f to, float coeff) {
        this.mData[0] = this.mData[0] + ((to.mData[0] - this.mData[0]) * coeff);
        this.mData[1] = this.mData[1] + ((to.mData[1] - this.mData[1]) * coeff);
        this.mData[2] = this.mData[2] + ((to.mData[2] - this.mData[2]) * coeff);
        this.mData[3] = this.mData[3] + ((to.mData[3] - this.mData[3]) * coeff);
    }

    public void inverse() {
        this.mData[0] = -this.mData[0];
        this.mData[1] = -this.mData[1];
        this.mData[2] = -this.mData[2];
        this.mData[3] = -this.mData[3];
    }

    public boolean isEqual(SGVector4f other, float epsilon) {
        return Math.abs(other.mData[0] - this.mData[0]) <= epsilon && Math.abs(other.mData[1] - this.mData[1]) <= epsilon && Math.abs(other.mData[2] - this.mData[2]) <= epsilon && Math.abs(other.mData[3] - this.mData[3]) <= epsilon;
    }

    public void multiply(SGVector4f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] * other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] * other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] * other.mData[2];
        fArr = this.mData;
        fArr[3] = fArr[3] * other.mData[3];
    }

    public void normalize() {
        float len = getLength();
        if (len != 0.0f) {
            this.mData[0] = this.mData[0] / len;
            this.mData[1] = this.mData[1] / len;
            this.mData[2] = this.mData[2] / len;
            this.mData[3] = this.mData[3] / len;
        }
    }

    public void scale(float scale) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] * scale;
        fArr = this.mData;
        fArr[1] = fArr[1] * scale;
        fArr = this.mData;
        fArr[2] = fArr[2] * scale;
        fArr = this.mData;
        fArr[3] = fArr[3] * scale;
    }

    public void set(float x, float y, float z, float w) {
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
        this.mData[3] = w;
    }

    public void setW(float w) {
        this.mData[3] = w;
    }

    public void setX(float x) {
        this.mData[0] = x;
    }

    public void setY(float y) {
        this.mData[1] = y;
    }

    public void setZ(float z) {
        this.mData[2] = z;
    }

    public void subtract(SGVector4f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] - other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] - other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] - other.mData[2];
        fArr = this.mData;
        fArr[3] = fArr[3] - other.mData[3];
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Vector4f");
    }
}
