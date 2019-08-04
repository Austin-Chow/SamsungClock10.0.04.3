package com.samsung.android.sdk.sgi.base;

public class SGMatrix4f {
    private static final float k_epsilon = 1.1920929E-7f;
    private float[] mData;

    public SGMatrix4f() {
        this.mData = new float[16];
    }

    public SGMatrix4f(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22, float a23, float a30, float a31, float a32, float a33) {
        this.mData = new float[16];
        set(a00, a01, a02, a03, a10, a11, a12, a13, a20, a21, a22, a23, a30, a31, a32, a33);
    }

    public SGMatrix4f(SGMatrix4f other) {
        this.mData = new float[16];
        set(other);
    }

    public SGMatrix4f(SGVector4f i, SGVector4f j, SGVector4f k, SGVector4f c) {
        this.mData = new float[16];
        this.mData[0] = i.getX();
        this.mData[1] = i.getY();
        this.mData[2] = i.getZ();
        this.mData[3] = i.getW();
        this.mData[4] = j.getX();
        this.mData[5] = j.getY();
        this.mData[6] = j.getZ();
        this.mData[7] = j.getW();
        this.mData[8] = k.getX();
        this.mData[9] = k.getY();
        this.mData[10] = k.getZ();
        this.mData[11] = k.getW();
        this.mData[12] = c.getX();
        this.mData[13] = c.getY();
        this.mData[14] = c.getZ();
        this.mData[15] = c.getW();
    }

    public SGMatrix4f(float[] data) {
        this.mData = new float[16];
        set(data);
    }

    public static SGMatrix4f createLookAtLH(SGVector3f eye, SGVector3f at, SGVector3f up) {
        return SGSgfxMatrix4f.createLookAtLH(eye, at, up);
    }

    public static SGMatrix4f createLookAtRH(SGVector3f eye, SGVector3f at, SGVector3f up) {
        return SGSgfxMatrix4f.createLookAtRH(eye, at, up);
    }

    public static SGMatrix4f createOrthoLH(float width, float height, float zNear, float zFar) {
        return SGSgfxMatrix4f.createOrthoLH(width, height, zNear, zFar);
    }

