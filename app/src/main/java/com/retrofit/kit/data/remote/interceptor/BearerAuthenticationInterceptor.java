package com.retrofit.kit.data.remote.interceptor;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BearerAuthenticationInterceptor implements Interceptor {

    private String bearerAuthenticationToken;

    /**
     * We pass the bearerAuthenticationToken here.
     */
    public BearerAuthenticationInterceptor(String bearerAuthenticationToken) {
        this.bearerAuthenticationToken = bearerAuthenticationToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        builder.removeHeader(HeaderInterceptor.HEADER_KEY_AUTHORIZATION);
        builder.addHeader(HeaderInterceptor.HEADER_KEY_AUTHORIZATION, "Bearer " +bearerAuthenticationToken);

        return chain.proceed(builder.build());
    }
}
