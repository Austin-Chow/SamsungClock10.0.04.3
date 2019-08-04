package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslPointerIconReflector {
    static final PointerIconBaseImpl IMPL;
    protected static String mClassName = "android.view.PointerIcon";

    private interface PointerIconBaseImpl {
        int getField_SEM_TYPE_STYLUS_DEFAULT();

        int getField_SEM_TYPE_STYLUS_MORE();

        int getField_SEM_TYPE_STYLUS_PEN_SELECT();

        int getField_SEM_TYPE_STYLUS_SCROLL_DOWN();

        int getField_SEM_TYPE_STYLUS_SCROLL_UP();
    }

    private static class PointerIconApi21Impl implements PointerIconBaseImpl {
        private PointerIconApi21Impl() {
        }

        public int getField_SEM_TYPE_STYLUS_DEFAULT() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "HOVERING_SPENICON_DEFAULT");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 1;
        }

        public int getField_SEM_TYPE_STYLUS_SCROLL_UP() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "HOVERING_SCROLLICON_POINTER_01");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 11;
        }

        public int getField_SEM_TYPE_STYLUS_SCROLL_DOWN() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "HOVERING_SCROLLICON_POINTER_05");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 15;
        }

        public int getField_SEM_TYPE_STYLUS_PEN_SELECT() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "HOVERING_PENSELECT_POINTER_01");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 21;
        }

        public int getField_SEM_TYPE_STYLUS_MORE() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "HOVERING_SPENICON_MORE");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 20010;
        }
    }

    private static class PointerIconApi24Impl extends PointerIconApi21Impl {
        private PointerIconApi24Impl() {
            super();
        }

        public int getField_SEM_TYPE_STYLUS_DEFAULT() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "SEM_TYPE_STYLUS_DEFAULT");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 1;
        }

        public int getField_SEM_TYPE_STYLUS_SCROLL_UP() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "SEM_TYPE_STYLUS_SCROLL_UP");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 11;
        }

        public int getField_SEM_TYPE_STYLUS_SCROLL_DOWN() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "SEM_TYPE_STYLUS_SCROLL_DOWN");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 15;
        }

        public int getField_SEM_TYPE_STYLUS_PEN_SELECT() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "SEM_TYPE_STYLUS_PEN_SELECT");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 21;
        }

        public int getField_SEM_TYPE_STYLUS_MORE() {
            Field field = SeslBaseReflector.getField(SeslPointerIconReflector.mClassName, "SEM_TYPE_STYLUS_MORE");
            if (field != null) {
                Object object = SeslBaseReflector.get(null, field);
                if (object instanceof Integer) {
                    return ((Integer) object).intValue();
                }
            }
            return 20010;
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new PointerIconApi24Impl();
        } else {
            IMPL = new PointerIconApi21Impl();
        }
    }

    public static int getField_SEM_TYPE_STYLUS_DEFAULT() {
        return IMPL.getField_SEM_TYPE_STYLUS_DEFAULT();
    }

    public static int getField_SEM_TYPE_STYLUS_SCROLL_UP() {
        return IMPL.getField_SEM_TYPE_STYLUS_SCROLL_UP();
    }

    public static int getField_SEM_TYPE_STYLUS_SCROLL_DOWN() {
        return IMPL.getField_SEM_TYPE_STYLUS_SCROLL_DOWN();
    }

    public static int getField_SEM_TYPE_STYLUS_PEN_SELECT() {
        return IMPL.getField_SEM_TYPE_STYLUS_PEN_SELECT();
    }

    public static int getField_SEM_TYPE_STYLUS_MORE() {
        return IMPL.getField_SEM_TYPE_STYLUS_MORE();
    }
}
