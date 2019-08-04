package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.design.C0011R;
import android.support.design.widget.SwipeDismissBehavior.OnDismissListener;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
    private static final boolean USE_OFFSET_API;
    static final Handler sHandler = new Handler(Looper.getMainLooper(), new C00211());
    private final AccessibilityManager mAccessibilityManager;
    private List<BaseCallback<B>> mCallbacks;
    private final ContentViewCallback mContentViewCallback;
    final Callback mManagerCallback;
    private final ViewGroup mTargetParent;
    final SnackbarBaseLayout mView;

    public interface ContentViewCallback {
        void animateContentIn(int i, int i2);

        void animateContentOut(int i, int i2);
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$1 */
    static class C00211 implements Callback {
        C00211() {
        }

        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ((BaseTransientBottomBar) message.obj).showView();
                    return true;
                case 1:
                    ((BaseTransientBottomBar) message.obj).hideView(message.arg1);
                    return true;
                default:
                    return false;
            }
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$4 */
    class C00224 implements OnDismissListener {
        C00224() {
        }

        public void onDismiss(View view) {
            view.setVisibility(8);
            BaseTransientBottomBar.this.dispatchDismiss(0);
        }

        public void onDragStateChanged(int state) {
            switch (state) {
                case 0:
                    SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.mManagerCallback);
                    return;
                case 1:
                case 2:
                    SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.mManagerCallback);
                    return;
                default:
                    return;
            }
        }
    }

    interface OnAttachStateChangeListener {
        void onViewAttachedToWindow(View view);

        void onViewDetachedFromWindow(View view);
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$5 */
    class C00245 implements OnAttachStateChangeListener {

        /* renamed from: android.support.design.widget.BaseTransientBottomBar$5$1 */
        class C00231 implements Runnable {
            C00231() {
            }

            public void run() {
                BaseTransientBottomBar.this.onViewHidden(3);
            }
        }

        C00245() {
        }

        public void onViewAttachedToWindow(View v) {
        }

        public void onViewDetachedFromWindow(View v) {
            if (BaseTransientBottomBar.this.isShownOrQueued()) {
                BaseTransientBottomBar.sHandler.post(new C00231());
            }
        }
    }

    interface OnLayoutChangeListener {
        void onLayoutChange(View view, int i, int i2, int i3, int i4);
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$6 */
    class C00256 implements OnLayoutChangeListener {
        C00256() {
        }

        public void onLayoutChange(View view, int left, int top, int right, int bottom) {
            BaseTransientBottomBar.this.mView.setOnLayoutChangeListener(null);
            if (BaseTransientBottomBar.this.shouldAnimate()) {
                BaseTransientBottomBar.this.animateViewIn();
            } else {
                BaseTransientBottomBar.this.onViewShown();
            }
        }
    }

    /* renamed from: android.support.design.widget.BaseTransientBottomBar$7 */
    class C00267 extends AnimatorListenerAdapter {
        C00267() {
        }

        public void onAnimationStart(Animator animator) {
            BaseTransientBottomBar.this.mContentViewCallback.animateContentIn(70, 180);
        }

        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.onViewShown();
        }
    }

    public static abstract class BaseCallback<B> {
        public void onDismissed(B b, int event) {
        }

        public void onShown(B b) {
        }
    }

    final class Behavior extends SwipeDismissBehavior<SnackbarBaseLayout> {
        Behavior() {
        }

        public boolean canSwipeDismissView(View child) {
            return child instanceof SnackbarBaseLayout;
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout parent, SnackbarBaseLayout child, MotionEvent event) {
            switch (event.getActionMasked()) {
                case 0:
                    if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                        SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.mManagerCallback);
                        break;
                    }
                    break;
                case 1:
                case 3:
                    SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.mManagerCallback);
                    break;
            }
            return super.onInterceptTouchEvent(parent, child, event);
        }
    }

    static class SnackbarBaseLayout extends FrameLayout {
        private OnAttachStateChangeListener mOnAttachStateChangeListener;
        private OnLayoutChangeListener mOnLayoutChangeListener;

        SnackbarBaseLayout(Context context) {
            this(context, null);
        }

        SnackbarBaseLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, C0011R.styleable.SnackbarLayout);
            if (a.hasValue(C0011R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(this, (float) a.getDimensionPixelSize(C0011R.styleable.SnackbarLayout_elevation, 0));
            }
            a.recycle();
            setClickable(true);
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (this.mOnLayoutChangeListener != null) {
                this.mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
            }
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.mOnAttachStateChangeListener != null) {
                this.mOnAttachStateChangeListener.onViewAttachedToWindow(this);
            }
            ViewCompat.requestApplyInsets(this);
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.mOnAttachStateChangeListener != null) {
                this.mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.mOnLayoutChangeListener = onLayoutChangeListener;
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
            this.mOnAttachStateChangeListener = listener;
        }
    }

    static {
        boolean z = VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19;
        USE_OFFSET_API = z;
    }

    void dispatchDismiss(int event) {
        SnackbarManager.getInstance().dismiss(this.mManagerCallback, event);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.mManagerCallback);
    }

    final void showView() {
        if (this.mView.getParent() == null) {
            LayoutParams lp = this.mView.getLayoutParams();
            if (lp instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams clp = (CoordinatorLayout.LayoutParams) lp;
                Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(0);
                behavior.setListener(new C00224());
                clp.setBehavior(behavior);
                clp.insetEdge = 80;
            }
            this.mTargetParent.addView(this.mView);
        }
        this.mView.setOnAttachStateChangeListener(new C00245());
        if (!ViewCompat.isLaidOut(this.mView)) {
            this.mView.setOnLayoutChangeListener(new C00256());
        } else if (shouldAnimate()) {
            animateViewIn();
        } else {
            onViewShown();
        }
    }

    void animateViewIn() {
        final int viewHeight = this.mView.getHeight();
        this.mView.setTranslationY((float) viewHeight);
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(new int[]{viewHeight, 0});
        animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator.setDuration(250);
        animator.addListener(new C00267());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            private int mPreviousAnimatedIntValue = viewHeight;

            public void onAnimationUpdate(ValueAnimator animator) {
                int currentAnimatedIntValue = ((Integer) animator.getAnimatedValue()).intValue();
                BaseTransientBottomBar.this.mView.setTranslationY((float) currentAnimatedIntValue);
                this.mPreviousAnimatedIntValue = currentAnimatedIntValue;
            }
        });
        animator.start();
    }

    private void animateViewOut(final int event) {
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(new int[]{0, this.mView.getHeight()});
        animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animator.setDuration(250);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                BaseTransientBottomBar.this.mContentViewCallback.animateContentOut(0, 180);
            }

            public void onAnimationEnd(Animator animator) {
                BaseTransientBottomBar.this.onViewHidden(event);
            }
        });
        animator.addUpdateListener(new AnimatorUpdateListener() {
            private int mPreviousAnimatedIntValue = 0;

            public void onAnimationUpdate(ValueAnimator animator) {
                int currentAnimatedIntValue = ((Integer) animator.getAnimatedValue()).intValue();
                BaseTransientBottomBar.this.mView.setTranslationY((float) currentAnimatedIntValue);
                this.mPreviousAnimatedIntValue = currentAnimatedIntValue;
            }
        });
        animator.start();
    }

    final void hideView(int event) {
        if (shouldAnimate() && this.mView.getVisibility() == 0) {
            animateViewOut(event);
        } else {
            onViewHidden(event);
        }
    }

    void onViewShown() {
        SnackbarManager.getInstance().onShown(this.mManagerCallback);
        if (this.mCallbacks != null) {
            for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
                ((BaseCallback) this.mCallbacks.get(i)).onShown(this);
            }
        }
    }

    void onViewHidden(int event) {
        SnackbarManager.getInstance().onDismissed(this.mManagerCallback);
        if (this.mCallbacks != null) {
            for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
                ((BaseCallback) this.mCallbacks.get(i)).onDismissed(this, event);
            }
        }
        if (VERSION.SDK_INT < 11) {
            this.mView.setVisibility(8);
        }
        ViewParent parent = this.mView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this.mView);
        }
    }

    boolean shouldAnimate() {
        return !this.mAccessibilityManager.isEnabled();
    }
}
