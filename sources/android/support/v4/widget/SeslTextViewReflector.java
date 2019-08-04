package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslTextViewReflector {
    static final TextViewBaseImpl IMPL;
    private static final Class<?> mClass = TextView.class;

    private interface TextViewBaseImpl {
        int getField_SEM_AUTOFILL_ID(TextView textView);

        boolean getField_mSingleLine(TextView textView);

        boolean isTextSelectionProgressing();

        boolean isTextViewHovered();

        void semSetActionModeMenuItemEnabled(TextView textView, int i, boolean z);
    }

    private static class TextViewApi21Impl implements TextViewBaseImpl {
        private TextViewApi21Impl() {
        }

        public int getField_SEM_AUTOFILL_ID(TextView textView) {
            return 0;
        }

        public void semSetActionModeMenuItemEnabled(TextView textView, int menuId, boolean enabled) {
            Method method = SeslBaseReflector.getMethod(SeslTextViewReflector.mClass, "setNewActionPopupMenu", Integer.TYPE, Boolean.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(textView, method, Integer.valueOf(menuId), Boolean.valueOf(enabled));
            }
        }

        public boolean getField_mSingleLine(TextView textView) {
            Field field = SeslBaseReflector.getDeclaredField(SeslTextViewReflector.mClass, "mSingleLine");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(textView, field);
                if (object instanceof Boolean) {
                    return ((Boolean) object).booleanValue();
                }
            }
            return false;
        }

        public boolean isTextSelectionProgressing() {
            Method method = SeslBaseReflector.getMethod(SeslTextViewReflector.mClass, "semIsTextSelectionProgressing", new Class[0]);
            if (method == null) {
                return false;
            }
            Object object = SeslBaseReflector.invoke(null, method, new Object[0]);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }

        public boolean isTextViewHovered() {
            Method method = SeslBaseReflector.getMethod(SeslTextViewReflector.mClass, "semIsTextViewHovered", new Class[0]);
            if (method == null) {
                return false;
            }
            Object object = SeslBaseReflector.invoke(null, method, new Object[0]);
            if (object instanceof Boolean) {
                return ((Boolean) object).booleanValue();
            }
            return false;
        }
    }

    private static class TextViewApi24Impl extends TextViewApi21Impl {
        private TextViewApi24Impl() {
            super();
        }

        public int getField_SEM_AUTOFILL_ID(TextView textView) {
            Field field = SeslBaseReflector.getDeclaredField(SeslTextViewReflector.mClass, "SEM_AUTOFILL_ID");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(textView, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 0;
        }

        public void semSetActionModeMenuItemEnabled(TextView textView, int menuId, boolean enabled) {
            Method method = SeslBaseReflector.getMethod(SeslTextViewReflector.mClass, "semSetActionModeMenuItemEnabled", Integer.TYPE, Boolean.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(textView, method, Integer.valueOf(menuId), Boolean.valueOf(enabled));
            }
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new TextViewApi24Impl();
        } else {
            IMPL = new TextViewApi21Impl();
        }
    }

    public static void semSetActionModeMenuItemEnabled(TextView textView, int menuId, boolean enabled) {
        IMPL.semSetActionModeMenuItemEnabled(textView, menuId, enabled);
    }

    public static int getField_SEM_AUTOFILL_ID(TextView textView) {
        return IMPL.getField_SEM_AUTOFILL_ID(textView);
    }

    public static boolean getField_mSingleLine(TextView textView) {
        return IMPL.getField_mSingleLine(textView);
    }

    public static boolean isTextSelectionProgressing() {
        return IMPL.isTextSelectionProgressing();
    }

    public static boolean isTextViewHovered() {
        return IMPL.isTextViewHovered();
    }
}
