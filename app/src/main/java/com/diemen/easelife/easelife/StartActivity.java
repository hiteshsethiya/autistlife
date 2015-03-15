package com.diemen.easelife.easelife;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.CheckBoxPreference;
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
import com.diemen.easelife.pushnotificationhandler.MyService;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.AppSettings;
import com.diemen.easelife.util.Util;


import java.util.ArrayList;
import java.util.List;



public class StartActivity extends ActionBarActivity {

    GridView categoriesGridView;
    User AddUser=new User();
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

                    RelativeLayout relative = (RelativeLayout)view;
                    ImageView clickedImageView = (ImageView)relative.getChildAt(0);

                    Categories updateCategoryLike = (Categories)clickedImageView.getTag();

                    if(updateCategoryLike != null && relative.getAnimation() == null) {

                         //go to edit activity
                            //Toast.makeText(StartActivity.this,updateCategoryLike.getDescription(), Toast.LENGTH_SHORT).show();
                            //Move to subcategory event
                            updateCategoryLike.setLikes(updateCategoryLike.getLikes() + 1);
                            DBManager.getInstance().updateCategoryLike(updateCategoryLike);
                            Intent subCategoryMove = new Intent("com.diemen.easelife.easelife.SUBCATEGORYACTIVITY");
                            subCategoryMove.putExtra("categoryId", updateCategoryLike.getId());
                            startActivity(subCategoryMove);
                    }
                    else
                    {
                        Intent addNewStuffIntent = new Intent("com.diemen.easelife.easelife.ADDNEWSTUFF");
                        if(relative.getAnimation() != null)
                        {
                            addNewStuffIntent.putExtra(EaseLifeConstants.PARCELABLE_OBJECT,updateCategoryLike);
                        }
                            addNewStuffIntent.putExtra("object", EaseLifeConstants.CATEGORIES_OBJECT);
                            startActivity(addNewStuffIntent);
                    }
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            Log.e(StartActivity.class.getName(), "Error in StartActivity ", e);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    private static final int PICK_CONTACT = 100;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.edit_category)
        {
            //Util.animateIngridView(categoriesGridView,shakeImages);
            Intent map = new Intent(getApplicationContext(),MapsActivity.class);
startActivity(map);
         /*   Util.changebackground(categoriesGridView,getResources().getColor(R.color.abc_primary_text_disable_only_material_light),
                    getResources().getColor(R.color.lightest_blue));*/
        }
        else if (id == R.id.action_new) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
            return true;
        }else if(id == R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String phoneNumber ="";
                        if ( hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false" ;

                        if (Boolean.parseBoolean(hasPhone))
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                            while (phones.moveToNext())
                            {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }
                        String name = cursor.getString(nameIdx);
                        phoneNumber= phoneNumber.replace(" ","");
                        String Phone=phoneNumber.length()>10?phoneNumber.substring(phoneNumber.length()-10):phoneNumber;

                        if(name!="" && phoneNumber!="") {
                            AddUser.setName(name);
                            AddUser.setPhoneNo(Phone);
                            DBManager.getInstance().addUser(AddUser);
                        }


                        Toast.makeText(getApplicationContext(),"name:"+name +" phone:"+Phone,Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    /* About XML inflator
    XML Layouts in Android need to be Inflated (parsed into View objects) before they are used. getLayoutInflator() gets you an instance of the LayoutInflator that will allow you to manually inflate layouts for specific uses.

    One example being in a Custom ArrayAdapter to populate a ListView with a Custom Layout.

    You need to manually inflate and populate your desired Layout for each individual list item in the ArrayAdapter's overridden getView() method.*/
}
