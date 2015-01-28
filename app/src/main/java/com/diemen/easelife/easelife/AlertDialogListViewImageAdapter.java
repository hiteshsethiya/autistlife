package com.diemen.easelife.easelife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diemen.easelife.model.EaseLifeConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.Inflater;

/**
 * Created by tfs-hitesh on 26/1/15.
 */
public class AlertDialogListViewImageAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater layoutInflator;

    public AlertDialogListViewImageAdapter(){}

    public AlertDialogListViewImageAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflator = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return viewPlates.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView;


        if(convertView == null)
        {
            listView = layoutInflator.inflate(R.layout.image_text_alert_dialog,null);
            ImageView imageView = (ImageView) listView.findViewById(R.id.imageView);
            TextView textView = (TextView) listView.findViewById(R.id.textView);
            imageView.setImageResource(viewPlates.get(viewPlateMapper[position]));
            textView.setText(viewPlateMapper[position]);
            textView.setPadding(listView.getWidth()/3,0,0,0);
        }
        else
        {
            listView = (View) convertView;
        }
        return listView;
    }

    private static final HashMap<String,Integer> viewPlates = new HashMap<String, Integer>();

    static
    {
        viewPlates.put(EaseLifeConstants.SELECT_IMAGE_FROM_GALLERY,R.drawable.gallery_bw_logo);
        viewPlates.put(EaseLifeConstants.CLICK_IMAGE_FROM_CAMERA,R.drawable.camera_logo);
    }

    private CharSequence viewPlateMapper[] = {
            EaseLifeConstants.SELECT_IMAGE_FROM_GALLERY,
            EaseLifeConstants.CLICK_IMAGE_FROM_CAMERA};
}
