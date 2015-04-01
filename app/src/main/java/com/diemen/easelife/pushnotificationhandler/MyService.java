package com.diemen.easelife.pushnotificationhandler;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

import com.diemen.easelife.model.Chat;
import com.diemen.easelife.model.User;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.AppSettings;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by user on 30-01-2015.
 */
public class MyService extends Service implements SensorEventListener {

    // Anxiety Variables
    SensorManager mSensorManager;
    private MyService mShakeDetector;
    private Sensor mAccelerometer;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.0F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount=0;


    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        Log.d("Shaker", "On Sensor Changed is called Anuj");
        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;


            // gForce will be close to 1 when there is no movement.
            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);


            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }


                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }


                mShakeTimestamp = now;
                mShakeCount++;


                mListener.onShake(mShakeCount);
            }
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new MyService();

        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {
                Toast.makeText(getApplicationContext(),"Shake Has Happened",Toast.LENGTH_LONG).show();

                AppSettings aps=new AppSettings(getApplicationContext());

                User user1=aps.getUser1();
                User user2=aps.getUser2();
                ParseUser currentUser = ParseUser.getCurrentUser();
                if(user1!=null)
                {
                    Chat chat=new Chat();
                    String ReceiverPhone=user1.getPhoneNo();
                    String Receiver=user1.getName();
                    chat.setSelf(true);
                    chat.setSender(currentUser.getUsername());
                    chat.setSenderPhone(currentUser.getString("PhoneNumber"));
                    chat.setReceiver(Receiver);
                    chat.setReceiverPhone(ReceiverPhone);
                    chat.setMessage(currentUser.getUsername() + "might be Anxious.You Should se him.");
                    Date date = new Date();
                    DBManager.getInstance().addChat(chat);


                    JSONObject data;
                    try {
                        ParseQuery pQuery = new ParseInstallation().getQuery();
                        pQuery.whereEqualTo("phone", ReceiverPhone);
                        ParsePush pushMessage = new ParsePush();
                        pushMessage.setQuery(pQuery);
                        //Always keep in mind "I am the Sender"
                        //when the user will receive this message he will be the sender;
                        data = new JSONObject("{\"alert\":\"Urgent Message\",\"Message\": \"" + chat.getMessage() + "\",\"ReceiverPhone\": \"" + currentUser.getString("PhoneNumber") + "\",\"Sender\":\"" + Receiver + "\",\"Receiver\":\"" + currentUser.getUsername() + "\" ,\"SenderPhone\":\"" + ReceiverPhone + "\",\"ReceivedOn\":\"" + date.getTime() + "\"}");
                        pushMessage.setMessage("Message From Ease Life");
                        pushMessage.setData(data);
                        pushMessage.sendInBackground();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if(user2!=null)
                {
                    Chat chat=new Chat();
                    String ReceiverPhone=user2.getPhoneNo();
                    String Receiver=user2.getName();
                    chat.setSelf(true);
                    chat.setSender(currentUser.getUsername());
                    chat.setSenderPhone(currentUser.getString("PhoneNumber"));
                    chat.setReceiver(Receiver);
                    chat.setReceiverPhone(ReceiverPhone);
                    chat.setMessage(currentUser.getUsername() + "might be Anxious.You Should se him.");
                    Date date = new Date();
                    DBManager.getInstance().addChat(chat);


                    JSONObject data;
                    try {
                        ParseQuery pQuery = new ParseInstallation().getQuery();
                        pQuery.whereEqualTo("phone", ReceiverPhone);
                        ParsePush pushMessage = new ParsePush();
                        pushMessage.setQuery(pQuery);
                        //Always keep in mind "I am the Sender"
                        //when the user will receive this message he will be the sender;
                        data = new JSONObject("{\"alert\":\"Urgent Message\",\"Message\": \"" + chat.getMessage() + "\",\"ReceiverPhone\": \"" + currentUser.getString("PhoneNumber") + "\",\"Sender\":\"" + Receiver + "\",\"Receiver\":\"" + currentUser.getUsername() + "\" ,\"SenderPhone\":\"" + ReceiverPhone + "\",\"ReceivedOn\":\"" + date.getTime() + "\"}");
                        pushMessage.setMessage("Message From Ease Life");
                        pushMessage.setData(data);
                        pushMessage.sendInBackground();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mSensorManager.registerListener(mShakeDetector,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onDestroy();
    }




}