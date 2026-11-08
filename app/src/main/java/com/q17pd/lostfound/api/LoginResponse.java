package com.q17pd.lostfound.api;

public class LoginResponse {

    private String status;
    private String message;
    public String getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public boolean isSuccess() {
        return "login: success".equals(status);
    }
}