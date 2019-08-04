package com.sec.android.app.clockpackage.alarm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar.SubAppBarPressListener;
import java.util.HashMap;

public class AlarmSnoozeActivity extends ClockActivity {
    private final String TAG = "AlarmSnoozeActivity";
    private int mDuration;
    private boolean mIsSnoozeActive = false;
    private int mRepeat;
    private View[] mSnoozeDurationList;
    private HashMap<View, Integer> mSnoozeDurationViewMap;
    private View[] mSnoozeRepeatList;
    private HashMap<View, Integer> mSnoozeRepeatViewMap;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSnoozeActivity$1 */
    class C05201 implements OnTouchListener {
        C05201() {
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View v, MotionEvent event) {
            return !AlarmSnoozeActivity.this.mIsSnoozeActive;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSnoozeActivity$2 */
    class C05212 implements SubAppBarPressListener {
        C05212() {
        }

        public void setChecked(boolean isChecked) {
            AlarmSnoozeActivity.this.mIsSnoozeActive = isChecked;
            Log.secD("AlarmSnoozeActivity", "mIsSnoozeActive : " + AlarmSnoozeActivity.this.mIsSnoozeActive);
            AlarmSnoozeActivity.this.setEnableRadioButton(AlarmSnoozeActivity.this.mSnoozeDurationList, isChecked);
            AlarmSnoozeActivity.this.setEnableRadioButton(AlarmSnoozeActivity.this.mSnoozeRepeatList, isChecked);
            if (AlarmSnoozeActivity.this.semIsResumed()) {
                ClockUtils.insertSaLog("105", "1040", isChecked ? "1" : "0");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSnoozeActivity$3 */
    class C05223 implements OnClickListener {
        C05223() {
        }

        public void onClick(View v) {
            CheckedTextView snoozeDurationCheckBox = (CheckedTextView) AlarmSnoozeActivity.this.findViewById(v.getId());
            if (snoozeDurationCheckBox != null && !snoozeDurationCheckBox.isChecked()) {
                AlarmSnoozeActivity.this.initRadioButton(AlarmSnoozeActivity.this.mSnoozeDurationList);
                snoozeDurationCheckBox.setChecked(true);
                AlarmSnoozeActivity.this.mDuration = ((Integer) AlarmSnoozeActivity.this.mSnoozeDurationViewMap.get(snoozeDurationCheckBox)).intValue();
                ClockUtils.insertSaLog("105", "1041");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSnoozeActivity$4 */
    class C05234 implements OnClickListener {
        C05234() {
        }

        public void onClick(View v) {
            CheckedTextView snoozeRepeatCheckBox = (CheckedTextView) AlarmSnoozeActivity.this.findViewById(v.getId());
            if (snoozeRepeatCheckBox != null && !snoozeRepeatCheckBox.isChecked()) {
                AlarmSnoozeActivity.this.initRadioButton(AlarmSnoozeActivity.this.mSnoozeRepeatList);
                snoozeRepeatCheckBox.setChecked(true);
                AlarmSnoozeActivity.this.mRepeat = ((Integer) AlarmSnoozeActivity.this.mSnoozeRepeatViewMap.get(snoozeRepeatCheckBox)).intValue();
                ClockUtils.insertSaLog("105", "1042");
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlarmSnoozeActivity", "onCreate");
        setContentView(C0490R.layout.alarm_snooze);
        TextView text1 = (TextView) findViewById(C0490R.id.snooze_layout1);
        TextView text2 = (TextView) findViewById(C0490R.id.snooze_layout2);
        try {
            findViewById(C0490R.id.snooze_duration_items).semSetRoundedCorners(15);
            findViewById(C0490R.id.snooze_duration_items).semSetRoundedCornerColor(15, getColor(C0490R.color.window_background_color));
            findViewById(C0490R.id.snooze_repeat_items).semSetRoundedCorners(15);
            findViewById(C0490R.id.snooze_repeat_items).semSetRoundedCornerColor(15, getColor(C0490R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmSnoozeActivity", "NoSuchMethodError : " + e);
        }
        if (!(StateUtils.isContextInDexMode(this) || StateUtils.isCustomTheme(this))) {
            findViewById(C0490R.id.snooze_duration_items).setBackground(getDrawable(C0490R.drawable.contents_area_background));
            findViewById(C0490R.id.snooze_repeat_items).setBackground(getDrawable(C0490R.drawable.contents_area_background));
        }
        if (text1 != null) {
            text1.setContentDescription(getResources().getString(C0490R.string.alarm_snooze_duration) + ',' + getResources().getString(C0490R.string.header));
        }
        if (text2 != null) {
            text2.setContentDescription(getResources().getString(C0490R.string.alarm_snooze_repeat) + ',' + getResources().getString(C0490R.string.header));
        }
        this.mSnoozeDurationList = new View[]{findViewById(C0490R.id.duration_layout0), findViewById(C0490R.id.duration_layout1), findViewById(C0490R.id.duration_layout2), findViewById(C0490R.id.duration_layout3), findViewById(C0490R.id.duration_layout4)};
        this.mSnoozeRepeatList = new View[]{findViewById(C0490R.id.repeat_layout), findViewById(C0490R.id.repeat_layout0), findViewById(C0490R.id.repeat_layout1), findViewById(C0490R.id.repeat_layout2), findViewById(C0490R.id.repeat_layout3)};
        this.mSnoozeDurationViewMap = new HashMap();
        int i = 0;
        for (View snoozeDuration : this.mSnoozeDurationList) {
            this.mSnoozeDurationViewMap.put(snoozeDuration, Integer.valueOf(i));
            i++;
        }
        this.mSnoozeRepeatViewMap = new HashMap();
        i = 0;
        for (View snoozeRepeat : this.mSnoozeRepeatList) {
            this.mSnoozeRepeatViewMap.put(snoozeRepeat, Integer.valueOf(i));
            i++;
        }
        if (savedInstanceState != null) {
            this.mIsSnoozeActive = savedInstanceState.getBoolean("alarm_snooze_active");
            this.mDuration = savedInstanceState.getInt("alarm_snooze_duration");
            this.mRepeat = savedInstanceState.getInt("alarm_snooze_repeat");
        } else {
            Intent intent = getIntent();
            this.mIsSnoozeActive = intent.getBooleanExtra("alarm_snooze_active", true);
            this.mDuration = intent.getIntExtra("alarm_snooze_duration", 1);
            this.mRepeat = intent.getIntExtra("alarm_snooze_repeat", 2);
        }
        Log.secD("AlarmSnoozeActivity", "mIsSnoozeActive : " + this.mIsSnoozeActive + ", " + this.mDuration + ", " + this.mRepeat);
        setClickListener();
        ((CheckedTextView) findViewById(this.mSnoozeDurationList[this.mDuration].getId())).setChecked(true);
        ((CheckedTextView) findViewById(this.mSnoozeRepeatList[this.mRepeat].getId())).setChecked(true);
        addSubAppBarView();
        if (!this.mIsSnoozeActive) {
            setEnableRadioButton(this.mSnoozeDurationList, false);
            setEnableRadioButton(this.mSnoozeRepeatList, false);
        }
        findViewById(C0490R.id.nestedscrollview).setOnTouchListener(new C05201());
    }

    private void addSubAppBarView() {
        ClockSubAppBar subAppBar = (ClockSubAppBar) findViewById(C0490R.id.sub_appbar_layout);
        subAppBar.setSubAppBarPressListener(new C05212());
        subAppBar.setChecked(this.mIsSnoozeActive);
    }

    protected void onResume() {
        super.onResume();
        ClockUtils.insertSaLog("105");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onSaveInstanceState(Bundle outState) {
        Log.secD("AlarmSnoozeActivity", "onSaveInstanceState() / mIsSnoozeActive=" + this.mIsSnoozeActive + "/mDuration =" + this.mDuration + "/mRepeat =" + this.mRepeat);
        outState.putBoolean("alarm_snooze_active", this.mIsSnoozeActive);
        outState.putInt("alarm_snooze_duration", this.mDuration);
        outState.putInt("alarm_snooze_repeat", this.mRepeat);
        super.onSaveInstanceState(outState);
    }

    protected void onDestroy() {
        int i;
        Log.secD("AlarmSnoozeActivity", "onDestroy()");
        for (i = 0; i < this.mSnoozeDurationList.length; i++) {
            this.mSnoozeDurationList[i].setOnClickListener(null);
            this.mSnoozeDurationList[i] = null;
        }
        for (i = 0; i < this.mSnoozeRepeatList.length; i++) {
            this.mSnoozeRepeatList[i].setOnClickListener(null);
            this.mSnoozeRepeatList[i] = null;
        }
        if (this.mSnoozeDurationViewMap != null) {
            this.mSnoozeDurationViewMap.clear();
            this.mSnoozeDurationViewMap = null;
        }
        if (this.mSnoozeRepeatViewMap != null) {
            this.mSnoozeRepeatViewMap.clear();
            this.mSnoozeRepeatViewMap = null;
        }
        super.onDestroy();
    }

    private void initRadioButton(View[] view) {
        for (View aView : view) {
            ((CheckedTextView) findViewById(aView.getId())).setChecked(false);
        }
    }

    private void setEnableRadioButton(View[] view, boolean isEnable) {
        for (View aView : view) {
            findViewById(aView.getId()).setEnabled(isEnable);
            findViewById(aView.getId()).setEnabled(isEnable);
        }
    }

    private void setClickListener() {
        int i = 0;
        for (View snoozeDurationList : this.mSnoozeDurationList) {
            snoozeDurationList.setOnClickListener(new C05223());
        }
        View[] viewArr = this.mSnoozeRepeatList;
        int length = viewArr.length;
        while (i < length) {
            viewArr[i].setOnClickListener(new C05234());
            i++;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (Feature.DEBUG_ENG) {
            Log.secD("AlarmSnoozeActivity", "..onKeyUp.. keyCode : " + keyCode);
        }
        if (keyCode == 168) {
            keyCode = 24;
        } else if (keyCode == 169) {
            keyCode = 25;
        }
        switch (keyCode) {
            case 4:
                sendDataChangedBroadCast();
                finish();
                insertSaLogNavigateUp();
                return true;
            case 24:
            case 25:
            case 82:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != 20 || this.mIsSnoozeActive || getCurrentFocus() == null || getCurrentFocus().getId() != C0490R.id.sub_appbar_switch) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    private void insertSaLogNavigateUp() {
        if (this.mDuration < AlarmItemUtil.ALARM_SNOOZE_DURATION_TABLE.length && this.mDuration >= 0 && this.mRepeat < AlarmItemUtil.ALARM_SNOOZE_COUNT_TABLE.length && this.mRepeat >= 0) {
            int duration = AlarmItemUtil.ALARM_SNOOZE_DURATION_TABLE[this.mDuration];
            int repeat = AlarmItemUtil.ALARM_SNOOZE_COUNT_TABLE[this.mRepeat];
            ClockUtils.insertSaLog("105", "1241", duration + " min / " + (repeat == AlarmItemUtil.CONTINUOUSLY ? "Continuously" : repeat + " times"));
        }
    }

    private void sendDataChangedBroadCast() {
        Log.secD("AlarmSnoozeActivity", "sendDataChangedBroadCast() " + this.mDuration + ", " + this.mRepeat + ", mIsSnoozeActive : " + this.mIsSnoozeActive);
        Intent intent = new Intent();
        intent.putExtra("alarm_snooze_active", this.mIsSnoozeActive);
        intent.putExtra("alarm_snooze_duration", this.mDuration);
        intent.putExtra("alarm_snooze_repeat", this.mRepeat);
        setResult(-1, intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                sendDataChangedBroadCast();
                finish();
                insertSaLogNavigateUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
