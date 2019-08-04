package android.support.v7.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;

public abstract class DialogPreference extends Preference {
    private Drawable mDialogIcon;
    private int mDialogLayoutResId;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;

    public interface TargetFragment {
        Preference findPreference(CharSequence charSequence);
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, C0263R.styleable.DialogPreference, defStyleAttr, defStyleRes);
        this.mDialogTitle = TypedArrayUtils.getString(a, C0263R.styleable.DialogPreference_dialogTitle, C0263R.styleable.DialogPreference_android_dialogTitle);
        if (this.mDialogTitle == null) {
            this.mDialogTitle = getTitle();
        }
        this.mDialogMessage = TypedArrayUtils.getString(a, C0263R.styleable.DialogPreference_dialogMessage, C0263R.styleable.DialogPreference_android_dialogMessage);
        this.mDialogIcon = TypedArrayUtils.getDrawable(a, C0263R.styleable.DialogPreference_dialogIcon, C0263R.styleable.DialogPreference_android_dialogIcon);
        this.mPositiveButtonText = TypedArrayUtils.getString(a, C0263R.styleable.DialogPreference_positiveButtonText, C0263R.styleable.DialogPreference_android_positiveButtonText);
        this.mNegativeButtonText = TypedArrayUtils.getString(a, C0263R.styleable.DialogPreference_negativeButtonText, C0263R.styleable.DialogPreference_android_negativeButtonText);
        this.mDialogLayoutResId = TypedArrayUtils.getResourceId(a, C0263R.styleable.DialogPreference_dialogLayout, C0263R.styleable.DialogPreference_android_dialogLayout, 0);
        a.recycle();
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, C0263R.attr.dialogPreferenceStyle, 16842897));
    }

    public CharSequence getDialogTitle() {
        return this.mDialogTitle;
    }

    public CharSequence getDialogMessage() {
        return this.mDialogMessage;
    }

    public Drawable getDialogIcon() {
        return this.mDialogIcon;
    }

    public CharSequence getPositiveButtonText() {
        return this.mPositiveButtonText;
    }

    public CharSequence getNegativeButtonText() {
        return this.mNegativeButtonText;
    }

    public int getDialogLayoutResource() {
        return this.mDialogLayoutResId;
    }

    protected void onClick() {
        getPreferenceManager().showDialog(this);
    }
}
