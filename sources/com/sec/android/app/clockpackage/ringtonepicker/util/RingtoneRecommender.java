package com.sec.android.app.clockpackage.ringtonepicker.util;

import com.samsung.android.media.mir.SemAudioThumbnail;
import com.samsung.android.media.mir.SemAudioThumbnail.ResultListener;
import com.sec.android.app.clockpackage.common.util.Log;

public class RingtoneRecommender {
    private boolean mIsOpen = false;
    private OnHighlightResultListener mListener;
    private ResultListener mSemAudioThumbnailListener = new C06901();
    private final SemAudioThumbnail mSmat = new SemAudioThumbnail();

    public interface OnHighlightResultListener {
        void onResult(int i, int i2);
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.util.RingtoneRecommender$1 */
    class C06901 implements ResultListener {
        C06901() {
        }

        public void onDone(long l) {
            RingtoneRecommender.this.mListener.onResult(1, (int) l);
        }

        public void onError(int i) {
            RingtoneRecommender.this.mListener.onResult(2, i);
        }
    }

    public boolean isOpen() {
        return this.mIsOpen;
    }

    public void doExtract(String path, OnHighlightResultListener listener) {
        this.mIsOpen = true;
        this.mListener = listener;
        extract(path);
    }

    public void close() {
        Log.secD("RingtoneRecommender", "close() is opened ? " + this.mIsOpen);
        if (this.mIsOpen) {
            this.mIsOpen = false;
            this.mListener = null;
            this.mSemAudioThumbnailListener = null;
        }
    }

    public boolean checkFile(String path) {
        return this.mSmat.checkFile(path);
    }

    private void extract(String path) {
        this.mSmat.extract(path, this.mSemAudioThumbnailListener);
    }
}
