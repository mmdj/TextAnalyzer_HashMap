package com.mdolzhansky.textanalyzer.analisis;

import android.content.Context;
import android.util.Log;

import com.mdolzhansky.textanalyzer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WordsCountAndSort {

    private static String[] arrWords;
    private static final String GET_TAG = "analyzer";
    private static List<Map.Entry<String, Integer>> sortedStopWordsList;
    private static HashMap<String, Integer> countedWordsMap;
    private static List<Map.Entry<String, Integer>> semanticCoreList;


    public static List<Map.Entry<String, Integer>> getSemanticCoreList() {
        return semanticCoreList;
    }

    public static List<Map.Entry<String, Integer>> getSortedStopWordsList() {
        return sortedStopWordsList;
    }

    /**
     * convert received text from string to words array
     *
     * @param textInString
     * @return String[]
     */

    public static String[] textInStringToArray(String textInString) {
        //get string array
        //without punctuation and digits, but dividing by spaces
        arrWords = textInString.toLowerCase().split("(?!'\\w+)(?!\\w+')[^\\w]+|([0-9])+");

        //This list for removing ""(empty) elements from arrWords:
        List<String> list = new ArrayList<>();
        for (String s : arrWords) {
            if (s != null && s.length() > 0) {
                list.add(s);
            }
        }
        arrWords = list.toArray(new String[list.size()]);

        return arrWords;
    }


    /**
     * counting an amount of words and sorting
     *
     * @param words
     * @return List of sorted and counted words
     */
    public static List<Map.Entry<String, Integer>> countAndSort(String[] words) {
        HashMap<String, Integer> map = new HashMap<>();

        countedWordsMap = fillMapAndCounting(map, words);
        Log.d(GET_TAG, "countAndSort started | countedWordsMap=" + countedWordsMap);
        return sortingList(map);
    }


    /**
     * Filling list from map and sorting
     *
     * @param map with counted words
     * @return List for sorting
     */
    private static List<Map.Entry<String, Integer>> sortingList(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> res = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            res.add(entry);
        }
        Collections.sort(res, new EntriesComparator());

        return res;
    }


    /**
     * counting the amount of words and filling the hashMap
     *
     * @param map      empty
     * @param strArray uncounted words
     */
    private static HashMap<String, Integer> fillMapAndCounting(HashMap<String, Integer> map, String[] strArray) {
        for (String string : strArray) {
            int value = 1;
            if (map.containsKey(string)) {
                value = map.get(string);
                value++;
            }
            map.put(string, value);//word,count
        }
        return map;
    }


    /**
     * counting the simple statistic
     *
     * @param textInString     all text in one string
     * @param stopWordsAllLang
     * @return map with  text statistic
     */

    public static LinkedHashMap<String, Integer> elementaryCounts(String textInString, ArrayList<String> stopWordsAllLang, Context context) {

        LinkedHashMap<String, Integer> elementaryCountsMap = new LinkedHashMap<>();

        //The amount of all characters
        int allChars = textInString.length();
        //     Log.d(GET_TAG, "The amount of all characters: " + allChars);


        //without spaces
        String strWOSpaces = textInString.replaceAll("(\\s)", "");
        int woSpaces = strWOSpaces.length();
        //     Log.d(GET_TAG, " without spaces: " + strWOSpaces);


        //  The amount of characters  and digits (Without punctuation marks and spaces, with apostrophe in the middle)
        String strWOPunct = textInString.replaceAll("(?!'\\w+)(?!\\w+')[^\\w]+", "");
        int woPunct = strWOPunct.length();
        //      Log.d(GET_TAG, "Without punctuation marks and spaces: " + woPunct);


        //  The amount of characters  (without digits and punctuation marks - significant chars)
        String strWithoutDigitsAndPunctuation = strWOPunct.replaceAll("([0-9])+", "");
        int significantChars = strWithoutDigitsAndPunctuation.length();
        //   Log.d(GET_TAG, " significantChars: " + significantChars);
        //   Log.d(GET_TAG, " strWithoutDigitsAndPunctuation: " + strWithoutDigitsAndPunctuation);


        /* ************** WORDS: *************** */

        //simple words count:
        // String[] arrWords =  textInString.toLowerCase().split("(?!'\\w+)(?!\\w+')[^\\w]+|([0-9])+");
        int allWords = arrWords.length;
        //   Log.d(GET_TAG, " without spaces and punctuation: " + allWords);


        //unique words count(w/o dublicate):

        Set<String> set = new HashSet<>(Arrays.asList(arrWords));
        int uniqueWords = set.size();
        String[] strUniqueWords = set.toArray(new String[set.size()]);
        //    Log.d(GET_TAG, "unique words: " + uniqueWords);


        // stop words count:
        HashMap<String, Integer> currentStopWords = checkStopWords(stopWordsAllLang);

        sortedStopWordsList = sortingList(currentStopWords);
        /*for (Map.Entry stopWord:sortedStopWordsList) {
            Log.d(GET_TAG, "sortedStopWordsList: " + stopWord.getKey());
        }*/


        int stopWords = 0;
        if (countedWordsMap != null) {
            semanticCoreList = findSemanticCore(currentStopWords);
            Log.d(GET_TAG, "elementaryCounts is started | semanticCoreList" + semanticCoreList);
        }
        if (currentStopWords != null) {
            Set setOfKeys = currentStopWords.keySet();

            for (Object setOfKey : setOfKeys) {
                String key = (String) setOfKey;
                Integer value = currentStopWords.get(key);
                Log.d(GET_TAG, "stopWords: " + key + " - " + value);
                stopWords += value;
            }

            //  Log.d(GET_TAG, "stopWords: " + stopWords);
        }


        //dilution (water)

        double dilution = currentStopWords != null ? dilutionCalculate(currentStopWords, allWords) : 0;
        //  Log.d(GET_TAG, "dilution: " + dilution);

        // filling map with results:
        elementaryCountsMap.put(context.getString(R.string.txtSummary_all_symbols_number), allChars);
        elementaryCountsMap.put(context.getString(R.string.txtSum_without_spaces), woSpaces);
        elementaryCountsMap.put(context.getString(R.string.txtSum_without_punctuation_marks), woPunct);
        elementaryCountsMap.put(context.getString(R.string.txtSum_significant_characters_only), significantChars);
        elementaryCountsMap.put(context.getString(R.string.txtSum_number_of_words), allWords);
        elementaryCountsMap.put(context.getString(R.string.txtSum_unique_words), uniqueWords);
        elementaryCountsMap.put(context.getString(R.string.txtSum_number_of_stop_words), stopWords);
        elementaryCountsMap.put(context.getString(R.string.txtSum_percent_of_dilution), (int) dilution);

        return elementaryCountsMap;
    }



    private static double dilutionCalculate(HashMap<String, Integer> currentStopWords, int nAllWords) {
        int nStopWords = 0;
        for (int value : currentStopWords.values()) {
            nStopWords += value;
        }
        return nAllWords == 0 ? 0 : (nStopWords * 100) / nAllWords;
    }


    private static HashMap<String, Integer> checkStopWords(ArrayList<String> stopWordsAllLang) {

        HashMap<String, Integer> currentStopWords = new HashMap<>();

        for (int i = 0; i < arrWords.length; i++) {

            int stopWordIndex = Collections.binarySearch(stopWordsAllLang, arrWords[i]);

            if (stopWordIndex < 0) {                    // If we don't have this stopword
                stopWordIndex = -stopWordIndex - 1;     // we will find its a potential index in array
            }

            // Log.d("analyzer", "found stopWord by index: " + String.valueOf(stopWordsAllLang.get(stopWordIndex)));

            // we must to check if there are several consecutive stop-words in the one sentence

            String textWord = arrWords[i];
            String[] stopSentence;
            ArrayList<String> foundTextWords = new ArrayList<>();
            String foundStopWord;
            if (stopWordsAllLang.size() > stopWordIndex) {
                do {

                    String stopWord = stopWordsAllLang.get(stopWordIndex++);

                    stopSentence = stopWord.split(" ");

                    if (stopSentence.length > 1 && i + stopSentence.length <= arrWords.length) {

                        StringBuilder textSentence = new StringBuilder();

                        //building a sentence from text:
                        for (int n = 0; n < stopSentence.length - 1; n++) {
                            textSentence = textSentence.append(arrWords[i + n]).append(" ");
                        }
                        //last word without space:
                        textSentence = textSentence.append(arrWords[i + stopSentence.length - 1]);
                        textWord = textSentence.toString();
                        // Log.d(GET_TAG, "textSentence after appending: " + textWord);

                    }

                    if (textWord.equals(stopWord)) {
                        foundTextWords.add(textWord);
                        i += stopSentence.length - 1;
                    }

                }
                while (stopSentence[0].equalsIgnoreCase(arrWords[i]));
            }
            if (!foundTextWords.isEmpty()) {
                foundStopWord = foundTextWords.get(foundTextWords.size() - 1);
                fillStopWordsMap(foundStopWord, currentStopWords);
            }

        }
        return currentStopWords;
    }

    private static HashMap<String, Integer> fillStopWordsMap(String stopWord, HashMap<String, Integer> currentStopWords) {
        int value = 1;

        if (currentStopWords.containsKey(stopWord)) {
            value = currentStopWords.get(stopWord);
            value++;
        }
        currentStopWords.put(stopWord, value);//word,count
        return currentStopWords;
    }

    private static List<Map.Entry<String, Integer>> findSemanticCore(HashMap<String, Integer> currentStopWords) {
        //Log.d(GET_TAG, "findSemanticCore is started | countedWordsMap=" + countedWordsMap);

        HashMap<String, Integer> semanticCoreMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : countedWordsMap.entrySet()) {
            // Check if the current value is a key in the 2nd map
            if ((currentStopWords==null || !currentStopWords.containsKey(entry.getKey())) && entry.getValue() > 1) {

                semanticCoreMap.put(entry.getKey(), entry.getValue());
                //Log.d(GET_TAG, "semanticCoreMap: " + entry.getKey() + ": " + entry.getValue());

            }
        }

        return sortingList(semanticCoreMap);
    }


}