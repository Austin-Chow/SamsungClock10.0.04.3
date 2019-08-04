package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.CheckBox;
import android.widget.TextView;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.WorldclockListViewActionModeListener;
import com.sec.android.app.clockpackage.worldclock.model.Worldclock;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockMainListViewModelData;

public class WorldclockListViewActionMode implements Callback {
    private final WorldclockListViewActionModeListener mActionModeListener;
    private final AppCompatActivity mActivity;
    private final WorldclockMainListAdapter mAdapter;
    private final WorldclockMainListViewModelData mListViewModelData;
    private int mOriginContentInset;
    private final RecyclerView mRecyclerView;

    WorldclockListViewActionMode(WorldclockMainListViewModelData worldclockMainListViewModelData, AppCompatActivity activity, WorldclockMainListAdapter adapter, RecyclerView listView, WorldclockListViewActionModeListener actionModeListener) {
        this.mListViewModelData = worldclockMainListViewModelData;
        this.mRecyclerView = listView;
        this.mActivity = activity;
        this.mAdapter = adapter;
        this.mActionModeListener = actionModeListener;
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        initMultiSelectActionBar();
        updateActionBarCheckBox();
        this.mOriginContentInset = this.mActionModeListener.onGetToolbar().getContentInsetStart();
        setToolbar(8, 0);
        this.mActionModeListener.onGetToolbar().addView(this.mListViewModelData.getMultiSelectModeTitle());
        this.mListViewModelData.setActionMode(mode);
        this.mListViewModelData.setIsActionMode(true);
        startActionMode(true);
        setSubtitle();
        ClockUtils.insertSaLog("111");
        return true;
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    public void onDestroyActionMode(ActionMode mode) {
        initActionBarCheckboxAni(false, this.mListViewModelData.getSelectAllLayout());
        this.mListViewModelData.setIsActionMode(false);
        this.mListViewModelData.setCheckedActionMode(false);
        this.mActionModeListener.onActionModeFinished();
        startActionMode(false);
        this.mListViewModelData.setActionMode(null);
        this.mActionModeListener.onGetToolbar().removeView(this.mListViewModelData.getMultiSelectModeTitle());
        setToolbar(0, this.mOriginContentInset);
        ClockUtils.insertSaLog("110");
    }

    void setCheckedState(int position, boolean isChecked, boolean isSelectAll) {
        if (isSelectAll) {
            int childCount = this.mAdapter.getItemCount();
            this.mAdapter.setSelectedAll(isChecked);
            for (int i = 0; i < childCount; i++) {
                ViewHolder holder = this.mRecyclerView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    this.mAdapter.setCheckedAll((WorldclockViewHolder) holder, isChecked);
                } else {
                    this.mAdapter.notifyItemChanged(i);
                }
            }
        } else {
            WorldclockViewHolder holder2 = (WorldclockViewHolder) this.mRecyclerView.findViewHolderForLayoutPosition(position);
            this.mAdapter.setChecked(position, isChecked, holder2);
            this.mAdapter.setSelectionBg(holder2, position);
        }
        updateActionModeActionBar();
    }

    void updateActionModeActionBar() {
        updateActionBarCheckBox();
        setSubtitle();
    }

