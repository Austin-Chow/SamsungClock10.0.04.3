package android.support.design.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class SeslAbsIndicatorView extends View {
    int mIndicatorColor;

    abstract void onHide();

    abstract void onPressed();

    abstract void onReleased();

    abstract void onSetSelectedIndicatorColor(int i);

    abstract void onShow();

    public SeslAbsIndicatorView(Context context) {
        super(context);
    }

    public SeslAbsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeslAbsIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SeslAbsIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSelectedIndicatorColor(int color) {
        this.mIndicatorColor = color;
        onSetSelectedIndicatorColor(this.mIndicatorColor);
    }

    public void setPressed() {
        onPressed();
    }

    public void setReleased() {
        onReleased();
    }

    public void setHide() {
        onHide();
    }

    public void setHideImmediatly() {
    }

    public void setShow() {
        onShow();
    }
}
