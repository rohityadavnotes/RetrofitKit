package com.retrofit.kit.data.remote.interceptor;

import android.content.Context;
import androidx.annotation.NonNull;
import com.retrofit.kit.internet.NetworkConnectivityUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
 * This interceptor will be called both if the network is available and if the network is not available
 *
 * If there is no Internet, get the cache that was stored 7 days ago. If the cache is older than 7 days,
 * then discard it, and indicate an error in fetching the response.
 * The 'max-stale' attribute is responsible for this behavior.
 * The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
 */
public class ProvideOfflineCacheInterceptor  implements Interceptor {

    private static final String TAG = ProvideOfflineCacheInterceptor.class.getSimpleName();

    /*
     * Default, Expired in once week.
     * 7 Days ago
     */
    public static int DEFAULT_CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS         = 7 * 24 * 60 * 60;

    public static final int MAX_STALE                                           = DEFAULT_CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS;
    public static final String HEADER_CACHE_CONTROL                             = "Cache-Control";
    public static final String HEADER_PRAGMA                                    = "Pragma";

    private Context context;

    public ProvideOfflineCacheInterceptor(Context context) {
        this.context = context;
    }

    public ProvideOfflineCacheInterceptor(Context context, int cacheDurationWithoutNetworkInSeconds) {
        this.context = context;
        DEFAULT_CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS  = cacheDurationWithoutNetworkInSeconds;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (!NetworkConnectivityUtil.isConnectedAll(context)) {

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(MAX_STALE, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();
        }

        return chain.proceed(request);
    }
}

