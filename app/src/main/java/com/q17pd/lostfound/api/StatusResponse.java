package com.q17pd.lostfound.api;

public class StatusResponse {

    private String status;
    private String message;
    public String getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public boolean isSuccess() {
        return status != null && status.endsWith(": success");
    }
}
