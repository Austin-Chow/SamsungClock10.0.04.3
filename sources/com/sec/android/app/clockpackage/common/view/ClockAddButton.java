package com.sec.android.app.clockpackage.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.C0645R;

public class ClockAddButton extends ConstraintLayout {
    private ConstraintLayout mAddButton;
    private TextView mButtonText;
    private final Context mContext;
    private int mViewWidth;

    public ClockAddButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0645R.layout.common_add_button, this, true);
        this.mAddButton = (ConstraintLayout) findViewById(C0645R.id.add_button);
        this.mButtonText = (TextView) findViewById(C0645R.id.add_text);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, C0645R.styleable.ClockAddButton);
        String defaultButtonText = typedArray.getString(C0645R.styleable.ClockAddButton_text);
        typedArray.recycle();
        setText(defaultButtonText);
    }

    public void setText(String string) {
        this.mButtonText.setText(string);
    }

    public TextView getTextView() {
        return this.mButtonText;
    }

    public ConstraintLayout getAddButton() {
        return this.mAddButton;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mAddButton.setMaxWidth((int) (((double) this.mViewWidth) * 0.75d));
        this.mAddButton.setMinWidth((int) (((double) this.mViewWidth) * 0.61d));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }
}
