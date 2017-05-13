package com.example.jeet.abhilocation;


import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jeet.abhilocation.Database.DeviceInfo;
import com.firebase.client.Firebase;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarContext;

public class MainActivity extends AppCompatActivity {
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        SugarContext.init(getApplicationContext());
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        checkDatabaseNumber();
    }

    private void checkDatabaseNumber() {
        DeviceInfo deviceInfo = DeviceInfo.findById(DeviceInfo.class,1);
        if(deviceInfo == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new OTPFragment()).commit();
        }
        else {
            if(deviceInfo.getNumber().equals("")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new OTPFragment()).commit();
            }
            else {
                setLocationFragment();
            }
        }
    }
    public void storeData(String address,String city,String country) {

        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            LocationDatabase location = new LocationDatabase();
            location.setAddress(address);
            location.setCity(city);
            location.setCountry(country);

            ref.child(number).setValue(location);
            Log.e("Data", "Stored");
        }
        catch(Exception e) {
            Log.e("Error",e.toString());
        }
    }
    public void setLocationFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LocationFragment()).commit();

    }
    public void setNumber(String number) {
        DeviceInfo deviceInfo = DeviceInfo.findById(DeviceInfo.class,1);
        if(deviceInfo == null) {
            deviceInfo = new DeviceInfo(number);
            deviceInfo.save();
        }
        this.number = number;
    }
}
