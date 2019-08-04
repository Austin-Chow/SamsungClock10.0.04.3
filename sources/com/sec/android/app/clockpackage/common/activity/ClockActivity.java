package com.sec.android.app.clockpackage.common.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;

public abstract class ClockActivity extends AppCompatActivity {
    private ViewGroup mContentView = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBarForLandscape(getResources().getConfiguration().orientation);
        if (!StateUtils.isCustomTheme(getApplicationContext())) {
            getWindow().setBackgroundDrawableResource(C0645R.drawable.window_content_area_for_clocktheme);
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    public void setContentView(int layoutResId) {
        try {
            setContentView((ViewGroup) LayoutInflater.from(this).inflate(layoutResId, null));
            Toolbar toolbar = (Toolbar) findViewById(C0645R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } catch (InflateException e) {
            Log.secE("ClockActivity", "setContentView InflateException");
        }
    }

    public void setContentView(View view) {
        super.setContentView(view);
        this.mContentView = (ViewGroup) view;
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        this.mContentView = (ViewGroup) view;
    }

    protected void onDestroy() {
        cleanupResources();
        super.onDestroy();
    }

    private void cleanupResources() {
        if (this.mContentView != null) {
            ClockUtils.nullViewDrawablesRecursive(this.mContentView);
        }
        this.mContentView = null;
        System.gc();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.secD("ClockActivity", "onConfigurationChanged newConfig.orientation = " + newConfig.orientation);
        hideStatusBarForLandscape(newConfig.orientation);
    }

    private void hideStatusBarForLandscape(int orientation) {
        if (!Feature.isTablet(getApplicationContext()) && !StateUtils.isContextInDexMode(this)) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            if (orientation == 2) {
                if (isInMultiWindowMode()) {
                    lp.flags &= -1025;
                } else {
                    lp.flags |= 1024;
                }
                lp.semAddExtensionFlags(1);
            } else {
                lp.flags &= -1025;
                lp.semClearExtensionFlags(1);
            }
            getWindow().setAttributes(lp);
        }
    }
}
