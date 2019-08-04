package com.sec.android.app.clockpackage.common.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.RubinStateContract;

public class CustomizationServiceOnActivity extends ClockActivity {
    private final String TAG = "CustomizationServiceOnActivity";
    CardView mCustomizationServiceLayout;
    private String mRubinText;

    /* renamed from: com.sec.android.app.clockpackage.common.activity.CustomizationServiceOnActivity$1 */
    class C06461 implements OnClickListener {
        C06461() {
        }

        public void onClick(View view) {
            CustomizationServiceOnActivity.this.showRubin();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("CustomizationServiceOnActivity", "onCreate");
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, C0645R.style.ClockTheme);
        setContentView(C0645R.layout.customization_service_onoff_settings);
        setSupportActionBar((Toolbar) findViewById(C0645R.id.customisation_service_setting_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RubinStateContract.checkRubinState(getApplicationContext());
        setRubinText();
        this.mCustomizationServiceLayout = (CardView) findViewById(C0645R.id.customizationServiceLayout);
        if (this.mCustomizationServiceLayout != null) {
            this.mCustomizationServiceLayout.setOnClickListener(new C06461());
            ClockUtils.updateListBothSideMargin(this, this.mCustomizationServiceLayout);
        }
    }

    protected void onResume() {
        super.onResume();
        RubinStateContract.checkRubinState(getApplicationContext());
        setRubinText();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                finish();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setRubinText() {
        int stringId = getSummaryTextId(RubinStateContract.getCurrentRubinState(), RubinStateContract.getRubinSupportedAppEnabled());
        if (stringId == C0645R.string.customization_service_using_as) {
            this.mRubinText = getRubinSummaryTextWithAccount(stringId);
        } else {
            this.mRubinText = getApplicationContext().getString(stringId);
        }
        ((TextView) findViewById(C0645R.id.customizationServiceSubText)).setText(this.mRubinText);
    }

    private int getSummaryTextId(String state, boolean enabled) {
        if ("OK".equals(state) || "CRITICAL_UPDATE_NEEDED".equals(state)) {
            return enabled ? C0645R.string.customization_service_using_as : C0645R.string.customization_service_paused;
        } else {
            if ("ACCOUNT_NOT_SIGNED_IN".equals(state) || "USER_NOT_CONSENT_TO_COLLECT_DATA".equals(state)) {
                return C0645R.string.customization_service_unused;
            }
            if ("USER_NOT_ENABLE_RUBIN_IN_DEVICE".equals(state)) {
                return C0645R.string.customization_service_paused;
            }
            return C0645R.string.customization_service_disable_state_summary;
        }
    }

    private void showRubin() {
        int targetPage = 2;
        String currentRubinState = RubinStateContract.getCurrentRubinState();
        if (currentRubinState.equals("ACCOUNT_NOT_SIGNED_IN") || currentRubinState.equals("USER_NOT_CONSENT_TO_COLLECT_DATA") || currentRubinState.equals("CRITICAL_UPDATE_NEEDED") || currentRubinState.equals("USER_NOT_ENABLE_RUBIN_IN_DEVICE")) {
            targetPage = 1;
        }
        Intent intent = new Intent();
        intent.setAction("com.samsung.android.rubin.CS_SETTINGS");
        intent.putExtra("targetPage", targetPage);
        startActivity(intent);
    }

    private String getRubinSummaryTextWithAccount(int stringId) {
        if (AccountManager.get(this).getAccountsByType("com.osp.app.signin").length <= 0) {
            return getApplicationContext().getResources().getString(C0645R.string.customization_service_paused);
        }
        return String.format(getApplicationContext().getResources().getString(stringId), new Object[]{accountArr[0].name});
    }
}
