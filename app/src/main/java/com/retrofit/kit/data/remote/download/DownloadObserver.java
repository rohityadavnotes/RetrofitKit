package com.retrofit.kit.data.remote.download;

import com.retrofit.kit.data.remote.exception.RemoteException;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class DownloadObserver<T> implements io.reactivex.Observer<retrofit2.Response<T>> {

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        onBegin(disposable);
    }

    @Override
    public void onNext(@NonNull retrofit2.Response<T> response) {
        System.out.println("code : "+response.code());
        System.out.println("errorBody : "+response.errorBody());
        System.out.println("body : "+response.body());
        onSuccess(response);
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        onFailure((RemoteException) throwable);
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    /**
     * OnSubscribe callback
     *
     * @param disposable
     */
    public abstract void onBegin(Disposable disposable);

    /**
     * onNext callback
     *
     * @param response
     */
    public abstract void onSuccess(@NonNull retrofit2.Response<T> response);

    /**
     * onError callback
     *
     * @param remoteException
     */
    public abstract void onFailure(RemoteException remoteException);

    /**
     * onCompleted callback
     */
    public abstract void onFinish();
}
