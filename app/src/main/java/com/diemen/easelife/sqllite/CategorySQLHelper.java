package com.diemen.easelife.sqllite;

import android.database.sqlite.SQLiteDatabase;

import com.diemen.easelife.model.Categories;
import com.j256.ormlite.support.ConnectionSource;

import java.util.ArrayList;
import java.util.List;

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
        initCategoryList.add(new Categories(1,"Places", "R.drawable.sample_2", true, 0, "Places I love to visit"));
        initCategoryList.add(new Categories(2,"Movies", "R.drawable.sample_3", true, 0, "Movies I love to watch"));
        initCategoryList.add(new Categories(3,"Music", "R.drawable.sample_4", true, 0, "Music I love to listen to"));
        /*Categories places = new Categories("Places", "R.drawable.sample_5", true, 0, "Places I love to visit");
        Categories places = new Categories("Places", "R.drawable.sample_6", true, 0, "Places I love to visit");
        Categories places = new Categories("Places", "R.drawable.sample_7", true, 0, "Places I love to visit");*/

        return initCategoryList;
    }
}
