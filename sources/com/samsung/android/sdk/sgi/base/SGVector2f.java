package com.samsung.android.sdk.sgi.base;

public final class SGVector2f {
    private float[] mData;

    public SGVector2f() {
        this.mData = new float[2];
    }

    public SGVector2f(float x, float y) {
        this.mData = new float[2];
        this.mData[0] = x;
        this.mData[1] = y;
    }

    public SGVector2f(SGVector2f other) {
        this.mData = new float[2];
        this.mData[0] = other.mData[0];
        this.mData[1] = other.mData[1];
    }

    public SGVector2f(float[] data) {
        this.mData = new float[2];
        this.mData[0] = data[0];
        this.mData[1] = data[1];
    }

    public void add(SGVector2f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] + other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] + other.mData[1];
    }

    public void divide(SGVector2f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] / other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] / other.mData[1];
    }

    public float[] getData() {
        return this.mData;
    }

    public float getDistance(SGVector2f other) {
        return (float) Math.sqrt((double) (((this.mData[0] - other.mData[0]) * (this.mData[0] - other.mData[0])) + ((this.mData[1] - other.mData[1]) * (this.mData[1] - other.mData[1]))));
    }

    public float getDotProduct(SGVector2f other) {
        return (this.mData[0] * other.mData[0]) + (this.mData[1] * other.mData[1]);
    }

    public float getLength() {
        return (float) Math.sqrt((double) ((this.mData[0] * this.mData[0]) + (this.mData[1] * this.mData[1])));
    }

    public float getX() {
        return this.mData[0];
    }

    public float getY() {
        return this.mData[1];
    }

    public void interpolate(SGVector2f to, float coeff) {
        this.mData[0] = this.mData[0] + ((to.mData[0] - this.mData[0]) * coeff);
        this.mData[1] = this.mData[1] + ((to.mData[1] - this.mData[1]) * coeff);
    }

    public void inverse() {
        this.mData[0] = -this.mData[0];
        this.mData[1] = -this.mData[1];
    }

    public boolean isEqual(SGVector2f other, float epsilon) {
        return Math.abs(other.mData[0] - this.mData[0]) <= epsilon && Math.abs(other.mData[1] - this.mData[1]) <= epsilon;
    }

    public void multiply(SGVector2f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] * other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] * other.mData[1];
    }

    public void normalize() {
        float len = getLength();
        if (len != 0.0f) {
            this.mData[0] = this.mData[0] / len;
            this.mData[1] = this.mData[1] / len;
        }
    }

    public void scale(float scale) {
        this.mData[0] = this.mData[0] * scale;
        this.mData[1] = this.mData[1] * scale;
    }

    public void set(float x, float y) {
        this.mData[0] = x;
        this.mData[1] = y;
    }

    public void setX(float x) {
        this.mData[0] = x;
    }

    public void setY(float y) {
        this.mData[1] = y;
    }

    public void subtract(SGVector2f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] - other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] - other.mData[1];
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Vector2f");
    }
}
