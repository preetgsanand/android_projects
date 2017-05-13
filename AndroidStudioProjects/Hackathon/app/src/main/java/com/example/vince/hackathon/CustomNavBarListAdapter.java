package com.example.vince.hackathon;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vince on 1/26/17.
 */
public class CustomNavBarListAdapter extends BaseAdapter {

    private String[] navList;
    private Context context;
    private LayoutInflater inflater;

    public CustomNavBarListAdapter(String[] navList,Context context) {
        this.navList = navList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return navList.length;
    }

    @Override
    public Object getItem(int position) {
        return navList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.nav_list_item,null);

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);

        title.setText(navList[position].toUpperCase());
        switch(position) {
            case 0:icon.setImageDrawable(context.getResources().getDrawable(R.drawable.job));
                break;
            case 1:icon.setImageDrawable(context.getResources().getDrawable(R.drawable.admin));
                break;
            case 2:icon.setImageDrawable(context.getResources().getDrawable(R.drawable.sing_out));
                break;
        }
        return view;
    }
}
