package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.app.Activity;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarTextWatcherListener;
import com.sec.android.app.clockpackage.worldclock.model.SearchBarData;

class SearchBarTextWatcher implements TextWatcher {
    private Activity mActivity;
    private EditText mAutoText;
    private SearchBarData mSearchBarData;
    private SearchBarTextWatcherListener mSearchBarListener;

    SearchBarTextWatcher(Activity activity, SearchBarData searchBarData, EditText autoText, SearchBarTextWatcherListener listener) {
        this.mActivity = activity;
        this.mSearchBarData = searchBarData;
        this.mAutoText = autoText;
        this.mSearchBarListener = listener;
    }

    public void afterTextChanged(Editable s) {
        boolean z = true;
        this.mSearchBarListener.onSetClearButtonVisibility(s.length() > 0);
        SearchBarTextWatcherListener searchBarTextWatcherListener = this.mSearchBarListener;
        if (s.length() > 0 || StateUtils.isUltraPowerSavingMode(this.mActivity) || !SpeechRecognizer.isRecognitionAvailable(this.mActivity)) {
            z = false;
        }
        searchBarTextWatcherListener.onSetVoiceButtonVisibility(z);
        if (s.length() > 100 && this.mAutoText != null) {
            this.mAutoText.setText(this.mSearchBarData.getLastString().subSequence(0, 100).toString());
            this.mAutoText.setSelection(100);
        }
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        String lastString = this.mSearchBarData.getLastString();
        if (lastString == null) {
            this.mSearchBarData.setLastString(s.toString());
            this.mSearchBarListener.onChangeList();
        } else if (!lastString.equals(s.toString())) {
            if (lastString.length() >= 100) {
                if (s.length() <= 100) {
                    this.mSearchBarData.setLastString(s.toString());
                }
            } else if (s.length() > 100) {
                this.mSearchBarData.setLastString(s.subSequence(0, 100).toString());
            } else {
                this.mSearchBarData.setLastString(s.toString());
            }
            if (this.mAutoText != null && (!this.mAutoText.isInTouchMode() || StateUtils.isMobileKeyboard(this.mActivity))) {
                this.mAutoText.setCursorVisible(true);
                this.mAutoText.setLongClickable(true);
            }
            this.mSearchBarListener.onChangeList();
        }
    }
}
