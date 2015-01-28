package com.diemen.easelife.pushnotificationhandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.diemen.easelife.easelife.StartActivity;
import com.diemen.easelife.model.Chat;
import com.diemen.easelife.sqllite.DBManager;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 12-12-2014.
 */
public class PushBroadcastReceiver extends ParsePushBroadcastReceiver{

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        Intent i = new Intent(context, ChatActivity.class);
        JSONObject object;
        ParseUser currentUser=ParseUser.getCurrentUser();
        Chat chat=new Chat();
        try {
            object=new JSONObject(intent.getExtras().getString("com.parse.Data"));
            chat.setSender(object.getString("Sender"));
            chat.setReceiver(object.getString("Receiver"));
            chat.setReceiverPhone(object.getString("ReceiverPhone"));
            chat.setSenderPhone(object.getString("SenderPhone"));
            chat.setMessage(object.getString("Message"));
            String Test="Test";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DBManager.getInstance().addChat(chat);
        i.putExtra("SenderPhone", chat.getSenderPhone());
        i.putExtra("ReceiverPhone",chat.getReceiverPhone());
        i.putExtra("Receiver", chat.getReceiver());
        i.putExtra("Sender", chat.getSender());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }
}
