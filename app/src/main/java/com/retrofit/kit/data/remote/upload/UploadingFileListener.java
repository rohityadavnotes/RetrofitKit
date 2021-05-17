package com.retrofit.kit.data.remote.upload;

public interface UploadingFileListener {

    /**
     * Invoked when, uploading file in progress
     *
     * @param progress
     * @param contentLength
     * @param isDone
     */
    void uploadingFileInProgress(int progress, long contentLength, boolean isDone);

    /**
     * Invoked when, error occur in uploading file
     *
     * @param errorMessage
     */
    void errorWhenUploadingFile(String errorMessage);

    /**
     * Invoked when, uploading file complete
     *
     * @param uploadFileFromThisLocation
     */
    void uploadingFileComplete(String uploadFileFromThisLocation);
}