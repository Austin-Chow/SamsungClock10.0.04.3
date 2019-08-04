package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.view.ActionBarPolicy;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

class AppCompatPopupWindow extends PopupWindow {
    private static final boolean COMPAT_OVERLAP_ANCHOR = (VERSION.SDK_INT < 21);
    private static final String TAG = "AppCompatPopupWindow";
    private Context mContext;
    private boolean mHasNavigationBar;
    private int mNavigationBarHeight;
    private boolean mOverlapAnchor;
    private final Rect mTempRect = new Rect();

    public AppCompatPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public AppCompatPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, C0247R.styleable.PopupWindow, defStyleAttr, defStyleRes);
        if (a.hasValue(C0247R.styleable.PopupWindow_overlapAnchor)) {
            setSupportOverlapAnchor(a.getBoolean(C0247R.styleable.PopupWindow_overlapAnchor, false));
        }
        this.mContext = context;
        if (VERSION.SDK_INT >= 23) {
            Transition enterTransition = getTransition(a.getResourceId(C0247R.styleable.PopupWindow_popupEnterTransition, 0));
            Transition exitTransition = getTransition(a.getResourceId(C0247R.styleable.PopupWindow_popupExitTransition, 0));
            setEnterTransition(enterTransition);
            setExitTransition(exitTransition);
        }
        setBackgroundDrawable(a.getDrawable(C0247R.styleable.PopupWindow_android_popupBackground));
        a.recycle();
        this.mHasNavigationBar = ActionBarPolicy.get(context).hasNavigationBar();
        this.mNavigationBarHeight = this.mContext.getResources().getDimensionPixelSize(C0247R.dimen.sesl_navigation_bar_height);
    }

    private Transition getTransition(int resId) {
        if (!(resId == 0 || resId == 17760256)) {
            Transition transition = TransitionInflater.from(this.mContext).inflateTransition(resId);
            if (transition != null) {
                boolean isEmpty = (transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0;
                if (!isEmpty) {
                    return transition;
                }
            }
        }
        return null;
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (COMPAT_OVERLAP_ANCHOR && this.mOverlapAnchor) {
            yoff -= anchor.getHeight();
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (COMPAT_OVERLAP_ANCHOR && this.mOverlapAnchor) {
            yoff -= anchor.getHeight();
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        if (COMPAT_OVERLAP_ANCHOR && this.mOverlapAnchor) {
            yoff -= anchor.getHeight();
        }
        super.update(anchor, xoff, yoff, width, height);
    }

    private void setSupportOverlapAnchor(boolean overlapAnchor) {
        if (COMPAT_OVERLAP_ANCHOR) {
            this.mOverlapAnchor = overlapAnchor;
        } else {
            PopupWindowCompat.setOverlapAnchor(this, overlapAnchor);
        }
    }

    public boolean getSupportOverlapAnchor() {
        return PopupWindowCompat.getOverlapAnchor(this);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        int distanceToBottom;
        Rect displayFrame = new Rect();
        if (ignoreBottomDecorations) {
            SeslViewReflector.getWindowDisplayFrame(anchor, displayFrame);
            if (this.mHasNavigationBar && this.mContext.getResources().getConfiguration().orientation != 2) {
                displayFrame.bottom -= this.mNavigationBarHeight;
            }
        } else {
            anchor.getWindowVisibleDisplayFrame(displayFrame);
        }
        int[] anchorPos = new int[2];
        anchor.getLocationOnScreen(anchorPos);
        int bottomEdge = displayFrame.bottom;
        if (getSupportOverlapAnchor()) {
            distanceToBottom = (bottomEdge - anchorPos[1]) - yOffset;
        } else {
            distanceToBottom = (bottomEdge - (anchorPos[1] + anchor.getHeight())) - yOffset;
        }
        int returnedHeight = Math.max(distanceToBottom, (anchorPos[1] - displayFrame.top) + yOffset);
        if (getBackground() == null) {
            return returnedHeight;
        }
        getBackground().getPadding(this.mTempRect);
        return returnedHeight - (this.mTempRect.top + this.mTempRect.bottom);
    }
}
