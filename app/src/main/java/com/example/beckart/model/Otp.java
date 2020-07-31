package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;

public class Otp {

    @SerializedName("otp")
    private String otp;
    @SerializedName("email")
    private String email;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public Otp(String otp, String email, boolean error, String message) {
        this.otp = otp;
        this.email = email;
        this.error = error;
        this.message = message;
    }

    public Otp(String message) {
        this.message = message;
        this.error = true;
    }

    public String getOtp() {
        return otp;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }
}
