package com.diemen.easelife.easelife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.diemen.easelife.model.User;
import com.diemen.easelife.pushnotificationhandler.LocationService;
import com.diemen.easelife.pushnotificationhandler.MyService;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.AppSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VVekariya on 30-Jan-15.
 */
public class SettingsActivity extends PreferenceActivity {
    private final String TAG = "SettingsActivity";
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    ListPreference listPrefUser1;
    ListPreference listPrefUser2;
    CheckBoxPreference shakePref;
    CheckBoxPreference locationPref;
    private AppSettings appPrefs;

    CharSequence entries[];
    CharSequence entryValues[];
    List<User> categoryList;
    Intent intent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        appPrefs = new AppSettings(getApplicationContext());
        listPrefUser1 = (ListPreference) findPreference("prefUser1");
        listPrefUser2 = (ListPreference) findPreference("prefUser2");
        shakePref=(CheckBoxPreference)findPreference("prefShakeService");
        locationPref=(CheckBoxPreference)findPreference("prefLocationService");

        User user1 = appPrefs.getUser1();
        if(user1 != null){
            listPrefUser1.setTitle(user1.getName());
        }

        User user2 = appPrefs.getUser2();
        if(user2 != null){
            listPrefUser2.setTitle(user2.getName());
        }

        if (listPrefUser1 != null) {
            categoryList = DBManager.getInstance().getAllUsers();
            Log.i(TAG, "size:" + categoryList.size());
            entries = new String[categoryList.size()];
            entryValues = new String[categoryList.size()];
            int i = 0;
            for (User category : categoryList) {
                entries[i] = category.getName();
                entryValues[i] = Integer.toString(i);
                i++;
            }
            listPrefUser1.setEntries(entries);
            listPrefUser1.setEntryValues(entryValues);
            listPrefUser2.setEntries(entries);
            listPrefUser2.setEntryValues(entryValues);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        shakePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean shakebool=Boolean.parseBoolean(newValue.toString());
                if(shakebool==true)
                {
                    intent = new Intent(getApplicationContext(),MyService.class);
                    startService(intent);
                    shakePref.setChecked(shakebool);
                    Toast.makeText(getApplicationContext(),"Anxiety Service Has Started",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    stopService(intent);
                    shakePref.setChecked(shakebool);
                    Toast.makeText(getApplicationContext(),"Anxiety Service Has Stopped",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        locationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean locbool=Boolean.parseBoolean(newValue.toString());
                if(locbool==true)
                {
                    intent = new Intent(getApplicationContext(),LocationService.class);
                    startService(intent);
                    locationPref.setChecked(locbool);
                    Toast.makeText(getApplicationContext(),"Location Service Has Started",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    stopService(intent);
                    locationPref.setChecked(locbool);
                    Toast.makeText(getApplicationContext(),"Location Service Has Stopped",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });



        listPrefUser1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                int i = Integer.parseInt(newValue.toString());
                listPrefUser1.setTitle(entries[i]);
                appPrefs.saveUser1(categoryList.get(i).getName(),categoryList.get(i).getPhoneNo());
                // Get the entry which corresponds to the current value and set as summary
//                preference.setSummary(listPrefUser1.getEntry());
                return false;
            }
        });

        listPrefUser2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // Set the value as the new value
                int i = Integer.parseInt(newValue.toString());
                listPrefUser2.setTitle(entries[i]);
                appPrefs.saveUser2(categoryList.get(i).getName(),categoryList.get(i).getPhoneNo());
                // Get the entry which corresponds to the current value and set as summary
//                preference.setSummary(listPrefUser1.getEntry());
                return false;
            }
        });

    }



}