package com.sec.android.app.clockpackage.alarm.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import com.samsung.android.view.animation.SineInOut60;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.WeekdayColorParser;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import java.util.Locale;

public class AlarmRepeatButton extends RelativeLayout {
    private static SparseBooleanArray sRepeatSelectItems;
    private AlarmRepeatListener mAlarmRepeatClickListener;
    public String mAlarmRepeatString;
    private final boolean[] mCheckedRepeatItems;
    private final int[] mColorSet;
    private int[] mColorValues;
    private Context mContext;
    private float mEndValue;
    public boolean mIsDragging;
    private boolean mIsRightWay;
    private int mMoveSelectedPosition;
    private DrawRepeatCircleView[] mRepeatAnimatingView;
    private final ToggleButton[] mRepeatBtn;
    private final boolean[] mRepeatBtnTouchState;
    private int mStartIndex;
    private float mStartValue;

    public interface AlarmRepeatListener {
        void setAlarmRepeatClick(int i);

        void setRepeatFocus();
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmRepeatButton$1 */
    class C05701 implements OnFocusChangeListener {
        C05701() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && AlarmRepeatButton.this.mAlarmRepeatClickListener != null) {
                AlarmRepeatButton.this.mAlarmRepeatClickListener.setRepeatFocus();
            }
        }
    }

    public class DrawRepeatCircleView extends View {
        Paint mPaint;
        final int mRadius = AlarmRepeatButton.this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_btn_select_circle_radius);
        View mRepeatToggleButton = null;
        float mSelectionRatio = 1.0f;
        final int mStroke = AlarmRepeatButton.this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_btn_select_circle_stroke);

        public DrawRepeatCircleView(Context context) {
            super(context);
            init();
        }

        private void init() {
            this.mPaint = new Paint();
            this.mPaint.setColor(AlarmRepeatButton.this.mContext.getColor(C0490R.color.alarm_repeat_toggle_btn_text_color_select));
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStrokeWidth((float) this.mStroke);
            this.mPaint.setStyle(Style.STROKE);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.mRepeatToggleButton != null) {
                canvas.drawCircle(this.mRepeatToggleButton.getX() + (((float) this.mRepeatToggleButton.getWidth()) / 2.0f), ((float) this.mRepeatToggleButton.getHeight()) / 2.0f, ((float) this.mRadius) * this.mSelectionRatio, this.mPaint);
            }
        }

        private void onRemove() {
            this.mPaint = null;
        }

        void setSelectionRatio(float ratio) {
            this.mSelectionRatio = ratio;
        }

        void setRepeatToggleButton(View repeatToggleButton) {
            this.mRepeatToggleButton = repeatToggleButton;
        }
    }

    private static final class SavedState extends BaseSavedState {
        private int mCheckedDay;

        SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public AlarmRepeatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCheckedRepeatItems = new boolean[7];
        this.mStartIndex = -1;
        this.mIsDragging = false;
        this.mRepeatBtnTouchState = new boolean[7];
        this.mRepeatBtn = new ToggleButton[7];
        this.mColorSet = new int[]{C0490R.color.alarm_repeat_toggle_btn_text_color_normal, C0490R.color.alarm_repeat_saturday_color, C0490R.color.alarm_repeat_sunday_color};
        this.mColorValues = new int[7];
        this.mStartValue = 0.0f;
        this.mEndValue = 1.0f;
    }

    public AlarmRepeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCheckedRepeatItems = new boolean[7];
        this.mStartIndex = -1;
        this.mIsDragging = false;
        this.mRepeatBtnTouchState = new boolean[7];
        this.mRepeatBtn = new ToggleButton[7];
        this.mColorSet = new int[]{C0490R.color.alarm_repeat_toggle_btn_text_color_normal, C0490R.color.alarm_repeat_saturday_color, C0490R.color.alarm_repeat_sunday_color};
        this.mColorValues = new int[7];
        this.mStartValue = 0.0f;
        this.mEndValue = 1.0f;
    }

    public AlarmRepeatButton(Context context) {
        super(context);
        this.mCheckedRepeatItems = new boolean[7];
        this.mStartIndex = -1;
        this.mIsDragging = false;
        this.mRepeatBtnTouchState = new boolean[7];
        this.mRepeatBtn = new ToggleButton[7];
        this.mColorSet = new int[]{C0490R.color.alarm_repeat_toggle_btn_text_color_normal, C0490R.color.alarm_repeat_saturday_color, C0490R.color.alarm_repeat_sunday_color};
        this.mColorValues = new int[7];
        this.mStartValue = 0.0f;
        this.mEndValue = 1.0f;
    }

    public void setContext(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        if (this.mContext == null) {
            this.mContext = getContext();
        }
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0490R.layout.alarm_repeat_button, this, true);
        inflateRepeatBtn();
        sRepeatSelectItems = new SparseBooleanArray();
    }

    private void inflateRepeatBtn() {
        int nCount;
        this.mRepeatAnimatingView = new DrawRepeatCircleView[7];
        int startId = C0490R.id.repeat_0;
        for (int i = 0; i < 7; i++) {
            this.mRepeatBtn[i] = (ToggleButton) findViewById(startId);
            this.mRepeatAnimatingView[i] = new DrawRepeatCircleView(this.mContext);
            this.mRepeatAnimatingView[i].setRepeatToggleButton(this.mRepeatBtn[i]);
            addView(this.mRepeatAnimatingView[i]);
            this.mRepeatBtn[i].setTag(Integer.valueOf(i));
            this.mRepeatBtn[i].setChecked(false);
            this.mRepeatBtn[i].setTypeface(Typeface.create("sans-serif", 0));
            startId++;
            this.mRepeatBtn[i].setOnFocusChangeListener(new C05701());
            this.mCheckedRepeatItems[i] = false;
            this.mRepeatAnimatingView[i].setVisibility(8);
        }
        int startDay = ClockUtils.getStartDayOfWeek() - 1;
        boolean isMirroringEnabled = StateUtils.isMirroringEnabled();
        int[] stringId = new int[]{C0490R.string.sun1, C0490R.string.mon1, C0490R.string.tue1, C0490R.string.wed1, C0490R.string.thu1, C0490R.string.fri1, C0490R.string.sat1};
        final int[] stringDescriptionId = new int[]{C0490R.string.sunday, C0490R.string.monday, C0490R.string.tuesday, C0490R.string.wednesday, C0490R.string.thursday, C0490R.string.friday, C0490R.string.saturday};
        this.mColorValues = WeekdayColorParser.getColors(this.mColorSet, startDay);
        for (nCount = 0; nCount < 7; nCount++) {
            int dayOfWeek;
            if (isMirroringEnabled) {
                Log.secD("AlarmRepeatButton", "isMirroringEnabled : true");
                dayOfWeek = ((startDay + 6) - nCount) % 7;
            } else {
                dayOfWeek = (startDay + nCount) % 7;
            }
            try {
                SpannableString spanDays = new SpannableString(getResources().getString(stringId[dayOfWeek]));
                this.mRepeatBtn[nCount].setTextColor(this.mContext.getColor(this.mColorValues[nCount]));
                this.mRepeatBtn[nCount].setText(spanDays);
                this.mRepeatBtn[nCount].setTextOn(spanDays);
                this.mRepeatBtn[nCount].setTextOff(spanDays);
                this.mRepeatBtn[nCount].setAccessibilityDelegate(new AccessibilityDelegate() {
                    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
                        super.onInitializeAccessibilityNodeInfo(host, info);
                        info.setText(AlarmRepeatButton.this.getResources().getString(stringDescriptionId[dayOfWeek]));
                    }
                });
                ClockUtils.setLargeTextSize(this.mContext, this.mRepeatBtn[nCount], (float) getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_toggle_btn_text_size));
            } catch (NoSuchMethodError e) {
            }
        }
        for (nCount = 0; nCount < 7; nCount++) {
            final int buttonNumber = nCount;
            this.mRepeatBtn[nCount].setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (AlarmRepeatButton.this.mRepeatBtn[buttonNumber].isChecked()) {
                        Log.secD("AlarmRepeatButton", "mRepeatBtn[buttonNumber].isChecked() - true, buttonNumber = " + buttonNumber);
                        AlarmRepeatButton.this.mRepeatAnimatingView[buttonNumber].setVisibility(0);
                        AlarmRepeatButton.this.setSelectionMarkAnimator(buttonNumber, true);
                    } else {
                        Log.secD("AlarmRepeatButton", "mRepeatBtn[buttonNumber].isChecked() - false, buttonNumber = " + buttonNumber);
                        AlarmRepeatButton.this.setSelectionMarkAnimator(buttonNumber, false);
                    }
                    AlarmRepeatButton.this.clickRepeatButton();
                }
            });
        }
    }

    public void setSelectionMarkAnimator(final int index, final boolean isShowingAnim) {
        Log.secD("AlarmRepeatButton", "setSelectionMarkAnimator = " + index + " , isShowAnim = " + isShowingAnim);
        int durationAnim = 300;
        if (isShowingAnim) {
            this.mStartValue = 0.0f;
            this.mEndValue = 1.0f;
        } else {
            this.mStartValue = 1.0f;
            this.mEndValue = 0.0f;
            durationAnim = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        }
        ValueAnimator repeatSelectionAnimator = ValueAnimator.ofFloat(new float[]{this.mStartValue, this.mEndValue});
        repeatSelectionAnimator.setDuration((long) durationAnim);
        repeatSelectionAnimator.setInterpolator(new SineInOut60());
        repeatSelectionAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (AlarmRepeatButton.this.mRepeatBtn[index] == null || AlarmRepeatButton.this.mRepeatAnimatingView[index] == null) {
                    animation.cancel();
                } else if (AlarmRepeatButton.this.mRepeatBtn[index].isChecked() != isShowingAnim) {
                    animation.cancel();
                    if (!AlarmRepeatButton.this.mRepeatBtn[index].isChecked()) {
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].setVisibility(8);
                    }
                } else {
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(((Float) animation.getAnimatedValue()).floatValue());
                }
            }
        });
        repeatSelectionAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (AlarmRepeatButton.this.mRepeatAnimatingView[index] == null) {
                    animation.cancel();
                } else {
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(AlarmRepeatButton.this.mStartValue);
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (AlarmRepeatButton.this.mRepeatBtn[index] != null && AlarmRepeatButton.this.mRepeatAnimatingView[index] != null) {
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(AlarmRepeatButton.this.mEndValue);
                    if (AlarmRepeatButton.this.mRepeatBtn[index].isChecked()) {
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].setVisibility(0);
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(1.0f);
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].invalidate();
                        return;
                    }
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(0.0f);
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setVisibility(8);
                }
            }

            public void onAnimationCancel(Animator animation) {
                Log.secD("AlarmRepeatButton", "Animation Cancel index = " + index);
                if (AlarmRepeatButton.this.mRepeatBtn[index] != null && AlarmRepeatButton.this.mRepeatAnimatingView[index] != null) {
                    if (AlarmRepeatButton.this.mRepeatBtn[index].isChecked()) {
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].setVisibility(0);
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(1.0f);
                        AlarmRepeatButton.this.mRepeatAnimatingView[index].invalidate();
                        return;
                    }
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setSelectionRatio(0.0f);
                    AlarmRepeatButton.this.mRepeatAnimatingView[index].setVisibility(8);
                }
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        ValueAnimator repeatInvalidateAnimator = ValueAnimator.ofFloat(new float[]{this.mStartValue, this.mEndValue});
        repeatInvalidateAnimator.setDuration((long) durationAnim);
        repeatInvalidateAnimator.setInterpolator(new SineInOut60());
        repeatInvalidateAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (AlarmRepeatButton.this.mRepeatBtn[index] == null || AlarmRepeatButton.this.mRepeatAnimatingView[index] == null) {
                    animation.cancel();
                    return;
                }
                if (AlarmRepeatButton.this.mRepeatBtn[index].isChecked() != isShowingAnim) {
                    animation.cancel();
                }
                AlarmRepeatButton.this.mRepeatAnimatingView[index].invalidate();
            }
        });
        AnimatorSet mRepeatAnimSet = new AnimatorSet();
        mRepeatAnimSet.playTogether(new Animator[]{repeatSelectionAnimator, repeatInvalidateAnimator});
        mRepeatAnimSet.start();
    }

    private void clickRepeatButton() {
        RingtonePlayer.stopMediaPlayer();
        int repeatCheckDay = getCheckDay();
        setCheckDayWithAnimation(repeatCheckDay, true);
        if (this.mAlarmRepeatClickListener != null) {
            this.mAlarmRepeatClickListener.setAlarmRepeatClick(repeatCheckDay);
        } else {
            Log.secE("AlarmRepeatButton", "mAlarmRepeatClickListener is null. not able to click alarmRepeat button.");
        }
    }

    public void removeInstance(boolean isDestroy) {
        if (this.mContext != null) {
            this.mContext = null;
        }
        if (isDestroy) {
            for (int a = 0; a < 7; a++) {
                this.mRepeatBtn[a] = null;
                this.mRepeatAnimatingView[a].onRemove();
                this.mRepeatAnimatingView[a] = null;
            }
        }
        removeAllViews();
        destroyDrawingCache();
    }

    public int getCheckDay() {
        int ret = 0;
        int[] repeatDays = Alarm.REPEAT_DAYS;
        int startDay = ClockUtils.getStartDayOfWeek() - 1;
        boolean isMirroringEnabled = StateUtils.isMirroringEnabled();
        for (int nCount = 0; nCount < 7; nCount++) {
            if (this.mRepeatBtn[nCount].isChecked()) {
                int dayOfWeek;
                if (isMirroringEnabled) {
                    dayOfWeek = ((startDay + 6) - nCount) % 7;
                } else {
                    dayOfWeek = (startDay + nCount) % 7;
                }
                ret |= repeatDays[dayOfWeek];
            }
        }
        Log.secD("AlarmRepeatButton", "getCheckDay : " + ret);
        return ret;
    }

    public void setCheckDay(int repeat) {
        Log.secD("AlarmRepeatButton", "setCheckDay : " + repeat);
        setCheckDayWithAnimation(repeat, false);
    }

    public String getAlarmRepeatText() {
        Log.secD("AlarmRepeatButton", "calculateAlarmRepeatText() - " + this.mAlarmRepeatString);
        return this.mAlarmRepeatString;
    }

    private void setCheckDayWithAnimation(int repeat, boolean isNeedRepeatAnimation) {
        int[] repeatDays = Alarm.REPEAT_DAYS;
        int startDay = ClockUtils.getStartDayOfWeek() - 1;
        for (int nCount = 0; nCount < 7; nCount++) {
            boolean z;
            int dayOfWeek = (startDay + nCount) % 7;
            boolean[] zArr = this.mCheckedRepeatItems;
            if ((repeatDays[dayOfWeek] & repeat) == repeatDays[dayOfWeek]) {
                z = true;
            } else {
                z = false;
            }
            zArr[nCount] = z;
        }
        if (sRepeatSelectItems != null) {
            for (int i = 0; i < 7; i++) {
                if (this.mCheckedRepeatItems[i]) {
                    sRepeatSelectItems.put(i, true);
                    this.mRepeatBtn[i].setChecked(true);
                    this.mRepeatAnimatingView[i].setVisibility(0);
                    this.mRepeatBtn[i].setTextColor(this.mContext.getColor(C0490R.color.alarm_repeat_toggle_btn_text_color_select));
                    this.mRepeatBtn[i].setTypeface(Typeface.create("sans-serif", 1));
                } else {
                    sRepeatSelectItems.put(i, false);
                    this.mRepeatBtn[i].setChecked(false);
                    if (!isNeedRepeatAnimation) {
                        this.mRepeatAnimatingView[i].setVisibility(8);
                    }
                    this.mRepeatBtn[i].setTextColor(this.mContext.getColor(this.mColorValues[i]));
                    this.mRepeatBtn[i].setTypeface(Typeface.create("sans-serif", 0));
                }
            }
            setRepeatText();
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mCheckedDay = getCheckDay();
        return savedState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            setCheckDay(savedState.mCheckedDay);
            super.onRestoreInstanceState(savedState.getSuperState());
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void setRepeatText() {
        int size = sRepeatSelectItems.size();
        int tempForFullRepeatString = 0;
        if (size == 0) {
            this.mAlarmRepeatString = "";
            return;
        }
        int checkCnt = 0;
        StringBuilder mStringBuilder = new StringBuilder();
        int startDay = ClockUtils.getStartDayOfWeek() - 1;
        for (int i = 0; i < 7; i++) {
            if (sRepeatSelectItems.get(i)) {
                if (checkCnt > 0) {
                    if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                        mStringBuilder.append("، ");
                    } else if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
                        mStringBuilder.append("、");
                    } else {
                        mStringBuilder.append(", ");
                    }
                }
                int StringLength = 2;
                if (Locale.getDefault().getLanguage().equalsIgnoreCase("fa")) {
                    StringLength = 0;
                }
                int dayOfWeek = (startDay + i) % 7;
                String dayString = ClockUtils.getDayOfWeekString(this.mContext, dayOfWeek + 1, StringLength);
                if (dayString != null) {
                    mStringBuilder.append(dayString);
                }
                checkCnt++;
                if (checkCnt == 1) {
                    tempForFullRepeatString = dayOfWeek;
                }
            }
        }
        Log.secD("AlarmRepeatButton", "setRepeatSubText() - size = " + size + ", checkCnt = " + checkCnt);
        if (checkCnt == 0) {
            this.mAlarmRepeatString = "";
        } else if (checkCnt == 7) {
            this.mAlarmRepeatString = this.mContext.getResources().getString(C0490R.string.every_day);
        } else if (Feature.isTablet(this.mContext) && checkCnt == 1) {
            this.mAlarmRepeatString = ClockUtils.getDayOfWeekString(this.mContext, tempForFullRepeatString + 1, 4);
        } else {
            this.mAlarmRepeatString = mStringBuilder.toString();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean z = true;
        int action = event.getActionMasked();
        int touchIndex = getTouchIndex((int) event.getX());
        Log.secD("AlarmRepeatButton", "onInterceptTouchEvent~! action = " + action + ",  touchIndex = " + touchIndex + " ,  mStartIndex = " + this.mStartIndex);
        int a;
        switch (action) {
            case 0:
                this.mIsDragging = false;
                this.mStartIndex = touchIndex;
                this.mRepeatBtnTouchState[touchIndex] = true;
                return false;
            case 1:
                this.mIsDragging = false;
                if (this.mStartIndex != touchIndex) {
                    return true;
                }
                this.mStartIndex = -1;
                for (a = 0; a < 7; a++) {
                    this.mRepeatBtnTouchState[a] = false;
                }
                return false;
            case 2:
                if (this.mStartIndex != touchIndex) {
                    onTouchEvent(event);
                }
                if (this.mStartIndex == touchIndex) {
                    z = false;
                }
                return z;
            case 3:
                this.mIsDragging = false;
                this.mStartIndex = -1;
                for (a = 0; a < 7; a++) {
                    this.mRepeatBtnTouchState[a] = false;
                }
                return false;
            default:
                return false;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int xValue = (int) event.getX();
        int action = event.getActionMasked();
        int touchIndex = getTouchIndex(xValue);
        Log.secD("AlarmRepeatButton", "onTouchEvent!!  action = " + action + " , touchIndex = " + touchIndex);
        int a;
        switch (action) {
            case 1:
                if (!(this.mIsDragging || this.mRepeatBtn[touchIndex] == null)) {
                    this.mRepeatBtn[touchIndex].performClick();
                }
                for (a = 0; a < 7; a++) {
                    this.mRepeatBtnTouchState[a] = false;
                }
                this.mIsDragging = false;
                break;
            case 2:
                if (this.mStartIndex == -1 || this.mStartIndex == touchIndex) {
                    if (!(this.mMoveSelectedPosition == -1 || this.mMoveSelectedPosition == touchIndex || !this.mRepeatBtnTouchState[this.mMoveSelectedPosition])) {
                        if (this.mMoveSelectedPosition < touchIndex) {
                            if (!this.mIsRightWay) {
                                this.mRepeatBtn[this.mMoveSelectedPosition].performClick();
                            } else if (this.mMoveSelectedPosition < 6 && this.mMoveSelectedPosition > 0) {
                                for (a = this.mMoveSelectedPosition + 1; a < touchIndex; a++) {
                                    this.mRepeatBtn[a].performClick();
                                }
                            }
                            this.mIsRightWay = true;
                        } else {
                            if (this.mIsRightWay) {
                                this.mRepeatBtn[this.mMoveSelectedPosition].performClick();
                            } else if (this.mMoveSelectedPosition < 6 && this.mMoveSelectedPosition > 0) {
                                for (a = this.mMoveSelectedPosition - 1; a > touchIndex; a--) {
                                    this.mRepeatBtn[a].performClick();
                                }
                            }
                            this.mIsRightWay = false;
                        }
                        this.mRepeatBtnTouchState[this.mMoveSelectedPosition] = false;
                    }
                } else if (this.mRepeatBtn[this.mStartIndex] != null) {
                    this.mRepeatBtn[this.mStartIndex].performClick();
                    this.mRepeatBtnTouchState[this.mStartIndex] = false;
                    this.mMoveSelectedPosition = this.mStartIndex;
                    if (this.mStartIndex < touchIndex) {
                        for (a = this.mMoveSelectedPosition + 1; a < touchIndex; a++) {
                            this.mRepeatBtn[a].performClick();
                        }
                        this.mIsRightWay = true;
                    } else {
                        this.mIsRightWay = false;
                        for (a = this.mMoveSelectedPosition - 1; a > touchIndex; a--) {
                            this.mRepeatBtn[a].performClick();
                        }
                    }
                    this.mStartIndex = -1;
                }
                Log.secD("AlarmRepeatButton", "onTouchEvent() - State[touchIndex] = " + this.mRepeatBtnTouchState[touchIndex] + " ,  mMoveSelectedPosition = " + this.mMoveSelectedPosition + " , touchIndex = " + touchIndex);
                if (!this.mRepeatBtnTouchState[touchIndex]) {
                    this.mRepeatBtnTouchState[touchIndex] = true;
                    this.mRepeatBtn[touchIndex].performClick();
                    this.mMoveSelectedPosition = touchIndex;
                    this.mIsDragging = true;
                    break;
                }
                break;
        }
        return true;
    }

    private int getTouchIndex(int xValue) {
        int repeatMargin = getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_btn_layout_start_end_margin);
        int buttonWidth = getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_toggle_width);
        int emptyWidth = (getWidth() - (buttonWidth * 7)) - (repeatMargin * 2);
        int touchIndex = (xValue - (repeatMargin - (emptyWidth / 12))) / ((emptyWidth / 6) + buttonWidth);
        if (touchIndex >= 7) {
            touchIndex = 6;
        } else if (touchIndex < 0) {
            touchIndex = 0;
        }
        if (StateUtils.isRtl()) {
            return 6 - touchIndex;
        }
        return touchIndex;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.height = getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_layout_height);
        params.bottomMargin = getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_btn_layout_bottom_margin);
    }

    public void setOnAlarmRepeatClickListener(AlarmRepeatListener listener) {
        this.mAlarmRepeatClickListener = listener;
    }

    public void setAllRepeatBtn(boolean isAllChecked) {
        for (int i = 0; i < 7; i++) {
            this.mRepeatBtn[i].setChecked(isAllChecked);
            if (isAllChecked) {
                this.mRepeatBtn[i].setTypeface(Typeface.create("sans-serif", 1));
            } else {
                this.mRepeatBtn[i].setTypeface(Typeface.create("sans-serif", 0));
                this.mRepeatAnimatingView[i].setVisibility(8);
                try {
                    this.mRepeatBtn[i].setTextColor(this.mContext.getColor(this.mColorValues[i]));
                } catch (NoSuchMethodError e) {
                }
            }
        }
    }
}
