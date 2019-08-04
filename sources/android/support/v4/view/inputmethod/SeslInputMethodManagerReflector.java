package android.support.v4.view.inputmethod;

import android.support.v4.SeslBaseReflector;
import android.view.inputmethod.InputMethodManager;
import java.lang.reflect.Method;

public class SeslInputMethodManagerReflector {
    private static final Class<?> mClass = InputMethodManager.class;

    public static int isAccessoryKeyboardState(InputMethodManager imm) {
        Method method = SeslBaseReflector.getMethod(mClass, "isAccessoryKeyboardState", new Class[0]);
        if (method == null) {
            return 0;
        }
        Object object = SeslBaseReflector.invoke(imm, method, new Object[0]);
        if (object instanceof Integer) {
            return ((Integer) object).intValue();
        }
        return 0;
    }
}
