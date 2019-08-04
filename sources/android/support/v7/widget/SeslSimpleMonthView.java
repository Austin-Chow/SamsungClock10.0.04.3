package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v4.feature.SeslCscFeatureReflector;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.appcompat.C0247R;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import dalvik.system.PathClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class SeslSimpleMonthView extends View {
    private static final int DEFAULT_MONTH_LINE = 6;
    private static final int DEFAULT_NUM_DAYS = 7;
    private static final String DEFAULT_WEEK_DAY_STRING_FEATURE = "XXXXXXR";
    private static final int DEFAULT_WEEK_START = 1;
    private static final float DIVISOR_FOR_CIRCLE_POSITION_Y = 2.7f;
    private static final int LEAP_MONTH = 1;
    private static final float LEAP_MONTH_WEIGHT = 0.5f;
    private static final int MAX_MONTH_VIEW_ID = 42;
    private static final int MIN_HEIGHT = 10;
    private static final int MONTH_WEIGHT = 100;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final String TAG = "SemSimpleMonthView";
    private static final String TAG_CSCFEATURE_CALENDAR_SETCOLOROFDAYS = "CscFeature_Calendar_SetColorOfDays";
    private static final int YEAR_WEIGHT = 10000;
    private Paint mAbnormalSelectedDayPaint;
    private final int mAbnormalStartEndDateBackgroundAlpha;
    private final Calendar mCalendar;
    private int mCalendarWidth;
    private Context mContext;
    private Method mConvertLunarToSolarMethod;
    private int[] mDayColorSet;
    private Method mDayLengthMethod;
    private int mDayNumberDisabledAlpha;
    private Paint mDayNumberPaint;
    private Paint mDayNumberSelectedPaint;
    private int mDayOfWeekStart;
    private int mDaySelectedCircleSize;
    private int mDaySelectedCircleStroke;
    private int mEnabledDayEnd;
    private int mEnabledDayStart;
    private int mEndDay;
    private int mEndMonth;
    private int mEndYear;
    private Method mGetDayMethod;
    private Method mGetMonthMethod;
    private Method mGetWeekDayMethod;
    private Method mGetYearMethod;
    private boolean mIsFirstMonth;
    private boolean mIsLastMonth;
    private int mIsLeapEndMonth;
    private boolean mIsLeapMonth;
    private int mIsLeapStartMonth;
    private boolean mIsLunar;
    private boolean mIsNextMonthLeap;
    private boolean mIsPrevMonthLeap;
    private boolean mIsRTL;
    private boolean mLockAccessibilityDelegate;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private int mMiniDayNumberTextSize;
    private int mMode;
    private int mMonth;
    private int mNormalTextColor;
    private int mNumCells;
    private int mNumDays;
    private OnDayClickListener mOnDayClickListener;
    private OnDeactivatedDayClickListener mOnDeactivatedDayClickListener;
    private int mPadding;
    private PathClassLoader mPathClassLoader;
    private final int mPrevNextMonthDayNumberAlpha;
    private int mSaturdayTextColor;
    private int mSelectedDay;
    private int mSelectedDayColor;
    private int mSelectedDayNumberTextColor;
    private Object mSolarLunarConverter;
    private int mStartDay;
    private int mStartMonth;
    private int mStartYear;
    private int mSundayTextColor;
    private Calendar mTempDate;
    private final MonthViewTouchHelper mTouchHelper;
    private int mWeekHeight;
    private int mWeekStart;
    private int mYear;

    public interface OnDayClickListener {
        void onDayClick(SeslSimpleMonthView seslSimpleMonthView, int i, int i2, int i3);
    }

    public interface OnDeactivatedDayClickListener {
        void onDeactivatedDayClick(SeslSimpleMonthView seslSimpleMonthView, int i, int i2, int i3, boolean z, boolean z2);
    }

    private class MonthViewTouchHelper extends ExploreByTouchHelper {
        private final Calendar mTempCalendar = Calendar.getInstance();
        private final Rect mTempRect = new Rect();

        public MonthViewTouchHelper(View host) {
            super(host);
        }

        public void setFocusedVirtualView(int virtualViewId) {
            getAccessibilityNodeProvider(SeslSimpleMonthView.this).performAction(virtualViewId, 64, null);
        }

        public void clearFocusedVirtualView() {
            int focusedVirtualView = getFocusedVirtualView();
            if (focusedVirtualView != Integer.MIN_VALUE) {
                getAccessibilityNodeProvider(SeslSimpleMonthView.this).performAction(focusedVirtualView, 128, null);
            }
        }

        protected int getVirtualViewAt(float x, float y) {
            int day = SeslSimpleMonthView.this.getDayFromLocation(x, y);
            if ((!SeslSimpleMonthView.this.mIsFirstMonth || day >= SeslSimpleMonthView.this.mEnabledDayStart) && (!SeslSimpleMonthView.this.mIsLastMonth || day <= SeslSimpleMonthView.this.mEnabledDayEnd)) {
                day += SeslSimpleMonthView.this.findDayOffset();
                int i = day;
                return day;
            }
            return Integer.MIN_VALUE;
        }

        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            int dayOffset = SeslSimpleMonthView.this.findDayOffset();
            for (int viewId = 1; viewId <= 42; viewId++) {
                int day = viewId - dayOffset;
                if ((!SeslSimpleMonthView.this.mIsFirstMonth || day >= SeslSimpleMonthView.this.mEnabledDayStart) && (!SeslSimpleMonthView.this.mIsLastMonth || day <= SeslSimpleMonthView.this.mEnabledDayEnd)) {
                    virtualViewIds.add(Integer.valueOf(viewId));
                }
            }
        }

        protected void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
            event.setContentDescription(getItemDescription(virtualViewId - SeslSimpleMonthView.this.findDayOffset()));
        }

        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfoCompat node) {
            virtualViewId -= SeslSimpleMonthView.this.findDayOffset();
            getItemBounds(virtualViewId, this.mTempRect);
            node.setContentDescription(getItemDescription(virtualViewId));
            node.setBoundsInParent(this.mTempRect);
            node.addAction(16);
            if (SeslSimpleMonthView.this.mSelectedDay != -1 && virtualViewId == SeslSimpleMonthView.this.mSelectedDay) {
                node.addAction(4);
                node.setClickable(true);
                node.setCheckable(true);
                node.setChecked(true);
            }
        }

        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            int i = 0;
            switch (action) {
                case 16:
                    virtualViewId -= SeslSimpleMonthView.this.findDayOffset();
                    if (SeslSimpleMonthView.this.mIsFirstMonth && virtualViewId < SeslSimpleMonthView.this.mEnabledDayStart) {
                        return true;
                    }
                    if (SeslSimpleMonthView.this.mIsLastMonth && virtualViewId > SeslSimpleMonthView.this.mEnabledDayEnd) {
                        return true;
                    }
                    int month;
                    Calendar calendar;
                    if (virtualViewId <= 0) {
                        if (SeslSimpleMonthView.this.mIsLunar) {
                            int access$800 = SeslSimpleMonthView.this.mMonth;
                            if (!SeslSimpleMonthView.this.mIsLeapMonth) {
                                i = 1;
                            }
                            month = access$800 - i;
                            if (month < 0) {
                                SeslSimpleMonthView.this.onDeactivatedDayClick(SeslSimpleMonthView.this.mYear - 1, month, SeslSimpleMonthView.this.getDaysInMonthLunar(11, SeslSimpleMonthView.this.mYear - 1, SeslSimpleMonthView.this.mIsLeapMonth) + virtualViewId, true);
                                return true;
                            }
                            SeslSimpleMonthView.this.onDeactivatedDayClick(SeslSimpleMonthView.this.mYear, month, SeslSimpleMonthView.this.getDaysInMonthLunar(month, SeslSimpleMonthView.this.mYear, SeslSimpleMonthView.this.mIsLeapMonth) + virtualViewId, true);
                            return true;
                        }
                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(SeslSimpleMonthView.this.mYear, SeslSimpleMonthView.this.mMonth, 1);
                        calendar.add(5, virtualViewId - 1);
                        SeslSimpleMonthView.this.onDeactivatedDayClick(calendar.get(1), calendar.get(2), calendar.get(5), true);
                        return true;
                    } else if (virtualViewId <= SeslSimpleMonthView.this.mNumCells) {
                        SeslSimpleMonthView.this.onDayClick(SeslSimpleMonthView.this.mYear, SeslSimpleMonthView.this.mMonth, virtualViewId);
                        return true;
                    } else if (SeslSimpleMonthView.this.mIsLunar) {
                        month = SeslSimpleMonthView.this.mMonth + 1;
                        if (month > 11) {
                            SeslSimpleMonthView.this.onDeactivatedDayClick(SeslSimpleMonthView.this.mYear + 1, 0, virtualViewId - SeslSimpleMonthView.this.mNumCells, false);
                            return true;
                        }
                        SeslSimpleMonthView.this.onDeactivatedDayClick(SeslSimpleMonthView.this.mYear, month, virtualViewId - SeslSimpleMonthView.this.mNumCells, false);
                        return true;
                    } else {
                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(SeslSimpleMonthView.this.mYear, SeslSimpleMonthView.this.mMonth, SeslSimpleMonthView.this.mNumCells);
                        calendar.add(5, virtualViewId - SeslSimpleMonthView.this.mNumCells);
                        SeslSimpleMonthView.this.onDeactivatedDayClick(calendar.get(1), calendar.get(2), calendar.get(5), false);
                        return true;
                    }
                default:
                    return false;
            }
        }

        private void getItemBounds(int day, Rect rect) {
            int offsetX = SeslSimpleMonthView.this.mPadding;
            int offsetY = (int) (-1.0f * SeslSimpleMonthView.this.mContext.getResources().getDisplayMetrics().density);
            int cellHeight = SeslSimpleMonthView.this.mWeekHeight;
            int cellWidth = SeslSimpleMonthView.this.mCalendarWidth / SeslSimpleMonthView.this.mNumDays;
            int index = (day - 1) + SeslSimpleMonthView.this.findDayOffset();
            int x = offsetX + ((index % SeslSimpleMonthView.this.mNumDays) * cellWidth);
            int y = offsetY + ((index / SeslSimpleMonthView.this.mNumDays) * cellHeight);
            rect.set(x, y, x + cellWidth, y + cellHeight);
        }

        private CharSequence getItemDescription(int day) {
            CharSequence charSequence;
            this.mTempCalendar.set(SeslSimpleMonthView.this.mYear, SeslSimpleMonthView.this.mMonth, day);
            CharSequence date = DateUtils.formatDateTime(SeslSimpleMonthView.this.mContext, this.mTempCalendar.getTimeInMillis(), 22);
            if (SeslSimpleMonthView.this.mIsLunar && SeslSimpleMonthView.this.mPathClassLoader != null) {
                int year = SeslSimpleMonthView.this.mYear;
                int month = SeslSimpleMonthView.this.mMonth;
                boolean isLeapMonth = SeslSimpleMonthView.this.mIsLeapMonth;
                if (day <= 0) {
                    month = SeslSimpleMonthView.this.mMonth - (SeslSimpleMonthView.this.mIsLeapMonth ? 0 : 1);
                    isLeapMonth = SeslSimpleMonthView.this.mIsPrevMonthLeap;
                    if (month < 0) {
                        year--;
                        month = 11;
                    }
                    day += SeslSimpleMonthView.this.getDaysInMonthLunar(month, year, isLeapMonth);
                } else if (day > SeslSimpleMonthView.this.mNumCells) {
                    month = SeslSimpleMonthView.this.mMonth + (SeslSimpleMonthView.this.mIsNextMonthLeap ? 0 : 1);
                    isLeapMonth = SeslSimpleMonthView.this.mIsNextMonthLeap;
                    if (month > 11) {
                        year++;
                        month = 0;
                    }
                    day -= SeslSimpleMonthView.this.mNumCells;
                }
                SeslSimpleMonthView.this.invoke(SeslSimpleMonthView.this.mSolarLunarConverter, SeslSimpleMonthView.this.mConvertLunarToSolarMethod, Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Boolean.valueOf(isLeapMonth));
                Object lunarYear = SeslSimpleMonthView.this.invoke(SeslSimpleMonthView.this.mSolarLunarConverter, SeslSimpleMonthView.this.mGetYearMethod, new Object[0]);
                Object lunarMonth = SeslSimpleMonthView.this.invoke(SeslSimpleMonthView.this.mSolarLunarConverter, SeslSimpleMonthView.this.mGetMonthMethod, new Object[0]);
                Object lunarDay = SeslSimpleMonthView.this.invoke(SeslSimpleMonthView.this.mSolarLunarConverter, SeslSimpleMonthView.this.mGetDayMethod, new Object[0]);
                if ((lunarYear instanceof Integer) && (lunarMonth instanceof Integer) && (lunarDay instanceof Integer)) {
                    Calendar.getInstance().set(((Integer) lunarYear).intValue(), ((Integer) lunarMonth).intValue(), ((Integer) lunarDay).intValue());
                    try {
                        Class<?> lunarDateUtilsClass = Class.forName("com.android.calendar.event.widget.datetimepicker.LunarDateUtils", true, SeslSimpleMonthView.this.mPathClassLoader);
                        if (lunarDateUtilsClass == null) {
                            Log.e(SeslSimpleMonthView.TAG, "getItemDescription, Calendar LunarDateUtils class is null");
                            return date;
                        }
                        CharSequence string = SeslSimpleMonthView.this.invoke(null, SeslSimpleMonthView.this.getMethod(lunarDateUtilsClass, "buildLunarDateString", Calendar.class, Context.class), calendar, SeslSimpleMonthView.this.getContext());
                        if (string instanceof String) {
                            date = string;
                        }
                    } catch (ClassNotFoundException e) {
                        Log.e(SeslSimpleMonthView.TAG, "getItemDescription, Calendar LunarDateUtils class not found");
                        charSequence = date;
                        return date;
                    }
                }
            }
            charSequence = date;
            return date;
        }
    }

    public SeslSimpleMonthView(Context context) {
        this(context, null);
    }

    public SeslSimpleMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843612);
    }

    public SeslSimpleMonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mDayColorSet = new int[7];
        this.mMode = 0;
        this.mDayOfWeekStart = 0;
        this.mPadding = 0;
        this.mSelectedDay = -1;
        this.mWeekStart = 1;
        this.mNumDays = 7;
        this.mNumCells = this.mNumDays;
        this.mEnabledDayStart = 1;
        this.mEnabledDayEnd = 31;
        this.mCalendar = Calendar.getInstance();
        this.mMinDate = Calendar.getInstance();
        this.mMaxDate = Calendar.getInstance();
        this.mTempDate = Calendar.getInstance();
        this.mIsLunar = false;
        this.mIsLeapMonth = false;
        this.mPathClassLoader = null;
        this.mIsFirstMonth = false;
        this.mIsLastMonth = false;
        this.mIsPrevMonthLeap = false;
        this.mIsNextMonthLeap = false;
        this.mContext = context;
        this.mIsRTL = isRTL();
        Resources res = context.getResources();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(C0247R.attr.colorPrimaryDark, outValue, true);
        if (outValue.resourceId != 0) {
            this.mSelectedDayColor = res.getColor(outValue.resourceId);
        } else {
            this.mSelectedDayColor = outValue.data;
        }
        this.mSundayTextColor = res.getColor(C0247R.color.sesl_date_picker_sunday_number_text_color_light);
        this.mSaturdayTextColor = res.getColor(C0247R.color.sesl_date_picker_saturday_text_color_light);
        TypedArray array = this.mContext.obtainStyledAttributes(attrs, C0247R.styleable.DatePicker, defStyle, 0);
        this.mNormalTextColor = array.getColor(C0247R.styleable.DatePicker_dayNumberTextColor, res.getColor(C0247R.color.sesl_date_picker_normal_day_number_text_color_light));
        this.mSelectedDayNumberTextColor = array.getColor(C0247R.styleable.DatePicker_selectedDayNumberTextColor, res.getColor(C0247R.color.sesl_date_picker_selected_day_number_text_color_light));
        this.mDayNumberDisabledAlpha = array.getInteger(C0247R.styleable.DatePicker_dayNumberDisabledAlpha, res.getInteger(C0247R.integer.sesl_day_number_disabled_alpha_light));
        array.recycle();
        this.mWeekHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_week_height);
        this.mDaySelectedCircleSize = res.getDimensionPixelSize(C0247R.dimen.sesl_date_picker_selected_day_circle_radius);
        this.mDaySelectedCircleStroke = res.getDimensionPixelSize(C0247R.dimen.sesl_date_picker_selected_day_circle_stroke);
        this.mMiniDayNumberTextSize = res.getDimensionPixelSize(C0247R.dimen.sesl_date_picker_day_number_text_size);
        this.mCalendarWidth = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_width);
        this.mPadding = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_padding);
        this.mTouchHelper = new MonthViewTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, this.mTouchHelper);
        setImportantForAccessibility(1);
        this.mLockAccessibilityDelegate = true;
        if (System.getString(this.mContext.getContentResolver(), "current_sec_active_themepackage") != null) {
            this.mDayNumberDisabledAlpha = res.getInteger(C0247R.integer.sesl_day_number_theme_disabled_alpha);
        }
        this.mPrevNextMonthDayNumberAlpha = res.getInteger(C0247R.integer.sesl_day_number_theme_disabled_alpha);
        this.mAbnormalStartEndDateBackgroundAlpha = res.getInteger(C0247R.integer.sesl_date_picker_abnormal_start_end_date_background_alpha);
        initView();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mIsRTL = isRTL();
        this.mTouchHelper.invalidateRoot();
        Resources res = this.mContext.getResources();
        this.mWeekHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_week_height);
        this.mDaySelectedCircleSize = res.getDimensionPixelSize(C0247R.dimen.sesl_date_picker_selected_day_circle_radius);
        this.mMiniDayNumberTextSize = res.getDimensionPixelSize(C0247R.dimen.sesl_date_picker_day_number_text_size);
        this.mCalendarWidth = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_width);
        initView();
    }

    void setTextColor() {
        String weekdayFeatureString = SeslCscFeatureReflector.getString(TAG_CSCFEATURE_CALENDAR_SETCOLOROFDAYS, DEFAULT_WEEK_DAY_STRING_FEATURE);
        for (int i = 0; i < this.mNumDays; i++) {
            char parsedColor = weekdayFeatureString.charAt(i);
            int index = (i + 2) % this.mNumDays;
            if (parsedColor == 'R') {
                this.mDayColorSet[index] = this.mSundayTextColor;
            } else if (parsedColor == 'B') {
                this.mDayColorSet[index] = this.mSaturdayTextColor;
            } else {
                this.mDayColorSet[index] = this.mNormalTextColor;
            }
        }
    }

    public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
        if (!this.mLockAccessibilityDelegate) {
            super.setAccessibilityDelegate(delegate);
        }
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.mOnDayClickListener = listener;
    }

    public void setOnDeactivatedDayClickListener(OnDeactivatedDayClickListener listener) {
        this.mOnDeactivatedDayClickListener = listener;
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        return this.mTouchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int i = 0;
        switch (event.getAction()) {
            case 1:
                int day = getDayFromLocation(event.getX(), event.getY());
                if ((!this.mIsFirstMonth || day >= this.mEnabledDayStart) && (!this.mIsLastMonth || day <= this.mEnabledDayEnd)) {
                    Calendar calendar;
                    int i2;
                    if (day > 0) {
                        if (day > this.mNumCells) {
                            if (!this.mIsLunar) {
                                calendar = Calendar.getInstance();
                                calendar.clear();
                                calendar.set(this.mYear, this.mMonth, this.mNumCells);
                                calendar.add(5, day - this.mNumCells);
                                onDeactivatedDayClick(calendar.get(1), calendar.get(2), calendar.get(5), false);
                                break;
                            }
                            int nextYear = this.mYear;
                            int i3 = this.mMonth;
                            if (this.mIsNextMonthLeap) {
                                i2 = 0;
                            } else {
                                i2 = 1;
                            }
                            int nextMonth = i3 + i2;
                            if (nextMonth > 11) {
                                nextYear++;
                                nextMonth = 0;
                            }
                            onDeactivatedDayClick(nextYear, nextMonth, day - this.mNumCells, false);
                            break;
                        }
                        onDayClick(this.mYear, this.mMonth, day);
                        break;
                    } else if (!this.mIsLunar) {
                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(this.mYear, this.mMonth, 1);
                        calendar.add(5, day - 1);
                        onDeactivatedDayClick(calendar.get(1), calendar.get(2), calendar.get(5), true);
                        break;
                    } else {
                        int prevYear = this.mYear;
                        i2 = this.mMonth;
                        if (!this.mIsLeapMonth) {
                            i = 1;
                        }
                        int prevMonth = i2 - i;
                        if (prevMonth < 0) {
                            prevYear--;
                            prevMonth = 11;
                        }
                        onDeactivatedDayClick(prevYear, prevMonth, getDaysInMonthLunar(prevMonth, prevYear, this.mIsPrevMonthLeap) + day, true);
                        break;
                    }
                }
        }
        return true;
    }

    private void initView() {
        this.mDayNumberSelectedPaint = new Paint();
        this.mDayNumberSelectedPaint.setAntiAlias(true);
        this.mDayNumberSelectedPaint.setColor(this.mSelectedDayColor);
        this.mDayNumberSelectedPaint.setTextAlign(Align.CENTER);
        this.mDayNumberSelectedPaint.setStrokeWidth((float) this.mDaySelectedCircleStroke);
        this.mDayNumberSelectedPaint.setFakeBoldText(true);
        this.mDayNumberSelectedPaint.setStyle(Style.FILL);
        this.mAbnormalSelectedDayPaint = new Paint(this.mDayNumberSelectedPaint);
        this.mAbnormalSelectedDayPaint.setColor(this.mNormalTextColor);
        this.mAbnormalSelectedDayPaint.setAlpha(this.mAbnormalStartEndDateBackgroundAlpha);
        this.mDayNumberPaint = new Paint();
        this.mDayNumberPaint.setAntiAlias(true);
        this.mDayNumberPaint.setTextSize((float) this.mMiniDayNumberTextSize);
        this.mDayNumberPaint.setTypeface(Typeface.create("sec-roboto-light", 0));
        this.mDayNumberPaint.setTextAlign(Align.CENTER);
        this.mDayNumberPaint.setStyle(Style.FILL);
        this.mDayNumberPaint.setFakeBoldText(false);
    }

    protected void onDraw(Canvas canvas) {
        drawDays(canvas);
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= 1 && day <= 7;
    }

    private static boolean isValidMonth(int month) {
        return month >= 0 && month <= 11;
    }

    void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart, int enabledDayEnd, Calendar minDate, Calendar maxDate, int startYear, int startMonth, int startDay, int isLeapStartMonth, int endYear, int endMonth, int endDay, int isLeapEndMonth, int mode) {
        this.mMode = mode;
        if (this.mWeekHeight < 10) {
            this.mWeekHeight = 10;
        }
        this.mSelectedDay = selectedDay;
        if (isValidMonth(month)) {
            this.mMonth = month;
        }
        this.mYear = year;
        this.mCalendar.clear();
        this.mCalendar.set(2, this.mMonth);
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(5, 1);
        this.mMinDate = minDate;
        this.mMaxDate = maxDate;
        if (!this.mIsLunar || this.mSolarLunarConverter == null) {
            this.mDayOfWeekStart = this.mCalendar.get(7);
            this.mNumCells = getDaysInMonth(this.mMonth, this.mYear);
        } else {
            invoke(this.mSolarLunarConverter, this.mConvertLunarToSolarMethod, Integer.valueOf(this.mYear), Integer.valueOf(this.mMonth), Integer.valueOf(1), Boolean.valueOf(this.mIsLeapMonth));
            Object lunarYear = invoke(this.mSolarLunarConverter, this.mGetYearMethod, new Object[0]);
            Object lunarMonth = invoke(this.mSolarLunarConverter, this.mGetMonthMethod, new Object[0]);
            Object lunarDay = invoke(this.mSolarLunarConverter, this.mGetDayMethod, new Object[0]);
            if ((lunarYear instanceof Integer) && (lunarMonth instanceof Integer) && (lunarDay instanceof Integer)) {
                Object dayOfWeekStart = invoke(this.mSolarLunarConverter, this.mGetWeekDayMethod, lunarYear, lunarMonth, lunarDay);
                if (dayOfWeekStart instanceof Integer) {
                    this.mDayOfWeekStart = ((Integer) dayOfWeekStart).intValue() + 1;
                }
            } else {
                this.mDayOfWeekStart = this.mCalendar.get(7);
            }
            this.mNumCells = getDaysInMonthLunar(this.mMonth, this.mYear, this.mIsLeapMonth);
        }
        if (isValidDayOfWeek(weekStart)) {
            this.mWeekStart = weekStart;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        if (this.mMonth == minDate.get(2) && this.mYear == minDate.get(1)) {
            enabledDayStart = minDate.get(5);
        }
        if (this.mMonth == maxDate.get(2) && this.mYear == maxDate.get(1)) {
            enabledDayEnd = maxDate.get(5);
        }
        if (enabledDayStart > 0 && enabledDayEnd < 32) {
            this.mEnabledDayStart = enabledDayStart;
        }
        if (enabledDayEnd > 0 && enabledDayEnd < 32 && enabledDayEnd >= enabledDayStart) {
            this.mEnabledDayEnd = enabledDayEnd;
        }
        this.mTouchHelper.invalidateRoot();
        this.mStartYear = startYear;
        this.mStartMonth = startMonth;
        this.mStartDay = startDay;
        this.mIsLeapStartMonth = isLeapStartMonth;
        this.mEndYear = endYear;
        this.mEndMonth = endMonth;
        this.mEndDay = endDay;
        this.mIsLeapEndMonth = isLeapEndMonth;
    }

    private int getDaysInMonthLunar(int month, int year, boolean isLeapMonth) {
        int solarDay = getDaysInMonth(month, year);
        if (this.mSolarLunarConverter == null) {
            Log.e(TAG, "getDaysInMonthLunar, mSolarLunarConverter is null");
            return solarDay;
        }
        Object dayLength = invoke(this.mSolarLunarConverter, this.mDayLengthMethod, Integer.valueOf(year), Integer.valueOf(month), Boolean.valueOf(isLeapMonth));
        if (dayLength instanceof Integer) {
            return ((Integer) dayLength).intValue();
        }
        Log.e(TAG, "getDaysInMonthLunar, dayLength is not Integer");
        return solarDay;
    }

    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                if (year % 4 != 0) {
                    return 28;
                }
                if (year % 100 != 0 || year % 400 == 0) {
                    return 29;
                }
                return 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(makeMeasureSpec(widthMeasureSpec, this.mCalendarWidth), heightMeasureSpec);
        this.mTouchHelper.invalidateRoot();
    }

    private int makeMeasureSpec(int measureSpec, int maxSize) {
        if (maxSize == -1) {
            return measureSpec;
        }
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        switch (mode) {
            case Integer.MIN_VALUE:
                this.mCalendarWidth = Math.min(size, maxSize);
                return MeasureSpec.makeMeasureSpec(this.mCalendarWidth, 1073741824);
            case 0:
                return MeasureSpec.makeMeasureSpec(maxSize, 1073741824);
            case 1073741824:
                this.mCalendarWidth = size;
                return measureSpec;
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mTouchHelper.invalidateRoot();
    }

    private void drawDays(Canvas canvas) {
        int y = (this.mWeekHeight * 2) / 3;
        int dayWidthHalf = this.mCalendarWidth / (this.mNumDays * 2);
        int j = findDayOffset();
        int start = -1;
        int end = -1;
        float circleYPosAdjust = ((float) this.mMiniDayNumberTextSize) / DIVISOR_FOR_CIRCLE_POSITION_Y;
        int lineNum = 0;
        int dayOffset = j;
        int startYear = this.mStartYear;
        float startMonth = (float) this.mStartMonth;
        int startDay = this.mStartDay;
        int endYear = this.mEndYear;
        float endMonth = (float) this.mEndMonth;
        int endDay = this.mEndDay;
        if (this.mIsLunar && this.mIsLeapStartMonth == 1) {
            startMonth += LEAP_MONTH_WEIGHT;
        }
        if (this.mIsLunar && this.mIsLeapEndMonth == 1) {
            endMonth += LEAP_MONTH_WEIGHT;
        }
        int year = this.mYear;
        float month = (float) this.mMonth;
        if (this.mIsLunar && this.mIsLeapMonth) {
            month += LEAP_MONTH_WEIGHT;
        }
        boolean invalidStartEndDate = false;
        int startYearMonthWeight = (startYear * YEAR_WEIGHT) + ((int) (100.0f * startMonth));
        int endYearMonthWeight = (endYear * YEAR_WEIGHT) + ((int) (100.0f * endMonth));
        int selectedYearMonthWeight = (year * YEAR_WEIGHT) + ((int) (100.0f * month));
        if (this.mMode != 0) {
            invalidStartEndDate = startYearMonthWeight + startDay > endYearMonthWeight + endDay;
        }
        if (!invalidStartEndDate) {
            if (startYear == endYear && startMonth == endMonth && year == startYear && month == startMonth) {
                start = startDay;
                end = endDay;
            } else if (startYearMonthWeight < selectedYearMonthWeight && selectedYearMonthWeight < endYearMonthWeight && (year != endYear || month != endMonth)) {
                start = 0;
                end = this.mNumCells + 1;
            } else if (year == startYear && month == startMonth) {
                start = startDay;
                end = this.mNumCells + 1;
            } else if (year == endYear && month == endMonth) {
                start = 0;
                end = endDay;
            }
        }
        int day = 1;
        while (day <= this.mNumCells) {
            int x;
            float startX;
            float startY;
            float circleStartY;
            if (this.mIsRTL) {
                x = (((((this.mNumDays - 1) - j) * 2) + 1) * dayWidthHalf) + this.mPadding;
            } else {
                x = (((j * 2) + 1) * dayWidthHalf) + this.mPadding;
            }
            this.mDayNumberPaint.setColor(this.mDayColorSet[(this.mWeekStart + j) % this.mNumDays]);
            if (day < this.mEnabledDayStart || day > this.mEnabledDayEnd) {
                this.mDayNumberPaint.setAlpha(this.mDayNumberDisabledAlpha);
            }
            Paint paint = this.mDayNumberPaint;
            if (invalidStartEndDate) {
                if ((startYear == year && startMonth == month && startDay == day && this.mMode == 2) || (endYear == year && endMonth == month && endDay == day && this.mMode == 1)) {
                    canvas.drawCircle((float) x, ((float) y) - circleYPosAdjust, (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
                    paint.setColor(this.mSelectedDayNumberTextColor);
                }
                if ((endYear == year && endMonth == month && endDay == day && this.mMode == 2) || (startYear == year && startMonth == month && startDay == day && this.mMode == 1)) {
                    canvas.drawCircle((float) x, ((float) y) - circleYPosAdjust, (float) this.mDaySelectedCircleSize, this.mAbnormalSelectedDayPaint);
                }
            } else {
                if (start < day && day < end) {
                    startX = (float) (x - dayWidthHalf);
                    startY = (((float) y) - circleYPosAdjust) - ((float) this.mDaySelectedCircleSize);
                    canvas.drawRect(startX, startY, startX + ((float) (dayWidthHalf * 2)), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                    paint.setColor(this.mSelectedDayNumberTextColor);
                }
                if (start != -1 && start == end && day == start) {
                    canvas.drawCircle((float) x, ((float) y) - circleYPosAdjust, (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
                    paint.setColor(this.mSelectedDayNumberTextColor);
                } else if (end == day) {
                    circleStartY = ((float) y) - circleYPosAdjust;
                    startX = this.mIsRTL ? (float) x : (float) (x - dayWidthHalf);
                    startY = circleStartY - ((float) this.mDaySelectedCircleSize);
                    canvas.drawRect(startX, startY, startX + ((float) dayWidthHalf), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                    canvas.drawCircle((float) x, circleStartY, (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
                    paint.setColor(this.mSelectedDayNumberTextColor);
                } else if (start == day) {
                    circleStartY = ((float) y) - circleYPosAdjust;
                    if (this.mIsRTL) {
                        startX = (float) (x - dayWidthHalf);
                    } else {
                        startX = (float) x;
                    }
                    startY = circleStartY - ((float) this.mDaySelectedCircleSize);
                    canvas.drawRect(startX, startY, startX + ((float) dayWidthHalf), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                    canvas.drawCircle((float) x, circleStartY, (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
                    paint.setColor(this.mSelectedDayNumberTextColor);
                }
            }
            if (this.mMode == 0 && day == end) {
                paint.setColor(this.mSelectedDayNumberTextColor);
            }
            canvas.drawText(String.format("%d", new Object[]{Integer.valueOf(day)}), (float) x, (float) y, paint);
            j++;
            if (j == this.mNumDays) {
                j = 0;
                y += this.mWeekHeight;
                lineNum++;
            }
            day++;
        }
        if (!this.mIsLastMonth) {
            int next = 1;
            while (lineNum != 6) {
                if (this.mIsRTL) {
                    x = (((((this.mNumDays - 1) - j) * 2) + 1) * dayWidthHalf) + this.mPadding;
                } else {
                    x = (((j * 2) + 1) * dayWidthHalf) + this.mPadding;
                }
                this.mDayNumberPaint.setColor(this.mDayColorSet[(this.mWeekStart + j) % this.mNumDays]);
                this.mDayNumberPaint.setAlpha(this.mPrevNextMonthDayNumberAlpha);
                if (this.mMode != 0 && end == this.mNumCells + 1) {
                    if (next < this.mEndDay || !isNextMonthEndMonth()) {
                        startX = (float) (x - dayWidthHalf);
                        startY = (((float) y) - circleYPosAdjust) - ((float) this.mDaySelectedCircleSize);
                        canvas.drawRect(startX, startY, startX + ((float) (dayWidthHalf * 2)), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                    } else if (next == this.mEndDay) {
                        circleStartY = ((float) y) - circleYPosAdjust;
                        if (this.mIsRTL) {
                            startX = (float) x;
                        } else {
                            startX = (float) (x - dayWidthHalf);
                        }
                        startY = circleStartY - ((float) this.mDaySelectedCircleSize);
                        canvas.drawRect(startX, startY, startX + ((float) dayWidthHalf), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                        canvas.drawCircle((float) x, circleStartY, (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
                    }
                }
                if (!this.mIsLunar) {
                    int nextMonth = this.mMonth + 1;
                    int nextYear = this.mYear;
                    if (nextMonth > 11) {
                        nextMonth = 0;
                        nextYear++;
                    }
                    this.mTempDate.clear();
                    this.mTempDate.set(nextYear, nextMonth, next);
                    if (this.mTempDate.after(this.mMaxDate)) {
                        this.mDayNumberPaint.setAlpha(this.mDayNumberDisabledAlpha);
                    }
                }
                paint = this.mDayNumberPaint;
                if (this.mMode != 0 && end == this.mNumCells + 1 && (next <= this.mEndDay || !isNextMonthEndMonth())) {
                    paint.setColor(this.mSelectedDayNumberTextColor);
                }
                canvas.drawText(String.format("%d", new Object[]{Integer.valueOf(next)}), (float) x, (float) y, paint);
                j++;
                if (j == this.mNumDays) {
                    j = 0;
                    y += this.mWeekHeight;
                    lineNum++;
                }
                next++;
            }
        }
        if (dayOffset > 0 && !this.mIsFirstMonth) {
            int prevYear;
            int prevMonth;
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(this.mYear, this.mMonth, 1);
            calendar.add(5, -dayOffset);
            int prevMonthDay = calendar.get(5);
            if (this.mIsLunar) {
                prevYear = this.mYear;
                prevMonth = this.mMonth - (this.mIsLeapMonth ? 0 : 1);
                if (prevMonth < 0) {
                    prevYear--;
                    prevMonth = 11;
                }
                prevMonthDay = (getDaysInMonthLunar(prevMonth, prevYear, this.mIsPrevMonthLeap) - dayOffset) + 1;
            }
            int prevMonthDayConut = dayOffset;
            for (int prev = 0; prev < prevMonthDayConut; prev++) {
                if (this.mIsRTL) {
                    x = (((((this.mNumDays - 1) - prev) * 2) + 1) * dayWidthHalf) + this.mPadding;
                } else {
                    x = (((prev * 2) + 1) * dayWidthHalf) + this.mPadding;
                }
                y = (this.mWeekHeight * 2) / 3;
                this.mDayNumberPaint.setColor(this.mDayColorSet[(this.mWeekStart + prev) % this.mNumDays]);
                this.mDayNumberPaint.setAlpha(this.mPrevNextMonthDayNumberAlpha);
                if (this.mMode != 0 && start == 0) {
                    if (prevMonthDay > this.mStartDay || !isPrevMonthStartMonth()) {
                        startX = (float) (x - dayWidthHalf);
                        startY = (((float) y) - circleYPosAdjust) - ((float) this.mDaySelectedCircleSize);
                        canvas.drawRect(startX, startY, startX + ((float) (dayWidthHalf * 2)), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                    } else if (prevMonthDay == this.mStartDay) {
                        circleStartY = ((float) y) - circleYPosAdjust;
                        if (this.mIsRTL) {
                            startX = (float) (x - dayWidthHalf);
                        } else {
                            startX = (float) x;
                        }
                        startY = circleStartY - ((float) this.mDaySelectedCircleSize);
                        canvas.drawRect(startX, startY, startX + ((float) dayWidthHalf), startY + ((float) (this.mDaySelectedCircleSize * 2)), this.mDayNumberSelectedPaint);
                        canvas.drawCircle((float) x, circleStartY, (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
                    }
                }
                if (!this.mIsLunar) {
                    prevMonth = this.mMonth - 1;
                    prevYear = this.mYear;
                    if (prevMonth < 0) {
                        prevMonth = 11;
                        prevYear--;
                    }
                    this.mTempDate.clear();
                    this.mTempDate.set(prevYear, prevMonth, prevMonthDay);
                    Calendar minDate = Calendar.getInstance();
                    minDate.clear();
                    minDate.set(this.mMinDate.get(1), this.mMinDate.get(2), this.mMinDate.get(5));
                    if (this.mTempDate.before(this.mMinDate)) {
                        this.mDayNumberPaint.setAlpha(this.mDayNumberDisabledAlpha);
                    }
                }
                paint = this.mDayNumberPaint;
                if (this.mMode != 0 && start == 0 && (prevMonthDay >= this.mStartDay || !isPrevMonthStartMonth())) {
                    paint.setColor(this.mSelectedDayNumberTextColor);
                }
                canvas.drawText(String.format("%d", new Object[]{Integer.valueOf(prevMonthDay)}), (float) x, (float) y, this.mDayNumberPaint);
                prevMonthDay++;
            }
        }
    }

    private boolean isPrevMonthStartMonth() {
        if (this.mIsLunar) {
            float month = (float) this.mMonth;
            float startMonth = (float) this.mStartMonth;
            if (this.mIsLeapMonth) {
                month += LEAP_MONTH_WEIGHT;
            }
            if (this.mIsLeapStartMonth == 1) {
                startMonth += LEAP_MONTH_WEIGHT;
            }
            float monthDiff = month - startMonth;
            if (this.mYear != this.mStartYear || (monthDiff >= 1.0f && (monthDiff != 1.0f || this.mIsPrevMonthLeap))) {
                if (this.mYear != this.mStartYear + 1) {
                    return false;
                }
                if (12.0f + monthDiff >= 1.0f && (12.0f + monthDiff != 1.0f || this.mIsPrevMonthLeap)) {
                    return false;
                }
            }
            return true;
        } else if ((this.mYear == this.mStartYear && this.mMonth == this.mStartMonth + 1) || (this.mYear == this.mStartYear + 1 && this.mMonth == 0 && this.mStartMonth == 11)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNextMonthEndMonth() {
        if (this.mIsLunar) {
            float month = (float) this.mMonth;
            float endMonth = (float) this.mEndMonth;
            if (this.mIsLeapMonth) {
                month += LEAP_MONTH_WEIGHT;
            }
            if (this.mIsLeapEndMonth == 1) {
                endMonth += LEAP_MONTH_WEIGHT;
            }
            float monthDiff = endMonth - month;
            if (this.mYear != this.mEndYear || (monthDiff >= 1.0f && (monthDiff != 1.0f || this.mIsNextMonthLeap))) {
                if (this.mYear != this.mEndYear - 1) {
                    return false;
                }
                if (12.0f + monthDiff >= 1.0f && (12.0f + monthDiff != 1.0f || this.mIsNextMonthLeap)) {
                    return false;
                }
            }
            return true;
        } else if ((this.mYear == this.mEndYear && this.mMonth == this.mEndMonth - 1) || (this.mYear == this.mEndYear - 1 && this.mMonth == 11 && this.mEndMonth == 0)) {
            return true;
        } else {
            return false;
        }
    }

    private int findDayOffset() {
        return (this.mDayOfWeekStart < this.mWeekStart ? this.mDayOfWeekStart + this.mNumDays : this.mDayOfWeekStart) - this.mWeekStart;
    }

    private int getDayFromLocation(float x, float y) {
        int dayStart = this.mPadding;
        if (this.mIsRTL) {
            x = ((float) this.mCalendarWidth) - x;
        }
        if (x < ((float) dayStart) || x > ((float) (this.mCalendarWidth + this.mPadding))) {
            return -1;
        }
        return ((((int) (((x - ((float) dayStart)) * ((float) this.mNumDays)) / ((float) this.mCalendarWidth))) - findDayOffset()) + 1) + (this.mNumDays * (((int) y) / this.mWeekHeight));
    }

    private void onDayClick(int year, int month, int day) {
        if (this.mOnDayClickListener != null) {
            playSoundEffect(0);
            this.mOnDayClickListener.onDayClick(this, year, month, day);
        }
        this.mTouchHelper.sendEventForVirtualView(day, 1);
    }

    private void onDeactivatedDayClick(int year, int month, int day, boolean isPrevMonth) {
        if (!this.mIsLunar) {
            this.mTempDate.clear();
            this.mTempDate.set(year, month, day);
            if (isPrevMonth) {
                Calendar minDate = Calendar.getInstance();
                minDate.clear();
                minDate.set(this.mMinDate.get(1), this.mMinDate.get(2), this.mMinDate.get(5));
                if (this.mTempDate.before(minDate)) {
                    return;
                }
            } else if (this.mTempDate.after(this.mMaxDate)) {
                return;
            }
        }
        if (this.mOnDeactivatedDayClickListener != null) {
            playSoundEffect(0);
            this.mOnDeactivatedDayClickListener.onDeactivatedDayClick(this, year, month, day, this.mIsLeapMonth, isPrevMonth);
        }
        this.mTouchHelper.sendEventForVirtualView(day, 1);
    }

    public void clearAccessibilityFocus() {
        this.mTouchHelper.clearFocusedVirtualView();
    }

    public int getWeekStart() {
        return this.mWeekStart;
    }

    public int getNumDays() {
        return this.mNumDays;
    }

    public void setStartDate(Calendar startDate, int isLeapMonth) {
        this.mStartYear = startDate.get(1);
        this.mStartMonth = startDate.get(2);
        this.mStartDay = startDate.get(5);
        this.mIsLeapStartMonth = isLeapMonth;
    }

    public void setEndDate(Calendar endDate, int isLeapMonth) {
        this.mEndYear = endDate.get(1);
        this.mEndMonth = endDate.get(2);
        this.mEndDay = endDate.get(5);
        this.mIsLeapEndMonth = isLeapMonth;
    }

    private boolean isRTL() {
        Locale defLocale = Locale.getDefault();
        if ("ur".equals(defLocale.getLanguage())) {
            return false;
        }
        byte defDirectionality = Character.getDirectionality(defLocale.getDisplayName(defLocale).charAt(0));
        if (defDirectionality == (byte) 1 || defDirectionality == (byte) 2) {
            return true;
        }
        return false;
    }

    public void setLunar(boolean isLunar, boolean isLeapMonth, PathClassLoader pathClassLoader) {
        this.mIsLunar = isLunar;
        this.mIsLeapMonth = isLeapMonth;
        if (this.mIsLunar && this.mSolarLunarConverter == null) {
            this.mPathClassLoader = pathClassLoader;
            try {
                Class<?> calendarFeatureClass = Class.forName("com.android.calendar.Feature", true, this.mPathClassLoader);
                if (calendarFeatureClass == null) {
                    Log.e(TAG, "setLunar, Calendar Feature class is null");
                    return;
                }
                this.mSolarLunarConverter = invoke(null, getMethod(calendarFeatureClass, "getSolarLunarConverter", new Class[0]), new Object[0]);
                try {
                    Class<?> solarLunarConverterClass = Class.forName("com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarConverter", true, this.mPathClassLoader);
                    if (solarLunarConverterClass == null) {
                        Log.e(TAG, "setLunar, Calendar Converter class is null");
                        return;
                    }
                    this.mConvertLunarToSolarMethod = getMethod(solarLunarConverterClass, "convertLunarToSolar", Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                    this.mGetWeekDayMethod = getMethod(solarLunarConverterClass, "getWeekday", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                    this.mGetYearMethod = getMethod(solarLunarConverterClass, "getYear", new Class[0]);
                    this.mGetMonthMethod = getMethod(solarLunarConverterClass, "getMonth", new Class[0]);
                    this.mGetDayMethod = getMethod(solarLunarConverterClass, "getDay", new Class[0]);
                    this.mDayLengthMethod = getMethod(solarLunarConverterClass, "getDayLengthOf", Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "setLunar, Calendar Converter class not found");
                }
            } catch (ClassNotFoundException e2) {
                Log.e(TAG, "setLunar, Calendar Feature class not found");
            }
        }
    }

    private <T> Method getMethod(Class<T> className, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = className.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, methodName + " NoSuchMethodException", e);
        }
        return method;
    }

    private Object invoke(Object callerInstance, Method method, Object... args) {
        Object obj = null;
        if (method == null) {
            Log.e(TAG, "method is null");
        } else {
            try {
                obj = method.invoke(callerInstance, args);
            } catch (IllegalAccessException e) {
                Log.e(TAG, method.getName() + " IllegalAccessException", e);
            } catch (IllegalArgumentException e2) {
                Log.e(TAG, method.getName() + " IllegalArgumentException", e2);
            } catch (InvocationTargetException e3) {
                Log.e(TAG, method.getName() + " InvocationTargetException", e3);
            }
        }
        return obj;
    }

    public void setFirstMonth() {
        this.mIsFirstMonth = true;
    }

    public void setLastMonth() {
        this.mIsLastMonth = true;
    }

    public void setPrevMonthLeap() {
        this.mIsPrevMonthLeap = true;
    }

    public void setNextMonthLeap() {
        this.mIsNextMonthLeap = true;
    }
}
