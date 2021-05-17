package com.retrofit.kit.data.remote.exception;

public class RemoteException extends Exception {

    private int code;
    private String displayMessage;

    public RemoteException(Throwable throwable) {
        super(throwable);
    }

    public RemoteException(int code, String displayMessage) {
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}