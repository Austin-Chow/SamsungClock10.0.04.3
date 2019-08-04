package com.sec.android.app.clockpackage.common.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;

public class ClockSubAppBar extends LinearLayout implements OnClickListener, Checkable, OnCheckedChangeListener {
    private static final int[] CheckedStateSet = new int[]{16842912};
    private final String TAG = "ClockSubAppBar";
    private boolean mChecked = false;
    private final Context mContext;
    private SubAppBarPressListener mSubAppBarPressListener;
    private Switch mSubAppBtn;

    public interface SubAppBarPressListener {
        void setChecked(boolean z);
    }

    public void setSubAppBarPressListener(SubAppBarPressListener listener) {
        this.mSubAppBarPressListener = listener;
    }

    public ClockSubAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.secD("ClockSubAppBar", "ClockSubAppBar");
        this.mContext = context;
        init();
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = getBackground();
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    private void init() {
        Log.secD("ClockSubAppBar", "init");
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0645R.layout.sub_appbar_layout, this, true);
        this.mSubAppBtn = (Switch) findViewById(C0645R.id.sub_appbar_switch);
        this.mSubAppBtn.setOnCheckedChangeListener(this);
        final View parentView = (View) this.mSubAppBtn.getParent();
        parentView.post(new Runnable() {
            public void run() {
                parentView.setTouchDelegate(new TouchDelegate(new Rect(0, 0, ClockSubAppBar.this.getWidth(), ClockSubAppBar.this.getHeight()), ClockSubAppBar.this.mSubAppBtn));
            }
        });
        ClockUtils.setLargeTextSize(this.mContext, (TextView) findViewById(C0645R.id.sub_appbar_textview), (float) getResources().getDimensionPixelSize(C0645R.dimen.sub_appbar_text_size));
        setFocusable(true);
        setClickable(true);
        setOnClickListener(this);
    }

    private void setSubAppText() {
        String str;
        if (this.mChecked) {
            str = getResources().getString(C0645R.string.switch_on);
        } else {
            str = getResources().getString(C0645R.string.switch_off);
        }
        ((TextView) findViewById(C0645R.id.sub_appbar_textview)).setText(str);
        findViewById(C0645R.id.sub_appbar_switch).setContentDescription(getLabel(this.mContext));
    }

    private String getLabel(Context context) {
        if (context instanceof AppCompatActivity) {
            ActivityInfo activityInfo = null;
            PackageManager packageManager = context.getPackageManager();
            try {
                activityInfo = packageManager.getActivityInfo(((AppCompatActivity) context).getComponentName(), 128);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (activityInfo != null) {
                return activityInfo.loadLabel(packageManager).toString();
            }
        }
        return "";
    }

    public void setChecked(boolean isChecked) {
        this.mChecked = isChecked;
        this.mSubAppBtn.setChecked(this.mChecked);
        setSubAppText();
        refreshDrawableState();
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void toggle() {
        this.mChecked = !this.mChecked;
        setChecked(this.mChecked);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setChecked(isChecked);
        this.mSubAppBarPressListener.setChecked(isChecked);
    }

    public void onClick(View v) {
        toggle();
    }
}
