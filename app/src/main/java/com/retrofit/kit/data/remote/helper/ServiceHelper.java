package com.retrofit.kit.data.remote.helper;

import retrofit2.Retrofit;

public class ServiceHelper {

    public static final String TAG = ServiceHelper.class.getSimpleName();

    private static ServiceHelper instance;

    public static ServiceHelper getInstance() {
        if (instance == null) {
            synchronized (ServiceHelper.class) {
                if (instance == null) {
                    instance = new ServiceHelper();
                }
            }
        }
        return instance;
    }

    public <S> S createService(Class<S> serviceClass, Retrofit retrofit) {
        return retrofit.create(serviceClass);
    }
}
