package jeet.com.kairosrecignition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserDetailFragment extends Fragment implements View.OnClickListener{

    private View view;
    public User user;
    public Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView email = (TextView) view.findViewById(R.id.email);
        TextView address = (TextView) view.findViewById(R.id.address);
        ImageView fb = (ImageView) view.findViewById(R.id.fb);
        ImageView insta = (ImageView) view.findViewById(R.id.insta);
        CircleImageView image = (CircleImageView) view.findViewById(R.id.image);

        fb.setOnClickListener(this);
        insta.setOnClickListener(this);

        name.setText("Name : "+user.getName());
        email.setText("Email : "+user.getEmail());
        address.setText("Address : "+user.getAddress());
        image.setImageBitmap(bitmap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb:
                startIntent("https://www.facebook.com/"+user.getFb());
                break;
            case R.id.insta:
                startIntent("https://www.instagram.com/"+user.getInsta());

                break;
        }
    }

    public void startIntent(String url) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
