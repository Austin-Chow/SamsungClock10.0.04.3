package android.support.v4.view;

import android.support.v4.SeslBaseReflector;
import android.view.ViewGroup;
import java.lang.reflect.Method;

public class SeslViewGroupReflector {
    static final ViewGroupBaseImpl IMPL = new ViewGroupApi21Impl();
    private static final Class<?> mClass = ViewGroup.class;

    private interface ViewGroupBaseImpl {
        void resolvePadding(ViewGroup viewGroup);
    }

    private static class ViewGroupApi21Impl implements ViewGroupBaseImpl {
        private ViewGroupApi21Impl() {
        }

        public void resolvePadding(ViewGroup viewGroup) {
            Method method = SeslBaseReflector.getMethod(SeslViewGroupReflector.mClass, "resolvePadding", new Class[0]);
            if (method != null) {
                SeslBaseReflector.invoke(viewGroup, method, new Object[0]);
            }
        }
    }

    public static void resolvePadding(ViewGroup viewGroup) {
        IMPL.resolvePadding(viewGroup);
    }
}
