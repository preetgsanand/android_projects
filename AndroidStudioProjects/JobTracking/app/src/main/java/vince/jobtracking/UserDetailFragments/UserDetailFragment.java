package vince.jobtracking.UserDetailFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vince.jobtracking.Database.Job;
import vince.jobtracking.Database.User;
import vince.jobtracking.JobDetailActivity;
import vince.jobtracking.MainActivity;
import vince.jobtracking.R;
import vince.jobtracking.UserDetailActivity;
import vince.jobtracking.Utils.Utils;

public class UserDetailFragment extends Fragment {

    private static long id;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        if(Utils.MainAdminActivity == 1) {
            setHasOptionsMenu(true);
        }
        else {
            setHasOptionsMenu(false);
        }
        initializeViews();
        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.job_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit: ((UserDetailActivity)getActivity()).setFragment(2);
                break;
            case R.id.report:
                callEmailIntent();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public static UserDetailFragment newInstance(long ID) {
        UserDetailFragment userDetailFragment = new UserDetailFragment();
        id = ID;
        return userDetailFragment;
    }
    private void initializeViews() {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView email = (TextView) view.findViewById(R.id.email);
        TextView phone = (TextView) view.findViewById(R.id.phone);
        TextView dob = (TextView) view.findViewById(R.id.dob);
        TextView department = (TextView) view.findViewById(R.id.department);
        ListView userJobs = (ListView) view.findViewById(R.id.userJobListView);

        User user = User.findById(User.class,id);

        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        dob.setText(Utils.LongToDate(user.getDob()));
        department.setText(getResources().getStringArray(R.array.departments)[(int)user.getDepartment()-1].toString());

        final List<Job> jobs = Job.listAll(Job.class);
        List<String> jobNames = new ArrayList<>();
        for(int i = 0 ; i < jobs.size() ; i++) {
            if(jobs.get(i).getUserId().contains(String.valueOf(user.getWebid()))) {
                jobNames.add(jobs.get(i).getName());

            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                jobNames);

        userJobs.setAdapter(adapter);

        userJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), JobDetailActivity.class);
                intent.putExtra("id",jobs.get(position).getId());
                startActivity(intent);
            }
        });


    }

    private void callEmailIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, User.findById(User.class,id).getEmail());
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

}
