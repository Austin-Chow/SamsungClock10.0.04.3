package android.support.v4.graphics;

import android.graphics.Rect;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslRectReflector {
    static final RectBaseImpl IMPL = new RectApi21Impl();
    private static final Class<?> mClass = Rect.class;

    private interface RectBaseImpl {
        void scale(Rect rect, float f);
    }

    private static class RectApi21Impl implements RectBaseImpl {
        private RectApi21Impl() {
        }

        public void scale(Rect rect, float scale) {
            Method method = SeslBaseReflector.getMethod(SeslRectReflector.mClass, "scale", Float.TYPE);
            if (method != null) {
                SeslBaseReflector.invoke(rect, method, Float.valueOf(scale));
            }
        }
    }

    public static void scale(Rect rect, float scale) {
        IMPL.scale(rect, scale);
    }
}
