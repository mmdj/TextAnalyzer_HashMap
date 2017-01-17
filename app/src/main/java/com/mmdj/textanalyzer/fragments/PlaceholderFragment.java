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
import static com.mmdj.textanalyzer.analisis.WordsCountAndSort.getSemanticCoreList;
import static com.mmdj.textanalyzer.analisis.WordsCountAndSort.getSortedStopWordsList;
import static com.mmdj.textanalyzer.analisis.WordsCountAndSort.textInStringToArray;


public class PlaceholderFragment extends Fragment {
    public static final int TYPE_SEMANTIC = 1;
    public static final int TYPE_STOP_WORD = 2;
    public static final int TYPE_ALL_WORDS = 3;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String GET_TAG = "PlaceHolderFragment";
    private int mPageLayout = R.layout.fragment_activity_listview;
    private int currentType;

    private View rootView;
    private String textInStringFromActivity = Result_Activity.getTextInString();
    private String[] allWords = textInStringToArray(textInStringFromActivity);
    //  private List<Map.Entry<String, Integer>> wordsList;

    public PlaceholderFragment() {
    }


    public PlaceholderFragment setPageLayout(int pageLayout) {
        this.mPageLayout = pageLayout;
        return this;
    }


    /***
     * It returns a new instance of this fragment for the given section number.
     ***/

    public static PlaceholderFragment newInstance(int sectionNumber, int listType) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.currentType = listType;
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

        super.onActivityCreated(savedInstanceState);

        ArrayList<String> stopWordsAllLang = getStopWordsFromArraysXML();
        LinkedHashMap<String, Integer> summaryMap = elementaryCounts(textInStringFromActivity, stopWordsAllLang);
        List<Map.Entry<String, Integer>> wordsList = countAndSort(allWords);
        List<Map.Entry<String, Integer>> sortedStopWordsList = getSortedStopWordsList();
        List<Map.Entry<String, Integer>> sortedSemanticCoreList = getSemanticCoreList();
        Log.d("analyze", "onCreateView is started | wordsList=" + wordsList);


        /***** words counting *****/

        if (rootView.findViewById(lstVw_result) != null) {
            //   int fragmentNumber = SectionsPagerAdapter.getPositionNumber();//this is not a true solution TODO

            if (currentType == TYPE_ALL_WORDS) {
                listViewFiller(wordsList);                   //List of all words fragment

                /*
                int i=0;
                for (Map.Entry word:wordsList) {
                     Log.d(GET_TAG, "Fragment"+ currentType + ". wordsList: " + (++i) +"-" + word.getKey());
                 }*/
            } else if (currentType == TYPE_SEMANTIC) {
                listViewFiller(sortedSemanticCoreList);//Semantic Core List
            } else if (currentType == TYPE_STOP_WORD) {
                listViewFiller(sortedStopWordsList);                  //List of stop-words fragment
            }


        } else {                                             //Summary fragment

            summaryPageFiller(summaryMap);
        }

        return rootView;
    }

    /**********************************
     * getting StopWords from xml
     ***********************************/
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
     * filling any ListView:
     *******************************/

    public void listViewFiller(List<Map.Entry<String, Integer>> wordsList) {

        List<Map<String, String>> arListOfMaps = new ArrayList<>();

        Map<String, String> simpleMap;
        for (Map.Entry<String, Integer> map : wordsList) {
            simpleMap = new HashMap<>();
            simpleMap.put("Word", map.getKey());
            simpleMap.put("Amount", map.getValue().toString());
            arListOfMaps.add(simpleMap);
        }

        SimpleAdapter txtInListAdapter = new SimpleAdapter(
                getActivity(),// take a context from  SectionsPagerAdapter
                arListOfMaps,
                R.layout.list_item,
                new String[]{"Word", "Amount"},
                new int[]{R.id.txtVw_wordName, R.id.txtVw_wordAmount});

        ListView lstVw_Result = (ListView) rootView.findViewById(lstVw_result);

        if (lstVw_Result != null) {
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

        //set text in activity
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


