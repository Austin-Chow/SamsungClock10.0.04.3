package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Handler;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateFormat;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.samsung.android.view.animation.SineInOut33;
import com.samsung.android.view.animation.SineInOut90;
import com.samsung.android.view.animation.SineOut90;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmItemViewHolder.OnViewHolderListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class AlarmCursorAdapter extends AlarmCursorRecyclerViewAdapter<ViewHolder> {
    private final ConstraintSet constraintSet = new ConstraintSet();
    private final boolean isLeftAMPM;
    private final int mAlarmCardViewMargin;
    private final int mAlarmListTabletHeight;
    private final int mAlarmListTopBottomPaddingBig;
    private final int mAlarmListTopBottomPaddingSmall;
    private OnAlarmListViewListener mAlarmListViewListener;
    private final int mAlertDaysGuideDeleteMargin;
    private final int mAlertDaysGuideMargin;
    private final int mAlertDaysMarginTop;
    private final int mAlertDaysPaddingStart;
    private String mAlertTextViewLetterSpace;
    private final int mAmPmMargin;
    private final Context mContext;
    private final String mCurrentLanguage;
    private final SparseBooleanArray mDeleteSelectedIds = new SparseBooleanArray();
    private final boolean mIsSupportAlarmOptionMenuForWorkingDay;
    private final boolean mIsSupportHolidayAlarm;
    private int mMode = 0;
    private final int mStartEndMargin;
    private final int mTimeGuideMargin;
    private int mTimeViewGuideLineMarginStart;
    public boolean mToggleLock = false;
    private int mToggleLockPosition = -1;
    private final TransitionSet mTransitionSet = new TransitionSet();

    public interface OnAlarmListViewListener {
        void onDeleteMenuItemClick();
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCursorAdapter$1 */
    class C05921 implements OnViewHolderListener {
        C05921() {
        }

        public void onDeleteMenuItemClick(int position) {
            AlarmCursorAdapter.this.mDeleteSelectedIds.put(position, true);
            if (AlarmCursorAdapter.this.mAlarmListViewListener != null) {
                AlarmCursorAdapter.this.mAlarmListViewListener.onDeleteMenuItemClick();
            }
        }
    }

    public void setOnViewHolderListener(OnAlarmListViewListener listViewListener) {
        if (listViewListener != null) {
            this.mAlarmListViewListener = listViewListener;
        }
    }

    public AlarmCursorAdapter(Context context, Cursor cursor) {
        boolean z = false;
        super(cursor);
        this.mContext = context;
        this.mIsSupportAlarmOptionMenuForWorkingDay = Feature.isSupportAlarmOptionMenuForWorkingDay();
        if (Feature.isSupportHolidayAlarm() || this.mIsSupportAlarmOptionMenuForWorkingDay) {
            z = true;
        }
        this.mIsSupportHolidayAlarm = z;
        this.mCurrentLanguage = Locale.getDefault().getLanguage();
        Resources resources = this.mContext.getResources();
        this.mAlarmListTopBottomPaddingSmall = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_top_bottom_padding_small);
        this.mAlarmListTopBottomPaddingBig = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_top_bottom_padding_big);
        this.mStartEndMargin = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_item_margin);
        this.mTimeGuideMargin = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_time_guide_with_switch_start_margin);
        this.mAmPmMargin = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_item_ampm_gap);
        this.mAlertDaysMarginTop = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_alert_day_margin_top_with_min);
        this.mAlertDaysPaddingStart = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_alert_day_margin_start);
        this.mAlertDaysGuideMargin = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_alert_day_guide_margin);
        this.mAlertDaysGuideDeleteMargin = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_alert_day_guide_delete_margin);
        this.mAlarmListTabletHeight = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_tablet_height);
        this.mAlarmCardViewMargin = resources.getDimensionPixelSize(C0490R.dimen.alarm_list_cardview_margin);
        setAlertTextViewLetterSpace();
        this.isLeftAMPM = StateUtils.isLeftAmPm();
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(300);
        this.mTransitionSet.addTransition(changeBounds);
        this.mTransitionSet.setInterpolator(new SineOut90());
    }

    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.secD("AlarmCursorAdapter", "onCreatViewHolder()");
        AlarmItemViewHolder alarmItemHolder = new AlarmItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0490R.layout.alarm_list_item, viewGroup, false), this.mContext);
        alarmItemHolder.setOnViewHolderListener(new C05921());
        return alarmItemHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        if (viewHolder != null) {
            boolean z;
            AlarmItemViewHolder alarmItemHolder = (AlarmItemViewHolder) viewHolder;
            int alarmActiveValue = cursor.getInt(1);
            boolean isAlarmNotActive = alarmActiveValue == 0;
            int alarmTime = cursor.getInt(4);
            int checkRepeatType = cursor.getInt(5);
            int dailyBriefing = cursor.getInt(11);
            String alarmName = cursor.getString(20);
            boolean isHolidayAlarm = isHoliday(dailyBriefing);
            if (DateFormat.is24HourFormat(this.mContext)) {
                this.mTimeViewGuideLineMarginStart = this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_list_timeview_min_guideline_with_24h);
            } else {
                this.mTimeViewGuideLineMarginStart = this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_list_timeview_min_guideline_with_12h);
            }
            this.constraintSet.clone(alarmItemHolder.mAlarmListItem);
            this.constraintSet.setGuidelineBegin(C0490R.id.alarm_timeview_min_guideline, this.mTimeViewGuideLineMarginStart);
            this.constraintSet.applyTo(alarmItemHolder.mAlarmListItem);
            alarmItemHolder.setTimeViewInAlarmItem(this.mContext, alarmTime, this.mCurrentLanguage);
            if ((checkRepeatType & 15) != 5) {
                alarmItemHolder.setDateAlertDayTextViewInAlarmItem(this.mContext, alarmActiveValue, alarmTime, dailyBriefing, cursor.getLong(3), cursor.getInt(9), cursor.getInt(8));
            } else {
                alarmItemHolder.setRepeatAlertDayTextViewInAlarmItem(this.mContext, isAlarmNotActive, checkRepeatType, cursor, this.mAlertTextViewLetterSpace);
            }
            if (this.mIsSupportHolidayAlarm) {
                alarmItemHolder.setAlarmHolidayWorkdayTextView(this.mContext, this.mIsSupportAlarmOptionMenuForWorkingDay, isAlarmNotActive);
            }
            alarmItemHolder.setLayoutAlarmNameAndHolidayTextView(isHolidayAlarm, alarmName);
            if (StateUtils.isScreenDp(this.mContext, 512)) {
                if (alarmItemHolder.getAdapterPosition() % 2 == 0) {
                    alarmItemHolder.setAlarmListTabletWindow(this.constraintSet, this.isLeftAMPM, this.mAmPmMargin, this.mAlarmListTabletHeight, 0, this.mAlarmCardViewMargin);
                } else {
                    alarmItemHolder.setAlarmListTabletWindow(this.constraintSet, this.isLeftAMPM, this.mAmPmMargin, this.mAlarmListTabletHeight, this.mAlarmCardViewMargin, 0);
                }
                alarmItemHolder.setAlarmListTopBottomPadding(0);
            } else {
                if (alarmItemHolder.setAlarmLisPositionByWindowSize(this.constraintSet, this.isLeftAMPM, this.mAmPmMargin, this.mAlertDaysMarginTop, this.mAlertDaysPaddingStart) && (!ClockUtils.isEnableString(alarmName) || alarmName.trim().isEmpty() || isHolidayAlarm)) {
                    alarmItemHolder.setAlarmListTopBottomPadding(this.mAlarmListTopBottomPaddingBig);
                } else {
                    alarmItemHolder.setAlarmListTopBottomPadding(this.mAlarmListTopBottomPaddingSmall);
                }
            }
            alarmItemHolder.mIsDeleteMode = this.mMode == 1;
            alarmItemHolder.setAlarmItemColor(this.mContext, isAlarmNotActive);
            alarmItemHolder.setAlarmItemDescription(this.mContext, isHoliday(dailyBriefing), isAlarmNotActive);
            alarmItemHolder.setAlarmDeleteModeCheckedState(this.mContext, this.mDeleteSelectedIds.get(cursor.getPosition()));
            setAlarmOnOffIconControl(alarmActiveValue, cursor.getPosition(), alarmItemHolder);
            if (this.mMode == 1) {
                z = true;
            } else {
                z = false;
            }
            setAlarmItemPosition(z, alarmItemHolder);
        }
    }

    public void setAlertTextViewLetterSpace() {
        if (StateUtils.getScreenDensityIndex(this.mContext) >= 2 || !StateUtils.isScreenDp(this.mContext, 321)) {
            this.mAlertTextViewLetterSpace = " ";
        } else {
            this.mAlertTextViewLetterSpace = "  ";
        }
    }

    private void setAlarmOnOffIconControl(final int curActive, final int position, final AlarmItemViewHolder holder) {
        boolean z;
        boolean z2 = true;
        holder.mAlarmOnOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean z = false;
                Log.secD("AlarmCursorAdapter", "mAlarmOnOffSwitch onCheckedChanged() mToggleLock : " + AlarmCursorAdapter.this.mToggleLock + ", position : " + position);
                if (position < AlarmCursorAdapter.this.getCursor().getCount()) {
                    boolean currentActive;
                    if (AlarmCursorAdapter.this.mToggleLock && AlarmCursorAdapter.this.mToggleLockPosition == position) {
                        int origPosition = AlarmCursorAdapter.this.getCursor().getPosition();
                        AlarmCursorAdapter.this.getCursor().moveToPosition(position);
                        currentActive = true;
                        if (AlarmItem.createItemFromCursor(AlarmCursorAdapter.this.getCursor()).mActivate == 0) {
                            currentActive = false;
                        }
                        holder.mAlarmOnOffSwitch.setChecked(currentActive);
                        AlarmCursorAdapter.this.getCursor().moveToPosition(origPosition);
                        return;
                    }
                    final boolean willChangeButtonActive = holder.mAlarmOnOffSwitch.isChecked();
                    AlarmCursorAdapter.this.mToggleLock = true;
                    AlarmCursorAdapter.this.mToggleLockPosition = position;
                    AlarmCursorAdapter.this.getCursor().moveToPosition(position);
                    final AlarmItem item = AlarmItem.createItemFromCursor(AlarmCursorAdapter.this.getCursor());
                    currentActive = true;
                    if (item.mActivate == 0) {
                        currentActive = false;
                    }
                    if (currentActive != willChangeButtonActive) {
                        boolean z2;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                AlarmDataHandler.setAlarmActive(AlarmCursorAdapter.this.mContext, item.mId, willChangeButtonActive, curActive);
                                if ((willChangeButtonActive ? 1 : null) == null) {
                                    AlarmNotificationHelper.clearNotification(AlarmCursorAdapter.this.mContext, item.mId);
                                }
                            }
                        }, 200);
                        ClockUtils.insertSaLog("101", "1005", willChangeButtonActive ? "1" : "0");
                        AlarmItemViewHolder alarmItemViewHolder = holder;
                        Context access$300 = AlarmCursorAdapter.this.mContext;
                        if (willChangeButtonActive) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        alarmItemViewHolder.setAlarmItemColor(access$300, z2);
                        int checkRepeatType = AlarmCursorAdapter.this.getCursor().getInt(5);
                        if (AlarmItem.isWeeklyAlarm(checkRepeatType)) {
                            AlarmItemViewHolder alarmItemViewHolder2 = holder;
                            Context access$3002 = AlarmCursorAdapter.this.mContext;
                            if (!willChangeButtonActive) {
                                z = true;
                            }
                            alarmItemViewHolder2.setRepeatAlertDayTextViewInAlarmItem(access$3002, z, checkRepeatType, AlarmCursorAdapter.this.getCursor(), AlarmCursorAdapter.this.mAlertTextViewLetterSpace);
                        } else {
                            holder.mAlarmAlertDayTextView.setTextColor(AlarmCursorAdapter.this.mContext.getColor(willChangeButtonActive ? C0490R.color.alarm_list_alert_day_color_on : C0490R.color.alarm_list_alert_day_color_off));
                        }
                        holder.mAlarmHolidayTextView.setTextColor(AlarmCursorAdapter.this.mContext.getColor(willChangeButtonActive ? C0490R.color.alarm_list_repeat_unselect_on : C0490R.color.alarm_list_repeat_unselect_off));
                    } else {
                        Log.secD("AlarmCursorAdapter", "Lock no more needed. Release toggle lock.");
                        AlarmCursorAdapter.this.mToggleLock = false;
                    }
                    holder.mAlarmOnOffSwitch.setSelected(true);
                }
            }
        });
        Switch switchR = holder.mAlarmOnOffSwitch;
        if (curActive == 1 || curActive == 2) {
            z = true;
        } else {
            z = false;
        }
        switchR.setChecked(z);
        Switch switchR2 = holder.mAlarmOnOffSwitch;
        if (this.mMode != 0) {
            z2 = false;
        }
        switchR2.setEnabled(z2);
    }

    private boolean isHoliday(int dailyBriefing) {
        if (!this.mIsSupportHolidayAlarm) {
            return false;
        }
        if ((dailyBriefing & 4) == 4 || (dailyBriefing & 8) == 8) {
            return true;
        }
        return false;
    }

    public void startAnimation(int position, final AlarmItemViewHolder alarmHolder) {
        if (alarmHolder != null) {
            boolean z;
            alarmHolder.inflateAlarmCheckBox();
            if (this.mMode == 1) {
                z = true;
            } else {
                z = false;
            }
            alarmHolder.mIsDeleteMode = z;
            this.constraintSet.clone(alarmHolder.mAlarmListItem);
            TransitionManager.beginDelayedTransition(alarmHolder.mAlarmListItem, this.mTransitionSet);
            if (alarmHolder.mIsDeleteMode) {
                alarmHolder.mAlarmOnOffSwitch.setEnabled(false);
                alarmHolder.mAlarmOnOffSwitch.setClickable(false);
                alarmHolder.mAlarmOnOffSwitch.animate().alphaBy(1.0f).alpha(0.0f).setDuration(100).setInterpolator(new SineInOut90()).start();
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_time_guideline, this.mTimeGuideMargin);
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_timeview_min_guideline, this.mTimeViewGuideLineMarginStart + (this.mTimeGuideMargin - this.mStartEndMargin));
                this.constraintSet.setGuidelineEnd(C0490R.id.alarm_alert_day_guideline, this.mAlertDaysGuideDeleteMargin);
                alarmHolder.mAlarmCheckBox.animate().alphaBy(0.0f).alpha(1.0f).scaleXBy(0.5f).scaleX(1.0f).scaleYBy(0.5f).scaleY(1.0f).setDuration(400).setStartDelay(100).setInterpolator(new SineInOut90()).start();
            } else {
                alarmHolder.mAlarmOnOffSwitch.setEnabled(true);
                alarmHolder.mAlarmOnOffSwitch.setClickable(true);
                alarmHolder.mAlarmOnOffSwitch.animate().alphaBy(0.0f).alpha(1.0f).setDuration(250).setStartDelay(150).setInterpolator(new SineInOut33()).start();
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_time_guideline, this.mStartEndMargin);
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_timeview_min_guideline, this.mTimeViewGuideLineMarginStart);
                this.constraintSet.setGuidelineEnd(C0490R.id.alarm_alert_day_guideline, this.mAlertDaysGuideMargin);
                alarmHolder.mAlarmCheckBox.animate().alphaBy(1.0f).alpha(0.0f).setDuration(150).setInterpolator(new SineInOut90()).withEndAction(new Runnable() {
                    public void run() {
                        alarmHolder.mAlarmCheckBox.setScaleX(0.5f);
                        alarmHolder.mAlarmCheckBox.setScaleY(0.5f);
                    }
                }).start();
                setAlarmDeleteModeCheckedState(alarmHolder, position);
            }
            this.constraintSet.applyTo(alarmHolder.mAlarmListItem);
            if (getCursor() != null) {
                boolean isAlarmNotActive;
                getCursor().moveToPosition(position);
                if (getCursor().getInt(1) == 0) {
                    isAlarmNotActive = true;
                } else {
                    isAlarmNotActive = false;
                }
                alarmHolder.setAlarmItemDescription(this.mContext, isHoliday(getCursor().getInt(11)), isAlarmNotActive);
            }
        }
    }

    private void setAlarmItemPosition(boolean bNeedToShowCheckbox, AlarmItemViewHolder alarmHolder) {
        if (alarmHolder != null) {
            this.constraintSet.clone(alarmHolder.mAlarmListItem);
            if (bNeedToShowCheckbox) {
                alarmHolder.inflateAlarmCheckBox();
                alarmHolder.mAlarmOnOffSwitch.setEnabled(false);
                alarmHolder.mAlarmOnOffSwitch.setClickable(false);
                alarmHolder.mAlarmCheckBox.setAlpha(1.0f);
                alarmHolder.mAlarmCheckBox.setScaleX(1.0f);
                alarmHolder.mAlarmCheckBox.setScaleY(1.0f);
                this.constraintSet.setAlpha(C0490R.id.alarmListCheckBox, 1.0f);
                this.constraintSet.setScaleX(C0490R.id.alarmListCheckBox, 1.0f);
                this.constraintSet.setScaleY(C0490R.id.alarmListCheckBox, 1.0f);
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_time_guideline, this.mTimeGuideMargin);
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_timeview_min_guideline, this.mTimeViewGuideLineMarginStart + (this.mTimeGuideMargin - this.mStartEndMargin));
                this.constraintSet.setGuidelineEnd(C0490R.id.alarm_alert_day_guideline, this.mAlertDaysGuideDeleteMargin);
                this.constraintSet.setAlpha(C0490R.id.alarm_onoff_switch, 0.0f);
            } else {
                alarmHolder.mAlarmOnOffSwitch.setEnabled(true);
                alarmHolder.mAlarmOnOffSwitch.setClickable(true);
                if (alarmHolder.mAlarmCheckBox != null) {
                    alarmHolder.mAlarmCheckBox.setAlpha(0.0f);
                    alarmHolder.mAlarmCheckBox.setScaleX(0.5f);
                    alarmHolder.mAlarmCheckBox.setScaleY(0.5f);
                    this.constraintSet.setAlpha(C0490R.id.alarmListCheckBox, 0.0f);
                    this.constraintSet.setScaleX(C0490R.id.alarmListCheckBox, 0.5f);
                    this.constraintSet.setScaleY(C0490R.id.alarmListCheckBox, 0.5f);
                } else if (StateUtils.isRtl()) {
                    alarmHolder.inflateAlarmCheckBox();
                }
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_time_guideline, this.mStartEndMargin);
                this.constraintSet.setGuidelineBegin(C0490R.id.alarm_timeview_min_guideline, this.mTimeViewGuideLineMarginStart);
                this.constraintSet.setGuidelineEnd(C0490R.id.alarm_alert_day_guideline, this.mAlertDaysGuideMargin);
                this.constraintSet.setAlpha(C0490R.id.alarm_onoff_switch, 1.0f);
            }
            this.constraintSet.applyTo(alarmHolder.mAlarmListItem);
        }
    }

    public void setAlarmDeleteModeCheckedState(AlarmItemViewHolder alarmItemViewHolder, int alarmPosition) {
        boolean z = true;
        if (alarmItemViewHolder != null) {
            if (this.mMode != 1) {
                z = false;
            }
            alarmItemViewHolder.mIsDeleteMode = z;
            alarmItemViewHolder.setAlarmDeleteModeCheckedState(this.mContext, this.mDeleteSelectedIds.get(alarmPosition));
        }
    }

    public void toggleAllSelectCheckBox(boolean isToggleCheck) {
        this.mDeleteSelectedIds.clear();
        if (isToggleCheck) {
            for (int i = 0; i < getItemCount(); i++) {
                this.mDeleteSelectedIds.put(i, true);
            }
        }
    }

    public void toggleSelectCheckBox(ArrayList<Integer> list, boolean isToggleMustCheck) {
        if (this.mMode == 1) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Integer selectedPosition = (Integer) it.next();
                if (!this.mDeleteSelectedIds.get(selectedPosition.intValue())) {
                    this.mDeleteSelectedIds.put(selectedPosition.intValue(), true);
                } else if (!isToggleMustCheck) {
                    this.mDeleteSelectedIds.delete(selectedPosition.intValue());
                }
            }
        }
    }

    public ArrayList<Integer> toggleSelectedAlarmPositions() {
        ArrayList<Integer> selectedItems = new ArrayList();
        int selectedAlarmSize = this.mDeleteSelectedIds.size();
        for (int i = 0; i < selectedAlarmSize; i++) {
            selectedItems.add(Integer.valueOf(this.mDeleteSelectedIds.keyAt(i)));
        }
        return selectedItems;
    }

    public boolean isAlarmListDeleteMode() {
        return this.mMode == 1;
    }

    private ArrayList<Integer> getSelectedAlarmIds() {
        ArrayList<Integer> mSelectedAlarmIds = new ArrayList();
        int i = this.mDeleteSelectedIds.size() - 1;
        while (i >= 0) {
            if (this.mDeleteSelectedIds.valueAt(i) && this.mDeleteSelectedIds.keyAt(i) != -1) {
                mSelectedAlarmIds.add(Integer.valueOf((int) getItemId(this.mDeleteSelectedIds.keyAt(i))));
            }
            i--;
        }
        return mSelectedAlarmIds;
    }

    public void setSelectedAlarmIds(ArrayList<Integer> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Integer selectedPosition = (Integer) it.next();
            if (!this.mDeleteSelectedIds.get(selectedPosition.intValue())) {
                this.mDeleteSelectedIds.put(selectedPosition.intValue(), true);
            }
        }
    }

    public int getSelectedItemsSize() {
        return this.mDeleteSelectedIds.size();
    }

    public void removeSelection() {
        if (this.mDeleteSelectedIds != null) {
            this.mDeleteSelectedIds.clear();
        }
    }

    public void setAlarmListMode(int alarmActionMode) {
        this.mMode = alarmActionMode;
    }

    public void deleteContent() {
        try {
            ArrayList<Integer> selectedAlarmIds = getSelectedAlarmIds();
            if (AlarmDataHandler.deleteAlarms(this.mContext, selectedAlarmIds)) {
                AlarmProvider.enableNextAlert(this.mContext);
                for (int i = 0; selectedAlarmIds.size() - i > 0; i++) {
                    AlarmNotificationHelper.clearNotification(this.mContext, ((Integer) selectedAlarmIds.get(i)).intValue());
                }
            }
            selectedAlarmIds.clear();
            this.mContext.sendBroadcast(new Intent("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET"));
        } catch (Exception e) {
            Log.secE("AlarmCursorAdapter", "deleteContent() Exception!! ");
        }
    }
}
