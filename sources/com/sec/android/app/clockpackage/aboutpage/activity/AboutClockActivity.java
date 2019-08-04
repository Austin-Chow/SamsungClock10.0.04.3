package com.sec.android.app.clockpackage.aboutpage.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.aboutpage.C0482R;
import com.sec.android.app.clockpackage.aboutpage.update.CheckForUpdates;
import com.sec.android.app.clockpackage.aboutpage.update.CheckForUpdates.StubListener;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.callback.OnSingleClickListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockAddButton;

public class AboutClockActivity extends ClockActivity {
    private final String KEY_UPDATE_STATE = "update_state";
    private final String TAG = "AboutClockActivity";
    private LinearLayout mAboutClockLayout;
    private LinearLayout mAppInfoView;
    private AppBarLayout mAppbarLayout;
    private TextView mAvailableText;
    private int mCheckingStatus = 4;
    private View mEmptyBottom;
    private View mEmptyMiddle;
    private View mEmptyTop;
    private LinearLayout mLowerLayout;
    private Button mOpenSource;
    private Button mOpenSourceInLower;
    private ProgressBar mProgressBar;
    private StubListener mStubListener = new C04831();
    private ClockAddButton mUpdateButton;
    private LinearLayout mUpperLayout;
    private TextView mVersionText;
    private LinearLayout mWebLinkView;

    /* renamed from: com.sec.android.app.clockpackage.aboutpage.activity.AboutClockActivity$1 */
    class C04831 implements StubListener {
        C04831() {
        }

        public void onUpdateCheckCompleted(int result, String verCode) {
            Log.secD("AboutClockActivity", "onUpdateCheckCompleted");
            if (!(result == 3 || result == 4)) {
                saveCheckedVersion(verCode);
            }
            AboutClockActivity.this.checkForUpdatesCompleted(result);
        }

