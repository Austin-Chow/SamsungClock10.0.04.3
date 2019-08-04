package com.sec.android.app.clockpackage.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar.SubAppBarPressListener;
import java.util.ArrayList;

public class AlarmHolidayActivity extends ClockActivity implements OnItemSelectedListener {
    private final String TAG = "AlarmHolidayActivity";
    private int mCheckedPos = -1;
    private boolean mIsHolidayOn;
    private boolean mIsSubstituteHolidayOn;
    private AlarmHolidayListAdapter mListAdapter;
    private final OnItemClickListener mListClickListener = new C05151();
    private ListView mListView;
    private ClockSubAppBar mSubAppBar;
    private final ArrayList<String> mSubstituteHolidayList = new ArrayList();

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmHolidayActivity$1 */
    class C05151 implements OnItemClickListener {
        C05151() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            boolean z = true;
            Log.secD("AlarmHolidayActivity", position + " item clicked(patternType) : =  " + position);
            AlarmHolidayActivity.this.mCheckedPos = position;
            AlarmHolidayActivity.this.mListView.setItemChecked(AlarmHolidayActivity.this.mCheckedPos, true);
            AlarmHolidayActivity alarmHolidayActivity = AlarmHolidayActivity.this;
            if (AlarmHolidayActivity.this.mCheckedPos != 0) {
                z = false;
            }
            alarmHolidayActivity.mIsSubstituteHolidayOn = z;
            ClockUtils.insertSaLog("610", "6101", AlarmHolidayActivity.this.mListView.getAdapter().getItem(position).toString());
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmHolidayActivity$2 */
    class C05162 implements SubAppBarPressListener {
        C05162() {
        }

        public void setChecked(boolean isChecked) {
            AlarmHolidayActivity.this.mIsHolidayOn = isChecked;
            Log.secD("AlarmHolidayActivity", "mIsHolidayOn : " + isChecked);
            AlarmHolidayActivity.this.mListView.setEnabled(isChecked);
            AlarmHolidayActivity.this.mListAdapter.notifyDataSetInvalidated();
            ClockUtils.insertSaLog("610", "6001", isChecked ? 1 : 0);
        }
    }

    private class AlarmHolidayListAdapter extends ArrayAdapter<String> {
        ViewHolder mHolder;

        AlarmHolidayListAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = AlarmHolidayActivity.this.getLayoutInflater().inflate(C0490R.layout.tw_simple_list_item_single_choice, parent, false);
                this.mHolder = new ViewHolder();
                this.mHolder.mCheckedTextView = (CheckedTextView) convertView.findViewById(16908308);
                convertView.setTag(this.mHolder);
            } else {
                this.mHolder = (ViewHolder) convertView.getTag();
            }
            this.mHolder.mCheckedTextView.setText((CharSequence) getItem(position));
            this.mHolder.mCheckedTextView.setEnabled(AlarmHolidayActivity.this.mIsHolidayOn);
            return convertView;
        }
    }

    private static class ViewHolder {
        private CheckedTextView mCheckedTextView;

        private ViewHolder() {
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.secD("AlarmHolidayActivity", "onItemSelected position : " + position);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    protected void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        Log.secD("AlarmHolidayActivity", "onCreate");
        setContentView(C0490R.layout.alarm_holiday_layout);
        Intent intent = getIntent();
        this.mIsHolidayOn = intent.getBooleanExtra("alarm_holiday_active", false);
        this.mIsSubstituteHolidayOn = intent.getBooleanExtra("alarm_substitute_holiday", false);
        this.mListView = (ListView) findViewById(C0490R.id.list);
        try {
            this.mListView.semSetRoundedCorners(15);
            this.mListView.semSetRoundedCornerColor(15, getColor(C0490R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmHolidayActivity", "NoSuchMethodError : " + e);
        }
        this.mSubstituteHolidayList.add(getResources().getString(C0490R.string.alarm_include_substitute));
        this.mSubstituteHolidayList.add(getResources().getString(C0490R.string.alarm_exclude_substitute));
        this.mListAdapter = new AlarmHolidayListAdapter(getApplicationContext(), 0, this.mSubstituteHolidayList);
        this.mListView.setAdapter(this.mListAdapter);
        this.mListView.setOnItemSelectedListener(this);
        this.mListView.setOnItemClickListener(this.mListClickListener);
        if (this.mIsSubstituteHolidayOn) {
            i = 0;
        } else {
            i = 1;
        }
        this.mCheckedPos = i;
        this.mListView.setItemChecked(this.mCheckedPos, true);
        this.mListView.setSelection(this.mCheckedPos);
        this.mListView.setEnabled(this.mIsHolidayOn);
        addSubAppBarView();
    }

    private void addSubAppBarView() {
        this.mSubAppBar = (ClockSubAppBar) findViewById(C0490R.id.sub_appbar_layout);
        this.mSubAppBar.setSubAppBarPressListener(new C05162());
        this.mSubAppBar.setChecked(this.mIsHolidayOn);
    }

    protected void onResume() {
        super.onResume();
        ClockUtils.insertSaLog("610");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        sendDataChangedBroadCast();
        finish();
        return true;
    }

    public void onBackPressed() {
        sendDataChangedBroadCast();
        finish();
        super.onBackPressed();
    }

    protected void onDestroy() {
        Log.secD("AlarmHolidayActivity", "onDestroy()");
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean("alarm_holiday_active", this.mIsHolidayOn);
        state.putBoolean("alarm_substitute_holiday", this.mIsSubstituteHolidayOn);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        int i = 0;
        super.onRestoreInstanceState(savedInstanceState);
        this.mIsHolidayOn = savedInstanceState.getBoolean("alarm_holiday_active", false);
        this.mIsSubstituteHolidayOn = savedInstanceState.getBoolean("alarm_substitute_holiday", false);
        this.mSubAppBar.setChecked(this.mIsHolidayOn);
        if (!this.mIsSubstituteHolidayOn) {
            i = 1;
        }
        this.mCheckedPos = i;
        this.mListView.setItemChecked(this.mCheckedPos, true);
        this.mListView.setSelection(this.mCheckedPos);
        this.mListView.setEnabled(this.mIsHolidayOn);
    }

    private void sendDataChangedBroadCast() {
        boolean z = false;
        if (this.mSubstituteHolidayList.isEmpty()) {
            setResult(0);
        }
        Log.secE("AlarmHolidayActivity", "sendDataChangedBroadCast :" + this.mIsHolidayOn + ", " + this.mCheckedPos);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("alarm_holiday_active", this.mIsHolidayOn);
        if (this.mCheckedPos == 0) {
            z = true;
        }
        this.mIsSubstituteHolidayOn = z;
        resultIntent.putExtra("alarm_substitute_holiday", this.mIsSubstituteHolidayOn);
        setResult(-1, resultIntent);
    }
}
