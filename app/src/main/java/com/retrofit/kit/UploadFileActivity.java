package com.retrofit.kit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.retrofit.kit.data.remote.Remote;
import com.retrofit.kit.data.remote.upload.UploadFile;
import com.retrofit.kit.data.remote.upload.UploadingFileListener;
import com.retrofit.kit.numberprogressbar.NumberProgressBar;
import com.retrofit.kit.utilities.LogcatUtils;
import com.retrofit.kit.utilities.file.RealPathUtils;
import java.io.File;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import static com.retrofit.kit.constants.RequestCodeConstants.SELECT_FILE_REQUEST_CODE;

public class UploadFileActivity extends AppCompatActivity {

    public static final String TAG = UploadFileActivity.class.getSimpleName();

    private TextView uploadHeadingTextView;
    private NumberProgressBar numberProgressBar;
    private ProgressBar prepareProgressBar;
    private TextView uploadFileFromThisLocationTextView, errorWhenUploadingFileTextView, uploadFileButtonTextView;

    private UploadingFileListener uploadingFileListener;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        initializeView();
        initializeObject();
        onTextChangedListener();
        initializeEvent();
    }

    protected void initializeView() {
        uploadHeadingTextView                           = findViewById(R.id.uploadHeadingTextView);
        numberProgressBar                               = findViewById(R.id.numberProgressBar);
        prepareProgressBar                              = findViewById(R.id.prepareProgressBar);
        uploadFileFromThisLocationTextView              = findViewById(R.id.uploadFileFromThisLocationTextView);
        errorWhenUploadingFileTextView                  = findViewById(R.id.errorWhenDownloadingFileTextView);
        uploadFileButtonTextView                        = findViewById(R.id.uploadFileButtonTextView);
    }

    protected void initializeObject() {
        OkHttpClient okHttpClient   = Remote.getInstance().getOkHttpClientForDownloadUpload(getApplicationContext());
        retrofit                    = Remote.getInstance().getDefaultRetrofit(okHttpClient);
    }

    protected void onTextChangedListener() {
        uploadingFileListener = new UploadingFileListener() {
            @Override
            public void uploadingFileInProgress(int progress, long contentLength, boolean isDone) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        numberProgressBar.setProgress(progress);

                        if (progress == 100)
                        {
                            if (prepareProgressBar.getVisibility() == View.GONE) {
                                prepareProgressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }

            @Override
            public void errorWhenUploadingFile(String errorMessage) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        errorWhenUploadingFileTextView.setText("Error When Downloading File : "+errorMessage);

                        if (prepareProgressBar.getVisibility() == View.VISIBLE) {
                            prepareProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void uploadingFileComplete(String uploadFileFromThisLocation) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        uploadFileFromThisLocationTextView.setText("Upload File From This Location : "+uploadFileFromThisLocation);

                        if (prepareProgressBar.getVisibility() == View.VISIBLE) {
                            prepareProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };
    }

    protected void initializeEvent() {
        uploadFileButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileManager(UploadFileActivity.this,
                        "*/*",
                        true,
                        true,
                        SELECT_FILE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if(resultCode == Activity.RESULT_OK)
        {
            if (resultData != null)
            {
                if(SELECT_FILE_REQUEST_CODE == requestCode)
                {
                    ArrayList<Uri> uris      = new ArrayList<>();
                    ArrayList<File> realPaths = new ArrayList<>();

                    if(resultData.getClipData() != null)
                    {
                        int count = resultData.getClipData().getItemCount();
                        int currentItem = 0;

                        while(currentItem < count)
                        {
                            Uri uri         = resultData.getClipData().getItemAt(currentItem).getUri();
                            String realPath = RealPathUtils.getRealPath(getApplicationContext(), uri);

                            uris.add(uri);

                            if (realPath != null)
                            {
                                realPaths.add(new File(realPath));
                            }
                            else
                            {
                                uploadingFileListener.errorWhenUploadingFile("Please give read permission");
                            }

                            currentItem = currentItem + 1;
                        }
                    }
                    else if(resultData.getData() != null)
                    {
                        Uri uri         = resultData.getData();
                        String realPath = RealPathUtils.getRealPath(getApplicationContext(), uri);
                        uris.add(uri);
                        if (realPath != null)
                        {
                            realPaths.add(new File(realPath));
                        }
                        else
                        {
                            uploadingFileListener.errorWhenUploadingFile("Please give read permission");
                        }
                    }

                    for (Uri uri : uris) {
                        System.out.println("URI : "+uri);
                    }

                    for (File realPath : realPaths) {
                        System.out.println("REAL PATH : "+realPath);
                    }

                    if (realPaths.size() != 0)
                    {
                        UploadFile.begin(UploadFileActivity.this,
                                retrofit,
                                "uploadedByName",
                                "fileName",
                                uris,
                                realPaths,
                                uploadingFileListener);
                    }
                    else
                    {
                        uploadingFileListener.errorWhenUploadingFile("Please give read permission");
                    }
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            LogcatUtils.informationMessage(TAG, "Activity canceled");
        }
        else
        {
            LogcatUtils.informationMessage(TAG, "Something want wrong");
        }
    }

    public void openFileManager(Activity currentActivity,
                                String mimeType,
                                boolean showCloudStorage,
                                boolean allowMultipleSelect,
                                int readRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleSelect);

        try
        {
            currentActivity.startActivityForResult(Intent.createChooser(intent, "Please select file"), readRequestCode);
        }
        catch (ActivityNotFoundException activityNotFoundException) {
            activityNotFoundException.printStackTrace();
        }
    }
}