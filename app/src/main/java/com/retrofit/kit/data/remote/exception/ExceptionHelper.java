package com.retrofit.kit.data.remote.exception;

import android.net.ParseException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.retrofit.kit.constants.AppConstants;
import com.retrofit.kit.data.remote.util.RemoteConfiguration;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ExceptionHelper {

    public static final String TAG = ExceptionHelper.class.getSimpleName();

    private static ExceptionHelper instance;

    public static ExceptionHelper getInstance() {
        if (instance == null) {
            synchronized (ExceptionHelper.class) {
                if (instance == null) {
                    instance = new ExceptionHelper();
                }
            }
        }
        return instance;
    }

    public static final int UNKNOWN_HOST_EXCEPTION           = 1001;
    public static final int CONNECT_EXCEPTION                = 1002;
    public static final int SOCKET_TIMEOUT_EXCEPTION         = 1003;
    public static final int CONNECT_TIMEOUT_EXCEPTION        = 1004;
    public static final int JSON_PARSE_EXCEPTION             = 1005;
    public static final int SSL_HANDSHAKE_EXCEPTION          = 1006;
    public static final int SOCKET_EXCEPTION                 = 1007;
    public static final int PROTOCOL_EXCEPTION               = 1008;
    public static final int IO_EXCEPTION                     = 1009;
    public static final int CLASS_CAST_EXCEPTION             = 1010;
    public static final int ILLEGAL_STATE_EXCEPTION          = 1011;
    public static final int NULL_POINTER_EXCEPTION           = 1012;

    public RemoteException handleException(Throwable throwable){
        RemoteException remoteException = null;

        if(throwable instanceof UnknownHostException)
        {
            String displayMessage = "We can not find the server at "+ RemoteConfiguration.HOST_URL+" If that address is correct, here are three other things you can try :\n" +
                    "\n" +
                    "1) Try again later.\n" +
                    "2) Check your network connection.\n" +
                    "3) If you are connected, Check that "+ AppConstants.APP_NAME+" has internet permission";
            remoteException = new RemoteException(throwable);
            remoteException.setCode(UNKNOWN_HOST_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof ConnectException)
        {
            String displayMessage = "Host and Port combination not valid, Connection refused.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(CONNECT_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof SocketTimeoutException)
        {
            String displayMessage = "Connection timeout error.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(SOCKET_TIMEOUT_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof ConnectTimeoutException)
        {
            String displayMessage = "Connection timeout error.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(CONNECT_TIMEOUT_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof JsonSyntaxException || throwable instanceof JsonSerializer || throwable instanceof NotSerializableException || throwable instanceof ParseException || throwable instanceof MalformedJsonException)
        {
            String displayMessage = "Parsing error! Something went wrong api is not responding properly.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(JSON_PARSE_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof javax.net.ssl.SSLHandshakeException)
        {
            String displayMessage = "javax.net.ssl.SSLHandshakeException, Certificate verification failed.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(SSL_HANDSHAKE_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if (throwable instanceof SocketException)
        {
            String displayMessage = "File stream is closed, download is suspended.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(SOCKET_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if (throwable instanceof ProtocolException)
        {
            String displayMessage = "File stream closed unexpectedly.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(PROTOCOL_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if (throwable instanceof IOException)
        {
            String displayMessage = "File stream error.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(IO_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof ClassCastException)
        {
            String displayMessage = "Type conversion error.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(CLASS_CAST_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof IllegalStateException)
        {
            String displayMessage = "Illegal state exception.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(ILLEGAL_STATE_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof NullPointerException)
        {
            String displayMessage = "Null pointer exception.\n"+throwable.getMessage();
            remoteException = new RemoteException(throwable);
            remoteException.setCode(NULL_POINTER_EXCEPTION);
            remoteException.setDisplayMessage(displayMessage);
        }
        else if(throwable instanceof retrofit2.HttpException)
        {
            retrofit2.HttpException httpException = (retrofit2.HttpException) throwable;

            HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(httpException.code());

            remoteException = new RemoteException(throwable);
            remoteException.setCode(httpStatusCode.value());
            remoteException.setDisplayMessage(httpStatusCode.getReasonPhrase());
        }
        else
        {
            String displayMessage = throwable.getMessage();

            if (displayMessage == null || displayMessage.length() <= 40)
            {
                displayMessage = "Something went wrong, please try again later.";

                remoteException = new RemoteException(throwable);
                remoteException.setCode(NULL_POINTER_EXCEPTION);
                remoteException.setDisplayMessage(displayMessage);
            }
        }

        return remoteException;
    }
}