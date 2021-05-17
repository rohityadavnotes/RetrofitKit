package com.retrofit.kit.data.remote.service;

import com.retrofit.kit.data.remote.RemoteResponse;
import com.retrofit.kit.data.remote.util.RemoteConfiguration;
import com.retrofit.kit.model.Data;
import com.retrofit.kit.model.Employee;
import com.retrofit.kit.model.EmployeeList;
import com.retrofit.kit.model.Page;
import java.util.Map;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RemoteObserverService {

    @GET(RemoteConfiguration.EMPLOYEE)
    Observable<Response<RemoteResponse<Employee>>> getEmployee();

    @GET(RemoteConfiguration.EMPLOYEE_LIST)
    Observable<Response<RemoteResponse<EmployeeList>>> getEmployeeList();

    /**
     * Post request with dynamic url and map
     *
     * @param url URL path
     * @param requestMap request parameters
     * @return
     *
     * Map<String, String> paramMap = new HashMap<>();
     *
     * paramMap.put("key", "00d91e8e0cca2b76f515926a36db68f5");
     * paramMap.put("phone", "13594347817");
     * paramMap.put("password", "123456");
     *
     * postMap(url, paramMap)
     */
    @FormUrlEncoded
    @POST
    Call<ResponseBody> postMap(@Url String url, @FieldMap Map<String, String> requestMap);

    @Multipart
    @POST(RemoteConfiguration.SIGN_UP)
    Observable<Response<RemoteResponse<Data>>> signUp(
            @Part MultipartBody.Part profilePic,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("gender") RequestBody gender,
            @Part("countryCode") RequestBody countryCode,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("fcmToken") RequestBody fcmToken);

    @FormUrlEncoded
    @POST(RemoteConfiguration.SIGN_IN)
    Observable<Response<RemoteResponse<Data>>> signIn(
            @Field("email") String email,
            @Field("password") String password,
            @Field("fcmToken") String fcmToken);

    @FormUrlEncoded
    @POST(RemoteConfiguration.SOCIAL_SIGN_IN)
    Observable<Response<RemoteResponse<Data>>> socialSignIn(
            @Field("provider") String provider,
            @Field("socialId") String socialId,
            @Field("picture") String picture,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("email") String email,
            @Field("fcmToken") String fcmToken);

    @FormUrlEncoded
    @POST(RemoteConfiguration.GET_USERS)
    Observable<Response<RemoteResponse<Page>>> getPage(
            @Field("apiKey") String key,
            @Field("pageNumber") String pageNumber);
}