package com.retrofit.kit.data.remote.helper;

import com.retrofit.kit.data.remote.func.RetryFunction;
import com.retrofit.kit.utilities.LogcatUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

    public static final String TAG = RxHelper.class.getSimpleName();

    private static RxHelper instance;

    public static RxHelper getInstance() {
        if (instance == null) {
            synchronized (RxHelper.class) {
                if (instance == null) {
                    instance = new RxHelper();
                }
            }
        }
        return instance;
    }

    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())

                        /* Update the UI on the main thread after the request is completed */
                        .observeOn(AndroidSchedulers.mainThread())

                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                /* Here you can do some operations yourself, such as judging the network connection status, etc. */
                                LogcatUtils.informationMessage(TAG, "doOnSubscribe : "+disposable.isDisposed());
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                LogcatUtils.informationMessage(TAG, "doFinally");
                            }
                        });
            }
        };
    }

    public <T> ObservableTransformer<T, T> applySchedulersWithRetry() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())

                        /* Update the UI on the main thread after the request is completed */
                        .observeOn(AndroidSchedulers.mainThread())

                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                /* Here you can do some operations yourself, such as judging the network connection status, etc. */
                                LogcatUtils.informationMessage(TAG, "doOnSubscribe : "+disposable.isDisposed());
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                LogcatUtils.informationMessage(TAG, "doFinally");
                            }
                        })

                        .retryWhen(new RetryFunction(3, 3));
            }
        };
    }

    /**
     * unsubscribe
     *
     * @param disposable subscription information
     */
    public void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            LogcatUtils.informationMessage(TAG,"Call dispose(Disposable disposable)");
        }
    }
}
