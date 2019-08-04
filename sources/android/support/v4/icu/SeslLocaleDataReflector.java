package android.support.v4.icu;

import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class SeslLocaleDataReflector {
    static final LocaleDataBaseImpl IMPL = new LocaleDataApi21Impl();
    private static final String mClassName = "libcore.icu.LocaleData";

    private interface LocaleDataBaseImpl {
        Object get(Locale locale);

        String[] getField_amPm(Object obj);

        String getField_narrowAm(Object obj);

        String getField_narrowPm(Object obj);

        char getField_zeroDigit(Object obj);
    }

    private static class LocaleDataApi21Impl implements LocaleDataBaseImpl {
        private LocaleDataApi21Impl() {
        }

        public char getField_zeroDigit(Object localeData) {
            Field field = SeslBaseReflector.getField(SeslLocaleDataReflector.mClassName, "zeroDigit");
            if (field != null) {
                Object zeroDigit = SeslBaseReflector.get(localeData, field);
                if (zeroDigit instanceof Character) {
                    return ((Character) zeroDigit).charValue();
                }
            }
            return '0';
        }

        public String[] getField_amPm(Object localeData) {
            Field field = SeslBaseReflector.getField(SeslLocaleDataReflector.mClassName, "amPm");
            if (field != null) {
                Object newInstance = Array.newInstance(String.class, 2);
                newInstance = SeslBaseReflector.get(localeData, field);
                if (newInstance instanceof String[]) {
                    return (String[]) newInstance;
                }
            }
            return new String[]{"Am", "Pm"};
        }

        public String getField_narrowAm(Object localeData) {
            Field field = SeslBaseReflector.getField(SeslLocaleDataReflector.mClassName, "narrowAm");
            if (field != null) {
                Object am = SeslBaseReflector.get(localeData, field);
                if (am instanceof String) {
                    return (String) am;
                }
            }
            return "Am";
        }

        public String getField_narrowPm(Object localeData) {
            Field field = SeslBaseReflector.getField(SeslLocaleDataReflector.mClassName, "narrowPm");
            if (field != null) {
                Object pm = SeslBaseReflector.get(localeData, field);
                if (pm instanceof String) {
                    return (String) pm;
                }
            }
            return "Pm";
        }

        public Object get(Locale locale) {
            Method method = SeslBaseReflector.getMethod(SeslLocaleDataReflector.mClassName, "get", Locale.class);
            if (method != null) {
                Object data = SeslBaseReflector.invoke(null, method, locale);
                if (data.getClass().getName().equals(SeslLocaleDataReflector.mClassName)) {
                    return data;
                }
            }
            return null;
        }
    }

    public static Object get(Locale locale) {
        return IMPL.get(locale);
    }

    public static char getField_zeroDigit(Object localeData) {
        return IMPL.getField_zeroDigit(localeData);
    }

    public static String[] getField_amPm(Object localeData) {
        return IMPL.getField_amPm(localeData);
    }

    public static String getField_narrowAm(Object localeData) {
        return IMPL.getField_narrowAm(localeData);
    }

    public static String getField_narrowPm(Object localeData) {
        return IMPL.getField_narrowPm(localeData);
    }
}
