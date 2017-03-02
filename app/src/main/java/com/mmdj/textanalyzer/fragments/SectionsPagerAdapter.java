package com.mmdj.textanalyzer.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mmdj.textanalyzer.R;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static String[] pageTitles = new String[5];
    private  Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
        pageTitles[0]=this.context.getString(R.string.pageTitles_summary);
        pageTitles[1]=this.context.getString(R.string.pageTitles_allWords);
        pageTitles[2]=this.context.getString(R.string.pageTitles_semanticCore);
        pageTitles[3]=this.context.getString(R.string.pageTitles_stopWords);
        pageTitles[4]=this.context.getString(R.string.pageTitles_textWithStopWords);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Fragment getItem(int position) { //compile all fragments

        switch (position) {
            case 0:
                return PlaceholderFragment
                        .newInstance(position + 1,0).setPageLayout(R.layout.fragment_summary);

            case 1:
                return PlaceholderFragment
                        .newInstance(position + 1,PlaceholderFragment.TYPE_ALL_WORDS).setPageLayout(R.layout.fragment_activity_listview);

            case 2:
                return PlaceholderFragment
                        .newInstance(position + 1,PlaceholderFragment.TYPE_SEMANTIC).setPageLayout(R.layout.fragment_activity_listview);

            case 3:
                return PlaceholderFragment
                        .newInstance(position + 1,PlaceholderFragment.TYPE_STOP_WORD).setPageLayout(R.layout.fragment_activity_listview);
            case 4:
                return PlaceholderFragment
                        .newInstance(position + 1,PlaceholderFragment.TYPE_TEXT_WITH_STOP_WORD).setPageLayout(R.layout.fragment_text_with_stop_words);
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
