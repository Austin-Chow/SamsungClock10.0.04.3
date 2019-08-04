package com.sec.android.app.clockpackage.commonwidget;

import android.app.ActionBar;
import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.Locale;

public abstract class WidgetSettingActivity extends Activity implements OnSeekBarChangeListener {
    protected int mAppWidgetId = 0;
    protected Context mContext;
    private TextView mCurrentTransparentTextView;
    protected FrameLayout mFadeEffectBody;
    private boolean mIsFirstEnabled = false;
    protected int mPreviewHeight;
    protected int mPreviewWidth;
    private String mSeekBarKey;
    protected int mTheme = 0;
    private String mThemeKey;
    protected int mTransparency = 255;
    protected int mWidgetType;

    /* renamed from: com.sec.android.app.clockpackage.commonwidget.WidgetSettingActivity$1 */
    class C06791 implements OnClickListener {
        C06791() {
        }

        public void onClick(View arg0) {
            WidgetSettingActivity.this.onBackPressed();
        }
    }

    private class PreviewOnGlobalLayoutListener implements OnGlobalLayoutListener {
        private PreviewOnGlobalLayoutListener() {
        }

        public void onGlobalLayout() {
            int measuredWidth = WidgetSettingActivity.this.mFadeEffectBody.getMeasuredWidth();
            int measuredHeight = WidgetSettingActivity.this.mFadeEffectBody.getMeasuredHeight();
            boolean isLayoutChanged = (WidgetSettingActivity.this.mPreviewWidth == measuredWidth && WidgetSettingActivity.this.mPreviewHeight == measuredHeight) ? false : true;
            if (isLayoutChanged) {
                WidgetSettingActivity.this.mPreviewWidth = measuredWidth;
                WidgetSettingActivity.this.mPreviewHeight = measuredHeight;
                WidgetSettingActivity.this.onGlobalLayoutChanged();
            }
        }
    }

    private class RadioGroupCheckedChangeListener implements OnCheckedChangeListener {
        private final RadioButton blackButton;
        private final RadioButton whiteButton;

