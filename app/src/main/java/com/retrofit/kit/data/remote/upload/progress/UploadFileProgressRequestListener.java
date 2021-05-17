package com.retrofit.kit.data.remote.upload.progress;

/**
 * Request body progress callback interface, such as for file upload
 */
public interface UploadFileProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean isDone);
}