package com.mmdj.textanalyzer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mmdj.textanalyzer.R;
import com.mmdj.textanalyzer.Result_Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mmdj.textanalyzer.R.id.lstVw_result;
import static com.mmdj.textanalyzer.analisis.WordsCountAndSort.countAndSort;
import static com.mmdj.textanalyzer.analisis.WordsCountAndSort.elementaryCounts;
import static com.mmdj.textanalyzer.analisis.WordsCountAndSort.textInStringToArray;


public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String GET_TAG = "PlaceHolderFragment";
    private int mPageLayout = R.layout.fragment_activity_listview;

    private View rootView;
    private String textInStringFromActivity = Result_Activity.getTextInString();
    private String[] allWords = textInStringToArray(textInStringFromActivity);
    private List<Map.Entry<String, Integer>> wordsList = countAndSort(allWords);

    public PlaceholderFragment() {
    }


    public PlaceholderFragment setPageLayout(int pageLayout) {
        this.mPageLayout = pageLayout;
        return this;
    }


    /***
     * It returns a new instance of this fragment for the given section number.
     ***/

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //Save the fragment during the turn
//        Log.d(GET_TAG, "onCreate started ");
    }


    /***
     * getting fragment
     ***/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(mPageLayout, container, false);
//        ViewForList = inflater.inflate(R.layout.fragment_activity_listview, container, false);

//        Log.d(GET_TAG, "onCreateView started and rootview is: " + rootView.findViewById(lstVw_result));

        super.onActivityCreated(savedInstanceState);


        /***** words counting *****/

        if (rootView.findViewById(lstVw_result) != null) {      //List of all words fragment

            listViewFiller(wordsList);

        } else {                                                //Summary fragment
            ArrayList<String> stopWordsAllLang = getStopWordsFromArraysXML();
            LinkedHashMap<String, Integer> summaryMap = elementaryCounts(textInStringFromActivity, stopWordsAllLang);
            summaryPageFiller(summaryMap);
        }


        return rootView;
    }


    private ArrayList<String> getStopWordsFromArraysXML() {
        String[] stopWordsEn = getResources().getStringArray(R.array.stopWordsEn);
        String[] stopWordsRu = getResources().getStringArray(R.array.stopWordsRu);
        String[] stopWordsHe = getResources().getStringArray(R.array.stopWordsHe);

        ArrayList<String> list = new ArrayList(Arrays.asList(stopWordsEn));
        list.addAll(Arrays.asList(stopWordsRu));
        list.addAll(Arrays.asList(stopWordsHe));
        Collections.sort(list);

        return list;
    }


    /*******************************
     * filling ListView:
     *******************************/

    public void listViewFiller(List<Map.Entry<String, Integer>> wordsList) {
        //  ListView LstVw_Result = new ListView(context);
        List<Map<String, String>> arListOfMaps = new ArrayList<>();
        // ArrayList<String> arList = new ArrayList<>();

        Map<String, String> simpleMap;
        for (Map.Entry<String, Integer> map : wordsList) {
            simpleMap = new HashMap<>();
            simpleMap.put("Word", map.getKey());
            simpleMap.put("Amount", map.getValue().toString());
            arListOfMaps.add(simpleMap);
        }

        //  Log.d(main.getLogTag(), "simpleMap: " + simpleMap.toString());
        // Log.d(main.getLogTag(), "ListOfMaps: " + arListOfMaps.toString());


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
                //SectionsPagerAdapter.mContext,//take a context from  SectionsPagerAdapter
                getActivity(),
                arListOfMaps,
                // android.R.layout.simple_list_item_2,                  //default
                R.layout.list_item,
                new String[]{"Word", "Amount"},
                // new int[] { android.R.id.text1,android.R.id.text2 }); //default
                new int[]{R.id.txtVw_wordName, R.id.txtVw_wordAmount});

        //  PlaceholderFragment.newInstance(3).getPageLayout()


        ListView lstVw_Result = (ListView) rootView.findViewById(R.id.lstVw_result);//null  rootView.findViewById(R.id.lstVw_result)
        Log.d("PlaceHolderFragment", "R.id.lstVw_result: " + lstVw_Result);
        Log.d("PlaceHolderFragment", "View " + rootView.findViewById(R.id.lstVw_result));
        if (lstVw_Result != null) {//??
            lstVw_Result.setAdapter(txtInListAdapter);
        }

    }

    public void summaryPageFiller(LinkedHashMap<String, Integer> map) {


        //map from WordsCountAndSort.elementaryCounts()
        String allChars = String.valueOf(map.get("allChars"));
        String woSpaces = String.valueOf(map.get("woSpaces"));
        String woPunct = String.valueOf(map.get("woPunct"));
        String charsSignificant = String.valueOf(map.get("significantChars"));
        String allWords = String.valueOf(map.get("allWords"));
        String uniqueWords = String.valueOf(map.get("uniqueWords"));
        String stopWords = String.valueOf(map.get("stopWords"));
        String dilution = String.valueOf(map.get("dilution"));


        //get id
        TextView charsNumberInt = (TextView) rootView.findViewById(R.id.txtVw_charsNumberInt);
        TextView charsWOSpaces = (TextView) rootView.findViewById(R.id.txtVw_charsNumberWithoutSpacesInt);
        TextView charsWOPunct = (TextView) rootView.findViewById(R.id.txtVw_charsWOPunctInt);
        TextView significantChars = (TextView) rootView.findViewById(R.id.txtVw_charsMeaningfulInt);
        TextView allWordsInt = (TextView) rootView.findViewById(R.id.txtVw_wordsNumberInt);
        TextView uniqueWordsInt = (TextView) rootView.findViewById(R.id.txtVw_uniqueWordsInt);
        TextView stopWordsInt = (TextView) rootView.findViewById(R.id.txtVw_stopWordsInt);
        TextView dilutionInt = (TextView) rootView.findViewById(R.id.txtVw_dilutionInt);

        charsNumberInt.setText(allChars);
        charsWOSpaces.setText(woSpaces);
        charsWOPunct.setText(woPunct);
        significantChars.setText(charsSignificant);
        allWordsInt.setText(allWords);
        uniqueWordsInt.setText(uniqueWords);
        stopWordsInt.setText(stopWords);
        dilutionInt.setText(dilution);

    }


}


