package com.mmdj.textanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result_Activity extends AppCompatActivity {
    public String[] strArray = new String[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        Intent intent = getIntent();
        String TextInString = intent.getStringExtra("TextInString");
        strArray = TextInString.split("[\\p{Punct}\\s]+");


        List<Map.Entry<String, Integer>> result = analyzeArray(strArray);

        ArrayList<String> arList = new ArrayList<>();
        for (Map.Entry<String, Integer> map : result) {
            arList.add(String.valueOf(map.getKey() + "\t\t-\t\t " + String.valueOf(map.getValue())));
        }

        ArrayAdapter<String> txtInListAdapter = new ArrayAdapter<>(
                this,                           //context
                R.layout.list_item,    //layout
                R.id.txtVw_listItem,    //list id
                arList);


        ListView LstVw_Result = (ListView) findViewById(R.id.lstVw_result2);
        if (LstVw_Result != null)
            LstVw_Result.setAdapter(txtInListAdapter);
    }



    public static List<Map.Entry<String, Integer>> analyzeArray(String[] strArray) {
        HashMap<String, Integer> map = new HashMap<>();

        fillMap(map, strArray);

        List<Map.Entry<String, Integer>> list = getList(map); //for sorting
        Collections.sort(list, new EntriesComparator());
        return list;
    }

    private static List<Map.Entry<String, Integer>> getList(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> res = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            res.add(entry);
        }
        return res;
    }

    private static void fillMap(HashMap<String, Integer> map, String[] strArray) {
        for (String string : strArray) {
            int value = 1;
            if (map.containsKey(string)) {
                value = map.get(string);
                value++;
            }
            map.put(string, value);
        }
    }
}
