package jeet.com.kairosrecignition;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vince on 3/23/17.
 */
public class CustomDetectListAdapter extends RecyclerView.Adapter<CustomDetectListAdapter.MyViewHolder> {

    private JSONArray faces;
    private Context context;
    private Bitmap bitmap;
    public CustomDetectListAdapter(Context context, JSONArray faces, Bitmap bitmap) {
        this.faces = faces;
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.detect_item,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            JSONObject face = faces.getJSONObject(position);
            final Bitmap resizedbitmap = Bitmap.createBitmap(bitmap, face.getInt("topLeftX"),
                    face.getInt("topLeftY"),2*(-face.getInt("topLeftX")+face.getInt("leftEyeCenterX"))+
                            face.getInt("rightEyeCenterX")-face.getInt("leftEyeCenterX"),
                    face.getInt("chinTipY")-face.getInt("topLeftY"));
            holder.imageView.setImageBitmap(resizedbitmap);
            holder.topLeftX.setText("Top Left X : " + face.get("topLeftX").toString());
            holder.topLeftY.setText("Top Left Y : " + face.get("topLeftY").toString());
            holder.chinTipX.setText("Chin Tip X : " + face.get("chinTipX").toString());
            holder.chinTipY.setText("Chin Tip Y : " + face.get("chinTipY").toString());
            holder.rightEyeCenterX.setText("Right Eye X : " + face.get("rightEyeCenterX").toString());
            holder.leftEyeCenterX.setText("Left Eye X : " + face.get("leftEyeCenterX").toString());
            holder.rightEyeCenterY.setText("Right Eye Y : " + face.get("rightEyeCenterY").toString());
            holder.leftEyeCenterY.setText("Left Eye Y : " + face.get("leftEyeCenterY").toString());

            JSONObject attributes = face.getJSONObject("attributes");
            holder.asian.setText("Asian : " + attributes.get("asian").toString());
            holder.hispaniac.setText("Hispanic : " + attributes.get("hispanic").toString());
            holder.white.setText("White : " + attributes.get("white").toString());
            holder.black.setText("Black : " + attributes.get("black").toString());
            holder.age.setText("Age : " + attributes.get("age").toString());
            holder.gender.setText("Gender : " + attributes.getJSONObject("gender").get("type").toString());


            holder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.setDetailInputFragment(resizedbitmap);
                }
            });
        }
        catch (Exception e) {
            Log.e("Adapter Json Detec",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return faces.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public  TextView topLeftX,topLeftY,chinTipX,chinTipY,rightEyeCenterX,rightEyeCenterY,
        leftEyeCenterX,leftEyeCenterY,lips,gender,asian,white,black,hispaniac,age;
        public CircleImageView imageView;
        public Button save;
        public MyViewHolder(View relativeView) {
            super(relativeView);

            imageView = (CircleImageView) relativeView.findViewById(R.id.image);
            topLeftX = (TextView) relativeView.findViewById(R.id.topLeftX);
            topLeftY = (TextView) relativeView.findViewById(R.id.topLeftY);
            chinTipX = (TextView) relativeView.findViewById(R.id.chinTipX);

            chinTipY = (TextView) relativeView.findViewById(R.id.chinTipY);
            rightEyeCenterX = (TextView) relativeView.findViewById(R.id.rightEyeCenterX);
            rightEyeCenterY = (TextView) relativeView.findViewById(R.id.rightEyeCenterY);
            leftEyeCenterX = (TextView) relativeView.findViewById(R.id.leftEyeCenterX);
            leftEyeCenterY = (TextView) relativeView.findViewById(R.id.leftEyeCenterY);
            lips = (TextView) relativeView.findViewById(R.id.lips);
            gender = (TextView) relativeView.findViewById(R.id.gender);
            asian = (TextView) relativeView.findViewById(R.id.asian);
            white = (TextView) relativeView.findViewById(R.id.white);
            black = (TextView) relativeView.findViewById(R.id.black);
            hispaniac = (TextView) relativeView.findViewById(R.id.hispaniac);
            age = (TextView) relativeView.findViewById(R.id.age);
            save = (Button) relativeView.findViewById(R.id.save);
        }
    }
}
