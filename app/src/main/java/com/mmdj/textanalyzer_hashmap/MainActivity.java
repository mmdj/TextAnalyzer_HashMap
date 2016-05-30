package com.mmdj.textanalyzer_hashmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText EditTextInput;
    ListView LstVw_Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /****************************************************
     * Main function
     ***************************************************/
    public void analyzeText(View view) {
        String[] strArray = new String[0];

        //doing change to weight of ListView
        LstVw_Result = (ListView) findViewById(R.id.lstVw_result);
        LstVw_Result.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f));



        EditTextInput = (EditText) findViewById(R.id.edtTxt_input);

        String TextInString = null;
        if (EditTextInput != null) {
            TextInString = EditTextInput.getText().toString();
        }

        if (TextInString != null && TextInString.isEmpty()) {                            //checking text
            doToast("There no text for analyze.");
            return;
        }

         /*** instance 2 to remove punctuation.***/
        if (TextInString != null) {
            Intent intent = new Intent(this, Result_Activity.class);
            startActivity(intent);
            strArray = TextInString.split("[\\p{Punct}\\s]+");
        }


        List<Map.Entry<String, Integer>> result = analyzeArray(strArray);

        ArrayList<String> arList = new ArrayList<>();
        for (Map.Entry<String, Integer> map : result) {
            arList.add(String.valueOf(map.getKey() + "\t\t--\t\t " + String.valueOf(map.getValue())));
        }

        ArrayAdapter<String> txtInListAdapter = new ArrayAdapter<>(
                this,                           //context
                R.layout.list_item,    //layout
                R.id.txtVw_listItem,    //list id
                arList);


        ListView lv = (ListView) findViewById(R.id.lstVw_result);
        if (lv != null)
            lv.setAdapter(txtInListAdapter);

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

            /* instance 2 to remove punctuation.
            if (string.endsWith(".") ||
                    string.endsWith(",") ||
                    string.endsWith("!") ||
                    string.endsWith("?") ||
                    string.endsWith("-")) {
                string = string.substring(0, string.length() - 1);
            }
            */
            if (map.containsKey(string)) {
                value = map.get(string);
                value++;
            }

            map.put(string, value);
        }

    }


    /************************
     * For toasting!
     ***********************/
    private void doToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
