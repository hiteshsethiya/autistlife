package com.diemen.easelife.easelife;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.model.EaseLifeConstants;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tfs-hitesh on 14/12/14.
 */

public class CategoriesImageAdapter extends BaseAdapter{

    private Context categoriesContext;
    private List<Categories> categoriesList;
    private LayoutInflater inflater;
    public static int notifyDataSetChangeIdentifier = 0;

    public CategoriesImageAdapter(Context context,List<Categories> list)
    {
        this.categoriesContext = context;
        this.categoriesList = list;
        inflater = (LayoutInflater) categoriesContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        categoriesThumbHM.put(1,R.drawable.places);
        categoriesThumbHM.put(2,R.drawable.movie);
        categoriesThumbHM.put(3,R.drawable.singing);
        categoriesThumbHM.put(4,R.drawable.people);
        categoriesThumbHM.put(5,R.drawable.pizza);
        categoriesThumbHM.put(8,R.drawable.user);
        categoriesThumbHM.put(9,R.drawable.school_new);
        categoriesThumbHM.put(10,R.drawable.music);

    }

    @Override
    public int getCount() {
        return categoriesList.size()+1;
    }

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
        if(position != categoriesList.size()) {
            holder.textView.setText(categoriesList.get(position).getCategoryName()
                    +" ("+categoriesList.get(position).getLikes()+")");
            String imageName = categoriesList.get(position).getImageResourcePath();

            if (imageName != null) {
                if (Util.isInteger(imageName)) {
                    holder.imageView.setImageResource(
                            categoriesThumbHM.get(Integer.parseInt(imageName)));
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
            holder.imageView.setTag(categoriesList.get(position));
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.add_category);
            holder.textView.setText("Add Category");
        }
        return convertView;

    }

    public static HashMap<Integer,Integer> categoriesThumbHM = new HashMap<Integer,Integer>();
    static class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }
}
