package android.support.v4.feature;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslFloatingFeatureReflector {
    static final FloatingFeatureBaseImpl IMPL;
    private static Object mInstance = IMPL.getInstance();

    private interface FloatingFeatureBaseImpl {
        Object getInstance();

        String getString(String str, String str2);
    }

    private static class FloatingFeatureApi21Impl implements FloatingFeatureBaseImpl {
        protected static String mClassName;

        public FloatingFeatureApi21Impl() {
            mClassName = "com.samsung.android.feature.FloatingFeature";
        }

        public Object getInstance() {
            Method method = SeslBaseReflector.getMethod(mClassName, "getInstance", new Class[0]);
            if (method == null) {
                return null;
            }
            Object feature = SeslBaseReflector.invoke(null, method, new Object[0]);
            return feature.getClass().getName().equals(mClassName) ? feature : null;
        }

        public String getString(String tag, String defaultValue) {
            if (SeslFloatingFeatureReflector.mInstance != null) {
                Method method = SeslBaseReflector.getMethod(mClassName, "getString", String.class, String.class);
                if (method != null) {
                    Object object = SeslBaseReflector.invoke(getInstance(), method, tag, defaultValue);
                    if (object instanceof String) {
                        return (String) object;
                    }
                }
            }
            return defaultValue;
        }
    }

    private static class FloatingFeatureApi24Impl extends FloatingFeatureApi21Impl {
        public FloatingFeatureApi24Impl() {
            mClassName = "com.samsung.android.feature.SemFloatingFeature";
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new FloatingFeatureApi24Impl();
        } else {
            IMPL = new FloatingFeatureApi21Impl();
        }
    }

    public static String getString(String tag, String defaultValue) {
        return IMPL.getString(tag, defaultValue);
    }
}
