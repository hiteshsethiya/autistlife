package com.diemen.easelife.easelife;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hitesh on 14/03/15.
 */
public class MapsActivity extends Activity {

    private static final LatLng sampleLoc = new LatLng(53.558, 9.927);
    private GoogleMap theMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        theMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map_fragment)).getMap();

        Marker sampleMarker = theMap.addMarker(new MarkerOptions().position(sampleLoc).title("Anjum"));

        theMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLoc, 15));

        // Zoom in, animating the camera.
        theMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
