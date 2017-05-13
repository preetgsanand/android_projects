package vince.zebapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CollegeDetailFragment extends Fragment {


    public static long id;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setTitle("DETAIL");

        view = inflater.inflate(R.layout.fragment_college_detail, container, false);
        setHasOptionsMenu(true);
        initializeViews(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO your code to hide item here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(false);
    }

    public static CollegeDetailFragment newInstance(long position) {

        id = position;
        CollegeDetailFragment fragment = new CollegeDetailFragment();
        return fragment;
    }

    private void initializeViews(View view) {
        TextView collegeName = (TextView) view.findViewById(R.id.name);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView city = (TextView) view.findViewById(R.id.city);
        TextView address = (TextView) view.findViewById(R.id.address);


        Person person = Person.findById(Person.class,id);

        collegeName.setText(person.getInstitute_name());
        description.setText(person.getDescription());
        city.setText(person.getCity());
        address.setText(person.getAddress());
    }
}
