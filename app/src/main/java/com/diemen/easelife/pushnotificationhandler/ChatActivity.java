package com.diemen.easelife.pushnotificationhandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ListView;

import com.diemen.easelife.easelife.R;

/**
 * Created by user on 11-01-2015.
 */
public class ChatActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.chat_message);
        ListView listView=(ListView)findViewById(R.id.listView1);
        Intent i=getIntent();
        String message=i.getStringExtra("message");
      //  listView.set








    }
}
