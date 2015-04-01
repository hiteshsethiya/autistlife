package com.diemen.easelife.easelife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private Subcategory subcategory;
    User addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        list=(ListView)findViewById(R.id.list);
        // Getting adapter by passing xml data ArrayList
        createList();
        adapter = new UserListAdapter(this, mList);
        list.setAdapter(adapter);
        list.setItemsCanFocus(true);
        subcategory = getIntent().getParcelableExtra(Subcategory.SUBCATEGORY_OBJECT);



        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                User user = mList.get(position);
                Intent gotoChat;
                ParseUser currentUser = ParseUser.getCurrentUser();
                gotoChat = new Intent(getApplicationContext(), ChatActivity.class);
                gotoChat.putExtra("Receiver",user.getName());
                gotoChat.putExtra("ReceiverPhone",user.getPhoneNo());
                gotoChat.putExtra("SenderPhone",currentUser.getString("PhoneNumber"));
                gotoChat.putExtra("Sender",currentUser.getUsername());
                gotoChat.putExtra("SrcActivity","UserListActivity");




                Chat chat = new Chat();
                chat.setMessage(getMessage(user));
                chat.setSelf(true);
                chat.setSender(currentUser.getUsername());
                chat.setSenderPhone(currentUser.getString("PhoneNumber"));
                chat.setReceiver(user.getName());
                chat.setReceiverPhone(user.getPhoneNo());
                SendMessage(chat);


               gotoChat.putExtra("Receiver", user.getName());
                gotoChat.putExtra("ReceiverPhone", user.getPhoneNo());
                gotoChat.putExtra("SenderPhone", currentUser.getString("PhoneNumber"));
                gotoChat.putExtra("Sender", currentUser.getUsername());
                gotoChat.putExtra("SrcActivity", "UserListActivity");

                startActivity(gotoChat);
                finish();
            }
        });
    }

    public void createList(){

        List<User> users = DBManager.getInstance().getAllUsers();

        if(mList != null)
        {
            mList.clear();
        }
        if(users != null) {
            for (User user : users) {
                mList.add(user);
            }
        }
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

    public String getMessage(User destionationUser)
    {
        StringBuilder message = new StringBuilder();
        message.append("Hi ");
        message.append(destionationUser.getName());
        message.append(",\n");
        message.append("Subcategory Name: ");
        message.append(subcategory.getSubcategoryName());
        message.append(". ");
        message.append(subcategory.getDescription());
        return message.toString();
    }

    /* All over ridden methods start here */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent subcategoryIntent = new Intent(getApplicationContext(),SubcategoryActivity.class);
        subcategoryIntent.putExtra("categoryId", subcategory.getCategoryId());
        startActivity(subcategoryIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_new: Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                  startActivityForResult(intent, PICK_CONTACT);
                                  break;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Implemented onActivityResult for Picking up a contact from User contacts.
     * This method is called once the User clicks on the preferred contact
     */

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        /*
                            Returns the zero-based index for the given column name, or -1 if the column doesn't exist. If you expect the column to exist use getColumnIndexOrThrow(String) instead, which will make the error more clear.
                         */
                        int hasPhone = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                        String phoneNumber ="";

                        if (hasPhone >= 0)
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                            phones.moveToFirst();
                            while (phones.moveToNext())
                            {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }
                        String name = cursor.getString(nameIdx);
                        phoneNumber = phoneNumber.replace(" ","");
                        phoneNumber = phoneNumber.replace("-","");
                        phoneNumber = phoneNumber.length()>10?phoneNumber.substring(phoneNumber.length()-10):phoneNumber;

                        if(name!="" && phoneNumber!="") {
                            addUser = new User();
                            addUser.setName(name);
                            addUser.setPhoneNo(phoneNumber);
                            addUser.setcontact_id(contactId);
                            if(checkAndUpdateUser(addUser)) {
                                Toast.makeText(getApplicationContext(), "Added: Name:" + name + "  Phone:" + phoneNumber, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Already Exists: Name:" + name + "  Phone:" + phoneNumber, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        createList(); // reload the items from database
        adapter.notifyDataSetChanged();
    }

    /* All over ridden methods end here */

    public boolean checkAndUpdateUser(User addUser)
    {
        if(addUser == null)
        {
            return false;
        }
        List<User> users = mList;
        if(users == null || users.size() == 0) {
             users = DBManager.getInstance().getAllUsers();
        }

        for(User user: users) {
            if (addUser.getPhoneNo().equals(user.getPhoneNo()))
            {
                return false;
            }
        }
        DBManager.getInstance().addUser(addUser);
        return true;
    }

    private final String TAG = "UserListActivity";
    public final static String MESSAGE_TO_USER = "messageToUser";
    private static final int PICK_CONTACT = 100;
}
