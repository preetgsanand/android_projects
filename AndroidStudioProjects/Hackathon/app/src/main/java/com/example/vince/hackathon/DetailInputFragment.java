package com.example.vince.hackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DetailInputFragment extends Fragment implements View.OnClickListener{

    private View view;
    private EditText name,number,email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_input, container, false);
        ((MainActivity)getActivity()).showAltertDialog();
        initializeViews();
        return view;
    }
    private void initializeViews() {
        Button submit = (Button) view.findViewById(R.id.submit);
        name = (EditText) view.findViewById(R.id.userName);
        email = (EditText) view.findViewById(R.id.userEmail);
        submit.setOnClickListener(this);

    }
    private void checkForm() {
        MainActivity.email = email.getText().toString();
        MainActivity.name = name.getText().toString();
        ((MainActivity)getActivity()).syncProfile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:checkForm();
        }
    }

    public void setEmail(String emailAddress) {
        if(email != null) {
            email.setText(emailAddress);
        }
    }
}
