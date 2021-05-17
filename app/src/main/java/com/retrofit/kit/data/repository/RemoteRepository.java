package com.retrofit.kit.data.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import com.retrofit.kit.RetrofitPostRequestActivity;
import com.retrofit.kit.data.remote.RemoteListener;
import com.retrofit.kit.data.remote.RemoteObserver;
import com.retrofit.kit.data.remote.RemoteResponse;
import com.retrofit.kit.data.remote.exception.HttpStatusCode;
import com.retrofit.kit.data.remote.exception.RemoteException;
import com.retrofit.kit.data.remote.helper.RxHelper;
import com.retrofit.kit.data.remote.helper.ServiceHelper;
import com.retrofit.kit.data.remote.service.RemoteObserverService;
import com.retrofit.kit.data.remote.util.RemoteConfiguration;
import com.retrofit.kit.model.Employee;
import com.retrofit.kit.model.Page;
import com.retrofit.kit.utilities.LogcatUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RemoteRepository  {

    public static final String TAG = RetrofitPostRequestActivity.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static RemoteRepository instance;

    public static RemoteRepository getInstance() {
        if (instance == null) {
            synchronized (RemoteRepository.class) {
                if (instance == null) {
                    instance = new RemoteRepository();
                }
            }
        }
        return instance;
    }

    public void getEmployee(Activity activity, Retrofit retrofit, RemoteListener<RemoteResponse<Employee>> networkListener) {
        RemoteObserverService service = ServiceHelper.getInstance().createService(RemoteObserverService.class, retrofit);

        Observable<Response<RemoteResponse<Employee>>> observable = service.getEmployee();
        RemoteObserver<RemoteResponse<Employee>> observer = new RemoteObserver<RemoteResponse<Employee>>() {
            @Override
            public void onBegin(Disposable disposable) {
                networkListener.requestAccept(disposable);
            }

            @Override
            public void onSuccess(Response<RemoteResponse<Employee>> response) {
                if (response.code() == 200)
                {
                    RemoteResponse<Employee> baseResponse = response.body();
                    if (networkListener != null) {
                        networkListener.onSuccess(baseResponse);
                    }
                }
                else
                {
                    HttpStatusCode httpStatusCode   = HttpStatusCode.valueOf(response.code());
                    RemoteException remoteException = new RemoteException(httpStatusCode.value(), httpStatusCode.getReasonPhrase());

                    if (networkListener != null) {
                        networkListener.onError(remoteException.getDisplayMessage());
                    }
                }
            }

            @Override
            public void onFailure(RemoteException remoteException) {
                if (networkListener != null) {
                    networkListener.onError(remoteException.getDisplayMessage());
                }
            }
        };

        observable
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        activity.runOnUiThread(new Runnable() {
                            public void run()
                            {
                                if (networkListener != null){
                                    networkListener.requestCancel();
                                }
                            }
                        });
                    }
                })
                .compose(RxHelper.getInstance().<Response<RemoteResponse<Employee>>>applySchedulers())
                /* Subscribe */
                .subscribe(observer);
    }

    public void getPage(Activity activity, Retrofit retrofit, RemoteListener<RemoteResponse<Page>> networkListener) {
        RemoteObserverService service = ServiceHelper.getInstance().createService(RemoteObserverService.class, retrofit);

        Observable<Response<RemoteResponse<Page>>> observable = service.getPage(RemoteConfiguration.API_KEY,"1");
        RemoteObserver<RemoteResponse<Page>> observer = new RemoteObserver<RemoteResponse<Page>>(activity) {
            @Override
            public void onBegin(Disposable disposable) {
                networkListener.requestAccept(disposable);
            }

            @Override
            public void onSuccess(Response<RemoteResponse<Page>> response) {
                if (response.code() == 200)
                {
                    RemoteResponse<Page> baseResponse = response.body();
                    if (networkListener != null) {
                        networkListener.onSuccess(baseResponse);
                    }
                }
                else
                {
                    HttpStatusCode httpStatusCode   = HttpStatusCode.valueOf(response.code());
                    RemoteException remoteException = new RemoteException(httpStatusCode.value(), httpStatusCode.getReasonPhrase());

                    if (networkListener != null) {
                        networkListener.onError(remoteException.getDisplayMessage());
                    }
                }
            }

            @Override
            public void onFailure(RemoteException remoteException) {
                if (networkListener != null) {
                    networkListener.onError(remoteException.getDisplayMessage());
                }
                LogcatUtils.informationMessage(TAG, "onFailure(String errorMessage) : "+remoteException.getDisplayMessage());
            }
        };

        observable
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        activity.runOnUiThread(new Runnable() {
                            public void run()
                            {
                                if (networkListener != null) {
                                    networkListener.requestCancel();
                                }
                            }
                        });
                    }
                })
                .compose(RxHelper.getInstance().<Response<RemoteResponse<Page>>>applySchedulers())
                /* Subscribe */
                .subscribe(observer);
    }
}