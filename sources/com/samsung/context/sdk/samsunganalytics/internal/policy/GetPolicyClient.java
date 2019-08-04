package com.samsung.context.sdk.samsunganalytics.internal.policy;

import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.connection.API;
import com.samsung.context.sdk.samsunganalytics.internal.connection.Directory;
import com.samsung.context.sdk.samsunganalytics.internal.connection.Domain;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.security.CertificateManager;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class GetPolicyClient implements AsyncTaskClient {
    private API api;
    private Callback<Void, Boolean> callback;
    private HttpsURLConnection conn = null;
    private SharedPreferences pref;
    private Map<String, String> qParams;
    private String trid;

    public GetPolicyClient(API api, String trid, Map<String, String> qParams, SharedPreferences pref, Callback<Void, Boolean> callback) {
        this.trid = trid;
        this.api = api;
        this.qParams = qParams;
        this.pref = pref;
        this.callback = callback;
    }

    public void run() {
        try {
            Builder builder = Uri.parse(this.api.getUrl()).buildUpon();
            for (String key : this.qParams.keySet()) {
                builder.appendQueryParameter(key, (String) this.qParams.get(key));
            }
            String date = SimpleDateFormat.getTimeInstance(2, Locale.US).format(new Date());
            builder.appendQueryParameter("ts", date).appendQueryParameter("tid", this.trid).appendQueryParameter("hc", Validation.sha256(this.trid + date + Validation.SALT));
            String csc = PolicyUtils.getCSC();
            if (!TextUtils.isEmpty(csc)) {
                builder.appendQueryParameter("csc", csc);
            }
            this.conn = (HttpsURLConnection) new URL(builder.build().toString()).openConnection();
            this.conn.setSSLSocketFactory(CertificateManager.getInstance().getSSLContext().getSocketFactory());
            this.conn.setRequestMethod(this.api.getMethod());
            this.conn.setConnectTimeout(3000);
        } catch (Exception e) {
            Debug.LogE("Fail to get Policy");
        }
    }

    public int onFinish() {
        boolean isFirstTry;
        Throwable th;
        int error = 0;
        BufferedReader reader = null;
        try {
            if (this.conn.getResponseCode() != ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                Debug.LogE("Fail to get Policy. Response code : " + this.conn.getResponseCode());
                error = -61;
            }
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            try {
                String jsonResponse = reader2.readLine();
                Debug.LogENG(jsonResponse);
                JSONObject obj = new JSONObject(jsonResponse);
                int rc = obj.getInt("rc");
                if (rc != 1000) {
                    Debug.LogE("Fail to get Policy; Invalid Message. Result code : " + rc);
                    error = -61;
                } else {
                    Debug.LogD("GetPolicyClient", "Get Policy Success");
                    if (TextUtils.isEmpty(this.pref.getString("lgt", "")) && this.callback != null) {
                        String logtype = obj.getString("lgt");
                        if (logtype != null && logtype.equals("rtb")) {
                            this.callback.onResult(Boolean.valueOf(true));
                        }
                    }
                    save(obj);
                }
                if (this.conn != null) {
                    this.conn.disconnect();
                }
                cleanUp(reader2);
                reader = reader2;
            } catch (Exception e) {
                reader = reader2;
                try {
                    Debug.LogE("Fail to get Policy");
                    error = -61;
                    cleanUp(reader);
                    isFirstTry = TextUtils.isEmpty(this.pref.getString("dom", ""));
                    this.pref.edit().putLong("policy_received_date", System.currentTimeMillis()).apply();
                    return error;
                } catch (Throwable th2) {
                    th = th2;
                    cleanUp(reader);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                reader = reader2;
                cleanUp(reader);
                throw th;
            }
        } catch (Exception e2) {
            Debug.LogE("Fail to get Policy");
            error = -61;
            cleanUp(reader);
            isFirstTry = TextUtils.isEmpty(this.pref.getString("dom", ""));
            this.pref.edit().putLong("policy_received_date", System.currentTimeMillis()).apply();
            return error;
        }
        isFirstTry = TextUtils.isEmpty(this.pref.getString("dom", ""));
        if (error == -61 && !isFirstTry) {
            this.pref.edit().putLong("policy_received_date", System.currentTimeMillis()).apply();
        }
        return error;
    }

    private void cleanUp(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                return;
            }
        }
        if (this.conn != null) {
            this.conn.disconnect();
        }
    }

    public void save(JSONObject obj) {
        try {
            this.pref.edit().putInt("oq-3g", obj.getInt("oq-3g") * 1024).putInt("dq-3g", obj.getInt("dq-3g") * 1024).putInt("oq-w", obj.getInt("oq-w") * 1024).putInt("dq-w", obj.getInt("dq-w") * 1024).putString("dom", "https://" + obj.getString("dom")).putString("uri", obj.getString("uri")).putString("bat-uri", obj.getString("bat-uri")).putString("lgt", obj.getString("lgt")).putInt("rint", obj.getInt("rint")).putLong("policy_received_date", System.currentTimeMillis()).apply();
            Domain.DLS.setDomain("https://" + obj.getString("dom"));
            Directory.DLS_DIR.setDirectory(obj.getString("uri"));
            Directory.DLS_DIR_BAT.setDirectory(obj.getString("bat-uri"));
            Debug.LogENG("dq-3g: " + (obj.getInt("dq-3g") * 1024) + ", dq-w: " + (obj.getInt("dq-w") * 1024) + ", oq-3g: " + (obj.getInt("oq-3g") * 1024) + ", oq-w: " + (obj.getInt("oq-w") * 1024));
        } catch (JSONException e) {
            Debug.LogE("Fail to get Policy");
            Debug.LogENG("[GetPolicyClient] " + e.getMessage());
        }
    }
}
