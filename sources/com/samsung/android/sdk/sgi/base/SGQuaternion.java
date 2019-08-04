package com.samsung.android.sdk.sgi.base;

public class SGQuaternion {
    private float[] mData;

    public SGQuaternion() {
        this.mData = new float[4];
        set(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public SGQuaternion(float value) {
        this.mData = new float[4];
        set(value);
    }

    public SGQuaternion(float x, float y, float z, float w) {
        this.mData = new float[4];
        set(x, y, z, w);
    }

    public SGQuaternion(SGQuaternion other) {
        this.mData = new float[4];
        set(other);
    }

    public SGQuaternion(float[] data) {
        this.mData = new float[4];
        System.arraycopy(data, 0, this.mData, 0, 4);
    }

    public static SGQuaternion createRotationAxis(float xAxis, float yAxis, float zAxis, float angleRad) {
        return SGSgfxQuaternion.createRotationAxis(xAxis, yAxis, zAxis, angleRad);
    }

    public static SGQuaternion createRotationAxis(SGVector3f axis, float angleRad) {
        return SGSgfxQuaternion.createRotationAxis(axis, angleRad);
    }

    public static SGQuaternion createRotationEuler(float xAngleRad, float yAngleRad, float zAngleRad, SGRotationOrder order) {
        return SGSgfxQuaternion.createRotationEuler(xAngleRad, yAngleRad, zAngleRad, order);
    }

    public static SGQuaternion createRotationEuler(SGVector3f anglesRad, SGRotationOrder order) {
        return SGSgfxQuaternion.createRotationEuler(anglesRad, order);
    }

    public static SGQuaternion createRotationX(float angleRad) {
        return SGSgfxQuaternion.createRotationX(angleRad);
    }

    public static SGQuaternion createRotationY(float angleRad) {
        return SGSgfxQuaternion.createRotationY(angleRad);
    }

    public static SGQuaternion createRotationZ(float angleRad) {
        return SGSgfxQuaternion.createRotationZ(angleRad);
    }

    public static SGQuaternion getIdentity() {
        return new SGQuaternion(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void add(float other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] + other;
        fArr = this.mData;
        fArr[1] = fArr[1] + other;
        fArr = this.mData;
        fArr[2] = fArr[2] + other;
        fArr = this.mData;
        fArr[3] = fArr[3] + other;
    }

    public void add(SGQuaternion other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] + other.mData[0];
        fArr = this.mData;
        fArr[1] = fArr[1] + other.mData[1];
        fArr = this.mData;
        fArr[2] = fArr[2] + other.mData[2];
        fArr = this.mData;
        fArr[3] = fArr[3] + other.mData[3];
    }

    public void conjugate() {
        set(-this.mData[0], -this.mData[1], -this.mData[2], this.mData[3]);
    }

    public void divide(float value) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] / value;
        fArr = this.mData;
        fArr[1] = fArr[1] / value;
        fArr = this.mData;
        fArr[2] = fArr[2] / value;
        fArr = this.mData;
        fArr[3] = fArr[3] / value;
    }

    public void exponent(float exp, float epsilon) {
        float omega = (float) Math.atan2(Math.sqrt((double) (((this.mData[0] * this.mData[0]) + (this.mData[1] * this.mData[1])) + (this.mData[2] * this.mData[2]))), (double) this.mData[3]);
        float s = (float) Math.sin((double) omega);
        this.mData[3] = (float) Math.cos((double) (exp * omega));
        s = (float) (Math.abs(omega) < epsilon ? (double) exp : Math.sin((double) (exp * omega)) / ((double) s));
        float[] fArr = this.mData;
        fArr[0] = fArr[0] * s;
        fArr = this.mData;
        fArr[1] = fArr[1] * s;
        fArr = this.mData;
        fArr[2] = fArr[2] * s;
    }

    public float[] getData() {
        return this.mData;
    }

    public float getDotProduct(SGQuaternion other) {
        return (((this.mData[0] * other.mData[0]) + (this.mData[1] * other.mData[1])) + (this.mData[2] * other.mData[2])) + (this.mData[3] * other.mData[3]);
    }

    public SGVector3f getEulerAnglesXYZ() {
        return SGSgfxQuaternion.getEulerAnglesXYZ(this);
    }

