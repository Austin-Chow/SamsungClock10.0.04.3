package android.support.v4.graphics;

import android.graphics.Paint;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslPaintReflector {
    static final PaintBaseImpl IMPL = new PaintApi21Impl();
    private static final Class<?> mClass = Paint.class;

    private interface PaintBaseImpl {
        float getHCTStrokeWidth(Paint paint);
    }

    private static class PaintApi21Impl implements PaintBaseImpl {
        private PaintApi21Impl() {
        }

        public float getHCTStrokeWidth(Paint paint) {
            Method method = SeslBaseReflector.getMethod(SeslPaintReflector.mClass, "getHCTStrokeWidth", new Class[0]);
            if (method != null) {
                Object width = SeslBaseReflector.invoke(paint, method, new Object[0]);
                if (width instanceof Float) {
                    return ((Float) width).floatValue();
                }
            }
            return 0.0f;
        }
    }

    public static float getHCTStrokeWidth(Paint paint) {
        return IMPL.getHCTStrokeWidth(paint);
    }
}
