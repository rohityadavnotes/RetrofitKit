package com.retrofit.kit.data.remote.download.write;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.retrofit.kit.utilities.LogcatUtils;
import com.retrofit.kit.utilities.file.RealPathUtils;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.ResponseBody;

public class FileWriteThread implements Runnable {

    public static final String TAG = FileWriteThread.class.getSimpleName();

    private Context context;
    private Uri writeDownloadedFileOnThisUri;
    private ResponseBody responseBody;
    private FileWriteHandler fileWriteHandler;

    public FileWriteThread(Context context,
                           Uri writeDownloadedFileOnThisUri,
                           ResponseBody responseBody,
                           FileWriteHandler fileWriteHandler) {

        this.context                        = context;
        this.writeDownloadedFileOnThisUri   = writeDownloadedFileOnThisUri;
        this.responseBody                   = responseBody;
        this.fileWriteHandler               = fileWriteHandler;
    }

    @Override
    public void run() {
        Log.d(TAG, Thread.currentThread().getName() + " : starting thread");

        ParcelFileDescriptor parcelFileDescriptor = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;

        /*
         * buffer size used for reading and writing
         * 4KB byte buffer 4 * 1024.
         */
        final int BUFFER_SIZE = 4096;

        try
        {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(writeDownloadedFileOnThisUri, "w");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            outputStream = new FileOutputStream(fileDescriptor);

            /*input stream to read file - with 8k buffer*/
            inputStream = responseBody.byteStream();

            /*
             * You are reading the file into a buffer of 4096 Bytes.
             * Then those 4096 bytes are written to outputStream.
             * This process repeats until the whole file is read into the outputStream.
             */
            byte[] bufferSize = new byte[BUFFER_SIZE];

            /*
             * this will be useful to display download percentage
             * might be -1: server did not report the length
             */
            long lengthOfFile = responseBody.contentLength();
            long total = 0;
            int count;

            while ((count = inputStream.read(bufferSize)) != -1)
            {
                total += count; /* same : total = total + count */

                /* Calculate the percentage downloaded */
                int progress = (int) ((total * 100) / lengthOfFile);
                LogcatUtils.debuggingMessage(TAG, "PROGRESS FROM WRITE FILE : " + progress);
                downloadingFileInProgress(progress, total, lengthOfFile);

                /*
                 * Transfer bytes from inputStream to outputStream.
                 */
                outputStream.write(bufferSize, 0, count);
            }

            /* Refresh the object output stream and write any bytes to the underlying stream (somewhere in ObjectOutputStream) */
            outputStream.flush();
            downloadingFileComplete(RealPathUtils.getRealPath(context, writeDownloadedFileOnThisUri));
        }
        catch (IOException iOException) {
            LogcatUtils.debuggingMessage(TAG, "IOException 1 : " + iOException.getMessage());
            errorWhenDownloadingFile(iOException.getMessage());
        }
        finally
        {
            if(outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "IOException 2 : " + iOException.getMessage());
                    errorWhenDownloadingFile(iOException.getMessage());
                }
            }

            if(inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "IOException 3 : " + iOException.getMessage());
                    errorWhenDownloadingFile(iOException.getMessage());
                }
            }

            if(parcelFileDescriptor != null)
            {
                try
                {
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "IOException 4 : " + iOException.getMessage());
                    errorWhenDownloadingFile(iOException.getMessage());
                }
            }

            if(responseBody != null)
            {
                responseBody.close();
            }
        }

        Log.d(TAG, Thread.currentThread().getName() + " : end thread");
    }

    private void downloadingFileInProgress(int progress, long bytesRead, long contentLength) {
        Message message = fileWriteHandler.obtainMessage();
        message.what = FileWriteHandler.FILE_WRITING_IN_PROGRESS;

        Bundle bundle = new Bundle();
        bundle.putInt(FileWriteHandler.PROGRESS, progress);
        bundle.putLong(FileWriteHandler.BYTES_READ, bytesRead);
        bundle.putLong(FileWriteHandler.CONTENT_LENGTH, contentLength);
        message.setData(bundle);

        fileWriteHandler.sendMessage(message);
    }

    private void errorWhenDownloadingFile(String errorMessage) {
        Message message = fileWriteHandler.obtainMessage();
        message.what = FileWriteHandler.ERROR_WHEN_FILE_WRITING;

        Bundle bundle = new Bundle();
        bundle.putString(FileWriteHandler.ERROR, errorMessage);
        message.setData(bundle);

        fileWriteHandler.sendMessage(message);
    }

    private void downloadingFileComplete(String downloadedFileLocation) {
        Message message = fileWriteHandler.obtainMessage();
        message.what = FileWriteHandler.FILE_WRITING_COMPLETE;

        Bundle bundle = new Bundle();
        bundle.putString(FileWriteHandler.DOWNLOADED_FILE_LOCATION, downloadedFileLocation);
        message.setData(bundle);

        fileWriteHandler.sendMessage(message);
    }
}