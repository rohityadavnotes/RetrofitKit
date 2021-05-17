package com.retrofit.kit.data.remote.service;

import com.retrofit.kit.data.remote.util.RemoteConfiguration;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface UploadService {

    @Multipart
    @POST(RemoteConfiguration.UPLOAD)
    Single<ResponseBody> upload(
            @Part("uploaded_by_name") RequestBody uploadedByName,
            @Part("file_name") RequestBody fileName,
            @Part MultipartBody.Part file
    );

    /**
     * Post request with dynamic url and single file
     *"file_parameter"
     * File file = new File(filePath);
     *
     * MediaType mediaType = MediaType.parse("multipart/form-data");
     * RequestBody requestBody = RequestBody.create(file, mediaType);
     * MultipartBody.Part part = MultipartBody.Part.createFormData(fileParameter, file.getName(), requestBody);
     *
     * postFile(url, part)
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadSingleFileWithDynamicUrl(@Url String uploadUrl, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Observable<ResponseBody> uploadMultipleFileWithDynamicUrl(@Url String uploadUrl, @Part List<MultipartBody.Part> files);

    /**
     * Post request with dynamic url and multiple file using map
     *
     * Map<String, RequestBody> requestMap = new HashMap<>();
     *
     * File file = new File(mediaPath);
     *
     * MediaType mediaType = MediaType.parse("multipart/form-data");
     * RequestBody requestBody = RequestBody.create(file, mediaType);
     *
     * requestMap.put("file_parameter\"; filename=\""+file.getName()+"\"", requestBody);
     *
     * In the above code file_parameter is the key name so if you are using php you will write
     *
     * $file               = $_FILES['file_parameter'];
     *
     * to get this.
     *
     * postFile(url, requestMap)
     */
    @Multipart
    @POST
    Call<ResponseBody> postFile(@Url String uploadUrl, @PartMap Map<String, RequestBody> requestMap);
}
