package vince.zebapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.google.gson.JsonParser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URISyntaxException;

import io.fabric.sdk.android.Fabric;

public class InitializationActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "nrdZchPnbE5YPZMK5HmWbk8vc";
    private static final String TWITTER_SECRET = "1PWRtLjlTVzCXGf1AEh0Oo3plJragD5bcSkDIRAbnghsvxNBGY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        setFragment(1);
    }


    public void setFragment(int code) {
        switch (code) {
            case 1:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new IntroFragment()).commit();
                break;
            case 2:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new OTPFragment()).addToBackStack(null).commit();
                break;
            case 3:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new IntroSyncFragment()).commit();
                break;
        }
    }

    public void setMainActivity() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void askPermissions() {
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
        showFileChooser();
    }

    private static final int FILE_SELECT_CODE = 0;

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("Log ", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        String path = getPath(this, uri);
                        Log.d("Log ", "File Path: " + path);
                        getJSON(path);
                    }
                    catch (Exception e) {
                        Log.d("Log",e.toString());
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Context context, Uri uri) throws URISyntaxException {
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
                Toast.makeText(getApplicationContext(),
                        "Cannot read file path.\nTry to use a different explorer.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    private void getJSON(String path) {
        File file = new File(path);
        int length = (int) file.length();

        byte[] bytes = new byte[length];
        try {
            FileInputStream in = new FileInputStream(file);

            in.read(bytes);

            in.close();

            String contents = new String(bytes);


            JSONObject jsonObject = new JSONObject(contents);
            parseJSON(jsonObject);
            Log.d("JSON", jsonObject.toString());
        }
        catch(Exception e) {
            Toast.makeText(getApplication(),
                    "Inconsistent File Format or File Path",
                    Toast.LENGTH_SHORT).show();
            Log.d("Log",e.toString());
        }
    }

    private void parseJSON(JSONObject jsonObject) {
        try {
            JSONArray response = jsonObject.getJSONArray("response");
            for(int j = 0 ; j < response.length() ; j++) {
                JSONObject main = response.getJSONObject(j);
                JSONArray message = main.getJSONArray("message");

                for (int i = 0; i < message.length(); i++) {
                    JSONObject obj = message.getJSONObject(i);

                    Person person = new Person();

                    person.setInstitute_name(obj.getString("institute_name"));
                    person.setCity(obj.getString("city"));
                    person.setAddress(obj.getString("address"));
                    person.setDescription(obj.getString("description"));
                    person.save();

                }
            }
        }
        catch (Exception e) {
            Log.d("Json Exception",e.toString());
        }
    }
}
