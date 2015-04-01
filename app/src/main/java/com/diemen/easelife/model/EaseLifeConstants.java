package com.diemen.easelife.model;

import java.io.File;

/**
 * Created by tfs-hitesh on 24/1/15.
 */
public class EaseLifeConstants {

    public final static int CATEGORIES_OBJECT = 1;
    public final static int SUB_CATEGORIES_OBJECT = 2;
    public final static String  PARCELABLE_OBJECT = "parcel";
    public final static String CHOOSE_PICTURE = "SELECT A PICTURE";
    public final static String SELECT_IMAGE_FROM_GALLERY = " Gallery";
    public final static String CLICK_IMAGE_FROM_CAMERA = " Camera";
    public final static String CANCEL  ="Back";

    public final static int REQUEST_CAMERA = 0;
    public final static int SELECT_FILE = 1;

    public final static String ERROR_SELECTING_IMAGE = "Sorry! Could not save image";
    public final static String imagesPath = File.separator
            + "easelife/";

    public final static String ADD_CATEGORY = "Add Category";

    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";
}
