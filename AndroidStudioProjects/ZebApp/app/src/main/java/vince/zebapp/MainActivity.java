package vince.zebapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private CollegeListFragment collegeListFragment;
    private boolean searchOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIntitalSetup();

    }

    private void closeKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                searchOpen = !searchOpen;
                setSearch(searchOpen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setFragment(int code) {
        switch (code) {
            case 1:
                collegeListFragment = new CollegeListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    collegeListFragment).commit();
                break;
        }
    }
    private void checkIntitalSetup() {
        Person person = Person.findById(Person.class,1);
        if(person == null) {
            callInitializationActivity();
        }
        else {
            closeKeyboard();
            setFragment(1);
        }
    }

    private void callInitializationActivity() {
        Intent intent = new Intent(getApplicationContext(),InitializationActivity.class);
        startActivity(intent);
        finish();
    }




    private void setSearch(boolean value) {
        RelativeLayout searchRelative = (RelativeLayout) findViewById(R.id.searchRelative);
        EditText search = (EditText) findViewById(R.id.searchQuery);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 4) {
                    List<Person> personList = new ArrayList<Person>();

                    for(int i = 0 ; i < collegeListFragment.persons.size() ; i++) {
                        if(collegeListFragment.persons.get(i).getInstitute_name().toLowerCase().contains(s.toString()
                        .toLowerCase())) {
                            personList.add(collegeListFragment.persons.get(i));
                        }
                    }

                    collegeListFragment.setRecyclerView(personList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(value == false) {
            searchRelative.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    0));
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(search.getWindowToken(), 0);
            collegeListFragment.setRecyclerView(collegeListFragment.persons);

        }
        else {
            searchRelative.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    140));
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(search, InputMethodManager.SHOW_FORCED);
        }
    }
    public void setDetailFragment(long id) {
        searchOpen = false;
        setSearch(searchOpen);
        CollegeDetailFragment collegeDetailFragment = CollegeDetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CollegeDetailFragment()).addToBackStack(null).commit();
    }



}