    public float getEulerAnglesZ() {
        return (float) Math.atan2((double) (((this.mData[3] * this.mData[2]) + (this.mData[0] * this.mData[1])) * 2.0f), (double) (1.0f - (((this.mData[1] * this.mData[1]) + (this.mData[2] * this.mData[2])) * 2.0f)));
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

    public void identity() {
        set(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void interpolateLineary(SGQuaternion to, float coeff) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] + ((to.mData[0] - this.mData[0]) * coeff);
        fArr = this.mData;
        fArr[1] = fArr[1] + ((to.mData[1] - this.mData[1]) * coeff);
        fArr = this.mData;
        fArr[2] = fArr[2] + ((to.mData[2] - this.mData[2]) * coeff);
        fArr = this.mData;
        fArr[3] = fArr[3] + ((to.mData[3] - this.mData[3]) * coeff);
    }

    public void interpolateSpherically(SGQuaternion to, float coeff, float epsilon) {
        SGSgfxQuaternion.interpolateSpherically(this, to, coeff, epsilon);
    }

    public void inverse() {
        float s = 1.0f / ((((this.mData[0] * this.mData[0]) + (this.mData[1] * this.mData[1])) + (this.mData[2] * this.mData[2])) + (this.mData[3] * this.mData[3]));
        set((-this.mData[0]) * s, (-this.mData[1]) * s, (-this.mData[2]) * s, this.mData[3] * s);
    }

    public boolean isEqual(SGQuaternion other, float epsilon) {
        return Math.abs(other.mData[0] - this.mData[0]) <= epsilon && Math.abs(other.mData[1] - this.mData[1]) <= epsilon && Math.abs(other.mData[2] - this.mData[2]) <= epsilon && Math.abs(other.mData[3] - this.mData[3]) <= epsilon;
    }

    public boolean isIdentity(float epsilon) {
        return isEqual(getIdentity(), epsilon);
    }

    public void multiply(float value) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] * value;
        fArr = this.mData;
        fArr[1] = fArr[1] * value;
        fArr = this.mData;
        fArr[2] = fArr[2] * value;
        fArr = this.mData;
        fArr[3] = fArr[3] * value;
    }

    public void multiply(SGQuaternion other) {
        float y0 = (((this.mData[3] * other.mData[1]) + (this.mData[1] * other.mData[3])) + (this.mData[2] * other.mData[0])) - (this.mData[0] * other.mData[2]);
        float z0 = (((this.mData[3] * other.mData[2]) + (this.mData[2] * other.mData[3])) + (this.mData[0] * other.mData[1])) - (this.mData[1] * other.mData[0]);
        float w0 = (((this.mData[3] * other.mData[3]) - (this.mData[0] * other.mData[0])) - (this.mData[1] * other.mData[1])) - (this.mData[2] * other.mData[2]);
        this.mData[0] = (((this.mData[3] * other.mData[0]) + (this.mData[0] * other.mData[3])) + (this.mData[1] * other.mData[2])) - (this.mData[2] * other.mData[1]);
        this.mData[1] = y0;
        this.mData[2] = z0;
        this.mData[3] = w0;
    }

    public void normalize() {
        float dist = getLength();
        if (dist != 0.0f) {
            divide(dist);
        }
    }

    public void rotateAxis(float xAxis, float yAxis, float zAxis, float angleRad) {
        SGSgfxQuaternion.rotateAxis(this, xAxis, yAxis, zAxis, angleRad);
    }

    public void rotateAxis(SGVector3f axis, float angleRad) {
        SGSgfxQuaternion.rotateAxis(this, axis, angleRad);
    }

    public void rotateEuler(float xAngleRad, float yAngleRad, float zAngleRad, SGRotationOrder order) {
        SGSgfxQuaternion.rotateEuler(this, xAngleRad, yAngleRad, zAngleRad, order);
    }

    public void rotateEuler(SGVector3f anglesRad, SGRotationOrder order) {
        SGSgfxQuaternion.rotateEuler(this, anglesRad, order);
    }

    public void rotateX(float angleRad) {
        SGSgfxQuaternion.rotateX(this, angleRad);
    }

    public void rotateY(float angleRad) {
        SGSgfxQuaternion.rotateY(this, angleRad);
    }

    public void rotateZ(float angleRad) {
        SGSgfxQuaternion.rotateZ(this, angleRad);
    }

    public void set(float value) {
        this.mData[0] = value;
        this.mData[1] = value;
        this.mData[2] = value;
        this.mData[3] = value;
    }

    public void set(float x, float y, float z, float w) {
        this.mData[0] = x;
        this.mData[1] = y;
        this.mData[2] = z;
        this.mData[3] = w;
    }

    public void set(SGQuaternion other) {
        this.mData[0] = other.mData[0];
        this.mData[1] = other.mData[1];
        this.mData[2] = other.mData[2];
        this.mData[3] = other.mData[3];
    }

    public void setW(float value) {
        this.mData[3] = value;
    }

    public void setX(float value) {
        this.mData[0] = value;
    }

    public void setY(float value) {
        this.mData[1] = value;
    }

    public void setZ(float value) {
        this.mData[2] = value;
    }

    public void subtract(float other) {
        float[] fArr = this.mData;
        fArr[0] = fArr[0] - other;
        fArr = this.mData;
        fArr[1] = fArr[1] - other;
        fArr = this.mData;
        fArr[2] = fArr[2] - other;
        fArr = this.mData;
        fArr[3] = fArr[3] - other;
    }

    public void subtract(SGQuaternion other) {
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
        return SGMathNative.arrayToString(this.mData, "Quaternion");
    }

    public SGVector3f transformVector(SGVector3f value) {
        return SGSgfxQuaternion.transformVector(this, value);
    }
}
