package com.example.vince.hackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AppIntroFragment extends Fragment implements View.OnClickListener{

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_info, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        Button start = (Button) view.findViewById(R.id.start);
        start.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.start:((MainActivity)getActivity()).setUpFragment(2);
        }
    }
}
