package android.support.v7.preference;

import android.os.Handler;
import android.support.v7.preference.PreferenceManager.PreferenceComparisonCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.Callback;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class PreferenceGroupAdapter extends Adapter<PreferenceViewHolder> implements OnPreferenceChangeInternalListener {
    private int mCategoryLayoutId = C0263R.layout.sesl_preference_category;
    private Handler mHandler = new Handler();
    boolean mIsCategoryAfter = false;
    Preference mNextGroupPreference = null;
    Preference mNextPreference = null;
    private PreferenceGroup mPreferenceGroup;
    private List<PreferenceLayout> mPreferenceLayouts;
    private List<Preference> mPreferenceList;
    private List<Preference> mPreferenceListInternal;
    Preference mPrevPreference = null;
    private Runnable mSyncRunnable = new C02601();
    private PreferenceLayout mTempPreferenceLayout = new PreferenceLayout();

    /* renamed from: android.support.v7.preference.PreferenceGroupAdapter$1 */
    class C02601 implements Runnable {
        C02601() {
        }

        public void run() {
            PreferenceGroupAdapter.this.syncMyPreferences();
        }
    }

    private static class PreferenceLayout {
        private String name;
        private int resId;
        private int widgetResId;

        public PreferenceLayout(PreferenceLayout other) {
            this.resId = other.resId;
            this.widgetResId = other.widgetResId;
            this.name = other.name;
        }

        public boolean equals(Object o) {
            if (!(o instanceof PreferenceLayout)) {
                return false;
            }
            PreferenceLayout other = (PreferenceLayout) o;
            if (this.resId == other.resId && this.widgetResId == other.widgetResId && TextUtils.equals(this.name, other.name)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return ((((this.resId + 527) * 31) + this.widgetResId) * 31) + this.name.hashCode();
        }
    }

    public PreferenceGroupAdapter(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
        this.mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        this.mPreferenceList = new ArrayList();
        this.mPreferenceListInternal = new ArrayList();
        this.mPreferenceLayouts = new ArrayList();
        if (this.mPreferenceGroup instanceof PreferenceScreen) {
            setHasStableIds(((PreferenceScreen) this.mPreferenceGroup).shouldUseGeneratedIds());
        } else {
            setHasStableIds(true);
        }
        syncMyPreferences();
    }

    private void syncMyPreferences() {
        for (Preference preference : this.mPreferenceListInternal) {
            preference.setOnPreferenceChangeInternalListener(null);
        }
        List<Preference> fullPreferenceList = new ArrayList(this.mPreferenceListInternal.size());
        flattenPreferenceGroup(fullPreferenceList, this.mPreferenceGroup);
        final List<Preference> visiblePreferenceList = new ArrayList(fullPreferenceList.size());
        for (Preference preference2 : fullPreferenceList) {
            if (preference2.isVisible()) {
                visiblePreferenceList.add(preference2);
            }
        }
        final List<Preference> oldVisibleList = this.mPreferenceList;
        this.mPreferenceList = visiblePreferenceList;
        this.mPreferenceListInternal = fullPreferenceList;
        PreferenceManager preferenceManager = this.mPreferenceGroup.getPreferenceManager();
        if (preferenceManager == null || preferenceManager.getPreferenceComparisonCallback() == null) {
            notifyDataSetChanged();
        } else {
            final PreferenceComparisonCallback comparisonCallback = preferenceManager.getPreferenceComparisonCallback();
            DiffUtil.calculateDiff(new Callback() {
                public int getOldListSize() {
                    return oldVisibleList.size();
                }

                public int getNewListSize() {
                    return visiblePreferenceList.size();
                }

                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return comparisonCallback.arePreferenceItemsTheSame((Preference) oldVisibleList.get(oldItemPosition), (Preference) visiblePreferenceList.get(newItemPosition));
                }

                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return comparisonCallback.arePreferenceContentsTheSame((Preference) oldVisibleList.get(oldItemPosition), (Preference) visiblePreferenceList.get(newItemPosition));
                }
            }).dispatchUpdatesTo((Adapter) this);
        }
        for (Preference preference22 : fullPreferenceList) {
            preference22.clearWasDetached();
        }
    }

    private void flattenPreferenceGroup(List<Preference> preferences, PreferenceGroup group) {
        group.sortPreferences();
        int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            Preference preference = group.getPreference(i);
            if (i == groupSize - 1) {
                this.mNextPreference = null;
                if (this.mIsCategoryAfter && preference == this.mNextGroupPreference) {
                    this.mNextGroupPreference = null;
                }
            } else {
                this.mNextPreference = group.getPreference(i + 1);
                if (preference == this.mNextGroupPreference) {
                    this.mNextGroupPreference = null;
                }
            }
            if (preference instanceof PreferenceCategory) {
                if (!preference.mIsRoundChanged) {
                    preference.seslSetSubheaderRoundedBg(15);
                }
                preference.mIsSolidRoundedCorner = group.mIsSolidRoundedCorner;
                preference.seslSetSubheaderColor(group.mSubheaderColor);
            }
            preferences.add(preference);
            if ((preference instanceof PreferenceCategory) && TextUtils.isEmpty(preference.getTitle()) && this.mCategoryLayoutId == preference.getLayoutResource()) {
                preference.setLayoutResource(C0263R.layout.sesl_preference_category_empty);
            }
            addPreferenceClassName(preference);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceAsGroup = (PreferenceGroup) preference;
                if (preferenceAsGroup.isOnSameScreenAsChildren()) {
                    this.mNextGroupPreference = this.mNextPreference;
                    flattenPreferenceGroup(preferences, preferenceAsGroup);
                }
            }
            preference.setOnPreferenceChangeInternalListener(this);
        }
    }

    private PreferenceLayout createPreferenceLayout(Preference preference, PreferenceLayout in) {
        PreferenceLayout pl = in != null ? in : new PreferenceLayout();
        pl.name = preference.getClass().getName();
        pl.resId = preference.getLayoutResource();
        pl.widgetResId = preference.getWidgetLayoutResource();
        return pl;
    }

    private void addPreferenceClassName(Preference preference) {
        PreferenceLayout pl = createPreferenceLayout(preference, null);
        if (!this.mPreferenceLayouts.contains(pl)) {
            this.mPreferenceLayouts.add(pl);
        }
    }

    public int getItemCount() {
        return this.mPreferenceList.size();
    }

    public Preference getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return (Preference) this.mPreferenceList.get(position);
    }

    public long getItemId(int position) {
        if (hasStableIds()) {
            return getItem(position).getId();
        }
        return -1;
    }

    public void onPreferenceChange(Preference preference) {
        int index = this.mPreferenceList.indexOf(preference);
        if (index != -1) {
            notifyItemChanged(index, preference);
        }
    }

    public void onPreferenceHierarchyChange(Preference preference) {
        this.mHandler.removeCallbacks(this.mSyncRunnable);
        this.mHandler.post(this.mSyncRunnable);
    }

    public int getItemViewType(int position) {
        this.mTempPreferenceLayout = createPreferenceLayout(getItem(position), this.mTempPreferenceLayout);
        int viewType = this.mPreferenceLayouts.indexOf(this.mTempPreferenceLayout);
        if (viewType != -1) {
            return viewType;
        }
        viewType = this.mPreferenceLayouts.size();
        this.mPreferenceLayouts.add(new PreferenceLayout(this.mTempPreferenceLayout));
        int i = viewType;
        return viewType;
    }

    public PreferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PreferenceLayout pl = (PreferenceLayout) this.mPreferenceLayouts.get(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(pl.resId, parent, false);
        ViewGroup widgetFrame = (ViewGroup) view.findViewById(16908312);
        if (widgetFrame != null) {
            if (pl.widgetResId != 0) {
                inflater.inflate(pl.widgetResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(8);
            }
        }
        return new PreferenceViewHolder(view);
    }

    public void onBindViewHolder(PreferenceViewHolder holder, int position) {
        getItem(position).onBindViewHolder(holder);
    }
}
