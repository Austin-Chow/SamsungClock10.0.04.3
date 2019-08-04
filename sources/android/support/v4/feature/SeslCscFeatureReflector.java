package android.support.v4.feature;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslCscFeatureReflector {
    static final CscFeatureBaseImpl IMPL;
    private static Object mInstance = IMPL.getInstance();

    private interface CscFeatureBaseImpl {
        Object getInstance();

        String getString(String str, String str2);
    }

    private static class CscFeatureApi21Impl implements CscFeatureBaseImpl {
        protected static String mClassName;

        public CscFeatureApi21Impl() {
            mClassName = "com.sec.android.app.CscFeature";
        }

        public Object getInstance() {
            Method method = SeslBaseReflector.getMethod(mClassName, "getInstance", new Class[0]);
            if (method != null) {
                Object feature = SeslBaseReflector.invoke(null, method, new Object[0]);
                if (feature.getClass().getName().equals(mClassName)) {
                    return feature;
                }
            }
            return null;
        }

        public String getString(String tag, String defaultValue) {
            if (SeslCscFeatureReflector.mInstance != null) {
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

    private static class CscFeatureApi24Impl extends CscFeatureApi21Impl {
        public CscFeatureApi24Impl() {
            mClassName = "com.samsung.android.feature.SemCscFeature";
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new CscFeatureApi24Impl();
        } else {
            IMPL = new CscFeatureApi21Impl();
        }
    }

    public static String getString(String tag, String defaultValue) {
        return IMPL.getString(tag, defaultValue);
    }
}
