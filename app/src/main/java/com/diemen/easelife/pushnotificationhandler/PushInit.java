package com.diemen.easelife.pushnotificationhandler;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import java.io.Console;

/**
 * Created by user on 10-01-2015.
 */
public class PushInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "pBQ7oHoCqFXmzyP4BXQQ1rlyfnzgKxvsCYHRHDMX", "jZy0HekTIFoKFU3fbJENMGkFJDFy3GCsQryrQUKZ");
//        ParseInstallation currentInstall=ParseInstallation.getCurrentInstallation();
//        currentInstall.put("phone","9742510297");
        System.out.println("Anuj:Resgistration sucessful");

    }
}
