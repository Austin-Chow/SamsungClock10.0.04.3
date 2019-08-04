package com.samsung.android.sdk.sgi.base;

public class SGMatrix3f {
    private static final float k_epsilon = 1.1920929E-7f;
    private float[] mData;

    public SGMatrix3f() {
        this.mData = new float[9];
    }

    public SGMatrix3f(float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22) {
        this.mData = new float[9];
        set(a00, a01, a02, a10, a11, a12, a20, a21, a22);
    }

    public SGMatrix3f(SGMatrix3f other) {
        this.mData = new float[16];
        set(other);
    }

    public SGMatrix3f(SGVector3f i, SGVector3f j, SGVector3f k) {
        this.mData = new float[9];
        this.mData[0] = i.getX();
        this.mData[1] = i.getY();
        this.mData[2] = i.getZ();
        this.mData[3] = j.getX();
        this.mData[4] = j.getY();
        this.mData[5] = j.getZ();
        this.mData[6] = k.getX();
        this.mData[7] = k.getY();
        this.mData[8] = k.getZ();
    }

    public SGMatrix3f(float[] data) {
        this.mData = new float[16];
        set(data);
    }

    public SGVector3f getColumn(int column) {
        if (column <= 2 && column >= 0) {
            return new SGVector3f(this.mData[column + 0], this.mData[column + 3], this.mData[column + 6]);
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public float[] getData() {
        return this.mData;
    }

    public float getElement(int index) {
        if (index <= 8 && index >= 0) {
            return this.mData[index];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public float getElement(int row, int column) {
        if (row >= 0 && row <= 2 && column >= 0 && column <= 2) {
            return this.mData[(row * 3) + column];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public SGVector3f getRow(int row) {
        if (row <= 2 && row >= 0) {
            return new SGVector3f(this.mData[(row * 3) + 0], this.mData[(row * 3) + 1], this.mData[(row * 3) + 2]);
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public void set(float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22) {
        this.mData[0] = a00;
        this.mData[1] = a01;
        this.mData[2] = a02;
        this.mData[3] = a10;
        this.mData[4] = a11;
        this.mData[5] = a12;
        this.mData[6] = a20;
        this.mData[7] = a21;
        this.mData[8] = a22;
    }

    public void set(SGMatrix3f other) {
        System.arraycopy(other.mData, 0, this.mData, 0, 9);
    }

    public void set(float[] other) {
        System.arraycopy(other, 0, this.mData, 0, 9);
    }

    public void setColumn(int column, SGVector3f value) {
        if (column > 2 || column < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[column + 0] = value.getX();
        this.mData[column + 3] = value.getY();
        this.mData[column + 6] = value.getZ();
    }

    public void setElement(int index, float value) {
        if (index > 8 || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[index] = value;
    }

    public void setElement(int row, int column, float value) {
        if (row < 0 || row > 2 || column < 0 || column > 2) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[(row * 3) + column] = value;
    }

    public void setRow(int row, SGVector3f value) {
        if (row > 2 || row < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[(row * 3) + 0] = value.getX();
        this.mData[(row * 3) + 1] = value.getY();
        this.mData[(row * 3) + 2] = value.getZ();
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Matrix3f");
    }
}
