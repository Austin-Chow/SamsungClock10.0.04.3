package com.samsung.context.sdk.samsunganalytics.internal.terms;

import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.internal.connection.API;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.security.CertificateManager;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTask implements AsyncTaskClient {
    private final API api = API.DATA_DELETE;
    private AsyncTaskCallback callback;
    private HttpsURLConnection conn = null;
    private String deviceID = "";
    private long timestamp;
    private String trid = "";

    public RegisterTask(String trid, String deviceID, long timestamp, AsyncTaskCallback callback) {
        this.trid = trid;
        this.deviceID = deviceID;
        this.timestamp = timestamp;
        this.callback = callback;
    }

    private String makeRequestBody() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("tid", this.trid);
            obj.put("lid", this.deviceID);
            obj.put("ts", this.timestamp);
        } catch (JSONException e) {
        }
        return obj.toString();
    }

    public void run() {
        try {
            Builder builder = Uri.parse(this.api.getUrl()).buildUpon();
            String date = SimpleDateFormat.getTimeInstance(2).format(new Date());
            builder.appendQueryParameter("ts", date).appendQueryParameter("hc", Validation.sha256(date + Validation.SALT));
            this.conn = (HttpsURLConnection) new URL(builder.build().toString()).openConnection();
            this.conn.setSSLSocketFactory(CertificateManager.getInstance().getSSLContext().getSocketFactory());
            this.conn.setRequestMethod(this.api.getMethod());
            this.conn.setConnectTimeout(3000);
            this.conn.setRequestProperty("Content-Type", "application/json");
            String body = makeRequestBody();
            if (!TextUtils.isEmpty(body)) {
                this.conn.setDoOutput(true);
                OutputStream out = new BufferedOutputStream(this.conn.getOutputStream());
                out.write(body.getBytes());
                out.flush();
                out.close();
            }
        } catch (Exception e) {
        }
    }

    public int onFinish() {
        BufferedReader reader = null;
        try {
            int resCode = this.conn.getResponseCode();
            if (resCode >= 400) {
                reader = new BufferedReader(new InputStreamReader(this.conn.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            }
            String resMessage = new JSONObject(reader.readLine()).getString("rc");
            if (resCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION && resMessage.equalsIgnoreCase("1000")) {
                Debug.LogENG("Success : " + resCode + " " + resMessage);
            } else {
                Debug.LogENG("Fail : " + resCode + " " + resMessage);
            }
            callback(resCode, resMessage);
        } catch (Exception e) {
            callback(0, "");
        } finally {
            cleanUp(reader);
        }
        return 0;
    }

    private void callback(int resCode, String resMessage) {
        if (this.callback != null) {
            if (resCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION && resMessage.equalsIgnoreCase("1000")) {
                this.callback.onSuccess(0, "", "", "");
            } else {
                this.callback.onFail(resCode, resMessage, "", "");
            }
        }
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
}
