package com.diemen.easelife.easelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.sqllite.DBManager;

import java.util.List;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class SubcategoryActivity extends ActionBarActivity{

     GridView subcategoryGridView;
     Button addButton;
    Integer categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_grid);

        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_subcategory_grid,null);
        addButton = (Button)findViewById(R.id.add_btn);


        categoryId = getIntent().getIntExtra("categoryId",1);
        List<Subcategory> subcategoryList = DBManager.getInstance().getSubcategoryByCategoryId(categoryId);
        Toast.makeText(SubcategoryActivity.this," Likes:"+categoryId, Toast.LENGTH_SHORT).show();


        subcategoryGridView = (GridView) findViewById(R.id.subcategory_grid);
        subcategoryGridView.setAdapter(new SubcategoryImageAdapter(this,categoryId,subcategoryList));

        subcategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RelativeLayout linearLayout = (RelativeLayout)view;
                ImageView clickedImageView = (ImageView)linearLayout.getChildAt(0);

                Subcategory updateCategoryLike = (Subcategory)clickedImageView.getTag();

                if(updateCategoryLike != null) {
                    updateCategoryLike.setLikes(updateCategoryLike.getLikes() + 1);
                    DBManager.getInstance().updateSubcategoryLike(updateCategoryLike);
                }

                Intent userList = new Intent(getApplicationContext(),UserListActivity.class);
                userList.putExtra(Subcategory.SUBCATEGORY_OBJECT,updateCategoryLike);
                startActivity(userList);
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewStuffIntent = new Intent("com.diemen.easelife.easelife.ADDNEWSTUFF");
                addNewStuffIntent.putExtra("object", EaseLifeConstants.SUB_CATEGORIES_OBJECT);
                addNewStuffIntent.putExtra("categoryId",categoryId);
                startActivity(addNewStuffIntent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
     Intent i=new Intent(this, StartActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {

            return true;
        }else if(id == R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
