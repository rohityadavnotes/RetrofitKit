package com.retrofit.kit.data.remote.download;

public interface DownloadingFileListener {

    /**
     * Invoked when, downloading file in progress
     *
     * @param progress
     * @param bytesRead
     * @param contentLength
     */
    void downloadingFileInProgress(int progress, long bytesRead, long contentLength);

    /**
     * Invoked when, error occur in downloading file
     *
     * @param errorMessage
     */
    void errorWhenDownloadingFile(String errorMessage);

    /**
     * Invoked when, downloading file complete
     *
     * @param downloadedFileLocation
     */
    void downloadingFileComplete(String downloadedFileLocation);
}