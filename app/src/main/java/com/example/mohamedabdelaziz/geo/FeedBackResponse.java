package com.example.mohamedabdelaziz.geo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedBackResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("feedback")
    @Expose
    private List<Feedback> feedback = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }


    public static class Feedback {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("medcine_name")
        @Expose
        private String medcineName;
        @SerializedName("user_name")
        @Expose
        private String userName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMedcineName() {
            return medcineName;
        }

        public void setMedcineName(String medcineName) {
            this.medcineName = medcineName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }
}