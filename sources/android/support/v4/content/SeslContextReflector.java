package android.support.v4.content;

import android.content.Context;
import android.os.UserHandle;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslContextReflector {
    static final ContextBaseImpl IMPL = new ContextApi21Impl();
    private static final Class<?> mClass = Context.class;

    private interface ContextBaseImpl {
        Context createPackageContextAsUser(Context context, String str, int i, UserHandle userHandle);
    }

    private static class ContextApi21Impl implements ContextBaseImpl {
        private ContextApi21Impl() {
        }

        public Context createPackageContextAsUser(Context context, String packageName, int flags, UserHandle user) {
            Method method = SeslBaseReflector.getMethod(SeslContextReflector.mClass, "createPackageContextAsUser", String.class, Integer.TYPE, UserHandle.class);
            if (method != null) {
                Object object = SeslBaseReflector.invoke(context, method, packageName, Integer.valueOf(flags), user);
                if (object instanceof Context) {
                    return (Context) object;
                }
            }
            return null;
        }
    }

    public static Context createPackageContextAsUser(Context context, String packageName, int flags, UserHandle user) {
        return IMPL.createPackageContextAsUser(context, packageName, flags, user);
    }
}
