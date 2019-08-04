package com.samsung.android.sdk.sgi.base;

import android.graphics.Rect;
import android.graphics.RectF;

public final class SGMathNative {
    static {
        SGConfiguration.initLibrary();
    }

    static String arrayToString(float[] array, String prefix) {
        StringBuilder result = new StringBuilder();
        if (prefix != null) {
            result.append(prefix);
        }
        result.append("(");
        if (array == null) {
            result.append("null");
        } else {
            int len = array.length - 1;
            for (int i = 0; i < len; i++) {
                result.append(array[i]);
                result.append(", ");
            }
            result.append(array[len]);
        }
        result.append(")");
        return result.toString();
    }

    static String arrayToString(int[] array, String prefix) {
        StringBuilder result = new StringBuilder();
        if (prefix != null) {
            result.append(prefix);
        }
        result.append("(");
        if (array == null) {
            result.append("null");
        } else {
            int len = array.length - 1;
            for (int i = 0; i < len; i++) {
                result.append(array[i]);
                result.append(", ");
            }
            result.append(array[len]);
        }
        result.append(")");
        return result.toString();
    }

    public static float[] getArrayRect(RectF rect) {
        if (rect == null) {
            throw new NullPointerException("parameter RectF is null");
        }
        return new float[]{rect.left, rect.top, rect.right, rect.bottom};
    }

    public static int[] getArrayRect(Rect rect) {
        if (rect == null) {
            return null;
        }
        return new int[]{rect.left, rect.top, rect.right, rect.bottom};
    }
}
