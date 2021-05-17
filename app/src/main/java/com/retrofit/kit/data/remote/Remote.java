package com.retrofit.kit.data.remote;

import android.content.Context;
import com.retrofit.kit.BuildConfig;
import com.retrofit.kit.data.remote.helper.GsonHelper;
import com.retrofit.kit.data.remote.helper.OkHttpClientHelper;
import com.retrofit.kit.data.remote.helper.RetrofitHelper;
import com.retrofit.kit.data.remote.interceptor.HeaderInterceptor;
import com.retrofit.kit.data.remote.util.RemoteConfiguration;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Remote {

    public static final String TAG = Remote.class.getSimpleName();

    private static Remote instance;

    public static Remote getInstance() {
        if (instance == null) {
            synchronized (Remote.class) {
                if (instance == null) {
                    instance = new Remote();
                }
            }
        }
        return instance;
    }

    /*
     ***********************************************************************************************
     ***************************** Get Default OkHttpClient and Retrofit ***************************
     ***********************************************************************************************
     */
    public OkHttpClient getDefaultOkHttpClient(Context context) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance(context).new Builder(context);
        boolean LOG_ENABLE = BuildConfig.DEBUG;
        builder.setShowLog(LOG_ENABLE);
        return builder.build();
    }

    public Retrofit getDefaultRetrofit(OkHttpClient okHttpClient) {
        RetrofitHelper.Builder builder = RetrofitHelper.getInstance().new Builder();
        builder.setBaseUrl(RemoteConfiguration.BASE_URL);
        builder.setOkHttpClient(okHttpClient);
        return builder.build();
    }

    /*
     ***********************************************************************************************
     *********** NO USE ANY INTERCEPTOR , CACHE, COOKIE, IF YOU WANT DOWNLOAD OR UPLOAD ************
     ************************** Get OkHttpClient for Download Upload file **************************
     */
    public OkHttpClient getOkHttpClientForDownloadUpload(Context context) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance(context).new Builder(context);
        return builder.build();
    }

    /*
     ********************************** POST METHOD NOT SUPPORT CACHE ******************************
     ********************************** Get OkHttpClient With Default Cache ************************
     ***********************************************************************************************
     */
    public OkHttpClient getOkHttpClientWithDefaultCacheEnable(Context context) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance(context).new Builder(context);
        builder.enableDefaultCache(true);
        boolean LOG_ENABLE = BuildConfig.DEBUG;
        builder.setShowLog(LOG_ENABLE);
        return builder.build();
    }

    /*
     ***********************************************************************************************
     ************************** Get OkHttpClient With Bearer Authentication Token ******************
     ***********************************************************************************************
     */
    public OkHttpClient getOkHttpClientWithBearerAuthenticationToken(Context context, String token) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance(context).new Builder(context);
        builder.setBearerAuthenticationToken(token);
        boolean LOG_ENABLE = BuildConfig.DEBUG;
        builder.setShowLog(LOG_ENABLE);
        return builder.build();
    }

    /*
     ***********************************************************************************************
     ************************************ Get OkHttpClient With Api Key ****************************
     ***********************************************************************************************
     */
    public OkHttpClient getOkHttpClientWithApiKey(Context context, String apiKey) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance(context).new Builder(context);
        builder.setApiKey(apiKey);
        boolean LOG_ENABLE = BuildConfig.DEBUG;
        builder.setShowLog(LOG_ENABLE);
        return builder.build();
    }

    /*
     ***********************************************************************************************
     ****************************** Get full customize OkHttpClient and Retrofit *******************
     ***********************************************************************************************
     */
    public OkHttpClient getCustomOkHttpClient(Context context) {
        OkHttpClientHelper.Builder builder = OkHttpClientHelper.getInstance(context).new Builder(context);

        builder.setConnectTimeout(RemoteConfiguration.CUSTOM_HTTP_CONNECT_TIMEOUT_IN_SECONDS);
        builder.setWriteTimeout(RemoteConfiguration.CUSTOM_HTTP_WRITE_TIMEOUT_IN_SECONDS);
        builder.setReadTimeout(RemoteConfiguration.CUSTOM_HTTP_READ_TIMEOUT_IN_SECONDS);

        Map<String, String> headers = new HashMap<>();
        headers.put(HeaderInterceptor.HEADER_KEY_CONTENT_TYPE, "application/json; charset=utf-8");
        headers.put(HeaderInterceptor.HEADER_KEY_ACCEPT, "application/json");
        headers.put(HeaderInterceptor.HEADER_KEY_ACCEPT_CHARSET, "utf-8");
        headers.put(HeaderInterceptor.HEADER_KEY_USER_AGENT, "Retrofit Kit");
        headers.put("app_version", "android_" + 1.0);

        builder.addHeader(headers);

        builder.setApiKey(RemoteConfiguration.API_KEY);

        builder.setBearerAuthenticationToken(RemoteConfiguration.BEARER_AUTHENTICATION_TOKEN);

        builder.setCustomCache(String.valueOf(
                context.getCacheDir()),
                RemoteConfiguration.CUSTOM_OK_HTTP_CACHE_FILE_NAME,
                RemoteConfiguration.CUSTOM_OK_HTTP_CACHE_SIZE,
                RemoteConfiguration.CUSTOM_CACHE_DURATION_WITH_NETWORK_IN_SECONDS,
                RemoteConfiguration.CUSTOM_CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS
        );

        /*
         * Add more custom interceptor, if you want
         *
         * If you add network only interceptor like this
         * builder.addInterceptor(new CustomInterceptor(context));
         *
         * If you add network network interceptor like this
         * builder.addNetworkInterceptor(new CustomInterceptor(context));
         */

        boolean LOG_ENABLE = BuildConfig.DEBUG;
        builder.setShowLog(LOG_ENABLE);

        return builder.build();
    }

    public Retrofit getCustomOkHttpClient(OkHttpClient okHttpClient) {
        RetrofitHelper.Builder builder = RetrofitHelper.getInstance().new Builder();
        builder.setBaseUrl(RemoteConfiguration.BASE_URL);
        builder.setOkHttpClient(okHttpClient);

        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create(GsonHelper.getGsonBuilder().create()));

        return builder.build();
    }
}
