package com.diemen.easelife.easelife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.sqllite.DBHelper;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
    private static int IMG_HEiGHT = 350;

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
            newCategory = new Categories();
            newSubcategory = null;
        }
        else if (whichClass == EaseLifeConstants.SUB_CATEGORIES_OBJECT) {
            viewPlate = "Sub Category";
            whereToGoBack = new Intent(this, SubcategoryActivity.class);
            newSubcategory = new Subcategory();
            newCategory = null;
            categoryId = getIntent().getIntExtra("categoryId", 0);
        }
        else {
            viewPlate = "User";
            whereToGoBack = new Intent(this, UserListActivity.class);
        }
        nameEditText.setHint(viewPlate + " Name");
        descriptionEditText.setHint(viewPlate + " Description");
        addButton.setText("Add " + viewPlate);

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

                save();
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("ActivityResult","***************************************");
        Bitmap bm = null;
        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
        File requiredFile = null;

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
                    try
                    {
                        bm = BitmapFactory.decodeFile(requiredFile.getAbsolutePath(),
                                btmapOptions);
                        selectedImagePath = Uri.fromFile(requiredFile).toString();
                    }
                    catch(Exception e)
                    {
                        Log.e("AddNewStuff","---- onActivityResult- Request Camera",e);
                    }


            } else if (requestCode == EaseLifeConstants.SELECT_FILE)
            {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, AddNewStuff.this);
                selectedImagePath = selectedImageUri.toString();
                requiredFile = new File(selectedImagePath);
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
            }
            bm = Bitmap.createScaledBitmap(bm,IMG_WIDTH,IMG_HEiGHT,true);
            selectImageBtn.setImageBitmap(bm);
            saveImage(requiredFile);
            if(requiredFile != null) {
                requiredFile.delete();
            }
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
        startActivity(whereToGoBack);
        finish();
    }

    public void saveImage(File sourceFile)
    {
        String path = android.os.Environment
                .getExternalStorageDirectory()
                + EaseLifeConstants.imagesPath;

        OutputStream fOut = null;
        File newFile = new File(path, String.valueOf(System
                .currentTimeMillis()) + ".jpg");
        selectedImagePath = newFile.getAbsolutePath();
        try {
            if(!newFile.exists())
            {
                newFile.getParentFile().mkdirs();
            }

            InputStream in = new FileInputStream(sourceFile);
            fOut = new FileOutputStream(newFile);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                fOut.write(buf, 0, len);
            }
            in.close();
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void save()
    {
        if(newCategory != null)
        {
            DBManager.getInstance().saveCategory(
                    new Categories(name,selectedImagePath,true,0,description)
            );
        }
        else if(newSubcategory != null)
        {
            DBManager.getInstance().saveSubCategory(
                    new Subcategory(categoryId, name,selectedImagePath, latitude, longitude,new Date(),true,description,0)
            );
        }

    }
}
