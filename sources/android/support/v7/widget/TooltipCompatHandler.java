package android.support.v7.widget;

import android.provider.Settings.System;
import android.support.v4.hardware.input.SeslInputManagerReflector;
import android.support.v4.provider.SeslSettingsReflector.SeslSystemReflector;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.SeslPointerIconReflector;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;

class TooltipCompatHandler implements OnAttachStateChangeListener, OnHoverListener, OnLongClickListener {
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000;
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000;
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500;
    private static final String TAG = "TooltipCompatHandler";
    private static TooltipCompatHandler sActiveHandler;
    private static boolean sIsCustomTooltipPosition;
    private static boolean sIsTooltipNull = false;
    private static int sLayoutDirection;
    private static TooltipCompatHandler sPendingHandler;
    private static int sPosX;
    private static int sPosY;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mFromTouch;
    private final Runnable mHideRunnable = new C04122();
    private boolean mIsSPenPointChanged = false;
    private boolean mIsShowRunnablePostDelayed = false;
    private TooltipPopup mPopup;
    private final Runnable mShowRunnable = new C04111();
    private final CharSequence mTooltipText;

    /* renamed from: android.support.v7.widget.TooltipCompatHandler$1 */
    class C04111 implements Runnable {
        C04111() {
        }

        public void run() {
            TooltipCompatHandler.this.show(false);
        }
    }

    /* renamed from: android.support.v7.widget.TooltipCompatHandler$2 */
    class C04122 implements Runnable {
        C04122() {
        }

        public void run() {
            TooltipCompatHandler.this.hide();
        }
    }

    public static void setTooltipText(View view, CharSequence tooltipText) {
        if (sPendingHandler != null && sPendingHandler.mAnchor == view) {
            setPendingHandler(null);
        }
        if (TextUtils.isEmpty(tooltipText)) {
            if (sActiveHandler != null && sActiveHandler.mAnchor == view) {
                sActiveHandler.hide();
            }
            view.setOnLongClickListener(null);
            view.setLongClickable(false);
            view.setOnHoverListener(null);
            return;
        }
        if (sActiveHandler == null || sActiveHandler.mAnchor != view) {
            TooltipCompatHandler tooltipCompatHandler = new TooltipCompatHandler(view, tooltipText);
        } else {
            sActiveHandler.hide();
        }
        view.setHapticFeedbackEnabled(false);
    }

    private TooltipCompatHandler(View anchor, CharSequence tooltipText) {
        this.mAnchor = anchor;
        this.mTooltipText = tooltipText;
        this.mAnchor.setOnLongClickListener(this);
        this.mAnchor.setOnHoverListener(this);
    }

    public boolean onLongClick(View v) {
        this.mAnchorX = v.getWidth() / 2;
        this.mAnchorY = v.getHeight() / 2;
        show(true);
        return true;
    }

    public boolean onHover(View v, MotionEvent event) {
        if ((this.mPopup == null || !this.mFromTouch) && (!event.isFromSource(InputDeviceCompat.SOURCE_STYLUS) || isSPenHoveringSettingsEnabled())) {
            AccessibilityManager manager = (AccessibilityManager) this.mAnchor.getContext().getSystemService("accessibility");
            if (!(manager.isEnabled() && manager.isTouchExplorationEnabled())) {
                switch (event.getAction()) {
                    case 7:
                        if (this.mAnchor.isEnabled() && this.mPopup == null) {
                            this.mAnchorX = (int) event.getX();
                            this.mAnchorY = (int) event.getY();
                            showPenPointEffect(event, true);
                            if (!this.mIsShowRunnablePostDelayed) {
                                setPendingHandler(this);
                                this.mIsShowRunnablePostDelayed = true;
                                break;
                            }
                        }
                        break;
                    case 10:
                        Log.d(TAG, "MotionEvent.ACTION_HOVER_EXIT : hide SeslTooltipPopup");
                        showPenPointEffect(event, false);
                        hide();
                        break;
                    default:
                        break;
                }
            }
        }
        return false;
    }

