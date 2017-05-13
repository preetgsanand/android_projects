package jeet.com.kairosrecignition;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetectInfoFragment extends Fragment {

    private View view;
    public Bitmap image;
    public JSONObject response;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detect_info, container, false);
        setRecycler();
        return view;
    }


    private void setRecycler() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        try {
            JSONArray images = response.getJSONArray("images");
            JSONObject obj = images.getJSONObject(0);
            JSONArray faces = obj.getJSONArray("faces");
            CustomDetectListAdapter adapter = new CustomDetectListAdapter(getContext(),
                    faces,
                    image);
            recyclerView.setAdapter(adapter);
        }
        catch (Exception e) {
            Log.e("Detect JSON Parse",e.toString());
        }
    }



}
