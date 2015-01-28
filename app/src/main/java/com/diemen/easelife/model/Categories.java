package com.diemen.easelife.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfs-hitesh on 14/12/14.
 */

@DatabaseTable
public class Categories implements Parcelable{

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
    public static final String LIKES_COLUMN_NAME = "likes";

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

    @Override
    public String toString() {
        return "Categories{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", imageResourcePath='" + imageResourcePath + '\'' +
                ", active=" + active +
                ", likes=" + likes +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return EaseLifeConstants.CATEGORIES_OBJECT;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.id);
        dest.writeString(this.categoryName);
        dest.writeString(this.imageResourcePath);
        dest.writeInt(this.likes);
        dest.writeByte((byte) (this.active ? 1 : 0));
        dest.writeString(this.description);
    }

    public static Categories readFromParcel(Parcel in)
    {
        int id = in.readInt();
        String categoryName = in.readString();
        String imageResourcePath = in.readString();
        int likes = in.readInt();
        boolean active = (boolean)(in.readByte()==1 ? true:false);
        String description = in.readString();

        return new Categories(id,categoryName,imageResourcePath, active,likes,description);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() // (5)
    {
        public Categories createFromParcel(Parcel in) // (6)
        {
            return readFromParcel(in);
        }

        public Categories[] newArray(int size) { // (7)
            return new Categories[size];
        }
    };
}
