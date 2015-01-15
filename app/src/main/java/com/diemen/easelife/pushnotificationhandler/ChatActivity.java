package com.diemen.easelife.pushnotificationhandler;

import com.diemen.easelife.easelife.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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
        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        listMessages = new ArrayList<Message>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);
        Message newmessage=new Message();
        newmessage.setMessage("Hi");
        newmessage.setFromName("anuj");
        newmessage.setSelf(true);

        Message newmessage1=new Message();
        newmessage1.setMessage("How are you");
        newmessage1.setFromName("anuj");
        newmessage1.setSelf(false);

        Message newmessage2=new Message();
        newmessage2.setMessage("anuj is good");
        newmessage2.setFromName("anuj");
        newmessage2.setSelf(true);


        appendMessage(newmessage);
        appendMessage(newmessage1);
        appendMessage(newmessage2);
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
}
