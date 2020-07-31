package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;

public class GenericServerResponse {

    @SerializedName("error")
    boolean error;
    @SerializedName("message")
    String message;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
