package com.mmdj.textanalyzer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mmdj.textanalyzer.fragments.SectionsPagerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.mmdj.textanalyzer.fragments.PlaceholderFragment.getSummaryMap;

public class Result_Activity extends AppCompatActivity {


    private static final String GET_TAG = "resultActivity";
    private static String textInString;


    public static String getTextInString() {
        return textInString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //give context to  SectionsPagerAdapter
        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            // mViewPager.setOffscreenPageLimit(4);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //sending email:
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Text analyzer");
                    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(fillMailBody().toString()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                   /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*///TODO to click on list item
                }

                private StringBuilder fillMailBody() {
                    StringBuilder body = new StringBuilder();
                    body.append("<HTML><BODY><h3>Summary:</h3></br>");
                    LinkedHashMap<String, Integer> summary = getSummaryMap();

                    for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                       body = body.append(entry.getKey()+": "+entry.getValue()+"</br>");
                    }
                    body= body.append("<h6>This mail was generated with Text Analyzer app</h6></BODY></HTML>");
                        Log.d(GET_TAG, "bodyHTML: " + body);
                    return body;
                }




            });
        }


        /**************** From getting intent till transforming data here ************/
        //this take textInString from MainActivity
        Intent intent = getIntent();


        if (savedInstanceState != null) {
            textInString = savedInstanceState.getString("text");

        } else textInString = intent.getStringExtra("textInString");
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


