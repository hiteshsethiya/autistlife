package com.diemen.easelife.easelife;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class SubcategoryImageAdapter extends BaseAdapter{
private int categoryId;
    private Context subcategoryContext;
    private List<Subcategory> subcategories;
    private LayoutInflater inflater;

    public SubcategoryImageAdapter(Context subcategoryContext,int categoryId,List<Subcategory> list) {
        this.subcategoryContext = subcategoryContext;
        this.categoryId = categoryId;
        this.subcategories = list;
        inflater = (LayoutInflater) subcategoryContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        subcategoriesThumbHM.put(6,R.drawable.avengers);
        subcategoriesThumbHM.put(7,R.drawable.home);
        subcategoriesThumbHM.put(8,R.drawable.user);
        subcategoriesThumbHM.put(9,R.drawable.school_new);
    }

    @Override
    public int getCount() {
        return subcategories.size()+1;
    } // the +1 has been added to show the Add Subcategory button at the end of the grid to add a new subcategory

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.grid_item,parent,false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.grid_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position != subcategories.size()) {
            holder.textView.setText(subcategories.get(position).getSubcategoryName()
                    + " (" + subcategories.get(position).getLikes() + ")");
            String imageName = subcategories.get(position).getImagePath();

            if (imageName != null) {
                if (Util.isInteger(imageName)) {
                    holder.imageView.setImageResource(
                            subcategoriesThumbHM.get(Integer.parseInt(imageName)));
                } else {
                    File sd = Environment.getExternalStorageDirectory();
                    File image = new File(sd + EaseLifeConstants.imagesPath, imageName);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                    //  bitmap = Bitmap.createScaledBitmap(bitmap,350
                    //,parent.getHeight(),true);
                    holder.imageView.setImageBitmap(bitmap);
                }
            } else {
                holder.imageView.setImageResource(R.drawable.imagenotselected);
            }
            holder.imageView.setTag(subcategories.get(position));
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.add_category);
            holder.textView.setText("Add Subcategory");
            holder.imageView.setTag(null);
        }
        return convertView;
    }

    public static HashMap<Integer,Integer> subcategoriesThumbHM = new HashMap<Integer,Integer>();
    static class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }
}
