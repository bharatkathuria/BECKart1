package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;


public class Review {

    @SerializedName("user_id")
    private int userId;
    @SerializedName("product_id")
    private int productId;

    @SerializedName("user_name")
    private String userName;
    @SerializedName("review_date")
    private String reviewDate;
    @SerializedName("rate")
    private float reviewRate;
    @SerializedName("feedback")
    private String feedback;

    public Review(int userId ,String userName,int productId, float reviewRate, String feedback) {
        this.userId = userId;
        this.userName = userName;
        this.productId = productId;
        this.reviewRate = reviewRate;
        this.feedback = feedback;
    }

    public Review(int userId ,int productId, float reviewRate, String feedback) {
        this.userId = userId;
        this.productId = productId;
        this.reviewRate = reviewRate;
        this.feedback = feedback;
    }

    public Review(String userName ,int productId, float reviewRate, String feedback) {
        this.userName = userName;
        this.productId = productId;
        this.reviewRate = reviewRate;
        this.feedback = feedback;
    }


    public String getUserName() {
        return userName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public float getReviewRate() {
        return reviewRate;
    }

    public String getFeedback() {
        return feedback;
    }

}
