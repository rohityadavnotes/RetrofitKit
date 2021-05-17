package com.retrofit.kit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import com.retrofit.kit.constants.RequestCodeConstants;
import com.retrofit.kit.data.remote.Remote;
import com.retrofit.kit.data.remote.download.DownloadFile;
import com.retrofit.kit.data.remote.download.DownloadingFileListener;
import com.retrofit.kit.data.remote.download.write.FileWriteHandler;
import com.retrofit.kit.model.DownloadThis;
import com.retrofit.kit.numberprogressbar.NumberProgressBar;
import com.retrofit.kit.saf.SAFUtils;
import com.retrofit.kit.utilities.AssetsFileUtil;
import com.retrofit.kit.utilities.MimeUtils;
import com.retrofit.kit.utilities.file.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class DownloadFileActivity extends AppCompatActivity {

    public static final String TAG = DownloadFileActivity.class.getSimpleName();

    private TextView downloadHeadingTextView;
    private NumberProgressBar numberProgressBar;
    private TextView downloadedFileLocationTextView, errorWhenDownloadingFileTextView, downloadFileButtonTextView;

    private Retrofit retrofit;

    private ArrayList<DownloadThis> downloadThisArrayList;
    private DownloadingFileListener downloadingFileListener;
    private FileWriteHandler fileWriteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);
        initializeView();
        initializeObject();
        onTextChangedListener();
        initializeEvent();
    }

    protected void initializeView() {
        downloadHeadingTextView                         = findViewById(R.id.downloadHeadingTextView);
        numberProgressBar                               = findViewById(R.id.numberProgressBar);
        downloadedFileLocationTextView                  = findViewById(R.id.downloadedFileLocationTextView);
        errorWhenDownloadingFileTextView                = findViewById(R.id.errorWhenDownloadingFileTextView);
        downloadFileButtonTextView                      = findViewById(R.id.downloadFileButtonTextView);
    }

    protected void initializeObject() {
        downloadThisArrayList   = new ArrayList<>();
        downloadThisArrayList   = getDownloadThisArrayList();

        OkHttpClient okHttpClient   = Remote.getInstance().getOkHttpClientForDownloadUpload(getApplicationContext());
        retrofit                    = Remote.getInstance().getDefaultRetrofit(okHttpClient);
    }

    protected void onTextChangedListener() {
        downloadingFileListener = new DownloadingFileListener() {
            @Override
            public void downloadingFileInProgress(final int progress, final long bytesRead, final long contentLength) {
                numberProgressBar.setProgress(progress);
            }

            @Override
            public void errorWhenDownloadingFile(final String errorMessage) {
                errorWhenDownloadingFileTextView.setText(getString(R.string.error_when_downloading_file)+errorMessage);
            }

            @Override
            public void downloadingFileComplete(final String downloadedFileLocation) {
                downloadedFileLocationTextView.setText(getString(R.string.downloaded_file_location)+downloadedFileLocation);
            }
        };
        fileWriteHandler = new FileWriteHandler(Looper.getMainLooper(), downloadingFileListener);
    }

    protected void initializeEvent() {
        downloadFileButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentFile rootDirectory   = SAFUtils.takeRootDirectoryWithPermission(getApplicationContext(),DownloadFileActivity.this);
                if(rootDirectory != null)
                {
                    createFileOwnDirectory(rootDirectory, "downloadsDirectory", downloadThisArrayList.get(0).getFileDownloadFromThisUrl());
                }
            }
        });
    }

    private ArrayList<DownloadThis> getDownloadThisArrayList() {
        try
        {
            JSONObject object = AssetsFileUtil.loadJSONFromAsset(getApplicationContext(),"json/download.json");
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String file_download_from_this_url = jsonObject.getString("file_download_from_this_url");

                downloadThisArrayList.add(new DownloadThis(id, name, description, file_download_from_this_url));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return downloadThisArrayList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if(resultCode == Activity.RESULT_OK)
        {
            if (resultData != null)
            {
                Uri uri = resultData.getData();

                if (uri != null)
                {
                    if(RequestCodeConstants.SELECT_FOLDER_REQUEST_CODE == requestCode)
                    {
                        /* Save the obtained directory permissions */
                        final int takeFlags = resultData.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        getContentResolver().takePersistableUriPermission(uri, takeFlags);

                        /* Save uri */
                        SharedPreferences sharedPreferences = getSharedPreferences(SAFUtils.SAF_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(SAFUtils.ALLOW_DIRECTORY, uri.toString());
                        sharedPreferencesEditor.apply();

                        DocumentFile rootDirectory   = SAFUtils.getRootDirectory(getApplicationContext(),uri);
                        createFileOwnDirectory(rootDirectory, "downloadsDirectory", downloadThisArrayList.get(0).getFileDownloadFromThisUrl());                    }
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(),  "Activity canceled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),  "Something want wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFileOwnDirectory(DocumentFile rootDirectory, String childDirectoryName, String downloadFileFromThisUrl) {
        DocumentFile childDirectory;

        if(rootDirectory.findFile(childDirectoryName) == null)
        {
            childDirectory  = SAFUtils.createDirectory(rootDirectory, childDirectoryName);
        }
        else
        {
            childDirectory = rootDirectory.findFile(childDirectoryName);
        }

        String fileNameWithExtension    = MimeUtils.getFileNameWithExtensionFromURL(downloadFileFromThisUrl);
        String mimeType                 = MimeUtils.guessMimeTypeFromExtension(FileUtils.getFileExtension(fileNameWithExtension));
        String fileNameWithoutExtension = FileUtils.getFileNameNoExtension(fileNameWithExtension);

        DocumentFile file = SAFUtils.createFile(childDirectory, mimeType, fileNameWithoutExtension);
        DownloadFile.begin(getApplicationContext(), retrofit, downloadFileFromThisUrl, SAFUtils.getUri(file), fileWriteHandler);
    }
}