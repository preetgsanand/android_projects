package com.example.vince.hackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JobDisplayFragment extends Fragment {


    private View view;
    private List<Job> jobs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_display, container, false);
        initializeViews();
        return view;
    }

    private void initializeViews() {
        ListView jobListView = (ListView) view.findViewById(R.id.jobListView);
        Button ongoing = (Button) view.findViewById(R.id.ongoing);
        Button deadline = (Button) view.findViewById(R.id.deadline);
        Button completed = (Button) view.findViewById(R.id.completed);

        jobs = Job.listAll(Job.class);
        Log.e("Size",jobs.size()+"");


        int ongoingNum=0,deadlineNum=0,completedNum=0;
        List<Job> jobs = Job.listAll(Job.class);

        for(Job job : jobs) {
            long deadlineLong=0,currentLong=0;
            SimpleDateFormat f = new SimpleDateFormat("dd MMM,yyyy");
            String curDate = f.format(new Date());

            try {
                Date deadlineDate = f.parse(job.getDeadlineDate());
                deadlineLong = deadlineDate.getTime();

                Date currentDate = f.parse(curDate);
                currentLong = currentDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(currentLong > deadlineLong) {
                deadlineNum+=1;
                job.setStatus("Abandoned");
                job.save();
            }
            else if(job.getStatus().equals("Ongoing")) {
                ongoingNum+=1;
            }
            else if(job.getStatus().equals("Completed")) {
                completedNum+=1;
            }
        }

        ongoing.setText(ongoingNum+" ONGOING");
        deadline.setText(deadlineNum+" ABANDONED");
        completed.setText(completedNum+ " COMPLETED");

        JobListCustomAdapter adapter = new JobListCustomAdapter(jobs,
                getContext());
        jobListView.setAdapter(adapter);
    }

}
