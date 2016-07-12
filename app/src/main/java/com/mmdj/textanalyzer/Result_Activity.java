package com.mmdj.textanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mmdj.textanalyzer.analisis.WordsCountAndSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Result_Activity extends AppCompatActivity {
    public String[] strArray = new String[0];
    private MainActivity main = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        Intent intent = getIntent();
        String textInString = intent.getStringExtra("textInString");

        //The amount of all characters
        WordsCountAndSort.elementaryCounts(textInString);


        //delete all punctuation signs and get string array:
        //TODO delete digits and other arrays
        strArray = textInString.toLowerCase().split("[\\p{Punct}\\s]+"); //without punctuation

        /***** words counting *****/
        List<Map.Entry<String, Integer>> wordsList = WordsCountAndSort.countAndSort(strArray);

        ArrayList<String> arList = new ArrayList<>();
        for (Map.Entry<String, Integer> map : wordsList) {
            arList.add(String.valueOf(map.getKey() + "\t\t-\t\t " + String.valueOf(map.getValue())));
        }

        ArrayAdapter<String> txtInListAdapter = new ArrayAdapter<>(
                this,                  //context
                R.layout.list_item,    //layout
                R.id.txtVw_listItem,   //list id
                arList);               //values


        ListView LstVw_Result = (ListView) findViewById(R.id.lstVw_result2);
        if (LstVw_Result != null)
            LstVw_Result.setAdapter(txtInListAdapter);
    }


}
