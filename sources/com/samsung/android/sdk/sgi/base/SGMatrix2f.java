package com.samsung.android.sdk.sgi.base;

public class SGMatrix2f {
    private static final float k_epsilon = 1.1920929E-7f;
    private float[] mData;

    public SGMatrix2f() {
        this.mData = new float[4];
    }

    public SGMatrix2f(float a00, float a01, float a10, float a11) {
        this.mData = new float[4];
        set(a00, a01, a10, a11);
    }

    public SGMatrix2f(SGMatrix2f other) {
        this.mData = new float[4];
        set(other);
    }

    public SGMatrix2f(SGVector2f i, SGVector2f j) {
        this.mData = new float[4];
        this.mData[0] = i.getX();
        this.mData[1] = i.getY();
        this.mData[2] = j.getX();
        this.mData[3] = j.getY();
    }

    public SGMatrix2f(float[] data) {
        this.mData = new float[4];
        set(data);
    }

    public SGVector2f getColumn(int column) {
        if (column <= 1 && column >= 0) {
            return new SGVector2f(this.mData[column + 0], this.mData[column + 2]);
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public float[] getData() {
        return this.mData;
    }

    public float getElement(int index) {
        if (index <= 3 && index >= 0) {
            return this.mData[index];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public float getElement(int row, int column) {
        if (row >= 0 && row <= 1 && column >= 0 && column <= 1) {
            return this.mData[(row * 2) + column];
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public SGVector2f getRow(int row) {
        if (row <= 1 && row >= 0) {
            return new SGVector2f(this.mData[(row * 2) + 0], this.mData[(row * 2) + 1]);
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public void set(float a00, float a01, float a10, float a11) {
        this.mData[0] = a00;
        this.mData[1] = a01;
        this.mData[2] = a10;
        this.mData[3] = a11;
    }

    public void set(SGMatrix2f other) {
        System.arraycopy(other.mData, 0, this.mData, 0, 4);
    }

    public void set(float[] other) {
        System.arraycopy(other, 0, this.mData, 0, 4);
    }

    public void setColumn(int column, SGVector2f value) {
        if (column > 1 || column < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[column + 0] = value.getX();
        this.mData[column + 2] = value.getY();
    }

    public void setElement(int index, float value) {
        if (index > 3 || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[index] = value;
    }

    public void setElement(int row, int column, float value) {
        if (row < 0 || row > 1 || column < 0 || column > 1) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[(row * 2) + column] = value;
    }

    public void setRow(int row, SGVector2f value) {
        if (row > 1 || row < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        this.mData[(row * 2) + 0] = value.getX();
        this.mData[(row * 2) + 1] = value.getY();
    }

    public String toString() {
        return SGMathNative.arrayToString(this.mData, "Matrix2f");
    }
}
