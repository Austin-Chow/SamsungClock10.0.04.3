package android.support.v7.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.Secure;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.PreferenceManager.OnPreferenceTreeClickListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Preference implements Comparable<Preference> {
    private boolean mAllowDividerAbove;
    private boolean mAllowDividerBelow;
    private boolean mBaseMethodCalled;
    private boolean mChangedSummaryColor;
    private boolean mChangedSummaryColorStateList;
    private final OnClickListener mClickListener;
    private Context mContext;
    private Object mDefaultValue;
    private String mDependencyKey;
    private boolean mDependencyMet;
    private List<Preference> mDependents;
    private boolean mEnabled;
    private Bundle mExtras;
    private String mFragment;
    private boolean mHasId;
    private boolean mHasSingleLineTitleAttr;
    private Drawable mIcon;
    private int mIconResId;
    private boolean mIconSpaceReserved;
    private long mId;
    private Intent mIntent;
    boolean mIsPreferenceRoundedBg;
    boolean mIsRoundChanged;
    public boolean mIsSolidRoundedCorner;
    private String mKey;
    private int mLayoutResId;
    private OnPreferenceChangeInternalListener mListener;
    private OnPreferenceChangeListener mOnChangeListener;
    private OnPreferenceClickListener mOnClickListener;
    private int mOrder;
    private boolean mParentDependencyMet;
    private PreferenceGroup mParentGroup;
    private boolean mPersistent;
    private PreferenceDataStore mPreferenceDataStore;
    private PreferenceManager mPreferenceManager;
    private boolean mSelectable;
    private boolean mShouldDisableView;
    private boolean mSingleLineTitle;
    int mSubheaderColor;
    boolean mSubheaderRound;
    private CharSequence mSummary;
    private int mSummaryColor;
    private ColorStateList mSummaryColorStateList;
    private ColorStateList mTextColorSecondary;
    private CharSequence mTitle;
    private int mViewId;
    private boolean mVisible;
    private boolean mWasDetached;
    int mWhere;
    private int mWidgetLayoutResId;

    public static class BaseSavedState extends AbsSavedState {
        public static final Creator<BaseSavedState> CREATOR = new C02561();

        /* renamed from: android.support.v7.preference.Preference$BaseSavedState$1 */
        static class C02561 implements Creator<BaseSavedState> {
            C02561() {
            }

            public BaseSavedState createFromParcel(Parcel in) {
                return new BaseSavedState(in);
            }

            public BaseSavedState[] newArray(int size) {
                return new BaseSavedState[size];
            }
        }

        public BaseSavedState(Parcel source) {
            super(source);
        }

        public BaseSavedState(Parcelable superState) {
            super(superState);
        }
    }

    /* renamed from: android.support.v7.preference.Preference$1 */
    class C02551 implements OnClickListener {
        C02551() {
        }

        public void onClick(View v) {
            Preference.this.performClick(v);
        }
    }

    interface OnPreferenceChangeInternalListener {
        void onPreferenceChange(Preference preference);

        void onPreferenceHierarchyChange(Preference preference);
    }

    public interface OnPreferenceChangeListener {
        boolean onPreferenceChange(Preference preference, Object obj);
    }

    public interface OnPreferenceClickListener {
        boolean onPreferenceClick(Preference preference);
    }

    public Preference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mOrder = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mViewId = 0;
        this.mEnabled = true;
        this.mSelectable = true;
        this.mPersistent = true;
        this.mDependencyMet = true;
        this.mParentDependencyMet = true;
        this.mVisible = true;
        this.mAllowDividerAbove = true;
        this.mAllowDividerBelow = true;
        this.mSingleLineTitle = true;
        this.mIsSolidRoundedCorner = false;
        this.mIsPreferenceRoundedBg = false;
        this.mSubheaderRound = false;
        this.mWhere = 0;
        this.mIsRoundChanged = false;
        this.mChangedSummaryColor = false;
        this.mChangedSummaryColorStateList = false;
        this.mShouldDisableView = true;
        this.mLayoutResId = C0263R.layout.sesl_preference;
        this.mClickListener = new C02551();
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, C0263R.styleable.Preference, defStyleAttr, defStyleRes);
        this.mIconResId = TypedArrayUtils.getResourceId(a, C0263R.styleable.Preference_icon, C0263R.styleable.Preference_android_icon, 0);
        this.mKey = TypedArrayUtils.getString(a, C0263R.styleable.Preference_key, C0263R.styleable.Preference_android_key);
        this.mTitle = TypedArrayUtils.getText(a, C0263R.styleable.Preference_title, C0263R.styleable.Preference_android_title);
        this.mSummary = TypedArrayUtils.getText(a, C0263R.styleable.Preference_summary, C0263R.styleable.Preference_android_summary);
        this.mOrder = TypedArrayUtils.getInt(a, C0263R.styleable.Preference_order, C0263R.styleable.Preference_android_order, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        this.mFragment = TypedArrayUtils.getString(a, C0263R.styleable.Preference_fragment, C0263R.styleable.Preference_android_fragment);
        this.mLayoutResId = TypedArrayUtils.getResourceId(a, C0263R.styleable.Preference_layout, C0263R.styleable.Preference_android_layout, C0263R.layout.preference);
        this.mWidgetLayoutResId = TypedArrayUtils.getResourceId(a, C0263R.styleable.Preference_widgetLayout, C0263R.styleable.Preference_android_widgetLayout, 0);
        this.mEnabled = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_enabled, C0263R.styleable.Preference_android_enabled, true);
        this.mSelectable = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_selectable, C0263R.styleable.Preference_android_selectable, true);
        this.mPersistent = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_persistent, C0263R.styleable.Preference_android_persistent, true);
        this.mDependencyKey = TypedArrayUtils.getString(a, C0263R.styleable.Preference_dependency, C0263R.styleable.Preference_android_dependency);
        this.mAllowDividerAbove = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_allowDividerAbove, C0263R.styleable.Preference_allowDividerAbove, this.mSelectable);
        this.mAllowDividerBelow = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_allowDividerBelow, C0263R.styleable.Preference_allowDividerBelow, this.mSelectable);
        if (a.hasValue(C0263R.styleable.Preference_defaultValue)) {
            this.mDefaultValue = onGetDefaultValue(a, C0263R.styleable.Preference_defaultValue);
        } else if (a.hasValue(C0263R.styleable.Preference_android_defaultValue)) {
            this.mDefaultValue = onGetDefaultValue(a, C0263R.styleable.Preference_android_defaultValue);
        }
        this.mShouldDisableView = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_shouldDisableView, C0263R.styleable.Preference_android_shouldDisableView, true);
        this.mHasSingleLineTitleAttr = a.hasValue(C0263R.styleable.Preference_singleLineTitle);
        if (this.mHasSingleLineTitleAttr) {
            this.mSingleLineTitle = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_singleLineTitle, C0263R.styleable.Preference_android_singleLineTitle, true);
        }
        this.mIconSpaceReserved = TypedArrayUtils.getBoolean(a, C0263R.styleable.Preference_iconSpaceReserved, C0263R.styleable.Preference_android_iconSpaceReserved, false);
        a.recycle();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(16842808, outValue, true);
        if (outValue.resourceId > 0) {
            this.mTextColorSecondary = context.getResources().getColorStateList(outValue.resourceId);
        }
    }

    public Preference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Preference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, C0263R.attr.preferenceStyle, 16842894));
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return null;
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public String getFragment() {
        return this.mFragment;
    }

    public PreferenceDataStore getPreferenceDataStore() {
        if (this.mPreferenceDataStore != null) {
            return this.mPreferenceDataStore;
        }
        if (this.mPreferenceManager != null) {
            return this.mPreferenceManager.getPreferenceDataStore();
        }
        return null;
    }

    public Bundle getExtras() {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        return this.mExtras;
    }

    public void setLayoutResource(int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    public final int getLayoutResource() {
        return this.mLayoutResId;
    }

    public void setWidgetLayoutResource(int widgetLayoutResId) {
        this.mWidgetLayoutResId = widgetLayoutResId;
    }

    public final int getWidgetLayoutResource() {
        return this.mWidgetLayoutResId;
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        int i = 4;
        holder.itemView.setOnClickListener(this.mClickListener);
        holder.itemView.setId(this.mViewId);
        holder.seslSetPreferenceBackgroundType(this.mIsPreferenceRoundedBg, this.mWhere, this.mSubheaderRound);
        TextView titleView = (TextView) holder.findViewById(16908310);
        if (titleView != null) {
            CharSequence title = getTitle();
            if (!TextUtils.isEmpty(title)) {
                titleView.setText(title);
                titleView.setVisibility(0);
                if (this.mHasSingleLineTitleAttr) {
                    titleView.setSingleLine(this.mSingleLineTitle);
                }
            } else if (TextUtils.isEmpty(title) && (this instanceof PreferenceCategory)) {
                titleView.setVisibility(0);
                if (this.mHasSingleLineTitleAttr) {
                    titleView.setSingleLine(this.mSingleLineTitle);
                }
            } else {
                titleView.setVisibility(8);
            }
        }
        TextView summaryView = (TextView) holder.findViewById(16908304);
        if (summaryView != null) {
            CharSequence summary = getSummary();
            if (TextUtils.isEmpty(summary)) {
                summaryView.setVisibility(8);
            } else {
                summaryView.setText(summary);
                if (this.mChangedSummaryColor) {
                    summaryView.setTextColor(this.mSummaryColor);
                    Log.d("Preference", "set Summary Color : " + this.mSummaryColor);
                } else if (this.mChangedSummaryColorStateList) {
                    summaryView.setTextColor(this.mSummaryColorStateList);
                    Log.d("Preference", "set Summary ColorStateList : " + this.mSummaryColorStateList);
                } else if (this.mTextColorSecondary != null) {
                    summaryView.setTextColor(this.mTextColorSecondary);
                }
                summaryView.setVisibility(0);
            }
        }
        ImageView imageView = (ImageView) holder.findViewById(16908294);
        if (imageView != null) {
            if (!((this.mIconResId == 0 && this.mIcon == null) || this.mIcon == null)) {
                imageView.setImageDrawable(this.mIcon);
            }
            if (this.mIcon != null) {
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(this.mIconSpaceReserved ? 4 : 8);
            }
        }
        View imageFrame = holder.findViewById(C0263R.id.icon_frame);
        if (imageFrame == null) {
            imageFrame = holder.findViewById(16908350);
        }
        if (imageFrame != null) {
            if (this.mIcon != null) {
                imageFrame.setVisibility(0);
            } else {
                if (!this.mIconSpaceReserved) {
                    i = 8;
                }
                imageFrame.setVisibility(i);
            }
        }
        if (this.mShouldDisableView) {
            setEnabledStateOnViews(holder.itemView, isEnabled());
        } else {
            setEnabledStateOnViews(holder.itemView, true);
        }
        boolean selectable = isSelectable();
        holder.itemView.setFocusable(selectable);
        holder.itemView.setClickable(selectable);
        holder.setDividerAllowedAbove(this.mAllowDividerAbove);
        holder.setDividerAllowedBelow(this.mAllowDividerBelow);
    }

    private void setEnabledStateOnViews(View v, boolean enabled) {
        v.setEnabled(enabled);
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(vg.getChildAt(i), enabled);
            }
        }
    }

    public void setOrder(int order) {
        if (order != this.mOrder) {
            this.mOrder = order;
            notifyHierarchyChanged();
        }
    }

    public int getOrder() {
        return this.mOrder;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    protected boolean isTalkBackIsRunning() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            String enabledServices = Secure.getString(getContext().getContentResolver(), "enabled_accessibility_services");
            if (enabledServices != null && (enabledServices.matches("(?i).*com.samsung.accessibility/com.samsung.android.app.talkback.TalkBackService.*") || enabledServices.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*") || enabledServices.matches("(?i).*com.samsung.accessibility/com.samsung.accessibility.universalswitch.UniversalSwitchService.*"))) {
                return true;
            }
        }
        return false;
    }

    public void seslSetSubheaderRoundedBg(int where) {
        this.mIsPreferenceRoundedBg = true;
        this.mWhere = where;
        this.mSubheaderRound = true;
        this.mIsRoundChanged = true;
    }

    public void seslSetSubheaderColor(int color) {
        this.mSubheaderColor = color;
    }

    public boolean isEnabled() {
        return this.mEnabled && this.mDependencyMet && this.mParentDependencyMet;
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public final boolean isVisible() {
        return this.mVisible;
    }

    long getId() {
        return this.mId;
    }

    protected void onClick() {
    }

    public String getKey() {
        return this.mKey;
    }

    public boolean hasKey() {
        return !TextUtils.isEmpty(this.mKey);
    }

    public boolean isPersistent() {
        return this.mPersistent;
    }

    protected boolean shouldPersist() {
        return this.mPreferenceManager != null && isPersistent() && hasKey();
    }

    public boolean callChangeListener(Object newValue) {
        return this.mOnChangeListener == null || this.mOnChangeListener.onPreferenceChange(this, newValue);
    }

    protected void callClickListener() {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onPreferenceClick(this);
        }
    }

    public void setOnPreferenceClickListener(OnPreferenceClickListener onPreferenceClickListener) {
        this.mOnClickListener = onPreferenceClickListener;
    }

    protected void performClick(View view) {
        performClick();
    }

    public void performClick() {
        if (isEnabled()) {
            onClick();
            if (this.mOnClickListener == null || !this.mOnClickListener.onPreferenceClick(this)) {
                PreferenceManager preferenceManager = getPreferenceManager();
                if (preferenceManager != null) {
                    OnPreferenceTreeClickListener listener = preferenceManager.getOnPreferenceTreeClickListener();
                    if (listener != null && listener.onPreferenceTreeClick(this)) {
                        return;
                    }
                }
                if (this.mIntent != null) {
                    getContext().startActivity(this.mIntent);
                }
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public SharedPreferences getSharedPreferences() {
        if (this.mPreferenceManager == null || getPreferenceDataStore() != null) {
            return null;
        }
        return this.mPreferenceManager.getSharedPreferences();
    }

    public int compareTo(Preference another) {
        if (this.mOrder != another.mOrder) {
            return this.mOrder - another.mOrder;
        }
        if (this.mTitle == another.mTitle) {
            return 0;
        }
        if (this.mTitle == null) {
            return 1;
        }
        if (another.mTitle == null) {
            return -1;
        }
        return this.mTitle.toString().compareToIgnoreCase(another.mTitle.toString());
    }

    final void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener listener) {
        this.mListener = listener;
    }

    protected void notifyChanged() {
        if (this.mListener != null) {
            this.mListener.onPreferenceChange(this);
        }
    }

    protected void notifyHierarchyChanged() {
        if (this.mListener != null) {
            this.mListener.onPreferenceHierarchyChange(this);
        }
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        if (!this.mHasId) {
            this.mId = preferenceManager.getNextId();
        }
        dispatchSetInitialValue();
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager, long id) {
        this.mId = id;
        this.mHasId = true;
        try {
            onAttachedToHierarchy(preferenceManager);
        } finally {
            this.mHasId = false;
        }
    }

    void assignParent(PreferenceGroup parentGroup) {
        this.mParentGroup = parentGroup;
    }

    public void onAttached() {
        registerDependency();
    }

    public void onDetached() {
        unregisterDependency();
        this.mWasDetached = true;
    }

    public final void clearWasDetached() {
        this.mWasDetached = false;
    }

    private void registerDependency() {
        if (!TextUtils.isEmpty(this.mDependencyKey)) {
            Preference preference = findPreferenceInHierarchy(this.mDependencyKey);
            if (preference != null) {
                preference.registerDependent(this);
                return;
            }
            throw new IllegalStateException("Dependency \"" + this.mDependencyKey + "\" not found for preference \"" + this.mKey + "\" (title: \"" + this.mTitle + "\"");
        }
    }

    private void unregisterDependency() {
        if (this.mDependencyKey != null) {
            Preference oldDependency = findPreferenceInHierarchy(this.mDependencyKey);
            if (oldDependency != null) {
                oldDependency.unregisterDependent(this);
            }
        }
    }

    protected Preference findPreferenceInHierarchy(String key) {
        if (TextUtils.isEmpty(key) || this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(key);
    }

    private void registerDependent(Preference dependent) {
        if (this.mDependents == null) {
            this.mDependents = new ArrayList();
        }
        this.mDependents.add(dependent);
        dependent.onDependencyChanged(this, shouldDisableDependents());
    }

    private void unregisterDependent(Preference dependent) {
        if (this.mDependents != null) {
            this.mDependents.remove(dependent);
        }
    }

    public void notifyDependencyChange(boolean disableDependents) {
        List<Preference> dependents = this.mDependents;
        if (dependents != null) {
            int dependentsCount = dependents.size();
            for (int i = 0; i < dependentsCount; i++) {
                ((Preference) dependents.get(i)).onDependencyChanged(this, disableDependents);
            }
        }
    }

    public void onDependencyChanged(Preference dependency, boolean disableDependent) {
        if (this.mDependencyMet == disableDependent) {
            this.mDependencyMet = !disableDependent;
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    public void onParentChanged(Preference parent, boolean disableChild) {
        if (this.mParentDependencyMet == disableChild) {
            this.mParentDependencyMet = !disableChild;
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    public boolean shouldDisableDependents() {
        return !isEnabled();
    }

    public PreferenceGroup getParent() {
        return this.mParentGroup;
    }

    protected void onPrepareForRemoval() {
        unregisterDependency();
    }

    private void dispatchSetInitialValue() {
        if (getPreferenceDataStore() != null) {
            onSetInitialValue(true, this.mDefaultValue);
        } else if (shouldPersist() && getSharedPreferences().contains(this.mKey)) {
            onSetInitialValue(true, null);
        } else if (this.mDefaultValue != null) {
            onSetInitialValue(false, this.mDefaultValue);
        }
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    }

    private void tryCommit(Editor editor) {
        if (this.mPreferenceManager.shouldCommit()) {
            editor.apply();
        }
    }

    protected boolean persistString(String value) {
        if (!shouldPersist()) {
            return false;
        }
        if (TextUtils.equals(value, getPersistedString(null))) {
            return true;
        }
        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putString(this.mKey, value);
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putString(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected String getPersistedString(String defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getString(this.mKey, defaultReturnValue);
        }
        return this.mPreferenceManager.getSharedPreferences().getString(this.mKey, defaultReturnValue);
    }

    protected boolean persistInt(int value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedInt(value ^ -1)) {
            return true;
        }
        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putInt(this.mKey, value);
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putInt(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected int getPersistedInt(int defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getInt(this.mKey, defaultReturnValue);
        }
        return this.mPreferenceManager.getSharedPreferences().getInt(this.mKey, defaultReturnValue);
    }

    protected boolean persistBoolean(boolean value) {
        boolean z = false;
        if (!shouldPersist()) {
            return false;
        }
        if (!value) {
            z = true;
        }
        if (value == getPersistedBoolean(z)) {
            return true;
        }
        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            dataStore.putBoolean(this.mKey, value);
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putBoolean(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected boolean getPersistedBoolean(boolean defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }
        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return dataStore.getBoolean(this.mKey, defaultReturnValue);
        }
        return this.mPreferenceManager.getSharedPreferences().getBoolean(this.mKey, defaultReturnValue);
    }

    public String toString() {
        return getFilterableStringBuilder().toString();
    }

    StringBuilder getFilterableStringBuilder() {
        StringBuilder sb = new StringBuilder();
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            sb.append(title).append(' ');
        }
        CharSequence summary = getSummary();
        if (!TextUtils.isEmpty(summary)) {
            sb.append(summary).append(' ');
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb;
    }

    public void saveHierarchyState(Bundle container) {
        dispatchSaveInstanceState(container);
    }

    void dispatchSaveInstanceState(Bundle container) {
        if (hasKey()) {
            this.mBaseMethodCalled = false;
            Parcelable state = onSaveInstanceState();
            if (!this.mBaseMethodCalled) {
                throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            } else if (state != null) {
                container.putParcelable(this.mKey, state);
            }
        }
    }

    protected Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        return BaseSavedState.EMPTY_STATE;
    }

    public void restoreHierarchyState(Bundle container) {
        dispatchRestoreInstanceState(container);
    }

    void dispatchRestoreInstanceState(Bundle container) {
        if (hasKey()) {
            Parcelable state = container.getParcelable(this.mKey);
            if (state != null) {
                this.mBaseMethodCalled = false;
                onRestoreInstanceState(state);
                if (!this.mBaseMethodCalled) {
                    throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
                }
            }
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        this.mBaseMethodCalled = true;
        if (state != BaseSavedState.EMPTY_STATE && state != null) {
            throw new IllegalArgumentException("Wrong state class -- expecting Preference State");
        }
    }
}
