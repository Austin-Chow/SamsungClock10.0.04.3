package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.CheckableConstraintLayout;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmItemViewHolder extends ViewHolder implements OnCreateContextMenuListener {
    public final TextView mAlarmAlertDayTextView;
    private final CardView mAlarmCardView;
    public CheckBox mAlarmCheckBox;
    public final TextView mAlarmHolidayTextView;
    public final CheckableConstraintLayout mAlarmListItem;
    private final TextView mAlarmNameView;
    public final Switch mAlarmOnOffSwitch;
    private OnViewHolderListener mAlarmViewHolderListener;
    private final TextView mAmpmTimeView;
    private final Context mContext;
    public boolean mIsDeleteMode;
    private final TextView mTimeView;

    public interface OnViewHolderListener {
        void onDeleteMenuItemClick(int i);
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmItemViewHolder$1 */
    class C05981 implements OnKeyListener {
        C05981() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == 0 && !AlarmItemViewHolder.this.mIsDeleteMode) {
                if (keyCode == 21 && AlarmItemViewHolder.this.mAlarmOnOffSwitch.isChecked()) {
                    AlarmItemViewHolder.this.mAlarmOnOffSwitch.performClick();
                    return true;
                } else if (keyCode == 22 && !AlarmItemViewHolder.this.mAlarmOnOffSwitch.isChecked()) {
                    AlarmItemViewHolder.this.mAlarmOnOffSwitch.performClick();
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmItemViewHolder$2 */
    class C05992 implements OnMenuItemClickListener {
        C05992() {
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            AlarmItemViewHolder.this.mAlarmViewHolderListener.onDeleteMenuItemClick(AlarmItemViewHolder.this.getAdapterPosition());
            return true;
        }
    }

    public void setOnViewHolderListener(OnViewHolderListener alarmViewHolderListener) {
        if (alarmViewHolderListener != null) {
            this.mAlarmViewHolderListener = alarmViewHolderListener;
        }
    }

    public AlarmItemViewHolder(View alarmItemView, Context context) {
        super(alarmItemView);
        this.mAlarmCardView = (CardView) alarmItemView.findViewById(C0490R.id.alarm_list_cardView);
        this.mAlarmListItem = (CheckableConstraintLayout) alarmItemView.findViewById(C0490R.id.alarm_list_item);
        this.mTimeView = (TextView) alarmItemView.findViewById(C0490R.id.alarm_item_time);
        this.mAmpmTimeView = (TextView) alarmItemView.findViewById(C0490R.id.alarm_item_ampm);
        this.mAlarmNameView = (TextView) alarmItemView.findViewById(C0490R.id.alarm_list_alarm_name);
        this.mAlarmOnOffSwitch = (Switch) alarmItemView.findViewById(C0490R.id.alarm_onoff_switch);
        this.mAlarmAlertDayTextView = (TextView) alarmItemView.findViewById(C0490R.id.alarm_list_alert_day);
        this.mAlarmHolidayTextView = (TextView) alarmItemView.findViewById(C0490R.id.alarm_list_holiday);
        this.mContext = context;
        this.mAlarmOnOffSwitch.semSetHoverPopupType(0);
        if (!StateUtils.isContextInDexMode(this.mContext) && !StateUtils.isCustomTheme(this.mContext)) {
            this.mAlarmListItem.setBackground(this.mContext.getDrawable(C0490R.drawable.common_cardview_item_area_background));
        } else if (StateUtils.isContextInDexMode(this.mContext)) {
            this.mAlarmListItem.setBackground(this.mContext.getDrawable(C0490R.drawable.common_cardview_item_area_background_for_dexmode));
        }
        ClockUtils.setLargeTextSize(this.mContext, new TextView[]{this.mAlarmNameView, this.mAlarmHolidayTextView}, 1.3f);
        alarmItemView.setOnCreateContextMenuListener(this);
        alarmItemView.setOnKeyListener(new C05981());
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        if (StateUtils.isContextInDexMode(this.mContext)) {
            contextMenu.setHeaderTitle(getAlarmTimeTextFromList());
            contextMenu.add(0, 0, 0, C0490R.string.delete).setOnMenuItemClickListener(new C05992());
        }
    }

    private String getAlarmTimeTextFromList() {
        StringBuilder alarmTimeText = new StringBuilder();
        if (this.mAmpmTimeView.getVisibility() != 0) {
            alarmTimeText.append(this.mTimeView.getText()).append(' ');
        } else if (StateUtils.isLeftAmPm()) {
            alarmTimeText.append(this.mAmpmTimeView.getText()).append(' ');
            alarmTimeText.append(this.mTimeView.getText()).append(' ');
        } else {
            alarmTimeText.append(this.mTimeView.getText()).append(' ');
            alarmTimeText.append(this.mAmpmTimeView.getText()).append(' ');
        }
        if (ClockUtils.isEnableString(this.mAlarmNameView.getText().toString())) {
            alarmTimeText.append(this.mAlarmNameView.getText());
        } else {
            alarmTimeText.append(this.mContext.getString(C0490R.string.alarm));
        }
        return alarmTimeText.toString();
    }

    public void setRepeatAlertDayTextViewInAlarmItem(Context context, boolean isInActive, int checkrepeatType, Cursor cursor, String textViewLetterSpace) {
        int repeatDays = checkrepeatType >> 4;
        int startDay = ClockUtils.getStartDayOfWeek();
        boolean isWorkingDay = AlarmItem.createItemFromCursor(cursor).isWorkingDay();
        StringBuilder repeatDayTextDescStr = new StringBuilder();
        String[] mDayOfWeekString = new String[7];
        boolean[] saveSelectItems = new boolean[7];
        int styleSpanStart = 0;
        SpannableStringBuilder repeatDayTextSpan = new SpannableStringBuilder();
        for (int nIndex = 0; nIndex < 7; nIndex++) {
            int repeatCharColor;
            int dayOfWeek = (((startDay - 1) + nIndex) % 7) + 1;
            int isRepeatDay = (repeatDays >> ((7 - dayOfWeek) * 4)) & 15;
            mDayOfWeekString[nIndex] = ClockUtils.getDayOfWeekString(context, (dayOfWeek + 1) - 1, 0);
            String dayDescriptionString = ClockUtils.getDayOfWeekString(context, dayOfWeek, 3);
            if (mDayOfWeekString[nIndex] != null) {
                repeatDayTextSpan.append(mDayOfWeekString[nIndex]).append(textViewLetterSpace);
            }
            if (isRepeatDay > 0 && !isWorkingDay) {
                saveSelectItems[nIndex] = true;
                repeatDayTextDescStr.append(dayDescriptionString).append(", ");
            }
            if (saveSelectItems[nIndex]) {
                if (isInActive) {
                    repeatCharColor = ColorUtils.setAlphaComponent(ContextCompat.getColor(context, C0490R.color.selected_repeat_on_text_color), context.getResources().getInteger(C0490R.integer.alarm_list_item_repeat_selected_alpha));
                } else {
                    repeatCharColor = context.getColor(C0490R.color.selected_repeat_on_text_color);
                }
            } else if (isInActive) {
                repeatCharColor = context.getColor(C0490R.color.alarm_list_repeat_unselect_off);
            } else {
                repeatCharColor = context.getColor(C0490R.color.alarm_list_repeat_unselect_on);
            }
            if (nIndex > 0) {
                styleSpanStart += mDayOfWeekString[nIndex - 1].length() + textViewLetterSpace.length();
            }
            int styleSpandEnd = styleSpanStart + mDayOfWeekString[nIndex].length();
            if (styleSpanStart >= styleSpandEnd) {
                break;
            }
            repeatDayTextSpan.setSpan(new TypefaceSpan(Typeface.create(saveSelectItems[nIndex] ? "sans-serif-medium" : "sans-serif", saveSelectItems[nIndex] ? 1 : 0)), styleSpanStart, styleSpandEnd, 33);
            repeatDayTextSpan.setSpan(new ForegroundColorSpan(repeatCharColor), styleSpanStart, styleSpandEnd, 33);
        }
        ClockUtils.setLargeTextSize(this.mContext, this.mAlarmAlertDayTextView, (float) this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_list_alert_repeat_days_text_size));
        this.mAlarmAlertDayTextView.setText(repeatDayTextSpan.subSequence(0, repeatDayTextSpan.length() - textViewLetterSpace.length()));
        this.mAlarmAlertDayTextView.setContentDescription(repeatDayTextDescStr);
    }

    public void setDateAlertDayTextViewInAlarmItem(Context context, int alarmActive, int alarmTime, int dailyBriefing, long alarmAlertTime, int snoozeCount, int snoozeDuration) {
        String onceAlarmNotidays;
        String onceAlarmNotidaysDesc;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curHour = calendar.get(11);
        int curMinute = calendar.get(12);
        if ((dailyBriefing & 1) == 1) {
            if (alarmAlertTime >= calendar.getTimeInMillis()) {
                calendar.setTimeInMillis(alarmAlertTime);
            } else if ((curHour * 100) + curMinute >= alarmTime) {
                calendar.add(6, 1);
            }
        } else if (alarmActive == 2) {
            if ((curHour * 100) + curMinute >= (AlarmItemUtil.ALARM_SNOOZE_DURATION_TABLE[snoozeDuration] * AlarmItemUtil.ALARM_SNOOZE_COUNT_TABLE[snoozeCount]) + alarmTime) {
                calendar.add(6, 1);
            }
        } else if ((curHour * 100) + curMinute >= alarmTime) {
            calendar.add(6, 1);
        }
        if (alarmActive == 0) {
            onceAlarmNotidays = ClockUtils.getFormatDateTime(context, calendar.getTimeInMillis(), false);
            onceAlarmNotidaysDesc = ClockUtils.getFormatDateTime(context, calendar.getTimeInMillis(), true) + ", ";
            this.mAlarmAlertDayTextView.setTextColor(context.getColor(C0490R.color.alarm_list_alert_day_color_off));
        } else {
            onceAlarmNotidays = ClockUtils.getFormatDateTime(context, alarmAlertTime, false);
            onceAlarmNotidaysDesc = ClockUtils.getFormatDateTime(context, alarmAlertTime, true) + ", ";
            this.mAlarmAlertDayTextView.setTextColor(context.getColor(C0490R.color.alarm_list_alert_day_color_on));
        }
        ClockUtils.setLargeTextSize(this.mContext, this.mAlarmAlertDayTextView, (float) this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_list_alert_day_text_size));
        this.mAlarmAlertDayTextView.setText(onceAlarmNotidays);
        this.mAlarmAlertDayTextView.setTypeface(Typeface.SANS_SERIF);
        this.mAlarmAlertDayTextView.setContentDescription(onceAlarmNotidaysDesc);
    }

    @SuppressLint({"SetTextI18n"})
    public void setTimeViewInAlarmItem(Context context, int alarmTime, String currentLanguage) {
        String hourStr;
        String minuteStr;
        String alarmTimeDescription;
        int alarmItemHour = alarmTime / 100;
        int alarmItemMinute = alarmTime % 100;
        String ampm = "";
        String timeSeparatorText = ClockUtils.getTimeSeparatorText(context);
        if (DateFormat.is24HourFormat(context)) {
            hourStr = ClockUtils.toTwoDigitString(alarmItemHour);
            minuteStr = ClockUtils.toTwoDigitString(alarmItemMinute);
        } else {
            if (alarmItemHour % 12 != 0) {
                hourStr = ClockUtils.toDigitString(alarmItemHour % 12);
            } else if ("ja".equals(currentLanguage)) {
                hourStr = ClockUtils.toDigitString(0);
            } else {
                hourStr = ClockUtils.toDigitString(12);
            }
            minuteStr = ClockUtils.toTwoDigitString(alarmItemMinute);
            String[] amPmTexts = new DateFormatSymbols().getAmPmStrings();
            if (alarmItemHour >= 12) {
                ampm = amPmTexts[1];
            } else {
                ampm = amPmTexts[0];
            }
            this.mAmpmTimeView.setText(ampm);
        }
        this.mTimeView.setText(hourStr + timeSeparatorText + minuteStr);
        Typeface tf = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (tf == null) {
            tf = Typeface.create("sans-serif-light", 0);
        }
        this.mTimeView.setTypeface(tf);
        if ("ko".equals(currentLanguage)) {
            SimpleDateFormat simpleDateFormate = new SimpleDateFormat("HH:mm", Locale.KOREA);
            Date date = null;
            try {
                date = simpleDateFormate.parse(hourStr + timeSeparatorText + minuteStr);
            } catch (ParseException e) {
                Log.secE("AlarmItemViewHolder", "setTimeViewInAlarmItem() simpleDateFormate ParseException!!");
            }
            if (date != null) {
                alarmTimeDescription = ampm + simpleDateFormate.format(date);
            } else {
                alarmTimeDescription = ampm + ' ' + hourStr + ' ' + minuteStr;
            }
        } else if ("pl".equals(currentLanguage)) {
            alarmTimeDescription = this.mTimeView.getText().toString() + ampm;
        } else {
            String hourDesc = context.getResources().getString(alarmItemHour <= 1 ? C0490R.string.timer_hour : C0490R.string.timer_hr);
            String minDesc = context.getResources().getString(alarmItemMinute <= 1 ? C0490R.string.timer_minute : C0490R.string.timer_min);
            if (alarmItemMinute == 0) {
                alarmTimeDescription = hourStr + ' ' + hourDesc + ' ' + ampm;
            } else {
                alarmTimeDescription = hourStr + ' ' + hourDesc + ' ' + minuteStr + ' ' + minDesc + ' ' + ampm;
            }
        }
        this.mTimeView.setContentDescription(alarmTimeDescription);
    }

    public void setAlarmListTopBottomPadding(int paddingValue) {
        this.mAlarmListItem.setPadding(0, paddingValue, 0, paddingValue);
    }

    public void setAlarmHolidayWorkdayTextView(Context context, boolean isSupportAlarmOptionMenuForWorkingDay, boolean isInActiveAlarm) {
        int color;
        if (isSupportAlarmOptionMenuForWorkingDay) {
            this.mAlarmHolidayTextView.setText(context.getResources().getString(C0490R.string.alarm_workdays));
            this.mAlarmHolidayTextView.setTypeface(Typeface.create("sans-serif-light", 0));
        }
        TextView textView = this.mAlarmHolidayTextView;
        if (isInActiveAlarm) {
            color = context.getColor(C0490R.color.alarm_list_public_holiday_color_off);
        } else {
            color = context.getColor(C0490R.color.alarm_list_public_holiday_color_on);
        }
        textView.setTextColor(color);
    }

    public void setAlarmItemColor(Context context, boolean isInActive) {
        int timeNameTextViewColor = context.getColor(isInActive ? C0490R.color.alarm_list_time_name_color_off : C0490R.color.alarm_list_time_name_color_on);
        this.mTimeView.setTextColor(timeNameTextViewColor);
        this.mAmpmTimeView.setTextColor(timeNameTextViewColor);
        this.mAlarmNameView.setTextColor(timeNameTextViewColor);
    }

    public void setAlarmItemDescription(Context context, boolean isHoliday, boolean isInActive) {
        StringBuilder alarmListItemDescription = new StringBuilder();
        if (this.mAlarmNameView.getVisibility() == 0 && this.mAlarmNameView.length() > 0) {
            alarmListItemDescription.append(this.mAlarmNameView.getText().toString()).append(", ");
        }
        alarmListItemDescription.append(this.mTimeView.getContentDescription()).append(", ");
        if (isHoliday) {
            alarmListItemDescription.append(this.mAlarmHolidayTextView.getText()).append(", ");
        }
        if (this.mAlarmAlertDayTextView.getContentDescription() != null) {
            alarmListItemDescription.append(this.mAlarmAlertDayTextView.getContentDescription().toString());
        }
        if (this.mIsDeleteMode) {
            alarmListItemDescription = alarmListItemDescription.delete(alarmListItemDescription.length() - 2, alarmListItemDescription.length() - 1);
            inflateAlarmCheckBox();
            this.mAlarmCheckBox.setContentDescription(alarmListItemDescription);
            this.mAlarmCheckBox.setImportantForAccessibility(1);
            this.mAlarmNameView.setImportantForAccessibility(2);
            this.mTimeView.setImportantForAccessibility(2);
            this.mAlarmHolidayTextView.setImportantForAccessibility(2);
            this.mAlarmAlertDayTextView.setImportantForAccessibility(2);
            this.mAlarmListItem.setImportantForAccessibility(2);
            return;
        }
        this.mAlarmOnOffSwitch.setContentDescription(alarmListItemDescription);
        alarmListItemDescription.append(context.getResources().getString(isInActive ? C0490R.string.alarm_off : C0490R.string.alarm_on));
        alarmListItemDescription.append(", ").append(context.getResources().getString(C0490R.string.alarm_item_double_tab_go_to_edit_for_tts));
        this.mAlarmListItem.setContentDescription(alarmListItemDescription);
        if (this.mAlarmCheckBox != null) {
            this.mAlarmCheckBox.setImportantForAccessibility(2);
        }
        this.mAlarmListItem.setImportantForAccessibility(1);
    }

    public void inflateAlarmCheckBox() {
        if (this.mAlarmCheckBox == null) {
            ((ViewStub) this.mAlarmListItem.findViewById(C0490R.id.alarm_checkBox_stub)).inflate();
            this.mAlarmCheckBox = (CheckBox) this.mAlarmListItem.findViewById(C0490R.id.alarmListCheckBox);
        }
    }

    public void setAlarmDeleteModeCheckedState(final Context context, boolean bChecked) {
        if (this.mIsDeleteMode) {
            inflateAlarmCheckBox();
            this.mAlarmCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
                    if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                        buttonView.post(new Runnable() {
                            public void run() {
                                buttonView.sendAccessibilityEvent(1);
                            }
                        });
                    }
                    AlarmItemViewHolder.this.mAlarmListItem.setChecked(isChecked);
                }
            });
            this.mAlarmListItem.setChecked(bChecked);
            this.mAlarmCheckBox.setChecked(bChecked);
            this.mAlarmOnOffSwitch.setClickable(false);
            this.mAlarmOnOffSwitch.setHovered(false);
            return;
        }
        this.mAlarmListItem.setChecked(false);
        if (this.mAlarmCheckBox != null) {
            this.mAlarmCheckBox.setChecked(false);
        }
        this.mAlarmOnOffSwitch.setClickable(true);
    }

    public void setLayoutAlarmNameAndHolidayTextView(boolean isHoliday, String alarmName) {
        this.mAlarmHolidayTextView.setVisibility(isHoliday ? 0 : 8);
        if (!ClockUtils.isEnableString(alarmName) || alarmName.trim().isEmpty()) {
            this.mAlarmNameView.setText("");
            this.mAlarmNameView.setVisibility(8);
            return;
        }
        this.mAlarmNameView.setText(alarmName);
        this.mAlarmNameView.setVisibility(0);
    }

    public void setAlarmListTabletWindow(ConstraintSet constraintSet, boolean isLeftAmPm, int amPmMargin, int listHeight, int cardViewMarginStart, int cardViewMarginEnd) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) this.mAlarmCardView.getLayoutParams();
        layoutParams.setMarginStart(cardViewMarginStart);
        layoutParams.setMarginEnd(cardViewMarginEnd);
        this.mAlarmCardView.setLayoutParams(layoutParams);
        this.mAlarmNameView.setMaxLines(1);
        this.mAlarmAlertDayTextView.setMaxLines(2);
        this.mAlarmListItem.setLayoutParams(new LayoutParams(-1, listHeight));
        int timeEndSideId = C0490R.id.alarm_item_ampm;
        if (DateFormat.is24HourFormat(this.mContext)) {
            this.mAmpmTimeView.setVisibility(8);
        } else {
            this.mAmpmTimeView.setVisibility(0);
            constraintSet.clone(this.mAlarmListItem);
            if (isLeftAmPm) {
                constraintSet.connect(C0490R.id.alarm_item_ampm, 6, C0490R.id.alarm_time_guideline, 7, 0);
                constraintSet.connect(C0490R.id.alarm_item_ampm, 7, 0, 7);
                constraintSet.connect(C0490R.id.alarm_item_time, 6, C0490R.id.alarm_item_ampm, 7, amPmMargin);
                constraintSet.connect(C0490R.id.alarm_item_time, 7, C0490R.id.alarm_list_alert_day, 6, 0);
                timeEndSideId = C0490R.id.alarm_item_time;
            }
            constraintSet.applyTo(this.mAlarmListItem);
        }
        constraintSet.clone(this.mAlarmListItem);
        constraintSet.connect(C0490R.id.alarm_list_alarm_name, 7, C0490R.id.alarm_alert_day_guideline, 7);
        constraintSet.connect(timeEndSideId, 7, C0490R.id.alarm_alert_day_guideline, 7);
        constraintSet.connect(C0490R.id.alarm_item_time, 4, C0490R.id.alarm_list_holiday, 3);
        constraintSet.connect(C0490R.id.alarm_list_holiday, 3, C0490R.id.alarm_item_time, 4);
        constraintSet.connect(C0490R.id.alarm_list_holiday, 6, C0490R.id.alarm_time_guideline, 6);
        constraintSet.connect(C0490R.id.alarm_list_alert_day, 6, C0490R.id.alarm_time_guideline, 6);
        constraintSet.setHorizontalBias(C0490R.id.alarm_list_holiday, 0.0f);
        constraintSet.setHorizontalBias(C0490R.id.alarm_list_alert_day, 0.0f);
        this.mAlarmHolidayTextView.setPadding(0, 0, 0, 0);
        this.mAlarmAlertDayTextView.setPadding(0, 0, 0, 0);
        constraintSet.applyTo(this.mAlarmListItem);
    }

    public boolean setAlarmLisPositionByWindowSize(ConstraintSet constraintSet, boolean isLeftAmPm, int amPmMargin, int alertDaysMarginTop, int alertDaysPaddingStart) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) this.mAlarmCardView.getLayoutParams();
        layoutParams.setMarginStart(0);
        layoutParams.setMarginEnd(0);
        this.mAlarmCardView.setLayoutParams(layoutParams);
        this.mAlarmNameView.setMaxLines(7);
        this.mAlarmAlertDayTextView.setMaxLines(7);
        this.mAlarmListItem.setLayoutParams(new LayoutParams(-1, -2));
        int timeEndSideId = C0490R.id.alarm_item_ampm;
        if (DateFormat.is24HourFormat(this.mContext)) {
            this.mAmpmTimeView.setVisibility(8);
        } else {
            this.mAmpmTimeView.setVisibility(0);
            constraintSet.clone(this.mAlarmListItem);
            if (isLeftAmPm) {
                constraintSet.connect(C0490R.id.alarm_item_ampm, 6, C0490R.id.alarm_time_guideline, 7, 0);
                constraintSet.connect(C0490R.id.alarm_item_ampm, 7, 0, 7);
                constraintSet.connect(C0490R.id.alarm_item_time, 6, C0490R.id.alarm_item_ampm, 7, amPmMargin);
                constraintSet.connect(C0490R.id.alarm_item_time, 7, C0490R.id.alarm_list_alert_day, 6, 0);
                timeEndSideId = C0490R.id.alarm_item_time;
            }
            constraintSet.applyTo(this.mAlarmListItem);
        }
        constraintSet.clone(this.mAlarmListItem);
        if (StateUtils.isMultiWindowMinSize(this.mContext, InputDeviceCompat.SOURCE_KEYBOARD, true)) {
            constraintSet.connect(C0490R.id.alarm_list_alarm_name, 7, 0, 7);
            constraintSet.connect(timeEndSideId, 7, 0, 7);
            constraintSet.connect(C0490R.id.alarm_item_time, 4, C0490R.id.alarm_list_holiday, 3, alertDaysMarginTop);
            constraintSet.connect(C0490R.id.alarm_list_holiday, 3, C0490R.id.alarm_item_time, 4);
            constraintSet.connect(C0490R.id.alarm_list_holiday, 6, C0490R.id.alarm_time_guideline, 6, 0);
            constraintSet.connect(C0490R.id.alarm_list_alert_day, 6, C0490R.id.alarm_time_guideline, 6, 0);
            constraintSet.setHorizontalBias(C0490R.id.alarm_list_holiday, 0.0f);
            constraintSet.setHorizontalBias(C0490R.id.alarm_list_alert_day, 0.0f);
            constraintSet.applyTo(this.mAlarmListItem);
            return false;
        }
        if (this.mAlarmHolidayTextView.getVisibility() == 0) {
            this.mAlarmAlertDayTextView.measure(0, 0);
            this.mAlarmHolidayTextView.measure(0, 0);
            if (this.mAlarmAlertDayTextView.getMeasuredWidth() < this.mAlarmHolidayTextView.getMeasuredWidth()) {
                constraintSet.connect(C0490R.id.alarm_list_alarm_name, 7, C0490R.id.alarm_list_holiday, 6);
            } else {
                constraintSet.connect(C0490R.id.alarm_list_alarm_name, 7, C0490R.id.alarm_list_alert_day, 6);
            }
        } else {
            constraintSet.connect(C0490R.id.alarm_list_alarm_name, 7, C0490R.id.alarm_list_alert_day, 6);
        }
        constraintSet.connect(timeEndSideId, 7, C0490R.id.alarm_list_alert_day, 6);
        constraintSet.connect(C0490R.id.alarm_item_time, 4, 0, 4, 0);
        constraintSet.connect(C0490R.id.alarm_list_holiday, 3, 0, 3);
        constraintSet.connect(C0490R.id.alarm_list_holiday, 6, C0490R.id.alarm_timeview_min_guideline, 7);
        constraintSet.connect(C0490R.id.alarm_list_alert_day, 6, C0490R.id.alarm_timeview_min_guideline, 7);
        constraintSet.setHorizontalBias(C0490R.id.alarm_list_holiday, 1.0f);
        constraintSet.setHorizontalBias(C0490R.id.alarm_list_alert_day, 1.0f);
        constraintSet.applyTo(this.mAlarmListItem);
        return true;
    }
}
