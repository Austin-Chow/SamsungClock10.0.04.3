package android.support.v4.widget;

import android.support.v4.SeslBaseReflector;
import android.widget.AdapterView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslAdapterViewReflector {
    static final AdapterViewBaseImpl IMPL = new AdapterViewApi21Impl();
    private static final Class<?> mClass = AdapterView.class;

    private interface AdapterViewBaseImpl {
        int getField_mSelectedPosition(AdapterView adapterView);

        void setBottomColor(AdapterView adapterView, int i);

        void setNextSelectedPositionInt(AdapterView adapterView, int i);

        void setSelectedPositionInt(AdapterView adapterView, int i);
    }

    private static class AdapterViewApi21Impl implements AdapterViewBaseImpl {
        private AdapterViewApi21Impl() {
        }

        public int getField_mSelectedPosition(AdapterView adapterView) {
            Field field = SeslBaseReflector.getDeclaredField(SeslAdapterViewReflector.mClass, "mSelectedPosition");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(adapterView, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return -1;
        }

        public void setSelectedPositionInt(AdapterView adapterView, int position) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslAdapterViewReflector.mClass, "setSelectedPositionInt", Integer.TYPE);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(adapterView, method, Integer.valueOf(position));
            }
        }

        public void setNextSelectedPositionInt(AdapterView adapterView, int position) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslAdapterViewReflector.mClass, "setNextSelectedPositionInt", Integer.TYPE);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(adapterView, method, Integer.valueOf(position));
            }
        }

        public void setBottomColor(AdapterView adapterView, int color) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslAdapterViewReflector.mClass, "semSetBottomColor", Integer.TYPE);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(adapterView, method, Integer.valueOf(color));
            }
        }
    }

    public static int getField_mSelectedPosition(AdapterView adapterView) {
        return IMPL.getField_mSelectedPosition(adapterView);
    }

    public static void setSelectedPositionInt(AdapterView adapterView, int position) {
        IMPL.setSelectedPositionInt(adapterView, position);
    }

    public static void setNextSelectedPositionInt(AdapterView adapterView, int position) {
        IMPL.setNextSelectedPositionInt(adapterView, position);
    }

    public static void setBottomColor(AdapterView adapterView, int color) {
        IMPL.setBottomColor(adapterView, color);
    }
}
