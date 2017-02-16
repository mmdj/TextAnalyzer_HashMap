package com.mmdj.textanalyzer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mmdj.textanalyzer.R;
import com.mmdj.textanalyzer.Result_Activity;
import com.mmdj.textanalyzer.UI.PopUpWindow;

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


public class PlaceholderFragment extends Fragment implements View.OnClickListener {
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


    private static LinkedHashMap<String, Integer> summaryMap;
    private static List<Map.Entry<String, Integer>> wordsList;
    private static List<Map.Entry<String, Integer>> sortedStopWordsList;
    private static List<Map.Entry<String, Integer>> sortedSemanticCoreList;

    public static LinkedHashMap<String, Integer> getSummaryMap() {
        return summaryMap;
    }

    public static List<Map.Entry<String, Integer>> getWordsList() {
        return wordsList;
    }

    public static List<Map.Entry<String, Integer>> getSortedStopWordList() {
        return sortedStopWordsList;
    }

    public static List<Map.Entry<String, Integer>> getSortedSemanticCoreList() {
        return sortedSemanticCoreList;
    }

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


        /**
         * TextViews onClick Handler (Help)
         */
        TextView txtVw_charsNumber = (TextView) rootView.findViewById(R.id.txtVw_charsNumber);
        TextView txtVw_charsNumberWithoutSpaces = (TextView) rootView.findViewById(R.id.txtVw_charsNumberWithoutSpaces);
        TextView txtVw_charsWOPunct = (TextView) rootView.findViewById(R.id.txtVw_charsWOPunct);
        TextView txtVw_charsMeaningful = (TextView) rootView.findViewById(R.id.txtVw_charsMeaningful);
        TextView txtVw_wordsNumber = (TextView) rootView.findViewById(R.id.txtVw_wordsNumber);
        TextView txtVw_uniqueWords = (TextView) rootView.findViewById(R.id.txtVw_uniqueWords);
        TextView txtVw_stopWords = (TextView) rootView.findViewById(R.id.txtVw_stopWords);
        TextView txtVw_dilution = (TextView) rootView.findViewById(R.id.txtVw_dilution);

        if (txtVw_charsNumber != null) {
            txtVw_charsNumber.setOnClickListener(this);
        }
        if (txtVw_charsNumberWithoutSpaces != null) {
            txtVw_charsNumberWithoutSpaces.setOnClickListener(this);
        }
        if (txtVw_charsWOPunct != null) {
            txtVw_charsWOPunct.setOnClickListener(this);
        }
        if (txtVw_charsMeaningful != null) {
            txtVw_charsMeaningful.setOnClickListener(this);
        }
        if (txtVw_wordsNumber != null) {
            txtVw_wordsNumber.setOnClickListener(this);
        }
        if (txtVw_uniqueWords != null) {
            txtVw_uniqueWords.setOnClickListener(this);
        }
        if (txtVw_stopWords != null) {
            txtVw_stopWords.setOnClickListener(this);
        }
        if (txtVw_dilution != null) {
            txtVw_dilution.setOnClickListener(this);
        }


        ArrayList<String> stopWordsAllLang = getStopWordsFromArraysXML();
        summaryMap = elementaryCounts(textInStringFromActivity, stopWordsAllLang, getContext());
        wordsList = countAndSort(allWords);
        sortedStopWordsList = getSortedStopWordsList();
        sortedSemanticCoreList = getSemanticCoreList();
        // Log.d("analyze", "onCreateView is started | wordsList=" + wordsList);


        /***** words counting *****/

        if (rootView.findViewById(lstVw_result) != null) {

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

        ArrayList<String> list = new ArrayList<>(Arrays.asList(stopWordsEn));
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
        String allChars = String.valueOf(map.get(getString(R.string.txtSummary_all_symbols_number)));
        String woSpaces = String.valueOf(map.get(getString(R.string.txtSum_without_spaces)));
        String woPunct = String.valueOf(map.get(getString(R.string.txtSum_without_punctuation_marks)));
        String charsSignificant = String.valueOf(map.get(getString(R.string.txtSum_significant_characters_only)));
        String allWords = String.valueOf(map.get(getString(R.string.txtSum_number_of_words)));
        String uniqueWords = String.valueOf(map.get(getString(R.string.txtSum_unique_words)));
        String stopWords = String.valueOf(map.get(getString(R.string.txtSum_number_of_stop_words)));
        String dilution = String.valueOf(map.get(getString(R.string.txtSum_percent_of_dilution)));

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

        String textColor ="#ff303030";
        if (Integer.parseInt(dilution)<=15) textColor = "#99cc00";
        else if (Integer.parseInt(dilution)<30) textColor = "#ff4444";
        else if (Integer.parseInt(dilution)>=30) textColor = "#ffbb33";
        dilutionInt.setTextColor(Color.parseColor(textColor));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtVw_charsNumber:
                new PopUpWindow(getActivity(), getString(R.string.helpSum_all_symbols)).doPopUpWindow();
                break;
            case R.id.txtVw_charsNumberWithoutSpaces:
                new PopUpWindow(getActivity(), getString(R.string.helpSum_woSpaces)).doPopUpWindow();
                break;
            case R.id.txtVw_charsWOPunct:
                new PopUpWindow(getActivity(), getString(R.string.helpSum_woPunctuation_marks)).doPopUpWindow();
                break;
            case  R.id.txtVw_charsMeaningful:
                new PopUpWindow(getActivity(),
                        getString(R.string.helpSum_charsMeaningful)).doPopUpWindow();
                break;
            case R.id.txtVw_wordsNumber:
                new PopUpWindow(getActivity(), getString(R.string.helpSum_number_of_words)).doPopUpWindow();
                break;
            case R.id.txtVw_uniqueWords:
                new PopUpWindow(getActivity(), getString(R.string.helpSum_unique_words)).doPopUpWindow();
                break;
            case R.id.txtVw_stopWords:
                new PopUpWindow(getActivity(), getString(R.string.helpSum_number_of_stop_words)).doPopUpWindow();
                break;
            case R.id.txtVw_dilution:
               new PopUpWindow(getActivity(), getString(R.string.helpSum_percent_of_dilution)).doPopUpWindow();
                break;
        }

    }
}


