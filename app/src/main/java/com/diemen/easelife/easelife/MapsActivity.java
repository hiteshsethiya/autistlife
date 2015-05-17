package com.diemen.easelife.easelife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.diemen.easelife.model.EaseLifeConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hitesh on 14/03/15.
 */
public class MapsActivity extends Activity implements
        ConnectionCallbacks,OnConnectionFailedListener{

    private Context context;
    private ImageButton setCurrentLocationButton;

    public static final String TAG = "MapsActivity";
    private LocationManager locationManager = null;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private LatLng mCurrentLocation;

    private Date locationLastModifiedAt;
    private static final LatLng EQUATOR = new LatLng(0.0, 0.0);
    private GoogleMap theMap;

    private boolean isGpsEnabled = false, isNetworkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        context = MapsActivity.this;

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        setupMapIfNeeded();

        if(getIntent().getStringExtra("object") != null && getIntent().getStringExtra("object").equals(EaseLifeConstants.ISCHATACTIVITY)) {
            /*
            If we get a user's location from chats activity then show the user's location else try to show the Current location of the device
             */

            mCurrentLocation = new LatLng(getIntent().getDoubleExtra(EaseLifeConstants.LATITUDE, 0.0),
                    getIntent().getDoubleExtra(EaseLifeConstants.LONGITUDE, 0.0));

            locationLastModifiedAt = (Date)getIntent().getSerializableExtra(EaseLifeConstants.LAST_LOCATION_UPDATE);


            if(theMap == null)
            {
                Toast.makeText(MapsActivity.this,"Unable to create Maps",Toast.LENGTH_SHORT).show();
                setupMapIfNeeded();

            }
            theMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 18.0f));
            addCustomMarker("Anuj Kumar Singh");

        }
        else {
            try {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                buildGoogleApiClient();
                mGoogleApiClient.connect();
                showAlertDialog();

            } catch (Exception e) {
                Log.e(TAG, "Exception in checking if GPS is enabled or network is enabled", e);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mCurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            setupMapIfNeeded();
            setUpMap();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show();
    }

    private void setupMapIfNeeded()
    {
        if(theMap == null)
        {
            theMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map_fragment)).getMap();
        }
    }

    /**
     * Use this function to add a marker at current location
     */
    private void setUpMap() {
        theMap.addMarker(new MarkerOptions()
                .position(mCurrentLocation)
                .title("You are here")
                .draggable(true));

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(mCurrentLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        theMap.moveCamera(center);
        theMap.animateCamera(zoom);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        setupMapIfNeeded();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getStringExtra("object") == null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void showAlertDialog()
    {
        if(!isGpsEnabled && !isNetworkEnabled) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps

                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    if(!isGpsEnabled)
                    {
                        finish();
                    }
                }
            });
            dialog.show();
        }
        }


    public void addCustomMarker(String userName)
    {
        Bitmap.Config conf  = Bitmap.Config.ARGB_8888;
        Bitmap theInjectedBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.user);
        Bitmap theBitmap = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas = new Canvas(theBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(Bitmap.createScaledBitmap(theInjectedBitmap, 80, 80, false), 0, 0, null);

        //add marker to Map

        StringBuilder modifedAtText = new StringBuilder(getString(R.string.last_location_modified_at)+" ");
        String lastLocModifiedAt = getlastLocationUpdatedDate();
        if(lastLocModifiedAt == null)
        {
            lastLocModifiedAt = "Time NA";
        }
        modifedAtText.append(lastLocModifiedAt);

        theMap.addMarker(new MarkerOptions().position(mCurrentLocation)
                .snippet(modifedAtText.toString())
                .title(userName)
                .icon(BitmapDescriptorFactory.fromBitmap(theBitmap))
                .draggable(false)
                .anchor(0.5f, 1));

        // Specifies the anchor to be at a particular point in the marker image.
    }


    private String getlastLocationUpdatedDate()
    {
        String modifiedAtDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd, yyyy HH:mm:ss a");
            modifiedAtDate = formatter.format(locationLastModifiedAt);
        }
        catch(Exception e)
        {
            Log.e(TAG," Unable to parse locationLastModifiedAt",e);
        }
        return modifiedAtDate;
    }
}