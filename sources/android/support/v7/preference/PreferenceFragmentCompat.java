package android.support.v7.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.DialogPreference.TargetFragment;
import android.support.v7.preference.PreferenceManager.OnDisplayPreferenceDialogListener;
import android.support.v7.preference.PreferenceManager.OnNavigateToScreenListener;
import android.support.v7.preference.PreferenceManager.OnPreferenceTreeClickListener;
import android.support.v7.preference.internal.AbstractMultiSelectListPreference;
import android.support.v7.util.SeslRoundedCorner;
import android.support.v7.util.SeslSubheaderRoundedCorner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PreferenceFragmentCompat extends Fragment implements TargetFragment, OnDisplayPreferenceDialogListener, OnNavigateToScreenListener, OnPreferenceTreeClickListener {
    private final DividerDecoration mDividerDecoration = new DividerDecoration();
    private Handler mHandler = new C02571();
    private boolean mHavePrefs;
    private boolean mInitDone;
    private int mLayoutResId = C0263R.layout.preference_list_fragment;
    private RecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus = new C02582();
    private int mRoundedCornerType = 2;
    private Runnable mSelectPreferenceRunnable;
    private SeslRoundedCorner mSeslListRoundedCorner;
    private SeslRoundedCorner mSeslRoundedCorner;
    private SeslRoundedCorner mSeslStrokeListRoundedCorner;
    private SeslSubheaderRoundedCorner mSeslSubheaderRoundedCorner;
    private Context mStyledContext;
    private int mSubheaderColor;

    /* renamed from: android.support.v7.preference.PreferenceFragmentCompat$1 */
    class C02571 extends Handler {
        C02571() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PreferenceFragmentCompat.this.bindPreferences();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: android.support.v7.preference.PreferenceFragmentCompat$2 */
    class C02582 implements Runnable {
        C02582() {
        }

        public void run() {
            PreferenceFragmentCompat.this.mList.focusableViewAvailable(PreferenceFragmentCompat.this.mList);
        }
    }

    private class DividerDecoration extends ItemDecoration {
        private boolean mAllowDividerAfterLastItem;
        private Drawable mDivider;
        private int mDividerHeight;

        private DividerDecoration() {
            this.mAllowDividerAfterLastItem = true;
        }

        public void seslOnDispatchDraw(Canvas c, RecyclerView parent, State state) {
            super.seslOnDispatchDraw(c, parent, state);
            int childCount = parent.getChildCount();
            int width = parent.getWidth();
            PreferenceViewHolder firstPreference = null;
            PreferenceViewHolder secondPreference = null;
            Adapter adapter = parent.getAdapter();
            for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
                PreferenceViewHolder preferenceViewHolder;
                View view = parent.getChildAt(childViewIndex);
                ViewHolder viewHolder = parent.getChildViewHolder(view);
                if (viewHolder instanceof PreferenceViewHolder) {
                    preferenceViewHolder = (PreferenceViewHolder) viewHolder;
                } else {
                    preferenceViewHolder = null;
                }
                if (childViewIndex == 0) {
                    firstPreference = preferenceViewHolder;
                } else if (childViewIndex == 1) {
                    secondPreference = preferenceViewHolder;
                }
                int top = ((int) view.getY()) + view.getHeight();
                if (this.mDivider != null && shouldDrawDividerBelow(view, parent)) {
                    this.mDivider.setBounds(0, top, width, this.mDividerHeight + top);
                    this.mDivider.draw(c);
                }
                if (!(PreferenceFragmentCompat.this.mRoundedCornerType == 0 || preferenceViewHolder == null || !preferenceViewHolder.mDrawBackground)) {
                    if (preferenceViewHolder.seslIsDrawSubheaderRound()) {
                        PreferenceFragmentCompat.this.mSeslSubheaderRoundedCorner.setRoundedCorners(preferenceViewHolder.mDrawCorners);
                        PreferenceFragmentCompat.this.mSeslSubheaderRoundedCorner.drawRoundedCorner(view, c);
                    } else {
                        PreferenceFragmentCompat.this.mSeslRoundedCorner.setRoundedCorners(preferenceViewHolder.mDrawCorners);
                        PreferenceFragmentCompat.this.mSeslRoundedCorner.drawRoundedCorner(view, c);
                    }
                }
            }
            if (PreferenceFragmentCompat.this.mRoundedCornerType == 0) {
                return;
            }
            if (PreferenceFragmentCompat.this.mRoundedCornerType != 2 || firstPreference == null || canScrollUp(parent) || firstPreference.seslIsDrawSubheaderRound() || (secondPreference != null && (secondPreference == null || secondPreference.seslGetDrawCorners() == 3))) {
                PreferenceFragmentCompat.this.mSeslListRoundedCorner.drawRoundedCorner(c);
            } else {
                PreferenceFragmentCompat.this.mSeslStrokeListRoundedCorner.drawRoundedCorner(c);
            }
        }

        private boolean canScrollUp(RecyclerView parent) {
            LayoutManager rlm = parent.getLayoutManager();
            if (!(rlm instanceof LinearLayoutManager)) {
                return false;
            }
            boolean canScrollUp = ((LinearLayoutManager) rlm).findFirstVisibleItemPosition() > 0;
            if (canScrollUp) {
                return canScrollUp;
            }
            View child = parent.getChildAt(0);
            if (child == null) {
                return canScrollUp;
            }
            if (child.getTop() < parent.getPaddingTop()) {
                return true;
            }
            return false;
        }

        private boolean shouldDrawDividerBelow(View view, RecyclerView parent) {
            boolean dividerAllowedBelow;
            ViewHolder holder = parent.getChildViewHolder(view);
            if ((holder instanceof PreferenceViewHolder) && ((PreferenceViewHolder) holder).isDividerAllowedBelow()) {
                dividerAllowedBelow = true;
            } else {
                dividerAllowedBelow = false;
            }
            if (!dividerAllowedBelow) {
                return false;
            }
            boolean nextAllowed = this.mAllowDividerAfterLastItem;
            int index = parent.indexOfChild(view);
            if (index < parent.getChildCount() - 1) {
                ViewHolder nextHolder = parent.getChildViewHolder(parent.getChildAt(index + 1));
                if ((nextHolder instanceof PreferenceViewHolder) && ((PreferenceViewHolder) nextHolder).isDividerAllowedAbove()) {
                    nextAllowed = true;
                } else {
                    nextAllowed = false;
                }
            }
            return nextAllowed;
        }

        public void setDivider(Drawable divider) {
            if (divider != null) {
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerHeight = 0;
            }
            this.mDivider = divider;
            PreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }

        public void setDividerHeight(int dividerHeight) {
            this.mDividerHeight = dividerHeight;
            PreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }

        public void setAllowDividerAfterLastItem(boolean allowDividerAfterLastItem) {
            this.mAllowDividerAfterLastItem = allowDividerAfterLastItem;
        }
    }

    public interface OnPreferenceDisplayDialogCallback {
        boolean onPreferenceDisplayDialog(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference);
    }

    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference);
    }

    public interface OnPreferenceStartScreenCallback {
        boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen);
    }

    public abstract void onCreatePreferences(Bundle bundle, String str);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypedValue tv = new TypedValue();
        getActivity().getTheme().resolveAttribute(C0263R.attr.preferenceTheme, tv, true);
        int theme = tv.resourceId;
        if (theme == 0) {
            throw new IllegalStateException("Must specify preferenceTheme in theme");
        }
        String rootKey;
        this.mStyledContext = new ContextThemeWrapper(getActivity(), theme);
        Log.d("SeslPreferenceFragmentC", this.mStyledContext + "-sesl_preference_version: " + "10.0.37");
        this.mPreferenceManager = new PreferenceManager(this.mStyledContext);
        this.mPreferenceManager.setOnNavigateToScreenListener(this);
        if (getArguments() != null) {
            rootKey = getArguments().getString("android.support.v7.preference.PreferenceFragmentCompat.PREFERENCE_ROOT");
        } else {
            rootKey = null;
        }
        onCreatePreferences(savedInstanceState, rootKey);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TypedArray a = this.mStyledContext.obtainStyledAttributes(null, C0263R.styleable.PreferenceFragmentCompat, C0263R.attr.preferenceFragmentCompatStyle, 0);
        this.mLayoutResId = a.getResourceId(C0263R.styleable.PreferenceFragmentCompat_android_layout, this.mLayoutResId);
        Drawable divider = a.getDrawable(C0263R.styleable.PreferenceFragmentCompat_android_divider);
        int dividerHeight = a.getDimensionPixelSize(C0263R.styleable.PreferenceFragmentCompat_android_dividerHeight, -1);
        boolean allowDividerAfterLastItem = a.getBoolean(C0263R.styleable.PreferenceFragmentCompat_allowDividerAfterLastItem, true);
        a.recycle();
        Resources r = getActivity().getResources();
        TypedArray ta = this.mStyledContext.obtainStyledAttributes(null, C0263R.styleable.View, 16843272, 0);
        Drawable d = ta.getDrawable(C0263R.styleable.View_android_background);
        if (d instanceof ColorDrawable) {
            this.mSubheaderColor = ((ColorDrawable) d).getColor();
        }
        Log.d("SeslPreferenceFragmentC", " sub header color = " + this.mSubheaderColor);
        ta.recycle();
        TypedValue tv = new TypedValue();
        getActivity().getTheme().resolveAttribute(C0263R.attr.preferenceTheme, tv, true);
        Context themedContext = new ContextThemeWrapper(inflater.getContext(), tv.resourceId);
        LayoutInflater themedInflater = inflater.cloneInContext(themedContext);
        View view = themedInflater.inflate(this.mLayoutResId, container, false);
        View rawListContainer = view.findViewById(16908351);
        if (rawListContainer instanceof ViewGroup) {
            ViewGroup listContainer = (ViewGroup) rawListContainer;
            RecyclerView listView = onCreateRecyclerView(themedInflater, listContainer, savedInstanceState);
            if (listView == null) {
                throw new RuntimeException("Could not create RecyclerView");
            }
            this.mList = listView;
            listView.addItemDecoration(this.mDividerDecoration);
            setDivider(divider);
            if (dividerHeight != -1) {
                setDividerHeight(dividerHeight);
            }
            this.mDividerDecoration.setAllowDividerAfterLastItem(allowDividerAfterLastItem);
            this.mList.setItemAnimator(null);
            if (this.mRoundedCornerType == 1) {
                this.mSeslSubheaderRoundedCorner = new SeslSubheaderRoundedCorner(getContext(), false);
                this.mSeslRoundedCorner = new SeslRoundedCorner(themedContext, false);
                this.mSubheaderColor = r.getColor(C0263R.color.sesl_round_and_bgcolor_dark);
                this.mSeslSubheaderRoundedCorner.setRoundedCornerColor(15, this.mSubheaderColor);
                this.mSeslRoundedCorner.setRoundedCornerColor(15, this.mSubheaderColor);
                listView.seslSetOutlineStrokeEnabled(false);
            } else if (this.mRoundedCornerType == 2) {
                this.mSeslRoundedCorner = new SeslRoundedCorner(themedContext);
                this.mSeslSubheaderRoundedCorner = new SeslSubheaderRoundedCorner(themedContext);
            }
            if (this.mRoundedCornerType != 0) {
                listView.seslSetFillBottomEnabled(true);
                listView.seslSetFillBottomColor(this.mSubheaderColor);
                this.mSeslListRoundedCorner = new SeslRoundedCorner(themedContext, false, true);
                this.mSeslListRoundedCorner.setRoundedCornerColor(15, this.mSubheaderColor);
                this.mSeslListRoundedCorner.setRoundedCorners(3);
                this.mSeslStrokeListRoundedCorner = new SeslRoundedCorner(themedContext);
                this.mSeslStrokeListRoundedCorner.setRoundedCorners(3);
            }
            listContainer.addView(this.mList);
            this.mHandler.post(this.mRequestFocus);
            return view;
        }
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
    }

    public void setDivider(Drawable divider) {
        this.mDividerDecoration.setDivider(divider);
    }

    public void setDividerHeight(int height) {
        this.mDividerDecoration.setDividerHeight(height);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mHavePrefs) {
            bindPreferences();
            if (this.mSelectPreferenceRunnable != null) {
                this.mSelectPreferenceRunnable.run();
                this.mSelectPreferenceRunnable = null;
            }
        }
        this.mInitDone = true;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Bundle container = savedInstanceState.getBundle("android:preferences");
            if (container != null) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(this);
    }

    public void onStop() {
        super.onStop();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(null);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(null);
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            unbindPreferences();
        }
        this.mList = null;
        super.onDestroyView();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle container = new Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle("android:preferences", container);
        }
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (this.mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            onUnbindPreferences();
            this.mHavePrefs = true;
            if (this.mInitDone) {
                postBindPreferences();
            }
        }
    }

    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.getPreferenceScreen();
    }

    public void setPreferencesFromResource(int preferencesResId, String key) {
        Preference root;
        requirePreferenceManager();
        Preference xmlRoot = this.mPreferenceManager.inflateFromResource(this.mStyledContext, preferencesResId, null);
        if (key != null) {
            root = xmlRoot.findPreference(key);
            if (!(root instanceof PreferenceScreen)) {
                throw new IllegalArgumentException("Preference object with key " + key + " is not a PreferenceScreen");
            }
        }
        root = xmlRoot;
        setPreferenceScreen((PreferenceScreen) root);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getFragment() == null) {
            return false;
        }
        boolean handled = false;
        if (getCallbackFragment() instanceof OnPreferenceStartFragmentCallback) {
            handled = ((OnPreferenceStartFragmentCallback) getCallbackFragment()).onPreferenceStartFragment(this, preference);
        }
        if (handled || !(getActivity() instanceof OnPreferenceStartFragmentCallback)) {
            return handled;
        }
        return ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(this, preference);
    }

    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        boolean handled = false;
        if (getCallbackFragment() instanceof OnPreferenceStartScreenCallback) {
            handled = ((OnPreferenceStartScreenCallback) getCallbackFragment()).onPreferenceStartScreen(this, preferenceScreen);
        }
        if (!handled && (getActivity() instanceof OnPreferenceStartScreenCallback)) {
            ((OnPreferenceStartScreenCallback) getActivity()).onPreferenceStartScreen(this, preferenceScreen);
        }
    }

    public Preference findPreference(CharSequence key) {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(key);
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    private void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            if (this.mRoundedCornerType == 1) {
                preferenceScreen.seslSetSubheaderColor(this.mSubheaderColor);
                preferenceScreen.mIsSolidRoundedCorner = true;
            }
            getListView().setAdapter(onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        onBindPreferences();
    }

    private void unbindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        onUnbindPreferences();
    }

    protected void onBindPreferences() {
    }

    protected void onUnbindPreferences() {
    }

    public final RecyclerView getListView() {
        return this.mList;
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(C0263R.layout.sesl_preference_recyclerview, parent, false);
        recyclerView.setLayoutManager(onCreateLayoutManager());
        recyclerView.setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate(recyclerView));
        return recyclerView;
    }

    public LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen);
    }

    public void onDisplayPreferenceDialog(Preference preference) {
        boolean handled = false;
        if (getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback) {
            handled = ((OnPreferenceDisplayDialogCallback) getCallbackFragment()).onPreferenceDisplayDialog(this, preference);
        }
        if (!handled && (getActivity() instanceof OnPreferenceDisplayDialogCallback)) {
            handled = ((OnPreferenceDisplayDialogCallback) getActivity()).onPreferenceDisplayDialog(this, preference);
        }
        if (!handled && getFragmentManager().findFragmentByTag("android.support.v7.preference.PreferenceFragment.DIALOG") == null) {
            DialogFragment f;
            if (preference instanceof EditTextPreference) {
                f = EditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            } else if (preference instanceof ListPreference) {
                f = ListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            } else if (preference instanceof AbstractMultiSelectListPreference) {
                f = MultiSelectListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            } else {
                throw new IllegalArgumentException("Tried to display dialog for unknown preference type. Did you forget to override onDisplayPreferenceDialog()?");
            }
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        }
    }

    public Fragment getCallbackFragment() {
        return null;
    }
}
