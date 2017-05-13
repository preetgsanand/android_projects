package vince.jobtracking.JobDetailFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vince.jobtracking.AddActivity;
import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.JobDetailActivity;
import vince.jobtracking.R;
import vince.jobtracking.Utils.Utils;

public class JobDetailEditFragment extends Fragment implements View.OnClickListener{

    private View view;
    private static long id;
    TextView name,description,deadline,userRequired;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Job job;
    Spinner status,submitRequest,department;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_job_detail_edit, container, false);
        setHasOptionsMenu(true);
        initializeViews();
        return  view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.job_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                editJob();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public static JobDetailEditFragment newInstance(long ID) {
        JobDetailEditFragment jobDetailEditFragment = new JobDetailEditFragment();
        id = ID;
        return  jobDetailEditFragment;
    }


    private void editJob() {
        if(id == 0) {
            job = new Job();
        }
        job.setName(name.getText().toString());
        job.setDescription(description.getText().toString());
        job.setDeadline(Utils.DateToLong(deadline.getText().toString()));
        job.setSubmitRequest(submitRequest.getSelectedItemPosition()+1);
        job.setStatus(status.getSelectedItemPosition()+1);
        job.setDepartment(department.getSelectedItemPosition()+1);
        job.setUserRequired(Integer.parseInt(userRequired.getText().toString()));
        if(Utils.activity == 3) {
            ((JobDetailActivity)getActivity()).callAPI(4,job,2);
        }
        else if(Utils.activity == 5) {
            ((AddActivity)getActivity()).callAPI(11,job,null);
        }


    }
    private void initializeViews() {

        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);
        deadline = (TextView) view.findViewById(R.id.deadline);
        submitRequest = (Spinner) view.findViewById(R.id.submitRequestSpinner);
        status = (Spinner) view.findViewById(R.id.statusSpinner);
        department = (Spinner) view.findViewById(R.id.departmentSpinner);
        Button selectDeadline = (Button) view.findViewById(R.id.selectDeadline);
        userRequired = (TextView) view.findViewById(R.id.userRequired);

        selectDeadline.setOnClickListener(this);

        List<User> users = User.find(User.class,"id!=?",1+"");
        List<String> userNames = new ArrayList<>();
        int spinnerPos = 0;

        if(id != 0) {
            job = Job.findById(Job.class, id);
        }


        ArrayAdapter<String> submitRequestAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.submit_request));

        submitRequest.setAdapter(submitRequestAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.job_status));

        status.setAdapter(statusAdapter);

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.departments));

        department.setAdapter(departmentAdapter);

        if(id != 0) {
            status.setSelection(job.getStatus()-1);
            submitRequest.setSelection(job.getSubmitRequest()-1);
            name.setText(job.getName());
            description.setText(job.getDescription());
            deadline.setText(Utils.LongToDate(job.getDeadline()));
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDeadline:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH)+1;
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                deadline.setText(year + "-" + String.format("%02d", monthOfYear) + "-" +
                                        String.format("%02d", dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

        }
    }

    public void setResult(int result) {
        switch (result) {
            case -1:
                Toast.makeText(getContext(),
                        "Submit Failed",
                        Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(),
                        "Submission Successful",
                        Toast.LENGTH_SHORT).show();
                if (Utils.activity == 3) {
                    ((JobDetailActivity) getActivity()).setFragment(1);
                }
                else if(Utils.activity == 5) {
                    ((AddActivity)getActivity()).setFragment(3,job,null);
                }
        }
    }
}
