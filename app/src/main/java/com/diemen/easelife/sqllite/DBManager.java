package com.diemen.easelife.sqllite;

import android.content.Context;
import android.util.Log;

import com.diemen.easelife.model.Categories;

import java.sql.SQLException;
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

    public DBManager(Context context)
    {
        dbManager = new DBManager(context);
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
            categories = getDbHelper().getCategoryDao().queryForAll();
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Error in getting all the categories",e);
            throw new RuntimeException(e);
        }
        return categories;
    }
}
