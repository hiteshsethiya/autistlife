package com.diemen.easelife.pushnotificationhandler;

import com.diemen.easelife.easelife.*;
import com.diemen.easelife.model.Chat;
import com.diemen.easelife.sqllite.DBManager;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 11-01-2015.
 */
public class ChatActivity extends Activity {

    private Button btnSend;
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;
    private static final String TAG_SELF = "self";

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
        Message newmessage=new Message();


        Intent chatIntent=getIntent();
        final String ReceiverPhone=chatIntent.getStringExtra("ReceiverPhone");
        final String SenderPhone=chatIntent.getStringExtra("SenderPhone");
        String Receiver=chatIntent.getStringExtra("Receiver");
        final String Sender=chatIntent.getStringExtra("Sender");




        List<Chat> list=DBManager.getInstance().getAllChats(SenderPhone,ReceiverPhone);

        for(Chat c: list)
        {
          newmessage.setFromName(c.getSender());
          newmessage.setMessage(c.getMessage());
          newmessage.setSelf(false);
          appendMessage(newmessage);
        }




        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser=ParseUser.getCurrentUser();
                Chat chat=new Chat();
                chat.setMessage(inputMsg.getText().toString());
                Date date=new Date();
                date.getTime();


              //  pushMessage.setMessage(message.getText().toString());
                JSONObject data;
                try {
                    ParseQuery pQuery=new ParseInstallation().getQuery();
                    pQuery.whereEqualTo("phone",SenderPhone);
                    ParsePush pushMessage=new ParsePush();
                    pushMessage.setQuery(pQuery);
                  data = new JSONObject("{\"alert\":\"New Message\",\"Message\": \""+chat.getMessage()+"\",\"ReceiverPhone\": \""+ReceiverPhone+"\",\"Sender\":\""+Sender+"\" ,\"SenderPhone\":\""+SenderPhone+"\",\"ReceivedOn\":\""+date.getTime()+"\"}");
                  pushMessage.setMessage("Anuj");
                  pushMessage.setData(data);
                  pushMessage.sendInBackground();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });




    }

    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,StartActivity.class);
        startActivity(i);
    }
}
