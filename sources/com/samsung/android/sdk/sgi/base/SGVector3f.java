package com.samsung.android.sdk.sgi.base;

public class SGVector3f {
    private float[] mData;

    public SGVector3f() {
        this.mData = new float[3];
    }

    public SGVector3f(float x, float y) {
        this.mData = new float[3];
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = 0.0f;
    }

    public SGVector3f(float x, float y, float z) {
        this.mData = new float[3];
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
    }

    public SGVector3f(SGVector3f other) {
        this.mData = new float[3];
        this.mData[0] = other.mData[0];
        this.mData[1] = other.mData[1];
        this.mData[2] = other.mData[2];
    }

    public SGVector3f(float[] data) {
        this.mData = new float[3];
        this.mData[0] = data[0];
        this.mData[1] = data[1];
        this.mData[2] = data[2];
    }

    public static SGVector3f createCrossed(SGVector3f firstVector, SGVector3f secondVector) {
        SGVector3f vector = new SGVector3f();
        vector.set((firstVector.getY() * secondVector.getZ()) - (firstVector.getZ() * secondVector.getY()), (firstVector.getZ() * secondVector.getX()) - (firstVector.getX() * secondVector.getZ()), (firstVector.getX() * secondVector.getY()) - (firstVector.getY() * secondVector.getX()));
        return vector;
    }

    public void add(SGVector3f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] + other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] + other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] + other.mData[2];
    }

    public void crossProduct(SGVector3f other) {
        set((this.mData[1] * other.mData[2]) - (this.mData[2] * other.mData[1]), (this.mData[2] * other.mData[0]) - (this.mData[0] * other.mData[2]), (this.mData[0] * other.mData[1]) - (this.mData[1] * other.mData[0]));
    }

    public void divide(SGVector3f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] / other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] / other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] / other.mData[2];
    }

    public float[] getData() {
        return this.mData;
    }

    public float getDistance(SGVector3f other) {
        return (float) Math.sqrt((double) ((((this.mData[0] - other.mData[0]) * (this.mData[0] - other.mData[0])) + ((this.mData[1] - other.mData[1]) * (this.mData[1] - other.mData[1]))) + ((this.mData[2] - other.mData[2]) * (this.mData[2] - other.mData[2]))));
    }

    public float getDotProduct(SGVector3f other) {
        return ((this.mData[0] * other.mData[0]) + (this.mData[1] * other.mData[1])) + (this.mData[2] * other.mData[2]);
    }

    public float getLength() {
        return (float) Math.sqrt((double) (((this.mData[0] * this.mData[0]) + (this.mData[1] * this.mData[1])) + (this.mData[2] * this.mData[2])));
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

    public void interpolate(SGVector3f to, float coeff) {
        this.mData[0] = this.mData[0] + ((to.mData[0] - this.mData[0]) * coeff);
        this.mData[1] = this.mData[1] + ((to.mData[1] - this.mData[1]) * coeff);
        this.mData[2] = this.mData[2] + ((to.mData[2] - this.mData[2]) * coeff);
    }

    public void inverse() {
        this.mData[0] = -this.mData[0];
        this.mData[1] = -this.mData[1];
        this.mData[2] = -this.mData[2];
    }

    public boolean isEqual(SGVector3f other, float epsilon) {
        return Math.abs(other.mData[0] - this.mData[0]) <= epsilon && Math.abs(other.mData[1] - this.mData[1]) <= epsilon && Math.abs(other.mData[2] - this.mData[2]) <= epsilon;
    }

    public void multiply(SGVector3f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] * other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] * other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] * other.mData[2];
    }

    public void normalize() {
        float len = getLength();
        if (len != 0.0f) {
            this.mData[0] = this.mData[0] / len;
            this.mData[1] = this.mData[1] / len;
            this.mData[2] = this.mData[2] / len;
        }
    }

    public void scale(float scale) {
        this.mData[0] = this.mData[0] * scale;
        this.mData[1] = this.mData[1] * scale;
        this.mData[2] = this.mData[2] * scale;
    }

    public void set(float x, float y, float z) {
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
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

    public void subtract(SGVector3f other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] - other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] - other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] - other.mData[2];
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Vector3f");
    }
}
