package android.support.v4.content.res;

import android.content.res.Resources;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Method;

public class SeslResourcesReflector {
    static final ResourcesBaseImpl IMPL = new ResourcesApi21Impl();
    private static final Class<?> mClass = Resources.class;

    private interface ResourcesBaseImpl {
        Object getCompatibilityInfo(Resources resources);
    }

    private static class ResourcesApi21Impl implements ResourcesBaseImpl {
        private ResourcesApi21Impl() {
        }

        public Object getCompatibilityInfo(Resources resources) {
            Method method = SeslBaseReflector.getMethod(SeslResourcesReflector.mClass, "getCompatibilityInfo", new Class[0]);
            if (method != null) {
                Object info = SeslBaseReflector.invoke(resources, method, new Object[0]);
                if (info.getClass().getName().equals("android.content.res.CompatibilityInfo")) {
                    return info;
                }
            }
            return null;
        }
    }

    static Object getCompatibilityInfo(Resources resources) {
        return IMPL.getCompatibilityInfo(resources);
    }
}
