package jeet.com.kairosrecignition;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailInputFragment extends Fragment implements View.OnClickListener{

    private View view;
    public Bitmap bitmap;
    private Handler handler;
    private ProgressDialog dialog;
    private EditText phone,email,address,fb,insta,name;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_input, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        name = (EditText) view.findViewById(R.id.userName);
        email = (EditText) view.findViewById(R.id.userEmail);
        phone = (EditText) view.findViewById(R.id.userPhone);
        address = (EditText) view.findViewById(R.id.userAddress);
        fb = (EditText) view.findViewById(R.id.userFB);
        insta = (EditText) view.findViewById(R.id.userInsta);
        Button submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                ((MainActivity)getActivity()).enrollFace(bitmap,name.getText().toString());
                break;
        }
    }

    public void userAdd() {
        handler = new Handler();
        dialog = new ProgressDialog(getActivity());
        user = new User();
        user.setEmail(email.getText().toString());
        user.setFb(fb.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setName(name.getText().toString());
        user.setInsta(insta.getText().toString());
        user.setAddress(address.getText().toString());

        final JSONObject object = Utils.UserToJson(user);
        Log.e("request",object.toString());
        new Thread() {
            public void run() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                        getString(R.string.user_add)+"?email="+email.getText().toString(),
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {

                                try {
                                    if (response == null) {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                    } else {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                try {
                                                    Log.e("JSON : ",response.toString());
                                                    parseResponse(response);

                                                } catch (Exception e) {

                                                }
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error Json : ",error.toString());
                                dialog.dismiss();
                            }
                        }
                );
                Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
            }
        }.start();


    }

    private void parseResponse(JSONObject response) {
        try {
            if (response.has("status")) {
                if(response.getString("status").equals("100")) {
                    Toast.makeText(getContext(),
                            "User Added",
                            Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).syncData();
                }
            }
            else if(response.has("email")) {
                if(response.getString("email").equals(email.getText().toString())) {
                    Toast.makeText(getContext(),
                            "User Added",
                            Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).syncData();
                }
            }
            else {
                Toast.makeText(getContext(),
                        "User Addition Failed",
                        Toast.LENGTH_SHORT).show();

            }
            dialog.dismiss();
        }
        catch (Exception e) {
            Log.e("Parsing Response Add",e.toString());
        }
    }
}
