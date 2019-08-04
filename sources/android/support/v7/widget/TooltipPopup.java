package android.support.v7.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.view.PointerIconCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

class TooltipPopup {
    private static final String TAG = "TooltipPopup";
    private final View mContentView;
    private final Context mContext;
    private final LayoutParams mLayoutParams = new LayoutParams();
    private final TextView mMessageView;
    private final int[] mTmpAnchorPos = new int[2];
    private final int[] mTmpAppPos = new int[2];
    private final Rect mTmpDisplayFrame = new Rect();

    /* renamed from: android.support.v7.widget.TooltipPopup$1 */
    class C04131 implements OnTouchListener {
        C04131() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 4) {
                TooltipPopup.this.hide();
                return false;
            } else if (motionEvent.getAction() != 0) {
                return false;
            } else {
                TooltipPopup.this.hide();
                return true;
            }
        }
    }

    TooltipPopup(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(16843945, outValue, false);
        if (outValue.data != 0) {
            this.mContext = new ContextThemeWrapper(context, outValue.data);
        } else {
            this.mContext = context;
        }
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C0247R.layout.sesl_tooltip, null);
        this.mMessageView = (TextView) this.mContentView.findViewById(C0247R.id.message);
        this.mContentView.setOnTouchListener(new C04131());
        this.mLayoutParams.setTitle(getClass().getSimpleName());
        this.mLayoutParams.packageName = this.mContext.getPackageName();
        this.mLayoutParams.type = PointerIconCompat.TYPE_HAND;
        this.mLayoutParams.width = -2;
        this.mLayoutParams.height = -2;
        this.mLayoutParams.format = -3;
        this.mLayoutParams.windowAnimations = C0247R.style.Animation_AppCompat_Tooltip;
        this.mLayoutParams.flags = 262152;
    }

    void show(View anchorView, int anchorX, int anchorY, boolean fromTouch, CharSequence tooltipText) {
        if (isShowing()) {
            hide();
        }
        this.mMessageView.setText(tooltipText);
        computePosition(anchorView, anchorX, anchorY, fromTouch, this.mLayoutParams);
        ((WindowManager) this.mContext.getSystemService("window")).addView(this.mContentView, this.mLayoutParams);
    }

    public void showActionItemTooltip(int x, int y, int layoutDirection, CharSequence tooltipText) {
        if (isShowing()) {
            hide();
        }
        this.mMessageView.setText(tooltipText);
        this.mLayoutParams.x = x;
        this.mLayoutParams.y = y;
        if (layoutDirection == 0) {
            this.mLayoutParams.gravity = 8388661;
        } else {
            this.mLayoutParams.gravity = 8388659;
        }
        ((WindowManager) this.mContext.getSystemService("window")).addView(this.mContentView, this.mLayoutParams);
    }

    void hide() {
        if (isShowing()) {
            ((WindowManager) this.mContext.getSystemService("window")).removeView(this.mContentView);
        }
    }

    boolean isShowing() {
        return this.mContentView.getParent() != null;
    }

    void updateContent(CharSequence tooltipText) {
        this.mMessageView.setText(tooltipText);
    }

    private void computePosition(View anchorView, int anchorX, int anchorY, boolean fromTouch, LayoutParams outParams) {
        int offsetX = anchorView.getWidth() / 2;
        int offsetBelow;
        if (anchorView.getHeight() >= this.mContext.getResources().getDimensionPixelOffset(C0247R.dimen.sesl_tooltip_precise_anchor_threshold)) {
            int offsetExtra = this.mContext.getResources().getDimensionPixelOffset(C0247R.dimen.sesl_tooltip_precise_anchor_extra_offset);
            offsetBelow = anchorY + offsetExtra;
            int offsetAbove = anchorY - offsetExtra;
        } else {
            offsetBelow = anchorView.getHeight();
        }
        outParams.gravity = 49;
        int tooltipOffset = this.mContext.getResources().getDimensionPixelOffset(fromTouch ? C0247R.dimen.sesl_tooltip_y_offset_touch : C0247R.dimen.sesl_tooltip_y_offset_non_touch);
        View appView = getAppRootView(anchorView);
        if (appView == null) {
            Log.e(TAG, "Cannot find app view");
            return;
        }
        appView.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
        if (this.mTmpDisplayFrame.left < 0 && this.mTmpDisplayFrame.top < 0) {
            int statusBarHeight;
            Resources res = this.mContext.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId != 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            } else {
                statusBarHeight = 0;
            }
            DisplayMetrics metrics = res.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, statusBarHeight, metrics.widthPixels, metrics.heightPixels);
        }
        appView.getLocationOnScreen(this.mTmpAppPos);
        anchorView.getLocationOnScreen(this.mTmpAnchorPos);
        int[] iArr = this.mTmpAnchorPos;
        iArr[0] = iArr[0] - this.mTmpAppPos[0];
        iArr = this.mTmpAnchorPos;
        iArr[1] = iArr[1] - this.mTmpAppPos[1];
        outParams.x = (this.mTmpAnchorPos[0] + offsetX) - (this.mTmpDisplayFrame.width() / 2);
        int spec = MeasureSpec.makeMeasureSpec(0, 0);
        this.mContentView.measure(spec, spec);
        int tooltipHeight = this.mContentView.getMeasuredHeight();
        int tooltipWidth = this.mContentView.getMeasuredWidth();
        int yAbove = this.mTmpAnchorPos[1] - tooltipHeight;
        int yBelow = this.mTmpAnchorPos[1] + anchorView.getHeight();
        if (fromTouch) {
            if (anchorView.getLayoutDirection() == 0) {
                outParams.x = ((this.mTmpAnchorPos[0] + offsetX) - ((this.mTmpDisplayFrame.width() + tooltipWidth) / 2)) + this.mContext.getResources().getDimensionPixelOffset(C0247R.dimen.sesl_hover_tooltip_popup_right_margin);
            } else {
                outParams.x = ((this.mTmpAnchorPos[0] + offsetX) - ((this.mTmpDisplayFrame.width() - tooltipWidth) / 2)) - this.mContext.getResources().getDimensionPixelOffset(C0247R.dimen.sesl_hover_tooltip_popup_left_margin);
            }
            if (yBelow + tooltipHeight > this.mTmpDisplayFrame.height()) {
                outParams.y = yAbove;
                return;
            } else {
                outParams.y = yBelow;
                return;
            }
        }
        outParams.x = (this.mTmpAnchorPos[0] + offsetX) - (this.mTmpDisplayFrame.width() / 2);
        if (yAbove >= 0) {
            outParams.y = yAbove;
        } else {
            outParams.y = yBelow;
        }
    }

    private static View getAppRootView(View anchorView) {
        for (Context context = anchorView.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return ((Activity) context).getWindow().getDecorView();
            }
        }
        return anchorView.getRootView();
    }
}
