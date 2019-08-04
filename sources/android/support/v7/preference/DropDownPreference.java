package android.support.v7.preference;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

public class DropDownPreference extends ListPreference {
    private final ArrayAdapter mAdapter;
    private final Context mContext;
    private final OnItemSelectedListener mItemSelectedListener;
    private AppCompatSpinner mSpinner;

    /* renamed from: android.support.v7.preference.DropDownPreference$1 */
    class C02501 implements OnItemSelectedListener {
        C02501() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            if (position >= 0) {
                String value = DropDownPreference.this.getEntryValues()[position].toString();
                if (!value.equals(DropDownPreference.this.getValue()) && DropDownPreference.this.callChangeListener(value)) {
                    DropDownPreference.this.setValue(value);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public DropDownPreference(Context context, AttributeSet attrs) {
        this(context, attrs, C0263R.attr.dropdownPreferenceStyle);
    }

    public DropDownPreference(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public DropDownPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mItemSelectedListener = new C02501();
        this.mContext = context;
        this.mAdapter = createAdapter();
        updateEntries();
    }

    protected void onClick() {
        this.mSpinner.performClick();
    }

    protected ArrayAdapter createAdapter() {
        return new ArrayAdapter(this.mContext, C0263R.layout.support_simple_spinner_dropdown_item);
    }

    private void updateEntries() {
        this.mAdapter.clear();
        if (getEntries() != null) {
            for (CharSequence c : getEntries()) {
                this.mAdapter.add(c.toString());
            }
        }
    }

    public int findSpinnerIndexOfValue(String value) {
        CharSequence[] entryValues = getEntryValues();
        if (!(value == null || entryValues == null)) {
            for (int i = entryValues.length - 1; i >= 0; i--) {
                if (entryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    protected void notifyChanged() {
        super.notifyChanged();
        this.mAdapter.notifyDataSetChanged();
    }

    public void onBindViewHolder(PreferenceViewHolder view) {
        this.mSpinner = (AppCompatSpinner) view.itemView.findViewById(C0263R.id.spinner);
        this.mSpinner.setSoundEffectsEnabled(false);
        if (!this.mAdapter.equals(this.mSpinner.getAdapter())) {
            this.mSpinner.setAdapter(this.mAdapter);
        }
        this.mSpinner.setOnItemSelectedListener(this.mItemSelectedListener);
        this.mSpinner.setSelection(findSpinnerIndexOfValue(getValue()));
        super.onBindViewHolder(view);
    }
}