    void setSubtitle() {
        if (this.mAdapter != null) {
            int checkedCount = this.mAdapter.getCheckedCount();
            if (this.mListViewModelData.getSelectItemText() != null) {
                if (checkedCount == 0) {
                    this.mListViewModelData.getSelectItemText().setText(C0836R.string.select_cities);
                } else {
                    this.mListViewModelData.getSelectItemText().setText(this.mActivity.getResources().getString(C0836R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)}));
                }
            }
            this.mActionModeListener.onActionModeUpdate(checkedCount);
        }
    }

    private void startActionMode(boolean isInSelectionMode) {
        if (this.mRecyclerView == null) {
            this.mListViewModelData.setCheckedActionMode(false);
            return;
        }
        int childCount = this.mAdapter.getItemCount();
        if (childCount != 0) {
            initActionBarCheckboxAni(true, this.mListViewModelData.getSelectAllLayout());
            if (!isInSelectionMode) {
                this.mAdapter.setSelectedAll(false);
            }
            if (this.mListViewModelData.isCheckedActionMode()) {
                this.mAdapter.notifyDataSetChanged();
                updateActionModeActionBar();
                return;
            }
            for (int i = 0; i < childCount; i++) {
                ViewHolder holder = this.mRecyclerView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    this.mAdapter.startEditModeAnimation((WorldclockViewHolder) holder, isInSelectionMode);
                    this.mAdapter.setCheckedAll((WorldclockViewHolder) holder, false);
                    this.mAdapter.setDescription((WorldclockViewHolder) holder, i);
                } else {
                    this.mAdapter.notifyItemChanged(i);
                }
            }
        }
    }

    private void initActionBarCheckboxAni(boolean show, View selectAllCustomView) {
        float f = 0.0f;
        float f2 = 1.0f;
        if (selectAllCustomView != null) {
            float f3;
            if (this.mListViewModelData.isCheckedActionMode()) {
                selectAllCustomView.setAlpha(show ? 1.0f : 0.0f);
                if (show) {
                    f3 = 1.0f;
                } else {
                    f3 = 0.5f;
                }
                selectAllCustomView.setScaleX(f3);
                if (show) {
                    f3 = 0.5f;
                } else {
                    f3 = 1.0f;
                }
                selectAllCustomView.setScaleY(f3);
            } else {
                if (show) {
                    f3 = 0.0f;
                } else {
                    f3 = 1.0f;
                }
                selectAllCustomView.setAlpha(f3);
                if (show) {
                    f3 = 0.5f;
                } else {
                    f3 = 1.0f;
                }
                selectAllCustomView.setScaleX(f3);
                if (show) {
                    f3 = 0.5f;
                } else {
                    f3 = 1.0f;
                }
                selectAllCustomView.setScaleY(f3);
            }
            ViewPropertyAnimator animate = selectAllCustomView.animate();
            if (show) {
                f = 1.0f;
            }
            ViewPropertyAnimator alpha = animate.alpha(f);
            if (show) {
                f3 = 1.0f;
            } else {
                f3 = 0.5f;
            }
            animate = alpha.scaleX(f3);
            if (!show) {
                f2 = 0.5f;
            }
            animate.scaleY(f2).setInterpolator(new SineInOut90()).setDuration(Worldclock.LIST_ANIMATION_DURATION.longValue()).setStartDelay(60).start();
        }
    }

    void updateActionBarCheckBox() {
        int count = this.mAdapter.getCheckedCount();
        if (!(this.mListViewModelData.getSelectAllCheckBox() == null || this.mActionModeListener.canNotCheckSelectAll())) {
            CheckBox selectAllCheckBox = this.mListViewModelData.getSelectAllCheckBox();
            boolean z = count != 0 && count == this.mAdapter.getItemCount();
            selectAllCheckBox.setChecked(z);
        }
        setSelectAllContentDescription(count);
    }

    private void initMultiSelectActionBar() {
        this.mListViewModelData.setMultiSelectModeTitle(LayoutInflater.from(this.mActivity).inflate(C0836R.layout.clock_multi_select_mode_actionbar, null));
        View multiSelectModeTitle = this.mListViewModelData.getMultiSelectModeTitle();
        this.mListViewModelData.setSelectAllLayout((ViewGroup) multiSelectModeTitle.findViewById(C0836R.id.select_all_layout));
        this.mListViewModelData.setSelectAllCheckBox((CheckBox) multiSelectModeTitle.findViewById(C0836R.id.select_all_cb));
        this.mListViewModelData.setSelectItemText((TextView) multiSelectModeTitle.findViewById(C0836R.id.selection_title));
        ClockUtils.setLargeTextSize(this.mActivity, this.mListViewModelData.getSelectItemText(), (float) this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.clock_list_select_item_text_size));
        this.mListViewModelData.getSelectAllLayout().setOnClickListener(WorldclockListViewActionMode$$Lambda$1.lambdaFactory$(this));
    }

    private /* synthetic */ void lambda$initMultiSelectActionBar$0(View v) {
        if (this.mListViewModelData.getSelectAllCheckBox().isChecked()) {
            this.mListViewModelData.getSelectAllCheckBox().setChecked(false);
        } else {
            this.mListViewModelData.getSelectAllCheckBox().setChecked(true);
        }
        setCheckedState(0, this.mListViewModelData.getSelectAllCheckBox().isChecked(), true);
        this.mActionModeListener.onUpdateDeleteButton();
        ClockUtils.insertSaLog("111", "1261");
    }

    private void setSelectAllContentDescription(int count) {
        StringBuilder allCheckboxDescription = new StringBuilder();
        if (count == 0) {
            allCheckboxDescription.append(this.mActivity.getResources().getString(C0836R.string.nothing_selected_tts)).append(' ').append(this.mActivity.getResources().getString(C0836R.string.double_tap_to_select_all_tts));
        } else if (this.mAdapter != null && count == this.mAdapter.getItemCount()) {
            allCheckboxDescription.append(this.mActivity.getResources().getString(C0836R.string.pd_selected, new Object[]{Integer.valueOf(count)})).append(',').append(' ').append(this.mActivity.getResources().getString(C0836R.string.double_tab_to_deselect_all_tts));
        } else if (count > 0) {
            allCheckboxDescription.append(this.mActivity.getResources().getString(C0836R.string.pd_selected, new Object[]{Integer.valueOf(count)})).append(", ").append(' ').append(this.mActivity.getResources().getString(C0836R.string.double_tap_to_select_all_tts));
        }
        if (this.mListViewModelData.getSelectAllCheckBox() != null) {
            this.mListViewModelData.getSelectAllCheckBox().setContentDescription(allCheckboxDescription);
        }
    }

    private void setToolbar(int visibility, int insetStart) {
        boolean isEditMode = false;
        int toolbarChildCount = this.mActionModeListener.onGetToolbar().getChildCount();
        this.mActionModeListener.onGetToolbar().setContentInsetsRelative(insetStart, 0);
        if (visibility == 8) {
            isEditMode = true;
        }
        for (int a = 0; a < toolbarChildCount; a++) {
            int index = a;
            if (this.mActionModeListener.onGetToolbar().getChildAt(index) instanceof ActionMenuView) {
                this.mActionModeListener.onGetToolbar().getChildAt(a).animate().alpha(isEditMode ? 0.0f : 1.0f).setInterpolator(new SineInOut90()).setDuration(150).withStartAction(WorldclockListViewActionMode$$Lambda$2.lambdaFactory$(this, isEditMode, index, visibility)).withEndAction(WorldclockListViewActionMode$$Lambda$3.lambdaFactory$(this, isEditMode, index, visibility)).start();
            } else {
                this.mActionModeListener.onGetToolbar().getChildAt(index).setVisibility(visibility);
            }
        }
    }

    private /* synthetic */ void lambda$setToolbar$1(boolean isEditMode, int index, int visibility) {
        if (!isEditMode) {
            this.mActionModeListener.onGetToolbar().getChildAt(index).setVisibility(visibility);
        }
    }

    private /* synthetic */ void lambda$setToolbar$2(boolean isEditMode, int index, int visibility) {
        if (isEditMode) {
            this.mActionModeListener.onGetToolbar().getChildAt(index).setVisibility(visibility);
        }
    }
}
