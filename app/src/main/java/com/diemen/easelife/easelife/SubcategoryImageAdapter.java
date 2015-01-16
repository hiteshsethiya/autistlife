package com.diemen.easelife.easelife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.Subcategory;
import com.diemen.easelife.sqllite.DBManager;
import com.diemen.easelife.util.Util;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class SubcategoryImageAdapter extends BaseAdapter{

    private Context subcategoryContext;
    private List<Subcategory> subcategories;

    public SubcategoryImageAdapter(Context subcategoryContext,int categoryId) {
        this.subcategoryContext = subcategoryContext;
        this.subcategories = DBManager.getInstance().getSubcategoryByCategoryId(categoryId);
        subcategoriesThumbHM.put(6,R.drawable.avengers);
        subcategoriesThumbHM.put(7,R.drawable.home);
        subcategoriesThumbHM.put(8,R.drawable.user);
        subcategoriesThumbHM.put(9,R.drawable.school);
    }

    @Override
    public int getCount() {
        return subcategories.size();
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

        LayoutInflater inflater = (LayoutInflater) subcategoryContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            grid = new View(subcategoryContext);
            grid = inflater.inflate(R.layout.image_and_text,null);

            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);

            imageView.setTag(subcategories.get(position));
            textView.setText(subcategories.get(position).getSubcategoryName());

            String imageResourcePath = subcategories.get(position).getImagePath();

            if(imageResourcePath != null && Util.isInteger(imageResourcePath))
            {
                imageView.setImageResource(subcategoriesThumbHM.get(Integer.parseInt(subcategories.get(position).getImagePath())));
            }
            else
            {
                imageView.setImageResource(subcategoriesThumbHM.get(0));
            }

        }
        else
        {
            grid = (View) convertView;
        }
        return grid;
    }

    private static HashMap<Integer,Integer> subcategoriesThumbHM = new HashMap<Integer,Integer>();
}
