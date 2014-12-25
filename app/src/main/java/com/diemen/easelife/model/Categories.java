package com.diemen.easelife.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

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

    @ForeignCollectionField
    private ForeignCollection<Subcategory> subcategoryList;


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ForeignCollection<Subcategory> getSubcategoryList() {
        return subcategoryList;
    }

    public void setSubcategoryList(ForeignCollection<Subcategory> subcategoryList) {
        this.subcategoryList = subcategoryList;
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
}
