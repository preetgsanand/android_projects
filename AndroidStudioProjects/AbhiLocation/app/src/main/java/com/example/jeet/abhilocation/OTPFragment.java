package com.example.jeet.abhilocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OTPFragment extends Fragment implements View.OnClickListener{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ot, container, false);
        Button submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit:
                EditText number = (EditText) view.findViewById(R.id.number);
                if(number.getText().toString().length() < 10) {
                    Toast.makeText(getContext(),
                            "Enter a valid number",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    ((MainActivity)getActivity()).setNumber(number.getText().toString());
                }
                ((MainActivity)getActivity()).setLocationFragment();
                break;
        }
    }
}
