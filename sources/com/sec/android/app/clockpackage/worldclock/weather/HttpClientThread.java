package com.sec.android.app.clockpackage.worldclock.weather;

import android.content.Context;
import android.net.Proxy;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

public class HttpClientThread {
    private static String TAG = HttpClientThread.class.getSimpleName();

    public static HttpResponse get(Context context, URL url, HeaderGroup headers) {
        return sendRequest(context, 1, url, null, headers);
    }

    private static HttpResponse sendRequest(Context context, int method, URL url, String body, HeaderGroup headers) {
        HttpParams hParam = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(hParam, 30000);
        HttpConnectionParams.setSoTimeout(hParam, 30000);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(hParam, schemeRegistry);
        manager.closeExpiredConnections();
        HttpClient httpClient = new DefaultHttpClient(manager, hParam);
        if (Proxy.getHost(context) != null) {
            httpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(Proxy.getHost(context), Proxy.getPort(context), "http"));
        }
        httpClient.getParams().setParameter("http.protocol.cookie-policy", "rfc2109");
        HttpUriRequest httpUriRequest = null;
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e1) {
            Log.secE(TAG, "URISyntaxException : " + e1);
        }
        if (headers != null) {
            Log.secD(TAG, "========= HEADER START ============");
            HeaderIterator iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header h = iterator.nextHeader();
                Log.secD(TAG, "H : " + h.getName() + " V : " + h.getValue());
            }
            Log.secD(TAG, "========= HEADER END ============");
        }
        switch (method) {
            case 1:
                Log.secD(TAG, "http request by @@ GET @@");
                httpUriRequest = new HttpGet(uri);
                break;
            case 2:
                Log.secD(TAG, "http request by @@ POST @@");
                HttpPost httpPost = new HttpPost(uri);
                Object httpUriRequest2 = httpPost;
                StringEntity strEntity = null;
                try {
                    strEntity = new StringEntity(body, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.secD(TAG, "ADVHTTP UnsupportedEncodingException");
                } catch (Exception e2) {
                    Log.secD(TAG, "ADVHTTP Exception");
                }
                httpPost.setEntity(strEntity);
                break;
        }
        if (httpUriRequest == null) {
            return null;
        }
        httpUriRequest.setHeader("User-Agent", "SAMSUNG-Android");
        httpUriRequest.setHeader("Accept", "*,*/*");
        httpUriRequest.setHeader("Content-Type", "text/xml");
        if (headers != null) {
            httpUriRequest.setHeaders(headers.getAllHeaders());
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpUriRequest, new BasicHttpContext());
        } catch (ClientProtocolException e3) {
            Log.secD(TAG, "ADVHTTP ClientProtocolException");
        } catch (SSLPeerUnverifiedException e4) {
            Log.secD(TAG, "ADVHTTP SSLPeerUnverifiedException " + e4);
        } catch (IOException e5) {
            Log.secD(TAG, "ADVHTTP IOException " + e5);
        } catch (Exception e6) {
            Log.secD(TAG, "ADVHTTP Exception " + e6);
        }
        if (response == null) {
            return response;
        }
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        return response;
    }
}
