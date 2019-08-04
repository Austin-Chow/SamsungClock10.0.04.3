package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import android.view.HapticFeedbackConstants;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslHapticFeedbackConstantsReflector {
    static final HapticFeedbackConstantsReflectorImpl IMPL;
    private static final Class<?> mClass = HapticFeedbackConstants.class;

    private interface HapticFeedbackConstantsReflectorImpl {
        int getField_VIBE_COMMON_A();

        int semGetVibrationIndex(int i);
    }

    private static class BaseHapticFeedbackConstantsReflectorImpl implements HapticFeedbackConstantsReflectorImpl {
        private BaseHapticFeedbackConstantsReflectorImpl() {
        }

        public int getField_VIBE_COMMON_A() {
            Field field = SeslBaseReflector.getField(SeslHapticFeedbackConstantsReflector.mClass, "VIBE_COMMON_A");
            if (field != null) {
                Object vibe = SeslBaseReflector.get(null, field);
                if (vibe instanceof Integer) {
                    return ((Integer) vibe).intValue();
                }
            }
            return -1;
        }

        public int semGetVibrationIndex(int index) {
            Method method = SeslBaseReflector.getMethod(SeslHapticFeedbackConstantsReflector.mClass, "semGetVibrationIndex", Integer.TYPE);
            if (method != null) {
                Object object = SeslBaseReflector.invoke(null, method, Integer.valueOf(index));
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return -1;
        }
    }

    private static class Api24HapticFeedbackConstantsReflectorImpl extends BaseHapticFeedbackConstantsReflectorImpl {
        private Api24HapticFeedbackConstantsReflectorImpl() {
            super();
        }

        public int getField_VIBE_COMMON_A() {
            Field field = SeslBaseReflector.getField(SeslHapticFeedbackConstantsReflector.mClass, "SEM_VIBE_COMMON_A");
            if (field != null) {
                Object vibe = SeslBaseReflector.get(null, field);
                if (vibe instanceof Integer) {
                    return ((Integer) vibe).intValue();
                }
            }
            return -1;
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new Api24HapticFeedbackConstantsReflectorImpl();
        } else {
            IMPL = new BaseHapticFeedbackConstantsReflectorImpl();
        }
    }

    public static int getField_VIBE_COMMON_A() {
        return IMPL.getField_VIBE_COMMON_A();
    }

    public static int semGetVibrationIndex(int index) {
        return IMPL.semGetVibrationIndex(index);
    }
}