        RadioGroupCheckedChangeListener(RadioButton whiteButton, RadioButton blackButton) {
            this.whiteButton = whiteButton;
            this.blackButton = blackButton;
        }

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == C0678R.id.radio_white) {
                WidgetSettingActivity.this.mTheme = 0;
                this.whiteButton.setChecked(true);
            } else if (checkedId == C0678R.id.radio_black) {
                WidgetSettingActivity.this.mTheme = 1;
                this.blackButton.setChecked(true);
            }
            WidgetSettingActivity.this.updatePreview();
            WidgetSettingActivity.this.updatePreference();
        }
    }

    protected abstract void drawPreview();

    protected abstract String getIntentActionName();

    protected abstract String getThemePreferenceKeyString();

    protected abstract String getTransparentPreferenceKeyString();

    protected abstract int getWidgetTypeFromId();

    protected abstract void onGlobalLayoutChanged();

    protected abstract void updatePreview();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        Intent intent = getIntent();
        this.mAppWidgetId = intent.getIntExtra("appWidgetId", 0);
        if (intent.hasExtra("WidgetType")) {
            this.mWidgetType = intent.getIntExtra("WidgetType", 0);
        } else {
            if (!WidgetSettingUtils.isValidWidgetId(this.mAppWidgetId) || TextUtils.isEmpty(getWidgetProviderClassName(this.mAppWidgetId))) {
                finishWithResult(0);
            }
            this.mIsFirstEnabled = true;
            this.mWidgetType = getWidgetTypeFromId();
            sendBroadcastForWidget();
        }
        setContentView(C0678R.layout.widget_setting_activity);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initPreference();
        initLayout();
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBar.setProgress(progress);
        this.mTransparency = progress;
        Log.secD("WidgetSettingActivity", "mTransparency : " + this.mTransparency);
        this.mCurrentTransparentTextView.setText(getCurrentTransparentText(progress));
        updatePreview();
    }

    private String getCurrentTransparentText(int transparency) {
        Log.secD("WidgetSettingActivity", "isUseArabianNumberInRtl : " + StateUtils.isUseArabianNumberInRtl());
        if (StateUtils.isUseArabianNumberInRtl()) {
            return "% " + String.valueOf((transparency * 100) / 255);
        }
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf((transparency * 100) / 255)}) + " %";
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        updatePreference();
    }

    protected void updatePreference() {
        Editor editor = WidgetSettingUtils.getSharedPreferences(this).edit();
        editor.putInt(this.mSeekBarKey, this.mTransparency);
        editor.putInt(this.mThemeKey, this.mTheme);
        editor.apply();
        sendBroadcastForWidget();
    }

    private void sendBroadcastForWidget() {
        Intent updateIntent = new Intent();
        updateIntent.setPackage("com.sec.android.app.clockpackage");
        updateIntent.setAction(getIntentActionName());
        updateIntent.putExtra("appWidgetId", this.mAppWidgetId);
        sendBroadcast(updateIntent);
    }

    protected void initPreference() {
        initPreferenceKeyString();
        this.mTransparency = WidgetSettingUtils.getSharedPreference(this, this.mSeekBarKey, WidgetSettingUtils.getDefaultTransparency(this.mWidgetType));
        this.mTheme = WidgetSettingUtils.getSharedPreference(this, this.mThemeKey, 0);
        updatePreference();
    }

    private void initPreferenceKeyString() {
        this.mThemeKey = getThemePreferenceKeyString();
        this.mSeekBarKey = getTransparentPreferenceKeyString();
    }

    protected void initLayout() {
        View bottomLayout = findViewById(C0678R.id.widget_bottom_layout);
        RadioButton whiteButton = (RadioButton) bottomLayout.findViewById(C0678R.id.radio_white);
        RadioButton blackButton = (RadioButton) bottomLayout.findViewById(C0678R.id.radio_black);
        if (this.mWidgetType != 1) {
            ((LinearLayout) findViewById(C0678R.id.widget_setting_general_label)).setVisibility(8);
            ((RelativeLayout) findViewById(C0678R.id.dual_clock_first)).setVisibility(8);
            ((RelativeLayout) findViewById(C0678R.id.dual_clock_second)).setVisibility(8);
        }
        if (this.mWidgetType != 2) {
            ((ConstraintLayout) findViewById(C0678R.id.alarm_widget_setting_edit_layout)).setVisibility(8);
        }
        if (this.mTheme == 0) {
            whiteButton.setChecked(true);
        } else {
            blackButton.setChecked(true);
        }
        ((RadioGroup) bottomLayout.findViewById(C0678R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroupCheckedChangeListener(whiteButton, blackButton));
        ((ImageButton) findViewById(C0678R.id.action_home)).setOnClickListener(new C06791());
        this.mCurrentTransparentTextView = (TextView) findViewById(C0678R.id.widget_setting_seek_bar_text);
        this.mCurrentTransparentTextView.setText(getCurrentTransparentText(this.mTransparency));
        this.mCurrentTransparentTextView.setWidth((int) Math.ceil((double) this.mCurrentTransparentTextView.getPaint().measureText("100 %")));
        Resources res = getResources();
        SeekBar seekBar = (SeekBar) findViewById(C0678R.id.widget_setting_seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgressDrawable(res.getDrawable(C0678R.drawable.widget_setting_seekbar_progress_white, null));
        seekBar.setProgress(this.mTransparency);
        this.mFadeEffectBody = (FrameLayout) findViewById(C0678R.id.widget_preview);
        ((LinearLayout) findViewById(C0678R.id.widget_setting_activity)).getViewTreeObserver().addOnGlobalLayoutListener(new PreviewOnGlobalLayoutListener());
    }

    public void onBackPressed() {
        finishWithResult(-1);
        super.onBackPressed();
    }

    private void finishWithResult(int resultCode) {
        if (needToCheckWidgetId()) {
            Intent resultValue = new Intent();
            resultValue.putExtra("appWidgetId", this.mAppWidgetId);
            setResult(resultCode, resultValue);
            if (resultCode == 0) {
                new AppWidgetHost(this, 0).deleteAppWidgetId(this.mAppWidgetId);
            }
        }
        finish();
    }

    private boolean needToCheckWidgetId() {
        return this.mIsFirstEnabled && WidgetSettingUtils.isValidWidgetId(this.mAppWidgetId);
    }

    protected String getWidgetProviderClassName(int appWidgetId) {
        AppWidgetProviderInfo appWidgetInfo = AppWidgetManager.getInstance(this).getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo != null) {
            ComponentName componentName = appWidgetInfo.provider;
            if (componentName != null) {
                return componentName.getClassName();
            }
        }
        return "";
    }

    protected void setPreviewSize() {
        int width;
        int height;
        if (StateUtils.isScreenDp(this.mContext, 600)) {
            if (this.mWidgetType == 1) {
                width = (int) (((float) this.mPreviewWidth) * 0.7f);
                height = (int) (((float) width) * 0.33f);
            } else {
                width = (int) (((float) this.mPreviewWidth) * 0.35f);
                height = (int) (((float) width) * 0.67f);
            }
        } else if (!StateUtils.isScreenDp(this.mContext, 457)) {
            width = this.mPreviewWidth;
            height = (int) (((float) width) * 0.31f);
        } else if (this.mWidgetType == 1) {
            width = (int) (((float) this.mPreviewWidth) * 0.82f);
            height = (int) (((float) width) * 0.33f);
        } else if (WidgetSettingUtils.isLandscape(this.mContext)) {
            width = (int) (((float) this.mPreviewWidth) * 0.82f);
            height = (int) (((float) width) * 0.33f);
        } else {
            width = (int) (((float) this.mPreviewWidth) * 0.41f);
            height = (int) (((float) width) * 0.67f);
        }
        int leftRightPadding = (this.mPreviewWidth - width) / 2;
        int topBottomPadding = (this.mPreviewHeight - height) / 2;
        this.mFadeEffectBody.setPadding(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding);
    }
}
