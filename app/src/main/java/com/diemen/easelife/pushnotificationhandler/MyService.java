package com.diemen.easelife.pushnotificationhandler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by user on 30-01-2015.
 */
public class MyService extends Service implements SensorEventListener {

    MediaPlayer favsong;
    private SensorManager mgr;
    private List<Sensor> sensorList;
    SensorManager mSensorManager;
    Shaker mSensorListener;
    private Sensor mAccelerometer;
    SensorManager mShakeDetector;


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      //  mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_UI,new Handler());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mgr.unregisterListener(this);
        super.onDestroy();
    }
}
