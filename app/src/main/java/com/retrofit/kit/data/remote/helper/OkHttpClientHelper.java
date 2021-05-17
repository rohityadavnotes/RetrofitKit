package com.retrofit.kit.data.remote.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.retrofit.kit.data.remote.cache.RemoteCache;
import com.retrofit.kit.data.remote.interceptor.ApiKeyInterceptor;
import com.retrofit.kit.data.remote.interceptor.BearerAuthenticationInterceptor;
import com.retrofit.kit.data.remote.interceptor.HeaderInterceptor;
import com.retrofit.kit.data.remote.interceptor.ProvideCacheInterceptor;
import com.retrofit.kit.data.remote.interceptor.ProvideOfflineCacheInterceptor;
import com.retrofit.kit.utilities.LogcatUtils;
import com.retrofit.kit.utilities.string.StringUtils;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import static android.util.Log.VERBOSE;

public class OkHttpClientHelper {

    @SuppressLint("StaticFieldLeak")
    private static OkHttpClientHelper instance;

    public static OkHttpClientHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (OkHttpClientHelper.class) {
                if (instance == null) {
                    instance = new OkHttpClientHelper();
                }
            }
        }
        return instance;
    }

    public class Builder {

        public final String TAG = OkHttpClientHelper.Builder.class.getSimpleName();

        /*
         * Note :  We can set timeouts settings on the underlying HTTP client.
         * If we don’t specify a client, Retrofit will create one with default connect and read timeouts.
         * By default, Retrofit uses the following timeouts :
         *                                                      Connection timeout: 10 seconds
         *                                                      Read timeout: 10 seconds
         *                                                      Write timeout: 10 seconds
         */
        private int DEFAULT_HTTP_CONNECT_TIMEOUT_IN_SECONDS             = 60;
        private int DEFAULT_HTTP_READ_TIMEOUT_IN_SECONDS                = 30;
        private int DEFAULT_HTTP_WRITE_TIMEOUT_IN_SECONDS               = 15;

        private String DEFAULT_OK_HTTP_CACHE_FILE_NAME                  = "okHttpClientCache";
        private long DEFAULT_OK_HTTP_CACHE_SIZE                         = 10 * 1024 * 1024; /* 10 MB Cache size */

        private Context context;

        private long connectTimeout                                     = DEFAULT_HTTP_CONNECT_TIMEOUT_IN_SECONDS;
        private long writeTimeout                                       = DEFAULT_HTTP_WRITE_TIMEOUT_IN_SECONDS;
        private long readTimeout                                        = DEFAULT_HTTP_READ_TIMEOUT_IN_SECONDS;

        private boolean enableDefaultCache                              = false;
        private String cacheDirectoryPath;
        private String cacheFileName                                    = DEFAULT_OK_HTTP_CACHE_FILE_NAME;
        private long cacheSize                                          = DEFAULT_OK_HTTP_CACHE_SIZE;

        private int cacheDurationWithNetworkInSeconds                   = 0;
        private int cacheDurationWithoutNetworkInSeconds                = 0;

        private Map<String, String> headers                             = null;
        private String apiKey                                           = null;
        private String bearerAuthenticationToken                        = null;

        private ArrayList<Interceptor> interceptorArrayList             = new ArrayList<Interceptor>();
        private ArrayList<Interceptor> networkInterceptorArrayList      = new ArrayList<Interceptor>();

        private boolean showLog                                         = false;

        public Builder(Context context) {
            this.context = context;
            this.cacheDirectoryPath = String.valueOf(context.getCacheDir());
        }

        public Builder setShowLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public Builder setConnectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setWriteTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder setReadTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder enableDefaultCache(boolean enableDefaultCache) {
            this.enableDefaultCache = enableDefaultCache;
            return this;
        }

        public Builder setCustomCache(String cacheDirectoryPath, String cacheFileName, long cacheSize) {
            this.cacheDirectoryPath = cacheDirectoryPath;
            this.cacheFileName = cacheFileName;
            this.cacheSize = cacheSize;
            this.enableDefaultCache = true;
            return this;
        }

        public Builder setCustomCache(String cacheDirectoryPath, String cacheFileName, long cacheSize, int cacheDurationWithNetworkInSeconds, int cacheDurationWithoutNetworkInSeconds) {
            this.cacheDirectoryPath = cacheDirectoryPath;
            this.cacheFileName = cacheFileName;
            this.cacheSize = cacheSize;
            this.cacheDurationWithNetworkInSeconds = cacheDurationWithNetworkInSeconds;
            this.cacheDurationWithoutNetworkInSeconds = cacheDurationWithoutNetworkInSeconds;
            this.enableDefaultCache = true;
            return this;
        }

        public Builder addHeader(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder setBearerAuthenticationToken(String bearerAuthenticationToken) {
            this.bearerAuthenticationToken = bearerAuthenticationToken;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            interceptorArrayList.add(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            networkInterceptorArrayList.add(interceptor);
            return this;
        }

        public OkHttpClient build(){
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
            okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
            okHttpClientBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);

            okHttpClientBuilder.retryOnConnectionFailure(true);

            if (headers != null && headers.size() > 0)
            {
                okHttpClientBuilder.addInterceptor(new HeaderInterceptor(headers));
            }

            if (!StringUtils.isEmpty(apiKey))
            {
                okHttpClientBuilder.addInterceptor(new ApiKeyInterceptor(apiKey));
            }

            if (!StringUtils.isEmpty(bearerAuthenticationToken))
            {
                okHttpClientBuilder.addInterceptor(new BearerAuthenticationInterceptor(bearerAuthenticationToken));
            }

            if (enableDefaultCache)
            {
                Cache cache = new RemoteCache(context, cacheDirectoryPath, cacheFileName, cacheSize).getCache();

                if (cache != null)
                {
                    okHttpClientBuilder.cache(cache);
                }

                if (isCustomCacheTime())
                {
                    /* used if network off OR on */
                    okHttpClientBuilder.addInterceptor(new ProvideOfflineCacheInterceptor(context, cacheDurationWithoutNetworkInSeconds));

                    /* only used when network is on */
                    okHttpClientBuilder.addNetworkInterceptor(new ProvideCacheInterceptor(context, cacheDurationWithNetworkInSeconds));
                }
                else
                {
                    /* used if network off OR on */
                   okHttpClientBuilder.addInterceptor(new ProvideOfflineCacheInterceptor(context));

                   /* only used when network is on */
                   okHttpClientBuilder.addNetworkInterceptor(new ProvideCacheInterceptor(context));
                }
            }

            /* used if network off OR on */
            if(interceptorArrayList.size() != 0)
            {
                for (Interceptor interceptor : interceptorArrayList) {
                    okHttpClientBuilder.addInterceptor(interceptor);
                }
            }

            /* only used when network is on */
            if(networkInterceptorArrayList.size() != 0)
            {
                for (Interceptor interceptor : networkInterceptorArrayList) {
                    okHttpClientBuilder.addNetworkInterceptor(interceptor);
                }
            }

            if (showLog)
            {
                Interceptor interceptor = new LoggingInterceptor.Builder().setLevel(Level.BASIC).log(VERBOSE).build();
                okHttpClientBuilder.addInterceptor(interceptor);
            }

            return okHttpClientBuilder.build();
        }

        public boolean isCustomCacheTime()
        {
            if(cacheDurationWithNetworkInSeconds <= 0)
            {
                LogcatUtils.errorMessage(TAG, "Could not set new CacheConfigurationInterceptor(context, cacheDurationWithNetworkInSeconds, cacheDurationWithoutNetworkInSeconds), not provide cacheDurationWithNetworkInSeconds");
                return false;
            }
            else if (cacheDurationWithoutNetworkInSeconds <= 0)
            {
                LogcatUtils.errorMessage(TAG, "Could not set new CacheConfigurationInterceptor(context, cacheDurationWithNetworkInSeconds, cacheDurationWithoutNetworkInSeconds), not provide cacheDurationWithoutNetworkInSeconds");
                return false;
            }

            return true;
        }
    }
}
