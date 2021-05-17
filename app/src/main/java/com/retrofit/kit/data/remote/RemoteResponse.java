package com.retrofit.kit.data.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Users can customize their own class according to their own needs to inherit BaseDataObserver<T>
 *
 * Apply to
 *          {
 *              "status": true,
 *              "code": 200,
 *              "message": "Get page successfully",
 *              "data": {
 *                          "current_page_number": 1,
 *                          "total_number_of_items": 2,
 *                          "item_in_one_page": 5,
 *                          "total_number_of_pages": 1,
 *                          "users": [{
 *                                      "id": "14",
 *                                      "first_name": "Rohit",
 *                                      "last_name": "Yadav",
 *                                      "gender": "MALE",
 *                                      "phone_number": "7898680304",
 *                                      "phone_number_verified": "1",
 *                                      "email": "rohitnotes24@gmail.com",
 *                                      "email_verified": "1",
 *                                      "password": "12345",
 *                                      "fcm_token": "sdfa",
 *                                      "last_login": "2021-02-11 07:48:24",
 *                                      "created_at": "2021-02-11 07:43:40",
 *                                      "updated_at": "2021-02-11 07:56:07",
 *                                      "expired_at": "2021-02-11 07:43:40",
 *                                      "account_verified_by_admin": "1"
 *                                    }, {
 *                                      "id": "15",
 *                                      "first_name": "Lucky",
 *                                      "last_name": "Yadav",
 *                                      "gender": "MALE",
 *                                      "phone_number": "7898680304",
 *                                      "phone_number_verified": "1",
 *                                      "email": "iamrohityadav24@gmail.com",
 *                                      "email_verified": "1",
 *                                      "password": "password",
 *                                      "fcm_token": "adaerwerrewerfsd",
 *                                      "last_login": "2021-02-11 07:44:35",
 *                                      "created_at": "2021-02-11 07:44:35",
 *                                      "updated_at": "2021-02-11 07:44:35",
 *                                      "expired_at": "2021-02-11 07:44:35",
 *                                      "account_verified_by_admin": "1"
 *                                   }]
 *                       }
 *              }
 */
public class RemoteResponse<T> {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private T data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
