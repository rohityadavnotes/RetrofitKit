package com.retrofit.kit.data.remote.upload.progress;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadFileProgressRequestBody extends RequestBody {

    private final RequestBody requestBody;
    private final UploadFileProgressRequestListener uploadFileProgressRequestListener;

    /**
     * constructor, assignment
     *
     * @param requestBody
     * @param uploadFileProgressRequestListener
     */
    public UploadFileProgressRequestBody(RequestBody requestBody, UploadFileProgressRequestListener uploadFileProgressRequestListener) {
        this.requestBody                        = requestBody;
        this.uploadFileProgressRequestListener  = uploadFileProgressRequestListener;
    }

    /**
     * Rewrite the contentType of the actual response body
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * Rewrite the contentLength that calls the actual response body
     *
     * @return contentLength
     */
    @Override
    public long contentLength() {
        try
        {
            return requestBody.contentLength();
        }
        catch (IOException iOException)
        {
            iOException.printStackTrace();
        }
        return -1;
    }

    /**
     * Rewrite to write
     *
     * @param sink
     * @throws IOException exception
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        ByteSink byteSink = new ByteSink(sink);
        BufferedSink bufferedSink = Okio.buffer(byteSink);
        requestBody.writeTo(bufferedSink); /* recursive call */
        bufferedSink.flush();
    }

    protected final class ByteSink extends ForwardingSink {

        /* The current number of bytes written */
        long bytesWritten = 0L;

        /* total byte length, avoid calling the contentLength () method multiple times */
        long contentLength = 0L;

        ByteSink(Sink sink) {
            super(sink);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            if (contentLength == 0) {
                /* Get the value of contentLength, no longer call later */
                contentLength = contentLength();
            }

            /* Increase the number of bytes currently written */
            bytesWritten += byteCount;
            uploadFileProgressRequestListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
        }
    }
}
