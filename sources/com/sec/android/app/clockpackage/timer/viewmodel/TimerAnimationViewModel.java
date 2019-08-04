package com.sec.android.app.clockpackage.timer.viewmodel;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.samsung.android.view.animation.SineInOut33;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerAnimationViewModelListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import com.sec.android.app.clockpackage.timer.view.TimerCircleView;
import com.sec.android.app.clockpackage.timer.view.TimerTimeView;

public class TimerAnimationViewModel {
    private Activity mActivity = null;
    private boolean mIsSupportResetButton = false;
    private TimerAnimationViewModelListener mTimerAnimationViewModelListener;

    public TimerAnimationViewModel(Activity activity) {
        this.mActivity = activity;
        this.mIsSupportResetButton = Feature.isSupportTimerResetButton();
    }

    public void setTimerAnimationViewModelListener(TimerAnimationViewModelListener listener) {
        this.mTimerAnimationViewModelListener = listener;
    }

    protected void keypadAnimation(boolean isEditMode, RelativeLayout buttonLayout, LayoutParams buttonLayoutParam, Button resetBtn, boolean isMultiWindowMode) {
        if (!isMultiWindowMode && !StateUtils.isContextInDexMode(this.mActivity) && buttonLayout != null && buttonLayoutParam != null) {
            buttonPositionAnimatorForKeypad(isEditMode, buttonLayoutParam, buttonLayout, resetBtn).start();
        }
    }

