package com.example.vince.shivaexpo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomItemListAdapter extends BaseAdapter{
    private List<Item> itemList;
    private Context mContext;
    private LayoutInflater inflater;

    CustomItemListAdapter(List<Item> items,Context context) {
        itemList = items;
        mContext = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.custom_item_list_adapter, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView number = (TextView) view.findViewById(R.id.number);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView rate = (TextView) view.findViewById(R.id.rate);


        number.setText(itemList.get(position).getId().toString());
        name.setText(itemList.get(position).getCode());
        quantity.setText(itemList.get(position).getQuantity());
        rate.setText(itemList.get(position).getRate());

        return view;
    }
}
