package android.support.v7.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.widget.SeekBar;

public class SeslSeekBar extends SeslAbsSeekBar {
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private OnSeekBarHoverListener mOnSeekBarHoverListener;

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeslSeekBar seslSeekBar, int i, boolean z);

        void onStartTrackingTouch(SeslSeekBar seslSeekBar);

        void onStopTrackingTouch(SeslSeekBar seslSeekBar);
    }

    public interface OnSeekBarHoverListener {
        void onHoverChanged(SeslSeekBar seslSeekBar, int i, boolean z);

        void onStartTrackingHover(SeslSeekBar seslSeekBar, int i);

        void onStopTrackingHover(SeslSeekBar seslSeekBar);
    }

    public SeslSeekBar(Context context) {
        this(context, null);
    }

    public SeslSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, C0247R.attr.seekBarStyle);
    }

    public SeslSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        super.onProgressRefresh(scale, fromUser, progress);
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.mOnSeekBarChangeListener = l;
    }

    void onStartTrackingTouch() {
        super.onStartTrackingTouch();
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        super.onStopTrackingTouch();
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return SeekBar.class.getName();
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (VERSION.SDK_INT >= 24 && canUserSetProgress()) {
            info.addAction(AccessibilityAction.ACTION_SET_PROGRESS);
        }
    }

    public void setOnSeekBarHoverListener(OnSeekBarHoverListener listener) {
        this.mOnSeekBarHoverListener = listener;
    }

    void onStartTrackingHover(int hoverLevel, int posX, int posY) {
        if (this.mOnSeekBarHoverListener != null) {
            this.mOnSeekBarHoverListener.onStartTrackingHover(this, hoverLevel);
        }
        super.onStartTrackingHover(hoverLevel, posX, posY);
    }

    void onStopTrackingHover() {
        if (this.mOnSeekBarHoverListener != null) {
            this.mOnSeekBarHoverListener.onStopTrackingHover(this);
        }
        super.onStopTrackingHover();
    }

    void onHoverChanged(int hoverLevel, int posX, int posY) {
        if (this.mOnSeekBarHoverListener != null) {
            this.mOnSeekBarHoverListener.onHoverChanged(this, hoverLevel, true);
        }
        super.onHoverChanged(hoverLevel, posX, posY);
    }
}
