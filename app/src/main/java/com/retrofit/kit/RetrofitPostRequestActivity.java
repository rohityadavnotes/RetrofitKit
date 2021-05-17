package com.retrofit.kit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.retrofit.kit.data.remote.Remote;
import com.retrofit.kit.data.remote.RemoteListener;
import com.retrofit.kit.data.remote.RemoteResponse;
import com.retrofit.kit.data.remote.helper.RxHelper;
import com.retrofit.kit.data.repository.RemoteRepository;
import com.retrofit.kit.model.Page;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitPostRequestActivity extends AppCompatActivity {

    public static final String TAG = RetrofitPostRequestActivity.class.getSimpleName();

    private TextView sendRequestButton;
    private TextView responseTextView;

    private Retrofit retrofit;
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_post_request);
        initializeView();
        initializeObject();
        initializeEvent();
    }

    protected void initializeView() {
        sendRequestButton = findViewById(R.id.sendRequestButtonTextView);
        responseTextView  = findViewById(R.id.responseTextView);
    }

    protected void initializeObject() {
        OkHttpClient okHttpClient   = Remote.getInstance().getDefaultOkHttpClient(getApplicationContext());
        retrofit                    = Remote.getInstance().getDefaultRetrofit(okHttpClient);
    }

    protected void initializeEvent() {
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });
    }

    private void callApi() {
        RemoteRepository.getInstance().getPage(RetrofitPostRequestActivity.this, retrofit, new RemoteListener<RemoteResponse<Page>>() {
            @Override
            public void requestAccept(Disposable disposable) {
                subscribe = disposable;
            }

            @Override
            public void requestCancel() {
                responseTextView.setText("cancel");
            }

            @Override
            public void onSuccess(RemoteResponse<Page> employeeBaseResponse) {
                responseTextView.setText(employeeBaseResponse.getData().getUsers().get(0).getEmail());
            }

            @Override
            public void onError(String errorMessage) {
                responseTextView.setText(errorMessage);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxHelper.getInstance().dispose(subscribe);
    }
}