package jeet.com.kairosrecignition;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kairos.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Kairos myKairos;
    private FloatingActionButton cameraButton;
    private int action=0;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private Handler handler;
    private DetailInputFragment detailInputFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateKairos();
        setFab();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Syncing");
        syncData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mennu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                syncData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setFab() {
        cameraButton = (FloatingActionButton) findViewById(R.id.fab);
        final Dialog mainDialog = new Dialog(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.camera_choice_dialog,null);
        ListView dialogList = (ListView) view.findViewById(R.id.dialog_listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.camera_options));
        dialogList.setAdapter(adapter);
        mainDialog.setContentView(view);
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainDialog.dismiss();
                switch (position) {
                    case 0:
                        setPopup(0);
                        break;
                    case 1:setPopup(1);
                        break;
                }
            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDialog.show();
            }
        });
    }
    private void instantiateKairos() {
        myKairos = new Kairos();

        String app_id = getResources().getString(R.string.kairos_app_id);
        String api_key = getResources().getString(R.string.kairos_api_key);
        myKairos.setAuthentication(this, app_id, api_key);
    }

    public void syncData() {
        handler = new Handler();
        progressDialog.show();
        new Thread() {
            public void run() {
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                        getString(R.string.user_list),
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(final JSONArray response) {

                                try {
                                    if (response == null) {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                try {
                                                    Log.e("JSON : ",response.toString());

                                                    int code = Utils.JsonArrayToUser(response);
                                                    if(code == 1) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Sync Complete",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Sync Failed",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    progressDialog.dismiss();
                                                    setFragment(1,null);

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
                                progressDialog.dismiss();
                            }
                        }
                );
                Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequest);
            }
        }.start();

    }
    private KairosListener listener = new KairosListener() {

        @Override
        public void onSuccess(String response) {
            Log.d("Kairos Sucess", response);
            switch (action) {
                case 0:
                    progressDialog.dismiss();
                    setFragment(2,response);
                    break;
                case 1:
                    progressDialog.dismiss();
                    try{
                    JSONObject object = new JSONObject(response);
                    if (object.has("images")) {
                        detailInputFragment.userAdd();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Save Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("Json Enroll",e.toString());
                    progressDialog.dismiss();
                }

                    break;
                case 2:checkRecognize(response);
                    Log.e("In case","Recognize");
                    break;
            }
        }

        @Override
        public void onFail(String response) {
            // your code here!
            Log.d("Kairos Failure", response);
        }
    };

    private void setFragment(int code,String response) {
        switch (code ){
            case 1:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserListFragment()).commit();
                break;
            case 2:try {
                JSONObject object = new JSONObject(response);
                if (object.has("images")) {
                    DetectInfoFragment detectInfoFragment = new DetectInfoFragment();
                    detectInfoFragment.response = object;
                    detectInfoFragment.image = bitmap;
                    getSupportFragmentManager().beginTransaction().addToBackStack("null").replace(R.id.fragment_container,
                            detectInfoFragment).commit();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "No Faces Found",
                            Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Log.e("Json Detec Parse",e.toString());
            }
                break;
        }
    }

    public void setPopup(final int code) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Select");
        View view = getLayoutInflater().inflate(R.layout.camera_choice_dialog,null);
        ListView dialogList = (ListView) view.findViewById(R.id.dialog_listview);
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.camera_select_options));
        dialogList.setAdapter(adapter);
        dialog.setContentView(view);

        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0:Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        switch (code) {
                            case 0:startActivityForResult(takePicture, 00);
                                break;
                            case 1:startActivityForResult(takePicture, 01);
                                break;

                        }
                        break;
                    case 1:Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        switch (code) {
                            case 0:startActivityForResult(pickPhoto, 10);
                                break;
                            case 1:startActivityForResult(pickPhoto, 11);
                                break;

                        }
                        break;
                }
            }
        });
        dialog.show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            switch (requestCode) {
                case 00:
                    Bitmap bitmap1 = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    Toast.makeText(getApplicationContext(),
                            "Detect - Camera",
                            Toast.LENGTH_SHORT).show();
                    action = 0;
                    bitmap = bitmap1;
                    detectFace(bitmap);
                    break;
                case 01:
                    Bitmap bitmap2 = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    Toast.makeText(getApplicationContext(),
                            "Recognize - Camera",
                            Toast.LENGTH_SHORT).show();
                    action = 2;
                    bitmap = bitmap2;
                    recognizeFace(bitmap2);
                    break;
                case 10:
                    Uri selectedImage1 = imageReturnedIntent.getData();
                    Bitmap bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage1);

                    Toast.makeText(getApplicationContext(),
                            "Detect - Upload",
                            Toast.LENGTH_SHORT).show();
                    action = 0;
                    bitmap = bitmap3;

                    detectFace(bitmap3);
                    break;
                case 11:
                    Uri selectedImage2 = imageReturnedIntent.getData();
                    Bitmap bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage2);

                    Toast.makeText(getApplicationContext(),
                            "Recognize - Upload",
                            Toast.LENGTH_SHORT).show();
                    action = 2;
                    bitmap = bitmap4;

                    recognizeFace(bitmap4);
                    break;
            }
        }
        catch (Exception e) {
            Log.e("Camera Intent",e.toString());
        }
    }

    private void detectFace(Bitmap image) {
        progressDialog.show();
        String selector = "FULL";
        String minHeadScale = "0.25";
        try {
            myKairos.detect(image, selector, minHeadScale, listener);
        }
        catch (Exception e) {
            Log.e("Kairos Json",e.toString());
        }
    }
    public void enrollFace(Bitmap bitmap,String name) {
        progressDialog.show();
        action = 1;
        String subjectId = name;
        String galleryId = "friends";
        String selector = "FULL";
        String multipleFaces = "false";
        String minHeadScale = "0.25";
        try {
            myKairos.enroll(bitmap,
                    subjectId,
                    galleryId,
                    selector,
                    multipleFaces,
                    minHeadScale,
                    listener);
        }
        catch (Exception e) {
            Log.e("Enroll Kairos",e.toString());
        }
    }

    private void recognizeFace(Bitmap bitmap) {
        progressDialog.show();
        String galleryId = "friends";
        String selector = "FULL";
        String threshold = "0.75";
        String minHeadScale = "0.25";
        String maxNumResults = "3";
        try {
            myKairos.recognize(bitmap,
                    galleryId,
                    selector,
                    threshold,
                    minHeadScale,
                    maxNumResults,
                    listener);
        }
        catch (Exception e) {
            Log.e("Recognize Kairos",e.toString());
        }
    }

    public void setDetailInputFragment(Bitmap bitmap) {
        detailInputFragment = new DetailInputFragment();
        detailInputFragment.bitmap = bitmap;
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                detailInputFragment).commit();
    }

    private void checkRecognize(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("images")) {
                Log.e("Has","Images");
                JSONArray images = jsonObject.getJSONArray("images");
                JSONObject object = images.getJSONObject(0);
                if(object.has("candidates")) {
                    Log.e("Has","Candidates");
                    JSONArray candidates = object.getJSONArray("candidates");
                    List<User> users = new ArrayList<>();
                    if(candidates.length() > 0) {
                        for (int i = 0; i <= candidates.length(); i++) {

                            JSONObject person = candidates.getJSONObject(i);
                            String name = person.getString("subject_id");
                            users = User.find(User.class,"name=?",name);
                            if(users.size() > 0) {
                                progressDialog.dismiss();
                                UserDetailFragment userDetailFragment = new UserDetailFragment();
                                userDetailFragment.user = users.get(0);
                                userDetailFragment.bitmap = this.bitmap;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        userDetailFragment).commit();
                                break;
                            }
                        }
                    }

                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "No Recognition Found",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "No Recognition Found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        catch (Exception e) {
            Log.e("Recognize Check Json",e.toString());
        }
    }
}
