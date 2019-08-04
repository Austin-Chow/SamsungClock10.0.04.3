package android.support.v4.content.res;

import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslConfigurationReflector {
    static final ConfigurationBaseImpl IMPL;
    private static final Class<?> mClass = Configuration.class;

    private interface ConfigurationBaseImpl {
        int getField_SEM_DESKTOP_MODE_ENABLED(Configuration configuration);

        int getField_semDesktopModeEnabled(Configuration configuration);
    }

    private static class ConfigurationApi21Impl implements ConfigurationBaseImpl {
        private ConfigurationApi21Impl() {
        }

        public int getField_semDesktopModeEnabled(Configuration configuration) {
            return -1;
        }

        public int getField_SEM_DESKTOP_MODE_ENABLED(Configuration configuration) {
            return 0;
        }
    }

    private static class ConfigurationApi24Impl extends ConfigurationApi21Impl {
        private ConfigurationApi24Impl() {
            super();
        }

        public int getField_semDesktopModeEnabled(Configuration configuration) {
            Field field = SeslBaseReflector.getDeclaredField(SeslConfigurationReflector.mClass, "semDesktopModeEnabled");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(configuration, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return -1;
        }

        public int getField_SEM_DESKTOP_MODE_ENABLED(Configuration configuration) {
            Field field = SeslBaseReflector.getDeclaredField(SeslConfigurationReflector.mClass, "SEM_DESKTOP_MODE_ENABLED");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(configuration, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 0;
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new ConfigurationApi24Impl();
        } else {
            IMPL = new ConfigurationApi21Impl();
        }
    }

    public static int getField_semDesktopModeEnabled(Configuration configuration) {
        return IMPL.getField_semDesktopModeEnabled(configuration);
    }

    public static int getField_SEM_DESKTOP_MODE_ENABLED(Configuration configuration) {
        return IMPL.getField_SEM_DESKTOP_MODE_ENABLED(configuration);
    }
}