    public void onViewAttachedToWindow(View v) {
    }

    public void onViewDetachedFromWindow(View v) {
        hide();
    }

    private void show(boolean fromTouch) {
        if (ViewCompat.isAttachedToWindow(this.mAnchor)) {
            long timeout;
            setPendingHandler(null);
            if (sActiveHandler != null) {
                sActiveHandler.hide();
            }
            sActiveHandler = this;
            this.mFromTouch = fromTouch;
            this.mPopup = new TooltipPopup(this.mAnchor.getContext());
            if (!sIsCustomTooltipPosition) {
                this.mPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
            } else if (!sIsTooltipNull || fromTouch) {
                this.mPopup.showActionItemTooltip(sPosX, sPosY, sLayoutDirection, this.mTooltipText);
                sIsCustomTooltipPosition = false;
            } else {
                return;
            }
            this.mAnchor.addOnAttachStateChangeListener(this);
            if (this.mFromTouch) {
                timeout = LONG_CLICK_HIDE_TIMEOUT_MS;
            } else if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 1) == 1) {
                timeout = HOVER_HIDE_TIMEOUT_SHORT_MS - ((long) ViewConfiguration.getLongPressTimeout());
            } else {
                timeout = HOVER_HIDE_TIMEOUT_MS - ((long) ViewConfiguration.getLongPressTimeout());
            }
            this.mAnchor.removeCallbacks(this.mHideRunnable);
            this.mAnchor.postDelayed(this.mHideRunnable, timeout);
        }
    }

    private void hide() {
        if (sActiveHandler == this) {
            sActiveHandler = null;
            if (this.mPopup != null) {
                this.mPopup.hide();
                this.mPopup = null;
                this.mAnchor.removeOnAttachStateChangeListener(this);
            } else {
                Log.e(TAG, "sActiveHandler.mPopup == null");
            }
        }
        this.mIsShowRunnablePostDelayed = false;
        if (sPendingHandler == this) {
            setPendingHandler(null);
        }
        this.mAnchor.removeCallbacks(this.mHideRunnable);
    }

    private static void setPendingHandler(TooltipCompatHandler handler) {
        if (sPendingHandler != null) {
            sPendingHandler.cancelPendingShow();
        }
        sPendingHandler = handler;
        if (sPendingHandler != null) {
            sPendingHandler.scheduleShow();
        }
    }

    private void scheduleShow() {
        this.mAnchor.postDelayed(this.mShowRunnable, (long) ViewConfiguration.getLongPressTimeout());
    }

    private void cancelPendingShow() {
        this.mAnchor.removeCallbacks(this.mShowRunnable);
    }

    private void update(CharSequence tooltipText) {
        this.mPopup.updateContent(tooltipText);
    }

    public static void setTooltipPosition(int x, int y, int layoutDirection) {
        sLayoutDirection = layoutDirection;
        sPosX = x;
        sPosY = y;
        sIsCustomTooltipPosition = true;
    }

    public static void setTooltipNull(boolean tooltipNull) {
        sIsTooltipNull = tooltipNull;
    }

    private void showPenPointEffect(MotionEvent event, boolean show) {
        if (event.getToolType(0) != 2) {
            return;
        }
        if (show) {
            SeslInputManagerReflector.setPointerIconType(SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_MORE());
            this.mIsSPenPointChanged = true;
        } else if (this.mIsSPenPointChanged) {
            SeslInputManagerReflector.setPointerIconType(SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
            this.mIsSPenPointChanged = false;
        }
    }

    boolean isSPenHoveringSettingsEnabled() {
        if (System.getInt(this.mAnchor.getContext().getContentResolver(), SeslSystemReflector.getField_SEM_PEN_HOVERING(), 0) == 1) {
            return true;
        }
        return false;
    }
}
