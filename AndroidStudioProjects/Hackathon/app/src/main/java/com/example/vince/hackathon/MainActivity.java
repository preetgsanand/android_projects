package com.example.vince.hackathon;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;


import com.digits.sdk.android.Digits;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {


    private static final String TWITTER_KEY = "nrdZchPnbE5YPZMK5HmWbk8vc";
    private static final String TWITTER_SECRET = "1PWRtLjlTVzCXGf1AEh0Oo3plJragD5bcSkDIRAbnghsvxNBGY";
    public static final String FIREBASE_URL = "https://hackathon-b198b.firebaseio.com";

    public static String name,email,phone;
    private DetailInputFragment detailInputFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        setUpFragment(1);
    }



    public void setUpFragment(int code) {
        switch (code) {
            case 1:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, new AppIntroFragment()).commit();
                break;
            case 3:
                detailInputFragment = new DetailInputFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).
                        replace(R.id.fragment_container, detailInputFragment).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_container, new OtpFragment()).commitAllowingStateLoss();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_container, new IntroSyncFragment()).commit();
                break;
            case 5:
        }
    }


    public void showAltertDialog() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setMessage("The app will now ask for permissions for connecting to online database," +
                            "and writing user data to the storage.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            askPermissons();
                        }
                    })
                    .show();
        }
        else {
            getAccountInfo();
        }

    }

    private void askPermissons() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.AUTHENTICATE_ACCOUNTS},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAccountInfo();


                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void getAccountInfo() {
        String email = checkAccounts();
        detailInputFragment.setEmail(email);
    }
    private String checkAccounts() {
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = manager.getAccountsByType("com.google");
        if (accounts.length == 0) {
            Toast.makeText(this,
                    "No gmail account found",
                    Toast.LENGTH_SHORT).show();
            return "";
        } else {
            return accounts[0].name.toString();
        }
    }

    public void syncProfile() {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Syncing", true);
        final Firebase ref = new Firebase(FIREBASE_URL).child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Profile> profileList = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("phone").getValue().equals(phone)) {
                        Log.e("Children", snapshot.getValue().toString());

                        parseSnapshot(snapshot);
                        dialog.dismiss();
                        return;
                    }
                }

                    ArrayList<Job> jobs = new ArrayList<>();
                    Profile profile = new Profile(phone,name,email,"user",jobs);
                    Job job = new Job("Gardening","Gardening","25 Jan,2017","27 Jan,2017",
                            "Ongoing");
                job.save();
                Job job1 = new Job("Gardening","Gardening","19 Jan,2017","25 Jan,2017",
                        "Ongoing");
                job1.save();
                    profile.addJob(job);
                    profile.addJob(job1);
                    profile.save();
                    ref.child(phone).setValue(profile);
            dialog.dismiss();
                setUpFragment(4);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void parseSnapshot(DataSnapshot snapshot) {
        DataSnapshot jobList = snapshot.child("jobList");
        ArrayList<Job> jobs = new ArrayList<>();
        for(DataSnapshot jobSnapshot : jobList.getChildren()) {
            Job job = new Job(jobSnapshot.child("title").getValue().toString(),
                    jobSnapshot.child("description").getValue().toString(),
                    jobSnapshot.child("addedDate").getValue().toString(),
                    jobSnapshot.child("deadlineDate").getValue().toString(),
                    jobSnapshot.child("status").getValue().toString());
            job.save();
            jobs.add(job);
        }
        Profile profile = new Profile(snapshot.child("phone").getValue().toString(),
                snapshot.child("name").getValue().toString(),
                snapshot.child("email").getValue().toString(),
                snapshot.child("role").getValue().toString(),
                jobs);
        profile.save();
        Log.e("Profile",profile.toString());
        setUpFragment(4);

    }

    public void setAppActivity() {
        Intent intent = new Intent(getBaseContext(), AppActivity.class);
        startActivity(intent);
    }

}
