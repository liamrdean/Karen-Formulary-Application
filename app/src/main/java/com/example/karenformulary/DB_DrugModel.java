package com.example.karenformulary;

/*
 * This is the java file that models the SQLite database structure for the drugs
 */

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
    // The string used to indicate the beginning and end of a image path
    public static final String imageDelimiter = "$";

    private int drugId;
    private String drugName;

    /*
    private DrugInfo infoEN;
    private DrugInfo infoKA;
    */
    private HashMap<String, String> tempnameinfoEN;
    private HashMap<String, String> tempnameinfoKA;

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
        this.tempnameinfoEN = infoEn;
        this.tempnameinfoKA = infoKa;
    }

    /*
    public DB_DrugModel(int drugId, String drugName, DrugInfo infoEN, DrugInfo infoKA) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.infoEN = infoEN;
        this.infoKA = infoKA;
    }
    */

    // This is what gets called by the array list generator thing, so deal with it manually
    @Override
    public String toString() {
        // English
        return  "id = " + drugId + "\n" +
                "name = '" + drugName + "'\n" +
                "EN NAMES \n" + tempnameinfoEN.toString() + "\n" +
                "KA NAMES \n" + tempnameinfoKA.toString() + "\n";

//                this.getInfo(MainActivity.isKaren).toString();

        /* Legacy/default
        return "DB_DrugModel{" +
                "drugId=" + drugId +
                ", drugName='" + drugName + '\'' +
                ", description_English='" + description_English + '\'' +
                ", description_Karen='" + description_Karen + '\'' +
                '}';
         */
    }

    // Just toString but new lines between items
    /*
    public String toStringVerbose() {
        String out = "DB_DrugModel{\n" +
                "  drugId=" + drugId + '\n' +
                "  drugName='" + drugName + "\'\n"
                +"\nEN:\n"
                + infoEN.toString()
                +"\nKA:\n"
                + infoKA.toString();
        out += '}';
        return out;
    }
    */

    // Getters and setters

    /*
    public DrugInfo getInfo(boolean inKaren) {
        return (inKaren) ? infoKA : infoEN;
    }
    */


    public Set<String> getDataFields() {
        Set<String> mergedSet = new HashSet<>();
        mergedSet.addAll(tempnameinfoEN.keySet());
        mergedSet.addAll(tempnameinfoKA.keySet());

        return mergedSet;
    }

    // Returns data in a list (Pre split to deal with images)
    // The key must be a String in DB_Helper.languageIndependentHeaders
    public List<String> getData(String key) {
        if (key.equals(DB_Helper.COL_NAME_STRING)) {
            // They are just asking for the name of this drug, return it.
            List<String> output = new ArrayList<>();
            output.add(this.drugName);
            return output;
        }


        HashMap<String, String> map = (MainActivity.isKaren) ? tempnameinfoKA : tempnameinfoEN;
        HashMap<String, String> other = (MainActivity.isKaren) ? tempnameinfoEN : tempnameinfoKA;
        return getDataFromMap(key, map, other);
    }

    // Get the value from map with key. If map does not have key, try with other.
    // If neither have it returns null
    // returns null if data is empty
    // splits the data with imageDelimiter
    private List<String> getDataFromMap(String key, HashMap<String, String> map, HashMap<String, String> other) {
        String s;

        if (!map.containsKey(key)) {
            if (other.containsKey(key)) {
                s = other.get(key);
            } else {
                return null;
            }
        } else {
             s = map.get(key);
        }

        if (s == null) {
            return null;
        }

        String[] arr = s.split(imageDelimiter);

        return Arrays.asList(arr);
    }

}
