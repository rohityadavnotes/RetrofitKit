package com.retrofit.kit.data.remote.util;

public class RemoteConfiguration {

    public static final int CUSTOM_HTTP_CONNECT_TIMEOUT_IN_SECONDS              = 2 * 60; /* 2 minutes */
    public static final int CUSTOM_HTTP_READ_TIMEOUT_IN_SECONDS                 = 40; /* 40 seconds */
    public static final int CUSTOM_HTTP_WRITE_TIMEOUT_IN_SECONDS                = 25; /* 25 seconds */

    public static final String CUSTOM_OK_HTTP_CACHE_FILE_NAME                   = "customOkHttpClientCache";
    public static final long CUSTOM_OK_HTTP_CACHE_SIZE                          = 20 * 1024 * 1024; /* 20 MB Cache size */

    public static final int CUSTOM_CACHE_DURATION_WITH_NETWORK_IN_SECONDS       = 10;
    public static final int CUSTOM_CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS    = 14 * 24 * 60 * 60; /* Expired in two week. */

    public static final String HOST_URL                                         = "https://backend24.000webhostapp.com";

    /* Note : If you use Retrofit 2 then add / at the end of base url. if use Retrofit 1 so remove it */
    public static final String BASE_URL                                         = "https://backend24.000webhostapp.com/";

    public static final String API_KEY                                          = "AIzaSyOzB818x55FASHvX4JuGQciR9lv7q";
    public static final String BEARER_AUTHENTICATION_TOKEN                      = "AIzaSyOzB818x55FASHvX4JuGQciR9lv7q";

    /*
     * End points
     * Note : If you use Retrofit 1 then add / at START.
     */
    public static final String EMPLOYEE                                         = "Json/employee.json";
    public static final String EMPLOYEE_LIST                                    = "Json/employeeList.json";

    public static final String UPLOAD                                           = "UploadFile/upload_multipart_file.php";

    public static final String SIGN_UP                                          = "RestApi/middleware/AppSignUp.php";
    public static final String SIGN_IN                                          = "RestApi/middleware/SignIn.php";
    public static final String SOCIAL_SIGN_IN                                   = "RestApi/middleware/SocialSignIn.php";
    public static final String GET_USERS                                        = "RestApi/middleware/GetUsers.php";
}
