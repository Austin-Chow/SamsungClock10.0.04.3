package android.support.v4.os;

import android.os.UserHandle;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslUserHandleReflector {
    static final UserHandleApi21Impl IMPL = new UserHandleApi21Impl();
    private static final Class<?> mClass = UserHandle.class;

    private interface UserHandleBaseImpl {
        int myUserId();
    }

    private static class UserHandleApi21Impl implements UserHandleBaseImpl {
        private UserHandleApi21Impl() {
        }

        public int myUserId() {
            Method method = SeslBaseReflector.getMethod(SeslUserHandleReflector.mClass, "myUserId", new Class[0]);
            if (method == null) {
                return 0;
            }
            Object object = SeslBaseReflector.invoke(null, method, new Object[0]);
            if (object instanceof Integer) {
                return ((Integer) object).intValue();
            }
            return 0;
        }
    }

    public static int myUserId() {
        return IMPL.myUserId();
    }
}
