package com.sec.android.app.clockpackage.commonalert.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.SemStatusBarManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.samsung.android.view.animation.Elastic70;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.C0661R;

public abstract class HeadUpNotificationService extends Service {
    private int mBikeMode = 0;
    private int mCallState = 0;
    protected Context mContext;
    private GestureDetector mGestureDetector;
    private SimpleOnGestureListener mGestureListener = new C06722();
    private RelativeLayout mHeadUpNotificationView;
    protected boolean mIsAnimationRunning;
    protected boolean mIsPreFlipOpen;
    private int mMinFlingVelocity;
    private float mStartPointX;
    private float mStartPointY;
    private SemStatusBarManager mStatusBarManager;
    private float mViewPointX;
    @SuppressLint({"ClickableViewAccessibility"})
    private OnTouchListener mViewTouchListener = new C06733();
    private LayoutParams mWindowAttributes;
    private WindowManager mWindowManager;
    protected PhoneStateListener phoneStateListener = new C06711();

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$1 */
    class C06711 extends PhoneStateListener {
        C06711() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if (HeadUpNotificationService.this.mCallState != state) {
                HeadUpNotificationService.this.mCallState = state;
                Log.secD("HeadUpNotificationService", "onCallStateChanged mCallState : " + HeadUpNotificationService.this.mCallState);
                HeadUpNotificationService.this.changedViewByPhoneState(HeadUpNotificationService.this.mCallState);
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$2 */
    class C06722 extends SimpleOnGestureListener {
        C06722() {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!(e1 == null || e2 == null)) {
                int distanceX = Math.abs(((int) e1.getX()) - ((int) e2.getX()));
                int distanceY = Math.abs(((int) e1.getY()) - ((int) e2.getY()));
                if (distanceX >= 50 && distanceX >= distanceY && Math.abs(velocityX) > ((float) HeadUpNotificationService.this.mMinFlingVelocity)) {
                    HeadUpNotificationService.this.animateForSlideOut(velocityX < 0.0f);
                }
            }
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$3 */
    class C06733 implements OnTouchListener {
        C06733() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            boolean z = false;
            Log.secD("HeadUpNotificationService", "onTouch " + event);
            if (HeadUpNotificationService.this.mGestureDetector != null) {
                HeadUpNotificationService.this.mGestureDetector.onTouchEvent(event);
            }
            if (HeadUpNotificationService.this.mIsAnimationRunning) {
                Log.secD("HeadUpNotificationService", "Animation is running...");
                if (HeadUpNotificationService.this.mHeadUpNotificationView.getAlpha() != 0.0f) {
                    return true;
                }
                Log.secD("HeadUpNotificationService", "Animation is running and alpha is 0f");
                return false;
            }
            switch (event.getAction()) {
                case 0:
                    HeadUpNotificationService.this.mStartPointX = event.getRawX();
                    HeadUpNotificationService.this.mStartPointY = event.getRawY();
                    HeadUpNotificationService.this.mViewPointX = (float) HeadUpNotificationService.this.mWindowAttributes.x;
                    return true;
                case 1:
                case 4:
                    HeadUpNotificationService.this.resetViewLayout();
                    return true;
                case 2:
                    int deltaX = (int) (event.getRawX() - HeadUpNotificationService.this.mStartPointX);
                    int deltaY = (int) (event.getRawY() - HeadUpNotificationService.this.mStartPointY);
                    HeadUpNotificationService.this.mWindowAttributes.x = (int) (HeadUpNotificationService.this.mViewPointX + ((float) deltaX));
                    if (!(HeadUpNotificationService.this.mWindowManager == null || HeadUpNotificationService.this.mHeadUpNotificationView == null)) {
                        float maxX = (float) HeadUpNotificationService.this.getMaxX();
                        float absX = Math.abs((float) HeadUpNotificationService.this.mWindowAttributes.x);
                        float alpha = 1.0f;
                        if (absX > 0.0f && absX < maxX) {
                            float ratio = absX / maxX;
                            if (ratio > 0.4f) {
                                HeadUpNotificationService headUpNotificationService = HeadUpNotificationService.this;
                                if (HeadUpNotificationService.this.mWindowAttributes.x < 0) {
                                    z = true;
                                }
                                headUpNotificationService.animateForSlideOut(z);
                                return true;
                            }
                            alpha = 1.0f - ratio;
                        }
                        HeadUpNotificationService.this.mHeadUpNotificationView.setAlpha(alpha);
                        if (deltaY > Math.abs(deltaX) && deltaY > SGKeyCode.CODE_NUMPAD_6 && HeadUpNotificationService.this.mStatusBarManager != null) {
                            HeadUpNotificationService.this.mStatusBarManager.expandNotificationsPanel();
                        }
                    }
                    HeadUpNotificationService.this.updateViewLayout();
                    return true;
                default:
                    return true;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$4 */
    class C06744 implements OnGlobalLayoutListener {
        C06744() {
        }

        public void onGlobalLayout() {
            HeadUpNotificationService.this.animateForShow();
            if (HeadUpNotificationService.this.mHeadUpNotificationView != null) {
                ViewTreeObserver observer = HeadUpNotificationService.this.mHeadUpNotificationView.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnGlobalLayoutListener(this);
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$5 */
    class C06755 extends AnimatorListenerAdapter {
        C06755() {
        }

        public void onAnimationEnd(Animator animation) {
            HeadUpNotificationService.this.mIsAnimationRunning = false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$6 */
    class C06766 extends AnimatorListenerAdapter {
        C06766() {
        }

        public void onAnimationEnd(Animator animation) {
            Log.secD("HeadUpNotificationService", "onAnimationEnd");
            if (HeadUpNotificationService.this.mHeadUpNotificationView != null) {
                HeadUpNotificationService.this.mHeadUpNotificationView.setTranslationY(0.0f);
                HeadUpNotificationService.this.mHeadUpNotificationView.setAlpha(1.0f);
            }
            HeadUpNotificationService.this.mIsAnimationRunning = false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService$7 */
    class C06777 extends AnimatorListenerAdapter {
        C06777() {
        }

        public void onAnimationEnd(Animator animation) {
            HeadUpNotificationService.this.mIsAnimationRunning = false;
            if (HeadUpNotificationService.this.mHeadUpNotificationView != null && HeadUpNotificationService.this.mHeadUpNotificationView.isShown()) {
                HeadUpNotificationService.this.mHeadUpNotificationView.setVisibility(4);
            }
        }
    }

    class BikeModeObserver extends ContentObserver {
        BikeModeObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = HeadUpNotificationService.this.mContext.getContentResolver();
            if (resolver != null) {
                resolver.registerContentObserver(Secure.getUriFor("isBikeMode"), false, this);
            }
        }

        public void onChange(boolean selfChange, Uri uri) {
            if (HeadUpNotificationService.this.mContext != null) {
                int isBikeMode = Secure.getInt(HeadUpNotificationService.this.mContext.getContentResolver(), "isBikeMode", 0);
                Log.secD("HeadUpNotificationService", "isBikeMode = " + isBikeMode + " mBikeMode = " + HeadUpNotificationService.this.mBikeMode);
                if (HeadUpNotificationService.this.mBikeMode != isBikeMode) {
                    HeadUpNotificationService.this.mBikeMode = isBikeMode;
                    if (HeadUpNotificationService.this.mBikeMode != 0) {
                        HeadUpNotificationService.this.animateForSlideOut(false);
                    }
                }
            }
        }
    }

    protected abstract void changedViewByPhoneState(int i);

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        Log.secD("HeadUpNotificationService", "onCreate");
        super.onCreate();
        if (StateUtils.isClockPackageInDexMode()) {
            Display[] displays = ((DisplayManager) getSystemService("display")).getDisplays("com.samsung.android.hardware.display.category.DESKTOP");
            try {
                if (displays[0] == null || displays[0].getState() != 2) {
                    this.mContext = this;
                } else {
                    this.mContext = createDisplayContext(displays[0]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.secE("HeadUpNotificationService", "Exception : " + e.toString());
                this.mContext = this;
            }
        } else {
            this.mContext = this;
        }
        this.mMinFlingVelocity = ViewConfiguration.get(this.mContext).getScaledMinimumFlingVelocity();
        this.mCallState = ((TelephonyManager) getSystemService("phone")).getCallState();
        showHeadUpNotification();
        this.mStatusBarManager = (SemStatusBarManager) getSystemService("sem_statusbar");
        if (Feature.isSupportBikeMode()) {
            new BikeModeObserver(new Handler()).observe();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mWindowAttributes = createLayoutParams();
        if (this.mHeadUpNotificationView != null) {
            this.mHeadUpNotificationView.removeAllViews();
            onCreateCustomView(this.mHeadUpNotificationView);
        }
        setDefaultPosition();
        updateViewLayout();
    }

    private void resetViewLayout() {
        if (this.mHeadUpNotificationView != null) {
            this.mHeadUpNotificationView.setAlpha(1.0f);
        }
        setDefaultPosition();
        updateViewLayout();
    }

    private void setDefaultPosition() {
        getLayoutParams().x = 0;
        getLayoutParams().y = 0;
        if (!StateUtils.isClockPackageInDexMode()) {
            int indicatorHeight = ClockUtils.getStatusBarHeight(this.mContext);
            if (this.mHeadUpNotificationView != null) {
                this.mHeadUpNotificationView.setPadding(0, ((int) this.mContext.getResources().getDimension(C0661R.dimen.clock_hun_layout_margin_top)) + indicatorHeight, 0, 0);
            }
        }
    }

    protected void showHeadUpNotification() {
        Log.secD("HeadUpNotificationService", "showHeadUpNotification");
        this.mHeadUpNotificationView = new RelativeLayout(this.mContext);
        this.mWindowAttributes = createLayoutParams();
        onCreateCustomView(this.mHeadUpNotificationView);
        setDefaultPosition();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mWindowManager.addView(this.mHeadUpNotificationView, this.mWindowAttributes);
        this.mHeadUpNotificationView.getViewTreeObserver().addOnGlobalLayoutListener(new C06744());
        if (!StateUtils.isContextInDexMode(this.mContext)) {
            this.mHeadUpNotificationView.setOnTouchListener(this.mViewTouchListener);
            this.mGestureDetector = new GestureDetector(this, this.mGestureListener);
        }
    }

    private void updateViewLayout() {
        if (this.mWindowManager != null && this.mHeadUpNotificationView != null) {
            this.mWindowManager.updateViewLayout(this.mHeadUpNotificationView, this.mWindowAttributes);
        }
    }

    public void animateForSlideOut(boolean toLeft) {
        if (this.mHeadUpNotificationView != null) {
            this.mIsAnimationRunning = true;
            Log.secD("HeadUpNotificationService", "animateForSlideOut:" + toLeft);
            int duration = getResources().getInteger(C0661R.integer.head_up_notification_slide_out_duration);
            float destination = toLeft ? -((float) getMaxX()) : (float) getMaxX();
            ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(this.mHeadUpNotificationView, View.TRANSLATION_X, new float[]{destination});
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mHeadUpNotificationView, View.ALPHA, new float[]{0.0f});
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(moveAnimator).with(alphaAnimator);
            animatorSet.setDuration((long) duration).addListener(new C06755());
            animatorSet.start();
        }
    }

    public void animateForShow() {
        if (this.mHeadUpNotificationView != null) {
            this.mHeadUpNotificationView.setVisibility(0);
            this.mIsAnimationRunning = true;
            Log.secD("HeadUpNotificationService", "animateForShow");
            this.mHeadUpNotificationView.setTranslationY(-((float) this.mHeadUpNotificationView.getHeight()));
            this.mHeadUpNotificationView.setAlpha(0.0f);
            ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(this.mHeadUpNotificationView, View.TRANSLATION_Y, new float[]{0.0f});
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mHeadUpNotificationView, View.ALPHA, new float[]{1.0f});
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(moveAnimator).with(alphaAnimator);
            animatorSet.setInterpolator(new Elastic70());
            animatorSet.setDuration(500).addListener(new C06766());
            animatorSet.start();
        }
    }

    public void animateForHide() {
        if (this.mHeadUpNotificationView != null) {
            this.mIsAnimationRunning = true;
            Log.secD("HeadUpNotificationService", "animateForHide");
            float height = (float) this.mHeadUpNotificationView.getHeight();
            ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(this.mHeadUpNotificationView, View.TRANSLATION_Y, new float[]{-height});
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.mHeadUpNotificationView, View.ALPHA, new float[]{0.0f});
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(moveAnimator).with(alphaAnimator);
            animatorSet.setDuration(247).addListener(new C06777());
            animatorSet.start();
        }
    }

    private LayoutParams createLayoutParams() {
        int layoutFlag = 25166632;
        if (StateUtils.isTalkBackEnabled(getContext()) || StateUtils.isUniversalSwitchEnabled(this.mContext)) {
            layoutFlag = (25166632 & -33) & -9;
        }
        Log.secD("HeadUpNotificationService", "layoutFlag : " + layoutFlag);
        LayoutParams lp = new LayoutParams(2014, layoutFlag, -3);
        if (StateUtils.isGameModeOn()) {
            lp.width = (int) this.mContext.getResources().getDimension(C0661R.dimen.clock_hun_layout_width_game);
        } else if (StateUtils.isClockPackageInDexMode()) {
            lp.width = (int) this.mContext.getResources().getDimension(C0661R.dimen.clock_hun_layout_width_dex);
        } else {
            lp.width = Math.min(this.mContext.getResources().getDisplayMetrics().widthPixels, this.mContext.getResources().getDisplayMetrics().heightPixels);
        }
        lp.height = -2;
        lp.gravity = 48;
        lp.setTitle(getClass().getName());
        return lp;
    }

    public Context getContext() {
        return this.mContext;
    }

    private LayoutParams getLayoutParams() {
        return this.mWindowAttributes;
    }

    private int getMaxX() {
        DisplayMetrics matrix = new DisplayMetrics();
        this.mWindowManager.getDefaultDisplay().getMetrics(matrix);
        return matrix.widthPixels;
    }

    protected void onCreateCustomView(ViewGroup root) {
    }

    public void onDestroy() {
        Log.secD("HeadUpNotificationService", "onDestroy()");
        if (this.mHeadUpNotificationView != null) {
            this.mHeadUpNotificationView.setVisibility(4);
        }
        removeHeadUpNotification();
        super.onDestroy();
    }

    protected void removeHeadUpNotification() {
        Log.secD("HeadUpNotificationService", "removeHeadUpNotification");
        if (this.mWindowManager != null) {
            this.mWindowManager.removeView(this.mHeadUpNotificationView);
        }
        this.mWindowManager = null;
        if (this.mHeadUpNotificationView != null) {
            this.mHeadUpNotificationView.setOnTouchListener(null);
            this.mHeadUpNotificationView.removeAllViews();
        }
        this.mHeadUpNotificationView = null;
        this.mGestureDetector = null;
    }
}
