package com.sec.android.app.clockpackage.aboutpage.update;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.TextUtils;
import com.sec.android.app.clockpackage.aboutpage.C0482R;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class CheckForUpdates {
    private static StubListener sStubListener;
    private static String sVersionCode = null;

    public interface StubListener {
        void onUpdateCheckCompleted(int i, String str);
    }

    /* renamed from: com.sec.android.app.clockpackage.aboutpage.update.CheckForUpdates$1 */
    static class C04871 implements OnClickListener {
        C04871() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    private static class CheckForUpdatesTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Context> mContext;

        CheckForUpdatesTask(Context context) {
            this.mContext = new WeakReference(context);
        }

        private void run() {
            Log.secD("CheckForUpdates", "CheckForUpdatesTask run");
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

        protected Integer doInBackground(Void... arg0) {
            Context context = (Context) this.mContext.get();
            if (context == null) {
                return Integer.valueOf(4);
            }
            return Integer.valueOf(CheckForUpdates.check(context));
        }

        protected void onPostExecute(Integer result) {
            if (CheckForUpdates.sStubListener != null) {
                CheckForUpdates.sStubListener.onUpdateCheckCompleted(result.intValue(), CheckForUpdates.sVersionCode);
            }
        }
    }

    public static void setCheckForUpdatesListener(Activity activity, StubListener stubListener, boolean forceUpdate) {
        Log.secD("CheckForUpdates", "setCheckForUpdateListener");
        sStubListener = stubListener;
        if (StateUtils.isUltraPowerSavingMode(activity)) {
            Log.secD("CheckForUpdates", "setCheckForUpdateListener isUltraPowerSavingMode");
            if (sStubListener != null) {
                sStubListener.onUpdateCheckCompleted(4, null);
                return;
            }
            return;
        }
        boolean needCheckForUpdates = false;
        SharedPreferences pref = activity.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
        String firstEnteranceTimeKey = "firstEnteranceTime";
        long firstEnteranceTime = pref.getLong(firstEnteranceTimeKey, 0);
        if (firstEnteranceTime > 0 && System.currentTimeMillis() - firstEnteranceTime > 86400000) {
            needCheckForUpdates = true;
            pref.edit().putLong(firstEnteranceTimeKey, System.currentTimeMillis()).apply();
        }
        Log.secD("CheckForUpdates", "needCheckForUpdates : " + needCheckForUpdates);
        if (!needCheckForUpdates && !forceUpdate) {
            return;
        }
        if (hasNetworkPermission(activity)) {
            checkForUpdates(activity.getApplicationContext());
            return;
        }
        Log.secD("CheckForUpdates", "setCheckForUpdateListener !hasNetworkPermission : doesn't have network permission");
        if (sStubListener != null) {
            sStubListener.onUpdateCheckCompleted(4, null);
        }
    }

    private static boolean hasNetworkPermission(Context context) {
        if (!Feature.isSupportChinaPresetTimer()) {
            Log.secD("CheckForUpdates", "not china");
            return true;
        } else if (context == null) {
            return true;
        } else {
            if ("OK".equals(context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getString("usingNetwork", null))) {
                return true;
            }
            showDialogForLegalNotice(context);
            return false;
        }
    }

    private static void showDialogForLegalNotice(final Context context) {
        Builder builder = new Builder(context);
        final SharedPreferences pref = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
        builder.setMessage(String.format(context.getString(C0482R.string.legal_notice_contents), new Object[]{context.getString(C0482R.string.app_name)})).setPositiveButton(C0482R.string.allow, new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pref.edit().putString("usingNetwork", "OK").apply();
                Log.secD("CheckForUpdates", "use network connection OK");
                dialog.dismiss();
                CheckForUpdates.checkForUpdates(context);
            }
        }).setNegativeButton(C0482R.string.deny, new C04871());
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                pref.edit().putString("usingNetwork", "CANCEL").apply();
                Log.secD("CheckForUpdates", "use network connection cancel");
            }
        });
        builder.create().show();
    }

    private static void checkForUpdates(Context context) {
        new CheckForUpdatesTask(context).run();
    }

    private static int check(Context context) {
        String szModel = Build.MODEL;
        String szPrefix = "SAMSUNG-";
        String requestUrl = "http://vas.samsungapps.com/stub/stubUpdateCheck.as";
        String mcc = TelephonyManagerUtils.getMcc(context);
        String mccOfChina = "460";
        if (szModel.contains(szPrefix)) {
            szModel = szModel.replaceFirst(szPrefix, "");
        }
        if ("460".equals(mcc)) {
            SharedPreferences pref = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
            String cnVasUrl = pref.getString("cnVasURL", null);
            long cnVasTime = pref.getLong("cnVasTime", 0);
            if (TextUtils.isEmpty(cnVasUrl) || System.currentTimeMillis() - cnVasTime > 172800000) {
                cnVasUrl = getChinaVasUrl();
                if (TextUtils.isEmpty(cnVasUrl) && StateUtils.isNetWorkConnected(context)) {
                    Log.secD("CheckForUpdates", "Network is connected. But cnVasUrl got nothing from server");
                    return 0;
                }
                Editor editor = pref.edit();
                editor.putString("cnVasURL", cnVasUrl);
                editor.putLong("cnVasTime", System.currentTimeMillis());
                editor.apply();
                Log.m41d("CheckForUpdates", cnVasUrl);
            }
            requestUrl = "http://" + cnVasUrl + "/stub/stubUpdateCheck.as";
            Log.secD("CheckForUpdates", requestUrl);
        }
        try {
            String packageName = context.getPackageName();
            int versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
            Uri.Builder builder = Uri.parse(requestUrl).buildUpon();
            builder.appendQueryParameter("appId", packageName).appendQueryParameter("callerId", packageName).appendQueryParameter("versionCode", String.valueOf(versionCode)).appendQueryParameter("deviceId", szModel).appendQueryParameter("mcc", mcc).appendQueryParameter("mnc", TelephonyManagerUtils.getMnc(context)).appendQueryParameter("csc", TargetInfo.getCsc()).appendQueryParameter("sdkVer", String.valueOf(VERSION.SDK_INT)).appendQueryParameter("pd", TargetInfo.getPd());
            requestUrl = builder.toString();
            Log.m41d("CheckForUpdates", requestUrl);
            return getResult(new URL(requestUrl));
        } catch (Exception e) {
            Log.m42e("CheckForUpdates", e.toString());
            return 3;
        }
    }

    private static String getChinaVasUrl() {
        String cnVasUrl = "";
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("https://cn-ms.samsungapps.com/getCNVasURL.as").openConnection();
            if (Callback.DEFAULT_DRAG_ANIMATION_DURATION != con.getResponseCode()) {
                throw new IOException("status code" + con.getResponseCode() + "!=" + Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            }
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                builder.append(line);
            }
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(builder.toString()));
            for (int parserEvent = parser.getEventType(); parserEvent != 1; parserEvent = parser.next()) {
                if (parserEvent == 2) {
                    if ("serverURL".equalsIgnoreCase(parser.getName())) {
                        cnVasUrl = parser.nextText();
                        Log.secD("CheckForUpdates", "cnVasUrl = " + cnVasUrl);
                    }
                }
            }
            if (con != null) {
                con.disconnect();
            }
            return cnVasUrl;
        } catch (Exception e) {
            Log.m42e("CheckForUpdates", e.toString());
            return cnVasUrl;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private static int getResult(URL url) {
        Exception e;
        Throwable th;
        int ret = 3;
        InputStream inputStream = null;
        HttpURLConnection con = null;
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            con = (HttpURLConnection) url.openConnection();
            if (Callback.DEFAULT_DRAG_ANIMATION_DURATION != con.getResponseCode()) {
                throw new IOException("status code" + con.getResponseCode() + "!=" + Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            }
            InputStream inputStream2 = new BufferedInputStream(con.getInputStream());
            try {
                parser.setInput(inputStream2, null);
                int resultCodeInt = versionCodeParser(parser);
                Log.secD("CheckForUpdates", "resultCodeInt = " + resultCodeInt);
                if (2 == resultCodeInt) {
                    ret = 2;
                } else if (resultCodeInt == 0) {
                    ret = 0;
                } else if (1 == resultCodeInt) {
                    ret = 1;
                } else if (1000 == resultCodeInt) {
                    ret = 1000;
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (IOException e2) {
                        Log.m42e("CheckForUpdates", e2.toString());
                    }
                    con.disconnect();
                    inputStream = inputStream2;
                }
            } catch (Exception e3) {
                e = e3;
                inputStream = inputStream2;
                try {
                    Log.m42e("CheckForUpdates", e.toString());
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e22) {
                            Log.m42e("CheckForUpdates", e22.toString());
                        }
                        con.disconnect();
                    }
                    return ret;
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e222) {
                            Log.m42e("CheckForUpdates", e222.toString());
                        }
                        con.disconnect();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = inputStream2;
                if (inputStream != null) {
                    inputStream.close();
                    con.disconnect();
                }
                throw th;
            }
            return ret;
        } catch (Exception e4) {
            e = e4;
            Log.m42e("CheckForUpdates", e.toString());
            if (inputStream != null) {
                inputStream.close();
                con.disconnect();
            }
            return ret;
        }
    }

    private static int versionCodeParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        int parserEvent = parser.getEventType();
        String resultCode = null;
        while (parserEvent != 1) {
            if (parserEvent == 2) {
                if ("resultCode".equals(parser.getName())) {
                    if (parser.next() == 4) {
                        resultCode = parser.getText();
                    }
                } else if ("versionCode".equals(parser.getName()) && parser.next() == 4) {
                    sVersionCode = parser.getText();
                }
            }
            parserEvent = parser.next();
        }
        return Integer.parseInt(resultCode);
    }

    public static void jumpToSamsungApps(Context context) {
        if (context == null) {
            Log.secD("CheckForUpdates", "jumpToSamsungApps - context is null");
            return;
        }
        Intent intent = new Intent();
        intent.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        intent.putExtra("directcall", true);
        intent.putExtra("CallerType", 1);
        intent.putExtra("GUID", context.getPackageName());
        intent.addFlags(335544352);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.m42e("CheckForUpdates", e.toString());
            }
        }
    }
}
