package com.retrofit.kit.data.remote.download.write;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.retrofit.kit.data.remote.download.DownloadingFileListener;

public class FileWriteHandler extends Handler {

    public static final int FILE_WRITING_IN_PROGRESS    = 2001;
    public static final String PROGRESS                 = "progress";
    public static final String BYTES_READ               = "bytes_read";
    public static final String CONTENT_LENGTH           = "content_length";

    public static final int ERROR_WHEN_FILE_WRITING     = 2002;
    public static final String ERROR                    = "error";

    public static final int FILE_WRITING_COMPLETE       = 2003;
    public static final String DOWNLOADED_FILE_LOCATION = "location";

    private DownloadingFileListener downloadingFileListener;

    public FileWriteHandler(Looper looper, DownloadingFileListener downloadingFileListener){
        super(looper);
        this.downloadingFileListener = downloadingFileListener;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case FILE_WRITING_IN_PROGRESS:
                if (downloadingFileListener != null) {
                    Bundle bundle = message.getData();

                    int progress        = bundle.getInt(PROGRESS);
                    long bytesRead      = bundle.getLong(BYTES_READ);
                    long contentLength  = bundle.getLong(CONTENT_LENGTH);

                    downloadingFileListener.downloadingFileInProgress(progress, bytesRead, contentLength);
                }
                break;
            case ERROR_WHEN_FILE_WRITING:
                if (downloadingFileListener != null) {
                    Bundle bundle = message.getData();
                    String errorWhenWriteFile = bundle.getString(ERROR);
                    downloadingFileListener.errorWhenDownloadingFile(errorWhenWriteFile);
                }
                break;
            case FILE_WRITING_COMPLETE:
                if (downloadingFileListener != null) {
                    Bundle bundle = message.getData();
                    String downloadedFileRealPath = bundle.getString(DOWNLOADED_FILE_LOCATION);
                    downloadingFileListener.downloadingFileComplete(downloadedFileRealPath);
                }
                break;
            default:
                super.handleMessage(message);
                break;
        }
    }
}
