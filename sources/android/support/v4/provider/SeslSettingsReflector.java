package android.support.v4.provider;

import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslSettingsReflector {

    public static class SeslSystemReflector {
        static final SystemBaseImpl IMPL;
        private static final Class<?> mClass = System.class;

        private interface SystemBaseImpl {
            String getField_SEM_PEN_HOVERING();
        }

        private static class SystemApi21Impl implements SystemBaseImpl {
            private SystemApi21Impl() {
            }

            public String getField_SEM_PEN_HOVERING() {
                Field field = SeslBaseReflector.getField(SeslSystemReflector.mClass, "PEN_HOVERING");
                if (field != null) {
                    Object object = SeslBaseReflector.get(null, field);
                    if (object instanceof String) {
                        return (String) object;
                    }
                }
                return "pen_hovering";
            }
        }

        private static class SystemApi24Impl extends SystemApi21Impl {
            private SystemApi24Impl() {
                super();
            }

            public String getField_SEM_PEN_HOVERING() {
                Object object = SeslBaseReflector.get(null, SeslBaseReflector.getField(SeslSystemReflector.mClass, "SEM_PEN_HOVERING"));
                if (object instanceof String) {
                    return (String) object;
                }
                return "pen_hovering";
            }
        }

        static {
            if (VERSION.SDK_INT >= 24) {
                IMPL = new SystemApi24Impl();
            } else {
                IMPL = new SystemApi21Impl();
            }
        }

        public static String getField_SEM_PEN_HOVERING() {
            return IMPL.getField_SEM_PEN_HOVERING();
        }
    }
}
