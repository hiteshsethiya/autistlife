package com.diemen.easelife.pushnotificationhandler;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Console;

/**
 * Created by user on 10-01-2015.
 */
public class PushInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "pBQ7oHoCqFXmzyP4BXQQ1rlyfnzgKxvsCYHRHDMX", "jZy0HekTIFoKFU3fbJENMGkFJDFy3GCsQryrQUKZ");

       System.out.println("Anuj:Resgistration sucessful");
    }

    public static void setPhoneNo(String phoneNo){
        ParseInstallation currentInstall=ParseInstallation.getCurrentInstallation();
        currentInstall.put("phone",phoneNo);
    }
}
