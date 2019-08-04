package com.sec.android.app.clockpackage.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.VibrationEffect.SemMagnitudeType;
import android.os.Vibrator;
import android.os.Vibrator.SemMagnitudeTypes;
import android.view.KeyEvent;
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
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar.SubAppBarPressListener;
import java.util.ArrayList;

public class AlarmVibPatternActivity extends ClockActivity implements OnItemSelectedListener {
    private static final Uri VIBRATION_URI = Uri.parse("content://com.android.settings.personalvibration.PersonalVibrationProvider");
    private final String TAG = "AlarmVibPatternActivity";
    private final ArrayList<String> mAlarmPatternList = new ArrayList();
    private final ArrayList<Integer> mAlarmPatternType = new ArrayList();
    private int mAlarmType;
    private int mCheckedPattern = 50035;
    private int mCheckedPos = -1;
    private boolean mIsTalkBackEnabled = false;
    private boolean mIsVibrationOn = true;
    private AlarmPatternListAdapter mListAdapter;
    private ListView mListView;
    private final OnItemClickListener mRingtoneClickListener = new C05341();
    private Vibrator mVibrator;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmVibPatternActivity$1 */
    class C05341 implements OnItemClickListener {
        C05341() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            AlarmVibPatternActivity.this.mCheckedPattern = ((Integer) AlarmVibPatternActivity.this.mAlarmPatternType.get(position)).intValue();
            Log.secD("AlarmVibPatternActivity", "onItemClick() / position = " + position + "/ mCheckedPattern = " + AlarmVibPatternActivity.this.mCheckedPattern);
            AlarmVibPatternActivity.this.doVibrate(position);
            AlarmVibPatternActivity.this.mListView.setItemChecked(position, true);
            ClockUtils.insertSaLog("106", "1051");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmVibPatternActivity$2 */
    class C05352 implements SubAppBarPressListener {
        C05352() {
        }

        public void setChecked(boolean isChecked) {
            AlarmVibPatternActivity.this.mIsVibrationOn = isChecked;
            Log.secD("AlarmVibPatternActivity", "mVibrationOn : " + isChecked);
            if (isChecked) {
                AlarmVibPatternActivity.this.mAlarmType = 2;
            } else {
                AlarmVibPatternActivity.this.mAlarmType = 0;
            }
            AlarmVibPatternActivity.this.mListView.setEnabled(isChecked);
            AlarmVibPatternActivity.this.mListAdapter.notifyDataSetInvalidated();
            if (AlarmVibPatternActivity.this.semIsResumed()) {
                ClockUtils.insertSaLog("106", "1050", isChecked ? "1" : "0");
            }
        }
    }

    private class AlarmPatternListAdapter extends ArrayAdapter<String> {
        Context mContext;
        ViewHolder mHolder;

