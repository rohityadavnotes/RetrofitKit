package com.retrofit.kit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.retrofit.kit.data.remote.Remote;
import com.retrofit.kit.data.remote.RemoteListener;
import com.retrofit.kit.data.remote.RemoteResponse;
import com.retrofit.kit.data.remote.glide.GlideCacheUtil;
import com.retrofit.kit.data.remote.glide.GlideImageLoader;
import com.retrofit.kit.data.remote.glide.GlideImageLoadingListener;
import com.retrofit.kit.data.remote.helper.RxHelper;
import com.retrofit.kit.data.remote.picasso.PicassoImageLoader;
import com.retrofit.kit.data.remote.picasso.PicassoImageLoadingListener;
import com.retrofit.kit.data.repository.RemoteRepository;
import com.retrofit.kit.model.Employee;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RequestTestActivity extends AppCompatActivity {

    public static final String TAG = RequestTestActivity.class.getSimpleName();

    private ImageView userImageView;
    private ProgressBar imageLoadingProgressBar, progressBar;
    private TextView responseTextView;
    private TextView sendRequestButtonTextView, cancelRequestButtonTextView;

    private Retrofit retrofit;
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);
        initializeView();
        initializeObject();
        initializeEvent();
    }

    protected void initializeView() {
        userImageView               = findViewById(R.id.userImageView);
        imageLoadingProgressBar     = findViewById(R.id.imageLoadingProgressBar);
        progressBar                 = findViewById(R.id.progressBar);
        progressBar                 = findViewById(R.id.progressBar);
        responseTextView            = findViewById(R.id.responseTextView);
        sendRequestButtonTextView   = findViewById(R.id.sendRequestButtonTextView);
        cancelRequestButtonTextView = findViewById(R.id.cancelRequestButtonTextView);
    }

    protected void initializeObject() {
        OkHttpClient okHttpClient   = Remote.getInstance().getOkHttpClientWithDefaultCacheEnable(getApplicationContext());
        retrofit                    = Remote.getInstance().getDefaultRetrofit(okHttpClient);
    }

    protected void initializeEvent() {
       /*GlideCacheUtil.getInstance().clearAllCache(this);

       GlideImageLoader.load(
                this,
                "https://backend24.000webhostapp.com/Json/profile.jpg",
                R.drawable.user_placeholder,
                R.drawable.error_placeholder,
                userImageView,
                new GlideImageLoadingListener() {
                    @Override
                    public void imageLoadSuccess() {
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void imageLoadError() {
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }
                });*/

       PicassoImageLoader.load(
                this,
                "https://backend24.000webhostapp.com/Json/profile.jpg",
                R.drawable.user_placeholder,
                R.drawable.error_placeholder,
                userImageView,
                new PicassoImageLoadingListener() {
                    @Override
                    public void imageLoadSuccess() {
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void imageLoadError(Exception exception) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        imageLoadingProgressBar.setVisibility(View.GONE);
                    }
                });

        sendRequestButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });

        cancelRequestButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxHelper.getInstance().dispose(subscribe);
            }
        });
    }

    private void callApi() {
        RemoteRepository.getInstance().getEmployee(RequestTestActivity.this, retrofit, new RemoteListener<RemoteResponse<Employee>>() {
            @Override
            public void requestAccept(Disposable disposable) {
                progressBar.setVisibility(View.VISIBLE);
                subscribe = disposable;
            }

            @Override
            public void requestCancel() {
                progressBar.setVisibility(View.GONE);
                responseTextView.setText("cancel");
            }

            @Override
            public void onSuccess(RemoteResponse<Employee> employeeBaseResponse) {
                progressBar.setVisibility(View.GONE);
                responseTextView.setText(employeeBaseResponse.getData().getEmployeeSalary());
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                responseTextView.setText(errorMessage);
            }
        });
    }
}