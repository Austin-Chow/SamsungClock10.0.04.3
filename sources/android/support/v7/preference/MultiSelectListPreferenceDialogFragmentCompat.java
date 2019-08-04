package android.support.v7.preference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.preference.internal.AbstractMultiSelectListPreference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectListPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private Set<String> mNewValues = new HashSet();
    private boolean mPreferenceChanged;

    /* renamed from: android.support.v7.preference.MultiSelectListPreferenceDialogFragmentCompat$1 */
    class C02541 implements OnMultiChoiceClickListener {
        C02541() {
        }

        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if (isChecked) {
                MultiSelectListPreferenceDialogFragmentCompat.this.mPreferenceChanged = MultiSelectListPreferenceDialogFragmentCompat.this.mPreferenceChanged | MultiSelectListPreferenceDialogFragmentCompat.this.mNewValues.add(MultiSelectListPreferenceDialogFragmentCompat.this.mEntryValues[which].toString());
            } else {
                MultiSelectListPreferenceDialogFragmentCompat.this.mPreferenceChanged = MultiSelectListPreferenceDialogFragmentCompat.this.mPreferenceChanged | MultiSelectListPreferenceDialogFragmentCompat.this.mNewValues.remove(MultiSelectListPreferenceDialogFragmentCompat.this.mEntryValues[which].toString());
            }
        }
    }

    public static MultiSelectListPreferenceDialogFragmentCompat newInstance(String key) {
        MultiSelectListPreferenceDialogFragmentCompat fragment = new MultiSelectListPreferenceDialogFragmentCompat();
        Bundle b = new Bundle(1);
        b.putString("key", key);
        fragment.setArguments(b);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AbstractMultiSelectListPreference preference = getListPreference();
            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException("MultiSelectListPreference requires an entries array and an entryValues array.");
            }
            this.mNewValues.clear();
            this.mNewValues.addAll(preference.getValues());
            this.mPreferenceChanged = false;
            this.mEntries = preference.getEntries();
            this.mEntryValues = preference.getEntryValues();
            return;
        }
        this.mNewValues.clear();
        this.mNewValues.addAll(savedInstanceState.getStringArrayList("MultiSelectListPreferenceDialogFragmentCompat.values"));
        this.mPreferenceChanged = savedInstanceState.getBoolean("MultiSelectListPreferenceDialogFragmentCompat.changed", false);
        this.mEntries = savedInstanceState.getCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entries");
        this.mEntryValues = savedInstanceState.getCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entryValues");
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("MultiSelectListPreferenceDialogFragmentCompat.values", new ArrayList(this.mNewValues));
        outState.putBoolean("MultiSelectListPreferenceDialogFragmentCompat.changed", this.mPreferenceChanged);
        outState.putCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entries", this.mEntries);
        outState.putCharSequenceArray("MultiSelectListPreferenceDialogFragmentCompat.entryValues", this.mEntryValues);
    }

    private AbstractMultiSelectListPreference getListPreference() {
        return (AbstractMultiSelectListPreference) getPreference();
    }

    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        int entryCount = this.mEntryValues.length;
        boolean[] checkedItems = new boolean[entryCount];
        for (int i = 0; i < entryCount; i++) {
            checkedItems[i] = this.mNewValues.contains(this.mEntryValues[i].toString());
        }
        builder.setMultiChoiceItems(this.mEntries, checkedItems, new C02541());
    }

    public void onDialogClosed(boolean positiveResult) {
        AbstractMultiSelectListPreference preference = getListPreference();
        if (positiveResult && this.mPreferenceChanged) {
            Set<String> values = this.mNewValues;
            if (preference.callChangeListener(values)) {
                preference.setValues(values);
            }
        }
        this.mPreferenceChanged = false;
    }
}