        private AlarmPatternListAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.mContext = null;
            this.mContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = AlarmVibPatternActivity.this.getLayoutInflater().inflate(C0490R.layout.tw_simple_list_item_single_choice, parent, false);
                this.mHolder = new ViewHolder();
                this.mHolder.mCheckedTextView = (CheckedTextView) convertView.findViewById(16908308);
                convertView.setTag(this.mHolder);
            } else {
                this.mHolder = (ViewHolder) convertView.getTag();
            }
            this.mHolder.mCheckedTextView.setText((CharSequence) getItem(position));
            this.mHolder.mCheckedTextView.setEnabled(AlarmVibPatternActivity.this.mIsVibrationOn);
            ClockUtils.setAccessibilityFontSize(this.mContext, this.mHolder.mCheckedTextView);
            if (AlarmVibPatternActivity.this.mIsTalkBackEnabled) {
                boolean z;
                if (AlarmVibPatternActivity.this.mIsVibrationOn) {
                    z = false;
                } else {
                    z = true;
                }
                convertView.setClickable(z);
            } else {
                convertView.setClickable(false);
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        private CheckedTextView mCheckedTextView;

        private ViewHolder() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlarmVibPatternActivity", "onCreate");
        if (savedInstanceState != null) {
            this.mCheckedPattern = savedInstanceState.getInt("vibration_pattern");
            this.mAlarmType = savedInstanceState.getInt("alarm_type");
        } else {
            Intent intent = getIntent();
            this.mCheckedPattern = intent.getIntExtra("vibration_pattern", 50035);
            this.mAlarmType = intent.getIntExtra("alarm_type", 2);
        }
        boolean z = this.mAlarmType == 2 || this.mAlarmType == 1;
        this.mIsVibrationOn = z;
        setContentView(C0490R.layout.vibration_pattern);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mListView = (ListView) findViewById(C0490R.id.list);
        try {
            findViewById(C0490R.id.list).semSetRoundedCorners(15);
            findViewById(C0490R.id.list).semSetRoundedCornerColor(15, getColor(C0490R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmVibPatternActivity", "NoSuchMethodError : " + e);
        }
        if (!(StateUtils.isContextInDexMode(this) || StateUtils.isCustomTheme(this))) {
            findViewById(C0490R.id.list).setBackground(getDrawable(C0490R.drawable.contents_area_background));
        }
        Cursor vibCursor = getContentResolver().query(VIBRATION_URI, new String[]{"vibration_name", "vibration_pattern"}, null, null, null);
        if (vibCursor != null) {
            vibCursor.moveToFirst();
            while (vibCursor.getPosition() < vibCursor.getCount()) {
                this.mAlarmPatternList.add(vibCursor.getString(vibCursor.getColumnIndex("vibration_name")));
                int mVibrationPattern = vibCursor.getInt(vibCursor.getColumnIndex("vibration_pattern"));
                this.mAlarmPatternType.add(Integer.valueOf(mVibrationPattern));
                if (this.mCheckedPattern == mVibrationPattern) {
                    this.mCheckedPos = vibCursor.getPosition();
                }
                vibCursor.moveToNext();
            }
            vibCursor.close();
        }
        this.mListAdapter = new AlarmPatternListAdapter(this, 0, this.mAlarmPatternList);
        this.mListView.setAdapter(this.mListAdapter);
        this.mListView.setOnItemSelectedListener(this);
        this.mListView.setOnItemClickListener(this.mRingtoneClickListener);
        this.mListView.setItemChecked(this.mCheckedPos, true);
        this.mListView.setSelection(this.mCheckedPos);
        this.mListView.setEnabled(this.mIsVibrationOn);
        addSubAppBarView();
        setDivider();
    }

    private void addSubAppBarView() {
        ClockSubAppBar subAppBar = (ClockSubAppBar) findViewById(C0490R.id.sub_appbar_layout);
        subAppBar.setSubAppBarPressListener(new C05352());
        subAppBar.setChecked(this.mIsVibrationOn);
    }

    private void setDivider() {
        InsetDrawable listDivider;
        int dividerInset = getResources().getDimensionPixelSize(C0490R.dimen.alarm_checkbox_inset_for_divider);
        Drawable divider = this.mListView.getDivider();
        if (StateUtils.isRtl()) {
            listDivider = new InsetDrawable(divider, 0, 0, dividerInset, 0);
        } else {
            InsetDrawable insetDrawable = new InsetDrawable(divider, dividerInset, 0, 0, 0);
        }
        this.mListView.setDivider(listDivider);
    }

    protected void onResume() {
        super.onResume();
        this.mIsTalkBackEnabled = StateUtils.isTalkBackEnabled(getApplicationContext());
        this.mListAdapter.notifyDataSetInvalidated();
        ClockUtils.insertSaLog("106");
    }

    protected void onSaveInstanceState(Bundle outState) {
        Log.secD("AlarmVibPatternActivity", "onSaveInstanceState() / mAlarmType=" + this.mAlarmType + "/mCheckedPattern =" + this.mCheckedPattern);
        outState.putInt("vibration_pattern", this.mCheckedPattern);
        outState.putInt("alarm_type", this.mAlarmType);
        super.onSaveInstanceState(outState);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mAlarmPatternType.size() == 0) {
            return true;
        }
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 20:
                    if (this.mIsVibrationOn && !this.mListView.hasFocus() && getCurrentFocus() != null && getCurrentFocus().getId() == C0490R.id.sub_appbar_switch) {
                        doVibrate(0);
                        break;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        Log.secD("AlarmVibPatternActivity", "onItemSelected() / position = " + position + "/ mListView.hasFocus = " + this.mListView.hasFocus());
        if (this.mListView.hasFocus() && this.mIsVibrationOn) {
            doVibrate(position);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        this.mVibrator.cancel();
        sendDataChangedBroadCast();
        finish();
        insertSaLogNavigateUp();
        return true;
    }

    public void onBackPressed() {
        this.mVibrator.cancel();
        sendDataChangedBroadCast();
        finish();
        insertSaLogNavigateUp();
        super.onBackPressed();
    }

    private void insertSaLogNavigateUp() {
        if (this.mListView.getCheckedItemPosition() != -1) {
            ClockUtils.insertSaLog("106", "1241", (String) this.mAlarmPatternList.get(this.mListView.getCheckedItemPosition()));
        }
    }

    protected void onDestroy() {
        Log.secD("AlarmVibPatternActivity", "onDestroy()");
        super.onDestroy();
    }

    private void sendDataChangedBroadCast() {
        if (this.mAlarmPatternList.isEmpty()) {
            setResult(0);
        }
        Log.secE("AlarmVibPatternActivity", "sendDataChangedBroadCast :" + this.mCheckedPattern);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("vibration_pattern", this.mCheckedPattern);
        resultIntent.putExtra("alarm_type", this.mAlarmType);
        setResult(-1, resultIntent);
    }

    private void doVibrate(int position) {
        Log.m41d("AlarmVibPatternActivity", "doVibrate() / position = " + position + "/ patternType = " + this.mAlarmPatternType.get(position));
        this.mVibrator.cancel();
        if (VERSION.SEM_INT >= 2701) {
            this.mVibrator.vibrate(VibrationEffect.semCreateWaveform(((Integer) this.mAlarmPatternType.get(position)).intValue(), -1, SemMagnitudeType.TYPE_MAX));
        } else {
            this.mVibrator.semVibrate(((Integer) this.mAlarmPatternType.get(position)).intValue(), -1, null, SemMagnitudeTypes.TYPE_MAX);
        }
    }
}
