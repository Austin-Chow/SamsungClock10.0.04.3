package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.stopwatch.C0706R;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchBtnViewModelListener;
import com.sec.android.app.clockpackage.stopwatch.model.StopwatchData;
import java.util.Locale;

public class StopwatchBtnViewModel implements OnClickListener, OnTouchListener {
    private Activity mActivity = null;
    private RelativeLayout mBtnLayout;
    private LayoutParams mBtnLayoutParam;
    private boolean mIsTouchOperation = false;
    private Button mLapBtn;
    private Button mResetBtn;
    private Button mResumeBtn;
    private Button mStartBtn;
    private Button mStopBtn;
    private StopwatchBtnViewModelListener mStopwatchBtnViewModelListener;
    private StopwatchManager mStopwatchManager;

    StopwatchBtnViewModel(Activity activity, View fragmentView, StopwatchManager stopwatchManager, StopwatchBtnViewModelListener stopwatchBtnViewModelListener) {
        this.mActivity = activity;
        this.mStopwatchBtnViewModelListener = stopwatchBtnViewModelListener;
        this.mStopwatchManager = stopwatchManager;
        initButtons(fragmentView);
    }

    public void onClick(View view) {
        if (view != null) {
            this.mStopwatchBtnViewModelListener.onDismissCopyPopupWindow();
            int id = view.getId();
            if (id == C0706R.id.stopwatch_startButton) {
                StopwatchManager.sStartedByUser = true;
                this.mStopwatchManager.start();
                this.mStopwatchBtnViewModelListener.onStartBtn();
            } else if (id == C0706R.id.stopwatch_stopButton) {
                this.mStopwatchManager.stop();
                this.mStopwatchBtnViewModelListener.onStopBtn();
            } else if (id == C0706R.id.stopwatch_lapButton) {
                if (this.mIsTouchOperation) {
                    view.setSoundEffectsEnabled(true);
                } else if (StopwatchData.getLapCount() < 999) {
                    this.mStopwatchManager.addLap();
                }
            } else if (id == C0706R.id.stopwatch_resumeButton) {
                this.mStopwatchManager.resume();
                this.mStopwatchBtnViewModelListener.onResumeBtn();
            } else if (id == C0706R.id.stopwatch_resetButton) {
                this.mStopwatchBtnViewModelListener.onResetBtn();
                this.mStopwatchManager.reset();
            }
            view.requestFocus();
        }
        this.mIsTouchOperation = false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(android.view.View r14, android.view.MotionEvent r15) {
        /*
        r13 = this;
        r12 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r11 = 40;
        r10 = 4;
        r9 = 0;
        r8 = -1;
        r0 = r15.getAction();
        r5 = 2;
        if (r0 == r5) goto L_0x0011;
    L_0x000e:
        r5 = 3;
        if (r0 != r5) goto L_0x005f;
    L_0x0011:
        if (r14 == 0) goto L_0x0061;
    L_0x0013:
        r5 = r14.getId();
        r6 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_lapButton;
        if (r5 == r6) goto L_0x002b;
    L_0x001b:
        r5 = r14.getId();
        r6 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_stopButton;
        if (r5 == r6) goto L_0x002b;
    L_0x0023:
        r5 = r14.getId();
        r6 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_resumeButton;
        if (r5 != r6) goto L_0x0061;
    L_0x002b:
        r1 = new android.graphics.Rect;
        r5 = r14.getLeft();
        r6 = r14.getTop();
        r7 = r14.getRight();
        r8 = r14.getBottom();
        r1.<init>(r5, r6, r7, r8);
        r5 = r14.getLeft();
        r6 = r15.getX();
        r6 = (int) r6;
        r5 = r5 + r6;
        r6 = r14.getTop();
        r7 = r15.getY();
        r7 = (int) r7;
        r6 = r6 + r7;
        r5 = r1.contains(r5, r6);
        if (r5 != 0) goto L_0x005e;
    L_0x005a:
        r5 = 1;
        r14.setSoundEffectsEnabled(r5);
    L_0x005e:
        return r9;
    L_0x005f:
        if (r0 != 0) goto L_0x005e;
    L_0x0061:
        r5 = 1;
        r13.mIsTouchOperation = r5;
        if (r14 == 0) goto L_0x005e;
    L_0x0066:
        r14.playSoundEffect(r9);
        r5 = "StopwatchBtnViewModel";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "v.getId() = ";
        r6 = r6.append(r7);
        r7 = r14.getId();
        r6 = r6.append(r7);
        r6 = r6.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r5, r6);
        r5 = r13.mStopwatchBtnViewModelListener;
        r5.onDismissCopyPopupWindow();
        r5 = r13.mActivity;
        r5 = r5.getApplicationContext();
        r6 = "vibrator";
        r4 = r5.getSystemService(r6);
        r4 = (android.os.Vibrator) r4;
        r3 = r14.getId();
        r5 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_startButton;
        if (r3 != r5) goto L_0x00c6;
    L_0x00a0:
        r5 = r4.semGetSupportedVibrationType();
        if (r5 != r10) goto L_0x00b3;
    L_0x00a6:
        r2 = android.view.HapticFeedbackConstants.semGetVibrationIndex(r11);
        r5 = android.os.VibrationEffect.SemMagnitudeType.TYPE_TOUCH;
        r5 = android.os.VibrationEffect.semCreateWaveform(r2, r8, r5);
        r4.vibrate(r5);
    L_0x00b3:
        r5 = 1;
        com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchManager.sStartedByUser = r5;
        r5 = r13.mStopwatchManager;
        r5.start();
        r5 = r13.mStartBtn;
        r13.performAccessibilityAction(r5, r12);
        r5 = r13.mStopwatchBtnViewModelListener;
        r5.onStartBtn();
        goto L_0x005e;
    L_0x00c6:
        r5 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_stopButton;
        if (r3 != r5) goto L_0x00ee;
    L_0x00ca:
        r5 = r4.semGetSupportedVibrationType();
        if (r5 != r10) goto L_0x00dd;
    L_0x00d0:
        r2 = android.view.HapticFeedbackConstants.semGetVibrationIndex(r11);
        r5 = android.os.VibrationEffect.SemMagnitudeType.TYPE_TOUCH;
        r5 = android.os.VibrationEffect.semCreateWaveform(r2, r8, r5);
        r4.vibrate(r5);
    L_0x00dd:
        r5 = r13.mStopwatchManager;
        r5.stop();
        r5 = r13.mStopBtn;
        r13.performAccessibilityAction(r5, r12);
        r5 = r13.mStopwatchBtnViewModelListener;
        r5.onStopBtn();
        goto L_0x005e;
    L_0x00ee:
        r5 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_lapButton;
        if (r3 != r5) goto L_0x0119;
    L_0x00f2:
        r5 = r4.semGetSupportedVibrationType();
        if (r5 != r10) goto L_0x0107;
    L_0x00f8:
        r5 = 39;
        r2 = android.view.HapticFeedbackConstants.semGetVibrationIndex(r5);
        r5 = android.os.VibrationEffect.SemMagnitudeType.TYPE_TOUCH;
        r5 = android.os.VibrationEffect.semCreateWaveform(r2, r8, r5);
        r4.vibrate(r5);
    L_0x0107:
        r14.setSoundEffectsEnabled(r9);
        r5 = com.sec.android.app.clockpackage.stopwatch.model.StopwatchData.getLapCount();
        r6 = 999; // 0x3e7 float:1.4E-42 double:4.936E-321;
        if (r5 >= r6) goto L_0x005e;
    L_0x0112:
        r5 = r13.mStopwatchManager;
        r5.addLap();
        goto L_0x005e;
    L_0x0119:
        r5 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_resumeButton;
        if (r3 != r5) goto L_0x0141;
    L_0x011d:
        r5 = r4.semGetSupportedVibrationType();
        if (r5 != r10) goto L_0x0130;
    L_0x0123:
        r2 = android.view.HapticFeedbackConstants.semGetVibrationIndex(r11);
        r5 = android.os.VibrationEffect.SemMagnitudeType.TYPE_TOUCH;
        r5 = android.os.VibrationEffect.semCreateWaveform(r2, r8, r5);
        r4.vibrate(r5);
    L_0x0130:
        r5 = r13.mStopwatchManager;
        r5.resume();
        r5 = r13.mResumeBtn;
        r13.performAccessibilityAction(r5, r12);
        r5 = r13.mStopwatchBtnViewModelListener;
        r5.onResumeBtn();
        goto L_0x005e;
    L_0x0141:
        r5 = com.sec.android.app.clockpackage.stopwatch.C0706R.id.stopwatch_resetButton;
        if (r3 != r5) goto L_0x005e;
    L_0x0145:
        r5 = r4.semGetSupportedVibrationType();
        if (r5 != r10) goto L_0x0158;
    L_0x014b:
        r2 = android.view.HapticFeedbackConstants.semGetVibrationIndex(r11);
        r5 = android.os.VibrationEffect.SemMagnitudeType.TYPE_TOUCH;
        r5 = android.os.VibrationEffect.semCreateWaveform(r2, r8, r5);
        r4.vibrate(r5);
    L_0x0158:
        r5 = r13.mResetBtn;
        r13.performAccessibilityAction(r5, r12);
        r5 = r13.mStopwatchBtnViewModelListener;
        r5.onResetBtn();
        r5 = r13.mStopwatchManager;
        r5.reset();
        goto L_0x005e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchBtnViewModel.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initButtons(View fragmentView) {
        this.mStartBtn = (Button) fragmentView.findViewById(C0706R.id.stopwatch_startButton);
        this.mStopBtn = (Button) fragmentView.findViewById(C0706R.id.stopwatch_stopButton);
        this.mResetBtn = (Button) fragmentView.findViewById(C0706R.id.stopwatch_resetButton);
        this.mResumeBtn = (Button) fragmentView.findViewById(C0706R.id.stopwatch_resumeButton);
        this.mLapBtn = (Button) fragmentView.findViewById(C0706R.id.stopwatch_lapButton);
        setButtonTextSize();
        this.mBtnLayout = (RelativeLayout) fragmentView.findViewById(C0706R.id.stopwatch_button);
        this.mStartBtn.setOnTouchListener(this);
        this.mStopBtn.setOnTouchListener(this);
        this.mLapBtn.setOnTouchListener(this);
        this.mResumeBtn.setOnTouchListener(this);
        this.mResetBtn.setOnTouchListener(this);
        this.mStartBtn.setOnClickListener(this);
        this.mStopBtn.setOnClickListener(this);
        this.mLapBtn.setOnClickListener(this);
        this.mResumeBtn.setOnClickListener(this);
        this.mResetBtn.setOnClickListener(this);
        if (!Feature.isFolder(this.mActivity.getApplicationContext())) {
            this.mResetBtn.semSetHoverPopupType(0);
        }
        if (this.mBtnLayout != null) {
            this.mBtnLayoutParam = (LayoutParams) this.mBtnLayout.getLayoutParams();
        }
    }

    private void setButtonMargin(Resources res, int width) {
        int buttonMargin = ((this.mBtnLayoutParam.width / 2) - width) / 2;
        if (this.mResetBtn != null) {
            LayoutParams params = (LayoutParams) this.mResetBtn.getLayoutParams();
            params.width = width;
            params.setMarginEnd(buttonMargin);
            this.mResetBtn.setLayoutParams(params);
        }
        if (this.mLapBtn != null) {
            params = (LayoutParams) this.mLapBtn.getLayoutParams();
            params.width = width;
            params.setMarginEnd(buttonMargin);
            this.mLapBtn.setLayoutParams(params);
        }
        if (this.mResumeBtn != null) {
            params = (LayoutParams) this.mResumeBtn.getLayoutParams();
            params.width = width;
            params.setMarginStart(buttonMargin);
            this.mResumeBtn.setLayoutParams(params);
        }
        if (this.mStopBtn != null) {
            params = (LayoutParams) this.mStopBtn.getLayoutParams();
            params.width = width;
            params.setMarginStart(buttonMargin);
            this.mStopBtn.setLayoutParams(params);
        }
    }

    private void setButtonTextSize() {
        Context context = this.mActivity.getApplicationContext();
        int btnTextSize = this.mActivity.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_button_textsize);
        ClockUtils.setLargeTextSize(context, this.mStartBtn, (float) btnTextSize);
        ClockUtils.setLargeTextSize(context, this.mStopBtn, (float) btnTextSize);
        ClockUtils.setLargeTextSize(context, this.mResetBtn, (float) btnTextSize);
        ClockUtils.setLargeTextSize(context, this.mResumeBtn, (float) btnTextSize);
        ClockUtils.setLargeTextSize(context, this.mLapBtn, (float) btnTextSize);
    }

    void updateButtonLayout(Resources res) {
        int orientation = res.getConfiguration().orientation;
        boolean isMultiWindowMode = this.mActivity.isInMultiWindowMode();
        int screenWidth = res.getDisplayMetrics().widthPixels;
        if (orientation == 2) {
            this.mBtnLayoutParam.width = (int) (((float) screenWidth) * 0.5f);
        } else if (screenWidth > res.getDimensionPixelSize(C0706R.dimen.stopwatch_screen_width_for_button_layout)) {
            this.mBtnLayoutParam.width = (int) (((float) screenWidth) * 0.75f);
        } else {
            this.mBtnLayoutParam.width = screenWidth - (res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_layout_margin) * 2);
        }
        int buttonWidth = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_width);
        if (isMultiWindowMode) {
            int minButtonLayoutWidth = (buttonWidth * 2) + (res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_min_margin) * 4);
            if (this.mBtnLayoutParam.width < minButtonLayoutWidth) {
                LayoutParams layoutParams = this.mBtnLayoutParam;
                if (screenWidth >= minButtonLayoutWidth) {
                    screenWidth = minButtonLayoutWidth;
                }
                layoutParams.width = screenWidth;
                if (this.mBtnLayoutParam.width < buttonWidth * 2) {
                    buttonWidth = this.mBtnLayoutParam.width / 2;
                }
            }
        }
        setButtonTextSize();
        setButtonMargin(res, buttonWidth);
        this.mBtnLayoutParam.height = getButtonLayoutHeight();
        if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            this.mBtnLayoutParam.setMarginStart(this.mActivity.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_landscape_timeview_margin));
            this.mBtnLayoutParam.topMargin = 0;
        } else if (orientation != 2) {
            this.mBtnLayoutParam.setMarginStart(0);
            if (!this.mActivity.isInMultiWindowMode()) {
                this.mBtnLayoutParam.topMargin = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_margin_top);
            } else if (StateUtils.isContextInDexMode(this.mActivity)) {
                this.mBtnLayoutParam.topMargin = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_margin_top);
            } else {
                this.mBtnLayoutParam.topMargin = 0;
            }
        } else if (StateUtils.isContextInDexMode(this.mActivity)) {
            this.mBtnLayoutParam.topMargin = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_margin_top);
        } else if (!Feature.isTablet(this.mActivity.getApplicationContext())) {
            this.mBtnLayoutParam.topMargin = 0;
        }
    }

    private int getButtonLayoutHeight() {
        return this.mActivity.getResources().getDimensionPixelSize(this.mActivity.isInMultiWindowMode() ? C0706R.dimen.stopwatch_button_height : C0706R.dimen.stopwatch_button_layout_height);
    }

    void exceededMaxCount() {
        this.mLapBtn.setClickable(false);
        this.mLapBtn.setSoundEffectsEnabled(false);
        this.mLapBtn.setEnabled(false);
        this.mLapBtn.setAlpha(0.4f);
        this.mLapBtn.setHovered(false);
        Toast.makeText(this.mActivity, String.format(this.mActivity.getString(C0706R.string.stopwatch_maxlap), new Object[]{Integer.valueOf(999)}), 0).show();
    }

    void setStartedViewState(boolean isResume) {
        setBtnAfterStarted(true);
        if (!(isResume || this.mStopBtn == null)) {
            this.mStopBtn.requestFocus();
            performAccessibilityAction(this.mStopBtn, 128);
        }
        if (StopwatchData.getLapCount() >= 999) {
            setLapBtnState(false);
        }
        setBtnAfterStopped(false);
        setStartBtnState(false);
    }

    void setStoppedViewState(boolean isResume) {
        setBtnAfterStopped(true);
        if (!(isResume || this.mResumeBtn == null)) {
            this.mResumeBtn.requestFocus();
            performAccessibilityAction(this.mResumeBtn, 128);
        }
        setStartBtnState(false);
        setBtnAfterStarted(false);
    }

    void setResetViewState(boolean isResume) {
        setStartBtnState(true);
        if (!(isResume || this.mStartBtn == null)) {
            performAccessibilityAction(this.mStartBtn, 128);
        }
        setBtnAfterStopped(false);
        setBtnAfterStarted(false);
    }

    void setBtnAfterStarted(boolean isShow) {
        int i = 0;
        if (this.mStopBtn != null && this.mLapBtn != null) {
            this.mStopBtn.setVisibility(isShow ? 0 : 8);
            this.mStopBtn.setEnabled(isShow);
            Button button = this.mLapBtn;
            if (!isShow) {
                i = 8;
            }
            button.setVisibility(i);
            this.mLapBtn.setEnabled(isShow);
            resizeButtonText(this.mStopBtn);
            resizeButtonText(this.mLapBtn);
        }
    }

    void setBtnAfterStopped(boolean isShow) {
        int i = 0;
        if (this.mResumeBtn != null && this.mResetBtn != null) {
            this.mResumeBtn.setVisibility(isShow ? 0 : 8);
            this.mResumeBtn.setEnabled(isShow);
            Button button = this.mResetBtn;
            if (!isShow) {
                i = 8;
            }
            button.setVisibility(i);
            this.mResetBtn.setEnabled(isShow);
            resizeButtonText(this.mResumeBtn);
            resizeButtonText(this.mResetBtn);
        }
    }

    void setStartBtnState(boolean isShow) {
        if (this.mStartBtn != null) {
            this.mStartBtn.setVisibility(isShow ? 0 : 8);
            this.mStartBtn.setEnabled(isShow);
            this.mStartBtn.requestFocus();
            resizeButtonText(this.mStartBtn);
        }
    }

    void setLapBtnState(boolean enabled) {
        if (this.mLapBtn != null) {
            this.mLapBtn.setClickable(enabled);
            this.mLapBtn.setEnabled(enabled);
            this.mLapBtn.setAlpha(enabled ? 1.0f : 0.4f);
            this.mLapBtn.setSoundEffectsEnabled(enabled);
        }
    }

    void setBtnAfterReseted() {
        if (this.mResumeBtn != null && this.mResetBtn != null) {
            this.mResumeBtn.setEnabled(false);
            this.mResetBtn.setEnabled(false);
        }
    }

    private void resizeButtonText(final Button button) {
        if (button.getVisibility() == 0) {
            button.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    button.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (button.getLineCount() == button.getMaxLines()) {
                        ClockUtils.setLargeTextSize(StopwatchBtnViewModel.this.mActivity.getApplicationContext(), button, (float) StopwatchBtnViewModel.this.mActivity.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_button_min_textsize));
                    }
                }
            });
        }
    }

    public void resizeButtonText() {
        resizeButtonText(this.mStartBtn);
        resizeButtonText(this.mStopBtn);
        resizeButtonText(this.mResetBtn);
        resizeButtonText(this.mLapBtn);
        resizeButtonText(this.mResumeBtn);
    }

    void startButtonAnimation(RelativeLayout buttonLayout, boolean isShow) {
        if (isShow) {
            buttonSlideAnimator(buttonLayout, this.mStopBtn, 1, true).start();
            buttonSlideAnimator(buttonLayout, this.mLapBtn, 2, true).start();
            return;
        }
        buttonSlideAnimator(buttonLayout, this.mResumeBtn, 1, false).start();
        buttonSlideAnimator(buttonLayout, this.mResetBtn, 2, false).start();
    }

    void setPortraitButtonRule() {
        this.mBtnLayoutParam.removeRule(20);
        this.mBtnLayoutParam.addRule(14);
    }

    void setLandscapeButtonRule() {
        this.mBtnLayoutParam.removeRule(14);
        this.mBtnLayoutParam.addRule(20);
    }

    private AnimatorSet buttonSlideAnimator(RelativeLayout buttonLayout, final View view, final int direction, boolean isShow) {
        ValueAnimator animator;
        ObjectAnimator objectAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        int buttonLayoutWidth = buttonLayout.getLayoutParams().width;
        int buttonWidth = view.getLayoutParams().width;
        final int buttonMargin = ((buttonLayoutWidth / 2) - buttonWidth) / 2;
        int distance = (buttonLayoutWidth - buttonWidth) / 2;
        if (isShow) {
            animator = ValueAnimator.ofInt(new int[]{distance, buttonMargin});
        } else {
            animator = ValueAnimator.ofInt(new int[]{buttonMargin, distance});
        }
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = ((Integer) animation.getAnimatedValue()).intValue();
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                if (direction == 1) {
                    layoutParams.setMarginStart(value);
                } else if (direction == 2) {
                    layoutParams.setMarginEnd(value);
                }
                view.setLayoutParams(layoutParams);
            }
        });
        animator.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f));
        animator.setDuration(500);
        animator.setTarget(view);
        view.setAlpha(isShow ? 0.0f : 1.0f);
        if (isShow) {
            objectAnimator = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f, 1.0f}).setDuration(300);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0f, 0.0f}).setDuration(100);
        }
        final boolean z = isShow;
        final View view2 = view;
        final int i = direction;
        animatorSet.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (!z && StopwatchBtnViewModel.this.mStartBtn != null) {
                    StopwatchBtnViewModel.this.setStartBtnState(true);
                    StopwatchBtnViewModel.this.mStartBtn.setAlpha(0.0f);
                    ObjectAnimator.ofFloat(StopwatchBtnViewModel.this.mStartBtn, "alpha", new float[]{0.0f, 1.0f}).setDuration(300).start();
                }
            }

            public void onAnimationEnd(Animator animation) {
                Log.secD("StopwatchBtnViewModel", "onAnimationEnd : isShow = " + z);
                if (StopwatchBtnViewModel.this.mStopwatchBtnViewModelListener.isFragmentAdded() && StopwatchBtnViewModel.this.mActivity != null && !z) {
                    StopwatchBtnViewModel.this.mStopwatchBtnViewModelListener.onSetViewState();
                    view2.setAlpha(1.0f);
                    LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
                    if (i == 1) {
                        layoutParams.setMarginStart(buttonMargin);
                    } else if (i == 2) {
                        layoutParams.setMarginEnd(buttonMargin);
                    }
                    view2.setLayoutParams(layoutParams);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.play(animator).with(objectAnimator);
        return animatorSet;
    }

    private void performAccessibilityAction(Button button, int action) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mActivity.getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            button.performAccessibilityAction(action, null);
        }
    }

    void setStopBtnText() {
        this.mStopBtn.setText(this.mActivity.getApplicationContext().getResources().getString("zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) ? C0706R.string.pause : C0706R.string.stop));
    }

    void releaseInstance() {
        try {
            if (this.mBtnLayout != null) {
                this.mBtnLayout.setBackground(null);
                this.mBtnLayout.removeAllViews();
                this.mBtnLayout.destroyDrawingCache();
                this.mBtnLayout = null;
            }
            if (this.mStartBtn != null) {
                this.mStartBtn.setOnTouchListener(null);
                this.mStartBtn.setOnClickListener(null);
                this.mStartBtn.setBackgroundResource(0);
                this.mStartBtn = null;
            }
            if (this.mStopBtn != null) {
                this.mStopBtn.setOnTouchListener(null);
                this.mStopBtn.setOnClickListener(null);
                this.mStopBtn.setBackgroundResource(0);
                this.mStopBtn = null;
            }
            if (this.mLapBtn != null) {
                this.mLapBtn.setOnTouchListener(null);
                this.mLapBtn.setOnClickListener(null);
                this.mLapBtn.setBackgroundResource(0);
                this.mLapBtn = null;
            }
            if (this.mResumeBtn != null) {
                this.mResumeBtn.setOnTouchListener(null);
                this.mResumeBtn.setOnClickListener(null);
                this.mResumeBtn.setBackgroundResource(0);
                this.mResumeBtn = null;
            }
            if (this.mResetBtn != null) {
                this.mResetBtn.setOnTouchListener(null);
                this.mResetBtn.setOnClickListener(null);
                this.mResetBtn.setBackgroundResource(0);
                this.mResetBtn = null;
            }
        } catch (Exception e) {
            Log.secE("StopwatchBtnViewModel", "Exception : " + e.toString());
        }
    }
}
