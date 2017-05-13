package com.example.vince.hackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileInfoFragment extends Fragment {


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        TextView email = (TextView) view.findViewById(R.id.email);

        Profile profile = Profile.findById(Profile.class,1);

        name.setText(profile.getName());
        phone.setText(profile.getPhone());
        email.setText(profile.getEmail());
    }

}
