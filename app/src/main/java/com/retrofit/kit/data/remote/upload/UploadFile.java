package com.retrofit.kit.data.remote.upload;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import com.retrofit.kit.data.remote.helper.ServiceHelper;
import com.retrofit.kit.data.remote.service.UploadService;
import com.retrofit.kit.data.remote.upload.progress.UploadFileProgressRequestBody;
import com.retrofit.kit.data.remote.upload.progress.UploadFileProgressRequestListener;
import com.retrofit.kit.utilities.LogcatUtils;
import com.retrofit.kit.utilities.file.RealPathUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class UploadFile {

    public static final String TAG = UploadFile.class.getSimpleName();
    public static Disposable disposable;

    public static void begin(final Activity context,
                             final Retrofit retrofit,
                             final String uploadedByName,
                             final String fileName,
                             final ArrayList<Uri> uris,
                             final ArrayList<File> realPaths,
                             final UploadingFileListener uploadingFileListener) {

        UploadService uploadService = ServiceHelper.getInstance().createService(UploadService.class, retrofit);

        Flowable<Integer> observable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                uploadService.upload(
                        createRequestBodyForText(uploadedByName),
                        createRequestBodyForText(fileName),
                        createMultipartBody(context, uris.get(0), "file_parameter", emitter)).blockingGet();
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);

        disposable = observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer progress) throws Exception {
                        /* call onProgress() */
                        uploadingFileListener.uploadingFileInProgress(progress, 0, false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        /* call onErrors() if error occurred during file upload */
                        LogcatUtils.debuggingMessage(TAG, "onError(Throwable throwable) : " + throwable.getMessage());
                        uploadingFileListener.errorWhenUploadingFile(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        /* call onSuccess() while file upload successful */
                        LogcatUtils.debuggingMessage(TAG, "onComplete()");
                        uploadingFileListener.uploadingFileComplete(realPaths.get(0).toString());
                    }
                });
    }

    public static void cancel(){
        if(disposable != null){
            disposable.dispose();
            disposable = null;
        }
    }

    public static RequestBody createRequestBodyForText(String text) {
        MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        return RequestBody.create(text, MEDIA_TYPE_TEXT);
    }

    public static RequestBody createRequestBodyForFile(Context context, Uri uri, File file) {
        return RequestBody.create(file, MediaType.parse(Objects.requireNonNull(context.getContentResolver().getType(uri))));
    }

    public static RequestBody createCountingRequestBody(Context context, Uri uri, File file, FlowableEmitter<Integer> emitter) {
        RequestBody requestBody = createRequestBodyForFile(context, uri, file);
        return new UploadFileProgressRequestBody(requestBody, new UploadFileProgressRequestListener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength, boolean isDone) {
                int progress = (int) ((bytesWritten * 100) / contentLength);
                emitter.onNext(progress);
            }
        });
    }

    public static MultipartBody.Part createMultipartBody(Context context, Uri uri, String parameter, FlowableEmitter<Integer> emitter) {
        File file = new File(RealPathUtils.getRealPath(context, uri));
        return MultipartBody.Part.createFormData(
                parameter,
                file.getName(),
                createCountingRequestBody(context, uri, file, emitter));
    }
}
