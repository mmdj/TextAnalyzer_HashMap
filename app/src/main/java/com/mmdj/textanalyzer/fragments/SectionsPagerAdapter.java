package com.mmdj.textanalyzer.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mmdj.textanalyzer.R;


public class SectionsPagerAdapter extends FragmentPagerAdapter {



    String[] pageTitles = {"Summary"
            , "Semantic Core"
            , "Words"
            , "Stop-words"};


    public SectionsPagerAdapter(FragmentManager fm) {

        super(fm);

    }


    @Override
    public Fragment getItem(int position) { //compile all fragments

        switch (position) {
            case 0:
                return PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_summary);

            case 1:
                return PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_summary);

            case 2:
                return PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_activity_listview);

            case 3:
                return PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_summary);
        }
        return null;
    }

    @Override
    public int getCount() { // how mach pages
        return pageTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position].toUpperCase();
    }


}
