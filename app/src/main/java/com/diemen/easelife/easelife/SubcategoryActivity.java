package com.diemen.easelife.easelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.sqllite.DBManager;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class SubcategoryActivity extends ActionBarActivity{

     GridView subcategoryGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_grid);

        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_subcategory_grid,null);

        subcategoryGridView = (GridView) findViewById(R.id.subcategory_grid);
        Integer categoryId = getIntent().getIntExtra("categoryId",1);
        Toast.makeText(SubcategoryActivity.this," Likes:"+categoryId, Toast.LENGTH_SHORT).show();

        subcategoryGridView.setAdapter(new SubcategoryImageAdapter(this,categoryId));

        subcategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout linearLayout = (LinearLayout)view;
                ImageView clickedImageView = (ImageView)linearLayout.getChildAt(0);

                Subcategory updateCategoryLike = (Subcategory)clickedImageView.getTag();

                if(updateCategoryLike != null) {
                    updateCategoryLike.setLikes(updateCategoryLike.getLikes() + 1);
                    DBManager.getInstance().updateSubcategoryLike(updateCategoryLike);
                    Toast.makeText(SubcategoryActivity.this,updateCategoryLike.getDescription() +" Likes:"+updateCategoryLike.getLikes(), Toast.LENGTH_SHORT).show();
                }
                //Move to subcategory event
                Toast.makeText(SubcategoryActivity.this,"Like "+updateCategoryLike.getLikes() + 1,Toast.LENGTH_SHORT).show();
                Intent userList = new Intent(getApplicationContext(),UserListActivity.class);
                startActivity(userList);



            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, StartActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
