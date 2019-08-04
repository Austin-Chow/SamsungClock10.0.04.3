package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.C0836R;

public class WorldclockInputFilter implements InputFilter {
    private Context mContext;
    private Toast mToast;

    public WorldclockInputFilter(Context context) {
        this.mContext = context;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        if (this.mToast == null) {
            this.mToast = Toast.makeText(this.mContext, null, 0);
        }
        int keep = 100 - (dest.length() - (dEnd - dStart));
        Log.secD("WorldclockInputFilter", "keep=" + keep + " start=" + start + " end=" + end + " dStart=" + dStart + " dEnd=" + dEnd);
        String format;
        if (end > 0 && keep <= 0) {
            format = this.mContext.getResources().getString(C0836R.string.input_max_message);
            if (!(this.mToast == null || this.mToast.getView().isShown())) {
                this.mToast.setText(String.format(format, new Object[]{Integer.valueOf(100)}));
                this.mToast.show();
            }
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            format = this.mContext.getResources().getString(C0836R.string.input_max_message);
            if (!(this.mToast == null || this.mToast.getView().isShown())) {
                this.mToast.setText(String.format(format, new Object[]{Integer.valueOf(100)}));
                this.mToast.show();
            }
            CharSequence sourceStr = source.subSequence(start, start + keep).toString();
            if (TextUtils.isEmpty(sourceStr)) {
                return "";
            }
            int length = sourceStr.length();
            if (Character.isHighSurrogate(sourceStr.charAt(length - 1))) {
                Log.secD("WorldclockInputFilter", "isHighSurrogate");
                sourceStr = sourceStr.substring(0, length - 1);
            }
            if (TextUtils.isEmpty(sourceStr)) {
                return "";
            }
            if (Character.isLowSurrogate(sourceStr.charAt(0))) {
                return sourceStr.substring(1);
            }
            return sourceStr;
        }
    }
}
