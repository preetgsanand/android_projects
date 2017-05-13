package com.example.vince.shivaexpo;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.orm.SugarContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private DrawerLayout drawerLayout;
    private ListView navList;
    private String CSVPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(getApplicationContext());
        //drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //navList = (ListView) findViewById(R.id.navList);
        //navList.setOnItemClickListener(this);
        //setUpDrawer();
        checkCSVDatabase();

    }
    public void setScanWithCode(String code) {
        ScanFragment.setItemId(code);
        setUpScanFragment();
    }
    public void setUpDrawer() {
        String[] drawerArray = getResources().getStringArray(R.array.drawer_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
               drawerArray );
        navList.setAdapter(adapter);
    }
    private void checkCSVDatabase() {
        List<Item> itemList = Item.listAll(Item.class);
        if(itemList.size() == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CSVCpicker()).commit();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ItemListFragment()).commit();
        }
    }

    public void scanBar(View view) {


        Intent intent = new Intent(this,CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);
    }


   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("Listener","called");
        switch(parent.getId()) {
            case R.id.navList:
                Log.e("Drawer","Called");
                switch (position) {

                    case 0:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ListFragment()).commit();
                        Log.e("List","Pressed");
                }
        }
    }*/
    public void csvPicker() {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    String contents = data.getStringExtra("SCAN_RESULT");
                    ScanFragment.setItemId('"'+contents+'"');
                    Toast toast = Toast.makeText(this, "Content:" + contents , Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("Error", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        String path = getPath(this, uri);
                        CSVPath = path;
                        Log.d("Error", "File Path: " + path);
                    }
                    catch(Exception e) {
                        Log.e("Exception",e.toString());
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
            setUpScanFragment();
        }
    }
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {

            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public void setUpScanFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ScanFragment()).addToBackStack(null).commit();
    }
    public void readCSV() {
        int readCode = 1;
        if(CSVPath == null) {
            Toast.makeText(getApplicationContext(),
                    "No File Selected",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(CSVPath);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "\\|";

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {

                String[] data = line.split(cvsSplitBy);
                String code,table,size,mp_cbm,ip_mp,fob,quantity,rate;

                Item item = new Item(data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]);
                item.save();

            }

        } catch (FileNotFoundException e) {
            readCode = 0;
            e.printStackTrace();
        } catch (IOException e) {
            readCode = 0;
            Toast.makeText(getApplicationContext(),
                    "Cannot Read File",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if(readCode == 1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ItemListFragment()).commit();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
