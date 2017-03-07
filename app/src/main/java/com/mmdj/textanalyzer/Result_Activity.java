package com.mmdj.textanalyzer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mmdj.textanalyzer.UI.PopUpWindow;
import com.mmdj.textanalyzer.fragments.SectionsPagerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.mmdj.textanalyzer.fragments.PlaceholderFragment.getSummaryMap;

public class Result_Activity extends AppCompatActivity {


    private static final String GET_TAG = "resultActivity";
    private static String textInString;
    private AdView mAdView;

    public static String getTextInString() {
        return textInString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //give context to  SectionsPagerAapter
        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager(), this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            // mViewPager.setOffscreenPageLimit(4);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

        /**
         *  adMob
         */

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("9742E5320F93696FBE2CA5608B8ABE86")
                .build();

        mAdView.loadAd(adRequest);

        /**************** From getting intent till transforming data here ************/
        //this take textInString from MainActivity
        Intent intent = getIntent();


        if (savedInstanceState != null) {
            textInString = savedInstanceState.getString("text");

        } else textInString = intent.getStringExtra("textInString");
        Log.d(GET_TAG,"textInString: "+textInString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", textInString);

    }

    /**************************
     * Menu:
     *******************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //   View viewItem = findViewById(id);

        if (id == R.id.action_about) {
            String message = getString(R.string.menuResult_about);
            PopUpWindow popUpWindow = new PopUpWindow(this, message);
            popUpWindow.doPopUpWindow();
            return true;
        }

        if (id == R.id.action_help) {
            String message = getString(R.string.menuResult_help);

            PopUpWindow popUpWindow = new PopUpWindow(this, message);
            popUpWindow.doPopUpWindow();

        }

        if (id == R.id.action_email) {

            //sending email:
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Text analyzer");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(fillMailBody().toString()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }


        return super.onOptionsItemSelected(item);
    }


    private StringBuilder fillMailBody() {
        StringBuilder body = new StringBuilder();
        body.append(getString(R.string.mail_summary));
        LinkedHashMap<String, Integer> summary = getSummaryMap();

        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            body = body.append("<div>")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("</div>");
        }
        body = body.append(getString(R.string.mail_footer));
        Log.d(GET_TAG, "bodyHTML: " + body);
        return body;
    }
}


