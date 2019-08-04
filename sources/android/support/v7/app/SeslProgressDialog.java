package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslProgressBar;
import android.support.v7.widget.ViewUtils;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.text.NumberFormat;

@Deprecated
public class SeslProgressDialog extends AlertDialog {
    public static final int STYLE_CIRCLE = 1000;
    public static final int STYLE_HORIZONTAL = 1;
    public static final int STYLE_SPINNER = 0;
    private boolean mHasStarted;
    private int mIncrementBy;
    private int mIncrementSecondaryBy;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private int mMax;
    private CharSequence mMessage;
    private TextView mMessageView;
    private SeslProgressBar mProgress;
    private Drawable mProgressDrawable;
    private TextView mProgressNumber;
    private String mProgressNumberFormat;
    private TextView mProgressPercent;
    private NumberFormat mProgressPercentFormat;
    private int mProgressStyle = 0;
    private int mProgressVal;
    private int mSecondaryProgressVal;
    private Handler mViewUpdateHandler;

    /* renamed from: android.support.v7.app.SeslProgressDialog$1 */
    class C02391 extends Handler {
        C02391() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = SeslProgressDialog.this.mProgress.getProgress();
            int max = SeslProgressDialog.this.mProgress.getMax();
            if (SeslProgressDialog.this.mProgressNumberFormat != null) {
                String format = SeslProgressDialog.this.mProgressNumberFormat;
                if (ViewUtils.isLayoutRtl(SeslProgressDialog.this.mProgressNumber)) {
                    SeslProgressDialog.this.mProgressNumber.setText(String.format(format, new Object[]{Integer.valueOf(max), Integer.valueOf(progress)}));
                } else {
                    SeslProgressDialog.this.mProgressNumber.setText(String.format(format, new Object[]{Integer.valueOf(progress), Integer.valueOf(max)}));
                }
            } else {
                SeslProgressDialog.this.mProgressNumber.setText("");
            }
            if (SeslProgressDialog.this.mProgressPercentFormat != null) {
                SpannableString tmp = new SpannableString(SeslProgressDialog.this.mProgressPercentFormat.format(((double) progress) / ((double) max)));
                tmp.setSpan(new StyleSpan(0), 0, tmp.length(), 33);
                SeslProgressDialog.this.mProgressPercent.setText(tmp);
                return;
            }
            SeslProgressDialog.this.mProgressPercent.setText("");
        }
    }

    public SeslProgressDialog(Context context) {
        super(context);
        initFormats();
    }

    public SeslProgressDialog(Context context, int theme) {
        super(context, theme);
        initFormats();
    }

    private void initFormats() {
        this.mProgressNumberFormat = "%1d/%1d";
        this.mProgressPercentFormat = NumberFormat.getPercentInstance();
        this.mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    public static SeslProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static SeslProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static SeslProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static SeslProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        SeslProgressDialog dialog = new SeslProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(null, C0247R.styleable.AlertDialog, 16842845, 0);
        View view;
        if (this.mProgressStyle == 1) {
            this.mViewUpdateHandler = new C02391();
            view = inflater.inflate(a.getResourceId(C0247R.styleable.AlertDialog_horizontalProgressLayout, C0247R.layout.sesl_alert_dialog_progress), null);
            this.mProgress = (SeslProgressBar) view.findViewById(C0247R.id.progress);
            this.mProgressNumber = (TextView) view.findViewById(C0247R.id.progress_number);
            this.mProgressPercent = (TextView) view.findViewById(C0247R.id.progress_percent);
            this.mMessageView = (TextView) view.findViewById(C0247R.id.message);
            setView(view);
        } else if (this.mProgressStyle == 1000) {
            setTitle(null);
            getWindow().setBackgroundDrawableResource(isLightTheme() ? C0247R.drawable.sesl_progress_dialog_circle_background_shape : C0247R.drawable.sesl_progress_dialog_circle_background_shape_dark);
            view = inflater.inflate(C0247R.layout.sesl_progress_dialog_circle, null);
            this.mProgress = (SeslProgressBar) view.findViewById(C0247R.id.progress);
            this.mMessageView = (TextView) view.findViewById(C0247R.id.message);
            setView(view);
            int circleSize = getContext().getResources().getDimensionPixelSize(C0247R.dimen.sesl_progress_dialog_circle_size);
            getWindow().setLayout(circleSize, circleSize);
        } else {
            view = inflater.inflate(a.getResourceId(C0247R.styleable.AlertDialog_progressLayout, C0247R.layout.sesl_progress_dialog), null);
            this.mProgress = (SeslProgressBar) view.findViewById(C0247R.id.progress);
            this.mMessageView = (TextView) view.findViewById(C0247R.id.message);
            setView(view);
        }
        a.recycle();
        if (this.mMax > 0) {
            setMax(this.mMax);
        }
        if (this.mProgressVal > 0) {
            setProgress(this.mProgressVal);
        }
        if (this.mSecondaryProgressVal > 0) {
            setSecondaryProgress(this.mSecondaryProgressVal);
        }
        if (this.mIncrementBy > 0) {
            incrementProgressBy(this.mIncrementBy);
        }
        if (this.mIncrementSecondaryBy > 0) {
            incrementSecondaryProgressBy(this.mIncrementSecondaryBy);
        }
        if (this.mProgressDrawable != null) {
            setProgressDrawable(this.mProgressDrawable);
        }
        if (this.mIndeterminateDrawable != null) {
            setIndeterminateDrawable(this.mIndeterminateDrawable);
        }
        if (this.mMessage != null) {
            setMessage(this.mMessage);
        }
        setIndeterminate(this.mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        this.mHasStarted = true;
    }

    protected void onStop() {
        super.onStop();
        this.mHasStarted = false;
    }

    public void setProgress(int value) {
        if (this.mHasStarted) {
            this.mProgress.setProgress(value);
            onProgressChanged();
            return;
        }
        this.mProgressVal = value;
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (this.mProgress != null) {
            this.mProgress.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
            return;
        }
        this.mSecondaryProgressVal = secondaryProgress;
    }

    public int getProgress() {
        if (this.mProgress != null) {
            return this.mProgress.getProgress();
        }
        return this.mProgressVal;
    }

    public int getSecondaryProgress() {
        if (this.mProgress != null) {
            return this.mProgress.getSecondaryProgress();
        }
        return this.mSecondaryProgressVal;
    }

    public int getMax() {
        if (this.mProgress != null) {
            return this.mProgress.getMax();
        }
        return this.mMax;
    }

    public void setMax(int max) {
        if (this.mProgress != null) {
            this.mProgress.setMax(max);
            onProgressChanged();
            return;
        }
        this.mMax = max;
    }

    public void incrementProgressBy(int diff) {
        if (this.mProgress != null) {
            this.mProgress.incrementProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementBy += diff;
    }

    public void incrementSecondaryProgressBy(int diff) {
        if (this.mProgress != null) {
            this.mProgress.incrementSecondaryProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementSecondaryBy += diff;
    }

    public void setProgressDrawable(Drawable d) {
        if (this.mProgress != null) {
            this.mProgress.setProgressDrawable(d);
        } else {
            this.mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminateDrawable(d);
        } else {
            this.mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminate(indeterminate);
        } else {
            this.mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        if (this.mProgress != null) {
            return this.mProgress.isIndeterminate();
        }
        return this.mIndeterminate;
    }

    public void setMessage(CharSequence message) {
        if (this.mProgress == null || this.mMessageView == null) {
            this.mMessage = message;
            return;
        }
        this.mMessageView.setText(message);
        if (this.mProgressStyle == 1 || this.mProgressStyle == 1000) {
            this.mMessageView.setVisibility("".equals(message) ? 8 : 0);
        }
    }

    public void setProgressStyle(int style) {
        this.mProgressStyle = style;
    }

    public void setProgressNumberFormat(String format) {
        this.mProgressNumberFormat = format;
        onProgressChanged();
    }

    public void setProgressPercentFormat(NumberFormat format) {
        this.mProgressPercentFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged() {
        if (this.mProgressStyle == 1 && this.mViewUpdateHandler != null && !this.mViewUpdateHandler.hasMessages(0)) {
            this.mViewUpdateHandler.sendEmptyMessage(0);
        }
    }

    private boolean isLightTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            return true;
        }
        return false;
    }
}
