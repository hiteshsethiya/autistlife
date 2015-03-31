package com.diemen.easelife.easelife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.model.User;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.util.List;



public class StartActivity extends ActionBarActivity {

    GridView categoriesGridView;

    List<Categories> list;
    CategoriesImageAdapter adapter;
    Animation shakeImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            DBManager.init(this);

            categoriesGridView = (GridView) findViewById(R.id.categories_grid);

            list = DBManager.getInstance().getAllCategories();
            adapter = new CategoriesImageAdapter(this,list);

            categoriesGridView.setAdapter(adapter);

            shakeImages = AnimationUtils.loadAnimation(this,R.anim.shakeimage);

            categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    RelativeLayout relative = (RelativeLayout) view; //Get the Layout
                    ImageView clickedImageView = (ImageView) relative.getChildAt(0); //Get the child Image View

                    Categories updateCategoryLike = (Categories) clickedImageView.getTag(); // Get the TAG which was set during adding the image in the adapter

                    // Update the Likes on the Item clicked
                    updateCategoryLike.setLikes(updateCategoryLike.getLikes() + 1);
                    DBManager.getInstance().updateCategoryLike(updateCategoryLike);

                    //Move to subcategory event
                    Intent subCategoryMove = new Intent("com.diemen.easelife.easelife.SUBCATEGORYACTIVITY");
                    subCategoryMove.putExtra("categoryId", updateCategoryLike.getId());

                    startActivity(subCategoryMove);
                    finish(); // Finish the current activity
                }
            });

            /**
             * Implementing Long press on item for editing that particular Item
             */
            categoriesGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    RelativeLayout relative = (RelativeLayout)view;
                    ImageView clickedImageView = (ImageView)relative.getChildAt(0);
                    Intent addNewStuffIntent = new Intent("com.diemen.easelife.easelife.ADDNEWSTUFF");

                    Categories updateCategoryLike = (Categories)clickedImageView.getTag();
                    addNewStuffIntent.putExtra(EaseLifeConstants.PARCELABLE_OBJECT,updateCategoryLike);

                    addNewStuffIntent.putExtra("object", EaseLifeConstants.CATEGORIES_OBJECT);
                    startActivity(addNewStuffIntent);
                    finish();
                    return true;
                }
            });
        }
        catch (Exception e)
        {
            Log.e(StartActivity.class.getName(), "Error in StartActivity ", e);
        }
    }


    /* All overridden methods start here */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    /* All overridden methods end here */

    /* About XML inflator
    XML Layouts in Android need to be Inflated (parsed into View objects) before they are used. getLayoutInflator() gets you an instance of the LayoutInflator that will allow you to manually inflate layouts for specific uses.

    One example being in a Custom ArrayAdapter to populate a ListView with a Custom Layout.

    You need to manually inflate and populate your desired Layout for each individual list item in the ArrayAdapter's overridden getView() method.*/
}
