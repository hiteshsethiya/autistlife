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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tfs-hitesh on 24/1/15.
 */
public class AddNewStuff extends ActionBarActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private Button addButton;
    private ImageButton selectImageBtn;
    private Categories category = null;
    private Subcategory subcategory = null;
    private String viewPlate = "First Text Field";
    private AlertDialog dialog;
    private static Intent whereToGoBack;
    private static int IMG_WIDTH = 350;
    private static int IMG_HEiGHT = 350;

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

        } else if (whichClass == EaseLifeConstants.SUB_CATEGORIES_OBJECT) {
            viewPlate = "Sub Category";
            whereToGoBack = new Intent(this, SubcategoryActivity.class);
        } else {
            viewPlate = "User";
            whereToGoBack = new Intent(this, UserListActivity.class);
        }
        nameEditText.setHint(viewPlate + " Name");
        descriptionEditText.setHint(viewPlate + " Description");
        addButton.setText("Add " + viewPlate);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameEditText != null || nameEditText.getText().length() < 2) {
                    nameEditText.setBackgroundColor(getResources().getColor(R.color.red_color));
                    Toast.makeText(AddNewStuff.this, "  cannot be left blank:", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = nameEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
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
                            File f = new File(android.os.Environment
                                    .getExternalStorageDirectory(), "temp.jpg");
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
        Log.e("ActivyResult","***************************************");


        if (resultCode == RESULT_OK) {
            if (requestCode == EaseLifeConstants.REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    bm = Bitmap.createScaledBitmap(bm, IMG_WIDTH,IMG_HEiGHT, true);
                    selectImageBtn.setImageBitmap(bm);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 128, 128);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == EaseLifeConstants.SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, AddNewStuff.this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                bm = Bitmap.createScaledBitmap(bm,IMG_WIDTH,IMG_HEiGHT,true);
                selectImageBtn.setImageBitmap(bm);
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


}
