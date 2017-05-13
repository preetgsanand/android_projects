package com.example.vince.shivaexpo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class ItemListFragment extends Fragment{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_item_list, container, false);
        setUpList();
        return view;
    }
    private void setUpList() {
        List<Item> items = (List<Item>) Item.listAll(Item.class);
        CustomItemListAdapter adapter = new CustomItemListAdapter(items,getContext());
        ListView itemList = (ListView) view.findViewById(R.id.item_list);
        itemList.setAdapter(adapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = Item.findById(Item.class,id+1);
                if(item != null) {
                    Log.e("Item : ",item.toString());
                    ((MainActivity)getActivity()).setScanWithCode(item.getCode());
                }
            }
        });
    }

}
