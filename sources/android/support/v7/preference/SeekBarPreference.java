package android.support.v7.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v7.preference.Preference.BaseSavedState;
import android.support.v7.widget.SeslSeekBar;
import android.support.v7.widget.SeslSeekBar.OnSeekBarChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class SeekBarPreference extends Preference {
    private boolean mAdjustable;
    private int mMax;
    private int mMin;
    private SeslSeekBar mSeekBar;
    private OnSeekBarChangeListener mSeekBarChangeListener;
    private int mSeekBarIncrement;
    private OnKeyListener mSeekBarKeyListener;
    private int mSeekBarValue;
    private boolean mShowSeekBarValue;
    private boolean mTrackingTouch;

    /* renamed from: android.support.v7.preference.SeekBarPreference$1 */
    class C02641 implements OnSeekBarChangeListener {
        C02641() {
        }

        public void onProgressChanged(SeslSeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && !SeekBarPreference.this.mTrackingTouch) {
                SeekBarPreference.this.syncValueInternal(seekBar);
            }
        }

        public void onStartTrackingTouch(SeslSeekBar seekBar) {
            SeekBarPreference.this.mTrackingTouch = true;
        }

        public void onStopTrackingTouch(SeslSeekBar seekBar) {
            SeekBarPreference.this.mTrackingTouch = false;
            if (seekBar.getProgress() + SeekBarPreference.this.mMin != SeekBarPreference.this.mSeekBarValue) {
                SeekBarPreference.this.syncValueInternal(seekBar);
            }
        }
    }

    /* renamed from: android.support.v7.preference.SeekBarPreference$2 */
    class C02652 implements OnKeyListener {
        C02652() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 0) {
                return false;
            }
            if ((!SeekBarPreference.this.mAdjustable && (keyCode == 21 || keyCode == 22)) || keyCode == 23 || keyCode == 66) {
                return false;
            }
            if (SeekBarPreference.this.mSeekBar != null) {
                return SeekBarPreference.this.mSeekBar.onKeyDown(keyCode, event);
            }
            Log.e("SeekBarPreference", "SeekBar view is null and hence cannot be adjusted.");
            return false;
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C02661();
        int max;
        int min;
        int seekBarValue;

        /* renamed from: android.support.v7.preference.SeekBarPreference$SavedState$1 */
        static class C02661 implements Creator<SavedState> {
            C02661() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        public SavedState(Parcel source) {
            super(source);
            this.seekBarValue = source.readInt();
            this.min = source.readInt();
            this.max = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.seekBarValue);
            dest.writeInt(this.min);
            dest.writeInt(this.max);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSeekBarChangeListener = new C02641();
        this.mSeekBarKeyListener = new C02652();
        TypedArray a = context.obtainStyledAttributes(attrs, C0263R.styleable.SeekBarPreference, defStyleAttr, defStyleRes);
        this.mMin = a.getInt(C0263R.styleable.SeekBarPreference_min, 0);
        setMax(a.getInt(C0263R.styleable.SeekBarPreference_android_max, 100));
        setSeekBarIncrement(a.getInt(C0263R.styleable.SeekBarPreference_seekBarIncrement, 0));
        this.mAdjustable = a.getBoolean(C0263R.styleable.SeekBarPreference_adjustable, true);
        this.mShowSeekBarValue = a.getBoolean(C0263R.styleable.SeekBarPreference_showSeekBarValue, true);
        a.recycle();
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, C0263R.attr.seekBarPreferenceStyle);
    }

    public void onBindViewHolder(PreferenceViewHolder view) {
        super.onBindViewHolder(view);
        view.itemView.setOnKeyListener(this.mSeekBarKeyListener);
        this.mSeekBar = (SeslSeekBar) view.findViewById(C0263R.id.seekbar);
        if (this.mSeekBar == null) {
            Log.e("SeekBarPreference", "SeekBar view is null in onBindViewHolder.");
            return;
        }
        this.mSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mSeekBar.setMax(this.mMax - this.mMin);
        if (this.mSeekBarIncrement != 0) {
            this.mSeekBar.setKeyProgressIncrement(this.mSeekBarIncrement);
        } else {
            this.mSeekBarIncrement = this.mSeekBar.getKeyProgressIncrement();
        }
        this.mSeekBar.setProgress(this.mSeekBarValue - this.mMin);
        this.mSeekBar.setEnabled(isEnabled());
    }

    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        int persistedInt;
        if (restoreValue) {
            persistedInt = getPersistedInt(this.mSeekBarValue);
        } else {
            persistedInt = ((Integer) defaultValue).intValue();
        }
        setValue(persistedInt);
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Integer.valueOf(a.getInt(index, 0));
    }

    public final void setMax(int max) {
        if (max < this.mMin) {
            max = this.mMin;
        }
        if (max != this.mMax) {
            this.mMax = max;
            notifyChanged();
        }
    }

    public final void setSeekBarIncrement(int seekBarIncrement) {
        if (seekBarIncrement != this.mSeekBarIncrement) {
            this.mSeekBarIncrement = Math.min(this.mMax - this.mMin, Math.abs(seekBarIncrement));
            notifyChanged();
        }
    }

    public void setValue(int seekBarValue) {
        setValueInternal(seekBarValue, true);
    }

    private void setValueInternal(int seekBarValue, boolean notifyChanged) {
        if (seekBarValue < this.mMin) {
            seekBarValue = this.mMin;
        }
        if (seekBarValue > this.mMax) {
            seekBarValue = this.mMax;
        }
        if (seekBarValue != this.mSeekBarValue) {
            this.mSeekBarValue = seekBarValue;
            persistInt(seekBarValue);
            if (notifyChanged) {
                notifyChanged();
            }
        }
    }

    private void syncValueInternal(SeslSeekBar seekBar) {
        int seekBarValue = this.mMin + seekBar.getProgress();
        if (seekBarValue == this.mSeekBarValue) {
            return;
        }
        if (callChangeListener(Integer.valueOf(seekBarValue))) {
            setValueInternal(seekBarValue, false);
        } else {
            seekBar.setProgress(this.mSeekBarValue - this.mMin);
        }
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        myState.seekBarValue = this.mSeekBarValue;
        myState.min = this.mMin;
        myState.max = this.mMax;
        return myState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state.getClass().equals(SavedState.class)) {
            SavedState myState = (SavedState) state;
            super.onRestoreInstanceState(myState.getSuperState());
            this.mSeekBarValue = myState.seekBarValue;
            this.mMin = myState.min;
            this.mMax = myState.max;
            notifyChanged();
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
