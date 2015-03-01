package com.diemen.easelife.easelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.diemen.easelife.model.Chat;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.model.User;
import com.diemen.easelife.pushnotificationhandler.ChatActivity;
import com.diemen.easelife.sqllite.DBManager;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 23-01-2015.
 */
public class UserListActivity extends ActionBarActivity {

    ListView list;
    ArrayList<User> mList = new ArrayList<User>();
    UserListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        list=(ListView)findViewById(R.id.list);
        // Getting adapter by passing xml data ArrayList
        adapter=new UserListAdapter(this, mList);
        createList();
        list.setAdapter(adapter);
        list.setItemsCanFocus(true);



        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                User user=mList.get(position);
                Intent gotoChat;
                ParseUser currentUser=ParseUser.getCurrentUser();
                gotoChat = new Intent(getApplicationContext(), ChatActivity.class);
                gotoChat.putExtra("Receiver",user.getName());
                gotoChat.putExtra("ReceiverPhone",user.getPhoneNo());
                gotoChat.putExtra("SenderPhone",currentUser.getString("PhoneNumber"));
                gotoChat.putExtra("Sender",currentUser.getUsername());
                gotoChat.putExtra("SrcActivity","UserListActivity");




                Chat chat = new Chat();
                chat.setMessage(" ");
                chat.setSelf(true);
                chat.setSender(currentUser.getUsername());
                chat.setSenderPhone(currentUser.getString("PhoneNumber"));
                chat.setReceiver(user.getName());
                chat.setReceiverPhone(user.getPhoneNo());
                SendMessage(chat);


                startActivity(gotoChat);
                finish();
            }
        });
    }

    public void createList(){

        List<User> userList= DBManager.getInstance().getAllUsers();
        for(User u : userList )
        {
            mList.add(u);
        }
        mList.add(new User("Anuj", "234234234242"));

    }

    public void SendMessage(Chat chat)
    {
        try {
            Date date = new Date();
            DBManager.getInstance().addChat(chat);
            JSONObject data;
            ParseQuery pQuery = new ParseInstallation().getQuery();
            pQuery.whereEqualTo("phone", chat.getReceiverPhone());
            ParsePush pushMessage = new ParsePush();
            pushMessage.setQuery(pQuery);
            //Always keep in mind "I am the Sender"
            //when the user will receive this message he will be the sender;
            data = new JSONObject("{\"alert\":\"New Message\",\"Message\": \"" + chat.getMessage() + "\",\"ReceiverPhone\": \"" + chat.getSenderPhone() + "\",\"Sender\":\"" + chat.getReceiver() + "\",\"Receiver\":\"" + chat.getSender() + "\" ,\"SenderPhone\":\"" + chat.getReceiverPhone() + "\",\"ReceivedOn\":\"" + date.getTime() + "\"}");
            pushMessage.setMessage("New Message");
            pushMessage.setData(data);
            pushMessage.sendInBackground();
        }
        catch (Exception e)
        {
            Log.e(" UserList Activity"," Method:Send Message");
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent subcategory = new Intent(getApplicationContext(),SubcategoryActivity.class);
        startActivity(subcategory);
        finish();
    }
}
