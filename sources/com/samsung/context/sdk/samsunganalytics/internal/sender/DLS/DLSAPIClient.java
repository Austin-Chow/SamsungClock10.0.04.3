package com.samsung.context.sdk.samsunganalytics.internal.sender.DLS;

import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.internal.connection.API;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskCallback;
import com.samsung.context.sdk.samsunganalytics.internal.executor.AsyncTaskClient;
import com.samsung.context.sdk.samsunganalytics.internal.policy.Validation;
import com.samsung.context.sdk.samsunganalytics.internal.security.CertificateManager;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;
import com.samsung.context.sdk.samsunganalytics.internal.sender.SimpleLog;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

public class DLSAPIClient implements AsyncTaskClient {
    private static final API REALTIME_API = API.SEND_LOG;
    private static final API RTB_API = API.SEND_BUFFERED_LOG;
    private AsyncTaskCallback asyncTaskCallback;
    private HttpsURLConnection conn = null;
    private Boolean isBatch = Boolean.valueOf(false);
    private LogType logType;
    private Queue<SimpleLog> logs;
    private SimpleLog simpleLog;
    private int timeoutInMilliseconds;
    private String trid;

    public DLSAPIClient(SimpleLog simpleLog, String trid, int timeout, AsyncTaskCallback callback) {
        this.simpleLog = simpleLog;
        this.trid = trid;
        this.asyncTaskCallback = callback;
        this.timeoutInMilliseconds = getTimeout(timeout);
        this.logType = simpleLog.getType();
    }

    public DLSAPIClient(LogType logType, Queue<SimpleLog> logs, String trid, int timeout, AsyncTaskCallback callback) {
        this.logs = logs;
        this.trid = trid;
        this.asyncTaskCallback = callback;
        this.isBatch = Boolean.valueOf(true);
        this.timeoutInMilliseconds = getTimeout(timeout);
        this.logType = logType;
    }

    private String getBody() {
        if (!this.isBatch.booleanValue()) {
            return this.simpleLog.getData();
        }
        Iterator<SimpleLog> iterator = this.logs.iterator();
        String body = ((SimpleLog) iterator.next()).getData();
        while (iterator.hasNext()) {
            body = body + "\u000e" + ((SimpleLog) iterator.next()).getData();
        }
        return body;
    }

    public void run() {
        try {
            API api = this.isBatch.booleanValue() ? RTB_API : REALTIME_API;
            Builder builder = Uri.parse(api.getUrl()).buildUpon();
            String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
            builder.appendQueryParameter("ts", date).appendQueryParameter("type", this.logType.getAbbrev()).appendQueryParameter("tid", this.trid).appendQueryParameter("hc", Validation.sha256(this.trid + date + Validation.SALT));
            this.conn = (HttpsURLConnection) new URL(builder.build().toString()).openConnection();
            this.conn.setSSLSocketFactory(CertificateManager.getInstance().getSSLContext().getSocketFactory());
            this.conn.setRequestMethod(api.getMethod());
            this.conn.addRequestProperty("Content-Encoding", this.isBatch.booleanValue() ? "gzip" : "text");
            this.conn.setConnectTimeout(this.timeoutInMilliseconds);
            String body = getBody();
            if (!TextUtils.isEmpty(body)) {
                OutputStream out;
                this.conn.setDoOutput(true);
                if (this.isBatch.booleanValue()) {
                    out = new BufferedOutputStream(new GZIPOutputStream(this.conn.getOutputStream()));
                } else {
                    out = new BufferedOutputStream(this.conn.getOutputStream());
                }
                out.write(body.getBytes());
                out.flush();
                out.close();
            }
            Debug.LogENG("[DLS Client] Send to DLS : " + body);
        } catch (Exception e) {
            Debug.LogE("[DLS Client] Send fail.");
            Debug.LogENG("[DLS Client] " + e.getMessage());
        }
    }

    public int onFinish() {
        int error;
        Exception e;
        Throwable th;
        BufferedReader reader = null;
        try {
            int resCode = this.conn.getResponseCode();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
            try {
                String resMessage = new JSONObject(reader2.readLine()).getString("rc");
                if (resCode == Callback.DEFAULT_DRAG_ANIMATION_DURATION && resMessage.equalsIgnoreCase("1000")) {
                    error = 1;
                    Debug.LogD("[DLS Sender] send result success : " + resCode + " " + resMessage);
                } else {
                    error = -7;
                    Debug.LogD("[DLS Sender] send result fail : " + resCode + " " + resMessage);
                }
                callback(resCode, resMessage);
                cleanUp(reader2);
                reader = reader2;
            } catch (Exception e2) {
                e = e2;
                reader = reader2;
                try {
                    Debug.LogE("[DLS Client] Send fail.");
                    Debug.LogENG("[DLS Client] " + e.getMessage());
                    error = -41;
                    callback(0, "");
                    cleanUp(reader);
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
        } catch (Exception e3) {
            e = e3;
            Debug.LogE("[DLS Client] Send fail.");
            Debug.LogENG("[DLS Client] " + e.getMessage());
            error = -41;
            callback(0, "");
            cleanUp(reader);
            return error;
        }
        return error;
    }

    private void callback(int resCode, String resMessage) {
        if (this.asyncTaskCallback != null) {
            if (resCode != Callback.DEFAULT_DRAG_ANIMATION_DURATION || !resMessage.equalsIgnoreCase("1000")) {
                if (this.isBatch.booleanValue()) {
                    while (!this.logs.isEmpty()) {
                        SimpleLog log = (SimpleLog) this.logs.poll();
                        this.asyncTaskCallback.onFail(resCode, log.getTimestamp() + "", log.getData(), log.getType().getAbbrev());
                    }
                    return;
                }
                this.asyncTaskCallback.onFail(resCode, this.simpleLog.getTimestamp() + "", this.simpleLog.getData(), this.simpleLog.getType().getAbbrev());
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

    private int getTimeout(int timeoutParam) {
        if (timeoutParam == 0) {
            return 3000;
        }
        return timeoutParam > 15000 ? 15000 : timeoutParam;
    }
}
