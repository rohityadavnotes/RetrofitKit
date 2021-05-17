package com.retrofit.kit.data.remote.helper;

import androidx.annotation.NonNull;
import com.retrofit.kit.utilities.LogcatUtils;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ParamHelper {

    public static final String TAG = ParamHelper.class.getSimpleName();

    private Map<String, Object> params;

    public ParamHelper addParams(String key, Object value) {
        if (params == null) {
            params = new TreeMap<>();
        }

        params.put(key, value);
        return this;
    }

    public Map getParams() {
        if (params == null) {
            return null;
        }

        return params;
    }

    public void clearParams() {
        if (params != null)
            params.clear();
    }

    public String createUrlFromParams(String url, Map<String, String> params) {
        try
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(url);

            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) {
                stringBuilder.append("&");
            } else {
                stringBuilder.append("?");
            }

            for (Map.Entry<String, String> urlParams : params.entrySet()) {
                String urlValues = urlParams.getValue();
                stringBuilder.append(urlParams.getKey()).append("=").append(urlValues).append("&");
            }

            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            return stringBuilder.toString();
        }
        catch (Exception exception) {
            LogcatUtils.errorMessage(TAG, exception.getMessage());
        }
        return url;
    }

    /**
     * Create request body for string
     */
    @NonNull
    public static RequestBody createRequestBodyForString(String string) {
        return RequestBody.create(string, MediaType.parse("text/plain"));
    }

    /**
     * Create request body for file
     */
    public static RequestBody createRequestBodyForFile(File file) {
        return RequestBody.create(file, MediaType.parse("multipart/form-data"));
    }

    /**
     * Create multipart part request parameter
     */
    public static MultipartBody.Part createMultipartBody(String fileParameter, String filePath) {
        File file = new File(filePath);
        RequestBody requestBody = createRequestBodyForFile(file);
        return MultipartBody.Part.createFormData(fileParameter, file.getName(), requestBody);
    }
}