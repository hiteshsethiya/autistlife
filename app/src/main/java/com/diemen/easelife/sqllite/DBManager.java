package com.diemen.easelife.sqllite;

import android.content.Context;
import android.util.Log;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.Subcategory;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfs-hitesh on 26/12/14.
 */
public class DBManager {

    private static DBManager dbManager;

    private DBHelper dbHelper;

    static public void init(Context context)
    {
        if(null == dbManager)
        {
            dbManager = new DBManager(context);
        }
    }

    static public DBManager getInstance() {
        return dbManager;
    }

    public DBManager(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    public static DBManager getDbManager() {
        return dbManager;
    }

    public static void setDbManager(DBManager dbManager) {
        DBManager.dbManager = dbManager;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Categories> getAllCategories()
    {
        List<Categories> categories = null;

        try
        {
            QueryBuilder<Categories,Integer> queryBuilder = getDbHelper().getCategoryDao().queryBuilder();
            queryBuilder.orderBy(Categories.LIKES_COLUMN_NAME,false);

            categories = queryBuilder.query();
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Error in getting all the categories",e);
            throw new RuntimeException(e);
        }
        return categories;
    }

    public void updateCategoryLike(Categories categories)
    {

        try
        {
            getDbHelper().getCategoryDao().createOrUpdate(categories);
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Method: updateCategoryLike() Error in updating category likes "+categories.toString(),e);
            throw new RuntimeException(e);
        }
    }

    public void updateSubcategoryLike(Subcategory subcategory)
    {
        try
        {
            getDbHelper().getSubcategoriesDao().createOrUpdate(subcategory);
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Method: updateCategoryLike() Error in updating category likes "+subcategory.toString(),e);
            throw new RuntimeException(e);

        }
    }

    public List<Subcategory> getSubcategoryByCategoryId(int id)
    {
        List<Subcategory> list = new ArrayList<Subcategory>();

        try
        {
            QueryBuilder<Subcategory,Integer> qb = getDbHelper().getSubcategoriesDao().queryBuilder();

            qb.where().eq(Subcategory.SUBCATEGORY_CATEGORY_ID,id);
            qb.orderBy(Subcategory.COL_NAME_LIKES,false);

            list = qb.query();
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Error while fetching subcategory by categoryid: "+id,e);
        }
        return list;
    }
    /*public int getCategoryLike(int id)
    {
        QueryBuilder<Categories,Integer> categoryQB = getDbHelper().getCategoryDao().queryBuilder();
        return 0;
    }*/
}
