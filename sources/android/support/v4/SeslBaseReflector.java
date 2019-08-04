package android.support.v4;

import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SeslBaseReflector {
    private static final String TAG = "SeslBaseReflector";

    public static Class<?> getClass(String className) {
        Class<?> keyClass = null;
        try {
            keyClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Fail to get class", e);
        }
        return keyClass;
    }

    public static Method getMethod(String className, String methodName, Class<?>... parameterTypes) {
        Class<?> keyClass = getClass(className);
        Method method = null;
        if (keyClass != null) {
            try {
                method = keyClass.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, methodName + " NoSuchMethodException", e);
            }
        }
        return method;
    }

    public static <T> Method getMethod(Class<T> classT, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = classT.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, methodName + " NoSuchMethodException", e);
        }
        return method;
    }

    public static Method getDeclaredMethod(String className, String methodName, Class<?>... parameterTypes) {
        Class<?> keyClass = getClass(className);
        Method method = null;
        if (keyClass != null) {
            try {
                method = keyClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, methodName + " NoSuchMethodException", e);
            }
        }
        return method;
    }

    public static <T> Method getDeclaredMethod(Class<T> classT, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = classT.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, methodName + " NoSuchMethodException", e);
        }
        return method;
    }

    public static Object invoke(Object callerInstance, Method method, Object... args) {
        Object obj = null;
        if (method == null) {
            Log.d(TAG, "method is null");
        } else {
            try {
                obj = method.invoke(callerInstance, args);
            } catch (IllegalAccessException e) {
                Log.e(TAG, method.getName() + " IllegalAccessException", e);
            } catch (IllegalArgumentException e2) {
                Log.e(TAG, method.getName() + " IllegalArgumentException", e2);
            } catch (InvocationTargetException e3) {
                Log.e(TAG, method.getName() + " InvocationTargetException", e3);
            }
        }
        return obj;
    }

    public static Field getField(String className, String fieldName) {
        Class<?> keyClass = getClass(className);
        Field memberField = null;
        if (keyClass != null) {
            try {
                memberField = keyClass.getField(fieldName);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, fieldName + " NoSuchMethodException", e);
            }
        }
        return memberField;
    }

    public static <T> Field getField(Class<T> classT, String fieldName) {
        Field memberField = null;
        try {
            memberField = classT.getField(fieldName);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, fieldName + " NoSuchMethodException", e);
        }
        return memberField;
    }

    public static Field getDeclaredField(String className, String fieldName) {
        Class<?> keyClass = getClass(className);
        Field memberField = null;
        if (keyClass != null) {
            try {
                memberField = keyClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, fieldName + " NoSuchMethodException", e);
            }
        }
        return memberField;
    }

    public static <T> Field getDeclaredField(Class<T> classT, String fieldName) {
        Field memberField = null;
        try {
            memberField = classT.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, fieldName + " NoSuchMethodException", e);
        }
        return memberField;
    }

    public static Object get(Object callerInstance, Field field) {
        Object obj = null;
        if (field == null) {
            Log.e(TAG, "field is null");
        } else {
            try {
                obj = field.get(callerInstance);
            } catch (IllegalAccessException e) {
                Log.e(TAG, field.getName() + " IllegalAccessException", e);
            } catch (IllegalArgumentException e2) {
                Log.e(TAG, field.getName() + " IllegalArgumentException", e2);
            }
        }
        return obj;
    }

    public static void set(Object callerInstance, Field field, Object value) {
        if (field == null) {
            Log.e(TAG, "field is null");
            return;
        }
        try {
            field.set(callerInstance, value);
        } catch (IllegalAccessException e) {
            Log.e(TAG, field.getName() + " IllegalAccessException", e);
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, field.getName() + " IllegalArgumentException", e2);
        }
    }
}
