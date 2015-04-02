package com.diemen.easelife.pushnotificationhandler;

import com.diemen.easelife.easelife.*;
import com.diemen.easelife.model.Chat;
import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.sqllite.DBManager;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 11-01-2015.
 */
public class ChatActivity extends ActionBarActivity {

    private Button btnSend;
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;
    private static final String TAG_SELF = "self";
    public String ReceiverPhoneNumber;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message);
        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);



        Bundle chatIntent=getIntent().getExtras();
        final String ReceiverPhone=chatIntent.getString("ReceiverPhone");
        final String SenderPhone=chatIntent.getString("SenderPhone");
        final String Receiver=chatIntent.getString("Receiver");
        final String Sender=chatIntent.getString("Sender");
        ReceiverPhoneNumber=ReceiverPhone;


        String SrcActivity=chatIntent.get("SrcActivity").toString();
                if(SrcActivity.toString().equals("UserListActivity")) {
                    getSupportActionBar().setTitle(Receiver);
                    List<Chat> list = DBManager.getInstance().getAllChats(ReceiverPhone);
                    for (Chat c : list) {
                        Message newmessage = new Message();
                        if(c.isSelf()==true)
                        newmessage.setFromName(c.getSender());
                        else
                        newmessage.setFromName(c.getReceiver());
                        newmessage.setMessage(c.getMessage());
                        newmessage.setSelf(c.isSelf());
                        appendMessage(newmessage);
                    }
                }

        else if(SrcActivity.toString().equals("PushBroadCastReceiver"))
                {
                    getSupportActionBar().setTitle(Receiver);
                    List<Chat> list = DBManager.getInstance().getAllChats(ReceiverPhone);
                    for (Chat c : list) {
                        Message newmessage = new Message();
                        if(c.isSelf()==true)
                            newmessage.setFromName(c.getSender());
                        else
                            newmessage.setFromName(c.getReceiver());
                        newmessage.setMessage(c.getMessage());
                        newmessage.setSelf(c.isSelf());
                        appendMessage(newmessage);
                    }
                }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = inputMsg.getText().toString();
                if (message != null && !message.isEmpty()) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Chat chat = new Chat();
                    chat.setMessage(inputMsg.getText().toString());
                    chat.setSelf(true);
                    chat.setSender(Sender);
                    chat.setSenderPhone(SenderPhone);
                    chat.setReceiver(Receiver);
                    chat.setReceiverPhone(ReceiverPhone);
                    Date date = new Date();
                    date.getTime();


                    DBManager.getInstance().addChat(chat);

                    Message newmessage = new Message();
                    newmessage.setFromName(currentUser.getUsername());
                    newmessage.setMessage(message);
                    newmessage.setSelf(true);
                    appendMessage(newmessage);
                    inputMsg.setText("");

                    //  pushMessage.setMessage(message.getText().toString());
                    JSONObject data;
                    try {
                        ParseQuery pQuery = new ParseInstallation().getQuery();
                        pQuery.whereEqualTo("phone", ReceiverPhone);
                        ParsePush pushMessage = new ParsePush();
                        pushMessage.setQuery(pQuery);
                        //Always keep in mind "I am the Sender"
                        //when the user will receive this message he will be the sender;
                        data = new JSONObject("{\"alert\":\"New Message\",\"Message\": \"" + chat.getMessage() + "\",\"ReceiverPhone\": \"" + SenderPhone + "\",\"Sender\":\"" + Receiver + "\",\"Receiver\":\"" + Sender + "\" ,\"SenderPhone\":\"" + ReceiverPhone + "\",\"ReceivedOn\":\"" + date.getTime() + "\"}");
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

    private void appendMessage(Message m) {
                listMessages.add(m);
                adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_activity, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Get the id of the item that has been clicked and call events
         */
        getUserLocation1(ReceiverPhoneNumber);

        /*   int id = item.getItemId();
        if(id == R.id.maps_activity)
        {
            Intent mapsActivity = new Intent("com.diemen.easelife.easelife.MAPSACTIVITY");
            mapsActivity.putExtra(EaseLifeConstants.LATITUDE,point.getLatitude());
            mapsActivity.putExtra(EaseLifeConstants.LONGITUDE,point.getLongitude());
            startActivity(mapsActivity);
        }*/
        return super.onOptionsItemSelected(item);
    }


    public void getUserLocation1(String PhoneNumber){
        ParseGeoPoint point=new ParseGeoPoint();
        ParseQuery<ParseObject> queryDriver=new ParseQuery("_User");
        queryDriver.whereEqualTo("PhoneNumber",PhoneNumber);
        queryDriver.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                for (ParseObject d : parseObjects) {
                    ParseGeoPoint point = (ParseGeoPoint) d.get("location");

                    if(point!=null) {
                        Intent mapsActivity = new Intent("com.diemen.easelife.easelife.MAPSACTIVITY");
                        mapsActivity.putExtra(EaseLifeConstants.LATITUDE, point.getLatitude());
                        mapsActivity.putExtra(EaseLifeConstants.LONGITUDE, point.getLongitude());
                        mapsActivity.putExtra("object",EaseLifeConstants.ISCHATACTIVITY);
                        startActivity(mapsActivity);
                    }
                }
            }
        });
    }




    private void showMessages(Message msg)
    {
      appendMessage(msg);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,StartActivity.class);
        startActivity(i);
    }


}
