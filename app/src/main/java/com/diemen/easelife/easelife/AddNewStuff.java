package com.diemen.easelife.easelife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Created by tfs-hitesh on 24/1/15.
 */
public class AddNewStuff extends ActionBarActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private Button addButton;
    private ImageButton selectImageBtn;
    private Categories newCategory = null;
    private Subcategory newSubcategory = null;
    private String selectedImagePath;
    private String viewPlate;
    private AlertDialog dialog;
    private static Intent whereToGoBack;
    private static int IMG_WIDTH = 350;
    private static int IMG_HEIGHT = 350;
    private boolean isEdit = false;
    private String name;
    private String description;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_stuff);

        nameEditText = (EditText) findViewById(R.id.new_name);
        descriptionEditText = (EditText) findViewById(R.id.new_description);
        addButton = (Button) findViewById(R.id.add_new_btn);
        selectImageBtn = (ImageButton) findViewById(R.id.select_image);

        int whichClass = getIntent().getIntExtra("object", 0);

        if (whichClass == EaseLifeConstants.CATEGORIES_OBJECT) {
            viewPlate = "Category";
            whereToGoBack = new Intent(this, StartActivity.class);
            newCategory = getIntent().getParcelableExtra(EaseLifeConstants.PARCELABLE_OBJECT);
            if(newCategory == null)
            {
                newCategory = new Categories();
            }
            else
            {
                setDetails();
            }
        }
        else if (whichClass == EaseLifeConstants.SUB_CATEGORIES_OBJECT) {
            viewPlate = "Subcategory";
            categoryId = getIntent().getIntExtra("categoryId", 1);
            whereToGoBack = new Intent(this, SubcategoryActivity.class);
            whereToGoBack.putExtra("categoryId",categoryId);
            newSubcategory = getIntent().getParcelableExtra(EaseLifeConstants.PARCELABLE_OBJECT);
            if(newSubcategory == null)
            {
                newSubcategory = new Subcategory();
            }
            else
            {
                setSubcategoryDetails();
            }

        }
        else {
            viewPlate = "User";
            whereToGoBack = new Intent(this, UserListActivity.class);
        }
        nameEditText.setHint(viewPlate + " Name");
        descriptionEditText.setHint(viewPlate + " Description");
        viewPlate = isEdit == true ? "Save "+viewPlate : "Add "+viewPlate;
        addButton.setText(viewPlate);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameEditText.getText().toString().trim().length() < 2) {
                    Util.animateBackgroundColor(nameEditText, getResources().getColor(R.color.material_blue_grey_800), getResources().getColor(R.color.red_color));
                    Util.animateBackgroundColor(nameEditText, getResources().getColor(R.color.red_color), getResources().getColor(R.color.white));
                    Toast.makeText(AddNewStuff.this,"Please enter a "+viewPlate+" name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedImagePath == null || selectedImagePath.length() == 0)
                {
                    Toast.makeText(AddNewStuff.this,"You haven't selected any image", Toast.LENGTH_SHORT).show();
                }
                name = nameEditText.getText().toString().trim();
                description = descriptionEditText.getText().toString().trim();

                save();
//                Toast.makeText(AddNewStuff.this,"Added a new "+viewPlate, Toast.LENGTH_SHORT).show();
                goBack();
            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewStuff.this);
                builder.setTitle(EaseLifeConstants.CHOOSE_PICTURE);
                builder.setIcon(R.drawable.select_image);

                ListView list=new ListView(AddNewStuff.this);

                list.setAdapter(new AlertDialogListViewImageAdapter(AddNewStuff.this));

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("AddNewStuff","Item Clicked : "+position+" id: "+id);

                        if (position == 0) {

                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    EaseLifeConstants.SELECT_FILE);

                        } else if (position == 1) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Date date = new Date();
                            selectedImagePath = Long.toString(date.getTime());
                            File f = new File(android.os.Environment
                                    .getExternalStorageDirectory(),selectedImagePath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, EaseLifeConstants.REQUEST_CAMERA);
                        } dialog.dismiss();
                    }
                });

                builder.setView(list);
                dialog=builder.create();
                dialog.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_stuff, menu);
        if(isEdit == false) {
                MenuItem delete = menu.findItem(R.id.delete_action);
                delete.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Get the id of the item that has been clicked and call events
         */

        int id = item.getItemId();
        if(id == R.id.delete_action)
        {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ActivityResult","***************************************");
        Bitmap bm = null;
        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
        btmapOptions.inSampleSize = 2;
        File requiredFile = null;

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == EaseLifeConstants.REQUEST_CAMERA) {

                    requiredFile = new File(Environment.getExternalStorageDirectory()
                            .toString());
                    for (File fileIterator : requiredFile.listFiles()) {
                        if (fileIterator.getName().equals(selectedImagePath)) {
                            requiredFile = fileIterator;
                            break;
                        }
                    }
                    try {
                        bm = BitmapFactory.decodeFile(requiredFile.getAbsolutePath(),
                                btmapOptions);
                        selectedImagePath = Uri.fromFile(requiredFile).toString();
                    } catch (Exception e) {
                        Log.e("AddNewStuff", "---- onActivityResult- Request Camera", e);
                    }


                } else if (requestCode == EaseLifeConstants.SELECT_FILE) {
                    Uri selectedImageUri = data.getData();
                    String tempPath = getPath(selectedImageUri, AddNewStuff.this);
                    selectedImagePath = tempPath;
                    requiredFile = new File(selectedImagePath);
                    bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                }
                bm = Bitmap.createScaledBitmap(bm, IMG_WIDTH, IMG_HEIGHT, true);
                selectImageBtn.setImageBitmap(bm);

                //create a file to write bitmap data
                File sd = Environment.getExternalStorageDirectory();
                String fileName = "el"+String.valueOf(System.currentTimeMillis())+".jpg";

                File destination= new File(sd+EaseLifeConstants.imagesPath, fileName);

                destination.createNewFile();

//Convert bitmap to byte array

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 10 , bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(destination);
                fos.write(bitmapdata);

               // saveImage(requiredFile);
               /* if(requiredFile != null)
                {
                    requiredFile.delete();
                }*/
                selectedImagePath = fileName;
                fos.close();
            }
        }
        catch(Exception e)
        {
            Log.e("AddNewStuff"," activity result() Error - ",e);
            Toast.makeText(AddNewStuff.this,EaseLifeConstants.ERROR_SELECTING_IMAGE,Toast.LENGTH_LONG).show();

        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }


    private void save()
    {
        if(newCategory != null)
        {
            if(isEdit == false) {
                newCategory = new Categories(name, selectedImagePath, true,0, description);
            }
            else
            {
                newCategory.setCategoryName(name);
                newCategory.setDescription(description);
                newCategory.setImageResourcePath(selectedImagePath);
                newCategory.setActive(true);
            }
            newCategory.save(this);
        }
        else if(newSubcategory != null)
        {
            if(isEdit == false) {
                newSubcategory = new Subcategory(categoryId, name, selectedImagePath, latitude, longitude, new Date(), true, description, 0);
            }
            else
            {
                newSubcategory.setActive(true);
                newSubcategory.setCategoryId(categoryId);
                newSubcategory.setDescription(description);
                newSubcategory.setSubcategoryName(name);
                newSubcategory.setLatitude(latitude);
                newSubcategory.setLongitude(longitude);
                newSubcategory.setCreatedAt(new Date());
                newSubcategory.setLikes(0);
                newSubcategory.setImagePath(selectedImagePath);
            }
            newSubcategory.save(this);
        }

    }


    public void goBack()
    {
        if(name != null && name.length() > 0)
        {
            CategoriesImageAdapter.notifyDataSetChangeIdentifier++;
        }
        startActivity(whereToGoBack);
        finish();
    }

    void setDetails()
    {
        isEdit = true;
        nameEditText.setText(newCategory.getCategoryName());
        descriptionEditText.setText(newCategory.getDescription());
        selectedImagePath = newCategory.getImageResourcePath();
        if(newCategory.getImageResourcePath() == null)
        {
            selectImageBtn.setImageResource(R.drawable.imagenotselected);
        }
        else if (Util.isInteger(newCategory.getImageResourcePath())) {

            selectImageBtn.setImageResource(
                    CategoriesImageAdapter.categoriesThumbHM.get(Integer.parseInt(newCategory.getImageResourcePath())));
        } else {
            File sd = Environment.getExternalStorageDirectory();
            File image = new File(sd + EaseLifeConstants.imagesPath, newCategory.getImageResourcePath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            selectImageBtn.setImageBitmap(bitmap);
        }
    }

    void setSubcategoryDetails()
    {
        isEdit = true;
        nameEditText.setText(newSubcategory.getSubcategoryName());
        descriptionEditText.setText(newSubcategory.getDescription());
        selectedImagePath = newSubcategory.getImagePath();
        if(newSubcategory.getImagePath() == null)
        {
            selectImageBtn.setImageResource(R.drawable.imagenotselected);
        }
        else if (Util.isInteger(newSubcategory.getImagePath())) {

            selectImageBtn.setImageResource(
                    SubcategoryImageAdapter.subcategoriesThumbHM.get(Integer.parseInt(newSubcategory.getImagePath())));
        } else {
            File sd = Environment.getExternalStorageDirectory();
            File image = new File(sd + EaseLifeConstants.imagesPath, newSubcategory.getImagePath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            selectImageBtn.setImageBitmap(bitmap);
        }

    }
}
