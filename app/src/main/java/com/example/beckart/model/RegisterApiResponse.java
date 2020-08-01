package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;

public class RegisterApiResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("User")
    private com.example.beckart.model.User user;

    private int id;


    public RegisterApiResponse(User user) {
        this.user = user;
    }
    public RegisterApiResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public com.example.beckart.model.User getUser() {
        return user;
    }

}
