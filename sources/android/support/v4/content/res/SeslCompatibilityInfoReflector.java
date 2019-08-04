package android.support.v4.content.res;

import android.content.res.Resources;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslCompatibilityInfoReflector {
    static final CompatibilityInfoBaseImpl IMPL = new CompatibilityInfoApi21Impl();
    private static final String mClassName = "android.content.res.CompatibilityInfo";

    private interface CompatibilityInfoBaseImpl {
        float getField_applicationScale(Resources resources);
    }

    private static class CompatibilityInfoApi21Impl implements CompatibilityInfoBaseImpl {
        private CompatibilityInfoApi21Impl() {
        }

        public float getField_applicationScale(Resources resources) {
            Object compatibilityInfo = SeslResourcesReflector.getCompatibilityInfo(resources);
            if (compatibilityInfo != null) {
                Field field = SeslBaseReflector.getField(SeslCompatibilityInfoReflector.mClassName, "applicationScale");
                if (field != null) {
                    Object scale = SeslBaseReflector.get(compatibilityInfo, field);
                    if (scale instanceof Integer) {
                        return (float) ((Integer) scale).intValue();
                    }
                }
            }
            return 1.0f;
        }
    }

    public static float getField_applicationScale(Resources resources) {
        return IMPL.getField_applicationScale(resources);
    }
}
