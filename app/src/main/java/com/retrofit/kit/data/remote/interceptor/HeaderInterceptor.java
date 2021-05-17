package com.retrofit.kit.data.remote.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    public static final String HEADER_KEY_ACCEPT                    = "Accept";
    public static final String HEADER_KEY_ACCEPT_CHARSET            = "Accept-Charset";
    public static final String HEADER_KEY_ACCEPT_ENCODING           = "Accept-Encoding";
    public static final String HEADER_KEY_ACCEPT_LANGUAGE           = "Accept-Language";
    public static final String HEADER_KEY_CONTENT_TYPE              = "Content-Type";
    public static final String HEADER_KEY_CONTENT_LENGTH            = "Content-Length";
    public static final String HEADER_KEY_CONTENT_ENCODING          = "Content-Encoding";
    public static final String HEADER_KEY_CONTENT_DISPOSITION       = "Content-Disposition";
    public static final String HEADER_KEY_CONTENT_RANGE             = "Content-Range";
    public static final String HEADER_KEY_USER_AGENT                = "User-Agent";
    public static final String HEADER_KEY_AUTHORIZATION             = "Authorization";

    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        if (headers != null && headers.size() > 0)
        {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys)
            {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        builder.addHeader("req_time", timestamp);

        return chain.proceed(builder.build());
    }
}