package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.Voice;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Locale;

public class AlarmTtsUtil implements OnInitListener {
    private Context mContext;
    private boolean mIsOpenTextToSpeech = false;
    private TextToSpeech mTextToSpeech;

    public AlarmTtsUtil(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        Log.secD("AlarmTtsUtil", "init");
        this.mTextToSpeech = new TextToSpeech(this.mContext.getApplicationContext(), this);
    }

    public void onInit(int status) {
        this.mIsOpenTextToSpeech = status == 0;
        Log.secD("AlarmTtsUtil", "mIsOpenTextToSpeech : " + this.mIsOpenTextToSpeech);
    }

    public boolean isOpenTextToSpeech() {
        return this.mIsOpenTextToSpeech;
    }

    public int getTtsDialogErrorCode() {
        String samsungTtsEngineName = "com.samsung.SMT";
        String googleTtsEngineName = "com.google.android.tts";
        String ttsEngine = this.mTextToSpeech.getDefaultEngine();
        Voice ttsVoice = this.mTextToSpeech.getDefaultVoice();
        int talkbackType = -1;
        if ("com.samsung.SMT".equals(ttsEngine)) {
            talkbackType = 0;
        } else if ("com.google.android.tts".equals(ttsEngine)) {
            talkbackType = 1;
        }
        if (talkbackType == 0 || talkbackType == 1) {
            Locale locale = Locale.getDefault();
            if (ttsVoice != null) {
                Locale ttsLocale = this.mTextToSpeech.getDefaultVoice().getLocale();
                if (this.mTextToSpeech.isLanguageAvailable(locale) < 0) {
                    return 2;
                }
                if (!(ttsLocale.getISO3Language().equals(locale.getISO3Language()) || ttsLocale.getISO3Country().equals(locale.getISO3Country()))) {
                    return 1;
                }
            } else if (this.mTextToSpeech.isLanguageAvailable(locale) < 0) {
                return 2;
            }
        }
        return -1;
    }

    public String getTtsMessage() {
        return this.mContext.getResources().getString(C0490R.string.alarm_tts_dialog, new Object[]{this.mContext.getResources().getString(C0490R.string.tts_language_default)});
    }

    public void stopTextToSpeech() {
        if (this.mTextToSpeech != null) {
            Log.secD("AlarmTtsUtil", "stopTextToSpeech() stop result = " + this.mTextToSpeech.stop());
        }
    }

    public void removeInstance() {
        if (this.mTextToSpeech != null) {
            Log.secD("AlarmTtsUtil", "releaseTextToSpeech()");
            this.mTextToSpeech.shutdown();
            this.mTextToSpeech = null;
        }
    }
}
