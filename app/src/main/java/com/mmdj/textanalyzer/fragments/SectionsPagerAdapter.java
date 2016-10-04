package com.mmdj.textanalyzer.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mmdj.textanalyzer.R;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static Context context;

    String[] pageTitles = {"Summary"
            , "Semantic Core"
            , "Words"
            , "Stop-words"};


    public SectionsPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        this.context = context;
    }

//public Context getContext() {
    //   return context;
//}

    @Override
    public Fragment getItem(int position) { //compile all fragments

        switch (position) {
            case 0:
                PlaceholderFragment pf_Summary = PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_summary);
                return pf_Summary;

            case 1:
                return PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_summary);

            case 2:
                PlaceholderFragment pf_wordsList = PlaceholderFragment
                        .newInstance(position + 1).setPageLayout(R.layout.fragment_activity_listview);
                return pf_wordsList;

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
