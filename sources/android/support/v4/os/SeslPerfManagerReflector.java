package android.support.v4.os;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslPerfManagerReflector {
    static final PerfManagerBaseImpl IMPL;

    private interface PerfManagerBaseImpl {
        boolean onSmoothScrollEvent(boolean z);
    }

    private static class PerfManagerApi21Impl implements PerfManagerBaseImpl {
        protected static String mClassName;

        public PerfManagerApi21Impl() {
            mClassName = "android.os.DVFSHelper";
        }

        public boolean onSmoothScrollEvent(boolean isScroll) {
            Method method = SeslBaseReflector.getMethod(mClassName, "onSmoothScrollEvent", Boolean.TYPE);
            if (method == null) {
                return false;
            }
            SeslBaseReflector.invoke(null, method, Boolean.valueOf(isScroll));
            return true;
        }
    }

    private static class PerfManagerApi24Impl extends PerfManagerApi21Impl {
        public PerfManagerApi24Impl() {
            mClassName = "com.samsung.android.os.SemPerfManager";
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new PerfManagerApi24Impl();
        } else {
            IMPL = new PerfManagerApi21Impl();
        }
    }

    public static boolean onSmoothScrollEvent(boolean isScroll) {
        return IMPL.onSmoothScrollEvent(isScroll);
    }
}