        private void saveCheckedVersion(String verCode) {
            Editor editor = AboutClockActivity.this.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
            editor.putString("marketVersionCode", verCode);
            editor.apply();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.aboutpage.activity.AboutClockActivity$2 */
    class C04842 extends OnSingleClickListener {
        C04842() {
        }

        public void onSingleClick(View v) {
            if (AboutClockActivity.this.mCheckingStatus != 3) {
                CheckForUpdates.jumpToSamsungApps(AboutClockActivity.this);
                ClockUtils.insertSaLog("136", "1361");
            } else if (StateUtils.isNetWorkConnected(AboutClockActivity.this.getApplicationContext())) {
                AboutClockActivity.this.checkForUpdatesNotCompleted();
            } else {
                Toast.makeText(AboutClockActivity.this.getApplicationContext(), AboutClockActivity.this.getString(C0482R.string.no_network_connection), 1).show();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.aboutpage.activity.AboutClockActivity$3 */
    class C04853 extends OnSingleClickListener {
        C04853() {
        }

        public void onSingleClick(View v) {
            Intent intent = new Intent();
            intent.setClassName(AboutClockActivity.this.getApplicationContext(), "com.sec.android.app.clockpackage.aboutpage.activity.OpenSourceLicenseActivity");
            AboutClockActivity.this.startActivity(intent);
            ClockUtils.insertSaLog("136", "1362");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.aboutpage.activity.AboutClockActivity$4 */
    class C04864 extends OnSingleClickListener {
        C04864() {
        }

        public void onSingleClick(View v) {
            Intent intent = new Intent();
            intent.setClassName(AboutClockActivity.this.getApplicationContext(), "com.sec.android.app.clockpackage.aboutpage.activity.OpenSourceLicenseActivity");
            AboutClockActivity.this.startActivity(intent);
            ClockUtils.insertSaLog("136", "1362");
        }
    }

    @SuppressLint({"SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AboutClockActivity", "onCreate()");
        setContentView(C0482R.layout.about_clock);
        setTitle(null);
        this.mAppbarLayout = (AppBarLayout) findViewById(C0482R.id.about_clock_app_bar);
        this.mAboutClockLayout = (LinearLayout) findViewById(C0482R.id.about_clock_layout);
        this.mAppInfoView = (LinearLayout) findViewById(C0482R.id.app_info_layout);
        this.mUpperLayout = (LinearLayout) findViewById(C0482R.id.upper_layout);
        this.mLowerLayout = (LinearLayout) findViewById(C0482R.id.lower_layout);
        this.mWebLinkView = (LinearLayout) findViewById(C0482R.id.web_link_layout);
        this.mEmptyTop = findViewById(C0482R.id.empty_view_top);
        this.mEmptyMiddle = findViewById(C0482R.id.empty_view_middle);
        this.mEmptyBottom = findViewById(C0482R.id.empty_view_bottom);
        this.mProgressBar = (ProgressBar) findViewById(C0482R.id.about_clock_progress);
        this.mAvailableText = (TextView) findViewById(C0482R.id.version_notice);
        this.mUpdateButton = (ClockAddButton) findViewById(C0482R.id.version_update_button);
        this.mUpdateButton.getTextView().setCompoundDrawables(null, null, null, null);
        this.mOpenSource = (Button) findViewById(C0482R.id.open_source_licences);
        this.mOpenSourceInLower = (Button) findViewById(C0482R.id.open_source_licences_in_lower_layout);
        setOpenSourceButtonWidth(this.mOpenSource);
        setOpenSourceButtonWidth(this.mOpenSourceInLower);
        setClickListener();
        String versionInfo = ClockUtils.getVersionInfo(this);
        this.mVersionText = (TextView) findViewById(C0482R.id.version);
        this.mVersionText.setText(getString(C0482R.string.version) + " " + versionInfo);
        if (savedInstanceState != null) {
            Log.secD("AboutClockActivity", "savedInstanceState != null");
            this.mCheckingStatus = savedInstanceState.getInt("update_state", 4);
        }
        checkForUpdatesCompleted(this.mCheckingStatus);
        if (this.mCheckingStatus == 4) {
            checkForUpdatesNotCompleted();
        }
        setOrientationLayout();
        setTextSize();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("update_state", this.mCheckingStatus);
    }

    protected void onResume() {
        super.onResume();
        ClockUtils.insertSaLog("136");
    }

    protected void onDestroy() {
        CheckForUpdates.setCheckForUpdatesListener(this, null, false);
        super.onDestroy();
    }

    private void checkForUpdatesNotCompleted() {
        setCheckingStatus(false);
        CheckForUpdates.setCheckForUpdatesListener(this, this.mStubListener, true);
    }

    private void checkForUpdatesCompleted(int result) {
        this.mCheckingStatus = result;
        setCheckingStatus(true);
        switch (result) {
            case 2:
                this.mAvailableText.setText(getString(C0482R.string.new_version_is_available));
                this.mUpdateButton.setText(getString(C0482R.string.update));
                this.mUpdateButton.setVisibility(0);
                break;
            case 3:
                this.mAvailableText.setText(Feature.isTablet(getApplicationContext()) ? getString(C0482R.string.cant_check_for_updates_tablet) : getString(C0482R.string.cant_check_for_updates_phone));
                this.mUpdateButton.setText(getString(C0482R.string.retry));
                this.mUpdateButton.setVisibility(0);
                break;
            case 4:
                this.mAvailableText.setVisibility(8);
                this.mUpdateButton.setVisibility(8);
                break;
            default:
                this.mAvailableText.setText(getString(C0482R.string.latest_version));
                this.mUpdateButton.setVisibility(8);
                break;
        }
        setOrientationLayout();
    }

    private void setCheckingStatus(boolean isCompleted) {
        int i;
        int i2 = 0;
        ProgressBar progressBar = this.mProgressBar;
        if (isCompleted) {
            i = 8;
        } else {
            i = 0;
        }
        progressBar.setVisibility(i);
        TextView textView = this.mAvailableText;
        if (isCompleted) {
            i = 0;
        } else {
            i = 8;
        }
        textView.setVisibility(i);
        ClockAddButton clockAddButton = this.mUpdateButton;
        if (this.mProgressBar.getVisibility() != 8) {
            i2 = 8;
        }
        clockAddButton.setVisibility(i2);
    }

    private void setClickListener() {
        this.mUpdateButton.setOnClickListener(new C04842());
        this.mOpenSource.setOnClickListener(new C04853());
        this.mOpenSourceInLower.setOnClickListener(new C04864());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0482R.menu.about_clock_menu, menu);
        menu.findItem(C0482R.id.app_info_menu).setShowAsAction(1);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
            ClockUtils.insertSaLog("136", "1241");
            return true;
        }
        if (item.getItemId() == C0482R.id.app_info_menu) {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", "com.sec.android.app.clockpackage", null));
            intent.setFlags(268468224);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        ClockUtils.insertSaLog("136", "1241");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mAppbarLayout.setExpanded(false);
        setOpenSourceButtonWidth(this.mOpenSource);
        setOpenSourceButtonWidth(this.mOpenSourceInLower);
        setOrientationLayout();
    }

    private void setOrientationLayout() {
        LayoutParams appInfoParams = (LayoutParams) this.mAppInfoView.getLayoutParams();
        LayoutParams webLinkParams = (LayoutParams) this.mWebLinkView.getLayoutParams();
        boolean isPortrait = getResources().getConfiguration().orientation == 1;
        double topRatio = isPortrait ? 0.086d : 0.036d;
        double bottomRatio = isPortrait ? 0.05d : 0.036d;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int topViewHeight = (int) (((double) screenHeight) * topRatio);
        int bottomViewHeight = (int) (((double) screenHeight) * bottomRatio);
        ViewGroup.LayoutParams layoutParams = this.mEmptyTop.getLayoutParams();
        this.mEmptyMiddle.getLayoutParams().height = topViewHeight;
        layoutParams.height = topViewHeight;
        this.mEmptyBottom.getLayoutParams().height = bottomViewHeight;
        if (isPortrait) {
            this.mAboutClockLayout.setOrientation(1);
            this.mAppInfoView.setGravity(49);
            this.mLowerLayout.setVisibility(0);
            this.mWebLinkView.setVisibility(8);
            return;
        }
        this.mAboutClockLayout.setOrientation(0);
        appInfoParams.weight = 5.0f;
        webLinkParams.weight = 5.0f;
        this.mAppInfoView.setGravity(17);
        this.mUpperLayout.setGravity(17);
        this.mLowerLayout.setVisibility(8);
        this.mWebLinkView.setVisibility(0);
        this.mWebLinkView.setGravity(17);
    }

    private void setTextSize() {
        Context context = getApplicationContext();
        ClockUtils.setLargeTextSize(context, (TextView) findViewById(C0482R.id.app_name), (float) getResources().getDimensionPixelSize(C0482R.dimen.about_clock_app_name_text_size));
        ClockUtils.setLargeTextSize(context, this.mUpdateButton.getTextView(), (float) getResources().getDimensionPixelSize(C0482R.dimen.clock_update_button_text_size));
        int textSize = getResources().getDimensionPixelSize(C0482R.dimen.about_clock_version_text_size);
        ClockUtils.setLargeTextSize(context, this.mVersionText, (float) textSize);
        ClockUtils.setLargeTextSize(context, this.mAvailableText, (float) textSize);
        textSize = getResources().getDimensionPixelSize(C0482R.dimen.about_clock_open_source_text_size);
        ClockUtils.setLargeTextSize(context, this.mOpenSource, (float) textSize);
        ClockUtils.setLargeTextSize(context, this.mOpenSourceInLower, (float) textSize);
    }

    private void setOpenSourceButtonWidth(Button view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mViewWidth = metrics.widthPixels;
        if (getResources().getConfiguration().orientation == 2) {
            mViewWidth /= 2;
        }
        view.setMaxWidth((int) (((double) mViewWidth) * 0.75d));
        view.setMinWidth((int) (((double) mViewWidth) * 0.61d));
    }
}
