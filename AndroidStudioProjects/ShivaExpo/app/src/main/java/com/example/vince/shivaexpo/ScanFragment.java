package com.example.vince.shivaexpo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;


public class ScanFragment extends Fragment {
    private View view;
    private static String code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_scan, container, false);
        setValues();
        return view;
    }
    private void setValues() {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView table = (TextView) view.findViewById(R.id.table);
        TextView mp_cbm = (TextView) view.findViewById(R.id.mp_cbm);
        TextView ip_mp = (TextView) view.findViewById(R.id.ip_mp);
        TextView fob = (TextView) view.findViewById(R.id.fob);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView rate = (TextView) view.findViewById(R.id.rate);
        TextView size = (TextView) view.findViewById(R.id.size);


        List<Item> item = Item.find(Item.class,"CODE = ?",code);
        Log.e("Result",code+" : "+item.toString());
        if(item.size() != 0) {
            name.setText(item.get(0).getCode());
            table.setText(item.get(0).getItem_table());
            mp_cbm.setText(item.get(0).getMp_cbm());
            ip_mp.setText(item.get(0).getIp_mp());
            fob.setText(item.get(0).getFob());
            quantity.setText(item.get(0).getQuantity());
            rate.setText(item.get(0).getRate());
            size.setText(item.get(0).getSize());
        }
        else {
            Toast.makeText(getContext(),
                    "No Entry Found",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public static void setItemId(String new_id) {
        code = new_id;
    }

}
