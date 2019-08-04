package android.support.v7.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.feature.SeslCscFeatureReflector;
import android.support.v4.feature.SeslFloatingFeatureReflector;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SeslHoverPopupWindowReflector;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslDatePickerSpinnerLayout.OnSpinnerDateChangedListener;
import android.support.v7.widget.SeslSimpleMonthView.OnDayClickListener;
import android.support.v7.widget.SeslSimpleMonthView.OnDeactivatedDayClickListener;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import dalvik.system.PathClassLoader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class SeslDatePicker extends LinearLayout implements OnDayClickListener, OnDeactivatedDayClickListener, OnClickListener, OnLongClickListener {
    public static final int DATE_MODE_END = 2;
    public static final int DATE_MODE_NONE = 0;
    public static final int DATE_MODE_START = 1;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final int DEFAULT_MONTH_PER_YEAR = 12;
    private static final int DEFAULT_START_YEAR = 1902;
    private static final int LEAP_MONTH = 1;
    private static final float MAX_FONT_SCALE = 1.2f;
    private static final int MESSAGE_CALENDAR_HEADER_MONTH_BUTTON_SET = 1001;
    private static final int MESSAGE_CALENDAR_HEADER_TEXT_VALUE_SET = 1000;
    private static final int NOT_LEAP_MONTH = 0;
    private static final boolean SESL_DEBUG = false;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final String TAG = "Picker";
    private static final String TAG_CSCFEATURE_CALENDAR_SETCOLOROFDAYS = "CscFeature_Calendar_SetColorOfDays";
    private static final int USE_LOCALE = 0;
    public static final int VIEW_TYPE_CALENDAR = 0;
    public static final int VIEW_TYPE_SPINNER = 1;
    private static PackageManager mPackageManager;
    private ViewAnimator mAnimator;
    private int mBackgroundBorderlessResId;
    private final OnFocusChangeListener mBtnFocusChangeListener;
    private RelativeLayout mCalendarHeader;
    private OnClickListener mCalendarHeaderClickListener;
    private RelativeLayout mCalendarHeaderLayout;
    private int mCalendarHeaderLayoutHeight;
    private TextView mCalendarHeaderText;
    private CalendarPagerAdapter mCalendarPagerAdapter;
    private int mCalendarViewMargin;
    private ViewPager mCalendarViewPager;
    private int mCalendarViewPagerHeight;
    private int mCalendarViewPagerWidth;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private Context mContext;
    private Calendar mCurrentDate;
    private Locale mCurrentLocale;
    private int mCurrentPosition;
    private int mCurrentViewType;
    private View mCustomButtonView;
    private int mDatePickerHeight;
    private LinearLayout mDatePickerLayout;
    private SimpleDateFormat mDayFormatter;
    private LinearLayout mDayOfTheWeekLayout;
    private int mDayOfTheWeekLayoutHeight;
    private int mDayOfTheWeekLayoutWidth;
    private DayOfTheWeekView mDayOfTheWeekView;
    private Calendar mEndDate;
    private View mFirstBlankSpace;
    private int mFirstBlankSpaceHeight;
    private int mFirstDayOfWeek;
    private Method mGetLunarMethod;
    private Handler mHandler;
    private Field mIndexOfLeapMonthField;
    private boolean mIsConfigurationChanged;
    private boolean mIsEnabled;
    private boolean mIsFarsiLanguage;
    private boolean mIsFirstMeasure;
    private boolean mIsFromSetLunar;
    private int mIsLeapEndMonth;
    private boolean mIsLeapMonth;
    private int mIsLeapStartMonth;
    private boolean mIsLunar;
    private boolean mIsLunarSupported;
    private boolean mIsRTL;
    private boolean mIsSimplifiedChinese;
    private boolean mIsTibetanLanguage;
    private boolean mLunarChanged;
    private int mLunarCurrentDay;
    private int mLunarCurrentMonth;
    private int mLunarCurrentYear;
    private int mLunarEndDay;
    private int mLunarEndMonth;
    private int mLunarEndYear;
    private int mLunarStartDay;
    private int mLunarStartMonth;
    private int mLunarStartYear;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private int mMode;
    private OnKeyListener mMonthBtnKeyListener;
    private OnTouchListener mMonthBtnTouchListener;
    private ImageButton mNextButton;
    private int mNumDays;
    private int mOldCalendarViewPagerWidth;
    private int mOldSelectedDay;
    private OnDateChangedListener mOnDateChangedListener;
    private int mPadding;
    PathClassLoader mPathClassLoader;
    private int mPositionCount;
    private ImageButton mPrevButton;
    private View mSecondBlankSpace;
    private int mSecondBlankSpaceHeight;
    private Object mSolarLunarTables;
    private SeslDatePickerSpinnerLayout mSpinnerLayout;
    private Calendar mStartDate;
    private Field mStartOfLunarYearField;
    private Calendar mTempDate;
    private Calendar mTempMinMaxDate;
    private int[] mTotalMonthCountWithLeap;
    private ValidationCallback mValidationCallback;
    private int mWeekStart;
    private Field mWidthPerYearField;

    public interface ValidationCallback {
        void onValidationChanged(boolean z);
    }

    public interface OnDateChangedListener {
        void onDateChanged(SeslDatePicker seslDatePicker, int i, int i2, int i3);
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$1 */
    class C03561 implements OnFocusChangeListener {
        C03561() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                SeslDatePicker.this.removeAllCallbacks();
            }
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$3 */
    class C03583 implements OnTouchListener {
        C03583() {
        }

        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == 1 || event.getAction() == 3) {
                SeslDatePicker.this.removeAllCallbacks();
            }
            return false;
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$4 */
    class C03594 implements OnKeyListener {
        C03594() {
        }

        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (SeslDatePicker.this.mIsRTL) {
                SeslDatePicker.this.mIsConfigurationChanged = false;
            }
            if (event.getAction() == 1 || event.getAction() == 3) {
                SeslDatePicker.this.removeAllCallbacks();
            }
            return false;
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$5 */
    class C03605 implements OnClickListener {
        C03605() {
        }

        public void onClick(View v) {
            SeslDatePicker.this.setCurrentViewType((SeslDatePicker.this.mCurrentViewType + 1) % 2);
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$6 */
    class C03616 implements OnSpinnerDateChangedListener {
        C03616() {
        }

        public void onDateChanged(SeslDatePickerSpinnerLayout view, int year, int monthOfYear, int dayOfMonth) {
            boolean z = true;
            SeslDatePicker.this.mCurrentDate.set(1, year);
            SeslDatePicker.this.mCurrentDate.set(2, monthOfYear);
            SeslDatePicker.this.mCurrentDate.set(5, dayOfMonth);
            if (SeslDatePicker.this.mIsLunar) {
                SeslDatePicker.this.mLunarCurrentYear = year;
                SeslDatePicker.this.mLunarCurrentMonth = monthOfYear;
                SeslDatePicker.this.mLunarCurrentDay = dayOfMonth;
            }
            switch (SeslDatePicker.this.mMode) {
                case 1:
                    SeslDatePicker.this.mStartDate.clear();
                    SeslDatePicker.this.mStartDate.set(1, year);
                    SeslDatePicker.this.mStartDate.set(2, monthOfYear);
                    SeslDatePicker.this.mStartDate.set(5, dayOfMonth);
                    if (SeslDatePicker.this.mIsLunar) {
                        SeslDatePicker.this.mLunarStartYear = year;
                        SeslDatePicker.this.mLunarStartMonth = monthOfYear;
                        SeslDatePicker.this.mLunarStartDay = dayOfMonth;
                        SeslDatePicker.this.mIsLeapStartMonth = 0;
                        break;
                    }
                    break;
                case 2:
                    SeslDatePicker.this.mEndDate.clear();
                    SeslDatePicker.this.mEndDate.set(1, year);
                    SeslDatePicker.this.mEndDate.set(2, monthOfYear);
                    SeslDatePicker.this.mEndDate.set(5, dayOfMonth);
                    if (SeslDatePicker.this.mIsLunar) {
                        SeslDatePicker.this.mLunarEndYear = year;
                        SeslDatePicker.this.mLunarEndMonth = monthOfYear;
                        SeslDatePicker.this.mLunarEndDay = dayOfMonth;
                        SeslDatePicker.this.mIsLeapEndMonth = 0;
                        break;
                    }
                    break;
                default:
                    SeslDatePicker.this.mStartDate.clear();
                    SeslDatePicker.this.mStartDate.set(1, year);
                    SeslDatePicker.this.mStartDate.set(2, monthOfYear);
                    SeslDatePicker.this.mStartDate.set(5, dayOfMonth);
                    SeslDatePicker.this.mEndDate.clear();
                    SeslDatePicker.this.mEndDate.set(1, year);
                    SeslDatePicker.this.mEndDate.set(2, monthOfYear);
                    SeslDatePicker.this.mEndDate.set(5, dayOfMonth);
                    if (SeslDatePicker.this.mIsLunar) {
                        SeslDatePicker.this.mLunarStartYear = year;
                        SeslDatePicker.this.mLunarStartMonth = monthOfYear;
                        SeslDatePicker.this.mLunarStartDay = dayOfMonth;
                        SeslDatePicker.this.mIsLeapStartMonth = 0;
                        SeslDatePicker.this.mLunarEndYear = year;
                        SeslDatePicker.this.mLunarEndMonth = monthOfYear;
                        SeslDatePicker.this.mLunarEndDay = dayOfMonth;
                        SeslDatePicker.this.mIsLeapEndMonth = 0;
                        SeslDatePicker.this.mIsLeapMonth = false;
                        break;
                    }
                    break;
            }
            SeslDatePicker seslDatePicker = SeslDatePicker.this;
            if (SeslDatePicker.this.mStartDate.after(SeslDatePicker.this.mEndDate)) {
                z = false;
            }
            seslDatePicker.onValidationChanged(z);
            SeslDatePicker.this.updateSimpleMonthView(false);
            SeslDatePicker.this.onDateChanged();
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$7 */
    class C03627 implements OnFocusChangeListener {
        C03627() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && SeslDatePicker.this.mCurrentViewType == 1) {
                SeslDatePicker.this.setEditTextMode(false);
            }
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$8 */
    class C03638 implements Runnable {
        C03638() {
        }

        public void run() {
            SeslDatePicker.this.updateSimpleMonthView(false);
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePicker$9 */
    class C03649 implements Runnable {
        C03649() {
        }

        public void run() {
            SeslDatePicker.this.updateSimpleMonthView(false);
        }
    }

    private class CalendarPageChangeListener implements OnPageChangeListener {
        private CalendarPageChangeListener() {
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            if (SeslDatePicker.this.mIsRTL) {
                SeslDatePicker.this.mIsConfigurationChanged = false;
            }
            if (SeslDatePicker.this.mIsFromSetLunar) {
                SeslDatePicker.this.mIsFromSetLunar = false;
                return;
            }
            SeslDatePicker.this.mCurrentPosition = position;
            int currentMonth = position + SeslDatePicker.this.getMinMonth();
            int year = (currentMonth / 12) + SeslDatePicker.this.getMinYear();
            int month = currentMonth % 12;
            int day = SeslDatePicker.this.mCurrentDate.get(5);
            if (SeslDatePicker.this.mIsLunar) {
                LunarDate lunarDate = SeslDatePicker.this.getLunarDateByPosition(position);
                year = lunarDate.year;
                month = lunarDate.month;
                day = SeslDatePicker.this.mLunarCurrentDay;
                SeslDatePicker.this.mIsLeapMonth = lunarDate.isLeapMonth;
            }
            boolean isYearChanged = false;
            if (year != SeslDatePicker.this.mTempDate.get(1)) {
                isYearChanged = true;
            }
            SeslDatePicker.this.mTempDate.set(1, year);
            SeslDatePicker.this.mTempDate.set(2, month);
            SeslDatePicker.this.mTempDate.set(5, 1);
            if (day > SeslDatePicker.this.mTempDate.getActualMaximum(5)) {
                day = SeslDatePicker.this.mTempDate.getActualMaximum(5);
            }
            SeslDatePicker.this.mTempDate.set(5, day);
            Message msg = SeslDatePicker.this.mHandler.obtainMessage();
            msg.what = 1000;
            msg.obj = Boolean.valueOf(isYearChanged);
            SeslDatePicker.this.mHandler.sendMessage(msg);
            Message msg1 = SeslDatePicker.this.mHandler.obtainMessage();
            msg1.what = 1001;
            SeslDatePicker.this.mHandler.sendMessage(msg1);
            SparseArray<SeslSimpleMonthView> views = SeslDatePicker.this.mCalendarPagerAdapter.views;
            if (views.get(position) != null) {
                ((SeslSimpleMonthView) views.get(position)).clearAccessibilityFocus();
                if (VERSION.SDK_INT >= 16) {
                    ((SeslSimpleMonthView) views.get(position)).setImportantForAccessibility(1);
                }
            }
            if (!(position == 0 || views.get(position - 1) == null)) {
                ((SeslSimpleMonthView) views.get(position - 1)).clearAccessibilityFocus();
                if (VERSION.SDK_INT >= 16) {
                    ((SeslSimpleMonthView) views.get(position - 1)).setImportantForAccessibility(2);
                }
            }
            if (position != SeslDatePicker.this.mPositionCount - 1 && views.get(position + 1) != null) {
                ((SeslSimpleMonthView) views.get(position + 1)).clearAccessibilityFocus();
                if (VERSION.SDK_INT >= 16) {
                    ((SeslSimpleMonthView) views.get(position + 1)).setImportantForAccessibility(2);
                }
            }
        }
    }

    private class CalendarPagerAdapter extends PagerAdapter {
        SparseArray<SeslSimpleMonthView> views = new SparseArray();

        public int getItemPosition(Object object) {
            return -2;
        }

        public int getCount() {
            SeslDatePicker.this.mPositionCount = ((SeslDatePicker.this.getMaxMonth() - SeslDatePicker.this.getMinMonth()) + 1) + ((SeslDatePicker.this.getMaxYear() - SeslDatePicker.this.getMinYear()) * 12);
            if (SeslDatePicker.this.mIsLunar) {
                SeslDatePicker.this.mPositionCount = SeslDatePicker.this.getTotalMonthCountWithLeap(SeslDatePicker.this.getMaxYear());
            }
            return SeslDatePicker.this.mPositionCount;
        }

        public Object instantiateItem(View pager, int position) {
            SeslSimpleMonthView v = new SeslSimpleMonthView(SeslDatePicker.this.mContext);
            SeslDatePicker.this.seslLog("instantiateItem : " + position);
            v.setClickable(true);
            v.setOnDayClickListener(SeslDatePicker.this);
            v.setOnDeactivatedDayClickListener(SeslDatePicker.this);
            v.setTextColor();
            int currentMonth = position + SeslDatePicker.this.getMinMonth();
            int year = (currentMonth / 12) + SeslDatePicker.this.getMinYear();
            int month = currentMonth % 12;
            boolean isLeapMonth = false;
            if (SeslDatePicker.this.mIsLunar) {
                LunarDate lunarDate = SeslDatePicker.this.getLunarDateByPosition(position);
                year = lunarDate.year;
                month = lunarDate.month;
                isLeapMonth = lunarDate.isLeapMonth;
            }
            int selectedDay = -1;
            if (SeslDatePicker.this.mCurrentDate.get(1) == year && SeslDatePicker.this.mCurrentDate.get(2) == month) {
                selectedDay = SeslDatePicker.this.mCurrentDate.get(5);
            }
            if (SeslDatePicker.this.mIsLunar) {
                selectedDay = -1;
                if (SeslDatePicker.this.mLunarCurrentYear == year && SeslDatePicker.this.mLunarCurrentMonth == month) {
                    selectedDay = SeslDatePicker.this.mLunarCurrentDay;
                }
            }
            if (SeslDatePicker.this.mIsLunarSupported) {
                v.setLunar(SeslDatePicker.this.mIsLunar, isLeapMonth, SeslDatePicker.this.mPathClassLoader);
            }
            int startYear = SeslDatePicker.this.mStartDate.get(1);
            int startMonth = SeslDatePicker.this.mStartDate.get(2);
            int startDay = SeslDatePicker.this.mStartDate.get(5);
            int endYear = SeslDatePicker.this.mEndDate.get(1);
            int endMonth = SeslDatePicker.this.mEndDate.get(2);
            int endDay = SeslDatePicker.this.mEndDate.get(5);
            if (SeslDatePicker.this.mIsLunar) {
                startYear = SeslDatePicker.this.mLunarStartYear;
                startMonth = SeslDatePicker.this.mLunarStartMonth;
                startDay = SeslDatePicker.this.mLunarStartDay;
                endYear = SeslDatePicker.this.mLunarEndYear;
                endMonth = SeslDatePicker.this.mLunarEndMonth;
                endDay = SeslDatePicker.this.mLunarEndDay;
            }
            v.setMonthParams(selectedDay, month, year, SeslDatePicker.this.getFirstDayOfWeek(), 1, 31, SeslDatePicker.this.mMinDate, SeslDatePicker.this.mMaxDate, startYear, startMonth, startDay, SeslDatePicker.this.mIsLeapStartMonth, endYear, endMonth, endDay, SeslDatePicker.this.mIsLeapEndMonth, SeslDatePicker.this.mMode);
            if (position == 0) {
                v.setFirstMonth();
            } else if (position == SeslDatePicker.this.mPositionCount - 1) {
                v.setLastMonth();
            }
            if (SeslDatePicker.this.mIsLunar) {
                if (position != 0 && SeslDatePicker.this.getLunarDateByPosition(position - 1).isLeapMonth) {
                    v.setPrevMonthLeap();
                }
                if (position != SeslDatePicker.this.mPositionCount - 1 && SeslDatePicker.this.getLunarDateByPosition(position + 1).isLeapMonth) {
                    v.setNextMonthLeap();
                }
            }
            SeslDatePicker.this.mNumDays = v.getNumDays();
            SeslDatePicker.this.mWeekStart = v.getWeekStart();
            ((ViewPager) pager).addView(v, 0);
            this.views.put(position, v);
            return v;
        }

        public void destroyItem(View pager, int position, Object view) {
            SeslDatePicker.this.seslLog("destroyItem : " + position);
            ((ViewPager) pager).removeView((View) view);
            this.views.remove(position);
        }

        public boolean isViewFromObject(View pager, Object obj) {
            return pager != null && pager.equals(obj);
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View view) {
            SeslDatePicker.this.seslLog("startUpdate");
        }

        public void finishUpdate(View view) {
            SeslDatePicker.this.seslLog("finishUpdate");
        }
    }

    class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        ChangeCurrentByOneFromLongPressCommand() {
        }

        private void setStep(boolean increment) {
            this.mIncrement = increment;
        }

        public void run() {
            if (this.mIncrement) {
                SeslDatePicker.this.mCalendarViewPager.setCurrentItem(SeslDatePicker.this.mCurrentPosition + 1);
            } else {
                SeslDatePicker.this.mCalendarViewPager.setCurrentItem(SeslDatePicker.this.mCurrentPosition - 1);
            }
            SeslDatePicker.this.postDelayed(this, SeslDatePicker.DEFAULT_LONG_PRESS_UPDATE_INTERVAL);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DateMode {
    }

    public class DayOfTheWeekView extends View {
        private int[] mDayColorSet = new int[7];
        private Calendar mDayLabelCalendar = Calendar.getInstance();
        private String mDefaultWeekdayFeatureString = "XXXXXXR";
        private Paint mMonthDayLabelPaint;
        private int mNormalDayTextColor;
        private int mSaturdayTextColor;
        private int mSundayTextColor;
        private String mWeekdayFeatureString;

        public DayOfTheWeekView(Context context, TypedArray array) {
            super(context);
            Resources res = context.getResources();
            int monthDayLabelTextSize = res.getDimensionPixelSize(C0247R.dimen.sesl_date_picker_month_day_label_text_size);
            this.mNormalDayTextColor = array.getColor(C0247R.styleable.DatePicker_dayTextColor, res.getColor(C0247R.color.sesl_date_picker_normal_text_color_light));
            this.mSundayTextColor = array.getColor(C0247R.styleable.DatePicker_sundayTextColor, res.getColor(C0247R.color.sesl_date_picker_sunday_text_color_light));
            this.mSaturdayTextColor = ResourcesCompat.getColor(res, C0247R.color.sesl_date_picker_saturday_text_color_light, null);
            this.mWeekdayFeatureString = SeslCscFeatureReflector.getString(SeslDatePicker.TAG_CSCFEATURE_CALENDAR_SETCOLOROFDAYS, this.mDefaultWeekdayFeatureString);
            this.mMonthDayLabelPaint = new Paint();
            this.mMonthDayLabelPaint.setAntiAlias(true);
            this.mMonthDayLabelPaint.setColor(this.mNormalDayTextColor);
            this.mMonthDayLabelPaint.setTextSize((float) monthDayLabelTextSize);
            this.mMonthDayLabelPaint.setTypeface(Typeface.create("sec-roboto-light", 0));
            this.mMonthDayLabelPaint.setTextAlign(Align.CENTER);
            this.mMonthDayLabelPaint.setStyle(Style.FILL);
            this.mMonthDayLabelPaint.setFakeBoldText(false);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (SeslDatePicker.this.mNumDays != 0) {
                int i;
                int y = (SeslDatePicker.this.mDayOfTheWeekLayoutHeight * 2) / 3;
                int dayWidthHalf = SeslDatePicker.this.mDayOfTheWeekLayoutWidth / (SeslDatePicker.this.mNumDays * 2);
                for (i = 0; i < SeslDatePicker.this.mNumDays; i++) {
                    int index = (i + 2) % SeslDatePicker.this.mNumDays;
                    switch (this.mWeekdayFeatureString.charAt(i)) {
                        case 'B':
                            this.mDayColorSet[index] = this.mSaturdayTextColor;
                            break;
                        case 'R':
                            this.mDayColorSet[index] = this.mSundayTextColor;
                            break;
                        default:
                            this.mDayColorSet[index] = this.mNormalDayTextColor;
                            break;
                    }
                }
                for (i = 0; i < SeslDatePicker.this.mNumDays; i++) {
                    int x;
                    int calendarDay = (SeslDatePicker.this.mWeekStart + i) % SeslDatePicker.this.mNumDays;
                    this.mDayLabelCalendar.set(7, calendarDay);
                    String dayLabel = SeslDatePicker.this.mDayFormatter.format(this.mDayLabelCalendar.getTime()).toUpperCase();
                    if (SeslDatePicker.this.mIsRTL) {
                        x = (((((SeslDatePicker.this.mNumDays - 1) - i) * 2) + 1) * dayWidthHalf) + SeslDatePicker.this.mPadding;
                    } else {
                        x = (((i * 2) + 1) * dayWidthHalf) + SeslDatePicker.this.mPadding;
                    }
                    this.mMonthDayLabelPaint.setColor(this.mDayColorSet[calendarDay]);
                    canvas.drawText(dayLabel, (float) x, (float) y, this.mMonthDayLabelPaint);
                }
            }
        }
    }

    public static class LunarDate {
        public int day;
        public boolean isLeapMonth;
        public int month;
        public int year;

        public LunarDate() {
            this.year = 1900;
            this.month = 1;
            this.day = 1;
            this.isLeapMonth = false;
        }

        public LunarDate(int year, int month, int day, boolean isLeap) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.isLeapMonth = isLeap;
        }

        public void set(int year, int month, int day, boolean isLeap) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.isLeapMonth = isLeap;
        }
    }

    public static class LunarUtils {
        private static PathClassLoader mClassLoader = null;

        public static PathClassLoader getPathClassLoader(Context context) {
            if (mClassLoader == null) {
                try {
                    ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(SeslDatePicker.getCalendarPackageName(), 128);
                    if (appInfo == null) {
                        Log.e(SeslDatePicker.TAG, "getPathClassLoader, appInfo is null");
                        return null;
                    }
                    String calendarPkgPath = appInfo.sourceDir;
                    if (calendarPkgPath == null || TextUtils.isEmpty(calendarPkgPath)) {
                        Log.e(SeslDatePicker.TAG, "getPathClassLoader, calendar package source directory is null or empty");
                        return null;
                    }
                    mClassLoader = new PathClassLoader(calendarPkgPath, ClassLoader.getSystemClassLoader());
                } catch (NameNotFoundException e) {
                    Log.e(SeslDatePicker.TAG, "getPathClassLoader, calendar package name not found");
                    return null;
                }
            }
            return mClassLoader;
        }
    }

    public interface OnEditTextModeChangedListener {
        void onEditTextModeChanged(SeslDatePicker seslDatePicker, boolean z);
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C03651();
        private final long mMaxDate;
        private final long mMinDate;
        private final int mSelectedDay;
        private final int mSelectedMonth;
        private final int mSelectedYear;

        /* renamed from: android.support.v7.widget.SeslDatePicker$SavedState$1 */
        static class C03651 implements Creator<SavedState> {
            C03651() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        private SavedState(Parcelable superState, int year, int month, int day, long minDate, long maxDate, int listPosition) {
            super(superState);
            this.mSelectedYear = year;
            this.mSelectedMonth = month;
            this.mSelectedDay = day;
            this.mMinDate = minDate;
            this.mMaxDate = maxDate;
        }

        private SavedState(Parcel in) {
            super(in);
            this.mSelectedYear = in.readInt();
            this.mSelectedMonth = in.readInt();
            this.mSelectedDay = in.readInt();
            this.mMinDate = in.readLong();
            this.mMaxDate = in.readLong();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mSelectedYear);
            dest.writeInt(this.mSelectedMonth);
            dest.writeInt(this.mSelectedDay);
            dest.writeLong(this.mMinDate);
            dest.writeLong(this.mMaxDate);
        }

        public int getSelectedDay() {
            return this.mSelectedDay;
        }

        public int getSelectedMonth() {
            return this.mSelectedMonth;
        }

        public int getSelectedYear() {
            return this.mSelectedYear;
        }

        public long getMinDate() {
            return this.mMinDate;
        }

        public long getMaxDate() {
            return this.mMaxDate;
        }
    }

    public SeslDatePicker(Context context) {
        this(context, null);
    }

    public SeslDatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 16843612);
    }

    public SeslDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIsConfigurationChanged = false;
        this.mIsFirstMeasure = true;
        this.mIsEnabled = true;
        this.mCurrentViewType = -1;
        this.mOldSelectedDay = -1;
        this.mPadding = 0;
        this.mBackgroundBorderlessResId = -1;
        this.mMode = 0;
        this.mFirstDayOfWeek = 0;
        this.mIsLunarSupported = false;
        this.mIsLunar = false;
        this.mIsLeapMonth = false;
        this.mIsFromSetLunar = false;
        this.mLunarChanged = false;
        this.mPathClassLoader = null;
        this.mBtnFocusChangeListener = new C03561();
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1000:
                        if (SeslDatePicker.this.mTempDate.get(1) <= SeslDatePicker.this.getMaxYear() && SeslDatePicker.this.mTempDate.get(1) >= SeslDatePicker.this.getMinYear()) {
                            String description;
                            SpannableString headerText = new SpannableString(SeslDatePicker.this.getMonthAndYearString(SeslDatePicker.this.mTempDate));
                            headerText.setSpan(new UnderlineSpan(), 0, headerText.length(), 0);
                            SeslDatePicker.this.mCalendarHeaderText.setText(headerText);
                            if (SeslDatePicker.this.mCurrentViewType == 0) {
                                description = SeslDatePicker.this.mContext.getString(C0247R.string.sesl_date_picker_switch_to_wheel_description);
                            } else {
                                description = SeslDatePicker.this.mContext.getString(C0247R.string.sesl_date_picker_switch_to_calendar_description);
                            }
                            SeslDatePicker.this.mCalendarHeaderText.setContentDescription(headerText + ", " + description);
                            return;
                        }
                        return;
                    case 1001:
                        if (SeslDatePicker.this.mCurrentViewType == 1) {
                            SeslDatePicker.this.mPrevButton.setAlpha(0.0f);
                            SeslDatePicker.this.mNextButton.setAlpha(0.0f);
                            SeslDatePicker.this.mPrevButton.setBackground(null);
                            SeslDatePicker.this.mNextButton.setBackground(null);
                            SeslDatePicker.this.mPrevButton.setEnabled(false);
                            SeslDatePicker.this.mNextButton.setEnabled(false);
                            SeslDatePicker.this.mPrevButton.setFocusable(false);
                            SeslDatePicker.this.mNextButton.setFocusable(false);
                            int TYPE_NONE = SeslHoverPopupWindowReflector.getField_TYPE_NONE();
                            if (TYPE_NONE != -1) {
                                SeslViewReflector.semSetHoverPopupType(SeslDatePicker.this.mPrevButton, TYPE_NONE);
                                SeslViewReflector.semSetHoverPopupType(SeslDatePicker.this.mNextButton, TYPE_NONE);
                                return;
                            }
                            return;
                        }
                        int TYPE_TOOLTIP = SeslHoverPopupWindowReflector.getField_TYPE_TOOLTIP();
                        if (TYPE_TOOLTIP != -1) {
                            SeslViewReflector.semSetHoverPopupType(SeslDatePicker.this.mPrevButton, TYPE_TOOLTIP);
                            SeslViewReflector.semSetHoverPopupType(SeslDatePicker.this.mNextButton, TYPE_TOOLTIP);
                        }
                        if (SeslDatePicker.this.mCurrentPosition > 0 && SeslDatePicker.this.mCurrentPosition < SeslDatePicker.this.mPositionCount - 1) {
                            SeslDatePicker.this.mPrevButton.setAlpha(1.0f);
                            SeslDatePicker.this.mNextButton.setAlpha(1.0f);
                            SeslDatePicker.this.mPrevButton.setBackgroundResource(SeslDatePicker.this.mBackgroundBorderlessResId);
                            SeslDatePicker.this.mNextButton.setBackgroundResource(SeslDatePicker.this.mBackgroundBorderlessResId);
                            SeslDatePicker.this.mPrevButton.setEnabled(true);
                            SeslDatePicker.this.mNextButton.setEnabled(true);
                            SeslDatePicker.this.mPrevButton.setFocusable(true);
                            SeslDatePicker.this.mNextButton.setFocusable(true);
                            return;
                        } else if (SeslDatePicker.this.mPositionCount == 1) {
                            SeslDatePicker.this.mPrevButton.setAlpha(0.4f);
                            SeslDatePicker.this.mNextButton.setAlpha(0.4f);
                            SeslDatePicker.this.mPrevButton.setBackground(null);
                            SeslDatePicker.this.mNextButton.setBackground(null);
                            SeslDatePicker.this.mPrevButton.setEnabled(false);
                            SeslDatePicker.this.mNextButton.setEnabled(false);
                            SeslDatePicker.this.mPrevButton.setFocusable(false);
                            SeslDatePicker.this.mNextButton.setFocusable(false);
                            SeslDatePicker.this.removeAllCallbacks();
                            return;
                        } else if (SeslDatePicker.this.mCurrentPosition == 0) {
                            SeslDatePicker.this.mPrevButton.setBackground(null);
                            SeslDatePicker.this.mNextButton.setBackgroundResource(SeslDatePicker.this.mBackgroundBorderlessResId);
                            SeslDatePicker.this.mPrevButton.setAlpha(0.4f);
                            SeslDatePicker.this.mPrevButton.setEnabled(false);
                            SeslDatePicker.this.mPrevButton.setFocusable(false);
                            SeslDatePicker.this.mNextButton.setAlpha(1.0f);
                            SeslDatePicker.this.mNextButton.setEnabled(true);
                            SeslDatePicker.this.mNextButton.setFocusable(true);
                            SeslDatePicker.this.removeAllCallbacks();
                            return;
                        } else if (SeslDatePicker.this.mCurrentPosition == SeslDatePicker.this.mPositionCount - 1) {
                            SeslDatePicker.this.mNextButton.setBackground(null);
                            SeslDatePicker.this.mPrevButton.setBackgroundResource(SeslDatePicker.this.mBackgroundBorderlessResId);
                            SeslDatePicker.this.mNextButton.setAlpha(0.4f);
                            SeslDatePicker.this.mNextButton.setEnabled(false);
                            SeslDatePicker.this.mNextButton.setFocusable(false);
                            SeslDatePicker.this.mPrevButton.setAlpha(1.0f);
                            SeslDatePicker.this.mPrevButton.setEnabled(true);
                            SeslDatePicker.this.mPrevButton.setFocusable(true);
                            SeslDatePicker.this.removeAllCallbacks();
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        };
        this.mMonthBtnTouchListener = new C03583();
        this.mMonthBtnKeyListener = new C03594();
        this.mCalendarHeaderClickListener = new C03605();
        this.mContext = context;
        this.mCurrentLocale = Locale.getDefault();
        this.mIsRTL = isRTL();
        this.mIsFarsiLanguage = isFarsiLanguage();
        this.mIsTibetanLanguage = isTibetanLanguage();
        this.mIsSimplifiedChinese = isSimplifiedChinese();
        if (this.mIsSimplifiedChinese) {
            this.mDayFormatter = new SimpleDateFormat("EEEEE", this.mCurrentLocale);
        } else {
            this.mDayFormatter = new SimpleDateFormat("EEE", this.mCurrentLocale);
        }
        this.mMinDate = getCalendarForLocale(this.mMinDate, this.mCurrentLocale);
        this.mMaxDate = getCalendarForLocale(this.mMaxDate, this.mCurrentLocale);
        this.mTempMinMaxDate = getCalendarForLocale(this.mMaxDate, this.mCurrentLocale);
        this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, this.mCurrentLocale);
        this.mTempDate = getCalendarForLocale(this.mCurrentDate, this.mCurrentLocale);
        Resources res = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, C0247R.styleable.DatePicker, defStyleAttr, defStyleRes);
        int startYear = a.getInt(C0247R.styleable.DatePicker_android_startYear, DEFAULT_START_YEAR);
        int endYear = a.getInt(C0247R.styleable.DatePicker_android_endYear, DEFAULT_END_YEAR);
        this.mMinDate.set(startYear, 0, 1);
        this.mMaxDate.set(endYear, 11, 31);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (VERSION.SDK_INT >= 23) {
            inflater.inflate(C0247R.layout.sesl_date_picker, this, true);
        } else {
            inflater.inflate(C0247R.layout.sesl_date_picker_legacy, this, true);
        }
        int firstDayOfWeek = a.getInt(C0247R.styleable.DatePicker_android_firstDayOfWeek, 0);
        if (firstDayOfWeek != 0) {
            setFirstDayOfWeek(firstDayOfWeek);
        }
        a.recycle();
        TypedArray semArray = this.mContext.obtainStyledAttributes(attrs, C0247R.styleable.DatePicker, defStyleAttr, defStyleRes);
        this.mDayOfTheWeekView = new DayOfTheWeekView(this.mContext, semArray);
        int calendarHeaderTextColor = semArray.getColor(C0247R.styleable.DatePicker_dayNumberTextColor, res.getColor(C0247R.color.sesl_date_picker_normal_day_number_text_color_light));
        int btnTintColor = semArray.getColor(C0247R.styleable.DatePicker_btnTintColor, res.getColor(C0247R.color.sesl_date_picker_button_tint_color_light));
        semArray.recycle();
        this.mCalendarViewPager = (ViewPager) findViewById(C0247R.id.sesl_date_picker_calendar);
        this.mCalendarPagerAdapter = new CalendarPagerAdapter();
        this.mCalendarViewPager.setAdapter(this.mCalendarPagerAdapter);
        this.mCalendarViewPager.setOnPageChangeListener(new CalendarPageChangeListener());
        this.mCalendarViewPager.setMouseWheelEventSupport(true);
        this.mPadding = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_padding);
        this.mCalendarHeader = (RelativeLayout) findViewById(C0247R.id.sesl_date_picker_calendar_header);
        this.mCalendarHeaderText = (TextView) findViewById(C0247R.id.sesl_date_picker_calendar_header_text);
        this.mCalendarHeaderText.setTextColor(calendarHeaderTextColor);
        this.mStartDate = getCalendarForLocale(this.mCurrentDate, this.mCurrentLocale);
        this.mEndDate = getCalendarForLocale(this.mCurrentDate, this.mCurrentLocale);
        this.mAnimator = (ViewAnimator) findViewById(C0247R.id.sesl_date_picker_view_animator);
        this.mSpinnerLayout = (SeslDatePickerSpinnerLayout) findViewById(C0247R.id.sesl_date_picker_spinner_view);
        this.mSpinnerLayout.setOnSpinnerDateChangedListener(this, new C03616());
        this.mCurrentViewType = 0;
        this.mCalendarHeaderText.setOnClickListener(this.mCalendarHeaderClickListener);
        this.mCalendarHeaderText.setOnFocusChangeListener(new C03627());
        this.mDayOfTheWeekLayoutHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_day_height);
        checkMaxFontSize();
        this.mCalendarViewPagerWidth = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_width);
        this.mCalendarViewMargin = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_margin);
        this.mDayOfTheWeekLayoutWidth = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_width);
        this.mDayOfTheWeekLayout = (LinearLayout) findViewById(C0247R.id.sesl_date_picker_day_of_the_week);
        this.mDayOfTheWeekLayout.addView(this.mDayOfTheWeekView);
        this.mDatePickerLayout = (LinearLayout) findViewById(C0247R.id.sesl_date_picker_layout);
        this.mCalendarHeaderLayout = (RelativeLayout) findViewById(C0247R.id.sesl_date_picker_calendar_header_layout);
        if (this.mIsRTL) {
            this.mPrevButton = (ImageButton) findViewById(C0247R.id.sesl_date_picker_calendar_header_next_button);
            this.mNextButton = (ImageButton) findViewById(C0247R.id.sesl_date_picker_calendar_header_prev_button);
            this.mPrevButton.setContentDescription(this.mContext.getString(C0247R.string.sesl_date_picker_increment_month));
            this.mNextButton.setContentDescription(this.mContext.getString(C0247R.string.sesl_date_picker_decrement_month));
        } else {
            this.mPrevButton = (ImageButton) findViewById(C0247R.id.sesl_date_picker_calendar_header_prev_button);
            this.mNextButton = (ImageButton) findViewById(C0247R.id.sesl_date_picker_calendar_header_next_button);
        }
        this.mPrevButton.setOnClickListener(this);
        this.mNextButton.setOnClickListener(this);
        this.mPrevButton.setOnLongClickListener(this);
        this.mNextButton.setOnLongClickListener(this);
        this.mPrevButton.setOnTouchListener(this.mMonthBtnTouchListener);
        this.mNextButton.setOnTouchListener(this.mMonthBtnTouchListener);
        this.mPrevButton.setOnKeyListener(this.mMonthBtnKeyListener);
        this.mNextButton.setOnKeyListener(this.mMonthBtnKeyListener);
        this.mPrevButton.setOnFocusChangeListener(this.mBtnFocusChangeListener);
        this.mNextButton.setOnFocusChangeListener(this.mBtnFocusChangeListener);
        this.mPrevButton.setColorFilter(btnTintColor);
        this.mNextButton.setColorFilter(btnTintColor);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(16843868, outValue, true);
        this.mBackgroundBorderlessResId = outValue.resourceId;
        this.mCalendarHeaderLayoutHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_header_height);
        this.mCalendarViewPagerHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_height);
        this.mOldCalendarViewPagerWidth = this.mCalendarViewPagerWidth;
        this.mCalendarHeaderText.setFocusable(true);
        this.mPrevButton.setNextFocusRightId(C0247R.id.sesl_date_picker_calendar_header_text);
        this.mNextButton.setNextFocusLeftId(C0247R.id.sesl_date_picker_calendar_header_text);
        this.mCalendarHeaderText.setNextFocusRightId(C0247R.id.sesl_date_picker_calendar_header_next_button);
        this.mCalendarHeaderText.setNextFocusLeftId(C0247R.id.sesl_date_picker_calendar_header_prev_button);
        this.mFirstBlankSpace = findViewById(C0247R.id.sesl_date_picker_between_header_and_weekend);
        this.mSecondBlankSpace = findViewById(C0247R.id.sesl_date_picker_between_weekend_and_calender);
        this.mFirstBlankSpaceHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_gap_between_header_and_weekend);
        this.mSecondBlankSpaceHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_gap_between_weekend_and_calender);
        this.mDatePickerHeight = (((this.mCalendarHeaderLayoutHeight + this.mFirstBlankSpaceHeight) + this.mDayOfTheWeekLayoutHeight) + this.mSecondBlankSpaceHeight) + this.mCalendarViewPagerHeight;
        updateSimpleMonthView(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setValidationCallback(ValidationCallback callback) {
        this.mValidationCallback = callback;
    }

    protected void onValidationChanged(boolean valid) {
        if (this.mValidationCallback != null) {
            this.mValidationCallback.onValidationChanged(valid);
        }
    }

    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        }
        long currentTimeMillis = oldCalendar.getTimeInMillis();
        Calendar newCalendar = Calendar.getInstance(locale);
        newCalendar.setTimeInMillis(currentTimeMillis);
        return newCalendar;
    }

    public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
        this.mCurrentDate.set(1, year);
        this.mCurrentDate.set(2, monthOfYear);
        this.mCurrentDate.set(5, dayOfMonth);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = year;
            this.mLunarCurrentMonth = monthOfYear;
            this.mLunarCurrentDay = dayOfMonth;
        }
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate = getCalendarForLocale(this.mMinDate, this.mCurrentLocale);
        }
        if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate = getCalendarForLocale(this.mMaxDate, this.mCurrentLocale);
        }
        this.mOnDateChangedListener = onDateChangedListener;
        updateSimpleMonthView(true);
        onDateChanged();
        this.mSpinnerLayout.setMinDate(this.mMinDate.getTimeInMillis());
        this.mSpinnerLayout.setMaxDate(this.mMaxDate.getTimeInMillis());
        if (this.mCurrentViewType == 0) {
            this.mSpinnerLayout.setVisibility(4);
            this.mSpinnerLayout.setEnabled(false);
        }
        this.mStartDate.clear();
        this.mStartDate.set(1, year);
        this.mStartDate.set(2, monthOfYear);
        this.mStartDate.set(5, dayOfMonth);
        this.mEndDate.clear();
        this.mEndDate.set(1, year);
        this.mEndDate.set(2, monthOfYear);
        this.mEndDate.set(5, dayOfMonth);
        if (this.mIsLunar) {
            this.mLunarStartYear = year;
            this.mLunarStartMonth = monthOfYear;
            this.mLunarStartDay = dayOfMonth;
            this.mLunarEndYear = year;
            this.mLunarEndMonth = monthOfYear;
            this.mLunarEndDay = dayOfMonth;
        }
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        this.mTempDate.set(1, year);
        this.mTempDate.set(2, month);
        this.mTempDate.set(5, dayOfMonth);
        this.mCurrentDate = getCalendarForLocale(this.mTempDate, this.mCurrentLocale);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = year;
            this.mLunarCurrentMonth = month;
            this.mLunarCurrentDay = dayOfMonth;
        }
        switch (this.mMode) {
            case 1:
                this.mStartDate.clear();
                this.mStartDate.set(1, year);
                this.mStartDate.set(2, month);
                this.mStartDate.set(5, dayOfMonth);
                if (this.mIsLunar) {
                    this.mLunarStartYear = year;
                    this.mLunarStartMonth = month;
                    this.mLunarStartDay = dayOfMonth;
                    break;
                }
                break;
            case 2:
                this.mEndDate.clear();
                this.mEndDate.set(1, year);
                this.mEndDate.set(2, month);
                this.mEndDate.set(5, dayOfMonth);
                if (this.mIsLunar) {
                    this.mLunarEndYear = year;
                    this.mLunarEndMonth = month;
                    this.mLunarEndDay = dayOfMonth;
                    break;
                }
                break;
            default:
                this.mStartDate.clear();
                this.mStartDate.set(1, year);
                this.mStartDate.set(2, month);
                this.mStartDate.set(5, dayOfMonth);
                this.mEndDate.clear();
                this.mEndDate.set(1, year);
                this.mEndDate.set(2, month);
                this.mEndDate.set(5, dayOfMonth);
                if (this.mIsLunar) {
                    this.mLunarStartYear = year;
                    this.mLunarStartMonth = month;
                    this.mLunarStartDay = dayOfMonth;
                    this.mLunarEndYear = year;
                    this.mLunarEndMonth = month;
                    this.mLunarEndDay = dayOfMonth;
                    break;
                }
                break;
        }
        updateSimpleMonthView(true);
        onDateChanged();
        SparseArray<SeslSimpleMonthView> views = this.mCalendarPagerAdapter.views;
        SeslSimpleMonthView view = (SeslSimpleMonthView) views.get(this.mCurrentPosition);
        if (view != null) {
            int enabledDayRangeStart;
            int enabledDayRangeEnd;
            if (getMinMonth() == month && getMinYear() == year) {
                enabledDayRangeStart = getMinDay();
            } else {
                enabledDayRangeStart = 1;
            }
            if (getMaxMonth() == month && getMaxYear() == year) {
                enabledDayRangeEnd = getMaxDay();
            } else {
                enabledDayRangeEnd = 31;
            }
            if (this.mIsLunarSupported) {
                view.setLunar(this.mIsLunar, this.mIsLeapMonth, this.mPathClassLoader);
            }
            int startYear = this.mStartDate.get(1);
            int startMonth = this.mStartDate.get(2);
            int startDay = this.mStartDate.get(5);
            int endYear = this.mEndDate.get(1);
            int endMonth = this.mEndDate.get(2);
            int endDay = this.mEndDate.get(5);
            if (this.mIsLunar) {
                startYear = this.mLunarStartYear;
                startMonth = this.mLunarStartMonth;
                startDay = this.mLunarStartDay;
                endYear = this.mLunarEndYear;
                endMonth = this.mLunarEndMonth;
                endDay = this.mLunarEndDay;
            }
            view.setMonthParams(dayOfMonth, month, year, getFirstDayOfWeek(), enabledDayRangeStart, enabledDayRangeEnd, this.mMinDate, this.mMaxDate, startYear, startMonth, startDay, this.mIsLeapStartMonth, endYear, endMonth, endDay, this.mIsLeapEndMonth, this.mMode);
            view.invalidate();
            if (!this.mIsLunar) {
                int prevPos = this.mCurrentPosition - 1;
                if (prevPos >= 0) {
                    SeslSimpleMonthView prevMonth = (SeslSimpleMonthView) views.get(prevPos);
                    if (prevMonth != null) {
                        prevMonth.setStartDate(this.mStartDate, this.mIsLeapStartMonth);
                        prevMonth.setEndDate(this.mEndDate, this.mIsLeapEndMonth);
                    }
                }
                int nextPos = this.mCurrentPosition + 1;
                if (nextPos < this.mPositionCount) {
                    SeslSimpleMonthView nextMonth = (SeslSimpleMonthView) views.get(nextPos);
                    if (nextMonth != null) {
                        nextMonth.setStartDate(this.mStartDate, this.mIsLeapStartMonth);
                        nextMonth.setEndDate(this.mEndDate, this.mIsLeapEndMonth);
                    }
                }
            }
        }
        if (this.mSpinnerLayout != null) {
            this.mSpinnerLayout.updateDate(year, month, dayOfMonth);
        }
    }

    private void onDateChanged() {
        if (this.mOnDateChangedListener != null) {
            int year = this.mCurrentDate.get(1);
            int monthOfYear = this.mCurrentDate.get(2);
            int dayOfMonth = this.mCurrentDate.get(5);
            if (this.mIsLunar) {
                year = this.mLunarCurrentYear;
                monthOfYear = this.mLunarCurrentMonth;
                dayOfMonth = this.mLunarCurrentDay;
            }
            this.mOnDateChangedListener.onDateChanged(this, year, monthOfYear, dayOfMonth);
        }
    }

    public int getYear() {
        if (this.mIsLunar) {
            return this.mLunarCurrentYear;
        }
        return this.mCurrentDate.get(1);
    }

    public int getMonth() {
        if (this.mIsLunar) {
            return this.mLunarCurrentMonth;
        }
        return this.mCurrentDate.get(2);
    }

    public int getDayOfMonth() {
        if (this.mIsLunar) {
            return this.mLunarCurrentDay;
        }
        return this.mCurrentDate.get(5);
    }

    public long getMinDate() {
        return this.mMinDate.getTimeInMillis();
    }

    public Calendar getMinDateCalendar() {
        return this.mMinDate;
    }

    public void setMinDate(long minDate) {
        this.mTempMinMaxDate.setTimeInMillis(minDate);
        if (this.mTempMinMaxDate.get(1) != this.mMinDate.get(1) || this.mTempMinMaxDate.get(6) == this.mMinDate.get(6)) {
            if (this.mIsLunar) {
                setTotalMonthCountWithLeap();
            }
            if (this.mCurrentDate.before(this.mTempMinMaxDate)) {
                this.mCurrentDate.setTimeInMillis(minDate);
                onDateChanged();
            }
            this.mMinDate.setTimeInMillis(minDate);
            this.mSpinnerLayout.setMinDate(this.mMinDate.getTimeInMillis());
            this.mCalendarPagerAdapter.notifyDataSetChanged();
            this.mHandler.postDelayed(new C03638(), 10);
        }
    }

    public long getMaxDate() {
        return this.mMaxDate.getTimeInMillis();
    }

    public Calendar getMaxDateCalendar() {
        return this.mMaxDate;
    }

    public void setMaxDate(long maxDate) {
        this.mTempMinMaxDate.setTimeInMillis(maxDate);
        if (this.mTempMinMaxDate.get(1) != this.mMaxDate.get(1) || this.mTempMinMaxDate.get(6) == this.mMaxDate.get(6)) {
            if (this.mIsLunar) {
                setTotalMonthCountWithLeap();
            }
            if (this.mCurrentDate.after(this.mTempMinMaxDate)) {
                this.mCurrentDate.setTimeInMillis(maxDate);
                onDateChanged();
            }
            this.mMaxDate.setTimeInMillis(maxDate);
            this.mSpinnerLayout.setMaxDate(this.mMaxDate.getTimeInMillis());
            this.mCalendarPagerAdapter.notifyDataSetChanged();
            this.mHandler.postDelayed(new C03649(), 10);
        }
    }

    public int getMinYear() {
        return this.mMinDate.get(1);
    }

    public int getMaxYear() {
        return this.mMaxDate.get(1);
    }

    public int getMinMonth() {
        return this.mMinDate.get(2);
    }

    public int getMaxMonth() {
        return this.mMaxDate.get(2);
    }

    public int getMinDay() {
        return this.mMinDate.get(5);
    }

    public int getMaxDay() {
        return this.mMaxDate.get(5);
    }

    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            super.setEnabled(enabled);
            this.mIsEnabled = enabled;
        }
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        event.getText().add(getFormattedCurrentDate());
        return true;
    }

    private String getFormattedCurrentDate() {
        return DateUtils.formatDateTime(this.mContext, this.mCurrentDate.getTimeInMillis(), 20);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        Locale locale;
        super.onConfigurationChanged(newConfig);
        this.mIsRTL = isRTL();
        this.mIsFarsiLanguage = isFarsiLanguage();
        this.mIsTibetanLanguage = isTibetanLanguage();
        if (VERSION.SDK_INT >= 24) {
            locale = newConfig.getLocales().get(0);
        } else {
            locale = newConfig.locale;
        }
        if (this.mCurrentLocale != locale) {
            this.mCurrentLocale = locale;
            this.mIsSimplifiedChinese = isSimplifiedChinese();
            if (this.mIsSimplifiedChinese) {
                this.mDayFormatter = new SimpleDateFormat("EEEEE", locale);
            } else {
                this.mDayFormatter = new SimpleDateFormat("EEE", locale);
            }
        }
        Resources res = this.mContext.getResources();
        this.mDatePickerLayout.setGravity(1);
        this.mIsFirstMeasure = true;
        this.mCalendarHeaderLayoutHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_header_height);
        this.mCalendarViewPagerHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_view_height);
        this.mDayOfTheWeekLayoutHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_day_height);
        this.mFirstBlankSpaceHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_gap_between_header_and_weekend);
        this.mSecondBlankSpaceHeight = res.getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_gap_between_weekend_and_calender);
        this.mDatePickerHeight = (((this.mCalendarHeaderLayoutHeight + this.mFirstBlankSpaceHeight) + this.mDayOfTheWeekLayoutHeight) + this.mSecondBlankSpaceHeight) + this.mCalendarViewPagerHeight;
        if (this.mIsRTL) {
            this.mIsConfigurationChanged = true;
        }
        checkMaxFontSize();
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek < 1 || firstDayOfWeek > 7) {
            throw new IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
        }
        this.mFirstDayOfWeek = firstDayOfWeek;
    }

    public int getFirstDayOfWeek() {
        if (this.mFirstDayOfWeek != 0) {
            return this.mFirstDayOfWeek;
        }
        return this.mCurrentDate.getFirstDayOfWeek();
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        int year = this.mCurrentDate.get(1);
        int month = this.mCurrentDate.get(2);
        int day = this.mCurrentDate.get(5);
        if (this.mIsLunar) {
            year = this.mLunarCurrentYear;
            month = this.mLunarCurrentMonth;
            day = this.mLunarCurrentDay;
        }
        return new SavedState(superState, year, month, day, this.mMinDate.getTimeInMillis(), this.mMaxDate.getTimeInMillis(), -1);
    }

    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(((BaseSavedState) state).getSuperState());
        SavedState ss = (SavedState) state;
        this.mCurrentDate.set(ss.getSelectedYear(), ss.getSelectedMonth(), ss.getSelectedDay());
        if (this.mIsLunar) {
            this.mLunarCurrentYear = ss.getSelectedYear();
            this.mLunarCurrentMonth = ss.getSelectedMonth();
            this.mLunarCurrentDay = ss.getSelectedDay();
        }
        this.mMinDate.setTimeInMillis(ss.getMinDate());
        this.mMaxDate.setTimeInMillis(ss.getMaxDate());
    }

    public void onDayOfMonthSelected(int year, int month, int day) {
        boolean z = true;
        this.mCurrentDate.set(1, year);
        this.mCurrentDate.set(2, month);
        this.mCurrentDate.set(5, day);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = year;
            this.mLunarCurrentMonth = month;
            this.mLunarCurrentDay = day;
        }
        Message msg = this.mHandler.obtainMessage();
        msg.what = 1000;
        this.mHandler.sendMessage(msg);
        switch (this.mMode) {
            case 1:
                this.mStartDate.clear();
                this.mStartDate.set(1, year);
                this.mStartDate.set(2, month);
                this.mStartDate.set(5, day);
                if (this.mIsLunar) {
                    this.mLunarStartYear = year;
                    this.mLunarStartMonth = month;
                    this.mLunarStartDay = day;
                    this.mIsLeapStartMonth = this.mIsLeapMonth ? 1 : 0;
                    break;
                }
                break;
            case 2:
                this.mEndDate.clear();
                this.mEndDate.set(1, year);
                this.mEndDate.set(2, month);
                this.mEndDate.set(5, day);
                if (this.mIsLunar) {
                    this.mLunarEndYear = year;
                    this.mLunarEndMonth = month;
                    this.mLunarEndDay = day;
                    this.mIsLeapEndMonth = this.mIsLeapMonth ? 1 : 0;
                    break;
                }
                break;
            default:
                this.mStartDate.clear();
                this.mEndDate.clear();
                this.mStartDate.set(1, year);
                this.mStartDate.set(2, month);
                this.mStartDate.set(5, day);
                this.mEndDate.set(1, year);
                this.mEndDate.set(2, month);
                this.mEndDate.set(5, day);
                if (this.mIsLunar) {
                    int i;
                    this.mLunarStartYear = year;
                    this.mLunarStartMonth = month;
                    this.mLunarStartDay = day;
                    this.mIsLeapStartMonth = this.mIsLeapMonth ? 1 : 0;
                    this.mLunarEndYear = year;
                    this.mLunarEndMonth = month;
                    this.mLunarEndDay = day;
                    if (this.mIsLeapMonth) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    this.mIsLeapEndMonth = i;
                    break;
                }
                break;
        }
        if (this.mMode != 0) {
            if (this.mStartDate.after(this.mEndDate)) {
                z = false;
            }
            onValidationChanged(z);
        }
        onDateChanged();
    }

    public Calendar getSelectedDay() {
        return this.mCurrentDate;
    }

    public Calendar getStartDate() {
        return this.mStartDate;
    }

    public Calendar getEndDate() {
        return this.mEndDate;
    }

    private void seslLog(String msg) {
    }

    private boolean isRTL() {
        if ("ur".equals(this.mCurrentLocale.getLanguage())) {
            return false;
        }
        byte defDirectionality = Character.getDirectionality(this.mCurrentLocale.getDisplayName(this.mCurrentLocale).charAt(0));
        if (defDirectionality == (byte) 1 || defDirectionality == (byte) 2) {
            return true;
        }
        return false;
    }

    public void onDayClick(SeslSimpleMonthView view, int year, int month, int selectedDay) {
        int enabledDayRangeStart;
        int enabledDayRangeEnd;
        seslLog("onDayClick day : " + selectedDay);
        int currentYear = this.mCurrentDate.get(1);
        int currentMonth = this.mCurrentDate.get(2);
        if (this.mIsLunar) {
            currentYear = this.mLunarCurrentYear;
            currentMonth = this.mLunarCurrentMonth;
        }
        onDayOfMonthSelected(year, month, selectedDay);
        boolean isNotSamePos = this.mCurrentPosition != (month - getMinMonth()) + ((year - getMinYear()) * 12);
        if (!(year == currentYear && month == currentMonth && selectedDay == this.mOldSelectedDay && !this.mIsLunar && !isNotSamePos)) {
            this.mOldSelectedDay = selectedDay;
            this.mCalendarPagerAdapter.notifyDataSetChanged();
        }
        if (getMinMonth() == month && getMinYear() == year) {
            enabledDayRangeStart = getMinDay();
        } else {
            enabledDayRangeStart = 1;
        }
        if (getMaxMonth() == month && getMaxYear() == year) {
            enabledDayRangeEnd = getMaxDay();
        } else {
            enabledDayRangeEnd = 31;
        }
        if (this.mIsLunarSupported) {
            view.setLunar(this.mIsLunar, this.mIsLeapMonth, this.mPathClassLoader);
        }
        int startYear = this.mStartDate.get(1);
        int startMonth = this.mStartDate.get(2);
        int startDay = this.mStartDate.get(5);
        int endYear = this.mEndDate.get(1);
        int endMonth = this.mEndDate.get(2);
        int endDay = this.mEndDate.get(5);
        if (this.mIsLunar) {
            startYear = this.mLunarStartYear;
            startMonth = this.mLunarStartMonth;
            startDay = this.mLunarStartDay;
            endYear = this.mLunarEndYear;
            endMonth = this.mLunarEndMonth;
            endDay = this.mLunarEndDay;
        }
        view.setMonthParams(selectedDay, month, year, getFirstDayOfWeek(), enabledDayRangeStart, enabledDayRangeEnd, this.mMinDate, this.mMaxDate, startYear, startMonth, startDay, this.mIsLeapStartMonth, endYear, endMonth, endDay, this.mIsLeapEndMonth, this.mMode);
        view.invalidate();
    }

    public void onDeactivatedDayClick(SeslSimpleMonthView view, int year, int month, int selectedDay, boolean isLeapMonth, boolean isPrevMonth) {
        if (this.mIsLunar) {
            LunarDate lunarDate = getLunarDateByPosition(isPrevMonth ? this.mCurrentPosition - 1 : this.mCurrentPosition + 1);
            year = lunarDate.year;
            month = lunarDate.month;
            this.mIsLeapMonth = lunarDate.isLeapMonth;
            this.mCurrentPosition = isPrevMonth ? this.mCurrentPosition - 1 : this.mCurrentPosition + 1;
            this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition);
            onDayClick(view, year, month, selectedDay);
            return;
        }
        onDayClick(view, year, month, selectedDay);
        updateSimpleMonthView(true);
    }

    private void updateSimpleMonthView(boolean animation) {
        int month = this.mCurrentDate.get(2);
        int year = this.mCurrentDate.get(1);
        if (this.mIsLunar) {
            year = this.mLunarCurrentYear;
            month = this.mLunarCurrentMonth;
        }
        if (this.mLunarChanged) {
            month = this.mTempDate.get(2);
            year = this.mTempDate.get(1);
        }
        int position = ((year - getMinYear()) * 12) + (month - getMinMonth());
        if (this.mIsLunar) {
            position = (year == getMinYear() ? -getMinMonth() : getTotalMonthCountWithLeap(year - 1)) + (month < getIndexOfleapMonthOfYear(year) ? month : month + 1);
            if ((this.mMode == 1 && month == this.mLunarStartMonth && this.mIsLeapStartMonth == 1) || ((this.mMode == 2 && month == this.mLunarEndMonth && this.mIsLeapEndMonth == 1) || (this.mMode == 0 && this.mIsLeapMonth))) {
                position++;
            }
        }
        this.mCurrentPosition = position;
        this.mCalendarViewPager.setCurrentItem(position, animation);
        Message msg = this.mHandler.obtainMessage();
        msg.what = 1000;
        msg.obj = Boolean.valueOf(true);
        this.mHandler.sendMessage(msg);
        Message msg1 = this.mHandler.obtainMessage();
        msg1.what = 1001;
        this.mHandler.sendMessage(msg1);
    }

    private static Activity scanForActivity(Context cont) {
        if (cont instanceof Activity) {
            return (Activity) cont;
        }
        if (cont instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        }
        return null;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newWidthMeasureSpec = makeMeasureSpec(widthMeasureSpec, this.mCalendarViewPagerWidth);
        if (VERSION.SDK_INT >= 24) {
            Activity activity = scanForActivity(this.mContext);
            if (activity != null && activity.isInMultiWindowMode()) {
                if (MeasureSpec.getSize(heightMeasureSpec) < this.mDatePickerHeight) {
                    setCurrentViewType(1);
                    this.mCalendarHeaderText.setOnClickListener(null);
                    this.mCalendarHeaderText.setClickable(false);
                } else if (!this.mCalendarHeaderText.hasOnClickListeners()) {
                    this.mCalendarHeaderText.setOnClickListener(this.mCalendarHeaderClickListener);
                    this.mCalendarHeaderText.setClickable(true);
                }
            }
        }
        if (this.mIsFirstMeasure || this.mOldCalendarViewPagerWidth != this.mCalendarViewPagerWidth) {
            this.mIsFirstMeasure = false;
            this.mOldCalendarViewPagerWidth = this.mCalendarViewPagerWidth;
            this.mCalendarHeaderLayout.setLayoutParams(new LayoutParams(-1, this.mCalendarHeaderLayoutHeight));
            this.mDayOfTheWeekLayout.setLayoutParams(new LayoutParams(this.mDayOfTheWeekLayoutWidth, this.mDayOfTheWeekLayoutHeight));
            this.mDayOfTheWeekView.setLayoutParams(new LayoutParams(this.mDayOfTheWeekLayoutWidth, this.mDayOfTheWeekLayoutHeight));
            this.mCalendarViewPager.setLayoutParams(new LayoutParams(this.mCalendarViewPagerWidth, this.mCalendarViewPagerHeight));
            if (this.mIsRTL && this.mIsConfigurationChanged) {
                this.mCalendarViewPager.seslSetConfigurationChanged(true);
            }
            this.mFirstBlankSpace.setLayoutParams(new LayoutParams(-1, this.mFirstBlankSpaceHeight));
            this.mSecondBlankSpace.setLayoutParams(new LayoutParams(-1, this.mSecondBlankSpaceHeight));
            super.onMeasure(newWidthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec);
    }

    private int makeMeasureSpec(int measureSpec, int maxSize) {
        if (maxSize == -1) {
            return measureSpec;
        }
        int size;
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == Integer.MIN_VALUE) {
            int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;
            if (smallestScreenWidthDp >= 600) {
                size = getResources().getDimensionPixelSize(C0247R.dimen.sesl_date_picker_dialog_min_width);
            } else {
                size = (int) (TypedValue.applyDimension(1, (float) smallestScreenWidthDp, getResources().getDisplayMetrics()) + 0.5f);
            }
        } else {
            size = MeasureSpec.getSize(measureSpec);
        }
        switch (mode) {
            case Integer.MIN_VALUE:
                this.mCalendarViewPagerWidth = size - (this.mCalendarViewMargin * 2);
                this.mDayOfTheWeekLayoutWidth = size - (this.mCalendarViewMargin * 2);
                measureSpec = MeasureSpec.makeMeasureSpec(size, 1073741824);
                break;
            case 0:
                measureSpec = MeasureSpec.makeMeasureSpec(maxSize, 1073741824);
                break;
            case 1073741824:
                this.mCalendarViewPagerWidth = size - (this.mCalendarViewMargin * 2);
                this.mDayOfTheWeekLayoutWidth = size - (this.mCalendarViewMargin * 2);
                break;
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
        int i = measureSpec;
        return measureSpec;
    }

    private String getMonthAndYearString(Calendar calendar) {
        if (this.mIsFarsiLanguage) {
            return new SimpleDateFormat("LLLL y", this.mCurrentLocale).format(calendar.getTime());
        }
        if (this.mIsTibetanLanguage) {
            return new SimpleDateFormat("y LLLL", this.mCurrentLocale).format(calendar.getTime());
        }
        StringBuilder stringBuilder = new StringBuilder(50);
        Formatter formatter = new Formatter(stringBuilder, this.mCurrentLocale);
        stringBuilder.setLength(0);
        long millis = calendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), formatter, millis, millis, 36, Time.getCurrentTimezone()).toString();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C0247R.id.sesl_date_picker_calendar_header_prev_button) {
            if (this.mIsRTL) {
                if (this.mCurrentPosition != this.mPositionCount - 1) {
                    this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition + 1);
                }
            } else if (this.mCurrentPosition != 0) {
                this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition - 1);
            }
        } else if (id != C0247R.id.sesl_date_picker_calendar_header_next_button) {
        } else {
            if (this.mIsRTL) {
                if (this.mCurrentPosition != 0) {
                    this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition - 1);
                }
            } else if (this.mCurrentPosition != this.mPositionCount - 1) {
                this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition + 1);
            }
        }
    }

    protected void onDetachedFromWindow() {
        removeAllCallbacks();
        super.onDetachedFromWindow();
    }

    public boolean onLongClick(View view) {
        int id = view.getId();
        if (id == C0247R.id.sesl_date_picker_calendar_header_prev_button && this.mCurrentPosition != 0) {
            postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
        } else if (id == C0247R.id.sesl_date_picker_calendar_header_next_button && this.mCurrentPosition != this.mPositionCount - 1) {
            postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
        }
        return false;
    }

    private void postChangeCurrentByOneFromLongPress(boolean increment, long delayMillis) {
        if (this.mChangeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(increment);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, delayMillis);
    }

    private void removeAllCallbacks() {
        if (this.mChangeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SeslDatePicker.this.mCalendarViewPager.setCurrentItem(SeslDatePicker.this.mCurrentPosition, false);
                }
            }, 200);
        }
    }

    public void setDateMode(int mode) {
        int startYear;
        int startMonth;
        int endYear;
        int endMonth;
        this.mMode = mode;
        switch (this.mMode) {
            case 1:
                startYear = this.mStartDate.get(1);
                startMonth = this.mStartDate.get(2);
                int startDayOfMonth = this.mStartDate.get(5);
                if (this.mIsLunar) {
                    startYear = this.mLunarStartYear;
                    startMonth = this.mLunarStartMonth;
                    startDayOfMonth = this.mLunarStartDay;
                }
                this.mSpinnerLayout.updateDate(startYear, startMonth, startDayOfMonth);
                break;
            case 2:
                endYear = this.mEndDate.get(1);
                endMonth = this.mEndDate.get(2);
                int endDayOfMonth = this.mEndDate.get(5);
                if (this.mIsLunar) {
                    endYear = this.mLunarEndYear;
                    endMonth = this.mLunarEndMonth;
                    endDayOfMonth = this.mLunarEndDay;
                }
                this.mSpinnerLayout.updateDate(endYear, endMonth, endDayOfMonth);
                break;
        }
        if (this.mCurrentViewType == 1) {
            this.mSpinnerLayout.setVisibility(0);
            this.mSpinnerLayout.setEnabled(true);
        }
        SeslSimpleMonthView view = (SeslSimpleMonthView) this.mCalendarPagerAdapter.views.get(this.mCurrentPosition);
        if (view != null) {
            int enabledDayRangeStart;
            int enabledDayRangeEnd;
            int year = this.mCurrentDate.get(1);
            int month = this.mCurrentDate.get(2);
            int selectedDay = this.mCurrentDate.get(5);
            if (this.mIsLunar) {
                year = this.mLunarCurrentYear;
                month = this.mLunarCurrentMonth;
                selectedDay = this.mLunarCurrentDay;
            }
            if (getMinMonth() == month && getMinYear() == year) {
                enabledDayRangeStart = getMinDay();
            } else {
                enabledDayRangeStart = 1;
            }
            if (getMaxMonth() == month && getMaxYear() == year) {
                enabledDayRangeEnd = getMaxDay();
            } else {
                enabledDayRangeEnd = 31;
            }
            startYear = this.mStartDate.get(1);
            startMonth = this.mStartDate.get(2);
            int startDay = this.mStartDate.get(5);
            endYear = this.mEndDate.get(1);
            endMonth = this.mEndDate.get(2);
            int endDay = this.mEndDate.get(5);
            if (this.mIsLunar) {
                startYear = this.mLunarStartYear;
                startMonth = this.mLunarStartMonth;
                startDay = this.mLunarStartDay;
                endYear = this.mLunarEndYear;
                endMonth = this.mLunarEndMonth;
                endDay = this.mLunarEndDay;
            }
            view.setMonthParams(selectedDay, month, year, getFirstDayOfWeek(), enabledDayRangeStart, enabledDayRangeEnd, this.mMinDate, this.mMaxDate, startYear, startMonth, startDay, this.mIsLeapStartMonth, endYear, endMonth, endDay, this.mIsLeapEndMonth, this.mMode);
            view.invalidate();
        }
        if (this.mIsLunar) {
            updateSimpleMonthView(false);
        }
        this.mCalendarPagerAdapter.notifyDataSetChanged();
    }

    public int getDateMode() {
        return this.mMode;
    }

    private void checkMaxFontSize() {
        float currentFontScale = this.mContext.getResources().getConfiguration().fontScale;
        int calendarHeaderTextSize = getResources().getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_calendar_header_month_text_size);
        if (currentFontScale > MAX_FONT_SCALE) {
            this.mCalendarHeaderText.setTextSize(0, (float) Math.floor(Math.ceil((double) (((float) calendarHeaderTextSize) / currentFontScale)) * 1.2000000476837158d));
        }
    }

    public void setCurrentViewType(int viewType) {
        Message msg;
        switch (viewType) {
            case 0:
                if (this.mCurrentViewType != viewType) {
                    this.mCalendarPagerAdapter.notifyDataSetChanged();
                    this.mSpinnerLayout.updateInputState();
                    this.mSpinnerLayout.setEditTextMode(false);
                    this.mAnimator.setDisplayedChild(0);
                    this.mSpinnerLayout.setVisibility(4);
                    this.mSpinnerLayout.setEnabled(false);
                    this.mCurrentViewType = viewType;
                    msg = this.mHandler.obtainMessage();
                    msg.what = 1000;
                    this.mHandler.sendMessage(msg);
                    break;
                }
                break;
            case 1:
                if (this.mCurrentViewType != viewType) {
                    int year;
                    int month;
                    int day;
                    switch (this.mMode) {
                        case 1:
                            year = this.mStartDate.get(1);
                            month = this.mStartDate.get(2);
                            day = this.mStartDate.get(5);
                            if (this.mIsLunar) {
                                year = this.mLunarStartYear;
                                month = this.mLunarStartMonth;
                                day = this.mLunarStartDay;
                                break;
                            }
                            break;
                        case 2:
                            year = this.mEndDate.get(1);
                            month = this.mEndDate.get(2);
                            day = this.mEndDate.get(5);
                            if (this.mIsLunar) {
                                year = this.mLunarEndYear;
                                month = this.mLunarEndMonth;
                                day = this.mLunarEndDay;
                                break;
                            }
                            break;
                        default:
                            year = this.mCurrentDate.get(1);
                            month = this.mCurrentDate.get(2);
                            day = this.mCurrentDate.get(5);
                            if (this.mIsLunar) {
                                year = this.mLunarCurrentYear;
                                month = this.mLunarCurrentMonth;
                                day = this.mLunarCurrentDay;
                                break;
                            }
                            break;
                    }
                    this.mSpinnerLayout.updateDate(year, month, day);
                    this.mAnimator.setDisplayedChild(1);
                    this.mSpinnerLayout.setEnabled(true);
                    this.mCurrentViewType = viewType;
                    msg = this.mHandler.obtainMessage();
                    msg.what = 1000;
                    this.mHandler.sendMessage(msg);
                    break;
                }
                break;
            default:
                return;
        }
        Message msg1 = this.mHandler.obtainMessage();
        msg1.what = 1001;
        this.mHandler.sendMessage(msg1);
    }

    public int getCurrentViewType() {
        return this.mCurrentViewType;
    }

    public void setEditTextMode(boolean editTextMode) {
        if (this.mCurrentViewType != 0) {
            this.mSpinnerLayout.setEditTextMode(editTextMode);
        }
    }

    public boolean isEditTextMode() {
        return this.mCurrentViewType != 0 && this.mSpinnerLayout.isEditTextMode();
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mSpinnerLayout.setOnEditTextModeChangedListener(this, onEditTextModeChangedListener);
    }

    public EditText getEditText(int picker) {
        return this.mSpinnerLayout.getEditText(picker);
    }

    public SeslNumberPicker getNumberPicker(int picker) {
        return this.mSpinnerLayout.getNumberPicker(picker);
    }

    public void setLunarSupported(boolean supported, View lunarButton) {
        this.mIsLunarSupported = supported;
        if (this.mIsLunarSupported) {
            if (this.mCustomButtonView != null) {
                this.mCalendarHeaderLayout.removeView(this.mCustomButtonView);
            }
            this.mCustomButtonView = lunarButton;
            if (this.mCustomButtonView != null) {
                RelativeLayout.LayoutParams buttonParams;
                ViewParent parentView = this.mCustomButtonView.getParent();
                if (parentView instanceof ViewGroup) {
                    ((ViewGroup) parentView).removeView(this.mCustomButtonView);
                }
                this.mCustomButtonView.setId(16908331);
                RelativeLayout.LayoutParams calendarHeaderParams = (RelativeLayout.LayoutParams) this.mCalendarHeader.getLayoutParams();
                calendarHeaderParams.addRule(13);
                calendarHeaderParams.addRule(16, this.mCustomButtonView.getId());
                RelativeLayout.LayoutParams prevButtonParams = (RelativeLayout.LayoutParams) this.mPrevButton.getLayoutParams();
                prevButtonParams.leftMargin = 0;
                this.mPrevButton.setLayoutParams(prevButtonParams);
                RelativeLayout.LayoutParams nextButtonParams = (RelativeLayout.LayoutParams) this.mNextButton.getLayoutParams();
                nextButtonParams.rightMargin = 0;
                this.mNextButton.setLayoutParams(nextButtonParams);
                calendarHeaderParams.leftMargin = getContext().getResources().getDimensionPixelOffset(C0247R.dimen.sesl_date_picker_lunar_calendar_header_margin);
                this.mCalendarHeader.setLayoutParams(calendarHeaderParams);
                ViewGroup.LayoutParams layoutParams = this.mCustomButtonView.getLayoutParams();
                if (layoutParams instanceof RelativeLayout.LayoutParams) {
                    buttonParams = (RelativeLayout.LayoutParams) layoutParams;
                } else if (layoutParams instanceof MarginLayoutParams) {
                    buttonParams = new RelativeLayout.LayoutParams((MarginLayoutParams) layoutParams);
                } else if (layoutParams != null) {
                    buttonParams = new RelativeLayout.LayoutParams(layoutParams);
                } else {
                    buttonParams = new RelativeLayout.LayoutParams(-2, -2);
                }
                buttonParams.addRule(15);
                buttonParams.addRule(21);
                this.mCustomButtonView.setLayoutParams(buttonParams);
                this.mCalendarHeaderLayout.addView(this.mCustomButtonView);
            }
        } else {
            this.mIsLunar = false;
            this.mIsLeapMonth = false;
            this.mCustomButtonView = null;
        }
        if (this.mIsLunarSupported && this.mPathClassLoader == null) {
            mPackageManager = this.mContext.getApplicationContext().getPackageManager();
            this.mPathClassLoader = LunarUtils.getPathClassLoader(getContext());
            if (this.mPathClassLoader != null) {
                try {
                    Class<?> calendarFeatureClass = Class.forName("com.android.calendar.Feature", true, this.mPathClassLoader);
                    if (calendarFeatureClass == null) {
                        Log.e(TAG, "setLunarSupported, Calendar Feature class is null");
                        return;
                    }
                    this.mSolarLunarTables = invoke(null, getMethod(calendarFeatureClass, "getSolarLunarTables", new Class[0]), new Object[0]);
                    try {
                        Class<?> solarLunarTablesClass = Class.forName("com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarTables", true, this.mPathClassLoader);
                        if (solarLunarTablesClass == null) {
                            Log.e(TAG, "setLunarSupported, Calendar Tables class is null");
                            return;
                        }
                        this.mGetLunarMethod = getMethod(solarLunarTablesClass, "getLunar", Integer.TYPE);
                        this.mStartOfLunarYearField = getField(solarLunarTablesClass, "START_OF_LUNAR_YEAR");
                        this.mWidthPerYearField = getField(solarLunarTablesClass, "WIDTH_PER_YEAR");
                        this.mIndexOfLeapMonthField = getField(solarLunarTablesClass, "INDEX_OF_LEAP_MONTH");
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "setLunarSupported, Calendar Tables class not found");
                    }
                } catch (ClassNotFoundException e2) {
                    Log.e(TAG, "setLunarSupported, Calendar Feature class not found");
                }
            }
        }
    }

    public void setLunar(boolean isLunar, boolean isLeapMonth) {
        if (this.mIsLunarSupported) {
            this.mIsLunar = isLunar;
            this.mIsLeapMonth = isLeapMonth;
            this.mSpinnerLayout.setLunar(isLunar, isLeapMonth, this.mPathClassLoader);
            if (isLunar) {
                setTotalMonthCountWithLeap();
                if (this.mMode == 0) {
                    int i;
                    this.mIsLeapStartMonth = isLeapMonth ? 1 : 0;
                    if (isLeapMonth) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    this.mIsLeapEndMonth = i;
                }
            }
            this.mIsFromSetLunar = true;
            this.mCalendarPagerAdapter.notifyDataSetChanged();
            this.mLunarChanged = true;
            updateSimpleMonthView(true);
            this.mLunarChanged = false;
        }
    }

    public boolean isLunar() {
        return this.mIsLunar;
    }

    public boolean isLeapMonth() {
        return this.mIsLeapMonth;
    }

    private void setTotalMonthCountWithLeap() {
        if (this.mSolarLunarTables != null && this.mPathClassLoader != null) {
            int count = 0;
            this.mTotalMonthCountWithLeap = new int[((getMaxYear() - getMinYear()) + 1)];
            for (int year = getMinYear(); year <= getMaxYear(); year++) {
                int lengthOfYear;
                int month;
                int indexOfLeapMonthOfYear;
                if (year == getMinYear()) {
                    month = getMinMonth() + 1;
                    indexOfLeapMonthOfYear = getIndexOfleapMonthOfYear(year);
                    if (indexOfLeapMonthOfYear > 12) {
                        lengthOfYear = (12 - month) + 1;
                    } else if (indexOfLeapMonthOfYear < month) {
                        lengthOfYear = (12 - month) + 1;
                    } else {
                        lengthOfYear = (13 - month) + 1;
                    }
                } else if (year == getMaxYear()) {
                    month = getMaxMonth() + 1;
                    indexOfLeapMonthOfYear = getIndexOfleapMonthOfYear(year);
                    if (indexOfLeapMonthOfYear > 12) {
                        lengthOfYear = month;
                    } else if (month < indexOfLeapMonthOfYear) {
                        lengthOfYear = month;
                    } else {
                        lengthOfYear = month + 1;
                    }
                } else {
                    lengthOfYear = getIndexOfleapMonthOfYear(year) > 12 ? 12 : 13;
                }
                count += lengthOfYear;
                this.mTotalMonthCountWithLeap[year - getMinYear()] = count;
            }
        }
    }

    private int getTotalMonthCountWithLeap(int year) {
        if (this.mTotalMonthCountWithLeap == null || year < getMinYear()) {
            return 0;
        }
        return this.mTotalMonthCountWithLeap[year - getMinYear()];
    }

    private LunarDate getLunarDateByPosition(int position) {
        int lengthOfYear = 12;
        LunarDate lunarDate = new LunarDate();
        int year = getMinYear();
        int month = 0;
        boolean isLeap = false;
        for (int i = getMinYear(); i <= getMaxYear(); i++) {
            if (position < getTotalMonthCountWithLeap(i)) {
                int totalMonthCountWithLeapBefore;
                year = i;
                if (year == getMinYear()) {
                    totalMonthCountWithLeapBefore = -getMinMonth();
                } else {
                    totalMonthCountWithLeapBefore = getTotalMonthCountWithLeap(i - 1);
                }
                int indexOfMonth = position - totalMonthCountWithLeapBefore;
                int indexOfLeapMonthOfYear = getIndexOfleapMonthOfYear(year);
                if (indexOfLeapMonthOfYear <= 12) {
                    lengthOfYear = 13;
                }
                month = indexOfMonth < indexOfLeapMonthOfYear ? indexOfMonth : indexOfMonth - 1;
                isLeap = lengthOfYear == 13 && indexOfLeapMonthOfYear == indexOfMonth;
                lunarDate.set(year, month, 1, isLeap);
                return lunarDate;
            }
        }
        lunarDate.set(year, month, 1, isLeap);
        return lunarDate;
    }

    private int getIndexOfleapMonthOfYear(int year) {
        if (this.mSolarLunarTables == null) {
            return 127;
        }
        Object startOfLunarYear = getObject(this.mSolarLunarTables, this.mStartOfLunarYearField);
        Object widthPerYear = getObject(this.mSolarLunarTables, this.mWidthPerYearField);
        Object indexOfLeapMonth = getObject(this.mSolarLunarTables, this.mIndexOfLeapMonthField);
        if ((startOfLunarYear instanceof Integer) && (widthPerYear instanceof Integer) && (indexOfLeapMonth instanceof Integer)) {
            int startIndexOfYear = (year - ((Integer) startOfLunarYear).intValue()) * ((Integer) widthPerYear).intValue();
            Object index = invoke(this.mSolarLunarTables, this.mGetLunarMethod, Integer.valueOf(((Integer) indexOfLeapMonth).intValue() + startIndexOfYear));
            if (index instanceof Byte) {
                return ((Byte) index).byteValue();
            }
            return 127;
        }
        Log.e(TAG, "getIndexOfleapMonthOfYear, not Integer");
        return 127;
    }

    public static String getCalendarPackageName() {
        String originalPackageName = "com.android.calendar";
        String packageName = SeslFloatingFeatureReflector.getString("SEC_FLOATING_FEATURE_CALENDAR_CONFIG_PACKAGE_NAME", "com.android.calendar");
        if (originalPackageName.equals(packageName)) {
            return packageName;
        }
        try {
            mPackageManager.getPackageInfo(packageName, 0);
            return packageName;
        } catch (NameNotFoundException e) {
            return originalPackageName;
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

    private <T> Field getField(Class<T> className, String fieldName) {
        Field memberField = null;
        try {
            memberField = className.getField(fieldName);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, fieldName + " NoSuchMethodException", e);
        }
        return memberField;
    }

    private Object getObject(Object callerInstance, Field field) {
        Object obj = null;
        if (field == null) {
            Log.e(TAG, "field is null");
        } else {
            try {
                obj = field.get(callerInstance);
            } catch (IllegalAccessException e) {
                Log.e(TAG, field.getName() + " IllegalAccessException", e);
            } catch (IllegalArgumentException e2) {
                Log.e(TAG, field.getName() + " IllegalArgumentException", e2);
            }
        }
        return obj;
    }

    public void setLunarStartDate(int year, int month, int day, boolean isLeap) {
        this.mLunarStartYear = year;
        this.mLunarStartMonth = month;
        this.mLunarStartDay = day;
        this.mIsLeapStartMonth = isLeap ? 1 : 0;
    }

    public int[] getLunarStartDate() {
        return new int[]{this.mLunarStartYear, this.mLunarStartMonth, this.mLunarStartDay, this.mIsLeapStartMonth};
    }

    public void setLunarEndDate(int year, int month, int day, boolean isLeap) {
        this.mLunarEndYear = year;
        this.mLunarEndMonth = month;
        this.mLunarEndDay = day;
        this.mIsLeapEndMonth = isLeap ? 1 : 0;
    }

    public int[] getLunarEndDate() {
        return new int[]{this.mLunarEndYear, this.mLunarEndMonth, this.mLunarEndDay, this.mIsLeapEndMonth};
    }

    private boolean isFarsiLanguage() {
        return "fa".equals(this.mCurrentLocale.getLanguage());
    }

    private boolean isTibetanLanguage() {
        return "bo".equals(this.mCurrentLocale.getLanguage());
    }

    private boolean isSimplifiedChinese() {
        return this.mCurrentLocale.getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage()) && this.mCurrentLocale.getCountry().equals(Locale.SIMPLIFIED_CHINESE.getCountry());
    }
}
