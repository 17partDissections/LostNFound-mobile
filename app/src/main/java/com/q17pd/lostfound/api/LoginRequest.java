package com.q17pd.lostfound.api;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("phone_num")
    private final String phoneNum;
    @SerializedName("passwd")
    private final String passwd;
    public LoginRequest(String phoneNum, String passwd) {
        this.phoneNum = phoneNum;
        this.passwd = passwd;
    }
}