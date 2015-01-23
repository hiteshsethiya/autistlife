package com.diemen.easelife.easelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.diemen.easelife.model.User;
import com.diemen.easelife.pushnotificationhandler.ChatActivity;

import java.util.ArrayList;

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

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent gotoChat;
                gotoChat = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(gotoChat);

            }
        });
    }

    public void createList(){
        mList.add(new User("Anuj", "234234234242"));
        mList.add(new User("Anuj1", "234234234242"));
        mList.add(new User("Anuj3", "234234234242"));
        mList.add(new User("Anuj5", "234234234242"));
        mList.add(new User("Anuj6", "234234234242"));



    }
}
