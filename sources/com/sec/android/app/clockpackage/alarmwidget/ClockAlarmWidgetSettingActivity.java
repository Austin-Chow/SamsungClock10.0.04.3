package com.sec.android.app.clockpackage.alarmwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetContract.ViewModel;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingActivity;

public class ClockAlarmWidgetSettingActivity extends WidgetSettingActivity {
    Button mAlarmChangeButton;
    int mAppListId;
    private final BroadcastReceiver mIntentReceiver = new C06362();
    private boolean mIsSelected = false;
    ClockAlarmWidgetModel mModel;
    private ViewGroup mPreviewFrame;
    ViewModel mViewModel;

    /* renamed from: com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetSettingActivity$1 */
    class C06351 implements OnClickListener {
        C06351() {
        }

        public void onClick(View v) {
            Intent changeIntent = new Intent();
            changeIntent.setAction("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW");
            changeIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmWidgetListActivity");
            changeIntent.setType("alarm_edit_direct");
            changeIntent.putExtra("AlarmLaunchMode", 2);
            changeIntent.putExtra("widgetId", ClockAlarmWidgetSettingActivity.this.mAppWidgetId);
            changeIntent.putExtra("from", "SimpleClockAlarmWidget");
            changeIntent.setFlags(268468224);
            if (ClockAlarmWidgetSettingActivity.this.getApplicationContext().getPackageManager() == null || ClockAlarmWidgetSettingActivity.this.getApplicationContext().getPackageManager().queryIntentActivities(changeIntent, 0).size() <= 0) {
                Log.secD("ClockAlarmWidgetSettingActivity", "Activity Not Found !");
            } else {
                ClockAlarmWidgetSettingActivity.this.startActivity(changeIntent);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetSettingActivity$2 */
    class C06362 extends BroadcastReceiver {
        C06362() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                int widgetId = intent.getIntExtra("appWidgetId", 0);
                if (action != null || widgetId == ClockAlarmWidgetSettingActivity.this.mAppWidgetId) {
                    Log.secD("ClockAlarmWidgetSettingActivity", "Intent action : " + action);
                    int i = -1;
                    switch (action.hashCode()) {
                        case -1662713978:
                            if (action.equals("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET")) {
                                i = 0;
                                break;
                            }
                            break;
                        case 1639412822:
                            if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_LAUNCH_ACTIVITY_FINISH")) {
                                i = 2;
                                break;
                            }
                            break;
                        case 1803106331:
                            if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SELECT")) {
                                boolean z = true;
                                break;
                            }
                            break;
                    }
                    switch (i) {
                        case 0:
                            ClockAlarmWidgetSettingActivity.this.mViewModel = null;
                            ClockAlarmWidgetSettingActivity.this.drawPreview();
                            if (ClockAlarmWidgetSettingActivity.this.mModel != null && ClockAlarmWidgetSettingActivity.this.mModel.getIsEmpty()) {
                                ClockAlarmWidgetSettingActivity.this.mAppListId = 0;
                            }
                            ClockAlarmWidgetSettingActivity.this.noItemFinishAlarmWidgetSetting();
                            return;
                        case 1:
                            int selectedIndex = intent.getIntExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", 0);
                            ClockAlarmWidgetSettingActivity.this.mViewModel = null;
                            ClockAlarmWidgetSettingActivity.this.mAppListId = selectedIndex;
                            ClockAlarmWidgetSettingActivity.this.drawPreview();
                            ClockAlarmWidgetSettingActivity.this.mIsSelected = true;
                            ClockAlarmWidgetSettingActivity.this.noItemFinishAlarmWidgetSetting();
                            return;
                        case 2:
                            ClockAlarmWidgetSettingActivity.this.mIsSelected = true;
                            ClockAlarmWidgetSettingActivity.this.noItemFinishAlarmWidgetSetting();
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        this.mAppWidgetId = getIntent().getIntExtra("appWidgetId", 0);
        this.mAppListId = ClockAlarmWidgetIdManager.getListItem(this, this.mAppWidgetId, ClockAlarmWidgetIdManager.getListItem(this, this.mAppWidgetId, 0) + 1);
        if (this.mAppListId <= 0) {
            alarmSelectWhenNoItem();
        } else {
            this.mIsSelected = true;
        }
        super.onCreate(savedInstanceState);
        registerReceiver();
    }

    protected void initLayout() {
        super.initLayout();
        drawPreview();
        this.mAlarmChangeButton = (Button) findViewById(R.id.alarm_widget_setting_change_button);
        this.mAlarmChangeButton.setOnClickListener(new C06351());
    }

    private void alarmSelectWhenNoItem() {
        Intent viewIntent = new Intent();
        if (ClockAlarmWidgetUtils.getAlarmItemCount(this) == 0) {
            viewIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity");
            viewIntent.putExtra("AlarmLaunchMode", 2);
            viewIntent.putExtra("widgetId", this.mAppWidgetId);
            viewIntent.putExtra("ListItemPosition", this.mAppListId);
            viewIntent.putExtra("from", "SimpleClockAlarmWidget");
            viewIntent.setType("alarm_create_direct");
            viewIntent.setFlags(335806464);
        } else {
            viewIntent.setAction("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW");
            viewIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmWidgetListActivity");
            viewIntent.setType("alarm_edit_direct");
            viewIntent.putExtra("AlarmLaunchMode", 2);
            viewIntent.putExtra("widgetId", this.mAppWidgetId);
            viewIntent.putExtra("from", "SimpleClockAlarmWidget");
            viewIntent.setFlags(268468224);
        }
        if (getApplicationContext().getPackageManager() == null || getApplicationContext().getPackageManager().queryIntentActivities(viewIntent, 0).size() <= 0) {
            Log.secD("ClockAlarmWidgetSettingActivity", "Activity Not Found !");
        } else {
            startActivity(viewIntent);
        }
    }

    private void noItemFinishAlarmWidgetSetting() {
        if (this.mAppListId == 0 && this.mIsSelected) {
            finish();
        }
    }

    protected void updatePreview() {
        if (this.mPreviewFrame != null) {
            this.mViewModel.setTransparency(this, this.mTheme, this.mTransparency);
            ImageView widgetBackground = (ImageView) this.mPreviewFrame.findViewById(R.id.alarm_widget_background);
            ImageButton onOffButton = (ImageButton) this.mPreviewFrame.findViewById(R.id.alarm_onoff_btn);
            TextView noItem = (TextView) this.mPreviewFrame.findViewById(R.id.label2);
            TextView alarmName = (TextView) this.mPreviewFrame.findViewById(R.id.alarm_name);
            TextView alarmTime = (TextView) this.mPreviewFrame.findViewById(R.id.alarm_time);
            TextView alarmAmpm = (TextView) this.mPreviewFrame.findViewById(R.id.alarm_ampm);
            TextView alarmAmpmKor = (TextView) this.mPreviewFrame.findViewById(R.id.alarm_ampm_kor);
            TextView alarmDate = (TextView) this.mPreviewFrame.findViewById(R.id.alarm_date);
            TextView alarmRepeat = (TextView) this.mPreviewFrame.findViewById(R.id.alarm_repeat_days);
            if (widgetBackground != null) {
                widgetBackground.setColorFilter(this.mModel.getBackgroundColor());
                widgetBackground.setImageAlpha(this.mModel.getTransparency());
            }
            if (onOffButton != null) {
                onOffButton.setImageResource(this.mModel.getOnOffImageColor());
            }
            if (noItem != null && noItem.getVisibility() == 0) {
                noItem.setTextColor(this.mModel.getNoItemTextColor());
            }
            if (alarmName != null && alarmName.getVisibility() == 0) {
                alarmName.setTextColor(this.mModel.getNameAndDateTextColor());
            }
            if (alarmTime != null) {
                alarmTime.setTextColor(this.mModel.getTimeAndAmPmTextColor());
            }
            if (alarmAmpm != null && alarmAmpm.getVisibility() == 0) {
                alarmAmpm.setTextColor(this.mModel.getTimeAndAmPmTextColor());
            }
            if (alarmAmpmKor != null && alarmAmpmKor.getVisibility() == 0) {
                alarmAmpmKor.setTextColor(this.mModel.getTimeAndAmPmTextColor());
            }
            if (alarmDate != null && alarmDate.getVisibility() == 0) {
                alarmDate.setTextColor(this.mModel.getNameAndDateTextColor());
            }
            if (alarmRepeat != null && alarmRepeat.getVisibility() == 0) {
                int i;
                Context context = this.mContext;
                int alarmRepeatIndex = this.mModel.getAlarmRepeatIndex();
                if (this.mModel.getIsActivate()) {
                    i = 1;
                } else {
                    i = 0;
                }
                alarmRepeat.setText(ClockAlarmWidgetUtils.getRepeatDaysString(context, alarmRepeatIndex, i, false, this.mModel.getIsDartFont()));
            }
        }
    }

    protected void drawPreview() {
        ViewModel viewModel = getViewModel();
        viewModel.setTransparency(this, this.mTheme, this.mTransparency);
        viewModel.refresh(this, this.mAppWidgetId, this.mAppListId, true);
        ViewGroup previewFrame = getPreviewFrame();
        previewFrame.addView(viewModel.getRemoteViews().apply(this, previewFrame));
    }

    private ViewGroup getPreviewFrame() {
        if (this.mPreviewFrame == null) {
            this.mPreviewFrame = (ViewGroup) findViewById(R.id.widget_preview);
        }
        this.mPreviewFrame.removeAllViewsInLayout();
        return this.mPreviewFrame;
    }

    private ViewModel getViewModel() {
        if (this.mViewModel == null) {
            this.mModel = ClockAlarmWidgetDataManager.loadModel(this, this.mAppWidgetId, this.mAppListId);
            this.mViewModel = new ClockAlarmWidgetViewModel(this.mModel);
        }
        return this.mViewModel;
    }

    protected void onGlobalLayoutChanged() {
        setPreviewSize();
        this.mViewModel = null;
        drawPreview();
    }

    protected void onResume() {
        super.onResume();
    }

    protected int getWidgetTypeFromId() {
        return 2;
    }

    protected String getThemePreferenceKeyString() {
        return new ViewModelHelper(this, this.mAppWidgetId).getThemeKey(2);
    }

    protected String getTransparentPreferenceKeyString() {
        return new ViewModelHelper(this, this.mAppWidgetId).getTransparentKey(2);
    }

    protected String getIntentActionName() {
        return "com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SETTING_CHANGED";
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        this.mPreviewFrame = null;
        this.mViewModel = null;
        this.mModel = null;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET");
        filter.addAction("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SELECT");
        filter.addAction("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_LAUNCH_ACTIVITY_FINISH");
        registerReceiver(this.mIntentReceiver, filter, null, null);
    }

    private void unregisterReceiver() {
        unregisterReceiver(this.mIntentReceiver);
    }
}
