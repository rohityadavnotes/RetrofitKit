package com.retrofit.kit.data.remote.interceptor;

import android.content.Context;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

/*
 * This interceptor will be called ONLY if the network is available
 *
 * If there is Internet, get the cache that was stored 5 seconds ago.
 * If the cache is older than 5 seconds, then discard it, and indicate an error in fetching the response.
 * The 'max-age' attribute is responsible for this behavior.
 */
public class ProvideCacheInterceptor  implements Interceptor {

    private static final String TAG = ProvideCacheInterceptor.class.getSimpleName();

    /*
     * Default, Expired in 5 Second
     */
    public static int DEFAULT_CACHE_DURATION_WITH_NETWORK_IN_SECONDS            = 5;

    public static final int MAX_AGE                                             = DEFAULT_CACHE_DURATION_WITH_NETWORK_IN_SECONDS;
    public static final String HEADER_CACHE_CONTROL                             = "Cache-Control";
    public static final String HEADER_PRAGMA                                    = "Pragma";

    private Context context;

    public ProvideCacheInterceptor(Context context) {
        this.context = context;
    }

    public ProvideCacheInterceptor(Context context, int cacheDurationWithNetworkInSeconds) {
        this.context = context;
        DEFAULT_CACHE_DURATION_WITH_NETWORK_IN_SECONDS     = cacheDurationWithNetworkInSeconds;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response = chain.proceed(chain.request());

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(MAX_AGE, TimeUnit.SECONDS)
                .build();

        return response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build();
    }
}
