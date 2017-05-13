package com.example.jeet.abhilocation;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment implements LocationProvider.Provider{

    private View view;
    private LocationProvider mLocationProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_location, container, false);
        mLocationProvider = new LocationProvider(getContext(), this);
        mLocationProvider.connect();
        return view;
    }

    @Override
    public void setNewLocation(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        getAddress(latlng);

    }
    public void getAddress(LatLng latLng) {
        List<Address> addresses;
        Geocoder mGeocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            Log.e("Location",address+city+state+country);
            setLocation(address,city,state,country);
        }
        catch(Exception e) {
            Log.e("Location error",e.toString());
            Toast.makeText(getContext(),
                    "Location not found",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void setLocation(String address,String city,String state,String country) {
        TextView addressView = (TextView) view.findViewById(R.id.address);
        TextView cityView = (TextView) view.findViewById(R.id.city);
        TextView countryView = (TextView) view.findViewById(R.id.country);

        addressView.setText(address+", "+city);
        cityView.setText(city);
        countryView.setText(country.toUpperCase());
        ((MainActivity)getActivity()).storeData(address+", "+city,state,country);
    }
}
