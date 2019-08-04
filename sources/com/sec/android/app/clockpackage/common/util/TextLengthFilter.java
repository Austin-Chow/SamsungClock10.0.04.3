package com.sec.android.app.clockpackage.common.util;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import com.sec.android.app.clockpackage.common.C0645R;

public class TextLengthFilter extends LengthFilter {
    private Context mContext;
    private final int mMaxLength;
    private onErrorTextListener mOnErrorTextListener;
    private TextInputLayout mTextInputLayoutId;

    public interface onErrorTextListener {
        void onErrorText(boolean z);
    }

    public TextLengthFilter(Context context, TextInputLayout textInputLayout, int maxLength, onErrorTextListener listener) {
        super(maxLength);
        this.mContext = context;
        this.mTextInputLayoutId = textInputLayout;
        this.mMaxLength = maxLength;
        this.mOnErrorTextListener = listener;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence result = super.filter(source, start, end, dest, dstart, dend);
        if (result != null) {
            if (!this.mTextInputLayoutId.isErrorEnabled()) {
                this.mTextInputLayoutId.setError(this.mContext.getResources().getString(C0645R.string.input_max_message, new Object[]{Integer.valueOf(this.mMaxLength)}));
                this.mTextInputLayoutId.setErrorEnabled(true);
                if (this.mOnErrorTextListener != null) {
                    this.mOnErrorTextListener.onErrorText(true);
                }
            }
            if (result.length() < 2) {
                return result;
            }
            int regionalIndicatorSymbolCount = 0;
            int resultLength = result.length();
            int i = resultLength;
            while (i >= 2 && isRegionalIndicatorSymbol(result.subSequence(i - 2, i))) {
                regionalIndicatorSymbolCount++;
                i -= 2;
            }
            if (regionalIndicatorSymbolCount % 2 == 0 || !isRegionalIndicatorSymbol(result.subSequence(resultLength - 2, resultLength))) {
                return result;
            }
            return result.subSequence(0, resultLength - 2);
        }
        this.mTextInputLayoutId.setErrorEnabled(false);
        if (this.mOnErrorTextListener == null) {
            return result;
        }
        this.mOnErrorTextListener.onErrorText(false);
        return result;
    }

    private boolean isRegionalIndicatorSymbol(CharSequence source) {
        int code = source.toString().codePointAt(0);
        if (127462 > code || code > 127487) {
            return false;
        }
        return true;
    }
}
