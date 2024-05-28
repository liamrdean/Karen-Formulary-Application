package com.karenformulary;

/*
 * This is the java file that models the SQLite database structure for the drugs
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// EN is short for English, KA is short for Karen
public class DB_DrugModel {
    // For when we have not manually assigned an id
    public static final int blankId = -1;

    // Unused variables related to images
    public static final char imageDelimiter = '$';
    public static final String imageDelimiterRegex = "\\$";

    /* Data for this DB_DrugModel */
    private int drugId;
    private String drugName;
    // The language specific data
    private HashMap<String, String> infoEN;
    private HashMap<String, String> infoKA;

    // Constructors
    public DB_DrugModel() {
    }

    public DB_DrugModel(String drugName) {
        this.drugId = blankId;
        this.drugName = drugName;
    }

    public DB_DrugModel(int drugId, String drugName, HashMap<String, String> infoEn, HashMap<String, String> infoKa) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.infoEN = infoEn;
        this.infoKA = infoKa;
    }

    // This is what gets called by the array list generator thing, so deal with it manually
    @Override
    public String toString() {
        // English
        return  "id = " + drugId + "\n" +
                "name = '" + drugName + "'\n" +
                "EN NAMES \n" + infoEN.toString() + "\n" +
                "KA NAMES \n" + infoKA.toString() + "\n";
    }

    // Getters
    public Set<String> getDataFields() {
        Set<String> mergedSet = new HashSet<>();
        mergedSet.addAll(infoEN.keySet());
        mergedSet.addAll(infoKA.keySet());

        return mergedSet;
    }

    // Returns data in a list (Pre split to deal with images)
    // The key MUST be a String in DB_Helper.languageIndependentHeaders
    public List<String> getData(String key, boolean isKaren) {
        if (key.equals(DB_Helper.COL_NAME_STRING)) {
            // They are just asking for the name of this drug, return it.
            List<String> output = new ArrayList<>();
            output.add(this.drugName);
            return output;
        }

        HashMap<String, String> map = (isKaren) ? infoKA : infoEN;
        HashMap<String, String> other = (isKaren) ? infoEN : infoKA;
        return getDataFromMap(key, map, other);
    }

    // Get the value from map with key. If map does not have key, try with other.
    // If neither have it returns null
    // returns null if data is empty
    // splits the data with imageDelimiter
    private List<String> getDataFromMap(String key, HashMap<String, String> map, HashMap<String, String> other) {
        String s;  // String representing data

        // Get the data
        if (!map.containsKey(key)) {
            if (other.containsKey(key)) {
                s = other.get(key);
            } else {  // Key is not in this model, and is invalid.
                Log.w("DrugModel", "Key " + key + " is not in either map!");
                return null;
            }
        } else {
             s = map.get(key);
        }

        if (s == null) {
            return null;
        }

        /* Process for images */
        String[] arr = s.split(imageDelimiterRegex);

        // Since having $$ means that there is a null or empty entry in the array, remove those
        // while adding the rest to the list
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i].trim();

            if (item != null && !item.isEmpty()) {
                /*
                 * Unused code for dealing with images
                // This is an image since text will always be text,image,text,image
                if (i % 2 == 1) {
                    item = imageDelimiter + item;
                }
                 */

                list.add(item);
            }
        }

        return list;
    }

}
