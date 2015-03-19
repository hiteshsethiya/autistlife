package com.diemen.easelife.pushnotificationhandler;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;

/**
 * Created by AnSingh on 3/19/2015.
 */



public class LocationService extends Service implements LocationListener {

    LocationManager locationManager;
    @Override
    public void onLocationChanged(Location location) {
        ParseInstallation currentInstall=ParseInstallation.getCurrentInstallation();
        currentInstall.put("location", new ParseGeoPoint(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,0, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}