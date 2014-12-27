package com.diemen.easelife.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfs-hitesh on 25/12/14.
 */

@DatabaseTable
public class Subcategory {

    @DatabaseField
    private int id;

    @DatabaseField
    private String subcategoryName;

    @DatabaseField
    private String imagePath;

    @DatabaseField
    private String latLong;

    @DatabaseField
    private String createdAt;

    @DatabaseField
    private String updatedAt;

    @DatabaseField
    private boolean active;

    @DatabaseField
    private String description;

    @ForeignCollectionField
    private ForeignCollection<Categories> categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Categories> getCategories() {

        ArrayList<Categories> subcategoryArrayList = new ArrayList<Categories>();

        for(Categories item : categories)
        {
            subcategoryArrayList.add(item);
        }

        return subcategoryArrayList;
    }
    public void setCategories(ForeignCollection<Categories> categories) {
        this.categories = categories;
    }
}
