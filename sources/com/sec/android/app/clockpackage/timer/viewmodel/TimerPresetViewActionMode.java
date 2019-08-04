package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.TextView;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerPresetViewActionModeListener;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetAdapter.PresetViewHolder;

public class TimerPresetViewActionMode implements Callback {
    private ActionMode mActionMode;
    private final TimerPresetViewActionModeListener mActionModeListener;
    private final Context mContext;
    private boolean mIsActionMode = false;
    private int mOriginContentInsetStart;
    private final TimerPresetAdapter mPresetAdapter;
    private final RecyclerView mPresetListView;
    private CheckBox mSelectAllCheckBox;
    private ViewGroup mSelectAllLayout;
    private View mSelectModeActionBar;
    private TextView mSelectModeTitle;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewActionMode$1 */
    class C08121 implements OnClickListener {
        C08121() {
        }

        public void onClick(View view) {
            TimerPresetViewActionMode.this.checkSelectAllCheckBox(!TimerPresetViewActionMode.this.mSelectAllCheckBox.isChecked());
            ClockUtils.insertSaLog("137", "1341");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewActionMode$2 */
    class C08132 implements Runnable {
        C08132() {
        }

        public void run() {
            for (int i = 0; i < TimerPresetViewActionMode.this.mPresetAdapter.getItemCount(); i++) {
                ViewHolder holder = TimerPresetViewActionMode.this.mPresetListView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    TimerPresetViewActionMode.this.mPresetAdapter.startActionModeAnimation((PresetViewHolder) holder);
                }
            }
        }
    }

    TimerPresetViewActionMode(Context context, RecyclerView presetListView, TimerPresetAdapter presetAdapter, TimerPresetViewActionModeListener actionModeListener) {
        this.mContext = context;
        this.mPresetListView = presetListView;
        this.mPresetAdapter = presetAdapter;
        this.mActionModeListener = actionModeListener;
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.secD("TimerPresetViewActionMode", "onCreateActionMode()");
        ClockUtils.insertSaLog("137");
        this.mActionMode = mode;
        this.mIsActionMode = true;
        initSelectModeActionBar();
        setActionMenuVisibility(8);
        startSelectModeActionBarAnimation(0);
        this.mActionModeListener.onSetViewEnabled(false);
        this.mPresetAdapter.clearCheckedItems();
        startPresetCheckBoxAnimation();
        return true;
    }

