package com.sec.android.app.clockpackage.worldclock.weather;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;

public class AdvancedHttpClient {
    private final Context mContext;
    private boolean mResponseOnThread;

    public AdvancedHttpClient(Context context, boolean responseOnThread) {
        this.mContext = context;
        this.mResponseOnThread = responseOnThread;
    }

    private void processResult(Message msg) {
        Exception e;
        HttpResponseHandler httpResponseHandler = msg.obj;
        HttpResponse httpResponse = httpResponseHandler.getResponse();
        if (httpResponse != null) {
            HttpEntity responseResultEntity = httpResponse.getEntity();
            if (responseResultEntity != null) {
                try {
                    String strResult;
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    String strStatus = httpResponse.getStatusLine().toString();
                    if (httpResponse.getStatusLine().getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                        Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
                        if (contentEncoding == null || !contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                            Log.secD("AdvancedHttpClient", "don't using gzip");
                            strResult = EntityUtils.toString(responseResultEntity, "UTF-8");
                        } else {
                            Log.secD("AdvancedHttpClient", "using gzip");
                            InputStream inStream = new GZIPInputStream(responseResultEntity.getContent());
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            while (true) {
                                int a = inStream.read();
                                if (a == -1) {
                                    break;
                                }
                                outStream.write(a);
                            }
                            inStream.close();
                            strResult = outStream.toString();
                            outStream.close();
                        }
                    } else {
                        Log.secD("AdvancedHttpClient", "Rsp Cd is not 200, Cd : " + statusCode);
                        strResult = EntityUtils.toString(responseResultEntity, "UTF-8");
                    }
                    Log.secD("AdvancedHttpClient", "Response : " + strResult);
                    if (this.mResponseOnThread) {
                        httpResponseHandler.onReceive(statusCode, strStatus, strResult);
                    } else {
                        responseResult(httpResponseHandler, statusCode, strStatus, strResult);
                    }
                    try {
                        responseResultEntity.consumeContent();
                        return;
                    } catch (Exception e2) {
                        Log.secE("AdvancedHttpClient", "responseResultEntity Exception : " + e2.toString());
                        return;
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    try {
                        responseResult(httpResponseHandler, -1, e2.toString(), "");
                        return;
                    } finally {
                        try {
                            responseResultEntity.consumeContent();
                        } catch (Exception e22) {
                            Log.secE("AdvancedHttpClient", "responseResultEntity Exception : " + e22.toString());
                        }
                    }
                } catch (Exception e32) {
                    e22 = e32;
                    responseResult(httpResponseHandler, -1, e22.toString(), "");
                    return;
                } catch (Exception e322) {
                    e22 = e322;
                    responseResult(httpResponseHandler, -1, e22.toString(), "");
                    return;
                }
            }
            responseResult(httpResponseHandler, -1, "HTTP parse error", "");
            return;
        }
        responseResult(httpResponseHandler, -1, "HTTP parse error", "");
    }

    private void responseResult(HttpResponseHandler httpResponseHandler, int code, String status, String result) {
        if (this.mContext == null) {
            Log.secD("AdvancedHttpClient", "ADVHTTP mContext is null");
            return;
        }
        try {
            final HttpResponseHandler httpResponseHandler2 = httpResponseHandler;
            final int i = code;
            final String str = status;
            final String str2 = result;
            ((Activity) this.mContext).runOnUiThread(new Runnable() {
                public void run() {
                    httpResponseHandler2.onReceive(i, str, str2);
                }
            });
        } catch (ClassCastException ex) {
            httpResponseHandler.onReceive(code, status, result);
            Log.secD("AdvancedHttpClient", "ADVHTTP : " + ex.toString());
        }
    }

    public Thread get(final URL url, final HeaderGroup headers, final HttpResponseHandler handler) {
        Thread t = new Thread() {
            public void run() {
                HttpResponse response = HttpClientThread.get(AdvancedHttpClient.this.mContext, url, headers);
                try {
                    C09122.sleep(1);
                    handler.setResponse(response);
                    AdvancedHttpClient.this.processResult(Message.obtain(null, 0, 0, 0, handler));
                } catch (InterruptedException e) {
                    Log.secD("AdvancedHttpClient", "get sleep interrupted exception : " + e.getMessage());
                }
            }
        };
        t.start();
        return t;
    }
}
