package com.diemen.easelife.sqllite;

import android.content.Context;
import android.util.Log;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.Chat;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.model.User;
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

    static public DBManager getInstance(Context context) {

        if(dbManager == null)
        {
            init(context);
        }
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

    public void addUser(User user)
    {
        try
        {
            getDbHelper().getUserDao().createOrUpdate(user);
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Method Add User ",e);
            throw new RuntimeException(e);
        }
    }

    public void addChat(Chat chat)
    {
        try
        {
            getDbHelper().getChatDao().createOrUpdate(chat);
        }
        catch(SQLException e)
        {
            Log.e(DBManager.class.getName(),"Method Chat addChat ",e);
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers()
    {
       try{
           List<User> list= getDbHelper().getUserDao().queryForAll();
           return list;
       } catch (SQLException e) {
           Log.e(DBManager.class.getName(),"Get list of all Users ",e);
           throw new RuntimeException(e);
       }
    }

    public List<Chat> getAllChats(String Sender,String Receiver)
    {
        try{
            List<Chat> Senderlist =getDbHelper().getChatDao().queryForEq("SenderPhone",Sender);
            List<Chat> AllChat=new ArrayList<Chat>();


            for(Chat s:Senderlist)
            {
                if(s.getReceiverPhone().equals(Receiver.toString()))
                    AllChat.add(s);
            }

            return AllChat;
        }
        catch (SQLException e){
            Log.e(DBManager.class.getName(),"Get all chats ",e);
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

    public boolean saveCategory(Categories categories)
    {
        boolean status = false;

        try
        {
            getDbHelper().getCategoryDao().createOrUpdate(categories);
            status = true;
        }
        catch(SQLException e)
        {
            status = false;
            Log.e("DBManager","SQL exception while saving category object "+categories.toString(),e);
        }
        return status;
    }

    public boolean saveSubCategory(Subcategory subcategory)
    {
        boolean status = false;

        try
        {
            getDbHelper().getSubcategoriesDao().createOrUpdate(subcategory);
            status = true;
        }
        catch(SQLException e)
        {
            status = false;
            Log.e("DBManager","SQL exception while saving subcategory object "+subcategory.toString(),e);
        }
        return status;
    }

    /*public int getCategoryLike(int id)
    {
        QueryBuilder<Categories,Integer> categoryQB = getDbHelper().getCategoryDao().queryBuilder();
        return 0;
    }*/
}
