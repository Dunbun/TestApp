package com.example.testapp;

import com.google.gson.annotations.SerializedName;

public class Comment {
    int postId;
    int id;
    String name;
    String email;
    @SerializedName("body")
    String body;

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }
}
