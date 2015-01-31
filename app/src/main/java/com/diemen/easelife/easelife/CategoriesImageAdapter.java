package com.diemen.easelife.easelife;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tfs-hitesh on 14/12/14.
 */
public class CategoriesImageAdapter extends BaseAdapter{

    private Context categoriesContext;
    private List<Categories> categoriesList;

    public CategoriesImageAdapter(Context context)
    {
        this.categoriesContext = context;
        this.categoriesList =  DBManager.getInstance().getAllCategories();
        categoriesThumbHM.put(1,R.drawable.places);
        categoriesThumbHM.put(2,R.drawable.movie);
        categoriesThumbHM.put(3,R.drawable.singing);
        categoriesThumbHM.put(4,R.drawable.people);
        categoriesThumbHM.put(5,R.drawable.pizza);
        categoriesThumbHM.put(8,R.drawable.user);
        categoriesThumbHM.put(9,R.drawable.school);
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) categoriesContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            grid = new View(categoriesContext);
            grid = inflater.inflate(R.layout.grid_item,null);

            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            if(position == categoriesList.size())
            {
                imageView.setTag(null);
                textView.setText("Add New Category");
                imageView.setImageResource(R.drawable.add_category);
            }
            else {
                imageView.setTag(categoriesList.get(position));
                textView.setText(categoriesList.get(position).getCategoryName());
                String imageResourcePath = categoriesList.get(position).getImageResourcePath();

                if (imageResourcePath != null && Util.isInteger(imageResourcePath)) {
                    //set image path from Picaso
                    imageView.setImageResource(categoriesThumbHM.get(Integer.parseInt(categoriesList.get(position).getImageResourcePath())));
                }
                else if(imageResourcePath != null)
                {
                    File imageFile = new File(imageResourcePath);

                    if(imageFile.exists())
                    {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        bitmap = Bitmap.createScaledBitmap(bitmap,imageView.getWidth(),imageView.getHeight(),true);
                        imageView.setImageBitmap(bitmap);
                    }
                    else {
                        imageView.setImageResource(categoriesThumbHM.get(0));
                    }
                }
                else {
                    imageView.setImageResource(categoriesThumbHM.get(0));
                }
            }
        }
        else
        {
            grid = (View) convertView;
        }
        return grid;
    }

    private static HashMap<Integer,Integer> categoriesThumbHM = new HashMap<Integer,Integer>();
}