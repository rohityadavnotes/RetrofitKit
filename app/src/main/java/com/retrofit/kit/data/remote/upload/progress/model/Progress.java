package com.retrofit.kit.data.remote.upload.progress.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Progress implements Parcelable {

    private long bytesWritten; /* Current read byte length */
    private long contentLength; /* total byte length */
    private boolean done; /* Is the reading completed */
    private int progress;

    public Progress(long bytesWritten, long contentLength, boolean done, int progress) {
        this.bytesWritten   = bytesWritten;
        this.contentLength  = contentLength;
        this.done           = done;
        this.progress       = progress;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public static Creator<Progress> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "ProgressModel{" +
                "bytesWritten = " + bytesWritten +
                ", contentLength = " + contentLength +
                ", done = " + done +
                ", progress = " + progress +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(bytesWritten);
        parcel.writeLong(contentLength);
        parcel.writeByte((byte) (done==true?1:0));
        parcel.writeInt(this.progress);
    }

    private static final Creator<Progress> CREATOR = new Creator<Progress>() {
        @Override
        public Progress createFromParcel(Parcel parcel) {
            return new Progress(parcel);
        }

        @Override
        public Progress[] newArray(int i) {
            return new Progress[i];
        }
    };

    protected Progress(Parcel parcel) {
        bytesWritten    = parcel.readLong();
        contentLength   = parcel.readLong();
        done            = parcel.readByte()!=0;
        progress        = parcel.readInt();
    }
}
