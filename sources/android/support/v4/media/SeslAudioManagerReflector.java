package android.support.v4.media;

import android.media.AudioManager;
import android.support.v4.SeslBaseReflector;
import java.lang.reflect.Field;

public class SeslAudioManagerReflector {
    static final AudioManagerBaseImpl IMPL = new AudioManagerApi21Impl();
    private static final Class<?> mClass = AudioManager.class;

    private interface AudioManagerBaseImpl {
        int getField_SOUND_TIME_PICKER_SCROLL();
    }

    private static class AudioManagerApi21Impl implements AudioManagerBaseImpl {
        private AudioManagerApi21Impl() {
        }

        public int getField_SOUND_TIME_PICKER_SCROLL() {
            Field field = SeslBaseReflector.getField(SeslAudioManagerReflector.mClass, "SOUND_TIME_PICKER_SCROLL");
            if (field != null) {
                Object sound = SeslBaseReflector.get(null, field);
                if (sound instanceof Integer) {
                    return ((Integer) sound).intValue();
                }
            }
            return 0;
        }
    }

    public static int getField_SOUND_TIME_PICKER_SCROLL() {
        return IMPL.getField_SOUND_TIME_PICKER_SCROLL();
    }
}
