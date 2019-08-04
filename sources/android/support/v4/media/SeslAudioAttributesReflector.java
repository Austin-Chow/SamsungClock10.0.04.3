package android.support.v4.media;

import android.media.AudioAttributes;
import android.os.Build.VERSION;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslAudioAttributesReflector {
    static final AudioAttributesBaseImpl IMPL;
    private static final Class<?> mClass = AudioAttributes.class;

    private interface AudioAttributesBaseImpl {
        int getField_FLAG_BYPASS_INTERRUPTION_POLICY();
    }

    private static class AudioAttributesApi21Impl implements AudioAttributesBaseImpl {
        private AudioAttributesApi21Impl() {
        }

        public int getField_FLAG_BYPASS_INTERRUPTION_POLICY() {
            return 1;
        }
    }

    private static class AudioAttributesApi23Impl extends AudioAttributesApi21Impl {
        private AudioAttributesApi23Impl() {
            super();
        }

        public int getField_FLAG_BYPASS_INTERRUPTION_POLICY() {
            Field field = SeslBaseReflector.getField(SeslAudioAttributesReflector.mClass, "FLAG_BYPASS_INTERRUPTION_POLICY");
            if (field != null) {
                Object flag = SeslBaseReflector.get(null, field);
                if (flag instanceof Integer) {
                    return ((Integer) flag).intValue();
                }
            }
            return 1;
        }
    }

    static {
        if (VERSION.SDK_INT >= 23) {
            IMPL = new AudioAttributesApi23Impl();
        } else {
            IMPL = new AudioAttributesApi21Impl();
        }
    }

    public static int getField_FLAG_BYPASS_INTERRUPTION_POLICY() {
        return IMPL.getField_FLAG_BYPASS_INTERRUPTION_POLICY();
    }
}
