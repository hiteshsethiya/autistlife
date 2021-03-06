package com.diemen.easelife.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.Chat;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfs-hitesh on 26/12/14.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private Dao<Categories, Integer> categoryDao = null;
    private Dao<Subcategory, Integer> subcategoriesDao = null;
    private Dao<User, Integer> userDao = null;
    private Dao<Chat, Integer> chatDao = null;

    public DBHelper(Context context)
    {
        super(context,DBConstants.DATABASE_NAME,null,DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try
        {
            TableUtils.createTable(connectionSource,Categories.class);
            TableUtils.createTable(connectionSource, Subcategory.class);
            TableUtils.createTable(connectionSource, Chat.class);
            TableUtils.createTable(connectionSource, User.class);
            init();

        }
        catch(SQLException e)
        {
            Log.e(DBHelper.class.getName(), "Couldn't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        List<String> allSql = new ArrayList<String>();
        try {
            for (String sql : allSql) {
                database.execSQL(sql);
            }
        }
        catch(android.database.SQLException e)
        {
            Log.e(DBHelper.class.getName(),"Could not execute query during upgrade",e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Categories, Integer> getCategoryDao()
    {
        try {
            if (null == categoryDao) {
                categoryDao = getDao(Categories.class);
            }
        }
        catch(SQLException e)
        {
            Log.e(DBHelper.class.getName(),"Unable to get Categories DAO! __|__",e);
            throw new RuntimeException(e);
        }
        return categoryDao;
    }

    public Dao<User, Integer> getUserDao()
    {
        try {
            if (null == userDao) {
                userDao = getDao(User.class);
            }
        }
        catch(SQLException e)
        {
            Log.e(DBHelper.class.getName(),"Unable to get Categories DAO! __|__",e);
            throw new RuntimeException(e);
        }
        return userDao;
    }

    public Dao<Chat, Integer> getChatDao()
    {
        try {
            if (null == chatDao) {
                chatDao = getDao(Chat.class);
            }
        }
        catch(SQLException e)
        {
            Log.e(DBHelper.class.getName(),"Unable to get Categories DAO! __|__",e);
            throw new RuntimeException(e);
        }
        return chatDao;
    }

    public Dao<Subcategory,Integer> getSubcategoriesDao()
    {
        try
        {
            if(null == subcategoriesDao)
            {
                subcategoriesDao = getDao(Subcategory.class);
            }
        }
        catch (SQLException e)
        {
            Log.e(DBHelper.class.getName(),"Unable to get Categories DAO! __|__",e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return subcategoriesDao;
    }

    private void init()
    {
        List<Categories> list =  CategorySQLHelper.init();


        for(Categories category : list) {
            try {
                getCategoryDao().createIfNotExists(category);
            }
            catch (SQLException e)
            {
                Log.e("DBHelper","Could not create category insert "+category.getId(),e);
            }
        }

        List<Subcategory> subcategories = CategorySQLHelper.initSubcategory();

        for(Subcategory subcategory : subcategories)
        {
            try {
                getSubcategoriesDao().createIfNotExists(subcategory);
            }
            catch (SQLException e)
            {
                Log.e("DBHelper","Could not create subcategory insert "+subcategory.getId(),e);
            }
        }
    }

}
