package com.sec.android.app.clockpackage.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;

public class AlarmWidgetListActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private AlarmMainFragment mAlarmFragment;
    private int mWidgetId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0490R.layout.alarm_widget_list_layout);
        this.bottomNavigation = (BottomNavigationView) findViewById(C0490R.id.bottom_navigation);
        if (savedInstanceState == null && this.mAlarmFragment == null) {
            this.mAlarmFragment = new AlarmMainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            this.mWidgetId = intent.getIntExtra("widgetId", -1);
            if (bundle != null) {
                bundle.putSerializable("Action", intent.getAction());
                this.mAlarmFragment.setArguments(bundle);
            } else {
                bundle = new Bundle();
                bundle.putSerializable("Action", intent.getAction());
                this.mAlarmFragment.setArguments(bundle);
            }
            ft.replace(C0490R.id.alarmFragment, this.mAlarmFragment);
            ft.commit();
            return;
        }
        this.mAlarmFragment = (AlarmMainFragment) getSupportFragmentManager().findFragmentById(C0490R.id.alarmFragment);
    }

    public void onSupportActionModeStarted(ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        this.bottomNavigation.setVisibility(0);
        if (this.mAlarmFragment != null) {
            this.mAlarmFragment.onStartActionMode(this.bottomNavigation);
        }
    }

    public void onSupportActionModeFinished(ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        this.bottomNavigation.setVisibility(8);
        if (this.mAlarmFragment != null) {
            this.mAlarmFragment.onFinishActionMode();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        AlarmUtil.sendFinishLaunchActivityToAlarmWidget(getApplicationContext(), this.mWidgetId);
    }
}
