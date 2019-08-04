package com.sec.android.app.clockpackage.commonalert.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PointerIconCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.C0661R;
import com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab.OnTriggerListener;

public abstract class Cover {
    protected Context mContext;
    protected final ScoverState mCoverState;
    protected int mCoverType = 2;
    protected View mCoverView;
    protected int mCoverViewSize = 0;
    private boolean mIsFinished = false;
    private AlertSlidingTab mSelector;
    private int mType = 0;

    protected abstract class CoverDialog extends Dialog {

        /* renamed from: com.sec.android.app.clockpackage.commonalert.view.Cover$CoverDialog$1 */
        class C06701 implements OnTriggerListener {
            C06701() {
            }

            public void onTrigger(View arg0, int arg1) {
                if (1 == arg1) {
                    switch (Cover.this.mType) {
                        case 0:
                            Log.secD("ClockCover", "Received key event for AlarmStop");
                            Cover.this.finishAlert(true);
                            break;
                        case 1:
                            Log.secD("ClockCover", "Received key event for finishTimer");
                            Cover.this.finishAlert(false);
                            break;
                    }
                    Cover.this.mIsFinished = true;
                }
            }

            public void onGrabbedStateChange(View v, int grabbedState) {
                Log.secD("ClockCover", "onGrabbedStateChange : " + grabbedState);
                if (Cover.this.getButton() != null && Cover.this.isOptionalButtionVisible()) {
                    if (grabbedState == 1) {
                        Cover.this.getButton().startAnimation(AnimationUtils.loadAnimation(Cover.this.mContext, C0661R.anim.fade_out));
                        Cover.this.getButton().setAlpha(0.0f);
                    } else if (!Cover.this.mIsFinished) {
                        Cover.this.getButton().startAnimation(AnimationUtils.loadAnimation(Cover.this.mContext, C0661R.anim.fade_in));
                        Cover.this.getButton().setAlpha(1.0f);
                    }
                }
            }
        }

        protected abstract int ccTabSelectorId();

        protected abstract void initViews();

        public CoverDialog(Context context, int coverSize) {
            super(context);
            Cover.this.mContext = context;
            requestWindowFeature(1);
            Cover.this.mCoverViewSize = coverSize;
            Log.secD("ClockCover", "mCoverViewSize : " + Cover.this.mCoverViewSize);
            switch (Cover.this.mCoverViewSize) {
                case 0:
                    setContentView(C0661R.layout.alarm_alert_cover);
                    Cover.this.mCoverView = findViewById(Cover.this.coverViewId(0));
                    break;
                case 2:
                case 4:
                    setContentView(C0661R.layout.alarm_alert_cover_clear);
                    Cover.this.mCoverView = findViewById(Cover.this.coverViewId(2));
                    ClockUtils.setLargeTextSize(Cover.this.mContext, new TextView[]{(TextView) findViewById(C0661R.id.clear_cover_top_icon)}, 1.3f);
                    break;
            }
            setSelector();
        }

        private void setSelector() {
            if (Cover.this.mCoverViewSize == 0) {
                Cover.this.mSelector = (AlertSlidingTab) Cover.this.mCoverView.findViewById(ccTabSelectorId());
            } else {
                Cover.this.mSelector = (AlertSlidingTab) findViewById(C0661R.id.cc_tab_selector);
            }
            if (Cover.this.mSelector != null) {
                Cover.this.mSelector.setOnTriggerListener(new C06701());
                AlertSlidingTab.setType(Cover.this.mType);
            }
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            boolean up;
            if (event.getAction() == 1) {
                up = true;
            } else {
                up = false;
            }
            switch (event.getKeyCode()) {
                case 3:
                case 4:
                case 24:
                case 25:
                case 26:
                case 27:
                case 79:
                case 80:
                case 164:
                case 168:
                case 169:
                case PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW /*1015*/:
                case 1082:
                    if (!up) {
                        return true;
                    }
                    Cover.this.finishAlert(false);
                    return true;
                default:
                    return super.dispatchKeyEvent(event);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            Log.secD("ClockCover", "onTouchEvent event = " + event);
            Log.secD("ClockCover", "onTouchEvent event = mIsEnableTorch : " + false);
            if (null == null && StateUtils.isLightNotificationEnabled(Cover.this.mContext)) {
                Intent mStopFlashNoti = new Intent();
                mStopFlashNoti.setAction("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION");
                Cover.this.mContext.sendBroadcast(mStopFlashNoti);
            }
            return super.onTouchEvent(event);
        }
    }

    protected abstract CoverDialog coverDialog();

    protected abstract int coverViewId(int i);

    protected abstract void finishAlert(boolean z);

    protected abstract View getButton();

    protected abstract boolean isOptionalButtionVisible();

    public Cover(Context context, int coverSize, ScoverState state, int type) {
        this.mCoverState = state;
        this.mCoverType = this.mCoverState.getType();
        this.mType = type;
    }

    public void displayDialog(Context context, int type) {
        displayDialog(context, type, false);
    }

    @SuppressLint({"NewApi"})
    public void displayDialog(Context context, int type, boolean isSnooze) {
        Log.secD("ClockCover", "displayDialog");
        if (coverDialog() != null && coverDialog().getWindow() != null) {
            coverDialog().initViews();
            setSystemUIFlagForFullScreen(context);
            coverDialog().getWindow().setLayout(-1, -1);
            coverDialog().getWindow().addFlags(69730304);
            coverDialog().getWindow().clearFlags(2);
            coverDialog().getWindow().setBackgroundDrawable(new ColorDrawable());
            if (this.mCoverState.getType() != 8) {
                coverDialog().getWindow().setLayout(this.mCoverState.getWindowWidth(), this.mCoverState.getWindowHeight());
            }
            coverDialog().getWindow().setGravity(49);
            LayoutParams wlp = coverDialog().getWindow().getAttributes();
            wlp.windowAnimations = 0;
            try {
                wlp.layoutInDisplayCutoutMode = 1;
            } catch (NoSuchFieldError e) {
                Log.secE("ClockCover", "setWindowManager NoSuchFieldError");
            }
            coverDialog().getWindow().setAttributes(wlp);
            coverDialog().setCancelable(false);
            coverDialog().show();
            Log.secD("ClockCover", "ClearCover popup is shown");
        }
    }

    public void setSystemUIFlagForFullScreen(Context context) {
        coverDialog().getWindow().getDecorView().setSystemUiVisibility(2566);
    }

    public void dismissDialog() {
        if (this.mSelector != null) {
            this.mSelector.inactiveHandle();
            this.mSelector.setOnTriggerListener(null);
            this.mSelector.resetContext();
        }
        try {
            if (coverDialog() != null) {
                coverDialog().dismiss();
            }
        } catch (Exception e) {
            Log.secE("ClockCover", "Exception : " + e.toString());
        }
        Log.secD("ClockCover", "ClearCover popup is dismissed");
    }
}