    private ValueAnimator buttonPositionAnimatorForKeypad(final boolean isEditMode, final LayoutParams buttonLayoutParam, final RelativeLayout buttonLayout, final Button resetBtn) {
        int startMargin;
        int endMargin;
        final int presetCount = TimerPresetItem.getPresetCount(this.mActivity);
        if (isEditMode) {
            startMargin = 0;
            endMargin = this.mTimerAnimationViewModelListener.onGetButtonLayoutBottomMargin();
        } else {
            startMargin = this.mTimerAnimationViewModelListener.onGetButtonLayoutBottomMargin();
            endMargin = 0;
        }
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{startMargin, endMargin});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                buttonLayoutParam.bottomMargin = ((Integer) animation.getAnimatedValue()).intValue();
                if (buttonLayout != null) {
                    buttonLayout.setLayoutParams(buttonLayoutParam);
                }
            }
        });
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (isEditMode) {
                    if (resetBtn != null && TimerAnimationViewModel.this.mIsSupportResetButton) {
                        resetBtn.setVisibility(8);
                    }
                    if (presetCount > 0) {
                        TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetPresetViewVisibility(8);
                    }
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (!isEditMode && TimerData.isTimerStateResetedOrNone()) {
                    if (resetBtn != null && TimerAnimationViewModel.this.mIsSupportResetButton) {
                        resetBtn.setVisibility(0);
                    }
                    if (presetCount > 0) {
                        TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetPresetViewVisibility(0);
                    }
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.setDuration(330);
        animator.setTarget(buttonLayout);
        return animator;
    }

    public void startAnimation(boolean isStart, TimerPickerViewModel pickerView, TimerTimeView timeView, TimerCircleView circleView, RelativeLayout titleLayout, TimerPresetViewModel presetView, RelativeLayout presetLayout, RelativeLayout buttonLayout, TimerBtnViewModel buttonView, Button resetBtn, TextView nameTextView, boolean isMultiWindowMode, boolean isDisplaySplitView) {
        Button startBtn = buttonView.getStartBtn();
        Button pauseBtn = buttonView.getPauseBtn();
        Button cancelBtn = buttonView.getCancelBtn();
        Button resumeBtn = buttonView.getResumeBtn();
        if (isStart) {
            buttonPositionOpacityAnimatorForStart(buttonLayout, pauseBtn, 1, isStart).start();
            buttonPositionOpacityAnimatorForStart(buttonLayout, cancelBtn, 2, isStart).start();
        } else if (pauseBtn.isShown()) {
            buttonPositionOpacityAnimatorForStart(buttonLayout, pauseBtn, 1, isStart).start();
            buttonPositionOpacityAnimatorForStart(buttonLayout, cancelBtn, 2, isStart).start();
        } else if (resumeBtn.isShown()) {
            buttonPositionOpacityAnimatorForStart(buttonLayout, resumeBtn, 1, isStart).start();
            buttonPositionOpacityAnimatorForStart(buttonLayout, cancelBtn, 2, isStart).start();
        }
        if (startBtn != null) {
            startButtonOpacityAnimatorForStart(isStart, startBtn, buttonView).start();
        }
        if (pickerView != null && !pickerView.isEditMode()) {
            opacityAnimatorForStart(isStart, pickerView, titleLayout, presetView, resetBtn, nameTextView, isMultiWindowMode, isDisplaySplitView).start();
            if (timeView != null) {
                timeView.startAnimation(isStart);
            }
            if (circleView != null) {
                circleView.startAnimation(isStart);
            }
            if (presetView != null && presetView.getPresetCount() > 0 && isDisplaySplitView) {
                if (!(isStart || timeView == null)) {
                    timeView.setVisibility(8);
                }
                splitViewPositionAnimatorForStart(isStart, presetLayout, buttonView).start();
            }
        }
    }

    private AnimatorSet buttonPositionOpacityAnimatorForStart(RelativeLayout buttonLayout, final View view, final int direction, boolean isStart) {
        ValueAnimator positionAnimator;
        ObjectAnimator opacityAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        int buttonLayoutWidth = buttonLayout.getLayoutParams().width;
        int buttonWidth = view.getLayoutParams().width;
        final int buttonMargin = ((buttonLayoutWidth / 2) - buttonWidth) / 2;
        int distance = (buttonLayoutWidth - buttonWidth) / 2;
        if (isStart) {
            positionAnimator = ValueAnimator.ofInt(new int[]{distance, buttonMargin});
        } else {
            positionAnimator = ValueAnimator.ofInt(new int[]{buttonMargin, distance});
        }
        positionAnimator.addUpdateListener(new AnimatorUpdateListener() {
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
        positionAnimator.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f));
        positionAnimator.setDuration(400);
        positionAnimator.setTarget(view);
        view.setAlpha(isStart ? 0.0f : 1.0f);
        if (isStart) {
            opacityAnimator = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f, 1.0f}).setDuration(300);
        } else {
            opacityAnimator = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0f, 0.0f}).setDuration(200);
        }
        opacityAnimator.setInterpolator(new SineInOut33());
        final boolean z = isStart;
        final View view2 = view;
        final int i = direction;
        animatorSet.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (!z) {
                    view2.setEnabled(false);
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (!z) {
                    view2.setVisibility(8);
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
        animatorSet.playTogether(new Animator[]{positionAnimator, opacityAnimator});
        return animatorSet;
    }

    private ObjectAnimator startButtonOpacityAnimatorForStart(final boolean isStart, final Button startBtn, TimerBtnViewModel buttonView) {
        ObjectAnimator opacityAnimator;
        if (isStart) {
            opacityAnimator = ObjectAnimator.ofFloat(startBtn, "alpha", new float[]{1.0f, 0.0f});
        } else {
            opacityAnimator = ObjectAnimator.ofFloat(startBtn, "alpha", new float[]{0.0f, 1.0f});
        }
        opacityAnimator.setInterpolator(new SineInOut33());
        opacityAnimator.setDuration(200);
        opacityAnimator.addListener(new AnimatorListener() {

            /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAnimationViewModel$5$1 */
            class C07771 implements OnGlobalLayoutListener {
                C07771() {
                }

                public void onGlobalLayout() {
                    startBtn.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (startBtn.getLineCount() == startBtn.getMaxLines()) {
                        ClockUtils.setLargeTextSize(TimerAnimationViewModel.this.mActivity.getApplicationContext(), startBtn, (float) TimerAnimationViewModel.this.mActivity.getResources().getDimensionPixelSize(C0728R.dimen.stopwatch_button_min_textsize));
                    }
                }
            }

            public void onAnimationStart(Animator animator) {
                startBtn.setVisibility(0);
                startBtn.getViewTreeObserver().addOnGlobalLayoutListener(new C07771());
                startBtn.setAlpha(isStart ? 1.0f : 0.0f);
                if (!isStart) {
                    startBtn.requestFocus();
                }
            }

            public void onAnimationEnd(Animator animator) {
                if (isStart) {
                    startBtn.setVisibility(8);
                    startBtn.setAlpha(1.0f);
                }
            }

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }
        });
        return opacityAnimator;
    }

    private AnimatorSet opacityAnimatorForStart(boolean isStart, TimerPickerViewModel pickerView, RelativeLayout titleLayout, TimerPresetViewModel presetView, Button resetBtn, TextView nameTextView, boolean isMultiWindowMode, boolean isDisplaySplitView) {
        ObjectAnimator pickerAnimator;
        ObjectAnimator titleAnimator;
        ObjectAnimator gridAnimator;
        ObjectAnimator resetAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        if (isStart) {
            pickerAnimator = ObjectAnimator.ofFloat(pickerView, "alpha", new float[]{1.0f, 0.0f}).setDuration(100);
        } else {
            pickerAnimator = ObjectAnimator.ofFloat(pickerView, "alpha", new float[]{0.0f, 1.0f}).setDuration(400);
        }
        if (isStart) {
            titleAnimator = ObjectAnimator.ofFloat(titleLayout, "alpha", new float[]{1.0f, 0.0f}).setDuration(100);
        } else {
            titleAnimator = ObjectAnimator.ofFloat(titleLayout, "alpha", new float[]{0.0f, 1.0f}).setDuration(400);
        }
        if (isStart) {
            gridAnimator = ObjectAnimator.ofFloat(presetView, "alpha", new float[]{1.0f, 0.0f}).setDuration(100);
        } else {
            gridAnimator = ObjectAnimator.ofFloat(presetView, "alpha", new float[]{0.0f, 1.0f}).setDuration(400);
        }
        if (isStart) {
            resetAnimator = ObjectAnimator.ofFloat(resetBtn, "alpha", new float[]{1.0f, 0.0f}).setDuration(100);
        } else {
            resetAnimator = ObjectAnimator.ofFloat(resetBtn, "alpha", new float[]{0.0f, 1.0f}).setDuration(400);
        }
        pickerAnimator.setInterpolator(new SineInOut33());
        titleAnimator.setInterpolator(new SineInOut33());
        gridAnimator.setInterpolator(new SineInOut33());
        resetAnimator.setInterpolator(new SineInOut33());
        final TimerPresetViewModel timerPresetViewModel = presetView;
        final TimerPickerViewModel timerPickerViewModel = pickerView;
        final boolean z = isDisplaySplitView;
        final boolean z2 = isStart;
        final RelativeLayout relativeLayout = titleLayout;
        final TextView textView = nameTextView;
        final Button button = resetBtn;
        final boolean z3 = isMultiWindowMode;
        AnimatorListener listener = new AnimatorListener() {

            /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAnimationViewModel$6$1 */
            class C07791 implements Runnable {
                C07791() {
                }

                public void run() {
                    if (textView != null) {
                        TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetTimerNameVisibility();
                        textView.setAlpha(1.0f);
                    }
                }
            }

            public void onAnimationStart(Animator animation) {
                float f = 1.0f;
                Log.secD("TimerAnimationViewModel", "onAnimationStart()");
                if (timerPresetViewModel != null && timerPresetViewModel.getPresetCount() > 0) {
                    if (!timerPickerViewModel.isEditMode()) {
                        if (!z) {
                            TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetPresetViewVisibility(0);
                        }
                        timerPresetViewModel.setAlpha(z2 ? 1.0f : 0.0f);
                    }
                    View gridView = timerPresetViewModel.getChildAt(0);
                    if (gridView != null) {
                        gridView.setFocusable(false);
                    }
                }
                if (timerPickerViewModel != null && relativeLayout != null && textView != null) {
                    timerPickerViewModel.setVisibility(0);
                    timerPickerViewModel.setAlpha(z2 ? 1.0f : 0.0f);
                    if (TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onIsDisplayTitleView()) {
                        float f2;
                        relativeLayout.setVisibility(0);
                        RelativeLayout relativeLayout = relativeLayout;
                        if (z2) {
                            f2 = 1.0f;
                        } else {
                            f2 = 0.0f;
                        }
                        relativeLayout.setAlpha(f2);
                    }
                    if (TimerAnimationViewModel.this.mIsSupportResetButton) {
                        button.setVisibility(0);
                        Button button = button;
                        if (!z2) {
                            f = 0.0f;
                        }
                        button.setAlpha(f);
                    }
                    if (z2) {
                        textView.setAlpha(0.0f);
                    } else {
                        textView.setVisibility(8);
                    }
                }
            }

            public void onAnimationEnd(Animator animation) {
                Log.secD("TimerAnimationViewModel", "onAnimationEnd()");
                if (timerPickerViewModel != null && button != null && relativeLayout != null && textView != null) {
                    if (z2) {
                        timerPickerViewModel.setVisibility(8);
                        relativeLayout.setVisibility(8);
                        if (!z) {
                            TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetPresetViewVisibility(8);
                        }
                        button.setVisibility(8);
                        if (z3) {
                            new Handler().postDelayed(new C07791(), 200);
                        } else {
                            textView.setAlpha(1.0f);
                        }
                    } else {
                        TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetViewState(false, true);
                    }
                    timerPickerViewModel.setAlpha(1.0f);
                    relativeLayout.setAlpha(1.0f);
                    if (timerPresetViewModel != null) {
                        timerPresetViewModel.setAlpha(1.0f);
                        View gridView = timerPresetViewModel.getChildAt(0);
                        if (gridView != null) {
                            gridView.setFocusable(true);
                        }
                    }
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        };
        if (presetView == null || presetView.getPresetCount() <= 0) {
            titleAnimator.addListener(listener);
            animatorSet.playTogether(new Animator[]{pickerAnimator, resetAnimator, titleAnimator});
        } else {
            gridAnimator.addListener(listener);
            animatorSet.playTogether(new Animator[]{pickerAnimator, resetAnimator, titleAnimator, gridAnimator});
        }
        return animatorSet;
    }

    private AnimatorSet splitViewPositionAnimatorForStart(final boolean isStart, final RelativeLayout presetLayout, TimerBtnViewModel buttonView) {
        AnimatorSet animatorSet = new AnimatorSet();
        final ConstraintLayout.LayoutParams presetParam = (ConstraintLayout.LayoutParams) presetLayout.getLayoutParams();
        final LayoutParams buttonParam = buttonView.getButtonLayoutParam();
        if (!(presetParam == null || buttonParam == null)) {
            ValueAnimator presetPositionAnimator;
            ValueAnimator buttonPositionAnimator;
            int distance = (int) (((float) this.mActivity.getResources().getDisplayMetrics().widthPixels) * 0.493f);
            if (isStart) {
                presetPositionAnimator = ValueAnimator.ofInt(new int[]{distance, 0});
            } else {
                presetPositionAnimator = ValueAnimator.ofInt(new int[]{0, distance});
            }
            presetPositionAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    presetParam.width = ((Integer) animation.getAnimatedValue()).intValue();
                    presetLayout.setLayoutParams(presetParam);
                }
            });
            distance /= 2;
            if (isStart) {
                buttonPositionAnimator = ValueAnimator.ofInt(new int[]{0, distance});
            } else {
                buttonPositionAnimator = ValueAnimator.ofInt(new int[]{distance, 0});
            }
            buttonPositionAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    buttonParam.setMarginStart(((Integer) animation.getAnimatedValue()).intValue());
                }
            });
            animatorSet.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animator) {
                    TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetPresetViewVisibility(0);
                }

                public void onAnimationEnd(Animator animator) {
                    if (isStart) {
                        buttonParam.setMarginStart(0);
                        TimerAnimationViewModel.this.mTimerAnimationViewModelListener.onSetPresetViewVisibility(8);
                    }
                }

                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }
            });
            animatorSet.playTogether(new Animator[]{presetPositionAnimator, buttonPositionAnimator});
            animatorSet.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f));
            animatorSet.setDuration(400);
        }
        return animatorSet;
    }
}