    public void onDestroyActionMode(ActionMode actionMode) {
        Log.secD("TimerPresetViewActionMode", "onDestroyActionMode()");
        ClockUtils.insertSaLog("130");
        this.mActionMode = null;
        this.mIsActionMode = false;
        setActionMenuVisibility(0);
        startSelectModeActionBarAnimation(8);
        this.mActionModeListener.onSetViewEnabled(true);
        this.mPresetAdapter.clearCheckedItems();
        this.mPresetAdapter.setIsDisplayCheckBox(false);
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    private void initSelectModeActionBar() {
        this.mSelectModeActionBar = LayoutInflater.from(this.mContext).inflate(C0728R.layout.clock_multi_select_mode_actionbar, null);
        this.mSelectAllLayout = (ViewGroup) this.mSelectModeActionBar.findViewById(C0728R.id.select_all_layout);
        this.mSelectAllCheckBox = (CheckBox) this.mSelectModeActionBar.findViewById(C0728R.id.select_all_cb);
        this.mSelectModeTitle = (TextView) this.mSelectModeActionBar.findViewById(C0728R.id.selection_title);
        ClockUtils.setLargeTextSize(this.mContext, this.mSelectModeTitle, (float) this.mContext.getResources().getDimensionPixelSize(C0728R.dimen.clock_list_select_item_text_size));
        this.mSelectAllLayout.setOnClickListener(new C08121());
        updateSelectModeActionBar();
    }

    private void setActionMenuVisibility(int visibility) {
        Toolbar toolbar = this.mActionModeListener.onGetToolbar();
        int toolbarChildCount = toolbar.getChildCount();
        boolean isVisible = visibility == 0;
        for (int i = 0; i < toolbarChildCount; i++) {
            View toolbarChildView = toolbar.getChildAt(i);
            if (toolbarChildView instanceof ActionMenuView) {
                toolbarChildView.animate().alpha(isVisible ? 1.0f : 0.0f).setInterpolator(new SineInOut90()).setDuration(150).withStartAction(TimerPresetViewActionMode$$Lambda$1.lambdaFactory$(isVisible, toolbarChildView)).withEndAction(TimerPresetViewActionMode$$Lambda$2.lambdaFactory$(isVisible, toolbarChildView)).start();
            }
        }
    }

    private static /* synthetic */ void lambda$setActionMenuVisibility$0(boolean isVisible, View toolbarChildView) {
        if (isVisible) {
            toolbarChildView.setVisibility(0);
        }
    }

    private static /* synthetic */ void lambda$setActionMenuVisibility$1(boolean isVisible, View toolbarChildView) {
        if (!isVisible) {
            toolbarChildView.setVisibility(8);
        }
    }

    public void updateSelectModeActionBar() {
        Resources res = this.mContext.getResources();
        int checkedCount = this.mPresetAdapter.getCheckedItemCount();
        if (this.mSelectAllCheckBox != null) {
            boolean isAllChecked = checkedCount != 0 && checkedCount == this.mPresetAdapter.getItemCount();
            this.mSelectAllCheckBox.setChecked(isAllChecked);
            StringBuilder selectAllDescription = new StringBuilder();
            if (checkedCount == 0) {
                selectAllDescription.append(res.getString(C0728R.string.nothing_selected_tts)).append(' ').append(res.getString(C0728R.string.double_tap_to_select_all_tts));
            } else if (isAllChecked) {
                selectAllDescription.append(res.getString(C0728R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)})).append(". ").append(res.getString(C0728R.string.double_tab_to_deselect_all_tts));
            } else {
                selectAllDescription.append(res.getString(C0728R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)})).append(". ").append(res.getString(C0728R.string.double_tap_to_select_all_tts));
            }
            this.mSelectAllCheckBox.setContentDescription(selectAllDescription);
        }
        if (this.mSelectModeTitle != null) {
            String selectTitle = res.getString(C0728R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)});
            if (checkedCount == 0) {
                selectTitle = res.getString(C0728R.string.timer_select_presets);
            }
            this.mSelectModeTitle.setText(selectTitle);
        }
        this.mActionModeListener.onUpdateActionModeMenu();
    }

    private void checkSelectAllCheckBox(boolean isChecked) {
        if (this.mSelectAllCheckBox != null && this.mPresetAdapter != null && this.mIsActionMode && this.mPresetAdapter.getItemCount() != 0) {
            this.mSelectAllCheckBox.setChecked(isChecked);
            this.mPresetAdapter.toggleSelectAllCheck(isChecked);
            updateSelectModeActionBar();
        }
    }

    private void startSelectModeActionBarAnimation(int visibility) {
        if (this.mSelectModeActionBar != null && this.mSelectAllCheckBox != null) {
            boolean isVisible = visibility == 0;
            this.mSelectModeActionBar.setAlpha(isVisible ? 0.0f : 1.0f);
            this.mSelectModeActionBar.animate().alpha(isVisible ? 1.0f : 0.0f).setInterpolator(new SineInOut90()).setDuration(400).withStartAction(TimerPresetViewActionMode$$Lambda$3.lambdaFactory$(this, isVisible)).withEndAction(TimerPresetViewActionMode$$Lambda$4.lambdaFactory$(this, isVisible)).start();
            Animation scaleAnimation = isVisible ? new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, 1, 0.5f, 1, 0.5f) : new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 1, 0.5f, 1, 0.5f);
            scaleAnimation.setInterpolator(new SineInOut90());
            scaleAnimation.setDuration(400);
            this.mSelectAllCheckBox.startAnimation(scaleAnimation);
        }
    }

    private /* synthetic */ void lambda$startSelectModeActionBarAnimation$2(boolean isVisible) {
        if (isVisible) {
            Toolbar toolbar = this.mActionModeListener.onGetToolbar();
            if (toolbar != null) {
                this.mOriginContentInsetStart = toolbar.getContentInsetStart();
                toolbar.setContentInsetsRelative(0, 0);
                toolbar.addView(this.mSelectModeActionBar);
            }
        }
    }

    private /* synthetic */ void lambda$startSelectModeActionBarAnimation$3(boolean isVisible) {
        if (!isVisible) {
            Toolbar toolbar = this.mActionModeListener.onGetToolbar();
            if (toolbar != null) {
                toolbar.removeView(this.mSelectModeActionBar);
                toolbar.setContentInsetsRelative(this.mOriginContentInsetStart, 0);
            }
        }
    }

    private void startPresetCheckBoxAnimation() {
        new Handler().postDelayed(new C08132(), 0);
    }

    public void finishActionMode() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
    }

    public boolean isActionMode() {
        return this.mIsActionMode;
    }

    public void releaseInstance() {
        this.mActionMode = null;
        this.mSelectAllLayout = null;
        this.mSelectAllCheckBox = null;
        this.mSelectModeTitle = null;
    }
}
