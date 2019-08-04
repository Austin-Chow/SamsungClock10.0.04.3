package com.sec.android.app.clockpackage.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.util.AttributeSet;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.ArrayList;

public class ClockTabLayout extends TabLayout implements OnSystemUiVisibilityChangeListener {
    private Activity mActivity;
    private boolean mIsResumed = false;
    private ArrayList<TextView> mTextViews;

    public ClockTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(Activity activity) {
        this.mActivity = activity;
        this.mTextViews = new ArrayList();
        for (int tabPosition = 0; tabPosition < getTabCount(); tabPosition++) {
            Tab tab = getTabAt(tabPosition);
            ViewGroup tabView = (ViewGroup) getTabView(tabPosition);
            if (!(tab == null || tabView == null)) {
                TextView textView = tab.seslGetTextView();
                this.mTextViews.add(textView);
                if (textView != null) {
                    tabView.setContentDescription(textView.getText() + " " + getResources().getString(C0645R.string.tab_tts, new Object[]{Integer.valueOf(tabPosition + 1), Integer.valueOf(4)}));
                }
                tabView.setPointerIcon(PointerIcon.getSystemIcon(getContext(), 1000));
            }
        }
        invalidateTabLayout();
    }

    private void invalidateTabLayout() {
        ArrayList<Float> tabTextWidthList = new ArrayList();
        float tabTextWidthSum = 0.0f;
        for (int tabPosition = 0; tabPosition < getTabCount(); tabPosition++) {
            float width = getTabTextWidth((TextView) this.mTextViews.get(tabPosition));
            tabTextWidthList.add(Float.valueOf(width));
            tabTextWidthSum += width;
        }
        addTabPaddingValue(tabTextWidthList, tabTextWidthSum);
        setScrollPosition(getSelectedTabPosition(), 0.0f, true);
    }

    private float getTabTextWidth(TextView textView) {
        return textView.getPaint().measureText(textView.getText().toString());
    }

    private void addTabPaddingValue(ArrayList<Float> tabTextWidthList, float tabTextWidthSum) {
        float tabTextPadding = (float) getResources().getDimensionPixelSize(C0645R.dimen.clock_tab_padding);
        float tabTextPaddingSum = tabTextPadding * 8.0f;
        float tabLayoutPadding = (float) getResources().getDimensionPixelSize(C0645R.dimen.clock_tab_layout_padding);
        Window window = this.mActivity.getWindow();
        Point size = new Point();
        if (StateUtils.isVisibleNaviBar(getContext()) || StateUtils.isInMultiwindow() || StateUtils.isContextInDexMode(getContext())) {
            window.getWindowManager().getDefaultDisplay().getSize(size);
        } else {
            window.getWindowManager().getDefaultDisplay().getRealSize(size);
        }
        float screenWidthPixels = (float) size.x;
        if (!StateUtils.isMultiWindowMinSize(this.mActivity, 480, true)) {
            float tabLayoutPaddingMax = screenWidthPixels * 0.125f;
            float tabLayoutPaddingMin = ((screenWidthPixels - tabTextWidthSum) - tabTextPaddingSum) / 2.0f;
            if (tabLayoutPaddingMin >= tabLayoutPaddingMax) {
                tabLayoutPadding = tabLayoutPaddingMax;
            } else if (tabLayoutPadding < tabLayoutPaddingMin) {
                tabLayoutPadding = tabLayoutPaddingMin;
            }
        }
        float widthPixels = screenWidthPixels - (2.0f * tabLayoutPadding);
        int i;
        if (tabTextWidthSum + tabTextPaddingSum < widthPixels) {
            float paddingLeftRight = (float) Math.ceil((double) (((widthPixels - (tabTextWidthSum + tabTextPaddingSum)) / 8.0f) + tabTextPadding));
            float paddingLastTab = (widthPixels - tabTextWidthSum) - (8.0f * paddingLeftRight);
            Log.secD("ClockTabLayout", "padding : " + paddingLeftRight + ", " + paddingLastTab);
            i = 0;
            while (i < tabTextWidthList.size()) {
                if (paddingLastTab == 0.0f || i != 3) {
                    getTabView(i).setMinimumWidth((int) (((Float) tabTextWidthList.get(i)).floatValue() + (2.0f * paddingLeftRight)));
                } else {
                    getTabView(i).setMinimumWidth((int) ((((Float) tabTextWidthList.get(i)).floatValue() + (2.0f * paddingLeftRight)) + paddingLastTab));
                }
                i++;
            }
        } else {
            for (i = 0; i < tabTextWidthList.size(); i++) {
                getTabView(i).setMinimumWidth((int) (((Float) tabTextWidthList.get(i)).floatValue() + (2.0f * tabTextPadding)));
            }
        }
        ((MarginLayoutParams) getLayoutParams()).setMargins((int) tabLayoutPadding, 0, (int) tabLayoutPadding, 0);
        requestLayout();
    }

    private View getTabView(int position) {
        ViewGroup viewGroup = getTabViewGroup();
        if (viewGroup == null || viewGroup.getChildCount() <= position) {
            return null;
        }
        return viewGroup.getChildAt(position);
    }

    private ViewGroup getTabViewGroup() {
        if (getChildCount() <= 0) {
            return null;
        }
        View view = getChildAt(0);
        if (view == null || !(view instanceof ViewGroup)) {
            return null;
        }
        return (ViewGroup) view;
    }

    public void setTabLayoutEnabled(boolean enabled) {
        setEnabled(enabled);
        for (int tabPosition = 0; tabPosition < getTabCount(); tabPosition++) {
            ViewGroup tabView = (ViewGroup) getTabView(tabPosition);
            if (tabView != null) {
                float f;
                tabView.setEnabled(enabled);
                if (enabled) {
                    f = 1.0f;
                } else {
                    f = 0.4f;
                }
                tabView.setAlpha(f);
            }
        }
    }

    public void setResumeStatus(boolean isResumed) {
        this.mIsResumed = isResumed;
    }

    public void onSystemUiVisibilityChange(int visibility) {
        Log.secD("ClockTabLayout", "onSystemUiVisibilityChange, visibility : " + visibility + ", mIsResumed : " + this.mIsResumed);
        if (this.mIsResumed) {
            invalidateTabLayout();
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        invalidateTabLayout();
    }
}
