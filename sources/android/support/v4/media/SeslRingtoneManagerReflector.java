package android.support.v4.media;

import android.media.RingtoneManager;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslRingtoneManagerReflector {
    static final RingtoneManagerBaseImpl IMPL = new RingtoneManagerApi21Impl();
    private static final Class<?> mClass = RingtoneManager.class;

    private interface RingtoneManagerBaseImpl {
        String getField_EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS();
    }

    private static class RingtoneManagerApi21Impl implements RingtoneManagerBaseImpl {
        private RingtoneManagerApi21Impl() {
        }

        public String getField_EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS() {
            Field field = SeslBaseReflector.getField(SeslRingtoneManagerReflector.mClass, "EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS");
            if (field != null) {
                Object flags = SeslBaseReflector.get(null, field);
                if (flags instanceof String) {
                    return (String) flags;
                }
            }
            return "android.intent.extra.ringtone.SHOW_DEFAULT";
        }
    }

    public static String getField_EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS() {
        return IMPL.getField_EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS();
    }
}
