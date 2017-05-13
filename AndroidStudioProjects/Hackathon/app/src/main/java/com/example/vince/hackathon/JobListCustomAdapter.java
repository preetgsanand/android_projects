package com.example.vince.hackathon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vince on 1/26/17.
 */
public class JobListCustomAdapter extends BaseAdapter {

    private List<Job> jobList;
    private Context context;
    private LayoutInflater inflater;

    public JobListCustomAdapter(List<Job> jobList,Context context) {
        this.jobList = jobList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return jobList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.job_list_custom_adapter_item,null);

        TextView number = (TextView) view.findViewById(R.id.number);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView title = (TextView) view.findViewById(R.id.jobTitle);
        final TextView description = (TextView) view.findViewById(R.id.description);
        TextView addedDate = (TextView) view.findViewById(R.id.addedDate);
        TextView deadline = (TextView) view.findViewById(R.id.deadline);
        final Button expansion = (Button) view.findViewById(R.id.expansionButton);
        final RelativeLayout descriptionLayout = (RelativeLayout) view.findViewById(R.id.descriptionLayout);

        expansion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams descriptionlp= (RelativeLayout.LayoutParams)descriptionLayout
                        .getLayoutParams();
                if(expansion.getText().equals("\u25BC")) {

                    descriptionlp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    descriptionLayout.setLayoutParams(descriptionlp);
                    expansion.setText("\u25B2");
                }
                else {
                    expansion.setText("▼");
                    descriptionlp.height = 0;
                    descriptionLayout.setLayoutParams(descriptionlp);
                }
            }
        });

        status.setText(jobList.get(position).getStatus());
        number.setText(Integer.valueOf(position+1)+"");

        if(jobList.get(position).getStatus().equals("Abandoned")) {
            number.setBackgroundColor(context.getResources().getColor(R.color.abandoned));
        }
        else if(jobList.get(position).getStatus().equals("Ongoing")) {
            number.setBackgroundColor(context.getResources().getColor(R.color.ongoing));
        }
        else if(jobList.get(position).getStatus().equals("Completed")) {
            number.setBackgroundColor(context.getResources().getColor(R.color.completed));
        }
        title.setText(jobList.get(position).getTitle());
        deadline.setText(jobList.get(position).getDeadlineDate());
        description.setText(jobList.get(position).getDescription());
        addedDate.setText(jobList.get(position).getAddedDate());

        return view;
    }
}