    public static SGMatrix4f createOrthoLH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return SGSgfxMatrix4f.createOrthoLH(left, right, top, bottom, zNear, zFar);
    }

    public static SGMatrix4f createOrthoRH(float width, float height, float zNear, float zFar) {
        return SGSgfxMatrix4f.createOrthoRH(width, height, zNear, zFar);
    }

    public static SGMatrix4f createOrthoRH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return SGSgfxMatrix4f.createOrthoRH(left, right, top, bottom, zNear, zFar);
    }

    public static SGMatrix4f createPerspectiveFovLH(float fovY, float aspect, float zNear, float zFar) {
        return SGSgfxMatrix4f.createPerspectiveFovLH(fovY, aspect, zNear, zFar);
    }

    public static SGMatrix4f createPerspectiveFovRH(float fovY, float aspect, float zNear, float zFar) {
        return SGSgfxMatrix4f.createPerspectiveFovRH(fovY, aspect, zNear, zFar);
    }

    public static SGMatrix4f createPerspectiveLH(float width, float height, float zNear, float zFar) {
        return SGSgfxMatrix4f.createPerspectiveLH(width, height, zNear, zFar);
    }

    public static SGMatrix4f createPerspectiveLH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return SGSgfxMatrix4f.createPerspectiveLH(left, right, top, bottom, zNear, zFar);
    }

    public static SGMatrix4f createPerspectiveRH(float width, float height, float zNear, float zFar) {
        return SGSgfxMatrix4f.createPerspectiveRH(width, height, zNear, zFar);
    }

    public static SGMatrix4f createPerspectiveRH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return SGSgfxMatrix4f.createPerspectiveRH(left, right, top, bottom, zNear, zFar);
    }

    public static SGMatrix4f createRotation(SGQuaternion rotation) {
        return SGSgfxMatrix4f.createRotation(rotation);
    }

    public static SGMatrix4f createRotation(SGVector3f anglesRad, SGRotationOrder order) {
        return SGSgfxMatrix4f.createRotation(anglesRad, order);
    }

    public static SGMatrix4f createRotationAxis(SGVector3f axis, float angleRad) {
        return SGSgfxMatrix4f.createRotationAxis(axis, angleRad);
    }

    public static SGMatrix4f createRotationX(float angleRad) {
        return SGSgfxMatrix4f.createRotationX(angleRad);
    }

    public static SGMatrix4f createRotationY(float angleRad) {
        return SGSgfxMatrix4f.createRotationY(angleRad);
    }

    public static SGMatrix4f createRotationZ(float angleRad) {
        return SGSgfxMatrix4f.createRotationZ(angleRad);
    }

    public static SGMatrix4f getIdentity() {
        return new SGMatrix4f(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void add(SGMatrix4f other) {
        for (int i = 0; i < 16; i++) {
            float[] fArr = this.mData;
            fArr[i] = fArr[i] + other.mData[i];
        }
    }

    public void divide(SGMatrix4f other) {
        for (int i = 0; i < 16; i++) {
            float[] fArr = this.mData;
            fArr[i] = fArr[i] / other.mData[i];
        }
    }

    public SGVector4f getColumn(int column) {
        if (column <= 3 && column >= 0) {
            return new SGVector4f(this.mData[column + 0], this.mData[column + 4], this.mData[column + 8], this.mData[column + 12]);
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public float[] getData() {
        return this.mData;
    }

    public float getDeterminant() {
        return SGSgfxMatrix4f.getDeterminant(this);
    }

    public float getElement(int index) {
        if (index <= 15 && index >= 0) {
            return this.mData[index];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public float getElement(int row, int column) {
        if (row >= 0 && row <= 3 && column >= 0 && column <= 3) {
            return this.mData[(row * 4) + column];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public SGVector4f getFullTranslation() {
        return SGSgfxMatrix4f.getFullTranslation(this);
    }

    public SGQuaternion getQuaternion() {
        return SGSgfxMatrix4f.getQuaternion(this);
    }

    public SGVector4f getRow(int row) {
        if (row <= 3 && row >= 0) {
            return new SGVector4f(this.mData[(row * 4) + 0], this.mData[(row * 4) + 1], this.mData[(row * 4) + 2], this.mData[(row * 4) + 3]);
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public SGVector3f getScale() {
        SGVector3f scale = new SGVector3f(this.mData[0], this.mData[4], this.mData[8]);
        float x = scale.getLength();
        scale.set(this.mData[1], this.mData[5], this.mData[9]);
        float y = scale.getLength();
        scale.set(this.mData[2], this.mData[6], this.mData[10]);
        scale.set(x, y, scale.getLength());
        return scale;
    }

    public float getTrace() {
        return ((this.mData[0] + this.mData[5]) + this.mData[10]) + this.mData[15];
    }

    public SGVector3f getTranslation() {
        return SGSgfxMatrix4f.getTranslation(this);
    }

    public void interpolateLineary(SGMatrix4f to, float coeff) {
        SGSgfxMatrix4f.interpolateLineary(this, to, coeff);
    }

    public void interpolateSpherically(SGMatrix4f to, float coeff) {
        SGSgfxMatrix4f.interpolateSpherically(this, to, coeff);
    }

    public void inverse() {
        SGSgfxMatrix4f.inverse(this);
    }

    public boolean isEqual(SGMatrix4f other) {
        return isEqual(other, k_epsilon);
    }

    public boolean isEqual(SGMatrix4f other, float epsilon) {
        for (int i = 0; i < 16; i++) {
            if (Math.abs(other.mData[i] - this.mData[i]) > epsilon) {
                return false;
            }
        }
        return true;
    }

    public boolean isIdentity() {
        return SGSgfxMatrix4f.isIdentity(this);
    }

    public void multiply(SGMatrix4f other) {
        SGSgfxMatrix4f.multiply(this, other);
    }

    public void multiplyByElements(SGMatrix4f other) {
        SGSgfxMatrix4f.multiplyByElements(this, other);
    }

    public void rotate(SGQuaternion rotation) {
        SGSgfxMatrix4f.rotate(this, rotation);
    }

    public void rotate(SGVector3f anglesRad, SGRotationOrder order) {
        SGSgfxMatrix4f.rotate(this, anglesRad, order);
    }

    public void rotateAxis(SGVector3f axis, float angleRad) {
        SGSgfxMatrix4f.rotateAxis(this, axis, angleRad);
    }

    public SGQuaternion rotateQuaternion(SGQuaternion quaternion) {
        return SGSgfxMatrix4f.rotateQuaternion(this, quaternion);
    }

    public SGVector3f rotateVector(SGVector3f vector) {
        return SGSgfxMatrix4f.rotateVector(this, vector);
    }

    public SGVector4f rotateVector(SGVector4f vector) {
        return SGSgfxMatrix4f.rotateVector(this, vector);
    }

    public void rotateX(float angleRad) {
        SGSgfxMatrix4f.rotateX(this, angleRad);
    }

    public void rotateY(float angleRad) {
        SGSgfxMatrix4f.rotateY(this, angleRad);
    }

    public void rotateZ(float angleRad) {
        SGSgfxMatrix4f.rotateZ(this, angleRad);
    }

    public void scale(SGVector3f scale) {
        SGSgfxMatrix4f.scale(this, scale);
    }

    public void set(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22, float a23, float a30, float a31, float a32, float a33) {
        this.mData[0] = a00;
        this.mData[1] = a01;
        this.mData[2] = a02;
        this.mData[3] = a03;
        this.mData[4] = a10;
        this.mData[5] = a11;
        this.mData[6] = a12;
        this.mData[7] = a13;
        this.mData[8] = a20;
        this.mData[9] = a21;
        this.mData[10] = a22;
        this.mData[11] = a23;
        this.mData[12] = a30;
        this.mData[13] = a31;
        this.mData[14] = a32;
        this.mData[15] = a33;
    }

    public void set(SGMatrix4f other) {
        System.arraycopy(other.mData, 0, this.mData, 0, 16);
    }

    public void set(float[] other) {
        System.arraycopy(other, 0, this.mData, 0, 16);
    }

    public void setColumn(int column, SGVector4f value) {
        if (column > 3 || column < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[column + 0] = value.getX();
        this.mData[column + 4] = value.getY();
        this.mData[column + 8] = value.getZ();
        this.mData[column + 12] = value.getW();
    }

    public void setElement(int index, float value) {
        if (index > 15 || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[index] = value;
    }

    public void setElement(int row, int column, float value) {
        if (row < 0 || row > 3 || column < 0 || column > 3) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[(row * 4) + column] = value;
    }

    public void setRow(int row, SGVector4f value) {
        if (row > 3 || row < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[(row * 4) + 0] = value.getX();
        this.mData[(row * 4) + 1] = value.getY();
        this.mData[(row * 4) + 2] = value.getZ();
        this.mData[(row * 4) + 3] = value.getW();
    }

    public void subtract(SGMatrix4f other) {
        for (int i = 0; i < 16; i++) {
            float[] fArr = this.mData;
            fArr[i] = fArr[i] - other.mData[i];
        }
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Matrix4f");
    }

    public SGVector3f transformVector(SGVector3f vector) {
        return SGSgfxMatrix4f.transformVector(this, vector);
    }

    public SGVector4f transformVector(SGVector4f vector) {
        return SGSgfxMatrix4f.transformVector(this, vector);
    }

    public void translate(SGVector3f vector) {
        SGSgfxMatrix4f.translate(this, vector);
    }

    public SGVector3f translateVector(SGVector3f vector) {
        return SGSgfxMatrix4f.translateVector(this, vector);
    }

    public SGVector4f translateVector(SGVector4f vector) {
        return SGSgfxMatrix4f.translateVector(this, vector);
    }

    public void transpose() {
        SGSgfxMatrix4f.transpose(this);
    }
}
