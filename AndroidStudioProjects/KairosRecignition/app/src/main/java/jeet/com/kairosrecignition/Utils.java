package jeet.com.kairosrecignition;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vince on 3/22/17.
 */
public class Utils {
    public static int JsonArrayToUser(JSONArray response) {
        User.deleteAll(User.class);
        try {
            for(int i = 0 ; i < response.length() ; i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                User user = new User();
                user.setEmail(jsonObject.getString("email"));
                user.setWebid(jsonObject.getLong("id"));
                user.setName(jsonObject.getString("name"));
                user.setFb(jsonObject.getString("facebook"));
                user.setInsta(jsonObject.getString("instagram"));
                user.setPhone(jsonObject.getString("phone"));
                user.setAddress(jsonObject.getString("address"));
                user.save();
            }
            return 1;
        }
        catch (Exception e) {
            Log.e("Json Array Utils",e.toString());
            return 0;
        }
    }


    public static JSONObject UserToJson(User user) {
        try {

            JSONObject object = new JSONObject();
            object.put("name",user.getName());
            object.put("address",user.getAddress());
            object.put("phone",user.getPhone());
            object.put("facebook",user.getFb());
            object.put("email",user.getEmail());
            object.put("instagram",user.getInsta());
            return object;
        }
        catch (Exception e) {
            Log.e("User To Jsn Utils",e.toString());
            return null;
        }
    }
}
