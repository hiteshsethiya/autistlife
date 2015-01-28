package com.diemen.easelife.easelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.model.User;
import com.diemen.easelife.pushnotificationhandler.ChatActivity;
import com.diemen.easelife.sqllite.DBManager;
import com.parse.ParseUser;

import java.util.ArrayList;
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent subcategory = new Intent(getApplicationContext(),SubcategoryActivity.class);
        startActivity(subcategory);
        finish();
    }
}
