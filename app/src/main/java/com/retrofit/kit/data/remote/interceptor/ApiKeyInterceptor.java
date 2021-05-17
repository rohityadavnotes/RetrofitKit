package com.retrofit.kit.data.remote.interceptor;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private String apiKey;

    /**
     * We pass the apiKey here.
     */
    public ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("api_key", apiKey);
        HttpUrl url = httpUrlBuilder.build();

        Request.Builder builder = request.newBuilder();
        builder.url(url);

        return chain.proceed(builder.build());
    }
}
