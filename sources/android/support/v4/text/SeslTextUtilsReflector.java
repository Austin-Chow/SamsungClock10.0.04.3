package android.support.v4.text;

import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import android.text.TextPaint;
import android.text.TextUtils;
import java.lang.reflect.Method;

public class SeslTextUtilsReflector {
    static final TextUtilsBaseImpl IMPL;
    private static final Class<?> mClass = TextUtils.class;

    private interface TextUtilsBaseImpl {
        char[] semGetPrefixCharForSpan(TextPaint textPaint, CharSequence charSequence, char[] cArr);
    }

    private static class TextUtilsApi21Impl implements TextUtilsBaseImpl {
        private TextUtilsApi21Impl() {
        }

        public char[] semGetPrefixCharForSpan(TextPaint paint, CharSequence text, char[] prefix) {
            Method method = SeslBaseReflector.getMethod(SeslTextUtilsReflector.mClass, "getPrefixCharForIndian", TextPaint.class, CharSequence.class, char[].class);
            if (method != null) {
                Object object = SeslBaseReflector.invoke(null, method, paint, text, prefix);
                if (object instanceof char[]) {
                    return (char[]) object;
                }
            }
            return new char[0];
        }
    }

    private static class TextUtilsApi24Impl extends TextUtilsApi21Impl {
        private TextUtilsApi24Impl() {
            super();
        }

        public char[] semGetPrefixCharForSpan(TextPaint paint, CharSequence text, char[] prefix) {
            Method method = SeslBaseReflector.getMethod(SeslTextUtilsReflector.mClass, "semGetPrefixCharForSpan", TextPaint.class, CharSequence.class, char[].class);
            if (method != null) {
                Object object = SeslBaseReflector.invoke(null, method, paint, text, prefix);
                if (object instanceof char[]) {
                    return (char[]) object;
                }
            }
            return new char[0];
        }
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new TextUtilsApi24Impl();
        } else {
            IMPL = new TextUtilsApi21Impl();
        }
    }

    public static char[] semGetPrefixCharForSpan(TextPaint paint, CharSequence text, char[] prefix) {
        return IMPL.semGetPrefixCharForSpan(paint, text, prefix);
    }
}
