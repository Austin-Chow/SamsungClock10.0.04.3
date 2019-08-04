package android.support.v4.widget;

import android.content.Context;
import android.support.v4.SeslBaseReflector;
import android.widget.Toast;
import java.lang.reflect.Method;

public class SeslToastReflector {
    static final ToastBaseImpl IMPL = new ToastApi21Impl();
    private static final Class<?> mClass = Toast.class;

    private interface ToastBaseImpl {
        Object twMakeText(Toast toast, Context context, CharSequence charSequence, int i);
    }

    private static class ToastApi21Impl implements ToastBaseImpl {
        private ToastApi21Impl() {
        }

        public Object twMakeText(Toast toast, Context context, CharSequence text, int duration) {
            Method method = SeslBaseReflector.getMethod(SeslToastReflector.mClass, "twMakeText", Context.class, CharSequence.class, Integer.TYPE);
            if (method == null) {
                return null;
            }
            return SeslBaseReflector.invoke(toast, method, context, text, Integer.valueOf(duration));
        }
    }

    public static Object twMakeText(Toast toast, Context context, CharSequence text, int duration) {
        return IMPL.twMakeText(toast, context, text, duration);
    }
}
