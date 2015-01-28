package com.diemen.easelife.easelife;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.diemen.easelife.model.EaseLifeConstants;

import java.io.File;

/**
 * Created by tfs-hitesh on 25/1/15.
 */
public class ImageSelectAlertDialogActivity extends ListActivity{

    public static final String RESULT_CODE = "result_code";



    public static final int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new AlertDialogListViewImageAdapter(ImageSelectAlertDialogActivity.this));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
