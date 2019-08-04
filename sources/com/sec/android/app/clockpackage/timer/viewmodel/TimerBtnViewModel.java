package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerBtnViewModelListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.view.TimerTimeView;

public class TimerBtnViewModel implements OnClickListener {
    private Activity mActivity;
    private RelativeLayout mButtonLayout = null;
    private LayoutParams mButtonLayoutParam = null;
    private Button mCancelBtn = null;
    private Button mPauseBtn = null;
    private Button mResumeBtn = null;
    private Button mStartBtn = null;
    private TimerBtnViewModelListener mTimerBtnViewModelListener;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerBtnViewModel$1 */
    class C07851 implements OnFocusChangeListener {
        C07851() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                TimerBtnViewModel.this.mTimerBtnViewModelListener.onDisablePickerEditMode();
            }
        }
    }

    TimerBtnViewModel(Activity activity, View fragmentView, TimerBtnViewModelListener timerBtnViewModelListener) {
        this.mActivity = activity;
        try {
            initButtons(fragmentView);
        } catch (RuntimeException e) {
            Log.secE("TimerBtnViewModel", "Exception : " + e.toString());
        }
        this.mTimerBtnViewModelListener = timerBtnViewModelListener;
    }

    private void initButtons(View mFragmentView) {
        Log.secD("TimerBtnViewModel", "initButtons()");
        this.mResumeBtn = (Button) mFragmentView.findViewById(C0728R.id.timer_resume_button);
        this.mPauseBtn = (Button) mFragmentView.findViewById(C0728R.id.timer_pause_button);
        this.mStartBtn = (Button) mFragmentView.findViewById(C0728R.id.timer_start_button);
        this.mCancelBtn = (Button) mFragmentView.findViewById(C0728R.id.timer_cancel_button);
        this.mButtonLayout = (RelativeLayout) mFragmentView.findViewById(C0728R.id.timer_button_layout);
        this.mButtonLayoutParam = (LayoutParams) this.mButtonLayout.getLayoutParams();
        this.mCancelBtn.semSetHoverPopupType(0);
        this.mActivity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        this.mStartBtn.setOnClickListener(this);
        this.mStartBtn.setOnFocusChangeListener(new C07851());
        this.mPauseBtn.setOnClickListener(this);
        this.mResumeBtn.setOnClickListener(this);
        this.mCancelBtn.setOnClickListener(this);
        int textSize = this.mActivity.getResources().getDimensionPixelSize(C0728R.dimen.stopwatch_button_textsize);
        ClockUtils.setLargeTextSize(this.mActivity.getApplicationContext(), this.mStartBtn, (float) textSize);
        ClockUtils.setLargeTextSize(this.mActivity.getApplicationContext(), this.mCancelBtn, (float) textSize);
        ClockUtils.setLargeTextSize(this.mActivity.getApplicationContext(), this.mResumeBtn, (float) textSize);
        ClockUtils.setLargeTextSize(this.mActivity.getApplicationContext(), this.mPauseBtn, (float) textSize);
    }

    public void onClick(View v) {
        Log.secD("TimerBtnViewModel", "onClick() / view = " + v.getId());
        v.setHapticFeedbackEnabled(true);
        int id = v.getId();
        if (id == C0728R.id.timer_start_button) {
            TimerManager.sStartedByUser = true;
            this.mTimerBtnViewModelListener.onStartBtn();
            insertStartLog();
        } else if (id == C0728R.id.timer_pause_button) {
            if (TimerData.getTimerState() == 1) {
                performAccessibilityAction(this.mPauseBtn, 128);
                this.mTimerBtnViewModelListener.onPauseBtn();
                ClockUtils.insertSaLog("130", "1323");
            } else {
                return;
            }
        } else if (id == C0728R.id.timer_resume_button) {
            if (TimerData.getTimerState() == 2) {
                performAccessibilityAction(this.mResumeBtn, 128);
                this.mTimerBtnViewModelListener.onResumeBtn();
                ClockUtils.insertSaLog("130", "1325");
            } else {
                return;
            }
        } else if (id == C0728R.id.timer_cancel_button) {
            this.mTimerBtnViewModelListener.onCancelBtn();
            ClockUtils.insertSaLog("130", "1324");
        }
        this.mTimerBtnViewModelListener.onCheckInputData();
    }

    public void setStartedViewState(boolean isStarted, boolean isResume, TimerTimeView mTimeView, TimerPickerViewModel mPickerView, TimerPresetViewModel mPresetView) {
        int i;
        Log.secD("TimerBtnViewModel", "setStartedViewState() / isStarted = " + isStarted + " / isResume = " + isResume);
        if (isStarted && mTimeView != null) {
            mTimeView.setTimeTextView(TimerData.getRemainMillis());
        }
        if (!isResume) {
            if (isStarted) {
                if (this.mPauseBtn != null) {
                    this.mPauseBtn.requestFocus();
                }
            } else if (this.mStartBtn != null) {
                this.mStartBtn.requestFocus();
            }
        }
        if (this.mPauseBtn != null) {
            Button button = this.mPauseBtn;
            if (isStarted) {
                i = 0;
            } else {
                i = 8;
            }
            button.setVisibility(i);
            if (isStarted) {
                resizeButtonText(this.mPauseBtn);
            }
            this.mPauseBtn.setEnabled(isStarted);
            this.mPauseBtn.setClickable(isStarted);
            if (!isResume && isStarted) {
                this.mPauseBtn.requestFocus();
                performAccessibilityAction(this.mPauseBtn, 64);
            }
        }
        if (this.mStartBtn != null) {
            this.mStartBtn.setVisibility(isStarted ? 8 : 0);
            if (!isStarted) {
                resizeButtonText(this.mStartBtn);
            }
            if (!(mPickerView == null || mPickerView.isFlipAnimationWorking() || (mPresetView != null && mPresetView.isActionMode()))) {
                boolean z;
                button = this.mStartBtn;
                if (isStarted) {
                    z = false;
                } else {
                    z = true;
                }
                button.setEnabled(z);
            }
            if (!(isResume || isStarted)) {
                this.mStartBtn.requestFocus();
                performAccessibilityAction(this.mStartBtn, 64);
            }
        }
        if (this.mCancelBtn != null) {
            button = this.mCancelBtn;
            if (isStarted) {
                i = 0;
            } else {
                i = 8;
            }
            button.setVisibility(i);
            if (isStarted) {
                resizeButtonText(this.mCancelBtn);
            }
            this.mCancelBtn.setEnabled(isStarted);
            this.mCancelBtn.setClickable(isStarted);
        }
        if (this.mResumeBtn != null) {
            this.mResumeBtn.setVisibility(8);
            this.mResumeBtn.setEnabled(false);
            this.mResumeBtn.setClickable(false);
        }
    }

    public void setStoppedViewState(boolean isResume) {
        Log.secD("TimerBtnViewModel", "setStoppedViewState() / isResume = " + isResume);
        if (this.mResumeBtn != null) {
            this.mResumeBtn.setVisibility(0);
            resizeButtonText(this.mResumeBtn);
            this.mResumeBtn.setEnabled(true);
            this.mResumeBtn.setClickable(true);
            if (!isResume) {
                this.mResumeBtn.requestFocus();
                performAccessibilityAction(this.mResumeBtn, 64);
            }
        }
        if (this.mPauseBtn != null) {
            this.mPauseBtn.setVisibility(8);
            this.mPauseBtn.setEnabled(false);
        }
        if (this.mStartBtn != null) {
            this.mStartBtn.setVisibility(8);
            this.mStartBtn.setEnabled(false);
        }
        if (this.mCancelBtn != null) {
            this.mCancelBtn.setVisibility(0);
            resizeButtonText(this.mCancelBtn);
            this.mCancelBtn.setEnabled(true);
        }
    }

    private void resizeButtonText(final Button button) {
        if (button.getVisibility() == 0) {
            button.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    button.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (button.getLineCount() == button.getMaxLines()) {
                        ClockUtils.setLargeTextSize(TimerBtnViewModel.this.mActivity.getApplicationContext(), button, (float) TimerBtnViewModel.this.mActivity.getResources().getDimensionPixelSize(C0728R.dimen.stopwatch_button_min_textsize));
                    }
                }
            });
        }
    }

    public void resizeButtonText() {
        resizeButtonText(this.mResumeBtn);
        resizeButtonText(this.mPauseBtn);
        resizeButtonText(this.mCancelBtn);
        resizeButtonText(this.mStartBtn);
    }

    public void performAccessibilityAction(Button button, int action) {
        if (ClockTab.isTimerTab() && this.mActivity.hasWindowFocus()) {
            AccessibilityManager accessibilityManager = (AccessibilityManager) this.mActivity.getSystemService("accessibility");
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                button.performAccessibilityAction(action, null);
            }
        }
    }

    public void updateLayout() {
        Resources res = this.mActivity.getResources();
        int orientation = res.getConfiguration().orientation;
        boolean isMultiWindowMode = this.mActivity.isInMultiWindowMode();
        if (this.mButtonLayout != null && this.mButtonLayoutParam != null) {
            LayoutParams params;
            if (StateUtils.isMobileKeyboard(this.mActivity.getApplicationContext()) || isMultiWindowMode || StateUtils.isContextInDexMode(this.mActivity)) {
                this.mButtonLayoutParam.bottomMargin = 0;
            } else if (this.mTimerBtnViewModelListener.isEditMode() && TimerUtils.isShowIme(this.mActivity.getApplicationContext())) {
                this.mButtonLayoutParam.bottomMargin = getButtonLayoutBottomMargin();
            } else {
                this.mButtonLayoutParam.bottomMargin = 0;
            }
            this.mButtonLayoutParam.height = getButtonLayoutHeight();
            int screenWidth = res.getDisplayMetrics().widthPixels;
            if (orientation == 2) {
                this.mButtonLayoutParam.width = (int) (((float) screenWidth) * 0.507f);
            } else if (screenWidth > res.getDimensionPixelSize(C0728R.dimen.stopwatch_screen_width_for_button_layout)) {
                this.mButtonLayoutParam.width = (int) (((float) screenWidth) * 0.75f);
            } else {
                this.mButtonLayoutParam.width = screenWidth - (res.getDimensionPixelSize(C0728R.dimen.stopwatch_button_layout_margin) * 2);
            }
            int buttonWidth = res.getDimensionPixelSize(C0728R.dimen.stopwatch_button_width);
            if (isMultiWindowMode) {
                int minButtonLayoutWidth = (buttonWidth * 2) + (res.getDimensionPixelSize(C0728R.dimen.stopwatch_button_min_margin) * 4);
                if (this.mButtonLayoutParam.width < minButtonLayoutWidth) {
                    LayoutParams layoutParams = this.mButtonLayoutParam;
                    if (screenWidth >= minButtonLayoutWidth) {
                        screenWidth = minButtonLayoutWidth;
                    }
                    layoutParams.width = screenWidth;
                    if (this.mButtonLayoutParam.width < buttonWidth * 2) {
                        buttonWidth = this.mButtonLayoutParam.width / 2;
                    }
                }
            }
            int buttonMargin = ((this.mButtonLayoutParam.width / 2) - buttonWidth) / 2;
            if (this.mCancelBtn != null) {
                params = (LayoutParams) this.mCancelBtn.getLayoutParams();
                params.width = buttonWidth;
                params.setMarginEnd(buttonMargin);
                this.mCancelBtn.setLayoutParams(params);
            }
            if (this.mResumeBtn != null) {
                params = (LayoutParams) this.mResumeBtn.getLayoutParams();
                params.width = buttonWidth;
                params.setMarginStart(buttonMargin);
                this.mResumeBtn.setLayoutParams(params);
            }
            if (this.mPauseBtn != null) {
                params = (LayoutParams) this.mPauseBtn.getLayoutParams();
                params.width = buttonWidth;
                params.setMarginStart(buttonMargin);
                this.mPauseBtn.setLayoutParams(params);
            }
        }
    }

    public int getButtonLayoutBottomMargin() {
        int defaultHeight;
        Resources res = this.mActivity.getResources();
        int inputMethodHeight = getInputMethodHeight();
        if (isHighContrastKeyboard()) {
            defaultHeight = res.getDimensionPixelSize(C0728R.dimen.timer_common_highcontrastkeyboard_layout_height);
        } else {
            defaultHeight = res.getDimensionPixelSize(C0728R.dimen.timer_common_keypad_layout_height);
        }
        return inputMethodHeight != 0 ? inputMethodHeight - res.getDimensionPixelSize(C0728R.dimen.clock_tab_height) : defaultHeight;
    }

    private int getInputMethodHeight() {
        try {
            if (this.mActivity.getResources().getConfiguration().orientation == 1) {
                return System.getInt(this.mActivity.getContentResolver(), "com.sec.android.inputmethod.height");
            }
            if (Feature.isTablet(this.mActivity.getApplicationContext())) {
                return System.getInt(this.mActivity.getContentResolver(), "com.sec.android.inputmethod.height_landscape");
            }
            return 0;
        } catch (SettingNotFoundException e) {
            Log.secE("TimerBtnViewModel", "Exception : " + e.toString());
            return 0;
        }
    }

    private int getButtonLayoutHeight() {
        return this.mActivity.getResources().getDimensionPixelSize(this.mActivity.isInMultiWindowMode() ? C0728R.dimen.stopwatch_button_height : C0728R.dimen.stopwatch_button_layout_height);
    }

    public void setPauseBtnClickable(boolean isButtonClickable) {
        if (this.mPauseBtn != null) {
            this.mPauseBtn.setClickable(isButtonClickable);
        }
    }

    public void setBtnCancelState() {
        this.mResumeBtn.setPressed(false);
        this.mPauseBtn.setPressed(false);
        this.mCancelBtn.setPressed(false);
        this.mResumeBtn.setHovered(false);
        this.mPauseBtn.setHovered(false);
        this.mCancelBtn.setHovered(false);
        performAccessibilityAction(this.mResumeBtn, 128);
        performAccessibilityAction(this.mPauseBtn, 128);
        performAccessibilityAction(this.mCancelBtn, 128);
    }

    public void setStartBtnEnable(boolean isButtonEnable) {
        if (this.mStartBtn != null) {
            this.mStartBtn.setEnabled(isButtonEnable);
            this.mStartBtn.setFocusable(isButtonEnable);
            this.mStartBtn.setAlpha(isButtonEnable ? 1.0f : 0.4f);
        }
    }

    public Button getResumeBtn() {
        return this.mResumeBtn;
    }

    public Button getPauseBtn() {
        return this.mPauseBtn;
    }

    public Button getCancelBtn() {
        return this.mCancelBtn;
    }

    public Button getStartBtn() {
        return this.mStartBtn;
    }

    public LayoutParams getButtonLayoutParam() {
        return this.mButtonLayoutParam;
    }

    public RelativeLayout getButtonLayout() {
        return this.mButtonLayout;
    }

    void releaseInstance() {
        if (this.mButtonLayout != null) {
            this.mButtonLayout.setBackground(null);
            this.mButtonLayout.removeAllViews();
            this.mButtonLayout.destroyDrawingCache();
            this.mButtonLayout = null;
        }
        if (this.mResumeBtn != null) {
            this.mResumeBtn.setOnClickListener(null);
            this.mResumeBtn.setBackgroundResource(0);
            this.mResumeBtn = null;
        }
        if (this.mPauseBtn != null) {
            this.mPauseBtn.setOnClickListener(null);
            this.mPauseBtn.setBackgroundResource(0);
            this.mPauseBtn = null;
        }
        if (this.mCancelBtn != null) {
            this.mCancelBtn.setOnClickListener(null);
            this.mCancelBtn.setBackgroundResource(0);
            this.mCancelBtn = null;
        }
        if (this.mStartBtn != null) {
            this.mStartBtn.setOnClickListener(null);
            this.mStartBtn.setOnTouchListener(null);
            this.mStartBtn.setBackgroundResource(0);
            this.mStartBtn = null;
        }
    }

    private void insertStartLog() {
        String SaDetail;
        long inputMillis = TimerData.getInputMillis();
        if (inputMillis < 60000) {
            SaDetail = "a";
        } else if (inputMillis < 300000) {
            SaDetail = "b";
        } else if (inputMillis < 1800000) {
            SaDetail = "c";
        } else if (inputMillis < 3600000) {
            SaDetail = "d";
        } else if (inputMillis < 7200000) {
            SaDetail = "e";
        } else {
            SaDetail = "f";
        }
        ClockUtils.insertSaLog("130", "1321", SaDetail);
        ClockUtils.insertSaLog("130", "1130", ClockUtils.toTwoDigitString(TimerData.getInputHour()) + ":" + ClockUtils.toTwoDigitString(TimerData.getInputMin()) + ":" + ClockUtils.toTwoDigitString(TimerData.getInputSec()));
    }

    private boolean isHighContrastKeyboard() {
        Cursor cursor;
        Throwable th;
        Throwable th2;
        boolean isHighContrastKeyboard = false;
        try {
            cursor = this.mActivity.getApplicationContext().getContentResolver().query(Uri.parse("content://com.sec.android.inputmethod.implement.setting.provider.KeyboardSettingsProvider"), null, "NAME=?", new String[]{"high_contrast_keyboard"}, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        isHighContrastKeyboard = "1".equals(cursor.getString(cursor.getColumnIndex("VALUE")));
                    }
                } catch (Throwable th3) {
                    th = th3;
                    th2 = th;
                }
            }
            if (cursor != null) {
                if (null != null) {
                    try {
                        cursor.close();
                    } catch (Throwable th4) {
                    }
                } else {
                    cursor.close();
                }
            }
        } catch (SQLiteException e) {
            RuntimeException e2 = e;
            Log.secE("TimerBtnViewModel", "Exception : " + e2.toString());
            return isHighContrastKeyboard;
        } catch (NullPointerException e3) {
            e2 = e3;
            Log.secE("TimerBtnViewModel", "Exception : " + e2.toString());
            return isHighContrastKeyboard;
        }
        return isHighContrastKeyboard;
        if (cursor != null) {
            if (th2 != null) {
                try {
                    cursor.close();
                } catch (Throwable th5) {
                }
            } else {
                cursor.close();
            }
        }
        throw th;
        throw th;
    }
}
