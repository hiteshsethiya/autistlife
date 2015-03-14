package com.diemen.easelife.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.diemen.easelife.sqllite.DBManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfs-hitesh on 25/12/14.
 */

@DatabaseTable
public class Subcategory implements Parcelable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int categoryId;
    public static final String SUBCATEGORY_CATEGORY_ID = "categoryId";

    @DatabaseField
    private String subcategoryName;

    @DatabaseField
    private String imagePath;

    @DatabaseField
    private double latitude;

    @DatabaseField
    private double longitude;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-EEEE hh:mm:ss")
    private Date createdAt;

    @DatabaseField
    private boolean active;

    @DatabaseField
    private String description;

    @DatabaseField
    private int likes;
    public static final String COL_NAME_LIKES = "likes";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Subcategory(){}

    public Subcategory(int id, int categoryId, String subcategoryName, String imagePath,double latitude,
                       double longitude, Date createdAt, boolean active, String description, int likes) {
        this.id = id;
        this.categoryId = categoryId;
        this.subcategoryName = subcategoryName;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.active = active;
        this.description = description;
        this.likes = likes;
    }

    public Subcategory(int categoryId, String subcategoryName, String imagePath, double latitude,
                       double longitude, Date createdAt, boolean active, String description, int likes) {
        this.categoryId = categoryId;
        this.subcategoryName = subcategoryName;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.active = active;
        this.description = description;
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", subcategoryName='" + subcategoryName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", createdAt=" + createdAt +
                ", active=" + active +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                '}';
    }

    @Override
    public int describeContents() {
        return EaseLifeConstants.SUB_CATEGORIES_OBJECT;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.categoryId);
        dest.writeString(this.subcategoryName);
        dest.writeString(this.getImagePath());
        dest.writeInt(this.likes);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeLong(this.createdAt.getTime());
        dest.writeByte((byte) (this.active ? 1 : 0));
        dest.writeString(this.description);
    }
    public static Subcategory readFromParcel(Parcel in)
    {
        int id = in.readInt();
        int categoryId = in.readInt();
        String subcategoryName = in.readString();
        String imagePath = in.readString();
        int likes = in.readInt();
        Double latitude = in.readDouble();
        Double longitude = in.readDouble();
        long createdAt = in.readLong();
        boolean active = (boolean)(in.readByte()==1 ? true:false);
        String description = in.readString();

        return new Subcategory(id,categoryId,subcategoryName,imagePath, latitude,
                    longitude,new Date(createdAt), active, description, likes);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() // (5)
    {
        public Subcategory createFromParcel(Parcel in) // (6)
        {
            return readFromParcel(in);
        }

        public Subcategory[] newArray(int size) { // (7)
            return new Subcategory[size];
        }
    };

    public boolean save(Context context)
    {
        return DBManager.getInstance(context).saveSubCategory(this);
    }

    public static final String SUBCATEGORY_OBJECT = "subcategoryObject";

}