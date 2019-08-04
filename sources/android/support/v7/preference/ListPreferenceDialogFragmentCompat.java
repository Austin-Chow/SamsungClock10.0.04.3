package android.support.v7.preference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import java.util.ArrayList;

public class ListPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private int mClickedDialogEntryIndex;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    /* renamed from: android.support.v7.preference.ListPreferenceDialogFragmentCompat$1 */
    class C02531 implements OnClickListener {
        C02531() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ListPreferenceDialogFragmentCompat.this.mClickedDialogEntryIndex = which;
            ListPreferenceDialogFragmentCompat.this.onClick(dialog, -1);
            dialog.dismiss();
        }
    }

    public static ListPreferenceDialogFragmentCompat newInstance(String key) {
        ListPreferenceDialogFragmentCompat fragment = new ListPreferenceDialogFragmentCompat();
        Bundle b = new Bundle(1);
        b.putString("key", key);
        fragment.setArguments(b);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            ListPreference preference = getListPreference();
            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            }
            this.mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
            this.mEntries = preference.getEntries();
            this.mEntryValues = preference.getEntryValues();
            return;
        }
        this.mClickedDialogEntryIndex = savedInstanceState.getInt("ListPreferenceDialogFragment.index", 0);
        this.mEntries = getCharSequenceArray(savedInstanceState, "ListPreferenceDialogFragment.entries");
        this.mEntryValues = getCharSequenceArray(savedInstanceState, "ListPreferenceDialogFragment.entryValues");
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ListPreferenceDialogFragment.index", this.mClickedDialogEntryIndex);
        putCharSequenceArray(outState, "ListPreferenceDialogFragment.entries", this.mEntries);
        putCharSequenceArray(outState, "ListPreferenceDialogFragment.entryValues", this.mEntryValues);
    }

    private static void putCharSequenceArray(Bundle out, String key, CharSequence[] entries) {
        ArrayList<String> stored = new ArrayList(entries.length);
        for (CharSequence cs : entries) {
            stored.add(cs.toString());
        }
        out.putStringArrayList(key, stored);
    }

    private static CharSequence[] getCharSequenceArray(Bundle in, String key) {
        ArrayList<String> stored = in.getStringArrayList(key);
        return stored == null ? null : (CharSequence[]) stored.toArray(new CharSequence[stored.size()]);
    }

    private ListPreference getListPreference() {
        return (ListPreference) getPreference();
    }

    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setSingleChoiceItems(this.mEntries, this.mClickedDialogEntryIndex, new C02531());
        builder.setPositiveButton(null, null);
    }

    public void onDialogClosed(boolean positiveResult) {
        ListPreference preference = getListPreference();
        if (positiveResult && this.mClickedDialogEntryIndex >= 0) {
            String value = this.mEntryValues[this.mClickedDialogEntryIndex].toString();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }
}
