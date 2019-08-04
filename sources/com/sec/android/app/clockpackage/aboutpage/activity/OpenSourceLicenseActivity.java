package com.sec.android.app.clockpackage.aboutpage.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.sec.android.app.clockpackage.aboutpage.C0482R;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import java.io.IOException;
import java.io.InputStream;

public class OpenSourceLicenseActivity extends ClockActivity {
    private WebView mWebView = null;

    private String readAssetFileAsString(String sourceHtmlLocation) {
        try {
            InputStream is = getAssets().open(sourceHtmlLocation);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWebView = new WebView(this);
        setContentView(this.mWebView);
        if ((getResources().getConfiguration().uiMode & 48) == 32) {
            String noticeString = readAssetFileAsString("text/NOTICE.html");
            this.mWebView.setBackgroundColor(0);
            this.mWebView.loadData("<html><head><style type=\"text/css\">body{color: #fff; background-color: #000;}</style></head><body>" + noticeString + "</body></html>", "text/html", "utf-8");
        } else {
            this.mWebView.loadUrl("file:///android_asset/text/NOTICE.html");
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(C0482R.string.open_source_licences);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        WebSettings settings = this.mWebView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    protected void onDestroy() {
        if (this.mWebView != null) {
            this.mWebView = null;
        }
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}
