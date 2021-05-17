package com.retrofit.kit.data.remote;

import android.app.Activity;
import com.retrofit.kit.R;
import com.retrofit.kit.customdialog.CustomDialog;
import com.retrofit.kit.data.remote.exception.ExceptionHelper;
import com.retrofit.kit.data.remote.exception.RemoteException;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public abstract class RemoteObserver<T> implements io.reactivex.Observer<Response<T>> {

    private boolean showDialog = false;
    private CustomDialog customDialog;
    private Activity activity;

    public RemoteObserver(Activity activity, Boolean showDialog) {
        this.activity       = activity;
        this.showDialog     = showDialog;
    }

    public RemoteObserver(Activity activity) {
        this(activity,true);
    }

    public RemoteObserver() {
    }

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        onBegin(disposable);
        if (showDialog) {
            customDialog = new CustomDialog(
                    activity,
                    R.style.Custom_Dialog_Style,
                    R.layout.loading_dialog,
                    false,
                    false);
            customDialog.show();
        }
    }

    @Override
    public void onNext(@NonNull Response<T> response) {
        System.out.println("code : "+response.code());
        System.out.println("errorBody : "+response.errorBody());
        System.out.println("body : "+response.body());
        onSuccess(response);
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        hidDialog();
        onFailure(ExceptionHelper.getInstance().handleException(throwable));
    }

    @Override
    public void onComplete() {
        hidDialog();
    }

    public void hidDialog() {
        if(showDialog && customDialog != null)
        {
            customDialog.dismiss();
            customDialog = null;
            showDialog = false;
        }
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
    public abstract void onSuccess(Response<T> response);

    /**
     * onError callback
     *
     * @param remoteException
     */
    public abstract void onFailure(RemoteException remoteException);
}