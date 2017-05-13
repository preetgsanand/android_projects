package vince.zebapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomCollegeListAdapter extends RecyclerView.Adapter<CustomCollegeListAdapter.MyViewHolder> {

    private List<Person> persons;
    private Context context;
    public CustomCollegeListAdapter(Context context,List<Person> persons) {
        this.persons = persons;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_college_list_row,
                parent,
                false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.collegeName.setText(persons.get(position).getInstitute_name());
        holder.collegImg.setImageResource(R.drawable.placeholder);
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView collegeName;
        public ImageView collegImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            collegeName = (TextView) itemView.findViewById(R.id.collegeName);
            collegImg = (ImageView) itemView.findViewById(R.id.collegeImg);
        }
    }
}
