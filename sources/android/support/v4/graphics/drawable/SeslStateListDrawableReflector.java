package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslStateListDrawableReflector {
    static final StateListDrawableBaseImpl IMPL = new StateListDrawableApi21Impl();
    private static final Class<?> mClass = StateListDrawable.class;

    private interface StateListDrawableBaseImpl {
        int getStateCount(StateListDrawable stateListDrawable);

        Drawable getStateDrawable(StateListDrawable stateListDrawable, int i);

        int[] getStateSet(StateListDrawable stateListDrawable, int i);
    }

    private static class StateListDrawableApi21Impl implements StateListDrawableBaseImpl {
        private StateListDrawableApi21Impl() {
        }

        public int getStateCount(StateListDrawable drawable) {
            Method method = SeslBaseReflector.getMethod(SeslStateListDrawableReflector.mClass, "getStateCount", new Class[0]);
            if (method == null) {
                return 0;
            }
            Object object = SeslBaseReflector.invoke(drawable, method, new Object[0]);
            if (object instanceof Integer) {
                return ((Integer) object).intValue();
            }
            return 0;
        }

        public Drawable getStateDrawable(StateListDrawable drawable, int index) {
            Method method = SeslBaseReflector.getMethod(SeslStateListDrawableReflector.mClass, "getStateDrawable", Integer.TYPE);
            if (method != null) {
                Object object = SeslBaseReflector.invoke(drawable, method, Integer.valueOf(index));
                if (object instanceof Drawable) {
                    return (Drawable) object;
                }
            }
            return null;
        }

        public int[] getStateSet(StateListDrawable drawable, int index) {
            Method method = SeslBaseReflector.getMethod(SeslStateListDrawableReflector.mClass, "getStateSet", Integer.TYPE);
            if (method != null) {
                Object object = SeslBaseReflector.invoke(drawable, method, Integer.valueOf(index));
                if (object instanceof int[]) {
                    return (int[]) object;
                }
            }
            return new int[0];
        }
    }

    public static int getStateCount(StateListDrawable drawable) {
        return IMPL.getStateCount(drawable);
    }

    public static Drawable getStateDrawable(StateListDrawable drawable, int index) {
        return IMPL.getStateDrawable(drawable, index);
    }

    public static int[] getStateSet(StateListDrawable drawable, int index) {
        return IMPL.getStateSet(drawable, index);
    }
}
