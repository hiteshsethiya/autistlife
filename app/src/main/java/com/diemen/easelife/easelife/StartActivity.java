package com.diemen.easelife.easelife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.User;
import com.diemen.easelife.sqllite.DBManager;
import com.parse.Parse;
import com.parse.ParseInstallation;


public class StartActivity extends ActionBarActivity {

    GridView categoriesGridView;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Parse.initialize(this, "pBQ7oHoCqFXmzyP4BXQQ1rlyfnzgKxvsCYHRHDMX", "jZy0HekTIFoKFU3fbJENMGkFJDFy3GCsQryrQUKZ");
        ParseInstallation currentInstall=ParseInstallation.getCurrentInstallation();
        currentInstall.put("phone","9742510298");

        DBManager.init(this);

        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_start,null); // R.layout. Name of the XML file to be associated with this class
        categoriesGridView = (GridView) findViewById(R.id.categories_grid);
        categoriesGridView.setAdapter(new CategoriesImageAdapter(this));
        }
        catch (Exception e)
        {
            Log.e(StartActivity.class.getName(), "Error in StartActivity ", e);
        }
    //    setContentView(contentView);
        categoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout linearLayout = (LinearLayout)view;
                ImageView clickedImageView = (ImageView)linearLayout.getChildAt(0);

                Categories updateCategoryLike = (Categories)clickedImageView.getTag();

                if(updateCategoryLike != null) {
                    updateCategoryLike.setLikes(updateCategoryLike.getLikes() + 1);
                    DBManager.getInstance().updateCategoryLike(updateCategoryLike);
                    Toast.makeText(StartActivity.this,updateCategoryLike.getDescription() +" Likes:"+updateCategoryLike.getLikes(), Toast.LENGTH_SHORT).show();
                }
                //Move to subcategory event
                Intent subCategoryMove = new Intent("com.diemen.easelife.easelife.SUBCATEGORYACTIVITY");
                subCategoryMove.putExtra("categoryId",updateCategoryLike.getId());
                startActivity(subCategoryMove);
                finish();
            }
        });
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
        if (id == R.id.action_new) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
            return true;
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



                        Toast.makeText(getApplicationContext(),"name:"+name +" phone:"+phoneNumber,Toast.LENGTH_LONG).show();
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
