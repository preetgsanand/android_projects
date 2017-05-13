package com.example.vince.shivaexpo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class CSVCpicker extends Fragment implements View.OnClickListener{
    private View view;
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_csvcpicker, container, false);
           setListeners();
           return view;
    }
    private void setListeners () {
        Button csv = (Button) view.findViewById(R.id.csv_picker);
        csv.setOnClickListener(this);
        Button submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.csv_picker:
                ((MainActivity)getActivity()).csvPicker();
                break;
            case R.id.submit:
                ((MainActivity)getActivity()).readCSV();
                break;

        }
    }

}
