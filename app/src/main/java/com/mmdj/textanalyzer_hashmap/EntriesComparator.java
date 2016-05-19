package com.mmdj.textanalyzer_hashmap;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Map;

public class EntriesComparator implements java.util.Comparator<Map.Entry<String, Integer>> {




    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        int res = Integer.compare(o2.getValue(), o1.getValue());

        return res == 0 ? o1.getKey().compareTo(o2.getKey()) : res;
    }
}
