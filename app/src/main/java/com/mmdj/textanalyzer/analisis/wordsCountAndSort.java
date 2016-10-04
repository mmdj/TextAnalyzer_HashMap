package com.mmdj.textanalyzer.analisis;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WordsCountAndSort {
    //   private static MainActivity main = new MainActivity();
    private static String[] arrWords;


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
        fillMapAndCounting(map, words);
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
    private static void fillMapAndCounting(HashMap<String, Integer> map, String[] strArray) {
        for (String string : strArray) {
            int value = 1;
            if (map.containsKey(string)) {
                value = map.get(string);
                value++;
            }
            map.put(string, value);//word,count
        }
    }


    /**
     * counting the simple statistic
     *
     * @param textInString     all text in one string
     * @param stopWordsAllLang
     * @return map with  text statistic
     */

    public static LinkedHashMap<String, Integer> elementaryCounts(String textInString, ArrayList<String> stopWordsAllLang) {

        LinkedHashMap<String, Integer> elementaryCountsMap = new LinkedHashMap<>();

        //The amount of all characters
        int allChars = textInString.length();
        Log.d("analyzer", "The amount of all characters: " + allChars);


        //without spaces
        String strWOSpaces = textInString.replaceAll("(\\s)", "");
        int woSpaces = strWOSpaces.length();
        Log.d("analyzer", " without spaces: " + strWOSpaces);


        //  The amount of characters  and digits (Without punctuation marks and spaces, with apostrophe in the middle)
        String strWOPunct = textInString.replaceAll("(?!'\\w+)(?!\\w+')[^\\w]+", "");
        int woPunct = strWOPunct.length();
        Log.d("analyzer", "Without punctuation marks and spaces: " + woPunct);


        //  The amount of characters  (without digits and punctuation marks - significant chars)
        String strWithoutDigitsAndPunctuation = strWOPunct.replaceAll("([0-9])+", "");
        int significantChars = strWithoutDigitsAndPunctuation.length();
        Log.d("analyzer", " significantChars: " + significantChars);
        Log.d("analyzer", " significantChars: " + strWithoutDigitsAndPunctuation);


        /*************** WORDS: ****************/

        //simple words count:
        // String[] arrWords =  textInString.toLowerCase().split("(?!'\\w+)(?!\\w+')[^\\w]+|([0-9])+");
        int allWords = arrWords.length;
        Log.d("analyzer", " without spaces and punctuation: " + allWords);


        //unique words count(w/o dublicate):

        Set<String> set = new HashSet<>(Arrays.asList(arrWords));
        int uniqueWords = set.size();
        String[] strUniqueWords = set.toArray(new String[set.size()]);
        Log.d("analyzer", "unique words: " + uniqueWords);


        // stop words count:
        HashMap<String, Integer> currentStopWords = checkStopWords(stopWordsAllLang);
        int stopWords = 0;

        if (currentStopWords != null) {
            Set setOfKeys = currentStopWords.keySet();
            Iterator iterator = setOfKeys.iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Integer value = currentStopWords.get(key);
                Log.d("analyzer", "stopWords: " + key + " - " + value);
                stopWords += value;
            }

            Log.d("analyzer", "stopWords: " + stopWords);
        }


        //dilution (water)

        double dilution = currentStopWords != null ? dilutionCalculate(currentStopWords, allWords) : 0;
        Log.d("analyzer", "dilution: " + dilution);

        // filling map with results:
        elementaryCountsMap.put("allChars", allChars);
        elementaryCountsMap.put("woSpaces", woSpaces);
        elementaryCountsMap.put("woPunct", woPunct);
        elementaryCountsMap.put("significantChars", significantChars);
        elementaryCountsMap.put("allWords", allWords);
        elementaryCountsMap.put("uniqueWords", uniqueWords);
        elementaryCountsMap.put("stopWords", stopWords);
        elementaryCountsMap.put("dilution", (int) dilution);


        return elementaryCountsMap;
    }

    private static double dilutionCalculate(HashMap<String, Integer> currentStopWords, int nAllWords) {
        int nStopWords = 0;
        for (int value : currentStopWords.values()) {
            nStopWords += value;
        }
        return  nAllWords == 0 ? 0 : (nStopWords*100)/ nAllWords;
    }


    private static HashMap<String, Integer> checkStopWords(ArrayList<String> stopWordsAllLang) {

        HashMap<String, Integer> currentStopWords = new HashMap<>();

        for (int i = 0; i < arrWords.length; i++) {

            int stopWordIndex = Collections.binarySearch(stopWordsAllLang, arrWords[i]);

            if (stopWordIndex < 0) {                    // If we don't have this stopword
                stopWordIndex = -stopWordIndex - 1;     // we will find its a potential index in array
            }

            Log.d("analyzer", "found stopWord by index: " + String.valueOf(stopWordsAllLang.get(stopWordIndex)));

            // we must to check if there are several consecutive stop-words in the one sentence

            String textWord = arrWords[i];
            String[] stopSentence;
            ArrayList<String> foundTextWords = new ArrayList<>();
            String foundStopWord;

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

                    Log.d("analyzer", "textSentence after appending: " + textWord);
                }

                if (textWord.equals(stopWord)) {
                    foundTextWords.add(textWord);
                    i += stopSentence.length - 1;
                }

            } while (stopSentence[0].equalsIgnoreCase(arrWords[i]));

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

}
