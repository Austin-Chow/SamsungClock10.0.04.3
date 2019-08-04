package com.sec.android.app.clockpackage.alarm.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.CheckBox;
import android.widget.TextView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;

public class AlarmCelebActionModeCallback implements MultiChoiceModeListener {
    private CelebActionModeListener mActionModeListener;
    private Context mContext;
    private boolean mIsActionMode = false;
    private ActionMode mListActionMode;
    private CheckBox mSelectAll;
    private ViewGroup mSelectAllLayout;
    private View mSelectModeActionBar;
    private TextView mSelectionTitle;

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCelebActionModeCallback$1 */
    class C05461 implements OnClickListener {
        C05461() {
        }

        public void onClick(View arg0) {
            boolean z;
            boolean z2 = true;
            boolean isAllChecked = AlarmCelebActionModeCallback.this.mSelectAll.isChecked();
            CheckBox access$000 = AlarmCelebActionModeCallback.this.mSelectAll;
            if (isAllChecked) {
                z = false;
            } else {
                z = true;
            }
            access$000.setChecked(z);
            AlarmCelebActionModeCallback alarmCelebActionModeCallback = AlarmCelebActionModeCallback.this;
            if (isAllChecked) {
                z2 = false;
            }
            alarmCelebActionModeCallback.onSelectAllCheck(z2);
            ClockUtils.insertSaLog("614", "6141");
        }
    }

    public interface CelebActionModeListener {
        void addViewOnToolBar(View view);

        void removeViewOnToolBar(View view);

        void setBottomBarVisibility(boolean z);

        void toggleSelectAll(boolean z);
    }

    public void setOnCelebActionModeListener(CelebActionModeListener actionModeListener) {
        this.mActionModeListener = actionModeListener;
    }

    AlarmCelebActionModeCallback(Context context) {
        this.mContext = context;
    }

    public boolean isActionMode() {
        return this.mIsActionMode;
    }

    private void setActionMode(ActionMode mode, boolean isActionMode) {
        if (this.mIsActionMode != isActionMode) {
            this.mIsActionMode = isActionMode;
            if (!isActionMode) {
                mode = null;
            }
            this.mListActionMode = mode;
        }
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        setActionMode(mode, true);
        this.mSelectModeActionBar = createCustomLayout(this.mContext);
        this.mActionModeListener.addViewOnToolBar(this.mSelectModeActionBar);
        updateSelectionMenu(0, 0);
        ClockUtils.insertSaLog("614");
        return true;
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    public void onDestroyActionMode(ActionMode mode) {
        this.mActionModeListener.removeViewOnToolBar(this.mSelectModeActionBar);
        setActionMode(mode, false);
    }

    private View createCustomLayout(Context context) {
        View customView = LayoutInflater.from(context).inflate(C0490R.layout.clock_multi_select_mode_actionbar, null);
        this.mSelectionTitle = (TextView) customView.findViewById(C0490R.id.selection_title);
        ClockUtils.setLargeTextSize(context, this.mSelectionTitle, (float) context.getResources().getDimensionPixelSize(C0490R.dimen.clock_list_select_item_text_size));
        this.mSelectAll = (CheckBox) customView.findViewById(C0490R.id.select_all_cb);
        this.mSelectAllLayout = (ViewGroup) customView.findViewById(C0490R.id.select_all_layout);
        this.mSelectAllLayout.setOnClickListener(new C05461());
        return customView;
    }

    public void updateSelectionMenu(int totalCount, int checkedCount) {
        boolean z;
        boolean z2 = true;
        CheckBox checkBox = this.mSelectAll;
        if (checkedCount == 0 || checkedCount != totalCount) {
            z = false;
        } else {
            z = true;
        }
        checkBox.setChecked(z);
        StringBuilder allCheckboxDescription = new StringBuilder();
        String selectTitle = ClockUtils.toDigitString(checkedCount);
        if (checkedCount == 0) {
            selectTitle = this.mContext.getString(C0490R.string.select_voices);
            allCheckboxDescription.append(this.mContext.getResources().getString(C0490R.string.nothing_selected_tts)).append(" ").append(this.mContext.getResources().getString(C0490R.string.double_tap_to_select_all_tts));
        } else if (checkedCount == totalCount) {
            allCheckboxDescription.append(this.mContext.getResources().getString(C0490R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)})).append(". ").append(this.mContext.getResources().getString(C0490R.string.double_tab_to_deselect_all_tts));
        } else if (checkedCount > 0) {
            allCheckboxDescription.append(this.mContext.getResources().getString(C0490R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)})).append(". ").append(this.mContext.getResources().getString(C0490R.string.double_tap_to_select_all_tts));
        }
        this.mSelectionTitle.setText(selectTitle);
        this.mSelectAll.setContentDescription(allCheckboxDescription);
        if (this.mSelectAllLayout != null) {
            this.mSelectAllLayout.setNextFocusRightId(C0490R.id.mitem_delete_action_mode);
        }
        if (this.mListActionMode != null) {
            this.mListActionMode.invalidate();
        }
        CelebActionModeListener celebActionModeListener = this.mActionModeListener;
        if (checkedCount == 0) {
            z2 = false;
        }
        celebActionModeListener.setBottomBarVisibility(z2);
    }

    private void onSelectAllCheck(boolean checked) {
        if (this.mIsActionMode) {
            this.mActionModeListener.toggleSelectAll(checked);
        }
    }

    public void finish() {
        if (this.mListActionMode != null) {
            this.mListActionMode.finish();
        }
    }
}
