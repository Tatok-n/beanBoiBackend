package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class GrinderService {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";



    public List<String> createAlphabeticalList (char startLetter, char endLetter) {
        char[] alphabetArray = alphabet.toCharArray();
        int startIndex = alphabet.indexOf(startLetter);
        int endIndex = alphabet.indexOf(endLetter);
        int modifier = (startIndex > endIndex)  ? -1 : 1;

        List<String> settingArray = new ArrayList<>();
        int i = startIndex;

        while (i != (endIndex +modifier)) {
            settingArray.add(String.valueOf(alphabetArray[i]));
            i+=modifier;
        }
        return settingArray;
    }

    public List<String> createIntList (int startNumerator, int endNumerator) {
        List<String> settingArray = new ArrayList<>();
        int modifier = (startNumerator > endNumerator) ? -1 : 1;
        int i = startNumerator;
        while (i != (endNumerator +modifier)) {
            settingArray.add(String.valueOf(i));
            i+=modifier;
        }
        return settingArray;
    }

    public List<String> createFloatList (float startNumerator, float endNumerator, float precision) {
        List<String> settingArray = new ArrayList<>();
        float modifier = (startNumerator > endNumerator) ? -1 : 1;
        modifier*=precision;
        float i = startNumerator;

        while (i != (endNumerator + modifier)) {
            settingArray.add(String.valueOf(i));
            i+=modifier;
            i = (float) (Math.round(i*Math.pow(10,4))/Math.pow(10,4));
        }

        return settingArray;
    }

    public List<List<String>> getIndividualSettingList (List<Map<String, Object>> settingRequests) {
        List<List<String>> individualSettingLists = new ArrayList<>();

        for (Map<String, Object> settingRequest : settingRequests) {
            if (settingRequest.get("type").equals("F")) {
                float startNum = Float.parseFloat(settingRequest.get("startNumerator").toString());
                float endNum = Float.parseFloat(settingRequest.get("endNumerator").toString());
                float precision = Float.parseFloat(settingRequest.get("precision").toString());
                individualSettingLists.add(createFloatList(startNum, endNum, precision));
            } else if (settingRequest.get("type").equals("I")) {
                int startNum = Integer.parseInt(settingRequest.get("startNumerator").toString());
                int endNum = Integer.parseInt(settingRequest.get("endNumerator").toString());
                individualSettingLists.add(createIntList(startNum, endNum));
            } else if (settingRequest.get("type").equals("A")) {
                char startChar = settingRequest.get("startLetter").toString().charAt(0);
                char endChar = settingRequest.get("endLetter").toString().charAt(0);
                individualSettingLists.add(createAlphabeticalList(startChar, endChar));
            }
        }
        return individualSettingLists;
    }

    public List<String> getSettingList (List<Map<String, Object>> settingRequests) {
        List<List<String>> getIndividualSettingLists = getIndividualSettingList(settingRequests);
        List<String> settings = new ArrayList<>();
        int maxSize = 1;

        for (List<String> settingList : getIndividualSettingLists) maxSize *= settingList.size();
        if (getIndividualSettingLists.size() == 1) return settings;

        String currString = "";
        int[] indices = new int[getIndividualSettingLists.size()];
        List<String> prevList = getIndividualSettingLists.get(0);
        List<String> nextList = new ArrayList<>();
        List<String> currList = new ArrayList<>();
        List<String> permutations = new ArrayList<>();
        Arrays.fill(indices, 0);

        for (int i = 1; i < getIndividualSettingLists.size(); i++) {
            nextList = getIndividualSettingLists.get(i);
            currList.clear();
            for (String setting : prevList) {
                for (String nextSetting : nextList) {
                    currString = setting + nextSetting;
                    currList.add(currString);
                }
            }
            prevList = List.copyOf(currList);

        }
        return currList;
    }


}
