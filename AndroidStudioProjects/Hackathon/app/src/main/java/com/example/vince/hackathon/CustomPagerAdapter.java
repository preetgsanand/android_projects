package com.example.vince.hackathon;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by vince on 1/26/17.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private ViewPager pager;
    private List<Fragment> fragmentList;
    private TabLayout tabLayout;

    public CustomPagerAdapter(FragmentManager fm, ViewPager pager,List<Fragment> fragmentList,TabLayout tabLayout) {
        super(fm);
        this.pager = pager;
        this.fragmentList = fragmentList;
        this.tabLayout = tabLayout;
        tabTitles = new String[] {"Profile",
        "Jobs"};
        tabLayout.setupWithViewPager(pager);

    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
