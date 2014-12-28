package com.diemen.easelife.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfs-hitesh on 14/12/14.
 */

@DatabaseTable
public class Categories {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String categoryName;

    @DatabaseField
    private String imageResourcePath;

    @DatabaseField
    private boolean active;

    @DatabaseField
    private int likes; // number of clicks

    @DatabaseField
    private String description;

    public Categories(){}

    public Categories( String categoryName, String imageResourcePath, boolean active, int likes, String description) {
        this.categoryName = categoryName;
        this.imageResourcePath = imageResourcePath;
        this.active = active;
        this.likes = likes;
        this.description = description;
    }

    public Categories(int id, String categoryName, String imageResourcePath, boolean active, int likes, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.imageResourcePath = imageResourcePath;
        this.active = active;
        this.likes = likes;
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageResourcePath() {
        return imageResourcePath;
    }

    public void setImageResourcePath(String imageResourcePath) {
        this.imageResourcePath = imageResourcePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
