package com.diemen.easelife.sqllite;

import android.database.sqlite.SQLiteDatabase;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.Subcategory;
import com.j256.ormlite.support.ConnectionSource;


import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Created by tfs-hitesh on 27/12/14.
 */
public class CategorySQLHelper {

    /*
        The Init method initializes the database onCreate with the static data needed to show during layout inflate
     */
    public static List<Categories> init()
    {
        List<Categories> initCategoryList = new ArrayList<Categories>();
        initCategoryList.add(new Categories(1,"Places", "1", true, 0, "Places I love to visit"));
        initCategoryList.add(new Categories(2,"Movies", "2", true, 0, "Movies I love to watch"));
        initCategoryList.add(new Categories(3,"Music", "3", true, 0, "Music I love to listen to"));
        initCategoryList.add(new Categories(4,"People", "4", true, 0, "My Family and Friends"));
        initCategoryList.add(new Categories(5,"Food", "5", true, 0, "Food i eat"));
        //initCategoryList.add(new Categories(6,"Places", "", true, 0, "Places I love to visit"));

        return initCategoryList;
    }

    public static List<Subcategory> initSubcategory()
    {
        List<Subcategory> initSubcategoryList = new ArrayList<Subcategory>();

        initSubcategoryList.add(new Subcategory(1,2,"Super man","6",0.00,0.00, new Date(),true, "Super man is the Movie i love",0));

        initSubcategoryList.add(new Subcategory(2,4,"Hitesh","8",0.00,0.00, new Date(),true, "Hi, Hitesh! ",0));

        initSubcategoryList.add(new Subcategory(3,4,"Anuj","8",0.00,0.00, new Date(),true, "Hi, Anuj! ",0));

        initSubcategoryList.add(new Subcategory(4,1,"School","9",0.00,0.00, new Date(),true, " i want to go to School  ",0));

        initSubcategoryList.add(new Subcategory(5,1,"Home","7",0.00,0.00, new Date(),true, "I want to go home ",0));

        return initSubcategoryList;
    }

}