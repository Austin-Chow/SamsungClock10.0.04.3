package android.support.v4.hardware.input;

import android.hardware.input.InputManager;
import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslInputManagerReflector {
    static final InputManagerBaseImpl IMPL;
    private static final Class<?> mClass = InputManager.class;

    private interface InputManagerBaseImpl {
        void setPointerIconType(int i);
    }

    private static class InputManagerApi21Impl implements InputManagerBaseImpl {
        protected static Object mInstance;

        public InputManagerApi21Impl() {
            Method method = SeslBaseReflector.getMethod(SeslInputManagerReflector.mClass, "getInstance", new Class[0]);
            if (method != null) {
                mInstance = SeslBaseReflector.invoke(null, method, new Object[0]);
            }
        }

        public void setPointerIconType(int iconId) {
        }
    }

    private static class InputManagerApi24Impl extends InputManagerApi21Impl {
        private InputManagerApi24Impl() {
        }

        public void setPointerIconType(int iconId) {
            if (mInstance != null) {
                Method method = SeslBaseReflector.getMethod(SeslInputManagerReflector.mClass, "setPointerIconType", Integer.TYPE);
                if (method != null) {
                    SeslBaseReflector.invoke(mInstance, method, Integer.valueOf(iconId));
                }
            }
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new InputManagerApi24Impl();
        } else {
            IMPL = new InputManagerApi21Impl();
        }
    }

    public static void setPointerIconType(int iconId) {
        IMPL.setPointerIconType(iconId);
    }
}
