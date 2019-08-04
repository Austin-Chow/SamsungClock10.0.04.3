package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import android.view.InputDevice;
import java.lang.reflect.Method;

public class SeslInputDeviceReflector {
    static final InputDeviceBaseImpl IMPL;
    private static final Class<?> mClass = InputDevice.class;

    private interface InputDeviceBaseImpl {
        void setPointerType(InputDevice inputDevice, int i);
    }

    private static class InputDeviceApi21Impl implements InputDeviceBaseImpl {
        private InputDeviceApi21Impl() {
        }

        public void setPointerType(InputDevice inputDevice, int pointerType) {
        }
    }

    private static class InputDeviceApi24Impl extends InputDeviceApi21Impl {
        private InputDeviceApi24Impl() {
            super();
        }

        public void setPointerType(InputDevice inputDevice, int pointerType) {
            Method method = SeslBaseReflector.getMethod(SeslInputDeviceReflector.mClass, "setPointerType", Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(inputDevice, method, Integer.valueOf(pointerType));
            }
        }
    }

    private static class InputDeviceApi28Impl extends InputDeviceApi24Impl {
        private InputDeviceApi28Impl() {
            super();
        }

        public void setPointerType(InputDevice inputDevice, int pointerType) {
            Method method = SeslBaseReflector.getMethod(SeslInputDeviceReflector.mClass, "semSetPointerType", Integer.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(inputDevice, method, Integer.valueOf(pointerType));
            }
        }
    }

    static {
        if (VERSION.SDK_INT >= 28) {
            IMPL = new InputDeviceApi28Impl();
        } else if (VERSION.SDK_INT >= 24) {
            IMPL = new InputDeviceApi24Impl();
        } else {
            IMPL = new InputDeviceApi21Impl();
        }
    }

    public static void setPointerType(InputDevice inputDevice, int pointerType) {
        if (inputDevice != null) {
            IMPL.setPointerType(inputDevice, pointerType);
        }
    }
}
