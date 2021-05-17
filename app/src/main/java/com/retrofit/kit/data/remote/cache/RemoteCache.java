package com.retrofit.kit.data.remote.cache;

import android.content.Context;
import com.retrofit.kit.utilities.LogcatUtils;
import java.io.File;
import okhttp3.Cache;

public class RemoteCache {

    public static final String TAG = RemoteCache.class.getSimpleName();

    private Context context;

    private String cacheDirectoryPath; /* inside this folder cache file create */
    private String cacheFileName; /* inside above folder cache file name */
    private long cacheSize; /* cache size */

    private Cache cache;
    private File cacheDir;

    public RemoteCache(Context context, String cacheDirectoryPath, String cacheFileName, long cacheSize) {
        this.context            = context;
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.cacheFileName      = cacheFileName;
        this.cacheSize          = cacheSize;
    }

    public Cache getCache() {
        setCache();
        return cache;
    }

    private void setCache() {
        if (cacheDir == null) {
            cacheDir = new File(context.getCacheDir(), cacheFileName);
        }
        try
        {
            if (cache == null)
            {
                cache = new Cache(cacheDir, cacheSize);
            }
        }
        catch (Exception exception)
        {
            LogcatUtils.errorMessage(TAG, "Could not create Cache : "+exception.getMessage());
        }
    }
}
