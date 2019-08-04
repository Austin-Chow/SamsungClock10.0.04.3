package com.sec.android.app.clockpackage.worldclock.weather;

import org.apache.http.HttpResponse;

public class HttpResponseHandler {
    private HttpResponse response;

    public void onReceive(int responseCode, String responseStatus, String responseBody) {
    }

    public HttpResponse getResponse() {
        return this.response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }
}
