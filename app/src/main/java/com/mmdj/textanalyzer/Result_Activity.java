package com.mmdj.textanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mmdj.textanalyzer.analisis.WordsCountAndSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result_Activity extends AppCompatActivity {
    public String[] strArray = new String[0];
  //  private MainActivity main = new MainActivity();

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



/*************************** From here filling ListView: *********************************/

        List<Map<String, String>> arListOfMaps = new ArrayList<>();
        // ArrayList<String> arList = new ArrayList<>();

        Map<String, String> simpleMap;
        for (Map.Entry<String, Integer> map : wordsList) {
            simpleMap = new HashMap<>();
            simpleMap.put("Word", map.getKey());
            simpleMap.put("Amount", map.getValue().toString());
            arListOfMaps.add(simpleMap);
        }

        // Log.d(main.getLogTag(), "simpleMap: " + simpleMap.toString());
        //Log.d(main.getLogTag(), "ListOfMaps: " + arListOfMaps.toString());


        //ArrayAdapter for list_item by one item:

       /*  List<Map<String, Integer>> arListOfMaps1 = new ArrayList<>();
        ArrayList<String> arList = new ArrayList<>();
        for (Map.Entry<String, Integer> map : wordsList) {
            arList.add(String.valueOf(map.getKey() + "\t\t\t\t " + String.valueOf(map.getValue())));
        }

       ArrayAdapter<String> txtInListAdapter = new ArrayAdapter<>(
                this,                  //context
                R.layout.list_item,    //layout
                R.id.txtVw_wordAmount, //list id
                arList);               //values
        */


        // ArrayAdapter for list_item by two items */
        SimpleAdapter txtInListAdapter = new SimpleAdapter(
                this,
                arListOfMaps,
                // android.R.layout.simple_list_item_2,                  //default
                R.layout.list_item,
                new String[]{"Word", "Amount"},
                // new int[] { android.R.id.text1,android.R.id.text2 }); //default
                new int[]{R.id.txtVw_wordName, R.id.txtVw_wordAmount});


        ListView LstVw_Result = (ListView) findViewById(R.id.lstVw_result);
        if (LstVw_Result != null)
            LstVw_Result.setAdapter(txtInListAdapter);
    }


}
