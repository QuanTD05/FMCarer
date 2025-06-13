package com.example.fmcarer;

import java.io.Serializable;

public class Post implements Serializable {

    private String id;
    private String userId;
    private String content;
    private String imageUrl;
    private String visibility; // "Gia đình" hoặc "Cộng đồng"

    public Post() {
    }

    public Post(String id, String userId, String content, String imageUrl, String visibility) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
