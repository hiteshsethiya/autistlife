package com.diemen.easelife.easelife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 23-01-2015.
 */
public class UserListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> data;
    private static LayoutInflater inflater=null;


    public UserListAdapter(Activity a, ArrayList<User> d) {
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.user_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        title.setTag(data.get(position));
        User  song = data.get(position);
        // Setting all values in listview

        title.setText(song.getName());
        artist.setText(song.getPhoneNo());
        return vi;
    }
}
