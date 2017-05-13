package com.example.vince.distress;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationProvider.Provider{



    private LocationProvider mLocationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        initializeProvider();
    }

    private void askPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    1);


        }

    }

    private void initializeProvider() {
        mLocationProvider = new LocationProvider(getApplicationContext(), (LocationProvider.Provider)mLocationProvider);
        mLocationProvider.connect();
    }

    public String getAddress(LatLng latLng) {
        List<Address> addresses;
        Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            return address+", "+city+", "+", "+state+", "+country;
        }
        catch(Exception e) {
            return "Error";
        }
    }

    @Override
    public void setNewLocation(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        Toast.makeText(getApplicationContext(),
                getAddress(latlng).toString(),
                Toast.LENGTH_SHORT);
    }
}
