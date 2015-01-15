package com.diemen.easelife.pushnotificationhandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.diemen.easelife.easelife.StartActivity;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by user on 12-12-2014.
 */
public class PushBroadcastReceiver extends ParsePushBroadcastReceiver{

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }
}
