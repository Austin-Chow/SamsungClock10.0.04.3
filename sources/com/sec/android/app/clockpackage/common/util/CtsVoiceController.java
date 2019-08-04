package com.sec.android.app.clockpackage.common.util;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.app.VoiceInteractor.AbortVoiceRequest;
import android.app.VoiceInteractor.CompleteVoiceRequest;
import android.app.VoiceInteractor.Prompt;
import android.content.Context;
import android.os.Build.VERSION;

public class CtsVoiceController {
    private static final CtsVoiceController sController = new CtsVoiceController();
    private Context mContext;

    private CtsVoiceController() {
    }

    public static CtsVoiceController getController() {
        return sController;
    }

    public void setContext(Context context) {
        if (this.mContext != context) {
            this.mContext = context.getApplicationContext();
        }
    }

    public void notifyVoiceSuccess(Activity activity, String message) {
        if (isMOrLater()) {
            VoiceInteractor voiceInteractor = activity.getVoiceInteractor();
            if (voiceInteractor != null) {
                voiceInteractor.submitRequest(new CompleteVoiceRequest(new Prompt(message), null));
            }
        }
    }

    public void notifyVoiceFailure(Activity activity, String message) {
        if (isMOrLater()) {
            VoiceInteractor voiceInteractor = activity.getVoiceInteractor();
            if (voiceInteractor != null) {
                voiceInteractor.submitRequest(new AbortVoiceRequest(new Prompt(message), null));
            }
        }
    }

    private boolean isMOrLater() {
        return VERSION.SDK_INT >= 23;
    }
}
