package com.retrofit.kit.data.remote.picasso;

public interface PicassoImageLoadingListener {
    void imageLoadSuccess();
    void imageLoadError(Exception exception);
}