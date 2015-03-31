package com.diemen.easelife.easelife;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.User;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 23-01-2015.
 */
public class UserListAdapter extends BaseAdapter {

    private Activity activity;
    private List<User> data;
    private static LayoutInflater inflater=null;


    public UserListAdapter(Activity a, List<User> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        try {

            if (convertView == null)
                vi = inflater.inflate(R.layout.user_list_row, null);

            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView artist = (TextView) vi.findViewById(R.id.artist); // artist name
            ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

            title.setTag(data.get(position));
            User song = data.get(position);
            // Setting all values in listview

            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                    .parseLong(song.getcontact_id()));

            Uri Image_uri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

            title.setText(song.getName());
            artist.setText(song.getPhoneNo());
            thumb_image.setImageURI(Image_uri);
        }
        catch(Exception e)
        {
            Log.i("USER LIST ADAPTER","Image not found for URI",e);
        }
        return vi;
    }
}
