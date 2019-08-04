package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslHoverPopupWindowReflector {
    static final HoverPopupWindowBaseImpl IMPL;

    private interface HoverPopupWindowBaseImpl {
        int getField_TYPE_NONE();

        int getField_TYPE_TOOLTIP();

        int getField_TYPE_USER_CUSTOM();

        void setGravity(Object obj, int i);

        void setHoverDetectTime(Object obj, int i);

        void setHoveringPoint(Object obj, int i, int i2);

        void setOffset(Object obj, int i, int i2);

        void update(Object obj);
    }

    private static class HoverPopupWindowApi21Impl implements HoverPopupWindowBaseImpl {
        protected static String mClassName;

        public HoverPopupWindowApi21Impl() {
            mClassName = "android.widget.HoverPopupWindow";
        }

        public int getField_TYPE_NONE() {
            Field field = SeslBaseReflector.getField(mClassName, "TYPE_NONE");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 0;
        }

        public int getField_TYPE_TOOLTIP() {
            Field field = SeslBaseReflector.getField(mClassName, "TYPE_TOOLTIP");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 1;
        }

        public int getField_TYPE_USER_CUSTOM() {
            Field field = SeslBaseReflector.getField(mClassName, "TYPE_USER_CUSTOM");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 3;
        }

        public void setGravity(Object hoverPopupWindow, int gravity) {
            Method method = SeslBaseReflector.getMethod(mClassName, "setPopupGravity", new Class[0]);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, Integer.valueOf(gravity));
            }
        }

        public void setOffset(Object hoverPopupWindow, int x, int y) {
            Method method = SeslBaseReflector.getMethod(mClassName, "setPopupPosOffset", Integer.TYPE, Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, Integer.valueOf(x), Integer.valueOf(y));
            }
        }

        public void setHoverDetectTime(Object hoverPopupWindow, int ms) {
            Method method = SeslBaseReflector.getMethod(mClassName, "setHoverDetectTime", Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, Integer.valueOf(ms));
            }
        }

        public void setHoveringPoint(Object hoverPopupWindow, int x, int y) {
            Method method = SeslBaseReflector.getMethod(mClassName, "setHoveringPoint", Integer.TYPE, Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, Integer.valueOf(x), Integer.valueOf(y));
            }
        }

        public void update(Object hoverPopupWindow) {
            Method method = SeslBaseReflector.getMethod(mClassName, "updateHoverPopup", new Class[0]);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, new Object[0]);
            }
        }
    }

    private static class HoverPopupWindowApi24Impl extends HoverPopupWindowApi21Impl {
        public HoverPopupWindowApi24Impl() {
            mClassName = "com.samsung.android.widget.SemHoverPopupWindow";
        }

        public void setGravity(Object hoverPopupWindow, int gravity) {
            Method method = SeslBaseReflector.getMethod(mClassName, "setGravity", new Class[0]);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, Integer.valueOf(gravity));
            }
        }

        public void setOffset(Object hoverPopupWindow, int x, int y) {
            Method method = SeslBaseReflector.getMethod(mClassName, "setOffset", Integer.TYPE, Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, Integer.valueOf(x), Integer.valueOf(y));
            }
        }

        public void update(Object hoverPopupWindow) {
            Method method = SeslBaseReflector.getMethod(mClassName, "update", new Class[0]);
            if (method != null) {
                SeslBaseReflector.invoke(hoverPopupWindow, method, new Object[0]);
            }
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new HoverPopupWindowApi24Impl();
        } else {
            IMPL = new HoverPopupWindowApi21Impl();
        }
    }

    public static int getField_TYPE_NONE() {
        return IMPL.getField_TYPE_NONE();
    }

    public static int getField_TYPE_TOOLTIP() {
        return IMPL.getField_TYPE_TOOLTIP();
    }

    public static int getField_TYPE_USER_CUSTOM() {
        return IMPL.getField_TYPE_USER_CUSTOM();
    }

    public static void setGravity(Object hoverPopupWindow, int gravity) {
        IMPL.setGravity(hoverPopupWindow, gravity);
    }

    public static void setOffset(Object hoverPopupWindow, int x, int y) {
        IMPL.setOffset(hoverPopupWindow, x, y);
    }

    public static void setHoverDetectTime(Object hoverPopupWindow, int ms) {
        IMPL.setHoverDetectTime(hoverPopupWindow, ms);
    }

    public static void setHoveringPoint(Object hoverPopupWindow, int x, int y) {
        IMPL.setHoveringPoint(hoverPopupWindow, x, y);
    }

    public static void update(Object hoverPopupWindow) {
        IMPL.update(hoverPopupWindow);
    }
}
