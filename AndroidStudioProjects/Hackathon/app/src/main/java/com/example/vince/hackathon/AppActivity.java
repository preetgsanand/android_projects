package com.example.vince.hackathon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{


    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        SugarContext.init(getApplicationContext());
        askPermissions();

    }


    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }
        else {
            checkIntitalSetup();
        }
    }

    private void initializeDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.navList);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.navigation_header,null);

        TextView name = (TextView) header.findViewById(R.id.name);
        Profile profile = Profile.findById(Profile.class,1);
        name.setText(profile.getName());
        drawerList.addHeaderView(header);
        drawerList.setOnItemClickListener(this);
        Button navigationToggle = (Button) findViewById(R.id.navigationToggle);
        navigationToggle.setOnClickListener(this);

        CustomNavBarListAdapter adapter = new CustomNavBarListAdapter(
                getResources().getStringArray(R.array.navList),
                getApplicationContext()
        );
        drawerList.setAdapter(adapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkIntitalSetup();

                } else {

                    Toast.makeText(AppActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private void checkIntitalSetup() {
        Profile profile = Profile.findById(Profile.class,1);
        if(profile == null) {
            callIntroActivity();
        }
        else {
            initializeDrawer();
        }
    }
    private void callIntroActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.navList) {
            switch(position) {
                case 1 :getSupportFragmentManager().beginTransaction().
                        replace(R.id.drawer_layout,new JobDisplayFragment()).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.navigationToggle:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
        }
    }
}
