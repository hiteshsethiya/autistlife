package com.diemen.easelife.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.diemen.easelife.model.User;

/*
 * This class is used to store app settings as well configuration
 * It uses shared preferences to store the data.
 */

public class AppSettings {

	private static final String APP_SHARED_PREFS = "EaseLife";
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;
  //  private final String TAG = "AppSettings";

    public AppSettings(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public User getUser1() {
        if(appSharedPrefs.getString("user1", null)==null){
            return null;
        }
        return new User(appSharedPrefs.getString("user1", null),
                appSharedPrefs.getString("phone1", null));
    }

    public User getUser2() {
        if(appSharedPrefs.getString("user2", null)==null){
            return null;
        }
        return new User(appSharedPrefs.getString("user2", null),
                appSharedPrefs.getString("phone2", null));
    }

    public void saveUser1(String name, String phoneNo) {
        prefsEditor.putString("user1", name);
        prefsEditor.putString("phone1", phoneNo);
        prefsEditor.commit();
    }

    public void saveUser2(String name, String phoneNo) {
        prefsEditor.putString("user2", name);
        prefsEditor.putString("phone2", phoneNo);
        prefsEditor.commit();
    }

    public boolean getShakeService(){
        return appSharedPrefs.getBoolean("prefShakeService", false);
    }

    public boolean getLocationService(){
        return appSharedPrefs.getBoolean("prefLocationService", false);
    }
        
}

