package com.example.portfoliobackendapp.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Images")
public class Image {
    @Id
    private String id;
    private String title;
    private String explanation;
    private String filepath;
    private String username;

    // No-arg constructor
    public Image() {}

    public Image(String title, String explanation, String filepath, String username) {
        this.title = title;
        this.explanation = explanation;
        this.filepath = filepath;
        this.username = username;
    }
    @Transient
    public String getPublicUrl() {
        return "/user-images/" + this.filepath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}