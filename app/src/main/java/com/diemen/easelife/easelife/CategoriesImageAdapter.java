package com.diemen.easelife.easelife;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.diemen.easelife.model.Categories;
import com.diemen.easelife.sqllite.DBManager;

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
    }

    @Override
    public int getCount() {
        return categoriesList.size();
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

        //Implement Sort based on likes to arrange the grid view


        if(convertView == null)
        {
            grid = new View(categoriesContext);
            grid = inflater.inflate(R.layout.image_and_text,null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);

            textView.setText(categoriesList.get(position).getCategoryName());
            if(categoriesList.get(position).getImageResourcePath() != null)
            {
                imageView.setImageResource(categoriesThumbs[position]);
            }
            else
            {
                //set image path from Picaso
                imageView.setImageResource(R.drawable.sample_7);
            }
        }
        else
        {
            grid = (View) convertView;
        }
        return grid;
    }

    private Integer[] categoriesThumbs = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6
    };
}