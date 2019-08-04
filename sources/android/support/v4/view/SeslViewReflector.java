package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import android.view.View;
import android.view.View.MeasureSpec;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslViewReflector {
    static final ViewBaseImpl IMPL;
    private static final Class<?> mClass = View.class;

    public static class SeslMeasureSpecReflector {
        private static final Class<?> mClass = MeasureSpec.class;

        public static int makeSafeMeasureSpec(int size, int mode) {
            boolean useZeroUnspecifiedMeasureSpec;
            if (VERSION.SDK_INT < 23) {
                useZeroUnspecifiedMeasureSpec = true;
            } else {
                useZeroUnspecifiedMeasureSpec = false;
            }
            if (useZeroUnspecifiedMeasureSpec && mode == 0) {
                return 0;
            }
            return MeasureSpec.makeMeasureSpec(size, mode);
        }
    }

    private interface ViewBaseImpl {
        void clearAccessibilityFocus(View view);

        int getField_mPaddingLeft(View view);

        int getField_mPaddingRight(View view);

        void getWindowDisplayFrame(View view, Rect rect);

        boolean isHighContrastTextEnabled(View view);

        boolean isHoveringUIEnabled(View view);

        boolean isInScrollingContainer(View view);

        boolean isVisibleToUser(View view, Rect rect);

        void notifyViewAccessibilityStateChangedIfNeeded(View view, int i);

        boolean requestAccessibilityFocus(View view);

        void resetPaddingToInitialValues(View view);

        void resolvePadding(View view);

        Object semGetHoverPopup(View view, boolean z);

        int semGetHoverPopupType(View view);

        int semGetRoundedCorners(View view);

        void semSetDirectPenInputEnabled(View view, boolean z);

        void semSetHoverPopupType(View view, int i);

        void semSetRoundedCorners(View view, int i);

        void setField_mPaddingLeft(View view, int i);

        void setField_mPaddingRight(View view, int i);
    }

    private static class ViewApi21Impl implements ViewBaseImpl {
        private ViewApi21Impl() {
        }

        public void setField_mPaddingLeft(View view, int value) {
            Field field = SeslBaseReflector.getDeclaredField(SeslViewReflector.mClass, "mPaddingLeft");
            if (field != null) {
                field.setAccessible(true);
                SeslBaseReflector.set(view, field, Integer.valueOf(value));
            }
        }

        public void setField_mPaddingRight(View view, int value) {
            Field field = SeslBaseReflector.getDeclaredField(SeslViewReflector.mClass, "mPaddingRight");
            if (field != null) {
                field.setAccessible(true);
                SeslBaseReflector.set(view, field, Integer.valueOf(value));
            }
        }

        public int getField_mPaddingLeft(View view) {
            Field field = SeslBaseReflector.getDeclaredField(SeslViewReflector.mClass, "mPaddingLeft");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(view, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 0;
        }

        public int getField_mPaddingRight(View view) {
            Field field = SeslBaseReflector.getDeclaredField(SeslViewReflector.mClass, "mPaddingRight");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(view, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 0;
        }

        public boolean isInScrollingContainer(View view) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "isInScrollingContainer", new Class[0]);
            if (method == null) {
                return false;
            }
            Object object = SeslBaseReflector.invoke(view, method, new Object[0]);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }

        public void clearAccessibilityFocus(View view) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "clearAccessibilityFocus", new Class[0]);
            if (method != null) {
                SeslBaseReflector.invoke(view, method, new Object[0]);
            }
        }

        public boolean requestAccessibilityFocus(View view) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "requestAccessibilityFocus", new Class[0]);
            if (method == null) {
                return false;
            }
            Object object = SeslBaseReflector.invoke(view, method, new Object[0]);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }

        public void notifyViewAccessibilityStateChangedIfNeeded(View view, int changeType) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "notifyViewAccessibilityStateChangedIfNeeded", Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(view, method, Integer.valueOf(changeType));
            }
        }

        public boolean isVisibleToUser(View view, Rect boundInView) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "isVisibleToUser", Rect.class);
            if (method == null) {
                return false;
            }
            method.setAccessible(true);
            Object object = SeslBaseReflector.invoke(view, method, boundInView);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }

        public void semSetDirectPenInputEnabled(View view, boolean enabled) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "setWritingBuddyEnabled", Boolean.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(view, method, Boolean.valueOf(enabled));
            }
        }

        public boolean isHoveringUIEnabled(View view) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "isHoveringUIEnabled", new Class[0]);
            if (method == null) {
                return false;
            }
            method.setAccessible(true);
            Object object = SeslBaseReflector.invoke(view, method, new Object[0]);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }

        public int semGetHoverPopupType(View view) {
            Field field = SeslBaseReflector.getDeclaredField(SeslViewReflector.mClass, "mHoverPopupType");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(view, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 0;
        }

        public void semSetHoverPopupType(View view, int type) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "setHoverPopupType", Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(view, method, Integer.valueOf(type));
            }
        }

        public Object semGetHoverPopup(View view, boolean createIfNotExist) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "getHoverPopupWindow", new Class[0]);
            if (method != null) {
                return SeslBaseReflector.invoke(view, method, new Object[0]);
            }
            return null;
        }

        public boolean isHighContrastTextEnabled(View view) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "isHighContrastTextEnabled", new Class[0]);
            if (method == null) {
                return false;
            }
            Object object = SeslBaseReflector.invoke(view, method, new Object[0]);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }

        public void resolvePadding(View view) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "resolvePadding", new Class[0]);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(view, method, new Object[0]);
            }
        }

        public void resetPaddingToInitialValues(View view) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "resetPaddingToInitialValues", new Class[0]);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(view, method, new Object[0]);
            }
        }

        public void getWindowDisplayFrame(View view, Rect outRect) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "getWindowVisibleDisplayFrame", Rect.class);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(view, method, outRect);
            }
        }

        public void semSetRoundedCorners(View view, int value) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "semSetRoundedCorners", Integer.TYPE);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(view, method, Integer.valueOf(value));
            }
        }

        public int semGetRoundedCorners(View view) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "semGetRoundedCorners", new Class[0]);
            if (method == null) {
                return 0;
            }
            Object object = SeslBaseReflector.invoke(view, method, new Object[0]);
            if (object instanceof Integer) {
                return ((Integer) object).intValue();
            }
            return 0;
        }
    }

    private static class ViewApi24Impl extends ViewApi21Impl {
        private ViewApi24Impl() {
            super();
        }

        public void semSetDirectPenInputEnabled(View view, boolean enabled) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "semSetDirectPenInputEnabled", Boolean.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(view, method, Boolean.valueOf(enabled));
            }
        }

        public int semGetHoverPopupType(View view) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "semGetHoverPopupType", new Class[0]);
            if (method == null) {
                return 0;
            }
            Object object = SeslBaseReflector.invoke(view, method, new Object[0]);
            if (object instanceof Integer) {
                return ((Integer) object).intValue();
            }
            return 0;
        }

        public void semSetHoverPopupType(View view, int type) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "semSetHoverPopupType", Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(view, method, Integer.valueOf(type));
            }
        }

        public Object semGetHoverPopup(View view, boolean createIfNotExist) {
            Method method = SeslBaseReflector.getMethod(SeslViewReflector.mClass, "semGetHoverPopup", Boolean.TYPE);
            if (method != null) {
                return SeslBaseReflector.invoke(view, method, new Object[0]);
            }
            return null;
        }

        public void getWindowDisplayFrame(View view, Rect outRect) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslViewReflector.mClass, "getWindowDisplayFrame", Rect.class);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(view, method, outRect);
            }
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new ViewApi24Impl();
        } else {
            IMPL = new ViewApi21Impl();
        }
    }

    public static void setField_mPaddingLeft(View view, int value) {
        IMPL.setField_mPaddingLeft(view, value);
    }

    public static void setField_mPaddingRight(View view, int value) {
        IMPL.setField_mPaddingRight(view, value);
    }

    public static int getField_mPaddingLeft(View view) {
        return IMPL.getField_mPaddingLeft(view);
    }

    public static int getField_mPaddingRight(View view) {
        return IMPL.getField_mPaddingRight(view);
    }

    public static boolean isInScrollingContainer(View view) {
        return IMPL.isInScrollingContainer(view);
    }

    public static void clearAccessibilityFocus(View view) {
        IMPL.clearAccessibilityFocus(view);
    }

    public static boolean requestAccessibilityFocus(View view) {
        return IMPL.requestAccessibilityFocus(view);
    }

    public static void notifyViewAccessibilityStateChangedIfNeeded(View view, int changeType) {
        IMPL.notifyViewAccessibilityStateChangedIfNeeded(view, changeType);
    }

    public static boolean isVisibleToUser(View view) {
        return IMPL.isVisibleToUser(view, null);
    }

    public static boolean isVisibleToUser(View view, Rect boundInView) {
        return IMPL.isVisibleToUser(view, boundInView);
    }

    public static int semGetHoverPopupType(View view) {
        return IMPL.semGetHoverPopupType(view);
    }

    public static void semSetHoverPopupType(View view, int type) {
        IMPL.semSetHoverPopupType(view, type);
    }

    public static void semSetDirectPenInputEnabled(View view, boolean enabled) {
        IMPL.semSetDirectPenInputEnabled(view, enabled);
    }

    public static boolean isHoveringUIEnabled(View view) {
        return IMPL.isHoveringUIEnabled(view);
    }

    public static boolean isHighContrastTextEnabled(View view) {
        return IMPL.isHighContrastTextEnabled(view);
    }

    public static Object semGetHoverPopup(View view, boolean createIfNotExist) {
        return IMPL.semGetHoverPopup(view, createIfNotExist);
    }

    public static void resolvePadding(View view) {
        IMPL.resolvePadding(view);
    }

    public static void resetPaddingToInitialValues(View view) {
        IMPL.resetPaddingToInitialValues(view);
    }

    public static void getWindowDisplayFrame(View view, Rect outRect) {
        IMPL.getWindowDisplayFrame(view, outRect);
    }

    public static void semSetRoundedCorners(View view, int value) {
        IMPL.semSetRoundedCorners(view, value);
    }

    public static int semGetRoundedCorners(View view) {
        return IMPL.semGetRoundedCorners(view);
    }
}
