package com.samsung.context.sdk.samsunganalytics.internal.connection;

public enum HttpMethod {
    GET("GET"),
    POST("POST");
    
    String method;

    private HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }
}
